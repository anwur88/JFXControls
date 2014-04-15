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

import org.devel.jfxcontrols.scene.control.skin.animation.Flinger;

/**
 * @author stefan.illgen
 *
 */
@SuppressWarnings({
	"unchecked", "rawtypes"
})
public class FlowFlinger<M, I extends IndexedCell<M>> extends FlowExtension<M, I> {

	private Flinger<M, I> flinger;
	private double dragY;

	public FlowFlinger(ExtensibleFlow<M, I> extensibleFlow, FlowAdjuster<M, I> adjuster) {
		super(extensibleFlow);
		// initEventHandling();
		addChildren(adjuster);
		bind(adjuster);
	}

	private void bind(FlowAdjuster<M, I> adjuster) {
		selectionModelProperty().bind(adjuster.selectionModelProperty());
		rowCountProperty().bindBidirectional(adjuster.rowCountProperty());
		fixedCellSizeProperty().bindBidirectional(adjuster.fixedCellSizeProperty());

	}

	public Flinger<M, I> getFlinger() {
		if (flinger == null)
			flinger = new Flinger<M, I>(1.0,
										(value) -> {
											getExtensibleFlow().adjustPixels(-value);
										},
										(maxDistance, distanceFactor) -> {

											boolean scrollDown = getFlowEntireRowAdjuster().isScrollDown();
											double currentPosition = getFlowEntireRowAdjuster().getCurrentPosition();
											double maxPositionDistance = getMaxViewHeight();

											double currentMaxDistance = (scrollDown)
												? -(maxPositionDistance - currentPosition)
												: currentPosition;

											// round up 2 entire rows
											double entireRowsCurrentDiffDistance = (getFixedCellSize() - (Math.abs(currentMaxDistance) % getFixedCellSize()));
											double entireRowsCurrentMaxDistance = (scrollDown)
												? currentMaxDistance
													- entireRowsCurrentDiffDistance
												: currentMaxDistance
													+ entireRowsCurrentDiffDistance;

											double entireRowsDiffDistance = (getFixedCellSize() - (Math.abs(maxDistance) % getFixedCellSize()));
											double entireRowsMaxDistance = (scrollDown)
												? maxDistance - entireRowsDiffDistance
												: maxDistance + entireRowsDiffDistance;

											double result = (Math.abs(entireRowsCurrentMaxDistance) < Math.abs(entireRowsMaxDistance))
												? entireRowsCurrentMaxDistance
												: entireRowsMaxDistance;

											double fullRowDistanceAddition = getFlowEntireRowAdjuster().getFullRowDistanceAddition();

											// correct result by already
											// adjusted
											// pixels through dragging
											result = result + fullRowDistanceAddition;

											return result;
										},
										(event2) -> {
										});
		return flinger;
	}

	private FlowAdjuster<M, I> getFlowEntireRowAdjuster() {
		for (FlowExtension<M, I> extension : getChildren()) {
			if (extension instanceof FlowAdjuster)
				return (FlowAdjuster<M, I>) extension;
		}
		return null;
	}

	public void setFlinger(Flinger<M, I> flinger) {
		this.flinger = flinger;
	}

	@Override
	public Map<EventType, EventHandler> createTypedEventHandlers() {

		Map<EventType, EventHandler> result = new HashMap<EventType, EventHandler>() {
			private static final long serialVersionUID = -7707743962676354987L;
			{
			}
		};
		result.putAll(getFlowEntireRowAdjuster().createTypedEventHandlers());
		return result;
	}

	@Override
	public Map<EventType, EventHandler> createTypedEventFilters() {
		return new HashMap<EventType, EventHandler>() {
			private static final long serialVersionUID = -3419029735075202493L;
			{
				put(MouseEvent.MOUSE_RELEASED, (event) -> {
					if (fling())
						event.consume();
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

	public boolean fling() {

		// prevent default selection behavior
		getSelectionModel().clearSelection();

		getFlowEntireRowAdjuster().getVelocityTracker()
								  .addPoint(0.0f,
											(float) dragY,
											System.currentTimeMillis());
		getFlowEntireRowAdjuster().getVelocityTracker().computeCurrentVelocity(1000);
		double velocity = getFlowEntireRowAdjuster().getVelocityTracker().getYVelocity();

		if (Math.abs(velocity) < 10.0) {
			return false;
		} else {
			getFlinger().fling(velocity);
			return true;
		}

	}
}
