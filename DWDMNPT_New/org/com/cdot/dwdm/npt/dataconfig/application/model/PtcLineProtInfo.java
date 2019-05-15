package application.model;

public class PtcLineProtInfo {
	private int NetworkId;
	private int NodeId;	
	private int ProtCardRackId;
	private int ProtCardSubrackId;
	private int ProtCardCardId;
	private String ProtCardCardType;
	private int ProtCardCardSubType;
	private int MpnRackId;
	private int MpnSubrackId;
	private int MpnCardId;
	private String MpnCardType;
	private int MpnCardSubType;
	private String ProtTopology;
	private String ProtMechanism;
	private int ProtStatus;
	private int ProtType;
	private int ActiveLine;
	
	public PtcLineProtInfo() {
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

	public int getProtCardRackId() {
		return ProtCardRackId;
	}

	public void setProtCardRackId(int protCardRackId) {
		ProtCardRackId = protCardRackId;
	}

	public int getProtCardSubrackId() {
		return ProtCardSubrackId;
	}

	public void setProtCardSubrackId(int protCardSubrackId) {
		ProtCardSubrackId = protCardSubrackId;
	}

	public int getProtCardCardId() {
		return ProtCardCardId;
	}

	public void setProtCardCardId(int protCardCardId) {
		ProtCardCardId = protCardCardId;
	}
	
	public String getProtCardCardType() {
		return ProtCardCardType;
	}

	public void setProtCardCardType(String string) {
		ProtCardCardType = string;
	}
	
	public String getMpnCardType() {
		return MpnCardType;
	}

	public void setMpnCardType(String string) {
		MpnCardType = string;
	}

	public int getMpnCardSubType() {
		return MpnCardSubType;
	}

	public void setMpnCardSubType(int mpnCardSubType) {
		MpnCardSubType = mpnCardSubType;
	}

	public int getProtCardCardSubType() {
		return ProtCardCardSubType;
	}

	public void setProtCardCardSubType(int protCardCardSubType) {
		ProtCardCardSubType = protCardCardSubType;
	}

	public int getMpnRackId() {
		return MpnRackId;
	}

	public void setMpnRackId(int mpnRackId) {
		MpnRackId = mpnRackId;
	}

	public int getMpnSubrackId() {
		return MpnSubrackId;
	}

	public void setMpnSubrackId(int mpnSubrackId) {
		MpnSubrackId = mpnSubrackId;
	}

	public int getMpnCardId() {
		return MpnCardId;
	}

	public void setMpnCardId(int mpnCardId) {
		MpnCardId = mpnCardId;
	}

	

	public String getProtMechanism() {
		return ProtMechanism;
	}

	public void setProtMechanism(String protMechanism) {
		ProtMechanism = protMechanism;
	}

	public int getProtStatus() {
		return ProtStatus;
	}

	public void setProtStatus(int protStatus) {
		ProtStatus = protStatus;
	}

	public int getProtType() {
		return ProtType;
	}

	public void setProtType(int protType) {
		ProtType = protType;
	}

	public int getActiveLine() {
		return ActiveLine;
	}

	public void setActiveLine(int activeLine) {
		ActiveLine = activeLine;
	}
	
	

	public String getProtTopology() {
		return ProtTopology;
	}

	public void setProtTopology(String protTopology) {
		ProtTopology = protTopology;
	}

	@Override
	public String toString() {
		return "PtcLineProtInfo [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", ProtCardRackId=" + ProtCardRackId
				+ ", ProtCardSubrackId=" + ProtCardSubrackId + ", ProtCardCardId=" + ProtCardCardId+ ", ProtCardCardType=" + ProtCardCardType + ", ProtCardCardSubType=" + ProtCardCardSubType+ 
				", MpnRackId="+ MpnRackId + ", MpnSubrackId=" + MpnSubrackId + ", MpnCardId=" + MpnCardId + ", MpnCardType=" + MpnCardType + ", MpnCardSubType=" + MpnCardSubType
				+ ", ProtTopology="+ ProtTopology + ", ProtMechanism=" + ProtMechanism + ", ProtStatus=" + ProtStatus + ", ProtType="
				+ ProtType + ", ActiveLine=" + ActiveLine + "]";
	}

	public PtcLineProtInfo(int networkId, int nodeId, int protCardRackId, int protCardSubrackId, int protCardCardId,String protCardCardType,
			int protCardCardSubType,int mpnRackId, int mpnSubrackId, int mpnCardId,String mpnCardType,int mpnCardSubType, String protTopology, String protMechanism, int protStatus,
			int protType, int activeLine) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		ProtCardRackId = protCardRackId;
		ProtCardSubrackId = protCardSubrackId;
		ProtCardCardId = protCardCardId;
		ProtCardCardType = protCardCardType;
		ProtCardCardSubType = protCardCardSubType;
		MpnRackId = mpnRackId;
		MpnSubrackId = mpnSubrackId;
		MpnCardId = mpnCardId;
		MpnCardType = mpnCardType;
		MpnCardSubType = mpnCardSubType;
		ProtTopology = protTopology;
		ProtMechanism = protMechanism;
		ProtStatus = protStatus;
		ProtType = protType;
		ActiveLine = activeLine;
	}

}
