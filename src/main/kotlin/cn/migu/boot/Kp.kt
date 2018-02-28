package cn.migu.boot

import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.Executors

val log = LoggerFactory.getLogger("")

fun main(args: Array<String>) {


    for (x in 1..5) {

        Executors.newSingleThreadExecutor().submit({

            for (a in 1..20) {
                try {
                    println(a)
                    val k1 = (Random().nextInt(2))
                    println(k1)
                    a / k1
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

}

