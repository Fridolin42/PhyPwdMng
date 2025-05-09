package data.serial

import com.fazecast.jSerialComm.SerialPort
import crypto.AES
import crypto.RSA
import crypto.sha256
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.system.exitProcess

object SerialPortIO {
    private val comPort: SerialPort = SerialPort.getCommPorts().first { it.toString().contains("CP2102") }
    private val reader = BufferedReader(InputStreamReader(comPort.inputStream))
    private val writer = BufferedWriter(OutputStreamWriter(comPort.outputStream))
    private val rsaModule = RSA()
    private lateinit var piPublicKey: String
    private lateinit var aesModule: AES

    init {
        println("available Ports: " + SerialPort.getCommPorts().joinToString(separator = "; "))
        if (!comPort.openPort())
            throw error("failed to open port ${comPort.descriptivePortName}")
        else
            println("Opened port: '$comPort'")
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0)
        println("Port open: ${comPort.isOpen}")
    }

    fun keyExchange() {
        val publicKey = rsaModule.getPublicKey()
        writer.write("/keyExchange $publicKey\n")
        writer.flush()
        try {
            val line = reader.readLine()
            if (line == null) {
                println("Stream closed")
                exitProcess(-1)
            }
            println("Public Key: $line")
            val chunks = line.split(" ").map { rsaModule.decrypt(it) }
            piPublicKey = chunks.joinToString("")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkPassword(password: String): Boolean {
        val encrypted = rsaModule.encrypt(password, piPublicKey)
        val sign = rsaModule.sign(encrypted)
        writer.write("/login $encrypted $sign\n")
        writer.flush()
        try {
//            while (true) {
            val line = reader.readLine()
            if (line == null) {
                println("Stream closed")
                return false
            }
            println("Received: $line")
            if (line == "<login> okay") {
                aesModule = AES(password)
                return true
            } else
                return false
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw error("something very strange happened")
    }

    fun request(path: String, body: String): String {
        send(path, body)
        try {
            while (true) {
                val line = reader.readLine()
                if (line == null) {
                    println("Stream closed")
                    break
                }
                println("Received: $line")
                val decrypted = aesModule.decrypt(line)
                println("Message: $decrypted")
                return decrypted
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw error("something very strange happened")
    }

    fun send(path: String, message: String) {
        println("Message raw: $path $message")
        writer.write(buildString {
            append(path)
            append(" ")
            if (message.isNotEmpty())
                append(aesModule.encrypt(message))
            append("\n")
        })
        writer.flush()
    }
}