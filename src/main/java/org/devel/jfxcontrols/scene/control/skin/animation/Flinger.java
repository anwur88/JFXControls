/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.IndexedCell;
import javafx.util.Duration;

/**
 * @author stefan.illgen
 *
 */
public class Flinger<M, I extends IndexedCell<M>> extends Transition {

	private final static double DECCELERATION = 1000;

	// private ExtensibleFlow<M, I> extensibleFlow;

	private double maxAngle;

	private double velocityAdjusted;

	private double accelerationAdjusted;

	private double velocity;

	private double distanceFactor;

	private EventHandler<ActionEvent> onFinishHandler;

	private MaxDistanceAdjuster maxDistanceAdjuster;

	private FlingReceiver receiver;

	private double angleAlreadyAdjusted;

	public Flinger(double distanceFactor,
		FlingReceiver receiver,
		MaxDistanceAdjuster maxDistanceAdjuster,
		EventHandler<ActionEvent> onFinishHandler) {
		this.distanceFactor = distanceFactor;
		this.receiver = receiver;
		this.maxDistanceAdjuster = maxDistanceAdjuster;
		this.onFinishHandler = onFinishHandler;
	}

	public Flinger() {
	}

	public boolean isFlinging() {
		return getStatus() == Status.RUNNING;
	}

	public static void fling(double velocity,
							 double distanceFactor,
							 FlingReceiver receiver,
							 EventHandler<ActionEvent> onFinishHandler) {
		fling(velocity, distanceFactor, receiver, null, onFinishHandler);
	}

	public static void fling(double velocity,
							 double distanceFactor,
							 FlingReceiver receiver,
							 MaxDistanceAdjuster maxDistanceAdjuster,
							 EventHandler<ActionEvent> onFinishHandler) {

		Flinger flinger = new Flinger(distanceFactor,
									  receiver,
									  maxDistanceAdjuster,
									  onFinishHandler);
		flinger.fling(velocity);

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

	/*
	 * (non-Javadoc)
	 * @see javafx.animation.Transition#interpolate(double)
	 */
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
		receiver.adjust(currentAngleToAdjust);
		angleAlreadyAdjusted += currentAngleToAdjust;
	}

}
