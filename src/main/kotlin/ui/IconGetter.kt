package ui

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File

object IconGetter {
    val client = HttpClient(CIO)
    suspend fun getIconURLFromSite(url: String): String? {
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
            .filter { it.contains("rel=\"icon\"") || it.contains("rel=\"shortcut icon\"") || it.contains("rel=\"apple-touch-icon\"") || it.contains("rel=\"apple-touch-icon-precomposed\"") }
            .sortedBy { if (it.contains("sizes=\"")) it.substringAfter("sizes=\"").substringBefore("x").toInt() else 0 }
//        println(htmlIconElements)
        return htmlIconElements.lastOrNull()?.substringAfter("href=\"")?.substringBefore("\"")
    }

    suspend fun downloadIcon(url: String) {
        val iconURL = getIconURLFromSite(url)
        iconURL?.let {
            val folder = File("img/")
            if (!folder.exists()) folder.mkdirs()
            val file = File("img/${System.nanoTime()}.${iconURL.substringAfterLast(".").substringBefore("?").substringBefore("#")}")
            println(file.absoluteFile)
            client.get(iconURL).bodyAsChannel().copyAndClose(file.writeChannel())
            println("finished downloading icon")
        }
    }
}