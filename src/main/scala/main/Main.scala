package main

object Main {
  def main(args: Array[String]): Unit = {
    //sim params
    val nSharks = 0
    val nTuna = 1
    val worldLength = 10
    val sBreed = 3
    val sEnergy = 7
    val tBreed = 3

    //create the world
    val world = new World(worldLength, nSharks,nTuna, List(), List(), List(), tBreed, sBreed, sEnergy)

    //create the sharks
    val sharks = (1 to world.nSharks).map(_ => Shark(sBreed, sEnergy))

    //create the tuna
    val tuna = (1 to world.nTuna).map(_ => Tuna(tBreed))

    //create the location for each shark
    val sharkLocations = sharks.map(_ => world.randomEmptyLocation)

    //create the location for each tuna
    val tunaLocations = tuna.map(_ => world.randomEmptyLocation)

    //create the map of sharks to locations
    val sharkMap = (sharks zip sharkLocations).toList

    //create the map of tuna to locations
    val tunaMap = (tuna zip tunaLocations).toList

    //create the world with the sharks and tuna
    val worldWithFish = new World(world.length, world.nSharks, world.nTuna, sharkMap, tunaMap, world.occupiedLocations, world.tBreed, world.sBreed, world.sEnergy)

    //start cycles
    val cycles = 100
    var i = 0
    while (i < cycles){
      var tunaToMove = worldWithFish.tunaList
      var sharksToMove = worldWithFish.sharksList
      tunaToMove.foreach(tuna => {
        worldWithFish.moveTuna(tuna._1, tuna._2)
      })
      sharksToMove.foreach(shark => worldWithFish.moveShark(shark._1, shark._2))
      i = i + 1

    }

  }
}
