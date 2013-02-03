package it.unitn.disi.peng.process.engine.model;

import java.util.HashMap;

import it.unitn.disi.peng.process.engine.service.EmailService;
import it.unitn.disi.peng.process.engine.service.Service;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class Task extends BpmnElement {
	private String className;
	private Service service;
	
	public Task(String id, String name, String className, Service service) {
		super(id, name);
		this.className = className;
		this.service = service;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
	public void execute(Activity activity, SubProcess subProcess) {
		
	    Log.i(this.getClass().getName(), "Executing task: " + getName());
	    
	    service.setSubProcess(subProcess);
	    
		if (className.equals(Service.FORM_SERVICE)) {
			activity.setContentView(service.getView(activity));
		}
		else if (className.equals(Service.EMAIL_SERVICE)) {
			EmailService es = (EmailService) service;
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{es.getEmail()});
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, es.getSubject());
			if (es.getBody() != null) {
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, es.getBody());
			}
//			else {
//				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, es.getBody());
//			}
			
			activity.startActivity(emailIntent);
		}
	}
	
	public HashMap<String, String> getVariables() {
		return service.getVariables();
	}
}
