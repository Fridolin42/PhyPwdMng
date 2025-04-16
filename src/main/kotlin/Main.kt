import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.scenes.Login
import ui.scenes.PwdList
import ui.scenes.logic.Scenes


fun main() = application {
    val sceneManager = remember { mutableStateOf(Scenes.LOGIN) }
//    CoroutineScope(Dispatchers.Default).launch {
//        IconGetter.downloadIcon("https://stackoverflow.com/questions/71980361/how-to-get-a-image-png-with-ktor-client-get-request")
//    }
    Window(onCloseRequest = {exitApplication()}, title = "PhyPwdMng") {
        when (sceneManager.value) {
            Scenes.LOGIN -> Login.show(sceneManager) {it == "123456"}
            Scenes.PWS_LIST -> PwdList.show(sceneManager)
        }
    }
}
