package data.serial

import com.fazecast.jSerialComm.SerialPort
import crypto.AES
import crypto.sha256
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object SerialPortIO {
    private val comPort: SerialPort = SerialPort.getCommPorts().first { it.toString().contains("CP2102") }
    private val reader = BufferedReader(InputStreamReader(comPort.inputStream))
    private val writer = BufferedWriter(OutputStreamWriter(comPort.outputStream))
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

    fun checkPassword(password: String): Boolean {
        writer.write("/login ${sha256(password)}\n")
        writer.flush()
        try {
            while (true) {
                val line = reader.readLine()
                if (line == null) {
                    println("Stream closed")
                    break
                }
                println("Received: $line")
                if (line == "<login> okay") {
                    aesModule = AES(password)
                    return true
                } else
                    return false
            }
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