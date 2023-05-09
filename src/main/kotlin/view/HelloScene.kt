package view

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

class HelloScene : BoardGameScene(500, 500) {

    private val helloLabel = Label(
        width = 500,
        height = 500,
        posX = 0,
        posY = 0,
        text = "Hi beauty!",
        font = Font(size = 50)
    )

    init {
        background = ColorVisual(121, 50, 168)
        addComponents(helloLabel)
    }

}