package it.unitn.disi.peng.process.engine.service;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class Text extends FormElement {

	public Text(String id, String type, String value) {
		super(id, type, value);
	}

	public View getView(Context context) {
		TextView tv = new TextView(context);
		tv.setText(value);
		return tv;
	}
}
