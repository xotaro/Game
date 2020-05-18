package model.disasters;

import controller.LogListener;
import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable {
	private int startCycle;
	private Rescuable target;
	private boolean active;
	private LogListener listener;

	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getStartCycle() {
		return startCycle;
	}

	public Rescuable getTarget() {
		return target;
	}

	public void strike() throws BuildingAlreadyCollapsedException, CitizenAlreadyDeadException {
		target.struckBy(this);
		active = true;
		if (listener != null)
			listener.onDiasterStrick(this);
	}

	public void setListener(LogListener listener) {
		this.listener = listener;
	}

}
