package application;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;

import application.constants.MapConstants;
import application.constants.SMConstants;
import application.controller.ResourcePlanUtils;
import application.model.Demand;
import application.model.Link;
import application.model.SnmpModel;
import application.model.SnmpModelDemand;
import application.model.SnmpModelNetworkRoute;
import application.service.DbService;
import application.service.SnmpTrapReceiver;


public class SMFunctions {
	
	DbService dbService;
	public static Logger logger = Logger.getLogger(ResourcePlanUtils.class.getName());

	public DbService getDbService() {
		return dbService;
	}

	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public SMFunctions(DbService dbService) {
		super();
		this.dbService = dbService;
	} 

	public  CommunityTarget fCreateCommunityTarget()
	{
		int snmpVersion  = SnmpConstants.version2c;
		String  community  = SMConstants.communityPublic;
		CommunityTarget comtarget=new CommunityTarget();
		comtarget.setCommunity(new OctetString(community));
		comtarget.setVersion(snmpVersion);
		comtarget.setAddress(new UdpAddress(SMConstants.destIpAddress + "/" + SMConstants.port));
		comtarget.setRetries(2);
		comtarget.setTimeout(1000);
		return comtarget;
	}

	public  String fGetNetworkTopologyStr(SnmpModel snmpModelObj)
	{
		System.out.println("Network topology String :"+"NetworkId:-"+snmpModelObj.getNetworkId()+"No of Demands:-"+snmpModelObj.getNumberofDemands()+"Node Count:-"+snmpModelObj.getNumberOfNodes()
		+"Link Count:-"+snmpModelObj.getNumberofLinks()+"NetworkId:-");
		StringBuilder str=new StringBuilder();  
		str.append(snmpModelObj.getNetworkId());
		str.append("#");
		str.append(snmpModelObj.getNumberOfNodes());
		str.append("#");
		int linkIdDirection=0;
		
		for(int i=0;i< snmpModelObj.getNumberOfNodes();i++)
		{
			str.append(snmpModelObj.snmpModelNode[i].getNodeId());
			str.append("|");			

			str.append(snmpModelObj.snmpModelNode[i].getNodeType());
			str.append("-");
			
			str.append(snmpModelObj.snmpModelNode[i].getNodeCapacity());
			str.append("-");

			str.append(snmpModelObj.snmpModelNode[i].isEast());
			linkIdDirection=dbService.getLinkService().FindLinkInDirection(snmpModelObj.getNetworkId(),
					snmpModelObj.snmpModelNode[i].getNodeId(),
					MapConstants.EAST);
			str.append("_"+linkIdDirection);
			str.append("-");

			str.append(snmpModelObj.snmpModelNode[i].isWest());
			linkIdDirection=dbService.getLinkService().FindLinkInDirection(snmpModelObj.getNetworkId(),
					snmpModelObj.snmpModelNode[i].getNodeId(),
					MapConstants.WEST);
			str.append("_"+linkIdDirection);
			str.append("-");

			str.append(snmpModelObj.snmpModelNode[i].isNorth());
			linkIdDirection=dbService.getLinkService().FindLinkInDirection(snmpModelObj.getNetworkId(),
					snmpModelObj.snmpModelNode[i].getNodeId(),
					MapConstants.NORTH);
			str.append("_"+linkIdDirection);
			str.append("-");

			str.append(snmpModelObj.snmpModelNode[i].isSouth());
			linkIdDirection=dbService.getLinkService().FindLinkInDirection(snmpModelObj.getNetworkId(),
					snmpModelObj.snmpModelNode[i].getNodeId(),
					MapConstants.SOUTH);
			str.append("_"+linkIdDirection);
			str.append("-");

			str.append(snmpModelObj.snmpModelNode[i].isNortheast());
			linkIdDirection=dbService.getLinkService().FindLinkInDirection(snmpModelObj.getNetworkId(),
					snmpModelObj.snmpModelNode[i].getNodeId(),
					MapConstants.NE);
			str.append("_"+linkIdDirection);
			str.append("-");

			str.append(snmpModelObj.snmpModelNode[i].isNorthwest());
			linkIdDirection=dbService.getLinkService().FindLinkInDirection(snmpModelObj.getNetworkId(),
					snmpModelObj.snmpModelNode[i].getNodeId(),
					MapConstants.NW);
			str.append("_"+linkIdDirection);
			str.append("-");

			str.append(snmpModelObj.snmpModelNode[i].isSoutheast());
			linkIdDirection=dbService.getLinkService().FindLinkInDirection(snmpModelObj.getNetworkId(),
					snmpModelObj.snmpModelNode[i].getNodeId(),
					MapConstants.SE);
			str.append("_"+linkIdDirection);
			str.append("-");

			str.append(snmpModelObj.snmpModelNode[i].isSouthwest());
			linkIdDirection=dbService.getLinkService().FindLinkInDirection(snmpModelObj.getNetworkId(),
					snmpModelObj.snmpModelNode[i].getNodeId(),
					MapConstants.SW);
			str.append("_"+linkIdDirection);

			if((i+1)!=snmpModelObj.getNumberOfNodes())
				str.append("#");

		}

		//String NetrworkTopologyValue  = "1#11#1|3-2-1-11-6-0-0-0-0-0#2|3-3-1-1-6-0-0-0-0-0#3|3-4-1-2-5-0-0-0-0-0#4|3-9-1-3-5-11-0-0-0-0#5|3-8-1-6-7-3-0-4-0-0#6|3-7-1-1-5-2-0-0-0-0#7|3-10-1-6-5-0-0-0-0-0#8|4-9-1-5-0-0-0-0-0-0#9|3-8-1-4-10-0-0-0-0-0#10|2-7-1-9-0-0-0-0-0-0#11|4-1-1-4-0-0-0-0-0-0";
		System.out.println("****&&&%%$#@% Network Topology : "+str);
		//System.out.println("Network Topology :"+NetrworkTopologyValue);
		//return NetrworkTopologyValue;
		return str.toString();

	}

