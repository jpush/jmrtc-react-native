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
