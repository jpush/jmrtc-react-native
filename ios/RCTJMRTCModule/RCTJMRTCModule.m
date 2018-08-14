//
//  RCTJMRTCModule.m
//  RCTJMRTCModule
//
//  Created by oshumini on 2018/7/16.
//  Copyright © 2018年 HXHG. All rights reserved.
//

#import "RCTJMRTCModule.h"

@implementation NSError (JMessage)
- (NSDictionary *)errorToDictionary {
    return @{@"code": @(self.code), @"description": [self description]};
}
@end

@implementation JMRTCSession (JMRTCRN)
- (NSDictionary *)sessionToDictionary {

    NSMutableDictionary *dic = @{}.mutableCopy;
    dic[@"channelId"] = @(self.channelId);
    switch (self.mediaType) {
        case JMRTCMediaVideo:
            dic[@"mediaType"] = @"video";
            break;
        case JMRTCMediaAudio:
            dic[@"mediaType"] = @"voice";
            break;
    }
    
    dic[@"inviter"] = [self userToDic:self.inviter];
    dic[@"invitingMembers"] = @[].mutableCopy;
    dic[@"joinedMembers"] = @[].mutableCopy;
    for (JMSGUser *user in self.invitingMembers) {
        [dic[@"invitingMembers"] addObject:[self userToDic:user]];
    }
    
    for (JMSGUser *user in self.joinedMembers) {
        [dic[@"joinedMembers"] addObject:[self userToDic:user]];
    }
    dic[@"startTime"] = @(self.startTime);
    return dic;
}

- (NSDictionary *)userToDic:(JMSGUser *)user {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    dict[@"type"] = @"user";
    dict[@"username"] = user.username;
    dict[@"nickname"] = user.nickname;
    dict[@"birthday"] = user.birthday;
    dict[@"region"] = user.region;
    dict[@"signature"] = user.signature;
    dict[@"address"] = [user address];
    dict[@"noteName"] = user.noteName;
    dict[@"noteText"] = user.noteText;
    dict[@"appKey"] = user.appKey;
    dict[@"isNoDisturb"] = @(user.isNoDisturb);
    dict[@"isInBlackList"] = @(user.isInBlacklist);
    dict[@"isFriend"] = @(user.isFriend);
    dict[@"extras"] = user.extras;
    
    if([[NSFileManager defaultManager] fileExistsAtPath: [user thumbAvatarLocalPath] ?: @""]){
        dict[@"avatarThumbPath"] = [user thumbAvatarLocalPath];
    } else {
        dict[@"avatarThumbPath"] = @"";
    }
    
    switch (user.gender) {
        case kJMSGUserGenderUnknown:
            dict[@"gender"] = @"unknown";
            break;
        case kJMSGUserGenderFemale:
            dict[@"gender"] = @"female";
            break;
        case kJMSGUserGenderMale:
            dict[@"gender"] = @"male";
            break;
        default:
            break;
    }
    return dict.copy;
}
@end

@interface RCTJMRTCModule() <JMRTCDelegate> {
}

@end

@implementation RCTJMRTCModule
RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

+ (id)allocWithZone:(NSZone *)zone {
    static RCTJMRTCModule *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [super allocWithZone:zone];
        [JMRTCClient addDelegate: sharedInstance];
    });
    return sharedInstance;
}

- (id)init {
    self = [super init];
    return self;
}

RCT_EXPORT_METHOD(initEngine:(RCTResponseSenderBlock)successCallback
                        fail:(RCTResponseSenderBlock)failCallback) {
    [JMRTCClient initializeEngine:^(id resultObject, NSError *error) {
        if (error) {
            failCallback(@[[error errorToDictionary]]);
            return;
        }
        
        successCallback(@[@{@"fds": @"fsfsf"}]);
    }];
}

RCT_EXPORT_METHOD(releaseEngine) {
    [JMRTCClient releaseEngine];
}

