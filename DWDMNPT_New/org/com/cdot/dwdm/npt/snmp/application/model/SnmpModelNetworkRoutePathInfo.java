package application.model;

public class SnmpModelNetworkRoutePathInfo {
	
	private int networkId;
	private int demandId;
	private String Path;	
	private String PathLinkId;	
	private String PathType;/**Added for Path Joint and Disjoint decision*/
	private int WavelengthNo;
    private String RegeneratorLocations;
    private String RegeneratorLocationsOsnr;
	private float Osnr;
	private int RoutePriority;
	private float Traffic;
	private String LineRate;
	private String Error;
	
    
	public String getError() {
		return Error;
	}
	public void setError(String error) {
		Error = error;
	}
	public String getLineRate() {
		return LineRate;
	}
	public void setLineRate(String lineRate) {
		LineRate = lineRate;
	}
	public String getRegeneratorLocations() {
		return RegeneratorLocations;
	}
	public void setRegeneratorLocations(String regeneratorLocations) {
		RegeneratorLocations = regeneratorLocations;
	}
	
	public String getRegeneratorLocationsOsnr() {
		return RegeneratorLocationsOsnr;
	}
	public void setRegeneratorLocationsOsnr(String regeneratorLocationsOsnr) {
		RegeneratorLocationsOsnr = regeneratorLocationsOsnr;
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
		return RoutePriority;
	}
	public void setRoutePriority(int routePriority) {
		RoutePriority = routePriority;
	}
	public float getTraffic() {
		return Traffic;
	}
	public void setTraffic(float traffic) {
		Traffic = traffic;
	}
	public String getPathType() {
		return PathType;
	}
	public void setPathType(String pathType) {
		PathType = pathType;
	}
	public String getPath() {
		return Path;
	}
	public void setPath(String path) {
		Path = path;
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
	public String getPathLinkId() {
		return PathLinkId;
	}
	public void setPathLinkId(String pathLinkId) {
		PathLinkId = pathLinkId;
	}
	
	
}