	public String fGetLinkMatrixStr(SnmpModel snmpModelObj)
	{
		StringBuilder str=new StringBuilder();  
		str.append(snmpModelObj.getNumberofLinks());
		str.append("#");

		for(int i=0;i< snmpModelObj.getNumberofLinks();i++)
		{
			str.append(snmpModelObj.snmpModelLink[i].getSrc());
			str.append("-");

			str.append(snmpModelObj.snmpModelLink[i].getTarget());
			str.append("-");
			
			str.append(snmpModelObj.snmpModelLink[i].getLinkId());
			str.append("-");

			str.append(snmpModelObj.snmpModelLink[i].getCost());
			str.append("-");

			str.append(snmpModelObj.snmpModelLink[i].getColor());
			str.append("-");

			str.append(snmpModelObj.snmpModelLink[i].getSrlg());
			str.append("-");

			str.append(snmpModelObj.snmpModelLink[i].getSpanLoss());
			str.append("-");
			
			str.append((int)snmpModelObj.snmpModelLink[i].getSpanLength());
			str.append("-");
			
			str.append(fGetLinkTypeValue(snmpModelObj.snmpModelLink[i].getLinkType()));

			if((i+1)!=snmpModelObj.getNumberofLinks())
				str.append("#");

		}
		System.out.println("Link Matrix :"+str);
		//String  LinkMatrixValue  = "17#1-2-40-1-17-0#1-6-60-2-1-0#1-11-40-1-2-0#2-6-70-2-3-0#2-3-90-1-4-0#3-5-45-3-5-0#3-4-100-1-6-0#4-5-31-3-7-0#4-9-49-2-8-0#4-11-80-1-9-0#5-6-40-2-10-0#5-7-30-3-11-0#5-8-56-3-12-0#6-7-60-1-13-0#7-10-90-1-14-0#8-9-44-3-15-0#9-10-100-3-16-0";
		//System.out.println("Network Topology :"+LinkMatrixValue);
		//return LinkMatrixValue;
		return str.toString();

	}
	
	public int fGetColorCode(String color)
	{
		switch (color) {
		case "Violet":
			return SMConstants.Violet;
		case "Indigo":
			return SMConstants.Indigo;
		case "Blue":
			return SMConstants.Blue;
		case "Green":
			return SMConstants.Green;
		case "Yellow":
			return SMConstants.Yellow;
		case "Orange":
			return SMConstants.Orange;
		case "Red":
			return SMConstants.Red;
		default:
			return 0;
		}
	}
	
	public int fGetLinkTypeValue(String type)
	{
		System.out.println("***** fGetLinkTypeValue() ***** Link Type Received :: "+type);
		switch (type) {
		case SMConstants.defaultLinkStr:
			return SMConstants.defaultLink;
		case SMConstants.hybridRamanLinkStr:
			return SMConstants.hybridRamanLink;
		case SMConstants.draRamanLinkStr:
			return SMConstants.draRamanLink;
		default:
			return 0;
		}
	}

	
	public  String fGetIncludeNodeSetStr(SnmpModel snmpModelObj)
	{

		
		StringBuilder str=new StringBuilder();  
        int numberofNodes;
		for(int i=0;i< snmpModelObj.getNumberofDemands();i++)
		{
			numberofNodes=0;
			str.append(snmpModelObj.snmpModelDemand[i].getDemandId());
			str.append("#");	
			String []nodesArr=snmpModelObj.snmpModelDemand[i].getNodeInclusion().split("#");
			
			if(nodesArr[0].equals("Empty"))
			{
				System.out.println("nodesArr[0].equals(empty) :: "+nodesArr[0]);
				str.append(numberofNodes);	
			}
			else
			{
				numberofNodes=nodesArr.length;
				str.append(numberofNodes);
				str.append("#");
				// any :1 , all:2 Default value is any :: Debug
				str.append(1);
				str.append("#");
				
				for(int c=0;c<numberofNodes;c++)
				{
					
					str.append(nodesArr[c]);
					
					if((c+1)!=numberofNodes)
					str.append("-");
				}
				//str.append(snmpModelObj.snmpModelDemand[i].getColorPreference());			
				
			}			

			if((i+1)!=snmpModelObj.getNumberofDemands())
				str.append("|");
		}

		String  NodeIncludeSetValue  = "0";
		System.out.println("Node Include Set :"+str);
		//System.out.println("Network Topology :"+IncludeSetValue);
		//return IncludeSetValue;
		return str.toString();
        //return IncludeSetValue;
	}
	
	public  String fGetIncludeSetStr(SnmpModel snmpModelObj)
	{

		
		StringBuilder str=new StringBuilder();  
        int numberofColors;
		for(int i=0;i< snmpModelObj.getNumberofDemands();i++)
		{
			numberofColors=0;
			str.append(snmpModelObj.snmpModelDemand[i].getDemandId());
			str.append("#");	
			String []colorsArr=snmpModelObj.snmpModelDemand[i].getColorPreference().split("#");
			
			if(colorsArr[0].equals("Empty"))
			{
				System.out.println("colorsArr[0].equals(empty) :: "+colorsArr[0]);
				str.append(numberofColors);	
			}
			else
			{
				numberofColors=colorsArr.length;
				str.append(numberofColors);
				str.append("#");
				
				for(int c=0;c<numberofColors;c++)
				{
					
					str.append(fGetColorCode(colorsArr[c]));
					
					if((c+1)!=numberofColors)
					str.append("-");
				}
				//str.append(snmpModelObj.snmpModelDemand[i].getColorPreference());			
				
			}			

			if((i+1)!=snmpModelObj.getNumberofDemands())
				str.append("|");
		}

		String  IncludeSetValue  = "0";
		System.out.println("Include Set :"+str);
		//System.out.println("Network Topology :"+IncludeSetValue);
		//return IncludeSetValue;
		return str.toString();
        //return IncludeSetValue;
	}



