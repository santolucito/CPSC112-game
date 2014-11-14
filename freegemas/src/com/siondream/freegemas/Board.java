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


	public Board() {
		_squares = new Square[size][size];
		//variety can be 1-7
		variety = 5;
	}

	public void fillInitialBoard() {
		do {
			for (int y = 0; y < size; ++y) {
				for (int x = 0; x < size; ++x) {
					_squares[x][y] = new Square(Square.numToType(randomGenerator.nextInt(variety)+1));
					_squares[x][y].fallStartPosY = y-size;
					_squares[x][y].fallDistance = size;
				}
			}
		} while(has_matches() || findPossibleSwaps().length == 0);
	}

	public void swap(int x1, int y1, int x2, int y2) {
		Square temp = _squares[x1][y1];
		_squares[x1][y1] = _squares[x2][y2]; 
		_squares[x2][y2] = temp;

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
		Boolean[] matches = getRowBools(x,y);
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

	public Boolean[] getRowBools(int x, int y) {
		Boolean[] matches = new Boolean[size];
		for(int i=0;i<size;i++){
			matches[i] = _squares[x][y].equals(_squares[i][y]); 
		}
		return matches;
	}

	public Point[] buildPossibleMatchColumn(int x, int y) {
		Boolean[] matches = getColumnBools(x,y);
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
	
	public Boolean[] getColumnBools(int x, int y) {
		Boolean[] matches = new Boolean[size];
		for(int i=0;i<size;i++){
			matches[i] = _squares[x][y].equals(_squares[x][i]); 
		}
		return matches;
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
	
	public void deleteMatches() {
		ListOfMatches matches = find_matches();
		for (int i = 0; i < matches.size(); ++i) {
			for (int j = 0; j < matches.get(i).size(); ++j) {
				/*if(j==3 && matches.get(i).size()>=4){
					makeSpecialSquare(matches.get(i).get(j).x,
									  matches.get(i).get(j).y);
				}
				else{*/
					deleteSquare(matches.get(i).get(j).x,
								matches.get(i).get(j).y);
				//}
			}
		}		
	}
	
	public void deleteSquare(int x, int y) {	
		_squares[x][y].setType(Square.Color.sqEmpty);
	}

	public void makeSpecialSquare(int x, int y) {	
		_squares[x][y].setType(Square.getDualType(_squares[x][y].getType()));
	}
	
	public void fillSpaces() {
		for(int x = 0; x < size; ++x){
			// Count how many jumps do we have to fall
			int jumps = 0;

			for(int y = 0; y < size; ++y){
				if(!_squares[x][y].equals(Square.Color.sqEmpty)) {
					break;
				}
				jumps++;
			}

			for(int y = 0; y < size; ++y){
				if(_squares[x][y].equals(Square.Color.sqEmpty)) {
					_squares[x][y].setType(Square.numToType(randomGenerator.nextInt(variety )+1));
					_squares[x][y].mustFall = true;
					_squares[x][y].fallStartPosY = y - jumps;
					_squares[x][y].fallDistance = jumps;
				}       
			}
		}   
	}

	public void calcFallMovements() {
		for (int x = 0; x < size; ++x) {
			// From bottom to top
			for (int y = size-1; y >= 0; --y) {
				_squares[x][y].fallStartPosY = y;

				// If square is empty, make all the squares above it fall
				if (_squares[x][y].equals(Square.Color.sqEmpty)) {
					for (int k = y - 1; k >= 0; --k) {
						_squares[x][k].mustFall = true;
						_squares[x][k].fallDistance++;

						if (_squares[x][k].fallDistance > size-1)
						{
							System.out.println("WARNING");
						}
					}
				}
			}
		}
	}

	public void applyFall() {
		for (int x = 0; x < size; ++x) {
			// From bottom to top in order not to overwrite squares
			for (int y = size-1; y >= 0; --y) {
				if (_squares[x][y].mustFall == true &&
						!_squares[x][y].equals(Square.Color.sqEmpty)) {
					int y0 = _squares[x][y].fallDistance;

					if (y + y0 > size-1)
					{
						System.out.println("WARNING");
					}

					_squares[x][y + y0] = _squares[x][y];
					_squares[x][y] = new Square(Square.Color.sqEmpty);
				}
			}
		}
	}

	public Match convert(Point[] built) {
		ArrayList<Point> newAsArrayList = (new ArrayList<Point>(Arrays.asList(built)));
		newAsArrayList.trimToSize();
		return new Match(newAsArrayList);
	}
	
	public void endAnimation() {
		for(int x = 0; x < size; ++x){
			for(int y = 0; y < size; ++y){
				_squares[x][y].mustFall = false;
				_squares[x][y].fallStartPosY = y;
				_squares[x][y].fallDistance = 0;
			}
		}
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
