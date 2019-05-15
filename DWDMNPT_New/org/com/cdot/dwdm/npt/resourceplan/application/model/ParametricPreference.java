package application.model;
/**
 * Used for storing the equipment preference and quantity present in the inventory
 * @author sunaina
 *
 */
public class ParametricPreference {	
	private int NetworkId;
	private int NodeId;
	private String Category;
	private String CardType;
	private String Parameter;// can be demand, direction, etc. 		
	private String Value;
	private String SerialNo;
	public ParametricPreference() {
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
	public String getParameter() {
		return Parameter;
	}
	public void setParameter(String parameter) {
		Parameter = parameter;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getSerialNo() {
		return SerialNo;
	}
	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}
	@Override
	public String toString() {
		return "ParametricPreference [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Category=" + Category
				+ ", CardType=" + CardType + ", Parameter=" + Parameter + ", Value=" + Value + ", SerialNo=" + SerialNo
				+ "]";
	}
	public ParametricPreference(int networkId, int nodeId, String category, String cardType, String parameter,
			String value, String serialNo) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Category = category;
		CardType = cardType;
		Parameter = parameter;
		Value = value;
		SerialNo = serialNo;
	}		
}
