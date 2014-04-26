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
	private Adjustable<T, I> adjustable;
	private double intialFlowPosition;
	private int intialFlowTotalHeight;

	public enum Action implements Command.Action<RowAdjust.Action> {
		INIT,
		DRAG,
		ADJUST_AFTER_DRAG,
		CONSUME,
		CONSUME_CLICKED,
		ADJUST_AFTER_LAYOUT_CHANGE,
		INIT_LAYOUT_CHANGE;

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

	public RowAdjust(Adjustable<T, I> adjustable) {
		super();
		this.adjustable = adjustable;
		setCycleDuration(DURATION_SCROLL_ADJUST_AFTER_DRAG);
		setInterpolator(STRONG_EASE_OUT);
	}

	public RowAdjust() {
		this(null);
	}

	@Override
	public boolean execute(RowAdjust.Action action) {

		if (adjustable != null) {

			switch (action) {

			case INIT:

				stop();
				dragging = false;
				currentMouseY = action.y();
				// getVelocityTracker().clear();
				// getVelocityTracker().addPoint(0.0f,
				// (float) currentMouseY,
				// System.currentTimeMillis());
				// return false;
				break;

			case DRAG:
				double deltaY = currentMouseY - action.y();
				if (deltaY != 0) {
					dragging = true;
					currentMouseY = action.y();
					adjustable.adjustPixels(deltaY);
					// getVelocityTracker().addPoint(0.0f,
					// (float) currentMouseY,
					// System.currentTimeMillis());
				}
				// return false;
				break;
			case ADJUST_AFTER_DRAG:
				if (dragging) {
					System.out.println("view relarrrrse");
					adjustDiff(action.animate());
					// return false;
				}
				break;

			case INIT_LAYOUT_CHANGE:
				intialFlowPosition = adjustable.getAbsPosition();
				intialFlowTotalHeight = adjustable.getTotalHeight();
				break;

			case ADJUST_AFTER_LAYOUT_CHANGE:
				// adjustable.layoutAdjustPixels(intialFlowPosition);
				break;

			case CONSUME_CLICKED:
				if (dragging) {
					dragging = false;
					System.out.println("cl cons");
					return false;
				}
				break;

			case CONSUME:
				return false;
			}

		} else {
			System.out.println("Adjustable must be set!");
		}

		return true;
	}

	private double adjustDiff(boolean animate) {
		if (animate) {
			double absDelta = adjustable.getEntireCellDelta();
			if (absDelta != 0) {
				stop();
				totalYAdjustForAnimation = absDelta;
				alreadyYAdjustForAnimation = 0;
				play();
			}
		} else {
			return adjustable.adjustEntireCellDelta();
		}
		return 0;
	}

	@Override
	protected void interpolate(double frac) {

		if (frac >= 1.0) {
			adjustable.adjustEntireCellDelta();
			totalYAdjustForAnimation = 0;
			alreadyYAdjustForAnimation = 0;
		} else if (frac > 0.0) {
			double currentYAdjust = totalYAdjustForAnimation * frac;
			if (currentYAdjust != alreadyYAdjustForAnimation) {
				adjustable.adjustPixels(currentYAdjust - alreadyYAdjustForAnimation);
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
		return adjustable;
	}

	@Override
	public void setReceiver(Adjustable<T, I> receiver) {
		this.adjustable = receiver;

	}

}
