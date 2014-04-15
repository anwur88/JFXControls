/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import java.util.HashMap;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.IndexedCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import org.devel.jfxcontrols.scene.control.skin.animation.EntireRowAdjuster;
import org.devel.jfxcontrols.scene.control.skin.animation.VelocityTracker;

/**
 * @author stefan.illgen
 *
 */
@SuppressWarnings({
	"unchecked", "rawtypes"
})
public class FlowAdjuster<M, I extends IndexedCell<M>> extends FlowExtension<M, I> {

	private EntireRowAdjuster<M, I> adjuster;
	private VelocityTracker velocityTracker;
	private double dragY;

	public FlowAdjuster(ExtensibleFlow<M, I> extensibleFlow) {
		super(extensibleFlow);
	}

	public EntireRowAdjuster<M, I> getAdjuster() {
		if (adjuster == null) {
			adjuster = new EntireRowAdjuster<>((value) -> {
				getExtensibleFlow().adjustPixels(-value);
			});
		}

		return adjuster;
	}

	public void setAdjuster(EntireRowAdjuster<M, I> adjuster) {
		this.adjuster = adjuster;
	}

	@Override
	public Map<EventType, EventHandler> createTypedEventHandlers() {
		return new HashMap<EventType, EventHandler>() {
			private static final long serialVersionUID = -7707743962676354987L;
			{

			}
		};
	}

	@Override
	public Map<EventType, EventHandler> createTypedEventFilters() {
		return new HashMap<EventType, EventHandler>() {
			private static final long serialVersionUID = -3419029735075202493L;
			{
				put(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						getAdjuster().stop();
						dragY = event.getY();
						getVelocityTracker().clear();
						getVelocityTracker().addPoint(0.0f,
													  (float) dragY,
													  System.currentTimeMillis());
						event.consume();
					}
				});
				put(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						double yDelta = dragY - event.getY();
						// if (yDelta != 0.0) {
						// if (getExtensibleFlow() != null) {
						System.out.println("Flow Adjuster Mouse Dragged: " + yDelta);
						adjustDiff(yDelta, false);
						// getExtensibleFlow().adjustPixels(yDelta);
						// }
						dragY = event.getY();
						getVelocityTracker().addPoint(0.0f,
													  (float) dragY,
													  System.currentTimeMillis());
						// }
					}
				});

				put(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						double computeDiff2EntireFirstCell = computeDiff2EntireFirstCell();
						System.out.println("Flow Adjuster Mouse Released: "
							+ computeDiff2EntireFirstCell);
						adjustDiff(computeDiff2EntireFirstCell, true);
					}
				});

				// put(MouseEvent.MOUSE_RELEASED, (event) -> {
				// event.consume();
				// // adjustEntireRow(true);
				// });
				put(MouseEvent.MOUSE_MOVED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_ENTERED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_EXITED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_ENTERED_TARGET, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_EXITED_TARGET, (event) -> {
					event.consume();
				});
				put(ScrollEvent.ANY, (event) -> {
					event.consume();
				});
				put(KeyEvent.ANY, (event) -> {
					event.consume();
				});
			}
		};
	}

	public void adjustEntireRow(boolean animate) {
		adjustDiff(getFullRowDistanceAddition(), animate);
	}

	public void adjustTop(I cell, boolean animate) {
		double diff = -(cell.getLayoutY() - getExtensibleFlow().getPosition());
		if (diff != 0) {
			adjustDiff(diff, animate);
		}
	}

	public void adjustDiff(double diff, boolean animate) {
		System.out.println(diff);
		if (animate) {
			getAdjuster().adjust(diff);
		} else {
			getExtensibleFlow().adjustPixels(diff);
		}
	}

	public double getFullRowDistanceAddition() {
		double currentPosition = getCurrentPosition();
		double normalizedPosition = currentPosition % getFixedCellSize();
		return isScrollDown()
			? (normalizedPosition)
			: -(getFixedCellSize() - normalizedPosition);
	}

	public void setVelocityTracker(VelocityTracker velocityTracker) {
		this.velocityTracker = velocityTracker;
	}

	public VelocityTracker getVelocityTracker() {
		if (velocityTracker == null)
			velocityTracker = new VelocityTracker();
		return velocityTracker;
	}

	public boolean isScrollDown() {
		return getVelocityTracker().getYVelocity() < 0.0;
	}

	public double getCurrentPosition() {
		double maxViewHeight = getRowCount() * getFixedCellSize();
		double maxPositionDistance = maxViewHeight - getExtensibleFlow().getHeight();
		double currentPosition = (getExtensibleFlow().getPosition())
			* maxPositionDistance;
		return currentPosition;
	}

	public float getYVelocity() {
		getVelocityTracker().computeCurrentVelocity(1000);
		return getVelocityTracker().getYVelocity();
	}

}
