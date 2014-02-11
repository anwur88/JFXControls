/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.devel.jfxcontrols.concurrent.RouteComputer;
import org.devel.jfxcontrols.concurrent.WebEngineLoader;
import org.devel.jfxcontrols.scene.control.skin.RouteMapViewSkin;

/**
 * @author stefan.illgen
 */
public class RouteMapView extends Control {

	private static final String DEFAULT_STYLE_CLASS = "route-map-view";
	// batch delay in seconds
	private static final int BATCH_DELAY = 2;

	private static final String GOOGLEMAPS_HTML = "googleMapsJSAPI.html";

	private WebView routeWebView;
	private WebEngine webEngine;
	private ScheduledThreadPoolExecutor threadPool;
	private ScheduledFuture<?> activeTask;

	private StringProperty startPosition;
	private StringProperty finishPosition;
	private RouteListener startPositionListener;
	private RouteListener finishPositionListener;

	public RouteMapView() {
		super();
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		initialize();
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

	@Override
	protected Skin<?> createDefaultSkin() {
		return new RouteMapViewSkin(this);
	}

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("route-map-view.css").toExternalForm();
	}

	private void initialize() {

		routeWebView = new WebView();
		routeWebView.setId("routeMapView");
		getChildren().add(routeWebView);

		// setupThreadPool();
		// loadEngine();
	}

	public void setupThreadPool() {

		if (threadPool == null) {
			threadPool = (ScheduledThreadPoolExecutor) Executors
					.newScheduledThreadPool(1);
			threadPool
					.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
			threadPool.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
			threadPool.setRemoveOnCancelPolicy(true);

			// TODO stefan - remove thread pool on window close
			// getScene().getWindow().setOnCloseRequest(
			// we -> {
			// try {
			// threadPool
			// .awaitTermination(
			// THREADPOOL_LOADER_TIMEOUT,
			// TimeUnit.SECONDS);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			// });
		}
	}

	public void loadEngine() {

		// get the web engine
		webEngine = routeWebView.getEngine();

		WebEngineLoader webEngineLoader = new WebEngineLoader(webEngine,
				getClass().getResource(GOOGLEMAPS_HTML));
		webEngineLoader.setOnSucceeded(wse -> {
			if (wse.getEventType().equals(
					WorkerStateEvent.WORKER_STATE_SUCCEEDED)) {
				// bind
				startPositionListener = new RouteListener();
				startPosition.addListener(startPositionListener);
				finishPositionListener = new RouteListener();
				finishPosition.addListener(finishPositionListener);
				// calculate route
				computeRoute();
			}
		});
		threadPool.schedule(webEngineLoader, 0, TimeUnit.SECONDS);

	}

	public void computeRoute() {
		computeRoute(0, TimeUnit.SECONDS);
	}

	public void computeRoute(int delay) {
		computeRoute(delay, TimeUnit.SECONDS);
	}

	public void computeRoute(int delay, TimeUnit unit) {

		if (unit != null) {
			if (activeTask != null)
				// cancel previous task, if still delayed or interrupt, if delay
				// of the new task is smaller than the delay
				// of the active task
				activeTask.cancel(activeTask.getDelay(unit) > delay);
			// schedule and store reference to future
			activeTask = threadPool.schedule(new RouteComputer(webEngine,
					getStartPosition(), getFinishPosition()), delay, unit);
		} else {
			if (activeTask != null)
				activeTask.cancel(true);
		}

	}

	/**
	 * 
	 * @author stefan.illgen
	 * 
	 */
	class RouteListener implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue<? extends String> arg0,
				String arg1, String arg2) {

			if (arg0.getValue() == null) {
				arg0.removeListener(this);
				return;
			}

			if (!arg2.trim().isEmpty())
				computeRoute(BATCH_DELAY, TimeUnit.SECONDS);
		}
	}

}
