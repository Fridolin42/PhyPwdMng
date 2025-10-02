import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.serial.SerialPortIO
import ui.scenes.EntryManager
import ui.scenes.Login
import ui.scenes.PwdList
import ui.scenes.logic.Scenes
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import kotlin.system.exitProcess


val sceneManager: MutableState<Scenes> = mutableStateOf(Scenes.LOGIN)
fun main() = application {
    SerialPortIO.keyExchange()
    Window(onCloseRequest = { exitProcess(0) }, title = "PhyPwdMng") {
        when (sceneManager.value) {
            Scenes.LOGIN -> Login.render()
            Scenes.PWS_LIST -> PwdList.render()
            Scenes.ENTRY_MANAGER -> EntryManager.render()
        }
    }
}

fun copyToClipboard(text: String) {
    val selection = StringSelection(text)
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}