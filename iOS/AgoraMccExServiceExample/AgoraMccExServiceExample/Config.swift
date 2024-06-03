//
//  Config.swift
//  Demo
//
//  Created by ZYP on 2022/12/23.
//

import Foundation

struct Config {
    /// ignore
    static let channelId = "MccExServiceExample"
    static let hostUid: UInt = 1
    static let audioUid: UInt = 2
    static let playerUid: Int = 100
    static let mccUid: Int = 333
    
    /// agora important vars
    static let rtcAppId = <#请填写声网rtcAppId#>
    static let rtcCertificate = <#请填写声网rtcCertificate#>
    
    /// ysd important vars
    static let pid: String = <#请填写YSDAppID#>
    static let pKey: String = <#请填写YSDAppKey#>
    static let token: String? = <#请填写YSDToken#>
    static let userId: String? = <#请填写YSDUserID#>
}
