/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.IndexedCell;
import javafx.util.Duration;

/**
 * @author stefan.illgen
 *
 */
public class EntireRowAdjuster<M, I extends IndexedCell<M>> extends Transition {

	private static Duration DURATION_SCROLL_ADJUST_AFTER_DRAG = Duration.millis(300);
	private static Interpolator STRONG_EASE_OUT = Interpolator.SPLINE(0.5, 0.9, 0.9, 0.95);
	private double totalYAdjustForAnimation;
	private double alreadyYAdjustForAnimation;
	private EntireRowAdjustReceiver receiver;

	/**
	 * 
	 */
	public EntireRowAdjuster(EntireRowAdjustReceiver receiver) {
		this.receiver = receiver;
		init();
	}

	private void init() {
		setCycleDuration(DURATION_SCROLL_ADJUST_AFTER_DRAG);
		setInterpolator(STRONG_EASE_OUT);
	}

	public void adjust(double yDelta) {

		if (yDelta != 0) {

			stop();

			totalYAdjustForAnimation = yDelta;
			alreadyYAdjustForAnimation = 0;

			play();

		}

	}

	/*
	 * (non-Javadoc)
	 * @see javafx.animation.Transition#interpolate(double)
	 */
	@Override
	protected void interpolate(double frac) {

		if (frac >= 1.0) {
			if (totalYAdjustForAnimation != alreadyYAdjustForAnimation) {
				receiver.adjust(totalYAdjustForAnimation - alreadyYAdjustForAnimation);
			}
			totalYAdjustForAnimation = 0;
			alreadyYAdjustForAnimation = 0;
		} else if (frac > 0.0) {
			double currentYAdjust = Math.round(totalYAdjustForAnimation * frac);
			if (currentYAdjust != alreadyYAdjustForAnimation) {
				receiver.adjust(currentYAdjust - alreadyYAdjustForAnimation);
				alreadyYAdjustForAnimation = currentYAdjust;
			}
		}
	}

}
