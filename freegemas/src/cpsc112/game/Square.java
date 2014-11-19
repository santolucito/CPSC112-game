package cpsc112.game;

public class Square  {
	public enum Color {sqEmpty,
		  sqWhite,
		  sqRed,
		  sqPurple,
		  sqOrange,
		  sqGreen,
		  sqYellow,
		  sqBlue,
		  sqGreenS}
	public int fallStartPosY;
	public int fallDistance;
	public boolean mustFall;
	private Color _color;
	
	public Square(Color color) {
		_color = color;
		mustFall = true;
	}
	
	public Square(Square other) {
		_color = other._color;
		fallStartPosY = other.fallStartPosY;
		fallDistance = other.fallDistance;
		mustFall = other.mustFall;
	}
	
	public Color getType() {
		return _color;
	}
	
	public void setType(Color type) {
		_color = type;
	}
	
	public boolean equals(Square other) {
		return (other._color == _color ||
				other._color == getDualType(_color) ||
				getDualType(other._color) == _color);
	}
	
	public boolean equals(Color type) {
		return type == _color;
	}
	
	public static Color getDualType(Color type){
		switch (type) {
		case sqEmpty:
			return Color.sqEmpty;
		case sqGreen:
			return Color.sqGreen;
		case sqGreenS:
			return Color.sqGreenS;
		case sqOrange:
			return Color.sqOrange;
		case sqPurple:
			return Color.sqPurple;
		case sqRed:
			return Color.sqRed;
		case sqWhite:
			return Color.sqWhite;
		case sqYellow:
			return Color.sqYellow;
		case sqBlue:
			return Color.sqBlue;
		default:
			return Color.sqWhite;
		}
	}
	public static Color numToType(int num) {
		switch (num) {
		case 1:
			return Color.sqWhite;
		case 2:
			return Color.sqRed;
		case 3:
			return Color.sqPurple;
		case 4:
			return Color.sqOrange;
		case 5:
			return Color.sqGreen;
		case 6:
			return Color.sqYellow;
		case 7:
			return Color.sqBlue;
		case 8:
			return Color.sqGreenS;
		default:
			return Color.sqEmpty;
		}
	}
	public static int typeToNum(Color type) {
		switch (type) {
		case sqWhite:
			return 1;
		case sqRed:
			return 2;
		case sqPurple:
			return 3;
		case sqOrange:
			return 4;
		case sqGreen:
			return 5;
		case sqYellow:
			return 6;
		case sqBlue:
			return 7;
		case sqGreenS:
			return 8;
		default:
			return 0;
		}
	}
	public String toString() {
		return typeToNum(this._color)+"";
	}
}
