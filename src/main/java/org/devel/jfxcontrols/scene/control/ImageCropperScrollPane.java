/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import javafx.scene.control.ScrollPane;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropperScrollPane extends ScrollPane {

	public ImageCropperScrollPane() {
		setupSkin();
	}

	// ### private API ###

	private void setupSkin() {
		getStyleClass().add("image-cropper-scroll-pane");
	}

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("image-cropper.css").toExternalForm();
	}

}
