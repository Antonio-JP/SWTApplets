package rioko.swt.applet.roFarm.simulation;

import rioko.swt.applet.roFarm.simulation.stacks.MultiROStack;
import rioko.swt.applet.roFarm.simulation.stacks.ROStack;
import rioko.swt.applet.roFarm.simulation.stacks.UniROStack;

public class ROFarm {
	private int childs = 0, teens = 0, adults = 0;
	
	private ROStack childProd, teensProd, adultsProd;
	private UniROStack extraProd;
	private boolean childEat = true, teensEat = true, adultsEat = true;
	private boolean childExtra = true, teensExtra = true, adultsExtra = true;
	
	private int food;
	private MultiROStack production;
	
	//Builder
	public ROFarm(UniROStack extraProd, int initChilds, int food, 
			UniROStack child, UniROStack teens, UniROStack adults, 
			boolean childEat, boolean teensEat, boolean adultsEat,
			boolean childExtra, boolean teensExtra, boolean adultsExtra) {
		this.extraProd = extraProd;
		this.production = new MultiROStack();
		this.childs = initChilds;
		this.food = food;
		
		this.childProd = child;
		this.teensProd = teens;
		this.adultsProd = adults;

		this.childEat = childEat;
		this.teensEat = teensEat;
		this.adultsEat = adultsEat;

		this.childExtra = childExtra;
		this.teensExtra = teensExtra;
		this.adultsExtra = adultsExtra;
	}
	
	//Static Farms of KoeRO
	public static ROFarm getPigFarm(int initChilds, int food) {
		return new ROFarm(new UniROStack("Abono", 1), initChilds, food,
				new UniROStack("Carne", 1), new UniROStack("Carne", 10), new UniROStack("Carne de lujo", 1),
				true, true, true, 
				true, true, true);
	}
	
	public static ROFarm getPecoFarm(int initChilds, int food) {
		return new ROFarm(new UniROStack("Huevo", 1), initChilds, food,
				new UniROStack("Huevo", 1), new UniROStack("Carne", 1), new UniROStack("Carne de gran calidad", 5),
				false, true, true, 
				false, false, true);
	}
	
	public static ROFarm getGoatFarm(int initChilds, int food) {
		return new ROFarm(new UniROStack("Lana", 1), initChilds, food,
				new UniROStack("Leche", 1), new UniROStack("Carne", 0), new UniROStack("Carne de gran calidad", 0),
				true, true, true, 
				false, false, true);
	}
	
	//Evolution method
	public boolean evolve() {
		//Checking food
		int foodNeeded = this.getNextFoodConsume();		
		
		if(this.childs == 0 && this.teens == 0 && this.adults == 0) {
			return false;
		}
		
		if(foodNeeded > this.food) {
			this.childs = 0; this.teens = 0; this.adults = 0;
			return false;
		}
		
		this.food -= foodNeeded;
		
		//Updating production
		if(this.childExtra) { this.production.mergeStacks(this.extraProd, this.childs); }
		if(this.teensExtra) { this.production.mergeStacks(this.extraProd, this.teens); }
		if(this.adultsExtra) { this.production.mergeStacks(this.extraProd, this.adults); }
		
		//Updating population
		int aux = this.adults/2;
		this.adults += this.teens;
		this.teens = this.childs;
		this.childs = aux;
		
		return true;
	}
	
	//Killing methods
	public int killAdults(int num) {
		int res = (num > this.adults)? this.adults : num;
		this.production.mergeStacks(this.adultsProd, res);
		this.adults -= res;
		
		return res;
	}
	
	public int killTeens(int num) {
		int res = (num > this.teens)? this.teens : num;
		this.production.mergeStacks(this.teensProd, res);
		this.teens -= res;
		
		return res;
	}
	
	public int killChildren(int num) {
		int res = (num > this.childs)? this.childs : num;
		this.production.mergeStacks(this.childProd, res);
		this.childs -= res;
		
		return res;
	}
	
	public int killAdults() {
		return this.killAdults(this.adults);
	}
	
	public int killTeens() {
		return this.killTeens(this.adults);
	}
	
	public int killChildren() {
		return this.killChildren(this.adults);
	}
	
	//Info methods
	public int getFood() {
		return this.food;
	}
	
	public int getNextFoodConsume() {
		int foodNeeded = 0;
		if(this.childEat) { foodNeeded += childs; }
		if(this.teensEat) { foodNeeded += teens; }
		if(this.adultsEat) { foodNeeded += adults; }
		
		return foodNeeded;
	}
	
	public int getPotentialAdultWeight() {
		return this.adultsProd.getTotalWeight()*this.adults;
	}
	
	public int getPotentialTeensWeight() {
		return this.teensProd.getTotalWeight()*this.teens;
	}
	
	public int getPotentialChildrenWeight() {
		return this.childProd.getTotalWeight()*this.childs;
	}
	
	public int getAdults() {
		return this.adults;
	}
	
	public int getTeens() {
		return this.teens;
	}
	
	public int getChildren() {
		return this.childs;
	}

	public ROStack getChildProd() {
		return childProd;
	}

	public ROStack getTeensProd() {
		return teensProd;
	}

	public ROStack getAdultsProd() {
		return adultsProd;
	}

	public UniROStack getExtraProd() {
		return extraProd;
	}

	public boolean isChildEat() {
		return childEat;
	}

	public boolean isTeensEat() {
		return teensEat;
	}

	public boolean isAdultsEat() {
		return adultsEat;
	}

	public boolean isChildExtra() {
		return childExtra;
	}

	public boolean isTeensExtra() {
		return teensExtra;
	}

	public boolean isAdultsExtra() {
		return adultsExtra;
	}

	public MultiROStack getProduction() {
		return production;
	}

	public int getPopulation() {
		return this.getAdults() + this.getTeens() + this.getPopulation();
	}
}
