# JMRTC React Native Plugin

![](./document/rctvideo.png)

## 安装

```shell
npm install jmrtc-react-native --save
react-native link jmrtc-react-native
```

## 配置

### iOS 手动配置部分

> **注意**：需要先确保自己工程中 `Info.plist` 包含 camera , Microphone  权限。

- 设置后台模式，之后需要点开后台音频功能：在 TARGETS -> Capabilities -> Background Modes 里选择 Audio, AirPlay, and Picture in Picture。


- 选择主工程 target -> Build Settings -> Enable Bitcode 设置为 No。

### Android
* [检查添加的配置项](document/check_android.md)

* 添加 JMRTCReactPackage

  在 MainApplication 中加上 JMRTCReactPackage ，JMRTCReactPackage 有两个参数：是否弹出 toast，是否打印插件log

  ```
  import io.jmrtc.android.JMRTCReactPackage;;   // <--   导入 JMRTCReactPackage

  public class MainApplication extends Application implements ReactApplication {

     // 设置为 true 将不会弹出 toast
     private boolean SHUTDOWN_TOAST = false;
     // 设置为 true 将不会打印 log
     private boolean SHUTDOWN_LOG = false;

      private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

          ...

          @Override
          protected List<ReactPackage> getPackages() {
              return Arrays.<ReactPackage>asList(
                      new MainReactPackage(),
                     new JMRTCReactPackage(SHUTDOWN_TOAST, SHUTDOWN_LOG)   //  <-- 添加 JMRTCReactPackage
              );
          }
      };

  ...
  }
  ```

## Usage

```javascript
import {  JMRTCViewController,JMRTCView} from 'jmrtc-react-native'

... your component code 

JMRTCViewController.initEngine((success) => {
  
}, (error) => {
  
})

render() {
  return <JMRTCView username='your user name or target user name' />
}
```



## APIs

[API](document/api.md)
