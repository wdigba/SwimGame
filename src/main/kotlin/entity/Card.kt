package entity

import kotlin.random.Random
/*Data structure for all Cards in game, provides stack-like construction
* */
data class Card (val suit: CardSuit,
                 val value: CardValue,
    ) {

    override fun toString() ="$suit$value"
}