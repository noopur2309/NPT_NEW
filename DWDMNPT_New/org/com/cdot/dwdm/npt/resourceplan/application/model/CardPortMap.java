package application.model;

public class CardPortMap {
	private String NodeKey;
	private String End1CardType;
	private String End1Location;
	private String End1Port;	
	private String End2CardType;
	private String End2Location;
	private String End2Port;	
	public CardPortMap() {
		super();		
	}
	public String getNodeKey() {
		return NodeKey;
	}
	public void setNodeKey(String nodeKey) {
		NodeKey = nodeKey;
	}
	public String getEnd1CardType() {
		return End1CardType;
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
	public CardPortMap(String nodeKey, String end1CardType, String end1Location, String end1Port, String end2CardType,
			String end2Location, String end2Port) {
		super();
		NodeKey = nodeKey;
		End1CardType = end1CardType;
		End1Location = end1Location;
		End1Port = end1Port;
		End2CardType = end2CardType;
		End2Location = end2Location;
		End2Port = end2Port;
	}
	@Override
	public String toString() {
		return "CardPortMap [NodeKey=" + NodeKey + ", End1CardType=" + End1CardType + ", End1Location=" + End1Location
				+ ", End1Port=" + End1Port + ", End2CardType=" + End2CardType + ", End2Location=" + End2Location
				+ ", End2Port=" + End2Port + "]";
	}


}
