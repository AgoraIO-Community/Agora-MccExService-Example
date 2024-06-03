//
//  AgoraMusicContentCenterEx.h
//  AgoraMCCService
//
//  Created by ZhouRui on 2024/4/22.
//

#import <Foundation/Foundation.h>
#import <AgoraRtcKit/AgoraRtcEngineKit.h>
#import "AgoraMusicContentCenterExObjects.h"

NS_ASSUME_NONNULL_BEGIN

/// Protocol for receiving event callbacks from AgoraMusicContentCenterEx.
@protocol AgoraMusicContentCenterExEventDelegate <NSObject>

/**
 * Callback for initialization result.
 *
 * @param state The state of initialization; see AgoraMusicContentCenterExState.
 * @param reason The reason for the state of initialization; see AgoraMusicContentCenterExStateReason.
 */
- (void)onInitializeResult:(AgoraMusicContentCenterExState)state
                    reason:(AgoraMusicContentCenterExStateReason)reason;

/**
 * Callback for start score result.
 *
 * @param songCode The code of the song.
 * @param state The state of starting score; see AgoraMusicContentCenterExState.
 * @param reason The reason for the state of starting score; see AgoraMusicContentCenterExStateReason.
 */
- (void)onStartScoreResult:(NSInteger)songCode
                     state:(AgoraMusicContentCenterExState)state
                    reason:(AgoraMusicContentCenterExStateReason)reason;

/**
 * Callback for preload process.
 *
 * @param requestId The request ID, same as returned by preload.
 * @param songCode The code of the song.
 * @param percent The preload progress (0 ~ 100).
 * @param lyricPath The path to the lyric file.
 * @param pitchPath The path to the pitch file.
 * @param offsetBegin The beginning offset of the song.
 * @param offsetEnd The ending offset of the song.
 * @param state The state of the preload; see AgoraMusicContentCenterExState.
 * @param reason The reason for the preload result; see AgoraMusicContentCenterExStateReason.
 */
- (void)onPreLoadEvent:(NSString*)requestId
              songCode:(NSInteger)songCode
               percent:(NSInteger)percent
             lyricPath:(NSString* _Nullable)lyricPath
             pitchPath:(NSString* _Nullable)pitchPath
           offsetBegin:(NSInteger)offsetBegin
             offsetEnd:(NSInteger)offsetEnd
                 state:(AgoraMusicContentCenterExState)state
                reason:(AgoraMusicContentCenterExStateReason)reason;

/**
 * Callback for lyric request result.
 *
 * @param requestId The request ID.
 * @param songCode The code of the song.
 * @param lyricPath The path to the lyric file, default path is in cache directory.
 * @param offsetBegin The beginning offset of the lyric.
 * @param offsetEnd The ending offset of the song.
 * @param reason The reason for the lyric result; see AgoraMusicContentCenterExStateReason.
 */
- (void)onLyricResult:(NSString*)requestId
             songCode:(NSInteger)songCode
            lyricPath:(NSString* _Nullable)lyricPath
          offsetBegin:(NSInteger)offsetBegin
            offsetEnd:(NSInteger)offsetEnd
               reason:(AgoraMusicContentCenterExStateReason)reason;

/**
 * Callback for pitch request result.
 *
 * @param requestId The request ID.
 * @param songCode The code of the song.
 * @param pitchPath The path to the pitch file, default path is in cache directory.
 * @param offsetBegin The beginning offset of the pitch.
 * @param offsetEnd The ending offset of the pitch.
 * @param reason The reason for the pitch result; see AgoraMusicContentCenterExStateReason.
 */
- (void)onPitchResult:(NSString*)requestId
             songCode:(NSInteger)songCode
            pitchPath:(NSString* _Nullable)pitchPath
          offsetBegin:(NSInteger)offsetBegin
            offsetEnd:(NSInteger)offsetEnd
               reason:(AgoraMusicContentCenterExStateReason)reason;
@end

@protocol AgoraMusicContentCenterExScoreEventDelegate <NSObject>
@optional
/**
 * Callback for pitch data.
 *
 * @param songCode The code of the song.
 * @param data The raw score data.
 */
-(void)onPitch:(NSInteger)songCode
          data:(AgoraRawScoreData *)data;

/**
 * Callback for line score data.
 *
 * @param songCode The code of the song.
 * @param value The cumulative score data.
 */
