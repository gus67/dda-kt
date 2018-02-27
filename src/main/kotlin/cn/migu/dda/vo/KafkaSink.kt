package cn.migu.dda.vo

class KafkaSink(val bootServr: String, val topic: String) {

    override fun toString(): String {
        return "KafkaSink(bootServr='$bootServr', topic='$topic')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KafkaSink) return false

        if (bootServr != other.bootServr) return false
        if (topic != other.topic) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bootServr.hashCode()
        result = 31 * result + topic.hashCode()
        return result
    }

}