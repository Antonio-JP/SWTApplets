package rioko.swt.applet.roFarm.simulation.stacks;

import java.util.Collection;
import java.util.HashMap;

import rioko.swt.applet.roFarm.simulation.ROObject;

public class MultiROStack extends ROStack {
	
	private HashMap<ROObject, Integer> amounts = new HashMap<>();

	@Override
	public Collection<ROObject> getStacked() {
		return this.amounts.keySet();
	}

	@Override
	public int getAmount(ROObject obj) {
		if(this.amounts.containsKey(obj)) {
			return this.amounts.get(obj);
		}
		
		return 0;
	}

	@Override
	protected void changeAmount(ROObject obj, int diff) {
		this.amounts.put(obj, this.getAmount(obj) + diff);
	}

}
