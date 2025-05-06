package ui.scenes

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import data.serial.SerialPortIO
import sceneManager
import ui.elements.PasswordField
import ui.scenes.logic.Scenes
import ui.scenes.logic.Scene

object Login : Scene {
    @Composable
    @Preview
    override fun render() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column {
                val pwd = remember { mutableStateOf("") }
                PasswordField(pwd, {})
                Button(onClick = {
                    if (SerialPortIO.checkPassword(pwd.value))
                        sceneManager.value = Scenes.PWS_LIST
                    else
                        pwd.value = ""
                }, content = {
                    Text("Login")
                })
            }
        }
    }
}