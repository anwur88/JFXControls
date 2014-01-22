/**
 * 
 */
package org.devel.jfxcontrols.sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.devel.jfxcontrols.scene.layout.SearchRoutePane;
import org.devel.jfxcontrols.util.Properties;

/**
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
		
		SearchRoutePane srp = new SearchRoutePane();
		Scene scene = new Scene(srp);
		// scene.getStylesheets().add("/path/to/css");
		stage.setScene(scene);
		stage.setTitle(getClass().getName());

		stage.show();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
