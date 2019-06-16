var CQShare = require('react-native').NativeModules.CQShare;
import { Platform } from 'react-native';
export function share(options,callback) {
    
    
    if (Platform.OS === 'ios') {
        CQShare.sharePictureWithOptions(options,(isSuccess)=>{callback && callback(isSuccess)})
    }else{
        CQShare.sharePictureWithOptions(options)
    }
    
    
}
