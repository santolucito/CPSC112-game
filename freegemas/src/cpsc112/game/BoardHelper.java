package cpsc112.game;

import java.util.Random;


public class BoardHelper {
	
	public Square[][] _squares;
	public int size = 8;
	public Point[] squaresThatCanBeSwapped;
	
	public Random randomGenerator = new Random();
	public  Board b;

	public BoardHelper(Board board) {
		//Don't worry about what this means
		//it is just connecting this file to the rest of the system
		b = board;
		this.squaresThatCanBeSwapped = b.squaresThatCanBeSwapped;
		this._squares = b._squares;
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
	public boolean has_matches(){
		return false;
		//return findMatches()[0].length>0;

	}
	
	//call this from within the program with the "Run tester()" button
	public void tester(){
		System.out.println("test me");
	}
	
	
	public Point[] buildPossibleMatchRow(int x, int y) {
		boolean[] matches = b.getRowBools(x,y);
		return null;
	}

	public Point[] buildPossibleMatchColumn(int x, int y) {
		boolean[] matches = b.getColumnBools(x,y);
		return null;
	}

	
	//no need to edit this, just try and figure out how it works
	//you will use this method in findPossibleSwaps
	public void swap(int x1, int y1, int x2, int y2) {
		Square temp = _squares[x1][y1];
		_squares[x1][y1] = _squares[x2][y2]; 
		_squares[x2][y2] = temp;
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