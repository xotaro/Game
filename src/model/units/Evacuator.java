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

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle, WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}

	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();

		if (target.getStructuralIntegrity() == 0 || target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity() && i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX() + target.getLocation().getY());

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof Citizen) {
			Citizen target = (Citizen) r;
			IncompatibleTargetException a = new IncompatibleTargetException(this, target, "You can't treat citizen with an Evacuater");
			throw a;
		} else if (this.canTreat(r) == false || ((ResidentialBuilding) r).getDisaster() instanceof GasLeak
				|| ((ResidentialBuilding) r).getDisaster() instanceof Fire) {
			ResidentialBuilding target = (ResidentialBuilding) r;
			CannotTreatException a = new CannotTreatException(this, target, "You can't treat this building");
			throw a;
		} else {
			super.respond(r);
		}
	}

	
}
