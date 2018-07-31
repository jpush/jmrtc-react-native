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

#import <Foundation/Foundation.h>

@class JMRTCSession,JMSGUser;

@protocol JMRTCDelegate <NSObject>

@optional;
/*!
 * @abstract 通话邀请已发出
 *
 * @param callSession 通话实体对象
 *
 * @discussion 在成功调用 [JMRTCClient startCallUsers:mediaType:handler:] 接口之后，会触发这个回调
 */
- (void)onCallOutgoing:(JMRTCSession *)callSession;

/*!
 * @abstract 收到通话邀请
 *
 * @param callSession 通话实体对象
 *
 * @discussion 被邀请者收到通话邀请，会触发此回调
 */
- (void)onCallReceiveInvite:(JMRTCSession *)callSession;

/*!
 * @abstract 通话正在连接
 *
 * @param callSession 通话实体对象
 *
 * @discussion 被邀请方调用 [JMRTCSession accept:] 接口之后，会触发此回调
 */
- (void)onCallConnecting:(JMRTCSession *)callSession;

/*!
 * @abstract 通话连接已建立
 *
 * @param callSession 通话实体对象
 *
 * @discussion 当被邀请方有任意一方成功调用 [JMRTCSession accept:] 接受邀请后，邀请方和接受方都会触发此回调通知上层通信连接已建立
 */
- (void)onCallConnected:(JMRTCSession *)callSession;

/*!
 * @abstract 有用户加入通话
 *
 * @param joinUser  加入的用户的用户信息
 */
- (void)onCallMemberJoin:(JMSGUser *)joinUser;

/*!
 * @abstract 通话断开
 *
 * @param callSession 通话实体对象
 * @param reason      断开原因
 *
 * @discussion 连接主动断开或异常断开时会触发此回调，断开原因请查看的 JMRTCDisconnectReason
 */
- (void)onCallDisconnect:(JMRTCSession *)callSession disconnectReason:(JMRTCDisconnectReason)reason;

/*!
 * @abstract 有用户离开
 *
 * @param leaveUser   退出通话的用户的用户信息
 * @param reason      退出原因
 *
 * @discussion 不管是正在被邀请的用户离开，还是已经加入通话的用户离开，都会触发这个回调
 */
- (void)onCallMemberLeave:(JMSGUser *)leaveUser reason:(JMRTCDisconnectReason)reason;

/*!
 * @abstract 通话过程中，有其他用户被邀请
 *
 * @param fromUser      邀请发起方用户信息
 * @param invitedUsers  被邀请方用户信息集合
 */
- (void)onCallOtherUserInvited:(NSArray <__kindof JMSGUser *>*)invitedUsers fromUser:(JMSGUser *)fromUser;

/*!
 * @abstract 通话过程中发生错误
 *
 * @param error 错误信息
 *
 * @discussion 错误具体情况请查看 error 的错误码和描述信息
 */
- (void)onCallError:(NSError *)error;

/*!
 * @abstract 远端用户开启/关闭视频流
 *
 * @param enabled  开启/关闭
 * @param user     远端用户
 */
- (void)onCallUserVideoStreamEnabled:(BOOL)enabled byUser:(JMSGUser *)user;

@end
