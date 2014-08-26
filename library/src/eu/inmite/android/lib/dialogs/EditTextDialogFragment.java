/*
 * Copyright 2013 Inmite s.r.o. (www.inmite.eu).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.inmite.android.lib.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

/**
 * Dialog for displaying an input field with a title and two buttons. Implement {@link
 * eu.inmite.android.lib.dialogs.ISimpleDialogListener} in your Fragment or Activity to rect on positive and negative button clicks. This class can
 * be extended and more parameters can be added in overridden build() method.
 *
 * @author jenz
 *
 */
public class EditTextDialogFragment extends BaseDialogFragment {

	protected static String ARG_TITLE = "title";
	protected static String ARG_HINT = "hint";
	protected static String ARG_POSITIVE_BUTTON = "positive_button";
	protected static String ARG_NEGATIVE_BUTTON = "negative_button";

    protected EditText mEditText;
	protected int mRequestCode;
	protected IEditTextDialogListener mInlineListener;

	public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
		return new SimpleDialogBuilder(context, fragmentManager, EditTextDialogFragment.class);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			mRequestCode = getTargetRequestCode();
		} else {
			Bundle args = getArguments();
			if (args != null) {
				mRequestCode = args.getInt(BaseDialogBuilder.ARG_REQUEST_CODE, 0);
			}
		}
	}

	/**
	 * Children can extend this to add more things to base builder.
	 */
	@Override
	protected Builder build(Builder builder) {
		final String title = getTitle();
		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		}

		final String positiveButtonText = getPositiveButtonText();
		if (!TextUtils.isEmpty(positiveButtonText)) {
			builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (mInlineListener != null) {
						mInlineListener.onPositiveButtonClicked(mEditText.getText().toString(), mRequestCode);
					}

					ISimpleDialogListener listener = getDialogListener();
					if (listener != null) {
						listener.onPositiveButtonClicked(mRequestCode);
					}
					
					dismiss();
				}
			});
		}

		final String negativeButtonText = getNegativeButtonText();
		if (!TextUtils.isEmpty(negativeButtonText)) {
			builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (mInlineListener != null) {
						mInlineListener.onNegativeButtonClicked(mEditText.getText().toString(), mRequestCode);
					}
					
					ISimpleDialogListener listener = getDialogListener();
					if (listener != null) {
						listener.onNegativeButtonClicked(mRequestCode);
					}
					
					dismiss();
				}
			});
		}

        mEditText = new EditText(getActivity());
        mEditText.setHint(getHint());
        mEditText.setLines(3);
        mEditText.setGravity(Gravity.TOP | Gravity.LEFT);
        builder.setView(mEditText);

		return builder;
	}
	
	protected void setListener(IEditTextDialogListener listener) {
		mInlineListener = listener;
	}

	protected String getTitle() {
		return getArguments().getString(ARG_TITLE);
	}

	protected String getHint() {
		return getArguments().getString(ARG_HINT);
	}

	protected String getPositiveButtonText() {
		return getArguments().getString(ARG_POSITIVE_BUTTON);
	}

	protected String getNegativeButtonText() {
		return getArguments().getString(ARG_NEGATIVE_BUTTON);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		
		if (mInlineListener != null) {
			mInlineListener.onCancelled(mEditText.getText().toString(), mRequestCode);
		}

		ISimpleDialogCancelListener listener = getCancelListener();
		if (listener != null) {
			listener.onCancelled(mRequestCode);
		}
	}

	protected ISimpleDialogListener getDialogListener() {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			if (targetFragment instanceof ISimpleDialogListener) {
				return (ISimpleDialogListener) targetFragment;
			}
		} else {
			if (getActivity() instanceof ISimpleDialogListener) {
				return (ISimpleDialogListener) getActivity();
			}
		}
		return null;
	}

	protected ISimpleDialogCancelListener getCancelListener() {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null) {
			if (targetFragment instanceof ISimpleDialogCancelListener) {
				return (ISimpleDialogCancelListener) targetFragment;
			}
		} else {
			if (getActivity() instanceof ISimpleDialogCancelListener) {
				return (ISimpleDialogCancelListener) getActivity();
			}
		}
		return null;
	}

	public static class SimpleDialogBuilder extends BaseDialogBuilder<SimpleDialogBuilder> {

		private String mTitle;
		private String mHint;
		private String mPositiveButtonText;
		private String mNegativeButtonText;

		private boolean mShowDefaultButton = true;
		private IEditTextDialogListener mInlineListener;

		protected SimpleDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends EditTextDialogFragment> clazz) {
			super(context, fragmentManager, clazz);
		}

		@Override
		protected SimpleDialogBuilder self() {
			return this;
		}

		public SimpleDialogBuilder setTitle(int titleResourceId) {
			mTitle = mContext.getString(titleResourceId);
			return this;
		}

		public SimpleDialogBuilder setTitle(String title) {
			mTitle = title;
			return this;
		}

        public SimpleDialogBuilder setHint(int hintResourceId) {
			mHint = mContext.getString(hintResourceId);
			return this;
		}

		public SimpleDialogBuilder setHint(String hint) {
			mHint = hint;
			return this;
		}

		public SimpleDialogBuilder setPositiveButtonText(int textResourceId) {
			mPositiveButtonText = mContext.getString(textResourceId);
			return this;
		}

		public SimpleDialogBuilder setPositiveButtonText(String text) {
			mPositiveButtonText = text;
			return this;
		}

		public SimpleDialogBuilder setNegativeButtonText(int textResourceId) {
			mNegativeButtonText = mContext.getString(textResourceId);
			return this;
		}

		public SimpleDialogBuilder setNegativeButtonText(String text) {
			mNegativeButtonText = text;
			return this;
		}
		
		public SimpleDialogBuilder setListener(IEditTextDialogListener listener) {
			mInlineListener = listener;
			return this;
		}

		/**
		 * When there is neither positive nor negative button, default "close" button is created if it was enabled.<br/>
		 * Default is true.
		 */
		public SimpleDialogBuilder hideDefaultButton(boolean hide) {
			mShowDefaultButton = !hide;
			return this;
		}

		@Override
		protected Bundle prepareArguments() {
			if (mShowDefaultButton && mPositiveButtonText == null && mNegativeButtonText == null) {
				mPositiveButtonText = mContext.getString(R.string.dialog_close);
			}

			Bundle args = new Bundle();
			args.putString(EditTextDialogFragment.ARG_TITLE, mTitle);
			args.putString(EditTextDialogFragment.ARG_HINT, mHint);
			args.putString(EditTextDialogFragment.ARG_POSITIVE_BUTTON, mPositiveButtonText);
			args.putString(EditTextDialogFragment.ARG_NEGATIVE_BUTTON, mNegativeButtonText);

			return args;
		}
		
		@Override
		public DialogFragment show() {
			EditTextDialogFragment dialog = (EditTextDialogFragment) super.show();

			if (mInlineListener != null) {
				dialog.setListener(mInlineListener);
			}

			return dialog;
		}

	}
}
