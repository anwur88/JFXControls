/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import javafx.scene.web.WebEngine;

/**
 * @author stefan.illgen
 * 
 */
public class RouteComputer extends UITask<Boolean> {

	private static final int WORK_DONE_EXECUTING = 0x32;
	private static final int WORK_DONE_EXECUTE = 0x64;
	private static final int WORK_TOTAL = 100;

	private WebEngine webEngine;
	private String startPosition;
	private String finishPosition;

	public RouteComputer(WebEngine webEngine, String startPosition,
			String finishPosition) {
		super(25);
		this.webEngine = webEngine;
		this.startPosition = startPosition;
		this.finishPosition = finishPosition;
	}

	@Override
	protected Boolean call() throws Exception {

		updateProgress(0, WORK_TOTAL);

		if (!webEngine.isJavaScriptEnabled())
			return false;

		// execute engine in a separate ui thread
		boolean result = runSync(() -> {
			webEngine.executeScript("calcRoute(\"" + startPosition + "\", \""
					+ finishPosition + "\")");
			updateProgress(WORK_DONE_EXECUTING, WORK_TOTAL);
			rideOn();
		});

		updateProgress(WORK_DONE_EXECUTE, WORK_TOTAL);

		return result;
	}

}
