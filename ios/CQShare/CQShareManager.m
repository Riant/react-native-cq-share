//
//  CQShareManager.m
//  CQShare
//
//  Created by cukiy on 2018/3/28.
//  Copyright © 2018年 cukiy. All rights reserved.
//

#import "CQShareManager.h"
#import <UIKit/UIKit.h>

@interface CQShareManager ()


@property (nonatomic, copy) RCTResponseSenderBlock callback;


@end

@implementation CQShareManager


RCT_EXPORT_MODULE(CQShare);


/** 
 *  分享图片或链接
 *  options  分享的数据,包含三个字段.  title(标题)  url(链接)  images(图片url数组)
 *  callbake   分享操作完成的回调. 返回true(分享成功)或false(分享失败)
 */
RCT_EXPORT_METHOD(sharePictureWithOptions:(NSDictionary *)options Callback:(RCTResponseSenderBlock)callback) {
    
    _callback = callback;
    NSMutableArray *optionArr = [[NSMutableArray alloc] init];
    
    
    // 添加url
    if(options[@"url"]) {
        [optionArr addObject:[NSURL URLWithString:options[@"url"]]];
    }
    // 添加图片
    if (options[@"remoteImages"]) {
        if (options[@"url"]) {
            [optionArr addObject:[UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:options[@"remoteImages"][0]]]]];
            
        } else {
            for (NSString *url in options[@"remoteImages"]) {
                [optionArr addObject:[UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:url]]]];
            }
        }
    } else if(options[@"localImages"]){
        if (options[@"url"]) {
            [optionArr addObject:[UIImage imageWithContentsOfFile:options[@"localImages"][0]]];
            
        } else {
            for (NSString *url in options[@"localImages"]) {
                [optionArr addObject:[UIImage imageWithContentsOfFile:url]];
            }
        }
        
    }
    // 添加标题
    if (options[@"title"]) {
        [optionArr addObject:options[@"title"]];
    }
    
    
    UIWindow *keyWindow  = [UIApplication sharedApplication].keyWindow;
    UIViewController *vc = keyWindow.rootViewController;
    while (vc.presentedViewController)
    {
        vc = vc.presentedViewController;
        
        if ([vc isKindOfClass:[UINavigationController class]])
        {
            vc = [(UINavigationController *)vc visibleViewController];
        }
        else if ([vc isKindOfClass:[UITabBarController class]])
        {
            vc = [(UITabBarController *)vc selectedViewController];
        }
    }
    
    
    UIActivityViewController *activityController=[[UIActivityViewController alloc]initWithActivityItems:optionArr applicationActivities:nil];
    
    UIActivityViewControllerCompletionWithItemsHandler completionBlock = ^(NSString *activityType,BOOL completed,NSArray *returnedItems, NSError *activityError) {
        
        if (completed)  {
            
            if(_callback) {
                _callback(@[@TRUE]);
            }
            
        }
        else  {
            
            if(_callback) {
                _callback(@[@FALSE]);
            }
        }
        
    };
    
    // 去掉多余的分享平台
    activityController.excludedActivityTypes = @[UIActivityTypePostToFacebook,UIActivityTypePostToTwitter, UIActivityTypePostToWeibo,UIActivityTypeMessage,UIActivityTypeMail,UIActivityTypePrint,UIActivityTypeCopyToPasteboard,UIActivityTypeAssignToContact,UIActivityTypeSaveToCameraRoll,UIActivityTypeAddToReadingList,UIActivityTypePostToFlickr,UIActivityTypePostToVimeo,UIActivityTypePostToTencentWeibo,UIActivityTypeAirDrop,UIActivityTypeOpenInIBooks];
    
    activityController.completionWithItemsHandler = completionBlock;
    
    dispatch_sync(dispatch_get_main_queue(), ^{
        [vc presentViewController:activityController animated:YES completion:nil];
    });
    

}






@end
