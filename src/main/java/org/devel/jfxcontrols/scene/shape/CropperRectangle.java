/**
 * 
 */
package org.devel.jfxcontrols.scene.shape;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * This {@link Rectangle} must know source image to calculate its movement
 * concerning minimum and maximum.
 * 
 * @author stefan.illgen
 * 
 */
public class CropperRectangle extends Rectangle implements Initializable {

	public static final double DEFAULT_WIDTH = 100;
	public static final double DEFAULT_HEIGHT = 133;
	public static final double DEFAULT_REF_X = 0;
	public static final double DEFAULT_REF_Y = 0;

	// reference points of mouse pressed location
	private DoubleProperty referencePointX;
	private DoubleProperty refY;
	private DoubleProperty maxX;
	private DoubleProperty maxY;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// mouse pressed
		addEventHandler(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						setRefX(event.getX());
						setRefY(event.getY());
					}
				});

		// rectangle moved
		addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				// ### X axis ###
				// offset inside cropperRectangle
				double offsetX = getRefX() - getX();
				// distance to translate the rectangle to
				double translateX = getTranslateX() + event.getX() - offsetX;
				// new x position of the left upper corner for the
				// cropperRectangle
				double newXLeft = getX() + translateX;
				// new x position of the downer edge for the cropperRectangle
				double newXRight = newXLeft + getWidth();
				if (newXLeft >= 0 && newXRight <= getMaxX())
					setTranslateX(translateX);

				// ### Y axis ###
				// offset inside cropperRectangle
				double offsetY = getRefY() - getY();
				// distance to translate the rectangle to
				double translateY = getTranslateY() + event.getY() - offsetY;
				// new y position of the upper edge for the cropperRectangle
				double newYUp = getY() + translateY;
				// new y position of the downer edge for the cropperRectangle
				double newYDown = newYUp + getHeight();
				if (newYUp >= 0 && newYDown <= getMaxY())
					setTranslateY(translateY);

			}
		});

	}

	public DoubleProperty refXProperty() {
		if (referencePointX == null)
			referencePointX = new SimpleDoubleProperty(DEFAULT_REF_X);
		return referencePointX;
	}

	public double getRefX() {
		return refXProperty().get();
	}

	public void setRefX(double refX) {
		this.refXProperty().set(refX);
	}

	public DoubleProperty refYProperty() {
		if (refY == null)
			refY = new SimpleDoubleProperty(DEFAULT_REF_Y);
		return refY;
	}

	public double getRefY() {
		return refYProperty().get();
	}

	public void setRefY(double refY) {
		this.refYProperty().set(refY);
	}

	public DoubleProperty maxXProperty() {
		if (maxX == null)
			maxX = new SimpleDoubleProperty(DEFAULT_WIDTH);
		return maxX;
	}

	public double getMaxX() {
		return  maxXProperty().get();
	}

	public void setMaxX(double maxX) {
		this.maxXProperty().set(maxX);
	}

	public DoubleProperty maxYProperty() {
		if (maxY == null)
			maxY = new SimpleDoubleProperty(DEFAULT_HEIGHT);
		return maxY;
	}

	public double getMaxY() {
		return maxYProperty().get();
	}

	public void setMaxY(double maxY) {
		this.maxYProperty().set(maxY);
	}

	public void reset() {
		// TODO stefan - solve via bindings
		setTranslateX(0.0);
		setTranslateY(0.0);
	}

}
