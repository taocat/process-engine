package it.unitn.disi.peng.process.engine.model;

import it.unitn.disi.peng.process.engine.service.Service;
import android.app.Activity;
import android.util.Log;

public class Task {
	private String name;
	private String className;
	private Service service;
	
	public Task() {
	}
	
	public Task(String name, String className, Service service) {
		this.name = name;
		this.className = className;
		this.service = service;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
	public void execute(Activity activity) {
	    Log.i(this.getClass().getName(), "Executing task: " + name);
		if (className.equals(Service.FORM_SERVICE)) {
			activity.setContentView(service.getView(activity));
		}
	}
}
