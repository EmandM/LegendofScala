:silent
var me = login("Emma", "eI7HUSc0W")

def retrieve(name : String) = me.inventory.find(_.name == name)
val map = retrieve("map").head
val legend = retrieve("map legend").head
val tiles = map.use[List[Tile]].head
def aroundMe(r : Int)(t : Tile) = {
    t.pos.x >= me.pos.x - r && t.pos.x <= me.pos.x + r &&
    t.pos.y >= me.pos.y - r && t.pos.y <= me.pos.y + r
}
def showTile(tile : Tile) = tile.pos match {
    case pos if pos == me.pos => Markers.Me
    case _ => legend.use[String](tile).head
}

def showMap {
    var lastY = 0

    println(tiles.filter(aroundMe(25)).map(tile => {
        var s = showTile(tile)
        if (tile.pos.y != lastY) {
            s = "\n" + s
        }
        lastY = tile.pos.y
        s
    }).mkString)
}

case class SwordMold(hilt : Item, blade : Item)
case class MaceMold(handle : Item, head : Item)
case class SpearMold(pole : Item, tip : Item)

def sword() = me.combine(new SwordMold(retrieve("stick").head, retrieve("diamond").head)).head
def mace() = me.combine(new MaceMold(retrieve("stick").head, retrieve("diamond").head)).head
def plank() = me.combine(new SpearMold(retrieve("stick").head, retrieve("diamond").head)).head

:silent

