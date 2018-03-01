package cn.migu.boot

import org.slf4j.LoggerFactory


val log = LoggerFactory.getLogger("")

fun main(args: Array<String>) {

    val clz = Class.forName("cn.migu.utils.TestRreflect")
    val o = clz.newInstance()
    val m = clz.getMethod("foo", Int::class.javaPrimitiveType, String::class.java)
    m.invoke(o, 1, "sdd")


}

