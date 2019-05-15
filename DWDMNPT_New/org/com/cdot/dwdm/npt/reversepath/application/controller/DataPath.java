package application.controller;
import application.controller.*;
import application.model.UpstreamOtnTdmSLsp;
import application.model.PortMappingLsp;
import application.model.LambdaLspInformation;
import application.model.TpnPortInfo;
import application.model.YCablePortInfo;
import application.model.PortInfo;
import application.model.OtnLspInformation;
import application.model.Circuit;
import application.model.CircuitDemandMapping;
import application.model.Demand;
import application.model.CardInfo;
import application.model.PtcClientProtInfo;
import application.model.NetworkRoute;
import application.controller.ResourcePlanning;
import application.constants.DataPathConfigFileConstants;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.controller.GenerateLinkWavelengthMap;
import application.service.DbService;
import application.constants.ResourcePlanConstants;
import application.service.DbService;
import application.constants.MapConstants;
import java.sql.SQLException;
import org.json.simple.*;

import org.json.JSONArray;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import ch.qos.logback.classic.Logger;
import application.MainMap;
public class DataPath 
{   
	
	
	/**
	 * @author surya
	 * @param XmlPorts
	 * @param dbPorts
	 * @return
	 */
	public List<Integer> FindDifference(List<Integer> XmlPorts, List<Integer> dbPorts)
     { List<Integer> Difference = new ArrayList<Integer>();
		 int m = XmlPorts.size();
		 int n = dbPorts.size();
		 for(int i=0; i< m; i++)
		 { int k =0;
		   for(int j=0;j<n;j++)
		   {  if(XmlPorts.get(i) == dbPorts.get(j))
		       { k =1;
		       }
		   else if(k==1)
		   { 
			    break;
		       }
			   
		   }
		   
		   if(k==0)
		   { Difference.add(XmlPorts.get(i));
		   
		   }
		 }
		 
     	return Difference ;
     }
	
