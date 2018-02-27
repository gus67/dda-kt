package cn.migu.dda.core

import cn.migu.dda.vo.DDAFile
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor
import org.slf4j.LoggerFactory
import java.io.File

class FileDetect : FileAlterationListenerAdaptor() {

    val log = LoggerFactory.getLogger(FileDetect::class.java)

    override fun onFileCreate(file: File) {
        if (!file.name.toUpperCase().endsWith(".TMP")
                && !file.name.toUpperCase().endsWith(".SWP")
                && !file.name.toUpperCase().endsWith(".FILEPART")
                && !file.name.toUpperCase().endsWith(".READY")
                && !file.name.toUpperCase().endsWith(".COMPLETE")
                && !file.name.toUpperCase().endsWith(".TRANSITION")) {

            //发现了新的文件先对文件进行正则过滤
            val regSet = InitDDA.regSinkinkMap.keys

            /**
             * 文件扔到对应的队列中
             * 队列中的内容
             * HdfsSink(path='hdfs://172.18.111.3:9000/gus/g1/')=[DDAFile(path='2.log1', clazz='cn.gus.ParseExcel')],
             * HdfsSink(path='hdfs://172.18.111.3:9000/gus/g2/')=[DDAFile(path='2.log2', clazz='NA')
             */
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