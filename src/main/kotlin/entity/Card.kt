package entity

data class Card (val suit: CardSuit,
                 val value: CardValue,
    ){
    override fun toString() ="$suit$value"
}