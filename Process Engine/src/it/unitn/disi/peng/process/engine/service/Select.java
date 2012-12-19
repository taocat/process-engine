package it.unitn.disi.peng.process.engine.service;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Select extends FormElement {
	String[] textArray;
	String[] valueArray;
	
	public Select(String id, String type, String value) {
		super(id, type, value);
	}
	
	public void setOptions(String[] textArray, String[] valueArray) {
		this.textArray = textArray;
		this.valueArray = valueArray;
//		this.textArray = new String[]{"yes", "no"};
	}

	public View getView(Context context) {
		Spinner sp = new Spinner(context);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, textArray);
		sp.setAdapter(adapter);
		return sp;
	}
}
