//
//  Log.swift
//  Demo
//
//  Created by ZYP on 2024/5/11.
//

import Foundation
import AgoraComponetLog

class Log {
    static let shared = Log()
    var componetLog: AgoraComponetLog!
    
    static func setupLogger() {
        let fileLogger = AgoraComponetFileLogger(logFilePath: nil,
                                                 filePrefixName: "AgoraMccExServiceExample",
                                                 maxFileSizeOfBytes: 1 * 1024 * 1024,
                                                 maxFileCount: 5,
                                                 domainName: "AMESE")
        let consoleLogger = AgoraComponetConsoleLogger(domainName: "AMESE")
        shared.componetLog = AgoraComponetLog(queueTag: "AgoraMccExServiceExample")
        shared.componetLog.configLoggers([fileLogger, consoleLogger])
    }
    
    static func errorText(text: String,
                          tag: String? = nil) {
        shared.componetLog.error(withText: text, tag: tag)
    }
    
    static func error(error: CustomStringConvertible,
                      tag: String? = nil) {
        shared.componetLog.error(withText: error.description, tag: tag)
    }
    
    static func info(text: String,
                     tag: String? = nil) {
        shared.componetLog.info(withText: text, tag: tag)
    }
    
    static func debug(text: String,
                      tag: String? = nil) {
        shared.componetLog.debug(withText: text, tag: tag)
    }
    
    static func warning(text: String,
                        tag: String? = nil) {
        shared.componetLog.warning(withText: text, tag: tag)
    }
}
