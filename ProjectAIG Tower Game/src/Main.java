import java.awt.event.MouseAdapter;

import javax.swing.JFrame;

public class Main extends JFrame{
	Panel panel;
	
	public Main(){
		panel = new Panel();
		add(panel);
		addKeyListener(panel);
		setSize(820,640);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String [] args){
		new Main();
	}
}
