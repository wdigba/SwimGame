package view

import service.*
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class SwimGameScene(private val rootService: RootService):
    BoardGameScene(1920, 1080, ColorVisual(109, 199, 97)), Refreshable {
}