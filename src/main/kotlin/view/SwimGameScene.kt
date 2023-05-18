package view

import entity.*
import service.*
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import java.awt.Color
import tools.aqua.bgw.visual.CompoundVisual

/**[BoardGameScene] provides representation of the game itself
 * all players keep their hand cards and located at the edges of the table
 * stack and middle cards, that can be used, located in the center
 * at first all cards of a player are unrevealed
 * with [revealCards] player can open hand cards and do some actions
 * before the hand cards are revealed, no action is available
 * with [switchAllButton] player can change all hand cards with middle cards
 * with [switchOneButton] player can change one card from hand with one card from the middle
 * with [passButton] player can pass
 * with [knockButton] player can knock to finish the game
 * "swim game" finishes automatically when one of the termination conditions was reached
 * in particular: when stack ran out of cards
 * or the last player of the last round has made an action
 * */
class SwimGameScene(private val rootService: RootService):
    BoardGameScene(1920, 1080), Refreshable {
    private var playerAmount = rootService.currentGame?.playerList?.size

    private var layouts = mutableListOf<LinearLayout<CardView>>()

    private var nameLabels = mutableListOf<Label>()

    private var playerCardIndex = 0

    private var midCardIndex = 0

    private var currentPlayerHandLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = 650,
        posY = 800,
        spacing = 30,
        alignment = Alignment.CENTER
    )

    private var currentPlayerLabel = Label(
        width = 200,
        height = 50,
        posX = 850,
        posY = 740,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private var topPlayerHandLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = 650,
        posY = 50,
        spacing = 30,
        alignment = Alignment.CENTER
    )

    private var topPlayerLabel = Label(
        width = 200,
        height = 50,
        posX = 850,
        posY = 280,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private var leftPlayerHandLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = -100,
        posY = 425,
        spacing = 30,
        alignment = Alignment.CENTER
    )

    private var leftPlayerLabel = Label(
        width = 200,
        height = 50,
        posX = 100,
        posY = 250,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private var rightPlayerHandLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = 1410,
        posY = 425,
        spacing = 30,
        alignment = Alignment.CENTER
    )

    private var rightPlayerLabel = Label(
        width = 200,
        height = 50,
        posX = 1610,
        posY = 250,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private var midCardsLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = 650,
        posY = 425,
        spacing = 30,
        alignment = Alignment.CENTER
    )

    private var cardStackLayout: CardStack<CardView> = CardStack(
        height = 220,
        width = 150,
        posX = 1200,
        posY = 425,
        alignment = Alignment.CENTER
    )

    // action buttons
    private val switchAllButton = Button(
        height = 70,
        width = 250,
        posX = 100,
        posY = 940,
        text = "switch all",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.switchAllCards()
        }
    }

    private val switchOneButton = Button(
        height = 70,
        width = 250,
        posX = 380,
        posY = 940,
        text = "switch one",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            removeComponents(this, knockButton, switchAllButton, passButton)
            tableTextLabel.text = "click on your card"
            addComponents(switchHandCardButton1, switchHandCardButton2, switchHandCardButton3)
        }
    }
    // buttons to choose card from hand cards
    private val switchHandCardButton1 = Button(
        height = 200,
        width = 120,
        posX = 740,
        posY = 810,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeHandSwapButtons(0)
        }
    }

    private val switchHandCardButton2 = Button(
        height = 200,
        width = 120,
        posX = 890,
        posY = 810,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeHandSwapButtons(1)
        }
    }

    private val switchHandCardButton3 = Button(
        height = 200,
        width = 120,
        posX = 1040,
        posY = 810,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeHandSwapButtons(2)
        }
    }
    /** desired card from hand cards was chosen
     * */
    private fun removeHandSwapButtons( switchCard: Int ){
        playerCardIndex = switchCard
        removeComponents(switchHandCardButton1, switchHandCardButton2, switchHandCardButton3)
        tableTextLabel.text = "click on middle card"
        addComponents(switchMidCardButton1, switchMidCardButton2, switchMidCardButton3)
    }
    // buttons to choose card from the middle
    private val switchMidCardButton1 = Button(
        height = 200,
        width = 120,
        posX = 740,
        posY = 435,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeTableSwapButtons( 0 )
        }
    }

    private val switchMidCardButton2 = Button(
        height = 200,
        width = 120,
        posX = 890,
        posY = 435,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeTableSwapButtons( 1 )
        }
    }

    private val switchMidCardButton3 = Button(
        height = 200,
        width = 120,
        posX = 1040,
        posY = 435,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeTableSwapButtons( 2 )
        }
    }
    /** desired card from the middle was chosen
     * */
    private fun removeTableSwapButtons( switchCard: Int ){
        midCardIndex = switchCard
        removeComponents(switchMidCardButton1, switchMidCardButton2, switchMidCardButton3)
        tableTextLabel.text = ""
        addComponents(knockButton, switchAllButton, passButton, switchOneButton)
        rootService.playerActionService.switchOneCard(midCardIndex, playerCardIndex)
    }

    private val knockButton = Button(
        height = 70,
        width = 250,
        posX = 1570,
        posY = 940,
        text = "knock",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.knock()
        }
    }
    // text on the table, is shown when somebody has knocked
    // or it´s time to choose cards (for "switch one card")
    private val tableTextLabel = Label(
        height = 60,
        width = 500,
        posX = 1300,
        posY = 850,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private val passButton = Button(
        height = 70,
        width = 250,
        posX = 1290,
        posY = 940,
        text = "pass",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.pass()
        }
    }
    // the very first button
    // without pressing it no actions available
    private val revealCards = Button (
        height = 70,
        width = 250,
        posX = 100,
        posY = 850,
        text = "reveal cards",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.revealCards()
            currentPlayerHandLayout.forEach { it.showFront() }
            addComponents(switchAllButton, switchOneButton, knockButton, passButton)
            removeComponents(this)
        }
    }

    private val cardStackCountLabel = Label(
        height = 60,
        width = 200,
        posX = 1175,
        posY = 630,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 25)
    )
    /** initialization of visual representation of cards
     * */
    private fun insertCards(cards: List<Card>, layout: LinearLayout<CardView>){
        for(i in cards.indices){
            val cardImageLoader = CardImageLoader()
            val currentCard = CardView(
                height = 200,
                width = 120,
                front = ImageVisual(cardImageLoader.frontImageFor(cards[i].suit, cards[i].value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            layout.add(currentCard)
        }
    }
    /** creates stack of cards
     * */
    private fun createStack(stack: MutableList<Card>, tableStack: CardStack<CardView>){
        for (i in stack.indices){
            val cardImageLoader = CardImageLoader()
            val currentCard = CardView(
                height = 200,
                width = 120,
                front = ImageVisual(cardImageLoader.frontImageFor(
                    stack[i].suit, stack[i].value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            tableStack.add(currentCard)
        }
    }
    /** clears the table from previous components
     * when new game was started
     * */
    private fun clearMap(){
        val components = arrayOf(midCardsLayout, cardStackLayout, currentPlayerHandLayout,
            topPlayerHandLayout, leftPlayerHandLayout, rightPlayerHandLayout,
            currentPlayerLabel, topPlayerLabel, leftPlayerLabel, rightPlayerLabel,
            cardStackCountLabel, revealCards, tableTextLabel, switchAllButton, switchOneButton, knockButton, passButton)
        val layouts = arrayOf(midCardsLayout, cardStackLayout, currentPlayerHandLayout,
            topPlayerHandLayout, leftPlayerHandLayout, rightPlayerHandLayout)
        for ( i in layouts ){
            i.clear()
        }
        for ( i in components ){
            removeComponents(i)
        }
        tableTextLabel.text = ""
    }
    /** changes visual representation of current player and his cards
     * same functionality as "next person´s turn"
     * */
    private fun changeCurrentPlayer(
        layouts: MutableList<LinearLayout<CardView>>, nameLabels: MutableList<Label>){
        val tempCardsList = mutableListOf<List<CardView>>()
        val tempNameList = mutableListOf<String>()
        for ( i in layouts){
            tempCardsList.add(i.components)
            i.clear()
        }
        for(i in nameLabels){
            tempNameList.add(i.text)
        }
        for( i in 0 until layouts.size ){
            layouts[i].addAll(tempCardsList[(i+1) % layouts.size])
            nameLabels[i].text = tempNameList[(i+1) % layouts.size]
            layouts[i].forEach{ CardView -> CardView.showBack() }
        }
        if (rootService.currentGame?.actPlayer?.cardsRevealed!!) {
            currentPlayerHandLayout.forEach{ CardView -> CardView.showFront() }
            return
        }
        addComponents(revealCards)
        removeComponents(switchAllButton, switchOneButton, knockButton, passButton)
        currentPlayerHandLayout.forEach{ CardView -> CardView.showBack() }
    }
    /** initialisation with two players and background
     * */
    init {
        leftPlayerHandLayout.rotate(90)
        rightPlayerHandLayout.rotate(-90)
        background = ColorVisual(2, 74, 5)
    }
    /** representation of the whole game
     * based on the amount of players
     * */
    override fun refreshAfterStartNewGame() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        playerAmount = rootService.currentGame?.playerList?.size

        clearMap()

        addComponents(midCardsLayout, cardStackLayout, tableTextLabel, revealCards)

        val playerNames = game.playerList

        when( playerAmount ) {
            4 -> {
                layouts = mutableListOf(
                    currentPlayerHandLayout, leftPlayerHandLayout,
                    topPlayerHandLayout, rightPlayerHandLayout
                )
                nameLabels = mutableListOf(
                    currentPlayerLabel, leftPlayerLabel,
                    topPlayerLabel, rightPlayerLabel
                )
                for (i in 0..3) {
                    nameLabels[i].text = playerNames[i].playerName
                    addComponents(layouts[i], nameLabels[i])
                    insertCards(game.playerList[i].handCards, layouts[i])
                }
                currentPlayerHandLayout.forEach { CardView -> CardView.showBack() }
                insertCards(game.midCards, midCardsLayout)
                midCardsLayout.forEach { CardView -> CardView.showFront() }
            }

            3 -> {
                layouts = mutableListOf(
                    currentPlayerHandLayout, leftPlayerHandLayout,
                    rightPlayerHandLayout
                )
                nameLabels = mutableListOf(currentPlayerLabel, leftPlayerLabel, rightPlayerLabel)
                for (i in 0..2) {
                    nameLabels[i].text = playerNames[i].playerName
                    addComponents(layouts[i], nameLabels[i])
                    insertCards(game.playerList[i].handCards, layouts[i])
                }
                currentPlayerHandLayout.forEach { CardView -> CardView.showBack() }
                insertCards(game.midCards, midCardsLayout)
                midCardsLayout.forEach { CardView -> CardView.showFront() }
            }
            else -> {
                layouts = mutableListOf(currentPlayerHandLayout, topPlayerHandLayout)
                nameLabels = mutableListOf(currentPlayerLabel, topPlayerLabel)
                for (i in 0..1) {
                    nameLabels[i].text = playerNames[i].playerName
                    addComponents(layouts[i], nameLabels[i])
                    insertCards(game.playerList[i].handCards, layouts[i])
                }
                currentPlayerHandLayout.forEach { CardView -> CardView.showBack() }
                insertCards(game.midCards, midCardsLayout)
                midCardsLayout.forEach { CardView -> CardView.showFront() }
            }
        }
        createStack(game.deckCards, cardStackLayout)
        cardStackCountLabel.text = "${rootService.currentGame?.deckCards?.size} cards left"
        addComponents(cardStackCountLabel)
    }
    /** representation of next player´s turn
     * */
    override fun refreshAfterChangeToNextPerson() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}

        val delay = DelayAnimation(2000)
        delay.onFinished = {
            changeCurrentPlayer(layouts, nameLabels)
            unlock()
        }
        lock()
        playAnimation(delay)
    }
    /** representation after all 3 cards were switched
     * */
    override fun refreshAfterSwitchAllCards() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}

        midCardsLayout.clear()
        insertCards(game.midCards, midCardsLayout)
        midCardsLayout.forEach { CardView -> CardView.showFront() }

        currentPlayerHandLayout.clear()
        insertCards(game.actPlayer.handCards, layouts[0])
        currentPlayerHandLayout.forEach { CardView -> CardView.showFront() }
    }
    /** representation after pass
     * */
    override fun refreshAfterPass() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}

        midCardsLayout.clear()
        insertCards(game.midCards, midCardsLayout)
        midCardsLayout.forEach { CardView -> CardView.showFront() }

        createStack(game.deckCards, cardStackLayout)
        cardStackCountLabel.text = "${rootService.currentGame?.deckCards?.size} cards left"
    }
    /** representation after 1 card was changed
     * */
    override fun refreshAfterSwitchOneCard() {

        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}

        midCardsLayout.clear()
        insertCards(game.midCards, midCardsLayout)
        midCardsLayout.forEach { CardView -> CardView.showFront() }

        currentPlayerHandLayout.clear()
        insertCards(game.actPlayer.handCards, layouts[0])
        currentPlayerHandLayout.forEach { CardView -> CardView.showFront() }
    }
    /** representation after player has knocked
     * */
    override fun refreshAfterKnock() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        tableTextLabel.text = "${game.actPlayer.playerName} has knocked"
    }
}