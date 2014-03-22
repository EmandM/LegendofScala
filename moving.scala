

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
        dir = me.look.exits(position.possiblemoves(0))
      } else {
        dir = "none"
      }
      return dir
    }
  }

  class movetostop() {
    var fight = new monster.battle
    def continue():Boolean = {
      (me.look.features.isEmpty && me.look.monsters.isEmpty && me.look.adventurers.isEmpty && !me.look.exits.isEmpty)
    }

    def move(first : String) = { 
      var position = new direction(first)
      while (continue) {
        var next = new movenext(position)
        position = new direction(next.nextmove)
        me.move(next.nextmove)
        if (me.look.monsters(0).name == "ogre") {
          fight.ogre
          position = new direction(next.nextmove)
        } else if (me.look.monsters(0).name == "kobold") {
          fight.kobold
          position = new direction(next.nextmove)
        } else if (me.look.monsters(0).name == "elf") {
          fight.elf
          position = new direction(next.nextmove)
        }
      }
      if (!continue) {
        me.look
      }
    }
  }
}

object monster {
  class create() {
    case class SwordMold(hilt : Item, blade : Item)
    case class MaceMold(handle : Item, head : Item)
    case class SpearMold(pole : Item, tip : Item)

    var stick, plank, ingot, diamond, weapon = me.inventory(0)

    def initialise {  
        stick = retrieve("stick").head
        plank = retrieve("plank").head
        ingot = retrieve("ingot").head
        diamond = retrieve("diamond").head
    }

    def dismantle() = {weapon.separate}
    def sword1() {case class SwordMold(hilt : Item, blade : Item); me.combine(new SwordMold(retrieve("stick").head.asInstanceOf[Item], retrieve("diamond").head.asInstanceOf[Item])).head}
    def sword():Item = {case class SwordMold(hilt : Item, blade : Item); initialise; me.combine(new SwordMold(stick, diamond)).head}
    def mace():Item  = {initialise; me.combine(new MaceMold(stick, diamond)).head}
    def spear():Item  = {initialise; me.combine(new SpearMold(stick.asInstanceOf[Item], diamond.asInstanceOf[Item])).head}
  }

  class battle() {
    var build = new create
    var weapon = build.weapon
    def heal() {if (me.hearts < 5) {retrieve("potion").head.use}}
    def strike() = weapon.use(me.look.monsters(0))
    def exists():Boolean = (!me.look.monsters.isEmpty)
    def attack() {while (exists) {strike; heal}}
    def ogre() = {weapon = build.sword; attack; build.dismantle}
    def kobold() = {weapon = build.mace; attack; build.dismantle}
    def elf() = {weapon = build.spear; attack; build.dismantle}
  }
}

var build = new monster.create