RCT_EXPORT_METHOD(startCallUsers:(NSDictionary *)params
                         success:(RCTResponseSenderBlock)successCallback
                            fail:(RCTResponseSenderBlock)failCallback) {
//    {users: [String], type = 'video' | 'voice'}

    if ((params[@"usernames"] == nil) ||
        (params[@"type"] == nil)) {
        failCallback(@[[self getParamError]]);
        return;
    }
    
    NSArray *usernames = params[@"usernames"];
    [JMSGUser userInfoArrayWithUsernameArray:usernames completionHandler:^(id resultObject, NSError *error) {
        if (error) {
            failCallback(@[[error errorToDictionary]]);
            return;
        }
        
        NSArray *users = resultObject;
        [JMRTCClient startCallUsers:users
                          mediaType:[self stringToMeiaType:params[@"type"]]
                            handler:^(id resultObject, NSError *error) {
                                if (error) {
                                    failCallback(@[[error errorToDictionary]]);
                                    return;
                                }
                                
                                // TODO: do something
                                JMRTCSession *session = resultObject;
                                successCallback(@[[session sessionToDictionary]]);
        }];
    }];
}

RCT_EXPORT_METHOD(setVideoView:(NSDictionary *)params) {
    if (!params[@"username"]) {
        NSLog(@"parames error username field do not exits");
        return;
    }
    [[NSNotificationCenter defaultCenter] postNotificationName:kJMRTCSetVideoView object: params];
}

RCT_EXPORT_METHOD(setVideoProfile:(NSDictionary *)params) {
    if (!params[@"profile"]) {
        NSLog(@"parames error profile field do not exits");
        return;
    }
    
    JMRTCVideoProfile profile = [self stringToVideoProfile:params[@"profile"]];
    [JMRTCClient setVideoProfile: profile];
}

RCT_EXPORT_METHOD(getVideoProfile:(RCTResponseSenderBlock)successCallback) {
    JMRTCVideoProfile profile = [JMRTCClient getVideoProfile];
    successCallback(@[@{@"profile": [self profileToString: profile]}]);
}

RCT_EXPORT_METHOD(switchCamera) {
    [[JMRTCClient currentCallSession] switchCameraMode];
}

RCT_EXPORT_METHOD(accept:(RCTResponseSenderBlock)successCallback
                    fail:(RCTResponseSenderBlock)failCallback) {
    
    if (![JMRTCClient currentCallSession]) {
        // TODO: 确定 未接听是否存在
        failCallback(@[[self getErrorWithLog:@"无法获取当前通话 session"]]);
        return;
    }
    
    [[JMRTCClient currentCallSession] accept:^(id resultObject, NSError *error) {
        if (error) {
            failCallback(@[[error errorToDictionary]]);
            return;
        }
        
        successCallback(@[@{}]);
    }];
    
}

RCT_EXPORT_METHOD(hangup:(RCTResponseSenderBlock)successCallback
                    fail:(RCTResponseSenderBlock)failCallback) {
    if (![JMRTCClient currentCallSession]) {
        // TODO: 确定 未接听是否存在
        failCallback(@[[self getErrorWithLog:@"无法获取当前通话 session"]]);
        return;
    }
    
    [[JMRTCClient currentCallSession] hangup:^(id resultObject, NSError *error) {
        if (error) {
            failCallback(@[[error errorToDictionary]]);
            return;
        }
        
        successCallback(@[@{}]);
    }];
}

RCT_EXPORT_METHOD(refuse:(RCTResponseSenderBlock)successCallback
                    fail:(RCTResponseSenderBlock)failCallback) {
    if (![JMRTCClient currentCallSession]) {
        // TODO: 确定 未接听是否存在
        failCallback(@[[self getErrorWithLog:@"无法获取当前通话 session"]]);
        return;
    }
    
    [[JMRTCClient currentCallSession] refuse:^(id resultObject, NSError *error) {
        if (error) {
            failCallback(@[[error errorToDictionary]]);
            return;
        }
        
        successCallback(@[@{}]);
    }];
}

