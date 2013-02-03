package it.unitn.disi.peng.process.engine.service;

import android.content.Context;
import android.view.View;

public class Button extends FormElement{
	public Button(String id, String type, String value) {
		super(id, type, value);
	}

	public View getView(Context context) {
		android.widget.Button bt = new android.widget.Button(context);
		bt.setText(value);
		return bt;
	}

	public void updateValue() {
	}
}
