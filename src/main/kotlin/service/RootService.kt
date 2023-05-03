package service
import entity.*
//import view.Refreshable

/**
 * Main class of the service layer, which serves as the central controller of the Swim Game. Connect the application layers.
 * Provides access to all other service classes and holds the [currentGame] state for these
 * services to access.
 */
class RootService {
    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)
    /**
     * Current active game. Can be `null` if the game hasn't started yet.
     */
    var currentGame : Swim? = null
    /*
    /**
     * Adds the provided [newRefreshable] to all services connected
     * to this root service
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }
    */
}