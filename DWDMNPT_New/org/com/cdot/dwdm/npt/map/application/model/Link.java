package application.model;

public class Link {	
	private int NetworkId;
	private int LinkId;
	private int SrcNode;
	private int DestNode;
	private int Colour;
	private int MetricCost;
	private float Length;
	private float SpanLoss;
	private int Capacity;
	private int FibreType;
	private int SrlgId;	
	private int  NSplices;
	private int LossPerSplice;
	private int NConnector;
	private int LossPerConnector;
	private int CalculatedSpanLoss;
	private float SpanLossCoff;
	private float CdCoff;
	private float Cd;
	private float PmdCoff;
	private float Pmd;
	private String SrcNodeDirection;
	private String DestNodeDirection;
	private String OMSProtection;
	private String LinkType;
	
	public Link() {
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
	public int getSrcNode() {
		return SrcNode;
	}
	public void setSrcNode(int srcNode) {
		SrcNode = srcNode;
	}
	public int getDestNode() {
		return DestNode;
	}
	public void setDestNode(int destNode) {
		DestNode = destNode;
	}
	public int getColour() {
		return Colour;
	}
	public void setColour(int colour) {
		Colour = colour;
	}
	public int getMetricCost() {
		return MetricCost;
	}
	public void setMetricCost(int metricCost) {
		MetricCost = metricCost;
	}
	public float getLength() {
		return Length;
	}
	public void setLength(float f) {
		Length = f;
	}
	public float getSpanLoss() {
		return SpanLoss;
	}
	public void setSpanLoss(float spanLoss) {
		SpanLoss = spanLoss;
	}
	public int getCapacity() {
		return Capacity;
	}
	public void setCapacity(int capacity) {
		Capacity = capacity;
	}
	public int getFibreType() {
		return FibreType;
	}
	public void setFibreType(int fibreType) {
		FibreType = fibreType;
	}
	
	public int getSrlgId() {
		return SrlgId;
	}
	public void setSrlgId(int srlgId) {
		SrlgId = srlgId;
	}
	
	public int getNSplices() {
		return NSplices;
	}
	public void setNSplices(int nSplices) {
		NSplices = nSplices;
	}

	public int getLossPerSplice() {
		return LossPerSplice;
	}
	public void setLossPerSplice(int lossPerSplice) {
		LossPerSplice = lossPerSplice;
	}

	public int getNConnector() {
		return NConnector;
	}
	public void setNConnector(int nConnector) {
		NConnector = nConnector;
	}

	public int getLossPerConnector() {
		return LossPerConnector;
	}
	public void setLossPerConnector(int lossPerConnector) {
		LossPerConnector = lossPerConnector;
	}

	public int getCalculatedSpanLoss() {
		return CalculatedSpanLoss;
	}
	public void setCalculatedSpanLoss(int calculatedSpanLoss) {
		CalculatedSpanLoss = calculatedSpanLoss;
	}

	public float getSpanLossCoff() {
		return SpanLossCoff;
	}
	public void setSpanLossCoff(float spanLossCoff) {
		SpanLossCoff = spanLossCoff;
	}

	public float getCdCoff() {
		return CdCoff;
	}
	public void setCdCoff(float cdCoff) {
		CdCoff = cdCoff;
	}

	public float getCd() {
		return Cd;
	}
	public void setCd(float cd) {
		Cd = cd;
	}

	public float getPmdCoff() {
		return PmdCoff;
	}
	public void setPmdCoff(float pmdCoff) {
		PmdCoff = pmdCoff;
	}

	public float getPmd() {
		return Pmd;
	}
	public void setPmd(float pmd) {
		Pmd = pmd;
	}

	public String getSrcNodeDirection() {
		return SrcNodeDirection;
	}
	public void setSrcNodeDirection(String srcNodeDirection) {
		SrcNodeDirection = srcNodeDirection;
	}


	public String getDestNodeDirection() {
		return DestNodeDirection;
	}
	public void setDestNodeDirection(String destNodeDirection) {
		DestNodeDirection = destNodeDirection;
	}

	public String getOMSProtection() {
		return OMSProtection;
	}	
	public void setOMSProtection(String oMSProtection) {
		OMSProtection = oMSProtection;
	}
	
	public String getLinkType() {
		return LinkType;
	}	
	public void setLinkType(String linkType) {
		LinkType = linkType;
	}

	public Link(int networkId, int linkId, int srcNode, int destNode, int colour, int metricCost, int length,
			float spanLoss, int capacity, int fibreType, int srlgId, int nSplices, int lossPerSplice, int nConnector,
			int lossPerConnector, int calculatedSpanLoss, float spanLossCoff, float cdCoff, float cd, float pmdCoff,
			float pmd, String srcNodeDirection, String destNodeDirection, String oMSProtection,String linkType) {
		super();
		NetworkId = networkId;
		LinkId = linkId;
		SrcNode = srcNode;
		DestNode = destNode;
		Colour = colour;
		MetricCost = metricCost;
		Length = length;
		SpanLoss = spanLoss;
		Capacity = capacity;
		FibreType = fibreType;
		SrlgId = srlgId;
		NSplices = nSplices;
		LossPerSplice = lossPerSplice;
		NConnector = nConnector;
		LossPerConnector = lossPerConnector;
		CalculatedSpanLoss = calculatedSpanLoss;
		SpanLossCoff = spanLossCoff;
		CdCoff = cdCoff;
		Cd = cd;
		PmdCoff = pmdCoff;
		Pmd = pmd;
		SrcNodeDirection = srcNodeDirection;
		DestNodeDirection = destNodeDirection;
		OMSProtection = oMSProtection;
		LinkType= linkType;
	}

	@Override
	public String toString() {
		return "Link [NetworkId=" + NetworkId + ", LinkId=" + LinkId + ", SrcNode=" + SrcNode + ", DestNode=" + DestNode
				+ ", Colour=" + Colour + ", MetricCost=" + MetricCost + ", Length=" + Length + ", SpanLoss=" + SpanLoss
				+ ", Capacity=" + Capacity + ", FibreType=" + FibreType + ", SrlgId=" + SrlgId + ", NSplices="
				+ NSplices + ", LossPerSplice=" + LossPerSplice + ", NConnector=" + NConnector + ", LossPerConnector="
				+ LossPerConnector + ", CalculatedSpanLoss=" + CalculatedSpanLoss + ", SpanLossCoff=" + SpanLossCoff
				+ ", CdCoff=" + CdCoff + ", Cd=" + Cd + ", PmdCoff=" + PmdCoff + ", Pmd=" + Pmd + ", SrcNodeDirection="
				+ SrcNodeDirection + ", DestNodeDirection=" + DestNodeDirection + ", OMSProtection=" + OMSProtection
				+ ", LinkType=" + LinkType+"]";
	}

}
