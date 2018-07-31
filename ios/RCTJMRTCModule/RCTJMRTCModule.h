//
//  RCTJMRTCModule.h
//  RCTJMRTCModule
//
//  Created by oshumini on 2018/7/16.
//  Copyright © 2018年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>
#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/RCTEventDispatcher.h>
#elif __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#import "RCTEventDispatcher.h"
#elif __has_include("React/RCTBridgeModule.h")
#import "React/RCTEventDispatcher.h"
#import "React/RCTBridgeModule.h"
#endif

#import <JMRTC/JMRTC.h>
#import <JMessage/JMessage.h>

#define kJMRTCSetVideoView @"JMRTC.SetVideoView"

#define kJMRTCCallOutgoing @"JMRTC.CallOutgoing"
#define kJMRTCCallReceiveInvite @"JMRTC.CallReceiveInvite"
#define kJMRTCCallConnecting @"JMRTC.CallConnecting"
#define kJMRTCCallConnected @"JMRTC.CallConnected"
#define kJMRTCCallMemberJoin @"JMRTC.CallMemberJoin"
#define kJMRTCCallDisconnect @"JMRTC.CallDisconnect"
#define kJMRTCCallMemberLeave @"JMRTC.CallMemberLeave"
#define kJMRTCCallOtherUserInvited @"JMRTC.CallOtherUserInvited"
#define kJMRTCCallError @"JMRTC.CallError"
#define kJMRTCCallUserVideoStreamEnabled @"JMRTC.CallUserVideoStreamEnabled"

@interface NSError (JMessage)
- (NSDictionary *)errorToDictionary;
@end

@interface JMRTCSession (JMRTCRN)
- (NSDictionary *)sessionToDictionary;
@end

@interface RCTJMRTCModule : NSObject <RCTBridgeModule>

@end
