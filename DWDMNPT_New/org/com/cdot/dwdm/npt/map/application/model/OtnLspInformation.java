/**
 * @author hp
 * @date 1st Jun, 2018
 */

package application.model;

public class OtnLspInformation {
	
	
	private int NetworkId;
	private int DemandId;
	private String LineRate;
	private String Path;
	private int WavelengthNo;
	private int RoutePriority;		
	private String CircuitId;
	private String TrafficType;	
	private String ProtectionType;		
	private int OtnLspTunnelId;
	private int LspId;	
	private int ForwardingAdj;
	
	



	public OtnLspInformation() {
		super();		
	}


	public OtnLspInformation(int networkId, int demandId, String LineRate,
			String path, int WavelengthNo, int routePriority, 
			String CircuitId, String TrafficType, String ProtectionType, 
			 int OtnLspTunnelId, int LspId, int ForwardingAdj) {
		super();
		NetworkId = networkId;
		DemandId = demandId;
		this.LineRate = LineRate;		
		this.Path = path;		
		this.WavelengthNo    = WavelengthNo;		
		RoutePriority = routePriority;		
		this.CircuitId = CircuitId;		
		this.TrafficType    = TrafficType;
		this.ProtectionType    = ProtectionType;
		this.OtnLspTunnelId = OtnLspTunnelId;
		this.LspId = LspId;
		this.ForwardingAdj = ForwardingAdj;
		
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
	public String getLineRate() {
		return LineRate;
	}
	public void setLineRate(String LineRate) {
		this.LineRate = LineRate;
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
	public void setWavelengthNo(int WavelengthNo) {
		this.WavelengthNo = WavelengthNo;
	}
	public int getRoutePriority() {
		return RoutePriority;
	}
	public void setRoutePriority(int routePriority) {
		RoutePriority = routePriority;
	}
	public String getCircuitId() {
		return CircuitId;
	}
	public void setCircuitId(String CircuitId) {
		this.CircuitId = CircuitId;
	}
	public String getTrafficType() {
		return TrafficType;
	}
	public void setTrafficType(String TrafficType) {
		this.TrafficType = TrafficType;
	}
	public String getProtectionType() {
		return ProtectionType;
	}
	public void setProtectionType(String ProtectionType) {
		this.ProtectionType = ProtectionType;
	}
	public int getOtnLspTunnelId() {
		return OtnLspTunnelId;
	}
	public void setOtnLspTunnelId(int OtnLspTunnelId) {
		this.OtnLspTunnelId = OtnLspTunnelId;
	}
	public int getLspId() {
		return LspId;
	}
	public void setLspId(int lspId) {
		this.LspId = lspId;
	}
	public int getForwardingAdj() {
		return ForwardingAdj;
	}
	public void setForwardingAdj(int ForwardingAdj) {
		this.ForwardingAdj = ForwardingAdj;
	}


	@Override
	public String toString() {
		return "OtnLspInformation [NetworkId=" + NetworkId + ", DemandId=" + DemandId + 
									", LineRate=" + LineRate+", Path=" + Path + 									
									", WavelengthNo=" + WavelengthNo+
									", RoutePriority=" + RoutePriority+
									", CircuitId=" + CircuitId +  
									", TrafficType=" + TrafficType + 
									", ProtectionType=" + ProtectionType + 									
									", OtnLspTunnelId=" + OtnLspTunnelId + 									
									", LspId=" + LspId +
									", ForwardingAdj=" + ForwardingAdj +
				 					"]";
	}

	
		
}
