package application.model;

public class IpDetails {

	String mcpIp;
	String subnet;
	String gateway;
	String lctIp;
	String scpIp;
	String routerIp;

	public String getMcpIp() {
		return mcpIp;
	}
	public void setMcpIp(String mcpIp) {
		this.mcpIp = mcpIp;
	}
	public String getSubnet() {
		return subnet;
	}
	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getLctIp() {
		return lctIp;
	}
	public void setLctIp(String lctIp) {
		this.lctIp = lctIp;
	}
	public String getScpIp() {
		return scpIp;
	}
	public void setScpIp(String scpIp) {
		this.scpIp = scpIp;
	}
	public String getRouterIp() {
		return routerIp;
	}
	public void setRouterIp(String routerIp) {
		this.routerIp = routerIp;
	}


}