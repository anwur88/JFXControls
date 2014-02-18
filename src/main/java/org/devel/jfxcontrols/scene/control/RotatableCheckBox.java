/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import javafx.scene.control.CheckBox;

/**
 * @author stefan.illgen
 *
 */
public class RotatableCheckBox extends CheckBox {

	private static final String DEFAULT_STYLE_CLASS = "rotatable-check-box";
	
	public RotatableCheckBox() {
		super();
		setSth();
	}

	public RotatableCheckBox(String text) {
		super(text);
		setSth();
	}
	

	private void setSth() {
		/*
		 * Do not set but add this style class in order to reuse given css
		 * constraints for check-box.
		 */
		getStyleClass().addAll(DEFAULT_STYLE_CLASS);
	}
}
