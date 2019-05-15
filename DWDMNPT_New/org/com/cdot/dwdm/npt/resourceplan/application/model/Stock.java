package application.model;
/**
 * Used for storing the equipment preference and quantity present in the inventory
 * @author sunaina
 *
 */
public class Stock {	
	private int NetworkId;
	private int NodeId;
	private String Category;
	private String CardType;
	private int Quantity;		
	private int UsedQuantity;
	private String SerialNo;
	private String Status;
	public Stock() {
		super();		
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
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}	
	public int getUsedQuantity() {
		return UsedQuantity;
	}
	public void setUsedQuantity(int usedQuantity) {
		UsedQuantity = usedQuantity;
	}
	
	
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
	
	public String getSerialNo() {
		return SerialNo;
	}
	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}
	@Override
	public String toString() {
		return "Stock [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Category=" + Category + ", CardType="
				+ CardType + ", Quantity=" + Quantity + ", UsedQuantity=" + UsedQuantity + ", SerialNo=" + SerialNo
				+ ", Status=" + Status + "]";
	}
	public Stock(int networkId, int nodeId, String category, String cardType, int quantity, int usedQuantity,
			String serialNo, String status) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Category = category;
		CardType = cardType;
		Quantity = quantity;
		UsedQuantity = usedQuantity;
		SerialNo = serialNo;
		Status = status;
	}
	
}
