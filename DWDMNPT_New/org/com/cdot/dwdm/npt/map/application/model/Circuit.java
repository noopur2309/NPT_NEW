package application.model;

public class Circuit {
	private int NetworkId;
	private int CircuitId;
	private int SrcNodeId;
	private int DestNodeId;
	private int QoS;
	private float RequiredTraffic;
	private String TrafficType;
	private String ProtectionType;
	private String ClientProtectionType;
	private String ColourPreference;
	private String PathType;
	private String LineRate;
	private String ChannelProtection;
	private String ClientProtection;
	private String NodeInclusion;
	private String VendorLabel;
	private String LambdaBlocking;
	
	
	public String getPathType() {
		return PathType;
	}
	public void setPathType(String pathType) {
		PathType = pathType;
	}
	public int getQoS()
	{ return QoS;
	}
	public void setQoS(int id)
	{
		this.QoS = id;
	}
	public Circuit() {
		super();		
	}
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public int getCircuitId() {
		return CircuitId;
	}
	public void setCircuitId(int circuitId) {
		CircuitId = circuitId;
	}
	public int getSrcNodeId() {
		return SrcNodeId;
	}
	public void setSrcNodeId(int srcNodeId) {
		SrcNodeId = srcNodeId;
	}
	public int getDestNodeId() {
		return DestNodeId;
	}
	public void setDestNodeId(int destNodeId) {
		DestNodeId = destNodeId;
	}
	
	
	public float getRequiredTraffic() {
		return RequiredTraffic;
	}
	public void setRequiredTraffic(float requiredTraffic) {
		RequiredTraffic = requiredTraffic;
	}
	public String getTrafficType() {
		return TrafficType;
	}
	public void setTrafficType(String trafficType) {
		TrafficType = trafficType;
	}
	public String getProtectionType() {
		return ProtectionType;
	}
	public void setProtectionType(String protectionType) {
		ProtectionType = protectionType;
	}
	public String getClientProtectionType() {
		return ClientProtectionType;
	}
	public void setClientProtectionType(String clientProtectionType) {
		ClientProtectionType = clientProtectionType;
	}
	public String getColourPreference() {
		return ColourPreference;
	}
	public void setColourPreference(String colourPreference) {
		ColourPreference = colourPreference;
	}	
	public String getLineRate() {
		return LineRate;
	}
	public void setLineRate(String lineRate) {
		LineRate = lineRate;
	}
	
	public String getChannelProtection() {
		return ChannelProtection;
	}
	public void setChannelProtection(String channelProtection) {
		ChannelProtection = channelProtection;
	}	
	
	public String getClientProtection() {
		return ClientProtection;
	}
	public void setClientProtection(String clientProtection) {
		ClientProtection = clientProtection;
	}
	public String getNodeInclusion() {
		return NodeInclusion;
	}
	public void setNodeInclusion(String nodeInclusion) {
		NodeInclusion = nodeInclusion;
	}
	public String getVendorlabel() {
		return VendorLabel;
	}
	public void setVendorlabel(String vendorlabel) {
		VendorLabel = vendorlabel;
	}
	public String getLambdaBlocking() {
		return LambdaBlocking;
	}
	public void setLambdaBlocking(String lambdaBlocking) {
		LambdaBlocking = lambdaBlocking;
	}
	
