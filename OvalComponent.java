
import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class OvalComponent extends JPanel {
	int x1,y1,w=40,h=40;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(java.awt.Color.GREEN);
		g.fillOval(x1,y1,w,h);
	}
	
	public OvalComponent(int x1,int y1, int w, int h) {
		this.x1=x1;
		this.y1=y1;
		this.w=w;
		this.h=h;
	}
	
}
