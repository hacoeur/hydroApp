package controllers
import models.Drink
import persistence.Serializer
import utils.Utilities.isValidListIndex

class DrinkAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType

    private var drinks = ArrayList<Drink>()

    fun add(drink: Drink): Boolean {
        return drinks.add(drink)
    }

    fun listAllDrinks(): String {
        return if (drinks.isEmpty()) {
            "No entries created yet"
        } else {
            var allEntries = ""
            for (i in drinks.indices) {
                allEntries += "$i: ${drinks[i]} \n"
            }
            allEntries
        }
    }

    fun listPerDate(date: String): String {
        return if (drinks.isEmpty()) {
            "No entries created yet"
        } else {
            var entriesPerDate = ""
            for (i in drinks.indices) {
                if (drinks[i].date == date) {
                    entriesPerDate +=
                        """$i: ${drinks[i]}
                        """.trimIndent()
                }
            }
            if (entriesPerDate == "") {
                "No entries with date: $date"
            } else {
                entriesPerDate
            }
        }.toString()
    }

    fun isGoalAchievedOnDay(date: String): String {
        var amount = 0
        val dayChosen = drinks.filter {
            drink ->
            drink.date == date
        }

        return if (drinks.isEmpty()) {
            "No entries created yet"
        }
        else {
            for (i in dayChosen.indices) {
                amount += dayChosen[i].sizeGlassMl
            }

            if (amount >= 1200) {
                "You've achieved your goal on: $date"
            } else {
                "You didn't achieve your goal on: $date"
            }
        }
    }

    fun listPerLiquid(liquid: String): String {
        return if (drinks.isEmpty()) {
            "No entries created yet"
        } else {
            var entriesPerLiquid = ""
            for (i in drinks.indices) {
                if (drinks[i].liquidType == liquid) {
                    entriesPerLiquid +=
                        """$i: ${drinks[i]}
                        """.trimIndent()
                }
            }
            if (entriesPerLiquid == "") {
                "No entries with liquid type: $liquid"
            } else {
                entriesPerLiquid
            }
        }
    }

    fun updateDrink(indexToUpdate: Int, drink: Drink?): Boolean {
        val selectedEntry = findEntry(indexToUpdate)

        return if ((selectedEntry != null) && (drink != null)) {
            selectedEntry.sizeGlassMl = drink.sizeGlassMl
            selectedEntry.liquidType = drink.liquidType
            selectedEntry.timeTaken = drink.timeTaken
            selectedEntry.date = drink.date
            true
        } else {
            false
        }
    }

    fun deleteDrink(indexToDelete: Int): Drink? {
        return if (isValidListIndex(indexToDelete, drinks)) {
            drinks.removeAt(indexToDelete)
        } else null
    }

// ----- indirect user purposes -----
    fun numberOfEntries(): Int {
        return drinks.size
    }

    fun findEntry(index: Int): Drink? {
        return if (isValidListIndex(index, drinks)) {
            drinks[index]
        } else null
    }

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, drinks)
    }

    @Throws(Exception::class)
    fun load() {
        drinks = serializer.read() as ArrayList<Drink>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(drinks)
    }
}
