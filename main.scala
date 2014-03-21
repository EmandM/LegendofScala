class direction(dir : String) {
  var current :String = findFirst(firstmove)
  var prev : String = previousdir(current)
  var possiblemoves = moves prev

  def errorCheck(dir : String) : String = dir.toLowerCase match {
    case "north" => "north"
    case "south" => "south"
    case "east" => "east"
    case "west" => "west"
    case _ => "Error"
  }
  def findFirst(dir : String) : String = dir match {
    case "" => {if (me.look.exits.length == 1) { me.look.exits(0)} else {throw new IllegalArgumentException ("Please enter a blank string only when there is only one possible direction to move")}}
    case _ => {if (errorCheck(dir) == "Error") {throw new IllegalArgumentException ("You need to enter a valid direction (North, South,East, West)")} else {errorCheck(dir)}}
  }

  def previousdir() : String = prev match {
    case "north" => "south"
    case "south" => "north"
    case "east" => "west"
    case "west" =>"east"
  }

  def newmove(possible : String, previous : String) : Boolean = possible match {
     case `previous` => false
     case _ => true
  } 

  def moves():List[Int] ={
    var movelist = List[Int]()
    for (prev : Int <- 0 to me.look.exits.length - 1) {
      if (newmove(me.look.exits(prev), previousdir)) {
        movelist :::= List(prev)
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
  def move() : String = {
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

class movetostop() {
  def continue():Boolean = {
    (me.look.features.isEmpty && me.look.monsters.isEmpty && me.look.adventurers.isEmpty)
  }

  def move(first : String) = { 
    var firstmove : String = new direction(first).move
  }
}




