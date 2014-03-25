

object movement {
  class direction(dir : String) {
    var current :String = findFirst(dir)
    var previous : String = previousdir(current)
    var possiblemoves = moves

    def errorCheck(dir : String) : String = dir.toLowerCase match { 
      case "north" => "north"
      case "south" => "south"
      case "east" => "east"
      case "west" => "west"
      case "northeast" => "northeast"
      case "northwest" => "northwest"
      case "southeast" => "southeast"
      case "southwest" => "southwest"
      case _ => "Error"
    }
    def findFirst(dir : String) : String = dir match {
      case "" => {if (me.look.exits.length == 1) { me.look.exits(0)} else {throw new IllegalArgumentException ("Please enter a blank string only when there is only one possible direction to move")}}
      case _ => {if (errorCheck(dir) == "Error") {throw new IllegalArgumentException ("You need to enter a valid direction (North, South,East, West)")} else {errorCheck(dir)}}
    }

    def previousdir(dir : String) : String = dir match {
      case "north" => "south"
      case "south" => "north"
      case "east" => "west"
      case "west" =>"east"
      case "northeast" => "southwest"
      case "northwest" => "southeast"
      case "southeast" => "northwest"
      case "southwest" => "northeast"
    }

    def goodmove(possible : String, previo : String) : Boolean = possible match {
       case `previo` => false
       case _ => true
    } 

    def moves():List[Int] = {
      var movelist = List[Int]()
      for (x : Int <- 0 to me.look.exits.length - 1) {
        if (goodmove(me.look.exits(x), previous)) {
          movelist :::= List(x)
        }
      }
      return movelist
    }
  }

  class movenext(location : direction) {
    var position = location
    var nextmove = findmove

    def findmove() : String = {
      var dir = ""
      if (me.look.exits.length == 1) {
        dir = me.look.exits(0)
      } else if (position.possiblemoves.length >= 1) {
        var num = new scala.util.Random()
        dir = me.look.exits(position.possiblemoves(num.nextInt(position.possiblemoves.length)))
      } else {
        dir = "none"
      }
      return dir
    }
  }

  class movetostop() {
    var steps = 0
    def continue():Boolean = {
      (me.look.features.isEmpty && me.look.monsters.isEmpty && me.look.adventurers.isEmpty && !me.look.exits.isEmpty && steps < 15)
    }

    def move(first : String) = { 
      steps = 0
      var position = new direction(first)
      while (continue) {
        var next = new movenext(position)
        position = new direction(next.nextmove)
        me.move(next.nextmove)
        steps += 1
        if (!me.look.monsters.isEmpty) {
          if (me.look.monsters(0).name == "ogre") {
            fightOgre
            position = new direction(next.nextmove)
          } else if (me.look.monsters(0).name == "kobold") {
            fightKobold
            position = new direction(next.nextmove)
          } else if (me.look.monsters(0).name == "elf") {
            fightElf
            position = new direction(next.nextmove)
          } 
        }
        if (steps >= 15) {
          println("Travelled " + steps + " steps. Would you like to continue? (y/n) ")
          var answer = Console.readChar()
            if (answer == 'y') {
              steps = 0
            } else if (answer == 'n') {
                println("\nEnding movement.")
                me.look; showMap
            } else {
              println("\nYou entered something other than y or n. Ending movement.")
              me.look; showMap
            }
          }
      }
      if (!continue) {
        me.look
      }
    }
  }
}

var legs = new movement.movetostop
