package it.unitn.disi.peng.process.engine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.util.Log;

public class SubProcess {
	public String name;
	private List <BpmnElement> elements;
	private BpmnElement start;
	private BpmnElement end;
	private boolean flows[][];
	private String conditions[][];
	private boolean isGateway;
	private int currentIndex;
	public static final int BEFORE_EXECUTION = -1;
	public static final int AFTER_EXECUTION = -2;
	private HashMap<String, String> variables;
	
	public SubProcess() {
		this.elements= new ArrayList <BpmnElement> (); 
		this.currentIndex = BEFORE_EXECUTION;
		this.isGateway = false; 
	}
	
	public SubProcess(String name) {
		this.elements= new ArrayList <BpmnElement> (); 
		this.currentIndex = BEFORE_EXECUTION;
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStart(BpmnElement element) {
		this.start = element;
	}
	
	public void setEnd(BpmnElement element) {
		this.end = element;
	}
	
	public void addElement(BpmnElement element) {
		this.elements.add(element);
	}
	
	public void addFlow(String sourceRef, String targetRef, String condition) {
		// initialize the matrix if not yet done
		if (flows == null) {
			flows = new boolean [elements.size()] [elements.size()];
		}
		
		if (conditions == null) {
			conditions = new String [elements.size()] [elements.size()];
		}
		
		int sourceIndex = elementId2Index(sourceRef);
		int targetIndex = elementId2Index(targetRef);
		if (sourceIndex >= 0 && targetIndex >= 0) {
			//if both source and target id found
			flows[sourceIndex] [targetIndex] = true;
			
			if (!condition.equals("")) {
				conditions[sourceIndex] [targetIndex] = condition;
			}
		}
	}
	
	public int elementId2Index(String id) {
		int i = 0;
		Iterator<BpmnElement> iterator = this.elements.iterator();
		while (iterator.hasNext()) {
			BpmnElement element = iterator.next();
			if (element.getId().equals(id)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public void execute(Activity activity) {
		if (start != null) {
			BpmnElement currentElement = start;
			int currentIndex = elements.indexOf(start);
			while (currentElement != null) {
				Log.i(this.getClass().getName(), currentElement.getId() + ":" + currentElement.getName());
				
				currentIndex = getNextIndex(currentIndex);
				if (currentIndex >= 0) {
					currentElement = elements.get(currentIndex);
				}
				else {
					// after executing one element, the next element is by default set to null, until there is actually a next element
					currentElement = null;
				}
			}
		}
	}
	
	public void executeNext(Activity activity) {
		BpmnElement currentElement;
		Log.i(this.getClass().getName(), "Current Index 1:" + currentIndex);
		currentIndex = getNextIndex(currentIndex);
		Log.i(this.getClass().getName(), "Current Index 2:" + currentIndex);
		if (currentIndex >= 0) {
			currentElement = elements.get(currentIndex);
			Log.i(this.getClass().getName(), "executeNext 1:" + currentIndex + ":" + currentElement.getName());
			if (currentElement instanceof Task) {
				Log.i(this.getClass().getName(), "executeNext 2:" + currentElement.getName());
				Task currentTask = (Task) currentElement;
				currentTask.execute(activity, this);
				variables = currentTask.getVariables();
			}
			else if (currentElement instanceof Gateway) {
				Log.i(this.getClass().getName(), "executeNext 2:" + currentElement.getName());
				isGateway = true;
				executeNext(activity);
//				currentTask.execute(activity, this);
//				variables = currentTask.getVariables();
			}
		}
	}
	
	public int getNextIndex(int ci) {
		
		if (ci == BEFORE_EXECUTION) {
			Log.i(this.getClass().getName(), "start index is:" + elements.indexOf(start));
			return elements.indexOf(start);
		}
		
		if (isGateway) {
			Log.i(this.getClass().getName(), "gateway 1:");
			int defaultTargetIndex = -1;
			for (int j = 0; j < elements.size(); j++) {
				if ( flows[ci][j] ) {
					if (conditions[ci][j] == null) {
						Log.i(this.getClass().getName(), "default gateway:" + j);
						defaultTargetIndex = j;
					}
					else {
						// get expression and expected value in condition
						String condition = conditions[ci][j];
						if (isExpressionTrue(condition)) {
							Log.i(this.getClass().getName(), "taking " + j + ":" + condition);
							return j;
						}
						
//						int equalIndex = condition.indexOf('=');
//						String expression = condition.substring(0, equalIndex);
//						String expectedValue = condition.substring(equalIndex + 1);
//						
//						// get runtime value of the expression
//						String runtimeValue = variables.get(expression);
//						
//
//						Log.i(this.getClass().getName(), condition + " : " + runtimeValue);
//						
//						// if it matches, jump to it
//						if (runtimeValue != null && runtimeValue.equals(expectedValue)) {
//							Log.i(this.getClass().getName(), "taking gateway:" + j);
//							return j;
//						}
					}
				}
			}
			// reset the flag
			isGateway = false;
			Log.i(this.getClass().getName(), "taking default gateway:" + defaultTargetIndex);
			// if none condition matches, jump to default
			return defaultTargetIndex;
		}
		
		// reach here only when it is not a gateway
		// try to find the next element
		for (int j = 0; j < elements.size(); j++) {
			if ( flows[ci][j] ) {
				// TODO
				// gateway
				// move to the next element
				return j;
			}
		}
		
		return AFTER_EXECUTION;
	}
	
	public void print() {
		Iterator<BpmnElement> iterator = this.elements.iterator();
		while (iterator.hasNext()) {
			BpmnElement element = iterator.next();
			Log.i(this.getClass().getName(), element.getId() + ":" + element.getName());
		}
		
		for (int i = 0; i < elements.size(); i++) {
			for (int j = 0; j < elements.size(); j++) {
				if (flows[i][j]) {
					Log.i(this.getClass().getName(), "" + i + "." + elements.get(i).getName() + "->" + j + "." + elements.get(j).getName());
				}
			}
		}
	}
	
	public HashMap<String, String> getVariables() {
		return variables;
	}
	
	public boolean isExpressionTrue(String expr) {
		expr = expr.replace("&gt;", ">");
		expr = expr.replace("&amp;", "&");
		expr = expr.replace("&lt;", "<");
		
		Log.i(this.getClass().getName(), expr);
		
		String[] simpleExpressions = expr.split("&&");
		for (String se : simpleExpressions) {
			Log.i(this.getClass().getName(), se);
			String[] tokens = se.split("[<=>]");
			String runtimeValue = variables.get(tokens[0]);
			Log.i(this.getClass().getName(), "runtime:" + runtimeValue);
			Log.i(this.getClass().getName(), "expected:" + tokens[1]);
			if (runtimeValue ==  null) {
				return false;
			}
			
			if (se.contains("=") && !runtimeValue.equals(tokens[1])) {
				return false;
			}
			
			if (se.contains("<") && !(Integer.parseInt(runtimeValue) < Integer.parseInt(tokens[1]))) {
				return false;
			}
			
			if (se.contains(">") && !(Integer.parseInt(runtimeValue) > Integer.parseInt(tokens[1]))) {
				return false;
			}
			
		}
		return true;
	}
}
