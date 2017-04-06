package clueGame;

import card.Card;

public class Solution {
	private String person;
	private String room;
	private String weapon;
	
	public Solution(String person, String room, String weapon) {
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	public Solution(Card person, Card room, Card weapon) {
		this(person.getName(), room.getName(), weapon.getName());
	}
	
	public boolean contains(Card c){
		return contains(c.getName());
	}
	
	public boolean contains(String s){
		return s.equals(person) || s.equals(room) || s.equals(weapon);
	}

	public String getPerson() {
		return person;
	}

	public String getRoom() {
		return room;
	}

	public String getWeapon() {
		return weapon;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof Solution){
			Solution s = (Solution) other;
			if(s.getPerson().equals(person) && s.getRoom().equals(room) && s.getWeapon().equals(weapon)){
				return true;
			}
		}
		return false;
	}
}
