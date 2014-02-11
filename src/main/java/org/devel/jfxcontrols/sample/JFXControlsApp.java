/**
 * 
 */
package org.devel.jfxcontrols.sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.devel.jfxcontrols.conf.Properties;
import org.devel.jfxcontrols.resource.ImageRegistry;

/**
 * TODO stefan - create a tabular to show controls.
 * 
 * @author stefan.illgen
 * 
 */
public class JFXControlsApp extends Application {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception {

		setup(stage);

		Scene scene = new Scene(getParent());
		// scene.getStylesheets().add("/path/to/css");
		stage.setScene(scene);
		stage.setTitle(getClass().getName());

		stage.show();

	}

	private void setup(Stage stage) {
		
		// conf
		Properties.init();
		
		// image registry
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (event.getEventType().equals(
						WindowEvent.WINDOW_CLOSE_REQUEST)
						&& event.getTarget().equals(stage)) {
					stage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this);
					ImageRegistry.instance().dispose();					
				}
					
			}
		});
	}

	private Parent getParent() {
		// return new SearchRoutePane();
		// return new Aggregator();
		// return new ImageCropper();
		return new JFXShowCase();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
