/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.util.List;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
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
	
	@Override
	protected void updateChildren() {
		super.updateChildren();
		addCircle();
	}
	
	@Override
	protected void layoutChildren(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		super.layoutChildren(x, y, w, h);
	}

	@Override
	protected double computeMinWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		// TODO Auto-generated method stub
		return super.computeMinWidth(height, topInset, rightInset, bottomInset,
				leftInset);
	}

	@Override
	protected double computeMinHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		// TODO Auto-generated method stub
		return super.computeMinHeight(width, topInset, rightInset, bottomInset,
				leftInset);
	}

	@Override
	protected double computePrefWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		// TODO Auto-generated method stub
		return super.computePrefWidth(height, topInset, rightInset, bottomInset,
				leftInset);
	}

	@Override
	protected double computePrefHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		// TODO Auto-generated method stub
		return super.computePrefHeight(width, topInset, rightInset, bottomInset,
				leftInset);
	}

	@Override
	protected double computeMaxWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		// TODO Auto-generated method stub
		return super.computeMaxWidth(height, topInset, rightInset, bottomInset,
				leftInset);
	}

	@Override
	protected double computeMaxHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		// TODO Auto-generated method stub
		return super.computeMaxHeight(width, topInset, rightInset, bottomInset,
				leftInset);
	}

	@Override
	protected double snappedTopInset() {
		// TODO Auto-generated method stub
		return super.snappedTopInset();
	}

	@Override
	protected double snappedBottomInset() {
		// TODO Auto-generated method stub
		return super.snappedBottomInset();
	}

	@Override
	protected double snappedLeftInset() {
		// TODO Auto-generated method stub
		return super.snappedLeftInset();
	}

	@Override
	protected double snappedRightInset() {
		// TODO Auto-generated method stub
		return super.snappedRightInset();
	}

	@Override
	protected double snapSpace(double value) {
		// TODO Auto-generated method stub
		return super.snapSpace(value);
	}

	@Override
	protected double snapSize(double value) {
		// TODO Auto-generated method stub
		return super.snapSize(value);
	}

	@Override
	protected double snapPosition(double value) {
		// TODO Auto-generated method stub
		return super.snapPosition(value);
	}

	@Override
	protected void positionInArea(Node child, double areaX, double areaY,
			double areaWidth, double areaHeight, double areaBaselineOffset,
			HPos halignment, VPos valignment) {
		// TODO Auto-generated method stub
		super.positionInArea(child, areaX, areaY, areaWidth, areaHeight,
				areaBaselineOffset, halignment, valignment);
	}

	@Override
	protected void positionInArea(Node child, double areaX, double areaY,
			double areaWidth, double areaHeight, double areaBaselineOffset,
			Insets margin, HPos halignment, VPos valignment) {
		// TODO Auto-generated method stub
		super.positionInArea(child, areaX, areaY, areaWidth, areaHeight,
				areaBaselineOffset, margin, halignment, valignment);
	}

	@Override
	protected void layoutInArea(Node child, double areaX, double areaY,
			double areaWidth, double areaHeight, double areaBaselineOffset,
			HPos halignment, VPos valignment) {
		// TODO Auto-generated method stub
		super.layoutInArea(child, areaX, areaY, areaWidth, areaHeight,
				areaBaselineOffset, halignment, valignment);
	}

	@Override
	protected void layoutInArea(Node child, double areaX, double areaY,
			double areaWidth, double areaHeight, double areaBaselineOffset,
			Insets margin, HPos halignment, VPos valignment) {
		// TODO Auto-generated method stub
		super.layoutInArea(child, areaX, areaY, areaWidth, areaHeight,
				areaBaselineOffset, margin, halignment, valignment);
	}

	@Override
	protected void layoutInArea(Node child, double areaX, double areaY,
			double areaWidth, double areaHeight, double areaBaselineOffset,
			Insets margin, boolean fillWidth, boolean fillHeight,
			HPos halignment, VPos valignment) {
		// TODO Auto-generated method stub
		super.layoutInArea(child, areaX, areaY, areaWidth, areaHeight,
				areaBaselineOffset, margin, fillWidth, fillHeight, halignment,
				valignment);
	}

	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		// TODO Auto-generated method stub
		return super.getCssMetaData();
	}
	
}
