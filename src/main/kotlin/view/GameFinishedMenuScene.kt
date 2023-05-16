package view

import service.RootService
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
class GameFinishedMenuScene (private val rootService: RootService):
    MenuScene(1000,1000), Refreshable{
    private val playerWinLabel = Label(
        width = 800,
        height = 70,
        posX = 100,
        posY = 200,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.BOLD, size = 60)
    )

    private val finalResult = Label(
        width = 800,
        height = 70,
        posX = -200,
        posY = 300,
        text = "Final Results:",
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.BOLD, size = 36)
    )

    private val player1Points = Label(
        width = 800,
        height = 70,
        posX = 80,
        posY = 360,
        alignment = Alignment.CENTER,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.BOLD, size = 30)
    )
    private val player2Points = Label(
        width = 800,
        height = 70,
        posX = 80,
        posY = 420,
        alignment = Alignment.CENTER,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.BOLD, size = 30)
    )

    private val player3Points = Label(
        width = 800,
        height = 70,
        posX = 80,
        posY = 480,
        alignment = Alignment.CENTER,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.BOLD, size = 30)
    )

    private val player4Points = Label(
        width = 800,
        height = 70,
        posX = 80,
        posY = 540,
        alignment = Alignment.CENTER,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.BOLD, size = 30)
    )

    val newGameButton = Button(
        width = 350,
        height = 80,
        posX = 125,
        posY = 800,
        text = "NEW GAME",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, size = 36)
    )

    val exitButton = Button(
        width = 350,
        height = 80,
        posX = 525,
        posY = 800,
        text = "EXIT",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, size = 36)
    )

    private fun clearMap(){
        removeComponents(playerWinLabel, finalResult, player1Points, player2Points,
            player3Points, player4Points)
    }

    init {
        background = ColorVisual(2, 74, 5)
        addComponents(newGameButton, exitButton)
    }

    private fun showPlayerPoints(){
        val game = rootService.currentGame
        checkNotNull(game) {"No started game found."}

        val playerLabels = listOf(player1Points, player2Points, player3Points, player4Points)

        val sortedPlayers = game.playerList.sortedByDescending { it.points }

        playerWinLabel.text = "${sortedPlayers[0].playerName.uppercase()} WON!"
        addComponents(playerWinLabel, finalResult)

        when( game.playerList.size ){
            4 -> {
                for( i in 0 until 4 ){
                    playerLabels[i].text = "Player ${sortedPlayers[i].playerName}:  " +
                            "${sortedPlayers[i].points} points"
                    addComponents(playerLabels[i])
                }
            }
            3 -> {
                for( i in 0 until 3 ){
                    playerLabels[i].text = "Player ${sortedPlayers[i].playerName}:  " +
                            "${sortedPlayers[i].points} points"
                    addComponents(playerLabels[i])
                }
            }
            else -> {
                for( i in 0 until 2 ){
                    playerLabels[i].text = "Player ${sortedPlayers[i].playerName}:  " +
                            "${sortedPlayers[i].points} points"
                    addComponents(playerLabels[i])
                }
            }
        }
    }

    override fun refreshAfterCalculateWinner() {
        clearMap()
        showPlayerPoints()
    }
}