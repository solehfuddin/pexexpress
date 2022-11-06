package com.pex.pex_courier.helper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import com.pex.pex_courier.ui.LoggerActivity
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess


class ForceCloseHandler(val context: Context?) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        val trace = StringWriter()
        e.printStackTrace(PrintWriter(trace))

        val sb = StringBuilder()
        sb.append("************ CAUSE OF ERROR ************\n\n")
        sb.append(trace.toString())

        sb.append("\n************ DEVICE INFORMATION ***********\n")
        sb.append("Brand: ")
        sb.append(Build.BRAND)

        val separator = "\n"
        sb.append(separator)
        sb.append("Device: ")
        sb.append(Build.DEVICE)
        sb.append(separator)
        sb.append("Model: ")
        sb.append(Build.MODEL)
        sb.append(separator)
        sb.append("Id: ")
        sb.append(Build.ID)
        sb.append(separator)
        sb.append("Product: ")
        sb.append(Build.PRODUCT)
        sb.append(separator)
        sb.append("\n************ FIRMWARE ************\n")
        sb.append("SDK: ")
        sb.append(Build.VERSION.SDK_INT)
        sb.append(separator)
        sb.append("Release: ")
        sb.append(Build.VERSION.RELEASE)
        sb.append(separator)
        sb.append("Incremental: ")
        sb.append(Build.VERSION.INCREMENTAL)
        sb.append(separator)

        val intent = Intent(context, LoggerActivity::class.java)
        intent.putExtra("error", sb.toString())
        context?.startActivity(intent)

        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}