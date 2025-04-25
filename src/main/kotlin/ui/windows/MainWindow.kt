package ui.windows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import ui.scenes.Login
import ui.scenes.PwdList
import ui.scenes.logic.Scenes
import kotlin.system.exitProcess

object MainWindow : Window {

    @Composable
    override fun launch() {
        val sceneManager = remember { mutableStateOf(Scenes.LOGIN) }
//    CoroutineScope(Dispatchers.Default).launch {
//        IconGetter.downloadIcon("https://stackoverflow.com/questions/71980361/how-to-get-a-image-png-with-ktor-client-get-request")
//    }
        Window(onCloseRequest = { exitProcess(0) }, title = "PhyPwdMng") {
            when (sceneManager.value) {
                Scenes.LOGIN -> Login.show(sceneManager) { it == "123456" }
                Scenes.PWS_LIST -> PwdList.show(sceneManager)
            }
        }
    }
}