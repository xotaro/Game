package controller;

import model.disasters.Disaster;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;

public interface LogListener {
	void onCitizenDie(Citizen citizen);
	void onBuildingCollapsed(ResidentialBuilding building);
	void onDiasterStrick(Disaster disaster);
}
