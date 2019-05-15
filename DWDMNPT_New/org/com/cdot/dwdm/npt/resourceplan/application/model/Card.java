package application.model;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Card {
	private int id;
	private String type;
	private SortedMap<Integer, String> ports;

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Card(int id, String type, SortedMap<Integer, String> ports) {
		super();
		this.id = id;
		this.type = type;
		this.ports = ports;
	}

	public Card(int id, String type) {
		super();
		this.id = id;
		this.type = type;
		this.ports= new TreeMap<Integer,String>();
	}
	
	
	@Override
	public String toString() {
		return "Card [id=" + id + ", type=" + type + ", ports=" + ports + "]";
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public SortedMap<Integer,String> getPorts() {
		return ports;
	}

	public void setPorts(SortedMap<Integer, String> ports) {
		this.ports = ports;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
