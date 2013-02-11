package it.unitn.disi.peng.process.engine.service;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class EmailService extends Service{
	private String email;
	private String subject;
	private String body;
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getBody() {
		return body;
	}
	
	@Override
	public View getView(Context context) {
		// emailDO Auemail-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getVariables() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void applyVariables() {
	    Log.i(this.getClass().getName(), "Applying Variables");
		HashMap<String, String> variables = subProcess.getVariables();

		if (email.startsWith("@")) {
		    Log.i(this.getClass().getName(), "Applying Variables:" + email);
			String incomingValue = variables.get(email.substring(1));
			if (incomingValue != null) {
			    Log.i(this.getClass().getName(), "incomingValue:" + incomingValue);
			    email = incomingValue;
			}
		}
		
		if (subject.startsWith("@")) {
		    Log.i(this.getClass().getName(), "Applying Variables:" + subject);
			String incomingValue = variables.get(subject.substring(1));
			if (incomingValue != null) {
			    Log.i(this.getClass().getName(), "incomingValue:" + incomingValue);
			    subject = incomingValue;
			}
		}

	    Log.i(this.getClass().getName(), "Applying Variables:" + body);
		if (body.startsWith("@")) {
		    Log.i(this.getClass().getName(), "Applying Variables:" + body);
			String incomingValue = variables.get(body.substring(1));
			if (incomingValue != null) {
			    Log.i(this.getClass().getName(), "incomingValue:" + incomingValue);
			    body = incomingValue;
			}
		}
	}

}
