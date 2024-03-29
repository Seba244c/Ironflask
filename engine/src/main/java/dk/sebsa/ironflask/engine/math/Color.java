package dk.sebsa.ironflask.engine.math;

public class Color {
	public float r = 1;
	public float g = 1;
	public float b = 1;
	public float a = 1;
	
	public Color(float r, float g, float b, float a) {
		this.r = Mathf.clamp(r, 0, 1);
		this.g = Mathf.clamp(g, 0, 1);
		this.b = Mathf.clamp(b, 0, 1);
		this.a = Mathf.clamp(a, 0, 1);
	}
	
	public Color(float r, float g, float b) {
		this.r = Mathf.clamp(r, 0, 1);
		this.g = Mathf.clamp(g, 0, 1);
		this.b = Mathf.clamp(b, 0, 1);
	}
	
	public static final Color black() {return new Color(0, 0, 0);}
	public static final Color white() {return new Color(1, 1, 1);}
	public static final Color red() {return new Color(1, 0, 0);}
	public static final Color green() {return new Color(0, 1, 0);}
	public static final Color blue() {return new Color(0, 0, 1);}
	public static final Color grey() {return new Color(0.5f, 0.5f, 0.5f);}
	public static final Color dimGrey() {return new Color(0.35f, 0.35f, 0.35f);}
	public static final Color darkGrey() {return new Color(0.3f, 0.3f, 0.3f);}
	public static final Color wine() {return new Color(0.5f, 0, 0);}
	public static final Color forest() {return new Color(0, 0.5f, 0);}
	public static final Color marine() {return new Color(0, 0, 0.5f);}
	public static final Color yellow() {return new Color(1, 1, 0);}
	public static final Color cyan() {return new Color(0, 1, 1);}
	public static final Color magenta() {return new Color(1, 0, 1);}
	public static final Color transparent() {return new Color(0, 0, 0, 0);}
	
	public static final Color logTrace() {return white();}
	public static final Color logInfo() {return new Color(0.4078f, 1, 0.2f, 1);}
	public static final Color logWarning() {return new Color(1, 0.7921f, 0.2f, 1);}
	public static final Color logError() {return new Color(1, 0.3921f, 0.2f, 1);}
	
	public String toString() {
		return "("+String.valueOf(r)+", "+String.valueOf(g)+", "+String.valueOf(b)+", "+String.valueOf(a)+")";
	}
	
	public boolean compare(Color c) {
		return c.r == r && c.g == g && c.b == b && c.a == a;
	}
	
	public static float[] toFloatArray(Color c) {
		float[] colors = new float[3];
		colors[0] = c.r;
		colors[1] = c.g;
		colors[2] = c.b;
		return colors;
	}
	
	public static Color fromFloatArray(float[] c) {
		return new Color(c[0], c[1], c[2]);
	}
	
	public static Color parseColor(String name) {
		if(name.startsWith("#")) {
			java.awt.Color c = java.awt.Color.decode(name);
			
			return new Color(c.getRed() / 255, c.getGreen() / 255, c.getBlue() / 255, c.getAlpha() / 255);
		}
		
		switch (name) {
		case "black":
			return black();
		case "red":
			return red();
		case "green":
			return green();
		case "blue":
			return blue();
		case "grey":
			return grey();
		case "dimGrey":
			return dimGrey();
		case "darkGrey":
			return darkGrey();
		case "wine":
			return wine();
		case "forest":
			return forest();
		case "marine":
			return marine();
		case "yellow":
			return yellow();
		case "cyan":
			return cyan();
		case "magenta":
			return magenta();
		case "transparent":
			return transparent();
		default:
			return white();
		}
	}
}
