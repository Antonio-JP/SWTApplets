package rioko.swt.applet.roFarm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rioko.swt.applet.roFarm.simulation.ROFarm;
import rioko.swt.applet.roFarm.simulation.ROSimulation;

public class OptionsComposite extends Composite {
	
	private Text maxWeight;
	private Text initChild;
	private Text initFood;
	private Button[] farm = new Button[3];
	private Button cart;
	private Button poring;

	public OptionsComposite(Composite arg0, int arg1) {
		super(arg0, arg1);
		
		GridLayout layout = new GridLayout(2,true);
		this.setLayout(layout);
		
		//First row: text with maximum weight and the types of farming
		//Weight selector
		Composite weight = new Composite(this, SWT.BORDER);
		GridData weight_ld = new GridData(GridData.FILL_HORIZONTAL);
		weight.setLayoutData(weight_ld);
		
		GridLayout weight_l = new GridLayout(2, false);
		weight.setLayout(weight_l);
		Label weight_label = new Label(weight, SWT.NONE);
		weight_label.setText("Max. Weight: ");
		
		maxWeight = new Text(weight, SWT.BORDER);
		GridData maxWeight_ld = new GridData(GridData.FILL_HORIZONTAL);
		maxWeight.setLayoutData(maxWeight_ld);
		
		//Type of farm
		Composite farms = new Composite(this, SWT.BORDER);
		GridData farms_ld = new GridData(GridData.FILL_HORIZONTAL);
		farms.setLayoutData(farms_ld);
		
		GridLayout farms_l = new GridLayout(2, false);
		farms.setLayout(farms_l);
		Label farms_label = new Label(farms, SWT.NONE);
		farms_label.setText("Type: ");
		
		Composite farms_types = new Composite(farms, SWT.NONE);
		farms_types.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		farms_types.setLayout(new GridLayout(3,true));
		
		farm[0] = new Button(farms_types, SWT.RADIO);
		farm[0].setText("Pecos");
		farm[0].setSelection(true);
		farm[1] = new Button(farms_types, SWT.RADIO);
		farm[1].setText("Pigs");
		farm[2] = new Button(farms_types, SWT.RADIO);
		farm[2].setText("Goats");

		farm[0].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		farm[1].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		farm[2].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//Second row: One text to put the numer of init children and two check boxes with boolean values
		//Initial data selector
		Composite farmInit = new Composite(this, SWT.BORDER);
		GridData farmInit_ld = new GridData(GridData.FILL_HORIZONTAL);
		farmInit.setLayoutData(farmInit_ld);
		GridLayout farmInit_l = new GridLayout(2, true);
		farmInit.setLayout(farmInit_l);
		
		Composite initCh = new Composite(farmInit, SWT.NONE);
		GridData initCh_ld = new GridData(GridData.FILL_HORIZONTAL);
		initCh.setLayoutData(initCh_ld);
		
		GridLayout initCh_l = new GridLayout(2, false);
		initCh.setLayout(initCh_l);
		Label init_label = new Label(initCh, SWT.NONE);
		init_label.setText("Init. Children: ");
		
		initChild = new Text(initCh, SWT.BORDER);
		GridData initChild_ld = new GridData(GridData.FILL_HORIZONTAL);
		initChild.setLayoutData(initChild_ld);
		
		Composite initFd = new Composite(farmInit, SWT.NONE);
		GridData initFd_ld = new GridData(GridData.FILL_HORIZONTAL);
		initFd.setLayoutData(initFd_ld);
		
		GridLayout initFd_l = new GridLayout(2, false);
		initFd.setLayout(initFd_l);
		Label initFd_label = new Label(initFd, SWT.NONE);
		initFd_label.setText("Init. Food: ");
		
		initFood = new Text(initFd, SWT.BORDER);
		GridData initFood_ld = new GridData(GridData.FILL_HORIZONTAL);
		initFood.setLayoutData(initFood_ld);
		
		//Checkboxes
		Composite checks = new Composite(this, SWT.BORDER);
		GridData checks_ld = new GridData(GridData.FILL_HORIZONTAL);
		checks.setLayoutData(checks_ld);
		GridLayout checks_l = new GridLayout(2, true);
		checks.setLayout(checks_l);
		
		cart = new Button(checks, SWT.CHECK);
		cart.setText("Has cart");
		cart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		poring = new Button(checks, SWT.CHECK);
		poring.setText("Has Poring");
		poring.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	public void createControl() {
		//Do nothing
	}

	public ROSimulation getROSimulation() {
		Integer max = Integer.parseInt(maxWeight.getText());
		Integer initCh = Integer.parseInt(initChild.getText());
		Integer initFd = Integer.parseInt(initFood.getText());
		boolean cart = this.cart.getSelection();
		boolean poring = this.poring.getSelection();
		
		ROFarm nextFarm = null;
		if(farm[0].getSelection()) {
			nextFarm = ROFarm.getPecoFarm(initCh, initFd);
		} else if(farm[1].getSelection()) {
			nextFarm = ROFarm.getPigFarm(initCh, initFd);
		} else if(farm[2].getSelection()) {
			nextFarm = ROFarm.getGoatFarm(initCh, initFd);
		}
		return new ROSimulation(nextFarm,max,poring,cart);
	}
}
