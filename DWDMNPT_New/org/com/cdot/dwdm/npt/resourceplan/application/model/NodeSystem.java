package application.model;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import application.service.CompareRack;

public class NodeSystem {
	private int id;
	private SortedMap<Integer,Rack> rack;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public SortedMap<Integer,Rack> getRack() {
		return rack;
	}
	public void setRack(SortedMap<Integer,Rack> rack) {
		this.rack = rack;
	}
	public NodeSystem(int id, SortedMap<Integer,Rack> rack) {
		super();
		
		this.id = id;
		this.rack = rack;
	}
	
	public NodeSystem(int id) {
		super();
		this.id = id;
		this.rack= new TreeMap<Integer,Rack>();
	}
	public NodeSystem() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "NodeSystem [id=" + id + ", rack=" + rack + "]";
	}
	
	
	

}
