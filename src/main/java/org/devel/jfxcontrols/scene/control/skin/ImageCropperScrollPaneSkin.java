/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;

import org.devel.jfxcontrols.scene.control.ImageCropperScrollPane;

import com.sun.javafx.scene.control.skin.ScrollPaneSkin;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropperScrollPaneSkin extends ScrollPaneSkin {

	public ImageCropperScrollPaneSkin(ImageCropperScrollPane scrollpane) {
		super(scrollpane);
	}

	@Override
	protected double computeMaxWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {

		if (((ImageCropperScrollPane) getSkinnable())
				.maxWidthObservablesProperty().size() != 0) {
			return getMaxValue(((ImageCropperScrollPane) getSkinnable())
					.maxWidthObservablesProperty())
					+ calculateHorizontalDimDiff();
		} else
			return super.computeMaxWidth(height, topInset, rightInset,
					bottomInset, leftInset) + calculateHorizontalDimDiff();
	}

	@Override
	protected double computeMaxHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {

		if (((ImageCropperScrollPane) getSkinnable())
				.maxHeightObservablesProperty().size() != 0) {
			return getMaxValue(((ImageCropperScrollPane) getSkinnable())
					.maxHeightObservablesProperty())
					+ calculateVerticalDimDiff();
		} else
			return super.computeMaxHeight(width, topInset, rightInset,
					bottomInset, leftInset);
	}

	private double calculateVerticalDimDiff() {
		return getSkinnable().getWidth()
				- getSkinnable().getViewportBounds().getWidth();
	}

	private double calculateHorizontalDimDiff() {
		return getSkinnable().getBoundsInParent().getHeight()
				- getSkinnable().getViewportBounds().getHeight();
	}

	private double getMaxValue(
			ObservableList<ReadOnlyDoubleProperty> observableList) {
		double result = 0.0;
		for (ReadOnlyDoubleProperty prop : observableList)
			result = (prop.get() > result) ? prop.get() : result;
		return result;
	}

}
