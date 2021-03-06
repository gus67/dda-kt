package cn.migu.boot

import cn.migu.dda.core.FileDetect
import cn.migu.dda.core.InitDDA
import cn.migu.dda.vo.FtpSink
import cn.migu.dda.vo.HdfsSink
import cn.migu.dda.vo.KafkaSink
import org.apache.commons.io.FileUtils
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object StartDDA {

    val log = LoggerFactory.getLogger("localFile")

    //val incomplete = LoggerFactory.getLogger("incomplete")

    @JvmStatic
    fun main(args: Array<String>) {
        val filename = "/Users/guxichang/code-src/dda-kt/src/main/resources/dda.cfg"

        InitDDA.initDDA(filename)

        //incomplete.info("incomplete")

        val suFile = File("./.INCOMPLETE")
        if (!suFile.exists()) {
            try {
                suFile.createNewFile()
            } catch (e: Exception) {
                log.error("ERROR---->", e.fillInStackTrace())
                return
            }
        }

        log.info("\u001b[32;1m根目录----> ${InitDDA.rootPath} \u001B[0m\n")

        val interval = TimeUnit.SECONDS.toMillis(1)
        val observer = FileAlterationObserver(File(InitDDA.rootPath))
        observer.addListener(FileDetect())
        //创建文件变化监听器
        val monitor = FileAlterationMonitor(interval, observer)
        // 开始监控
        monitor.start()


        /**
         * 队列消费
         * 1、找出所有sink->quene[DDAFile]
         * 2、为每一个队列卡开启一个线程
         */
        for (key in InitDDA.regQuene.keys) {
            Executors.newSingleThreadExecutor().submit({
                val abq = InitDDA.regQuene[key]

                //每一个队列一个线程，开启循环模式
                /**
                 * 1、判断文件是否需要插件转换
                 * 2、对文件转码试试
                 * 3、对大文件分割
                 * 4、传输
                 * 5、改名
                 * 6、对kafka数据需要保证容错
                 * 7、hdfs和ftp不需要保证容错，覆盖无影响
                 *
                 */
                while (true) {
                    try {
                        val fp = abq!!.take()
                        val path = fp.path
                        val clazz = fp.clazz
                        /**
                         * 反射插件
                         * 文件现文件本地化
                         * 失败回放
                         */
                        if (key is KafkaSink) {
                            log.info("\u001b[33;1mkafkaSink队列处理数据--->${key.bootServr},${key.topic}\u001B[0m\n")


                        } else if (key is HdfsSink) {
                            log.info("\u001b[33;1mhdfsSink队列处理数据--->${key.path}\u001B[0m\n")
                        } else if (key is FtpSink) {
                            log.info("\u001b[33;1mftpSink队列处理数据--->${key.ip},${key.port},${key.name},${key.passwd},${key.path}\u001B[0m\n")
                        }
                    } catch (e: Exception) {

                    }
                }
            })
        }
    }

    fun transcoding(filePath: String) {
        val p = "file --mime-encoding $filePath".execute()
        p.waitFor()
        val text = p.text()
        if (text.toLowerCase().indexOf("iso-8859-1") != -1) {

            /**
             * 多个线程同时读，需要保护一下,考虑移到另一个目录下
             */

            FileUtils.copyFile(File(filePath), File("./tmp/"))

            var ptr: Process? = null
            if (InitDDA.os.indexOf("linux") != -1) {
                ptr = "iconv -f gbk -t utf-8 $filePath -o $filePath.READY".execute()
            } else if (InitDDA.os.indexOf("mac") != -1) {
                ptr = "iconv -c -f gbk -t utf-8 $filePath > $filePath.READY".execute()
            } else {
                log.error("不支持win")
                System.exit(1)
            }
            if (ptr?.waitFor() == 0) {

            }
        }
    }

    fun String.execute(): Process {
        val runtime = Runtime.getRuntime()
        return runtime.exec(this)
    }

    fun Process.text(): String {
        var output = ""
        //    输出 Shell 执行的结果
        val inputStream = this.inputStream
        val isr = InputStreamReader(inputStream)
        val reader = BufferedReader(isr)
        var line: String? = ""
        while (line != null) {
            line = reader.readLine()
            if (line != null)
                output += line + "\n"
        }
        return output
    }
}


