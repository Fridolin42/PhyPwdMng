import ui.App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.IconGetter


fun main() = application {
    CoroutineScope(Dispatchers.Default).launch {
        IconGetter.downloadIcon("https://stackoverflow.com/questions/71980361/how-to-get-a-image-png-with-ktor-client-get-request")
    }
    Window(onCloseRequest = ::exitApplication, title = "PhyPwdMng") {
        App.app()
    }
}
