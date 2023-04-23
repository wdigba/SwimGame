package entity

data class Player (val playerName: String,
              var handCards:MutableList<Card>,
              var points : Double = 0.0,
    ){
}