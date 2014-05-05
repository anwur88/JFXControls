/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.command;

import javafx.animation.Transition;
import javafx.scene.control.IndexedCell;

/**
 * 
 * @author stefan.illgen
 *
 * @param <T>
 *            Shared Type of Expandable and Adjustable
 * @param <E>
 *            Expandable
 * @param <A>
 *            Adjustable
 */
public class Expand<T, A extends IndexedCell<T>> extends Transition
	implements
		Command<Expand.Action, Expandable<T>> {

	public enum Action implements Command.Action<Expand.Action> {
		CONSUME, EXPAND, NONE;

		private double y;
		private boolean animate;
		private Object source;
		private Object target;
		private int count;

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

		public Object getSource() {
			return source;
		}

		public Action source(Object source) {
			this.source = source;
			return this;
		}

		public Object getTarget() {
			return target;
		}

		public Action target(Object target) {
			this.target = target;
			return this;
		}

		public int getCount() {
			return count;
		}

		public Action count(int count) {
			this.count = count;
			return this;
		}

	}

	private Expandable<T> expandable;
	private RowAdjust<T, A> rowAdjust;

	/**
	 * @param rowAdjust
	 * 
	 */
	public Expand(Expandable<T> receiver, RowAdjust<T, A> rowAdjust) {
		this.expandable = receiver;
		this.rowAdjust = rowAdjust;
	}

	public Expand(RowAdjust<T, A> rowAdjust) {
		this(null, rowAdjust);
	}

	@Override
	protected void interpolate(double frac) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean execute(Action action) {

		if (expandable != null) {

			switch (action) {

			case EXPAND:
				System.out.println("expands clicked");
				if (action.getCount() == 1) {
					if (!rowAdjust.isDragging()) {
						rowAdjust.execute(RowAdjust.Action.INIT_LAYOUT_CHANGE);
						int selectedRowIndex = expandable.expand();
						rowAdjust.execute(RowAdjust.Action.ADJUST_ROW_INDEX.rowIndex(selectedRowIndex)
																		   .length(expandable.getLength()));
						// rowAdjust.execute(RowAdjust.Action.ADJUST_DELTA.delta(delta));
					}
				}
				return false;

			case CONSUME:
				return false;

			case NONE:
				return true;
			}

		}

		return true;
	}

	@Override
	public Expandable<T> getReceiver() {
		return expandable;
	}

	@Override
	public void setReceiver(Expandable<T> receiver) {
		expandable = receiver;
	}

}
