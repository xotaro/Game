package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.disasters.Injury;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location,
			int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);
		Citizen target = (Citizen) getTarget();
		
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getToxicity() > 0) {
			target.setToxicity(target.getToxicity() - getTreatmentAmount());
			if (target.getToxicity() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getToxicity() == 0)
			heal();

	}

	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {
		if( r instanceof ResidentialBuilding) {
			ResidentialBuilding target=(ResidentialBuilding)r;
			IncompatibleTargetException a = new IncompatibleTargetException(this, target, "You can't treat a building with Disease Control Unit");
			throw a;
		}else if(this.canTreat(r)==false ||((Citizen)r).getDisaster() instanceof Collapse||((Citizen)r).getDisaster() instanceof Fire||((Citizen)r).getDisaster() instanceof Injury ) {
			Citizen target=(Citizen)r;
			CannotTreatException a = new CannotTreatException(this, target, "You can't treat this citizen");
			throw a;
		}
		
		if (getTarget() != null && ((Citizen) getTarget()).getToxicity() > 0
				&& getState() == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
	}

}
