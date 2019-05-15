package application.model;

import java.text.SimpleDateFormat;

public class Network {
	private int NetworkId;
	private String NetworkName;	
	private String SubNetworkId;
	private String Topology;
	private String Area;
	private String ServiceProvider;		
	private String SAPI;
	private int NetworkIdBrownField;
	private String NetworkUpdateDate; 
	private String NetworkBrownFieldUpdateDate; 
	private String UserName;
	public Network() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public String getSubNetworkId() {
		return SubNetworkId;
	}
	public void setSubNetworkId(String subNetworkId) {
		SubNetworkId = subNetworkId;
	}	
	public String getNetworkName() {
		return NetworkName;
	}
	public void setNetworkName(String networkName) {
		NetworkName = networkName;
	}

	public String getTopology() {
		return Topology;
	}

	public void setTopology(String topology) {
		Topology = topology;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

	public String getServiceProvider() {
		return ServiceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		ServiceProvider = serviceProvider;
	}
	
	

	
	public int getNetworkIdBrownField() {
		return NetworkIdBrownField;
	}

	public void setNetworkIdBrownField(int networkIdBrownField) {
		NetworkIdBrownField = networkIdBrownField;
	}


	public String getNetworkUpdateDate() {
		return NetworkUpdateDate;
	}

	public void setNetworkUpdateDate(String networkUpdateDate) {
		NetworkUpdateDate = networkUpdateDate;
	}

	public String getNetworkBrownFieldUpdateDate() {
		return NetworkBrownFieldUpdateDate;
	}

	public void setNetworkBrownFieldUpdateDate(String networkBrownFieldUpdateDate) {
		NetworkBrownFieldUpdateDate = networkBrownFieldUpdateDate;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}
	
	

	public String getSAPI() {
		return SAPI;
	}

	public void setSAPI(String sAPI) {
		SAPI = sAPI;
	}

	@Override
	public String toString() {
		return "Network [NetworkId=" + NetworkId + ", NetworkName=" + NetworkName + ", Topology=" + Topology + ", Area="
				+ Area + ", ServiceProvider=" + ServiceProvider + ", SAPI=" + SAPI + ", NetworkIdBrownField="
				+ NetworkIdBrownField + ", NetworkUpdateDate=" + NetworkUpdateDate + ", NetworkBrownFieldUpdateDate="
				+ NetworkBrownFieldUpdateDate + ", UserName=" + UserName + "]";
	}

	public Network(int networkId, String networkName, String topology, String area, String serviceProvider, String sAPI,
			int networkIdBrownField, String networkUpdateDate, String networkBrownFieldUpdateDate, String userName) {
		super();
		NetworkId = networkId;
		NetworkName = networkName;
		Topology = topology;
		Area = area;
		ServiceProvider = serviceProvider;
		SAPI = sAPI;
		NetworkIdBrownField = networkIdBrownField;
		NetworkUpdateDate = networkUpdateDate;
		NetworkBrownFieldUpdateDate = networkBrownFieldUpdateDate;
		UserName = userName;
	}

	
}
