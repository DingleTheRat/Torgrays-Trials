package net.dingletherat.torgrays_trials.main;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.Duration;

public class Game extends JPanel {
	// Screen settings
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale; // 48x48 tile
	public final int maxScreenCol = 20;
	public final int maxScreenRow = 12;
	public int screenWidth = tileSize * maxScreenCol; // 960 pixels
	public int screenHeight = tileSize * maxScreenRow; // 576 pixels
	
	public Thread gameThread;
	public double FPS = 0;
	
	// Classes
	public UI ui;
	
	public ST.GameStates gameState = ST.GameStates.TITLE;
	public ST.UIStates uiState = ST.UIStates.NONE;
	
	public void setup() {
        Main.LOGGER.info("Setting up game");
        gameThread = new Thread(this::gameLoop);
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		
		ui = new UI(this);
	}
	
	public void gameLoop() {
		final int TARGET_FPS = 60;
		final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;
		
		while (Main.running) {
			long start = System.nanoTime();
			
			update();
			repaint();
			
			long elapsed = System.nanoTime() - start;
			long sleepTime = OPTIMAL_TIME - elapsed;
			
			if (sleepTime > 0) {
				try {
					Thread.sleep(Duration.ofNanos(sleepTime - 750_000));
					elapsed = System.nanoTime() - start;
					DecimalFormat df = new DecimalFormat("0.00");
					FPS = Double.parseDouble(df.format(1_000_000_000.0 / elapsed));
				} catch (InterruptedException e) {
					Main.handleException(e);
				}
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);
		
		switch (gameState) {
			case TITLE -> ui.draw(g);
		}
	}
	
	private void update() {
	}
}
