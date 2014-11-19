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
	
	@Override public boolean equals(Object other) {
	    boolean result = false;
	    if (other instanceof Point) {
	        Point that = (Point) other;
	        result = (this.getX() == that.getX() && this.getY() == that.getY());
	    }
	    return result;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}
}
