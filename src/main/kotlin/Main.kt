import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.scenes.logic.Scenes
import kotlin.system.exitProcess


val sceneManager: MutableState<Scenes> = mutableStateOf(Scenes.LOGIN)
fun main() = application {
    Window(onCloseRequest = { exitProcess(0) }, title = "PhyPwdMng") {
        sceneManager.value.scene.render()
    }
}
