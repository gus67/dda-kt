package cn.migu.boot

import cn.migu.dda.core.FileDetect
import cn.migu.dda.core.InitDDA
import cn.migu.dda.vo.FtpSink
import cn.migu.dda.vo.HdfsSink
import cn.migu.dda.vo.KafkaSink
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object StartDDA {

    val log = LoggerFactory.getLogger(StartDDA::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val filename = "/Users/guxichang/code-src/dda-kt/src/main/resources/dda.cfg"

        InitDDA.initDDA(filename)

        log.info(InitDDA.rootPath)

        val interval = TimeUnit.SECONDS.toMillis(1)
        val observer = FileAlterationObserver(File(InitDDA.rootPath))
        observer.addListener(FileDetect())
        //创建文件变化监听器
        val monitor = FileAlterationMonitor(interval, observer)
        // 开始监控
        monitor.start()

        for (k in InitDDA.regQuene.keys) {
            Executors.newSingleThreadExecutor().submit(Runnable {
                val abq = InitDDA.regQuene[k]
                val fp = abq!!.take()
                val path = fp.path
                val clazz = fp.clazz
                /**
                 * 文件现文件本地化
                 * 反射插件
                 * 失败回放
                 */
                if (k is KafkaSink) {
                    k.bootServr
                    k.topic
                } else if (k is HdfsSink) {
                    k.path
                } else if (k is FtpSink)
                    k.ip
            })
        }
    }
}