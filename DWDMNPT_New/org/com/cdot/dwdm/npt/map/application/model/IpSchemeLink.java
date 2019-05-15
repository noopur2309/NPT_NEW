package application.model;

public class IpSchemeLink {	
	private int NetworkId;
	private int LinkId;
	private long LinkIp;
	private long SrcIp;
	private long DestIp;
	private long SubnetMask;
	
	public long getSubnetMask() {
		return SubnetMask;
	}
	public void setSubnetMask(long subnetMask) {
		SubnetMask = subnetMask;
	}
	public IpSchemeLink() {
		super();		
	}
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public long getLinkIp() {
		return LinkIp;
	}
	public void setLinkIp(long linkIp) {
		LinkIp = linkIp;
	}
	public long getSrcIp() {
		return SrcIp;
	}
	public void setSrcIp(long srcIp) {
		SrcIp = srcIp;
	}
	public long getDestIp() {
		return DestIp;
	}
	public void setDestIp(long destIp) {
		DestIp = destIp;
	}
	
	
	public int getLinkId() {
		return LinkId;
	}
	public void setLinkId(int linkId) {
		LinkId = linkId;
	}
	
	public IpSchemeLink(int networkId, int linkId, int linkIp, long srcIp, long destIp) {
		super();
		NetworkId = networkId;
		LinkId = linkId;
		LinkIp = linkIp;
		SrcIp = srcIp;
		DestIp = destIp;
	}
	@Override
	public String toString() {
		return "IpSchemeLink [NetworkId=" + NetworkId + ", LinkId=" + LinkId + ", LinkIp=" + LinkIp + ", SrcIp=" + SrcIp
				+ ", DestIp=" + DestIp + "]";
	}
	
	
	
	

}
