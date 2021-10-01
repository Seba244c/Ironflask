package dk.sebsa.ironflask.launcher;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static void main(String[] args) {
		
	}
	
	public static void runEngine() {
		try {
			if(OS.indexOf("win") >= 0) {
				Runtime.getRuntime().exec(".\\ieditor-int.exe", null, new File(".\\"));
				return;
			} else if(OS.indexOf("mac") >= 0) {
				Runtime.getRuntime().exec("./startMainEditor");
				return;
			}
		} catch (IOException e) {
			JFrame jFrame = new JFrame();
			JOptionPane.showMessageDialog(jFrame, "Couldnt launch Ironflask Editor");
			JOptionPane.showMessageDialog(jFrame, e);
			return;
		}

		JFrame jFrame = new JFrame();
		JOptionPane.showMessageDialog(jFrame, "OS, " + OS + ", is not supported");
	}
}
