package swinggui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;
import card.Card;

@SuppressWarnings("serial")
public class AccuseWindow extends JFrame {
	
	private List<JComboBox<String>> listOfComboBoxes;

	public AccuseWindow(List<Card> people, List<Card> weapons, String playerRoomLocation) {
		this.listOfComboBoxes = new ArrayList<JComboBox<String>>();
		
		setLayout(new GridLayout(4,1));
		add(createPanel(playerRoomLocation));
		add(createPanel(people, "People"));
		add(createPanel(weapons, "Weapons"));
		add(createAccuseButton());
		
		this.setSize(playerRoomLocation.length()*10, 200);
		this.setVisible(true);
	}

	private Component createAccuseButton() {
		JButton accuse = new JButton();
		accuse.setText("Accuse!");
		accuse.addActionListener(new CloseListener(this));
		return accuse;
	}
	
	private class CloseListener implements ActionListener {
		
		private AccuseWindow window;
		
		public CloseListener(AccuseWindow currentFrame) {
			this.window = currentFrame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Board.getInstance().setPlayerAccusation(window.getComboBoxesValue());
			this.window.dispose();
		}
	}
	
	public String[] getComboBoxesValue() {
		String [] strArray = new String[2];
		strArray[0] = listOfComboBoxes.get(0).getSelectedItem().toString();
		strArray[1] = listOfComboBoxes.get(1).getSelectedItem().toString();
		System.out.println(strArray[0] + strArray[1]);
		return strArray;
	}

	private Component createPanel(List<Card> people, String Name) {
		JPanel outerpart = new JPanel();
		outerpart.setBorder(new TitledBorder(new EtchedBorder(), Name));
		outerpart.add(createCombobox(people));
		return outerpart;
	}
	
	private Component createPanel(String name) {
		JPanel outerpart = new JPanel();
		outerpart.setBorder(new TitledBorder(new EtchedBorder(), "Room"));
		outerpart.add(createCombobox(name));
		return outerpart;
	}

	private Component createCombobox(String roomName) {
		JTextField text = new JTextField();
		text.setText(roomName);
		text.setEditable(false);
		return text;
	}

	private Component createCombobox(List<Card> list) {
		JComboBox<String> dropDown = new JComboBox<String>();
		for (Card c : list) {
			dropDown.addItem(c.getName());
		}
		this.listOfComboBoxes.add(dropDown);
		return dropDown;
	}

}
