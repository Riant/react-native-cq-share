
感谢 [cukiy](https://www.npmjs.com/~cukiy) 开源分享，原库 https://www.npmjs.com/package/react-native-cq-share 貌似不再维护更新，也没有提供 Github 仓库，但我个人在使用过程中需要修改部分内容，所以算是 fork 一个版本到这里维护。

### Npm Install

```shell
$ npm install --save git+ssh://git@github.com:Riant/react-native-cq-share.git
```

### Automatically Link

```shell
$ react-native link react-native-cq-share
```


#### Use
```
import React, { Component } from 'react';
import {
    Platform,
    StyleSheet,
    Text,
    View,
    TouchableOpacity
} from 'react-native';

import { share } from 'react-native-cq-share'

export default class App extends Component<Props> {
render() {
    return (
        <View style={styles.container}>
            <TouchableOpacity style={{height:60,width:100,backgroundColor:'red'}}
                              onPress={()=>share(options,callbake)}>
            </TouchableOpacity>
        </View>
        );
    }
}
```
#### Parameters

### iOS
```
share方法有以下参数:
options: 分享的数据. 包含四个字段.
        title(String. 标题)
        url(String. 链接)
        remoteImages(Array. 远程图片url数组)
        localImages(Array. 本地图片路径数组)
callbake:  分享完成的回调. 返回true(分享成功)或false(分享失败)
```

### Android
```
share方法有以下参数:
options: 分享的数据. 包含四个字段.
        title(String.纯文本分享)
        remoteImages(Array. 远程图片url数组)
        localImages(Array. 本地图片路径数组)
        description(String.图片分享默认描述文本)    
```
