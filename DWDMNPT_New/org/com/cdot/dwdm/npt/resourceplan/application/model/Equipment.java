package application.model;

public class Equipment {	
	private int EquipmentId;
	private String Name;
	private String PartNo;
	private int PowerConsumption;
	private int TypicalPower;
	private int SlotSize;
	private String Details;
	private float Price;
	private int Category;
	private String RevisionCode;	
	public Equipment() {
		super();		
	}
	

	public int getEquipmentId() {
		return EquipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		EquipmentId = equipmentId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
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
	public int getSlotSize() {
		return SlotSize;
	}
	public void setSlotSize(int slotSize) {
		SlotSize = slotSize;
	}
	public String getDetails() {
		return Details;
	}
	public void setDetails(String details) {
		Details = details;
	}	
	public float getPrice() {
		return Price;
	}

	public void setPrice(float price) {
		Price = price;
	}


	public int getCategory() {
		return Category;
	}


	public void setCategory(int category) {
		Category = category;
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


	public Equipment(int equipmentId, String name, String partNo, int powerConsumption, int typicalPower, int slotSize,
			String details, float price, int category, String revisionCode) {
		super();
		EquipmentId = equipmentId;
		Name = name;
		PartNo = partNo;
		PowerConsumption = powerConsumption;
		TypicalPower = typicalPower;
		SlotSize = slotSize;
		Details = details;
		Price = price;
		Category = category;
		RevisionCode = revisionCode;
	}

	@Override
	public String toString() {
		return "Equipment [EquipmentId=" + EquipmentId + ", Name=" + Name + ", PartNo=" + PartNo + ", PowerConsumption="
				+ PowerConsumption + ", TypicalPower=" + TypicalPower + ", SlotSize=" + SlotSize + ", Details="
				+ Details + ", Price=" + Price + ", Category=" + Category + ", RevisionCode=" + RevisionCode + "]";
	}

}
