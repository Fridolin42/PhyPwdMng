package data

import kotlinx.serialization.json.Json

object ExampleData {
    fun getData(): Folder {
        return Json.decodeFromString<Folder>(
            "{\"name\": \"/\",\"children\": [{\"name\": \"cloud\",\"children\": [],\"entries\": [{\"website\": \"mvnrepository.com\",\"username\": \"test@mail.com\"}]},{\"name\": \"programming\",\"children\": [],\"entries\": [{\"website\": \"jetbrains.com\",\"username\": \"test@mail.com\"}]}],\"entries\": [{\"website\": \"vplan.plus\", \"username\": \"Fridolin\"},{\"website\": \"chat.openai.com\",\"username\": \"test@mail.com\"},{\"website\": \"chefkoch.de\",\"username\": \"max musterman\"}]}"
        )
    }
}