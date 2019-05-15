package application.model;

public class Topology {	
	private int NetworkId;
	private int NodeId;
	private int Dir1;
	private int Dir2;
	private int Dir3;
	private int Dir4;
	private int Dir5;
	private int Dir6;
	private int Dir7;
	private int Dir8;
	public Topology() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Topology(int networkId, int nodeId, int dir1, int dir2, int dir3, int dir4, int dir5, int dir6, int dir7,
			int dir8) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Dir1 = dir1;
		Dir2 = dir2;
		Dir3 = dir3;
		Dir4 = dir4;
		Dir5 = dir5;
		Dir6 = dir6;
		Dir7 = dir7;
		Dir8 = dir8;
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
	public int getDir1() {
		return Dir1;
	}
	public void setDir1(int dir1) {
		Dir1 = dir1;
	}
	public int getDir2() {
		return Dir2;
	}
	public void setDir2(int dir2) {
		Dir2 = dir2;
	}
	public int getDir3() {
		return Dir3;
	}
	public void setDir3(int dir3) {
		Dir3 = dir3;
	}
	public int getDir4() {
		return Dir4;
	}
	public void setDir4(int dir4) {
		Dir4 = dir4;
	}
	public int getDir5() {
		return Dir5;
	}
	public void setDir5(int dir5) {
		Dir5 = dir5;
	}
	public int getDir6() {
		return Dir6;
	}
	public void setDir6(int dir6) {
		Dir6 = dir6;
	}
	public int getDir7() {
		return Dir7;
	}
	public void setDir7(int dir7) {
		Dir7 = dir7;
	}
	public int getDir8() {
		return Dir8;
	}
	public void setDir8(int dir8) {
		Dir8 = dir8;
	}
	@Override
	public String toString() {
		return "Topology [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Dir1=" + Dir1 + ", Dir2=" + Dir2
				+ ", Dir3=" + Dir3 + ", Dir4=" + Dir4 + ", Dir5=" + Dir5 + ", Dir6=" + Dir6 + ", Dir7=" + Dir7
				+ ", Dir8=" + Dir8 + "]";
	}	
	
	

}
