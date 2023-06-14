package main

import scalafx.animation.{KeyFrame, Timeline}
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.{IntegerProperty, ObjectProperty}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.control.ButtonType.Apply.text
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color.{Black, Blue, Green, Red, White}
import scalafx.scene.shape.{Circle, Rectangle}
import scalafx.scene.text.Text
import scalafx.util.Duration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DisplaySim extends JFXApp3 {

  def createWorld(width: Int, nSharks: Int, nTuna: Int, sBreed: Int, tBreed: Int, sEnergy: Int): World = {
    val world = new World(width, nSharks, nTuna, List(), List(), List(),tBreed,sBreed,sEnergy)

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
    val worldWithFish = new World(world.length, world.nSharks, world.nTuna, sharkMap, tunaMap, world.occupiedLocations, tBreed, sBreed, sEnergy)
    worldWithFish
  }

  def updateWorld(world: World): World = {
    var tunaToMove = world.tunaList
    var sharksToMove = world.sharksList
    tunaToMove.foreach(tuna => world.moveTuna(tuna._1, tuna._2))
    sharksToMove.foreach(shark => world.moveShark(shark._1, shark._2))

    world
  }



  override def start(): Unit = {

    stage = new PrimaryStage {
      val worldLenght = new IntegerProperty(this, "worldLength", 10)
      val nSharks = new IntegerProperty(this, "nSharks", 5)
      val nTuna = new IntegerProperty(this, "nTuna", 10)
      val sBreed = new IntegerProperty(this, "sBreed", 5)
      val tBreed = new IntegerProperty(this, "tBreed", 3)
      val sEnergy = new IntegerProperty(this, "sEnergy", 7)
      val cycles = new IntegerProperty(this, "cycles", 100)
      title = "Predator Prey Simulation"
      width = 660
      height = 800
      scene = new Scene {

        fill = White
        val title = new Text("Predator Prey Simulation")
        title.layoutX = 20
        title.layoutY = 30
        //increase the font size of the title
        title.setStyle("-fx-font-size: 20pt;")

        val widthLabel = new Label("world width")
        widthLabel.layoutX = 20
        widthLabel.layoutY = 50

        val widthField = new TextField()
        widthField.layoutX = 20
        widthField.layoutY = 70
        widthField.text = "10"

        val nSharksLabel = new Label("number of sharks")
        nSharksLabel.layoutX = 200
        nSharksLabel.layoutY = 50

        val nSharksField = new TextField()
        nSharksField.layoutX = 200
        nSharksField.layoutY = 70
        nSharksField.text = "5"

        val sBreedLabel = new Label("number of cycles before reproduction for sharks")
        sBreedLabel.layoutX = 20
        sBreedLabel.layoutY = 150
        sBreedLabel.setMaxWidth(150)
        sBreedLabel.setWrapText(true)

        val sBreedField = new TextField()
        sBreedField.layoutX = 20
        sBreedField.layoutY = 190
        sBreedField.text = "7"

        val tBreedLabel = new Label("number of cycles before reproduction for tuna")
        tBreedLabel.layoutX = 200
        tBreedLabel.layoutY = 150
        tBreedLabel.setMaxWidth(150)
        tBreedLabel.setWrapText(true)

        val tBreedField = new TextField()
        tBreedField.layoutX = 200
        tBreedField.layoutY = 190
        tBreedField.text = "7"

        val sEnergyLabel = new Label("number of shark energy units")
        sEnergyLabel.layoutX = 380
        sEnergyLabel.layoutY = 150
        sEnergyLabel.setMaxWidth(150)
        sEnergyLabel.setWrapText(true)

        val sEnergyField = new TextField()
        sEnergyField.layoutX = 380
        sEnergyField.layoutY = 190
        sEnergyField.text = "5"

        val cyclesLabel = new Label("number of cycles to run simulation")
        cyclesLabel.layoutX = 20
        cyclesLabel.layoutY = 250
        cyclesLabel.setMaxWidth(150)
        cyclesLabel.setWrapText(true)

        val cyclesField = new TextField()
        cyclesField.layoutX = 20
        cyclesField.layoutY = 290
        cyclesField.text = "100"

        val nTunaLabel = new Label("number of tuna")
        nTunaLabel.layoutX = 380
        nTunaLabel.layoutY = 50

        val nTunaField = new TextField()
        nTunaField.layoutX = 380
        nTunaField.layoutY = 70
        nTunaField.text = "10"

        val startButton = new Button("Start the simulation")
        startButton.layoutX = 200
        startButton.layoutY = 250
        startButton.setPrefWidth(330)
        startButton.setPrefHeight(65)
        startButton.onMouseClicked = _ => {
          worldLenght.update(widthField.text.value.toInt)
          nSharks.update(nSharksField.text.value.toInt)
          nTuna.update(nTunaField.text.value.toInt)
          sBreed.update(sBreedField.text.value.toInt)
          tBreed.update(tBreedField.text.value.toInt)
          sEnergy.update(sEnergyField.text.value.toInt)
          cycles.update(cyclesField.text.value.toInt)
          //move to a new scene




          val simulationScene = new Scene {
            fill = White
            val title = new Text("Your simulation is running")
            title.layoutX = 15
            title.layoutY = 30
            //increase the font size of the title
            title.setStyle("-fx-font-size: 15pt;")


            val simulationRectangle = new Rectangle {
              x = 20
              y = 40
              width = 600
              height = 600
              fill = White
              stroke = Black
            }

            val sharkRectangle = new Rectangle {
              x = 20
              y = 650
              width = 30
              height = 30
              fill = Red
            }

            val tunaRectangle = new Rectangle {
              x = 20
              y = 700
              width = 30
              height = 30
              fill = Blue
            }

            val sharkLabel = new Text("Sharks")
            sharkLabel.layoutX = 60
            sharkLabel.layoutY = 670
            sharkLabel.setStyle("-fx-font-size: 15pt;")
            sharkLabel.setFill(Red)

            val tunaLabel = new Text("Tuna")
            tunaLabel.layoutX = 60
            tunaLabel.layoutY = 720
            tunaLabel.setStyle("-fx-font-size: 15pt;")
            tunaLabel.setFill(Blue)


            //create the world
            val world = createWorld(worldLenght.toInt, nSharks.toInt, nTuna.toInt, sBreed.toInt, tBreed.toInt, sEnergy.toInt)
            //draw the sharks and tunas in the world
            val sharkLocations = world.sharksList.map(_._2)
            val tunaLocations = world.tunaList.map(_._2)
            val sharkCircles = sharkLocations.map(location => new Circle {
              centerX = location.x * 600 / world.length + 30
              centerY = location.y * 600 / world.length + 50
              radius = 10
              fill = Red
            })
            val tunaCircles = tunaLocations.map(location => new Circle {
              centerX = location.x * 600 / world.length + 30
              centerY = location.y * 600 / world.length + 50
              radius = 10
              fill = Blue
            })
            //add the sharks and tunas to the world
            val sharkCirclesList = sharkCircles.toList
            val tunaCirclesList = tunaCircles.toList
            val worldCircles = sharkCirclesList ::: tunaCirclesList

            val timeline = new Timeline {
              cycleCount = cycles.value
              keyFrames = KeyFrame(Duration(300), onFinished = _ => update()) :: Nil
            }

            //create a startbutton
            val startButton = new Button("Start the simulation")
            startButton.layoutX = 200
            startButton.layoutY = 700
            startButton.onMouseClicked = _ => {
              //move to a new scene
              timeline.play()
            }

            content = List(title, simulationRectangle, sharkRectangle, tunaRectangle, sharkLabel, tunaLabel, startButton) ::: worldCircles

            //run the simulation
            def update(): Unit = {
              val newWorld = updateWorld(world)
              val newSharkLocations = newWorld.sharksList.map(_._2)
              val newTunaLocations = newWorld.tunaList.map(_._2)
              val newSharkCircles = newSharkLocations.map(location => new Circle {
                centerX = location.x * 600 / world.length + 30
                centerY = location.y * 600 / world.length + 50
                radius = 10
                fill = Red
              })
              val newTunaCircles = newTunaLocations.map(location => new Circle {
                centerX = location.x * 600 / world.length + 30
                centerY = location.y * 600 / world.length + 50
                radius = 10
                fill = Blue
              })
              val newSharkCirclesList = newSharkCircles.toList
              val newTunaCirclesList = newTunaCircles.toList
              val newWorldCircles = newSharkCirclesList ::: newTunaCirclesList
              content = List(title, simulationRectangle, sharkRectangle, tunaRectangle, sharkLabel, tunaLabel) ::: newWorldCircles
            }

            //run the simulation for the number of cycles specified by the user

          }

          stage.scene = simulationScene
        }




        content = List(title, widthLabel, widthField, nSharksLabel, nSharksField, nTunaLabel, nTunaField, sBreedLabel, sBreedField, tBreedLabel, tBreedField, sEnergyLabel, sEnergyField, cyclesLabel, cyclesField, startButton)


      }




    }

  }

}


