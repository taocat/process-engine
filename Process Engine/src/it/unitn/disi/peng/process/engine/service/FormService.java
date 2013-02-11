package it.unitn.disi.peng.process.engine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class FormService extends Service {
	List<FormElement> elements;
	
	public FormService() {
		this.elements = new ArrayList<FormElement>();
		this.variables = new HashMap<String, String>();
	}

	public void addElement(FormElement e) {
		elements.add(e);
	}
	
	private void updateElementValues() {
		Iterator<FormElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			FormElement e = iterator.next();
			e.updateValue();
		    Log.i(this.getClass().getName(), "Update variables " + e.getId() + ":" + e.getValue());
			variables.put(e.getId(), e.getValue());			
		}
	}
	
	public View getView(final Context context) {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		// assign incoming values "value=@id" 
//		applyVariables();
		
		Iterator<FormElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			FormElement e = iterator.next();
			View label = e.getLabel(context);
			View view = e.getView(context);
			layout.addView(label);
			layout.addView(view);
			
			if (e.type.toLowerCase().equals("submit")) {
				view.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						updateElementValues();
						subProcess.executeNext((Activity) context);
					}
					
				});
			}
		}
		return layout;
	}
	
	public HashMap<String, String> getVariables() {
		return variables;
	}
	
	public void applyVariables() {
	    Log.i(this.getClass().getName(), "Applying Variables");
		Iterator<FormElement> iterator = elements.iterator();
		HashMap<String, String> variables = subProcess.getVariables();
		while (iterator.hasNext()) {
			FormElement e = iterator.next();
			String value = e.getValue();
			if (value.startsWith("@")) {
			    Log.i(this.getClass().getName(), "Applying Variables:" + value);
				String incomingValue = variables.get(value.substring(1));
				if (incomingValue != null) {
				    Log.i(this.getClass().getName(), "incomingValue:" + incomingValue);
					e.setValue(incomingValue);
				}
			}
		}
	}
}
