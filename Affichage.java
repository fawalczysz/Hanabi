
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.*;
import java.util.*;

@SuppressWarnings({"serial","deprecation"})
public class Affichage extends JPanel implements MouseMotionListener{
	JFrame frame;
	JLayeredPane lpane;
	JPanel jPanel; 
	JPanel hoverPanel= new JPanel();
	JPanel commande = new JPanel();
	JPanel hintPanel;
	JPanel boardPanel;
	JPanel stackCardPanel;
	
	ArrayList<JPanel> actionPanels;
	
	JPanel blueTokenPanel;
	JPanel redTokenPanel;
	
	JLabel nbRedTokens;
	JLabel nbBlueTokens;
	
	JPanel cardStackPanel;
	JPanel discardStackPanel;
	JPanel cardPanel;
	
	OvalComponent displayCurrentPlayer;
	
	int hoveredPlayer;
	int hoveredCard;
	boolean clicked;
	static int algorithm = Image.SCALE_SMOOTH;
	
	public Affichage (){
		super(); // appel au constructeur de la classe mere
		jPanel= new JPanel();
		
		hoveredPlayer=-1;
		hoveredCard=-1;
		clicked=false;
		commande.setVisible(false);
		actionPanels= new ArrayList<JPanel>();
		for (int i=0;i<4;i++) {actionPanels.add(new JPanel());}
	}
	
	public void dispose() {
		frame.dispose();
	}
	
	
	public BufferedImage rotate(BufferedImage bImage,int newW ,int newH, boolean sens) {

		Image tmp = bImage.getScaledInstance(newW,newH,algorithm);
		BufferedImage resized = new BufferedImage(newW,newH,BufferedImage.TYPE_INT_ARGB);
		Graphics graphics=resized.createGraphics();
		graphics.drawImage(tmp, 0, 0, null);
		graphics.dispose();
		
		int h=resized.getHeight();
		int w=resized.getWidth();
		
	    BufferedImage bRotate = new BufferedImage(h,w,BufferedImage.TYPE_INT_ARGB);
	    int rgbValue;
    	for(int col = 0; col < w; col++){
    		for(int row = 0; row < h; row++){
	        	try {
	        		rgbValue = resized.getRGB(col,row);	
	        		if (sens) bRotate.setRGB(row - 1,w- col, rgbValue); // non clock sense
	        		else bRotate.setRGB(h- row - 1,col, rgbValue);	//clock sense
	            }catch (ArrayIndexOutOfBoundsException e) {};
	        }
	    }

	    return bRotate;
	}

	public void bind() {
		frame.addMouseMotionListener(this);
		enableClick();
	}
		
	private int cardIdentifier(int z,int width, int margin) {
		int card=0;
		while (z>width) {
			z=z-(width);
			if (z<margin) {card=-1;}
			else {card=card+1;z=z-margin;}
		}
		return card;
	}
	
	private void updateHover(int player,int card,int x1,int y1,int h,int w,int margin) {
		jPanel.setBackground(java.awt.Color.BLUE);
		if (player%2==0)  jPanel.setBounds(x1+(card*(w+margin))-10, y1-10, w+20, h+20);
		else jPanel.setBounds(x1-10, y1+(card*(h+margin))-10, w+20, h+20);
		jPanel.setVisible(true);
		lpane.add(jPanel,new Integer (1),0);
			
	}

	public void clearActionPanels() {
		jPanel.setVisible(false);
		for (int i=0;i<actionPanels.size();i++) {
			actionPanels.get(i).setVisible(false);
		}
	}
	
	

