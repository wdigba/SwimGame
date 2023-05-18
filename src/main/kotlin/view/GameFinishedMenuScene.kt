package view

import service.RootService
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import java.awt.Color

/**[MenuScene] displayed after the game was finished
 * it provides the information about winner
 * and also shows list of players and their points
 * we can start new game with [newGameButton]
 * or quit the game with [exitButton]
 * */
class GameFinishedMenuScene (private val rootService: RootService):
    MenuScene(1000,1000), Refreshable{
        // labels to output players and their points
    private val playerWinLabel = Label(
        width = 800,
        height = 70,
        posX = 100,
        posY = 200,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 60)
    )

    private val finalResult = Label(
        width = 300,
        height = 70,
        posX = 350,
        posY = 300,
        text = "final results:",
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 36),
        alignment = Alignment.CENTER
    )

    private val player1Points = Label(
        width = 800,
        height = 70,
        posX = 80,
        posY = 420,
        alignment = Alignment.CENTER,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )
    private val player2Points = Label(
        width = 800,
        height = 70,
        posX = 80,
        posY = 480,
        alignment = Alignment.CENTER,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private val player3Points = Label(
        width = 800,
        height = 70,
        posX = 80,
        posY = 540,
        alignment = Alignment.CENTER,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private val player4Points = Label(
        width = 800,
        height = 70,
        posX = 80,
        posY = 600,
        alignment = Alignment.CENTER,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )
    // button to start new game (starts from "new game scene")
    val newGameButton = Button(
        width = 350,
        height = 80,
        posX = 125,
        posY = 800,
        text = "NEW GAME",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 36)
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.WHITE.apply {
                transparency = 1.0
            })
        }
    }
    // button to quit the game
    val exitButton = Button(
        width = 350,
        height = 80,
        posX = 525,
        posY = 800,
        text = "EXIT",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 36)
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.9
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.WHITE.apply {
                transparency = 1.0
            })
        }
    }
    /** cleans table to reveal new results
     * */
    private fun clearMap(){
        removeComponents(playerWinLabel, finalResult, player1Points, player2Points,
            player3Points, player4Points)
    }
    /** constructor provides background color and adds buttons
     * */
    init {
        background = ColorVisual(2, 74, 5)
        addComponents(newGameButton, exitButton)
    }
    /** method shows players with their names and amount of points
     * provides list of players in descending order based on points
     * when 2 or more players have the same number of points
     * winner is the first listed person
     * */
    private fun showPlayerPoints(){
        val game = rootService.currentGame
        checkNotNull(game) {"No started game found."}

        val playerLabels = listOf(player1Points, player2Points, player3Points, player4Points)

        val sortedPlayers = game.playerList.sortedByDescending { it.points }

        playerWinLabel.text = "${sortedPlayers[0].playerName.uppercase()} WON!"
        addComponents(playerWinLabel, finalResult)
        // choose how many players table should show
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
    /** after results were calculated it clears the table
     * for following games and shows current result
     * */
    override fun refreshAfterCalculateWinner() {
        clearMap()
        showPlayerPoints()
    }
}