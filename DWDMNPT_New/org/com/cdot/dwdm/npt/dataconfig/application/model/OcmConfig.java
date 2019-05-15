package application.model;

public class OcmConfig {
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	private String CardType;
	private int CardSubType;
	private int OcmId;
	public OcmConfig() {
		super();
		// TODO Auto-generated constructor stub
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
	public int getRack() {
		return Rack;
	}
	public void setRack(int rack) {
		Rack = rack;
	}
	public int getSbrack() {
		return Sbrack;
	}
	public void setSbrack(int sbrack) {
		Sbrack = sbrack;
	}
	public int getCard() {
		return Card;
	}
	public void setCard(int card) {
		Card = card;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	
	public int getCardSubType() {
		return CardSubType;
	}
	public void setCardSubType(int cardSubType) {
		CardSubType = cardSubType;
	}
	public int getOcmId() {
		return OcmId;
	}
	public void setOcmId(int ocmId) {
		OcmId = ocmId;
	}
	@Override
	public String toString() {
		return "OcmConfig [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Rack=" + Rack + ", Sbrack=" + Sbrack
				+ ", Card=" + Card + ", CardType=" + CardType + ", CardSubType=" + CardSubType + ", OcmId=" + OcmId
				+ "]";
	}
	public OcmConfig(int networkId, int nodeId, int rack, int sbrack, int card, String cardType, int cardSubType,
			int ocmId) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Rack = rack;
		Sbrack = sbrack;
		Card = card;
		CardType = cardType;
		CardSubType = cardSubType;
		OcmId = ocmId;
	}
	

	
}
