package it.unitn.disi.peng.process.engine.service;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class Hidden extends FormElement {

	public Hidden(String id, String type, String value) {
		super(id, type, value);
	}

	public View getView(Context context) {
		return null;
	}

	public void updateValue() {
		// do nothing
	    Log.i(this.getClass().getName(), "Update Value " + id + ":" + value);
	}

}
