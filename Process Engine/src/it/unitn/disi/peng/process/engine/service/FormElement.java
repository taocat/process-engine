package it.unitn.disi.peng.process.engine.service;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public abstract class FormElement {
	protected String id;
	protected String type;
	protected String value;

	public abstract View getView(Context context);
	
	public View getLabel(Context context) {
		TextView label = new TextView(context);
		label.setText(id);
		return label;
	}	
	
	FormElement(String id, String type, String value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
}
