package application.model;

public class CircuitDemandMapping {
	private int NetworkId;
	private int CircuitId;
	private int DemandId;
	
	public CircuitDemandMapping() {
		super();
		// TODO Auto-generated constructor stub
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


	public CircuitDemandMapping(int networkId, int circuitId, int demandId) {
		super();
		NetworkId = networkId;
		CircuitId = circuitId;
		DemandId = demandId;
	}


	public int getCircuitId() {
		return CircuitId;
	}


	public void setCircuitId(int circuitId) {
		CircuitId = circuitId;
	}


	@Override
	public String toString() {
		return "CircuitDemandMapping [NetworkId=" + NetworkId + ", CircuitId=" + CircuitId + ", DemandId=" + DemandId
				+ "]";
	}
	
	
		

}
