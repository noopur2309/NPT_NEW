package application.model;

public class DemandMapping {
	private int NetworkId;
	private int CircuitId;
	private int DemandId;
	private int SrcNodeId;
	private int DestNodeId;
	private float RequiredTraffic;
	private String TrafficType;
	private String ProtectionType;
	private String ClientProtectionType;
	private String ColourPreference;	
	private String PathType;
	private String LineRate;
	private String ChannelProtection;
	private String ClientProtection;
	private String LambdaBlocking;
	private String NodeInclusion;
	
	
	public DemandMapping() {
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
	
	public int getDemandId() {
		return DemandId;
	}
	public void setDemandId(int demandId) {
		DemandId = demandId;
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
	
	@Override
	public String toString() {
		return "DemandMapping [NetworkId=" + NetworkId + ", CircuitId=" + CircuitId + ", DemandId=" + DemandId
				+ ", SrcNodeId=" + SrcNodeId + ", DestNodeId=" + DestNodeId + ", RequiredTraffic=" + RequiredTraffic
				+ ", TrafficType=" + TrafficType + ", ProtectionType=" + ProtectionType + ", ClientProtectionType="
				+ ClientProtectionType + ", ColourPreference=" + ColourPreference + ", PathType=" + PathType
				+ ", LineRate=" + LineRate + ", ChannelProtection=" + ChannelProtection + "]";
	}
	public DemandMapping(int networkId, int circuitId, int demandId, int srcNodeId, int destNodeId, int requiredTraffic,
			String trafficType, String protectionType, String clientProtectionType, String colourPreference,
			String pathType, String lineRate, String channelProtection,String clientProtection,String lambdaBlocking,String nodeInclusion) {
		super();
		NetworkId = networkId;
		CircuitId = circuitId;
		DemandId = demandId;
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
		LambdaBlocking = lambdaBlocking;
		NodeInclusion = nodeInclusion;
	}
		

}
