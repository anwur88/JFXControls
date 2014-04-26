/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.command;

import javafx.animation.Transition;

/**
 * @author stefan.illgen
 *
 */
public class Fling extends Transition implements Command<Fling.Action, Flingable> {

	public enum Action implements Command.Action<Fling.Action> {
		PRESS, DRAG, RELEASE;

		private double y;
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

		@Override
		public Action y(double y) {
			this.y = y;
			return this;
		}

		@Override
		public double y() {
			return y;
		}

	}

	private Flingable flingable;

	public Fling(Flingable flingable) {
		this.flingable = flingable;
	}

	@Override
	protected void interpolate(double frac) {

	}

	@Override
	public boolean execute(Fling.Action action) {
		return true;
	}

	@Override
	public Flingable getReceiver() {
		return flingable;
	}

	@Override
	public void setReceiver(Flingable receiver) {
		// TODO Auto-generated method stub

	}

}
