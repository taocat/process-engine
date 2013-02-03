package it.unitn.disi.peng.process.engine.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Gateway extends BpmnElement {
	private HashMap<String, String> branches;
	
//	private String condition;
//	private String value;

	public Gateway(String id, String name) {
		super(id, name);
		branches = new HashMap<String, String>();
	}
	
	public void addBranch(String condition, String targetId) {
		branches.put(condition, targetId);
	}
	
	public String selectBranch(HashMap<String, String> variables) {
		String defualtBranch = branches.get("");
		String selectedBranch = defualtBranch;
		
		Set<String> conditionSet = branches.keySet();
		Iterator<String> conditionIterator = conditionSet.iterator();
		while(conditionIterator.hasNext()) {
			String conditionExpression = conditionIterator.next();
			int indexEqual = conditionExpression.indexOf('=');
			if (indexEqual > 0) {
				String cond = conditionExpression.substring(0, indexEqual);
				String expectedValue = conditionExpression.substring(indexEqual + 1);
				String value = variables.get(cond);
				if (value != null && expectedValue.equals(value)) {
					selectedBranch = branches.get(conditionExpression);
				}
			}
		}
		return selectedBranch;
	}
	
//	public void setCondition(String condition) {
//		this.condition = condition;
//	}
//	
//	public void setValue(String value) {
//		this.value = value;
//	}
//	
//	public String getCondition() {
//		return condition;
//	}
//	
//	public String getValue() {
//		return value;
//	}
}
