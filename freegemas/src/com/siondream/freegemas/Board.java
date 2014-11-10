package com.siondream.freegemas;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Board {
	private Square[][] _squares;
	public final int size = 8;

	// Aux 
	//test
	private MultipleMatch _matches = new MultipleMatch();
	private Match[][] _columns = new Match[size][size];
	private Match[][] _rows = new Match[size][size];
	private Coord[] _matchCoords = new Coord[1000];
	private Coord[] _solCoords = new Coord[1000];
	private Array<Coord> _results = new Array<Coord>();

	public Board() {
		_squares = new Square[size][size];


		//this all will be removed
		for (int x = 0; x < size; ++x) {
			for (int y = 0; y < size; ++y) {
				_columns[x][y] = new Match();
				_rows[x][y] = new Match();
			}
		}

		for (int x = 0; x < 1000; ++x) {
			_matchCoords[x] = new Coord();
			_solCoords[x] = new Coord();
		}
		//
	}

	public void swap(int x1, int y1, int x2, int y2) {

		Square temp = _squares[x1][y1];
		_squares[x1][y1] = _squares[x2][y2]; 
		_squares[x2][y2] = temp;

	}

	public void fillInitialBoard() {
		do {
			System.out.println("### Generating...");

			for (int y = 0; y < size; ++y) {
				for (int x = 0; x < size; ++x) {
					//TODO refactor all of this into constructor
					_squares[x][y] = new Square(Square.numToType(MathUtils.random(1, 7)));
					_squares[x][y].mustFall = true;
					_squares[x][y].fallStartPosY = y-size;//(int)MathUtils.random(-7, -1);
					_squares[x][y].fallDistance = size;//j - _squares[i][j].origY;
				}
			}

		} while(has_matches() || solutions().size == 0);

		System.out.println("The generated board has no matches and some possible solutions.");
	}

	//return an array of arrays of matching locations
	public MultipleMatch find_matches() {
		
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

	public int buildPossibleMatchHorizontal(int x, int y) {
		Match possibleMatch = new Match();
		possibleMatch.add(new Coord(x,y));
		int scanPosition = x + 1;
		while (scanPosition < size && _squares[x][y].equals(_squares[scanPosition][y]) ) {
			possibleMatch.add(new Coord(scanPosition,y));	
			scanPosition++;
		}
		if(possibleMatch.size>=3){
			_matches.add(possibleMatch);
		}
		return scanPosition;
	}
	
	public int buildPossibleMatchVertical(int x, int y) {
		Match possibleMatch = new Match();
		possibleMatch.add(new Coord(x,y));
		int scanPosition = y + 1;
		while (scanPosition < size && _squares[x][y].equals(_squares[x][scanPosition]) ) {
			possibleMatch.add(new Coord(x,scanPosition));	
			scanPosition++;
		}
		if(possibleMatch.size>=3){
			_matches.add(possibleMatch);
		}
		return scanPosition;
	}
	

	
	public void calcFallMovements() {
		for (int x = 0; x < size; ++x) {
			// From bottom to top
			for (int y = size-1; y >= 0; --y) {
				_squares[x][y].fallStartPosY = y;

				// If square is empty, make all the squares above it fall
				if (_squares[x][y].equals(Square.Type.sqEmpty)) {
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
						!_squares[x][y].equals(Square.Type.sqEmpty)) {
					int y0 = _squares[x][y].fallDistance;

					if (y + y0 > size-1)
					{
						System.out.println("WARNING");
					}

					_squares[x][y + y0] = _squares[x][y];
					_squares[x][y] = new Square(Square.Type.sqEmpty);
				}
			}
		}
	}

	public void fillSpaces() {
		for(int x = 0; x < size; ++x){
			// Count how many jumps do we have to fall
			int jumps = 0;

			for(int y = 0; y < size; ++y){
				if(!_squares[x][y].equals(Square.Type.sqEmpty)) {
					break;
				}
				++jumps;
			}

			for(int y = 0; y < size; ++y){
				if(_squares[x][y].equals(Square.Type.sqEmpty)) {
					_squares[x][y].setType(Square.numToType(MathUtils.random(1, 7)));
					_squares[x][y].mustFall = true;  
					_squares[x][y].fallStartPosY = y - jumps;
					_squares[x][y].fallDistance = jumps;
				}       
			}
		}   
	}

	public Boolean has_matches(){
		return find_matches().size!=0;
	}

	public Array<Coord> solutions() {
		_results.clear();
		int currCoord = 0;

		if(has_matches()){
			_solCoords[currCoord].x = -1;
			_solCoords[currCoord].y = -1;
			_results.add(_solCoords[currCoord]);
			++currCoord;
			return _results;
		}

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
						_solCoords[currCoord].x = x;
						_solCoords[currCoord].y = y;
						_results.add(_solCoords[currCoord]);
						++currCoord;
					}

					swap(x, y, x, y - 1);
				}

				// Swap with the one below and check
				if (y < size-1) {
					swap(x, y, x, y + 1);
					if (has_matches()) {
						_solCoords[currCoord].x = x;
						_solCoords[currCoord].y = y;
						_results.add(_solCoords[currCoord]);
						++currCoord;
					}

					swap(x, y, x, y + 1);
				}

				// Swap with the one on the left and check
				if (x > 0) {
					swap(x, y, x - 1, y);
					if (has_matches()) {
						_solCoords[currCoord].x = x;
						_solCoords[currCoord].y = y;
						_results.add(_solCoords[currCoord]);
						++currCoord;
					}

					swap(x, y, x - 1, y);
				}

				// Swap with the one on the right and check
				if (x < size-1) {
					swap(x, y, x + 1, y);
					if (has_matches()) {
						_solCoords[currCoord].x = x;
						_solCoords[currCoord].y = y;
						_results.add(_solCoords[currCoord]);
						++currCoord;
					}

					swap(x, y, x + 1, y);
				}
			}
		}

		return _results;
	}

	
	//NO NEED TO EDIT BELOW THIS LINE
	public void endAnimation() {
		for(int x = 0; x < size; ++x){
			for(int y = 0; y < size; ++y){
				_squares[x][y].mustFall = false;
				_squares[x][y].fallStartPosY = y;
				_squares[x][y].fallDistance = 0;
			}
		}
	}
	
	
	public void deleteSquare(int x, int y) {	
		_squares[x][y].setType(Square.Type.sqEmpty);

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
