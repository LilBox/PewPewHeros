package RPG;

import java.util.ArrayList;

import Enemy.BasicEnemy;
import Enemy.BossBailey;
import Entity.Entity;

public class Spawner {

	Level l;

	public static final int BASIC = 0, BAILEY = 1;

	ArrayList<Spawn> spawns = new ArrayList<Spawn>();

	public Spawner(Level l) {
		this.l = l;
	}

	public void registerSpawn(int entity, int num, int count, int xDis, int yDis, int start) {
		spawns.add(new Spawn(entity, num, count, xDis, yDis, l, start));
	}

	public void update(int time) {
		for (Spawn s : spawns) {
			s.update(l.getRefreshTime());
		}
	}

	class Spawn {

		int entity, sTime, num, xDis, yDis, count = 0;

		public Spawn(int entity, int num, int count, int xDis, int yDis, Level l, int start) {
			this.entity = entity;
			this.num = num;
			this.sTime = count;
			this.count = start;
			this.xDis = xDis;
			this.yDis = yDis;
		}

		public void update(int time) {
			count += time;
			if (count >= sTime) {
				count = 0;
				for (int i = 0; i < num; i++) {
					l.getPlayer();
					Spawner.getEntity(entity, l.getPlayer().getLevel(),
							(int) (l.getPlayer().getX() + Math.random() * xDis * 2 - xDis),
							(int) (l.getPlayer().getY() + Math.random() * yDis * 2 - yDis));
					l.addEntity(Spawner.getEntity(entity, l.getPlayer().getLevel(),
							(int) (l.getPlayer().getX() + Math.random() * xDis * 2 - xDis),
							(int) (l.getPlayer().getY() + Math.random() * yDis * 2 - yDis)));
				}
			}
		}
	}

	public static Entity getEntity(int num, int level, int x, int y) {
		switch (num) {
		case BASIC:
			return new BasicEnemy(level, x, y);
		case BAILEY:
			return new BossBailey(level, x, y);
		}
		return null;
	}
}
