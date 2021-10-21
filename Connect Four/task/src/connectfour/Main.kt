package connectfour

import java.lang.NumberFormatException

fun main() {
    println("Connect four")

    // Input of players name.
    println("First player's name: ")
    val firstPlayersName = readLine()!!
    println("Second player's name: ")
    val secondPlayersName = readLine()!!

    val intRegex = "[5-9]".toRegex()
    val splitRegex = "[x, X]".toRegex()
    val boardSize = mutableListOf(6, 7)

    // Input board size.
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

    // Creating and printing of game board.
    val board = boardCreate(boardSize)

    var freePosition: Int
    var columnNumber: Int
    var nextStep: String
    var isPlayer1 = true
    var fourInLine: Boolean
    var fourInDiagonal: Boolean

    // Playing the game.
    while (true) {
        if (isPlayer1) {
            println("$firstPlayersName's turn:")
        } else {
            println("$secondPlayersName's turn:")
        }

        // Input players step.
        nextStep = readLine()!!
        if (nextStep == "end") {
            println("Game over!")
            return
        } else{
            // Check correction of column number.
            try {
                columnNumber = nextStep.toInt() - 1
            } catch (e: NumberFormatException) {
                println("Incorrect column number")
                continue
            }

            if (columnNumber < 0 || columnNumber > boardSize[1] - 1) {
                println("The column number is out of range (1 - ${boardSize[1]})")
                continue
            }

            // Check if the column isn't full.
            freePosition = checkColumn(columnNumber, boardSize, board)

            if (freePosition == -1) {
                println("Column ${columnNumber + 1} is full")
                continue
            } else {
                // If column isn't full writing marker and checking winning condition.
                if (isPlayer1) {
                    board[freePosition][columnNumber] = "o"
                    fourInLine = checkWinByLine(board)
                    fourInDiagonal = checkWinByDiagonal(board, false)
                    if (fourInLine || fourInDiagonal) {
                        printBoard(board)
                        println("Player $firstPlayersName won")
                        println("Game over!")
                        return
                    }
                    isPlayer1 = false
                } else {
                    board[freePosition][columnNumber] = "*"
                    fourInLine = checkWinByLine(board)
                    fourInDiagonal = checkWinByDiagonal(board, false)
                    if (fourInLine || fourInDiagonal) {
                        printBoard(board)
                        println("Player $secondPlayersName won")
                        println("Game over!")
                        return
                    }
                    isPlayer1 = true
                }
            }
        }

        printBoard(board)

        if (boardIsFull(board)) {
            println("It is a draw")
            println("Game over!")
            return
        }
    }
}

// Creating game board and print it.
fun boardCreate(boardSize: List<Int>) : MutableList<MutableList<String>> {
    val firstLane = mutableListOf<String>()
    for (i in 1..boardSize[1]) {
        firstLane.add(i.toString())
    }

    val board = mutableListOf(firstLane)


    val line = mutableListOf<String>()
    for (i in 0 until boardSize[1]) {
        line.add(" ")
    }

    repeat(boardSize[0]) { board.add(line.toMutableList()) }

    val lastLine = mutableListOf<String>()
    for (i in 0 until boardSize[1]) {
        lastLine.add("═")
    }

    board.add(lastLine)

    printBoard(board)
    return board
}

// Printing game board.
fun printBoard(board: MutableList<MutableList<String>>) {
    println(board[0].joinToString(" ", " ", " "))
    for (i in 1 until board.size - 1) {
        println(board[i].joinToString("║", "║", "║"))
    }
    println(board.last().joinToString("╩", "╚", "╝"))
}

// Checking if column isn't full. If column is full return -1,
// else return max index of free position.
fun checkColumn(columnNumber: Int,
                 boardSize: MutableList<Int>,
                 board: MutableList<MutableList<String>>) : Int {
    for (i in boardSize[0] downTo 1) {
        if (board[i][columnNumber] == " ") return i
    }
    return -1
}

// Check of winning condition in rows and columns.
fun checkWinByLine(board: MutableList<MutableList<String>>) : Boolean {
    val list = mutableListOf<String>()
    var str: String

    for (i in 1 until board.size - 1) {
        str = board[i].joinToString("")
        if (str.contains("oooo") || str.contains("****")) return true
    }

    for (i in 0 until board[0].size) {
        for (j in 0 until board.size) {
            list.add(board[j][i])
        }
        str = list.joinToString("")
        if (str.contains("oooo") || str.contains("****")) return true
    }
    return false
}

// Check if game board is full
fun boardIsFull(board: MutableList<MutableList<String>>) : Boolean {
    for (i in 1 until board.size - 1) {
        if (board[i].contains(" ")) return false
    }
    return true
}

// Check of winning condition in diagonals.
fun checkWinByDiagonal (board: MutableList<MutableList<String>>, reversed: Boolean) : Boolean {
    val boardReversed = mutableListOf<MutableList<String>>()
    val list = mutableListOf<String>()
    var str: String
    var n = 4

    for (i in board.size - 5 downTo 1) {
        for (j in 0 until n) {
            list.add(board[j + i][j])
        }
        str = list.joinToString("")
        if (str.contains("oooo") || str.contains("****")) return true
        list.clear()
        n++
    }

    n = 4
    for (i in board[0].size - 5 downTo 0 ) {
        for (j in 1..n) {
            list.add(board[j][i + j])
        }
        str = list.joinToString("")
        if (str.contains("oooo") || str.contains("****")) return true
        list.clear()
        n++
    }

    if (!reversed) {
        for (i in 0 until board.size) {
            boardReversed.add(board[i].asReversed())
        }
        if(checkWinByDiagonal(boardReversed, true)) return true
    }
    return false
}