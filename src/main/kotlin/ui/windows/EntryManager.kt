package ui.windows

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import data.structure.Entry
import windows

class EntryManager(val entry: Entry? = null, val callback: (entry: Entry) -> Unit) : Window {
    init {
        windows.add(this)
    }

    @Composable
    override fun launch() {
        var isWindowsOpen by remember { mutableStateOf(true) }
        if (isWindowsOpen)
            Window(onCloseRequest = { isWindowsOpen = false }, title = "PhyPwdMng") {
                var website by remember { mutableStateOf(entry?.website?.value ?: "") }
                var username by remember { mutableStateOf(entry?.username?.value ?: "") }
                var password by remember { mutableStateOf("") } //TODO think about whether to load the password
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Website")
                    TextField(website, onValueChange = { website = it })
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Username")
                    TextField(username, onValueChange = { username = it })
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Password")
                    TextField(password, onValueChange = { password = it }, visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(onClick = {
                        isWindowsOpen = false
                        callback(Entry(mutableStateOf(website), mutableStateOf(username)))
                    }) {
                        Text("Save")
                    }
                }
            }
    }
}