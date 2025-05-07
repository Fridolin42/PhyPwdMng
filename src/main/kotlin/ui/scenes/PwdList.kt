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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.exmaple.getExampleData
import data.serial.SerialPortIO
import data.structure.Entry
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
    val currentCheckedFolderPath = mutableStateOf("")
    val currentRenderedFolderPath = mutableStateOf("/")

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
        path: String = "/"
    ) {
        println("render folder path: $path")
        Box(modifier = Modifier.clickable {
            println("clicked folder path: $path")
            currentRenderedFolderPath.value = path
            currentFolder.value = data
            selectedElementIndex.value = -1
        }) {
            Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)) {
                Checkbox(
                    currentCheckedFolderPath.value == path,
                    onCheckedChange = {
                        currentCheckedFolderPath.value = if (it) path else ""
                        println("clicked checbox path: ${currentCheckedFolderPath.value}")
                    }, modifier = Modifier.size(20.dp)
                )
                Text(
                    data.name.value,
                    modifier = Modifier
                        .padding(start = (12 + padding).dp)
                        .background(Color(0xFFDDDDDD), RoundedCornerShape(4.dp))
                        .padding(1.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                )
            }
        }
        data.children.forEach {
            folder(it, currentFolder, selectedElementIndex, padding + 8, "$path${it.name.value}/")
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
        Column(modifier = Modifier.fillMaxWidth()) {
            //Add
            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                val name = mutableStateOf(JOptionPane.showInputDialog("Folder name"))
                if (name.value == null) return@Button
                val children = SnapshotStateList<Folder>()
                val entries = SnapshotStateList<Entry>()
                val newFolder = Folder(name, children, entries)
                val status = SerialPortIO.request("/create/folder", "/ ${name.value}")
                println(status)
                if (status == "<success>")
                    data.children.add(newFolder)
            }) {
                Text("Add")
            }

            Spacer(Modifier.height(4.dp))
            //Edit
            Button(modifier = Modifier.align(Alignment.CenterHorizontally), enabled = currentCheckedFolderPath.value.length > 1, onClick = {
                val folderPath = currentCheckedFolderPath.value
                println("folder path: $folderPath")
                val currentName = folderPath.substringAfterLast("/")
                println("currentName: $currentName")
                val newName = JOptionPane.showInputDialog("Folder name", currentName)
                if (newName == null) return@Button
                val status = SerialPortIO.request("/update/folder", "$folderPath $newName")
                println(status)
                if (status == "<success>")
                    currentFolder.value.name.value = newName
            }) {
                Text("Edit")
            }

            Spacer(Modifier.height(4.dp))
            //Remove
            Button(modifier = Modifier.align(Alignment.CenterHorizontally), enabled = currentCheckedFolderPath.value.length > 1, onClick = {
                val folderPath = currentCheckedFolderPath.value
                println("folder path: $folderPath")
                if (folderPath == "/") return@Button
                val state = SerialPortIO.request("/delete/folder", folderPath)
                println(state)
                if (state != "<success>") return@Button
                var current = data
                var motherFolder = data
                for (segment in folderPath.split("/").filter { it.isNotEmpty() }) {
                    motherFolder = current
                    current = current.children.first { it.name.value == segment }
                }
//                val status = SerialPortIO.request("/delete/folder", folderPath)
//                if (status == "<success>")
                motherFolder.children.remove(current)
                current.entries.clear()
            }) {
                Text("Remove")
            }
        }
    }
}