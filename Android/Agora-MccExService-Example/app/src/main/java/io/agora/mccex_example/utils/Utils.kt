package io.agora.mccex_example.utils

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.UUID

class Utils {
    companion object {
        fun getRandomString(length: Int): String {
            val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            val random = Random()
            val sb = StringBuffer()
            for (i in 0 until length) {
                val number = random.nextInt(62)
                sb.append(str[number])
            }
            return sb.toString()
        }

        fun readAssetJsonArray(context: Context, fileName: String): String {
            val content: String

            try {
                val assetManager = context.assets
                val inputStream: InputStream = assetManager.open(fileName)
                val inputStreamReader = InputStreamReader(inputStream)
                content = inputStreamReader.readText()
                inputStreamReader.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

            return content
        }

        fun hideKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun toDp(value: Int): Int {
            val density = Resources.getSystem().displayMetrics.density
            return (value * density).toInt()
        }

        fun getUuidId(): String {
            return UUID.randomUUID().toString().replace("-", "")
        }

        fun addChineseQuotes(str: String): String {
            return "“$str”"
        }

        fun removeChineseQuotes(str: String): String {
            return if (str.isNotEmpty()) {
                if (str.startsWith("“") && str.endsWith("”")) {
                    str.substring(1, str.length - 1)
                } else if (str.startsWith("“") && !str.endsWith("”")) {
                    str.substring(1, str.length)
                } else if (!str.startsWith("“") && str.endsWith("”")) {
                    str.substring(0, str.length - 1)
                } else if (str.startsWith("\"") && str.endsWith("\"")) {
                    str.substring(1, str.length - 1)
                } else if (str.startsWith("\"") && !str.endsWith("\"")) {
                    str.substring(1, str.length)
                } else if (!str.startsWith("\"") && str.endsWith("\"")) {
                    str.substring(0, str.length - 1)
                } else {
                    str
                }
            } else {
                str
            }
        }

        fun getCurrentDateStr(pattern: String): String {
            return SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
        }

        fun isNetworkConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        fun onlyChinese(str: String): Boolean {
            val regex = Regex("[^\\p{InCJKUnifiedIdeographs}\\p{P}0-9]+")
            val result = regex.replace(str, "")
            return result.length == str.length
        }

        fun saveTextToFile(text: String, filePath: String, isAppend: Boolean) {
            try {
                val file = File(filePath)
                if (!file.exists()) {
                    file.createNewFile()
                }
                try {
                    BufferedWriter(FileWriter(filePath, isAppend)).use { writer ->
                        writer.write(
                            text
                        )
                    }
                } catch (e: Exception) {
                    LogUtils.d("saveTextToFile: " + e.message)
                }
            } catch (e: Exception) {
                LogUtils.e("saveTextToFile: " + e.message)
            }
        }
    }
}