/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropper extends Control {

	// source n target image properties
	private ObjectProperty<Image> sourceImage;
	private ObjectProperty<Image> targetImage;
	// cropper properties
	private ObjectProperty<Paint> cropperFillColor;
	private ObjectProperty<Paint> cropperStrokeColor;
	private DoubleProperty cropperWidth;
	private DoubleProperty cropperHeight;

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
	
//	private ImageCropperSkin getMySkin() {
//		return (ImageCropperSkin) super.getSkin();
//	}
	
	// source n target image properties
	
	public ObjectProperty<Image> sourceImageProperty() {
		if(sourceImage == null)
			sourceImage = new SimpleObjectProperty<Image>();
		return sourceImage;
	}

	public Image getSourceImage() {
		return sourceImageProperty().get();
	}

	public void setSourceImage(Image sourceImage) {
		sourceImageProperty().set(sourceImage);
	}

	public ObjectProperty<Image> targetImageProperty() {
		if(targetImage == null)
			targetImage = new SimpleObjectProperty<Image>();
		return targetImage;
	}

	public Image getTargetImage() {
		return targetImageProperty().get();
	}

	public void setTargetImage(Image targetImage) {
		targetImageProperty().set(targetImage);
	}
	
	// cropper properties
	
	public ObjectProperty<Paint> cropperFillColorProperty() {
		return cropperFillColor;
	}

	public Paint getCropperFillColor() {
		return cropperFillColor.get();
	}
	
	public void setCropperFillColor(Paint cropperColor) {
		this.cropperFillColor.set(cropperColor);
	}

	public ObjectProperty<Paint> cropperStrokeColorProperty() {
		return cropperStrokeColor;
	}

	public Paint getCropperStrokeColor() {
		return cropperStrokeColor.get();
	}
	
	public void setCropperStrokeColor(Color cropperStrokeColor) {
		this.cropperStrokeColor.set(cropperStrokeColor);
	}

	public DoubleProperty cropperWidthProperty() {
		return cropperWidth;
	}

	public double getCropperWidth() {
		return cropperWidth.get();
	}
	
	public void setCropperWidth(double cropperWidth) {
		this.cropperWidth.set(cropperWidth);
	}

	public DoubleProperty cropperHeightProperty() {
		return cropperHeight;
	}

	public double getCropperHeight() {
		return cropperHeight.get();
	}
	
	public void setCropperHeight(double cropperHeight) {
		this.cropperHeight.set(cropperHeight);
	}
	
}
