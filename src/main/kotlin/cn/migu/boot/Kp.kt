package cn.migu.boot

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.slf4j.LoggerFactory
import java.net.URI
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission


val log = LoggerFactory.getLogger("")

fun main(args: Array<String>) {


    val path = "hdfs://hadoop/dda/d0/d01/d02/d03"
    var root = ""

    var remoteP = ""

    val sp = path.split("/")

    var cnt = 0

    for (x in sp) {
        when (cnt) {
            0 -> root += x
            1 -> root += "//"
            2 -> root += x

            else -> remoteP += "/" + x
        }

        cnt += 1

    }

    println(root)
    println(remoteP)
    var hdfs: FileSystem? = null
    try {
        val localPath = "/Users/guxichang/monitor/33.log1"
        val conf = Configuration()
        hdfs = FileSystem.get(URI(root), conf)

        if (!hdfs.exists(Path(remoteP))) {

            hdfs.mkdirs(Path(remoteP), FsPermission(
                    FsAction.ALL,
                    FsAction.ALL,
                    FsAction.ALL))
        }

        hdfs.copyFromLocalFile(false, true, Path(localPath), Path(remoteP))
        hdfs.close()
    } catch (e: Exception) {
        log.error("error!", e.fillInStackTrace())
        hdfs?.close()
    }
}