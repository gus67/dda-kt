package cn.migu.utils

import cn.migu.dda.vo.HdfsSink
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission
import org.slf4j.LoggerFactory
import java.net.URI

class HdfsUtils {

    val log = LoggerFactory.getLogger(HdfsUtils::class.java)

    fun dda4HdfsPut(hdfsSink: HdfsSink, filePath: String) {

        var hdfs: FileSystem? = null

        var hdfsHead = ""

        var hdfsPath = ""

        try {
            var cnt = 0
            val sps = hdfsSink.path.split("/")
            for (x in sps) {
                for (x in sps) {
                    when (cnt) {
                        0 -> hdfsHead += x
                        1 -> hdfsHead += "//"
                        2 -> hdfsHead += x
                        else -> hdfsPath += "/" + x
                    }
                    cnt += 1
                }
            }

            hdfs = FileSystem.get(URI(hdfsHead), Configuration())

            if (!hdfs.exists(Path(hdfsPath))) {

                hdfs.mkdirs(Path(hdfsPath), FsPermission(
                        FsAction.ALL,
                        FsAction.ALL,
                        FsAction.ALL))
            }

            hdfs.copyFromLocalFile(false, Path(filePath), Path(hdfsPath))

            hdfs.close()



        } catch (e: Exception) {

            log.error("found fatal error！ write $filePath to ${hdfsSink.path}--->", e.fillInStackTrace())

            hdfs?.close()
        }
    }
}