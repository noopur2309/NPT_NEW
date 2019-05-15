package application.model;

import java.util.HashSet;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import application.service.CompareCard;

public class Sbrack {
	private int id;		
	private SortedMap<Integer,Card> slots;

	public Sbrack(int id, SortedMap<Integer,Card> slots) {
		super();
		this.id = id;
		this.slots = slots;
	}

	@Override
	public String toString() {
		return "Sbrack [id=" + id + ", slots=" + slots + "]";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SortedMap<Integer,Card> getSlots() {
		return slots;
	}

	public void setSlots(SortedMap<Integer,Card> slots) {
		this.slots = slots;
	}
	
	

	public Sbrack(int id) {
		super();
		this.id = id;
		this.slots= new TreeMap<Integer,Card>();
	}	
}
