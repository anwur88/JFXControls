/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;

import org.devel.jfxcontrols.scene.control.Aggregator;

/**
 * @author stefan.illgen
 *
 */
public class AggregatorSkin extends SkinBase<Aggregator> {

	public AggregatorSkin(Aggregator control) {
		super(control);
		createNodes();
		getSkinnable().requestLayout();
	}
	
	/**
	 * TODO stefan - What 4 getControl()?
	 * 
	 * @return
	 */
	public Aggregator getControl() {
		return getSkinnable();
	}
	
	private Button aggregatedButton;

	private void createNodes() {
		
		aggregatedButton = new Button("Aggregated Button");
		getChildren().add(aggregatedButton);
		
	}

	@Override
	protected void layoutChildren(double contentX, double contentY,
			double contentWidth, double contentHeight) {
		super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
		
//		layoutInArea(aggregatedButton, 0, 0, aggregatedButton.getWidth(), aggregatedButton.getHeight(), Control.USE_PREF_SIZE, HPos.LEFT, VPos.TOP);
		layoutInArea(aggregatedButton, 0, 0, contentWidth, contentHeight, Control.USE_PREF_SIZE, HPos.LEFT, VPos.TOP);
	}

	@Override
	protected double computePrefWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		
		super.computePrefWidth(height, topInset, rightInset, bottomInset,
				leftInset);
		
		double prefWidth = aggregatedButton.prefWidth(height);
		return prefWidth;
	}

	@Override
	protected double computePrefHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		
		super.computePrefHeight(width, topInset, rightInset, bottomInset,
				leftInset);
		
		double prefHeight = aggregatedButton.prefHeight(width);
		return prefHeight;
	}
	
	

}
