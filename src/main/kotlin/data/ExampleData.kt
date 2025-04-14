package data

import kotlinx.serialization.json.Json

object ExampleData {
    fun getData(): Folder {
        return Json.decodeFromString<Folder>(
            "{\"name\": \"/\",\"children\": [{\"name\": \"cloud\",\"children\": [],\"entries\": [{\"website\": \"mchost24.de\",\"username\": \"test@mail.com\"}]},{\"name\": \"programming\",\"children\": [],\"entries\": [{\"website\": \"jetbrains.com\",\"username\": \"test@mail.com\"}]}],\"entries\": [{\"website\": \"chat.openai.com\",\"username\": \"test@mail.com\"},{\"website\": \"chefkoch.de\",\"username\": \"max musterman\"}]}"
        )
    }
}