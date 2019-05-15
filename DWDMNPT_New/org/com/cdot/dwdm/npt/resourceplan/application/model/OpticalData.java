package application.model;

public class OpticalData {	
	private int EquipmentId;
	private float PIn;
	private float PInMax;
	private float PInMin;
	private float POut;
	private float POutMax;
	private float POutMin;
	private float Loss;
	private float Gain;
	
	public OpticalData() {
		super();		
	}

	public int getEquipmentId() {
		return EquipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		EquipmentId = equipmentId;
	}

	public float getPIn() {
		return PIn;
	}

	public void setPIn(float pIn) {
		PIn = pIn;
	}

	public float getPInMax() {
		return PInMax;
	}

	public void setPInMax(float pInMax) {
		PInMax = pInMax;
	}

	public float getPInMin() {
		return PInMin;
	}

	public void setPInMin(float pInMin) {
		PInMin = pInMin;
	}

	public float getPOut() {
		return POut;
	}

	public void setPOut(float pOut) {
		POut = pOut;
	}

	public float getPOutMax() {
		return POutMax;
	}

	public void setPOutMax(float pOutMax) {
		POutMax = pOutMax;
	}

	public float getPOutMin() {
		return POutMin;
	}

	public void setPOutMin(float pOutMin) {
		POutMin = pOutMin;
	}

	public float getLoss() {
		return Loss;
	}

	public void setLoss(float loss) {
		Loss = loss;
	}

	public float getGain() {
		return Gain;
	}

	public void setGain(float gain) {
		Gain = gain;
	}

	public OpticalData(int equipmentId, float pIn, float pInMax, float pInMin, float pOut, float pOutMax, float pOutMin,
			float loss, float gain) {
		super();
		EquipmentId = equipmentId;
		PIn = pIn;
		PInMax = pInMax;
		PInMin = pInMin;
		POut = pOut;
		POutMax = pOutMax;
		POutMin = pOutMin;
		Loss = loss;
		Gain = gain;
	}

	@Override
	public String toString() {
		return "OpticalData [EquipmentId=" + EquipmentId + ", PIn=" + PIn + ", PInMax=" + PInMax + ", PInMin=" + PInMin
				+ ", POut=" + POut + ", POutMax=" + POutMax + ", POutMin=" + POutMin + ", Loss=" + Loss + ", Gain="
				+ Gain + "]";
	}

	

	

	
	
	

}