	public void mouseMoved(MouseEvent e) {
		if (!clicked) {
			int x= e.getX();
			int y= e.getY();

			jPanel.setVisible(false);
			if (439<x && x<927 && 588<y && y<753) {
				int card=cardIdentifier(x-439,110,16);
				hoveredCard=card;
				if (card!=-1) {
					updateHover(0,card,439,588,165,110,16);
					hoveredPlayer=0;
				}
				else hoveredPlayer=-1;
			}
			else if (18<x && x<183 && 142<y && y<630) {
				int card=cardIdentifier(y-142,110,16);
				hoveredCard=card;
				if (card!=-1) {
					updateHover(1,card,18,142,110,165,16);
					hoveredPlayer=1;
				}
				else hoveredPlayer=-1;
			}
			else if (439<x && x<927 && 20<y && y<185) {
				int card=cardIdentifier(x-439,110,16);
				hoveredCard=card;
				if (card!=-1) {  
					updateHover(2,card,439,20,165,110,16);
					hoveredPlayer=2;
				}
				else hoveredPlayer=-1;
			}
			else if (1183<x && x<1348 && 142<y && y<630) {
				int card=cardIdentifier(y-142,110,16);
				hoveredCard=card;
				if (card!=-1) {
					updateHover(3,card,1183,142,110,165,16);
					hoveredPlayer=3;
				}
				else hoveredPlayer=-1;
			}
			else hoveredPlayer=-1;
		}
			
	}
	
	
		
	public void mouseDragged(MouseEvent e) {
		int x= e.getX();
		int y= e.getY();

	}
	
