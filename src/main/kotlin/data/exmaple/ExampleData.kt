package data.exmaple

import data.structure.Folder
import data.structure.SerializableFolder
import data.structure.map
import de.fridolin1.io.serial.SerialPortIO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun getExampleData(): Folder {
//    return map(
//        Json.decodeFromString<SerializableFolder>(
//            "{\"name\": \"/\",\"children\": [{\"name\": \"cloud\",\"children\": [],\"entries\": [{\"website\": \"mvnrepository.com\",\"username\": \"test@mail.com\"}]},{\"name\": \"programming\",\"children\": [],\"entries\": [{\"website\": \"jetbrains.com\",\"username\": \"test@mail.com\"}]}],\"entries\": [{\"website\": \"vplan.plus\", \"username\": \"Fridolin\"},{\"website\": \"chat.openai.com\",\"username\": \"test@mail.com\"},{\"website\": \"chefkoch.de\",\"username\": \"max musterman\"}]}"
//        )
//    )
    return map(Json.decodeFromString<SerializableFolder>(SerialPortIO.request("/get/structure")))
}
