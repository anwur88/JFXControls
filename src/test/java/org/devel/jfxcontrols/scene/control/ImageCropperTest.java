/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.loadui.testfx.Assertions.verifyThat;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;

import org.apache.commons.io.FileUtils;
import org.devel.jfxcontrols.concurrent.ImageLoader;
import org.devel.jfxcontrols.concurrent.ImageWriter;
import org.devel.jfxcontrols.conf.Properties;
import org.devel.jfxcontrols.scene.image.SourceImageView;
import org.devel.jfxcontrols.scene.shape.CropperRectangle;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.TestUtils;

/**
 * 
 * @author stefan.illgen
 * 
 */
public class ImageCropperTest extends GuiTest {

	private ImageCropper root;
	private SourceImageView sourceImageView;
	private CropperRectangle cropperRectangle;
	private boolean saveFinished;

	// private ImageCropperSkin skin;
	
	public ImageCropperTest() {
		super();
	}

	@Override
	protected Parent getRootNode() {
		root = new ImageCropper();
		// skin = (ImageCropperSkin) root.getSkin();
		return root;
	}

	@Before
	public void setup() {
		new Properties().load();
		try {
			super.setupStage();
		} catch (Throwable e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Does not cover setting correct view port on target property
	 */
	@Test
	public void imageCroppingCheck() {

		// Pre
		find("#imageCropperGridPane", root);

		// Actions
		
		// load image
		// Button loadImageButton = find("#loadImageButton", root);
		// click(loadImageButton, MouseButton.PRIMARY);
		// workaround without using buttons 
		//TODO stefan - How 2 test dialogs in sub stages?
		loadSourceImage(_createImageFile("/org/devel/jfxcontrols/sample/img/goerl_aliced_500.jpg"));
		verifyThat(sourceImageView, notNullValue());

		// crop (move rectangle by dragging)
		cropperRectangle = find("#cropperRectangle");
		double x = cropperRectangle.getX();
		double layoutX = cropperRectangle.getLayoutX();
		double translateX = cropperRectangle.getTranslateX();
		double refX = cropperRectangle.getRefX();
		drag(cropperRectangle, MouseButton.PRIMARY).by(10, 10);
		// verify new position of cropper rectangle
		verifyThat(cropperRectangle.getTranslateX(), is(10.0));
		verifyThat(cropperRectangle.getTranslateY(), is(10.0));

		// save image
		File targetFile = new File(FileUtils.getUserDirectoryPath().concat("//"+"goerl_aliced_500_target.jpg"));		
		saveImage(targetFile);
		verifyThat(targetFile.exists(), is(true));

	}

	private File _getExistingImageFile(String path) {
		File result = null;
		try {
			URI uri = getClass().getResource(path).toURI();
			result = new File(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private File _createImageFile(String path) {
		File result = _getExistingImageFile(path);
		return (result != null && result.exists()) ? result : null;
	}

	private void loadSourceImage(File image) {
		if (image != null) {

			ImageLoader task = new ImageLoader(image);
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					// if load image was successful
					if (task.getValue()) {
						root.setSourceImage(task.getImage());
					}
				}
			});
			new Thread(task).start();

			sourceImageView = find("#sourceImageView", root);
			TestUtils.awaitCondition(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return sourceImageView.getImage() != null;
				}
			}, 10);
		}
	}

	private void saveImage(File image) {
		if (image != null) {

			saveFinished = false;
			
			ImageWriter task = new ImageWriter(image,
					root.getTargetImage(), cropperRectangle.getTranslateX(),
					cropperRectangle.getTranslateY(),
					cropperRectangle.getWidth(), cropperRectangle.getHeight());
			
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					saveFinished = task.getValue(); 
				}
			});

			new Thread(task).start();
			
			TestUtils.awaitCondition(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return saveFinished;
				}
			}, 10);
		}
	}

}
