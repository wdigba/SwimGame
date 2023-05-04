package service
import view.Refreshable
/**
 * Abstract service class that handles multiples [Refreshable]s (usually UI elements) which are notified
 * of changes to refresh via the [onAllRefreshables] method
 * */
abstract class AbstractRefreshingService {
    private val refreshables = mutableListOf<Refreshable>()
    /**
     * adds a new [Refreshable]to the list
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        refreshables += newRefreshable
    }
    /**
     * Executes the [method] on the [Refreshable]s in the list
     * @param method the specific method which will be called on every [Refreshable]
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }

}
