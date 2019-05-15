package application.model;

public class MapData {
	private int NetworkId;
	private String Map;
	public MapData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public String getMap() {
		return Map;
	}
	public void setMap(String map) {
		Map = map;
	}
	@Override
	public String toString() {
		return "MapData [NetworkId=" + NetworkId + ", Map=" + Map + "]";
	}
	public MapData(int networkId, String map) {
		super();
		NetworkId = networkId;
		Map = map;
	}
	
}
