package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import maze.MazeGenerator;
import maze.Vector2;

import static utilz.Constants.GameConsts.UPS_SET;
import static utilz.Constants.MapConsts.*;
import static utilz.Constants.SizeConsts.SCALE;
import static utilz.LoadImage.GetSprite;
import static utilz.HelpMethods.CanMoveHere;

public class Enemy extends Entity {
	
	private enum Direction {
		UP, LEFT, DOWN, RIGHT;
	}
	
	private final MazeGenerator mazeGenerator;
	private final Player target;
	private final Random random;
	private final int[][] map;
	
	private final int roamSpeed = 2;
	private final int chaseSpeed = 3;
	private final int range = 5;
	
	private int prevXidx, prevYidx;
	private float lostSightTimer = 0.0f;
	
	private final BufferedImage aliveImg;
	private final BufferedImage deadImg;
	
	private final Font font;
	private String msg = "";
	private Color msgColor;
	private float confuseDuration = 0.0f;
	
	private boolean isAlive = true;
	private boolean isChasing = false;
	private boolean missonCompletedWithRespectPlusPlus = false;
	private Direction direction;
	
	public Enemy(int width, int height, MazeGenerator mazeGenerator) {
		this(0, 0, width, height, mazeGenerator);
	}

	public Enemy(int x, int y, int width, int height, MazeGenerator mazeGenerator) {
		super(x, y, width, height);
		this.mazeGenerator = mazeGenerator;
		map = mazeGenerator.getMaze();
		target = mazeGenerator.gePlayer();
		random = new Random();
		aliveImg = GetSprite("Enemy");
		deadImg = GetSprite("Enemy_Dead");
		font = new Font("Arial Rounded MT Bold", Font.BOLD, 18);
		setRandomDirection();
	}
	
	public void update() {
		if (isAlive) {
			int dx = 0, dy = 0;
			int speed = isChasing ? chaseSpeed : roamSpeed;
			
			if      (direction == Direction.UP)    dy = -speed;
			else if (direction == Direction.LEFT ) dx = -speed;
			else if (direction == Direction.DOWN ) dy = +speed;
			else if (direction == Direction.RIGHT) dx = +speed;
			
			super.update();
			
			if (CanMoveHere(x+dx, y+dy, width, height, map)) {			
				x += dx;
				y += dy;
			} else {
				turnRandomly();
			}
			super.update();
			
			if (checkLineOfSight()) lostSightTimer = 3.0f;
			else if (isChasing) lostSightTimer -= 1.0f/UPS_SET;
			isChasing = (lostSightTimer > 0) && target.isAlive;
			
			if (onSingleTile() && (xIdx != prevXidx || yIdx != prevYidx)) {
				if (isChasing) {
					setTargetDirection();
				} else {
					turnRandomly();
				}
				prevXidx = xIdx;
				prevYidx = yIdx;
			}
			checkCollision();
			updateMsg();
		}
	}
	
	private void updateMsg() {
		if (missonCompletedWithRespectPlusPlus) {
			msg = ":)";
			msgColor = Color.green;
		} else if (isChasing) {
			msg = "!";
			msgColor = Color.red;
			confuseDuration = 2.0f;
		} else if (confuseDuration > 0.0f && target.isAlive) {
			msg = "?";
			msgColor = Color.yellow;
			confuseDuration -= 1.0f/UPS_SET;
		} else {
			msg = "";
		}
	}
	
	public void render(Graphics g) {
		if (isAlive) {
			g.drawImage(aliveImg, x, y, width, height, null);
			g.setColor(msgColor);
			g.setFont(font);
			g.drawString(msg, x + width/2 - g.getFontMetrics().stringWidth(msg)/2, (int) (y - 5*SCALE));
		} else {
			g.drawImage(deadImg, x, y, width, height, null);
		}
	}
	
