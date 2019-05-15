package application.model;

import java.util.List;

public class PortInfo {	
//	private String NodeKey;
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	private String CardType;
	private int Port;//line and client both
	private int LinePort;
	private int EquipmentId;	
	private int CircuitId;
	private int DemandId;
	private String Direction;
	private int MpnPortNo;
	
	public int getMpnPortNo() {
		return MpnPortNo;
	}
	public void setMpnPortNo(int mpnPortNo) {
		MpnPortNo = mpnPortNo;
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
	
	public PortInfo() {
		super();		
	}
//	public String getNodeKey() {
//		return NodeKey;
//	}
//	public void setNodeKey(String nodeKey) {
//		NodeKey = nodeKey;
//	}
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
	
	public int getPort() {
		return Port;
	}
	public void setPort(int port) {
		Port = port;
	}
	public int getLinePort() {
		return LinePort;
	}
	public void setLinePort(int linePort) {
		LinePort = linePort;
	}
	public int getEquipmentId() {
		return EquipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		EquipmentId = equipmentId;
	}
	public int getCircuitId() {
		return CircuitId;
	}
	public void setCircuitId(int circuitId) {
		CircuitId = circuitId;
	}
	public int getDemandId() {
		return DemandId;
	}
	public void setDemandId(int demandId) {
		DemandId = demandId;
	}
	
	public String getDirection() {
		return Direction;
	}
	public void setDirection(String direction) {
		Direction = direction;
	}
	
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	@Override
	public String toString() {
		return "PortInfo [NetworkId=" + NetworkId +" NodeId=" + NodeId + ", Rack=" + Rack + ", Sbrack=" + Sbrack + ", Card=" + Card
				+ ", CardType=" + CardType + ", Port=" + Port + ", LinePort=" + LinePort + ", EquipmentId="
				+ EquipmentId + ", CircuitId=" + CircuitId + ", DemandId=" + DemandId + ", Direction=" + Direction
				+ "]";
	}
	
	@Override
    public boolean equals(Object obj) {

        try {
            PortInfo linePort  = (PortInfo) obj;
            return Card==linePort.Card
            		& Rack==linePort.getRack()
            		& Sbrack==linePort.getSbrack()
            		& DemandId==linePort.getDemandId()
            		& CardType.equals(linePort.getCardType())
            		& LinePort==linePort.getLinePort()
            		& Direction.equals(linePort.getDirection());
        }
        catch (Exception e)
        {
            return false;
        }

    }
	
	
	public PortInfo(/*String nodeKey,*/int networkId,int nodeId, int rack, int sbrack, int card, String cardType, int port, int linePort,
			int equipmentId, int circuitId, int demandId, String direction,int mpnPortNo) {
		super();
//		NodeKey = nodeKey;
		NetworkId = networkId;
		NodeId = nodeId;
		Rack = rack;
		Sbrack = sbrack;
		Card = card;
		CardType = cardType;
		Port = port;
		LinePort = linePort;
		EquipmentId = equipmentId;
		CircuitId = circuitId;
		DemandId = demandId;
		Direction = direction;
		MpnPortNo = mpnPortNo;
	}	
		
}
