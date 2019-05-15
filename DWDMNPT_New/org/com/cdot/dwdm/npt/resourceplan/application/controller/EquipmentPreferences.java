package application.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.AllocationExceptions;
import application.model.CardInfo;
import application.model.EquipmentPreference;
import application.model.ParametricPreference;
import application.model.Stock;
import application.service.DbService;

public class EquipmentPreferences {
	DbService dbService;
	ResourcePlanUtils rpu; 
	public static Logger logger = Logger.getLogger(EquipmentPreferences.class.getName());
	
	public EquipmentPreferences(DbService dbService) {
		super();		
		this.dbService = dbService;	
		rpu = new ResourcePlanUtils(dbService);			
	}
	
	/**
	 * Finds the Preferred Equipment Type or Equipment according to the Preferences
	 * @param Category
	 * @param Param
	 * @param Value
	 * @return
	 * 	Check the parametric pref  first if(parametric pref exists) -> get eq info, check if quantity exists in stock, if yes reduce it, 
	 *  and if serial no is found in param pref then mark it used  
	 *  if(parametric pref not exists) -> check the stock, if any quantity of that category exists then use it, if not follow the preference order
	 *  if more than one quantity exist in stock in same category follow the pref order for the existing quantities first  
	 * @throws SQLException 
	 */
	public Object[] fGetPreferredEqTypeold(int networkid, int nodeid, String category, String param, String value) throws SQLException
	{		
		logger.info("fGetPreferredEqType"+" NetworkId: "+networkid+" NodeId: "+nodeid+" Category: "+category+" Param: "+param+" Value: "+value);
		Object eqInfo[] = new Object[2] ;
			
			ParametricPreference  pp = dbService.getParametricPreferenceService().Find(networkid, nodeid, category, param, value);
			if(pp!=null) 
			{
				eqInfo[0]=pp.getCardType();
				eqInfo[1]=pp.getSerialNo();
				
				logger.info(" parametric preference is: Type : "+eqInfo[0]+" Sr No: "+eqInfo[1]);
				
				//find parametric stock with serial no
				Stock st = dbService.getStockService().Find(networkid, nodeid, category, eqInfo[0].toString(),eqInfo[1].toString());	
				if(st!=null)
				{
					logger.info("found parametric preference stock"+st.toString());
					int quantity = st.getQuantity();
					st.setQuantity(quantity-1);
					
					dbService.getStockService().UpdateQuantity(st);
					if(!eqInfo[1].toString().isEmpty()) {
						st.setStatus("Used");
						dbService.getStockService().UpdateStatus(st);						
					}
				}				
				else
				{
					logger.info("parametrically preferred equipment not found available in stock");					
					eqInfo[0]= pp.getCardType();	
					eqInfo[1]="";
				}
			}
			else// ParametricPreference doesnot exist
			{
				logger.info(" parametric preference doesnot exist ");
				String cardtype = dbService.getStockService().FindPreferredExistingStock(networkid, nodeid, category);				
				if(cardtype!=null)
				{
					eqInfo[0]= cardtype;
					//try to find the stock with unused serial no
					Stock st1 = dbService.getStockService().FindUnUsedSrNoStock(networkid, nodeid, category, cardtype);
					if(st1!=null)
					{
						logger.info("Unused serial no stock found in stock"+st1.toString());
						if(!st1.getStatus().equalsIgnoreCase("Used"))
						{
							eqInfo[1]=st1.getSerialNo();
							st1.setStatus("Used");
							dbService.getStockService().UpdateStatus(st1);
						}
						else
						{
							eqInfo[1]="";
						}
						int quantity = st1.getQuantity();
						st1.setQuantity(quantity-1);	
						
						dbService.getStockService().UpdateQuantity(st1);
						
					}
					else if(st1==null)
					{
						//try to find stock without serial no 						
						Stock st = dbService.getStockService().Find(networkid, nodeid, category, cardtype);
						if(st!=null)
						{
							logger.info("Stock found is: "+st.toString());
							if(!st.getStatus().equalsIgnoreCase("Used"))
							{
								eqInfo[1]=st.getSerialNo();
//								st.setStatus("Used");
								dbService.getStockService().UpdateStatus(st);
							}
							else
							{
								eqInfo[1]="";
							}
							int quantity = st.getQuantity();
							st.setQuantity(quantity-1);	
							
							dbService.getStockService().UpdateQuantity(st);
							
						}
						else
						{
							logger.info("No Stock found, Fetching preference for allocation ");
							EquipmentPreference ep = dbService.getEquipmentPreferenceService().FindPreferredEq(networkid, nodeid, category);
							eqInfo[0]= ep.getCardType();	
							eqInfo[1]="";
						}
						
					}					
				}
				else//no quantity exists in stock
				{
					logger.info("No Stock found, Fetching preference for allocation ");
					EquipmentPreference ep = dbService.getEquipmentPreferenceService().FindPreferredEq(networkid, nodeid, category);
					eqInfo[0]= ep.getCardType();	
					eqInfo[1]="";
				}
					
			}
			
			logger.info("fGetPreferredEqType"+" NetworkId: "+networkid+" NodeId: "+nodeid+" Cardtype: "+eqInfo[0].toString()+" Card Serial No: "+eqInfo[1]);
		
		return eqInfo;		
	}
	
