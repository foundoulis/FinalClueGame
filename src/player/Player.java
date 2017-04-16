package player;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import clueGame.BoardCell;
import clueGame.Solution;
import card.Card;


public abstract class Player {
	private String name;
	protected int row;
	protected int col;
	private Color color;
	protected List<Card> myCards;
	protected List<Card> seenCards;
	
	public Player(String name, Color color, int row, int col){
	    this.name = name;
	    this.color = color;
	    this.row = row;
	    this.col = col;
	    
	    myCards = new ArrayList<Card>();
	    seenCards = new ArrayList<Card>();
	}

	public abstract Card disproveSuggestion(Solution suggestion);
	
	public void addCard(Card card) {
		myCards.add(card);
	}
	
	public Color convertColor(String strColor) {
		Color color;
		try {
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());
			color = (Color)field.get(null);
		} catch (Exception e) {
			color = null;
		}
		return color;
	}

	public String getName() {
		return name;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return col;
	}
	
	public void setLocation(int r, int c) {
		row = r;
		col = c;
	}
	
	public void moveToTarget(BoardCell target) {
		setLocation(target.getRow(), target.getColumn());
	}

	public Color getColor() {
		return color;
	}
	
	public List<Card> getCards() {
		return myCards;
	}
	
	public void setCards(List<Card> cards) {
		myCards = cards;
	}

	public List<Card> getSeenCards() {
		return seenCards;
	}
	
	@Override
	public String toString(){
		return this.getClass() + ": " + name + ". Hand: " + myCards.toString();
	}
}
