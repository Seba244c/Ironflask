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
import dk.sebsa.ironflask.engine.graph.Material;
import dk.sebsa.ironflask.engine.gui.animations.MoveInFromSide;
import dk.sebsa.ironflask.engine.gui.annotaions.ButtonHandler;
import dk.sebsa.ironflask.engine.gui.enums.Anchor;
import dk.sebsa.ironflask.engine.gui.enums.Side;
import dk.sebsa.ironflask.engine.gui.enums.GUIDynamicType;
import dk.sebsa.ironflask.engine.gui.objects.Box;
import dk.sebsa.ironflask.engine.gui.objects.Button;
import dk.sebsa.ironflask.engine.gui.objects.GuiList;
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
			parseGeneralGuiObject(element, button, parent);
			return button;
		} else if(name == "text") {
			Text text = new Text(parent,
					(Label) parseNode(element.getElementsByTagName("label").item(0), parent),
					element.getAttribute("center").startsWith("true"));
			
			parseGeneralGuiObject(element, text, parent);			
			return text;
		} else if(name == "label") {
			Font f = (Font) parseNode(element.getElementsByTagName("font").item(0), parent);
			String s = getString(element.getElementsByTagName("text").item(0).getTextContent());
			
			vars.put("textSize", String.valueOf(f.getStringWidth(s)));
			return new Label(s, f);
		} else if(name == "font") {
			return Font.getFont(
					new java.awt.Font(element.getTextContent(),
							Math.round(varf(element.getAttribute("style"))),
							Math.round(varf(element.getAttribute("size")))));
		} else if(name == "pos" || name == "size" || name == "guivector") {
			Node nx = element.getElementsByTagName("x").item(0);
			Node ny = element.getElementsByTagName("y").item(0);
			
			return new GUIDynamicVector(guiVar(nx), guiVar(ny));
		} else if(name == "constraint") {
			((Window) parent).addCosntraint(new Constraint(
					parseSide(element.getElementsByTagName("side").item(0).getTextContent()),
					guiVar(element.getElementsByTagName("guivar").item(0))));
		} else if(name == "list") {
			GuiList list = new GuiList(parent);
			parseGeneralGuiObject(element, list, parent);
			
			parseChildren(list, node.getChildNodes());
		} else if(name == "sprite") {
			Sprite sprite = Sprite.getSprite(element.getTextContent());
			if(sprite != null) return sprite;
			
			Material spriteMaterial = Material.getMaterial(element.getTextContent());
			if(spriteMaterial != null) return new Sprite(spriteMaterial);
						
			return new Sprite(new Material(Color.parseColor(element.getTextContent())));
		} else if(name == "box") {
			Box box = new Box(parent);
			parseGeneralGuiObject(element, box, parent);
			
			return box;
		}
		
		return null;
	}
	
	private static void parseGeneralGuiObject(Element element, GuiObject object, Parent parent) {
		try {
			object.setAnchor(parseAnchor(element.getAttribute("anchor")));
			try {
				object.posistion = (GUIDynamicVector)	parseNode(element.getElementsByTagName("pos").item(0), parent);
				object.size = (GUIDynamicVector)		parseNode(element.getElementsByTagName("size").item(0), parent);
			} catch (Exception e) { /* Save my ass */ }
			
			// Animations
			NodeList animations = element.getElementsByTagName("animation");
			for(int i = 0; i < animations.getLength(); i++) {
				Node node = animations.item(i);
				Animation animation = parseAnimation((Element) node, parent);
				
				if(animation != null)
					object.animations.add(animation);
			}
			
			// Sprite
			NodeList spriteList = element.getElementsByTagName("sprite");
			if(spriteList.getLength() > 0) object.sprite = (Sprite) parseNode(spriteList.item(0), parent);
		} catch (Exception e) {
			System.out.println("Error when parsing generalGuiObject" + element.getBaseURI());
			System.out.println(" - " + element.getTagName());
			System.out.println(" - " + element.toString());
			System.out.println("\n--== Stacktrace =--");
			e.printStackTrace();
			System.out.println("--== Stacktrace =--\n");
		}
	}
	
	private static Animation parseAnimation(Element element, Parent parent) {
		String type = element.getAttribute("type");
		float time = varf(element.getAttribute("time"));
		float waitTime = varf(element.getAttribute("waitTime"));
		
		if(type.startsWith("MoveInFromSide")) {
			return new MoveInFromSide(parseSide(element.getElementsByTagName("side").item(0).getTextContent()), parent, time, waitTime);
		}
		
		return null;
	}
	
	private static GUIDynamicVar guiVar(Node node) {
		Element element = (Element) node;
		
		if(element.getTextContent().endsWith("0"))
			return null;
		else {
			NodeList insideText = element.getChildNodes();
			float val = varf(insideText.item(insideText.getLength()-1).getTextContent());
			GUIDynamicType type;
			
			if(element.getAttribute("dynamic").equals("true")) type = GUIDynamicType.Dynamic;
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
	
	private static Side parseSide(String cs) {
		switch (cs) {
		case "left":
			return Side.Left;
		case "right":
			return Side.Right;
		case "bottom":
			return Side.Bottom;
		default:
			return Side.Top;
		}
	}
	
	private static String getString(String name) {
		return LocalizationManager.getString(name);
	}
	
	private static float varf(String input) {
		input = input.replace(" ","");
		
		// Parse all variables
		if(input.contains("{")) {
			String newString = "";
			String tempVarName = "";
			String tempVarValue = "";
			int parseState = 0;
			
			for (char c : input.toCharArray()) {
				if (parseState == 0 && c != '{') newString += c;
				else if (parseState == 0 && c == '{') { parseState = 1; tempVarName = ""; }
				else if (parseState == 1 && c != '}') tempVarName += c;
				else if (parseState == 1 && c == '}') {
					parseState = 0;
					
					if(vars.containsKey(tempVarName)) tempVarValue = vars.get(tempVarName);
					else tempVarValue = "0";
					
					newString += tempVarValue; 
				}
			}
			
			input = newString;
		}
		
		// Handle + & -
		if(input.contains("+") || input.contains("-")) {
			String left = "";
			char middle = 'e';
			String right = "";
			float lTemp = 0;
			float rTemp = 0;
			
			for (char c : input.toCharArray()) {
				if (middle == 'e') {
					if(c == '+' || c == '-') middle = c;
					else left += c;
				} else {
					if(c == '+' || c == '-') {
						lTemp = Float.parseFloat(left); 
						rTemp = Float.parseFloat(right);
						
						if(middle == '+') left = String.valueOf(lTemp + rTemp);
						else left = String.valueOf(lTemp - rTemp);
						
						middle = c;
						right = "";
					} else right += c;
				}
			}
			
			// Do last calculation
			lTemp = Float.parseFloat(left); 
			rTemp = Float.parseFloat(right);
			if(middle == '+') left = String.valueOf(lTemp + rTemp);
			else left = String.valueOf(lTemp - rTemp);

			return Float.parseFloat(left);
		} else {
			return Float.parseFloat(input);
		}
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
