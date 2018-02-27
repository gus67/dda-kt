package cn.migu.dda.vo

class HdfsSink(val path: String) {
    override fun toString(): String {
        return "HdfsSink(path='$path')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HdfsSink) return false

        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

}