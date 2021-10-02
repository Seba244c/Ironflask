package dk.sebsa.ironflask.launcher;

import static org.lwjgl.glfw.GLFW.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.enums.WindowFlag;
import dk.sebsa.ironflask.engine.io.Window;

public class Main {
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static Application application;
	
	public static void main(String[] args) throws IOException {
		Window.setWindowCreationFlag(WindowFlag.Resizable, GLFW_FALSE);
		application = new Application("Ironflask Launcher", false, Main::finishedLoading);
		
		application.stack.addLayerToTop(new UILayer(application));
		
		application.run();
	}
	
	public static void finishedLoading(Application app) {
		
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