	/**
	 * @author surya
	 * @param PortMappings
	 * @return
	 */
	public List<Integer> GetXMLPorts(NodeList PortMappings)
	 {
            List<Integer> PortMapArray = new ArrayList<Integer>();
	        for(int p=0;p< PortMappings.getLength();p++) // finding port numbers from xml file
	        {  Node PortMapping = PortMappings.item(p);
	        Element PortMapElement = (Element)PortMapping;
	        PortMappingLsp PortMap = new PortMappingLsp();
	        PortMap.setPortNumber(Integer.parseInt( PortMapElement.getElementsByTagName("PortNumber").item(0).getTextContent()));
	        PortMap.setTsGroupId(Integer.parseInt( PortMapElement.getElementsByTagName("TsGroupId").item(0).getTextContent()));
	        PortMapArray.add(PortMap.getPortNumber());
	        
	        }
	        return PortMapArray;
	 }
	
	
	/**
	 * 
	 * @brief 
	 * @author surya
	 * @date 
	 * @param dbService
	 * @param networkID
	 * @param NodeId
	 * @param TunnelId
	 * @param Newports
	 * @param dbports
	 * @param networkInfoMap
	 * @throws SQLException
	 */
	 public void UpdateAllDbs(DbService dbService,int networkID,int NodeId, int TunnelId,List <Integer> Newports,List <Integer> dbports,HashMap<String,Object> networkInfoMap,String TrafficType) throws SQLException
	 {  
		 
		 
		 
         for(int m=0; m < Newports.size(); m++)
         {  
        	 
        	//finding circuitid from otnlspinformation 
        	List<OtnLspInformation> OtnLspInformation = dbService.getOtnLspInformationSerivce().FindCircuitIdByTunnelId(networkID, TunnelId);
            int DemandId = OtnLspInformation.get(0).getDemandId();
            String CircuitID = OtnLspInformation.get(0).getCircuitId();
            String Circuit[] = CircuitID.split(",");
           //System.out.println(CircuitID +Circuit[Circuit.length-1]);
            Circuit PrevCircuit = dbService.getCircuitService().FindCircuit(networkID, Integer.parseInt(Circuit[0]));
        	//System.out.println(PrevCircuit);
         	int PrevCircuitId = PrevCircuit.getCircuitId();
	        PrevCircuit.setCircuitId(-1);
	        PrevCircuit.setQoS(0);
	        PrevCircuit.setTrafficType(TrafficType);
	        //System.out.println(PrevCircuit);
	        //adding CircuitId in Circuit Table
	        ArrayList<Integer> NodeArray = new ArrayList<Integer>();
	        NodeArray.add(PrevCircuit.getSrcNodeId());
	        NodeArray.add(PrevCircuit.getDestNodeId());
	        
	        int CircuitId = dbService.getCircuitService().InsertCircuit(PrevCircuit, networkInfoMap );
	        
	        List<NetworkRoute> AllRoutes = dbService.getNetworkRouteService().FindAllByDemandId(networkID, DemandId);
	        
	        Float UpdatedTraffic = AllRoutes.get(0).getTraffic() + PrevCircuit.getRequiredTraffic();
	        dbService.getNetworkRouteService().UpdateTrafficForDemand(networkID, DemandId, UpdatedTraffic);
	        
	        
	        //Updating CircuitId in OtnLspInformation table
	        CircuitID = CircuitID+","+CircuitId;
	        dbService.getOtnLspInformationSerivce().UpdateCircuitID(networkID, TunnelId, CircuitID); 
	        // updating CircuitDemandMappingEntry 
	        CircuitDemandMapping CircuitDemand = dbService.getCircuitDemandMappingService().FindDemand(networkID, PrevCircuitId);
	        CircuitDemand.setCircuitId(CircuitId);
	        dbService.getCircuitDemandMappingService().InsertCircuitDemandMapping(CircuitDemand); 
	       
	        // updating Demandtable
	        dbService.getDemandService().UpdateCircuitSet(networkID, CircuitDemand.getDemandId(),CircuitID);
	        
	        for(int count = 0; count < NodeArray.size(); count++)
	       
	        {
                 NodeId = NodeArray.get(count);  	
                 
                 MainMap.logger.info("Adding Details for Node : "+NodeId);
	        
	        Demand DemandForCircuit = dbService.getDemandService().FindDemand(networkID,DemandId);
	            
	        //finding car information
	        List<CardInfo> Cards = dbService.getCardInfoService().FindMpn(networkID, NodeId, DemandId);
	        int RackId = Cards.get(0).getRack();
	        int Sbrack = Cards.get(0).getSbrack();
         	   
	            
	            if(m==0)
         	    {
         	    	
         	    	List<PortInfo> PreviousPorts = dbService.getPortInfoService().FindPortInfobyPortID(networkID, NodeId,dbports.get(0),DemandId);
            		PortInfo PreviousPortWork = PreviousPorts.get(0);
            		PortInfo PreviousPortProt = PreviousPorts.get(1);
            		  
            		   
            		PreviousPortWork.setPort(Newports.get(m));
            		PreviousPortWork.setCircuitId(CircuitId);
            		dbService.getPortInfoService().Insert(PreviousPortWork);
            		   
            		PreviousPortProt.setPort(Newports.get(m));
            		PreviousPortProt.setCircuitId(CircuitId);
            		dbService.getPortInfoService().Insert(PreviousPortProt);
            		   
         	    	
         	    	
         	    	List<TpnPortInfo> TpnofPreviousUp = dbService.getTpnPortInfoService().FindAll(networkID, NodeId,RackId,Sbrack, dbports.get(0), 1);
         	    	//System.out.println("tpn of previous"+TpnofPreviousUp +dbports.get(0));
         	    	List<TpnPortInfo> TpnofPreviousDown = dbService.getTpnPortInfoService().FindAll(networkID, NodeId,RackId,Sbrack, dbports.get(0), 0);
         	   
         	    	  
          		   //Inserting Port Information for Working
          		   TpnPortInfo TpntoInsertUp = TpnofPreviousUp.get(0);
          		   TpnPortInfo TpntoInsertDown = TpnofPreviousDown.get(0);
          		   TpntoInsertUp.setPortId(Newports.get(m));
          		   dbService.getTpnPortInfoService().Insert(TpntoInsertUp);
          		   TpntoInsertDown.setPortId(Newports.get(m));
          		   dbService.getTpnPortInfoService().Insert(TpntoInsertDown);
          		   
          		   
          		   //Inserting Port Information for Protection
          		   TpnPortInfo TpntoInsertUpProt = TpnofPreviousUp.get(1);
          		   TpnPortInfo TpntoInsertDownProt = TpnofPreviousDown.get(1);
          		   TpntoInsertUpProt.setPortId(Newports.get(m));
          		   dbService.getTpnPortInfoService().Insert(TpntoInsertUpProt);
          		   TpntoInsertDownProt.setPortId(Newports.get(m));
          		   dbService.getTpnPortInfoService().Insert(TpntoInsertDownProt);
          		       
          		   //int NetworkGfId =  (Integer)networkInfoMap.get(MapConstants.GreenFieldId);
    	           //ResourcePlanning Resource = new ResourcePlanning(dbService);
    	           //Resource.fAssignYcablesBrField(NetworkGfId);
         	       //Resource.update_ycable_patchcord_info(networkID, NodeId, 1);
     	            

         	    
         	    
         	    
         	    }
	           
         	    else
         	    {   
         	    	
         	    	
         	    	List<PortInfo> PreviousPorts = dbService.getPortInfoService().FindPortInfobyPortID(networkID, NodeId,Newports.get(m-1),DemandId);
      		        PortInfo PreviousPortWork = PreviousPorts.get(0);
      		        PortInfo PreviousPortProt = PreviousPorts.get(1);
      		  
      		   
      		        PreviousPortWork.setPort(Newports.get(m));
      		        PreviousPortWork.setCircuitId(CircuitId);
      		        dbService.getPortInfoService().Insert(PreviousPortWork);
      		   
      		        PreviousPortProt.setPort(Newports.get(m));
      		        PreviousPortProt.setCircuitId(CircuitId);
      		        dbService.getPortInfoService().Insert(PreviousPortProt);
      		   
         	    	 
     	           List<TpnPortInfo> TpnofPreviousUp = dbService.getTpnPortInfoService().FindAll(networkID, NodeId,RackId,Sbrack, Newports.get(m-1), 1);
          		   List<TpnPortInfo> TpnofPreviousDown = dbService.getTpnPortInfoService().FindAll(networkID, NodeId,RackId,Sbrack, Newports.get(m-1), 0);
          		  
         		   //Inserting Port Information for Working
         		   TpnPortInfo TpntoInsertUp = TpnofPreviousUp.get(0);
         		   TpnPortInfo TpntoInsertDown = TpnofPreviousDown.get(0);
         		   TpntoInsertUp.setPortId(Newports.get(m));
         		   dbService.getTpnPortInfoService().Insert(TpntoInsertUp);
         		   TpntoInsertDown.setPortId(Newports.get(m));
         		   dbService.getTpnPortInfoService().Insert(TpntoInsertDown);
         		   
         		   
         		   //Inserting Port Information for Protection
         		   TpnPortInfo TpntoInsertUpProt = TpnofPreviousUp.get(1);
         		   TpnPortInfo TpntoInsertDownProt = TpnofPreviousDown.get(1);
         		   TpntoInsertUpProt.setPortId(Newports.get(m));
         		   dbService.getTpnPortInfoService().Insert(TpntoInsertUpProt);
         		   TpntoInsertDownProt.setPortId(Newports.get(m));
         		   dbService.getTpnPortInfoService().Insert(TpntoInsertDownProt);
         		   //int NetworkGfId =  (Integer)networkInfoMap.get(MapConstants.GreenFieldId);    
    	           //ResourcePlanning Resource = new ResourcePlanning(dbService);
    	          // Resource.fAssignYcablesBrField(NetworkGfId);
    	           //Resource.update_ycable_patchcord_info(networkID, NodeId, 1);
         	      
         	       
         	    
         	    
         	    }
	           
	        }
         
         
         }
         
        
         GenerateLinkWavelengthMap linkwmap = new GenerateLinkWavelengthMap(dbService);
			dbService.getLinkWavelengthService().DeleteByNetworkId(networkID);
			dbService.getLinkWavelengthInfoService().DeleteByNetworkId(networkID);
			linkwmap.fgenerateLinkWavelengthInformation(networkID);
         
         
         ArrayList<Integer> NodeArray2 = new ArrayList<Integer>();
         List<OtnLspInformation> OtnLspInformation = dbService.getOtnLspInformationSerivce().FindCircuitIdByTunnelId(networkID, TunnelId);
         int DemandId = OtnLspInformation.get(0).getDemandId();
         String CircuitID = OtnLspInformation.get(0).getCircuitId();
         String Circuit[] = CircuitID.split(",");
        //System.out.println(CircuitID +Circuit[Circuit.length-1]);
         Circuit PrevCircuit = dbService.getCircuitService().FindCircuit(networkID, Integer.parseInt(Circuit[0]));
     	//System.out.println(PrevCircuit);
      	int PrevCircuitId = PrevCircuit.getCircuitId();
	       //System.out.println(PrevCircuit);
	       //adding CircuitId in Circuit Table
	        
	        NodeArray2.add(PrevCircuit.getSrcNodeId());
	        NodeArray2.add(PrevCircuit.getDestNodeId());
         
         

	   for(int count = 0; count < NodeArray2.size(); count++)
	       
	   {
              
	      NodeId = NodeArray2.get(count); 
	      MainMap.logger.info("Adding Ycables and PatchCords for Node : "+NodeId);
	      
         int NetworkGfId =  (Integer)networkInfoMap.get(MapConstants.GreenFieldId);    
         //System.out.println("NetworkGfId :"+NetworkGfId);
         ResourcePlanning Resource = new ResourcePlanning(dbService);
         PatchCordAllocation pc=new PatchCordAllocation(dbService);
        
         
         /**
			 * For Added Circuits
			 */
			List<CardInfo> YCabs = dbService.getCardInfoService().FindCardInfoByCardType(networkID, NodeId,
					ResourcePlanConstants.YCable1x2Unit);
			List<Circuit> addedCirs = dbService.getCircuitService().FindAddedCircuitsInBrField(NetworkGfId, networkID,
					NodeId);
			CardInfo YCabTobeFilled = new CardInfo();
			for (int j = 0; j < addedCirs.size(); j++) {
				if (addedCirs.get(j).getClientProtectionType().equalsIgnoreCase(MapConstants.YCableProtection)) {
					CircuitDemandMapping dem = dbService.getCircuitDemandMappingService().FindDemand(networkID,
							addedCirs.get(j).getCircuitId());
					CardInfo mpnN = dbService.getCardInfoService().FindMpn(networkID, NodeId, dem.getDemandId(),
							ResourcePlanConstants.Working);

					if (mpnN != null) {
						for (int i = 0; i < YCabs.size(); i++) {
							YCabTobeFilled = YCabs.get(i);
							int nPorts = dbService.getYcablePortInfoService().FindUsedPortCount(networkID, NodeId,
									YCabs.get(i).getRack(), YCabs.get(i).getSbrack(), YCabs.get(i).getCard());
							if (nPorts == 10) {
								System.out.println(" Y Cable full " + YCabs.get(i).toString());
								continue;
							} else {
								// YCabTobeFilled=YCabs.get(i);
								// System.out.println(" YCabTobeFilled "+ YCabTobeFilled.toString());
								break;
							}
						}
						MainMap.logger.info(" YCabTobeFilled " + YCabTobeFilled.toString());
						MainMap.logger.info("Circuit " + addedCirs.get(j).getCircuitId() + " Demand " + dem.getDemandId()
						+ "\n MPN N ()" + mpnN.toString());
						YCablePortInfo ycab = new YCablePortInfo();
						ycab.setNetworkId(networkID);
						ycab.setNodeId(NodeId);
						ycab.setMpnLocN("" + mpnN.getRack() + "_" + "" + mpnN.getSbrack() + "_" + "" + mpnN.getCard());
						PortInfo port = dbService.getPortInfoService().FindPortInfo(networkID, NodeId, mpnN.getRack(),
								mpnN.getSbrack(), mpnN.getCard(), addedCirs.get(j).getCircuitId());
						ycab.setMpnPortN(port.getPort());
						ycab.setYCableRack(YCabTobeFilled.getRack());
						ycab.setYCableSbrack(YCabTobeFilled.getSbrack());
						ycab.setYCableCard(YCabTobeFilled.getCard());

						CardInfo mpnP = dbService.getCardInfoService().FindMpn(networkID, NodeId, dem.getDemandId(),
								ResourcePlanConstants.Protection);

						if (mpnP != null)// protection mpn exists
						{
							MainMap.logger.info("Circuit " + addedCirs.get(j).getCircuitId() + " Demand " + dem.getDemandId()
							+ "\n MPN P ()" + mpnP.toString());
							ycab.setMpnLocP(
									"" + mpnP.getRack() + "_" + "" + mpnP.getSbrack() + "_" + "" + mpnP.getCard());
							PortInfo portP = dbService.getPortInfoService().FindPortInfo(networkID, NodeId,
									mpnP.getRack(), mpnP.getSbrack(), mpnP.getCard(), addedCirs.get(j).getCircuitId());
							ycab.setMpnPortP(portP.getPort());
						}
						for(int p=0;p<Newports.size();p++)
						{ if(Newports.get(p) == ycab.getMpnPortP())
						{
						dbService.getYcablePortInfoService().Insert(ycab);
						//System.out.println("Inserted " + ycab.toString());
					                  }
						}
						
						
					}
					
					
				}
			}
			
         dbService.getPatchCordService().DeletePatchCordInfoby(networkID,NodeId);
	     pc.update_ycable_patchcord_info(networkID, NodeId, 1);
	     
	     
         }
	   
	 } 
		 
		 
		 
		 
	 
	 
	
	 
	       
	
	 
	 
	 
	 
	 
