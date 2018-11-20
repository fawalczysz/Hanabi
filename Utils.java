
import java.util.Scanner;

public class Utils {
	static final int	 HAND_SIZE	 = 4;
	static final Scanner inputReader = new Scanner(System.in);
	
	static int getIntFromInput(String prompt, int min, int max) {
		int		answer = min - 1;
		
		while (answer < min || answer > max) {
			System.out.println(prompt);
			answer = inputReader.nextInt();
		}
		
		return answer;
	}

	public static int colorToInt(Color color) {
		int val=-1;
		switch (color) {
			case WHITE:
				val=0; break;
			case RED:
				val=1; break;
			case BLUE:
				val=2; break;
			case YELLOW:
				val=3; break;
			case GREEN:
				val=4; break;
			default:
				break;
		}
		return val;
	}
	
	public static Color intToColor(int val) {
		Color color=Color.NONE;
		switch (val) {
			case (0):
				color=Color.WHITE; break;
			case (1):
				color=Color.RED; break;
			case (2):
				color=Color.BLUE; break;
			case (3):
				color=Color.YELLOW; break;
			case (4):
				color=Color.GREEN; break;
			default:
				break;
		}
		return color;
	}	
}
