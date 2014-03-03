/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import java.util.List;

import org.devel.jfxcontrols.scene.control.RotatableCheckBox;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.control.CheckBox;

import com.sun.javafx.scene.control.skin.CheckBoxSkin;

/**
 * @author stefan.illgen
 *
 */
public class RotatableCheckBoxSkin extends CheckBoxSkin {

	public RotatableCheckBoxSkin(CheckBox checkbox) {
		super(checkbox);
	}

	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
//		return CheckBox.getClassCssMetaData();
		return RotatableCheckBox.getClassCssMetaData();
	}
	
//	/** 
//     * @return The CssMetaData associated with this class, which may include the
//     * CssMetaData of its super classes.
//     */    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
//        return SkinBase.StyleableProperties.STYLEABLES;
//    }

	
	
}
