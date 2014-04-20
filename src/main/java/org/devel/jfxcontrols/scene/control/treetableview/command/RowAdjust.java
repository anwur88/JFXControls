/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.command;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.IndexedCell;
import javafx.util.Duration;

/**
 * @author stefan.illgen
 *
 */
public class RowAdjust<T, I extends IndexedCell<T>> extends Transition
	implements
		Command<RowAdjust.Action, Adjustable<T, I>> {

	private static Duration DURATION_SCROLL_ADJUST_AFTER_DRAG = Duration.millis(300);
	private static Interpolator STRONG_EASE_OUT = Interpolator.SPLINE(0.5, 0.9, 0.9, 0.95);

	private double totalYAdjustForAnimation;
	private double alreadyYAdjustForAnimation;
	private double currentMouseY;
	// private VelocityTracker velocityTracker;
	private boolean dragging;
	private final Adjustable<T, I> receiver;

	public enum Action implements Command.Action<RowAdjust.Action> {
		PRESS, DRAG, RELEASE, CONSUME;

		private double y = 0.0d;;
		private boolean animate;

		@Override
		public Action animate(boolean animate) {
			this.animate = animate;
			return this;
		}

		@Override
		public boolean animate() {
			return animate;
		}

		public Action y(double y) {
			this.y = y;
			return this;
		}

		public double y() {
			return y;
		}
	}

	public RowAdjust(Adjustable<T, I> receiver) {
		super();
		this.receiver = receiver;
		setCycleDuration(DURATION_SCROLL_ADJUST_AFTER_DRAG);
		setInterpolator(STRONG_EASE_OUT);
	}

	@Override
	public boolean execute(RowAdjust.Action action) {

		switch (action) {

		case PRESS:

			stop();
			dragging = false;
			currentMouseY = action.y();
			// getVelocityTracker().clear();
			// getVelocityTracker().addPoint(0.0f,
			// (float) currentMouseY,
			// System.currentTimeMillis());
			return false;

		case DRAG:
			dragging = true;
			double deltaY = currentMouseY - action.y();
			if (deltaY != 0) {
				currentMouseY = action.y();
				receiver.adjustPixels(deltaY);
				// getVelocityTracker().addPoint(0.0f,
				// (float) currentMouseY,
				// System.currentTimeMillis());
			}
			return false;

		case RELEASE:
			if (dragging) {
				adjustDiff(action.animate());
				dragging = false;
				return false;
			}
			break;

		case CONSUME:
			return false;
		}

		return true;
	}

	private double adjustDiff(boolean animate) {
		if (animate) {
			double absDelta2 = receiver.getEntireCellDelta();
			double absDelta = absDelta2;
			if (absDelta != 0) {
				stop();
				totalYAdjustForAnimation = absDelta;
				alreadyYAdjustForAnimation = 0;
				play();
			}
		} else {
			return receiver.adjustEntireCellDelta();
		}
		return 0;
	}

	@Override
	protected void interpolate(double frac) {

		if (frac >= 1.0) {
			receiver.adjustEntireCellDelta();
			totalYAdjustForAnimation = 0;
			alreadyYAdjustForAnimation = 0;
		} else if (frac > 0.0) {
			double currentYAdjust = totalYAdjustForAnimation * frac;
			if (currentYAdjust != alreadyYAdjustForAnimation) {
				receiver.adjustPixels(currentYAdjust - alreadyYAdjustForAnimation);
				alreadyYAdjustForAnimation = currentYAdjust;
			}
		}

	}

	// private VelocityTracker getVelocityTracker() {
	// if (velocityTracker == null)
	// velocityTracker = new VelocityTracker();
	// return velocityTracker;
	// }

	@Override
	public Adjustable<T, I> getReceiver() {
		return receiver;
	}

}
