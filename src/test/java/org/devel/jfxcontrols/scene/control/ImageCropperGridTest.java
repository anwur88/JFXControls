/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.loadui.testfx.Assertions.verifyThat;

import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;

import org.devel.jfxcontrols.scene.image.SourceImageView;
import org.devel.jfxcontrols.scene.shape.CropperRectangle;
import org.devel.jfxcontrols.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.TestUtils;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropperGridTest extends GuiTest {

	private ImageCropperGrid root;

	@Override
	protected Parent getRootNode() {
		root = new ImageCropperGrid();
		return root;
	}

	@Before
	public void tearUp() {
		new Properties().loadProxyConf();
	}

	@Test
	public void imageCropTest() {

		// Pre
		find("#imageCropperView", root);

		// Actions

		// load image
		// Button loadImageButton = find("#loadImageButton", root);
		// click(loadImageButton, MouseButton.PRIMARY);
		// workaround without using buttons
		root.loadSourceImage(_createImageFile("/org/devel/jfxcontrols/sample/img/goerl_aliced_500.jpg"));
		final SourceImageView sourceImageView = find("#sourceImageView", root);
		TestUtils.awaitCondition(new Callable<Boolean>() {			
			@Override
			public Boolean call() throws Exception {
				return sourceImageView.getImage() != null;
			}
		}, 10);
		verifyThat(sourceImageView, notNullValue());
		
		// crop (move rectangle by dragging)
		CropperRectangle cropperRectangle = find("#cropperRectangle");
		drag(cropperRectangle, MouseButton.PRIMARY).by(10, 10);
		double x = cropperRectangle.getX();
		// verify new position of cropper rectangle
		verifyThat(cropperRectangle.getX(), is(10));
		verifyThat(cropperRectangle.getY(), is(10));
		// verify changed value target image view
		// TODO stefan test cropper move > don't know how 2, so left out until now

		// save image
//		Button saveImageButton = find("#saveImageButton", root);
//		click(saveImageButton, MouseButton.PRIMARY);
		File targetFile = _getImageFile("/org/devel/jfxcontrols/sample/img/goerl_aliced_500_target.jpg");
		root.saveImage(targetFile);
		verifyThat(targetFile.exists(), is(true));

	}

	private File _getImageFile(String path) {
		File result = null;
		try {
			result = new File(getClass().getResource(
					path).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return result;
	}

	private File _createImageFile(String path) {
		File result = _getImageFile(path);
		return (result != null && result.exists()) ? result : null;
	}

}
