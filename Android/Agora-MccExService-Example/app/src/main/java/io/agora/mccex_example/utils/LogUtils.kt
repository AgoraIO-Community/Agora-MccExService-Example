package io.agora.mccex_example.utils

import android.content.Context
import android.util.Log
import io.agora.logging.ConsoleLogger
import io.agora.logging.FileLogger
import io.agora.logging.LogManager
import io.agora.logging.Logger
import io.agora.mccex_example.constants.Constants

object LogUtils {
    private val LOGGERS: MutableList<Logger> = ArrayList(3)

    fun enableLog(context: Context, enableLog: Boolean, saveLogFile: Boolean) {
        try {
            val logFilePath = context.getExternalFilesDir(null)!!.path

            destroy()

            if (enableLog) {
                LOGGERS.add(ConsoleLogger())
            }

            if (saveLogFile) {
                val supportTags = mutableListOf(
                    Constants.TAG
                )
                LOGGERS.add(
                    FileLogger(
                        logFilePath,
                        Constants.LOG_FILE_NAME,
                        (1024 * 1024).toLong(),
                        3,
                        supportTags
                    )
                )
            }

            for (logger in LOGGERS) {
                LogManager.instance().addLogger(logger)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(Constants.TAG, "initLog error:" + e.message)
        }
    }

    fun destroy() {
        for (logger in LOGGERS) {
            LogManager.instance().removeLogger(logger)
        }
        LOGGERS.clear()
    }

    fun d(msg: String?) {
        LogManager.instance().debug(Constants.TAG, msg)
    }

    fun d(tag: String, msg: String?) {
        LogManager.instance().debug(tag, msg)
    }

    fun e(msg: String?) {
        LogManager.instance().error(Constants.TAG, msg)
    }

    fun e(tag: String, msg: String?) {
        LogManager.instance().error(tag, msg)
    }

    fun i(msg: String?) {
        LogManager.instance().info(Constants.TAG, msg)
    }

    fun i(tag: String, msg: String?) {
        LogManager.instance().info(tag, msg)
    }
}
