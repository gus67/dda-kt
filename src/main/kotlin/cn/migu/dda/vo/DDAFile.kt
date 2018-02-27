package cn.migu.dda.vo

class DDAFile(val path: String, val clazz: String) {

    override fun toString(): String {
        return "DDAFile(path='$path', clazz='$clazz')"
    }
}