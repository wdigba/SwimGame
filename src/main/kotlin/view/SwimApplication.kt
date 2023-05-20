package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication
/**
 * implementation of the BGW [BoardGameApplication] for the "swim game"
 * the root, that coordinates all scenes of the game
 * @property rootService connects with service layer
 * @property gameScene BoardGameScene shows game itself
 * @property newGameMenuScene start scene with ability to choose players
 * @property endMenuScene end scene shows results and allows to restart game
 */
class SwimApplication : BoardGameApplication("Swim Game"), Refreshable {

    private val rootService = RootService()
    private val gameScene = SwimGameScene(rootService)
    // set actions related to adding and removing players
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
    // set actions related to start new game or quit
    private val endMenuScene = GameFinishedMenuScene(rootService).apply {
        exitButton.onMouseClicked = {
            exit()
        }
        newGameButton.onMouseClicked = {
            newGameMenuScene.resetInputs()
            this@SwimApplication.showMenuScene(newGameMenuScene)
            rootService.currentGame = null
        }
    }
    /** constructor initializes scenes and add option to refresh
     * */
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
    /** after game was started hides menu scene
     * */
    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
    }
    /** after game was ended shows end scene
     * */
    override fun refreshAfterCalculateWinner () {
        this.showMenuScene(endMenuScene)
    }

}

