package rioko.swt.applet.roFarm.simulation.stacks;

import java.util.ArrayList;

import rioko.swt.applet.roFarm.simulation.ROObject;

public class UniROStack extends ROStack {
	private ROObject stacked;
	private int amount = 0;
	
	public UniROStack(String name, int amount) {
		this.stacked = ROObject.getObject(name);
		this.amount = amount;
	}
	
	public void changeStack(int diff) {
		this.changeAmount(stacked, diff);
	}
	
	public int getAmount() {
		return this.getAmount(stacked);
	}
	
	@Override
	public ArrayList<ROObject> getStacked() {
		ArrayList<ROObject> res = new ArrayList<>(); res.add(stacked);
		return res;
	}

	@Override
	public int getAmount(ROObject obj) {
		if(isObject(obj)) {
			return amount;
		}
		return 0;
	}

	@Override
	protected void changeAmount(ROObject obj, int diff) {
		if(isObject(obj)) {
			this.amount += diff;
		}
	}
	
	private boolean isObject(ROObject obj) {
		return obj.getName().equals(stacked.getName());
	}
}
