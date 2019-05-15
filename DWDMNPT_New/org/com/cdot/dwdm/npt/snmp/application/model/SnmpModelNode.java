package application.model;

public class SnmpModelNode {
	
	private int NodeId;
	private int NodeType;
	private int NodeCapacity;
	private int east;
	private int west;
	private int north;
	private int south;
	private int northeast;
	private int northwest;
	private int southeast;
	private int southwest;
	
	
	public int getNodeId() {
		return NodeId;
	}
	public void setNodeId(int nodeId) {
		NodeId = nodeId;
	}
	public int getNodeType() {
		return NodeType;
	}
	public void setNodeType(int nodeType) {
		NodeType = nodeType;
	}	
	public int isEast() {
		return east;
	}
	public void setEast(int east) {
		this.east = east;
	}
	public int isWest() {
		return west;
	}
	public void setWest(int west) {
		this.west = west;
	}
	public int isNorth() {
		return north;
	}
	public void setNorth(int north) {
		this.north = north;
	}
	public int isSouth() {
		return south;
	}
	public void setSouth(int south) {
		this.south = south;
	}
	public int isNortheast() {
		return northeast;
	}
	public void setNortheast(int northeast) {
		this.northeast = northeast;
	}
	public int isNorthwest() {
		return northwest;
	}
	public void setNorthwest(int northwest) {
		this.northwest = northwest;
	}
	public int isSoutheast() {
		return southeast;
	}
	public void setSoutheast(int southeast) {
		this.southeast = southeast;
	}
	public int isSouthwest() {
		return southwest;
	}
	public void setSouthwest(int southwest) {
		this.southwest = southwest;
	}
	public int getNodeCapacity() {
		return NodeCapacity;
	}
	public void setNodeCapacity(int nodeCapacity) {
		NodeCapacity = nodeCapacity;
	}
	
	
}
