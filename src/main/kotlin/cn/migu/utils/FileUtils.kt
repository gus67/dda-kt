package cn.migu.utils

import java.io.File

object FileUtils {

    //遍历文件数并过滤
    fun traverseFileTree() {
        val systemDir = File("/Users/guxichang/monitor")
        val fileTree: FileTreeWalk = systemDir.walk()
        fileTree.maxDepth(10)
                .filter { it.isFile }
                .filter { it.extension != "COMPLETE" }
                .forEach(::println)
    }

    /**
     * 插件化转换
     * "0" 失败
     * "1" 成功
     */
    fun reflectPlugin(clazzName: String, methodName: String, filePath: String): String {
        val clz = Class.forName(clazzName)
        val o = clz.newInstance()
        val m = clz.getMethod(methodName, Int::class.javaPrimitiveType, String::class.java)
        return m.invoke(o, filePath, filePath + ".TRANSITION").toString()
    }
}