	 /**
	  * @author surya
	  * @param dbService
	  * @param node
	  * @param eElement
	  * @param root
	  * @param networkInfoMap
	  * @param NodeId
	  * @throws SQLException
	  */
	 public void AddClientofSameService(DbService dbService,int Type,Node node, Element eElement,Element root,HashMap<String,Object> networkInfoMap,int NodeId) throws SQLException
	 
	 
	 {
        
		 
		 
		 
		int networkID = (Integer)networkInfoMap.get(MapConstants.NetworkId);
        int TunnelId = 	(Integer.parseInt(eElement.getElementsByTagName("TunnelId").item(0).getTextContent()));
        int LSPId = 	(Integer.parseInt(eElement.getElementsByTagName("LSPId").item(0).getTextContent()));	
        int SignalType = (Integer.parseInt(eElement.getElementsByTagName("SignalType").item(0).getTextContent()));
        List<OtnLspInformation> OtnLspInformation = dbService.getOtnLspInformationSerivce().FindCircuitIdByTunnelId(networkID, TunnelId);
        int DemandId = OtnLspInformation.get(0).getDemandId();
        String CircuitID = OtnLspInformation.get(0).getCircuitId();
        String Circuit[] = CircuitID.split(",");
        //System.out.println(CircuitID +Circuit[Circuit.length-1]);
        Circuit PrevCircuit = dbService.getCircuitService().FindCircuit(networkID, Integer.parseInt(Circuit[Circuit.length-1]));
        ParseUpStream(dbService,Type,eElement,root,networkInfoMap,DemandId,TunnelId,NodeId,LSPId, SignalType);
        
       
      }
	 
	 
	 public String DeleteCircuitinCircuitSet(String CircuitSet,int CircuitId)
	 {
		String Circuits[] = CircuitSet.split(",");
	    String result = null;
	   // System.out.println("remove"+CircuitId);
		for(int p =0; p<Circuits.length; p++)
			
		{   if((p == 0) && ! Circuits[p].equals(Integer.toString(CircuitId)))
	        { //System.out.println(Circuits[0].equals(Integer.toString(CircuitId)));
		     result = Circuits[p];
		     //System.out.println("First"+result);
		    }
		
		else if(p!=0 && p!= Circuits.length-1 && Circuits[p].equals(Integer.toString(CircuitId)))
			{
				
				result =  result+ ","+Circuits[p+1];
				p = p+1;
				// System.out.println("Second"+result);
			}
			else if( p==0 && Circuits[p].equals(Integer.toString(CircuitId)))
			{
			    result = Circuits[p+1];
			    p = p+1;
			    //System.out.println("Third"+result);
			
			}
			else if ( p == Circuits.length -1 && Circuits[p].equals(Integer.toString(CircuitId)))
			{
				result = result;
				 //System.out.println("Four"+result);
			}
					
					
			else 
			{
			   result = result + ","+ Circuits[p]; 
			  // System.out.println("Five"+result);
			}
			
				
				
			}
			
			
			
			
		
	
			 
		 
		 
		 return result;
	 }
			 
			 
			 
	 
	 
