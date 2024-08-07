package io.agora.mccex_example.agora

import android.content.Context
import io.agora.mccex.IMusicContentCenterEx
import io.agora.mccex.IMusicContentCenterExEventHandler
import io.agora.mccex.IMusicContentCenterExScoreEventHandler
import io.agora.mccex.IMusicPlayer
import io.agora.mccex.MusicContentCenterExConfiguration
import io.agora.mccex.constants.ChargeMode
import io.agora.mccex.constants.LyricType
import io.agora.mccex.constants.MccExState
import io.agora.mccex.constants.MccExStateReason
import io.agora.mccex.constants.MusicPlayMode
import io.agora.mccex.constants.ScoreHardLevel
import io.agora.mccex.model.LineScoreData
import io.agora.mccex.model.RawScoreData
import io.agora.mccex.model.YsdVendorConfigure
import io.agora.mccex.utils.Utils
import io.agora.mccex_example.BuildConfig
import io.agora.mccex_example.utils.LogUtils
import io.agora.mediaplayer.Constants
import io.agora.mediaplayer.IMediaPlayerObserver
import io.agora.mediaplayer.data.PlayerUpdatedInfo
import io.agora.mediaplayer.data.SrcInfo
import io.agora.rtc2.IAudioFrameObserver
import io.agora.rtc2.RtcEngine

object MccExManager : IMusicContentCenterExEventHandler, IMusicContentCenterExScoreEventHandler {
    private var mMccExService: IMusicContentCenterEx? = null
    private var mCallback: MccExCallback? = null
    private var mMusicPlayer: IMusicPlayer? = null

    private var mMusicPlayMode = MusicPlayMode.MUSIC_PLAY_MODE_ORIGINAL

    private var mStatus = Status.IDLE

    internal enum class Status(var value: Int) {
        IDLE(0),
        Opened(1),
        Started(2),
        Paused(3),
        Stopped(4);

        fun isAtLeast(state: Status): Boolean {
            return compareTo(state) >= 0
        }
    }

    private val mMediaPlayerObserver: IMediaPlayerObserver = object : IMediaPlayerObserver {
        override fun onPlayerStateChanged(
            state: Constants.MediaPlayerState,
            error: Constants.MediaPlayerError
        ) {
            LogUtils.d("onPlayerStateChanged: $state $error")

            if (Constants.MediaPlayerState.PLAYER_STATE_OPEN_COMPLETED == state) {
                if (mStatus == Status.IDLE) {
                    onMusicOpenCompleted()
                }
            }
            if (Constants.MediaPlayerState.PLAYER_STATE_PLAYING == state) {
                onMusicPlaying()
            } else if (Constants.MediaPlayerState.PLAYER_STATE_PAUSED == state) {
                onMusicPause()
            } else if (Constants.MediaPlayerState.PLAYER_STATE_STOPPED == state) {
                onMusicStop()
            } else if (Constants.MediaPlayerState.PLAYER_STATE_PLAYBACK_ALL_LOOPS_COMPLETED == state) {
                onMusicCompleted()
            } else if (Constants.MediaPlayerState.PLAYER_STATE_FAILED == state) {
                onMusicOpenError(error.ordinal)
            }
        }

        override fun onPositionChanged(positionMs: Long, timestampMs: Long) {

        }

        override fun onPlayerEvent(
            eventCode: Constants.MediaPlayerEvent?,
            elapsedTime: Long,
            message: String?
        ) {
        }

        override fun onMetaData(type: Constants.MediaPlayerMetadataType, data: ByteArray) {}
        override fun onPlayBufferUpdated(playCachedBuffer: Long) {}
        override fun onPreloadEvent(src: String, event: Constants.MediaPlayerPreloadEvent) {}
        override fun onAgoraCDNTokenWillExpire() {}
        override fun onPlayerSrcInfoChanged(from: SrcInfo, to: SrcInfo) {}
        override fun onPlayerInfoUpdated(info: PlayerUpdatedInfo) {}
        override fun onAudioVolumeIndication(volume: Int) {}
    }

    fun initMccExService(
        rtcEngine: RtcEngine,
        audioFrameObserver: IAudioFrameObserver,
        context: Context,
        callback: MccExCallback
    ) {
        LogUtils.d("MccEx sdk version = ${IMusicContentCenterEx.getSdkVersion()}")
        mCallback = callback
        mMccExService = IMusicContentCenterEx.create(rtcEngine)
        val configuration = MusicContentCenterExConfiguration()
        configuration.context = context
        configuration.vendorConfigure = YsdVendorConfigure(
            BuildConfig.YSD_APP_ID,
            BuildConfig.YSD_APP_KEY,
            BuildConfig.YSD_APP_TOKEN,
            BuildConfig.YSD_APP_UID,
            Utils.getUuid(),
            ChargeMode.ONCE,
            60 * 15
        )
        configuration.eventHandler = this
        configuration.scoreEventHandler = this
        configuration.audioFrameObserver = audioFrameObserver
        configuration.enableLog = true
        configuration.enableSaveLogToFile = true
        configuration.logFilePath = context.getExternalFilesDir(null)!!.path
        mMccExService?.initialize(configuration)
    }

