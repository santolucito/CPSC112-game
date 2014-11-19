package cpsc112.game;

public class Point {
	public int x;
	public int y;
	
	public Point() {
		x = -1;
		y = -1;
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString(){
		return "("+x+","+y+")";
	}
}
