package application.model;

public class Bom {	
	private int NodeId;
	private String StationName;
	private String SiteName;
	private String Name;
	private int Quantity;
	private int Price;
	private int TotalPrice;
	private String PartNo;
	private int PowerConsumption;/**Added as per the requirement*/
	private int TypicalPower;
	private String RevisionCode;
	

	private int Category;
	private String Location;
	private int EquipmentId;
	public Bom() {
		super();		
	}

	public int getNodeId() {
		return NodeId;
	}
	public void setNodeId(int nodeId) {
		NodeId = nodeId;
	}

	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public int getPrice() {
		return Price;
	}
	public void setPrice(int price) {
		Price = price;
	}
	public int getTotalPrice() {
		return TotalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		TotalPrice = totalPrice;
	}
	public String getPartNo() {
		return PartNo;
	}
	public void setPartNo(String partNo) {
		PartNo = partNo;
	}
	public int getPowerConsumption() {
		return PowerConsumption;
	}
	public void setPowerConsumption(int powerConsumption) {
		PowerConsumption = powerConsumption;
	}
	public int getCategory() {
		return Category;
	}
	public void setCategory(int category) {
		Category = category;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public String getSiteName() {
		return SiteName;
	}

	public void setSiteName(String siteName) {
		SiteName = siteName;
	}
	
	public String getRevisionCode() {
		return RevisionCode;
	}

	public void setRevisionCode(String revisionCode) {
		RevisionCode = revisionCode;
	}

	public int getTypicalPower() {
		return TypicalPower;
	}

	public void setTypicalPower(int typicalPower) {
		TypicalPower = typicalPower;
	}

	
	public int getEquipmentId() {
		return EquipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		EquipmentId = equipmentId;
	}

	@Override
	public String toString() {
		return "Bom [NodeId=" + NodeId + ", StationName=" + StationName + ", SiteName=" + SiteName + ", Name=" + Name
				+ ", Quantity=" + Quantity + ", Price=" + Price + ", TotalPrice=" + TotalPrice + ", PartNo=" + PartNo
				+ ", PowerConsumption=" + PowerConsumption + ", TypicalPower=" + TypicalPower + ", RevisionCode="
				+ RevisionCode + ", Category=" + Category + ", Location=" + Location + ", EquipmentId=" + EquipmentId
				+ "]";
	}

	public Bom(int nodeId, String stationName, String siteName, String name, int quantity, int price, int totalPrice,
			String partNo, int powerConsumption, int typicalPower, String revisionCode, int category, String location,
			int equipmentId) {
		super();
		NodeId = nodeId;
		StationName = stationName;
		SiteName = siteName;
		Name = name;
		Quantity = quantity;
		Price = price;
		TotalPrice = totalPrice;
		PartNo = partNo;
		PowerConsumption = powerConsumption;
		TypicalPower = typicalPower;
		RevisionCode = revisionCode;
		Category = category;
		Location = location;
		EquipmentId = equipmentId;
	}

	
			
}
