package connectfour

import java.lang.NumberFormatException

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

    val board = boardCreate(boardSize)

    var freePosition: Int
    var columnNumber: Int
    var nextStep: String
    var isPlayer1 = true

    while (true) {
        if (isPlayer1) {
            println("$firstPlayersName's turn:")
        } else {
            println("$secondPlayersName's turn:")
        }
        nextStep = readLine()!!
        if (nextStep == "end") {
            println("Game over!")
            return
        } else{
            try {
                columnNumber = nextStep.toInt()
            } catch (e: NumberFormatException) {
                println("Incorrect column number")
                continue
            }

            if (columnNumber < 1 || columnNumber > boardSize[1]) {
                println("The column number is out of range (1 - ${boardSize[1]})")
                continue
            }

            freePosition = checkColumn(columnNumber, boardSize, board)

            if (freePosition == -1) {
                println("Column $columnNumber is full")
                continue
            } else {
                if (isPlayer1) {
                    board[freePosition][columnNumber * 2 - 1] = 'o'
                    isPlayer1 = false
                } else {
                    board[freePosition][columnNumber * 2 - 1] = '*'
                    isPlayer1 = true
                }
            }
        }
        printBoard(board)
    }
}

fun boardCreate (boardSize: List<Int>) : MutableList<MutableList<Char>> {
    val firstLane = mutableListOf<Char>()
    for (i in 1..boardSize[1]) {
        firstLane.add(' ')
        firstLane.add(i.digitToChar())
    }
    firstLane.add(' ')

    val board = mutableListOf(firstLane)

    val line = mutableListOf<Char>()
    for (i in 0 until boardSize[1]) {
        line.add('║')
        line.add(' ')
    }
    line.add('║')

    repeat(boardSize[0]) { board.add(line.toMutableList()) }

    val lastLine = mutableListOf<Char>()
    lastLine.add('╚')
    lastLine.add('═')
    for (i in 0 until boardSize[1] - 1) {
        lastLine.add('╩')
        lastLine.add('═')
    }
    lastLine.add('╝')

    board.add(lastLine)

    printBoard(board)
    return board
}

fun printBoard (board: MutableList<MutableList<Char>>) {
    for (i in 0 until board.size) {
        println(board[i].joinToString(""))
    }
}

fun checkColumn (columnNumber: Int,
                 boardSize: MutableList<Int>,
                 board: MutableList<MutableList<Char>>) :Int {
    for (i in boardSize[0] downTo 1) {
        if (board[i][columnNumber * 2 - 1] == ' ') return i
    }
    return -1
}