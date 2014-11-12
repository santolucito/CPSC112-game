package com.siondream.freegemas;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.math.MathUtils;

public class Board {
	private Square[][] _squares;
	public final int size = 8;

	// Aux 
	private ListOfMatches _matches = new ListOfMatches();
	private Coord[] foundSolutions = new Coord[0];

	public Board() {
		_squares = new Square[size][size];
	}

	//TODO
	public void swap(int x1, int y1, int x2, int y2) {
		Square temp = _squares[x1][y1];
		_squares[x1][y1] = _squares[x2][y2]; 
		_squares[x2][y2] = temp;

	}

	//TODO
	public void fillInitialBoard() {
		do {
			System.out.println("### Generating...");

			for (int y = 0; y < size; ++y) {
				for (int x = 0; x < size; ++x) {
					_squares[x][y] = new Square(Square.numToType(MathUtils.random(1, 7)));
					_squares[x][y].fallStartPosY = y-size;
													//(int)MathUtils.random(-7, -1);
					_squares[x][y].fallDistance = size;
													//y - _squares[x][y].fallStartPosY;
				}
			}

		} while(has_matches() || find_solutions().length == 0);

		System.out.println("The generated board has no matches and some possible solutions.");
	}


	//return an array of arrays of matching locations
	public ListOfMatches find_matches() {
		_matches.clear();

		//check for matches in each row
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				int lastMatchPosition = buildPossibleMatchHorizontal(x, y);
				x=lastMatchPosition-1;
			}
		}		
		//check for matches in each column
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int lastMatchPosition = buildPossibleMatchVertical(x, y);
				y=lastMatchPosition-1;
			}
		}

		return _matches;
	}
	
	public Boolean has_matches(){
		return find_matches().size()!=0;
	}

	//given x and y (a position of a square),
	//iterate down the row looking for matches
	//return the position of first square that doesnt match the given square
	public int buildPossibleMatchHorizontal(int x, int y) {
		Coord[] possibleMatch = new Coord[1];
		possibleMatch[0] = new Coord(x,y);
		
		int ctr=1;
		int scanPosition = x + 1;
		while (scanPosition < size && _squares[scanPosition][y].equals(_squares[x][y])) {
			if(ctr>=possibleMatch.length){
				possibleMatch = expandArray(possibleMatch);
			}
			possibleMatch[ctr] = new Coord(scanPosition,y);
			ctr++;				
			scanPosition++;
		}
		if(possibleMatch.length>=3){
			_matches.add(convert(possibleMatch));
		}
		return x+possibleMatch.length;
	}

	public int buildPossibleMatchVertical(int x, int y) {
		Coord[] possibleMatch = new Coord[1];
		possibleMatch[0] = new Coord(x,y);
		
		int ctr=1;
		int scanPosition = y + 1;
		while (scanPosition < size &&_squares[x][scanPosition].equals(_squares[x][y])) {
			if(ctr>=possibleMatch.length){
				possibleMatch = expandArray(possibleMatch);
			}
			possibleMatch[ctr] = new Coord(x,scanPosition);
			ctr++;	
			scanPosition++;
		}
		if(possibleMatch.length>=3){
			_matches.add(convert(possibleMatch));
		}
		return y+possibleMatch.length;
	}
	
	//TODO
	//return an array of positions that could be swapped in some direction to create a match 
	public Coord[] find_solutions() {
		foundSolutions = new Coord[0];
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
						foundSolutions = expandArray(foundSolutions);
						foundSolutions[ctr] = (new Coord(x,y));
						ctr++;
					}
					swap(x, y, x, y - 1);
				}

				// Swap with the one below and check
				if (y < size-1) {
					swap(x, y, x, y + 1);
					if (has_matches()) {
						foundSolutions = expandArray(foundSolutions);
						foundSolutions[ctr] = (new Coord(x,y));
						ctr++;
					}
					swap(x, y, x, y + 1);
				}

				// Swap with the one on the left and check
				if (x > 0) {
					swap(x, y, x - 1, y);
					if (has_matches()) {
						foundSolutions = expandArray(foundSolutions);
						foundSolutions[ctr] = (new Coord(x,y));
						ctr++;
					}
					swap(x, y, x - 1, y);
				}

				// Swap with the one on the right and check
				if (x < size-1) {
					swap(x, y, x + 1, y);
					if (has_matches()) {
						foundSolutions = expandArray(foundSolutions);
						foundSolutions[ctr] = (new Coord(x,y));
						ctr++;
					}
					swap(x, y, x + 1, y);
				}
			}
		}

		return foundSolutions;
	}
	
	//return a new array with all the same elements, but one extra space
	private Coord[] expandArray(Coord[] originalArray) {
		Coord[] newArray = new Coord[originalArray.length+1];
		for(int i=0; i<originalArray.length; i++){
			newArray[i]=originalArray[i];
		}
		return newArray;
	}


	//TODO
	public void deleteMatches() {
		ListOfMatches matches = find_matches();
		for (int i = 0; i < matches.size(); ++i) {
			for (int j = 0; j < matches.get(i).size(); ++j) {
				if(j==3 && matches.get(i).size()>=4){
					makeSpecialSquare(matches.get(i).get(j).x,
									  matches.get(i).get(j).y);
				}
				else{
					deleteSquare(matches.get(i).get(j).x,
								matches.get(i).get(j).y);
				}
			}
		}		
	}
	
	public void deleteSquare(int x, int y) {	
		_squares[x][y].setType(Square.Color.sqEmpty);
	}

	public void makeSpecialSquare(int x, int y) {	
		_squares[x][y].setType(Square.getDualType(_squares[x][y].getType()));
	}
	
	
	
	//NO NEED TO EDIT BELOW THIS LINE
	//FEEL FREE TO CHECK THESE OUT AS EXAMPLES IF YOU LIKE

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
					_squares[x][y].setType(Square.numToType(MathUtils.random(1, 7)));
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

	private Match convert(Coord[] originalAsArray) {
		ArrayList<Coord> newAsArrayList = (new ArrayList<Coord>(Arrays.asList(originalAsArray)));
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
