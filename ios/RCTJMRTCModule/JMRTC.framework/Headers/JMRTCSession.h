/*
 *    | |    | |  \ \  / /  | |    | |   / _______|
 *    | |____| |   \ \/ /   | |____| |  / /
 *    | |____| |    \  /    | |____| |  | |   _____
 *    | |    | |    /  \    | |    | |  | |  |____ |
 *    | |    | |   / /\ \   | |    | |  \ \______| |
 *    | |    | |  /_/  \_\  | |    | |   \_________|
 *
 * Copyright © 2017年 Shenzhen HXHG. All rights reserved.
 */

#import <UIKit/UIKit.h>

@class JMSGUser;


/*!
 * @abstract 通话实体
 */
@interface JMRTCSession : NSObject

/// 不支持使用的初始化方法
- (instancetype _Nonnull)init NS_UNAVAILABLE;

/*!
 * @abstract当前通话的频道id
 */
@property(nonatomic, assign, readonly) SInt64 channelId;

/*!
 * @abstract当前用户使用的媒体类型
 */
@property(nonatomic, assign, readonly) JMRTCMediaType mediaType;

/*!
 * @abstract 邀请当前用户加入通话的邀请者
 */
@property(nonatomic, strong, readonly) JMSGUser *_Nullable inviter;

/*!
 * @abstract 正在邀请中的用户
 */
@property(nonatomic, strong, readonly) NSArray <__kindof JMSGUser *>*_Nullable invitingMembers;

/*!
 * @abstract 已经加入通话的用户
 */
@property(nonatomic, strong, readonly) NSArray <__kindof JMSGUser *>*_Nullable joinedMembers;

/*!
 * @abstract 连接建立时间
 */
@property(nonatomic, assign, readonly) long long startTime;

/*!
 * @abstract 接听来电
 *
 * ### 此接口只能在收到通话邀请回调 [JMRTCDelegate onCallReceiveInvite:] 之后才能调用；
 *
 * ### 调用成功后，双方都会触发 [JMRTCDelegate onCallConnected:] 回调，通知上层通话连接已建立；
 *
 * ### 并且 SDK 会触发 [JMRTCDelegate onCallMemberJoin:] 通知当前已经在通话频道内的用户有新用户加入。
 */
- (void)accept:(JMRTCCompletionHandler _Nullable)handler;

/*!
 * @abstract 挂断通话
 *
 * ### 调用成功后，挂断方会触发 [JMRTCDelegate onCallDisconnect::disconnectReason:]通知上层连接断开，通话结束；
 *
 * ### 其他用户会触发 [JMRTCDelegate onCallMemberLeave:reason:]通知上层有用户离开.
 *
 * @discussion 注意：SDK 一般情况不会主动调用 hangup，挂断通话操作由上层决定。此接口可以在邀请阶段以及通话阶段由任意通话中用户发起
 */
- (void)hangup:(JMRTCCompletionHandler _Nullable)handler;

/*!
 * @abstract 拒绝通话邀请
 *
 * ### 此接口只能在收到通话邀请回调 [JMRTCDelegate onCallReceiveInvite:] 之后才能调用；
 *
 * ### 调用成功后，拒绝方会触发 [JMRTCDelegate onCallDisconnect:disconnectReason:]通知上层连接断开，通话结束；
 *
 * ### 其他用户会触发 [JMRTCDelegate onCallMemberLeave:reason:]通知上层有用户离开.
 *
 * @discussion 此接口可以在邀请阶段以及通话阶段由任意通话中用户发起
 */
- (void)refuse:(JMRTCCompletionHandler _Nullable)handler;

/*!
 * @abstract 邀请用户加入通话
 *
 * ### 被邀请方收到邀请时，会触发 [JMRTCDelegate onCallReceiveInvite:] 回调；
 *
 * ### 通话中的其他用户，会触发 [JMRTCDelegate onCallOtherUserInvited:fromUser:] 回调
 *
 * @param users      用户列表
 * @param handler    回调,error=nil 时表示操作成功
 *
 * @discussion 在通话已经建立的前提下，再邀请其他用户加入当前通话.
 * 如果被邀请人中包含未登录用户只会向已登录用户发起邀请.
 */
- (void)inviteUsers:(NSArray <__kindof JMSGUser *>*_Nonnull )users
            handler:(JMRTCCompletionHandler _Nullable)handler;

/*!
 * @abstract 设置用户视频展示 View
 *
 * @param view   视频的View
 * @param user    用户（自己或他人）
 *
 * @discussion 在发起通话邀请之后，设置视频的展示 view
 */
- (void)setVideoView:(UIView *_Nonnull)view user:(JMSGUser *_Nonnull)user;

/*!
 * @abstract 静音状态
 */
@property(nonatomic, assign, readonly) BOOL isMuted;

/*!
 * @abstract 设置静音状态
 *
 * @param muted 是否静音
 */
- (BOOL)setMuted:(BOOL)muted;

/*!
 * @abstract扬声器状态，是否开启扬声器
 *
 * @discussion 音频通话的默认值为NO，视频通话的默认值为YES。
 */
@property(nonatomic, assign, readonly) BOOL isSpeakerphoneEnabled;

/*!
 * @abstract 设置扬声器状态
 *
 * @param enabled  是否开启扬声器，音频通话默:NO，视频通话默认:YES
 *
 * @discussion 只在视频或语音通话连接建立之后调用有效
 */
- (BOOL)setSpeakerEnabled:(BOOL)enabled;

/*!
 * @abstract 是否开启视频流输出
 */
@property(nonatomic, assign, readonly) BOOL isVideoStreamEnabled;

/*!
 * @abstract 设置视频流状态
 *
 * @param enabled  开启/关闭，音频通话默:NO，视频通话默认:YES
 *
 * @discussion 该方法不影响本地视频流获取，没有禁用摄像头，只是暂停发送本地视频流，只在视频通话连接建立之后调用有效。
 *
 * 通话中的其他用户，会触发 [JMRTCDelegate onCallUserVideoStreamEnabled:byUser:] 回调
 */
- (BOOL)setVideoStreamEnabled:(BOOL)enabled;

/*!
 * @abstract 切换前后摄像头
 */
- (BOOL)switchCameraMode;

@end
