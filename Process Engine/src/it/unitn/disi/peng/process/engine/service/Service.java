package it.unitn.disi.peng.process.engine.service;

import it.unitn.disi.peng.process.engine.model.SubProcess;

import java.util.HashMap;

import android.content.Context;
import android.view.View;

public abstract class Service {
	public static final String FORM_SERVICE = "it.unitn.disi.peng.process.engine.service.FormService";
	public static final String EMAIL_SERVICE = "it.unitn.disi.peng.process.engine.service.EmailService";
	
	protected SubProcess subProcess;
	
	protected HashMap<String, String> variables;
		
	public abstract View getView(Context context);
	public abstract HashMap<String, String> getVariables();
	public abstract void applyVariables();
	
	public void setSubProcess(SubProcess subProcess) {
		this.subProcess = subProcess;
	}
}
