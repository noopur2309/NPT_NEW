package application.model;

import java.util.HashSet;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import application.service.CompareSbrack;

public class Rack {
	private int id;
	private SortedMap<Integer,Sbrack> sbrack;
	public Rack(int id, SortedMap<Integer,Sbrack> sbrack) {
		super();
		this.id = id;
		this.sbrack = sbrack;
	}
	
	@Override
	public String toString() {
		return "Rack [id=" + id + ", sbrack=" + sbrack + "]";
	}

	public SortedMap<Integer,Sbrack> getSbrack() {
		return sbrack;
	}
	public void setSbrack(SortedMap<Integer,Sbrack> sbrack) {
		this.sbrack = sbrack;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Rack() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Rack(int id) {
		super();
		this.id = id;		
		this.sbrack= new TreeMap<Integer,Sbrack>();
	}
	
	
	
	
	
}
