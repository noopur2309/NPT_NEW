package application.model;

public class Inventory {	
	private int NetworkId;
	private int NodeId;
	private int EquipmentId;
	private double Quantity;
	public Inventory() {
		super();		
	}
	public Inventory(int networkId, int nodeId, int equipmentId, double quantity) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		EquipmentId = equipmentId;
		Quantity = quantity;
	}
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public int getNodeId() {
		return NodeId;
	}
	public void setNodeId(int nodeId) {
		NodeId = nodeId;
	}
	public int getEquipmentId() {
		return EquipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		EquipmentId = equipmentId;
	}
	public double getQuantity() {
		return Quantity;
	}
	public void setQuantity(double quantity) {
		Quantity = quantity;
	}
	@Override
	public String toString() {
		return "Inventory [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", EquipmentId=" + EquipmentId
				+ ", Quantity=" + Quantity + "]";
	}
	
		
}
