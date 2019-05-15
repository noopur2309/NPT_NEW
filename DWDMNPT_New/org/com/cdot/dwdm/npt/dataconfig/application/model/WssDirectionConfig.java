package application.model;

public class WssDirectionConfig {
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	private String CardType;
	private int CardSubType;	
	private String WssDirection;
	private int WssDirectionType;
	private int LaserStatus;
	private int AttenuationConfigMode;
	private int FixedAttenuation;
	private int PreEmphasisTriggerPowerDiff;
	private int Attenuation;
	
	
	public WssDirectionConfig() {
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



	public String getWssDirection() {
		return WssDirection;
	}


	public void setWssDirection(String wssDirection) {
		WssDirection = wssDirection;
	}


	public int getLaserStatus() {
		return LaserStatus;
	}

	public void setLaserStatus(int laserStatus) {
		LaserStatus = laserStatus;
	}

	public int getAttenuationConfigMode() {
		return AttenuationConfigMode;
	}

	public void setAttenuationConfigMode(int attenuationConfigMode) {
		AttenuationConfigMode = attenuationConfigMode;
	}

	public int getFixedAttenuation() {
		return FixedAttenuation;
	}

	public void setFixedAttenuation(int fixedAttenuation) {
		FixedAttenuation = fixedAttenuation;
	}

	public int getAttenuation() {
		return Attenuation;
	}

	public void setAttenuation(int attenuation) {
		Attenuation = attenuation;
	}


	

	public int getWssDirectionType() {
		return WssDirectionType;
	}



	public void setWssDirectionType(int wssDirectionType) {
		WssDirectionType = wssDirectionType;
	}



	public int getPreEmphasisTriggerPowerDiff() {
		return PreEmphasisTriggerPowerDiff;
	}



	public void setPreEmphasisTriggerPowerDiff(int preEmphasisTriggerPowerDiff) {
		PreEmphasisTriggerPowerDiff = preEmphasisTriggerPowerDiff;
	}

	
}
