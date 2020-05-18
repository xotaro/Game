package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.GasLeak;
import model.disasters.Injury;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;


public abstract class FireUnit extends Unit {

	public FireUnit(String unitID, Address location, int stepsPerCycle,WorldListener worldListener) {
		super(unitID, location, stepsPerCycle,worldListener);
	}
	public void respond (Rescuable r) throws CannotTreatException, IncompatibleTargetException{
		if( r instanceof Citizen) {
			Citizen target=(Citizen)r;
			IncompatibleTargetException a = new IncompatibleTargetException(this, target, "You can't treat citizen with Fire Unit");
			throw a;
		}else if(this.canTreat(r)==false||((ResidentialBuilding)r).getDisaster() instanceof Collapse||((ResidentialBuilding)r).getDisaster() instanceof GasLeak ||((ResidentialBuilding)r).getDisaster() instanceof Injury) {
			ResidentialBuilding target=(ResidentialBuilding)r;
			CannotTreatException a = new CannotTreatException(this, target, "You can't treat this building");
			throw a;
		}else {
		super.respond(r);
		}
		
		 
		
		
	}
}
