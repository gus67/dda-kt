package cn.migu.dda.vo

class FtpSink(val ip: String, val port: Int, val name: String, val passwd: String, val path: String) {

    override fun toString(): String {
        return "FtpSink(ip='$ip', port=$port, name='$name', passwd='$passwd', path='$path')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FtpSink) return false

        if (ip != other.ip) return false
        if (port != other.port) return false
        if (name != other.name) return false
        if (passwd != other.passwd) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ip.hashCode()
        result = 31 * result + port
        result = 31 * result + name.hashCode()
        result = 31 * result + passwd.hashCode()
        result = 31 * result + path.hashCode()
        return result
    }
}