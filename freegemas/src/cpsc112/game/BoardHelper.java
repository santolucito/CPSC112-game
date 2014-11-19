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
		int currPos = 0;
		for (int i = 0; i < b.getSize(); i++)
		{
			for (int j = 0; j < b.getSize(); j++)
			{
				Point p = new Point(i, j);
				Point[] temp = buildPossibleMatchRow(p,b);
				if (temp.length > 2) {
					if (currPos == foundMatches.length) {
						foundMatches = expandArray(foundMatches);
					}
					foundMatches[currPos] = temp;
					currPos++;
				}
				temp = buildPossibleMatchColumn(p,b);
				if (temp.length > 2) {
					if (currPos == foundMatches.length) {
						foundMatches = expandArray(foundMatches);
					}
					foundMatches[currPos] = temp;
					currPos++;
				}
			}
		}
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

		int count = 0;

		for (int i = x; i < b.getSize() && matches[i]; i++)
		{
			count++;
		}
		Point[] ps = new Point[count];
		for (int i = 0; i < count; i++)
		{
			ps[i] = new Point(x+i, y);
		}
		return ps;
	}

	public static Point[] buildPossibleMatchColumn(Point p, Board b) {
		int x = p.x;
		int y = p.y;
		boolean[] matches = b.getColumnBools(new Point(x,y));
		//at this point x is the x location
		int count = 0;

		for (int i = y; i < b.getSize() && matches[i]; i++)
		{
			count++;
		}
		Point[] ps = new Point[count];
		for (int i = 0; i < count; i++)
		{
			ps[i] = new Point(x, y+i);
		}
		return ps;
	}


	public static Point[] findPossibleSwaps(Board b) {
		Point[] returnVals = new Point[3];
		int currPos = 0;
		for (int i = 0; i < b.getSize(); i++)
		{
			for (int j = 0; j < b.getSize(); j++)
			{
				if(i+1<b.getSize()){
					b.swap(i,j,i+1,j);
					if (hasMatches(b)) {
						if (currPos == returnVals.length) {
							returnVals = expandArray(returnVals);
						}
						returnVals[currPos] = new Point(i,j);
						currPos++;
					}
					b.swap(i,j,i+1,j);
				}
				if(j+1<b.getSize()){
					b.swap(i,j,i,j+1);
					if (hasMatches(b)) {
						if (currPos == returnVals.length) {
							returnVals = expandArray(returnVals);
						}
						returnVals[currPos] = new Point(i,j);
						currPos++;
					}
					b.swap(i,j,i,j+1);
				}
			}
		}
		return returnVals;
	}

	//return a new array with all the same elements, but twice the space
	public static Point[] expandArray(Point[] originalArray) {
		Point[] newArray = new Point[originalArray.length*2];
		for(int i=0; i<originalArray.length; i++){
			newArray[i]=originalArray[i];
		}
		return newArray;
		//return originalArray;
	}

	//call this from within the program with the "Run tester()" button
	public static void tester(Board b){
		System.out.println("test me");
	}
	
}
