package com.siondream.freegemas;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Board {
	public Square[][] _squares;
	public final int size = 8;
	// Aux 
	public ListOfMatches _matches = new ListOfMatches();
	public Point[] squaresThatCanBeSwapped = new Point[0];
	public int variety;
	public static Random randomGenerator = new Random();
	public static BoardHelper helper;

	public Board() {
		_squares = new Square[size][size];
		//variety can be 1-7
		variety = 5;
		//BoardHelper give us access to some methods you dont need here
		helper = new BoardHelper(this);
	}

	//return an array of arrays of matching locations
	public ListOfMatches find_matches() {
		_matches.clear();

		//check for matches in each row
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				Point[] built = buildPossibleMatchRow(x, y);
				x=x+built.length-1;
				checkCorrectness(x, built);
				if(built.length>=3){
					_matches.add(convert(built));
				}
			}
		}		
		//check for matches in each column
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Point[] built = buildPossibleMatchColumn(x, y);
				y=y+built.length-1;
				checkCorrectness(y, built);
				if(built.length>=3){
					_matches.add(convert(built));
				}
			}
		}

		return _matches;
	}



	public Boolean has_matches(){
		//return false;
		return find_matches().size()!=0;
	}

	//given x and y (a position of a square),
	//iterate down the row looking for matches
	//return the position of first square that doesnt match the given square
	public Point[] buildPossibleMatchRow(int x, int y) {
		Boolean[] matches = helper.getRowBools(x,y);
		int ctr=x;
		int length = 0;
		while(ctr<size && matches[ctr]){
			length++;
			ctr++;
		}

		Point[] possibleMatch = new Point[length];
		for(int i=0;i<length;i++){
			possibleMatch[i] = new Point(x+i,y);
		}

		return possibleMatch;
	}

	public Point[] buildPossibleMatchColumn(int x, int y) {
		Boolean[] matches = helper.getColumnBools(x,y);
		int ctr=y;
		int length = 0;
		while(ctr<size && matches[ctr]){
			length++;
			ctr++;
		}

		Point[] possibleMatch = new Point[length];

		for(int i=0;i<length;i++){
			possibleMatch[i] = new Point(x,y+i);
		}

		return possibleMatch;
	}

	//return an array of positions that could be swapped in some direction to create a match 
	public Point[] findPossibleSwaps() {
		squaresThatCanBeSwapped = new Point[1];
		//squaresThatCanBeSwapped[0] = (new Point(0,0));
		//if(true) return squaresThatCanBeSwapped;

		int ctr=0;

		/* 
	       Check all possible boards
	       (49 * 4) + (32 * 2) although there are many duplicates
		 */
		for(int x = 0; x < size; ++x){
			for(int y = 0; y < size; ++y){

				// Swap with the one above and check
				if (y > 0) {
					swap(x, y, x, y - 1);
					if (has_matches()) {
						squaresThatCanBeSwapped = expandArray(squaresThatCanBeSwapped);
						squaresThatCanBeSwapped[ctr] = (new Point(x,y));
						ctr++;
					}
					swap(x, y, x, y - 1);
				}

				// Swap with the one below and check
				if (y < size-1) {
					swap(x, y, x, y + 1);
					if (has_matches()) {
						squaresThatCanBeSwapped = expandArray(squaresThatCanBeSwapped);
						squaresThatCanBeSwapped[ctr] = (new Point(x,y));
						ctr++;
					}
					swap(x, y, x, y + 1);
				}

				// Swap with the one on the left and check
				if (x > 0) {
					swap(x, y, x - 1, y);
					if (has_matches()) {
						squaresThatCanBeSwapped = expandArray(squaresThatCanBeSwapped);
						squaresThatCanBeSwapped[ctr] = (new Point(x,y));
						ctr++;
					}
					swap(x, y, x - 1, y);
				}

				// Swap with the one on the right and check
				if (x < size-1) {
					swap(x, y, x + 1, y);
					if (has_matches()) {
						squaresThatCanBeSwapped = expandArray(squaresThatCanBeSwapped);
						squaresThatCanBeSwapped[ctr] = (new Point(x,y));
						ctr++;
					}
					swap(x, y, x + 1, y);
				}
			}
		}

		return squaresThatCanBeSwapped;
	}

	public void swap(int x1, int y1, int x2, int y2) {
		Square temp = _squares[x1][y1];
		_squares[x1][y1] = _squares[x2][y2]; 
		_squares[x2][y2] = temp;

	}

	//return a new array with all the same elements, but one extra space
	public Point[] expandArray(Point[] originalArray) {
		Point[] newArray = new Point[originalArray.length+1];
		for(int i=0; i<originalArray.length; i++){
			newArray[i]=originalArray[i];
		}
		return newArray;
	}

	//NO NEED TO EDIT BELOW THIS LINE
	//FEEL FREE TO CHECK THESE OUT AS EXAMPLES IF YOU LIKE

	public void checkCorrectness(int x, Point[] built) {
		if(built[built.length-1]==null || x>size){
			System.err.println("The array you built in buildPossibleMatchHori/Vert was too long\n"+
					"Make sure it is exactly as long as the match you found\n"+
					"length: "+built.length+" "+x+" "+built[built.length-1]);
			System.exit(0);
		}
	}
	public void fillInitialBoard(){
		helper.fillInitialBoard();
	}
	public void deleteMatches() {
		helper.deleteMatches();
	}

	public void fillSpaces() {
		helper.fillSpaces();
	}

	public void calcFallMovements() {
		helper.calcFallMovements();
	}

	public void applyFall() {
		helper.applyFall();
	}

	public Match convert(Point[] built) {
		ArrayList<Point> newAsArrayList = (new ArrayList<Point>(Arrays.asList(built)));
		newAsArrayList.trimToSize();
		return new Match(newAsArrayList);
	}

	public void endAnimation() {
		helper.endAnimation();
	}

	public Square getSquare(int x, int y) {
		return _squares[x][y];
	}

	public Square[][] getSquares() {
		return _squares;
	}

	public String toString() {
		String output = "";

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				//output += "(" + _squares[i][j].fallStartPosY + ", " + _squares[i][j].fallDistance + ")  ";
				output += "["+_squares[i][j].toString()+"] ";
			}
			output += "\n";
		}
		output += "\n";

		return output;
	}

}
