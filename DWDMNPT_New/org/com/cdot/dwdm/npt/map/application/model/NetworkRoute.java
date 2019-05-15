package application.model;

public class NetworkRoute {
	private int NetworkId;
	private int DemandId;
	private int RoutePriority;
	private String Path;
	private String PathLinkId;
	private String PathType;/**Added for Joint or Disjoint path decision*/
	private float Traffic;
	private int WavelengthNo;
	private String LineRate;
	private float Osnr;
	private String RegeneratorLoc;
	private String RegeneratorLocOsnr;
	private String Error;
	private int ThreeRLocationHeadToTail;
	private int ThreeRLocationTailToHead;	
	public NetworkRoute() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getRegeneratorLocOsnr() {
		return RegeneratorLocOsnr;
	}

	public void setRegeneratorLocOsnr(String regeneratorLocOsnr) {
		RegeneratorLocOsnr = regeneratorLocOsnr;
	}

	public String getRegeneratorLoc() {
		return RegeneratorLoc;
	}

	public void setRegeneratorLoc(String regeneratorLoc) {
		RegeneratorLoc = regeneratorLoc;
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
	public int getRoutePriority() {
		return RoutePriority;
	}
	public void setRoutePriority(int routePriority) {
		RoutePriority = routePriority;
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
	public float getTraffic() {
		return Traffic;
	}
	public void setTraffic(float traffic) {
		Traffic = traffic;
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
	public int getThreeRLocationHeadToTail() {
		return ThreeRLocationHeadToTail;
	}
	public void setThreeRLocationHeadToTail(int threeRLocationHeadToTail) {
		ThreeRLocationHeadToTail = threeRLocationHeadToTail;
	}
	public int getThreeRLocationTailToHead() {
		return ThreeRLocationTailToHead;
	}
	public void setThreeRLocationTailToHead(int threeRLocationTailToHead) {
		ThreeRLocationTailToHead = threeRLocationTailToHead;
	}

	public String getLineRate() {
		return LineRate;
	}

	public void setLineRate(String lineRate) {
		LineRate = lineRate;
	}
	

	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
	}

	public NetworkRoute(int networkId, int demandId, int routePriority, String path, String pathType, float traffic,
			int wavelengthNo, String lineRate, float osnr, String regeneratorLoc, String regeneratorLocOsnr,
			String error, int threeRLocationHeadToTail, int threeRLocationTailToHead) {
		super();
		NetworkId = networkId;
		DemandId = demandId;
		RoutePriority = routePriority;
		Path = path;
		PathType = pathType;
		Traffic = traffic;
		WavelengthNo = wavelengthNo;
		LineRate = lineRate;
		Osnr = osnr;
		RegeneratorLoc = regeneratorLoc;
		RegeneratorLocOsnr = regeneratorLocOsnr;
		Error = error;
		ThreeRLocationHeadToTail = threeRLocationHeadToTail;
		ThreeRLocationTailToHead = threeRLocationTailToHead;
	}

	@Override
	public String toString() {
		return "NetworkRoute [NetworkId=" + NetworkId + ", DemandId=" + DemandId + ", RoutePriority=" + RoutePriority
				+ ", Path=" + Path + ", PathType=" + PathType + ", Traffic=" + Traffic + ", WavelengthNo="
				+ WavelengthNo + ", LineRate=" + LineRate + ", Osnr=" + Osnr + ", RegeneratorLoc=" + RegeneratorLoc
				+ ", RegeneratorLocOsnr=" + RegeneratorLocOsnr + ", Error=" + Error + ", ThreeRLocationHeadToTail="
				+ ThreeRLocationHeadToTail + ", ThreeRLocationTailToHead=" + ThreeRLocationTailToHead + "]";
	}

	public String getPathLinkId() {
		return PathLinkId;
	}

	public void setPathLinkId(String pathLinkId) {
		PathLinkId = pathLinkId;
	}

	
		
}
