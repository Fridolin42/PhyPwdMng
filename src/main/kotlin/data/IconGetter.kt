package data

import androidx.compose.animation.scaleOut
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.serialization.json.Json
import org.jetbrains.skia.Image
import java.awt.image.BufferedImage
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.exists

object IconGetter {
    private val client = HttpClient(CIO)
    private val config: HashMap<String, String>
    private val configFile: File = File("img/config.json")

    init {
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
        val text = BufferedReader(FileReader(configFile)).readText()
        config = if (text.isNotEmpty()) Json.decodeFromString<HashMap<String, String>>(text) else HashMap()
    }

    private suspend fun getIconURLFromSite(url: String): String? {
        val htmlIconElements = client.get(url).bodyAsText().split("<link").map {
            var inQuote = false
            var backslashes = 0
            var quoteChar = '"'
            it.toCharArray().forEachIndexed() { i, c ->
                if (!inQuote && c == '\'') quoteChar = '\''
                else if (!inQuote && c == '"') quoteChar = '\"'
                if (c == quoteChar && backslashes % 2 == 0) inQuote = !inQuote
                if (c == '\\') backslashes++
                else backslashes = 0
                if (c == '>' && !inQuote) return@map it.substring(0, i)
            }
            return@map it
        }
            .filter {
                it.contains("rel=\"icon\"") || it.contains("rel=\"shortcut icon\"") || it.contains("rel=\"apple-touch-icon\"") || it.contains(
                    "rel=\"apple-touch-icon-precomposed\""
                )
            }
            .sortedBy {
                if (it.contains("sizes=\"")) {
                    val size = it.substringAfter("sizes=\"").substringBefore("x")
                    if (size != "any\"") return@sortedBy size.toInt()
                    else return@sortedBy Int.MAX_VALUE
                } else 0
            }
//        println(htmlIconElements)
        return htmlIconElements.lastOrNull()?.substringAfter("href=\"")?.substringBefore("\"")
    }

    private suspend fun downloadIcon(url: String): String? {
        println(url)

        val iconURL = getIconURLFromSite(url) // deine eigene Funktion
        if (iconURL == null) return null

        try {
            // Ordner anlegen
            val folder = File("img/")
            if (!folder.exists()) folder.mkdirs()

            // Ziel-Datei vorbereiten
            val file = File("img/${System.nanoTime()}.png")
            println("Ziel-Datei: ${file.absolutePath}")

            // Bilddaten als InputStream laden
            val response: HttpResponse = client.get(iconURL)
            val contentType = response.headers["Content-Type"]
            println("Content-Type: $contentType")

            val stream = response.bodyAsChannel().toInputStream()
            val image: BufferedImage? = ImageIO.read(stream)

            if (image == null) {
                println("❌ Konnte Bild nicht laden (nicht unterstütztes Format?)")
                return null
            }

            // Als PNG speichern
            ImageIO.write(image, "png", file)
            println("✅ Bild gespeichert: ${file.path}")
            return file.path

        } catch (e: Exception) {
            println("❌ Fehler beim Herunterladen: ${e.message}")
            return null
        }
//        println(url)
//        val iconURL = getIconURLFromSite(url)
//        iconURL?.let {
//            val folder = File("img/")
//            if (!folder.exists()) folder.mkdirs()
//            val file = File("img/${System.nanoTime()}.png"
////                "img/${System.nanoTime()}.${
////                    iconURL.substringAfterLast(".").substringBefore("?").substringBefore("#")
////                }"
//            )
//            println(file.absoluteFile)
//            val img = ImageIO.read(client.get(iconURL).bodyAsChannel().toInputStream())
//            ImageIO.write(img, "png", file)
////            client.get(iconURL).bodyAsChannel().copyAndClose(file.writeChannel())
//            println("finished downloading icon")
//            return file.path
//        }
//        return null
    }

    private suspend fun getIcon(url: String, forceDownload: Boolean): Path? {
        config[url]?.let {
            if (forceDownload) return@let
            val path = Path(it)
            if (path.exists()) return path
            config.remove(url)
        }
        val filePath = downloadIcon(if (url.startsWith("https://") || url.startsWith("http://")) url else "https://$url")
        filePath?.let {
            config[url] = it
            saveConfig()
            return Path(it)
        }
        return null
    }

    suspend fun getImageBitmap(url: String, forceDownload: Boolean = false): ImageBitmap? {
        val path = getIcon(url, forceDownload)
        if (path == null) return null
        val bytes = Files.readAllBytes(path)
        println(bytes == null)
        return Image.makeFromEncoded(bytes).toComposeImageBitmap()
    }

    private fun saveConfig() {
        val writer = BufferedWriter(FileWriter(configFile))
        writer.append(Json.encodeToString(config))
        writer.close()
    }
}