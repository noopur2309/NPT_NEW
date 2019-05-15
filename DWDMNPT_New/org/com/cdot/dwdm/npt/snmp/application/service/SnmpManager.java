package application.service;


import application.MainMap;
import application.SMFunctions;
import application.model.SnmpModel;
import application.model.SnmpModelNetworkRoute;



public class SnmpManager {


	public SnmpModelNetworkRoute SnmpRwaExecution(SnmpModel snmpModelObj,DbService dbService)  throws Exception{

		/**Retrieve Data From Obj*/

		MainMap.logger = MainMap.logger.getLogger(SnmpManager.class.getName());/**Logger Start*/

		MainMap.logger.info("-----------------Snmp Obj Data --------------------------");
		MainMap.logger.info("NumberofNodes :- "+snmpModelObj.getNumberOfNodes()+
				"Numberoflinks :-"+snmpModelObj.getNumberofLinks());

		/*	 MainMap.logger.info("-----------------Link Data --------------------------");
			 MainMap.logger.info("Source Node :- "+Integer.parseInt(snmpModelObj.snmpModelLink[0].getSrc())+
		 			 "Destination Node :-"+Integer.parseInt(snmpModelObj.get("DestNode").toString())+
		 			 "Color :- "+Integer.parseInt(snmpModelObj.get("Colour").toString())+
		 			"Metric Cost :- "+Integer.parseInt(snmpModelObj.get("MetricCost").toString())+
		 			"Length :- "+Integer.parseInt(snmpModelObj.get("Length").toString())+
		 			"Span Loss :- "+Integer.parseInt(snmpModelObj.get("SpanLoss").toString())+
		 			"Capacity :- "+Integer.parseInt(snmpModelObj.get("Capacity").toString())+
		 			"Fiber Type :- "+Integer.parseInt(snmpModelObj.get("FiberType").toString()));


			 MainMap.logger.info("-----------------Topology Data --------------------------");			 
			 MainMap.logger.info("Node Type :- "+snmpModelObj.get("NodeType").toString()+
			 			"East :- "+Integer.parseInt(snmpModelObj.get("Dir1").toString())+
			 			"West :- "+Integer.parseInt(snmpModelObj.get("Dir2").toString())+
			 			"North :- "+Integer.parseInt(snmpModelObj.get("Dir3").toString())+
			 			"South :- "+Integer.parseInt(snmpModelObj.get("Dir4").toString())+
			 			"NE :- "+Integer.parseInt(snmpModelObj.get("Dir5").toString())+
			 			"NW :- "+Integer.parseInt(snmpModelObj.get("Dir6").toString())+
			 			"SE :- "+Integer.parseInt(snmpModelObj.get("Dir7").toString())+
			 			"SW :- "+Integer.parseInt(snmpModelObj.get("Dir8").toString()));

		 */
		/**Snmp set Call*/		
		SMFunctions smfObj=new SMFunctions(dbService);
		SnmpModelNetworkRoute snmpModelNetworkRoute=smfObj.snmpSet(snmpModelObj);
		if(snmpModelNetworkRoute!=null)
		{
		System.out.println("returning algorithm data successfully");	
		return snmpModelNetworkRoute;
		}
		else return null;
	}

	


}
