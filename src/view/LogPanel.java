package view;

public class LogPanel extends DataPanel {

	public LogPanel ()
	{
		super("Log","Game started...");
	}

	public void updateData(String data) {
		super.updateData(data, false);
	}
}
