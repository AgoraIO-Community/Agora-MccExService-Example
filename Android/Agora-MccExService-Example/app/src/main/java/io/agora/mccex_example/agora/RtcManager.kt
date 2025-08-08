package io.agora.mccex_example.agora

import android.content.Context
import io.agora.mccex_example.utils.KeyCenter
import io.agora.mccex_example.utils.LogUtils
import io.agora.mccex_example.utils.Utils
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IAudioFrameObserver
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.audio.AudioParams
import java.io.FileOutputStream
import java.nio.ByteBuffer

object RtcManager : IAudioFrameObserver {
    private var mRtcEngine: RtcEngine? = null
    private var mCallback: RtcCallback? = null
    private var mChannelId: String = ""
    private var mTime: String = ""
    private val SAVE_AUDIO_RECORD_PCM = false


    fun initRtcEngine(context: Context, rtcCallback: RtcCallback) {
        mCallback = rtcCallback
        try {
            LogUtils.d("RtcEngine version:" + RtcEngine.getSdkVersion())
            val rtcEngineConfig = RtcEngineConfig()
            rtcEngineConfig.mContext = context
            rtcEngineConfig.mAppId = KeyCenter.APP_ID
            rtcEngineConfig.mChannelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
            rtcEngineConfig.mEventHandler = object : IRtcEngineEventHandler() {
                override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
                    LogUtils.d("onJoinChannelSuccess channel:$channel uid:$uid elapsed:$elapsed")
                    mCallback?.onJoinChannelSuccess(channel, uid, elapsed)
                }

                override fun onLeaveChannel(stats: RtcStats) {
                    LogUtils.d("onLeaveChannel")
                    mCallback?.onLeaveChannel(stats)
                }
            }
            rtcEngineConfig.mAudioScenario = Constants.AUDIO_SCENARIO_CHORUS
            mRtcEngine = RtcEngine.create(rtcEngineConfig)

            mRtcEngine?.setParameters("{\"rtc.enable_debug_log\":true}")

            mRtcEngine?.enableAudio()

            mRtcEngine?.setDefaultAudioRoutetoSpeakerphone(true)


            mChannelId = Utils.getCurrentDateStr("yyyyMMddHHmmss") + Utils.getRandomString(2)
            val ret = mRtcEngine?.joinChannel(
                KeyCenter.getRtcToken(
                    mChannelId,
                    KeyCenter.getUid()
                ),
                mChannelId,
                KeyCenter.getUid(),
                object : ChannelMediaOptions() {
                    init {
                        publishMicrophoneTrack = true
                        autoSubscribeAudio = true
                        clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
                    }
                })
            LogUtils.d("initRtcEngine ret:$ret")
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.e("initRtcEngine error:" + e.message)
        }
    }

    override fun onRecordAudioFrame(
        channelId: String?,
        type: Int,
        samplesPerChannel: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int,
        buffer: ByteBuffer?,
        renderTimeMs: Long,
        avsync_type: Int
    ): Boolean {
        val length = buffer!!.remaining()
        val origin = ByteArray(length)
        buffer[origin]
        buffer.flip()
        if (SAVE_AUDIO_RECORD_PCM) {
            try {
                val fos = FileOutputStream(
                    "/sdcard/Android/Data/io.agora.mccex_demo/cache/audio_" + mTime + ".pcm",
                    true
                )
                fos.write(origin)
                fos.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        return true
    }

    override fun onPlaybackAudioFrame(
        channelId: String?,
        type: Int,
        samplesPerChannel: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int,
        buffer: ByteBuffer?,
        renderTimeMs: Long,
        avsync_type: Int
    ): Boolean {

        return true
    }

    override fun onMixedAudioFrame(
        channelId: String?,
        type: Int,
        samplesPerChannel: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int,
        buffer: ByteBuffer?,
        renderTimeMs: Long,
        avsync_type: Int
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onEarMonitoringAudioFrame(
        type: Int,
        samplesPerChannel: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int,
        buffer: ByteBuffer?,
        renderTimeMs: Long,
        avsync_type: Int
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onPlaybackAudioFrameBeforeMixing(
        channelId: String?,
        uid: Int,
        type: Int,
        samplesPerChannel: Int,
        bytesPerSample: Int,
        channels: Int,
        samplesPerSec: Int,
        buffer: ByteBuffer?,
        renderTimeMs: Long,
        avsync_type: Int,
        rtpTimestamp: Int
    ): Boolean {
        TODO("Not yet implemented")
    }


    override fun getObservedAudioFramePosition(): Int {
        TODO("Not yet implemented")
    }

    override fun getRecordAudioParams(): AudioParams {
        TODO("Not yet implemented")
    }

    override fun getPlaybackAudioParams(): AudioParams {
        TODO("Not yet implemented")
    }

    override fun getMixedAudioParams(): AudioParams {
        TODO("Not yet implemented")
    }

    override fun getEarMonitoringAudioParams(): AudioParams {
        TODO("Not yet implemented")
    }


    fun updatePublishMediaPlayerOption(playerId: Int) {
        val options = ChannelMediaOptions()
        options.publishMediaPlayerId = playerId
        options.publishMediaPlayerAudioTrack = true
        mRtcEngine?.updateChannelMediaOptions(options)
    }


    fun leaveChannel() {
        LogUtils.d("RtcManager leaveChannel")
        mRtcEngine?.registerAudioFrameObserver(null)
        mRtcEngine?.leaveChannel()
    }

    fun destroy() {
        LogUtils.d("RtcManager destroy")
        RtcEngine.destroy()
    }


    fun getChannelId(): String {
        return mChannelId
    }

    fun getRtcEngine(): RtcEngine? {
        return mRtcEngine
    }

    interface RtcCallback {
        fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int)
        fun onLeaveChannel(stats: IRtcEngineEventHandler.RtcStats)
    }
}
