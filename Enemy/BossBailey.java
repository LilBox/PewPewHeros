package Enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entity.BasicProjectile;
import Entity.Entity;
import Entity.Player;
import Item.BasicItem;
import RPG.ImageLoader;
import RPG.Level;
import RPG.Utils;

public class BossBailey extends Entity {

	private final static double FIRE_RATE = 4500, FIRE_RANGE = 300, PLAYER_TRACK_RANGE = 600, DAMAGE = 45, SPEED = 20,
			MOVE_SPEED = 4, HEALTH_GROWTH = 350, BASE_HP = 1000, DROP_RATE = 100, ATTACK_PERCENT = 50,
			ATTACK_BASE = 100, ATTACK_GROWTH = 50, DEFENSE_BASE = 10, DEFENSE_GROWTH = 2.5;

	private double count;

	public BossBailey(int level, int x, int y) {
		this.setMaxHealth(BASE_HP + HEALTH_GROWTH * level).setHealth(BASE_HP + HEALTH_GROWTH * level).setY(y)
				.setLevel(level);
	}

	@Override
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(ImageLoader.getBossImage(0).getWidth(),
				ImageLoader.getBossImage(0).getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		g.drawImage(ImageLoader.getBossImage(0), 0, 0, null);

		double healthBarXLen = (2 * image.getHeight() / 5 + 3 * image.getWidth() / 5) * this.getHealth()
				/ this.getMaxHealth();

		g.setColor(Color.BLACK);
		g.drawLine(1 * image.getWidth() / 5, image.getHeight() / 5, 4 * image.getWidth() / 5, image.getHeight() / 5);
		g.drawLine(1 * image.getWidth() / 5, 2 * image.getHeight() / 5, 4 * image.getWidth() / 5,
				2 * image.getHeight() / 5);
		g.drawArc(1 * image.getWidth() / 5 - image.getHeight() / 10, image.getHeight() / 5, image.getHeight() / 5,
				image.getHeight() / 5, -90, -180);
		g.drawArc(4 * image.getWidth() / 5 - image.getHeight() / 10, image.getHeight() / 5, image.getHeight() / 5,
				image.getHeight() / 5, -90, 180);

		g.setColor(Color.RED);

		g.fillRect((int) (1.5 * image.getWidth() / 5), image.getHeight() / 5 + 1,
				(int) Math.round(healthBarXLen > image.getHeight() / 5
						? healthBarXLen < (image.getHeight() / 5 + 3 * image.getWidth() / 5)
								? healthBarXLen - image.getHeight() / 5
								: 3 * image.getWidth() / 5
						: 0),
				image.getHeight() / 5 - 2);

		g.fillArc(1 * image.getWidth() / 5 - image.getHeight() / 10, image.getHeight() / 5 + 1,
				(int) Math.round((healthBarXLen > image.getHeight() / 5 ? image.getHeight() / 5 : healthBarXLen)),
				image.getHeight() / 5 - 2, -90, -180);
		g.fillArc(4 * image.getWidth() / 5 - image.getHeight() / 10, image.getHeight() / 5 + 1,
				(int) Math.round((healthBarXLen > image.getHeight() / 5 + 2 * image.getWidth() / 5
						? healthBarXLen - image.getHeight() / 5 - 2 * image.getWidth() / 5
						: 0)),
				image.getHeight() / 5 - 2, -90, 180);

		return image;
	}

	@Override
	public int getSide() {
		return Entity.ENEMY;
	}

	@Override
	public void update(Level l) {
		if (this.getHealth() <= 0) {
			((Player) this.getKilledBy())
					.increaseXP(Utils.calculateXP(this.getKilledBy().getLevel(), this.getLevel(), 3));
			l.removeEntity(this);
		}
		this.reduceCCDuration(l.getRefreshTime());
		if (this.canMove()) {
			if (Utils.getDistance(this, l.getPlayer()) <= FIRE_RANGE) {
				this.setDX(0).setDY(0);
				if (count >= FIRE_RATE) {
					l.addEntity(new BasicProjectile(this, l.getPlayer().getX(), l.getPlayer().getY(), SPEED,
							(DAMAGE * this.getLevel()), 2000, 0.0, Color.GRAY, 30, 30, FIRE_RANGE, l));
					count = 0;
				} else {
					count += l.getRefreshTime();
				}
			} else if (Utils.getDistance(this, l.getPlayer()) <= PLAYER_TRACK_RANGE) {
				Utils.setDXDY(this, l.getPlayer().getX(), l.getPlayer().getY(), MOVE_SPEED, l);
			}
		} else {
			this.setDX(0).setDY(0);
		}
	}

	@Override
	public Entity killedBy(Entity e) {
		super.killedBy(e);
		if (e instanceof Player) {
			if (Math.random() * 100 < DROP_RATE) {
				if (Math.random() * 100 < ATTACK_PERCENT) {
					((Player) e).addItem(
							new BasicItem(this.getLevel(), ImageLoader.BAILEY_DROP, ATTACK_BASE, ATTACK_GROWTH, 0, 0));
				} else {
					((Player) e).addItem(new BasicItem(this.getLevel(), ImageLoader.BAILEY_DROP, 0, 0, DEFENSE_BASE,
							DEFENSE_GROWTH));
				}
			}
		}
		return this;
	}
}
