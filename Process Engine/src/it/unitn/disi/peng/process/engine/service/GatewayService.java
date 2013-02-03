package it.unitn.disi.peng.process.engine.service;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class GatewayService extends Service {
	Spinner sp;
	android.widget.Button bt;
	
	private HashMap<String, String> branches;
	
	public GatewayService() {
		super();
		branches = new HashMap<String, String>();
	}
	
	public void addBranch(String condition, String targetId) {
		branches.put(condition, targetId);
	}

	@Override
	public View getView(Context context) {
		sp = new Spinner(context);
		String[] conditionArray = (String[]) branches.keySet().toArray();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, conditionArray );
		sp.setAdapter(adapter);
		return sp;
	}

	@Override
	public HashMap<String, String> getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

}