	public void  DeleteClientDetails(DbService dbService,List<Integer> PortMapArray,HashMap<String,Object> networkInfoMap,int NodeId,int TunnelId,int DemandId,int LSPId) throws SQLException
 	
	{   int networkID = (Integer)networkInfoMap.get(MapConstants.NetworkId);
	    List<CardInfo> Cards = dbService.getCardInfoService().FindMpn(networkID, NodeId, DemandId);
        int RackId = Cards.get(0).getRack();
        int Sbrack = Cards.get(0).getSbrack();
        int Card  = Cards.get(0).getCard();
        ResourcePlanning  DeleteResource = new ResourcePlanning(dbService); 
        PatchCordAllocation pc=new PatchCordAllocation(dbService);
        int ProtCardId = Cards.get(1).getCard();
        
        
		for(int p=0;p < PortMapArray.size();p++)
		
		{  
			//Finding CircuitId for ports
			int CircuitId = dbService.getPortInfoService().FindCircuitIdforPortID(networkID, NodeId,RackId,Sbrack,Card, PortMapArray.get(p));
			Circuit circuit = dbService.getCircuitService().FindCircuit(networkID, CircuitId);
			ArrayList<Integer> NodeArray = new ArrayList<Integer>();
			NodeArray.add(circuit.getSrcNodeId());
			NodeArray.add(circuit.getDestNodeId());
			dbService.getCircuitService().DeleteCircuit(networkID, CircuitId);
			dbService.getCircuitDemandMappingService().DeleteCircuitDemandMapping(networkID, CircuitId);
			
			Demand Demand = dbService.getDemandService().FindDemand(networkID, DemandId);
			List<OtnLspInformation> OtnLspofClient = dbService.getOtnLspInformationSerivce().FindCircuitIdByTunnelId(networkID,TunnelId);
			String CircuitSetInOtnLsp = OtnLspofClient.get(0).getCircuitId();		
			String CircuitSet = Demand.getCircuitSet();
			//System.out.println(CircuitSet +CircuitId);
			String updatedCircuitset = DeleteCircuitinCircuitSet(CircuitSet,CircuitId);
			dbService.getDemandService().UpdateCircuitSet(networkID, DemandId, updatedCircuitset);
		    
			if(CircuitSetInOtnLsp.length()!= MapConstants.I_ONE)
			{
			   dbService.getOtnLspInformationSerivce().UpdateCircuitID(networkID, TunnelId, updatedCircuitset);
			
			}
			else 
			{
				dbService.getOtnLspInformationSerivce().DeleteSpecificOtnLsp(networkID,DemandId,TunnelId,LSPId);
			}
			
			
			
			for(int count = 0; count < NodeArray.size(); count++)
				
			{	NodeId = NodeArray.get(count);	
			
			MainMap.logger.info("Deleting Details of NodeId : " +NodeId);
			
			//Deleting Ports in Ports table
            dbService.getPortInfoService().DeletePort(networkID, NodeId, RackId, Sbrack, Card, PortMapArray.get(p));
			dbService.getPortInfoService().DeletePort(networkID, NodeId, RackId, Sbrack, ProtCardId, PortMapArray.get(p));
			
			//Deleting portInfo in TpnPortInfo for Upstream and DownStream for both Working Card and Protection Card
			dbService.getTpnPortInfoService().DeleteClient(networkID, NodeId, RackId, Sbrack, Card, MapConstants.I_ZERO,PortMapArray.get(p) );
			dbService.getTpnPortInfoService().DeleteClient(networkID, NodeId, RackId, Sbrack, Card, MapConstants.I_ONE,PortMapArray.get(p) );
			dbService.getTpnPortInfoService().DeleteClient(networkID, NodeId, RackId, Sbrack, ProtCardId, MapConstants.I_ZERO,PortMapArray.get(p) );
			dbService.getTpnPortInfoService().DeleteClient(networkID, NodeId, RackId, Sbrack, ProtCardId, MapConstants.I_ONE,PortMapArray.get(p) );
			
			dbService.getPtcClientProtInfoService().DeleteClient(networkID, NodeId, RackId, Sbrack,Card,PortMapArray.get(p) );
		   //Deleting Ycableports
			dbService.getYcablePortInfoService().Deleteycableport(networkID, NodeId, PortMapArray.get(p));
			 dbService.getPatchCordService().DeletePatchCordInfoby(networkID,NodeId);
			 
		     pc.update_ycable_patchcord_info(networkID, NodeId, 1);
			
			
			
		}
		
		
		}
		
		
		
		
	}
	 
        	
        	
