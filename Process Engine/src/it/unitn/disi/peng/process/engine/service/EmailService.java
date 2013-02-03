package it.unitn.disi.peng.process.engine.service;

import java.util.HashMap;

import android.content.Context;
import android.view.View;

public class EmailService extends Service{
	private String email;
	private String subject;
	private String body;
	private String bodySrc;
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public void setBodySrc(String bodySrc) {
		this.bodySrc = bodySrc;
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
	
	public String getBodySrc() {
		return bodySrc;
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

}
