package npt.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import npt.snmp.SMConstants;
import npt.snmp.SMFunctions;

public class SnmpManager {
	  public static void main(String[] args) throws Exception
	  {

	    System.out.println("SNMP SET");

	    // Create TransportMapping and Listen
	    TransportMapping transport = new DefaultUdpTransportMapping();
	    transport.listen();

	    // Create Target Address object
	    CommunityTarget comtarget = SMFunctions.fCreateCommunityTarget(); 
	    
	    // Create the PDU object
	    PDU pdu = new PDU();
	    Snmp snmp=null;
	    OID oid=null;
	    Variable var=null;
	    VariableBinding varBind=null;
	    int i=4;
	    while(i>0)
	    {
	    // Setting the Oid and Value 
	    switch(i)
	    {
	    case 4:
	    	 oid = new OID(SMConstants.NetrworkTopologyOid);
	         var = new OctetString(SMFunctions.fGetNetworkTopologyStr());
	         varBind = new VariableBinding(oid,var);
	        break;
	    case 3:
	    	 oid = new OID(SMConstants.LinkMatrixOid);
	         var = new OctetString(SMFunctions.fGetLinkMatrixStr());
	         varBind = new VariableBinding(oid,var);
	        break;
	    case 2:
	    	 oid = new OID(SMConstants.IncludeSetOid);
	         var = new OctetString(SMFunctions.fGetIncludeSetStr());
	         varBind = new VariableBinding(oid,var);
	        break;
	    case 1:
	    	 oid = new OID(SMConstants.DemandSetOid);
	         var = new OctetString(SMFunctions.fGetDemandSetStr());
	         varBind = new VariableBinding(oid,var);
	        break;
	    }
	    
	    pdu.add(varBind);
	    
	    pdu.setType(PDU.SET);
	    pdu.setRequestID(new Integer32(1));

	    // Create Snmp object for sending data to Agent
	    snmp = new Snmp(transport);

	    //System.out.println("\nRequest:\n[ Note: Set Request is sent for sysContact oid in RFC 1213 MIB.");
	    System.out.println("Set operation will change the "+var +" value to " + oid );
	    System.out.println("Once this operation is completed, Querying for sysContact will get the value = " + var + " ]");
	    
	    System.out.println("Request:\nSending Snmp Set Request to Agent...");
	    
	    //Response Only on DemandSet
	    if(i>1)
	    {
	    	//Set Request
	    	snmp.set(pdu, comtarget);
	    }
	    else
	    {
	    ResponseEvent response = snmp.set(pdu, comtarget);
	    
	    // Process Agent Response
	    if (response != null)
	    {
	      System.out.println("\nResponse:\nGot Snmp Set Response from Agent");
	      PDU responsePDU = response.getResponse();

	      if (responsePDU != null)
	      {
	        int errorStatus = responsePDU.getErrorStatus();
	        int errorIndex = responsePDU.getErrorIndex();
	        String errorStatusText = responsePDU.getErrorStatusText();

	        if (errorStatus == PDU.noError)
	        {
	          System.out.println("Snmp Set Response = " + responsePDU.getVariableBindings());
	        }
	        else
	        {
	          System.out.println("Error: Request Failed");
	          System.out.println("Error Status = " + errorStatus);
	          System.out.println("Error Index = " + errorIndex);
	          System.out.println("Error Status Text = " + errorStatusText);
	        }
	      }
	      else
	      {
	        System.out.println("Error: Response PDU is null");
	      }
	    }
	    else
	    {
	      System.out.println("Error: Agent Timeout... ");
	    }
	    }
	    i--;
	   }
	    snmp.close();
	  }

}
