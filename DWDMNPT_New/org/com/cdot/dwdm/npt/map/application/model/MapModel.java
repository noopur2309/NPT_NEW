/**
 * 
 */
package application.model;

/**
 * @author hp
 *
 */
public class MapModel {

	
	private String name;
	private  String sname;
	
	private String stationName;
	private String siteName;
	private String gneFlag;
	private String vlanTag;
	private String cells;
	private String[] cellsArray = new String[20];

	
	public String[] getCells() {
		return cellsArray;
	}

	public void setCells(String cells) {
		this.cells = cells;
	}

	public String getVlanTag() {
		return vlanTag;
	}

	public void setVlanTag(String vlanTag) {
		this.vlanTag = vlanTag;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	private String degree;
	

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getGneFlag() {
		return gneFlag;
	}

	public void setGneFlag(String gneFlag) {
		this.gneFlag = gneFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSName() {
		return sname;
	}

	public void setSName(String sname) {
		this.sname = sname;
	}
	
}
