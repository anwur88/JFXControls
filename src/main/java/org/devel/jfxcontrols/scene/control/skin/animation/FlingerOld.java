package org.devel.jfxcontrols.scene.control.skin.animation;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class FlingerOld {

	private final static double DECCELERATION = 1000;
	private Animation flingAnimation;

	public FlingerOld() {
		super();
	}

	public void stopAnimation() {
		if (flingAnimation != null) {
			flingAnimation.stop();
		}
	}

	public boolean isFlinging() {
		return flingAnimation == null ? false
				: (flingAnimation.getStatus() == Status.RUNNING ? true : false);
	}

	public void fling(double velocity, double distanceFactor,
			FlingReceiver receiver, EventHandler<ActionEvent> onFinishHandler) {
		fling(velocity, distanceFactor, receiver, null, onFinishHandler);
	}

	public void fling(double velocity, double distanceFactor,
			FlingReceiver receiver, MaxDistanceAdjuster maxDistanceAdjuster,
			EventHandler<ActionEvent> onFinishHandler) {
		stopAnimation();
		double acceleration = Math.signum(velocity) * DECCELERATION;
		double t = velocity / acceleration;
		double dmax = -1.0 * acceleration / 2.0 * (t) * (t) + velocity * (t);

		double dmaxAdjusted;
		if (maxDistanceAdjuster != null) {
			dmaxAdjusted = maxDistanceAdjuster.adjustMaxDistance(dmax,
					distanceFactor);
		} else {
			dmaxAdjusted = dmax;
		}
		double velocityAdjusted = 2 * dmaxAdjusted / t;
		double accelerationAdjusted = 2 * dmaxAdjusted / (t * t);
		flingAnimation = new Transition() {
			private double angleAlreadyAdjusted = 0;
			private final double maxAngle = dmaxAdjusted * distanceFactor;
			{
				setCycleDuration(Duration.millis(t * 1000));
				setInterpolator(Interpolator.LINEAR);
			}

			@Override
			protected void interpolate(double frac) {
				double timePassedSeconds = getCycleDuration().toSeconds()
						* frac;
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
		};
		flingAnimation.setOnFinished(onFinishHandler);
		flingAnimation.play();
	}

}
