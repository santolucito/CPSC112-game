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
	}
	
	public Square getSquare(int x, int y) {
		if (x < 0 || x >= size || y < 0 || y >= size) {
			return null;
		}
		else {
			return _squares[x][y];
		}
	}
	
	public Square[][] getSquares() {
		return _squares;
	}
	
	public void swap(int x1, int y1, int x2, int y2) {
//		if (x1 >= 0 && x1 < size &&
//			y1 >= 0 && y1 < size &&
//			x2 >= 0 && x2 < size &&
//			y2 >= 0 && y2 < size) {
			
			Square temp = _squares[x1][y1];
			_squares[x1][y1] = _squares[x2][y2]; 
			_squares[x2][y2] = temp;
//		}
	}
	
	public void del(int x, int y) {
//		if (x >= 0 && x < size &&
//			y >= 0 && y < size) {
			
			_squares[x][y].setType(Square.Type.sqEmpty);
//		}
	}
	
	public void generate() {
		boolean repeat = false;
		
		do {
			repeat = false;
			System.out.println("### Generating...");
			
			for (int i = 0; i < size; ++i) {
				for (int j = 0; j < size; ++j) {
					_squares[i][j] = new Square(Square.numToType(MathUtils.random(1, 7)));
	                _squares[i][j].mustFall = true;
	                _squares[i][j].origY = (int)MathUtils.random(-7, -1);
	                _squares[i][j].destY = j - _squares[i][j].origY;
	                
				}
			}
			
			if (has_matches()) {
				System.out.println("Generated board has matches, repeating...");
				repeat = true;
			}
			
			else if (solutions().size == 0) {
				System.out.println("Generated board doesn't have solutions, repeating...");
				repeat = true;
			}
			//System.out.print(this);
		} while(repeat);
		
		System.out.println("The generated board has no matches but some possible solutions.");
	}
	
	public void calcFallMovements() {
		for (int x = 0; x < size; ++x) {
			// From bottom to top
			for (int y = size-1; y >= 0; --y) {
				// origY stores the initial position in the fall
				_squares[x][y].origY = y;
				
				// If square is empty, make all the squares above it fall
				if (_squares[x][y].equals(Square.Type.sqEmpty)) {
					for (int k = y - 1; k >= 0; --k) {
						_squares[x][k].mustFall = true;
						_squares[x][k].destY++;
						
						if (_squares[x][k].destY > size-1)
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
					int y0 = _squares[x][y].destY;
					
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
	                _squares[x][y].origY = y - jumps;
	                _squares[x][y].destY = jumps;
	            }       
	        }
	    }   
	}
	
	//all uses only refer to use (check().size != 0)
	//should be able to refactor to return a boolean
	//then refactor Match bs away completely 
	public MultipleMatch find_matches() {
	    int k;
    
	    _matches.clear();
	    int currCoord = 0;
	    
	    // First, we check each row (horizontal)
	    for (int y = 0; y < size; ++y) {

	        for (int x = 0; x < size; ++x) {

	            Match currentRow = _rows[y][x];
	            currentRow.clear();
	            _matchCoords[currCoord].x = x;
	            _matchCoords[currCoord].y = y;
	            currentRow.add(_matchCoords[currCoord]);
	            ++currCoord;

	            for (k = x + 1; k < size; ++k) {
	                if (_squares[x][y].equals(_squares[k][y]) &&
	                   !_squares[x][y].equals(Square.Type.sqEmpty)) {
	                	_matchCoords[currCoord].x = k;
	    	            _matchCoords[currCoord].y = y;
	                    currentRow.add(_matchCoords[currCoord]);
	                    ++currCoord;
	                }
	                else {
	                    break;
	                }
	            }

	            if (currentRow.size > 2) {
	                _matches.add(currentRow);
	            }

	            x = k - 1;
	        }   
	    }

	    for (int x = 0; x < size; ++x) {
	        for (int y = 0; y < size; ++y) {

	            Match currentColumn = _columns[x][y];
	            currentColumn.clear();
	            _matchCoords[currCoord].x = x;
	            _matchCoords[currCoord].y = y;
	            currentColumn.add(_matchCoords[currCoord]);
	            ++currCoord;

	            for (k = y + 1; k < size; ++k) {
	                if (_squares[x][y].equals(_squares[x][k]) &&
	                	!_squares[x][y].equals(Square.Type.sqEmpty)) {
	                	_matchCoords[currCoord].x = x;
	    	            _matchCoords[currCoord].y = k;
	                	currentColumn.add(_matchCoords[currCoord]);
	                	++currCoord;
	                }
	                else {
	                    break;
	                }
	            }

	            if (currentColumn.size > 2) {
	                _matches.add(currentColumn);
	            }

	            y = k - 1;
	        }
	    }
	    //true if exist matches 
	    return _matches;
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
	
	public void endAnimation() {
		for(int x = 0; x < size; ++x){
	        for(int y = 0; y < size; ++y){
	            _squares[x][y].mustFall = false;
	            _squares[x][y].origY = y;
	            _squares[x][y].destY = 0;
	        }
	    }
	}
	
	public String toString() {
		String string = "";
		
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				string += "(" + _squares[i][j].origY + ", " + _squares[i][j].destY + ")  ";
			}
			
			string += "\n";
		}
		
		string += "\n";
		
		return string;
	}
}
