package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Injury;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	public void treat()   {
		
		getTarget().getDisaster().setActive(false);
 
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
	
		{
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0) 
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}
		
	}
	
	public void respond (Rescuable r) throws CannotTreatException, IncompatibleTargetException{
		if( r instanceof Citizen) {
			Citizen target=(Citizen)r;
			IncompatibleTargetException a = new IncompatibleTargetException(this, target, "Can't treat citizen with Gas control unit");
			throw a;
		}else if(this.canTreat(r)==false||((ResidentialBuilding)r).getDisaster() instanceof Collapse||((ResidentialBuilding)r).getDisaster() instanceof Fire) {
			ResidentialBuilding target=(ResidentialBuilding)r;
			CannotTreatException a = new CannotTreatException(this, target, " You can't treat this building");
			throw a;
		}else {
			ResidentialBuilding target=(ResidentialBuilding)r;
			if (target != null && this.getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);

		
		}
		
		 
		
		
	}
	
}
