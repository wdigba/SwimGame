package view

import service.*
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.visual.CompoundVisual

/**[MenuScene] displayed before starting a new game
 * we can write players´ names and start playing directly after [startGameButton] was pressed
 * without names game will not be started
 * [quitButton] is used to end the program
 * click + to add a new player with [addPlayerButton]
 * click - to remove the player with [removePLayerButton]
 * when we click on these buttons more than acceptable, nothing happens
 * */
class NewGameMenuScene (private val rootService: RootService) :
    MenuScene(1000, 1000), Refreshable {
    /* variables player(Int)Label related to the field "Player (Int)"
     * variables player(Int)Input related to the field with entered name
     * */
    private val player1Label = Label(
        width = 200,
        height = 60,
        posX = 80,
        posY = 350,
        text = "Player 1",
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private val player1Input = TextField(
        width =600,
        height = 60,
        posX = 300,
        posY = 350,
        prompt = "",
        font = Font(fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private val player2Label = Label(
        width = 200,
        height = 60,
        posX = 80,
        posY = 430,
        text = "Player 2",
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private val player2Input = TextField(
        width =600,
        height = 60,
        posX = 300,
        posY = 430,
        prompt = "",
        font = Font(fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    val player3Label = Label(
        width = 200,
        height = 60,
        posX = 80,
        posY = 510,
        text = "Player 3",
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    val player3Input = TextField(
        width =600,
        height = 60,
        posX = 300,
        posY = 510,
        prompt = "",
        font = Font(fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    val player4Label = Label(
        width = 200,
        height = 60,
        posX = 80,
        posY = 590,
        text = "Player 4",
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    val player4Input = TextField(
        width =600,
        height = 60,
        posX = 300,
        posY = 590,
        prompt = "",
        font = Font(fontWeight = Font.FontWeight.LIGHT, size = 30)
    )
    // button to start new game
    private val startGameButton = Button(
        width = 350,
        height = 80,
        posX = 300,
        posY = 800,
        text = "START",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 36),
        visual = ColorVisual(255,255,255)
    ).apply {//animation for the button
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
        onMouseClicked = {
            rootService.gameService.startNewGame(getPlayers())
        }
    }
    // button to quit the game
    val quitButton = Button(
        width = 150,
        height = 80,
        posX = 100,
        posY = 800,
        text = "QUIT",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 36),
        visual = ColorVisual(255, 255, 255)
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
    // button to add more players (max 4)
    val addPlayerButton = Button(
        width = 80,
        height = 80,
        posX = 800,
        posY = 800,
        text = "+",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 36),
        visual = ColorVisual(255, 255, 255)
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
    // button to remove players (min 2)
    val removePLayerButton = Button(
        width = 80,
        height = 80,
        posX = 700,
        posY = 800,
        text = "-",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 36),
        visual = ColorVisual(255, 255, 255)
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
    /**function to get inputted players´ names to start a new game
     * */
    private fun getPlayers() : List<String> {
        val result = mutableListOf<String>()
        if(player1Input.text.trim() != ""){
            result.add(player1Input.text.trim())
        }
        if(player2Input.text.trim() != ""){
            result.add(player2Input.text.trim())
        }
        if(player3Input.text.trim() != ""){
            result.add(player3Input.text.trim())
        }
        if(player4Input.text.trim() != ""){
            result.add(player4Input.text.trim())
        }
        return result
    }
    /** clears input when we want to start a new game from "game finished scene"
     * */
    fun resetInputs() {
        player1Input.text = ""
        player2Input.text = ""
        player3Input.text = ""
        player4Input.text = ""
    }

    /**
     * default structure with two players
     * */
    init {
        background = ColorVisual(2, 74, 5)
        addComponents(
            player1Label,
            player2Label,
            player1Input,
            player2Input,
            startGameButton,
            quitButton,
            addPlayerButton,
            removePLayerButton,
        )
    }
}