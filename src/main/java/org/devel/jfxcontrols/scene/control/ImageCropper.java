/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import javafx.scene.control.Control;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropper extends Control {

	// ##### properties ######

	public ImageCropper() {
		super();
		setStyle(null);
		getStyleClass().add("image-cropper");
	}

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource(
				"/org/devel/jfxcontrols/scene/control/"
						+ getClass().getSimpleName() + ".css").toExternalForm();
	}


}
