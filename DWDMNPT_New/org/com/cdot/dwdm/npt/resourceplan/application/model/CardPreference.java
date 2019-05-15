package application.model;
/**
 * Used for storing the  
 * @author sunaina
 *
 */
public class CardPreference {	
	private int NetworkId;
	private int NodeId;
	private String CardFeature;	
	private String CardType;	
	private int EquipmentId;	
	private int Preference; //valid values 1/2/3 according to the usage preference
	public CardPreference() {
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
	public int getEquipmentId() {
		return EquipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		EquipmentId = equipmentId;
	}
	
	public int getPreference() {
		return Preference;
	}
	public void setPartPreference(int partPreference) {
		Preference = partPreference;
	}	
	
	public String getCardFeature() {
		return CardFeature;
	}
	public void setCardFeature(String cardFeature) {
		CardFeature = cardFeature;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	public void setPreference(int preference) {
		Preference = preference;
	}
	public CardPreference(int networkId, int nodeId, String cardFeature, String cardType, int equipmentId,
			int preference) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		CardFeature = cardFeature;
		CardType = cardType;
		EquipmentId = equipmentId;
		Preference = preference;
	}
	@Override
	public String toString() {
		return "CardPreference [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", CardFeature=" + CardFeature
				+ ", CardType=" + CardType + ", EquipmentId=" + EquipmentId + ", Preference=" + Preference + "]";
	}
	
	
}
