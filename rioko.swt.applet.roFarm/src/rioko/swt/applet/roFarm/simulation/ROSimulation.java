package rioko.swt.applet.roFarm.simulation;

import java.util.ArrayList;

public class ROSimulation {
	private boolean runned = false;
	
	private ROFarm farm;
	private ArrayList<String> actions = new ArrayList<>();
	
	private int maxWeight;
	private boolean hasPoring;
	private boolean hasCart;
	private int poringObjects = 1;
	private int poringWeight = 0;
	
	public ROSimulation(ROFarm farm, int maxHeight) {
		this(farm, maxHeight, true, false);
	}
	
	public ROSimulation(ROFarm farm, int maxHeight, boolean hasPoring, boolean hasCart) {
		this.farm = farm;
		this.maxWeight = maxHeight;
		this.hasPoring = hasPoring;
		this.hasCart = hasCart;
	}
	
	public void run() {
		if(!this.runned) {
			this.runned = true;
			int turn = 0;
			do {
				//Checking if the farm is finishing
				if(farm.getFood() <= farm.getNextFoodConsume()) {
					actions.add("Round " + turn + ": Killing adults (" + farm.killAdults() + ")");
					actions.add("\t(Farm is finishing)");
				}
				
				//Checking if we are full
				if(((!hasPoring) || (poringObjects >= 10)) && 
						((farm.getProduction().getTotalWeight() + farm.getPotentialAdultWeight() - poringWeight) > maxWeight + ((hasCart)? 8000 : 0))) {
					actions.add("Round "+ turn + ": You idiot! You are going to lose things.");
					actions.add("\tKilling as adults as possible");
					int nAdults = (maxWeight + ((hasCart) ? 8000 : 0) + poringWeight - farm.getProduction().getTotalWeight())/farm.getAdultsProd().getTotalWeight();
					farm.killAdults(nAdults);
					break;
				}
				
				//Checking if we exceed our weight
				if(farm.getAdultsProd().getTotalWeight()*(farm.getAdults() + farm.getTeens()) > this.maxWeight) {
					actions.add("Round " + turn + ": Killing adults(" + farm.killAdults() + ")");
					actions.add("\t(Next is too much for weight)");
					
					//Adding to Poring (if possible)
					if(hasPoring && poringObjects < 10) {
						poringWeight = farm.getProduction().getTotalWeight();
						poringObjects++;
						actions.add("\tAdding objects to Poring. Total in Poring = " + poringObjects);
					}
				}
				
				turn ++;
			} while(farm.evolve());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		
		this.run();
		
		res.append("********************************\n");
		res.append("*** FINAL REPORT ************\n");
		res.append("********************************\n");
		res.append("** Final production:\n");
		for(ROObject obj : farm.getProduction().getStacked()) {
			res.append("** - "+obj.getName()+": "+farm.getProduction().getAmount(obj)+"\n");
		}
		res.append("****\n");

		res.append("** Peso total: " + farm.getProduction().getTotalWeight() + "\n"); 
		res.append("** Dinero total (Bruto): " + farm.getProduction().getTotalPrize() + "\n");
		res.append("** Dinero total (Neto): " + farm.getProduction().getTotalPrize()*0.6*((hasCart)?1.24:1) + "\n");
		if(hasPoring) {
			res.append("** Objetos en Poring: " + poringObjects + "\n"); 
			res.append("** Peso en Poring: "+poringWeight+"\n");
		}
		res.append("********************************\n");
		res.append("** Summary of the proccess:\n");
		res.append("************** \n");
		for(String action : actions) {
			res.append("** "+action+"\n");
		}
		res.append("********************************\n");
		
		
		return res.toString();
	}
}
