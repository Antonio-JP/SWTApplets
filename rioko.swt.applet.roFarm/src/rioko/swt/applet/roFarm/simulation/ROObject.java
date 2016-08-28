package rioko.swt.applet.roFarm.simulation;

import java.util.HashMap;

public class ROObject {
	
	private static HashMap<String,ROObject> INSTANCES = null;
	
	private String name = "";
	private int weight = 0;
	private int prize = 0;
	
	//Static methods
	public static ROObject getObject(String name) {
		if(INSTANCES == null) {
			buildInstances();
		}
		return INSTANCES.get(name);
	}
	
	private static void buildInstances() {
		INSTANCES = new HashMap<>();
		new ROObject("Carne de gran calidad",5,500);
		new ROObject("Carne",3,100);
		new ROObject("Carne de lujo",10,750);
		new ROObject("Huevo",3,50);
		new ROObject("Abono",5,15);
		new ROObject("Lana",1,756);
	}
	
	//Builders
	private ROObject(String name, int weight, int prize) {
		this.name = name;
		this.weight = weight;
		this.prize = prize;
		
		INSTANCES.put(this.getName(), this);
	}
	
	//Getters
	public String getName() {
		return this.name;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getPrize() {
		return prize;
	}
	
}
