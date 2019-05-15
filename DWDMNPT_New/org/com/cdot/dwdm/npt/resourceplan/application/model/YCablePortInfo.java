package application.model;

public class YCablePortInfo {	
	private int NetworkId;
	private int NodeId;
	private int YCableRack;
	private int YCableSbrack;
	private int YCableCard;	
	private int YCablePort;
	private String MpnLocN;
	private int MpnPortN;	
	private String MpnLocP;
	private int MpnPortP;	
	
	public YCablePortInfo() {
		super();		
	}
	
	
	public int getYCableRack() {
		return YCableRack;
	}
	public void setYCableRack(int yCableRack) {
		YCableRack = yCableRack;
	}
	public int getYCableSbrack() {
		return YCableSbrack;
	}
	public void setYCableSbrack(int yCableSbrack) {
		YCableSbrack = yCableSbrack;
	}
	public int getYCableCard() {
		return YCableCard;
	}
	public void setYCableCard(int yCableCard) {
		YCableCard = yCableCard;
	}
	public int getYCablePort() {
		return YCablePort;
	}
	public void setYCablePort(int yCablePort) {
		YCablePort = yCablePort;
	}
	

	public String getMpnLocN() {
		return MpnLocN;
	}


	public void setMpnLocN(String mpnLocN) {
		MpnLocN = mpnLocN;
	}


	public int getMpnPortN() {
		return MpnPortN;
	}


	public void setMpnPortN(int mpnPortN) {
		MpnPortN = mpnPortN;
	}


	public String getMpnLocP() {
		return MpnLocP;
	}


	public void setMpnLocP(String mpnLocP) {
		MpnLocP = mpnLocP;
	}


	public int getMpnPortP() {
		return MpnPortP;
	}


	public void setMpnPortP(int mpnPortP) {
		MpnPortP = mpnPortP;
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


	@Override
	public String toString() {
		return "YCablePortInfo [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", YCableRack=" + YCableRack
				+ ", YCableSbrack=" + YCableSbrack + ", YCableCard=" + YCableCard + ", YCablePort=" + YCablePort
				+ ", MpnLocN=" + MpnLocN + ", MpnPortN=" + MpnPortN + ", MpnLocP=" + MpnLocP + ", MpnPortP=" + MpnPortP
				+ "]";
	}


	public YCablePortInfo(int networkId, int nodeId, int yCableRack, int yCableSbrack, int yCableCard, int yCablePort,
			String mpnLocN, int mpnPortN, String mpnLocP, int mpnPortP) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		YCableRack = yCableRack;
		YCableSbrack = yCableSbrack;
		YCableCard = yCableCard;
		YCablePort = yCablePort;
		MpnLocN = mpnLocN;
		MpnPortN = mpnPortN;
		MpnLocP = mpnLocP;
		MpnPortP = mpnPortP;
	}
	
}
