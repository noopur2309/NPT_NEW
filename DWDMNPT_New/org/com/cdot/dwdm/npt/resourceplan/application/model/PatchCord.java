package application.model;

public class PatchCord {
//	private String NodeKey;
	private int NetworkId;
	private int NodeId;
	private int EquipmentId;
	private String CordType;
	private String End1CardType;
	private String End1Location;
	private String End1Port;	
	private String End2CardType;
	private String End2Location;
	private String End2Port;
	private int Length;
	private String DirectionEnd1;
	private String[] DirectionEnd2=new String[2];
	public PatchCord() {
		super();		
	}

//	public String getNodeKey() {
//		return NodeKey;
//	}
//
//	public void setNodeKey(String nodeKey) {
//		NodeKey = nodeKey;
//	}	

	public String getEnd1CardType() {
		return End1CardType;
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

	public void setDirectionEnd2(String[] directionEnd2) {
		DirectionEnd2 = directionEnd2;
	}

	public void setEnd1CardType(String end1CardType) {
		End1CardType = end1CardType;
	}

	public String getEnd1Location() {
		return End1Location;
	}

	public void setEnd1Location(String end1Location) {
		End1Location = end1Location;
	}

	public String getEnd1Port() {
		return End1Port;
	}

	public void setEnd1Port(String end1Port) {
		End1Port = end1Port;
	}

	public int getEquipmentId() {
		return EquipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		EquipmentId = equipmentId;
	}

	public String getCordType() {
		return CordType;
	}

	public void setCordType(String cordType) {
		CordType = cordType;
	}

	public String getEnd2CardType() {
		return End2CardType;
	}

	public void setEnd2CardType(String end2CardType) {
		End2CardType = end2CardType;
	}

	public String getEnd2Location() {
		return End2Location;
	}

	public void setEnd2Location(String end2Location) {
		End2Location = end2Location;
	}

	public String getEnd2Port() {
		return End2Port;
	}

	public void setEnd2Port(String end2Port) {
		End2Port = end2Port;
	}

	public int getLength() {
		return Length;
	}

	public void setLength(int length) {
		Length = length;
	}

	public String getDirectionEnd1() {
		return DirectionEnd1;
	}

	public void setDirectionEnd1(String directionEnd1) {
		DirectionEnd1 = directionEnd1;
	}

	public String getDirectionEnd2() {
		return DirectionEnd2[0];
	}

	public void setDirectionEnd2(String directionEnd2) {
		DirectionEnd2[0] = directionEnd2;
	}
	
	@Override
	public String toString() {
		return "PatchCord [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", EquipmentId=" + EquipmentId + ", CordType=" + CordType
				+ ", End1CardType=" + End1CardType + ", End1Location=" + End1Location + ", End1Port=" + End1Port
				+ ", End2CardType=" + End2CardType + ", End2Location=" + End2Location + ", End2Port=" + End2Port
				+ ", Length=" + Length + ", DirectionEnd1=" + DirectionEnd1 + ", DirectionEnd2=" + DirectionEnd2 + "]";
	}

	public PatchCord(/*String nodeKey*/int networkId,int nodeId, int equipmentId, String cordType, String end1CardType, String end1Location,
			String end1Port, String end2CardType, String end2Location, String end2Port, int length,
			String directionEnd1,String... directionEnd2) {
		super();
//		NodeKey = nodeKey;
		NetworkId = networkId;
		NodeId = nodeId;
		EquipmentId = equipmentId;
		CordType = cordType;
		End1CardType = end1CardType;
		End1Location = end1Location;
		End1Port = end1Port;
		End2CardType = end2CardType;
		End2Location = end2Location;
		End2Port = end2Port;
		Length = length;
		DirectionEnd1 = directionEnd1;
		if(directionEnd2.length>0)
			DirectionEnd2[0]=directionEnd2[0].toString();
		else
			DirectionEnd2[0]="";

	}
}
