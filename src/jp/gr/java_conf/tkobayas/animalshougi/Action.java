package jp.gr.java_conf.tkobayas.animalshougi;

import jp.gr.java_conf.tkobayas.animalshougi.animal.Animal;

public class Action {

	private Animal animal;
	
	private int newCol;
	private int newRow;
	
	public Action() {	
	}
	
	public Action(Animal animal, int newCol, int newRow) {
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
	
}
