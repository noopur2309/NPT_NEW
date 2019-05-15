package application.model;

public class SnmpModelNetworkRoute {
	
	/*private int networkId;
	private int demandId;*/

	private int PathOutputCount;
	
	public int getPathOutputCount() {
		return PathOutputCount;
	}
	public void setPathOutputCount(int pathOutputCount) {
		PathOutputCount = pathOutputCount;
	}

	private String ThreeRLocationHeadToTail;
	private String ThreeRLocationTailToHead;
	public SnmpModelNetworkRoutePathInfo[] snmpModelNetworkRoutePathInfo = new SnmpModelNetworkRoutePathInfo[2000];
	
	public String getThreeRLocationHeadToTail() {
		return ThreeRLocationHeadToTail;
	}
	public void setThreeRLocationHeadToTail(String threeRLocationHeadToTail) {
		ThreeRLocationHeadToTail = threeRLocationHeadToTail;
	}
	public String getThreeRLocationTailToHead() {
		return ThreeRLocationTailToHead;
	}
	public void setThreeRLocationTailToHead(String threeRLocationTailToHead) {
		ThreeRLocationTailToHead = threeRLocationTailToHead;
	}

	
public SnmpModelNetworkRoute(int totalPaths) {
		
	    this.PathOutputCount=totalPaths;
		for(int i=0;i<PathOutputCount;i++)
		{
			snmpModelNetworkRoutePathInfo[i]=new SnmpModelNetworkRoutePathInfo();
		}
		
	}
public SnmpModelNetworkRoute() {
	// TODO Auto-generated constructor stub
}

}
