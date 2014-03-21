object move {
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

    def moves():List[Int] ={
      var movelist = List[Int]()
      for (x : Int <- 0 to me.look.exits.length - 1) {
        if (goodmove(me.look.exits(x), previous)) {
          movelist :::= List(x)
        }
      }
      return movelist
    }
  }

  class nextmove(location : direction) {
    var position = location
    var unused = List[(nz.org.sesa.los.client.Position, String)]()
    var nextdirection = move

    def fork() = {
      for (x <- 1 to position.possiblemoves.length - 1) {
        unused :::= List((me.pos, me.look.exits(position.possiblemoves(x))))
      }

    }
    def findmove() : String = {
      if (me.look.exits.length == 1) {
        me.look.exits(0)
      } else if (position.possiblemoves.length == 1) {
        me.look.exits(position.possiblemoves(0))
      } else if (position.possiblemoves.length > 1) {
        fork
        me.look.exits(position.possiblemoves(0))
      } else {
        "none"
      }
    }
  }

  class movestop() {
    var nofork : Boolean = true

    def nounused(next : nextmove) : Boolean = {
        if (next.unused.length > 0) {
          false
        } else {
          true
        }
    }

    def continue():Boolean = {
      (me.look.features.isEmpty && me.look.monsters.isEmpty && me.look.adventurers.isEmpty && nofork && !me.look.exits.isEmpty)
    }

    def move(first : String) = { 
      var position = new direction(first)

      while (continue) {
        var next = new nextmove(position)
        nofork = nounused(next)
        me.move(next.findmove)
        position = new direction(next.findmove)
      }
      if (!continue) {
        me.look
      }
    }
  }
}
