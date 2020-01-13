package gameClient;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.*;
import utils.Point3D;

public class GameArena {

	private game_service game;
	private graph game_graph;
	private ArrayList<Fruit> fruits;
	private ArrayList<Robot> robots;
		
	
	private double maxX,minX,maxY,minY;

	public static final Fruit_Comparator _Comp = new Fruit_Comparator();

	public GameArena(int num) {
		game = Game_Server.getServer(num);
		game_graph = new DGraph(game.getGraph());
		setScaleParameters();
		fruits = new ArrayList<Fruit>();
		robots = new ArrayList<Robot>();
		initFruits();
		setRobotsPositions();
		initRobots();
	}
	
	private void initFruits() {
		fruits.clear();
		for (String fruit_str : game.getFruits()) {
			Fruit fruit = new Fruit(fruit_str);
			setEdgeToFruits(fruit);
			fruits.add(fruit);
		}	
		fruits.sort(_Comp);
	}
	
	private void initRobots() {
		robots.clear();
		for (String robot_str : game.getRobots()) {
			Robot robot = new Robot(robot_str);
			robots.add(robot);
		}	
	}
	
	
	private void setEdgeToFruits(Fruit fruit) {
		for (node_data node : this.game_graph.getV()) {
			for (edge_data edge : this.game_graph.getE(node.getKey())) {
				node_data dst = this.game_graph.getNode(edge.getDest());
				double d1 = node.getLocation().distance3D(fruit.getLocation());
				double d2 = fruit.getLocation().distance3D(dst.getLocation());
				double dist = node.getLocation().distance3D(dst.getLocation());
				double tmp = dist - (d1 + d2);
				int t;
				if (node.getKey() > dst.getKey()) {
					t = 1;
				} else {
					t = -1;
				}

				if ((Math.abs(tmp) <= Point3D.EPS2) && (fruit.getType() == t)) {
					fruit.setEdge(edge);
					return;
				}
			}
		}
	}
	
	private int robotsNum() {
		String game_str = game.toString();
		int robots_num = 0;
		try {
			JSONObject json_game = new JSONObject(game_str).getJSONObject("GameServer");
			robots_num = json_game.getInt("robots");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return robots_num;
	}
	
	private void setRobotsPositions() {
		fruits.sort(_Comp);
		int robots_num = robotsNum();
		
		for(int i = 0 ; i<robots_num;i++) {
			int src_node = fruits.get(i).getEdge().getSrc();
			game.addRobot(src_node);
		}	
	}
	
	public void update() {
		initFruits();
		initRobots();
	}
	
	public game_service getGame() {
		return game;
	}
	
	public ArrayList<Fruit> getFruits() {
		return fruits;
	}
	
	public ArrayList<Robot> getRobots() {
		return robots;
	}
	
	public graph getGraph() {
		return game_graph;
	}
	
	private void setScaleParameters() {
		maxX = Double.MIN_VALUE;
		minX = Double.MAX_VALUE;
		maxY = Double.MIN_VALUE;
		minY = Double.MAX_VALUE;

		for (node_data node : game_graph.getV()) {
			maxX = Math.max(maxX, node.getLocation().x());
			maxY = Math.max(maxY, node.getLocation().y());
			minX = Math.min(minX, node.getLocation().x());
			minY = Math.min(minY, node.getLocation().y());
		}
	}
	
	public double maxX() {
		return maxX;
	}
	
	public double minX() {
		return minX;
	}
	
	public double maxY() {
		return maxY;
	}
	
	public double minY() {
		return minY;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
