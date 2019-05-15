package application.model;

public class RouteMapping {
	private int NetworkId;
	private int DemandId;
	private int SrcNodeId;
	private int DestNodeId;	
	private float Traffic;
	private String ProtectionType;
	private int RoutePriority;
	private String Path;
	private String PathLinkId;
	private String PathType;
	private String LineRate;
	private String RegeneratorLoc;
	private String Error;

	public String getLineRate() {
		return LineRate;
	}


	public void setLineRate(String lineRate) {
		LineRate = lineRate;
	}


	public String getRegeneratorLoc() {
		return RegeneratorLoc;
	}


	public void setRegeneratorLoc(String regeneratorLoc) {
		RegeneratorLoc = regeneratorLoc;
	}


	/**Added for Joint/Disjoint path decision*/
	private int WavelengthNo;
	private float Osnr;
	private float RouteLength;
	private int RouteCost;
	private float RouteCd;
	private double RoutePmd;
	public RouteMapping() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
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
	
	public String getProtectionType() {
		return ProtectionType;
	}
	public void setProtectionType(String protectionType) {
		ProtectionType = protectionType;
	}
	public String getPath() {
		return Path;
	}
	public void setPath(String path) {
		Path = path;
	}
	public String getPathType() {
		return PathType;
	}
	public void setPathType(String pathType) {
		PathType = pathType;
	}
	public int getWavelengthNo() {
		return WavelengthNo;
	}
	public void setWavelengthNo(int wavelengthNo) {
		WavelengthNo = wavelengthNo;
	}
	public float getOsnr() {
		return Osnr;
	}
	public void setOsnr(float osnr) {
		Osnr = osnr;
	}
	public float getRouteLength() {
		return RouteLength;
	}
	public void setRouteLength(float len) {
		RouteLength = len;
	}
	
	public int getRouteCost() {
		return RouteCost;
	}
	public void setRouteCost(int routeCost) {
		RouteCost = routeCost;
	}

	public int getDemandId() {
		return DemandId;
	}
	
	public void setDemandId(int demandId) {
		DemandId = demandId;
	}

	public float getTraffic() {
		return Traffic;
	}

	public void setTraffic(float traffic) {
		Traffic = traffic;
	}

	public int getRoutePriority() {
		return RoutePriority;
	}

	public void setRoutePriority(int routePriority) {
		RoutePriority = routePriority;
	}


	public float getRouteCd() {
		return RouteCd;
	}


	public void setRouteCd(float routeCd) {
		RouteCd = routeCd;
	}


	public double getRoutePmd() {
		return RoutePmd;
	}


	public void setRoutePmd(double routePmd) {
		RoutePmd = routePmd;
	}
	


	public String getError() {
		return Error;
	}


	public void setError(String error) {
		Error = error;
	}


	@Override
	public String toString() {
		return "RouteMapping [NetworkId=" + NetworkId + ", DemandId=" + DemandId + ", SrcNodeId=" + SrcNodeId
				+ ", DestNodeId=" + DestNodeId + ", Traffic=" + Traffic + ", ProtectionType=" + ProtectionType
				+ ", RoutePriority=" + RoutePriority + ", Path=" + Path + ", PathType=" + PathType + ", LineRate="
				+ LineRate + ", RegeneratorLoc=" + RegeneratorLoc + ", Error=" + Error + ", WavelengthNo="
				+ WavelengthNo + ", Osnr=" + Osnr + ", RouteLength=" + RouteLength + ", RouteCost=" + RouteCost
				+ ", RouteCd=" + RouteCd + ", RoutePmd=" + RoutePmd + "]";
	}


	public RouteMapping(int networkId, int demandId, int srcNodeId, int destNodeId, float traffic,
			String protectionType, int routePriority, String path, String pathType, String lineRate,
			String regeneratorLoc, String error, int wavelengthNo, float osnr, int routeLength, int routeCost,
			float routeCd, double routePmd) {
		super();
		NetworkId = networkId;
		DemandId = demandId;
		SrcNodeId = srcNodeId;
		DestNodeId = destNodeId;
		Traffic = traffic;
		ProtectionType = protectionType;
		RoutePriority = routePriority;
		Path = path;
		PathType = pathType;
		LineRate = lineRate;
		RegeneratorLoc = regeneratorLoc;
		Error = error;
		WavelengthNo = wavelengthNo;
		Osnr = osnr;
		RouteLength = routeLength;
		RouteCost = routeCost;
		RouteCd = routeCd;
		RoutePmd = routePmd;
	}


	public String getPathLinkId() {
		return PathLinkId;
	}


	public void setPathLinkId(String pathLinkId) {
		PathLinkId = pathLinkId;
	}


		
			
}