-(void)onLineScore:(NSInteger)songCode
             value:(AgoraLineScoreData *)value;

@end


@protocol AgoraMusicPlayerProtocolEx;
__attribute__((visibility("default")))
@interface AgoraMusicContentCenterEx : NSObject

/// Return the shared instance of AgoraMusicContentCenterEx.
+ (instancetype)sharedInstance;

/// Destroy the shared instance of AgoraMusicContentCenterEx.
+ (void)destroy;

/**
 * Initialize the service.
 *
 * @param config The configuration for initialization.
 * @return Returns:
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)initialize:(AgoraMusicContentCenterExConfiguration *)config;

/**
 * Renew the token for the service.
 *
 * @param token The token to be renewed.
 * @return Returns:
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)renewToken:(NSString * _Nonnull)token;

/**
 * Register an event delegate.
 *
 * @param eventDelegate The event delegate to be registered.
 * @return Returns:
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)registerEventDelegate:(id<AgoraMusicContentCenterExEventDelegate> _Nullable)eventDelegate;

/**
 * Register a score event delegate.
 *
 * @param scoreDelegate The score event delegate to be registered.
 * @return Returns:
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)registerScoreDelegate:(id<AgoraMusicContentCenterExScoreEventDelegate> _Nullable)scoreDelegate;

/**
  * Register the audio frame observer
  *
  * @param delegate observer object
  * @return Returns:
  * - 0: Success.
  * - < 0: Failure.
  */
- (NSInteger)registerAudioFrameDelegate:(id<AgoraAudioFrameDelegate> _Nullable)delegate;

/**
 * Preload a media file with specified parameters.
 *
 * @param songCode The identify of the media file that you want to play.
 * @return The request identification
 */
- (NSString * _Nullable)preload:(NSInteger)songCode;

/**
 * Preload a media file with specified parameters.
 *
 * @param songCode The identify of the media file that you want to play.
 * @return
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)isPreload:(NSInteger)songCode;

/**
 * Get the lyric for a song.
 *
 * @param songCode The code of the song.
 * @param lyricType The type of lyric.
 * @return The request identification.
 */
- (NSString * _Nullable)getLyric:(NSInteger)songCode lyricType:(AgoraMusicLyricType)lyricType;

/**
 * Get the pitch for a song.
 *
 * @param songCode The code of the song.
 * @return The request identification.
 */
- (NSString * _Nullable)getPitch:(NSInteger)songCode;

/**
 * Create a music player source object and return its pointer.
 *
 * @param delegate The object who need AgoraRtcMediaPlayerDelegate method to get the player information
 * @return
 * - The pointer to an object who realize the AgoraMusicPlayerProtocol, if the method call succeeds.
 * - The empty pointer NULL, if the method call fails.
 */
- (id<AgoraMusicPlayerProtocolEx>)createMusicPlayerWithDelegate:(id<AgoraRtcMediaPlayerDelegate> _Nullable)delegate;

/**
 * Destroy a music player source object and return result.
 *
 * @param musicPlayer The music player.
 * @return
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)destroyMusicPlayer:(id<AgoraMusicPlayerProtocolEx>)musicPlayer;

/**
 * Get the internal song code.
 *
 * @param musicId The ID of the music.
 * @param jsonOption The JSON option.
 * @return Returns:
 * - Internal song code if successful.
 * - < 0: Failure.
 */
- (NSInteger)getInternalSongCode:(NSString *)musicId jsonOption:(NSString *_Nullable)jsonOption;

/**
 * Start scoring for a song.
 *
 * @param songCode The code of the song.
 * @return Returns:
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)startScore:(NSInteger)songCode;

/**
 * Pause scoring.
 *
 * @return Returns:
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)pauseScore;

/**
 * Resume scoring.
 *
 * @return Returns:
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)resumeScore;

/**
 * Stop scoring.
 *
 * @return Returns:
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)stopScore;

/**
 * Set the score level.
 *
 * @param level The score level to set.
 * @return Returns:
 * - 0: Success.
 * - < 0: Failure.
 */
- (NSInteger)setScoreLevel:(AgoraYSDScoreHardLevel)level;

/// Get the SDK version.
/// @return Returns the SDK version.
+ (NSString *)getSdkVersion;

@end

NS_ASSUME_NONNULL_END
