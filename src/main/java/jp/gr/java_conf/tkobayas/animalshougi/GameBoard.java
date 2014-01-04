package jp.gr.java_conf.tkobayas.animalshougi;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.tkobayas.animalshougi.action.MoveAction;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Animal;

public class GameBoard {
	
	private Animal[][] grid;
	
	private List<Animal> player1hand;
	private List<Animal> player2hand;
	
	public GameBoard() {
		grid = new Animal[3][4];
		player1hand = new ArrayList<Animal>();
		player2hand = new ArrayList<Animal>();
	}
	
	public Animal[][] getGrid() {
		return grid;
	}

	public void setGrid(Animal[][] grid) {
		this.grid = grid;
	}

	public List<Animal> getPlayer1hand() {
		return player1hand;
	}

	public void setPlayer1hand(List<Animal> player1hand) {
		this.player1hand = player1hand;
	}

	public List<Animal> getPlayer2hand() {
		return player2hand;
	}

	public void setPlayer2hand(List<Animal> player2hand) {
		this.player2hand = player2hand;
	}

	public void addAnimal(Animal animal) {
		grid[animal.getCol()][animal.getRow()] = animal;
	}
	
	public List<Animal> getAllAnimals() {
		List<Animal> animals = new ArrayList<Animal>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (grid[i][j] != null) {
					animals.add(grid[i][j]);
				}
			}
		}
		animals.addAll(player1hand);
		animals.addAll(player2hand);
		
		return animals;
	}
	
	public Animal getAnimal(int col, int row) {
		return grid[col][row];
	}

	public void update(MoveAction action) {
		Animal animal = action.getAnimal();

		grid[animal.getCol()][animal.getRow()] = null;
		animal.setCol(action.getNewCol());
		animal.setRow(action.getNewRow());
		grid[animal.getCol()][animal.getRow()] = animal;
	}
	
	public boolean containsFriend(Player player, int col, int row) {
		Animal animal = grid[col][row];
		if (animal != null && animal.getPlayer() == player) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean containsFoe(Player player, int col, int row) {
		Animal animal = grid[col][row];
		if (animal != null && animal.getPlayer() != player) {
			return true;
		} else {
			return false;
		}
	}
	
	public void captureAnimal(Animal capturedAnimal, Player toPlayer) {
		grid[capturedAnimal.getCol()][capturedAnimal.getRow()] = null;
		
		capturedAnimal.setCol(-1);
		capturedAnimal.setRow(-1);
		capturedAnimal.setPlayer(toPlayer);
		
		if (toPlayer == Player.PLAYER1) {
			player1hand.add(capturedAnimal);
		} else {
			player2hand.add(capturedAnimal);
		}
	}
}
