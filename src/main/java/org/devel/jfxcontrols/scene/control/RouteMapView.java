/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.control.Control;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import org.devel.jfxcontrols.concurrent.CalcRouteTask;

import com.sun.javafx.scene.web.Debugger;

/**
 * @author stefan.illgen
 */
@SuppressWarnings("restriction")
public class RouteMapView extends Control {

	private static final String GOOGLEMAPS_HTML = "googleMapsJSAPI.html";
	private static final boolean DEBUG = false;

	private WebView routeMapView;
	private WebEngine webEngine;
	private Worker<Void> loadWorker;
	private StringProperty startPosition;
	private StringProperty finishPosition;
	private RouteListener startPositionListener;
	private RouteListener finishPositionListener;
	private CalcRouteTask calcRouteTask;
	private ExecutorCompletionService<List<Boolean>> executorCompletionService;
	private ScheduledThreadPoolExecutor threadPool;

	public RouteMapView() {
		setupSkin();
		setupEngine();

	}

	/**
	 * 
	 * @return
	 */
	public WebEngine getWebEngine() {
		return webEngine;
	}

	/**
	 * 
	 * @return
	 */
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
	 * @param startPosition
	 */
	public void setStartPosition(String startPosition) {
		this.startPositionProperty().set(startPosition);
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

	/**
	 * 
	 * @param finishPosition
	 */
	public void setFinishPosition(String finishPosition) {
		this.finishPositionProperty().set(finishPosition);
	}

	// ### private API ###

	private void setupSkin() {
		getStyleClass().add("route-map-view");
	}

	/**
	 * Integrate skin.
	 */
	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("route-map-view.css").toExternalForm();
	}

	/**
	 * 
	 */
	private void setupEngine() {

		routeMapView = new WebView();
		routeMapView.setId("routeMapView");

		// add it to the scene graph
		getChildren().add(routeMapView);

		// get the web engine
		webEngine = routeMapView.getEngine();
		loadMonitors(webEngine);
		initializeDebugger(webEngine);

		// load url into the engine
		final URL urlGoogleMaps = getClass().getResource(GOOGLEMAPS_HTML);
		webEngine.load(urlGoogleMaps.toExternalForm());

		// listen for webEngine to initiate displaying of the route
		getWebEngine().getLoadWorker().stateProperty()
				.addListener(new EngineSucceededListener());

	}

	/**
	 * 
	 */
	private void calcRoute() {

		// if (calcRouteTask == null)
		// calcRouteTask = new CalcRouteTask(webEngine,
		// startPositionProperty(), finishPositionProperty());
		// if (threadPool == null)
		// threadPool = (ScheduledThreadPoolExecutor) Executors
		// .newScheduledThreadPool(1);
		//
		//
		// // clear if still scheduled
		// if (calcRouteTask.isScheduled())
		// threadPool.getQueue().remove(calcRouteTask);
		// // schedule
		// threadPool.schedule(calcRouteTask, 2, TimeUnit.SECONDS);

		webEngine.executeScript("calcRoute(\"" + getStartPosition() + "\", \""
				+ getFinishPosition() + "\")");
	}

	private void loadMonitors(WebEngine webEngine) {

		loadWorker = webEngine.getLoadWorker();

		// monitor state
		loadWorker.stateProperty().addListener(
				new ChangeListener<Worker.State>() {
					public void changed(ObservableValue<? extends State> ov,
							State oldValue, State newValue) {
						if (DEBUG)
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
						if (DEBUG)
							System.err.printf(
									"Exception changed, old: %s, new: %s%n",
									oldValue, newValue);
					}
				});

	}

	@SuppressWarnings({ "deprecation" })
	private void initializeDebugger(WebEngine webEngine) {
		if (DEBUG) {
			Debugger debugger = webEngine.impl_getDebugger();
			debugger.setMessageCallback(new Callback<String, Void>() {
				@Override
				public Void call(String arg0) {
					if (DEBUG)
						System.err.println(arg0);
					return null;
				}
			});
		}
	}

	/**
	 * 
	 * @author stefan.illgen
	 * 
	 */
	class EngineSucceededListener implements ChangeListener<State> {

		@Override
		public void changed(ObservableValue<? extends State> state, State arg1,
				State newState) {
			if (newState == State.SUCCEEDED) {
				// remove change listener
				state.removeListener(this);
				// bind
				startPositionListener = new RouteListener();
				startPosition.addListener(startPositionListener);
				finishPositionListener = new RouteListener();
				finishPosition.addListener(finishPositionListener);
				// calculate route
				calcRoute();
			}
		}
	}

	/**
	 * 
	 * @author stefan.illgen
	 * 
	 */
	class RouteListener implements ChangeListener<String> {

		public long lastMillis = 0;

		@Override
		public void changed(ObservableValue<? extends String> arg0,
				String arg1, String arg2) {

			if (arg0.getValue() == null) {
				arg0.removeListener(this);
				return;
			}

			if (!arg2.trim().isEmpty())
				calcRoute();
		}
	}

}
