/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

/**
 * @author stefan.illgen
 * 
 */
public class CalcRouteTask extends Task<Boolean> {

	private static final int WORK_DONE_EXECUTE = 100;
	private static final int WORK_TOTAL = 100;

	private WebEngine webEngine;
	private StringProperty startPosition;
	private StringProperty finishPosition;

	public CalcRouteTask(WebEngine webEngine, StringProperty startPosition,
			StringProperty finishPosition) {
		super();
		this.webEngine = webEngine;
		this.startPosition = startPosition;
		this.finishPosition = finishPosition;
	}

	public StringProperty startPositionProperty() {
		if (startPosition == null)
			startPosition = new SimpleStringProperty();
		return startPosition;
	}

	/**
	 * 
	 * @return
	 */
	public String getStartPosition() {
		return startPositionProperty().get();
	}

	/**
	 * 
	 * @return
	 */
	public StringProperty finishPositionProperty() {
		if (finishPosition == null)
			finishPosition = new SimpleStringProperty();
		return finishPosition;
	}

	/**
	 * 
	 * @return
	 */
	public String getFinishPosition() {
		return finishPositionProperty().get();
	}

	@Override
	protected Boolean call() throws Exception {

		if (!webEngine.isJavaScriptEnabled())
			return false;

		updateProgress(0, WORK_TOTAL);
		
		// execute engine in a separate ui thread
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				webEngine.executeScript("calcRoute(\"" + getStartPosition()
						+ "\", \"" + getFinishPosition() + "\")");
			}
		});
		
		updateProgress(WORK_DONE_EXECUTE, WORK_TOTAL);
		return true;
	}

	public boolean isScheduled() {
		return getState().equals(State.SCHEDULED);
	}

}
