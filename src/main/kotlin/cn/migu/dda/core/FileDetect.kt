package cn.migu.dda.core

import cn.migu.dda.vo.DDAFile
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor
import org.slf4j.LoggerFactory
import java.io.File

class FileDetect : FileAlterationListenerAdaptor() {

    val log = LoggerFactory.getLogger(FileDetect::class.java)!!

    /**
     * 监控逻辑
     * 1、发现了新文件
     * 2、文件后缀过滤
     * 3、文件正则过滤
     * 4、从regSinkinkMap中找出正则和输出源对应关系
     * 5、从regSinkinkMap获得sink对象，迭代存入regQuene，形成sink->quene[DDAFile]
     */

    override fun onFileCreate(file: File) {
        if (!file.name.toUpperCase().endsWith(".TMP")
                && !file.name.toUpperCase().endsWith(".DDA")
                && !file.name.toUpperCase().endsWith(".SWP")
                && !file.name.toUpperCase().endsWith(".FILEPART")
                && !file.name.toUpperCase().endsWith(".READY")
                && !file.name.toUpperCase().endsWith(".COMPLETE")
                && !file.name.toUpperCase().endsWith(".TRANSITION")) {

            //发现了新的文件先对文件进行正则过滤
            val regSet = InitDDA.regSinkinkMap.keys

            for (k in regSet) {

                if (Regex(k).matches(file.name)) {

                    val sinks = InitDDA.regSinkinkMap[k]

                    for (sink in sinks!!) {

                        InitDDA.regQuene[sink]?.put(DDAFile(file.name, InitDDA.regClazz[k]!!))

                        log.info("find file ---->${file.name} in reg---->$k to quene---->$sink")
                    }
                }
            }
        }
    }
}