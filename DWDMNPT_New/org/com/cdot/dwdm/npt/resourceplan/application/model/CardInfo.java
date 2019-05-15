package application.model;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class CardInfo {	
//	private String NodeKey;
	private int NetworkId;
	private int NodeId;
	private int Rack;
	private int Sbrack;
	private int Card;
	private String CardType;
	private String Direction;
	private int Wavelength;
	private int DemandId;
	private int EquipmentId;
	private String Status;
	private int CircuitId;	
	private String SerialNo;
	
	public CardInfo() {
		super();		
	}

//	public String getNodeKey() {
//		return NodeKey;
//	}
//
//	public void setNodeKey(String nodeKey) {
//		NodeKey = nodeKey;
//	}

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

	
	public String getDirection() {
		return Direction;
	}

	public void setDirection(String direction) {
		Direction = direction;
	}

	public int getWavelength() {
		return Wavelength;
	}

	public void setWavelength(int wavelength) {
		Wavelength = wavelength;
	}

	
	public int getDemandId() {
		return DemandId;
	}

	public void setDemandId(int demandId) {
		DemandId = demandId;
	}
	
	public int getEquipmentId() {
		return EquipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		EquipmentId = equipmentId;
	}

	
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public int getCircuitId() {
		return CircuitId;
	}

	public void setCircuitId(int circuitId) {
		CircuitId = circuitId;
	}
	

	public String getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}

	@Override
	public String toString() {
		return "CardInfo [NetworkId=" + NetworkId +" NodeId=" + NodeId + ", Rack=" + Rack + ", Sbrack=" + Sbrack + ", Card=" + Card
				+ ", CardType=" + CardType + ", Direction=" + Direction + ", Wavelength=" + Wavelength + ", DemandId="
				+ DemandId + ", EquipmentId=" + EquipmentId + ", Status=" + Status + ", CircuitId=" + CircuitId
				+ ", SerialNo=" + SerialNo + "]";
	}
	
	@Override
    public boolean equals(Object anObject) {
		
		if(anObject == this)return true;
		
        if (!(anObject instanceof CardInfo)) {
            return false;
        }
        
        CardInfo otherMember = (CardInfo)anObject;
        
        return new EqualsBuilder()
//                .append(getNodeKey(), otherMember.getNodeKey())
        		.append(getNetworkId(), otherMember.getNetworkId())
        		.append(getNodeId(), otherMember.getNodeId())
        		.append(getRack(), otherMember.getRack())
                .append(getSbrack(), otherMember.getSbrack())
                .append(getCard(), otherMember.getCard())
                .append(getCardType(), otherMember.getCardType())
                .append(getDemandId(), otherMember.getDemandId())
                .append(getDirection(), otherMember.getDirection())
                .append(getEquipmentId(), otherMember.getEquipmentId())
                .append(getStatus(), otherMember.getStatus())
                .append(getCircuitId(), otherMember.getCircuitId())
                .append(getWavelength(), otherMember.getWavelength())
                .append(getSerialNo(), otherMember.getSerialNo())
                .isEquals();
	}

	public CardInfo(/*String nodeKey,*/int networkId,int nodeId, int rack, int sbrack, int card, String cardType, String direction, int wavelength,
			int demandId, int equipmentId, String status, int circuitId, String serialNo) {
		super();
//		NodeKey = nodeKey;
		NetworkId = networkId;
		NodeId = nodeId;
		Rack = rack;
		Sbrack = sbrack;
		Card = card;
		CardType = cardType;
		Direction = direction;
		Wavelength = wavelength;
		DemandId = demandId;
		EquipmentId = equipmentId;
		Status = status;
		CircuitId = circuitId;
		SerialNo = serialNo;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getNetworkId(),getNodeId(), getRack(), getSbrack(),getCard(),getCardType(),getDirection(),getWavelength(),
				getDemandId(),getEquipmentId(),getStatus(),getCircuitId(),getSerialNo(),getStatus());
	}	
}
