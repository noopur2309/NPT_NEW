package application.model;

public class OpticalPowerCdcRoadmDrop {
	private int NetworkId;
	private int NodeId;
	private String Direction;
	private int NoOfLambdas;
	private float PaIn;
	private float PaOut;
	private float RxWssIn;
	private float RxWssOut;
	private float RxWssAttenuation;
	private float DropEdfaIn;
	private float DropEdfaOut;
	private float DropMcsIn;
	private float DropMcsOut;
	private float MpnIn;
	
	public OpticalPowerCdcRoadmDrop() {
		super();
		// TODO Auto-generated constructor stub
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

	public int getNoOfLambdas() {
		return NoOfLambdas;
	}

	public void setNoOfLambdas(int noOfLambdas) {
		NoOfLambdas = noOfLambdas;
	}

	public float getPaIn() {
		return PaIn;
	}

	public void setPaIn(float paIn) {
		PaIn = paIn;
	}

	public float getPaOut() {
		return PaOut;
	}

	public void setPaOut(float paOut) {
		PaOut = paOut;
	}

	public float getRxWssIn() {
		return RxWssIn;
	}

	public void setRxWssIn(float rxWssIn) {
		RxWssIn = rxWssIn;
	}

	public float getRxWssOut() {
		return RxWssOut;
	}

	public void setRxWssOut(float rxWssOut) {
		RxWssOut = rxWssOut;
	}

	public float getRxWssAttenuation() {
		return RxWssAttenuation;
	}

	public void setRxWssAttenuation(float rxWssAttenuation) {
		RxWssAttenuation = rxWssAttenuation;
	}

	public float getDropEdfaIn() {
		return DropEdfaIn;
	}

	public void setDropEdfaIn(float dropEdfaIn) {
		DropEdfaIn = dropEdfaIn;
	}

	public float getDropEdfaOut() {
		return DropEdfaOut;
	}

	public void setDropEdfaOut(float dropEdfaOut) {
		DropEdfaOut = dropEdfaOut;
	}

	public float getDropMcsIn() {
		return DropMcsIn;
	}

	public void setDropMcsIn(float dropMcsIn) {
		DropMcsIn = dropMcsIn;
	}

	public float getDropMcsOut() {
		return DropMcsOut;
	}

	public void setDropMcsOut(float dropMcsOut) {
		DropMcsOut = dropMcsOut;
	}

	public float getMpnIn() {
		return MpnIn;
	}

	public void setMpnIn(float mpnIn) {
		MpnIn = mpnIn;
	}

	public OpticalPowerCdcRoadmDrop(int networkId, int nodeId, String direction, int noOfLambdas, float paIn, float paOut,
			float rxWssIn, float rxWssOut, float rxWssAttenuation, float dropEdfaIn, float dropEdfaOut, float dropMcsIn,
			float dropMcsOut, float mpnIn) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Direction = direction;
		NoOfLambdas = noOfLambdas;
		PaIn = paIn;
		PaOut = paOut;
		RxWssIn = rxWssIn;
		RxWssOut = rxWssOut;
		RxWssAttenuation = rxWssAttenuation;
		DropEdfaIn = dropEdfaIn;
		DropEdfaOut = dropEdfaOut;
		DropMcsIn = dropMcsIn;
		DropMcsOut = dropMcsOut;
		MpnIn = mpnIn;
	}

	@Override
	public String toString() {
		return "OpticalDataAll [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Direction=" + Direction
				+ ", NoOfLambdas=" + NoOfLambdas + ", PaIn=" + PaIn + ", PaOut=" + PaOut + ", RxWssIn=" + RxWssIn
				+ ", RxWssOut=" + RxWssOut + ", RxWssAttenuation=" + RxWssAttenuation + ", DropEdfaIn=" + DropEdfaIn
				+ ", DropEdfaOut=" + DropEdfaOut + ", DropMcsIn=" + DropMcsIn + ", DropMcsOut=" + DropMcsOut
				+ ", MpnIn=" + MpnIn + "]";
	}

}