	public Circuit(int networkId, int circuitId, int qoS, int srcNodeId, int destNodeId, float requiredTraffic,
			String trafficType, String protectionType, String clientProtectionType, String colourPreference,
			String pathType, String lineRate, String channelProtection,String vendorLabel, String clientProtection,
			String lambdaBlocking, String nodeInclusion) {
		super();
		NetworkId = networkId;
		CircuitId = circuitId;
		QoS  = qoS;
		SrcNodeId = srcNodeId;
		DestNodeId = destNodeId;
		RequiredTraffic = requiredTraffic;
		TrafficType = trafficType;
		ProtectionType = protectionType;
		ClientProtectionType = clientProtectionType;
		ColourPreference = colourPreference;
		PathType = pathType;
		LineRate = lineRate;
		ChannelProtection = channelProtection;
		ClientProtection = clientProtection;
		VendorLabel = vendorLabel;
		NodeInclusion = nodeInclusion;
		LambdaBlocking = lambdaBlocking;
	}
	@Override
	public String toString() {
		return "Circuit [NetworkId=" + NetworkId + ", CircuitId=" + CircuitId + ", QoS=" + QoS + ",SrcNodeId=" + SrcNodeId
				+ ", DestNodeId=" + DestNodeId + ", RequiredTraffic=" + RequiredTraffic + ", TrafficType=" + TrafficType
				+ ", ProtectionType=" + ProtectionType + ", ClientProtectionType=" + ClientProtectionType
				+ ", ColourPreference=" + ColourPreference + ", PathType=" + PathType + ", LineRate=" + LineRate
				+ ", ChannelProtection=" + ChannelProtection + ", ClientProtection=" + ClientProtection
				+ ", VendorLabel=" + VendorLabel + ", NodeInclusion=" + NodeInclusion 
				+ ", LambdaBlocking=" + LambdaBlocking+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + CircuitId;
		result = prime * result + ((ClientProtectionType == null) ? 0 : ClientProtectionType.hashCode());
		result = prime * result + ((ColourPreference == null) ? 0 : ColourPreference.hashCode());
		result = prime * result + DestNodeId;
		result = prime * result + ((LineRate == null) ? 0 : LineRate.hashCode());
		result = prime * result + NetworkId;
		result = prime * result + ((PathType == null) ? 0 : PathType.hashCode());
		result = prime * result + ((ProtectionType == null) ? 0 : ProtectionType.hashCode());
		result = prime * result + Float.floatToIntBits(RequiredTraffic);
		result = prime * result + SrcNodeId;
		result = prime * result + ((TrafficType == null) ? 0 : TrafficType.hashCode());
		result = prime * result + ((ClientProtection == null) ? 0 : ClientProtection.hashCode());
		result = prime * result + ((VendorLabel == null) ? 0 : VendorLabel.hashCode());
		result = prime * result + ((NodeInclusion == null) ? 0 : NodeInclusion.hashCode());
		result = prime * result + ((LambdaBlocking == null) ? 0 : LambdaBlocking.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Circuit other = (Circuit) obj;
		if (CircuitId != other.CircuitId)
			return false;
		if (ClientProtectionType == null) {
			if (other.ClientProtectionType != null)
				return false;
		} else if (!ClientProtectionType.equals(other.ClientProtectionType))
			return false;
		if (ColourPreference == null) {
			if (other.ColourPreference != null)
				return false;
		} else if (!ColourPreference.equals(other.ColourPreference))
			return false;
		if (DestNodeId != other.DestNodeId)
			return false;
		if (LineRate == null) {
			if (other.LineRate != null)
				return false;
		} else if (!LineRate.equals(other.LineRate))
			return false;
		if (NetworkId != other.NetworkId)
			return false;
		if (PathType == null) {
			if (other.PathType != null)
				return false;
		} else if (!PathType.equals(other.PathType))
			return false;
		if (ProtectionType == null) {
			if (other.ProtectionType != null)
				return false;
		} else if (!ProtectionType.equals(other.ProtectionType))
			return false;
		if (Float.floatToIntBits(RequiredTraffic) != Float.floatToIntBits(other.RequiredTraffic))
			return false;
		if (SrcNodeId != other.SrcNodeId)
			return false;
		if ( QoS!= other.QoS)
			return false;
		if (TrafficType == null) {
			if (other.TrafficType != null)
				return false;
		} else if (!TrafficType.equals(other.TrafficType))
			return false;
		if (ProtectionType == null) {
			if (other.ProtectionType != null)
				return false;
		} else if (!ProtectionType.equals(other.ProtectionType))
			return false;
		if (ClientProtection == null) {
			if (other.ClientProtection != null)
				return false;
		} else if (!ClientProtection.equals(other.ClientProtection))
			return false;
		if (VendorLabel == null) {
			if (other.VendorLabel != null)
				return false;
		} else if (!VendorLabel.equals(other.VendorLabel))
			return false;
		if (NodeInclusion == null) {
			if (other.NodeInclusion != null)
				return false;
		} else if (!NodeInclusion.equals(other.NodeInclusion))
			return false;
		if (LambdaBlocking == null) {
			if (other.NodeInclusion != null)
				return false;
		} else if (!LambdaBlocking.equals(other.LambdaBlocking))
			return false;
		return true;
	}
	
	
	/* Compare two circuit objects*/
	public boolean compareCircuit(Circuit otherCircuit){

		if(TrafficType.equals(otherCircuit.TrafficType) && 
				PathType.equals(otherCircuit.PathType) && 
				NodeInclusion.equals(otherCircuit.NodeInclusion) && 
				LambdaBlocking.equals(otherCircuit.LambdaBlocking) && 
				VendorLabel.equals(otherCircuit.VendorLabel) && 
				ClientProtection.equals(otherCircuit.ClientProtection) && 
				ProtectionType.equals(otherCircuit.ProtectionType) && 
				ChannelProtection.equals(otherCircuit.ChannelProtection) && 
				ColourPreference.equals(otherCircuit.ColourPreference) && 
				ClientProtectionType.equals(otherCircuit.ClientProtectionType) &&
				LineRate.equals(otherCircuit.getLineRate()) &&
				SrcNodeId==otherCircuit.getSrcNodeId() &&
				QoS  == otherCircuit.getQoS() &&
				DestNodeId==otherCircuit.getDestNodeId() &&
				RequiredTraffic==otherCircuit.getRequiredTraffic())
			return true;	

		return false;
	}
	
	
}
