package application.model;

public class AmplifierConfig {
	public String getDirection() {
		return Direction;
	}



	public void setDirection(String direction) {
		Direction = direction;
	}

	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	
	private String Direction;
	private String AmpType;
	private int AmpStatus;
	private float GainLimit;
	private int ConfigurationMode;
	private float VoaAttenuation;
	private String EdfaDirId;
		
	public AmplifierConfig() {
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

	public int getAmpStatus() {
		return AmpStatus;
	}

	public void setAmpStatus(int ampStatus) {
		AmpStatus = ampStatus;
	}

	public float getGainLimit() {
		return GainLimit;
	}

	public void setGainLimit(float gainLimit) {
		GainLimit = gainLimit;
	}

	public int getConfigurationMode() {
		return ConfigurationMode;
	}

	public void setConfigurationMode(int configurationMode) {
		ConfigurationMode = configurationMode;
	}

	public float getVoaAttenuation() {
		return VoaAttenuation;
	}

	public void setVoaAttenuation(float voaAttenuation) {
		VoaAttenuation = voaAttenuation;
	}

	public String getAmpType() {
		return AmpType;
	}

	public void setAmpType(String ampType) {
		AmpType = ampType;
	}



	public String getEdfaDirId() {
		return EdfaDirId;
	}



	public void setEdfaDirId(String edfaDirId) {
		EdfaDirId = edfaDirId;
	}
	
	

	
}
