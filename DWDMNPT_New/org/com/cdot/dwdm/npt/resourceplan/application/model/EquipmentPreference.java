package application.model;
/**
 * Used for storing the equipment preference and quantity present in the inventory
 * @author sunaina
 *
 */
public class EquipmentPreference {	
	private int NetworkId;
	private int NodeId;
	private String Category;
	private String CardType;	
	private int Preference; //valid values 1/2/3 according to the usage preference	
	private String Redundancy;	//Yes/No/NA
	public EquipmentPreference() {
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
	public int getPreference() {
		return Preference;
	}
	public void setPreference(int preference) {
		Preference = preference;
	}	
	
	public String getRedundancy() {
		return Redundancy;
	}
	public void setRedundancy(String redundancy) {
		Redundancy = redundancy;
	}
	@Override
	public String toString() {
		return "EquipmentPreference [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Category=" + Category
				+ ", CardType=" + CardType + ", Preference=" + Preference + ", Redundancy=" + Redundancy + "]";
	}
	public EquipmentPreference(int networkId, int nodeId, String category, String cardType, int preference,
			String redundancy) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Category = category;
		CardType = cardType;
		Preference = preference;
		Redundancy = redundancy;
	}
	
}
