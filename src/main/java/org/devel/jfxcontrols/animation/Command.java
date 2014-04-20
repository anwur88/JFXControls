package org.devel.jfxcontrols.animation;

public interface Command<A extends Command.Action<A>, R extends Receiver> {

	public interface Action<A> {

		A animate(boolean animate);

		default boolean animate() {
			return false;
		}
	}

	/**
	 * 
	 * @param event
	 * @param receiver
	 * @return true, if subsequent actions may be performed by chained commands,
	 *         or false if no further action handling is required.
	 */
	boolean execute(A action);

	R getReceiver();

}
