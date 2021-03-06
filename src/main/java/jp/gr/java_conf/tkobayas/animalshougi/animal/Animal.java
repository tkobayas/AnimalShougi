package jp.gr.java_conf.tkobayas.animalshougi.animal;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jp.gr.java_conf.tkobayas.animalshougi.Player;

public class Animal {
	
	private Player player;

	private int col;
	private int row;
	
	private ImageView view;
	
	public Animal(Player player, int col, int row) {
		this.player = player;
		this.col = col;
		this.row = row;
	}
	
	public ImageView getView() {
		return view;
	}

	public void setView(ImageView view) {
		this.view = view;
	}

	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}

	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
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
