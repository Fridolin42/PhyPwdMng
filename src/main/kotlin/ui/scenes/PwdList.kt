package ui.scenes

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.exmaple.getExampleData
import data.serial.SerialPortIO
import data.structure.Folder
import sceneManager
import ui.Theme.AutoTheme
import ui.elements.Image
import ui.scenes.logic.Scene
import ui.scenes.logic.Scenes
import javax.swing.JOptionPane

object PwdList : Scene {

    val data = getExampleData()
    val currentFolder = mutableStateOf(data)
    val selectedElementIndex = mutableStateOf(-1)
    var entryManagerAction = ""
    val currentCheckedFolder = mutableStateOf("/")

    @Composable
    @Preview
    override fun render() {
        AutoTheme {
            Row {
                Column(
                    modifier = Modifier.background(Color(0xFF888888)).padding(4.dp).fillMaxHeight()
                        .width(IntrinsicSize.Max)
                ) {
                    folder(data, currentFolder, selectedElementIndex)
                    Spacer(modifier = Modifier.weight(1f))
                    crudControllerFolder()
                }
                Column(modifier = Modifier.padding(start = 8.dp).width(IntrinsicSize.Max)) {
                    Text(
                        "PhyPwdMng",
                        style = TextStyle(fontSize = 32.sp),
                        modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    entries(currentFolder, selectedElementIndex)
                    Spacer(modifier = Modifier.weight(1f))
                    crudControllerEntries()
                }
            }
        }
    }

    @Composable
    @Preview
    private fun folder(
        data: Folder,
        currentFolder: MutableState<Folder>,
        selectedElementIndex: MutableState<Int>,
        padding: Int = 0,
        path: String = ""
    ) {
        Box(modifier = Modifier.clickable {
            println(path)
            currentFolder.value = data
            selectedElementIndex.value = -1
        }) {
            Row {
                Checkbox(
                    currentCheckedFolder.value == path,
                    onCheckedChange = { if (it) currentCheckedFolder.value = path else currentCheckedFolder.value = "/" })
                Text(
                    data.name.value,
                    modifier = Modifier
                        .padding(start = padding.dp, bottom = 4.dp)
                        .background(Color(0xFFDDDDDD), RoundedCornerShape(4.dp))
                        .padding(1.dp)
                        .fillMaxWidth(),
                )
            }
        }
        data.children.forEach {
            folder(it, currentFolder, selectedElementIndex, padding + 8, "$path${it.name}/")
        }
    }

    @Composable
    @Preview
    private fun entries(folder: MutableState<Folder>, selectedElementIndex: MutableState<Int>) {
        Row {
            Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                folder.value.entries.forEachIndexed { i, entry ->
                    Row(modifier = Modifier.padding(bottom = 8.dp)) {
                        Checkbox(
                            selectedElementIndex.value == i,
                            onCheckedChange = { selectedElementIndex.value = i },
                            modifier = Modifier.size(20.dp).align(Alignment.CenterVertically).padding(end = 8.dp)
                        )
                        Image(entry.website.value, modifier = Modifier.size(24.dp).align(Alignment.CenterVertically))
                        Text(
                            entry.website.value, maxLines = 1, modifier = Modifier.padding(start = 4.dp)
                                .background(Color(0xFFEDEDED), RoundedCornerShape(4.dp)).padding(1.dp).fillMaxWidth()
                        )
                    }
                }
            }
            Column(modifier = Modifier.width(IntrinsicSize.Max).padding(start = 8.dp)) {
                for (entry in folder.value.entries) {
                    Text(
                        entry.username.value, maxLines = 1,
                        modifier = Modifier.padding(bottom = 8.dp)
                            .background(Color(0xFFEDEDED), RoundedCornerShape(4.dp)).padding(1.dp).fillMaxWidth()
                    )
                }
            }
            Column(modifier = Modifier.width(IntrinsicSize.Max).padding(start = 8.dp)) {
                repeat(folder.value.entries.size) { i ->
                    Text(
                        "<password>", maxLines = 1,
                        modifier = Modifier.padding(bottom = 8.dp).background(Color(0xFFEDEDED), RoundedCornerShape(4.dp)).padding(1.dp)
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    fun crudControllerEntries() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val buttonColors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray, contentColor = Color.Black)
            Button(colors = buttonColors, onClick = {
                entryManagerAction = "add"
                sceneManager.value = Scenes.ENTRY_MANAGER
            }) {
                Text("Add")
            }
            Button(enabled = selectedElementIndex.value != -1, colors = buttonColors, onClick = {
                entryManagerAction = "edit"
                sceneManager.value = Scenes.ENTRY_MANAGER
            }) {
                Text("Edit")
            }
            Button(enabled = selectedElementIndex.value != -1, colors = buttonColors, onClick = {
                currentFolder.value.entries.removeAt(selectedElementIndex.value)
            }) {
                Text("Remove")
            }
        }
    }

    @Preview
    @Composable
    fun crudControllerFolder() {
        Column {
            Button(onClick = {
                JOptionPane.showInputDialog("Folder name")
            }) {
                Text("Add")
            }
            Spacer(Modifier.height(4.dp))
            Button(enabled = currentCheckedFolder.value.isNotEmpty(), onClick = {
                JOptionPane.showInputDialog("Folder name", currentFolder.value.name)
            }) {
                Text("Edit")
            }
            Spacer(Modifier.height(4.dp))
            Button(enabled = currentCheckedFolder.value.isNotEmpty(), onClick = {
                if (currentCheckedFolder.value == "/") return@Button
                val state = SerialPortIO.request("/remove/folder", "$currentCheckedFolder")
                println(state)
                var current = data
                var motherFolder = data
                for (segment in currentCheckedFolder.value.split("/").filter { it.isNotEmpty() }) {
                    motherFolder = current
                    current = current.children.first { it.name.value == segment }
                }
                motherFolder.children.remove(current)
            }) {
                Text("Remove")
            }
        }
    }
}