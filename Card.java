
public class Card {
	
	private Color color;
	private int   value;
	
	Card() { color = Color.NONE; value = 0; }
	
	Card(Color color, int value)
	{
		this.color = color;
		this.value = value;
	}

	Color getColor()			{ return color; }
	void  setColor(Color color) { this.color = color; }
	
	int  getValue() 		 { return value; }
	void setValue(int value) { this.value = value; }
	
	public String toString() { return "[" + getColorString(color) + ", " + ((value > 0) ? value : "?") + "]"; }
	
	String getColorString(Color color)
	{
		switch (color)
		{
			case WHITE   : return "White ";
			case RED     : return "Red   ";
			case BLUE    : return "Blue  ";
			case YELLOW  : return "Yellow";
			case GREEN   : return "Green ";
			case NONE    : return " ???? ";
		}
		
		return null;
	}
}