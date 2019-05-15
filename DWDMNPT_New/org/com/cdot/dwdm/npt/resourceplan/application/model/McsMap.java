package application.model;

public class McsMap {
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	private int McsId;
	private int McsSwitchPort;
	private String McsCommonPort;
	private String TpnLoc;
	private int TpnLinePortNo;
	private String EdfaLoc;
	private int EdfaId;
	public McsMap() {
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
	public int getMcsId() {
		return McsId;
	}
	public void setMcsId(int mcsId) {
		McsId = mcsId;
	}
	public int getMcsSwitchPort() {
		return McsSwitchPort;
	}
	public void setMcsSwitchPort(int mcsSwitchPort) {
		McsSwitchPort = mcsSwitchPort;
	}
	public String getMcsCommonPort() {
		return McsCommonPort;
	}
	public void setMcsCommonPort(String mcsCommonPort) {
		McsCommonPort = mcsCommonPort;
	}
	public String getTpnLoc() {
		return TpnLoc;
	}
	public void setTpnLoc(String tpnLoc) {
		TpnLoc = tpnLoc;
	}
	public int getTpnLinePortNo() {
		return TpnLinePortNo;
	}
	public void setTpnLinePortNo(int tpnLinePortNo) {
		TpnLinePortNo = tpnLinePortNo;
	}
	
	public String getEdfaLoc() {
		return EdfaLoc;
	}
	public void setEdfaLoc(String edfaLoc) {
		EdfaLoc = edfaLoc;
	}
	public int getEdfaId() {
		return EdfaId;
	}
	public void setEdfaId(int edfaId) {
		EdfaId = edfaId;
	}
	@Override
	public String toString() {
		return "McsMap [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Rack=" + Rack + ", Sbrack=" + Sbrack
				+ ", Card=" + Card + ", McsId=" + McsId + ", McsSwitchPort=" + McsSwitchPort + ", McsCommonPort="
				+ McsCommonPort + ", TpnLoc=" + TpnLoc + ", TpnLinePortNo=" + TpnLinePortNo + ", EdfaLoc=" + EdfaLoc
				+ ", EdfaId=" + EdfaId + "]";
	}
	public McsMap(int networkId, int nodeId, int rack, int sbrack, int card, int mcsId, int mcsSwitchPort,
			String mcsCommonPort, String tpnLoc, int tpnLinePortNo, String edfaLoc, int edfaId) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Rack = rack;
		Sbrack = sbrack;
		Card = card;
		McsId = mcsId;
		McsSwitchPort = mcsSwitchPort;
		McsCommonPort = mcsCommonPort;
		TpnLoc = tpnLoc;
		TpnLinePortNo = tpnLinePortNo;
		EdfaLoc = edfaLoc;
		EdfaId = edfaId;
	}
	
	
}