	public Object[] fGetPreferredEqType(int networkid, int nodeid, String category, String param, String value) throws SQLException
	{		
		logger.info("fGetPreferredEqType"+" NetworkId: "+networkid+" NodeId: "+nodeid+" Category: "+category+" Param: "+param+" Value: "+value);
		Object eqInfo[] = new Object[2] ;
			
			ParametricPreference  pp = dbService.getParametricPreferenceService().Find(networkid, nodeid, category, param, value);
			if(pp!=null) 
			{
				eqInfo[0]=pp.getCardType();
				eqInfo[1]=pp.getSerialNo();
				
				logger.info(" parametric preference is: Type : "+eqInfo[0]+" Sr No: "+eqInfo[1]);
				
				if(!pp.getSerialNo().isEmpty())
				{
					//find parametric stock with serial no
					Stock st = dbService.getStockService().Find(networkid, nodeid, category, eqInfo[0].toString(),eqInfo[1].toString());
					if(st!=null)
					{
						logger.info("found parametric preference stock"+st.toString());
						int quantity = st.getQuantity();
						st.setQuantity(quantity-1);
						dbService.getStockService().UpdateQuantity(st);
						
						int Usedquantity = st.getUsedQuantity();
						st.setUsedQuantity(Usedquantity+1);
						dbService.getStockService().UpdateUsedQuantity(st);
						
						st.setStatus("Used");
						dbService.getStockService().UpdateStatus(st);						
					}
					else//find parametric existing stock without serial no
					{
						logger.info("parametrically preferred equipment with Sr no not found available in stock, Getting existing stock with parametrically preferred eq type ");						
						Stock st1 = dbService.getStockService().FindUnUsedStockWithoutSrNo(networkid, nodeid, category, pp.getCardType());
						if(st1!=null)
						{
							logger.info(" found unused stock without sr no ");
							eqInfo[0]= st1.getCardType();
							eqInfo[1]="";
							int quantity = st1.getQuantity();
							st1.setQuantity(quantity-1);								
							dbService.getStockService().UpdateQuantity(st1);
							
							int Usedquantity = st1.getUsedQuantity();
							st1.setUsedQuantity(Usedquantity+1);
							dbService.getStockService().UpdateUsedQuantity(st1);
						}
						else
						{
							//try to find the stock with unused serial no
							Stock st3 = dbService.getStockService().FindUnUsedStockFreeFromParamPref(networkid, nodeid, category, pp.getCardType());
							if(st3!=null)
							{
								logger.info("Unused stock with Serial No, free from Param Pref found in stock "+st3.toString());
								eqInfo[0]= st3.getCardType();
								eqInfo[1]=st3.getSerialNo();
								int quantity = st3.getQuantity();
								st3.setQuantity(quantity-1);
								dbService.getStockService().UpdateQuantity(st3);
								
								int Usedquantity = st3.getUsedQuantity();
								st3.setUsedQuantity(Usedquantity+1);
								dbService.getStockService().UpdateUsedQuantity(st3);
								
								st3.setStatus("Used");
								dbService.getStockService().UpdateStatus(st3);														
							}
							else//no quantity exists in stock
							{
								logger.info("parametrically preferred equipment not found available in stock, procurement needed");					
								eqInfo[0]= pp.getCardType();	
								eqInfo[1]="";
							}
						}						
					}					
				}
				else
				{
					//find existing parametric stock without serial no
					logger.info("parametrically preferred equipment with Sr no not found available in stock, Getting existing stock with parametrically preferred eq type ");						
					Stock st2 = dbService.getStockService().FindUnUsedStockWithoutSrNo(networkid, nodeid, category, pp.getCardType());
					if(st2!=null)
					{
						logger.info(" found unused stock with sr no ");
						eqInfo[0]= st2.getCardType();
						eqInfo[1]="";
						int quantity = st2.getQuantity();
						st2.setQuantity(quantity-1);								
						dbService.getStockService().UpdateQuantity(st2);	
						
						int Usedquantity = st2.getUsedQuantity();
						st2.setUsedQuantity(Usedquantity+1);
						dbService.getStockService().UpdateUsedQuantity(st2);
					}
					else
					{
						//try to find the stock with unused serial no
						Stock st3 = dbService.getStockService().FindUnUsedStockFreeFromParamPref(networkid, nodeid, category, pp.getCardType());
						if(st3!=null)
						{
							logger.info("Unused stock with Serial No, free from Param Pref found in stock "+st3.toString());
							eqInfo[0]= st3.getCardType();
							eqInfo[1]=st3.getSerialNo();
							int quantity = st3.getQuantity();
							st3.setQuantity(quantity-1);
							dbService.getStockService().UpdateQuantity(st3);
							
							int Usedquantity = st3.getUsedQuantity();
							st3.setUsedQuantity(Usedquantity+1);
							dbService.getStockService().UpdateUsedQuantity(st3);
							
							st3.setStatus("Used");
							dbService.getStockService().UpdateStatus(st3);														
						}
						else//no quantity exists in stock
						{
							logger.info("parametrically preferred equipment not found available in stock, procurement needed");					
							eqInfo[0]= pp.getCardType();	
							eqInfo[1]="";
						}
						
					}					
				}			
			}
			else// ParametricPreference doesnot exist
			{
				logger.info(" parametric preference doesnot exist, Finding Preferred Existing Stock ");
				String cardtype = dbService.getStockService().FindPreferredExistingStock(networkid, nodeid, category);				
				if(cardtype!=null)
				{
					eqInfo[0]= cardtype;
					
					//try to find stock without serial no 					
					Stock st = dbService.getStockService().FindUnUsedStockWithoutSrNo(networkid, nodeid, category, cardtype);
					if(st!=null)
					{
						logger.info(" Getting unused stock with out sr no, Stock found is: " + st.toString());	
						eqInfo[0]= st.getCardType();
						eqInfo[1]="";
						
						int quantity = st.getQuantity();
						st.setQuantity(quantity-1);	
						
						dbService.getStockService().UpdateQuantity(st);
						
						int Usedquantity = st.getUsedQuantity();
						st.setUsedQuantity(Usedquantity+1);
						dbService.getStockService().UpdateUsedQuantity(st);
					}
					else//find stock with sr no but check any parametric preference does not exist for this particular sr no
					{						
						//try to find the stock with unused serial no
						Stock st1 = dbService.getStockService().FindUnUsedStockFreeFromParamPref(networkid, nodeid, category, cardtype);
						if(st1!=null)
						{
							logger.info("Unused stock with Serial No, free from Param Pref found in stock "+st1.toString());
							eqInfo[0]= st1.getCardType();
							eqInfo[1]=st1.getSerialNo();
							int quantity = st1.getQuantity();
							st1.setQuantity(quantity-1);
							dbService.getStockService().UpdateQuantity(st1);
							
							int Usedquantity = st1.getUsedQuantity();
							st1.setUsedQuantity(Usedquantity+1);
							dbService.getStockService().UpdateUsedQuantity(st1);
							
							st1.setStatus("Used");
							dbService.getStockService().UpdateStatus(st1);														
						}
						else//no quantity exists in stock
						{
							logger.info("No Stock found, Fetching preference for allocation ");
							EquipmentPreference ep = dbService.getEquipmentPreferenceService().FindPreferredEq(networkid, nodeid, category);
							eqInfo[0]= ep.getCardType();	
							eqInfo[1]="";
						}
						
					}										
				}
				else//no quantity exists in stock
				{
					logger.info("No Stock found, Fetching preference for allocation ");
					EquipmentPreference ep = dbService.getEquipmentPreferenceService().FindPreferredEq(networkid, nodeid, category);
					eqInfo[0]= ep.getCardType();	
					eqInfo[1]="";
				}
					
			}
			
			logger.info("fGetPreferredEqType, allocated "+" NetworkId: "+networkid+" NodeId: "+nodeid+" Cardtype: "+eqInfo[0].toString()+" Card Serial No: "+eqInfo[1]);
		
		return eqInfo;		
	}
	
//	public void InitializeEqPreferenceDb(int networkid) throws SQLException
//	{
//		 List <Integer> nodelist=rpu.fgetNodesToConfigure(networkid);
//		 		 
//		 for (int i = 0; i < nodelist.size(); i++) 
//		 {	
//			 int nodeid =nodelist.get(i);			
//			 int nodetype=dbService.getNodeService().FindNode(networkid, nodeid).getNodeType();
//			 int nodedegree=dbService.getNodeService().FindNode(networkid, nodeid).getDegree();
//			 switch(nodetype)      
//			 {
//				 case MapConstants.roadm:
//				 {
//					 for (int j = 0; j < ResourcePlanConstants.CatMpn100GList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpn100G, ResourcePlanConstants.CatMpn100GList[j], j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }					 
//					 for (int j = 0; j < ResourcePlanConstants.CatTpn10GList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatTpn10G, ResourcePlanConstants.CatTpn10GList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }					 
//					 for (int j = 0; j < ResourcePlanConstants.CatYCableList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatYCable, ResourcePlanConstants.CatYCableList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }	
//					 for (int j = 0; j < ResourcePlanConstants.CatCsccList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatCscc, ResourcePlanConstants.CatCsccList[j],  j+1,  ResourcePlanConstants.Yes);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }	
//					 for (int j = 0; j < ResourcePlanConstants.CatMpcList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpc, ResourcePlanConstants.CatMpcList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }	
//					 
//					 for (int j = 0; j < ResourcePlanConstants.CatSfpEthernetList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpEthernet, ResourcePlanConstants.CatSfpEthernetList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }
//					 
//					 for (int j = 0; j < ResourcePlanConstants.CatSfpNonEthernetList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpNonEthernet, ResourcePlanConstants.CatSfpNonEthernetList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }
//					 
//					 for (int j = 0; j < ResourcePlanConstants.CatDegreeList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatDegree, ResourcePlanConstants.CatDegreeList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }
//					 
//					 EquipmentPreference ep1 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x9,  ResourcePlanConstants.PrefOne,  ResourcePlanConstants.No);
//					 dbService.getEquipmentPreferenceService().Insert(ep1);						 
//					 EquipmentPreference ep2 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x2x20,  ResourcePlanConstants.PrefTwo,  ResourcePlanConstants.No);
//					 dbService.getEquipmentPreferenceService().Insert(ep2);					 					 
//					 
//				 }break;
//				 case MapConstants.te:
//				 case MapConstants.twoBselectRoadm:
//				 case MapConstants.hub:
//				 {
//					 for (int j = 0; j < ResourcePlanConstants.CatMpn100GList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpn100G, ResourcePlanConstants.CatMpn100GList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }					 
//					 for (int j = 0; j < ResourcePlanConstants.CatTpn10GList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatTpn10G, ResourcePlanConstants.CatTpn10GList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }					 
//					 for (int j = 0; j < ResourcePlanConstants.CatYCableList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatYCable, ResourcePlanConstants.CatYCableList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }
//					 for (int j = 0; j < ResourcePlanConstants.CatCsccList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatCscc, ResourcePlanConstants.CatCsccList[j],  j+1,  ResourcePlanConstants.Yes);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }	
//					 for (int j = 0; j < ResourcePlanConstants.CatMpcList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpc, ResourcePlanConstants.CatMpcList[j],  j+1,  ResourcePlanConstants.Yes);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }	
//					 for (int j = 0; j < ResourcePlanConstants.CatSfpEthernetList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpEthernet, ResourcePlanConstants.CatSfpEthernetList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }
//					 
//					 for (int j = 0; j < ResourcePlanConstants.CatSfpNonEthernetList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpNonEthernet, ResourcePlanConstants.CatSfpNonEthernetList[j],  j+1,  ResourcePlanConstants.No);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }
//					 EquipmentPreference ep1 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x2,  ResourcePlanConstants.PrefOne,  ResourcePlanConstants.No);
//					 dbService.getEquipmentPreferenceService().Insert(ep1);	
//					 EquipmentPreference ep2 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x9,  ResourcePlanConstants.PrefTwo,  ResourcePlanConstants.No);
//					 dbService.getEquipmentPreferenceService().Insert(ep2);
//					 EquipmentPreference ep3 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x2x20,  ResourcePlanConstants.PrefThree,  ResourcePlanConstants.No);
//					 dbService.getEquipmentPreferenceService().Insert(ep3);		
//					 					 
//				 }break;	
//				 case MapConstants.ila:
//				 {
//					 for (int j = 0; j < ResourcePlanConstants.CatCsccList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatCscc, ResourcePlanConstants.CatCsccList[j],  j+1,  ResourcePlanConstants.Yes);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }	
//					 for (int j = 0; j < ResourcePlanConstants.CatMpcList.length; j++)
//					 {
//						 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpc, ResourcePlanConstants.CatMpcList[j],  j+1,  ResourcePlanConstants.Yes);
//						 dbService.getEquipmentPreferenceService().Insert(ep);									
//					 }						 
//				 }break;
//			 }
//			 
//		 }
//	}
	
