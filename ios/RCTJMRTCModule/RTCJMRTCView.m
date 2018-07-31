//
//  RTCJMRTCView.m
//  RCTJMRTCModule
//
//  Created by oshumini on 2018/7/16.
//  Copyright © 2018年 HXHG. All rights reserved.
//

#import "RTCJMRTCView.h"
#import "RCTJMRTCModule.h"

@implementation RTCJMRTCView

- (instancetype)init {
    self = [super init];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(SetVideoView:)
                                                 name:kJMRTCSetVideoView
                                               object:nil];
    self.backgroundColor = [UIColor redColor];
    return self;
}

- (void)SetVideoView:(NSNotification *) notification {
    NSDictionary *params = [notification object];
    
    
    if (![JMRTCClient currentCallSession]) {
        return;
    }
    
    if ([params[@"username"] isEqualToString:self.username]) {
        [JMSGUser userInfoArrayWithUsernameArray:@[self.username] completionHandler:^(id resultObject, NSError *error) {
            
            if (error) {
                NSLog(@"get user from user name error: %@", [error description]);
                return;
            }
            
            NSArray *users = resultObject;
            
            if (users.count == 0) {
                NSLog(@"there not user here!");
                return;
            }
            [[JMRTCClient currentCallSession] setVideoView:self user: users[0]];
        }];
    }
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver: self];
}

@end
