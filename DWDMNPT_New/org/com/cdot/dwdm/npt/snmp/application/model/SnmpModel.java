package application.model;

public class SnmpModel {

	private int NetworkId;
	private int NumberOfNodes;
	private int NumberofLinks;
	private int NumberofDemands;
	private int NumberofOldDemands;
	private String NetworkFieldType;

	
	public String getNetworkFieldType() {
		return NetworkFieldType;
	}
	public void setNetworkFieldType(String networkFieldType) {
		this.NetworkFieldType = networkFieldType;
	}
	
	public int getNetworkId() {
		return NetworkId;
	}

	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	
	public int getNumberofDemands() {
		return NumberofDemands;
	}

	public void setNumberofDemands(int numberofDemands) {
		NumberofDemands = numberofDemands;
	}

	public int getNumberOfNodes() {
		return NumberOfNodes;
	}

	public void setNumberOfNodes(int numberOfNodes) {
		NumberOfNodes = numberOfNodes;
	}

	public int getNumberofLinks() {
		return NumberofLinks;
	}

	public void setNumberofLinks(int numberofLinks) {
		NumberofLinks = numberofLinks;
	}
	
	public int getNumberofOldDemands() {
		return NumberofOldDemands;
	}

	public void setNumberofOldDemands(int numberofOldDemands) {
		NumberofOldDemands = numberofOldDemands;
	}


	
	public SnmpModelNode[] snmpModelNode = new SnmpModelNode[1000];
	public SnmpModelLink[] snmpModelLink = new SnmpModelLink[1000];
	public SnmpModelDemand[] snmpModelDemand = new SnmpModelDemand[1000];
	public SnmpModelOldDemand[] snmpModelOldDemand = new SnmpModelOldDemand[1000];/**Old Demand data for Brown Field*/
	
	public SnmpModel(int nodes, int links,int demands, int oldDemands, String networkFieldType) {
		
		NumberOfNodes = nodes;
		NumberofLinks = links;
		NumberofDemands= demands;
		NetworkFieldType = networkFieldType;
		
		for(int i=0;i<NumberOfNodes;i++)
		{
			snmpModelNode[i]=new SnmpModelNode();
		}
		
		for(int i=0;i<NumberofLinks;i++)
		{
			snmpModelLink[i]=new SnmpModelLink();
		}
		
		for(int i=0;i<NumberofDemands;i++)
		{
			snmpModelDemand[i]=new SnmpModelDemand();
		}
		
		for(int i=0;i<oldDemands;i++)
		{
			snmpModelOldDemand[i]=new SnmpModelOldDemand();
		}
	}


}
