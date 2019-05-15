package application.model;

public class McsPortMapping {
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	private String CardType;
	private int CardSubType;
	private int McsId;
	private String McsAddPortInfo;
	private String McsDropPortInfo;
	private int AddTpnRackId;
	private int AddTpnSubRackId;
	private int AddTpnSlotId;
	private int AddTpnLinePortNum;
	private int DropTpnRackId;
	private int DropTpnSubRackId;
	private int DropTpnSlotId;
	private int DropTpnLinePortNum;
	private int McsSwitchPort;

	public McsPortMapping() {
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


	public int getMcsId() {
		return McsId;
	}

	public void setMcsId(int mcsId) {
		McsId = mcsId;
	}

	public int getAddTpnRackId() {
		return AddTpnRackId;
	}

	public void setAddTpnRackId(int addTpnRackId) {
		AddTpnRackId = addTpnRackId;
	}

	public int getAddTpnSubRackId() {
		return AddTpnSubRackId;
	}

	public void setAddTpnSubRackId(int addTpnSubRackId) {
		AddTpnSubRackId = addTpnSubRackId;
	}

	public int getAddTpnSlotId() {
		return AddTpnSlotId;
	}

	public void setAddTpnSlotId(int addTpnSlotId) {
		AddTpnSlotId = addTpnSlotId;
	}

	public int getDropTpnRackId() {
		return DropTpnRackId;
	}

	public void setDropTpnRackId(int dropTpnRackId) {
		DropTpnRackId = dropTpnRackId;
	}

	public int getDropTpnSubRackId() {
		return DropTpnSubRackId;
	}

	public void setDropTpnSubRackId(int dropTpnSubRackId) {
		DropTpnSubRackId = dropTpnSubRackId;
	}

	public int getDropTpnSlotId() {
		return DropTpnSlotId;
	}

	public void setDropTpnSlotId(int dropTpnSlotId) {
		DropTpnSlotId = dropTpnSlotId;
	}


	public int getAddTpnLinePortNum() {
		return AddTpnLinePortNum;
	}


	public void setAddTpnLinePortNum(int addTpnLinePortNum) {
		AddTpnLinePortNum = addTpnLinePortNum;
	}


	public int getDropTpnLinePortNum() {
		return DropTpnLinePortNum;
	}


	public void setDropTpnLinePortNum(int dropTpnLinePortNum) {
		DropTpnLinePortNum = dropTpnLinePortNum;
	}


	public String getMcsAddPortInfo() {
		return McsAddPortInfo;
	}


	public void setMcsAddPortInfo(String mcsAddPortInfo) {
		McsAddPortInfo = mcsAddPortInfo;
	}


	public String getMcsDropPortInfo() {
		return McsDropPortInfo;
	}


	public void setMcsDropPortInfo(String mcsDropPortInfo) {
		McsDropPortInfo = mcsDropPortInfo;
	}
	
	


	public int getMcsSwitchPort() {
		return McsSwitchPort;
	}


	public void setMcsSwitchPort(int mcsSwitchPort) {
		McsSwitchPort = mcsSwitchPort;
	}


	@Override
	public String toString() {
		return "McsPortMapping [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Rack=" + Rack + ", Sbrack=" + Sbrack
				+ ", Card=" + Card + ", CardType=" + CardType + ", CardSubType=" + CardSubType + ", McsId=" + McsId
				+ ", McsAddPortInfo=" + McsAddPortInfo + ", McsDropPortInfo=" + McsDropPortInfo + ", AddTpnRackId="
				+ AddTpnRackId + ", AddTpnSubRackId=" + AddTpnSubRackId + ", AddTpnSlotId=" + AddTpnSlotId
				+ ", AddTpnLinePortNum=" + AddTpnLinePortNum + ", DropTpnRackId=" + DropTpnRackId
				+ ", DropTpnSubRackId=" + DropTpnSubRackId + ", DropTpnSlotId=" + DropTpnSlotId
				+ ", DropTpnLinePortNum=" + DropTpnLinePortNum + "]";
	}


	public McsPortMapping(int networkId, int nodeId, int rack, int sbrack, int card, String cardType, int cardSubType,
			int mcsId, String mcsAddPortInfo, String mcsDropPortInfo, int addTpnRackId, int addTpnSubRackId,
			int addTpnSlotId, int addTpnLinePortNum, int dropTpnRackId, int dropTpnSubRackId, int dropTpnSlotId,
			int dropTpnLinePortNum) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Rack = rack;
		Sbrack = sbrack;
		Card = card;
		CardType = cardType;
		CardSubType = cardSubType;
		McsId = mcsId;
		McsAddPortInfo = mcsAddPortInfo;
		McsDropPortInfo = mcsDropPortInfo;
		AddTpnRackId = addTpnRackId;
		AddTpnSubRackId = addTpnSubRackId;
		AddTpnSlotId = addTpnSlotId;
		AddTpnLinePortNum = addTpnLinePortNum;
		DropTpnRackId = dropTpnRackId;
		DropTpnSubRackId = dropTpnSubRackId;
		DropTpnSlotId = dropTpnSlotId;
		DropTpnLinePortNum = dropTpnLinePortNum;
	}
	
}