	 public void ParseUpStream(DbService dbService,int Type,Element eElement,Element root,HashMap<String,Object> networkInfoMap,int DemandId,int TunnelId,int NodeId,int LSPId,int SignalType) throws SQLException
	   
	 {   
		 
		    int networkID = (Integer)networkInfoMap.get(MapConstants.NetworkId);
		    
		    //Parsing UPSTREAM  tag
	        NodeList UpStream = eElement.getElementsByTagName("Upstream");
	        MainMap.logger.info("No of Upstream Tags"+UpStream.getLength());
	        
	        
	        Node UpstreamNode = UpStream.item(0);
	        Element UpstreamElement = (Element)UpstreamNode;
	        UpstreamOtnTdmSLsp Up = new UpstreamOtnTdmSLsp();
	    
	        Up .setWavelength(Integer.parseInt( UpstreamElement.getElementsByTagName("Wavelength").item(0).getTextContent()));	
	        Up .setDirection(Integer.parseInt( UpstreamElement.getElementsByTagName("Direction").item(0).getTextContent()));	
	        Up .setEncodingType(Integer.parseInt( UpstreamElement.getElementsByTagName("EncodingType").item(0).getTextContent()));	
	        Up .setGpid(Integer.parseInt( UpstreamElement.getElementsByTagName("GPID").item(0).getTextContent()));	
	        Up .setSwitchingType(Integer.parseInt( UpstreamElement.getElementsByTagName("SwitchingType").item(0).getTextContent()));	
	        Up .setTpn(Integer.parseInt( UpstreamElement.getElementsByTagName("TPN").item(0).getTextContent()));	
	        Up .setProtectionSubType(Integer.parseInt( UpstreamElement.getElementsByTagName("ProtectionSubType").item(0).getTextContent()));	
	        Up .setIsRevertive(Integer.parseInt( UpstreamElement.getElementsByTagName("IsRevertive").item(0).getTextContent()));	
	        Up .setFecType(Integer.parseInt( UpstreamElement.getElementsByTagName("FecType").item(0).getTextContent()));	
	        Up .setFecStatus(Integer.parseInt( UpstreamElement.getElementsByTagName("FecStatus").item(0).getTextContent()));	
	        Up .setTxSegment(Integer.parseInt( UpstreamElement.getElementsByTagName("TxSegment").item(0).getTextContent()));	
	        Up .setOperatorSpecific( UpstreamElement.getElementsByTagName("OperatorSpecific").item(0).getTextContent());	
	        Up .setTimDectMode(Integer.parseInt( UpstreamElement.getElementsByTagName("TimDectMode").item(0).getTextContent()));	
	        Up .setTcmActStatus(Integer.parseInt( UpstreamElement.getElementsByTagName("TCMActStatus").item(0).getTextContent()));	
	        Up .setTcmActValue(Integer.parseInt( UpstreamElement.getElementsByTagName("TCMActValue").item(0).getTextContent()));
	        NodeList UpElement =  UpstreamElement.getElementsByTagName("TxTCMPriority");
	        
	        
	       for(int u=0;u<UpElement.getLength();u++)
	       {  
	          Up .setTxTcmPriority(UpElement.item(u).getTextContent());	
	        }
	        Up .setGccType(Integer.parseInt( UpstreamElement.getElementsByTagName("GccType").item(0).getTextContent()));
	        Up .setGccStatus(Integer.parseInt( UpstreamElement.getElementsByTagName("GccStatus").item(0).getTextContent()));
	        Up .setGccValue(Integer.parseInt( UpstreamElement.getElementsByTagName("GccValue").item(0).getTextContent()));

	        
	        System.out.println("GPID"+Up.getGpid());
	        String TrafficType =  DataPathConfigFileConstants.TrafficTypeMapping(SignalType,Integer.parseInt(UpstreamElement.getElementsByTagName("GPID").item(0).getTextContent())).toString();
	        
	        
	        
	        
	        //parsing PortMapping tag
	        
	        NodeList PortMappings =  UpstreamElement.getElementsByTagName("PortMapping");
	        List<Integer> PortMapArray = GetXMLPorts(PortMappings);
	        List<CardInfo> Cards = dbService.getCardInfoService().FindMpn(networkID, NodeId, DemandId);//finding card service
	        int RackId = Cards.get(0).getRack();
	        int Sbrack = Cards.get(0).getSbrack();
	        
	        List<Integer> dbPorts = dbService.getPortInfoService().FindPorts(networkID, NodeId, RackId, Sbrack, DemandId);//finding existing ports in database
	        MainMap.logger.info("Actual dbPOrts" +dbPorts);
	        List<Integer> Newports = FindDifference(PortMapArray,dbPorts); //finding new ports added in xml file
	        MainMap.logger.info("NewPorts : "+Newports);
	       
	        if(Type == MapConstants.I_TWO || Type  ==  MapConstants.I_ONE)
	        
	        { 
	        if(!Newports.isEmpty())
	       {
	          UpdateAllDbs(dbService,networkID,NodeId,TunnelId,Newports,dbPorts,networkInfoMap,TrafficType);
	          UpdatePtcClientProt(dbService,root,NodeId,networkInfoMap);
	       }
	       else
	       { MainMap.logger.info("No Changes");
	       
	       }
	        
		 
	        }
	       
	        else if(Type==3)
	        {   //MainMap.logger.info("deleting db");
	        	DeleteClientDetails(dbService,PortMapArray,networkInfoMap,NodeId,TunnelId,DemandId,LSPId);
	        	
	        	
	        }
		 
		 
	 }
        
        
        

