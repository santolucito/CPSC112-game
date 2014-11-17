package cpsc112.game;

import java.util.Random;


public class BoardHelper {

	public Square[][] squares;
	public int size = 8;
	public Point[] squaresThatCanBeSwapped;

	public Random randomGenerator = new Random();
	public  Board b;

	public BoardHelper(Board board) {
		//Don't worry about what this means
		//it is just connecting this file to the rest of the system
		b = board;
		this.squaresThatCanBeSwapped = b.squaresThatCanBeSwapped;
		this.squares = b._squares;
		this.size = b.size;
	}

	public Point[][] findMatches() {
		Point[][] foundMatches = new Point[2][0];
		return foundMatches;
	}

	public Point[][] expandArray(Point[][] originalArray) {
		Point[][] newArray = new Point[originalArray.length*2][0];
		for(int i=0; i<originalArray.length; i++){
			newArray[i]=originalArray[i];
		}
		return newArray;
	}

	//enable this method to get started
	public boolean hasMatches(){
		Point[][] matches = findMatches();
		if (matches[0] == null || matches[0].length == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	//call this from within the program with the "Run tester()" button
	public void tester(){
		System.out.println("test me");
	}


	public Point[] buildPossibleMatchRow(Point p) {
		int x = p.x;
		int y = p.y;
		boolean[] matches = b.getRowBools(p);
		return null;
	}

	public Point[] buildPossibleMatchColumn(Point p) {
		int x = p.x;
		int y = p.y;
		boolean[] matches = b.getColumnBools(p);
		return null;
	}


	//no need to edit this, just try and figure out how it works
	//you will use this method in findPossibleSwaps
	public void swap(Point p1, Point p2) {
		Square temp = squares[p1.x][p1.y];
		squares[p1.x][p1.y] = squares[p2.x][p2.y]; 
		squares[p2.x][p2.y] = temp;
	}

	public Point[] findPossibleSwaps() {
		squaresThatCanBeSwapped = new Point[1];
		squaresThatCanBeSwapped[0] = (new Point(0,0));
		return squaresThatCanBeSwapped;

	}

	//return a new array with all the same elements, but twice the space
	public Point[] expandArray(Point[] originalArray) {
		return null;
	}

}
