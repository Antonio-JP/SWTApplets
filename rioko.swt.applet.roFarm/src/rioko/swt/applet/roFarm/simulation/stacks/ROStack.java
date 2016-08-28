package rioko.swt.applet.roFarm.simulation.stacks;

import java.util.Collection;

import rioko.swt.applet.roFarm.simulation.ROObject;

public abstract class ROStack {
	public abstract Collection<ROObject> getStacked();
	public abstract int getAmount(ROObject obj);
	protected abstract void changeAmount(ROObject obj, int diff);
	
	public int getTotalPrize() {
		int res = 0;
		for(ROObject obj : this.getStacked()) {
			res += obj.getPrize()*this.getAmount(obj);
		}
		
		return res;
	}
	
	public int getTotalWeight() {
		int res = 0;
		for(ROObject obj : this.getStacked()) {
			res += obj.getWeight()*this.getAmount(obj);
		}
		
		return res;
	}
	
	public void mergeStacks(ROStack stack, int num) {
		for(ROObject obj : stack.getStacked()) {
			this.changeAmount(obj, stack.getAmount(obj)*num);
		}
	}
	
	public void mergeStacks(ROStack stack) {
		this.mergeStacks(stack, 1);
	}
}
