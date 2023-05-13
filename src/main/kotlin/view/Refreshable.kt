package view

/**
 * This interface provides a mechanism by which service layer classes tell to view classes
 * that some changes have been made to the object layer so that the user´s interface can be updated
 * */
interface Refreshable {
    /**
     * perform refreshes after a new game started
     */
    fun refreshAfterStartNewGame() {}
    /**
     * perform refreshes after we changed turn to another player
     */
    fun refreshAfterChangeToNextPerson() {}
    /**
     * perform refreshes after winner calculation (when game was over)
     */
    fun refreshAfterCalculateWinner() {}
    /**
     * perform refreshes after player decided to pass
     */
    fun refreshAfterPass() {}
    /**
     * perform refreshes after player decided to knock
     */
    fun refreshAfterKnock() {}
    /**
     * perform refreshes after player decided to change one card with middle card
     */
    fun refreshAfterSwitchOneCard() {}
    /**
     * perform refreshes after player decided to change all cards with middle cards
     */
    fun refreshAfterSwitchAllCards() {}
}