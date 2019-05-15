package application.model;

public class PtcClientProtInfo {
	private int NetworkId;
	private int NodeId;	
	private int ActMpnRackId;
	private int ActMpnSubrackId;
	private int ActMpnCardId;
	private String ActMpnCardType;
	private int ActMpnCardSubType;
	private int ActMpnPort;
	private int ProtMpnRackId;
	private int ProtMpnSubrackId;
	private int ProtMpnCardId;
	private String ProtMpnCardType;
	private int ProtMpnCardSubType;
	private int ProtMpnPort;
	private int ProtCardRackId;
	private int ProtCardSubrackId;
	private int ProtCardCardId;
	private String ProtTopology;
	private String ProtMechanism;
	private int ProtStatus;
	private int ProtType;
	private int ActiveLine;
	
	public PtcClientProtInfo() {
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

	public int getActMpnRackId() {
		return ActMpnRackId;
	}

	public void setActMpnRackId(int actMpnRackId) {
		ActMpnRackId = actMpnRackId;
	}

	public int getActMpnSubrackId() {
		return ActMpnSubrackId;
	}

	public void setActMpnSubrackId(int actMpnSubrackId) {
		ActMpnSubrackId = actMpnSubrackId;
	}

	public int getActMpnCardId() {
		return ActMpnCardId;
	}

	public void setActMpnCardId(int actMpnCardId) {
		ActMpnCardId = actMpnCardId;
	}

	

	public int getActMpnCardSubType() {
		return ActMpnCardSubType;
	}

	public void setActMpnCardSubType(int actMpnCardSubType) {
		ActMpnCardSubType = actMpnCardSubType;
	}

	public int getActMpnPort() {
		return ActMpnPort;
	}

	public void setActMpnPort(int actMpnPort) {
		ActMpnPort = actMpnPort;
	}

	public int getProtMpnRackId() {
		return ProtMpnRackId;
	}

	public void setProtMpnRackId(int protMpnRackId) {
		ProtMpnRackId = protMpnRackId;
	}

	public int getProtMpnSubrackId() {
		return ProtMpnSubrackId;
	}

	public void setProtMpnSubrackId(int protMpnSubrackId) {
		ProtMpnSubrackId = protMpnSubrackId;
	}

	public int getProtMpnCardId() {
		return ProtMpnCardId;
	}

	public void setProtMpnCardId(int protMpnCardId) {
		ProtMpnCardId = protMpnCardId;
	}

	

	public String getActMpnCardType() {
		return ActMpnCardType;
	}
	public void setActMpnCardType(String actMpnCardType) {
		ActMpnCardType = actMpnCardType;
	}
	public String getProtMpnCardType() {
		return ProtMpnCardType;
	}
	public void setProtMpnCardType(String protMpnCardType) {
		ProtMpnCardType = protMpnCardType;
	}
	public int getProtMpnCardSubType() {
		return ProtMpnCardSubType;
	}

	public void setProtMpnCardSubType(int protMpnCardSubType) {
		ProtMpnCardSubType = protMpnCardSubType;
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

	

	public String getProtTopology() {
		return ProtTopology;
	}
	public void setProtTopology(String protTopology) {
		ProtTopology = protTopology;
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
	public int getProtMpnPort() {
		return ProtMpnPort;
	}
	public void setProtMpnPort(int protMpnPort) {
		ProtMpnPort = protMpnPort;
	}
	@Override
	public String toString() {
		return "PtcClientProtInfo [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", ActMpnRackId=" + ActMpnRackId
				+ ", ActMpnSubrackId=" + ActMpnSubrackId + ", ActMpnCardId=" + ActMpnCardId + ", ActMpnCardType="
				+ ActMpnCardType + ", ActMpnCardSubType=" + ActMpnCardSubType + ", ActMpnPort=" + ActMpnPort
				+ ", ProtMpnRackId=" + ProtMpnRackId + ", ProtMpnSubrackId=" + ProtMpnSubrackId + ", ProtMpnCardId="
				+ ProtMpnCardId + ", ProtMpnCardType=" + ProtMpnCardType + ", ProtMpnCardSubType=" + ProtMpnCardSubType
				+ ", ProtMpnPort=" + ProtMpnPort + ", ProtCardRackId=" + ProtCardRackId + ", ProtCardSubrackId="
				+ ProtCardSubrackId + ", ProtCardCardId=" + ProtCardCardId + ", ProtTopology=" + ProtTopology
				+ ", ProtMechanism=" + ProtMechanism + ", ProtStatus=" + ProtStatus + ", ProtType=" + ProtType
				+ ", ActiveLine=" + ActiveLine + "]";
	}
	public PtcClientProtInfo(int networkId, int nodeId, int actMpnRackId, int actMpnSubrackId, int actMpnCardId,
			String actMpnCardType, int actMpnCardSubType, int actMpnPort, int protMpnRackId, int protMpnSubrackId,
			int protMpnCardId, String protMpnCardType, int protMpnCardSubType, int protMpnPort, int protCardRackId,
			int protCardSubrackId, int protCardCardId, String protTopology, String protMechanism, int protStatus,
			int protType, int activeLine) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		ActMpnRackId = actMpnRackId;
		ActMpnSubrackId = actMpnSubrackId;
		ActMpnCardId = actMpnCardId;
		ActMpnCardType = actMpnCardType;
		ActMpnCardSubType = actMpnCardSubType;
		ActMpnPort = actMpnPort;
		ProtMpnRackId = protMpnRackId;
		ProtMpnSubrackId = protMpnSubrackId;
		ProtMpnCardId = protMpnCardId;
		ProtMpnCardType = protMpnCardType;
		ProtMpnCardSubType = protMpnCardSubType;
		ProtMpnPort = protMpnPort;
		ProtCardRackId = protCardRackId;
		ProtCardSubrackId = protCardSubrackId;
		ProtCardCardId = protCardCardId;
		ProtTopology = protTopology;
		ProtMechanism = protMechanism;
		ProtStatus = protStatus;
		ProtType = protType;
		ActiveLine = activeLine;
	}
	
}
