package ui.scenes

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import data.structure.Entry
import sceneManager
import ui.scenes.logic.Scene
import ui.scenes.logic.Scenes

object EntryManager : Scene {

    @Composable
    override fun render() {
        val entry: Entry? =
            if (PwdList.entryManagerAction == "edit") PwdList.currentFolder.value.entries.elementAtOrNull(PwdList.selectedElementIndex.value) else null
        var website by remember { mutableStateOf(entry?.website?.value ?: "") }
        var username by remember { mutableStateOf(entry?.username?.value ?: "") }
        var password by remember { mutableStateOf("") } //TODO think about whether to load the password
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            modifier = Modifier.Companion.fillMaxSize()
        ) {
            Text("Website")
            TextField(website, onValueChange = { website = it })
            Spacer(modifier = Modifier.Companion.height(20.dp))
            Text("Username")
            TextField(username, onValueChange = { username = it })
            Spacer(modifier = Modifier.Companion.height(20.dp))
            Text("Password")
            TextField(password, onValueChange = { password = it }, visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.Companion.height(40.dp))
            Row {
                Button(onClick = {
                    if (entry != null) {
                        entry.website.value = website
                        entry.username.value = username
                    } else
                        PwdList.currentFolder.value.entries.add(Entry(mutableStateOf(website), mutableStateOf(username), System.nanoTime()))
                    sceneManager.value = Scenes.PWS_LIST
                }) {
                    Text("Save")
                }
                Spacer(Modifier.width(20.dp))
                Button(onClick = {
                    sceneManager.value = Scenes.PWS_LIST
                }) {
                    Text("Exit")
                }
            }
        }
    }
}
