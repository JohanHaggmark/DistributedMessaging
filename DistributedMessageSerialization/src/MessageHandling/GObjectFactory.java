package MessageHandling;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;

public class GObjectFactory {
	GObject gObject;
	public GObjectFactory(String shape, String color, String x, String y, String width, String height) {
		gObject = new GObject(
				new Shape(shape),
				getColor(color),
				Integer.parseInt(x),
				Integer.parseInt(y),
				Integer.parseInt(width),
				Integer.parseInt(height));
	}


	private Color getColor(String color) {
		if(color.equals("Red")) {
			return Color.RED;
		} else if(color.equals("Blue")) {
			return Color.BLUE;
		} else if(color.equals("Green")) {
			return Color.GREEN;
		}else if(color.equals("White")) {
			return Color.WHITE;
		}else if(color.equals("Pink")) {
			return Color.PINK;
		}
		return Color.RED;
	}
	public GObject getGObject() {
		return gObject;
	}
	
	public static String getStringFromColor(Color color) {
		if(color.equals(Color.RED)) {
			return "Red";
		}else if(color.equals(Color.BLUE)){
			return "Blue";
		}else if(color.equals(Color.GREEN)){
			return "Green";
		}else if(color.equals(Color.WHITE)){
			return "White";
		}else if(color.equals(Color.PINK)){
			return "Pink";
		}
		return "RED";
	}
	
	public static LinkedList<String> addObjects(HashMap<String,String> map) {
		LinkedList<String> objects = new LinkedList<String>();
		for(String key : map.keySet()) {
			if(map.get(key).equals("add")) {
				objects.add(key);
			}
		}
		return objects;
	}
	
	public static LinkedList<String> removeObjects(HashMap<String,String> map) {
		LinkedList<String> objects = new LinkedList<String>();
		for(String key : map.keySet()) {
			if(map.get(key).equals("remove")) {
				objects.add(key);
			}
		}
		return  objects;
	}
	
	
	public static GObject createGObjectByString(String s) {
		String[] split = s.split("'-'");
		return new GObjectFactory(split[0], split[1], split[2], split[3], split[4], split[5]).getGObject();
	}
	
	public static String getStringOfObject(GObject object) {
		return (object.getShape().getType() + "'-'" + GObjectFactory.getStringFromColor(object.getColor()) + "'-'"
				+ String.valueOf(object.getX()) + "'-'" + String.valueOf(object.getY()) + "'-'"
				+ String.valueOf(object.getWidth()) + "'-'" + String.valueOf(object.getHeight()));
	}
}
