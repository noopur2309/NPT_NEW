package npt.snmp;
import org.snmp4j.CommunityTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

public class SMFunctions {
	
	public static CommunityTarget fCreateCommunityTarget()
	{
		int snmpVersion  = SnmpConstants.version2c;
		String  community  = SMConstants.communityPublic;
		CommunityTarget comtarget=new CommunityTarget();
	    comtarget.setCommunity(new OctetString(community));
	    comtarget.setVersion(snmpVersion);
	    comtarget.setAddress(new UdpAddress(SMConstants.ipAddress + "/" + SMConstants.port));
	    comtarget.setRetries(2);
	    comtarget.setTimeout(1000);
	    return comtarget;
	}
	
	public static String fGetNetworkTopologyStr()
	{
		String NetrworkTopologyValue  = "3#7#2-2-6-3-0-0-0-0-0#4-1-6-4-0-0-0-0-0#3-1-6-7-0-0-0-0-0#2-2-6-5-0-0-0-0-0#4-4-6-7-0-0-0-0-0#1-1-2-3-4-5-7-0-0#2-3-6-5-0-0-0-0-0";
		
		return NetrworkTopologyValue;
		
	}
	
	public static String fGetLinkMatrixStr()
	{
		String  LinkMatrixValue  = "24#1-2-10-1-2-11#2-1-10-1-2-11#1-3-12-2-3-11#3-1-12-2-3-11#1-6-11-1-4-11#6-1-11-1-4-11#2-6-13-1-5-11#6-2-13-1-5-11#3-6-14-1-6-11#6-3-14-1-6-11#2-4-15-3-7-11#4-2-15-3-7-11#4-6-16-3-8-11#6-4-16-3-8-11#4-5-18-3-9-11#5-4-18-3-9-11#6-5-17-2-10-11#5-6-17-2-10-11"
				  +"#3-7-15-1-3-11#7-3-15-1-3-11#6-7-17-2-3-11#7-6-17-2-3-11#5-7-16-2-1-11#7-5-16-2-1-11";
		
		return LinkMatrixValue;
		
	}
	
	public static String fGetIncludeSetStr()
	{
		String  IncludeSetValue  = "3#1-2-3";
		
		return IncludeSetValue;
		
	}
	
	public static String fGetDemandSetStr()
	{
		String  DemandSetValue  = "3#0#4";
		
		return DemandSetValue;
		
	}

}




	

