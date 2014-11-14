package com.siondream.freegemas;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board {

	private BoardHelper helper;
	private Random randomGenerator = new Random();
	
	public Square[][] _squares;
	public final int size = 8;
	// Aux 
	public ListOfMatches _matches = new ListOfMatches();
	public Point[] squaresThatCanBeSwapped = new Point[0];
	public int variety;
	
	public Board() {
		_squares = new Square[size][size];
		//variety can be 1-7
		variety = 5;
		
		helper = new BoardHelper(this);
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
		} while(helper.has_matches() || helper.findPossibleSwaps().length == 0);		
	}

	//return an array of arrays of matching locations
	public ListOfMatches find_matches() {
		_matches.clear();

		//check for matches in each row
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				Point[] built = helper.buildPossibleMatchRow(x, y);
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
				Point[] built = helper.buildPossibleMatchColumn(x, y);
				y=y+built.length-1;
				checkCorrectness(y, built);
				if(built.length>=3){
					_matches.add(convert(built));
				}
			}
		}

		return _matches;
	}

	public Point[] findPossibleSwaps() {
		return helper.findPossibleSwaps();
	}
	
	public void swap(int x1, int y1, int x2, int y2){
		helper.swap(x1, y1, x2, y2);
	}
	public boolean has_matches(){
		helper.has_matches();
		//return false;
		return find_matches().size()!=0;
	}

	public void checkCorrectness(int x, Point[] built) {
		if(built[built.length-1]==null || x>size){
			System.err.println("The array you built in buildPossibleMatchHori/Vert was too long\n"+
					"Make sure it is exactly as long as the match you found\n"+
					"length: "+built.length+" "+x+" "+built[built.length-1]);
			System.exit(0);
		}
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
	private void deleteSquare(int x, int y) {	
		_squares[x][y].setType(Square.Color.sqEmpty);
	}

	private void makeSpecialSquare(int x, int y) {
		//need some concept of equivalance classes for this
		_squares[x][y].setType(Square.getDualType(_squares[x][y].getType()));
	}
	
	public boolean[] getColumnBools(int x, int y) {
		boolean[] matches = new boolean[size];
		for(int i=0;i<size;i++){
			matches[i] = _squares[x][y].equals(_squares[x][i]); 
		}
		return matches;
	}
	public boolean[] getRowBools(int x, int y) {
		boolean[] matches = new boolean[size];
		for(int i=0;i<size;i++){
			matches[i] = _squares[x][y].equals(_squares[i][y]); 
		}
		return matches;
	}

	
	public Match convert(Point[] built) {
		ArrayList<Point> newAsArrayList = (new ArrayList<Point>(Arrays.asList(built)));
		newAsArrayList.trimToSize();
		return new Match(newAsArrayList);
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
