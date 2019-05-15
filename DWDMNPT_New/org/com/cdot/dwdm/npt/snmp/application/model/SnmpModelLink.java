package application.model;

public class SnmpModelLink{
	
	private int Src;
	private int Target;
	private int Cost;
	private int Srlg;
	private int Color;
	private float SpanLength;	
	private float SpanLoss;
	private int Capacity;
	private int FiberType;
	private String LinkType;
	private int LinkId;
	
	public String getLinkType() {
		return LinkType;
	}
	public void setLinkType(String string) {
		LinkType = string;
	}	
	public int getCapacity() {
		return Capacity;
	}
	public void setCapacity(int capacity) {
		Capacity = capacity;
	}
	public int getFiberType() {
		return FiberType;
	}
	public void setFiberType(int fiberType) {
		FiberType = fiberType;
	}
	public float getSpanLoss() {
		return SpanLoss;
	}
	public void setSpanLoss(float spanLoss) {
		SpanLoss = spanLoss;
	}
	public int getSrc() {
		return Src;
	}
	public void setSrc(int src) {
		Src = src;
	}
	public int getTarget() {
		return Target;
	}
	public void setTarget(int target) {
		Target = target;
	}
	public int getCost() {
		return Cost;
	}
	public void setCost(int cost) {
		Cost = cost;
	}
	public int getSrlg() {
		return Srlg;
	}
	public void setSrlg(int srlg) {
		Srlg = srlg;
	}
	public int getColor() {
		return Color;
	}
	public void setColor(int color) {
		Color = color;
	}
	public float getSpanLength() {
		return SpanLength;
	}
	public void setSpanLength(float f) {
		SpanLength = f;
	}
	public int getLinkId() {
		return LinkId;
	}
	public void setLinkId(int linkId) {
		LinkId = linkId;
	}
	
}
