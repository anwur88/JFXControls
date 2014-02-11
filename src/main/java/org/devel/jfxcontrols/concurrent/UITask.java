/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * @author stefan
 * 
 */
public abstract class UITask<V> extends Task<V> {

	private CountDownLatch latch;

	/**
	 * Equivalent 2 Display.syncExec(..) known from SWT.
	 * 
	 * @param runnable
	 * @return true, if the runnable was run successfully inside the UI thread,
	 *         otherwise false
	 */
	protected boolean runSync(Runnable runnable) {
		latch = new CountDownLatch(1);

		Platform.runLater(() -> {
			runnable.run();
		});
		try {
			return latch.await(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}

	protected void rideOn() {
		latch.countDown();
	}

}
