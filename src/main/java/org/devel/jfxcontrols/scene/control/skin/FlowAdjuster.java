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

import com.sun.javafx.scene.control.skin.VirtualFlow;

/**
 * @author stefan.illgen
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FlowAdjuster<M, I extends IndexedCell<M>> extends
		FlowExtension<M, I> {

	private EntireRowAdjuster<M, I> adjuster;
	private VelocityTracker velocityTracker;
	private double pressedY;
	private double draggedY;

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
						draggedY = pressedY = event.getY();
						getVelocityTracker().clear();
						getVelocityTracker().addPoint(0.0f, (float) draggedY,
								System.currentTimeMillis());
						event.consume();
					}
				});
				put(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						double deltaY = draggedY - event.getY();
						// if (yDelta != 0.0) {
						// if (getExtensibleFlow() != null) {
						adjustDiff(deltaY, false);
						draggedY = event.getY();
						getVelocityTracker().addPoint(0.0f, (float) draggedY,
								System.currentTimeMillis());
						// }
					}
				});

				put(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						adjustDiff(computeDiffEntireRow(), false);
					}
				});

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

	/**
	 * Adjusts the flow on entire cell sizes (i.e. row height or column width).
	 * If the dragged distance for one cell is lower than the half of the cells
	 * size, the the {@link VirtualFlow} is adjusted to show the lower cell. If
	 * it is larger than the half size, the flow gets adjusted on the next cell.
	 * 
	 * @param animate
	 */
	public void adjustEntireRow(boolean animate) {
		adjustDiff(computeDiffEntireRow(), animate);
	}

	/*
	 * Computes the difference needed for adjusting the {@link VirtualFlow} on
	 * entire cell sizes (i.e. row height or column width). If the dragged
	 * distance for one cell is lower than the half of the cells size, the
	 * returned distance will adjust the {@link VirtualFlow} to show the lower
	 * cell. If it is larger than the half size, the returned distance is meant
	 * for summing it up to adjust on the next cell.
	 * 
	 * @return the difference needed for adjusting the {@link VirtualFlow} on
	 * entire cell sizes
	 */
	private double computeDiffEntireRow() {
		double totalDeltaY = -(draggedY - pressedY);
		double rowDeltaY = totalDeltaY % getFixedCellSize();
		double maxY = getFixedCellSize();
		double computeDiff2EntireFirstCell = rowDeltaY > 0 ? (Math
				.abs(rowDeltaY) < maxY / 2 ? -rowDeltaY : maxY - rowDeltaY)
				: (Math.abs(rowDeltaY) < maxY / 2 ? -rowDeltaY
						: -(maxY + rowDeltaY));
		return computeDiff2EntireFirstCell;
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
		return isScrollDown() ? (normalizedPosition)
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
		double maxPositionDistance = maxViewHeight
				- getExtensibleFlow().getHeight();
		double currentPosition = (getExtensibleFlow().getPosition())
				* maxPositionDistance;
		return currentPosition;
	}

	public float getYVelocity() {
		getVelocityTracker().computeCurrentVelocity(1000);
		return getVelocityTracker().getYVelocity();
	}

}
