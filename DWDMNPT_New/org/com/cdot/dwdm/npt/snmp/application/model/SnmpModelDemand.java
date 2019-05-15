package application.model;

public class SnmpModelDemand {
	
	private int networkId;
	private int demandId;
	private int srcNode;
	private int destNode;
	private float requiredTraffic;
	private String lineRate;
	private String protectionType;
	private String colorPreference;
	private String PathType;
	private String ChannelProtection;
	private String ClientProtection;
	private String LambdaBlocking;
	private String NodeInclusion;
	
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
	public String getPathType() {
		return PathType;
	}
	public void setPathType(String pathType) {
		this.PathType = pathType;
	}
	public int getNetworkId() {
		return networkId;
	}
	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}
	
	public int getDemandId() {
		return demandId;
	}
	public void setDemandId(int demandId) {
		this.demandId = demandId;
	}
	public int getSrcNode() {
		return srcNode;
	}
	public void setSrcNode(int srcNode) {
		this.srcNode = srcNode;
	}
	public int getDestNode() {
		return destNode;
	}
	public void setDestNode(int destNode) {
		this.destNode = destNode;
	}

	public float getRequiredTraffic() {
		return requiredTraffic;
	}
	public void setRequiredTraffic(float requiredTraffic) {
		this.requiredTraffic = requiredTraffic;
	}
	public String getProtectionType() {
		return protectionType;
	}
	public void setProtectionType(String string) {
		this.protectionType = string;
	}
	public String getColorPreference() {
		return colorPreference;
	}
	public void setColorPreference(String colorPreference) {
		this.colorPreference = colorPreference;
	}
	public String getLineRate() {
		return lineRate;
	}
	public void setLineRate(String lineRate) {
		this.lineRate = lineRate;
	}
	
	

}
