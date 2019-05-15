package application.model;

public class UpstreamOtnTdmSLsp {
	
    int wavelength;
    int direction;
    int tpn;
	int encodingType;
	int switchingType;
	int gpid;
	int protectionSubType;
	int isRevertive;
	int fecType;
	int fecStatus;
	int txSegment;
	String operatorSpecific;
	int timDectMode;
	int tcmActStatus;
	int tcmActValue;
	int txTcmMode;
	String txTcmPriority;
	int gccType;
	int gccStatus;
	int gccValue;
	
	
	public int getWavelength() {
		return wavelength;
	}


	public void setWavelength(int wavelength) {
		this.wavelength = wavelength;
	}


	public int getDirection() {
		return direction;
	}


	public void setDirection(int direction) {
		this.direction = direction;
	}


	public int getTpn() {
		return tpn;
	}


	public void setTpn(int tpn) {
		this.tpn = tpn;
	}


	public int getEncodingType() {
		return encodingType;
	}


	public void setEncodingType(int encodingType) {
		this.encodingType = encodingType;
	}


	public int getSwitchingType() {
		return switchingType;
	}


	public void setSwitchingType(int switchingType) {
		this.switchingType = switchingType;
	}


	public int getGpid() {
		return gpid;
	}


	public void setGpid(int gpid) {
		this.gpid = gpid;
	}


	public int getProtectionSubType() {
		return protectionSubType;
	}


	public void setProtectionSubType(int protectionSubType) {
		this.protectionSubType = protectionSubType;
	}


	public int getIsRevertive() {
		return isRevertive;
	}


	public void setIsRevertive(int isRevertive) {
		this.isRevertive = isRevertive;
	}


	public int getFecType() {
		return fecType;
	}


	public void setFecType(int fecType) {
		this.fecType = fecType;
	}


	public int getFecStatus() {
		return fecStatus;
	}


	public void setFecStatus(int fecStatus) {
		this.fecStatus = fecStatus;
	}


	public int getTxSegment() {
		return txSegment;
	}


	public void setTxSegment(int txSegment) {
		this.txSegment = txSegment;
	}


	public String getOperatorSpecific() {
		return operatorSpecific;
	}


	public void setOperatorSpecific(String operatorSpecific) {
		this.operatorSpecific = operatorSpecific;
	}


	public int getTimDectMode() {
		return timDectMode;
	}


	public void setTimDectMode(int timDectMode) {
		this.timDectMode = timDectMode;
	}


	public int getTcmActStatus() {
		return tcmActStatus;
	}


	public void setTcmActStatus(int tcmActStatus) {
		this.tcmActStatus = tcmActStatus;
	}


	public int getTcmActValue() {
		return tcmActValue;
	}


	public void setTcmActValue(int tcmActValue) {
		this.tcmActValue = tcmActValue;
	}


	public int getTxTcmMode() {
		return txTcmMode;
	}


	public void setTxTcmMode(int txTcmMode) {
		this.txTcmMode = txTcmMode;
	}


	public String getTxTcmPriority() {
		return txTcmPriority;
	}


	public void setTxTcmPriority(String txTcmPriority) {
		if(this.txTcmPriority==null)
		{  this.txTcmPriority = txTcmPriority;
		}
		else
		{  
			this.txTcmPriority = this.txTcmPriority+"_"+txTcmPriority;
		}
	}


	public int getGccType() {
		return gccType;
	}


	public void setGccType(int gccType) {
		this.gccType = gccType;
	}


	public int getGccStatus() {
		return gccStatus;
	}


	public void setGccStatus(int gccStatus) {
		this.gccStatus = gccStatus;
	}


	public int getGccValue() {
		return gccValue;
	}


	public void setGccValue(int gccValue) {
		this.gccValue = gccValue;
	}


	
	
}
