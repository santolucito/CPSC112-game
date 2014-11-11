package com.siondream.freegemas;

import java.util.ArrayList;

public class ListOfMatches extends ArrayList<Match> {
	
	private static final long serialVersionUID = 1L;

	public ListOfMatches() {
		super();
	}
	
	public boolean isMatched(Coord c) {
		for (int i = 0; i < size(); ++i) {
			if (get(i).isMatched(c)) {
				return true;
			}
		}
		
		return false;
	}
}
