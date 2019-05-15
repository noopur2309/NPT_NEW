package application.model;

public class CircuitCheck {
	private int NetworkId;
	private int CircuitId;
	private int SrcNodeId;
	private int DestNodeId;
	private float RequiredTraffic;
	private String TrafficType;
	private String ProtectionType;
	private String ClientProtectionType;
	private String ColourPreference;
	private String PathType;
	private String LineRate;
	private boolean flag;
	
	
	public String getPathType() {
		return PathType;
	}
	public void setPathType(String pathType) {
		PathType = pathType;
	}
	public CircuitCheck() {
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
	
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public CircuitCheck(int networkId, int circuitId, int srcNodeId, int destNodeId, float requiredTraffic,
			String trafficType, String protectionType, String clientProtectionType, String colourPreference,
			String pathType, String lineRate, boolean flag) {
		super();
		NetworkId = networkId;
		CircuitId = circuitId;
		SrcNodeId = srcNodeId;
		DestNodeId = destNodeId;
		RequiredTraffic = requiredTraffic;
		TrafficType = trafficType;
		ProtectionType = protectionType;
		ClientProtectionType = clientProtectionType;
		ColourPreference = colourPreference;
		PathType = pathType;
		LineRate = lineRate;
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "CircuitCheck [\nCircuitId=" + CircuitId + ", \nRequiredTraffic=" + RequiredTraffic + ", \nflag=" + flag + "]";
	}
	
	
	
}
