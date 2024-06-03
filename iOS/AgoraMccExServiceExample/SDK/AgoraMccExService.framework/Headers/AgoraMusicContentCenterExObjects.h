//
//  AgoraMusicContentCenterExObjects.h
//  AgoraMCCService
//
//  Created by ZhouRui on 2024/4/22.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, AgoraMusicContentCenterExStateReason) {
    /**
     * -10: PTS error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorPtsError = -10,
    /**
     * -9: Not activated error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorNoActivateError = -9,
    /**
     * -8: Repeat request error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorRepeatRequestError = -8,
    /**
     * -7: Privilege error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorPrivilegeError = -7,
    /**
     * -6: Request error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorRequestError = -6,
    /**
     * -5: Network error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorNetworkError = -5,
    /**
     * -4: Token error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorTokenError = -4,
    /**
     * -3: Lyric error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorLyricError = -3,
    /**
     * -2: Pitch error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorPitchError = -2,
    /**
     * -1: Parameter error.
     */
    AgoraMusicContentCenterExStateReasonYSDErrorParamError = -1,
    /**
     * 0: No error occurs and request succeeds.
     */
    AgoraMusicContentCenterExStateReasonOK = 0,
    /**
     * 1: The general error.
     */
    AgoraMusicContentCenterExStateReasonError = 1,
    /**
     * 2: Http internal error. Please retry later.
     */
    AgoraMusicContentCenterExStateReasonErrorHttpInternalError = 2,
    /**
     * 3: Invalid signature.
     */
    AgoraMusicContentCenterExStateReasonErrorInvalidSignature = 3,
};

/**
 * The status of music content center
 */
typedef NS_ENUM(NSUInteger, AgoraMusicContentCenterExState) {
    /**
     * 0: Initialize successfully.
     */
    AgoraMusicContentCenterExStateInitialized = 0,
    /**
     * 1: Initialize fail.
     */
    AgoraMusicContentCenterExStateInitializeFailed = 1,
    /**
     * 2: No error occurs and preload succeeds.
     */
    AgoraMusicContentCenterExStatePreloadOK = 2,

    /**
     * 3: A general error occurs.
     */
    AgoraMusicContentCenterExStatePreloadError = 3,

    /**
     * 4: The media file is preloading.
     */
    AgoraMusicContentCenterExStatePreloading = 4,
    
    /**
     * 5: The media file is removed.
     */
    AgoraMusicContentCenterExStatePreloadRemoveCache = 5,
    
    /**
     * 6: Start score completed.
     */
    AgoraMusicContentCenterExStateStartScoreCompleted = 6,
    
    /**
     * 7: Start score failed.
     */
    AgoraMusicContentCenterExStateStartScoreFailed = 7,
};

/**
 * Modes for playing songs.
 */
typedef NS_ENUM(NSUInteger, AgoraMusicPlayMode) {
    /**
     * The music player is in the accompany mode, which means playing the accompaniment only.
     */
    AgoraMusicPlayModeAccompany = 0,
    /**
     * The music player is in the origin mode, which means playing the original song.
     */
    AgoraMusicPlayModeOriginal = 1,
};

/**
 * Type for lyric.
 */
typedef NS_ENUM(NSUInteger, AgoraMusicLyricType) {
    AgoraMusicLyricTypeKRC = 0
    /// extension
};

/**
 * Third party vendors provide services.
 */
typedef NS_ENUM(NSUInteger, AgoraMusicContentCenterExServiceVendor) {
    AgoraMusicContentCenterExServiceVendorYSD = 0
    /// extension
};

/**
 * Mode for YSD charge.
 */
typedef NS_ENUM(NSUInteger, AgoraYSDChargeMode) {
    AgoraYSDChargeModeMonthly = 1, //monthly
    AgoraYSDChargeModeOnce = 2 //once
};

/**
 * Hard level for score.
 */
typedef NS_ENUM(NSUInteger, AgoraYSDScoreHardLevel) {
    AgoraYSDScoreHardLevel1 = 0,
    AgoraYSDScoreHardLevel2 = 1,
    AgoraYSDScoreHardLevel3 = 2,
    AgoraYSDScoreHardLevel4 = 3,
    AgoraYSDScoreHardLevel5 = 4
};

/**
 * Version for YSD score algorithm, default & suggest use AgoraYSDScoreVersionOld.
 */