     /**
      * @author surya
      * @param dbService
      * @param node
      * @param eElement
      * @param root
      * @param networkInfoMap
      * @param NodeId
      * @throws SQLException
      */
      public void AddClientofDifferentService(DbService dbService,int Type,Node node,Element eElement,Element root, HashMap<String,Object> networkInfoMap, int NodeId) throws SQLException
      
      {
    	 
    	  int networkID = (Integer)networkInfoMap.get(MapConstants.NetworkId);
          int TunnelId = 	(Integer.parseInt(eElement.getElementsByTagName("TunnelId").item(0).getTextContent()));
          int SignalType = (Integer.parseInt(eElement.getElementsByTagName("SignalType").item(0).getTextContent()));
          
          int LSPId = 	(Integer.parseInt(eElement.getElementsByTagName("LSPId").item(0).getTextContent()));	
         
    	  NodeList InterfaceId = eElement.getElementsByTagName("InterfaceId");
    	  NodeList Wavelengths = eElement.getElementsByTagName("Wavelength");
    	  NodeList UpStream = eElement.getElementsByTagName("Upstream");
    	  Node UpstreamNode = UpStream.item(0);
	      Element UpstreamElement = (Element)UpstreamNode;
    	  //for TrafficType
    	  String GPID =  UpstreamElement.getElementsByTagName("GPID").item(0).getTextContent();
    	  String TrafficType =  DataPathConfigFileConstants.TrafficTypeMapping(SignalType,Integer.parseInt(UpstreamElement.getElementsByTagName("GPID").item(0).getTextContent())).toString();
    	  //System.out.println("InterfaceId"+InterfaceId.getLength() +InterfaceId.item(0).getTextContent());
    	  int ForwardingAdjacency = Integer.parseInt(InterfaceId.item(0).getTextContent());
    	  int Wavelength  =   Integer.parseInt(Wavelengths.item(0).getTextContent());
    	  
    	  
    	  
    	  LambdaLspInformation LambdaLSP = dbService.getLambdaLspInformationSerivce().FindLSP(networkID,ForwardingAdjacency); 
    	  if(LambdaLSP == null)
    	  { MainMap.logger.info("LambdaLSP Doesnot Exist with that Forwarding Adjacency");
    	  }
    	  else
    	  {
    	  MainMap.logger.info("LambdaLsp :"+LambdaLSP);
    	  int DemandId = LambdaLSP.getDemandId();
    	  int RoutePriority = LambdaLSP.getRoutePriority();
    	  Demand DemandInfoforOTNTDMLSP = dbService.getDemandService().FindDemand(networkID, DemandId);
    	  String CircuitSet = DemandInfoforOTNTDMLSP.getCircuitSet();
    	  String Circuit[] = CircuitSet.split(",");
    	  //System.out.println(Circuit[Circuit.length-1]);
    	  int FirstCircuitId = Integer.parseInt(Circuit[Circuit.length-1]);
    	  Circuit CircuitforOTNTDMLSP = dbService.getCircuitService().FindCircuit(networkID, FirstCircuitId);
    	
          //checking the TunnelId availability for network
    	  OtnLspInformation Otn = dbService.getOtnLspInformationSerivce().FindLSP(networkID, DemandId, TunnelId,LSPId);
    	  
    	  if(Otn==null)
    		  
    	  {
    		  
    	  
    	  
    	  OtnLspInformation OtnLSP = new OtnLspInformation();
    	  OtnLSP.setNetworkId(networkID);
    	  OtnLSP.setDemandId(DemandId);
    	  OtnLSP.setCircuitId(CircuitSet);
    	  OtnLSP.setPath(LambdaLSP.getPath());
    	  OtnLSP.setRoutePriority(LambdaLSP.getRoutePriority());
    	  OtnLSP.setWavelengthNo(Wavelength);
    	  OtnLSP.setOtnLspTunnelId(TunnelId);
    	  OtnLSP.setLspId(LSPId);
    	  OtnLSP.setProtectionType(CircuitforOTNTDMLSP.getProtectionType());
    	  OtnLSP.setTrafficType(TrafficType);
    	  OtnLSP.setLineRate(DemandInfoforOTNTDMLSP.getLineRate());
    	  
    	  //inserting a new row in OTNTDMLSPInformation table
    	  dbService.getOtnLspInformationSerivce().InsertOtnLspInformation(OtnLSP);
    	  
    	  ParseUpStream(dbService,Type,eElement, root, networkInfoMap, DemandId, TunnelId, NodeId,LSPId,SignalType);
    	  UpdatePtcClientProt(dbService,root,NodeId,networkInfoMap);
    	  
    	  }
    	  
    	  else
    	  {
    		  MainMap.logger.info("Already existing tunnel Id" +Otn);
    		  
    	  }
    	  }
    	  
    	  
    	  
    	  
    	  
    	  
    	  
      }
      
