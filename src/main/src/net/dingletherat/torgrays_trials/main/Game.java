package net.dingletherat.torgrays_trials.main;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.Duration;

public class Game extends JPanel {
	public Thread gameThread;
	public int width = 800;
	public int height = 600;
	public double FPS = 0;
	
	public void setup() {
		gameThread = new Thread(this::gameLoop);
		setPreferredSize(new Dimension(width, height));
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
			System.out.println(FPS);
			
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
		g.fillRect(0, 0, width, height);
	}
	
	private void update() {
	}
}
