/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.devel.jfxcontrols.scene.image.SourceImageView;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropperOld extends Control {

	// ##### properties ######

	// TODO
	private StringProperty loadButtonText;
	private StringProperty saveButtonText;
	private DoubleProperty targetHeight;
	private DoubleProperty targetWidth;

	private ObjectProperty<SourceImageView> sourceImageView;
	private ObjectProperty<ImageView> targetImageView;

	public ImageCropperOld() {
		super();
		setStyle(null);
		getStyleClass().add("image-cropper-old");
		setFocusTraversable(false);
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

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource(
				"/org/devel/jfxcontrols/scene/control/"
						+ getClass().getSimpleName() + ".css").toExternalForm();
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

}
