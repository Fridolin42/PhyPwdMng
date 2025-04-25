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
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.exmaple.getExampleData
import data.structure.Entry
import data.structure.Folder
import data.structure.SerializableEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.Theme.AutoTheme
import ui.elements.Image
import ui.scenes.logic.Scenes
import ui.windows.EntryManager

object PwdList {

    @Composable
    @Preview
    fun show(sceneManager: MutableState<Scenes>) {
        val data = getExampleData()
        val currentFolder = mutableStateOf(data.entries)
        val selectedElementIndex = remember { mutableStateOf(-1) }
//        val entries =  mutableStateListOf<SerializableEntry>()
//        entries.addAll(data.entries)


        AutoTheme {
            Row {
                Column(
                    modifier = Modifier.background(Color(0xFF888888)).padding(4.dp).fillMaxHeight()
                        .width(IntrinsicSize.Max)
                ) { folder(data, currentFolder, selectedElementIndex) }
                Column(modifier = Modifier.padding(start = 8.dp).width(IntrinsicSize.Max)) {
                    Text(
                        "PhyPwdMng",
                        style = TextStyle(fontSize = 32.sp),
                        modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    entries(currentFolder, selectedElementIndex)
                    Spacer(modifier = Modifier.weight(1f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        val buttonColors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray, contentColor = Color.Black)
                        Button(colors = buttonColors, onClick = {
                            EntryManager {
                                println(it)
                                currentFolder.value.add(it)
                            }
                        }) {
                            Text("Add")
                        }
                        Button(enabled = selectedElementIndex.value != -1, colors = buttonColors, onClick = {
                            EntryManager(currentFolder.value[selectedElementIndex.value]) {
                                currentFolder.value[selectedElementIndex.value] = it
                            }
                        }) {
                            Text("Edit")
                        }
                        Button(enabled = selectedElementIndex.value != -1, colors = buttonColors, onClick = {
                            currentFolder.value.removeAt(selectedElementIndex.value)
                        }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }

    @Composable
    @Preview
    private fun folder(data: Folder, currentFolder: MutableState<SnapshotStateList<Entry>>, selectedElementIndex: MutableState<Int>, padding: Int = 0, path: String = "/") {
        Box(modifier = Modifier.clickable {
            println(path)
            currentFolder.value = data.entries
            selectedElementIndex.value = -1
        }) {
            Text(
                data.name.value,
                modifier = Modifier
                    .padding(start = padding.dp, bottom = 4.dp)
                    .background(Color(0xFFDDDDDD), RoundedCornerShape(4.dp))
                    .padding(1.dp)
                    .fillMaxWidth(),
            )
        }
        data.children.forEach {
            folder(it, currentFolder, selectedElementIndex, padding + 8, "$path${it.name}/")
        }
    }

    @Composable
    @Preview
    private fun entries(entries: MutableState<SnapshotStateList<Entry>>, selectedElementIndex: MutableState<Int>) {
        Row {
            Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                entries.value.forEachIndexed { i, entry ->
                    Row(modifier = Modifier.padding(bottom = 8.dp)) {
                        Checkbox(
                            selectedElementIndex.value == i,
                            onCheckedChange = { selectedElementIndex.value = i },
                            modifier = Modifier.size(20.dp).align(Alignment.CenterVertically).padding(end = 8.dp)
                        )
                        Image(entry.website.value, modifier = Modifier.size(24.dp).align(Alignment.CenterVertically))
                        Text(
                            entry.website.value, modifier = Modifier.padding(start = 4.dp)
                                .background(Color(0xFFEDEDED), RoundedCornerShape(4.dp)).padding(1.dp).fillMaxWidth()
                        )
                    }
                }
            }
            Column(modifier = Modifier.width(IntrinsicSize.Max).padding(start = 8.dp)) {
                for (entry in entries.value) {
                    Text(
                        entry.username.value,
                        modifier = Modifier.padding(bottom = 8.dp)
                            .background(Color(0xFFEDEDED), RoundedCornerShape(4.dp)).padding(1.dp).fillMaxWidth()
                    )
                }
            }
            Column(modifier = Modifier.width(IntrinsicSize.Max).padding(start = 8.dp)) {
                repeat(entries.value.size) { i ->
                    Text(
                        "<password>",
                        modifier = Modifier.padding(bottom = 8.dp).background(Color(0xFFEDEDED), RoundedCornerShape(4.dp)).padding(1.dp)
                    )
                }
            }
        }
    }
}