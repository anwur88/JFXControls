/**
 * 
 */
package org.devel.jfxcontrols.animation;

import javafx.animation.Transition;

/**
 * @author stefan.illgen
 *
 */
public class Fling extends Transition implements Command<Fling.Action, Flingable> {

	public enum Action implements Command.Action<Fling.Action> {
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

	private Flingable flingable;

	/**
	 * 
	 */
	public Fling(Flingable flingable) {
		this.flingable = flingable;
	}

	/*
	 * (non-Javadoc)
	 * @see javafx.animation.Transition#interpolate(double)
	 */
	@Override
	protected void interpolate(double frac) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean execute(Fling.Action action) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Flingable getReceiver() {
		return flingable;
	}

}
