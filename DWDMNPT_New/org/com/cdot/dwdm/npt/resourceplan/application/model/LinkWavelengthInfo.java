package application.model;

/**
 * 
 * @author gpon
 * This associates the data a particular wavelength on a link with its details like traffic etc.
 *
 */
public class LinkWavelengthInfo {	
	private int NetworkId;	
	private int LinkId;
	private int Wavelength;
	private int DemandId;
	private String LineRate;
	private float Traffic;	
	private String Path;
	private int RoutePriority;
	private String Status;
	public LinkWavelengthInfo() {
		super();		
	}
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public int getLinkId() {
		return LinkId;
	}
	public void setLinkId(int linkId) {
		LinkId = linkId;
	}
	public int getWavelength() {
		return Wavelength;
	}
	public void setWavelength(int wavelength) {
		Wavelength = wavelength;
	}
	public int getDemandId() {
		return DemandId;
	}
	public void setDemandId(int demandId) {
		DemandId = demandId;
	}
	
	public String getLineRate() {
		return LineRate;
	}
	public void setLineRate(String lineRate) {
		LineRate = lineRate;
	}
	
	public float getTraffic() {
		return Traffic;
	}
	public void setTraffic(float traffic) {
		Traffic = traffic;
	}
	public String getPath() {
		return Path;
	}
	public void setPath(String path) {
		Path = path;
	}
	
	public int getRoutePriority() {
		return RoutePriority;
	}
	public void setRoutePriority(int routePriority) {
		RoutePriority = routePriority;
	}
	
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public LinkWavelengthInfo(int networkId, int linkId, int wavelength, int demandId, String lineRate, float traffic,
			String path, int routePriority, String status) {
		super();
		NetworkId = networkId;
		LinkId = linkId;
		Wavelength = wavelength;
		DemandId = demandId;
		LineRate = lineRate;
		Traffic = traffic;
		Path = path;
		RoutePriority = routePriority;
		Status = status;
	}
	@Override
	public String toString() {
		return "LinkWavelengthInfo [NetworkId=" + NetworkId + ", LinkId=" + LinkId + ", Wavelength=" + Wavelength
				+ ", DemandId=" + DemandId + ", LineRate=" + LineRate + ", Traffic=" + Traffic + ", Path=" + Path
				+ ", RoutePriority=" + RoutePriority + ", Status=" + Status + "]";
	}
	
	
	
	
	
}
