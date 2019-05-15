package application.model;

public class MuxDemuxPortInfo {	
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int CardId;	
	private int wavelength;
	private int portNum;
	
	public MuxDemuxPortInfo() {
		super();		
	}

	public int getNetworkId() {
		return NetworkId;
	}

	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}

	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
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

	public int getCardId() {
		return CardId;
	}

	public void setCardId(int cardId) {
		CardId = cardId;
	}

	public int getWavelength() {
		return wavelength;
	}

	public void setWavelength(int wavelength) {
		this.wavelength = wavelength;
	}



	
	
//	@Override
//	public String toString() {
//		return "MuxDemuxPortInfo [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", muxDemuxRack=" + muxDemuxRack
//				+ ", muxDemuxSubrack=" + muxDemuxSubrack + ", muxDemuxCardId=" + muxDemuxCardId + " wavelength="+wavelength+"]";
//	}
//
////
//	public MuxDemuxPortInfo(int networkId, int nodeId, int yCableRack, int yCableSbrack, int yCableCard, int yCablePort,
//			String mpnLocN, int mpnPortN, String mpnLocP, int mpnPortP) {
//		super();
//		NetworkId = networkId;
//		NodeId = nodeId;
//		YCableRack = yCableRack;
//		YCableSbrack = yCableSbrack;
//		YCableCard = yCableCard;
//		YCablePort = yCablePort;
//		MpnLocN = mpnLocN;
//		MpnPortN = mpnPortN;
//		MpnLocP = mpnLocP;
//		MpnPortP = mpnPortP;
//	}
	
}
