package cn.migu.utils

import sun.management.VMManagement
import java.lang.management.ManagementFactory

object JvmUtils {

    fun jvmPid(): Int {
        try {
            val runtime = ManagementFactory.getRuntimeMXBean()
            val jvm = runtime.javaClass.getDeclaredField("jvm")
            jvm.isAccessible = true
            val mgmt = jvm.get(runtime) as VMManagement
            val pidMethod = mgmt.javaClass.getDeclaredMethod("getProcessId")
            pidMethod.isAccessible = true
            return pidMethod.invoke(mgmt) as Int
        } catch (e: Exception) {
            return -1
        }

    }
}