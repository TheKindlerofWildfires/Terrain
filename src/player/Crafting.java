package player;

public class Crafting {
	/**
	 * I am testing the crafting systems --> but this is all really sad
	 * 
	 * @param args
	 */
	public int level;
	public Item[] A = new Item[1];
	public Item[] B = new Item[3];
	public Item[] C = new Item[6];
	public Item[] D = new Item[9];

	// when placing an item in a crafting table make sure to pass it an index.
	// ideally I place items here and check if they match a recipe and then
	// cause things to happen
	public Item Craft() {
		Item v = validRecipe();
		if (v != null) {
			System.out.println("boo");
			clear();
			return v;
		} else {
			return null;
		}

	}

	private void clear() {
		for (int i = 0; i < A.length; i++) {
			A[i] = null;
		}
		for (int i = 0; i < B.length; i++) {
			B[i] = null;
		}
		for (int i = 0; i < C.length; i++) {
			C[i] = null;
		}
		for (int i = 0; i < D.length; i++) {
			D[i] = null;
		}

	}

	private Item validRecipe() {

		// it has to run through a recipe list and check if the recipe matches
		if (A[0].tag == "crystal" && B[0].tag == B[1].tag && B[1].tag == B[2].tag && B[2].tag == "scrap") {
			return new Item("Power Unit", 0, 100, "F");// think of a clever way
														// to detect if this is
														// in an outside ring
		}
		boolean b = nextTo(new Item("crystal", 0, 1, "A"), new Item("crysta", 0, 1, "B1"));
		System.out.println(b);
		return null;
	}

	/**
	 * This just does teir 3 bcause I'm lazy
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean nextTo(Item a, Item b) {
		boolean var;
		switch (a.location) {
		case "A":
			if (b.location.contains("B")) {
				var = true;
			} else {
				var = false;
			}
			break;
		case "B1":
			if (b.location == "A" || b.location == "C1" || b.location == "C2") {
				var = true;
			} else {
				var = false;
			}
			break;
		case "B2":
			if (b.location == "A" || b.location == "C3" || b.location == "C5") {
				var = true;
			} else {
				var = false;
			}
			break;
		case "B3":
			if (b.location == "A" || b.location == "C4" || b.location == "C6") {
				var = true;
			} else {
				var = false;
			}
			break;
		case "C1":
			if (b.location == "B1") {
				var = true;
			} else {
				var = false;
			}
			break;
		case "C2":
			if (b.location == "B1") {
				var = true;
			} else {
				var = false;
			}
			break;
		case "C3":
			if (b.location == "B2") {
				var = true;
			} else {
				var = false;
			}
			break;
		case "C4":
			if (b.location == "B3") {
				var = true;
			} else {
				var = false;
			}
			break;
		case "C5":
			if (b.location == "B2") {
				var = true;
			} else {
				var = false;
			}
			break;
		case "C6":
			if (b.location == "B3") {
				var = true;
			} else {
				var = false;
			}
			break;
		default:
			var = false;
			break;
		}
		return var;
	}
}
