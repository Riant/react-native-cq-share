package com.example.cukiy.cqshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class CQShareManager extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public CQShareManager(ReactApplicationContext reactContext) {
        super(reactContext);

        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "CQShare";
    }

    @ReactMethod
    public void sharePictureWithOptions(ReadableMap options) {


        String title = options.hasKey("title") ? options.getString("title") : null;
        ReadableArray remoteImages = options.hasKey("remoteImages") ? options.getArray("remoteImages") : null;
        ReadableArray localImages = options.hasKey("localImages") ? options.getArray("localImages") : null;
        String description = options.hasKey("description") ? options.getString("description") : null;

        if (title != null){

            // 分享文本
            Intent textIntent = new Intent(Intent.ACTION_SEND);
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_TEXT, title);
            getCurrentActivity().startActivity(Intent.createChooser(textIntent, "分享"));

        } else if (remoteImages != null || localImages != null) {
            // 分享图片
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
//          ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//          intent.setComponent(comp);
            if (description != null) {
                intent.putExtra("Kdescription", description);
            }

            ArrayList<Uri> images = new ArrayList<Uri> ();

            if (remoteImages != null) {
                ArrayList<String> urls = new ArrayList<String>();
                for (int i=0; i<remoteImages.size(); i++ ) {
                    urls.add(remoteImages.getString(i));
                }
                images = saveImageToAlbum(this.reactContext,urls);
            }

            if (localImages != null) {
                // –暂时不做操作
            }

            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,images);
	    getCurrentActivity().startActivity(Intent.createChooser(intent, "图片分享"));
        }
    }

    //保存文件到指定路径
    public static ArrayList<Uri> saveImageToAlbum(ReactApplicationContext reactContext, ArrayList<String> urls) {

        ArrayList<Uri> uris = new ArrayList<Uri>();

        for (String url : urls) {

            // 创建文件路径
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "shareImgs";
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = handleFileName(url) + ".jpg";
            File file = new File(appDir, fileName);

            if(file.exists()) {
                uris.add(Uri.fromFile(file));
            } else {
                // url转成bitmap
                URL myFileUrl = null;
                Bitmap bitmap = null;
                try {
                    myFileUrl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();

                    // 保存图片
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        //通过io流的方式来压缩保存图片
                        boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                        fos.flush();
                        fos.close();

                        //把文件插入到系统图库
                        //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

                        //保存图片后发送广播通知更新数据库
                        Uri uri = Uri.fromFile(file);
                        reactContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                        if (isSuccess) {
                            uris.add(uri);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return uris;
    }


    // 处理图片文件名
    public static String handleFileName(String url) {
        url = url.substring(url.lastIndexOf("/"),url.length() - 4);
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = url.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }




}
