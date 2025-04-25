import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.window.application
import ui.windows.MainWindow
import ui.windows.Window


lateinit var windows: SnapshotStateList<Window>
fun main() = application {
    windows = remember { mutableStateListOf<Window>() }
    windows.forEach {
        key(it) {
            it.launch()
        }
    }
    MainWindow.launch()
}
