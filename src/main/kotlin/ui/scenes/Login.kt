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
import ui.elements.PasswordField
import ui.scenes.logic.Scenes

object Login {
    @Composable
    @Preview
    fun show(sceneManager: MutableState<Scenes>, passwordCheck: (String) -> Boolean) {
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
}