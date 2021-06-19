package dk.sebsa.ironflask.engine.gui;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.gui.annotaions.ButtonHandler;
import dk.sebsa.ironflask.engine.gui.enums.Anchor;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.gui.objects.Text;
import dk.sebsa.ironflask.engine.gui.text.Font;
import dk.sebsa.ironflask.engine.gui.text.Label;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.local.LocalizationManager;
import dk.sebsa.ironflask.engine.math.Color;

import java.lang.reflect.*;

public class GUIXmlParser {
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private static Application app;
	private static HashMap<String, String> vars = new HashMap<>();
	private static HashMap<String, Consumer<Button>> buttonhandlers;
	
	public static Window getWindow(String fileName, Application appIn) throws Exception {
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File((Paths.get(".").toAbsolutePath().normalize().toString() + "/resources/")+"gui/"+fileName));
        doc.getDocumentElement().normalize();
        app = appIn;
		        
		return buildWindow(doc.getElementsByTagName("window").item(0));
	}
	
	private static Window buildWindow(Node windowNode) {
		Element element = (Element) windowNode;
		Window window = new Window(getString(element.getAttribute("title")), !Boolean.getBoolean(element.getAttribute("flat")));
		window.setBackgroundColor(Color.parseColor(element.getAttribute("color")));
		
		parseChildren(window, windowNode.getChildNodes());
		
		return window;
	}
	
	private static void parseChildren(Parent parent, NodeList objects) {
		for (int i = 0; i < objects.getLength(); i++) {
            Node node = objects.item(i);
            
            if(node.getNodeType() == Node.ELEMENT_NODE)
            	parseNode(node, parent);
		}
	}
	
	private static Object parseNode(Node node, Parent parent) {
		Element element = (Element) node;
		String name = node.getNodeName();
		
		if(name == "button") {
			Button button = new Button(parent,
					app.input,
					buttonhandlers.get(element.getElementsByTagName("handler").item(0).getTextContent()),
					(Label) parseNode(element.getElementsByTagName("label").item(0), parent),
					element.getAttribute("center").startsWith("true"));
			System.out.println(element.getElementsByTagName("handler").item(0).getTextContent());
			button.setAnchor(parseAnchor(element.getAttribute("anchor")));
			button.posistion = (GUIDynamicVector)	parseNode(element.getElementsByTagName("pos").item(0), parent);
			button.size = (GUIDynamicVector)		parseNode(element.getElementsByTagName("size").item(0), parent);
						
			return button;
		} else if(name == "text") {
			Text text = new Text(parent,
					(Label) parseNode(element.getElementsByTagName("label").item(0), parent),
					element.getAttribute("center").startsWith("true"));
			
			text.setAnchor(parseAnchor(element.getAttribute("anchor")));
			text.posistion = (GUIDynamicVector)	parseNode(element.getElementsByTagName("pos").item(0), parent);
			text.size = (GUIDynamicVector)		parseNode(element.getElementsByTagName("size").item(0), parent);
						
			return text;
		} else if(name == "label") {
			Font f = (Font) parseNode(element.getElementsByTagName("font").item(0), parent);
			String s = getString(element.getElementsByTagName("text").item(0).getTextContent());
			
			vars.put("textSize", String.valueOf(f.getStringWidth(s)));
			return new Label(s, f);
		} else if(name == "font") {
			return Font.getFont(new java.awt.Font(element.getTextContent(), vari(element.getAttribute("style")), vari(element.getAttribute("size"))));
		} else if(name == "pos" || name == "size" || name == "guivector") {
			Node nx = element.getElementsByTagName("x").item(0);
			Node ny = element.getElementsByTagName("y").item(0);
			
			return new GUIDynamicVector(guiVar(nx), guiVar(ny));
		}
		
		return null;
	}
	
	private static GUIDynamicVar guiVar(Node node) {
		Element element = (Element) node;
		
		if(element.getTextContent().endsWith("0"))
			return null;
		else {
			float val = varf(element.getElementsByTagName("v").item(0).getTextContent());
			GUIDynamicType type;
			
			if(element.getElementsByTagName("dynamic").item(0).getTextContent().startsWith("true")) type = GUIDynamicType.Dynamic;
			else type = GUIDynamicType.Fixed;
			
			return new GUIDynamicVar(type, val);
		}
	}
	
	private static Anchor parseAnchor(String anchor) {
		switch (anchor) {
		case "tm":
			return Anchor.TopMiddle;
		case "rm":
			return Anchor.RightMiddle;
		case "lm":
			return Anchor.LeftMiddle;
		case "bm":
			return Anchor.BottomLeft;
		case "tr":
			return Anchor.TopRight;
		case "tl":
			return Anchor.TopLeft;
		case "br":
			return Anchor.BottomRight;
		default:
			return Anchor.BottomLeft;
		}
	}
	
	private static String getString(String name) {
		return LocalizationManager.getString(name);
	}
	
	private static float varf(String input) {
		return Float.valueOf(var(input));
		
	}
	
	private static int vari(String input) {
		return Integer.valueOf(var(input));
	}
	
	private static String var(String input) {
		if(!input.startsWith("{")) return input;
		
		String varName = input.substring(1, input.length()-1);
		
		if(vars.containsKey(varName)) return vars.get(varName);
		
		return "0";
	}
	
	public static void setupButtons(final Class<?> type, Object instance) {
		buttonhandlers = new HashMap<>();
		Class<?> klass = type;
	    while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
	        // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
	        for (final Method method : klass.getDeclaredMethods()) {
	            if (method.isAnnotationPresent(ButtonHandler.class)) {
	            	
	            	List<String> warnings = new ArrayList<>(3);
	            	if (!java.lang.reflect.Modifier.isPublic(method.getModifiers()))
	                    warnings.add(String.format("BUtton Handler Method %s must be public", method));
	                if (method.getParameterCount() != 1)
	                    warnings.add(String.format("BUtton Handler Method %s must consume only one argument", method));
	                if (method.getParameterCount() == 1 && !method.getParameterTypes()[0].equals(Button.class))
	                    warnings.add(String.format("BUtton Handler Method %s must consume %s", method, Button.class));

	                if (!warnings.isEmpty()) {
	                    for(String s : warnings) {
	                    	LoggingUtil.coreLog(Severity.Warning, s);
	                    }
	                    continue;
	                }
	            	
	                ButtonHandler annotInstance = method.getAnnotation(ButtonHandler.class);
	                Consumer<Button> buttonConsumer = toConsumer(method, instance);
	                buttonhandlers.put(annotInstance.ID(), buttonConsumer);
	                LoggingUtil.coreLog(Severity.Trace, "ButtonHandler set: " + annotInstance.ID());
	            }
	        }
	        klass = klass.getSuperclass();
	    }
	}
	

	private static <T> Consumer<T> toConsumer(Method m, Object instance) {
	    return param -> {
	        try {
	        	
	            m.invoke(instance, param);
	        } catch (IllegalAccessException | InvocationTargetException e) {
	            throw new RuntimeException(e);
	        }
	    };
	}
}
