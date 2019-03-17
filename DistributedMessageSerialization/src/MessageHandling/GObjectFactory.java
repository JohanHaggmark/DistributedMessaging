package MessageHandling;

import java.awt.Color;

public class GObjectFactory {
	GObject gObject;
	String add;
	public GObjectFactory(String shape, String color, String x, String y, String width, String height, String add) {
		gObject = new GObject(
				new Shape(shape),
				getColor(color),
				Integer.parseInt(x),
				Integer.parseInt(y),
				Integer.parseInt(width),
				Integer.parseInt(height));
				this.add = add;
	}
	
	public String getAdd() {
		return add;
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
	
	
}