    fun getMccExService(): IMusicContentCenterEx? {
        return mMccExService
    }

    fun destroy() {
        mMusicPlayer?.unRegisterPlayerObserver(mMediaPlayerObserver)
        mMusicPlayer?.let { mMccExService?.destroyMusicPlayer(it) ?: "" }
        IMusicContentCenterEx.destroy()
        mMccExService = null
    }

    override fun onInitializeResult(state: MccExState, reason: MccExStateReason) {
        LogUtils.d("onInitializeResult: state = $state, reason = $reason")
        mMusicPlayer = mMccExService?.createMusicPlayer()
        mMusicPlayer?.registerPlayerObserver(mMediaPlayerObserver)
        mCallback?.onInitializeResult(state, reason)
    }

    override fun onStartScoreResult(songCode: Long, state: MccExState, reason: MccExStateReason) {
        LogUtils.d("onStartScoreResult: songCode = $songCode, state = $state, reason = $reason")
        if (state == MccExState.START_SCORE_STATE_COMPLETED) {
            mMccExService?.setScoreLevel(ScoreHardLevel.LEVEL5)
        }
        openMusic(songCode)
        mCallback?.onStartScoreResult(songCode, state, reason)
    }


    override fun onPreLoadEvent(
        requestId: String,
        songCode: Long,
        percent: Int,
        lyricPath: String,
        pitchPath: String,
        songOffsetBegin: Int,
        songOffsetEnd: Int,
        lyricOffset: Int,
        state: MccExState,
        reason: MccExStateReason
    ) {
        LogUtils.d("onPreLoadEvent: requestId = $requestId, songCode = $songCode, percent = $percent, lyricPath = $lyricPath, pitchPath = $pitchPath, songOffsetBegin = $songOffsetBegin, songOffsetEnd = $songOffsetEnd, lyricOffset = $lyricOffset, state = $state, reason = $reason")
        if (state == MccExState.PRELOAD_STATE_COMPLETED && percent == 100) {
            mMccExService?.startScore(songCode)
        }
        mCallback?.onPreLoadEvent(
            requestId,
            songCode,
            percent,
            lyricPath,
            pitchPath,
            songOffsetBegin,
            songOffsetEnd,
            lyricOffset,
            state,
            reason
        )
    }

    override fun onLyricResult(
        requestId: String,
        songCode: Long,
        lyricPath: String,
        songOffsetBegin: Int,
        songOffsetEnd: Int,
        lyricOffset: Int,
        reason: MccExStateReason
    ) {
        LogUtils.d("onLyricResult: requestId = $requestId, songCode = $songCode, lyricPath = $lyricPath, songOffsetBegin = $songOffsetBegin, songOffsetEnd = $songOffsetEnd, lyricOffset = $lyricOffset, reason = $reason")
        mCallback?.onLyricResult(
            requestId,
            songCode,
            lyricPath,
            songOffsetBegin,
            songOffsetEnd,
            lyricOffset,
            reason
        )
    }

    override fun onPitchResult(
        requestId: String,
        songCode: Long,
        pitchPath: String,
        songOffsetBegin: Int,
        songOffsetEnd: Int,
        reason: MccExStateReason
    ) {
        LogUtils.d("onPitchResult: requestId = $requestId, songCode = $songCode, pitchPath = $pitchPath, songOffsetBegin = $songOffsetBegin, songOffsetEnd = $songOffsetEnd, reason = $reason")
        mCallback?.onPitchResult(
            requestId,
            songCode,
            pitchPath,
            songOffsetBegin,
            songOffsetEnd,
            reason
        )
    }


    override fun onPitch(songCode: Long, data: RawScoreData) {
        LogUtils.d("onPitch: songCode = $songCode, data = $data")
        mCallback?.onPitch(songCode, data)
    }

    override fun onLineScore(songCode: Long, value: LineScoreData) {
        LogUtils.d("onLineScore: songCode = $songCode, value = $value")
        mCallback?.onLineScore(songCode, value)
    }


    private fun onMusicOpenCompleted() {
        LogUtils.d("onMusicOpenCompleted")
        mMusicPlayer?.play()

        mStatus = Status.Opened
        mCallback?.onPlayStateChange()
    }

    private fun onMusicPlaying() {
        LogUtils.d("onMusicPlaying")
        mStatus = Status.Started
        mCallback?.onPlayStateChange()
    }

    private fun onMusicPause() {
        LogUtils.d("onMusicPause")
        mStatus = Status.Paused
        mCallback?.onPlayStateChange()
    }

    private fun onMusicStop() {
        LogUtils.d("onMusicStop")
        if (mStatus != Status.IDLE) {
            mStatus = Status.Stopped
        }

        reset()
        mCallback?.onPlayStateChange()
    }

