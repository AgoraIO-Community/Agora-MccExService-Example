package io.agora.mccex_example.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupStatus
import com.lxj.xpopup.impl.LoadingPopupView
import io.agora.mccex.constants.MccExState
import io.agora.mccex.constants.MccExStateReason
import io.agora.mccex.constants.MusicPlayMode
import io.agora.mccex_example.R
import io.agora.mccex_example.agora.MccExManager
import io.agora.mccex_example.agora.RtcManager
import io.agora.mccex_example.constants.Constants
import io.agora.mccex_example.databinding.ActivityMainBinding
import io.agora.mccex_example.model.SongInfo
import io.agora.mccex_example.utils.LogUtils
import io.agora.mccex_example.utils.ToastUtils
import io.agora.mccex_example.utils.Utils
import io.agora.rtc2.IRtcEngineEventHandler
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), MccExManager.MccExCallback, RtcManager.RtcCallback {
    private val TAG: String = Constants.TAG + "-MainActivity"
    private lateinit var binding: ActivityMainBinding
    private val MY_PERMISSIONS_REQUEST_CODE = 123
    private var mLoadingPopup: LoadingPopupView? = null
    private var mJoinSuccess = false
    private val mSongCacheList: MutableList<SongInfo> = mutableListOf()

    init {
        mSongCacheList.add(
            SongInfo(
                "40289835",
                "",
                "十年",
                "陈奕迅"
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
        initData()
        initView()
    }

    override fun onResume() {
        super.onResume()
        isNetworkConnected();
    }

    private fun checkPermissions() {
        val permissions =
            arrayOf(Manifest.permission.RECORD_AUDIO)
        if (EasyPermissions.hasPermissions(this, *permissions)) {
            // 已经获取到权限，执行相应的操作
        } else {
            EasyPermissions.requestPermissions(
                this,
                "需要录音权限",
                MY_PERMISSIONS_REQUEST_CODE,
                *permissions
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // 权限被授予，执行相应的操作
        LogUtils.d(TAG, "onPermissionsGranted requestCode:$requestCode perms:$perms")
    }

    fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        LogUtils.d(TAG, "onPermissionsDenied requestCode:$requestCode perms:$perms")
        // 权限被拒绝，显示一个提示信息
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // 如果权限被永久拒绝，可以显示一个对话框引导用户去应用设置页面手动授权
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun initData() {
        mLoadingPopup = XPopup.Builder(this@MainActivity)
            .hasBlurBg(true)
            .asLoading("正在加载中")
    }

    private fun initView() {
        handleOnBackPressed()
        enableView(false)

        val versionName = applicationContext.packageManager.getPackageInfo(
            applicationContext.packageName,
            0
        ).versionName

        binding.versionTv.text = "Demo Version: ${versionName}"

        binding.playBtn.setOnClickListener {
            if (MccExManager.isMusicPlaying()) {
                MccExManager.pause()
            } else if (MccExManager.isMusicPause()) {
                MccExManager.resume()
            } else {
                val randomIndex = mSongCacheList.indices.random()
                val songInfo = mSongCacheList[randomIndex]
                LogUtils.i("preloadMusic songInfo: $songInfo ")
                MccExManager.preloadMusic(songInfo.songId, songInfo.optionJson)
                mLoadingPopup?.show()
            }
        }

        binding.stopBtn.setOnClickListener {
            MccExManager.stop()
        }

        binding.playModeBtn.setOnClickListener {
            if (MccExManager.isPlayOriginal()) {
                MccExManager.setPlayMode(MusicPlayMode.MUSIC_PLAY_MODE_ACCOMPANY)
            } else {
                MccExManager.setPlayMode(MusicPlayMode.MUSIC_PLAY_MODE_ORIGINAL)
            }
            updateView()
        }

        binding.joinRoomBtn.setOnClickListener {
            if (!mJoinSuccess) {
                joinRoom()
            } else {
                leaveRoom()
            }
        }

        updateView();
    }

    private fun updateView() {
        if (MccExManager.isMusicPlaying()) {
            binding.playBtn.text = resources.getString(R.string.pause)
            binding.stopBtn.isEnabled = true
        } else if (MccExManager.isMusicPause()) {
            binding.playBtn.text = resources.getString(R.string.play)
            binding.stopBtn.isEnabled = true
        } else {
            binding.playBtn.text = resources.getString(R.string.play)
            binding.stopBtn.isEnabled = false
        }

        if (MccExManager.isPlayOriginal()) {
            binding.playModeBtn.text = resources.getString(R.string.play_accompany)
        } else {
            binding.playModeBtn.text = resources.getString(R.string.play_original)
        }
    }

    private fun enableView(enable: Boolean) {
        binding.playBtn.isEnabled = enable
        binding.stopBtn.isEnabled = enable
        binding.playModeBtn.isEnabled = enable

    }

    private fun handleOnBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val xPopup = XPopup.Builder(this@MainActivity)
                    .asConfirm("退出", "确认退出程序", {
                        exit()
                    }, {})
                xPopup.show()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun isNetworkConnected(): Boolean {
        val isConnect = Utils.isNetworkConnected(this)
        if (!isConnect) {
            LogUtils.d("Network is not connected")
            ToastUtils.showLongToast(this, "请连接网络!")
        }
        return isConnect
    }

    private fun exit() {
        LogUtils.destroy()
        MccExManager.destroy()
        finishAffinity()
        finish()
        exitProcess(0)
    }

    private fun joinRoom() {
        RtcManager.initRtcEngine(this, this)
    }

    private fun leaveRoom() {
        if (MccExManager.isMusicPlaying()) {
            MccExManager.stop()
            updateView()
        }
        MccExManager.destroy()
        RtcManager.leaveChannel()
    }

    override fun onInitializeResult(state: MccExState, reason: MccExStateReason) {
        runOnUiThread {
            if (MccExState.INITIALIZE_STATE_COMPLETED != state) {
                ToastUtils.showLongToast(this, "加入房间失败")
                enableView(false)
            } else {
                mJoinSuccess = true
                ToastUtils.showLongToast(this, "加入房间成功")
                enableView(true)
                binding.joinRoomBtn.text = resources.getString(R.string.leave)
            }
        }

    }

    override fun onPlayStateChange() {
        super.onPlayStateChange()
        runOnUiThread {
            if (((mLoadingPopup?.isShow == true) || (PopupStatus.Showing == mLoadingPopup?.popupStatus)) && MccExManager.isMusicPlaying()) {
                mLoadingPopup?.dismiss()
            }
            updateView()
        }
    }

    override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
        RtcManager.getRtcEngine()?.let {
            MccExManager.initMccExService(
                it,
                RtcManager,
                applicationContext,
                this
            )
        }
    }

    override fun onLeaveChannel(stats: IRtcEngineEventHandler.RtcStats) {
        mJoinSuccess = false
        runOnUiThread {
            ToastUtils.showLongToast(this, "离开房间成功")
            enableView(false)
            binding.joinRoomBtn.text = resources.getString(R.string.join)
        }
    }

}





