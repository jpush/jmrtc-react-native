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

#ifndef JMRTCCommonDefine_h
#define JMRTCCommonDefine_h


typedef void (^JMRTCCompletionHandler)(id resultObject, NSError *error);

/*!
 媒体类型
 */
typedef NS_ENUM(NSInteger, JMRTCMediaType) {
    /// 音频
    JMRTCMediaAudio = 1,
    /// 视频
    JMRTCMediaVideo = 2,
};

#pragma mark - Call

/*!
 错误码
 */
typedef NS_ENUM(NSInteger, JMRTCErrorCode) {
    /// 网络错误
    JMRTCNetworkError          = 871001,
    /// 无效操作
    JMRTCOperationUnavailable  = 871002,
    /// 参数错误
    JMRTCInvalidParam          = 871003,
    /// IM 账户未登录
    JMRTCUserNotLogin          = 871004,
    /// IM 账户登录状态异常
    JMRTCUserLoginStatusError  = 871005,
    /// 音视频引擎未初始化
    JMRTCEngineUnInitialize    = 871006,
    /// 初始化音视频引擎失败
    JMRTCInitializeEngineFail  = 871007,
    /// 用户已经在通话中
    JMRTCUserAlreadyInCall     = 871008,
};

/*!
 通话结束原因
 */
typedef NS_ENUM(NSInteger, JMRTCDisconnectReason) {
    /// 拒绝
    JMRTCDisconnectReasonRefuse         = 872001,
    /// 挂断
    JMRTCDisconnectReasonHangup         = 872002,
    /// 超时未响应
    JMRTCDisconnectReasonCancel         = 872003,
    /// 忙碌
    JMRTCDisconnectReasonBusy           = 872004,
    /// 网络出错
    JMRTCDisconnectReasonNetworkError   = 872005,
};

/*!
 视频输出的编码参数
 */
typedef NS_ENUM(NSInteger, JMRTCVideoProfile) {
    /*!
     320x240, 15fps, 200kbps
     */
    JMRTC_VIDEO_PROFILE_240P = 20,
    /*!
     640x360, 15fps, 400kbps
     */
    JMRTC_VIDEO_PROFILE_360P = 30,
    /*!
     640x480, 15fps, 500kbps
     */
    JMRTC_VIDEO_PROFILE_480P = 40,
    /*!
     1280x720, 15fps, 1000kbps
     */
    JMRTC_VIDEO_PROFILE_720P = 50,
    /*!
     默认的视频参数
     */
    JMRTC_VIDEO_PROFILE_DEFAULT = JMRTC_VIDEO_PROFILE_360P,
};



#endif /* JMRTCCommonDefine_h */
