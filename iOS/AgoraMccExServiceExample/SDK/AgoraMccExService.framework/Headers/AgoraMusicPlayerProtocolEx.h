//
//  AgoraMusicPlayerProtocolEx.h
//  AgoraMCCService
//
//  Created by ZhouRui on 2024/4/22.
//

#import <Foundation/Foundation.h>
#import <AgoraMccExService/AgoraMusicContentCenterExObjects.h>
#import <AgoraRtcKit/AgoraRtcMediaPlayerProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@protocol AgoraMusicPlayerProtocolEx <AgoraRtcMediaPlayerProtocol>

/**
 * Open a media file with specified parameters.
 *
 * @param songCode The identifier of the media file that you want to play.
 * @param startPos The playback position (ms) of the music file.
 * @return
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)openMediaWithSongCode:(NSInteger)songCode startPos:(NSInteger)startPos NS_SWIFT_NAME(openMedia(songCode:startPos:));

/**
* Set the mode for playing songs.
* You can call this method to switch from original to accompaniment.
* If you do not call this method to set the mode, the SDK plays the accompaniment by default.
* This method must be called after `openMediaWithSongCode` callback complete.
*
* @param mode The playing mode.
* @return
* - 0: Success.
* - < 0: Failure.
*/
- (NSInteger)setPlayMode:(AgoraMusicPlayMode)mode NS_SWIFT_NAME(setPlayMode(mode:));

@end

NS_ASSUME_NONNULL_END
