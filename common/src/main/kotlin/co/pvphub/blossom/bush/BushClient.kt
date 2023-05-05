package co.pvphub.blossom.bush

import co.pvphub.blossom.URL
import co.pvphub.blossom.logger
import co.pvphub.blossom.util.Request
class BushClient(
//    val token: String token is disabled until further discussion
) {
    fun isIpBlacklisted(ip: String): Boolean {
        val res = post(URL + "ip").bodyJson(Ip(ip)).sendJson<IpInfo>(IpInfo::class.java)
        res?.let {
            logger.info("${it.ip} has tried to join/ping the server but is blacklisted from the blossom security guard for \"${it.reason}\"")
            return true
        }
        return false

    }

    fun get(url: String): Request = Request(Request.Method.GET, url) // token is disabled until further discussion .bearer(token)
    fun post(url: String): Request = Request(Request.Method.POST, url) // token is disabled until further discussion  .bearer(token)
    data class Ip(val ip: String)
    data class IpInfo(val ip: String, val reason: String)
}