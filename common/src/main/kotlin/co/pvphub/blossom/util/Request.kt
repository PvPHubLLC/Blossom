package co.pvphub.blossom.util

import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import java.util.stream.Stream

class Request(var method: Method?, url: String) {
    private val CLIENT = HttpClient.newHttpClient()
    private val GSON = Gson()
    private lateinit var builder: HttpRequest.Builder

    init {
        try {
            builder = HttpRequest.newBuilder().uri(URI(url)).header("User-Agent", "Blossom")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    enum class Method {
        GET, POST
    }

    fun bearer(token: String): Request {
        builder.header("Authorization", "Bearer $token")
        return this
    }

    fun header(name: String?, value: String?): Request {
        builder.header(name, value)
        return this
    }

    fun bodyString(string: String?): Request {
        builder.header("Content-Type", "text/plain")
        builder.method(method!!.name, HttpRequest.BodyPublishers.ofString(string))
        method = null
        return this
    }

    fun bodyForm(string: String?): Request {
        builder.header("Content-Type", "application/x-www-form-urlencoded")
        builder.method(method!!.name, HttpRequest.BodyPublishers.ofString(string))
        method = null
        return this
    }

    fun bodyJson(string: String?): Request {
        builder.header("Content-Type", "application/json")
        builder.method(method!!.name, HttpRequest.BodyPublishers.ofString(string))
        method = null
        return this
    }

    fun bodyJson(`object`: Any?): Request {
        builder.header("Content-Type", "application/json")
        builder.method(
            method!!.name,
            HttpRequest.BodyPublishers.ofString(GSON.toJson(`object`))
        )
        method = null
        return this
    }

    private fun <T> _send(accept: String, responseBodyHandler: HttpResponse.BodyHandler<T>): T? {
        builder.header("Accept", accept)
        if (method != null) builder.method(method!!.name, HttpRequest.BodyPublishers.noBody())
        return try {
            val res: HttpResponse<T> =
                CLIENT.send<T>(builder.build(), responseBodyHandler)
            if (res.statusCode() == 200) res.body() else null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: InterruptedException) {
            e.printStackTrace()
            null
        }
    }

    fun send() {
        _send("*/*", HttpResponse.BodyHandlers.discarding())
    }

    fun sendInputStream(): InputStream {
        return _send("*/*", HttpResponse.BodyHandlers.ofInputStream())!!
    }

    fun sendString(): String {
        return _send("*/*", HttpResponse.BodyHandlers.ofString())!!
    }

    fun sendLines(): Stream<String> {
        return _send("*/*", HttpResponse.BodyHandlers.ofLines())!!
    }

    fun <T> sendJson(type: Type?): T? {
        val `in` = _send("application/json", HttpResponse.BodyHandlers.ofInputStream())
        return if (`in` == null) null else GSON.fromJson<T>(
            InputStreamReader(
                `in`
            ), type
        )
    }
}