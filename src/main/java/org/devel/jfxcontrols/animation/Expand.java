/**
 * 
 */
package org.devel.jfxcontrols.animation;

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
