package RPG;

import Entity.Entity;
import Entity.Projectile;

public class Utils {
	public static double calculateDamage(Projectile p, Entity e) {
		return (p.getDamage() * p.getPenetration() / 100)
				+ (p.getDamage() * ((100 - p.getPenetration()) / 100)) * ((100 - e.getDefense()) / 100) > 0
						? (p.getDamage() * p.getPenetration() / 100)
								+ (p.getDamage() * ((100 - p.getPenetration()) / 100)) * ((100 - e.getDefense()) / 100)
						: 0;
	}

	public static double calculateXP(int playerLevel, int enemyLevel, double xp) {
		return xp - ((playerLevel - enemyLevel) * xp / 10) > 0 ? xp - ((playerLevel - enemyLevel) * xp / 10) : 0;
	}

	public static boolean isEnemy(Entity e1, Entity e2) {
		return e1.getSide() == Entity.ENEMY && e2.getSide() == Entity.PLAYER
				|| e1.getSide() == Entity.PLAYER && e2.getSide() == Entity.ENEMY;
	}

	public static double getDistance(Entity e1, Entity e2) {
		return Math.sqrt(Math.pow(e1.getX() - e2.getX(), 2) + Math.pow(e1.getY() - e2.getY(), 2));
	}

	public static double getDistance(Entity e1, double targetX, double targetY) {
		return Math.sqrt(Math.pow(targetX - e1.getX(), 2) + Math.pow(targetY - e1.getY(), 2));
	}

	public static void move(Entity e, double targetX, double targetY, double distance) {
		if (distance > 0) {
			e.setX(e.getX() + ((targetX - e.getX()) * (distance / Utils.getDistance(e, targetX, targetY))))
					.setY(e.getY() + ((targetY - e.getY()) * (distance / Utils.getDistance(e, targetX, targetY))));
		}
	}

	public static void setDXDY(Entity e1, double targetX, double targetY, double speed, Level l) {
		e1.setDX(targetX == e1.getX() ? 0
				: 20 / l.getRefreshTime() * speed * (targetX - e1.getX()) / Utils.getDistance(e1, targetX, targetY))
				.setDY(targetY == e1.getY() ? 0
						: 20 / l.getRefreshTime() * speed * (targetY - e1.getY())
								/ Utils.getDistance(e1, targetX, targetY));
	}

	public static double calcItemValue(int level, double baseMax, double growthMax) {
		return Math.random() * level * growthMax + Math.random() * baseMax;
	}
}
