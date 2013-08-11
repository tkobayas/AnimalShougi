package jp.gr.java_conf.tkobayas.animalshougi.animal;

import javafx.scene.image.Image;

public class Animal {
	
	private int player;

	private int x;
	private int y;
	
	public Animal(int player, int x, int y) {
		this.player = player;
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	
	public Image getImage() {
		String name = this.getClass().getSimpleName();
		//System.out.println(name);
		String path = getClass().getResource("/img/" + name + ".png").toString();
		//System.out.println(path);
		return new Image(path);
	}
}