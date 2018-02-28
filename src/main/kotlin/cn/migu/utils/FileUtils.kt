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
}