	public void enableClick() {
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x=e.getX();
				int y=e.getY();
				if (hoveredPlayer!=-1) {
					if (!clicked) {
						commande = new JPanel();
						GridBagLayout layout = new GridBagLayout();
						GridBagConstraints gbc = new GridBagConstraints();  
		
						JLabel titre, action1, action2;
						if (hoveredPlayer==Hanabi.getCurrentPlayer()) {
							//own hand
							titre = new JLabel("   Action carte   ");
							action1 = new JLabel("Jouer une carte            ");				
							action2 = new JLabel("        Defausser une carte");
		
						}
						else {
							//other hand
							titre = new JLabel  ("   Action carte   ");
							action1 = new JLabel("Indice Couleur     ");				
							action2 = new JLabel("      Indice Valeur");
						}
						commande.setLayout(layout);
					    gbc.gridx = 0;
					    gbc.gridy = 0;
					    gbc.gridheight = 2;
					    gbc.gridwidth = 3;
					    commande.add(titre,gbc);
					    gbc.gridheight = 1;
					    gbc.gridwidth = 1;
					    gbc.gridy = 2;
					    gbc.gridx = 0;
					    commande.add(action1,gbc);
					    gbc.gridx = 1;
					    commande.add(action2,gbc);
						commande.setBackground(java.awt.Color.WHITE);
						commande.setBounds(508, 209, 350, 70);
						commande.setVisible(true);
						lpane.add(commande, new Integer(1),0);
						clicked=true;

					}
					else {
						if (533<x && 209<y && x<833 && y<279) {
							Action action = new Action(Hanabi.getCurrentPlayer());
							if (hoveredPlayer==Hanabi.getCurrentPlayer()) {//si memes cartes

								
								
								if (x<683) {	
									action.setCard(Hanabi.getPlayers().get(hoveredPlayer).getHand().get(hoveredCard));
									action.setIdHintPlayer(hoveredPlayer);
									action.setType(0);
									
									Hanabi.getPlayers().get(hoveredPlayer).playCard(Hanabi.getPlayers().get(hoveredPlayer).getHand().get(hoveredCard));
									Hanabi.getPlayers().get(hoveredPlayer).drawCard();
									Hanabi.next=true;
									
								}
								else {
									action.setCard(Hanabi.getPlayers().get(hoveredPlayer).getHand().get(hoveredCard));
									action.setIdHintPlayer(hoveredPlayer);
									action.setType(1);
									Hanabi.getPlayers().get(hoveredPlayer).discardCard(Hanabi.getPlayers().get(hoveredPlayer).getHand().get(hoveredCard));
									Hanabi.getPlayers().get(hoveredPlayer).drawCard();
									Hanabi.next=true;
								}
							}
							else {
								System.out.println("carte indicee: "+hoveredCard);
								if (x<683) {
									Card card=new Card(Hanabi.getPlayers().get(hoveredPlayer).getHand().get(hoveredCard).getColor(),0);
									action.setCard(card);
									action.setIdHintPlayer(hoveredPlayer);
									action.setType(3);
									
									Hanabi.getPlayers().get(hoveredPlayer).addInfo(new Card(Hanabi.getPlayers().get(hoveredPlayer).getHand().get(hoveredCard).getColor(),0));
									Hanabi.next=true;
									GameBoard.getInstance().removeBlueToken();
									
								}
								else {
									Card card=new Card(Color.NONE,Hanabi.getPlayers().get(hoveredPlayer).getHand().get(hoveredCard).getValue());
									action.setCard(card);
									action.setIdHintPlayer(hoveredPlayer);
									action.setType(3);
									Hanabi.getPlayers().get(hoveredPlayer).addInfo(new Card(Color.NONE,Hanabi.getPlayers().get(hoveredPlayer).getHand().get(hoveredCard).getValue()));
									Hanabi.next=true;
									GameBoard.getInstance().removeBlueToken();
								}

							}
							commande.setVisible(false);
							lpane.remove(commande);
							clicked=false;
							hoveredPlayer=-1;
							jPanel.setVisible(false);
							update();
							Hanabi.getHistory().add(action);
						}
						else {
							lpane.remove(commande);
							commande.setVisible(false);
							clicked=false;
							hoveredPlayer=-1;
							jPanel.setVisible(false);
							backgroundImage();
						}
						
					}
				}
			}
		}
		);
	}
	
	public void init() {
		try {
			lpane.setVisible(false);
		} catch (NullPointerException e) {}
		
		frame = new JFrame("Display Image");
		
		lpane = new JLayeredPane();
        		
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        frame.add(lpane);
        frame.setBackground(new java.awt.Color(0,0,0,0));
        frame.repaint();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
	}
	
	private void displayActionPanels(int x1,int y1,int h,int w) {
		actionPanels.get(0).setBackground(java.awt.Color.BLUE);
		actionPanels.get(0).setBounds(x1-10, y1-10, w+20, h+20);
		actionPanels.get(0).setVisible(true);
		lpane.add(actionPanels.get(0),new Integer (1),0);
	}

	
	private void displayHintPanels(int player,int card,int x1,int y1,int h,int w,int margin) {
		actionPanels.get(card).setBackground(java.awt.Color.BLUE);
		if (player%2==0)  actionPanels.get(card).setBounds(x1+(card*(w+margin))-10, y1-10, w+20, h+20);
		else actionPanels.get(card).setBounds(x1-10, y1+(card*(h+margin))-10, w+20, h+20);
		actionPanels.get(card).setVisible(true);
		lpane.add(actionPanels.get(card),new Integer (1),0);
	}

	
	public void displayAction(Action action) {
		switch (action.getType()){
		case 0:
			int val=Utils.colorToInt(action.getCard().getColor());
			//100* stack+22*stack+376+stack*4,304,110,165
			displayActionPanels(100* val+22*val+376+val*4,304,165,110);
			break;	
		case 1:
			displayActionPanels(225, 412, 165, 110);
			break;
		
		
		case 2 :
			try {
				lpane.remove(hintPanel);
				hintPanel.removeAll();
			} catch (NullPointerException e) {}
			
			hintPanel = new JPanel();
			JLabel hintLabel = new JLabel(action.getIdHintPlayer()+" "+action.getCard());
			hintPanel.add(hintLabel);
			
			hintPanel.setBackground(java.awt.Color.WHITE);
			hintPanel.setBounds(20, 20, 200, 70);
			hintPanel.setVisible(true);
			hintPanel.setOpaque(true);
			lpane.add(hintPanel, new Integer(2),0);
			
			for (int i=0;i<Utils.HAND_SIZE;i++) {
				try {
					if (action.getCard().getColor()==Color.NONE) {
						if (action.getCard().getValue()==Hanabi.getPlayers().get(action.getIdHintPlayer()).getHand().get(i).getValue()) {
							switch (action.getIdHintPlayer()) {
								case(0): displayHintPanels(0,i,439,588,165,110,16); break;
								case(1): displayHintPanels(1,i,18,142,110,165,16); break;
								case(2): displayHintPanels(2,i,439,20,165,110,16);break;
								case(3): displayHintPanels(3,i,1183,142,110,165,16);break;
							}
							
						}
					}
					else {
						if (action.getCard().getColor()==Hanabi.getPlayers().get(action.getIdHintPlayer()).getHand().get(i).getColor()) {
							switch (action.getIdHintPlayer()) {
								case(0): displayHintPanels(0,i,439,588,165,110,16); break;
								case(1): displayHintPanels(1,i,18,142,110,165,16); break;
								case(2): displayHintPanels(2,i,439,20,165,110,16);break;
								case(3): displayHintPanels(3,i,1183,142,110,165,16);break;
							}
							
						}		
						
					}
				}catch (IndexOutOfBoundsException e) {}	
			}
			break;
		}

	}
	
	public void backgroundImage() {
		try {
			boardPanel.removeAll();
		}catch (NullPointerException e) {}
        boardPanel = new JPanel();
        ImageIcon boardIcon=new ImageIcon("graphics/GameBoard.jpg");
        Image boardIconResized = boardIcon.getImage().getScaledInstance(1366, 768,algorithm);    
        JLabel boardLabel = new JLabel(new ImageIcon(boardIconResized));
        boardPanel.add(boardLabel);
        boardPanel.setBounds(0,0,1366,768);
        boardPanel.setOpaque(true);
		boardPanel.setVisible(true);
        lpane.add(boardPanel, new Integer(0),0);

	}
	
	public void gameBoardStack() {
		//stack display
		try {
			lpane.remove(stackCardPanel);
			stackCardPanel.removeAll();
		}catch (NullPointerException e) {}
		
		for (int stack=0;stack<5;stack++) {
			stackCardPanel = new JPanel();
			Color color=Utils.intToColor(stack);
			
			int stackValue=GameBoard.getInstance().getCardStack(color);
			if (stackValue!=0) {
				String stackCardName = "graphics/Card"+colorToString(color)+stackValue+".png";
				
		        ImageIcon stackCardIcon=new ImageIcon(stackCardName);
		        Image stackCardIconResized = stackCardIcon.getImage().getScaledInstance(104, 160,algorithm);    
		        JLabel stackCardLabel = new JLabel(new ImageIcon(stackCardIconResized));
		        stackCardLabel.setOpaque(true);
		        		   
		        stackCardPanel.add(stackCardLabel);		        
		        stackCardPanel.setBounds(100* stack+22*stack+376+stack*4,304,110,165);
		        stackCardPanel.setBackground(new java.awt.Color(0,0,0,0));
		        lpane.add(stackCardPanel, new Integer(2),0);
			}
		}
		
		
	}
	
	
	public void updateTokens() {
		//blueTokenDisplay
		try {
			lpane.remove(nbRedTokens);
			lpane.remove(nbBlueTokens);
		

		}catch (NullPointerException e) {} 
        
		

        //tokens
		blueTokenPanel = new JPanel();
		redTokenPanel = new JPanel();   
		
        ImageIcon blueTokenIcon=new ImageIcon("graphics/blueToken.png");
        Image blueTokenResized = blueTokenIcon.getImage().getScaledInstance(65, 65,Image.SCALE_DEFAULT);    
        JLabel blueTokenLabel = new JLabel(new ImageIcon(blueTokenResized));
        blueTokenPanel.setBackground(new java.awt.Color(0,0,0,0));

        blueTokenPanel.add(blueTokenLabel);
        blueTokenPanel.setBounds(1051, 254, 69, 69);
        
        lpane.add(blueTokenPanel, new Integer(1),0);  
        blueTokenPanel.setVisible(true);
		

        redTokenPanel.setBackground(new java.awt.Color(0,0,0,0));
        ImageIcon redTokenIcon=new ImageIcon("graphics/redToken.png");
        Image redTokenResized = redTokenIcon.getImage().getScaledInstance(65, 65,Image.SCALE_DEFAULT);    
        JLabel redTokenLabel = new JLabel(new ImageIcon(redTokenResized));

        redTokenPanel.add(redTokenLabel);
        redTokenPanel.setBounds(1051, 449, 69, 69);
        
        lpane.add(redTokenPanel, new Integer(1),0); 
        redTokenPanel.setVisible(true);
        
		
		
		
		nbRedTokens = new JLabel(String.valueOf(GameBoard.getInstance().getRedTokenStack()));
		nbBlueTokens = new JLabel(String.valueOf(GameBoard.getInstance().getBlueTokenStack()));
				
        nbBlueTokens.setFont(new Font(nbBlueTokens.getFont().getName(), Font.PLAIN,20));
        nbBlueTokens.setBounds(1080,283,20,20);
        nbBlueTokens.setBackground(new java.awt.Color(0,0,0,0));
        lpane.add(nbBlueTokens, new Integer(2),0);  
		nbBlueTokens.setVisible(true);
        
		//redTokenDisplay

        nbRedTokens.setFont(new Font(nbRedTokens.getFont().getName(), Font.PLAIN,20));
        nbRedTokens.setBounds(1080,478,20,20);
        nbRedTokens.setBackground(new java.awt.Color(0,0,0,0));
        lpane.add(nbRedTokens, new Integer(2),0);  
        nbRedTokens.setVisible(true);
	}
	
	public void cardStackDisplay() {
        //cardStack display
		try {
			lpane.remove(cardStackPanel);
			cardStackPanel.removeAll();
		} catch(NullPointerException e) {}
		if (GameBoard.getInstance().getCardDeckSize()>0) {
			cardStackPanel=new JPanel();
	        ImageIcon cardStackIcon = new ImageIcon("graphics/CardBack.png");
	        Image cardStackResized = cardStackIcon.getImage().getScaledInstance(104, 160,Image.SCALE_DEFAULT);    
	        JLabel cardStackLabel = new JLabel(new ImageIcon(cardStackResized));
	        cardStackPanel.setOpaque(true);
	        cardStackPanel.add(cardStackLabel);
	        cardStackPanel.setBounds(225, 196, 110, 165);
	        cardStackPanel.setBackground(new java.awt.Color(0,0,0,0));
	        lpane.add(cardStackPanel, new Integer(2),0);
		}

	}
	
	public void updateDiscardStack() {
		if (discardStackPanel != null) {
			lpane.remove(discardStackPanel);
			discardStackPanel.removeAll();
		}
		
		//discardStack display
		discardStackPanel=new JPanel();
		Card carte=GameBoard.getInstance().getDiscardDeck().peek();
		String discardStackName="graphics/Card"+colorToString(carte.getColor())+carte.getValue()+".png";
        ImageIcon discardStackIcon=new ImageIcon(discardStackName);
        Image discardStackResized = discardStackIcon.getImage().getScaledInstance(104, 160,Image.SCALE_DEFAULT);    
        JLabel discardStackLabel = new JLabel(new ImageIcon(discardStackResized));
        discardStackLabel.setOpaque(true);
        discardStackPanel.add(discardStackLabel);
        discardStackPanel.setBounds(225, 412, 110, 165);
        
        discardStackPanel.setBackground(new java.awt.Color(0,0,0,0));
        lpane.add(discardStackPanel, new Integer(2),0);
	}
	
	public void updateHand(int player) {
        //hand display      
        
		for (int i =0 ;i<Utils.HAND_SIZE;i++) {
			cardPanel = new JPanel();
			//out of bounds 
			try {
				
				Card carte=Hanabi.getPlayers().get(player).getHand().get(i);
				String cardName="";
				if (player==0) {
						cardName="graphics/CardBack.png";
					}
				else {
					cardName="graphics/Card"+colorToString(carte.getColor())+carte.getValue()+".png";					
				}

				if (player%2==0) {

			        ImageIcon cardIcon=new ImageIcon(cardName);
			        Image cardIconResized = cardIcon.getImage().getScaledInstance(104, 160,algorithm);    
			        JLabel cardLabel = new JLabel(new ImageIcon(cardIconResized));
			        cardLabel.setOpaque(true);
			        cardPanel.add(cardLabel);
				}
				else{
					try {
						BufferedImage bImage = ImageIO.read(new File(cardName));
						BufferedImage cardTurned = rotate(bImage,104,160,player==1);
				        JLabel cardLabel = new JLabel(new ImageIcon(cardTurned));
				        cardLabel.setOpaque(true);		
				        cardPanel.add(cardLabel);
				        
					}catch (IOException e) {}
				}
		        
		        cardPanel.setBackground(new java.awt.Color(0,0,0,0));
		        switch (player) {
			        case (0) :
			        	cardPanel.setBounds(100* i+22*i+439+i*4,588,110,165);
			        	break;
					case (1) :
			        	cardPanel.setBounds(18,i*100+142+i*26,165,110);
						break;			        	
			        case (2) :
			        	cardPanel.setBounds(100* i+22*i+439+i*4,20,110,165);
			        	break;		        
					case (3) :
			        	cardPanel.setBounds(1183,i*100+142+i*26,165,110);
						break;
			        
		        }
		        cardPanel.setBackground(new java.awt.Color(0,0,0,0));
		        cardPanel.setOpaque(true);
		        lpane.add(cardPanel, new Integer(2),0);
		        
			}catch (IndexOutOfBoundsException e) {}	
		}
	}

	public void displayCurrentPlayer() {
		switch (Hanabi.getCurrentPlayer()) {
			case(1):
				displayCurrentPlayer = new OvalComponent(0,0,20,20); 
				displayCurrentPlayer.setBounds(200,380,20,20);break;
			case(2):
				displayCurrentPlayer = new OvalComponent(0,0,20,20); 
				displayCurrentPlayer.setBounds(675,200,20,20); break;
			case(3):
				displayCurrentPlayer = new OvalComponent(0,0,20,20);
				displayCurrentPlayer.setBounds(1140,380,20,20);break;
			case(0):
				displayCurrentPlayer = new OvalComponent(0,0,20,20); 
				displayCurrentPlayer.setBounds(675,540,20,20);break;
		}
		
		displayCurrentPlayer.setVisible(true);
		displayCurrentPlayer.setOpaque(true);
		displayCurrentPlayer.setBackground(new java.awt.Color(0,0,0,0));
		lpane.add(displayCurrentPlayer,new Integer(5),0);
	}
	
	public void redraw() {
		lpane.repaint();
		repaint();
	}
	
	
	public void update() {

		try {
			lpane.removeAll();
		} catch (NullPointerException e) {}
		lpane = new JLayeredPane();
		clearActionPanels();

		backgroundImage();
		
		displayCurrentPlayer();
		cardStackDisplay();
		gameBoardStack();
		if (!GameBoard.getInstance().getDiscardDeck().isEmpty()) updateDiscardStack();	
		updateTokens();
		if (!Hanabi.getHistory().isEmpty())
			displayAction(Hanabi.getHistory().get(Hanabi.getHistory().size()-1));

		
		lpane.setVisible(true);
		try {
			lpane.remove(cardPanel);
			cardPanel.removeAll();
		} catch (NullPointerException e) {}
		for(int i=0;i<4;i++)
			updateHand(i);
		
		frame.add(lpane);

		frame.setVisible(true);
	}
	
	private String colorToString(Color color) {
		String output="";
		switch (color) {
			case WHITE:
				output="White"; break;
			case RED:
				output="Red"; break;
			case BLUE:
				output="Blue"; break;
			case YELLOW:
				output="Yellow"; break;
			case GREEN:
				output="Green"; break;
			default:
				break;
		}
		return output;
		
	}
	

}


