package main;

import java.awt.Graphics;
import java.awt.event.WindowEvent;

import entities.Player;
import maze.MazeGenerator;

import static utilz.Constants.GameConsts.*;
import static utilz.Constants.SizeConsts.*;

public class Game implements Runnable {
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	
	private Player player;
	private MazeGenerator mazeGenerator;
	
	public Game() {
		initClasses();
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(this, gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
		
		startGameLoop();
	}

	private void initClasses() {
		player = new Player(TILE_SIZE-4, TILE_SIZE-4);
		mazeGenerator = new MazeGenerator(player);
		player.setMap(mazeGenerator.getMaze());
	}
	
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	private void update() {
		player.update();
	}
	
	public void render(Graphics g) {
		mazeGenerator.render(g);
		player.render(g);
	}
	
	public void windowFocusLost() {
		player.stop();
	}
	
	public void closeWindow() {
		gameWindow.dispatchEvent(new WindowEvent(gameWindow, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void run() {
		
		double timePerFrame  = 1_000_000_000 / FPS_SET;
		double timePerUpdate = 1_000_000_000 / UPS_SET;

		long previousTime = System.nanoTime();
		long currentTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;
		
		while (true) {
			currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;

			}
		}
	}
	
	public Player getPlayer() {
		return player;
	}

}
