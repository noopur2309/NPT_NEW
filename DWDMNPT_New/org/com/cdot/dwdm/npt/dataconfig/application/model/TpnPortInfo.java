package application.model;

public class TpnPortInfo {
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	private int Stream;	
	private String CardType;
	private int CardSubType;	
	private int PortId;
	private int ProtectionSubType;
	private int IsRevertive;
	private int FecType;
	private int FecStatus;
	private int TxSegment;
	private String OperatorSpecific;
	private int TimDectMode;
	private int TCMActStatus;
	private int TCMActValue;
	private int TxTCMMode;
	private String TxTCMPriority;
	private int GccType;
	private int GccStatus;
	private int GccValue;
	public TpnPortInfo() {
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
	public int getPortId() {
		return PortId;
	}
	public void setPortId(int portId) {
		PortId = portId;
	}
	public int getProtectionSubType() {
		return ProtectionSubType;
	}
	public void setProtectionSubType(int protectionSubType) {
		ProtectionSubType = protectionSubType;
	}
	public int getIsRevertive() {
		return IsRevertive;
	}
	public void setIsRevertive(int isRevertive) {
		IsRevertive = isRevertive;
	}
	public int getFecType() {
		return FecType;
	}
	public void setFecType(int fecType) {
		FecType = fecType;
	}
	public int getFecStatus() {
		return FecStatus;
	}
	public void setFecStatus(int fecStatus) {
		FecStatus = fecStatus;
	}
	public int getTxSegment() {
		return TxSegment;
	}
	public void setTxSegment(int txSegment) {
		TxSegment = txSegment;
	}
	public String getOperatorSpecific() {
		return OperatorSpecific;
	}
	public void setOperatorSpecific(String operatorSpecific) {
		OperatorSpecific = operatorSpecific;
	}
	public int getTimDectMode() {
		return TimDectMode;
	}
	public void setTimDectMode(int timDectMode) {
		TimDectMode = timDectMode;
	}
	public int getTCMActStatus() {
		return TCMActStatus;
	}
	public void setTCMActStatus(int tCMActStatus) {
		TCMActStatus = tCMActStatus;
	}
	public int getTCMActValue() {
		return TCMActValue;
	}
	public void setTCMActValue(int tCMActValue) {
		TCMActValue = tCMActValue;
	}
	public int getTxTCMMode() {
		return TxTCMMode;
	}
	public void setTxTCMMode(int txTCMMode) {
		TxTCMMode = txTCMMode;
	}
	public String getTxTCMPriority() {
		return TxTCMPriority;
	}
	public void setTxTCMPriority(String txTCMPriority) {
		TxTCMPriority = txTCMPriority;
	}
	public int getGccType() {
		return GccType;
	}
	public void setGccType(int gccType) {
		GccType = gccType;
	}
	public int getGccStatus() {
		return GccStatus;
	}
	public void setGccStatus(int gccStatus) {
		GccStatus = gccStatus;
	}
	public int getGccValue() {
		return GccValue;
	}
	public void setGccValue(int gccValue) {
		GccValue = gccValue;
	}
	public int getStream() {
		return Stream;
	}
	public void setStream(int stream) {
		Stream = stream;
	}
	@Override
	public String toString() {
		return "TpnPortInfo [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Rack=" + Rack + ", Sbrack=" + Sbrack
				+ ", Card=" + Card + ", Stream=" + Stream + ", CardType=" + CardType + ", CardSubType=" + CardSubType
				+ ", PortId=" + PortId + ", ProtectionSubType=" + ProtectionSubType + ", IsRevertive=" + IsRevertive
				+ ", FecType=" + FecType + ", FecStatus=" + FecStatus + ", TxSegment=" + TxSegment
				+ ", OperatorSpecific=" + OperatorSpecific + ", TimDectMode=" + TimDectMode + ", TCMActStatus="
				+ TCMActStatus + ", TCMActValue=" + TCMActValue + ", TxTCMMode=" + TxTCMMode + ", TxTCMPriority="
				+ TxTCMPriority + ", GccType=" + GccType + ", GccStatus=" + GccStatus + ", GccValue=" + GccValue + "]";
	}
	public TpnPortInfo(int networkId, int nodeId, int rack, int sbrack, int card, int stream, String cardType,
			int cardSubType, int portId, int protectionSubType, int isRevertive, int fecType, int fecStatus,
			int txSegment, String operatorSpecific, int timDectMode, int tCMActStatus, int tCMActValue, int txTCMMode,
			String txTCMPriority, int gccType, int gccStatus, int gccValue) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Rack = rack;
		Sbrack = sbrack;
		Card = card;
		Stream = stream;
		CardType = cardType;
		CardSubType = cardSubType;
		PortId = portId;
		ProtectionSubType = protectionSubType;
		IsRevertive = isRevertive;
		FecType = fecType;
		FecStatus = fecStatus;
		TxSegment = txSegment;
		OperatorSpecific = operatorSpecific;
		TimDectMode = timDectMode;
		TCMActStatus = tCMActStatus;
		TCMActValue = tCMActValue;
		TxTCMMode = txTCMMode;
		TxTCMPriority = txTCMPriority;
		GccType = gccType;
		GccStatus = gccStatus;
		GccValue = gccValue;
	}
	
	
	
	
	
}
