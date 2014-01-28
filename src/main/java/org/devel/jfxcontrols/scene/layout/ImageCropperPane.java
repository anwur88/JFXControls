/**
 * 
 */
package org.devel.jfxcontrols.scene.layout;

import java.io.IOException;
import java.net.URL;

import org.devel.jfxcontrols.scene.control.ImageCropper;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

/**
 * @author stefan.illgen
 *
 */
public class ImageCropperPane extends GridPane {

	public ImageCropperPane(final ImageCropper imageCropper) {
		
		// load FXML
		URL url = getClass().getResource("ImageCropperPane.fxml");
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
