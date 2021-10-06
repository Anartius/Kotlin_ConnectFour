package connectfour

fun main() {
    println("Connect four")
    println("First player's name: ")
    val firstPlayersName = readLine()!!
    println("Second player's name: ")
    val secondPlayersName = readLine()!!

    val intRegex = "[5-9]".toRegex()
    val splitRegex = "[x, X]".toRegex()
    val boardSize = mutableListOf(6, 7)

    while (true) {
        println("""
            Set the board dimensions (Rows x Columns)
            Press Enter for default (6 x 7)
        """.trimIndent())
        val sizeInputStr = readLine()!!.replace("\t*\\s*".toRegex(), "")
        if (sizeInputStr.isEmpty()) break

        if (!sizeInputStr.matches("\\d+[x, X]\\d+".toRegex())) {
            println("Invalid input")
            continue
        } else {
            val sizeInputList = sizeInputStr.split(splitRegex)

            if (sizeInputList[0].matches(intRegex)) {
                boardSize[0] = sizeInputList[0].toInt()
            } else {
                println("Board rows should be from 5 to 9")
                continue
            }

            if (sizeInputList[1].matches(intRegex)) {
                boardSize[1] = sizeInputList[1].toInt()
                break
            } else {
                println("Board columns should be from 5 to 9")
                continue
            }
        }
    }
    println("""
        $firstPlayersName VS $secondPlayersName
        ${boardSize[0]} X ${boardSize[1]} board
        """.trimIndent())
}