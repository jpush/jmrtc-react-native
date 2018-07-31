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

#import "JMRTCDelegate.h"

@class JMRTCSession;
@interface JMRTCClient : NSObject

/*!
 * @abstract 初始化音视频引擎。
 *
 * @param handler  初始话回调，error = nil 表示初始化成功
 *
 * @discussion 在使用其他接口之前，必须先调用此接口初始化引擎。
 */
+ (void)initializeEngine:(JMRTCCompletionHandler _Nullable)handler;

/*!
 * @abstract 释放音视频引擎
 *
 * @discussion 挂断后建议释放引擎，释放之后如果需要再次使用音视频服务，需要重新调用初始化接口来重新初始化音视频引擎。
 */
+ (void)releaseEngine;

/*!
 * @abstract 添加音视频监听
 *
 * @param delegate 需要监听的 Delegate Protocol
 *
 * @discussion 建议在 didFinishLaunchingWithOptions 方法中添加全局监听，避免遗漏监听
 */
+ (void)addDelegate:(id<JMRTCDelegate>_Nonnull)delegate;

/*!
 * @abstract 发起一个通话
 *
 * @param users            邀请的用户列表
 * @param type             发起的通话媒体类型
 * @param handler          回调，error = nil 表示成功，result 为 JMRTCSession 对象
 *
 * @discussion 在此接口成功之后，可以设置会话中用户展示视图、摄像头等属性;
 * 如果被邀请人中包含未登录用户只会向已登录用户发起邀请。
 */
+ (void)startCallUsers:(NSArray <__kindof JMSGUser *>*_Nonnull)users
             mediaType:(JMRTCMediaType)type
               handler:(JMRTCCompletionHandler _Nullable)handler;

/*!
 * @abstract 设置视频输出的编码属性
 *
 * @param profile 视频编码属性,默认是 JMRTC_VIDEO_PROFILE_360P
 *
 * ### 建议在发起通话之前，调用此接口设置，避免在通话过程中调用此接口
 *
 * @discussion 注意：多人视频通话期间设置此接口可能会失效
 */
+ (void)setVideoProfile:(JMRTCVideoProfile)profile;

/*!
 * @abstract 获取当前设置的视频编码属性
 */
+ (JMRTCVideoProfile)getVideoProfile;

/*!
 * @abstract 当前的通话会话实体
 */
+ (JMRTCSession *_Nullable)currentCallSession;

@end
