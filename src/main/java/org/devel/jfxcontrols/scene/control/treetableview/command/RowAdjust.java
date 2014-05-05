/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.command;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.ReadOnlyBooleanProperty;
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
	private int selectedIndex = -1;

	public enum Action implements Command.Action<RowAdjust.Action> {
		INIT,
		DRAG,
		ADJUST_AFTER_DRAG,
		CONSUME,
		ADJUST_ROW_INDEX,
		INIT_LAYOUT_CHANGE,
		ADJUST_DELTA;

		private double y = 0.0d;;
		private boolean animate;
		private double delta;
		private int rowIndex;
		private ReadOnlyBooleanProperty needsLayout;
		private double length;

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

		public double delta() {
			return delta;
		}

		public RowAdjust.Action delta(double delta) {
			this.delta = delta;
			return this;
		}

		public int rowIndex() {
			return rowIndex;
		}

		public RowAdjust.Action rowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
			return this;
		}

		public RowAdjust.Action needsLayoutProperty(ReadOnlyBooleanProperty needsLayout) {
			this.needsLayout = needsLayout;
			return this;
		}

		public ReadOnlyBooleanProperty needsLayoutProperty() {
			return needsLayout;
		}

		public double length() {
			return length;
		}

		public RowAdjust.Action length(double length) {
			this.length = length;
			return this;
		}
	}

	public RowAdjust(Adjustable<T, I> adjustable) {
		this();
		if (adjustable == null) {
			throw new IllegalArgumentException("Parameter adjstable must not be null.");
		}
		setReceiver(adjustable);
	}

	public RowAdjust() {
		super();
		setCycleDuration(DURATION_SCROLL_ADJUST_AFTER_DRAG);
		setInterpolator(STRONG_EASE_OUT);
	}

	@Override
	public boolean execute(RowAdjust.Action action) {

		if (adjustable != null) {

			switch (action) {

			case INIT:

				stop();
				currentMouseY = action.y();
				dragging = false;
				// getVelocityTracker().clear();
				// getVelocityTracker().addPoint(0.0f,
				// (float) currentMouseY,
				// System.currentTimeMillis());
				// return false;
				break;

			case DRAG:
				double deltaY = currentMouseY - action.y();
				if (Math.abs(deltaY) > 5) {
					dragging = true;
					currentMouseY = action.y();
					adjustable.adjustPixels(deltaY);
					// getVelocityTracker().addPoint(0.0f,
					// (float) currentMouseY,
					// System.currentTimeMillis());
				}
				break;
			case ADJUST_AFTER_DRAG:
				if (dragging) {
					System.out.println("view relarrrrse");
					adjustDiff(action.animate());

					// return false;
				}
				break;

			case INIT_LAYOUT_CHANGE:
				intialFlowPosition = adjustable.getAbsPosition()
					- (adjustable.getAbsPosition() % adjustable.getFixedCellLength());
				break;

			case ADJUST_ROW_INDEX:
				adjustable.layoutAdjustPixels(action.rowIndex(), action.length());
				return false;

			case CONSUME:
				return false;
			case ADJUST_DELTA:
				adjustable.adjustPixels(action.delta());
				break;
			default:
				break;
			}

		} else {
			System.out.println("Adjustable must be set!");
		}

		return true;
	}

	private double adjustDiff(boolean animate) {
		if (animate) {
			double absDelta = adjustable.computeEntireCellDelta();
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
	public void setReceiver(Adjustable<T, I> adjustable) {
		this.adjustable = adjustable;
	}

	public boolean isDragging() {
		return dragging;
	}

}
