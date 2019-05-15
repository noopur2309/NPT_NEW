package application.model;

/**
 * 
 * @author gpon
 * This is to be used for Optical data calculations at the controller end
 *
 */
public class LinkWavelengthMap {	
	private int NetworkId;
	private int NodeId;
	private int LinkId;
	String Direction;
	float SpanLoss;
	private String Wavelengths;
	private int nWaves;
	private int nMpns;
	public LinkWavelengthMap() {
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
	public String getDirection() {
		return Direction;
	}
	public void setDirection(String direction) {
		Direction = direction;
	}
	public float getSpanLoss() {
		return SpanLoss;
	}
	public void setSpanLoss(float spanLoss) {
		SpanLoss = spanLoss;
	}
	public String getWavelengths() {
		return Wavelengths;
	}
	public void setWavelengths(String wavelengths) {
		Wavelengths = wavelengths;
	}
	public int getLinkId() {
		return LinkId;
	}
	public void setLinkId(int linkId) {
		LinkId = linkId;
	}
	
	public int getnWaves() {
		return nWaves;
	}
	public void setnWaves(int nWaves) {
		this.nWaves = nWaves;
	}
	public int getnMpns() {
		return nMpns;
	}
	public void setnMpns(int nMpns) {
		this.nMpns = nMpns;
	}
	public LinkWavelengthMap(int networkId, int nodeId, int linkId, String direction, float spanLoss,
			String wavelengths, int nWaves, int nMpns) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		LinkId = linkId;
		Direction = direction;
		SpanLoss = spanLoss;
		Wavelengths = wavelengths;
		this.nWaves = nWaves;
		this.nMpns = nMpns;
	}
	@Override
	public String toString() {
		return "LinkWavelengthMap [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", LinkId=" + LinkId
				+ ", Direction=" + Direction + ", SpanLoss=" + SpanLoss + ", Wavelengths=" + Wavelengths + ", nWaves="
				+ nWaves + ", nMpns=" + nMpns + "]";
	}
	
}