	public  String fGetExcludeSetStr(SnmpModel snmpModelObj)
	{

		StringBuilder str=new StringBuilder();  
        int numberofColors;
        int totalNumOfolors=7;
        Set<Integer> includeSet=new HashSet();
        Set<Integer> excludeSet=new HashSet();
        for(int i=1;i<=totalNumOfolors;i++)
        	excludeSet.add(i);
        
		for(int i=0;i< snmpModelObj.getNumberofDemands();i++)
		{
			for(int k=1;k<=totalNumOfolors;k++)
	        	excludeSet.add(k);
			includeSet.clear();
			
			numberofColors=0;
			str.append(snmpModelObj.snmpModelDemand[i].getDemandId());
			str.append("#");	
			String []colorsArr=snmpModelObj.snmpModelDemand[i].getColorPreference().split("#");
			
			if(colorsArr[0].equals("Empty") | colorsArr.length==0)
			{
				str.append(numberofColors);	
			}
			else
			{
				numberofColors=colorsArr.length;
				
				for(int c=0;c<colorsArr.length;c++)	
					includeSet.add(fGetColorCode(colorsArr[c]));				
				excludeSet.removeAll(includeSet);
				
				str.append(totalNumOfolors-numberofColors);
				str.append("#");
				excludeSet.forEach(c->{
					str.append(c);    	
					str.append("-");
				});
				
				//delete last ' - '
				str.deleteCharAt(str.length()-1);
				
								
//				numberofColors=colorsArr.length;
//				str.append(totalNumOfolors-numberofColors);
//				str.append("#");
//				int []includeColorsArr=new int[totalNumOfolors];
//				for(int c=0;c<numberofColors;c++)			
//					includeColorsArr[fGetColorCode(colorsArr[c])-1]=1;
//				
//				for(int j=0;j<includeColorsArr.length;j++)
//	              {
//	                if(includeColorsArr[j]==0)
//	                {
//	                	str.append(j+1);    				
//	    				if((j+1)!=includeColorsArr.length)
//	    				str.append("-");
//	                }
//					
//	              }					
				//str.append(snmpModelObj.snmpModelDemand[i].getColorPreference());
				
			}
			

			if((i+1)!=snmpModelObj.getNumberofDemands())
				str.append("|");
		}
		
		String  ExcludeSetValue  = "0";
		System.out.println("Exclude Set :"+str);
		//System.out.println("Network Topology :"+ExcludeSetValue);
		//return ExcludeSetValue;
		return str.toString();
		//return ExcludeSetValue;

	}

	
	private int fGetProtectiontype(String protectionType) {
	// TODO Auto-generated method stub
	
	switch (protectionType) {
	case SMConstants.PtcTypeNoneStr: 
		return SMConstants.PtcTypeNone;
	case SMConstants.OneIsToOnePtcTypeStr:
		return SMConstants.OneIsToOnePtcType;
	case SMConstants.OnePlusOnePlusRPtcStr:
		return SMConstants.OnePlusOnePlusRPtcType;
	case SMConstants.OnePlusOnePlusTwoRPtcTypeStr:
		return SMConstants.OnePlusOnePlusTwoRPtcType;
	case SMConstants.OneIsToTwoRPtcTypeStr:
		return SMConstants.OneIsToTwoRPtcType;
	case SMConstants.OnePlusOnePtcStr:
		return SMConstants.OnePlusOnePtcType;
	default:
		return 1;
	}
	
    }
	
	private int fGetPathType(String pathType) {
		// TODO Auto-generated method stub
		
		switch (pathType) {
		case "Disjoint": 
			return 0;
		case "Non Disjoint": 
			return 1;
		default:
			return 0;
		}
		
	    }
	
	private int fGetProtectionMechanism(SnmpModelDemand snmpModelDemand) {
		// TODO Auto-generated method stub
		
		System.out.println("fGetProtectionMechanism() -- snmpModelDemand ::"+snmpModelDemand.getClientProtection()+" "+snmpModelDemand.getChannelProtection());
		if(snmpModelDemand.getClientProtection().equals(SMConstants.YesStr))
			return SMConstants.ClientProtection;
		else if(snmpModelDemand.getChannelProtection().equals(SMConstants.YesStr))
			return SMConstants.ChannelProtection;
		else return 0;
		
	    }
	
	
	/** 
	 * @param snmpModelObj
	 * @brief To generate Old Demand Set for RWA as per the following format:
	 * DESCRIPTION
     * [SET:DemandId1!Path#WavelengthNumber#WaveLengthType#Traffic#Osnr#3RLocHeadToTail#3RLocTailToHead#ProtectionType$........$UP_TO_NUMBER_OF_EXISTING_PATHS_FOR_DEMAND_1|
     *      DemandId2!Path#WavelengthNumber#TotalCapacity#Traffic#Osnr#3RLocHeadToTail#3RLocTailToHead#ProtectionType$........$UP_TO_NUMBER_OF_EXISTING_PATHS_FOR_DEMAND_2
	 *		{Path:SourceNode-....-DestinationNode}
	 *		{NOTE:WaveLengthType:10 for 10G,100 for 100G 200 for 200G .....n for nG}   
	 *      
	 *      Old Demand Set : Sample				   			      
   			      
      	      1!1-2#40#100#100.0#0#0#0#3!
      	        1-3-2#40#100#100.0#0#0#0#3|
      	      2!1-3#39#100#50.0#0#0#0#3!
      	          1-2-3#39#100#50.0#0#0#0#3|
      	      3!2-3#41#100#20.0#0#0#0#3!
      	        2-1-3#41#100#20.0#0#0#0#3
				
	 * @return
	 */
	public  String fGetOldDemandSetStr(SnmpModel snmpModelObj)
	{		
		StringBuilder str=new StringBuilder();
		int oldDemandId=0;		
		
		System.out.println("fGetOldDemandSetStr : snmpModelObj.getNumberofOldDemands() "+ snmpModelObj.getNumberofOldDemands());
        
		for(int i=0;i< snmpModelObj.getNumberofOldDemands();i++)
		{
			int numberOfPath=0;
			if(oldDemandId != snmpModelObj.snmpModelOldDemand[i].getDemandId()) {/**Demand is changed*/
				
				if(/*(i+1)!=snmpModelObj.getNumberofDemands() ||*/ (i!=0)) {
					///System.out.println("True condition for | append ");
					str.append("|");
				}
				
				oldDemandId= snmpModelObj.snmpModelOldDemand[i].getDemandId();				
				str.append(snmpModelObj.snmpModelOldDemand[i].getDemandId());		
				str.append("!");
				
				///System.out.println("oldDemandId "+oldDemandId);
			}
			else {
				str.append("!");
			}
			
				
			String [] pathNodeArr=snmpModelObj.snmpModelOldDemand[i].getPath().split(",");
			System.out.println(" pathNodeArr : "+ snmpModelObj.snmpModelOldDemand[i].getPath());
				
			if(pathNodeArr[0].equals("Empty"))
			{
				System.out.println("pathNodeArr[0].equals(empty) :: "+pathNodeArr[0]);					
			}
			else
			{
				numberOfPath=pathNodeArr.length;
				
				for(int c=0;c<numberOfPath;c++)
				{
					
					if(c != numberOfPath-1)
						str.append(pathNodeArr[c]+"-");
					else
						str.append(pathNodeArr[c]);
					
				}
				str.append("#");
				System.out.println("Link Set:: "+snmpModelObj.snmpModelOldDemand[i].getLinkIdSet());
				String [] linkIdSetArr=snmpModelObj.snmpModelOldDemand[i].getLinkIdSet().split(",");
				System.out.println("linkIdSetArr:: "+linkIdSetArr.length);
				for(int k=0;k<linkIdSetArr.length;k++)
				{					
					if(k != linkIdSetArr.length-1)
						str.append(linkIdSetArr[k]+"-");
					else
						str.append(linkIdSetArr[k]);					
				}	
				
				str.append("#");
				
				/**Add Attributes*/
				str.append(snmpModelObj.snmpModelOldDemand[i].getWavelengthNo());
				str.append("#");
				str.append(snmpModelObj.snmpModelOldDemand[i].getLineRate());
				str.append("#");
				str.append(snmpModelObj.snmpModelOldDemand[i].getTraffic());
				str.append("#");				
				
				/**DBG => Filled with Zero as per the RWA input*/
				str.append("0"); /**OSNR*/ 						
				str.append("#");
				str.append("0"); /**3RLocHeadToTail*/
				str.append("#");
				str.append("0"); /**3RLocTailToHead*/
				str.append("#");
				str.append(fGetProtectiontype(snmpModelObj.snmpModelOldDemand[i].getProtectionType()));
				
				
				
			}
				
			/*if((i+1)!=snmpModelObj.getNumberofDemands() && (flag==true) && (i!=0)) {
				str.append("|");				
				flag=false;
			}
				*/
					
		}
		

		
		System.out.println("Old Demand Set :"+str);

		return str.toString();

	}
    
	
	public  String fGetDemandSetStr(SnmpModel snmpModelObj)
	{
		StringBuilder str=new StringBuilder();  
		str.append(snmpModelObj.getNetworkId());
		str.append("|");
		for(int i=0;i< snmpModelObj.getNumberofDemands();i++)
		{
			str.append(snmpModelObj.snmpModelDemand[i].getDemandId());
			str.append("-");
			str.append(snmpModelObj.snmpModelDemand[i].getSrcNode());
			str.append("-");
			str.append(snmpModelObj.snmpModelDemand[i].getDestNode());
			str.append("-");
			str.append(fGetProtectionMechanism(snmpModelObj.snmpModelDemand[i]));
			str.append("-");
			str.append(fGetProtectiontype(snmpModelObj.snmpModelDemand[i].getProtectionType()));
			str.append("-");			
			str.append(snmpModelObj.snmpModelDemand[i].getRequiredTraffic());
			str.append("-");
			str.append(fGetPathType(snmpModelObj.snmpModelDemand[i].getPathType()));
			str.append("-");
			str.append(snmpModelObj.snmpModelDemand[i].getLineRate());
			//str.append(0);
            
			if((i+1)!=snmpModelObj.getNumberofDemands())
				str.append("#");
		}
		System.out.println("Demand Set :"+str);
		//String  DemandSetValue  = "1|1-1-9-4-80#2-2-5-5-100#3-2-5-2-80#4-5-6-3-80#5-5-6-3-20";
		//System.out.println("Network Topology :"+DemandSetValue);
		//return DemandSetValue;
		return str.toString();

	}
    
