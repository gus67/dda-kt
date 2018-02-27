package cn.migu.dda.core

import cn.migu.dda.vo.DDAFile
import cn.migu.dda.vo.FtpSink
import cn.migu.dda.vo.HdfsSink
import cn.migu.dda.vo.KafkaSink
import java.io.File
import java.util.concurrent.ArrayBlockingQueue

object InitDDA {

    //正则和输出源对应关系，一对多
    val regSinkinkMap = hashMapOf<String, ArrayList<Any>>()
    //监控目录的根路径
    var rootPath: String? = null
    //正则队列对应关系,阻塞队列
    val regQuene = hashMapOf<Any, ArrayBlockingQueue<DDAFile>>()
    //队列边界最大长度，否则阻塞
    val MAX_QUENE_LENGTH = 65535
    //正则和和解析类对应关系
    val regClazz = hashMapOf<String, String>()

    fun initDDA(configPath: String) {
        val file = File(configPath)
        var tmp: String? = null
        var clazz: String? = null
        file.forEachLine {
            if (it.startsWith("RP")) {
                rootPath = it.split(" ")[1]
            } else if (it.startsWith("REG")) {
                tmp = it.split(" ")[1]
                clazz = it.split(" ")[2]
                regClazz.put(tmp.toString(), clazz.toString())
                regSinkinkMap.put(tmp.toString(), arrayListOf())
            } else if (it.startsWith("K")) {
                val ks = it.split(" ")
                val kafkaSink = KafkaSink(ks[1], ks[2])
                regSinkinkMap[tmp]?.add(kafkaSink)
                regQuene.put(kafkaSink, ArrayBlockingQueue(MAX_QUENE_LENGTH))
            } else if (it.startsWith("H")) {
                val hs = it.split(" ")
                val hdfsSink = HdfsSink(hs[1])
                regSinkinkMap[tmp]?.add(hdfsSink)
                regQuene.put(hdfsSink, ArrayBlockingQueue(MAX_QUENE_LENGTH))
            } else if (it.startsWith("F")) {
                val fs = it.split(" ")
                val ftpSink = FtpSink(fs[1], fs[2].toInt(), fs[3], fs[4], fs[5])
                regQuene.put(ftpSink, ArrayBlockingQueue(MAX_QUENE_LENGTH))
                regSinkinkMap[tmp]?.add(ftpSink)
            }
        }
    }
}