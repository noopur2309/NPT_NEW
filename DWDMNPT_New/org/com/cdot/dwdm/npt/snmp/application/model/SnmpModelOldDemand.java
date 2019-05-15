/**
 * 
 */
package application.model;

/**
 * @author hp
 * @brief  This Model Represents the old demand [From NetworkRoute] which been already catered by RWA. 
 * 		   In case of Brown Field Execution This set need to be shared   	
 * @date   21/9/2017
 */
public class SnmpModelOldDemand {
	
	private int networkId;	
	private int demandId;
	private int routePriority;
	private int destNode;
	private int wavelengthNo;
	private int threeRLocationHeadToTail;
	private int threeRLocationTailToHead;
	
	
	private float traffic;
	private float osnr;
	
	private String lineRate;
	private String path;
	private String linkIdSet;
	private String pathType;
	private String regeneratorLoc;
	private String regeneratorLocOsnr;
	private String protectionType;
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
	public String getProtectionType() {
		return protectionType;
	}
	public void setProtectionType(String protectionType) {
		this.protectionType = protectionType;
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
	public int getRoutePriority() {
		return routePriority;
	}
	public void setRoutePriority(int routePriority) {
		this.routePriority = routePriority;
	}
	public int getDestNode() {
		return destNode;
	}
	public void setDestNode(int destNode) {
		this.destNode = destNode;
	}
	public int getWavelengthNo() {
		return wavelengthNo;
	}
	public void setWavelengthNo(int wavelengthNo) {
		this.wavelengthNo = wavelengthNo;
	}
	public String getLineRate() {
		return lineRate;
	}
	public void setLineRate(String lineRate) {
		this.lineRate = lineRate;
	}
	public float getTraffic() {
		return traffic;
	}
	public void setTraffic(float traffic) {
		this.traffic = traffic;
	}
	public float getOsnr() {
		return osnr;
	}
	public void setOsnr(float osnr) {
		this.osnr = osnr;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getLinkIdSet() {
		return linkIdSet;
	}
	public void setLinkIdSet(String linkIdSet) {
		this.linkIdSet = linkIdSet;
	}
	public String getPathType() {
		return pathType;
	}
	public void setPathType(String pathType) {
		this.pathType = pathType;
	}
	public String getRegeneratorLoc() {
		return regeneratorLoc;
	}
	public void setRegeneratorLoc(String regeneratorLoc) {
		this.regeneratorLoc = regeneratorLoc;
	}
	public String getRegeneratorLocOsnr() {
		return regeneratorLocOsnr;
	}
	public void setRegeneratorLocOsnr(String regeneratorLocOsnr) {
		this.regeneratorLocOsnr = regeneratorLocOsnr;
	}
	public int getThreeRLocationHeadToTail() {
		return threeRLocationHeadToTail;
	}
	public void setThreeRLocationHeadToTail(int threeRLocationHeadToTail) {
		this.threeRLocationHeadToTail = threeRLocationHeadToTail;
	}
	public int getThreeRLocationTailToHead() {
		return threeRLocationTailToHead;
	}
	public void setThreeRLocationTailToHead(int threeRLocationTailToHead) {
		this.threeRLocationTailToHead = threeRLocationTailToHead;
	}
	
	
}
