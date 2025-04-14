package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Entry
import data.ExampleData
import data.Folder
import ui.Theme.AutoTheme

object App {

    @Composable
    @Preview
    fun app() {
        val data by remember { mutableStateOf(ExampleData.getData()) }
        val entries = mutableStateOf(data.entries)

        AutoTheme {
            Row {
                Column(
                    modifier = Modifier.background(Color(0xFF888888)).padding(4.dp).fillMaxHeight().width(IntrinsicSize.Max)
                ) { folder(data, entries) }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text("PhyPwdMng", style = TextStyle(fontSize = 32.sp), modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(), textAlign = TextAlign.Center)
                    entries(entries)
                }
            }
        }
    }

    @Composable
    @Preview
    private fun folder(data: Folder, entries: MutableState<List<Entry>>, padding: Int = 0, path: String = "/") {
        Box(modifier = Modifier.clickable {
            println(path)
            entries.value = data.entries
        }) {
            Text(
                data.name,
                modifier = Modifier
                    .padding(start = padding.dp, bottom = 4.dp)
                    .background(Color(0xFFDDDDDD), RoundedCornerShape(4.dp))
                    .padding(1.dp)
                    .fillMaxWidth(),
            )
        }
        data.children.forEach {
            folder(it, entries, padding + 8, "$path${it.name}/")
        }
    }

    @Composable
    @Preview
    private fun entries(entries: MutableState<List<Entry>>) {
        Row {
            Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                for (entry in entries.value) {
                    Text(
                        entry.website, modifier = Modifier.padding(bottom = 8.dp)
                            .background(Color(0xFFEDEDED), RoundedCornerShape(4.dp)).padding(1.dp).fillMaxWidth()
                    )
                }
            }
            Column(modifier = Modifier.width(IntrinsicSize.Max).padding(start = 8.dp)) {
                for (entry in entries.value) {
                    Text(
                        entry.username,
                        modifier = Modifier.padding(bottom = 8.dp)
                            .background(Color(0xFFEDEDED), RoundedCornerShape(4.dp)).padding(1.dp).fillMaxWidth()
                    )
                }
            }
            Column(modifier = Modifier.width(IntrinsicSize.Max).padding(start = 8.dp)) {
                for (entry in entries.value) {
                    Text(
                        "<password>",
                        modifier = Modifier.padding(bottom = 8.dp)
                            .background(Color(0xFFEDEDED), RoundedCornerShape(4.dp)).padding(1.dp).fillMaxWidth()
                    )
                }
            }
        }
    }
}