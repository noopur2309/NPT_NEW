package application.service;

import java.io.IOException;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import application.SMFunctions;
import application.constants.SMConstants;
import application.model.SnmpModelNetworkRoute;


public class SnmpTrapReceiver implements CommandResponder {
	static int isRunning = 0;
	
	 public void TrapReceiver( SnmpTrapReceiver obj) {		
		System.out.println(isRunning);
		System.out.println("Why are you not running?");
		if(isRunning == 0)
		{
			//SnmpTrapReceiver snmp4jTrapReceiver = new SnmpTrapReceiver();
			isRunning = 1;
			try {
				obj.listen(new UdpAddress(SMConstants.sourceIpAddress+"/1026"));///"192.168.115.193/1025"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Already listening");
		}
		
	}

	/**
	 * Trap Listner
	 */
	public synchronized void listen(TransportIpAddress address)
			throws IOException {
		AbstractTransportMapping transport;
		if (address instanceof TcpAddress) {
			transport = new DefaultTcpTransportMapping((TcpAddress) address);
		} else {
			transport = new DefaultUdpTransportMapping((UdpAddress) address);
		}

		ThreadPool threadPool = ThreadPool.create("DispatcherPool", 10);
		MessageDispatcher mDispathcher = new MultiThreadedMessageDispatcher(
				threadPool, new MessageDispatcherImpl());

		// add message processing models
		mDispathcher.addMessageProcessingModel(new MPv1());
		mDispathcher.addMessageProcessingModel(new MPv2c());

		// add all security protocols
		SecurityProtocols.getInstance().addDefaultProtocols();
		SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

		// Create Target
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString("public"));

		Snmp snmp = new Snmp(mDispathcher, transport);
		snmp.addCommandResponder(this);

		transport.listen();
		System.out.println("Listening on " + address);

		try {
			this.wait();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * This method will be called whenever a pdu is received on the given port
	 * specified in the listen() method
	 */
	  public synchronized void processPdu(CommandResponderEvent cmdRespEvent)
	  {		  
		    PDU pdu = cmdRespEvent.getPDU();
		    
		    if (pdu != null) {
			VariableBinding v = pdu.get(3);
			Variable vars = v.getVariable();
			SMFunctions smfObj=new SMFunctions(new DbService()); 
			System.out.println("Received PDU... " + pdu);
			SnmpModelNetworkRoute snmpModelNetworkRoute=smfObj.fSaveDemandResponse(vars.toString());
		    
			
		   
		        /*System.out.println(pdu.getClass().getName());
		        System.out.println("trapType = " + pdu.getType());
		        System.out.println("isPDUv1 = " + (pdu instanceof PDUv1));
		        System.out.println("isTrap = " + (pdu.getType() == PDU.TRAP));
		        System.out.println("isInform = " + (pdu.getType() == PDU.INFORM));
		        System.out.println("variableBindings = " + pdu.getVariableBindings().toString().trim());
		       // System.out.println("variableBindingstoArray = " + pdu.getVariableBindings().toArray());
		        StringBuilder b=new StringBuilder();  
		        int vCount = pdu.size();
		    	if(vCount>0) {
		    		b.append("\n\tBindings [" + vCount + "]:");
		    		for(int i = 0; i < vCount; i++) {
		    			VariableBinding vb = pdu.get(i);
		    			b.append("\n\t\t#").append(i).append(":");
		    			b.append("\n\t\t\tOID:").append(vb.getOid().toString());
		    			Variable var = vb.getVariable();
		    			b.append("\n\t\t\tType:").append(var.getClass().getSimpleName());
		    			b.append("\n\t\t\tVaue:").append(var.toString());
		    		}
		    	} else {
		    		b.append("\n\tNo Bindings");
		    	}
		    	 System.out.println("b = " + b);
		       // trapCount++;
*/		    } else {
		        System.err.println("ERROR: Can't create PDU");
		    }
	}

	
}