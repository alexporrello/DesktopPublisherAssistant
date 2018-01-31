package main;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

import mslinks.ShellLink;
import ui.Tools;

public class Installer extends JFrame {
	private static final long serialVersionUID = 1454580848392670182L;

	public static final String programLocation = System.getenv("APPDATA") + "\\Desktop Publisher Assistant-app\\Desktop_Publisher_Assistant.jar";
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				Tools.copyOutResource("Desktop_Publisher_Assistant.jar", programLocation);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				ShellLink.createLink(programLocation, FileSystemView.getFileSystemView().getHomeDirectory() + "\\Desktop Publisher Assistant.lnk");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
