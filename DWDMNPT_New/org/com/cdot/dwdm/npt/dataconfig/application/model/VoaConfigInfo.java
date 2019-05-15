package application.model;

public class VoaConfigInfo {
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	
	private int Direction;
	private int ChannelType;
	private int AttenuationConfigMode;
	private float Attenuation;
	
		
	public VoaConfigInfo() {
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


	public int getDirection() {
		return Direction;
	}


	public void setDirection(int direction) {
		Direction = direction;
	}


	public int getChannelType() {
		return ChannelType;
	}


	public void setChannelType(int channelType) {
		ChannelType = channelType;
	}


	public int getAttenuationConfigMode() {
		return AttenuationConfigMode;
	}


	public void setAttenuationConfigMode(int attenuationConfigMode) {
		AttenuationConfigMode = attenuationConfigMode;
	}


	public float getAttenuation() {
		return Attenuation;
	}


	public void setAttenuation(float attenuation) {
		Attenuation = attenuation;
	}


	@Override
	public String toString() {
		return "VoaConfigInfo [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Rack=" + Rack + ", Sbrack=" + Sbrack
				+ ", Card=" + Card + ", Direction=" + Direction + ", ChannelType=" + ChannelType
				+ ", AttenuationConfigMode=" + AttenuationConfigMode + ", Attenuation=" + Attenuation + "]";
	}	
}
