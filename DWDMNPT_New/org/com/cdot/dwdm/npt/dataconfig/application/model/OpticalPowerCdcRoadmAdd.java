package application.model;

public class OpticalPowerCdcRoadmAdd {
	private int NetworkId;
	private int NodeId;
	private String Direction;
	private int NoOfLambdas;
	private float BaIn;
	private float BaOut;
	private float TxWssIn;
	private float TxWssOut;
	private float TxWssAttenuation;
	private float AddEdfaIn;
	private float AddEdfaOut;
	private float AddMcsIn;
	private float AddMcsOut;
	private float MpnLaunchPower;
	
	public OpticalPowerCdcRoadmAdd() {
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

	public float getBaIn() {
		return BaIn;
	}

	public void setBaIn(float baIn) {
		BaIn = baIn;
	}

	public float getBaOut() {
		return BaOut;
	}

	public void setBaOut(float baOut) {
		BaOut = baOut;
	}

	public float getTxWssIn() {
		return TxWssIn;
	}

	public void setTxWssIn(float txWssIn) {
		TxWssIn = txWssIn;
	}

	public float getTxWssOut() {
		return TxWssOut;
	}

	public void setTxWssOut(float txWssOut) {
		TxWssOut = txWssOut;
	}

	public float getTxWssAttenuation() {
		return TxWssAttenuation;
	}

	public void setTxWssAttenuation(float txWssAttenuation) {
		TxWssAttenuation = txWssAttenuation;
	}

	public float getAddEdfaIn() {
		return AddEdfaIn;
	}

	public void setAddEdfaIn(float addEdfaIn) {
		AddEdfaIn = addEdfaIn;
	}

	public float getAddEdfaOut() {
		return AddEdfaOut;
	}

	public void setAddEdfaOut(float addEdfaOut) {
		AddEdfaOut = addEdfaOut;
	}

	public float getAddMcsIn() {
		return AddMcsIn;
	}

	public void setAddMcsIn(float addMcsIn) {
		AddMcsIn = addMcsIn;
	}

	public float getAddMcsOut() {
		return AddMcsOut;
	}

	public void setAddMcsOut(float addMcsOut) {
		AddMcsOut = addMcsOut;
	}

	public float getMpnLaunchPower() {
		return MpnLaunchPower;
	}

	public void setMpnLaunchPower(float mpnLaunchPower) {
		MpnLaunchPower = mpnLaunchPower;
	}

	public OpticalPowerCdcRoadmAdd(int networkId, int nodeId, String direction, int noOfLambdas, float baIn,
			float baOut, float txWssIn, float txWssOut, float txWssAttenuation, float addEdfaIn, float addEdfaOut,
			float addMcsIn, float addMcsOut, float mpnLaunchPower) {
		super();
		NetworkId = networkId;
		NodeId = nodeId;
		Direction = direction;
		NoOfLambdas = noOfLambdas;
		BaIn = baIn;
		BaOut = baOut;
		TxWssIn = txWssIn;
		TxWssOut = txWssOut;
		TxWssAttenuation = txWssAttenuation;
		AddEdfaIn = addEdfaIn;
		AddEdfaOut = addEdfaOut;
		AddMcsIn = addMcsIn;
		AddMcsOut = addMcsOut;
		MpnLaunchPower = mpnLaunchPower;
	}

	@Override
	public String toString() {
		return "OpticalPowerCdcRoadmAdd [NetworkId=" + NetworkId + ", NodeId=" + NodeId + ", Direction=" + Direction
				+ ", NoOfLambdas=" + NoOfLambdas + ", BaIn=" + BaIn + ", BaOut=" + BaOut + ", TxWssIn=" + TxWssIn
				+ ", TxWssOut=" + TxWssOut + ", TxWssAttenuation=" + TxWssAttenuation + ", AddEdfaIn=" + AddEdfaIn
				+ ", AddEdfaOut=" + AddEdfaOut + ", AddMcsIn=" + AddMcsIn + ", AddMcsOut=" + AddMcsOut
				+ ", MpnLaunchPower=" + MpnLaunchPower + "]";
	}

	
}


