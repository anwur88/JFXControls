/**
 * 
 */
package org.devel.jfxcontrols.sample;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.devel.jfxcontrols.scene.control.ImageCropper;
import org.devel.jfxcontrols.util.Properties;

/**
 * TODO stefan - create a tabular to show controls.
 * 
 * @author stefan.illgen
 *
 */
public class JFXControlsApp extends Application {

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception {
		
		new Properties().loadProxyConf();
		
		Scene scene = new Scene(getParent());
		// scene.getStylesheets().add("/path/to/css");
		stage.setScene(scene);
		stage.setTitle(getClass().getName());

		stage.show();
	}

	private Parent getParent() {
//		return new SearchRoutePane();
//		return new Aggregator();
		return new ImageCropper();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
