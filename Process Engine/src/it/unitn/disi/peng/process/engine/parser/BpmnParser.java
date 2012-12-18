package it.unitn.disi.peng.process.engine.parser;

import it.unitn.disi.peng.process.engine.model.SubProcess;
import it.unitn.disi.peng.process.engine.model.Task;
import it.unitn.disi.peng.process.engine.service.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class BpmnParser {
	Activity activity;

	public BpmnParser(Activity activity) {
		this.activity = activity;
	}
	
	public void parse(InputStream in) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		HashMap<String, String> prefMap = new HashMap<String, String>() {{
		    put("ns", "http://www.omg.org/spec/BPMN/20100524/MODEL");
		    put("mpe", "http://peng.disi.unitn.it");
		}};
		SimpleNamespaceContext namespaces = new SimpleNamespaceContext(prefMap);
		xPath.setNamespaceContext(namespaces);
		
		InputSource inputSource = new InputSource(in);
		try {
			NodeList subProcesses = (NodeList) xPath.evaluate("/ns:definitions/ns:process/ns:subProcess", inputSource, XPathConstants.NODESET);
			Log.i(this.getClass().getName(), "subProcess:" + subProcesses.getLength());
			
			for (int i = 0; i < subProcesses.getLength(); i++) {
				Element subProcessElement = (Element) subProcesses.item(i);
				NodeList taskElements = (NodeList) xPath.evaluate("ns:task", subProcessElement, XPathConstants.NODESET);
				
				String subProcessName = xPath.evaluate("@name", subProcessElement);
				SubProcess subProcess = new SubProcess(subProcessName);
				Log.i(this.getClass().getName(), "task:" + subProcessElement.getChildNodes().getLength() + ":" + taskElements.getLength());
				
				for (int j = 0; j < taskElements.getLength(); j++) {
					Element taskElement = (Element) taskElements.item(j);
					Task task = parseTask(xPath, taskElement);
					subProcess.addTask(task);
				}
				subProcess.execute(activity);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	public Task parseTask(XPath xPath, Object object) {
		Task task = null;

		Element taskElement = (Element) object;
		String taskName;
		try {
			taskName = xPath.evaluate("@name", taskElement);
			Node service = (Node) xPath.evaluate(
					"ns:extensionElements/mpe:service", taskElement,
					XPathConstants.NODE);
			String className = xPath.evaluate("@class", service);
			Log.i(this.getClass().getName(), taskName + ":" + className);

			if (className.equals(Service.FORM_SERVICE)) {
				FormService fs = new FormService();
				NodeList serviceElements = service.getChildNodes();
				for (int k = 0; k < serviceElements.getLength(); k++) {
					Node serviceElement = serviceElements.item(k);
					String serviceElementName = serviceElement.getNodeName();
					if (serviceElementName.equals("mpe:text")) {
						String id = xPath.evaluate("@id", serviceElement);
						String value = xPath.evaluate("@value", serviceElement);
						Text text = new Text(id, "", value);
						fs.addElement(text);
						Log.i(this.getClass().getName(), "mpe:text");
					}
					else if (serviceElementName.equals("mpe:input")) {
						String id = xPath.evaluate("@id", serviceElement);
						String type = xPath.evaluate("@type", serviceElement);
						String value = xPath.evaluate("@value", serviceElement);
						Input input = new Input(id, type, value);
						fs.addElement(input);
						Log.i(this.getClass().getName(), "mpe:input");
					}
				}
				task = new Task(taskName, className, fs);
			} else if (className.equals(Service.EMAIL_SERVICE)) {

			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task;
	}
}
