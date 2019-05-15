package application.model;

public class LinkWavelength {	
	private int NetworkId;
	private int LinkId;
	private String Wavelengths;
	public LinkWavelength() {
		super();		
	}
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public int getLinkId() {
		return LinkId;
	}
	public void setLinkId(int linkId) {
		LinkId = linkId;
	}
	public String getWavelengths() {
		return Wavelengths;
	}
	public void setWavelengths(String wavelengths) {
		Wavelengths = wavelengths;
	}
	public LinkWavelength(int networkId, int linkId, String wavelengths) {
		super();
		NetworkId = networkId;
		LinkId = linkId;
		Wavelengths = wavelengths;
	}
	@Override
	public String toString() {
		return "LinkWavelength [NetworkId=" + NetworkId + ", LinkId=" + LinkId + ", Wavelengths=" + Wavelengths + "]";
	}

	
	

}
