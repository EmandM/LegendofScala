object weapons {

	class fight() {
		case class MaceMold(handle : Item, head : Item)

		def fightKobold() = {
			val List(stick, plank) = weapon.separate
			weapon = me.combine(new MaceMold(stick, plank)).head
			weapon.use(me.look.monsters(0))
			weapon.use(me.look.monsters(0))
		}
}