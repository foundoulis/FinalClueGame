package card;

import player.Player;

public class Card {
	private String name;
	private CardType type;
	private boolean dealt;
	
	public Card(CardType type, String name) {
		this.type = type;
		this.name = name;
		dealt = false;
	}
	
	@Override
	public boolean equals(Object object) {
		return
				object instanceof Card &&
				((Card) object).getName() == name &&
				((Card) object).getType() == type &&
				((Card) object).hasBeenDealt() == dealt;
	}
	
	public CardType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasBeenDealt() {
		return dealt;
	}
	
	public void dealToPlayer(Player player) {
		player.addCard(this);
		dealt = true;
	}
	
	@Override
	public String toString(){
		return type.toString() + " " + name;
	}
	
	public void isSolution(){
		dealt = true;
	}
}