typedef NS_ENUM(NSUInteger, AgoraYSDScoreVersion) {
    AgoraYSDScoreVersionOld = 0,
    AgoraYSDScoreVersionNew = 1,
};

@class AgoraRtcEngineKit;
@class AgoraVendorConfigure;
@protocol AgoraMusicContentCenterExEventDelegate;
@protocol AgoraMusicContentCenterExScoreEventDelegate;
@protocol AgoraAudioFrameDelegate;

__attribute__((visibility("default")))
@interface AgoraMusicContentCenterExConfiguration : NSObject

/// The AgoraRtcEngineKit instance.
@property (nonatomic, strong, readonly) AgoraRtcEngineKit *rtcEngine;
/// The vendor configuration.
@property (nonatomic, strong, readonly) AgoraVendorConfigure *vendorConfigure;
/// Whether logging is enabled.
@property (nonatomic, assign, readonly) BOOL enableLog;
/// Whether to save log to file.
@property (nonatomic, assign, readonly) BOOL enableSaveLogToFile;
/// custom file path , sdk will use internal default path if seeting nil or empty string
@property (nonatomic, copy, readonly) NSString * _Nullable logFilePath;
/// The maximum cache size.
@property (nonatomic, assign, readonly) NSInteger maxCacheSize;
/// The event delegate.
@property (nonatomic, weak, readonly) id<AgoraMusicContentCenterExEventDelegate> _Nullable eventDelegate;
/// The score event delegate.
@property (nonatomic, weak, readonly) id<AgoraMusicContentCenterExScoreEventDelegate> _Nullable scoreEventDelegate;
/// The audio frame delegate.
@property (nonatomic, weak, readonly) id<AgoraAudioFrameDelegate> _Nullable audioFrameDelegate;

/**
 * Initializes the configuration.
 *
 * @param rtcEngine The AgoraRtcEngineKit instance.
 * @param vendorConfigure The vendor configuration.
 * @param enableLog Whether to enable logging.
 * @param enableSaveLogToFile Whether to save log to file.
 * @param logFilePath The file path for logging.
 * @param maxCacheSize The maximum cache size.
 * @param eventDelegate The event delegate.
 * @param scoreEventDelegate The score event delegate.
 * @param audioFrameDelegate The audio frame delegate.
 * @return An instance of AgoraMusicContentCenterExConfiguration.
 */
- (instancetype)initWithRtcEngine:(nonnull AgoraRtcEngineKit *)rtcEngine
                  vendorConfigure:(AgoraVendorConfigure *)vendorConfigure
                        enableLog:(BOOL)enableLog
              enableSaveLogToFile:(BOOL)enableSaveLogToFile
                      logFilePath:(NSString * _Nullable)logFilePath
                     maxCacheSize:(NSInteger)maxCacheSize
                    eventDelegate:(id<AgoraMusicContentCenterExEventDelegate> _Nullable)eventDelegate
               scoreEventDelegate:(id<AgoraMusicContentCenterExScoreEventDelegate> _Nullable)scoreEventDelegate
               audioFrameDelegate:(id<AgoraAudioFrameDelegate> _Nullable)audioFrameDelegate;
- (instancetype)init __attribute__((unavailable("Use initWithRtcEngine instead")));
@end

/// Vendor configure
__attribute__((visibility("default")))
@interface AgoraVendorConfigure : NSObject
/// The service vendor which provides services.
@property (nonatomic, assign, readonly) AgoraMusicContentCenterExServiceVendor serviceVendor;
/// The APPID of vendor.
@property (nonatomic, copy, readonly) NSString *appId;
/// The APPKey of vendor.
@property (nonatomic, copy, readonly) NSString *appKey;
/// The token of vendor for authentication, 24 hours expired.
@property (nonatomic, copy, readonly) NSString *token;
/// The user ID of vendor.
@property (nonatomic, copy, readonly) NSString *userId;
/// The device ID of vendor.
@property (nonatomic, copy, readonly) NSString *deviceId;

/**
 * Initializes the vendor configuration.
 *
 * @param serviceVendor The service vendor.
 * @param appId The APPID of vendor.
 * @param appKey The APPKey of vendor.
 * @param token The token of vendor for authentication.
 * @param userId The user ID of vendor.
 * @param deviceId The device ID of vendor.
 * @return An instance of AgoraVendorConfigure.
 */
