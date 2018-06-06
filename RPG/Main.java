package RPG;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Entity.Player;

public class Main extends JFrame {

	public Main() throws IOException {
		ImageLoader.loadImages();
		ImageIcon[] buttons = new ImageIcon[] {
				new ImageIcon(ImageLoader.getPlayerImage(0).getScaledInstance(60, 60, Image.SCALE_SMOOTH)),
				new ImageIcon(ImageLoader.getPlayerImage(1).getScaledInstance(60, 60, Image.SCALE_SMOOTH)),
				new ImageIcon(ImageLoader.getPlayerImage(2).getScaledInstance(60, 60, Image.SCALE_SMOOTH)),
				new ImageIcon(ImageLoader.getPlayerImage(3).getScaledInstance(60, 60, Image.SCALE_SMOOTH)) };
		Level l = new Level(this, Player.getDefualtPlayer(JOptionPane.showOptionDialog(null, "Please select character",
				"Character Select", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0])),
				20);
		this.setContentPane(l);
		this.addKeyListener(l);
		this.addMouseListener(l);
		Thread t = new Thread(l);
		t.start();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 800);
		setVisible(true);
		this.setIconImage(ImageLoader.getTitle());
	}

	public static void main(String[] args) {
		try {
			new Main();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