	public void InitializeEqPreferenceDb(int networkid) throws SQLException
	{
		 List <Integer> nodelist=rpu.fgetNodesToConfigure(networkid);
		 		 
		 for (int i = 0; i < nodelist.size(); i++) 
		 {	
			 int nodeid =nodelist.get(i);			
			 int nodetype=dbService.getNodeService().FindNode(networkid, nodeid).getNodeType();
			 int nodedegree=dbService.getNodeService().FindNode(networkid, nodeid).getDegree();
			 fInsertEquipmentPreferencesDb(networkid,nodeid,nodetype);			 
		 }
	}
	
	public void fInsertEquipmentPreferencesDb(int networkid,int nodeid,int nodetype)
	{
		
		 /*** Common to all node types */
		 
		/* Chassis type */
		 for (int j = 0; j < ResourcePlanConstants.CatChassisTypeList.length; j++)
		 {
			 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatChassisType, ResourcePlanConstants.CatChassisTypeList[j],  j+1,  ResourcePlanConstants.No);
			 try {
				dbService.getEquipmentPreferenceService().Insert(ep);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}									
		 }
		 
		 for (int j = 0; j < ResourcePlanConstants.CatOtdrList.length; j++)
		 {
			 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatOtdr, ResourcePlanConstants.CatOtdrList[j],  j+1,  ResourcePlanConstants.No);
			 try {
				dbService.getEquipmentPreferenceService().Insert(ep);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}									
		 }
		 
		 for (int j = 0; j < ResourcePlanConstants.CatOscList.length; j++)
		 {
			 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatOsc, ResourcePlanConstants.CatOscList[j],  j+1,  ResourcePlanConstants.No);
			 try {
				dbService.getEquipmentPreferenceService().Insert(ep);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}									
		 }
		 
		 for (int j = 0; j < ResourcePlanConstants.CatOpmList.length; j++)
		 {
			 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatOpm, ResourcePlanConstants.CatOpmList[j],  j+1,  ResourcePlanConstants.No);
			 try {
				dbService.getEquipmentPreferenceService().Insert(ep);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}									
		 }
		 
		 /*** Based on node type */
		switch(nodetype)      
		 {
			 case MapConstants.roadm:
			 {
				 for (int j = 0; j < ResourcePlanConstants.CatMpn100GList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpn100G, ResourcePlanConstants.CatMpn100GList[j], j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 // OPX card added in pref category - CGX and CGM both included in this category
				 for (int j = 0; j < ResourcePlanConstants.CatMpn100GOPXList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpn100GOPX, ResourcePlanConstants.CatMpn100GOPXList[j], j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 for (int j = 0; j < ResourcePlanConstants.CatTpn10GList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatTpn10G, ResourcePlanConstants.CatTpn10GList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }					 
				 for (int j = 0; j < ResourcePlanConstants.CatYCableList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatYCable, ResourcePlanConstants.CatYCableList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 for (int j = 0; j < ResourcePlanConstants.CatCsccList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatCscc, ResourcePlanConstants.CatCsccList[j],  j+1,  ResourcePlanConstants.Yes);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 for (int j = 0; j < ResourcePlanConstants.CatMpcList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpc, ResourcePlanConstants.CatMpcList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 
				 for (int j = 0; j < ResourcePlanConstants.CatSfpEthernetList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpEthernet, ResourcePlanConstants.CatSfpEthernetList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 
				 for (int j = 0; j < ResourcePlanConstants.CatSfpNonEthernetList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpNonEthernet, ResourcePlanConstants.CatSfpNonEthernetList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 
				 for (int j = 0; j < ResourcePlanConstants.CatDegreeList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatDegree, ResourcePlanConstants.CatDegreeList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 
				 EquipmentPreference ep1 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x9,  ResourcePlanConstants.PrefOne,  ResourcePlanConstants.No);
				 try {
					dbService.getEquipmentPreferenceService().Insert(ep1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}						 
				 EquipmentPreference ep2 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x2x20,  ResourcePlanConstants.PrefTwo,  ResourcePlanConstants.No);
				 try {
					dbService.getEquipmentPreferenceService().Insert(ep2);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				 
				 for (int j = 0; j < ResourcePlanConstants.CatAmplifierList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatAmplifier, ResourcePlanConstants.CatAmplifierList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 
			 }break;
			 case MapConstants.te:
			 case MapConstants.twoBselectRoadm:
			 case MapConstants.hub:
			 {
				 for (int j = 0; j < ResourcePlanConstants.CatMpn100GList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpn100G, ResourcePlanConstants.CatMpn100GList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }					 
				 for (int j = 0; j < ResourcePlanConstants.CatTpn10GList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatTpn10G, ResourcePlanConstants.CatTpn10GList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }		
				 
				// OPX card added in pref category - CGX and CGM both included in this category
				 for (int j = 0; j < ResourcePlanConstants.CatMpn100GOPXList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpn100GOPX, ResourcePlanConstants.CatMpn100GOPXList[j], j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 
				 for (int j = 0; j < ResourcePlanConstants.CatYCableList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatYCable, ResourcePlanConstants.CatYCableList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 for (int j = 0; j < ResourcePlanConstants.CatCsccList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatCscc, ResourcePlanConstants.CatCsccList[j],  j+1,  ResourcePlanConstants.Yes);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 for (int j = 0; j < ResourcePlanConstants.CatMpcList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpc, ResourcePlanConstants.CatMpcList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 for (int j = 0; j < ResourcePlanConstants.CatSfpEthernetList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpEthernet, ResourcePlanConstants.CatSfpEthernetList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 
				 for (int j = 0; j < ResourcePlanConstants.CatSfpNonEthernetList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpNonEthernet, ResourcePlanConstants.CatSfpNonEthernetList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 EquipmentPreference ep1 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x2,  ResourcePlanConstants.PrefOne,  ResourcePlanConstants.No);
				 try {
					dbService.getEquipmentPreferenceService().Insert(ep1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				 EquipmentPreference ep2 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x9,  ResourcePlanConstants.PrefTwo,  ResourcePlanConstants.No);
				 try {
					dbService.getEquipmentPreferenceService().Insert(ep2);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 EquipmentPreference ep3 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x2x20,  ResourcePlanConstants.PrefThree,  ResourcePlanConstants.No);
				 try {
					dbService.getEquipmentPreferenceService().Insert(ep3);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				 
				 for (int j = 0; j < ResourcePlanConstants.CatAmplifierList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatAmplifier, ResourcePlanConstants.CatAmplifierList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 					 
			 }break;	
			 case MapConstants.ila:
			 {
				 for (int j = 0; j < ResourcePlanConstants.CatCsccList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatCscc, ResourcePlanConstants.CatCsccList[j],  j+1,  ResourcePlanConstants.Yes);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 for (int j = 0; j < ResourcePlanConstants.CatMpcList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpc, ResourcePlanConstants.CatMpcList[j],  j+1,  ResourcePlanConstants.Yes);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }						 
			 }break;
			 case MapConstants.cdRoadm:
			 {
				 for (int j = 0; j < ResourcePlanConstants.CatMpn100GList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpn100G, ResourcePlanConstants.CatMpn100GList[j], j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 // OPX card added in pref category - CGX and CGM both included in this category
				 for (int j = 0; j < ResourcePlanConstants.CatMpn100GOPXList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpn100GOPX, ResourcePlanConstants.CatMpn100GOPXList[j], j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 for (int j = 0; j < ResourcePlanConstants.CatTpn10GList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatTpn10G, ResourcePlanConstants.CatTpn10GList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }					 
				 for (int j = 0; j < ResourcePlanConstants.CatYCableList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatYCable, ResourcePlanConstants.CatYCableList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 for (int j = 0; j < ResourcePlanConstants.CatCsccList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatCscc, ResourcePlanConstants.CatCsccList[j],  j+1,  ResourcePlanConstants.Yes);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 for (int j = 0; j < ResourcePlanConstants.CatMpcList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatMpc, ResourcePlanConstants.CatMpcList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }	
				 
				 for (int j = 0; j < ResourcePlanConstants.CatSfpEthernetList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpEthernet, ResourcePlanConstants.CatSfpEthernetList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 
				 for (int j = 0; j < ResourcePlanConstants.CatSfpNonEthernetList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatSfpNonEthernet, ResourcePlanConstants.CatSfpNonEthernetList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 
				 for (int j = 0; j < ResourcePlanConstants.CatDegreeList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatDegree, ResourcePlanConstants.CatDegreeList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 
				 EquipmentPreference ep1 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x9,  ResourcePlanConstants.PrefOne,  ResourcePlanConstants.No);
				 try {
					dbService.getEquipmentPreferenceService().Insert(ep1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}						 
				 EquipmentPreference ep2 = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatWss, ResourcePlanConstants.CardWss1x2x20,  ResourcePlanConstants.PrefTwo,  ResourcePlanConstants.No);
				 try {
					dbService.getEquipmentPreferenceService().Insert(ep2);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				 
				 for (int j = 0; j < ResourcePlanConstants.CatAmplifierList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatAmplifier, ResourcePlanConstants.CatAmplifierList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
				 
				 for (int j = 0; j < ResourcePlanConstants.CatAddDropWssCdRoadmList.length; j++)
				 {
					 EquipmentPreference ep = new EquipmentPreference(networkid, nodeid, ResourcePlanConstants.CatAddDropWssCdRoadm, ResourcePlanConstants.CatAddDropWssCdRoadmList[j],  j+1,  ResourcePlanConstants.No);
					 try {
						dbService.getEquipmentPreferenceService().Insert(ep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				 }
			 }break;
		 }
		
	}
	
	public String fgetRedundancyReq(int networkid, int nodeid, String category)
	{
		String rd = dbService.getEquipmentPreferenceService().FindRedundancy(networkid, nodeid, category);
		return rd;		
	}
	
	/**
	 * This function will mark the places as reserve in the CardInfo tables
	 * @param networkid
	 * @throws SQLException
	 */
	public void fhandleAllocationExceptions(int networkid) throws SQLException
	{
		List <Integer> nodelist=rpu.fgetNodesToConfigure(networkid);		
		 for (int i = 0; i < nodelist.size(); i++) 
		 {	
			 int nodeid =nodelist.get(i);
			 List<AllocationExceptions> ae = dbService.getAllocationExceptionsService().FindExceptions(networkid, nodeid, ResourcePlanConstants.ExceptionTypeResrv);
				for (int j = 0; j < ae.size(); j++)
				{
					int[] id = rpu.locationIds(ae.get(j).getLocation());
					if(rpu.fCheckSlotAvailabilityDb(networkid, nodeid, id[0], id[1], id[2]))
					rpu.fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardReserved, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", 0,"");			
				}
			 
		 }
			
		
	}
	
	
	/**
	 * This function will mark the places as reserve in the CardInfo tables
	 * @param networkid
	 * @throws SQLException
	 */
	public void fhandleAllocationExceptionsBrField(int networkid) throws SQLException
	{
		System.out.println("fhandleAllocationExceptionsBrField ----- "+networkid);
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		List <Integer> nodelist=rpu.fgetNodesToConfigure(networkidBF);		
		for (int i = 0; i < nodelist.size(); i++) 
		{	
			int nodeid =nodelist.get(i);
			List<AllocationExceptions> deletedAE = dbService.getAllocationExceptionsService().FindDeletedExceptionsBrField(networkid, networkidBF, nodeid);
			System.out.println("deletedAE size"+deletedAE.size());
			for (int j = 0; j < deletedAE.size(); j++)
			{
				int[] id = rpu.locationIds(deletedAE.get(j).getLocation());
				System.out.println("deletedAE Location:"+id[0]+"-"+id[1]+"-"+id[2]);
				if(deletedAE.get(j).getType().equals(ResourcePlanConstants.ExceptionTypeResrv) && !rpu.fCheckSlotAvailabilityDb(networkidBF, nodeid, id[0], id[1], id[2]))
					dbService.getCardInfoService().DeleteCard(networkidBF, nodeid,  id[0], id[1], id[2]);			
			}

			List<AllocationExceptions> newAE = dbService.getAllocationExceptionsService().FindAddedExceptionsBrField(networkid, networkidBF, nodeid);
			System.out.println("newAE size"+newAE.size());
			for (int j = 0; j < newAE.size(); j++)
			{
				int[] id = rpu.locationIds(newAE.get(j).getLocation());
				System.out.println("newAE Location:"+id[0]+"-"+id[1]+"-"+id[2]);
				if(newAE.get(j).getType().equals(ResourcePlanConstants.ExceptionTypeResrv) && rpu.fCheckSlotAvailabilityDb(networkidBF, nodeid, id[0], id[1], id[2]))
					rpu.fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardReserved, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", 0,"");			
			}	

//			List<AllocationExceptions> ae = dbService.getAllocationExceptionsService().FindExceptions(networkid, nodeid, ResourcePlanConstants.ExceptionTypeResrv);
//			for (int j = 0; j < ae.size(); j++)
//			{
//				int[] id = rpu.locationIds(ae.get(j).getLocation());
//				if(rpu.fCheckSlotAvailabilityDb(networkid, nodeid, id[0], id[1], id[2]))
//					rpu.fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardReserved, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", 0,"");			
//			}	

		}


	}

}
