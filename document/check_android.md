### Android 配置
jmrtc-react-native 需要依赖 jmessage-react-native 以及 jcore-react-native才能正常使用

* 检查 app 下的 build.gradle 配置：
```
dependencies {
    compile project(':jmessage-react-plugin')  // 添加 jmessage 依赖
    compile project(':jmrtc-react-native')    //添加 jmrtc 依赖
    compile project(':jcore-react-native')    // 添加 jcore 依赖
    compile fileTree(dir: "libs", include: ["*.jar"])
    compile "com.android.support:appcompat-v7:23.0.1"
    compile "com.facebook.react:react-native:+"  // From node_modules

}
```
* 检查 android 项目下的 settings.gradle 配置有没有包含以下内容：
```
include ':jmessage-react-plugin'
project(':jmessage-react-plugin').projectDir = new File(rootProject.projectDir, '../node_modules/jmessage-react-plugin/android')
include ':jcore-react-native'
project(':jcore-react-native').projectDir = new File(rootProject.projectDir, '../node_modules/jcore-react-native/android')
include ':jmrtc-react-native'
project(':jmrtc-react-native').projectDir = new File(rootProject.projectDir, '../node_modules/jmrtc-react-native/android')
```
* 现在重新 sync 一下项目，应该能看到 jmessage-react-native ,jmrtc-react-native 以及 jcore-react-native 作为 android Library 项目导进来了。

