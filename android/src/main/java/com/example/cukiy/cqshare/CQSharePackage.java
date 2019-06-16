package com.example.cukiy.cqshare;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by cukiy on 2018/3/29.
 */
public class CQSharePackage implements ReactPackage {// 必须重写该方法
        @Override
        public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
            List<NativeModule> modules = new ArrayList<>();
            modules.add(new CQShareManager(reactContext));
            return modules;
        }

        // 这个方法@Override注解会报错  所以就注释了
        //  @Override
        public List<Class<? extends JavaScriptModule>> createJSModules() {
            return Collections.emptyList();
        }

        //必须重写该方法. 将我们创建的TextView注册到视图管理列表中
        @Override
        public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
            return Collections.emptyList();
        }
}
