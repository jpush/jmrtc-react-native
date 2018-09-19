

[Common API](#common-api)
- [initEngine](#initengine)
- [releaseEngine](#releaseengine)
- [startCallUsers](#startcallusers)
- [setVideoView](#setvideoview)
- [setvideoprofile](#setvideoprofile)
- [getVideoProfile](#getvideoprofile)
- [switchCamera](#switchcamera)
- [accept](#accept)
- [hangup](#hangup)
- [refuse](#refuse)
- [inviteUsers](#inviteusers)
- [isMuted](#ismuted)
- [setIsMuted](#setismuted)
- [isSpeakerphoneEnabled](#isspeakerphoneenabled)
- [setIsSpeakerphoneEnabled](#setIsspeakerphoneenabled)
- [isVideoStreamEnabled](#isvideostreamenabled)
- [setIsVideoStreamEnabled](#setIsvideostreamenabled)

[Event](#event)

- [CallOutgoing](#calloutgoing)
- [CallReceiveInvite](#callreceiveinvite)
- [CallConnecting](#callconnecting)
- [CallConnected](#callconnected)
- [CallMemberJoin](#callmemberjoin)
- [CallDisconnect](#calldisconnect)
- [CallMemberLeave](#callmemberleave)
- [CallOtherUserInvited](#callotheruserinvited)
- [CallError](#callerror)
- [CallUserVideoStreamEnabled](#calluservideostreamenabled)


[Model](model)

- [Session](session)

## Common API

### initEngine

初始化音视频引擎，需要初始化成功回调后只能执行音视频相关操作。

```javascript
JMRTCViewController.initEngine(() => {
  // 音视频引擎初始化成功，想在可以做音视频相关操作。
}, (error) => {
  
})
```

### releaseEngine

释放音视频引擎.

```
JMRTCViewController.releaseEngine()
```

### startCallUsers

发起一个通话(音频或者视频)。

```javascript
const params = {
	usernames: [string],  // 要呼叫的用户名
  	type = 'video' | 'voice' // 呼叫类型，'video' 或 'voice'
}

JMRTCViewController.startCallUsers(params, () => {
  
}, (err) => {
  
})
```

### setVideoView

视频聊天绑定影像视图输出 `JMRTCView` 有 `username` 属性 需要匹配，详情见示例。

```javascript
const params = {
  username: string // 注意这个 username 要和 JMRTCView 的 props.username 匹配，否则会失效
}
JMRTCViewController.setVideoView(params)
```

### setvideoprofile

设置视频输出的编码属性

```javascript
const params = {
  profile: string //{profile: '240p' | '360p' | '480p' | '720p'} 视频编码属性,不调用这个接口默认是 '360p'
}

JMRTCViewController.setVideoProfile(params)
```

### getVideoProfile

设置视频输出的编码属性

```javascript
JMRTCViewController.getVideoProfile((res) => {
  // res =  = {profile: '240p' | '360p' | '480p' | '720p'}
})
```

### switchCamera

切换摄像头。

```javascript
JMRTCViewController.switchCamera()
```

### accept

接收通话邀请。

```javascript
JMRTCViewController.accept(() => {
	// success
}, () => {
	// fail
})
```

### hangup

挂断当前通话

```javascript
JMRTCViewController.hangup(() => {
	// success
}, () => {
	// fail
})
```

### refuse

拒绝通话邀请

```javascript
JMRTCViewController.refuse(() => {
	// success
}, () => {
	// fail
})
```

### inviteUsers

邀请其他用户加入通话。

```javascript
const params = { usernames: [String] } 
JMRTCViewController.inviteUsers(params, () => {
  
}, () => {
  
})
```

### isMuted(ios)

(ios only) 获取静音状态。

```javascript
JMRTCViewController.isMuted((boolean) => { })
```

### setIsMuted

设置静音状态。

```javascript
JMRTCViewController.setIsMuted({muted: true})
```

### isSpeakerphoneEnabled

(ios only) 获取扬声器状态。

```javascript
JMRTCViewController.isSpeakerphoneEnabled((boolean) => { })
```

### setIsSpeakerphoneEnabled

设置扬声器状态

```javascript
JMRTCViewController.setIsSpeakerphoneEnabled({speakerphoneEnabled: true})
```

### isVideoStreamEnabled

(ios only) 获取视频流状态。

```javascript
JMRTCViewController.isVideoStreamEnabled((boolean) => { })
```

### setIsVideoStreamEnabled

设置视频流状态。

```javascript
JMRTCViewController.setIsVideoStreamEnabled({videoStreamEnabled: true})
```



## Event

### CallOutgoing

通话邀请已发出

```javascript
const handler = (session) => {
	
}

//添加监听
JMRTCViewController.addCallOutgoingListener(handler)
// 移除监听
JMRTCViewController.removeListener(handler)
```



### CallReceiveInvite

收到通话邀请

```javascript
const handler = (session) => {
	
}

//添加监听
JMRTCViewController.addCallReceiveInviteListener(handler)
// 移除监听
JMRTCViewController.removeListener(handler)
```

### CallConnecting

通话正在连接

```javascript
const handler = (session) => {
	
}

//添加监听
JMRTCViewController.addCallConnectingListener(handler)
// 移除监听
JMRTCViewController.removeListener(handler)
```
### CallConnected

通话连接已建立

```javascript
const handler = (session) => {
	
}

//添加监听
JMRTCViewController.addCallConnectedListener(handler)
// 移除监听
JMRTCViewController.removeListener(handler)
```
### CallMemberJoin

有用户加入通话

```javascript
const handler = (userInfo) => {
	
}

//添加监听
JMRTCViewController.addCallMemberJoinListener(handler)
// 移除监听
JMRTCViewController.removeListener(handler)
```
### CallDisconnect

通话断开

```javascript
const handler = (res) => {
	// res =  {session: Session, reason: 'refuse' | 'hangup' | 'cancel' | 'busy' | 'networkError'}
}

//添加监听
JMRTCViewController.addCallDisconnectListener(handler)
// 移除监听
JMRTCViewController.removeListener(handler)
```
### CallMemberLeave

有用户离开

```javascript
const handler = (res) => {
	// res = {user: userInfo, reason: 'refuse' | 'hangup' | 'cancel' | 'busy' | 'networkError'}
}

//添加监听
JMRTCViewController.addCallMemberLeaveListener(handler)
// 移除监听
JMRTCViewController.removeListener(handler)
```
### CallOtherUserInvited

通话过程中，有其他用户被邀请

```javascript
const handler = (res) => {
	// res = {invitedUsers: userInfo, fromUser: userInfo}
}

//添加监听
JMRTCViewController.addCallOtherUserInvitedListener(handler)
// 移除监听
JMRTCViewController.removeCallOutgoingListener(handler)
```
### CallError

通话过程中发生错误

```javascript
const handler = (session) => {
	
}

//添加监听
JMRTCViewController.addCallErrorListener(handler)
// 移除监听
JMRTCViewController.removeListener(handler)
```
### CallUserVideoStreamEnabled

远端用户开启/关闭视频流

```javascript
const handler = (res) => {
	// res = {enabled: boolean, user:userInfo }
}

//添加监听
JMRTCViewController.addCallUserVideoStreamEnabledListener(handler)
// 移除监听
JMRTCViewController.removeListener(handler)
```
## Model

### Session

```json
userModel = {
	type: 'user',
	username: string,           // 用户名
	appKey: string,             // 用户所属应用的 appKey，可与 username 共同作为用户的唯一标识
	nickname: string,           // 昵称
	gender: string,             // 'male' / 'female' / 'unknown'
	avatarThumbPath: string,    // 头像的缩略图地址
	birthday: number,           // 日期的毫秒数
	region: string,             // 地区
	signature: string,          // 个性签名
	address: string,            // 具体地址
	noteName: string,           // 备注名
	noteText: string,           // 备注信息
	isNoDisturb: boolean,       // 是否免打扰
	isInBlackList: boolean,     // 是否在黑名单中
	isFriend:boolean            // 是否为好友
}
session = {
 	channelId: string,
	mediaType: "video" | "voice",
	inviter: userModel,
	invitingMembers: [userModel],
	joinedMembers: [userModel],
	startTime: number
}
```

