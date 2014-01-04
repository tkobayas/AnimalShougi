package jp.gr.java_conf.tkobayas.animalshougi.action;

import jp.gr.java_conf.tkobayas.animalshougi.animal.Animal;

public class CaptureAction implements Action {

	private Animal animal;

	public CaptureAction() {	
	}
	
	public CaptureAction(Animal animal) {
		this.animal = animal;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	
}
