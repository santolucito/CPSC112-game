package com.siondream.freegemas;

import java.awt.Point;
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

	public Point[][] find_matches() {

		Point[][] _matches = new Point[2][0];
		int ctr=0;
		//check for matches in each row
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				Point[] built = buildPossibleMatchRow(x, y);
				x=x+built.length-1;
				if(built.length>=3){
					if(ctr>=_matches.length)
						_matches=expandArray(_matches);
					_matches[ctr]=built;
					ctr++;
				}
			}
		}		
		//check for matches in each column
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Point[] built = buildPossibleMatchColumn(x, y);
				y=y+built.length-1;
				if(built.length>=3){
					if(ctr>=_matches.length)
						_matches=expandArray(_matches);
					_matches[ctr]=built;
					ctr++;
				}
			}
		}
		return _matches;
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
		//return false;
		return find_matches()[0].length>0;

	}
	
	public Point[] buildPossibleMatchRow(int x, int y) {
		boolean[] matches = b.getRowBools(x,y);
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
		boolean[] matches = b.getColumnBools(x,y);
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

	
	//no need to edit this, just try and figure out how it works
	//you will use this method in findPossibleSwaps
	public void swap(int x1, int y1, int x2, int y2) {
		Square temp = _squares[x1][y1];
		_squares[x1][y1] = _squares[x2][y2]; 
		_squares[x2][y2] = temp;
	}
	
	public Point[] findPossibleSwaps() {
		squaresThatCanBeSwapped = new Point[1];
		//squaresThatCanBeSwapped[0] = (new Point(0,0));
		//if(true) return squaresThatCanBeSwapped;

		int ctr=0;

	    //Check all possible boards
		for(int x = 0; x < size; ++x){
			for(int y = 0; y < size; ++y){

				// Swap with the one above and check
				if (y > 0) {
					swap(x, y, x, y - 1);
					if (has_matches()) {
						if(ctr>=squaresThatCanBeSwapped.length)
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
						if(ctr>=squaresThatCanBeSwapped.length)
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
						if(ctr>=squaresThatCanBeSwapped.length)
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
						if(ctr>=squaresThatCanBeSwapped.length)
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
	
	//return a new array with all the same elements, but twice the space
	public Point[] expandArray(Point[] originalArray) {
		Point[] newArray = new Point[originalArray.length*2];
		for(int i=0; i<originalArray.length; i++){
			newArray[i]=originalArray[i];
		}
		return newArray;
	}

}
