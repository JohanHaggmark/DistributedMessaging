/**
 *
 * @author brom
 */

package DCAD;

import java.io.Serializable;

final class Shape implements Serializable{
    private String type;

    private Shape(String type) {
	this.type = type;
    }

    public Shape() {
    	
    }
    @Override
    public String toString() {return type;}

    public static Shape OVAL  = new Shape("OVAL");
    public static Shape RECTANGLE = new Shape("RECTANGLE");
    public static Shape LINE = new Shape("LINE");
    public static Shape FILLED_RECTANGLE = new Shape("FILLED_RECTANGLE");
    public static Shape FILLED_OVAL = new Shape("FILLED_OVAL");

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static Shape getOVAL() {
		return OVAL;
	}

	public static void setOVAL(Shape oVAL) {
		OVAL = oVAL;
	}

	public static Shape getRECTANGLE() {
		return RECTANGLE;
	}

	public static void setRECTANGLE(Shape rECTANGLE) {
		RECTANGLE = rECTANGLE;
	}

	public static Shape getLINE() {
		return LINE;
	}

	public static void setLINE(Shape lINE) {
		LINE = lINE;
	}

	public static Shape getFILLED_RECTANGLE() {
		return FILLED_RECTANGLE;
	}

	public static void setFILLED_RECTANGLE(Shape fILLED_RECTANGLE) {
		FILLED_RECTANGLE = fILLED_RECTANGLE;
	}

	public static Shape getFILLED_OVAL() {
		return FILLED_OVAL;
	}

	public static void setFILLED_OVAL(Shape fILLED_OVAL) {
		FILLED_OVAL = fILLED_OVAL;
	}
}
