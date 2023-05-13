package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication
/**
 * Implementation of the BGW [BoardGameApplication] for the "Swim Game"
 */
class SwimApplication : BoardGameApplication("Swim Game"), Refreshable {

    private val rootService = RootService()
    private val gameScene = SwimGameScene(rootService)
    private val newGameMenuScene = NewGameMenuScene(rootService).apply {
        var playerCount = 2
        quitButton.onMouseClicked = {
            exit()
        }
        addPlayerButton.onMouseClicked = {
            if(playerCount == 2){
                addComponents(player3Input, player3Label)
                playerCount++
            }
            else if(playerCount == 3){
                addComponents(player4Input, player4Label)
                playerCount++
            }
        }
        removePLayerButton.onMouseClicked = {
            if(playerCount == 3){
                removeComponents(player3Input, player3Label)
                player3Input.text = ""
                playerCount--
            }
            else if(playerCount == 4){
                removeComponents(player4Input, player4Label)
                player4Input.text = ""
                playerCount--
            }
        }
    }

    private val endMenuScene = GameFinishedMenuScene(rootService).apply {
        exitButton.onMouseClicked = {
            exit()
        }
    }

   init {
       this.showMenuScene(newGameMenuScene)
       this.showGameScene(gameScene)
       rootService.addRefreshables(
           this,
           newGameMenuScene,
           gameScene,
           endMenuScene
       )
    }
    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
    }
    override fun refreshAfterCalculateWinner () {
        this.showMenuScene(endMenuScene)
    }

}

