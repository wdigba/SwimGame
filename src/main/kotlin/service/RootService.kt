package service
import entity.*

class RootService {
    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)

    var currentGame : Swim? = null

}