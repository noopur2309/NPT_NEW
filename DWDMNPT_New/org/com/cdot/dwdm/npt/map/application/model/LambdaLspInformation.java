/**
 * @author hp
 * @date 24th May, 2018
 */

package application.model;

public class LambdaLspInformation {
	
	
	private int NetworkId;
	private int DemandId;
	private int RoutePriority;
	private String Path;	
	private int LambdaLspTunnelId;
	private int LspId;	
	private int ForwardingAdj;
	// private int OtnLspTunnelId;		
	// private String ServiceType;	
	


	public LambdaLspInformation() {
		super();		
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
	
	public int getLambdaLspTunnelId() {
		return LambdaLspTunnelId;
	}
	public void setLambdaLspTunnelId(int LambdaLspTunnelId) {
		this.LambdaLspTunnelId = LambdaLspTunnelId;
	}

	 public int getLspId() {
	 	return LspId;
	 }
	 public void setLspId(int LspId) {
	 	this.LspId = LspId;
	 }
	 
	 public int getForwardingAdj() {
		 	return ForwardingAdj;
	 }
	 public void setForwardingAdj(int ForwardingAdj) {
	 	this.ForwardingAdj = ForwardingAdj;
	 }



	// public String getServiceType() {
	// 	return ServiceType;
	// }
	// public void setServiceType(String ServiceType) {
	// 	this.ServiceType = ServiceType;
	// }

	public LambdaLspInformation(int networkId, int demandId, int routePriority, 
			String path, int LambdaLspTunnelId, int LspId,int ForwardingAdj) {
		super();
		NetworkId = networkId;
		DemandId = demandId;
		RoutePriority = routePriority;
		Path = path;
		this.LambdaLspTunnelId = LambdaLspTunnelId;		
		this.LspId = LspId;		
	    this.ForwardingAdj    = ForwardingAdj;
		
	}

	@Override
	public String toString() {
		return "LambdaLspInformation [NetworkId=" + NetworkId + ", DemandId=" + DemandId + ", RoutePriority=" + RoutePriority
				+ ", Path=" + Path + ", LambdaLspTunnelId=" + LambdaLspTunnelId +
				", LspId=" + LspId + 
				", ForwardingAdj=" + ForwardingAdj +
				 "]";
	}

	
		
}
