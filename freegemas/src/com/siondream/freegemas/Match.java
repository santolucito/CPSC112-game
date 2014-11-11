package com.siondream.freegemas;

import java.util.ArrayList;

public class Match extends ArrayList<Coord> {

	private static final long serialVersionUID = -978651348070998435L;

	public Match() {
		super();
	}
	
	public Match(ArrayList<Coord> arrayList) {
		super(arrayList);
	}

	public boolean isMatched(Coord c) {
		return contains(c);
	}
	
	public Coord getMidSquare() {
		if (size() > 0)	{
			return get(size() / 2);
		}
		return null;
	}
	
	public String toString() {
		String string = "Matches: ";
		
		for (int i = 0; i < size(); ++i) {
			string += "(" + get(i).x + ", " + get(i).y + ")"; 
		}
		
		return string;
	}
}
