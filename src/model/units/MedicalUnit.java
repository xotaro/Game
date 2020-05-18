package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public abstract class MedicalUnit extends Unit {
	private int healingAmount;
	private int treatmentAmount;

	public MedicalUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
		healingAmount = 10;
		treatmentAmount = 10;
	}

	public int getTreatmentAmount() {
		return treatmentAmount;
	}

	public void heal() {
		Citizen target = (Citizen) getTarget();
		if (target.getHp() < 100)
			target.setHp(target.getHp() + healingAmount);

		if (target.getHp() == 100)
			jobsDone();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof ResidentialBuilding) {
			ResidentialBuilding target = (ResidentialBuilding) r;
			IncompatibleTargetException a = new IncompatibleTargetException(this, target, "Can't treat building with Medical unit");
			throw a;
		} else if (this.canTreat(r) == false) {
			ResidentialBuilding target = (ResidentialBuilding) r;
			CannotTreatException a = new CannotTreatException(this, target, "You can't treat this citizen");
			throw a;
		}
	}

	@Override
	public String toString() {
		return String.format("%s\nTreatment amount: %s\nHealing amount: %s", super.toString(),treatmentAmount,healingAmount);
	}

}
