//
//  ViewController.swift
//  AgoraMccExServiceExample
//
//  Created by ZhouRui on 2024/6/3.
//

import UIKit
import AgoraMccExService

class ViewController: UIViewController {
    
    private let mccManager = MCCManager()
    private let songId = 40289835
    var songCode: Int?
    let logTag = "TestVC"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        mccManager.delegate = self
        mccManager.initRtcEngine()
        mccManager.joinChannel()
        
        if let token = Config.token, let userId = Config.userId {
            mccManager.initMccEx(pid: Config.pid,
                                 pKey: Config.pKey,
                                 token: token,
                                 userId: userId)
        }
    }
    
    deinit {
        Log.info(text: "deinit", tag: logTag)
    }
}

// MARK: - RTCManagerDelegate
extension ViewController: MCCManagerDelegate {
    func onMccExInitialize(_ manager: MCCManager) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else {
                return
            }
            let songCode = mccManager.getInternalSongCode(songId: songId)
            mccManager.createMusicPlayer()
            mccManager.preload(songId: songCode)
        }
    }
    
    func onProloadMusic(_ manager: MCCManager, songId: Int, lyricData: Data, pitchData: Data) {
        DispatchQueue.main.async {
            manager.setScoreLevel(level: .level5)
            manager.startScore(songId: songId)
        }
    }
    
    func onMccExScoreStart(_ manager: MCCManager) {
        let songCode = mccManager.getInternalSongCode(songId: songId)
        manager.open(songId: songCode)
    }
    
    func onOpenMusic(_ manager: MCCManager) {
        DispatchQueue.main.async {
            manager.playMusic()
        }
    }
    
    func onPitch(_ songCode: Int, data: AgoraRawScoreData) {
        // handle your logic
        Log.debug(text: "onPitch", tag: logTag)
    }

    func onLineScore(_ songCode: Int, value: AgoraLineScoreData) {
        // handle your logic
        Log.debug(text: "onLineScore", tag: logTag)
    }
}

