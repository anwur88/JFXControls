/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;

import org.devel.jfxcontrols.conf.AppMode;

/**
 * @author stefan.illgen
 * 
 */
@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
public class WebEngineLoader extends UITask<Boolean> {

	private static final long TOTAL_WORK = 100;
	private static final long WORK_DONE_LOAD = 100;
	private static final long WORK_DONE_PRE_LOAD = 33;

	private WebEngine webEngine;
	private URL url;
	private Worker<Void> loadWorker;
	private Map<ChangeListener, ObservableValue> cls = new HashMap<ChangeListener, ObservableValue>();

	public WebEngineLoader(WebEngine webEngine, URL url) {
		super(20, TimeUnit.SECONDS);
		this.webEngine = webEngine;
		this.url = url;
	}

	@Override
	protected Boolean call() throws Exception {

		updateProgress(0, TOTAL_WORK);

		boolean result = runSync(() -> {
			loadMonitors(webEngine);
			initializeDebugger(webEngine);
			updateProgress(WORK_DONE_PRE_LOAD, TOTAL_WORK);
			rideOn();
		});

		result &= runSync(() -> {
			webEngine.load(url.toExternalForm());
			ChangeListener<Worker.State> cl = (ov, oldState, newState) -> {
				if (newState != null && newState == State.SUCCEEDED) {
					updateProgress(WORK_DONE_LOAD, TOTAL_WORK);
					rideOn();
				}
			};
			loadWorker.stateProperty().addListener(cl);
		});

		dispose();

		return result;
	}

	private void loadMonitors(WebEngine webEngine) {

		loadWorker = webEngine.getLoadWorker();

		// monitor state
		final ChangeListener<State> cl1 = (ov, oldValue, newValue) -> {
			if (AppMode.DEV.isActive())
				System.err.printf("State changed, old: %s, new: %s%n",
						oldValue, newValue);
		};
		loadWorker.stateProperty().addListener(cl1);
		cls.put(cl1, loadWorker.stateProperty());

		// monitor exceptions
		ChangeListener<Throwable> cl2 = (ov, oldValue, newValue) -> {
			if (AppMode.DEV.isActive())
				System.err.printf("Exception changed, old: %s, new: %s%n",
						oldValue.getMessage(), newValue.getMessage());
		};
		loadWorker.exceptionProperty().addListener(cl2);
		cls.put(cl2, loadWorker.exceptionProperty());
		
	}

	private void initializeDebugger(WebEngine webEngine) {
		if (AppMode.DEV.isActive()) {
			webEngine.impl_getDebugger().setMessageCallback(m -> {
				if (AppMode.DEV.isActive())
					System.err.println(m);
				return null;
			});
		}
	}

	private void dispose() {
		cls.keySet().stream().forEach(c -> cls.get(c).removeListener(c));
	}

}
