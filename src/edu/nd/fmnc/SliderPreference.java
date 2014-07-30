package edu.nd.fmnc;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SliderPreference extends DialogPreference implements
		OnSeekBarChangeListener, OnClickListener {

	private static final String ns = "http://schemas.android.com/apk/res/android";

	private SeekBar mSeekBar;
	private TextView text1, valueText;
	private Context mContext;
	private String diagMsg;
	private int mDefault, max, value = 0;

	public SliderPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		int msgID = attrs.getAttributeResourceValue(ns, "dialogMessage",
				0);
		if (msgID == 0)
			diagMsg = attrs.getAttributeValue(ns, "dialogMessage");
		else
			diagMsg = mContext.getString(msgID);

		mDefault = attrs.getAttributeIntValue(ns, "defaultValue", 0);
		max = attrs.getAttributeIntValue(ns, "max", 359);

	}

	@Override
	protected View onCreateDialogView() {

		LinearLayout.LayoutParams params;
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(5, 5, 5, 5);

		text1 = new TextView(mContext);
		text1.setPadding(30, 15, 30, 15);
		if (diagMsg != null) {
			text1.setText(diagMsg);
		}
		layout.addView(text1);

		valueText = new TextView(mContext);
		valueText.setGravity(Gravity.CENTER_HORIZONTAL);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(valueText, params);

		mSeekBar = new SeekBar(mContext);
		mSeekBar.setOnSeekBarChangeListener(this);
		layout.addView(mSeekBar, params);

		if (shouldPersist()) {
			value = getPersistedInt(mDefault);
		}

		mSeekBar.setMax(max);
		mSeekBar.setProgress(value);

		FMNCActivity.setTime(value);

		return layout;

	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		mSeekBar.setMax(max);
		mSeekBar.setProgress(value);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
		super.onSetInitialValue(restore, defaultValue);
		if (restore) {
			value = shouldPersist() ? getPersistedInt(mDefault) : 0;
		} else {
			value = (Integer) defaultValue;
		}
	}

	/* Methods to allow checking of the status of the seekbar */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		String t;
		if (progress < 6) {
			t = String.valueOf((progress + 1) * 10);
			valueText.setText(t.concat(" s"));
		} else if (progress < 359) {
			t = String.valueOf((progress + 1) / 6);
			int r = (progress + 1) % 6;
			String rem = String.valueOf(r * 10);
			valueText.setText(t.concat(" m  " + rem + " s"));
		} else {
			valueText.setText("1 hr");
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	public void setMax(int maxVal) {
		max = maxVal;
	}

	public int getMax() {
		return max;
	}

	public void setProgress(int progress) {
		value = progress;
		if (mSeekBar != null) {
			mSeekBar.setProgress(progress);
		}
	}

	public int getProgress() {
		return value;
	}

	// Methods for using the on click functionality
	@Override
	public void onClick(View arg0) {
		if (shouldPersist()) {
			value = mSeekBar.getProgress();
			persistInt(mSeekBar.getProgress());
			callChangeListener(Integer.valueOf(mSeekBar.getProgress()));
		}
		((AlertDialog) getDialog()).dismiss();

		FMNCActivity.setTime(value);

	}

	@Override
	public void showDialog(Bundle state) {
		super.showDialog(state);
		Button positive = ((AlertDialog) getDialog())
				.getButton(AlertDialog.BUTTON_POSITIVE);
		positive.setOnClickListener(this);
	}

}
