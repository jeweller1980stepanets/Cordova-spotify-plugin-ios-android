//
//  SpotifyPlugin.h
//

#import "Cordova/CDV.h"
#import <SpotifyAudioPlayback/SpotifyAudioPlayback.h>
#import <SpotifyAuthentication/SpotifyAuthentication.h>
#import <SpotifyMetadata/SpotifyMetadata.h>

#import <WebKit/WebKit.h>

NSString *dateToString(NSDate *date);
NSDate *stringToDate(NSString *dateString);

@interface SpotifyPlugin : CDVPlugin
-(void)myPluginMethod:(CDVInvokedUrlCommand*)command;
-(id)init;
-(void)login :(CDVInvokedUrlCommand*)command;
-(void)play:(CDVInvokedUrlCommand*)command;
-(void)pause:(CDVInvokedUrlCommand*)command;
-(void)next:(CDVInvokedUrlCommand*)command;
-(void)prev:(CDVInvokedUrlCommand*)command;
-(void)logout:(CDVInvokedUrlCommand*)command;
-(void)seek:(CDVInvokedUrlCommand*)command;
-(void)volume:(CDVInvokedUrlCommand*)command;
-(void)audioStreaming:(SPTAudioStreamingController *)audioStreaming didChangeToTrack:(NSDictionary *)trackMetadata;
-(void)audioStreamingDidSkipToNextTrack:(SPTAudioStreamingController *)audioStreaming;
-(void)audioStreamingDidSkipToPreviousTrack:(SPTAudioStreamingController *)audioStreaming;
-(void)audioStreaming:(SPTAudioStreamingController *)audioStreaming didSeekToOffset:(NSTimeInterval)offset;//????
-(void)audioStreaming:(SPTAudioStreamingController *)audioStreaming didChangePosition:(NSTimeInterval)position;
-(void)audioStreaming:(SPTAudioStreamingController *)audioStreaming didChangeVolume:(SPTVolume)volume;
@end
