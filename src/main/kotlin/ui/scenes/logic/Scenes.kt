package ui.scenes.logic

import ui.scenes.EntryManager
import ui.scenes.Login
import ui.scenes.PwdList

enum class Scenes(val scene: Scene) {
    LOGIN(Login),
    PWS_LIST(PwdList),
    ENTRY_MANAGER(EntryManager)
}