- (instancetype)initWithServiceVendor:(AgoraMusicContentCenterExServiceVendor)serviceVendor
                                appId:(nonnull NSString *)appId
                               appKey:(nonnull NSString *)appKey
                                token:(nonnull NSString *)token
                               userId:(nonnull NSString *)userId
                             deviceId:(nonnull NSString *)deviceId;

- (instancetype)init __attribute__((unavailable("Use initWithServiceVendor instead")));
@end

/// YSD Vendor configure
__attribute__((visibility("default")))
@interface AgoraYSDVendorConfigure : AgoraVendorConfigure

/// The expire time(in seconds) of url token, different from the above token.
@property (nonatomic, assign, readonly) NSInteger urlTokenExpireTime;
/// The charge mode specific to YSD.
@property (nonatomic, assign, readonly) AgoraYSDChargeMode chargeMode;

/**
 * Initializes the YSD vendor configuration.
 *
 * @param appId The APPID of vendor.
 * @param appKey The APPKey of vendor.
 * @param token The token of vendor for authentication.
 * @param userId The user ID of vendor.
 * @param deviceId The device ID of vendor.
 * @param urlTokenExpireTime The expire time of url token.
 * @param chargeMode The charge mode specific to YSD.
 * @return An instance of AgoraYSDVendorConfigure.
 */
- (instancetype)initWithAppId:(nonnull NSString *)appId
                       appKey:(nonnull NSString *)appKey
                        token:(nonnull NSString *)token
                       userId:(nonnull NSString *)userId
                     deviceId:(nonnull NSString *)deviceId
           urlTokenExpireTime:(NSInteger)urlTokenExpireTime
                   chargeMode:(AgoraYSDChargeMode)chargeMode;
- (instancetype)init __attribute__((unavailable("Use initWithAppid instead")));
@end

__attribute__((visibility("default")))
@interface AgoraRawScoreData : NSObject
/// The progress in milliseconds.
@property(nonatomic, assign, readonly) NSUInteger progressInMs;
/// The speaker‘s current pitch.
@property(nonatomic, assign, readonly) CGFloat speakerPitch;
/// The pitch score of current speaker.
@property(nonatomic, assign, readonly) CGFloat pitchScore;

/**
 * Initializes the raw score data.
 *
 * @param progressInMs The progress in milliseconds.
 * @param speakerPitch The speaker pitch.
 * @param pitchScore The pitch score.
 * @return An instance of AgoraRawScoreData.
 */
- (instancetype)initWithProgressInMs:(NSUInteger)progressInMs
                        speakerPitch:(CGFloat)speakerPitch
                          pitchScore:(CGFloat)pitchScore;
- (instancetype)init __attribute__((unavailable("Use initWithProgressInMs instead")));
@end

__attribute__((visibility("default")))
@interface AgoraLineScoreData : NSObject
/// The progress in milliseconds.
@property(nonatomic, assign, readonly) NSUInteger progressInMs;
/// The index of the performed line.
@property(nonatomic, assign, readonly) NSUInteger performedLineIndex;
/// The pitch score of the line.
@property(nonatomic, assign, readonly) CGFloat linePitchScore;
/// The total number of performed lines.
@property(nonatomic, assign, readonly) NSUInteger performedTotalLines;
/// The cumulative total pitch scores of all lines.
@property(nonatomic, assign, readonly) CGFloat cumulativeTotalLinePitchScores;
/// The energy score.
@property(nonatomic, assign, readonly) CGFloat energyScore;

/**
 * Initializes the line score data.
 *
 * @param progressInMs The progress in milliseconds.
 * @param performedLineIndex The performed line index.
 * @param linePitchScore The line pitch score.
 * @param performedTotalLines The total performed lines.
 * @param cumulativeTotalLinePitchScores The cumulative total line pitch scores.
 * @param energyScore The energy score.
 * @return An instance of AgoraLineScoreData.
 */
- (instancetype)initWithProgressInMs:(NSUInteger)progressInMs
                  performedLineIndex:(NSUInteger)performedLineIndex
                      linePitchScore:(CGFloat)linePitchScore
                 performedTotalLines:(NSUInteger)performedTotalLines
      cumulativeTotalLinePitchScores:(CGFloat)cumulativeTotalLinePitchScores
                         energyScore:(CGFloat)energyScore;
- (instancetype)init __attribute__((unavailable("Use initWithProgressInMs instead")));
@end

NS_ASSUME_NONNULL_END
