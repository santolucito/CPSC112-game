package cpsc112.game;

import java.util.Arrays;
import java.util.Random;


public class BoardHelper {


	public static void main(String args[]){
		Board b = new Board();
		b.runTests();
	}


	public static Point[][] findMatches(Board b) {
		Point[][] foundMatches = new Point[1][0];
		return foundMatches;
	}

	public static Point[][] expandArray(Point[][] originalArray) {
		Point[][] newArray = new Point[originalArray.length*2][0];
		for(int i=0; i<originalArray.length; i++){
			newArray[i]=originalArray[i];
		}
		return newArray;
	}

	public static boolean hasMatches(Board b){
		Point[][] matches = findMatches(b);
		if (matches[0] == null || matches[0].length == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static Point[] buildPossibleMatchRow(Point p, Board b) {
		int x = p.x;
		int y = p.y;
		boolean[] matches = b.getRowBools(new Point(x,y));
		//at this point x is the x location

		return null;
	}

	public static Point[] buildPossibleMatchColumn(Point p, Board b) {
		int x = p.x;
		int y = p.y;
		boolean[] matches = b.getColumnBools(new Point(x,y));
		//at this point x is the x location

		return null;
	}


	public static Point[] findPossibleSwaps(Board b) {
		Point[] foundSwaps = new Point[3];
		return foundSwaps;
	}

	//return a new array with all the same elements, but twice the space
	public static Point[] expandArray(Point[] originalArray) {
		return originalArray;
	}

	//call this from within the program with the "Run tester()" button
	public static void tester(Board b){
		System.out.println("test me");
	}
	
}
