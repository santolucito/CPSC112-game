package com.siondream.freegemas;

import java.util.Random;

public class BoardHelper {

	private Board b;
	private static Random randomGenerator = new Random();

	
	public BoardHelper(Board board) {
		this.b = board;
	}
	public void endAnimation() {
		for(int x = 0; x < b.size; ++x){
			for(int y = 0; y < b.size; ++y){
				b._squares[x][y].mustFall = false;
				b._squares[x][y].fallStartPosY = y;
				b._squares[x][y].fallDistance = 0;
			}
		}
	}
	public void applyFall() {
		for (int x = 0; x < b.size; ++x) {
			// From bottom to top in order not to overwrite squares
			for (int y = b.size-1; y >= 0; --y) {
				if (b._squares[x][y].mustFall == true &&
						!b._squares[x][y].equals(Square.Color.sqEmpty)) {
					int y0 = b._squares[x][y].fallDistance;

					if (y + y0 > b.size-1)
					{
						System.out.println("WARNING");
					}

					b._squares[x][y + y0] = b._squares[x][y];
					b._squares[x][y] = new Square(Square.Color.sqEmpty);
				}
			}
		}		
	}
	public void calcFallMovements() {
		for (int x = 0; x < b.size; ++x) {
			// From bottom to top
			for (int y = b.size-1; y >= 0; --y) {
				b._squares[x][y].fallStartPosY = y;

				// If square is empty, make all the squares above it fall
				if (b._squares[x][y].equals(Square.Color.sqEmpty)) {
					for (int k = y - 1; k >= 0; --k) {
						b._squares[x][k].mustFall = true;
						b._squares[x][k].fallDistance++;

						if (b._squares[x][k].fallDistance > b.size-1)
						{
							System.out.println("WARNING");
						}
					}
				}
			}
		}
		
	}
	public void fillSpaces() {
		for(int x = 0; x < b.size; ++x){
			// Count how many jumps do we have to fall
			int jumps = 0;

			for(int y = 0; y < b.size; ++y){
				if(!b._squares[x][y].equals(Square.Color.sqEmpty)) {
					break;
				}
				jumps++;
			}

			for(int y = 0; y < b.size; ++y){
				if(b._squares[x][y].equals(Square.Color.sqEmpty)) {
					b._squares[x][y].setType(Square.numToType(randomGenerator.nextInt(b.variety )+1));
					b._squares[x][y].mustFall = true;
					b._squares[x][y].fallStartPosY = y - jumps;
					b._squares[x][y].fallDistance = jumps;
				}       
			}
		} 
	}
	public void deleteMatches() {
		ListOfMatches matches = b.find_matches();
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
		b._squares[x][y].setType(Square.Color.sqEmpty);
	}

	private void makeSpecialSquare(int x, int y) {
		//need some concept of equivalance classes for this
		b._squares[x][y].setType(Square.getDualType(b._squares[x][y].getType()));
	}
	
	public Boolean[] getColumnBools(int x, int y) {
		Boolean[] matches = new Boolean[b.size];
		for(int i=0;i<b.size;i++){
			matches[i] = b._squares[x][y].equals(b._squares[x][i]); 
		}
		return matches;
	}
	public Boolean[] getRowBools(int x, int y) {
		Boolean[] matches = new Boolean[b.size];
		for(int i=0;i<b.size;i++){
			matches[i] = b._squares[x][y].equals(b._squares[i][y]); 
		}
		return matches;
	}
	public void fillInitialBoard() {
		do {
			for (int y = 0; y < b.size; ++y) {
				for (int x = 0; x < b.size; ++x) {
					b._squares[x][y] = new Square(Square.numToType(randomGenerator.nextInt(b.variety)+1));
					b._squares[x][y].fallStartPosY = y-b.size;
					b._squares[x][y].fallDistance = b.size;
				}
			}
		} while(b.has_matches() || b.findPossibleSwaps().length == 0);		
	}
	
	

}
