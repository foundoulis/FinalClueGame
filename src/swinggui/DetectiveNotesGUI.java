package swinggui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotesGUI extends JDialog {

	public DetectiveNotesGUI() {
		setLayout(new GridLayout(3, 2));
		add(createPanel("People"));
		add(createPanel("Rooms"));
		add(createPanel("Weapons"));
	}
	
	private JPanel createPanel(String title) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), title));
		return panel;
	}

}
