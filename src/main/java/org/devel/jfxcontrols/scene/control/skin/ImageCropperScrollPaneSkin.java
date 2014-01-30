/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import org.devel.jfxcontrols.scene.control.ImageCropperScrollPane;

import com.sun.javafx.scene.control.skin.ScrollPaneSkin;

/**
 * @author stefan.illgen
 *
 */
public class ImageCropperScrollPaneSkin extends ScrollPaneSkin {

	public ImageCropperScrollPaneSkin(ImageCropperScrollPane control) {
		super(control);
	}

	@Override
	protected double computeMinWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		
		return super.computeMinWidth(height, topInset, rightInset, bottomInset,
				leftInset);
	}

	@Override
	protected double computeMinHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		
		return super.computeMinHeight(width, topInset, rightInset, bottomInset,
				leftInset);
	}
	
	

}