RCT_EXPORT_METHOD(inviteUsers:(NSDictionary *)params
                      success:(RCTResponseSenderBlock)successCallback
                         fail:(RCTResponseSenderBlock)failCallback) {

    if (!params[@"usernames"]) {
        return;
    }
    
    [JMSGUser userInfoArrayWithUsernameArray:params[@"usernames"] completionHandler:^(id resultObject, NSError *error) {
        if (error) {
            failCallback(@[[error errorToDictionary]]);
            return;
        }
        
        NSArray *users = resultObject;
        [[JMRTCClient currentCallSession] inviteUsers:users handler:^(id resultObject, NSError *error) {
            if (error) {
                failCallback(@[[error errorToDictionary]]);
                return;
            }
            
            successCallback(@[@{}]);
        }];
    }];
}

RCT_EXPORT_METHOD(isMuted:(RCTResponseSenderBlock)callback) {
    callback(@[@([JMRTCClient currentCallSession].isMuted)]);
    
}

RCT_EXPORT_METHOD(setIsMuted:(NSDictionary *)params) {
    
    if (!params[@"muted"]) {
        return;
    }
    NSNumber *isMuted = params[@"muted"];
    [[JMRTCClient currentCallSession] setMuted: isMuted.boolValue];
}

RCT_EXPORT_METHOD(isSpeakerphoneEnabled:(RCTResponseSenderBlock)callback) {
    callback(@[@([JMRTCClient currentCallSession].isSpeakerphoneEnabled)]);
}

RCT_EXPORT_METHOD(setIsSpeakerphoneEnabled:(NSDictionary *)params) {
    
    if (!params[@"speakerphoneEnabled"]) {
        return;
    }
    
    NSNumber *isSpeakerphoneEnabled = params[@"speakerphoneEnabled"];
    [[JMRTCClient currentCallSession] setSpeakerEnabled: isSpeakerphoneEnabled.boolValue];
}

RCT_EXPORT_METHOD(isVideoStreamEnabled:(RCTResponseSenderBlock)callback) {
    callback(@[@([JMRTCClient currentCallSession].isVideoStreamEnabled)]);
}

RCT_EXPORT_METHOD(setIsVideoStreamEnabled:(NSDictionary *)params) {
    
    if (!params[@"videoStreamEnabled"]) {
        return;
    }
    
    NSNumber *isVideoStreamEnabled = params[@"videoStreamEnabled"];
    [[JMRTCClient currentCallSession] setVideoStreamEnabled: isVideoStreamEnabled.boolValue];
}

- (JMRTCMediaType)stringToMeiaType:(NSString *)str {
    if ([str isEqualToString:@"video"]) {
        return JMRTCMediaVideo;
    } else {
        return JMRTCMediaAudio;
    }
}

- (NSString *)mediaTypeToString:(JMRTCMediaType)type {
    switch (type) {
        case JMRTCMediaVideo:
            return @"video";
            break;
        case JMRTCMediaAudio:
            return @"voice";
    }
}

- (JMRTCVideoProfile)stringToVideoProfile:(NSString *)str {
    if ([str isEqualToString:@"240p"]) {
        return JMRTC_VIDEO_PROFILE_240P;
    } else if ([str isEqualToString:@"480p"]) {
        return JMRTC_VIDEO_PROFILE_480P;
    } else if ([str isEqualToString:@"720p"]) {
        return JMRTC_VIDEO_PROFILE_720P;
    } else {
        return JMRTC_VIDEO_PROFILE_360P;
    }
}

- (NSString *)profileToString: (JMRTCVideoProfile)profile {
    switch (profile) {
        case JMRTC_VIDEO_PROFILE_240P:
            return @"240p";
        case JMRTC_VIDEO_PROFILE_480P:
            return @"480p";
        case JMRTC_VIDEO_PROFILE_720P:
            return @"720p";
            
        default:
            return @"360p";
    }
}

