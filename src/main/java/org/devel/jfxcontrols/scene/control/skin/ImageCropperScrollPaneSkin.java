/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import javafx.scene.control.ScrollPane;

import com.sun.javafx.scene.control.skin.ScrollPaneSkin;

/**
 * @author stefan.illgen
 *
 */
@SuppressWarnings("restriction")
public class ImageCropperScrollPaneSkin extends ScrollPaneSkin {

	public ImageCropperScrollPaneSkin(ScrollPane scrollpane) {
		super(scrollpane);
	}

	@Override
	protected double computeMaxWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		return super.computeMaxWidth(height, topInset, rightInset, bottomInset,
				leftInset) + calculateHorizontalDimDiff();
	}

	@Override
	protected double computeMaxHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		return super.computeMaxHeight(width, topInset, rightInset, bottomInset,
				leftInset) + calculateVerticalDimDiff();
	}

	private double calculateVerticalDimDiff() {
		return getSkinnable().getWidth()
				- getSkinnable().getViewportBounds().getWidth();
	}
	
	private double calculateHorizontalDimDiff() {
		return getSkinnable().getBoundsInParent().getHeight()
				- getSkinnable().getViewportBounds()
						.getHeight();
	}	

}
