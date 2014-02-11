/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import java.net.URL;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.util.Callback;

import org.devel.jfxcontrols.conf.AppMode;

import com.sun.javafx.scene.web.Debugger;

/**
 * @author stefan.illgen
 * 
 */
public class WebEngineLoader extends UITask<Boolean> {

	private static final long TOTAL_WORK = 100;
	private static final long WORK_DONE_LOAD = 100;
	private static final long WORK_DONE_PRE_LOAD = 33;

	private WebEngine webEngine;
	private URL url;
	private Worker<Void> loadWorker;

	public WebEngineLoader(WebEngine webEngine, URL url) {
		this.webEngine = webEngine;
		this.url = url;
	}

	@Override
	protected Boolean call() throws Exception {

		updateProgress(0, TOTAL_WORK);

		runSync(() -> {
			loadMonitors(webEngine);
			initializeDebugger(webEngine);
			updateProgress(WORK_DONE_PRE_LOAD, TOTAL_WORK);
			rideOn();
		});

		return runSync(() -> {
			webEngine.load(url.toExternalForm());
			loadWorker.stateProperty().addListener(
					(ov, oldState, newState) -> {
						if (newState != null && newState == State.SUCCEEDED) {
							updateProgress(WORK_DONE_LOAD, TOTAL_WORK);
							rideOn();	
						}
					});
		});

	}

	private void loadMonitors(WebEngine webEngine) {

		loadWorker = webEngine.getLoadWorker();

		// monitor state
		loadWorker.stateProperty().addListener(
				new ChangeListener<Worker.State>() {
					public void changed(ObservableValue<? extends State> ov,
							State oldValue, State newValue) {
						if (ov.getValue() == null)
							ov.removeListener(this);
						if (AppMode.DEV.isActive())
							System.err.printf(
									"State changed, old: %s, new: %s%n",
									oldValue, newValue);
					}
				});

		// monitor exceptions
		loadWorker.exceptionProperty().addListener(
				new ChangeListener<Throwable>() {
					public void changed(
							ObservableValue<? extends Throwable> ov,
							Throwable oldValue, Throwable newValue) {
						if (ov.getValue() == null)
							ov.removeListener(this);
						if (AppMode.DEV.isActive())
							System.err.printf(
									"Exception changed, old: %s, new: %s%n",
									oldValue, newValue);
					}
				});

	}

	@SuppressWarnings({ "deprecation" })
	private void initializeDebugger(WebEngine webEngine) {
		if (AppMode.DEV.isActive()) {
			Debugger debugger = webEngine.impl_getDebugger();
			debugger.setMessageCallback(new Callback<String, Void>() {
				@Override
				public Void call(String arg0) {
					if (AppMode.DEV.isActive())
						System.err.println(arg0);
					return null;
				}
			});
		}
	}

}
