package org.devel.jfxcontrols.scene.image;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;

import org.devel.jfxcontrols.scene.shape.CropperRectangle;

/**
 * This is no {@link Control}, thus layout calculations take place in here. 
 * 
 * @author stefan.illgen
 * 
 */
public class SourceImageView extends ImageView {

	/*
	 * Property 4 handling custom rectangles.
	 */
	private ObjectProperty<CropperRectangle> _cropperRectangle;

	public void initialize() {
		
		// calculate fit height in relation to source image size and source
		// scroll pane size
		fitHeightProperty().bind(new DoubleBinding() {
			{
				super.bind(imageProperty());
			}

			@Override
			protected double computeValue() {
				if (getImage() == null)
					return 0;
				return getImage().getHeight();
			}
		});

		// calculate fit width in relation to source image size and source
		// scroll pane size
		fitWidthProperty().bind(new DoubleBinding() {
			{
				super.bind(imageProperty());
			}

			@Override
			protected double computeValue() {
				if (getImage() == null)
					return 0;
				return getImage().getWidth();
			}
		});

		// bind cropper max points 2 source image size
		getCropperRectangle().maxXProperty().bind(fitWidthProperty());
		getCropperRectangle().maxYProperty().bind(fitHeightProperty());
	}

	public ObjectProperty<CropperRectangle> cropperRectangleProperty() {
		if (_cropperRectangle == null)
			_cropperRectangle = new SimpleObjectProperty<CropperRectangle>(
					new CropperRectangle());
		return _cropperRectangle;
	}

	public CropperRectangle getCropperRectangle() {
		return cropperRectangleProperty().get();
	}

	public void setCropperRectangle(CropperRectangle cropperRectangle) {
		cropperRectangleProperty().set(cropperRectangle);
	}

	public void reset() {
		setImage(null);
	}

}
