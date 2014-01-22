/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import javafx.scene.control.SkinBase;

import org.devel.jfxcontrols.scene.control.RouteMapView;

/**
 * Skin, that allows a proportion of 16:9 and defines some default values.
 * 
 * @author stefan.illgen
 * 
 */
public class MapWebViewSkin extends SkinBase<RouteMapView> {

	public MapWebViewSkin(RouteMapView control) {
		super(control);
	}

	@Override
	protected double computeMinWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		if (height < 0) {
			return Double.MIN_VALUE;
		} else
			return (height / 9) * 16;
	}

	@Override
	protected double computeMinHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		if (width < 0) {
			return Double.MIN_VALUE;
		} else
			return (width / 16) * 9;
	}

	@Override
	protected double computePrefWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		if (height < 0) {
			return 320;
		} else
			return (height / 9) * 16;
	}

	@Override
	protected double computePrefHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		if (width < 0) {
			return 180;
		} else
			return (width / 16) * 9;
	}

	@Override
	protected double computeMaxWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		if (height < 0) {
			return Double.MAX_VALUE;
		} else
			return (height / 9) * 16;
	}

	@Override
	protected double computeMaxHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		if (width < 0) {
			return Double.MAX_VALUE;
		} else
			return (width / 16) * 9;
	}

}
