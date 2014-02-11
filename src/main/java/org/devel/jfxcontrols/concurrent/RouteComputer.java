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
		super();
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
		runSync(() -> {
			webEngine.executeScript("calcRoute(\"" + startPosition + "\", \""
					+ finishPosition + "\")");
			updateProgress(WORK_DONE_EXECUTING, WORK_TOTAL);
			rideOn();
		});

		updateProgress(WORK_DONE_EXECUTE, WORK_TOTAL);

		return true;
	}

//	protected boolean runSync(Runnable runnable) {
//		final CountDownLatch latch = new CountDownLatch(1);
//
//		Platform.runLater(() -> {
//			runnable.run();
//			latch.countDown();
//		});
//		try {
//			updateProgress(WORK_DONE_EXECUTING, WORK_TOTAL);
//			return latch.await(5, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			return false;
//		}
//	}

}