	public  void fSnmplistenerGet()
	{
		String  ipAddress  = SMConstants.destIpAddress;
	    Address tHost = GenericAddress.parse(ipAddress);
	    String  community  = SMConstants.communityPublic;
	    String  oidValue  = SMConstants.GetDemandOutputOid+1+"."+1; 
	    Snmp snmp;
	    
	    try {
	        TransportMapping transport = new DefaultUdpTransportMapping();
	        snmp = new Snmp(transport);
	        transport.listen();
	        CommunityTarget target = new CommunityTarget();
	        target.setCommunity(new OctetString(community));
	        target.setAddress(tHost);
	        target.setRetries(2);
	        target.setTimeout(5000);
	        target.setVersion(SnmpConstants.version2c); //Set the correct SNMP version here
	        PDU pdu = new PDU();
	        //Depending on the MIB attribute type, appropriate casting can be done here
	        pdu.add(new VariableBinding(new OID(oidValue)));
	        pdu.setType(PDU.GET);
	        ResponseListener listener = new ResponseListener() {

	            public void onResponse(ResponseEvent event) {
	                PDU strResponse;
	                String result;
	                ((Snmp) event.getSource()).cancel(event.getRequest(), this);
	                strResponse = event.getResponse();
	                if (strResponse != null) {
	                    result = strResponse.getErrorStatusText();
	                    System.out.println("get Status is: " + result);
	                }
	            }
	        };
	        snmp.send(pdu, target, null, listener);
	        snmp.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	
	}
	
    
	/*public  SnmpModelNetworkRoute fSnmpGet(SnmpModel snmpModelObj)
	{
		String  ipAddress  = SMConstants.destIpAddress;
		String  port    = SMConstants.port;
		// OID of MIB RFC 1213; Scalar Object = .iso.org.dod.internet.mgmt.mib-2.system.sysDescr.0
		
		int    snmpVersion  = SnmpConstants.version2c;

		String  community  = SMConstants.communityPublic;

		System.out.println("SNMP GET Demo");

		// Create TransportMapping and Listen
		TransportMapping transport = null;
		try {
			transport = new DefaultUdpTransportMapping();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			transport.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create Target Address object


		// Create Snmp object for sending data to Agent
		
		String Response = "";
		JSONObject rwaOutputJsonobj=new JSONObject();
		JSONArray RwaOutputArr=new JSONArray();
		
		CommunityTarget comtarget = new CommunityTarget();
		comtarget.setCommunity(new OctetString(community));
		comtarget.setVersion(snmpVersion);
		comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
		//comtarget.setRetries(5);
		//comtarget.setTimeout(100);
		

		for(int demandNum=1;demandNum<4;demandNum++)
		{
			PDU responsePDU=null;
			PDU pdu = new PDU();
			ResponseEvent response;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Snmp snmp = new Snmp(transport);            
			String  oidValue  = SMConstants.GetDemandOutputOid+1+"."+(demandNum+1);  // ends with 0 for scalar object
			System.out.println(oidValue);
			// Create the PDU object		
			pdu.clear();
			pdu.add(new VariableBinding(new OID(oidValue)));
			pdu.setType(PDU.GET);
			
			System.out.println(pdu.getVariableBindings());
			//pdu.setRequestID(new Integer32(1));
			System.out.println("Sending Request to Agent for demand:-"+demandNum);
			response = null;
			try {
				response = snmp.get(pdu, comtarget);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			// Process Agent Response
			if (response != null)
			{
				System.out.println("Got Response from Agent "+response);
				if(responsePDU!=null)
				responsePDU.clear();
				responsePDU = response.getResponse();
				//System.out.println("Response from rwa: " + responsePDU.toString());
				if (responsePDU != null)
				{
					int errorStatus = responsePDU.getErrorStatus();
					int errorIndex = responsePDU.getErrorIndex();
					String errorStatusText = responsePDU.getErrorStatusText();

					if (errorStatus == PDU.noError)
					{
						System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings());
						VariableBinding v = responsePDU.get(0);
						Variable vars = v.getVariable();
						SMFunctions smfObj=new SMFunctions();
						System.out.println("Received PDU... " + pdu);
						Response =vars.toString();
						JSONObject jsonObj = new JSONObject(Response);
						JSONArray RwaOutputJsonObjArr = (JSONArray)jsonObj.get("RwaOutput");
						JSONObject pathJsonObj = new JSONObject(Response);
						for (int i = 0; i < RwaOutputJsonObjArr.length(); i++) 
						{*//**loop through all the elements of the network*//*
							pathJsonObj =(JSONObject) RwaOutputJsonObjArr.getJSONObject(i);		
							RwaOutputArr.put(pathJsonObj);
						}
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
			try {
				snmp.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		rwaOutputJsonobj.put("RwaOutputStr",RwaOutputArr);
		System.out.println(rwaOutputJsonobj.toString());
		//JSONArray RwaOutputArray=(JSONArray)rwaOutputJsonobj.get("RwaOutputStr");
		SnmpModelNetworkRoute snmpModelNetworkRoute=fSaveDemandResponse(Response);
		return snmpModelNetworkRoute;
		
	}*/

	public SnmpModelNetworkRoute snmpSet(SnmpModel snmpModelObj)  throws Exception
	{
		//SMFunctions smFunObj=new SMFunctions();
		System.out.println("SNMP SET START");

		// Create TransportMapping and Listen
		TransportMapping transport = new DefaultUdpTransportMapping();
		transport.listen();

		// Create Target Address object
		CommunityTarget comtarget = fCreateCommunityTarget(); 

		// Create the PDU object
		PDU pdu = new PDU();
		Snmp snmp=null;
		OID oid=null;
		Variable var=null;
		VariableBinding varBind=null;
		
		System.out.println(" snmpModelObj.getNumberofOldDemands() "+ snmpModelObj.getNumberofOldDemands() );
		
		int setStringCount=SMConstants.setSetStringCount;
		
		if((snmpModelObj.getNetworkFieldType().equalsIgnoreCase(MapConstants.BrownField) || snmpModelObj.getNetworkFieldType().equalsIgnoreCase(MapConstants.GreenField ))
				&& snmpModelObj.getNumberofOldDemands() == MapConstants.I_ZERO )
			setStringCount=SMConstants.setSetStringCount - MapConstants.I_ONE;/**In case of Green Field*/		
		
		System.out.println(" setStringCount : "+setStringCount + " snmpModelObj.getNumberofOldDemands() "+ snmpModelObj.getNumberofOldDemands());
		
		snmp = new Snmp(transport);
		String linkmatrix=SMConstants.LinkMatrixOid+snmpModelObj.getNetworkId(),
				includeSet=SMConstants.IncludeSetOid+snmpModelObj.getNetworkId(),
				excludeSet=SMConstants.ExcludeSetOid+snmpModelObj.getNetworkId(),
				oldDemandSet=SMConstants.GetOldDemandSetInputOid+snmpModelObj.getNetworkId(),
				includeNodeSet=SMConstants.IncludeNodeSetOid+snmpModelObj.getNetworkId();
		
		while(setStringCount>0)
		{
			// Setting the Oid and Value 
			switch(setStringCount)
			{
			/**Step For BrownField : Old Demand Set*/
			case 7:
				oid = new OID(oldDemandSet);
				System.out.println("GetOldDemandSetInputOid : "+oid);
				var = new OctetString(fGetOldDemandSetStr(snmpModelObj));
				varBind = new VariableBinding(oid,var);
				break;
			
			/**Step For Node Include Set*/
			case 2:
				oid = new OID(includeNodeSet);
				System.out.println("includeNodeSet oid : "+oid);
				var = new OctetString(fGetIncludeNodeSetStr(snmpModelObj));
				varBind = new VariableBinding(oid,var);
				break;
			
			case 5:
				oid = new OID(SMConstants.NetrworkTopologyOid);
				System.out.println("NetrworkTopologyOid : "+oid);
				var = new OctetString(fGetNetworkTopologyStr(snmpModelObj));
				varBind = new VariableBinding(oid,var);
				break;
			case 4:
				oid = new OID(linkmatrix);
				System.out.println("linkmatrix : "+oid);
				var = new OctetString(fGetLinkMatrixStr(snmpModelObj));
				varBind = new VariableBinding(oid,var);
				break;
			case 3:
				oid = new OID(includeSet);
				System.out.println("includeSet : "+oid);
				var = new OctetString(fGetIncludeSetStr(snmpModelObj));
				varBind = new VariableBinding(oid,var);
				break;
			case 6:
				oid = new OID(excludeSet);
				System.out.println("excludeSet : "+oid);
				var = new OctetString(fGetExcludeSetStr(snmpModelObj));
				varBind = new VariableBinding(oid,var);
				break;    
			case 1:
				oid = new OID(SMConstants.DemandSetOid);
				System.out.println("DemandSetOid : "+oid);
				var = new OctetString(fGetDemandSetStr(snmpModelObj));
				varBind = new VariableBinding(oid,var);
				break;
			}

			pdu.add(varBind);

			pdu.setType(PDU.SET);
			pdu.setRequestID(new Integer32(1));

			// Create Snmp object for sending data to Agent


			//System.out.println("\nRequest:\n[ Note: Set Request is sent for sysContact oid in RFC 1213 MIB.");
			/*System.out.println("Set operation will change the "+var +" value to " + oid );
			System.out.println("Once this operation is completed, Querying for sysContact will get the value = " + var + " ]");

			System.out.println("Request:\nSending Snmp Set Request to Agent...");*/

			//Response Only on DemandSet
			//Set Request

			snmp.set(pdu, comtarget);

			if(setStringCount==1)
			{

				//System.out.println("Trap receiver activated");
				//SnmpTrapReceiver snmp4jTrapReceiver = new SnmpTrapReceiver();
				//snmp4jTrapReceiver.TrapReceiver(snmp4jTrapReceiver);	
				//snmp4jTrapReceiver.listen(new UdpAddress("192.168.115.187/1025"));
				System.out.println("Getting Status for Rwa Algo Output");
				int response=2;
				int attempts=0;
				while(response==2)
				{
					System.out.println("RwaOutputStatus Response in while Loop"+response);
					response=fSnmpGetRwaOutputStatus();
					if(attempts>3)
						break;
					else attempts++;
					Thread.sleep(4000);
				}
				if(attempts>10)
				System.out.println("Number of attempts exceeded 3");
				
				System.out.println("response rwaOutputStatus"+response);
				//int rwasGetStatus=fSnmpGetRwaOutputStatus();

				if(response==1)
				{
					//String ResponseJsonStr="";
					
					/*SnmpModelNetworkRoute snmpModelNetworkRoute=fSnmpGet(snmpModelObj);

					if(snmpModelNetworkRoute==null)
					{
						System.out.println("Rwa Output Status Response is success with value"+response+" . Get Output Data is a failure");
					}
					else
					{
						return snmpModelNetworkRoute;
					}*/
					//fSnmplistenerGet();
					String Responses = "";
					JSONObject rwaOutputJsonobj=new JSONObject();
					JSONArray RwaOutputArr=new JSONArray();
					
					
					String Response="";
					//int i=1;
					for(int i=0;i<snmpModelObj.getNumberofDemands();i++)
					{
						Response=fSnmpGetRwaOutput(snmpModelObj.snmpModelDemand[i].getDemandId(),snmpModelObj.getNetworkId());
						//System.out.println("Response"+Response);
						if(Response!="")
						{
						JSONObject jsonObj = new JSONObject(Response);
						JSONArray RwaOutputJsonObjArr = (JSONArray)jsonObj.get("RwaOutput");
						JSONObject pathJsonObj = new JSONObject(Response);
						for (int r = 0; r < RwaOutputJsonObjArr.length(); r++) 
						{
							pathJsonObj =(JSONObject) RwaOutputJsonObjArr.getJSONObject(r);	
							//System.out.println("Path Obj String Response"+pathJsonObj.toString());
							RwaOutputArr.put(pathJsonObj);							
						}
						}
						Thread.sleep(1000);
					}
					rwaOutputJsonobj.put("RwaOutputStr",RwaOutputArr);
					System.out.println("------------------------------------------------------------");
					System.out.println("Final Output Json String from RWA ::"+ rwaOutputJsonobj.toString());
					System.out.println("------------------------------------------------------------");
					SnmpModelNetworkRoute snmpModelNetworkRoute=fSaveDemandResponse(rwaOutputJsonobj.toString());
					return snmpModelNetworkRoute;
				}
				else if(response==0)
				{
					System.out.println("Rwa Output Status Response is a failure with value "+response);
				}				
			}
			setStringCount--;

		}
		snmp.close();
		return null;
	}

	private int fSnmpGetRwaOutputStatus() {
		// TODO Auto-generated method stub
		int rwaOutputStatus=0;
		String  ipAddress  = SMConstants.destIpAddress;
		String  port    = SMConstants.port;
		// OID of MIB RFC 1213; Scalar Object = .iso.org.dod.internet.mgmt.mib-2.system.sysDescr.0
		String  oidValue  = SMConstants.GetDemandOutputStatus;  // ends with 0 for scalar object

		int    snmpVersion  = SnmpConstants.version2c;

		String  community  = SMConstants.communityPublic;

		System.out.println("SNMP GET RWA OUTPUT STATUS");

		// Create TransportMapping and Listen
		TransportMapping transport = null;
		try {
			transport = new DefaultUdpTransportMapping();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			transport.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create Target Address object
		CommunityTarget comtarget = new CommunityTarget();
		comtarget.setCommunity(new OctetString(community));
		comtarget.setVersion(snmpVersion);
		comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
		comtarget.setRetries(2);
		comtarget.setTimeout(1000);

		// Create the PDU object
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(oidValue)));
		pdu.setType(PDU.GET);
		//pdu.setRequestID(new Integer32(1));

		// Create Snmp object for sending data to Agent
		Snmp snmp = new Snmp(transport);

		System.out.println("Sending Request to Agent for Getting Rwa Status...");
		ResponseEvent response = null;
		try {
			response = snmp.get(pdu, comtarget);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Process Agent Response
		if (response != null)
		{
			System.out.println("Got Response from Agent for Rwa Status...");
			PDU responsePDU = response.getResponse();

			if (responsePDU != null)
			{
				int errorStatus = responsePDU.getErrorStatus();
				int errorIndex = responsePDU.getErrorIndex();
				String errorStatusText = responsePDU.getErrorStatusText();

				if (errorStatus == PDU.noError)
				{
					System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings());
					VariableBinding v = responsePDU.get(0);
					Variable vars = v.getVariable();
					System.out.println("Received PDU for Rwa Status...... " + pdu);
					System.out.println("Received PDU for Rwa Status...... " + vars.toString());
					//rwaOutputStatus=fSaveDemandResponse(vars.toString());
					rwaOutputStatus=Integer.parseInt(vars.toString());
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
		try {
			snmp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rwaOutputStatus;
	}
	
	

	public SnmpModelNetworkRoute fSaveDemandResponse(String string) {
		// TODO Auto-generated method stub
		//System.out.println("Json Demand Output Sting"+string);
		JSONObject jsonObj = new JSONObject(string);
		JSONArray RwaOutputJsonObjArr=new JSONArray();
		JSONArray RegenLocOutputJsonObjArr=new JSONArray();
		JSONArray RegenLocOsnrOutputJsonObjArr=new JSONArray();
		JSONArray NodeFailureArray=new JSONArray();
		JSONArray LinkFailureArray=new JSONArray();
		
		if(jsonObj.length()!=0)
		{
		RwaOutputJsonObjArr = (JSONArray)jsonObj.get("RwaOutputStr");

		SnmpModelNetworkRoute snmpModelNetworkRoute=new SnmpModelNetworkRoute(RwaOutputJsonObjArr.length());
		/*snmpModelNetworkRoute.setNetworkId(Integer.parseInt(jsonObj.get("NetworkId").toString().trim()));
		snmpModelNetworkRoute.setDemandId(Integer.parseInt(jsonObj.get("DemandId").toString().trim()));
		snmpModelNetworkRoute.setRoutePriority(0);
		snmpModelNetworkRoute.setTraffic(0);
		snmpModelNetworkRoute.setThreeRLocationHeadToTail("NA");
		snmpModelNetworkRoute.setThreeRLocationTailToHead("NA");*/
        
		
		JSONObject DemandOutputJsonObj;
		String pathStr,errorString,linkIdStr;
		String regeneratorLocationStr,regeneratorLocationOsnrStr;
		System.out.println("--------------------------------------");
		int currDemandId=RwaOutputJsonObjArr.getJSONObject(0).getInt("DemandId"),
		    prevDemandId=RwaOutputJsonObjArr.getJSONObject(0).getInt("DemandId"),
		    errorStateToRoutePriority=5;
		for (int i = 0; i < RwaOutputJsonObjArr.length(); i++) {/**loop through all the elements of the network*/
            			
			DemandOutputJsonObj = RwaOutputJsonObjArr.getJSONObject(i);	
			currDemandId=DemandOutputJsonObj.getInt("DemandId");
			if(currDemandId!=prevDemandId)
			{
				prevDemandId=currDemandId;
				errorStateToRoutePriority=5;
			}
			
			RegenLocOutputJsonObjArr = (JSONArray)DemandOutputJsonObj.get("RegeneratorLocations");
			
			pathStr=DemandOutputJsonObj.get("Path").toString().trim().replaceAll("[<>\\[\\]-]", "");
			linkIdStr=DemandOutputJsonObj.get("LinkIdSet").toString().trim().replaceAll("[<>\\[\\]-]", "");
			errorString="";
			
			regeneratorLocationStr=DemandOutputJsonObj.get("RegeneratorLocations").toString().trim().replaceAll("[<>\\[\\]-]", "");
			if(RegenLocOutputJsonObjArr.length()==0)
				regeneratorLocationStr="-";
			
			RegenLocOsnrOutputJsonObjArr = (JSONArray)DemandOutputJsonObj.get("RegeneratorLocationsOSNR");
			regeneratorLocationOsnrStr=DemandOutputJsonObj.get("RegeneratorLocationsOSNR").toString().trim().replaceAll("[<>\\[\\]-]", "");
			if(RegenLocOsnrOutputJsonObjArr.length()==0)
				regeneratorLocationOsnrStr="-";
				
			
			System.out.println("Path"+i+": path--- "+pathStr);
			System.out.println("Path"+i+": linkIdStr--- "+linkIdStr);
			System.out.println("Path"+i+": Osnr---"+DemandOutputJsonObj.get("Osnr"));
			System.out.println("Path"+i+": Wavelength---"+DemandOutputJsonObj.get("Wavelength"));
			System.out.println("Path"+i+": Osnr---"+DemandOutputJsonObj.get("DemandId"));
			System.out.println("Path"+i+": Wavelength---"+DemandOutputJsonObj.get("NetworkId"));
			System.out.println("Path"+i+": Type---"+DemandOutputJsonObj.get("Type"));
			System.out.println("Path"+i+": Reg Loc---"+DemandOutputJsonObj.get("RegeneratorLocations"));
			System.out.println("Path"+i+": Reg Loc Osnr---"+DemandOutputJsonObj.get("RegeneratorLocationsOSNR"));
			System.out.println("Path"+i+": Error State---"+DemandOutputJsonObj.get("ErrorState"));
			
			int errorState=DemandOutputJsonObj.getInt("ErrorState");			
			if(errorState==SMConstants.NODEFAILURE)				
			{
				NodeFailureArray=(JSONArray)DemandOutputJsonObj.get("NodeFailure");
				System.out.println("NodeFailure:"+NodeFailureArray.join(",").toString());
				//errorString+="Node Id : "+NodeFailureArray.getInt(0)+"\n";
				errorString+=fNodeFailureStr(NodeFailureArray.getInt(1));	
				
				if(NodeFailureArray.getInt(1)==SMConstants.OSNROUTOFRANG)
					errorString+=" at Node"+NodeFailureArray.getInt(0);
				else if(NodeFailureArray.getInt(1)==SMConstants.HUBNODEINPATH)
				{
					errorString="Hub Node("+NodeFailureArray.getInt(0)+") in path";
				}
				else
					errorString+=" due to Node "+NodeFailureArray.getInt(0);		
				
				System.out.println("Path"+i+": NodeFailure---"+DemandOutputJsonObj.get("NodeFailure"));
			}
			else if(errorState==SMConstants.LINKFAILURE)				
			{
				LinkFailureArray=(JSONArray)DemandOutputJsonObj.get("LinkFailure");
				//errorString+="Ingress Nod : "+LinkFailureArray.getInt(0)+"Egress Nod : "+LinkFailureArray.getInt(1)+"\n";
				errorString+=fLinkFailureStr(LinkFailureArray);				
				System.out.println("Path"+i+": LinkFailure---"+DemandOutputJsonObj.get("LinkFailure"));
			}
			else if(errorState==SMConstants.INCLUDESETNODEFAILURE_ANY)				
			{
//				NodeFailureArray=(JSONArray)DemandOutputJsonObj.get("NodeFailure");
//				//errorString+="Node Id : "+NodeFailureArray.getInt(0)+"\n";
//				errorString+=fNodeFailureStr(NodeFailureArray.getInt(1));	
//				
//				if(NodeFailureArray.getInt(1)==SMConstants.OSNROUTOFRANG)
//					errorString+=" at Node"+NodeFailureArray.getInt(0);
//				else
//					errorString+=" due to Node "+NodeFailureArray.getInt(0);		
//				
//				System.out.println("Path"+i+": Include Any Failure---"+DemandOutputJsonObj.get("NodeFailure"));
				
				System.out.println("Path"+i+": Not a Single Node From Include Node Set---");
				errorString="No Include Set node in path";
			}
			else if(errorState==SMConstants.INCLUDESETNODEFAILURE_ALL)				
			{
				NodeFailureArray=(JSONArray)DemandOutputJsonObj.get("NodeFailure");
				//errorString+="Node Id : "+NodeFailureArray.getInt(0)+"\n";
				errorString+=fNodeFailureStr(NodeFailureArray.getInt(1));	
				
				if(NodeFailureArray.getInt(1)==SMConstants.OSNROUTOFRANG)
					errorString+=" at Node"+NodeFailureArray.getInt(0);
				else
					errorString+=" due to Node "+NodeFailureArray.getInt(0);		
				
				System.out.println("Path"+i+": Include All Failure---"+DemandOutputJsonObj.get("NodeFailure"));
			}
			else if(errorState==SMConstants.LESSPATHCOUNT)
			{
				System.out.println("Path"+i+": LessPathFound---");
				errorString="Paths generated for this demand are less than requested";
			}
			else if(errorState==SMConstants.NOPATHFOUND)
			{
				System.out.println("Path"+i+": NoPathFound---");
				errorString="No paths were found for this demand.";
			}
			
			System.out.println("Error String :"+errorString);
			
			System.out.println("--------------------------------------");			
			
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setDemandId(DemandOutputJsonObj.getInt("DemandId"));
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setNetworkId(DemandOutputJsonObj.getInt("NetworkId"));
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setPath(pathStr);
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setPathLinkId(linkIdStr);
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setOsnr(Float.parseFloat(DemandOutputJsonObj.get("Osnr").toString()));
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setWavelengthNo(DemandOutputJsonObj.getInt("Wavelength"));
			
			if((DemandOutputJsonObj.get("Type").toString().trim()).equalsIgnoreCase("Working"))
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setRoutePriority(1);
			else if((DemandOutputJsonObj.get("Type").toString().trim()).equalsIgnoreCase("Protection"))
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setRoutePriority(2);
			else if((DemandOutputJsonObj.get("Type").toString().trim()).equalsIgnoreCase("Restoration1"))
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setRoutePriority(3);
			else if((DemandOutputJsonObj.get("Type").toString().trim()).equalsIgnoreCase("Restoration2"))
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setRoutePriority(4);				
			if((DemandOutputJsonObj.get("Type").toString().trim()).equalsIgnoreCase("Rejected") && (errorState==SMConstants.LINKFAILURE || errorState==SMConstants.NODEFAILURE || errorState==SMConstants.INCLUDESETNODEFAILURE_ALL || errorState==SMConstants.INCLUDESETNODEFAILURE_ANY))
		    snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setRoutePriority(errorStateToRoutePriority++);
			else if((DemandOutputJsonObj.get("Type").toString().trim()).equalsIgnoreCase("Rejected") && errorState==SMConstants.LESSPATHCOUNT )
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setRoutePriority(-1);
			
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setTraffic(Float.parseFloat(DemandOutputJsonObj.get("Capacity").toString()));
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setPathType(DemandOutputJsonObj.get("SubType").toString());
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setRegeneratorLocations(regeneratorLocationStr);
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setRegeneratorLocationsOsnr(regeneratorLocationOsnrStr);
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setLineRate(DemandOutputJsonObj.get("LineRate").toString());
			snmpModelNetworkRoute.snmpModelNetworkRoutePathInfo[i].setError(errorString);
			
		}
		return snmpModelNetworkRoute;
		}
		else 
		{
			System.out.println("Empty Json Object Returned");
			
		}

		return null;


	}
	
	int fGetPathTypeInt(String str)
	{
		switch(str)
		{
		case SMConstants.DisjointStr:
			return SMConstants.Disjoint;		
		case SMConstants.NonDisjointStr:
			return SMConstants.NonDisjoint;		
		default:
			return -1;
		}		
	}
	
	String fNodeFailureStr(int code)
	{
		switch(code)
		{
		case SMConstants.NONNODEDISJOINT:
			return SMConstants.NONNODEDISJOINTSTR;		
		case SMConstants.ODDTOEVEN:
			return SMConstants.ODDTOEVENSTR;
		case SMConstants.EVENTOODD:
			return SMConstants.EVENTOODDSTR;
		case SMConstants.OSNROUTOFRANG:
			return SMConstants.OSNROUTOFRANGSTR;
		case SMConstants.HUBNODEINPATH:
			return SMConstants.OSNROUTOFRANGSTR;
		default:
			return "Returning Default Case for Unknown Node Failure Code";
		}		
	}
	
	String fLinkFailureStr(JSONArray LinkFailureArray)
	{
		switch(LinkFailureArray.getInt(2))
		{
		case SMConstants.NONSRLGDISJOINT:
			return SMConstants.NONSRLGDISJOINTSTR+" due to link b/w Node "+LinkFailureArray.getInt(0)+" and Node "+LinkFailureArray.getInt(1);		
		case SMConstants.NOLEMBDAAVAILABLE:
			return SMConstants.NOLEMBDAAVAILABLESTR+" at link b/w Node "+LinkFailureArray.getInt(0)+" and Node "+LinkFailureArray.getInt(1);
		case SMConstants.LOADHIGH:
			return SMConstants.LOADHIGHSTR+" at link b/w Node "+LinkFailureArray.getInt(0)+" and Node "+LinkFailureArray.getInt(1);
		case SMConstants.NOCHANNLEPROTECTIONLEMBDAAVAILABLE:
			return SMConstants.NOCHANNLEPROTECTIONLEMBDAAVAILABLESTR+" at link b/w Node "+LinkFailureArray.getInt(0)+" and Node "+LinkFailureArray.getInt(1);
		default:
			return "Returning Default Case for Unknown Link Failure Code";
		}		
	}
	
	private String fSnmpGetRwaOutput(int d,int networkId) {
		// TODO Auto-generated method stub
		String rwaResponseOutputStr="";
		String  ipAddress  = SMConstants.destIpAddress;
		String  port    = SMConstants.port;
		// OID of MIB RFC 1213; Scalar Object = .iso.org.dod.internet.mgmt.mib-2.system.sysDescr.0
		String  oidValue  = SMConstants.GetDemandOutputOid+networkId+"."+d;  // ends with 0 for scalar object

		int    snmpVersion  = SnmpConstants.version2c;

		String  community  = SMConstants.communityPublic;

		System.out.println("SNMP GET RWA OUTPUT STATUS");

		// Create TransportMapping and Listen
		TransportMapping transport = null;
		try {
			transport = new DefaultUdpTransportMapping();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			transport.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create Target Address object
		CommunityTarget comtarget = new CommunityTarget();
		comtarget.setCommunity(new OctetString(community));
		comtarget.setVersion(snmpVersion);
		comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
		comtarget.setRetries(2);
		comtarget.setTimeout(1000);

		// Create the PDU object
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(oidValue)));
		pdu.setType(PDU.GET);
		//pdu.setRequestID(new Integer32(1));

		// Create Snmp object for sending data to Agent
		Snmp snmp = new Snmp(transport);

		System.out.println("Sending Request to Agent for Getting Rwa Status...");
		ResponseEvent response = null;
		try {
			response = snmp.get(pdu, comtarget);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Process Agent Response
		if (response != null)
		{
			System.out.println("Got Response from Agent for Rwa Status...");
			PDU responsePDU = response.getResponse();

			if (responsePDU != null)
			{
				int errorStatus = responsePDU.getErrorStatus();
				int errorIndex = responsePDU.getErrorIndex();
				String errorStatusText = responsePDU.getErrorStatusText();

				if (errorStatus == PDU.noError)
				{
					System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings());
					VariableBinding v = responsePDU.get(0);
					Variable vars = v.getVariable();
					//System.out.println("Received PDU for Rwa Status...... " + pdu);
					System.out.println("Received PDU for Rwa Status...... " + vars.toString());
					//rwaOutputStatus=fSaveDemandResponse(vars.toString());
					//rwaOutputStatus=Integer.parseInt(vars.toString());
					rwaResponseOutputStr =vars.toString();
					
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
		try {
			snmp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rwaResponseOutputStr;
	}
	
}