      /**
       * @author surya
       * @param dbService
       * @param root
       * @param NodeId
       * @param networkInfoMap
       * @throws SQLException
       */
      public void UpdatePtcClientProt(DbService dbService,Element root,int NodeId,HashMap<String,Object> networkInfoMap) throws SQLException
      {   
    	  
    	  int networkID = (Integer)networkInfoMap.get(MapConstants.NetworkId);
    	  NodeList PtcClientTags = root.getElementsByTagName("PtcClientProtInfo");
    	  //System.out.println("COUNT"+PtcClientTags.getLength());
          
    	  for(int i =0;i <PtcClientTags.getLength();i++)
    	  { 
    		  Node PtcClient = PtcClientTags.item(i);
  	          Element eElement = (Element)PtcClient;
  	          PtcClientProtInfo PtcClients = new  PtcClientProtInfo();
  	          PtcClients.setNetworkId(networkID);
  	          PtcClients.setNodeId(NodeId);
  	          PtcClients.setActMpnRackId(Integer.parseInt( eElement.getElementsByTagName("ActMpnRackId").item(0).getTextContent()));
  	          PtcClients.setActMpnSubrackId(Integer.parseInt( eElement.getElementsByTagName("ActMpnSubrackId").item(0).getTextContent()));
  	          PtcClients.setActMpnCardId(Integer.parseInt( eElement.getElementsByTagName("ActMpnCardId").item(0).getTextContent()));
  	          PtcClients.setActMpnCardType( eElement.getElementsByTagName("ActMpnCardType").item(0).getTextContent());
  	          PtcClients.setActMpnCardSubType(Integer.parseInt(eElement.getElementsByTagName("ActMpnCardSubType").item(0).getTextContent()));
  	          PtcClients.setActMpnPort(Integer.parseInt( eElement.getElementsByTagName("ActMpnPort").item(0).getTextContent()));
  	          PtcClients.setProtMpnRackId(Integer.parseInt( eElement.getElementsByTagName("ProtMpnRackId").item(0).getTextContent()));
  	          PtcClients.setProtMpnSubrackId(Integer.parseInt( eElement.getElementsByTagName("ProtMpnSubrackId").item(0).getTextContent()));
  	          PtcClients.setProtMpnCardId(Integer.parseInt( eElement.getElementsByTagName("ProtMpnCardId").item(0).getTextContent()));
  	          PtcClients.setProtMpnCardType(eElement.getElementsByTagName("ProtMpnCardType").item(0).getTextContent());
  	          PtcClients.setProtMpnCardSubType(Integer.parseInt(eElement.getElementsByTagName("ProtMpnCardSubType").item(0).getTextContent()));
  	          PtcClients.setProtMpnPort(Integer.parseInt( eElement.getElementsByTagName("ProtMpnPort").item(0).getTextContent()));
  	          PtcClients.setProtCardRackId(Integer.parseInt( eElement.getElementsByTagName("ProtCardRackId").item(0).getTextContent()));
  	          PtcClients.setProtCardSubrackId(Integer.parseInt( eElement.getElementsByTagName("ProtCardSubrackId").item(0).getTextContent()));
  	          PtcClients.setProtCardCardId(Integer.parseInt( eElement.getElementsByTagName("ProtCardCardId").item(0).getTextContent()));  
  	          PtcClients.setProtTopology(eElement.getElementsByTagName("ProtTopology").item(0).getTextContent());  
  	          PtcClients.setProtMechanism(eElement.getElementsByTagName("ProtMechanism").item(0).getTextContent());   
  	          PtcClients.setProtStatus(Integer.parseInt( eElement.getElementsByTagName("ProtStatus").item(0).getTextContent()));     
  	          PtcClients.setProtType(Integer.parseInt( eElement.getElementsByTagName("ProtType").item(0).getTextContent())); 
  	          PtcClients.setActiveLine(Integer.parseInt( eElement.getElementsByTagName("ActiveLine").item(0).getTextContent())); 
    	      
  	          PtcClientProtInfo Client = dbService.getPtcClientProtInfoService().FindClient(networkID,NodeId,PtcClients.getActMpnRackId(),PtcClients.getActMpnSubrackId(),PtcClients.getActMpnCardId(),PtcClients.getActMpnPort());
  	          //System.out.println(Client);
  	          if(Client==null)
  	          {
  	        	  dbService.getPtcClientProtInfoService().Insert(PtcClients);
  	        	  //System.out.println("done");
  	        	  
  	        	  
  	          }
  	       
  	          
  	          
  	          
  		          
  	          
    	  }
    	  
    	  
    	  
      }
      
     
		 
