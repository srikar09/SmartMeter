/*
 * Copyright (C) 2008 The Android Open Source Project
 * Copyright 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.sfsu.cs.orange.ocr.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.widget.ImageView;

/**
 * A button designed to be used for the on-screen shutter button.
 * It's currently an {@code ImageView} that can call a delegate when the
 * pressed state changes.
  */
public class ShutterButton extends ImageView {
	/**
	 * A callback to be invoked when a ShutterButton's pressed state changes.
	 */
	public interface OnShutterButtonListener {
		/**
		 * Called when a ShutterButton has been pressed.
		 *
		 * @param b The ShutterButton that was pressed.
		 */
		void onShutterButtonFocus(ShutterButton b, boolean pressed);

		void onShutterButtonClick(ShutterButton b);
	}

	private OnShutterButtonListener mListener;
	private boolean mOldPressed;

	public ShutterButton(Context context) {
		super (context);
	}

	public ShutterButton(Context context, AttributeSet attrs) {
		super (context, attrs);
	}

	public ShutterButton(Context context, AttributeSet attrs,
			int defStyle) {
		super (context, attrs, defStyle);
	}

	public void setOnShutterButtonListener(OnShutterButtonListener listener) {
		mListener = listener;
	}

	/**
	 * Hook into the drawable state changing to get changes to isPressed -- the
	 * onPressed listener doesn't always get called when the pressed state
	 * changes.
	 */
	 @Override
	 protected void drawableStateChanged() {
		 super .drawableStateChanged();
		 //this is where you need to insert the time interval that you need.
		 final boolean pressed = isPressed();
		 if (pressed != mOldPressed) {
			 if (!pressed) {

				 //on pressing the camera button, the sequence of events is focus is pressed,
				 //image is take and focus is released.
				 post(new Runnable() {
					 public void run() {
						 callShutterButtonFocus(pressed);
					 }
				 });
			 } else {
				 callShutterButtonFocus(pressed);
			 }
			 mOldPressed = pressed;
		 }
	 }

	 private void callShutterButtonFocus(boolean pressed) {
		 if (mListener != null) {
			 mListener.onShutterButtonFocus(this , pressed);
		 }
	 }

	 @Override
	 public boolean performClick() {
		 boolean result = super.performClick();
		 playSoundEffect(SoundEffectConstants.CLICK);
		 if (mListener != null) {
			 mListener.onShutterButtonClick(this);
		 }
		 return result;
	 }
}