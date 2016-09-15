// Basic Point class with the methods I wanted
public class Coord {
	private int x;
	private int y;
	
	public Coord(int newX, int newY) {
		x = newX;
		y = newY;
	}
	
	public int X() {
		return x;
	}
	public int Y() {
		return y;
	}
	public void X(int newX) {
		x = newX;
	}
	public void Y(int newY) {
		y = newY;
	}
	
	public String toString() {
		String output = x + ", " + y;
		return output;
	}
}