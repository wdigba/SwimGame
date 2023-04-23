package entity
/**
 * A data class representing a playing card
 * @property suit the suit of the card, as [CardSuit] object
 * @property value the value of the card, as [CardValue] object
 * */
data class Card (val suit: CardSuit,
                 val value: CardValue,
    ){
    /**
     *@return string representation of the card, in the format "[suit] [value]"
     * */
    override fun toString() ="$suit$value"
}