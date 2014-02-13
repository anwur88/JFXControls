/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import com.sun.javafx.scene.control.skin.CheckBoxSkin;

/**
 * @author stefan.illgen
 * 
 */
public class CircleCheckBoxSkin extends CheckBoxSkin {

	private final Circle circle = new Circle();

	public CircleCheckBoxSkin(CheckBox checkbox) {
		super(checkbox);
		// configure circle
		circle.getStyleClass().setAll("circle");
		circle.setCenterX(5f);
		circle.setCenterY(5f);
		circle.setRadius(10.0f);
		updateChildren();
	}

	@Override
	protected void updateChildren() {
		super.updateChildren();
		addCircle();
	}

	private void addCircle() {
		if (getChildren() != null && getChildren().size() > 1
				&& getChildren().get(1) != null
				&& getChildren().get(1) instanceof StackPane) {
			StackPane box = (StackPane) getChildren().get(1);
			// add before inner box			
			if (circle != null)
				box.getChildren().add(0, circle);
		}
	}
}
