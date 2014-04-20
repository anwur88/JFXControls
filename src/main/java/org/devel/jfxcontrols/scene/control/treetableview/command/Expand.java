/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.command;

import javafx.animation.Transition;

/**
 * @author stefan.illgen
 *
 */
public class Expand<T> extends Transition
	implements
		Command<Expand.Action, Expandable<T>> {

	public enum Action implements Command.Action<Expand.Action> {
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

	private Expandable<T> expandable;

	/**
	 * 
	 */
	public Expand(Expandable<T> expandable) {
		this.expandable = expandable;
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
	public boolean execute(Action action) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Expandable<T> getReceiver() {
		return expandable;
	}

}
