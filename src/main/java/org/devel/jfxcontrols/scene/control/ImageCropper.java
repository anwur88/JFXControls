/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.image.Image;

import org.devel.jfxcontrols.scene.image.SourceImageView;
import org.devel.jfxcontrols.scene.image.TargetImageView;

/**
 * @author stefan.illgen
 *
 */
public class ImageCropper extends Control {
	
	// ##### properties ######
	
	// TODO
	private StringProperty loadButtonText;
	private StringProperty saveButtonText;
	private DoubleProperty targetHeight;
	private DoubleProperty targetWidth;
	
	private SourceImageView sourceImageView;
	private TargetImageView targetImageView;
	
	public ImageCropper() {
		super();
		setStyle(null);
		getStyleClass().add("image-cropper");
		// TODO stefan set focus traversable 2 true
		setFocusTraversable(false);
	}
	
	
	
	public ObjectProperty<Image> sourceImageProperty() {
		return sourceImageView.imageProperty();
	}

	public Image getSourcemage() {
		return sourceImageView.imageProperty().get();
	}

	public void setSourceImage(Image sourceImage) {
		sourceImageView.imageProperty().set(sourceImage);
	}

	public ObjectProperty<Image> targetImageProperty() {
		return targetImageView.imageProperty();
	}

	public Image getTargetImage() {
		return targetImageView.imageProperty().get();
	}

	public void setTargetImage(Image targetImage) {
		targetImageView.imageProperty().set(targetImage);
	}
	
	
	
	@Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource(
                "/org/devel/jfxcontrols/scene/control/"
                        + getClass().getSimpleName() + ".css").toExternalForm();
    }

}