- (NSString *)reasonToString: (JMRTCDisconnectReason)reason {
    switch (reason) {
        case JMRTCDisconnectReasonRefuse:
            return @"refuse";
            break;
        case JMRTCDisconnectReasonHangup:
            return @"hangup";
            break;
        case JMRTCDisconnectReasonCancel:
            return @"cancel";
            break;
        case JMRTCDisconnectReasonBusy:
            return @"busy";
            break;
        case JMRTCDisconnectReasonNetworkError:
            return @"networkError";
            break;
    }
}

- (NSDictionary *)getParamError {
    return @{@"code": @(1), @"description": @"param error!"};
}

- (NSDictionary *)getErrorWithLog:(NSString *)log {
    return @{@"code": @(1), @"description": log};
}

- (NSDictionary *)userToDic:(JMSGUser *)user {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    dict[@"type"] = @"user";
    dict[@"username"] = user.username;
    dict[@"nickname"] = user.nickname;
    dict[@"birthday"] = user.birthday;
    dict[@"region"] = user.region;
    dict[@"signature"] = user.signature;
    dict[@"address"] = [user address];
    dict[@"noteName"] = user.noteName;
    dict[@"noteText"] = user.noteText;
    dict[@"appKey"] = user.appKey;
    dict[@"isNoDisturb"] = @(user.isNoDisturb);
    dict[@"isInBlackList"] = @(user.isInBlacklist);
    dict[@"isFriend"] = @(user.isFriend);
    dict[@"extras"] = user.extras;
    
    if([[NSFileManager defaultManager] fileExistsAtPath: [user thumbAvatarLocalPath] ?: @""]){
        dict[@"avatarThumbPath"] = [user thumbAvatarLocalPath];
    } else {
        dict[@"avatarThumbPath"] = @"";
    }
    
    switch (user.gender) {
        case kJMSGUserGenderUnknown:
            dict[@"gender"] = @"unknown";
            break;
        case kJMSGUserGenderFemale:
            dict[@"gender"] = @"female";
            break;
        case kJMSGUserGenderMale:
            dict[@"gender"] = @"male";
            break;
        default:
            break;
    }
    return dict.copy;
}

- (void)onCallOutgoing:(JMRTCSession *)callSession {
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallOutgoing
                                                 body:[callSession sessionToDictionary]];
}

- (void)onCallReceiveInvite:(JMRTCSession *)callSession {
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallReceiveInvite
                                                 body:[callSession sessionToDictionary]];
}

- (void)onCallConnecting:(JMRTCSession *)callSession {
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallConnecting
                                                 body:[callSession sessionToDictionary]];
}

- (void)onCallConnected:(JMRTCSession *)callSession {
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallConnected
                                                 body:[callSession sessionToDictionary]];
}

- (void)onCallMemberJoin:(JMSGUser *)joinUser {
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallMemberJoin
                                                 body:[self userToDic:joinUser]];
}

- (void)onCallDisconnect:(JMRTCSession *)callSession disconnectReason:(JMRTCDisconnectReason)reason {
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallDisconnect
                                                 body:[callSession sessionToDictionary]];
}

- (void)onCallMemberLeave:(JMSGUser *)leaveUser reason:(JMRTCDisconnectReason)reason {
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallMemberLeave
                                                 body:@{@"user": [self userToDic:leaveUser], @"reason": [self reasonToString:reason]}];
}

- (void)onCallOtherUserInvited:(NSArray <__kindof JMSGUser *>*)invitedUsers fromUser:(JMSGUser *)fromUser {
    NSMutableArray *userDics = @[].mutableCopy;
    for (JMSGUser *user in invitedUsers) {
        [userDics addObject:[self userToDic:user]];
    }
    
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallOtherUserInvited
                                                 body:@{@"invitedUsers": userDics, @"fromUser": [self userToDic:fromUser]}];
}

- (void)onCallError:(NSError *)error {
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallError
                                                 body:[error errorToDictionary]];
}

- (void)onCallUserVideoStreamEnabled:(BOOL)enabled byUser:(JMSGUser *)user {
    
    [self.bridge.eventDispatcher sendAppEventWithName:kJMRTCCallUserVideoStreamEnabled
                                                 body:@{@"enabled": @(enabled), @"user":[self userToDic:user]}];
}
@end
