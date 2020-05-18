package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.people.Citizen;
import model.people.CitizenState;


public class Injury extends Disaster {

	public Injury(int startCycle, Citizen target) {
		super(startCycle, target);
	}
	@Override
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException 
	{
		Citizen target = (Citizen)getTarget();
		if(target.getHp()==0||target.getState()==CitizenState.DECEASED)
		{
			CitizenAlreadyDeadException a=new CitizenAlreadyDeadException(this,"CitizenAlreadyDead");
			throw a;
		}
		else {
		target.setBloodLoss(target.getBloodLoss()+30);
		super.strike();
	}}
	@Override
	public void cycleStep() {
		Citizen target = (Citizen)getTarget();
		target.setBloodLoss(target.getBloodLoss()+10);
		
	}

}
