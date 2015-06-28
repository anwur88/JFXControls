/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import javafx.scene.control.Control;

/**
 * @author stefan.illgen
 *
 */
public class Aggregator extends Control {
	
	public Aggregator() {
		setStyle(null);
		getStyleClass().add("aggregator");
	}

	@Override
	public String getUserAgentStylesheet() {
		return getClass().getResource(
				"/org/devel/jfxcontrols/scene/control/"
						+ getClass().getSimpleName() + ".css").toExternalForm();
	}
	
	

}
