package org.devel.jfxcontrols.scene.control.treetableview.command;

import java.util.ArrayList;
import java.util.List;

public interface Command<A extends Command.Action<A>, R extends Receiver> {

	public interface Action<A> {

		A animate(boolean animate);

		default boolean animate() {
			return false;
		}

		A y(double y);

		double y();
	}

	/**
	 * 
	 * @param event
	 * @param receiver
	 * @return true, if subsequent actions may be performed by chained commands,
	 *         or false if no further action handling is required.
	 */
	boolean execute(A action);

	/**
	 * 
	 * @return
	 */
	R getReceiver();

	/**
	 * 
	 * @return
	 */
	default <AC extends Command.Action<AC>, RC extends Receiver>
		List<Command<AC, RC>>
		getChildren() {
		return new ArrayList<Command<AC, RC>>() {
			private static final long serialVersionUID = -1626235262358427044L;
			{
			}
		};
	}

}
