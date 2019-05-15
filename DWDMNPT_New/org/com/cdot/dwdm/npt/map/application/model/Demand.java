package application.model;

public class Demand {
	private int NetworkId;
	private int DemandId;
	private int SrcNodeId;
	private int DestNodeId;
	private float RequiredTraffic;
	private String ProtectionType;
	private String ColourPreference;
	private String CircuitSet;
	private String PathType;
	private String LineRate;
	private String ClientProtectionType;
	private String ChannelProtection;
	private String ClientProtection;
	private String LambdaBlocking;
	private String NodeInclusion;
	
	public Demand() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public int getDemandId() {
		return DemandId;
	}
	public void setDemandId(int demandId) {
		DemandId = demandId;
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
	public String getProtectionType() {
		return ProtectionType;
	}
	public void setProtectionType(String protectionType) {
		ProtectionType = protectionType;
	}
	public String getColourPreference() {
		return ColourPreference;
	}
	public void setColourPreference(String colourPreference) {
		ColourPreference = colourPreference;
	}
	public String getCircuitSet() {
		return CircuitSet;
	}
	public void setCircuitSet(String circuitSet) {
		CircuitSet = circuitSet;
	}
	
	public String getPathType() {
		return PathType;
	}
	public void setPathType(String pathType) {
		PathType = pathType;
	}
	
	public String getLineRate() {
		return LineRate;
	}
	public void setLineRate(String lineRate) {
		LineRate = lineRate;
	}
	
	
	public String getClientProtectionType() {
		return ClientProtectionType;
	}
	public void setClientProtectionType(String clientProtectionType) {
		ClientProtectionType = clientProtectionType;
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
	public String getLambdaBlocking() {
		return LambdaBlocking;
	}
	public void setLambdaBlocking(String lambdaBlocking) {
		LambdaBlocking = lambdaBlocking;
	}
	public String getNodeInclusion() {
		return NodeInclusion;
	}
	public void setNodeInclusion(String nodeInclusion) {
		NodeInclusion = nodeInclusion;
	}
	
	public Demand(int networkId, int demandId, int srcNodeId, int destNodeId, float requiredTraffic,
			String protectionType, String colourPreference, String circuitSet, String pathType, String lineRate,
			String clientProtectionType, String channelProtection,String clientProtection,String lambdaBlocking,String nodeInclusion) {
		super();
		NetworkId = networkId;
		DemandId = demandId;
		SrcNodeId = srcNodeId;
		DestNodeId = destNodeId;
		RequiredTraffic = requiredTraffic;
		ProtectionType = protectionType;
		ColourPreference = colourPreference;
		CircuitSet = circuitSet;
		PathType = pathType;
		LineRate = lineRate;
		ClientProtectionType = clientProtectionType;
		ChannelProtection = channelProtection;
		ClientProtection = clientProtection;
		LambdaBlocking = lambdaBlocking;
		NodeInclusion = nodeInclusion;
	}
	@Override
	public String toString() {
		return "Demand [NetworkId=" + NetworkId + ", DemandId=" + DemandId + ", SrcNodeId=" + SrcNodeId
				+ ", DestNodeId=" + DestNodeId + ", RequiredTraffic=" + RequiredTraffic + ", ProtectionType="
				+ ProtectionType + ", ColourPreference=" + ColourPreference + ", CircuitSet=" + CircuitSet
				+ ", PathType=" + PathType + ", LineRate=" + LineRate + ", ClientProtectionType=" + ClientProtectionType
				+ ", ChannelProtection=" + ChannelProtection + ", ClientProtection=" + ClientProtection 
				+ ", LambdaBlocking=" + LambdaBlocking + ", NodeInclusion=" + NodeInclusion+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ChannelProtection == null) ? 0 : ChannelProtection.hashCode());
		result = prime * result + ((CircuitSet == null) ? 0 : CircuitSet.hashCode());
		result = prime * result + ((ClientProtectionType == null) ? 0 : ClientProtectionType.hashCode());
		result = prime * result + ((ColourPreference == null) ? 0 : ColourPreference.hashCode());
		result = prime * result + DemandId;
		result = prime * result + DestNodeId;
		result = prime * result + ((LineRate == null) ? 0 : LineRate.hashCode());
		result = prime * result + NetworkId;
		result = prime * result + ((PathType == null) ? 0 : PathType.hashCode());
		result = prime * result + ((ProtectionType == null) ? 0 : ProtectionType.hashCode());
		result = prime * result + ((ClientProtection == null) ? 0 : ClientProtection.hashCode());
		result = prime * result + ((LambdaBlocking == null) ? 0 : LambdaBlocking.hashCode());
		result = prime * result + ((NodeInclusion == null) ? 0 : NodeInclusion.hashCode());
		result = prime * result + Float.floatToIntBits(RequiredTraffic);
		result = prime * result + SrcNodeId;
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
		Demand other = (Demand) obj;
		if (ChannelProtection == null) {
			if (other.ChannelProtection != null)
				return false;
		} else if (!ChannelProtection.equals(other.ChannelProtection))
			return false;
		if (CircuitSet == null) {
			if (other.CircuitSet != null)
				return false;
		} else if (!CircuitSet.equals(other.CircuitSet))
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
		if (DemandId != other.DemandId)
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
		
		/* New addition ***/
		if (ClientProtection == null) {
			if (other.ClientProtection != null)
				return false;
		} else if (!ClientProtection.equals(other.ClientProtection))
			return false;
		
		if (LambdaBlocking == null) {
			if (other.LambdaBlocking != null)
				return false;
		} else if (!LambdaBlocking.equals(other.LambdaBlocking))
			return false;
		
		if (NodeInclusion == null) {
			if (other.NodeInclusion != null)
				return false;
		} else if (!NodeInclusion.equals(other.NodeInclusion))
			return false;
		return true;
	}
	
	
		
}
