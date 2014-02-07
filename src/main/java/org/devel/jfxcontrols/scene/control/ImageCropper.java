/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import org.devel.jfxcontrols.scene.image.SourceImageView;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropper extends Control {

	private ObjectProperty<ImageView> targetImageView;
	private ObjectProperty<SourceImageView> sourceImageView;

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
	
	public ObjectProperty<SourceImageView> sourceImageViewProperty() {
		if (sourceImageView == null)
			sourceImageView = new SimpleObjectProperty<SourceImageView>(
					new SourceImageView()) {
			};
		return sourceImageView;
	}

	public void setSourceImageView(SourceImageView sourceImageView) {
		sourceImageViewProperty().set(sourceImageView);
	}
	
	public SourceImageView getSourceImageView() {
		return sourceImageViewProperty().get();
	}
	
	public ObjectProperty<ImageView> targetImageViewProperty() {
		if (targetImageView == null)
			targetImageView = new SimpleObjectProperty<ImageView>(
					new ImageView()) {
			};
		return targetImageView;
	}

	public void setTargetImageView(ImageView targetImageView) {
		targetImageViewProperty().set(targetImageView);
	}

	public ObjectProperty<ImageView> getTargetImageView() {
		return targetImageViewProperty();
	}
	
	public ObjectProperty<Image> sourceImageProperty() {
		return sourceImageViewProperty().get().imageProperty();
	}

	public Image getSourcemage() {
		return sourceImageViewProperty().get().imageProperty().get();
	}

	public void setSourceImage(Image sourceImage) {
		sourceImageViewProperty().get().imageProperty().set(sourceImage);
	}

	public ObjectProperty<Image> targetImageProperty() {
		return targetImageViewProperty().get().imageProperty();
	}

	public Image getTargetImage() {
		return targetImageViewProperty().get().imageProperty().get();
	}

	public void setTargetImage(Image targetImage) {
		targetImageViewProperty().get().imageProperty().set(targetImage);
	}


}
