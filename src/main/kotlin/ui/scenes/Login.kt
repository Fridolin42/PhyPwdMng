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
                var pwd by remember { mutableStateOf("") }
                PasswordField({ pwd = it })
                Button(onClick = { sceneManager.value = Scenes.PWS_LIST }, content = {
                    Text("Login")
                }, enabled = passwordCheck(pwd))
            }
        }
    }

    //TODO propper Password check
    fun passwordCheck(pwd: String) = pwd == "123456"
}