	private void turnRandomly() {
		final Vector2 up     = new Vector2(xIdx, yIdx-1);
		final Vector2 left   = new Vector2(xIdx-1, yIdx);
		final Vector2 down   = new Vector2(xIdx, yIdx+1);
		final Vector2 right  = new Vector2(xIdx+1, yIdx);
		
		final Direction[] arr = new Direction[4];
		int options = 0;
		
		if (mazeGenerator.isPath(up   ) && direction != Direction.DOWN ) arr[options++] = Direction.UP;
		if (mazeGenerator.isPath(left ) && direction != Direction.RIGHT) arr[options++] = Direction.LEFT;
		if (mazeGenerator.isPath(down ) && direction != Direction.UP   ) arr[options++] = Direction.DOWN;
		if (mazeGenerator.isPath(right) && direction != Direction.LEFT ) arr[options++] = Direction.RIGHT;
		
		if (options > 0) {			
			// Slight change to make a U turn at any random time
			if (random.nextInt(30) == 0) {
				uTurn();
			} else {
				direction = arr[random.nextInt(options)];
			}
		} else {
			uTurn();
		}
	}
	
	private void uTurn() {
		switch (direction) {
		case UP:
			direction = Direction.DOWN;
			break;
		case LEFT:
			direction = Direction.RIGHT;
			break;
		case DOWN:
			direction = Direction.UP;
			break;
		case RIGHT:
			direction = Direction.LEFT;
			break;				
		}
	}
	
	private boolean checkLineOfSight() {
		final Vector2 myPos = new Vector2(xIdx, yIdx);
		final Vector2 targetPos = new Vector2(target.xIdx, target.yIdx);
		if (myPos.distance(targetPos) > range) {
			return false;
		} else if (xIdx == target.xIdx) {
			for (int j = Math.min(yIdx, target.yIdx)+1; j < Math.max(yIdx, target.yIdx); j++) {
				if (map[xIdx][j] == WALL || map[xIdx][j] == SPIKE) return false;
			}
			return true;
		} else if (yIdx == target.yIdx) {
			for (int i = Math.min(xIdx, target.xIdx)+1; i < Math.max(xIdx, target.xIdx); i++) {
				if (map[i][yIdx] == WALL || map[i][yIdx] == SPIKE) return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	private void setTargetDirection() {
		if (target.isAlive) {
			final ArrayList<Vector2> visited = new ArrayList<Vector2>();
			final ArrayList<Direction> path = new ArrayList<Direction>();
			
			Vector2 origin = new Vector2(xIdx, yIdx);
			Vector2 targetPos = new Vector2(target.xIdx, target.yIdx);
			
			if (origin.equals(targetPos)) return;
			
			visited.add(origin);
			isTarget(origin, targetPos, visited, path);
			if (path.isEmpty()) {
				isChasing = false;
				lostSightTimer = 0.0f;
				turnRandomly();
			} else {
				direction = path.get(path.size()-1);
			}
		}
	}
	
	private boolean isTarget(Vector2 origin, Vector2 targetPos, ArrayList<Vector2> visited, ArrayList<Direction> path) {
		final Vector2 up     = new Vector2(origin.x, origin.y-1);
		final Vector2 left   = new Vector2(origin.x-1, origin.y);
		final Vector2 down   = new Vector2(origin.x, origin.y+1);
		final Vector2 right  = new Vector2(origin.x+1, origin.y);
		
		if (origin.equals(targetPos)) return true;
		
		if (!visited.contains(up) && mazeGenerator.isPath(up)) {
			visited.add(up);
			if (isTarget(up, targetPos, visited, path)) {
				path.add(Direction.UP);
				return true;
			}
		}
		if (!visited.contains(left) && mazeGenerator.isPath(left)) {
			visited.add(left);
			if (isTarget(left, targetPos, visited, path)) {
				path.add(Direction.LEFT);
				return true;
			}
		}
		if (!visited.contains(down) && mazeGenerator.isPath(down)) {
			visited.add(down);
			if (isTarget(down, targetPos, visited, path)) {
				path.add(Direction.DOWN);
				return true;
			}
		}
		if (!visited.contains(right) && mazeGenerator.isPath(right)) {
			visited.add(right);
			if (isTarget(right, targetPos, visited, path)) {
				path.add(Direction.RIGHT);
				return true;
			}
		}
		
		return false;
	}
	
	private void setRandomDirection() {
		final Direction[] arr = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
		direction = arr[random.nextInt(4)];
	}
	
	private void checkCollision() {
		Rectangle hitbox = new Rectangle(x, y, width, height);
		Rectangle targetHitbox = new Rectangle(target.x, target.y, target.width, target.height);
		if (hitbox.intersects(targetHitbox)) {
			target.isAlive = false;
			missonCompletedWithRespectPlusPlus = true;
		}
	}

}
