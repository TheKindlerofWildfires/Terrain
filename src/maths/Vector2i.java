package maths;

public class Vector2i {

	public int x;
	public int y;

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vector2i)) {
			return false;
		}
		if (o == this) {
			return true;
		}
		Vector2i vec = (Vector2i) o;
		if (this.x == vec.x && this.y == vec.y) {
			return true;
		} else {
			return false;
		}
	}
}
