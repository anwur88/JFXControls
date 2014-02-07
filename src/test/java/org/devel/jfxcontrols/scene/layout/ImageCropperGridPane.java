/**
 * 
 */
package org.devel.jfxcontrols.scene.layout;

import java.io.IOException;
import java.net.URL;

import org.devel.jfxcontrols.scene.control.ImageCropperGridPaneController;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

/**
 * This does not work! When importing FXML based custom controls into the scene
 * builder, the control cannot be resolved.
 * 
 * @author stefan.illgen
 * 
 */
public class ImageCropperGridPane extends GridPane {

	public ImageCropperGridPane() {
		loadFXML();
	}

	private void loadFXML() {

		URL url = getClass().getResource("ImageCropperGridPane.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(new ImageCropperGridPaneController());

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
