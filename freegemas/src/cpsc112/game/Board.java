package cpsc112.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board {

	public BoardHelper helper;
	public Random randomGenerator = new Random();

	public Square[][] _squares;
	public int size = 8;
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
	
	public void fillInitialBoard(int[][] s) {
		if(s.length!=size && s[0].length!=size){
			System.err.println("Tried to initialize board with wrong size array");
			System.err.println("Expected size: "+size+", Board size: "+s.length+" by "+ s[0].length);
			System.exit(0);
		}
		for (int x = 0; x < s.length; ++x) {
			for (int y = 0; y < s.length; ++y) {
				_squares[x][y] = new Square(Square.numToType(s[x][y]));
			}
		}
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

	public boolean[] getRowBools(Point p) {
		boolean[] matches = new boolean[size];
		for(int i=0;i<size;i++){
			matches[i] = _squares[p.x][p.y].equals(_squares[i][p.y]); 
		}
		return matches;
	}
	public boolean[] getColumnBools(Point p) {
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

		for (int y = 0; y < size; ++y) {
			for (int x = 0; x < size; ++x) {
				//output += "(" + squares[i][j].fallStartPosY + ", " + squares[i][j].fallDistance + ")  ";
				output += _squares[x][y]+" ";
			}
			output += "\n";
		}

		return output;
	}

	public void runTests() {
		
		this.size=3;
		this.helper.size=3;
		//1 1 2
		//2 3 1
		//3 2 2
		int[][] squares =  {{1,2,3},{1,3,2},{2,1,3}};
		this.fillInitialBoard(squares);
		System.out.println("Running unit tests...\nUsing the following board");
		System.out.println(this);


		for(boolean b : getColumnBools(new Point(0,0)))
			System.out.println(b);
		System.out.println();

		unitTest(new Point[][]{{}},
				this.helper.findMatches(),
				"findMatches");

		this.helper.swap(new Point(2,0),new Point(2,1));
		unitTest(new Point[][] {{new Point(0,0),new Point(1,0),new Point(2,0)}},
				this.helper.findMatches(),
				"findMatches");
		this.helper.swap(new Point(2,0),new Point(2,1));

		unitTest(new Point[] {new Point(0,0),new Point(1,0)}, 
				this.helper.buildPossibleMatchRow(new Point(0,0)),
				"buildPossibleMatchRow on (0,0)");
		unitTest(new Point[] {new Point(0,2)}, 
				this.helper.buildPossibleMatchRow(new Point(0,2)),
				"buildPossibleMatchRow on (0,2)");
		unitTest(new Point[] {new Point(2,2)}, 
				this.helper.buildPossibleMatchRow(new Point(2,2)),
				"buildPossibleMatchRow on (2,2)");
		
		unitTest(new Point[]{new Point(0,0)},
				this.helper.buildPossibleMatchColumn(new Point(0,0)),
				"buildPossibleMatchColumn on (0,0)");
		unitTest(new Point[] {new Point(0,2)}, 
				this.helper.buildPossibleMatchColumn(new Point(0,2)),
				"buildPossibleMatchColumn on (0,2)");
		unitTest(new Point[] {new Point(2,2)}, 
				this.helper.buildPossibleMatchColumn(new Point(2,2)),
				"buildPossibleMatchColumn on (2,2)");

		unitTest(new Point[] {new Point(2,0),new Point(1,1)},
				this.helper.findPossibleSwaps(),
				"findPossibleSwaps");		
		
		Point[] ex = new Point[] {new Point(0,1),new Point(1,1)};
		unitTest(new Point[] {new Point(0,1),new Point(1,1),null,null},
				this.helper.expandArray(ex),
				"expandArray");
		
		this.size=4;
		this.helper.size=4;
		//1 1 2 1
		//2 3 1 3
		//3 2 2 1
		//3 1 1 3
		int[][] squares2 =  {{1,2,3,3},{1,3,2,1},{2,1,3,1},{1,3,1,3}};
		this.fillInitialBoard(squares2);
		System.out.println("\nRunning more unit tests...\nUsing the following board");
		System.out.println(this);

		
		unitTest(new Point[][]{{}},
				this.helper.findMatches(),
				"findMatches");

		this.helper.swap(new Point(2,0),new Point(2,1));
		unitTest(new Point[][] {{new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0)}},
				this.helper.findMatches(),
				"findMatches");
		this.helper.swap(new Point(2,0),new Point(2,1));

		unitTest(new Point[] {new Point(0,0),new Point(1,0)}, 
				this.helper.buildPossibleMatchRow(new Point(0,0)),
				"buildPossibleMatchRow on (0,0)");
		unitTest(new Point[] {new Point(0,2)}, 
				this.helper.buildPossibleMatchRow(new Point(0,2)),
				"buildPossibleMatchRow on (0,2)");
		unitTest(new Point[] {new Point(2,2)}, 
				this.helper.buildPossibleMatchRow(new Point(2,2)),
				"buildPossibleMatchRow on (2,2)");
		
		unitTest(new Point[]{new Point(0,0)},
				this.helper.buildPossibleMatchColumn(new Point(0,0)),
				"buildPossibleMatchColumn on (0,0)");
		unitTest(new Point[] {new Point(0,2),new Point(0,3)}, 
				this.helper.buildPossibleMatchColumn(new Point(0,2)),
				"buildPossibleMatchColumn on (0,2)");
		unitTest(new Point[] {new Point(2,2)}, 
				this.helper.buildPossibleMatchColumn(new Point(2,2)),
				"buildPossibleMatchColumn on (2,2)");

		unitTest(new Point[] {new Point(0,1),new Point(1,1),new Point(2,0),new Point(2,1),new Point(2,2),new Point(3,2)},
				this.helper.findPossibleSwaps(),
				"findPossibleSwaps");
		
	}


	private static void unitTest(Point[][] correct, Point[][] user, String method) {
		System.out.println("\nTESTING "+method);
		boolean works = true;
		for(int k=0;k<correct.length;k++){
			for(int i=0;i<user.length;i++){
				if(user[i].length<3) works=false;
				for(int j=0;j<user[i].length;j++){
					works=Arrays.asList(correct[k]).contains(user[i][j])&&works;
				}
			}	
		}
		if(Arrays.deepEquals(new Point[][] {{}},correct) && Arrays.deepEquals(new Point[][] {{}},user))
			works=true;
		if(works)
			System.out.println("PASSED");
		else{
			System.out.println("FAILED");
			System.out.println("Correct output: "+Arrays.deepToString(correct));
			System.out.println("Your output:    "+Arrays.deepToString(user));
		}
	}


	public static void unitTest(Point[] correct, Point[] user, String method) {
		System.out.println("\nTESTING "+method);
		boolean works = true;
		if(method.equals("findPossibleSwaps")){
			for(int i=0;i<correct.length;i++){
				if(correct[i]!=null)
					works = Arrays.asList(user).contains(correct[i])&&works;
			}
			for(int i=0;i<user.length;i++){
				if(user[i]!=null)
				works = Arrays.asList(correct).contains(user[i])&&works;
			}
		}
		else if(!Arrays.deepEquals(correct,user)){
			works = false;
		}
		if(works){
			System.out.println("PASSED");
	}
		else{
			System.out.println("FAILED");
			System.out.println("Correct output: "+Arrays.deepToString(correct));
			System.out.println("Your output:    "+Arrays.deepToString(user));
		}
	}


}
