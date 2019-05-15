package application.model;

public class IpSchemeNode {	
	private int NetworkId;
	private int NodeId;
	private long LctIp;
	private long RouterIp;
	private long ScpIp;
	private long McpIp;
	private long McpSubnet;
	private long McpGateway;
	private long RsrvdIp1;
	private long RsrvdIp2;	
	public IpSchemeNode() {
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
	public long getLctIp() {
		return LctIp;
	}
	public void setLctIp(long lctIp) {
		LctIp = lctIp;
	}
	public long getRouterIp() {
		return RouterIp;
	}
	public void setRouterIp(long routerIp) {
		RouterIp = routerIp;
	}
	public long getScpIp() {
		return ScpIp;
	}
	public void setScpIp(long scpIp) {
		ScpIp = scpIp;
	}
	public long getMcpIp() {
		return McpIp;
	}
	public void setMcpIp(long mcpIp) {
		McpIp = mcpIp;
	}
	public long getRsrvdIp1() {
		return RsrvdIp1;
	}
	public void setRsrvdIp1(long rsrvdIp1) {
		RsrvdIp1 = rsrvdIp1;
	}
	public long getRsrvdIp2() {
		return RsrvdIp2;
	}
	public void setRsrvdIp2(long rsrvdIp2) {
		RsrvdIp2 = rsrvdIp2;
	}
	
	public long getMcpSubnet() {
		return McpSubnet;
	}
	public void setMcpSubnet(long mcpSubnet) {
		McpSubnet = mcpSubnet;
	}
	public long getMcpGateway() {
		return McpGateway;
	}
	public void setMcpGateway(long mcpGateway) {
		McpGateway = mcpGateway;
	}
	public IpSchemeNode(int networkId, int nodeId, long lctIp, long routerIp, long scpIp, long mcpIp, long mcpSubnet,
			long mcpGateway, long rsrvdIp1, long rsrvdIp2) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		LctIp = lctIp;
		RouterIp = routerIp;
		ScpIp = scpIp;
		McpIp = mcpIp;
		McpSubnet = mcpSubnet;
		McpGateway = mcpGateway;
		RsrvdIp1 = rsrvdIp1;
		RsrvdIp2 = rsrvdIp2;
	}
	@Override
	public String toString() {
		return "IpSchemeNode [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", LctIp=" + LctIp + ", RouterIp="
				+ RouterIp + ", ScpIp=" + ScpIp + ", McpIp=" + McpIp + ", McpSubnet=" + McpSubnet + ", McpGateway="
				+ McpGateway + ", RsrvdIp1=" + RsrvdIp1 + ", RsrvdIp2=" + RsrvdIp2 + "]";
	}
	
	
	
}
