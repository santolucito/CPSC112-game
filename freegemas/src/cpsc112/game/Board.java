package cpsc112.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board {

	public BoardHelper helper;
	public Random randomGenerator = new Random();

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
			//System.out.println(find_matches().size() +" "+ findPossibleSwaps().length);

		} while(hasMatches() || findPossibleSwaps().length == 0);

	}

	public ListOfMatches findMatches() {
		Point[][] m = helper.findMatches();
		convert(m);
		return _matches;
	}

	private void convert(Point[][] built) {
		_matches.clear();
		for(int i=0;i<built.length;i++){
			if(built[i].length>=3){
				checkCorrectness(built[i]);
				_matches.add(convert(built[i]));
			}
		}
	}

	public Point[] findPossibleSwaps() {
		Point[] ps =  helper.findPossibleSwaps();
		if (ps == null)
		{
			Point p = new Point(0,0); 
			ps = new Point[1];
			ps[0] = p;
		}
		return ps;	}

	public void swap(int x1, int y1, int x2, int y2){
		helper.swap(new Point(x1, y1), new Point(x2, y2));
	}
	public boolean hasMatches(){
		return findMatches().size()!=0;
	}

	public void checkCorrectness(Point[] built) {
		if(built[built.length-1]==null){
			System.err.println("The array you built in buildPossibleMatchHori/Vert was too long\n"+
					"Make sure it is exactly as long as the match you found\n"+
					"length: "+built.length+"\n"+
					"x[length-1]: "+built[built.length-1]);
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
		ListOfMatches matches = findMatches();
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

	public boolean[] getColumnBools(Point p) {
		boolean[] matches = new boolean[size];
		for(int i=0;i<size;i++){
			matches[i] = _squares[p.x][p.y].equals(_squares[i][p.y]); 
		}
		return matches;
	}
	public boolean[] getRowBools(Point p) {
		boolean[] matches = new boolean[size];
		for(int i=0;i<size;i++){
			matches[i] = _squares[p.x][p.y].equals(_squares[p.x][i]); 
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


	public String toHTML() {
		String output = "<table>";

		for (int i = 0; i < size; ++i) {
			output += "<tr>";
			for (int j = 0; j < size; ++j) {
				output += "<td><img src=\"http://santolucito.github.io/cs112/"+_squares[i][j].toString()+".png\"></td>";
			}
			output += "</tr>";
		}
		output += "</table>";

		return output;
	}
	
	public String toString() {
		String output = "";

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				//output += "(" + squares[i][j].fallStartPosY + ", " + squares[i][j].fallDistance + ")  ";
				output += "["+_squares[i][j].toString()+"] ";
			}
			output += "\n";
		}
		output += "\n";

		return output;
	}

}
