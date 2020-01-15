package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dataStructure.*;
import utils.Point3D;
import utils.Range;

public class MyGameGUI extends JFrame implements MouseListener {

	private static int WIDTH = 1200;
	private static int HEIGHT = 800;
	private static double arena_maxX, arena_maxY, arena_minX, arena_minY;

	private GameArena arena;
	private boolean auto_game;

	public MyGameGUI(GameArena arena, boolean b) {
		this.arena = arena;
		this.auto_game = b;
		arena_maxX = arena.maxX();
		arena_maxY = arena.maxY();
		arena_minX = arena.minX();
		arena_minY = arena.minY();
		initGUI();
	}

	private void initGUI() {
		this.setTitle("Graph GUI");
		this.setBounds(200, 0, WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
		if (!auto_game) {
			this.addMouseListener(this);
		}
	}

	private void setTime(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("Time: " + arena.getGame().timeToEnd() / 1000, 50, 70);
	}

	public void paint(Graphics g) {
		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		g2d.setBackground(new Color(240, 240, 240));
		g2d.clearRect(0, 0, WIDTH, HEIGHT);

		paintGraph(g2d);

		setTime(g2d);
		paintFruits(g2d);
		paintRobots(g2d);

		Graphics2D g2dComponent = (Graphics2D) g;
		g2dComponent.drawImage(bufferedImage, null, 0, 0);

	}

	private void paintGraph(Graphics2D g) {
		WIDTH = getWidth();
		HEIGHT = getHeight();
		double minX = arena_minX;
		double minY = arena_minY;
		double maxX = arena_maxX;
		double maxY = arena_maxY;
		for (node_data node : arena.getGraph().getV()) {

//			int node_x = (int) scale(node.getLocation().x(), minX, maxX, 50, WIDTH - 50);
//			int node_y = (int) scale(node.getLocation().y(), minY, maxY, 200, HEIGHT - 200);
//
//			g.setColor(Color.BLUE);
//			g.fillOval(node_x - 5, node_y - 5, 10, 10);

			if (arena.getGraph().getE(node.getKey()) != null) {
				for (edge_data edge : arena.getGraph().getE(node.getKey())) {

					node_data src = arena.getGraph().getNode(edge.getSrc());
					node_data dest = arena.getGraph().getNode(edge.getDest());

					int src_x = (int) scale(src.getLocation().x(), minX, maxX, 50, WIDTH - 50);
					int src_y = (int) scale(src.getLocation().y(), minY, maxY, 200, HEIGHT - 200);
					int dest_x = (int) scale(dest.getLocation().x(), minX, maxX, 50, WIDTH - 50);
					int dest_y = (int) scale(dest.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

					g.setColor(Color.RED);
					g.drawLine(src_x, src_y, dest_x, dest_y);

					g.setColor(Color.YELLOW);
					int dir_x = (((((((src_x + dest_x) / 2) + dest_x) / 2) + dest_x) / 2));
					int dir_y = (((((((src_y + dest_y) / 2) + dest_y) / 2) + dest_y) / 2));

					g.drawOval(dir_x - 5, dir_y - 5, 10, 10);

				}
			}
//			g.setColor(Color.BLACK);
//			g.setFont(new Font("Arial", Font.BOLD, 15));
//			g.drawString(node.getKey() + "", node_x + 5, node_y + 5);

		}
		for (node_data node : arena.getGraph().getV()) {
			int node_x = (int) scale(node.getLocation().x(), minX, maxX, 50, WIDTH - 50);
			int node_y = (int) scale(node.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

			g.setColor(Color.BLUE);
			g.fillOval(node_x - 5, node_y - 5, 10, 10);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(node.getKey() + "", node_x + 5, node_y + 5);
		}
	}

	private void paintFruits(Graphics2D g) {
		synchronized (arena.getFruits()) {
			WIDTH = getWidth();
			HEIGHT = getHeight();
			double minX = arena_minX;
			double minY = arena_minY;
			double maxX = arena_maxX;
			double maxY = arena_maxY;
//			Iterator<Fruit> it = game.getFruits().iterator();
//			while (it.hasNext()) {
//				Fruit fruit = it.next();
			for (Fruit fruit : arena.getFruits()) {
				g.setColor(Color.ORANGE);
				if (fruit.getType() == 1) {
					g.setColor(Color.GREEN);
				}
				int fruit_x = (int) scale(fruit.getLocation().x(), minX, maxX, 50, WIDTH - 50);
				int fruit_y = (int) scale(fruit.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

				g.fillOval(fruit_x - 7, fruit_y - 7, 15, 15);
				g.setColor(Color.BLACK);
				g.drawString(fruit.getValue() + "", fruit_x + 10, fruit_y + 10);
				// }
			}
		}
	}

	private void paintRobots(Graphics2D g) {
		WIDTH = getWidth();
		HEIGHT = getHeight();
		double minX = arena_minX;
		double minY = arena_minY;
		double maxX = arena_maxX;
		double maxY = arena_maxY;

		List<String> rob = arena.getGame().getRobots();
		for (int i = 0; i < rob.size(); i++) {
			g.drawString(rob.get(i), WIDTH / 5, 70 + (20 * i));
		}
//		synchronized (arena.getRobots()) {

//			Iterator<Robot> it = game.getRobots().iterator();
//			while (it.hasNext()) {
//				Robot robot = it.next();
//			for (Robot robot : arena.getRobots()) {

		for (int i = 0; i < arena.numOfRobots(); i++) {
			Robot robot = arena.getRobots().get(i);

			int robot_x = (int) scale(robot.getLocation().x(), minX, maxX, 50, WIDTH - 50);
			int robot_y = (int) scale(robot.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

			g.setColor(Color.GRAY);
			g.drawOval(robot_x - 15, robot_y - 15, 30, 30);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(robot.getID() + "", robot_x - 5, robot_y + 5);
			// }
//			}
		}
	}

	/**
	 * 
	 * @param data  denote some data to be scaled
	 * @param r_min the minimum of the range of your data
	 * @param r_max the maximum of the range of your data
	 * @param t_min the minimum of the range of your desired target scaling
	 * @param t_max the maximum of the range of your desired target scaling
	 * @return
	 */
	private double scale(double data, double r_min, double r_max, double t_min, double t_max) {

		double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	private double scaleBack(double scaled_data, double r_min, double r_max, double t_min, double t_max) {
		double res = ((scaled_data - t_min) / (t_max - t_min)) * (r_max - r_min) + r_min;
		return res;
	}
	//

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!arena.getGame().isRunning())
			return;
		WIDTH = getWidth();
		HEIGHT = getHeight();
		double minX = arena_minX;
		double minY = arena_minY;
		double maxX = arena_maxX;
		double maxY = arena_maxY;

		int x = e.getX(), y = e.getY();
		double original_x = scaleBack(x, minX, maxX, 50, WIDTH - 50);
		double original_y = scaleBack(y, minY, maxY, 200, HEIGHT - 200);

		int rid = arena.getRobotFromCoordinates(original_x, original_y);

		if (rid != -1) {
			String dest_str = JOptionPane.showInputDialog("Enter next node for robot: " + rid);
			int dest = -1;
			try {
				dest = Integer.parseInt(dest_str);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			arena.getGame().chooseNextEdge(rid, dest);
		}

//		ArrayList<Robot> tmp = (ArrayList<Robot>) arena.getRobots().clone();
//
//		synchronized (tmp) {
//
//			// Iterator<Robot> it = game.getRobots().iterator();
//			// while (it.hasNext()) {
//			// Robot robot = it.next();
//			for (Robot robot : tmp) {
//				if (robot.getDest() == -1) {
//					String dest_str = JOptionPane.showInputDialog("Enter next node for robot: " + robot.getID());
//					int dest = -1;
//					try {
//						dest = Integer.parseInt(dest_str);
//					} catch (Exception e1) {
//						JOptionPane.showMessageDialog(this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//					}
//					arena.getGame().chooseNextEdge(robot.getID(), dest);
//				}
//			}
//			// }
//		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
