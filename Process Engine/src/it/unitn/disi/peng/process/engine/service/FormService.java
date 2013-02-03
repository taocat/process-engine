package it.unitn.disi.peng.process.engine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class FormService extends Service {
	List<FormElement> elements;
	
	public FormService() {
		this.elements = new ArrayList<FormElement>();
	}

	public void addElement(FormElement e) {
		elements.add(e);
	}
	
	private void updateElementValues() {
		Iterator<FormElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			FormElement e = iterator.next();
			e.updateValue();
		}
	}
	
	public View getView(final Context context) {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
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
						subProcess.executeNext((Activity) context);
					}
					
				});
			}
		}
		return layout;
	}
	
	public HashMap<String, String> getVariables() {
		HashMap<String, String> variables = new HashMap<String, String>();
		Iterator<FormElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			FormElement e = iterator.next();
			e.updateValue();
			variables.put(e.getId(), e.getValue());			
		}
		return variables;
	}
}
