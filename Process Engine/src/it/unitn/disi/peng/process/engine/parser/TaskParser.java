package it.unitn.disi.peng.process.engine.parser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;
import it.unitn.disi.peng.process.engine.model.Task;
import it.unitn.disi.peng.process.engine.service.FormService;
import it.unitn.disi.peng.process.engine.service.Input;
import it.unitn.disi.peng.process.engine.service.Service;
import it.unitn.disi.peng.process.engine.service.Text;

public class TaskParser {

	public Task parse(XPath xPath, Object object) {
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
