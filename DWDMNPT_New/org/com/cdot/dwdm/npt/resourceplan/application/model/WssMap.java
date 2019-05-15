package application.model;

public class WssMap {
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	private String CardType;
	private int CardSubtype;
	private int WssSetNo;
	private int WssLevel2SwitchPort;
	private String WssLevel2CommonPort;	
	private String TpnLoc;
	private int TpnLinePortNo;
	private String WssLevel1Loc;
	private String WssLevel1SwitchPort;
	private String WssLevel1CommonPort;
	private String EdfaLoc;
	private int EdfaId;
	public WssMap() {
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
	
	public int getWssSetNo() {
		return WssSetNo;
	}
	public void setWssSetNo(int wssSetNo) {
		WssSetNo = wssSetNo;
	}
	public int getWssLevel2SwitchPort() {
		return WssLevel2SwitchPort;
	}
	public void setWssLevel2SwitchPort(int wssLevel2SwitchPort) {
		WssLevel2SwitchPort = wssLevel2SwitchPort;
	}
	public String getWssLevel2CommonPort() {
		return WssLevel2CommonPort;
	}
	public void setWssLevel2CommonPort(String wssLevel2CommonPort) {
		WssLevel2CommonPort = wssLevel2CommonPort;
	}
	public String getWssLevel1Loc() {
		return WssLevel1Loc;
	}
	public void setWssLevel1Loc(String wssLevel1Loc) {
		WssLevel1Loc = wssLevel1Loc;
	}
	public String getWssLevel1SwitchPort() {
		return WssLevel1SwitchPort;
	}
	public void setWssLevel1SwitchPort(String string) {
		WssLevel1SwitchPort = string;
	}
	public String getWssLevel1CommonPort() {
		return WssLevel1CommonPort;
	}
	public void setWssLevel1CommonPort(String wssLevel1CommonPort) {
		WssLevel1CommonPort = wssLevel1CommonPort;
	}
	
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	public int getCardSubtype() {
		return CardSubtype;
	}
	public void setCardSubtype(int cardSubtype) {
		CardSubtype = cardSubtype;
	}
	
	@Override
	public String toString() {
		return "McsMap [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Rack=" + Rack + ", Sbrack=" + Sbrack
				+ ", Card=" + Card+ ", CardType=" + CardType+ ", CardSubtype=" + CardSubtype + ", WssSetNo=" + WssSetNo + ", WssLevel2SwitchPort=" + WssLevel2SwitchPort + ", WssLevel2CommonPort="
				+ WssLevel2CommonPort + ", TpnLoc=" + TpnLoc + ", TpnLinePortNo=" + TpnLinePortNo + ", EdfaLoc=" + EdfaLoc
				+ ", EdfaId=" + EdfaId + ", WssLevel1Loc=" + WssLevel1Loc+ ", WssLevel1SwitchPort=" + WssLevel1SwitchPort+ ", WssLevel1SwitchPort=" + WssLevel1SwitchPort+ "]";
	}
	public WssMap(int networkId, int nodeId, int rack, int sbrack, int card,String cardType,int cardSubtype, int wssSetNo, int wssLevel2SwitchPort,
			String wssLevel2CommonPort,String wssLevel1Loc, String wssLevel1SwitchPort,
			String wssLevel1CommonPort, String tpnLoc, int tpnLinePortNo, String edfaLoc, int edfaId) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Rack = rack;
		Sbrack = sbrack;
		Card = card;
		CardType = cardType;
		CardSubtype = cardSubtype;
		WssSetNo=wssSetNo;
		TpnLoc = tpnLoc;
		WssLevel2SwitchPort=wssLevel2SwitchPort;
		WssLevel2CommonPort=wssLevel2CommonPort;
		TpnLinePortNo = tpnLinePortNo;
		WssLevel1Loc=wssLevel1Loc;
		WssLevel1SwitchPort=wssLevel1SwitchPort;
		WssLevel1CommonPort=wssLevel1CommonPort;
		EdfaLoc = edfaLoc;
		EdfaId = edfaId;
	}
	
	
}