    private fun onMusicCompleted() {
        LogUtils.d("onMusicCompleted")
        reset()
        mCallback?.onPlayStateChange()
    }

    private fun onMusicOpenError(error: Int) {
        LogUtils.d("onMusicOpenError: $error")
        mStatus = Status.IDLE
        mCallback?.onPlayStateChange()
    }

    private fun openMusic(songCode: Long) {
        LogUtils.d("openMusic() called songCode=$songCode")
        mMusicPlayer?.setPlayMode(mMusicPlayMode)
        mMusicPlayer?.open(songCode, 0)
    }

    fun stop() {
        LogUtils.d("stop() called")
        if (mStatus == Status.IDLE) {
            return
        }
        mStatus = Status.IDLE
        mMusicPlayer?.stop()
    }

    fun pause() {
        LogUtils.d("pause() called")

        if (!mStatus.isAtLeast(Status.Started)) {
            return
        }
        mStatus = Status.Paused
        mMusicPlayer?.pause()
    }

    fun resume() {
        LogUtils.d("resume() called")

        if (!mStatus.isAtLeast(Status.Started)) {
            return
        }
        mStatus = Status.Started
        mMusicPlayer?.resume()
    }

    fun seek(time: Long) {
        mMusicPlayer?.seek(time)
    }

    fun preloadMusic(songId: String, jsonOption: String) {
        LogUtils.d("preloadMusic() called")
        try {
            val songCode = mMccExService?.getInternalSongCode(songId, jsonOption) ?: 0L
            if (songCode == 0L) {
                LogUtils.e("getInternalSongCode failed songId=$songId")
                return
            }
            if (0 == mMccExService?.isPreloaded(songCode)) {
                LogUtils.d("mcc is preloaded songCode=$songCode")
                mMccExService?.startScore(songCode)
            } else {
                val requestId = mMccExService?.preload(songCode) ?: ""
                LogUtils.d("preload requestId=$requestId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun reset() {
        mStatus = Status.IDLE
    }

    fun setPlayMode(playMode: MusicPlayMode) {
        mMusicPlayMode = playMode
        mMusicPlayer?.setPlayMode(playMode)
    }

    fun isMusicPlaying(): Boolean {
        return mStatus == Status.Started
    }

    fun isMusicPause(): Boolean {
        return mStatus == Status.Paused
    }

    fun isPlayOriginal(): Boolean {
        return mMusicPlayMode == MusicPlayMode.MUSIC_PLAY_MODE_ORIGINAL
    }

    fun getInternalSongCode(songId: String, jsonOption: String?): Long {
        return mMccExService?.getInternalSongCode(songId, jsonOption) ?: 0L
    }

    fun isPreloaded(songCode: Long): Int {
        return mMccExService?.isPreloaded(songCode) ?: -1
    }

    fun preload(songCode: Long): String? {
        return mMccExService?.preload(songCode)
    }

    fun getLyric(songCode: Long, lyricType: LyricType): String {
        return mMccExService?.getLyric(songCode, lyricType) ?: ""
    }

    fun getPitch(songCode: Long): String {
        return mMccExService?.getPitch(songCode) ?: ""
    }

    fun startScore(songCode: Long): Int {
        return mMccExService?.startScore(songCode) ?: -1
    }

    fun setScoreLevel(level: ScoreHardLevel): Int {
        return mMccExService?.setScoreLevel(level) ?: -1
    }

    fun pauseScore(): Int {
        return mMccExService?.pauseScore() ?: -1
    }

    fun resumeScore(): Int {
        return mMccExService?.resumeScore() ?: -1
    }

    fun createMusicPlayer(): IMusicPlayer? {
        return mMccExService?.createMusicPlayer()
    }

    fun hasInitialized(): Boolean {
        return mMccExService != null
    }


    interface MccExCallback {
        fun onInitializeResult(state: MccExState, reason: MccExStateReason) {

        }

        fun onStartScoreResult(songCode: Long, state: MccExState, reason: MccExStateReason) {

        }

        fun onPreLoadEvent(
            requestId: String,
            songCode: Long,
            percent: Int,
            lyricPath: String,
            pitchPath: String,
            songOffsetBegin: Int,
            songOffsetEnd: Int,
            lyricOffset: Int,
            state: MccExState,
            reason: MccExStateReason
        ) {

        }

        fun onLyricResult(
            requestId: String,
            songCode: Long,
            lyricPath: String,
            songOffsetBegin: Int,
            songOffsetEnd: Int,
            lyricOffset: Int,
            reason: MccExStateReason
        ) {

        }

        fun onPitchResult(
            requestId: String,
            songCode: Long,
            pitchPath: String,
            songOffsetBegin: Int,
            songOffsetEnd: Int,
            reason: MccExStateReason
        ) {
        }

        fun onPlayStateChange() {

        }

        fun onPitch(songCode: Long, data: RawScoreData) {

        }

        fun onLineScore(songCode: Long, value: LineScoreData) {

        }
    }

}