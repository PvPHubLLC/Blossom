package co.pvphub.blossom.malscan

import co.pvphub.blossom.URL
import co.pvphub.blossom.util.Request
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
class MalscanClient(val token: String) {

    fun scan(f: File) {
        val files = loadFilesFromZip(f.absolutePath)
        val classes = files.filter { isClass(it.key, it.value) }.keys.map { it.replace(".class", "").replace("/", ".") }.toSet()
        val packages = files.filter { !it.key.contains(".") }.keys.map { it.replace("/",".") }.toSet()
        val res = post(URL + "scan").bodyJson(PluginData(classes, packages, hash(f.absolutePath))).sendJson<Detection>(Detection::class.java)
        res?.let {
            f.delete()
        }
    }

    fun get(url: String): Request = Request(Request.Method.GET, url).bearer(token)
    fun post(url: String): Request = Request(Request.Method.POST, url).bearer(token)
    private fun loadFilesFromZip(file: String): Map<String, ByteArray> {
        val files: MutableMap<String, ByteArray> = HashMap()
        try {
            ZipFile(file).use { zipFile ->
                zipFile.entries().asIterator().forEachRemaining { zipEntry: ZipEntry? ->
                    try {
                        files[zipEntry!!.name] = zipFile.getInputStream(zipEntry).readAllBytes()
                    } catch (e: Exception) {
                    }
                }
            }
        } catch (_: Exception) { }
        return files
    }
    private fun isClass(fileName: String, bytes: ByteArray): Boolean {
        return bytes.size >= 4 && String.format(
            "%02X%02X%02X%02X",
            bytes[0],
            bytes[1],
            bytes[2],
            bytes[3]
        ) == "CAFEBABE" && (fileName.endsWith(".class") || fileName.endsWith(".class/"))
    }
    private fun hash(f: String): String {
        val buffer = ByteArray(8192)
        var count: Int
        val digest = MessageDigest.getInstance("SHA-256")
        val bis = BufferedInputStream(FileInputStream(f))
        while (bis.read(buffer).also { count = it } > 0) {
            digest.update(buffer, 0, count)
        }
        bis.close()
        return digest.digest().decodeToString()

    }
    data class PluginData(val classes: Set<String>, val packages: Set<String>, val hash: String)
    data class Detection(val type: String, val code: String)
}