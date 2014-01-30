/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropperScrollPane extends ScrollPane {

	public ImageCropperScrollPane() {
		setupSkin();
		loadFXML();
	}

	// ### private API ###

	private void loadFXML() {
		// load FXML
		URL url = getClass().getResource("ImageCropperScrollPane.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}

	private void setupSkin() {
		getStyleClass().add("image-cropper-scroll-pane");
	}

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("image-cropper.css").toExternalForm();
	}

}
