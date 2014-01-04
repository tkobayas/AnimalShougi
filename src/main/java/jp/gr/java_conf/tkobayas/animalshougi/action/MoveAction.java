package jp.gr.java_conf.tkobayas.animalshougi.action;

import jp.gr.java_conf.tkobayas.animalshougi.Direction;
import jp.gr.java_conf.tkobayas.animalshougi.Player;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Animal;

public class MoveAction implements Action {

	private Animal animal;
	
	private int newCol;
	private int newRow;
	
	public MoveAction() {	
	}
	
	public MoveAction(Animal animal, int newCol, int newRow) {
		this.animal = animal;
		this.newCol = newCol;
		this.newRow = newRow;
	}
	
	public Animal getAnimal() {
		return animal;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	public int getNewCol() {
		return newCol;
	}
	public void setNewCol(int newCol) {
		this.newCol = newCol;
	}
	public int getNewRow() {
		return newRow;
	}
	public void setNewRow(int newRow) {
		this.newRow = newRow;
	}
	
	public Direction getDirection() {
		
		int side = (newCol - animal.getCol()) * (animal.getPlayer() == Player.PLAYER1 ? +1 : -1);
		int forword = (animal.getRow() - newRow) * (animal.getPlayer() == Player.PLAYER2 ? +1 : -1);
		
		if (side == 0 && forword == 0) {
			return Direction.NONE;
		}
		if (side == 0 && forword == -1) {
			return Direction.BACKWARD;
		}
		if (side == 0 && forword == 1) {
			return Direction.FORWARD;
		}
		if (side == -1 && forword == 0) {
			return Direction.LEFT;
		}
		if (side == -1 && forword == 1) {
			return Direction.FORWARD_LEFT;
		}
		if (side == -1&& forword == -1) {
			return Direction.BACKWARD_LEFT;
		}
		if (side == 1 && forword == 0) {
			return Direction.RIGHT;
		}
		if (side == 1 && forword == 1) {
			return Direction.FORWARD_RIGHT;
		}
		if (side == 1 && forword == -1) {
			return Direction.BACKWARD_RIGHT;
		}
		
		return Direction.INVALID;
	}
}
