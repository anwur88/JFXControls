/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.command;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.IndexedCell;
import javafx.util.Duration;

import org.devel.jfxcontrols.scene.control.skin.animation.MaxDistanceAdjuster;

/**
 * @author stefan.illgen
 *
 */
public class Fling<T, A extends IndexedCell<T>> extends Transition
	implements
		Command<Fling.Action, Flingable> {

	private final static double DECCELERATION = 1000;

	public enum Action implements Command.Action<Fling.Action> {
		PRESS, DRAG, FLING;

		private double y;
		private boolean animate;
		public double velocity;

		@Override
		public Action animate(boolean animate) {
			this.animate = animate;
			return this;
		}

		@Override
		public boolean animate() {
			return animate;
		}

		@Override
		public Action y(double y) {
			this.y = y;
			return this;
		}

		@Override
		public double y() {
			return y;
		}

		public Action velocity(double velocity) {
			this.velocity = velocity;
			return this;
		}

		public double velocity() {
			return velocity;
		}

	}

	private Flingable flingable;
	private double distanceFactor;
	private MaxDistanceAdjuster maxDistanceAdjuster;
	private EventHandler<ActionEvent> onFinishHandler;
	private int angleAlreadyAdjusted;
	private double velocityAdjusted;
	private double accelerationAdjusted;
	private double maxAngle;
	private RowAdjust<T, A> rowAdjust;

	public Fling(Flingable flingable,
		double distanceFactor,
		MaxDistanceAdjuster maxDistanceAdjuster,
		EventHandler<ActionEvent> onFinishHandler,
		RowAdjust<T, A> rowAdjust) {
		this.distanceFactor = distanceFactor;
		this.flingable = flingable;
		this.maxDistanceAdjuster = maxDistanceAdjuster;
		this.onFinishHandler = onFinishHandler;
		this.rowAdjust = rowAdjust;
	}

	@Override
	public boolean execute(Fling.Action action) {

		if (flingable != null) {

			switch (action) {
			case FLING:
				fling(action.velocity());
				break;

			default:
				break;
			}

		}

		return true;
	}

	public void fling(double velocity) {
		stop();
		angleAlreadyAdjusted = 0;
		double acceleration = Math.signum(velocity) * DECCELERATION;
		double t = velocity / acceleration;
		double dmax = -1.0 * acceleration / 2.0 * (t) * (t) + velocity * (t);

		double dmaxAdjusted;
		if (maxDistanceAdjuster != null) {
			dmaxAdjusted = maxDistanceAdjuster.adjustMaxDistance(dmax, distanceFactor);
		} else {
			dmaxAdjusted = dmax;
		}
		velocityAdjusted = 2 * dmaxAdjusted / t;
		accelerationAdjusted = 2 * dmaxAdjusted / (t * t);

		setCycleDuration(Duration.millis(t * 1000));
		setInterpolator(Interpolator.LINEAR);
		maxAngle = dmaxAdjusted * distanceFactor;
		setOnFinished(onFinishHandler);
		play();
	}

	@Override
	protected void interpolate(double frac) {
		double timePassedSeconds = getCycleDuration().toSeconds() * frac;
		double distance = (velocityAdjusted * timePassedSeconds - accelerationAdjusted
			* timePassedSeconds * timePassedSeconds / 2.0)
			* distanceFactor;
		double currentAngleToAdjust = distance - angleAlreadyAdjusted;
		if (Math.abs(distance) > Math.abs(maxAngle)) {
			currentAngleToAdjust = maxAngle - angleAlreadyAdjusted;
		}
		rowAdjust.execute(RowAdjust.Action.ADJUST_DELTA.delta(currentAngleToAdjust));
		angleAlreadyAdjusted += currentAngleToAdjust;
	}

	@Override
	public Flingable getReceiver() {
		return flingable;
	}

	@Override
	public void setReceiver(Flingable receiver) {
		flingable = receiver;
	}

}
