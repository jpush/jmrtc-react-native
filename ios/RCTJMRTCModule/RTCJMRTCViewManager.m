//
//  RTCJMRTCViewManager.m
//  RCTJMRTCModule
//
//  Created by oshumini on 2018/7/17.
//  Copyright © 2018年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>
#import "RTCJMRTCView.h"
#import "RCTJMRTCModule.h"

@interface RTCJMRTCViewManager : RCTViewManager

@property (strong, nonatomic)RTCJMRTCView *rctJMRTCView;
@end

@implementation RTCJMRTCViewManager

RCT_EXPORT_MODULE()
- (UIView *)view
{
    _rctJMRTCView = [RTCJMRTCView new];
    return _rctJMRTCView;
}

RCT_CUSTOM_VIEW_PROPERTY(username, NSString, RTCJMRTCView) {
    NSString *username = [RCTConvert NSString: json];
    view.username = username;
}

@end
