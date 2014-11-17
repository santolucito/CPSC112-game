package cpsc112.game;

import java.util.ArrayList;

public class ListOfMatches extends ArrayList<Match> {
	
	private static final long serialVersionUID = 1L;

	public ListOfMatches() {
		super();
	}
	
	public ListOfMatches(ArrayList<Match> newAsArrayList) {
		super(newAsArrayList);
	}

	public boolean isMatched(Point c) {
		for (int i = 0; i < size(); ++i) {
			if (get(i).isMatched(c)) {
				return true;
			}
		}
		
		return false;
	}
}
