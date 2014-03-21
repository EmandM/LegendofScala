
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
      println("direction.moves start")
      var movelist = List[Int]()
      for (x : Int <- 0 to me.look.exits.length - 1) {
        if (goodmove(me.look.exits(x), previous)) {
          movelist :::= List(x)
        }
      }
      println("direction.moves end")
      return movelist
    }
  }

  class nextmove(location : direction) {
    var position = location
    var nextdirection = findmove

    def findmove() : String = {
      var dir = ""
      println("Findmove is starting")
      println(position.possiblemoves.length)
      if (me.look.exits.length == 1) {
        dir = me.look.exits(0)
      } else if (position.possiblemoves.length >= 1) {
        dir = me.look.exits(position.possiblemoves(0))
      } else {
        dir = "none"
      }
      println("Findmove is ending")
      return dir
    }
  }

  class movetostop() {

    def continue():Boolean = {
      println("continue start")
      (me.look.features.isEmpty && me.look.monsters.isEmpty && me.look.adventurers.isEmpty && !me.look.exits.isEmpty)
    }

    def move(first : String) = { 
      var position = new direction(first)
      println("movetostop.move start")
      while (continue) {
        println("whileloop start")
        var next = new nextmove(position)
        position = new direction(next.findmove)
        me.move(next.findmove)
        println(me.look.exits)å
        println(next.findmove)
        println("Whileloop end")
      }
      if (!continue) {
        me.look
      }
    }
  }
}

var legs = new movement.movetostop


