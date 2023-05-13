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
        font = Font(fontWeight = Font.FontWeight.LIGHT, size = 30)
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
        posY = 170,
        font = Font(fontWeight = Font.FontWeight.LIGHT, size = 30)
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
        posY = 170,
        font = Font(fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private var midCardsLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = 550,
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

    //action buttons

    private val switchAllButton = Button(
        height = 60,
        width = 200,
        posX = 30,
        posY = 900,
        text = "switch all",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 25)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.switchAllCards()
        }
    }

    private val switchOneButton = Button(
        height = 60,
        width = 200,
        posX = 260,
        posY = 900,
        text = "switch one",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 25)
    ).apply {
        onMouseClicked = {
            removeComponents(this, knockButton, switchAllButton, passButton)
            actionPersonLabel.text = "click on your card"
            addComponents(switchHandCardButton1, switchHandCardButton2, switchHandCardButton3)
        }
    }

    private val switchHandCardButton1 = Button(
        height = 50,
        width = 120,
        posX = 720,
        posY = 1025,
        text = "Pick",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, size = 25)
    ).apply {
        onMouseClicked = {
            removeHandSwapButtons(0)
        }
    }

    private val switchHandCardButton2 = Button(
        height = 50,
        width = 120,
        posX = 890,
        posY = 1025,
        text = "Pick",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, size = 25)
    ).apply {
        onMouseClicked = {
            removeHandSwapButtons(1)
        }
    }

    private val switchHandCardButton3 = Button(
        height = 50,
        width = 120,
        posX = 1060,
        posY = 1025,
        text = "Pick",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, size = 25)
    ).apply {
        onMouseClicked = {
            removeHandSwapButtons(2)
        }
    }

    private fun removeHandSwapButtons( switchCard: Int ){
        playerCardIndex = switchCard
        removeComponents(switchHandCardButton1, switchHandCardButton2, switchHandCardButton3)
        actionPersonLabel.text = "choose card from middle"
        addComponents(switchMidCardButton1, switchMidCardButton2, switchMidCardButton3)
    }

    private val switchMidCardButton1 = Button(
        height = 50,
        width = 120,
        posX = 620,
        posY = 650,
        text = "Pick",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, size = 25)
    ).apply {
        onMouseClicked = {
            removeTableSwapButtons( 0 )
        }
    }

    private val switchMidCardButton2 = Button(
        height = 50,
        width = 120,
        posX = 790,
        posY = 650,
        text = "Pick",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, size = 25)
    ).apply {
        onMouseClicked = {
            removeTableSwapButtons( 1 )
        }
    }

    private val switchMidCardButton3 = Button(
        height = 50,
        width = 120,
        posX = 960,
        posY = 650,
        text = "Pick",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, size = 25)
    ).apply {
        onMouseClicked = {
            removeTableSwapButtons( 2 )
        }
    }

    private fun removeTableSwapButtons( switchCard: Int ){
        midCardIndex = switchCard
        removeComponents(switchMidCardButton1, switchMidCardButton2, switchMidCardButton3)
        actionPersonLabel.text = ""
        addComponents(knockButton, switchAllButton, passButton, switchOneButton)
        rootService.playerActionService.switchOneCard(midCardIndex, playerCardIndex)
    }

    private val knockButton = Button(
        height = 60,
        width = 200,
        posX = 30,
        posY = 980,
        text = "knock",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 25)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.knock()
        }
    }

    private val actionPersonLabel = Label(
        height = 60,
        width = 500,
        posX = 1300,
        posY = 900,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private val passButton = Button(
        height = 60,
        width = 200,
        posX = 260,
        posY = 980,
        text = "pass",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 25)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.pass()
        }
    }

    //label for the amount of cards in the stack left
    private val cardStackCountLabel = Label(
        height = 60,
        width = 200,
        posX = 1175,
        posY = 630,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 25)
    )

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

    private fun clearMap(){
        val components = arrayOf(midCardsLayout, cardStackLayout, currentPlayerHandLayout,
            topPlayerHandLayout, leftPlayerHandLayout, rightPlayerHandLayout,
            currentPlayerLabel, topPlayerLabel, leftPlayerLabel, rightPlayerLabel,
            cardStackCountLabel, actionPersonLabel)
        val layouts = arrayOf(midCardsLayout, cardStackLayout, currentPlayerHandLayout,
            topPlayerHandLayout, leftPlayerHandLayout, rightPlayerHandLayout)
        for ( i in layouts ){
            i.clear()
        }
        for ( i in components ){
            removeComponents(i)
        }
        actionPersonLabel.text = ""
    }

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
        currentPlayerHandLayout.forEach{ CardView -> CardView.showFront() }
    }

    init {
        leftPlayerHandLayout.rotate(90)
        rightPlayerHandLayout.rotate(-90)
        background = ColorVisual(2, 74, 5)
        addComponents(switchAllButton, switchOneButton, knockButton, passButton)
    }

    override fun refreshAfterStartNewGame() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        playerAmount = rootService.currentGame?.playerList?.size

        clearMap()

        addComponents(midCardsLayout, cardStackLayout, actionPersonLabel)

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
                currentPlayerHandLayout.forEach { CardView -> CardView.showFront() }
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
                currentPlayerHandLayout.forEach { CardView -> CardView.showFront() }
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
                currentPlayerHandLayout.forEach { CardView -> CardView.showFront() }
                insertCards(game.midCards, midCardsLayout)
                midCardsLayout.forEach { CardView -> CardView.showFront() }
            }
        }
        createStack(game.deckCards, cardStackLayout)
        cardStackCountLabel.text = "${rootService.currentGame?.deckCards?.size} cards left"
        addComponents(cardStackCountLabel)
    }

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

    override fun refreshAfterPass() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}

        midCardsLayout.clear()
        insertCards(game.midCards, midCardsLayout)
        midCardsLayout.forEach { CardView -> CardView.showFront() }

        createStack(game.deckCards, cardStackLayout)
        cardStackCountLabel.text = "${rootService.currentGame?.deckCards?.size} cards left"
    }

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

    override fun refreshAfterKnock() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        actionPersonLabel.text = "${game.actPlayer.playerName} has knocked"
    }

}