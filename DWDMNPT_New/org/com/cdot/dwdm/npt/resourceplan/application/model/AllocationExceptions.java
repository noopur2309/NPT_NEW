package application.model;
/**
 * Used for storing the equipment preference and quantity present in the inventory
 * @author sunaina
 *
 */
public class AllocationExceptions {	
	private int NetworkId;
	private int NodeId;
	private String SerialNo;	
	private String CardType;
	private String Location; 		
	private String Port;
	private String Type;//faulty / reserved
//	private String Status; // used / free
	
	
	public AllocationExceptions() {
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

	public String getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}

	public String getCardType() {
		return CardType;
	}

	public void setCardType(String cardType) {
		CardType = cardType;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getPort() {
		return Port;
	}

	public void setPort(String port) {
		Port = port;
	}

//	public String getStatus() {
//		return Status;
//	}
//
//	public void setStatus(String status) {
//		Status = status;
//	}

	public String getType() {
		return Type;
	}

	public void setType(String excepType) {
		Type = excepType;
	}

	@Override
	public String toString() {
		return "AllocationExceptions [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", SerialNo=" + SerialNo
				+ ", CardType=" + CardType + ", Location=" + Location + ", Port=" + Port /*+ ", Status=" + Status*/
				+ ", ExcepType=" + Type + "]";
	}

	public AllocationExceptions(int networkId, int nodeId, String serialNo, String cardType, String location,
			String port, String status, String excepType) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		SerialNo = serialNo;
		CardType = cardType;
		Location = location;
		Port = port;
		Type = excepType;
//		Status = status;
		
	}		
}
