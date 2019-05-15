package application.model;

public class Circuit10gAgg {
	
	private int NetworkId;
	private int CircuitId;
	private int Circuit10gAggId;
	private float RequiredTraffic;
	private String TrafficType;
	private String LineRate;	
	
	
	public Circuit10gAgg() {
		super();		
	}
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public int getCircuitId() {
		return CircuitId;
	}
	public void setCircuitId(int circuitId) {
		CircuitId = circuitId;
	}
	public int getCircuit10gAggId() {
		return Circuit10gAggId;
	}
	public void setCircuit10gAggId(int Circuit10gAggId) {
		this.Circuit10gAggId = Circuit10gAggId;
	}
	
	
	public float getRequiredTraffic() {
		return RequiredTraffic;
	}
	public void setRequiredTraffic(float requiredTraffic) {
		RequiredTraffic = requiredTraffic;
	}
	public String getTrafficType() {
		return TrafficType;
	}
	public void setTrafficType(String trafficType) {
		TrafficType = trafficType;
	}
	
	public String getLineRate() {
		return LineRate;
	}
	public void setLineRate(String lineRate) {
		LineRate = lineRate;
	}
	

	public Circuit10gAgg(int networkId, int circuitId, int Circuit10gAggId, float requiredTraffic,
			String trafficType, String lineRate) {
		super();
		NetworkId = networkId;
		CircuitId = circuitId;
		this.Circuit10gAggId = Circuit10gAggId;		
		RequiredTraffic = requiredTraffic;
		TrafficType = trafficType;		
		LineRate = lineRate;
		
	}
	@Override
	public String toString() {
		return "Circuit [NetworkId=" + NetworkId + ", CircuitId=" + CircuitId + ", Circuit10gAggId=" + Circuit10gAggId
				+ ", RequiredTraffic=" + RequiredTraffic + ", TrafficType=" + TrafficType+ ", LineRate=" + LineRate+"]";
	}	
	
		
	
}