     public void DeleteClientforOTNTDMLSP(DbService dbService,int Type,Node node,Element eElement,Element root,HashMap<String,Object> networkInfoMap,int NodeId) throws SQLException
     {
    	 
    	 int networkID = (Integer)networkInfoMap.get(MapConstants.NetworkId);
         int TunnelId = 	(Integer.parseInt(eElement.getElementsByTagName("TunnelId").item(0).getTextContent()));
         int LSPId = 	(Integer.parseInt(eElement.getElementsByTagName("LSPId").item(0).getTextContent()));	
        // System.out.println("inside Delete");
         List<OtnLspInformation> OtnLspInformation = dbService.getOtnLspInformationSerivce().FindCircuitIdByTunnelId(networkID, TunnelId);
         int DemandId = OtnLspInformation.get(0).getDemandId();
         ParseUpStream(dbService,Type,eElement,root,networkInfoMap,DemandId,TunnelId,NodeId,LSPId,0);
    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	 
     }
     
    
	 
    
	
	/**
	 * @author surya
	 * @param dbService
	 * @param networkInfoMapAddClientOTNTDMLSP
	 */
	public  JSONObject parsingDataPathXML(DbService dbService,String FileName,String Path )
	{  
		
		JSONObject FinalJsonArray = new JSONObject();
		File fXmlFile = new File(Path + FileName); // input file
		try 
		{  
			
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fXmlFile);
			Element root = document.getDocumentElement();
			MainMap.logger.info("root"+root);
			String NetworkName = root.getElementsByTagName("NetworkName").item(0).getTextContent();
			
			int networkID = dbService.getNetworkService().GetNetworkInfoByNetworkName(NetworkName).getNetworkId();
		    int NetworkType = Integer.parseInt(root.getElementsByTagName("NetworkType").item(0).getTextContent());
			String SiteName = root.getElementsByTagName("SiteName").item(0).getTextContent();
			String StationName = root.getElementsByTagName("StationName").item(0).getTextContent();
			int NodeId = Integer.parseInt(root.getElementsByTagName("NodeId").item(0).getTextContent());
			HashMap<String,Object> networkInfoMap = new HashMap<String,Object>();
			networkInfoMap.put(MapConstants.NetworkId, networkID);
			if(NetworkType == 1)
			{
			networkInfoMap.put(MapConstants.NetworkType, "BrownField");
			int GreenFieldId = (int)dbService.getNetworkService().GetGreenFieldNetworkId(networkID);
			networkInfoMap.put(MapConstants.GreenFieldId, GreenFieldId);
			networkInfoMap.put(MapConstants.BrownFieldId, networkID);
			}
			
			else
			{
				networkInfoMap.put(MapConstants.NetworkType, "GreenField");
				networkInfoMap.put(MapConstants.GreenFieldId, networkID);
				networkInfoMap.put(MapConstants.BrownFieldId, 0);
				
			}
			 
			
			//MapConstants.GreenFieldId
			
			//parsing OTNTDMLSP tag
			NodeList OTNTDMLSP= root.getElementsByTagName("OTNTDMLSP");
			
			
			for(int i=0; i< OTNTDMLSP.getLength();i++)
		    
			
			{      
					
					Node node = OTNTDMLSP.item(i);
		            Element eElement = (Element) node;
		            int TYPE = (Integer.parseInt(eElement.getElementsByTagName("TYPE").item(0).getTextContent()));
		            MainMap.logger.info("TYPE"     +TYPE);
		            
		            switch (TYPE)
		            {
		           
		            
		            case 1 :  {   MainMap.logger.info("Adding Client of Different Service");
		                        AddClientofDifferentService(dbService,TYPE,node,eElement,root,networkInfoMap,NodeId);
		                           break;
		                                                           }
		            	
		            case 2 :  { MainMap.logger.info("Adding Client of Same Service");
		            	        AddClientofSameService(dbService,TYPE,node,eElement,root,networkInfoMap,NodeId);	
		                        break;
		                                                           }
		            
		            case 3 : {  
		            	         MainMap.logger.info("Deleting Client........");
		                         DeleteClientforOTNTDMLSP(dbService,TYPE,node,eElement,root,networkInfoMap,NodeId);
		            	          
		            	          
		            	          break;
		                                                           } 
		            
		            
		            default : {MainMap.logger.info("Not a Valid Type");
		                        break;
		                                }
		            
		            }
		            
		            
		            
		            }
			
			      
		            
		            
		            
		    
		    
		}
		
		catch (Exception e)

		{
			e.printStackTrace();

		}
		
		
		return FinalJsonArray;
		
	}
	
}
		
		
			
			
			
			


