/**
 * 
 */
package org.devel.jfxcontrols.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.IndexedCell;
import javafx.util.Duration;

import org.devel.jfxcontrols.scene.control.skin.animation.VelocityTracker;

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
	private double startMouseY;
	private double currentMouseY;
	private VelocityTracker velocityTracker;
	private boolean dragging;
	private final Adjustable<T, I> receiver;

	public enum Action implements Command.Action<RowAdjust.Action> {
		PRESS, DRAG, RELEASE;

		private double mouseY;
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

		public Action mouseY(double mouseY) {
			this.mouseY = mouseY;
			return this;
		}

		public double mouseY() {
			return mouseY;
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
			currentMouseY = startMouseY = action.mouseY();
			getVelocityTracker().clear();
			getVelocityTracker().addPoint(0.0f,
										  (float) currentMouseY,
										  System.currentTimeMillis());
			return false;

		case DRAG:
			dragging = true;
			double deltaY = currentMouseY - action.mouseY();
			if (deltaY != 0) {
				System.out.println("receiver.adjustPixels: "
					+ receiver.adjustPixels(deltaY));
				currentMouseY = action.mouseY();
				getVelocityTracker().addPoint(0.0f,
											  (float) currentMouseY,
											  System.currentTimeMillis());
			}
			return false;

		case RELEASE:
			if (dragging) {
				adjustDiff(action.animate());
				dragging = false;
				return false;
			}
			break;

		default:
			break;
		}

		return true;
	}

	/*
	 * Computes the difference needed for adjusting the {@link VirtualFlow} on
	 * entire cell sizes (i.e. row height or column width). If the dragged
	 * distance for one cell is lower than the half of the cells size, the
	 * returned distance will adjust the {@link VirtualFlow} to show the lower
	 * cell. If it is larger than the half size, the returned distance is meant
	 * for summing it up to adjust on the next cell.
	 * @return the difference needed for adjusting the {@link VirtualFlow} on
	 * entire cell sizes
	 */
	private double computeDiffEntireRow(Adjustable<T, I> receiver) {
		double totalDeltaY = -(currentMouseY - startMouseY);
		double rowDeltaY = totalDeltaY % receiver.getFixedCellLength();
		double maxY = receiver.getFixedCellLength();
		double computeDiff2EntireFirstCell = rowDeltaY > 0
			? (Math.abs(rowDeltaY) < maxY / 2 ? -rowDeltaY : maxY - rowDeltaY)
			: (Math.abs(rowDeltaY) < maxY / 2 ? -rowDeltaY : -(maxY + rowDeltaY));
		return computeDiff2EntireFirstCell;
	}

	private double adjustDiff(boolean animate) {
		if (animate) {
			// double absDelta1 = computeDiffEntireRow(receiver);
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
			// receiver.adjustPixels(computeDiffEntireRow(receiver));
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

	private VelocityTracker getVelocityTracker() {
		if (velocityTracker == null)
			velocityTracker = new VelocityTracker();
		return velocityTracker;
	}

	@Override
	public Adjustable<T, I> getReceiver() {
		return receiver;
	}

}
