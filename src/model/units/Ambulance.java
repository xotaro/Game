package model.units;


import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Infection;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}
	

	@Override
	public void treat()  {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		
		
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();

	}

	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {
		if( r instanceof ResidentialBuilding) {
			ResidentialBuilding target=(ResidentialBuilding)r;
			IncompatibleTargetException a = new IncompatibleTargetException(this, target, "You can't treat a building with an ambulance");
			throw a;
		}else if(this.canTreat(r)==false|| ((Citizen)r).getDisaster() instanceof Infection) {
			Citizen target=(Citizen)r;
			CannotTreatException a = new CannotTreatException(this, target, " You can't treat this citizen");
			throw a;
		}
		if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0
				&& getState() == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
	}

}
