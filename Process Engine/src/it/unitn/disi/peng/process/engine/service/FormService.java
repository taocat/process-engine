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
			if (view != null) {
//				layout.addView(label);
				layout.addView(view);
			}
			
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
			value = replaceVariables(value);
			e.setValue(value);
			if (value.startsWith("@")) {
				String[] terms = value.split("\\+");
				String incomingValue = "";
				incomingValue += variables.get(terms[0].substring(1));
			    Log.i(this.getClass().getName(), "+" + variables.get(terms[0].substring(1)));
				for (int i = 1; i < terms.length; i++) {
				    Log.i(this.getClass().getName(), "+" + variables.get(terms[i].substring(1)));
					incomingValue += "\n";
					incomingValue += variables.get(terms[i].substring(1));
				}
			    Log.i(this.getClass().getName(), "incomingValue:" + incomingValue);
				e.setValue(incomingValue);
//			    Log.i(this.getClass().getName(), "Applying Variables:" + value);
//				String incomingValue = variables.get(value.substring(1));
//				if (incomingValue != null) {
//				    Log.i(this.getClass().getName(), "incomingValue:" + incomingValue);
//					e.setValue(incomingValue);
//				}
			}
		}
	}
	
	private String replaceVariables(String v) {
		if (!v.contains("{")) {
			return v;
		}
		
		HashMap<String, String> incomingVariables = subProcess.getVariables();
		
		char[]chars = v.toCharArray();
		StringBuilder sb = new StringBuilder();
		StringBuilder var = null;
		int state = 0;
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			if (state == 0) {
				if (ch == '{') {
					state = 1;
				}
				else {
					sb.append(ch);
				}
			}
			else if (state == 1) {
				if (ch == '@') {
					var = new StringBuilder();
					state = 2;
				}
			}
			else if (state == 2) {
				if (ch == '}') {
				    Log.i(this.getClass().getName(), "var:" + var.toString() + "=" + incomingVariables.get(var.toString()));
					sb.append(incomingVariables.get(var.toString()));
					state = 0;
				}
				else {
					var.append(ch);
				}
			}
			
		}
		return sb.toString();
	}
}
