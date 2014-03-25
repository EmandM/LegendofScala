:silent
var me = login("Emma", "eI7HUSc0W")

def retrieve(name: String) = me.inventory.find(_.name.endsWith(name))
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
var weapon = me.inventory(0)
var stop = 0
def buildSword {case class SwordMold(hilt : Item, blade : Item); me.combine(new SwordMold(retrieve("stick").head, retrieve("diamond").head)).head}
def buildMace {case class MaceMold(handle : Item, head : Item); me.combine(new MaceMold(retrieve("stick").head, retrieve("diamond").head)).head}
def buildSpear {case class SpearMold(pole : Item, tip : Item); me.combine(new SpearMold(retrieve("stick").head, retrieve("diamond").head)).head}
def buildAncientStaff {
    case class AncientStaffMold(shaft : Item, fireGem : Item, waterGem : Item, earthGem : Item, airGem : Item)
    me.combine(new AncientStaffMold(retrieve("fire gem").head, retrieve("water gem").head, retrieve("earth gem").head, retrieve("air gem").head))
}
def dismantleWeapon  {me.inventory.find(_.name.startsWith("diamond ")).head.separate}
def heal {if (me.hearts < 6) {retrieve("potion").head.use}}
def strike {
    if(!retrieve("sword").isEmpty) {
        retrieve("sword").head.use(me.look.monsters(0))
    } else if (!retrieve("mace").isEmpty) {
        retrieve("mace").head.use(me.look.monsters(0))
    } else if (!retrieve("spear").isEmpty) {
        retrieve("spear").head.use(me.look.monsters(0))
    } else if(!retrieve("staff").isEmpty) {
        retrieve("staff").head.use(me.look.monsters(0))
    }
    stop += 1
}
def exists():Boolean = (!me.look.monsters.isEmpty && (stop < 10))
def attack() {stop = 0; while (exists) {strike; heal}}
def fightOgre() = {buildSword; attack; dismantleWeapon}
def fightKobold() = {buildMace; attack; dismantleWeapon}
def fightElf() = {buildSpear; attack; dismantleWeapon}
def fightDragon = {buildAncientStaff; attack}
def useFeature = {if (!me.look.features.isEmpty) {if (me.look.features(0).name == "portal") {me.look.features(0).use; me.look} else {me.look.features(0).use}}}
def examineFeature = me.look.features(0).examine

:silent

