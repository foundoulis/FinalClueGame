package swinggui;

import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;

public class BoardGUI extends JPanel {

	public BoardGUI(Board b) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), "Board"));
	}

}
