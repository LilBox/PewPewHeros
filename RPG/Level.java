package RPG;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Entity.Entity;
import Entity.Player;

public class Level extends JPanel implements Runnable, KeyListener, MouseListener {

	private MapGenerator gen;
	private ArrayList<Entity> entities, add, remove;
	private Player player;
	private Main m;

	private int refreshTime = 20;

	private Spawner spawn;
	
	double x, y;

	public Level(Main m, Player player, int refresh) {
		this.m = m;
		this.player = player;
		gen = new MapGenerator();
		this.refreshTime = refresh;
		add = new ArrayList<Entity>();
		remove = new ArrayList<Entity>();
		entities = new ArrayList<Entity>();
		spawn = new Spawner(this);
		spawn.registerSpawn(Spawner.BASIC, 5, 25000, 500, 500, 25000);
		spawn.registerSpawn(Spawner.BAILEY, 1, 210000, 500, 500, 0);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D gI = (Graphics2D) g;
		gI.fillRect(0, 0, this.getWidth(), this.getHeight());

		gI.drawImage(
				gen.getTempImage((int) Math.round(-player.getX() + gen.size * 128),
						(int) Math.round(-player.getY() + gen.size * 128), this.getWidth(), this.getHeight()),
				0, 0, null);
		
		synchronized (entities) {
			for (Entity e : entities) {
				gI.drawImage(e.getImage(),
						(int) Math.round(e.getX() - player.getX() + (this.getWidth() - e.getImage().getWidth()) / 2),
						(int) Math.round(e.getY() - player.getY() + (this.getHeight() - e.getImage().getHeight()) / 2),
						this);
			}
		}

		gI.drawImage(player.getPlayerInfoBar(400, 120), 10, 10, null);

		gI.drawImage(player.getImage(), (this.getWidth() - player.getImage().getWidth()) / 2,
				(this.getHeight() - player.getImage().getHeight()) / 2, null);

		gI.drawImage(player.getImage(this), 0, 0, null);

		g.drawImage(player.getPlayerSkills(4 * this.getHeight() / 10, this.getHeight() / 10),
				(int) (1.5 * this.getWidth() / 4), (int) 8.5 * this.getHeight() / 10, null);

	}

	@Override
	public void run() {
		while (true) {
			long time = System.currentTimeMillis();
			if (player.canMove()) {
				player.setX(player.getX() + player.getDX()).setY(player.getY() + player.getDY());
			}
			synchronized (entities) {
				spawn.update(this.getRefreshTime());
			}
			player.update(this);
			synchronized (entities) {
				for (Entity e : entities) {
					e.update(this);
				}
				for (Entity e : add) {
					entities.add(e);
				}
				for (Entity e : remove) {
					entities.remove(e);
				}
			}
			add.clear();
			remove.clear();
			synchronized (entities) {
				for (Entity e : entities) {
					e.setX(e.getX() + e.getDX()).setY(e.getY() + e.getDY());
				}
			}
			this.invalidate();
			this.repaint();
			try {
				Thread.sleep((refreshTime - (System.currentTimeMillis() - time)) > 0
						? refreshTime - (System.currentTimeMillis() - time)
						: 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Entity> getEntites() {
		return entities;
	}

	public Level addEntity(Entity e) {
		if (e != null) {
			add.add(e);
		}
		return this;
	}

	public Level removeEntity(Entity e) {
		if (e != null) {
			remove.add(e);
		}
		return this;
	}

	public Level playerDead() {
		JOptionPane.showMessageDialog(null, "YOU LOST!");
		System.exit(1);
		return this;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_D:
			x = player.getSpeed();
			break;
		case KeyEvent.VK_A:
			x = -player.getSpeed();
			break;
		case KeyEvent.VK_W:
			y = -player.getSpeed();
			break;
		case KeyEvent.VK_S:
			y = player.getSpeed();
			break;
		}
		player.setDX(y == 0 ? x : x / 2).setDY(x == 0 ? y : y / 2);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_D:
			x = x == player.getSpeed() ? 0 : x;
			break;
		case KeyEvent.VK_A:
			x = x == -player.getSpeed() ? 0 : x;
			break;
		case KeyEvent.VK_W:
			y = y == -player.getSpeed() ? 0 : y;
			break;
		case KeyEvent.VK_S:
			y = y == player.getSpeed() ? 0 : y;
			break;
		}
		player.setDX(y == 0 ? x : x / 2).setDY(x == 0 ? y : y / 2);
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, m);
		player.keyPress(ke.getKeyChar(), p.x + ((int) player.getX()) - this.getWidth() / 2,
				p.y + ((int) player.getY()) - this.getHeight() / 2, this);
	}

	@Override
	public void mouseClicked(MouseEvent me) {
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		player.mousePress(me.getX(), me.getY(), this);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		player.mouseRelease(me.getX(), me.getY(), this);
	}

	public int getRefreshTime() {
		return refreshTime;
	}

	public Player getPlayer() {
		return player;
	}
}
