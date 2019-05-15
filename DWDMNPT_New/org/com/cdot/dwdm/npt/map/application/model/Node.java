package application.model;

public class Node {	
	private int NetworkId;
	private int NodeId;
	private String StationName;
	private String SiteName;
	private int NodeType;
	private int NodeSubType;
	private int Degree;
	private String Ip;
	private int IsGne;
	private int VlanTag;	
	private String EmsSubnet;
	private String EmsGateway;
	private String IpV6Add;
	private String Capacity;
	private int Direction;
	private String OpticalReach;
	public Node() {
		super();		
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
	public String getStationName() {
		return StationName;
	}
	public void setStationName(String stationName) {
		StationName = stationName;
	}
	public String getSiteName() {
		return SiteName;
	}
	public void setSiteName(String siteName) {
		SiteName = siteName;
	}
	public int getNodeType() {
		return NodeType;
	}
	public void setNodeType(int nodeType) {
		NodeType = nodeType;
	}
	public int getDegree() {
		return Degree;
	}
	public void setDegree(int degree) {
		Degree = degree;
	}
	public String getIp() {
		return Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	
	public void setIsGne(int isGne) {
		IsGne = isGne;
	}
	public int getVlanTag() {
		return VlanTag;
	}
	public void setVlanTag(int vlanTag) {
		VlanTag = vlanTag;
	}
	
	
	public int getNodeSubType() {
		return NodeSubType;
	}
	public void setNodeSubType(int nodeSubType) {
		NodeSubType = nodeSubType;
	}
	public int getIsGne() {
		return IsGne;
	}

	public String getEmsSubnet() {
		return EmsSubnet;
	}

	public void setEmsSubnet(String emsSubnet) {
		EmsSubnet = emsSubnet;
	}

	public String getEmsGateway() {
		return EmsGateway;
	}

	public void setEmsGateway(String emsGateway) {
		EmsGateway = emsGateway;
	}

	public String getIpV6Add() {
		return IpV6Add;
	}

	public void setIpV6Add(String ipV6Add) {
		IpV6Add = ipV6Add;
	}

	public String getCapacity() {
		return Capacity;
	}

	public void setCapacity(String capacity) {
		Capacity = capacity;
	}

	public int getDirection() {
		return Direction;
	}

	public void setDirection(int direction) {
		Direction = direction;
	}
	
	

	public String getOpticalReach() {
		return OpticalReach;
	}

	public void setOpticalReach(String opticalReach) {
		OpticalReach = opticalReach;
	}

	@Override
	public String toString() {
		return "Node [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", StationName=" + StationName + ", SiteName="
				+ SiteName + ", NodeType=" + NodeType + ", NodeSubType=" + NodeSubType + ", Degree=" + Degree + ", Ip="
				+ Ip + ", IsGne=" + IsGne + ", VlanTag=" + VlanTag + ", EmsSubnet=" + EmsSubnet + ", EmsGateway="
				+ EmsGateway + ", IpV6Add=" + IpV6Add + ", Capacity=" + Capacity + ", Direction=" + Direction
				+ ", OpticalReach=" + OpticalReach + "]";
	}

	public Node(int networkId, int nodeId, String stationName, String siteName, int nodeType, int nodeSubType,
			int degree, String ip, int isGne, int vlanTag, String emsSubnet, String emsGateway, String ipV6Add,
			String capacity, int direction, String opticalReach) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		StationName = stationName;
		SiteName = siteName;
		NodeType = nodeType;
		NodeSubType = nodeSubType;
		Degree = degree;
		Ip = ip;
		IsGne = isGne;
		VlanTag = vlanTag;
		EmsSubnet = emsSubnet;
		EmsGateway = emsGateway;
		IpV6Add = ipV6Add;
		Capacity = capacity;
		Direction = direction;
		OpticalReach = opticalReach;
	}


}
