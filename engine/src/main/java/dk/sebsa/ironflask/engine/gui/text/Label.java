package dk.sebsa.ironflask.engine.gui.text;

public class Label {
	private char[] charArray;
	private String text;
	public Font font;

	public Label(String text, Font font) {
		charArray = text.toCharArray();
		this.text = text;
		this.font = font;
	}
	
	public final String getText() {
		return text;
	}

	public char[] getCharArray() {
		return charArray;
	}
}
