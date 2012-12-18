package it.unitn.disi.peng.process.engine.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class FormService extends Service {
	List<FormElement> elements;
	
	public FormService(Context context) {
		super(context);
		this.elements = new ArrayList<FormElement>();
	}

	public void addElement(FormElement e) {
		elements.add(e);
	}
	
	public View getView() {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		Iterator<FormElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			FormElement e = iterator.next();
			View label = e.getLabel(context);
			View view = e.getView(context);
			layout.addView(label);
			layout.addView(view);
		}
		return layout;
	}
}
