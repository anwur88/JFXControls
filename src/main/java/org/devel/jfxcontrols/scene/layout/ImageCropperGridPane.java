/**
 * 
 */
package org.devel.jfxcontrols.scene.layout;

import java.io.IOException;
import java.net.URL;

import org.devel.jfxcontrols.scene.control.ImageCropperGrid;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

/**
 * @author stefan.illgen
 *
 */
public class ImageCropperGridPane extends GridPane {

	public ImageCropperGridPane(final ImageCropperGrid imageCropper) {
		
		// load FXML
		URL url = getClass().getResource("ImageCropperGridPane.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(imageCropper);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
