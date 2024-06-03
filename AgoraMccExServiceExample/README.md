# AgoraMccExServiceExample

这是一个使用 Agora MCC (Music Content Center) 服务的简单示例应用。该应用展示了如何初始化 RTC 引擎、加入频道、初始化 MCC 服务、预加载音乐、设置评分等级并开始评分。

## 目录

- [先决条件](#先决条件)
- [安装](#安装)
- [使用](#使用)
- [功能](#功能)
- [代码说明](#代码说明)
- [授权](#授权)

## 先决条件

在运行此示例之前，请确保您已经满足以下条件：

1. 已安装 Xcode。
2. 有一个有效的 Agora 开发者账号，以及 YSD 账号信息。
3. 已创建 Agora 项目，并获取了以下信息：
   - `App ID`
   - `App Certificate`
   - `YSD Token`
   - `YSD User ID`
   - `YSD AppID (pid)`
   - `YSD AppKey (pKey)`

## 安装

1. 克隆此存储库到本地：

    ```bash
    git clone https://github.com/yourusername/MCCManagerDemo.git
    cd AgoraMccExServiceExample
    ```

2. 使用 CocoaPods 安装依赖项：

    ```bash
    pod install
    ```

3. 打开 Xcode 项目：

    ```bash
    open AgoraMccExServiceExample.xcworkspace
    ```

## 使用

1. 在 `Config.swift` 文件中填入您的 Agora 项目信息：

    ```swift
    struct Config {
        static let pid: String = "Your Project ID"
        static let pKey: String = "Your Project Key"
        static let token: String? = "Your Token"
        static let userId: String? = "Your User ID"
    }
    ```

2. 运行应用。

## 功能

- 初始化 RTC 引擎
- 加入频道
- 初始化 MCC 服务
- 获取内部歌曲代码
- 创建音乐播放器
- 预加载音乐
- 设置评分等级
- 开始评分
- 播放音乐
- 处理评分数据
