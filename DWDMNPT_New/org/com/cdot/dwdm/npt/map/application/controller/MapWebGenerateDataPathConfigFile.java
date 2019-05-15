package application.controller;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import application.MainMap;
import application.constants.DataPathConfigFileConstants;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.AmplifierConfig;
import application.model.CardInfo;
import application.model.EquipmentPreference;
import application.model.LambdaLspInformation;
import application.model.Link;
import application.model.McsMap;
import application.model.McsPortMapping;
import application.model.NetworkRoute;
import application.model.Node;
import application.model.OcmConfig;
import application.model.OtnLspInformation;
import application.model.PortInfo;
import application.model.PtcClientProtInfo;
import application.model.PtcLineProtInfo;
import application.model.RouteMapping;
import application.model.Topology;
import application.model.TpnPortInfo;
import application.model.VoaConfigInfo;
import application.model.WssDirectionConfig;
import application.model.WssMap;
import application.service.DbService;
import application.service.IPv4;
import application.service.TAR;



/**
 * @author hp 
 * @brief  This controller deals with Generation of Data Path Config File as per the given format
 * @date   19th Jun,2017
 */
public class MapWebGenerateDataPathConfigFile {
	
	
	Set<Integer> NodeSetForNetworkInfo = new LinkedHashSet<Integer>(); /**Global LinkedHashSet for Network Info Tag*/	
	String currentNetworkType="";
	int greenFieldNetworkId;
	int brownFieldNetworkId;
	int [] enabledDirArrayForEdfa = new int[MapConstants.I_EIGHT];
	public static Logger DataPathLogger = Logger.getLogger(MapWebGenerateDataPathConfigFile.class.getName());/**Logger for the Class*/
	
	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateTELinkTagDataPathFileXML(int nodeId, int networkId, DbService dbService, Document doc, Element rootElement){
		
		/**/
		/**TELink Tag Start*/
		{			
			
			/** Initiate DB service for per node link data*/
			List<Map<String,Object>> teLinkListObj   = dbService.getLinkService().PerNodeLinkData_cf(networkId, nodeId);
			
			// DataPathDetails->TELink
			Element TELinkDetails  = doc.createElement("TELinkDetails");
			rootElement.appendChild(TELinkDetails);		
			
			System.out.println(" Node Id - > teLinkListObj " + nodeId+ " -> "+teLinkListObj);
				
			
			for(int i=0; i<teLinkListObj.size(); i++)
			/**DirectionDetails Tag Start : Direction Times*/		
			{
				// DataPathDetails->TELink->DirectionDetails
				Element DirectionDetails  = doc.createElement("DirectionDetails");
				TELinkDetails.appendChild(DirectionDetails);
				
					
				//DataPathDetails->TELink->DirectionDetails->TYPE			

				Map<String, Object> listToSearchFor = new HashMap<>();
				Link objToPush = new Link();
				objToPush.setLinkId((int)teLinkListObj.get(i).get("LinkId"));				
				listToSearchFor.put("teLinkPerDirectionList", objToPush);

				Element TYPE  = doc.createElement("TYPE");
				TYPE.appendChild(doc.createTextNode(String.valueOf(
													injectTagFor(DataPathConfigFileConstants.InjectTagForDirectionDetails,
													listToSearchFor, nodeId, dbService)
													))); ///String.valueOf("0")));
				DirectionDetails.appendChild(TYPE);	
				
				{
				/**DirectionDetails Tag Common Field Start*/	
					// DataPathDetails->TELink->DirectionDetails->Direction
					Element Direction  = doc.createElement("Direction");
					Direction.appendChild( doc.createTextNode(DataPathConfigFileConstants.directionConstantsHashMap.get(
										teLinkListObj.get(i).get("Direction").toString()).toString()));
					DirectionDetails.appendChild(Direction);
					
					// DataPathDetails->TELink->DirectionDetails->SRLG
					Element SRLG = doc.createElement("SRLG");
					SRLG.appendChild( doc.createTextNode(teLinkListObj.get(i).get("SrlgId").toString()));
					DirectionDetails.appendChild(SRLG);
					
					// DataPathDetails->TELink->DirectionDetails->TEM
					Element TEM = doc.createElement("TEM");
					TEM.appendChild( doc.createTextNode(teLinkListObj.get(i).get("MetricCost").toString()));
					DirectionDetails.appendChild(TEM);
					
					// DataPathDetails->TELink->DirectionDetails->Colour
					Element Colour = doc.createElement("Colour");
					Colour.appendChild( doc.createTextNode(teLinkListObj.get(i).get("Colour").toString()));
					DirectionDetails.appendChild(Colour);
					
					// DataPathDetails->TELink->DirectionDetails->LinkProtectionType
					Element LinkProtectionType  = doc.createElement("LinkProtectionType");
					LinkProtectionType.appendChild( doc.createTextNode("0")); /** DBG => For non-mesh topologies 
													[1+1 (=1), 1: N (=2), 1:1 (=3) or none (=0)] its require else 0 */
					DirectionDetails.appendChild(LinkProtectionType);
					
					// DataPathDetails->TELink->DirectionDetails->AttenuationMode
					Element AttenuationMode  = doc.createElement("AttenuationMode");
					AttenuationMode.appendChild( doc.createTextNode(String.valueOf(DataPathConfigFileConstants.AttenuationConfigMode_Manual))); 
					DirectionDetails.appendChild(AttenuationMode);
				/**DirectionDetails Tag Common Field End*/
					
					 int incrementalCounter=0;
					/**DataLink Tag Field Start : Lambda Times*/
					 
					 /** Initiate DB service for per node link data*/
					 List<Map<String,Object>> teLambdaPerLinkListObj   = dbService.getLinkService().PerLinkWavelengths_cf
							 												(networkId, nodeId, Integer.parseInt(teLinkListObj.get(i).get("LinkId").toString()));
					 
					 
					 System.out.println( " teLambdaPerLinkListObj "+ nodeId +" -> " +Integer.parseInt(teLinkListObj.get(i).get("LinkId").toString()) +" -> "+ teLambdaPerLinkListObj);
					for(int j=0; j<teLambdaPerLinkListObj.size(); j++)
					{
						
					if(teLambdaPerLinkListObj.get(j).get("Wavelength")!=null){	
						incrementalCounter++;

						int lambdaId 	= (int) teLambdaPerLinkListObj.get(j).get("Wavelength"); /**Wavelength Id*/
						int srcNodeDirectionId =   (int) DataPathConfigFileConstants.directionConstantsHashMap.get(teLambdaPerLinkListObj.get(j).get("LocalNodeDirection")); /** Source Node Direction Id*/
						int dstNodeDirectionId =   (int) DataPathConfigFileConstants.directionConstantsHashMap.get(teLambdaPerLinkListObj.get(j).get("RemoteNodeDirection")); /** Destination Node Direction Id*/
						String minLspBandWidth="null", maxLspBandwidth="null";
						
						if(teLambdaPerLinkListObj.get(j).get("LineRate").equals(
								DataPathConfigFileConstants.LineRate_100G)) {/**100 G Linerate*/
							
							maxLspBandwidth = DataPathConfigFileConstants.LineRate_100G;
							
							/**Traffic is in multiple of 10G*/
							if(((int)Float.parseFloat(teLambdaPerLinkListObj.get(j).get("Traffic").toString()) %
									Integer.parseInt(DataPathConfigFileConstants.Traffic_10G)) 
									== MapConstants.I_ZERO) {
								
								minLspBandWidth = DataPathConfigFileConstants.Traffic_10G;
							}
						}
						
						else if(teLambdaPerLinkListObj.get(j).get("LineRate").equals(
								DataPathConfigFileConstants.LineRate_10G)) {/**10 G Linerate*/							
							
							maxLspBandwidth = DataPathConfigFileConstants.LineRate_10G;
							minLspBandWidth = DataPathConfigFileConstants.Traffic_1G;
							
						}
						
						else if(teLambdaPerLinkListObj.get(j).get("LineRate").equals(
								DataPathConfigFileConstants.LineRate_200G)) {/**200 G Linerate*/							
							
							maxLspBandwidth = DataPathConfigFileConstants.LineRate_200G;
							minLspBandWidth = DataPathConfigFileConstants.Traffic_10G;
							
						}
						
						
						
						
						System.out.println("lambdaId, srcNodeDirectionId, dstNodeDirectionId  : "+ lambdaId+", "+ srcNodeDirectionId+", "+dstNodeDirectionId);

						/*int finalValue = ((bytes[0] & 0xff) << 24) | ((bytes[1] & 0xff) << 16) |
						          ((bytes[2] & 0xff) << 8)  | (bytes[3] & 0xff);
						 */
						String localTeLinkIdValue 		= (((( srcNodeDirectionId & 0xff) << 24) | (incrementalCounter & 0xff)))+"";
						String remoteTeLinkIdValue 		= (((( dstNodeDirectionId & 0xff) << 24) | (incrementalCounter & 0xff)))+"";
						String localInterfaceIdValue 	= (((( srcNodeDirectionId & 0xff) << 8) | (lambdaId & 0xff)))+"";
						String remoteInterfaceIdValue 	= (((( dstNodeDirectionId & 0xff) << 8) | (lambdaId & 0xff)))+"";
						
						System.out.println(localTeLinkIdValue + " & "+ remoteTeLinkIdValue+ " & "+ localInterfaceIdValue + " & "+ remoteInterfaceIdValue);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink
						Element DataLink  = doc.createElement("DataLink");
						DirectionDetails.appendChild(DataLink);
					
				
						// DataPathDetails->TELink->DirectionDetails->DataLink->Wavelength 
						Element Wavelength = doc.createElement("Wavelength");
						Wavelength.appendChild( doc.createTextNode(teLambdaPerLinkListObj.get(j).get("Wavelength").toString()));
						DataLink.appendChild(Wavelength);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->TELinkId 
						Element TELinkId = doc.createElement("TELinkId");
						TELinkId.appendChild( doc.createTextNode(localTeLinkIdValue));
						DataLink.appendChild(TELinkId);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->RemoteTELinkId 
						Element RemoteTELinkId = doc.createElement("RemoteTELinkId");
						RemoteTELinkId.appendChild( doc.createTextNode(remoteTeLinkIdValue));
						DataLink.appendChild(RemoteTELinkId);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->TypeofInterface 
						Element TypeofInterface = doc.createElement("TypeofInterface");
						String pathForInterface = dbService.getLinkWavelengthInfoService().FindPathForlinkWavelength(networkId, 
								Integer.parseInt(teLambdaPerLinkListObj.get(j).get("LinkId").toString()), Integer.parseInt(teLambdaPerLinkListObj.get(j).get("DemandId").toString()))
						.getPath();
						/**Check for the Port (Add/Drop) or Pass through*/
						String[] splittedPathForInterface = pathForInterface.split(",");
						String  TypeOfInterFaceStr="";
						int pathNodeCount=0;
						for(String tempPath : pathForInterface.split(",")){
							pathNodeCount++;
						}
						if(splittedPathForInterface[0].equalsIgnoreCase(String.valueOf(nodeId)) ||
								  splittedPathForInterface[pathNodeCount-1].equalsIgnoreCase(String.valueOf(nodeId))){/**Add/Drop*/
							TypeOfInterFaceStr="0";
						}
						else{/**Pass through*/
							TypeOfInterFaceStr="1";
						}
						TypeofInterface.appendChild( doc.createTextNode(TypeOfInterFaceStr)); /**(port (=0) /component (=1) 
																													(multiplex capable: ports being add/drop or TDM))*/
						DataLink.appendChild(TypeofInterface);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->LocalInterfaceId 
						Element LocalInterfaceId = doc.createElement("LocalInterfaceId");
						LocalInterfaceId.appendChild( doc.createTextNode(localInterfaceIdValue));
						DataLink.appendChild(LocalInterfaceId);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->RemoteInterfaceId 
						Element RemoteInterfaceId = doc.createElement("RemoteInterfaceId");
						RemoteInterfaceId.appendChild( doc.createTextNode(remoteInterfaceIdValue));
						DataLink.appendChild(RemoteInterfaceId);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->LinkType 
						Element LinkType = doc.createElement("LinkType"); /** only PPP in our system */
						LinkType.appendChild( doc.createTextNode("0"));
						DataLink.appendChild(LinkType);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->InterfaceSwitchType 
						Element InterfaceSwitchType = doc.createElement("InterfaceSwitchType");
						InterfaceSwitchType.appendChild( doc.createTextNode(DataPathConfigFileConstants.SwitchingType)); /** Lambda switching type (port on ROADM) (=150)/ TDM (POTP)
						 																												(=100) */
						DataLink.appendChild(InterfaceSwitchType);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->InterfaceEncodingType 
						Element InterfaceEncodingType = doc.createElement("InterfaceEncodingType");
						InterfaceEncodingType.appendChild( doc.createTextNode(DataPathConfigFileConstants.InterfaceEncodingType));
						DataLink.appendChild(InterfaceEncodingType);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->MinLSPBandwidth 
						Element MinLSPBandwidth = doc.createElement("MinLSPBandwidth");
						MinLSPBandwidth.appendChild( doc.createTextNode(/*teLambdaPerLinkListObj.get(j).get("Traffic").toString()*/
																		minLspBandWidth)); /** bandwidth used by each client */
						DataLink.appendChild(MinLSPBandwidth);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->MaxLSPBandwidth 
						Element MaxLSPBandwidth = doc.createElement("MaxLSPBandwidth");
						MaxLSPBandwidth.appendChild( doc.createTextNode(/*teLambdaPerLinkListObj.get(j).get("LineRate").toString()*/
								maxLspBandwidth)); /** bandwidth of this wavelength */
						DataLink.appendChild(MaxLSPBandwidth);
						
						/** DBG => Following two params need to be check later */
						// DataPathDetails->TELink->DirectionDetails->DataLink->MaxReservableBandwidth 
						Element MaxReservableBandwidth = doc.createElement("MaxReservableBandwidth");
						MaxReservableBandwidth.appendChild( doc.createTextNode(teLambdaPerLinkListObj.get(j).get("LineRate").toString())); /** if TE link of single 100G λ it is 100G 
																														and in case of multiple link its summation */
						DataLink.appendChild(MaxReservableBandwidth);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->MaxBandwidth 
						Element MaxBandwidth = doc.createElement("MaxBandwidth");
						MaxBandwidth.appendChild( doc.createTextNode(teLambdaPerLinkListObj.get(j).get("LineRate").toString())); /** if TE link of single 100G λ it is 100G 
																									and in case of multiple link its summation */
						DataLink.appendChild(MaxBandwidth);
						
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->AttenuationTx 
						Element AttenuationTx = doc.createElement("AttenuationTx");
						AttenuationTx.appendChild( doc.createTextNode(String.valueOf(DataPathConfigFileConstants.TX_CM_MANUAL_VARIABLE)));
						DataLink.appendChild(AttenuationTx);
						
						// DataPathDetails->TELink->DirectionDetails->DataLink->AttenuationRx 
						Element AttenuationRx = doc.createElement("AttenuationRx");
						AttenuationRx.appendChild( doc.createTextNode(String.valueOf(DataPathConfigFileConstants.RX_CM_MANUAL_VARIABLE)));
						DataLink.appendChild(AttenuationRx);
					
					}
					/**DataLink Tag Field End*/		
					}
				} 
			}		
			/**DirectionDetails Tag End*/
		}		
		/**TELink Tag End*/
	/*	catch(Exception e){
			System.out.println(" TELink Exception "+ e);
		}*/
		
	}


	/**
	 * API to Find LambdaLSP Entry from the Database if exist or else the New Entry
	 * @param rowPathListObj
	 * @param dbService
	 * @param networkId
	 * @author hp
	 * @date 31st May, 2018
	 */
	public LambdaLspInformation  GetLambdaLspInformation(int networkId,  Map<String, Object> rowPathListObj,  DbService dbService){

		LambdaLspInformation lambdaLspInformationObj = null;

		try{
			
			if(currentNetworkType.equalsIgnoreCase(MapConstants.GreenField)){
					/**create the new entry*/
					lambdaLspInformationObj = new LambdaLspInformation( networkId,
																	   (int)rowPathListObj.get("DemandId"),
																	   (int)rowPathListObj.get("RoutePriority"),
																	   (String)rowPathListObj.get("Path"),
																	   DataPathConfigFileConstants.tunnelIdGlobal,
																	   (int)rowPathListObj.get("LspId"),
																	   (int)rowPathListObj.get("ForwardingAdj")
																	   );					
																		 
			}
			else if(currentNetworkType.equalsIgnoreCase(MapConstants.BrownField)){
					
				try{
						/**find entry from the common api / DB entry*/
						lambdaLspInformationObj = dbService.getLambdaLspInformationSerivce().FindLsp(networkId,
						(int)rowPathListObj.get("DemandId"),
						///(int)rowPathListObj.get("RoutePriority"),
						(String)rowPathListObj.get("Path"));
						DataPathLogger.info("lambdaLspInformationObj: "+lambdaLspInformationObj.toString());	
				}
				catch (Exception e) {/**Entry not found from DB, so generate the new one*/


					/** insert the new entry */
				
					/**find the max tunnel id from db and create the obj */										
					int lambdaLspTunnelId = 	dbService.getLambdaLspInformationSerivce().FindMaxTunnelIdLambdaLspInformation
															(networkId, greenFieldNetworkId, (int)rowPathListObj.get("DemandId"));
					
					/**find the max forwarding adj from db and create the obj */										
					int ForwardingAdj = 	dbService.getLambdaLspInformationSerivce().FindMaxForwardingAdjLambdaLspInformation
															(networkId, greenFieldNetworkId);

					DataPathLogger.info("lambdaLspTunnelId: "+lambdaLspTunnelId +" and ForwardingAdj: "+ForwardingAdj);	

					lambdaLspInformationObj = new LambdaLspInformation(networkId,
																(int)rowPathListObj.get("DemandId"),
																(int)rowPathListObj.get("RoutePriority"), 
																(String) rowPathListObj.get("Path"), 															
																lambdaLspTunnelId,
																(int)rowPathListObj.get("LspId"),
																ForwardingAdj
					)	;

					

					/**Also add the entry to decide TYPE using diff */
					try{
						dbService.getLambdaLspInformationSerivce().InsertLambdaLspInformation(lambdaLspInformationObj);
					}	
					catch (SQLException es) {										
						es.printStackTrace();
					}

				
			}

		}
		
		


		}
		catch(Exception e){
			DataPathLogger.error(" Errror in GetLambdaLspInformation : "+e);
		}



		return lambdaLspInformationObj;
	}

	/**
	 * API to Set LambdaLSP Entry into the Database 
	 * @param rowPathListObj
	 * @param dbService
	 * @param networkId
	 * @author hp
	 * @date 31st May, 2018
	 */
	public void  SetLambdaLspInformation(int TYPE,  Map<String, Object> rowPathListObj, LambdaLspInformation lambdaLspInformationObj , DbService dbService){
		

		if(TYPE == DataPathConfigFileConstants.NoChangeTagValue){/**No change in LSP, same as before */
		
		
		
			if (currentNetworkType.equalsIgnoreCase(MapConstants.GreenField)) {
			
				/** insert the new entry */
				try {
					dbService.getLambdaLspInformationSerivce().InsertLambdaLspInformation(lambdaLspInformationObj);
				} catch (SQLException e) {				
					e.printStackTrace();
				}

			}
			
			else if (currentNetworkType.equalsIgnoreCase(MapConstants.BrownField)) {
				/** find entry from the common api */				
			}

		}
		else if(TYPE == DataPathConfigFileConstants.AddTagValue){/** New Lsp Added */

			/** insert the new entry */
			try {
				dbService.getLambdaLspInformationSerivce().InsertLambdaLspInformation(lambdaLspInformationObj);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if(TYPE == DataPathConfigFileConstants.ModifiedTagValue){/** Lsp Modified */


		}
		else if(TYPE == DataPathConfigFileConstants.DeleteTagValue){/** Lsp Deleted */


		}	
	}


	/**
	 * API to Find OTNLSP Entry from the Database if exist or else the New Entry
	 * @param rowPathListObj
	 * @param dbService
	 * @param networkId
	 * @author hp
	 * @date 1st Jun, 2018
	 */
	public OtnLspInformation  GetOtnLspInformation(int networkId, Map<String, Object> rowPathListObj,  DbService dbService, int typeOfOtnLSP){

		OtnLspInformation otnLspInformationObj = null;
		
		try{

				if(currentNetworkType.equalsIgnoreCase(MapConstants.GreenField))
				{		/**create the new entry*/
				
					
						System.out.println("(int)rowPathListObj.get(\"ForwardingAdj\")	 "+(int)rowPathListObj.get("ForwardingAdj")	);
	
						otnLspInformationObj = new OtnLspInformation(networkId,
																	(int)rowPathListObj.get("DemandId"),
																	(String)rowPathListObj.get("LineRate"), 
																	(String) rowPathListObj.get("Path"), 
																	(int)rowPathListObj.get("WavelengthNo"), 
																	(int)rowPathListObj.get("RoutePriority"), 
																	String.valueOf(rowPathListObj.get("CircuitId")), 
																	(String)rowPathListObj.get("TrafficType"),
																	(String)rowPathListObj.get("ProtectionType"),
																	DataPathConfigFileConstants.tunnelIdGlobal,
																	(int)rowPathListObj.get("lspId") ,
																	(int)rowPathListObj.get("ForwardingAdj")																	
																	)
																	;
						System.out.println("otnLspInformationObj  1"+otnLspInformationObj.toString());
				}
				else if(currentNetworkType.equalsIgnoreCase(MapConstants.BrownField))
				{
					
						System.out.println(" Brownfield ...... ");
						System.out.println("(int)rowPathListObj.get(\"ForwardingAdj\")	2 "+(int)rowPathListObj.get("ForwardingAdj")	);
						/**find entry from the common api / DB entry*/
						try {							
							
						/**First find out LSP based on DemandId*/	
						otnLspInformationObj = dbService.getOtnLspInformationSerivce().FindLsp(networkId,
																	(int)rowPathListObj.get("DemandId"),
																	(String)rowPathListObj.get("CircuitId"),
																	(String)rowPathListObj.get("Path"),
																	(String)rowPathListObj.get("TrafficType"),
																	(int)rowPathListObj.get("RoutePriority"),
																	typeOfOtnLSP);						
						

						DataPathLogger.info("Found from DB : otnLspInformationObj "+otnLspInformationObj.toString());
						

						} 
						
						catch (Exception e) {/**If LSP not found*/								
								
								/**find the max tunnel id from db and create the obj */	///asda									
								int otnLspTunnelId = 	dbService.getOtnLspInformationSerivce().FindMaxTunnelIdOtnLspInformation
														(networkId, greenFieldNetworkId,  String.valueOf(rowPathListObj.get("CircuitId")), 
														(String)rowPathListObj.get("TrafficType"),
														typeOfOtnLSP);
				
								
								DataPathLogger.info(" New otnLspTunnelId ==> "+otnLspTunnelId);

								otnLspInformationObj = new OtnLspInformation(networkId,
																			(int)rowPathListObj.get("DemandId"),
																			(String)rowPathListObj.get("LineRate"), 
																			(String) rowPathListObj.get("Path"), 
																			(int)rowPathListObj.get("WavelengthNo"), 
																			(int)rowPathListObj.get("RoutePriority"), 
																			String.valueOf(rowPathListObj.get("CircuitId")), 
																			(String)rowPathListObj.get("TrafficType"),
																			(String)rowPathListObj.get("ProtectionType"),
																			otnLspTunnelId,
																			(int)rowPathListObj.get("lspId"),
																			(int)rowPathListObj.get("ForwardingAdj")
																			
								)	;

							
								/**Also add the entry to decide TYPE using diff */

								/** insert the new entry */
								try {
									dbService.getOtnLspInformationSerivce().InsertOtnLspInformation(otnLspInformationObj);
								} catch (SQLException es) {										
									es.printStackTrace();
								}

						}


				}
		
	   }
	   catch(Exception e){
		   DataPathLogger.error(" Errror in GetOtnLspInformation : "+e);
	   }




		return otnLspInformationObj;
	}

	/**
	 * API to Set OTNLSP Entry into the Database 
	 * @param rowPathListObj
	 * @param dbService
	 * @param networkId
	 * @author hp
	 * @date 1st Jun, 2018
	 */
	public void  SetOtnLspInformation(int TYPE,  Map<String, Object> rowPathListObj, OtnLspInformation otnLspInformationObj , DbService dbService){
		

		if(TYPE == DataPathConfigFileConstants.NoChangeTagValue){/**No change in LSP, same as before */
		
		
		
			if (currentNetworkType.equalsIgnoreCase(MapConstants.GreenField)) {
			
				/** insert the new entry */
				try {
					dbService.getOtnLspInformationSerivce().InsertOtnLspInformation(otnLspInformationObj);
				} catch (SQLException e) {					
					e.printStackTrace();
				}

			}
			
			else if (currentNetworkType.equalsIgnoreCase(MapConstants.BrownField)) {
				/** find entry from the common api */				

					
	
			}

		}
		else if(TYPE == DataPathConfigFileConstants.AddTagValue){/** New Lsp Added */

			/** insert the new entry */
			try {
				dbService.getOtnLspInformationSerivce().InsertOtnLspInformation(otnLspInformationObj);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if(TYPE == DataPathConfigFileConstants.ModifiedTagValue){/** Lsp Modified */


		}
		else if(TYPE == DataPathConfigFileConstants.DeleteTagValue){/** Lsp Deleted */


		}	
	}





	/**
	 * API to Find the LambdaLspTunnel Id based on the current database entry
	 * @param rowPathListObj
	 * @param dbService
	 * @param networkId
	 * @author hp
	 * @date 24th May, 2018
	 */
	// public int FindLambdaLspTunnelId(Map<String, Object> rowPathListObj,  int networkId, int nodeId, DbService dbService){
		
	// 	int tunnelIdToInsert = MapConstants.I_MINUS_ONE; /**Lsp to return Finally*/

	// 	List<LambdaLspInformation>  lspListToSearchFor = dbService.getLambdaLspInformationSerivce().FindAll(networkId)
	// 												.stream()
	// 												.filter(
	// 														rowObj ->  rowObj.getDemandId() == (int)rowPathListObj.get("DemandId") 
	// 																	&&
	// 																	rowObj.getPath().equalsIgnoreCase((String)rowPathListObj.get("Path"))  
	// 																	&&
	// 																	rowObj.getRoutePriority() == (int)rowPathListObj.get("RoutePriority") 
																	
	// 													)
	// 												.collect(Collectors.toList());
															
	// 		if (	
	// 			lspListToSearchFor			 
	// 			.isEmpty()		
	// 		)
	// 		{/**No LSP Entry Found in DB*/	


	// 			tunnelIdToInsert = DataPathConfigFileConstants.tunnelIdGlobal;

	// 			if(currentNetworkType.equalsIgnoreCase(MapConstants.BrownField)){/**Get the next Available Id : Since the no entry found for Brownfield*/

	// 				try {
	// 					// tunnelIdToInsert = (int)dbService.getLspInformationSerivce().FindMaxTunnelIdLspInformation(networkId)
	// 					// 						.get("LambdaLspTunnelId")  + MapConstants.I_THOUSAND;	
	// 					//DataPathConfigFileConstants.tunnelIdGlobal 
	// 				} catch (Exception e) {
	// 					DataPathLogger.error("FindMaxTunnelIdLspInformation Lambda Error : "+e);
	// 				}
					
	// 			}
	// 			/**lsptunnelid decided now */
	// 			DataPathLogger.debug("tunnelIdToInsert : "+ tunnelIdToInsert);

	// 			/**Check into OtnLsp query to make the Lambda and Otn lsp entry in sync */
	// 			List<Map<String, Object>> otnPathList    = dbService.getNetworkRouteService().FindRoutesWithServiceBasedNodeEnds_cf(networkId, nodeId);								
				

	// 			for (Map<String, Object> rowOtnPathListObj : otnPathList){

	// 					/**Filter List based on the following params */
	// 					if(rowOtnPathListObj.get("DemandId") == rowPathListObj.get("DemandId")
	// 						&&
	// 						rowOtnPathListObj.get("WavelengthNo") == rowPathListObj.get("WavelengthNo")
	// 						&&
	// 						rowOtnPathListObj.get("Path").toString().equalsIgnoreCase((String)rowPathListObj.get("Path"))
	// 						&&
	// 						rowOtnPathListObj.get("Priority") == rowPathListObj.get("Priority")
	// 					){
	// 							/** Insert the entry into Db : Total Entries = OTNLSP times */
	// 							LambdaLspInformation lspInformation = new LambdaLspInformation(networkId,
	// 									(int) rowOtnPathListObj.get("DemandId"), (int) rowOtnPathListObj.get("RoutePriority"),
	// 									(String) rowOtnPathListObj.get("Path"), tunnelIdToInsert, MapConstants.I_MINUS_ONE,
	// 									(String) rowOtnPathListObj.get("TrafficType"));/** initially otn lsp is set to zero */

	// 							try {
	// 								dbService.getLambdaLspInformationSerivce().InsertLambdaLspInformation(lspInformation);

	// 							} catch (Exception e) {
	// 								DataPathLogger.error("Error in LspInfo Insert : " + e);
	// 							}			

	// 					}
	// 			}		
	// 		}

	// 		else{
	// 			/**Entry already exist into DB : Only in case of Brownfield*/
	// 			///DataPathConfigFileConstants.tunnelIdGlobal = lspListToSearchFor.get(0).getLambdaLspTunnelId();
	// 			tunnelIdToInsert = DataPathConfigFileConstants.tunnelIdGlobal;
	// 		}

		
	// 		DataPathLogger.debug("DataPathConfigFileConstants.tunnelIdGlobal => "+DataPathConfigFileConstants.tunnelIdGlobal);		
	// 		DataPathLogger.debug("return  tunnelIdToInsert  => "+ tunnelIdToInsert);		
	
	// 		return tunnelIdToInsert;	
	// }


	// /**
	//  * API to Find the LambdaLspTunnel Id based on the current database entry
	//  * @param rowPathListObj
	//  * @param dbService
	//  * @param networkId
	//  * @author hp
	//  * @date 24th May, 2018
	//  */
	// public int FindOtnLspTunnelId(Map<String, Object> rowPathListObj,  int networkId, int nodeId, DbService dbService){
		

	// 	int otnLspTunnelIdToInsert=MapConstants.I_MINUS_ONE; /**Lsp to return Finally*/

	// 	List<LambdaLspInformation>  lspListToSearchFor = dbService.getLambdaLspInformationSerivce().FindAll(networkId)
	// 												.stream()
	// 												.filter(
	// 														rowObj ->  rowObj.getDemandId() == (int)rowPathListObj.get("DemandId") 
	// 																	&&
	// 																	rowObj.getPath().equalsIgnoreCase((String)rowPathListObj.get("Path"))  
	// 																	&&
	// 																	rowObj.getRoutePriority() == (int)rowPathListObj.get("RoutePriority") 
	// 																	&&
	// 																	rowObj.getServiceType().toString().equalsIgnoreCase((String)rowPathListObj.get("TrafficType")) 
																	
	// 													)
	// 												.collect(Collectors.toList());
															
	// 		if (	
	// 			lspListToSearchFor			 
	// 			.isEmpty()		
	// 		)
	// 		{/**No LSP Entry Found in DB*/		
	// 			// Can't be possible, since LambdaLsp must have done the entry before OtnLsp
	// 		}

	// 		else{/**Entry already exist into DB, so update it */
				
			
	// 			otnLspTunnelIdToInsert = DataPathConfigFileConstants.tunnelIdGlobal;

				
	// 			/**lsptunnelid decided now */
	// 			DataPathLogger.debug("otnLspTunnelIdToInsert : "+ otnLspTunnelIdToInsert);
				
				
				
	// 			if(lspListToSearchFor.get(0).getOtnLspTunnelId() == MapConstants.I_MINUS_ONE){/**New Otn Lsp */


	// 						if(currentNetworkType.equalsIgnoreCase(MapConstants.BrownField)){/**Get the next Available Id : Since the no entry found for Brownfield*/

	// 							try {
	// 								// otnLspTunnelIdToInsert = (int)dbService.getLspInformationSerivce().FindMaxTunnelIdLspInformation(networkId)
	// 								// 						.get("LambdaLspTunnelId")  + MapConstants.I_THOUSAND;	
	// 								//DataPathConfigFileConstants.tunnelIdGlobal 
	// 							} catch (Exception e) {
	// 								DataPathLogger.error("FindMaxTunnelIdLspInformation Lambda Error : "+e);
	// 							}
								
	// 						}


	// 						/**Insert the entry into Db for next search*/
	// 						LambdaLspInformation lspInformation = new LambdaLspInformation(networkId, (int)lspListToSearchFor.get(0).getDemandId(),																	
	// 						(int)lspListToSearchFor.get(0).getRoutePriority(),
	// 						(String)lspListToSearchFor.get(0).getPath(),																	
	// 						lspListToSearchFor.get(0).getLambdaLspTunnelId(),
	// 						otnLspTunnelIdToInsert,
	// 						lspListToSearchFor.get(0).getServiceType()
	// 						);


	// 						try {					
	// 							dbService.getLambdaLspInformationSerivce().UpdateLambdaLspInformation(lspInformation);				
	// 						} 
	// 						catch (Exception e) {
	// 							DataPathLogger.error("Error in LspInfo Insert : "+e);
	// 						}	

	// 			}	
	// 				// else{/**Entry already done : Must be brownfield case */
						
	// 				// 	otnLspTunnelIdToInsert = DataPathConfigFileConstants.tunnelIdGlobal;

	// 				// 	if(currentNetworkType.equalsIgnoreCase(MapConstants.BrownField)){

	// 				// 		try {
	// 				// 			otnLspTunnelIdToInsert = (int)dbService.getLspInformationSerivce().FindMaxTunnelIdLspInformation(networkId)
	// 				// 									.get("OtnLspTunnelId")  + MapConstants.I_THOUSAND;	
	// 				// 		} catch (Exception e) {
	// 				// 			DataPathLogger.error("FindMaxTunnelIdLspInformation Otn Error : "+e);
	// 				// 		}
							
	// 				// 	}

	// 				// 	DataPathLogger.debug("otnLspTunnelIdToInsert : "+ otnLspTunnelIdToInsert);
						
						
	// 				// ///	DataPathConfigFileConstants.tunnelIdGlobal = 	lspListToSearchFor.get(0).getOtnLspTunnelId();
	// 				// otnLspTunnelIdToInsert = DataPathConfigFileConstants.tunnelIdGlobal;
	// 				// }	
				
	// 		}

		
	// 	DataPathLogger.debug("DataPathConfigFileConstants.tunnelIdGlobal => "+DataPathConfigFileConstants.tunnelIdGlobal);		
	// 	DataPathLogger.debug("return otnLspTunnelIdToInsert  => "+otnLspTunnelIdToInsert);		

	// 	return otnLspTunnelIdToInsert;
	// }
	
	
	
	/**
	 * 
	 * @desc Special tag to add for deleted LambdaLsp as only TunnelId, Source, Destination and LspId are required, all the data to fetch from greenfield only
	 * @date 12th July, 2018
	 * @param networkID
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateDeletedLambdaLSPTagDataPathFileXML(int networkId, int nodeId, int nodeDegree, List<LambdaLspInformation> deletedLambdaLspList, DbService dbService,
			Document doc, Element rootElement){
		
		try {
			
			DataPathLogger.debug("generateDeletedLambdaLSPTagDataPathFileXML deletedOtnLspList :  "+ deletedLambdaLspList);
			
			
			/**LambdaLSP Tag Start*/
			for (LambdaLspInformation rowdeletedLambdaLspListObj : deletedLambdaLspList) {
				

				// DataPathDetails->LSP->LambdaLSP
				Element LambdaLSP  = doc.createElement("LambdaLSP");
				rootElement.appendChild(LambdaLSP);		
				
				{
					
					//DataPathDetails->LSP->LambdaLSP->TYPE
					Element TYPE  = doc.createElement("TYPE");
					TYPE.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.DeleteTagValue))); /**No need to call injectTag since we know its a delete case*/
					LambdaLSP.appendChild(TYPE);	
				
									
					{
						// DataPathDetails->LSP->LambdaLSP->PathDetails
						Element PathDetails  = doc.createElement("PathDetails");
						LambdaLSP.appendChild(PathDetails);		
		
						{

							// DataPathDetails->LSP->LambdaLSP->PathDetails->TunnelId
							Element TunnelId  = doc.createElement("TunnelId");				
							TunnelId.appendChild(doc.createTextNode(String.valueOf(rowdeletedLambdaLspListObj.getLambdaLspTunnelId())));/**It would be 2 byte positive integer value. Unique across source and destination*/
							PathDetails.appendChild(TunnelId);
							
							/**Find out Source and Destination Node from Path String*/							
							String[] pathStringArray= rowdeletedLambdaLspListObj.getPath().split(",");
							String pathSourceNode        = pathStringArray[MapConstants.I_ZERO];
							String pathDestinationNode   = pathStringArray[pathStringArray.length-MapConstants.I_ONE];
							
							
							// DataPathDetails->LSP->LambdaLSP->PathDetails->Source
							Element Source  = doc.createElement("Source");
							Long sourceIp = dbService.getIpSchemeNodeService().FindIpSchemeNode(networkId, Integer.parseInt(pathSourceNode)).getRouterIp();
							Source.appendChild(doc.createTextNode(IPv4.integerToStringIP(sourceIp)));/**It signifies the IPv4 address of the source nodes given at the time of Node setup*/
							PathDetails.appendChild(Source);

							// DataPathDetails->LSP->LambdaLSP->PathDetails->Destination
							Element Destination  = doc.createElement("Destination");
							Long desitnationIp = dbService.getIpSchemeNodeService().FindIpSchemeNode(networkId, Integer.parseInt(pathDestinationNode)).getRouterIp();
							Destination.appendChild(doc.createTextNode(IPv4.integerToStringIP(desitnationIp)));/**It signifies the IPv4 address of the destination nodes given at the time of Node setup*/
							PathDetails.appendChild(Destination);

							// DataPathDetails->LSP->LambdaLSP->PathDetails->LSPId
							Element LSPId  = doc.createElement("LSPId");
							LSPId.appendChild(doc.createTextNode(String.valueOf(rowdeletedLambdaLspListObj.getLspId())));/**It would be 2 byte positive integer value, Different then TunnelId*/
							PathDetails.appendChild(LSPId);
							
						}
					}
					
				
				}
				
			}
			/**LambdaLSP Tag End*/
				
			
			
			
			
			
		}
		catch(Exception e) {	
			
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param netowrkId
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateLambdaLSPTagDataPathFileXML(int networkId, int nodeId, int nodeDegree, DbService dbService, Document doc, Element rootElement, HashMap<String,Integer> interfaceMapForLsp){
		
		
		/**/{
		/** Initiate all the required DB Services */		
		List<Map<String, Object>> pathList    = dbService.getNetworkRouteService().FindRoutesWithNodeEnds_cf(networkId, nodeId);
		
		/** ID initialization */
		int lspId=0, forwardingAdgencyLocalCounter=DataPathConfigFileConstants.forwardingAdjacencyGlobal, pathDemandId=MapConstants.I_MINUS_ONE;
		String  pathSourceNode="", pathDestinationNode=""; 
		System.out.println("pathList  for nodeid"+nodeId + " and "+pathList);
		
		
		
		for (Map<String, Object> rowPathListObj : pathList) 
			/**LambdaLSP Tag Start*/
			{
			
			String  interfaceMapForLspKey= rowPathListObj.get("DemandId").toString()+"#"+rowPathListObj.get("WavelengthNo").toString()+
					"#"+rowPathListObj.get("Path").toString();

			System.out.println(" interfaceMapForLspKey "+interfaceMapForLspKey);
			
			System.out.println("rowPathListObj.get(WavelengthNo).equals(MapConstants.I_ZERO) " +
					rowPathListObj.get("WavelengthNo").equals(MapConstants.I_ZERO));
			if(!rowPathListObj.get("WavelengthNo").equals(MapConstants.I_ZERO)) /**For Path With Lambda*/{
					
				
				// DataPathDetails->LSP->LambdaLSP
				Element LambdaLSP  = doc.createElement("LambdaLSP");
				rootElement.appendChild(LambdaLSP);		


				/**Shifted here */

				String pathString = rowPathListObj.get("Path").toString(), pathLocalSourceNode="", pathLocalDestinationNode="";
				int pathLocalDemandId=(int)rowPathListObj.get("DemandId");/**localDemandId to check with the global one*/
			



				if(pathDemandId == MapConstants.I_MINUS_ONE)/**First occurrence*/					
					pathDemandId=pathLocalDemandId; 
				
				/** Condition for path type in order to update lspId & tunnelId :=> Logic changed from Destination based to demand based*/
				if(pathLocalDemandId == pathDemandId){ /** same tunnel only lsp changes */
					lspId++; 
				}
				else{ /**tunnel changed*/

					DataPathConfigFileConstants.tunnelIdGlobal++;					
					lspId=MapConstants.I_ONE;/**Reset LspId on changing of tunnel*/
				}


				/**Get LambdaLSP object first */
				
				rowPathListObj.put("LspId", lspId);	
				rowPathListObj.put("ForwardingAdj", forwardingAdgencyLocalCounter);
				LambdaLspInformation lambdaLspInformationObj = GetLambdaLspInformation(networkId,  rowPathListObj,  dbService);

				System.out.println("lambdaLspInformationObj : "+lambdaLspInformationObj.toString());

						
				//DataPathDetails->LSP->LambdaLSP->TYPE
				Element TYPE  = doc.createElement("TYPE");
				TYPE.appendChild(doc.createTextNode(String.valueOf(
								injectTagFor(DataPathConfigFileConstants.InjectTagForLAMBDALSP, rowPathListObj, nodeId, dbService))));
				LambdaLSP.appendChild(TYPE);					
										
				/** Wavelength number throughout the path */
				String wavelenghtNo = String.valueOf(rowPathListObj.get("WavelengthNo"));
				
				/** Path Type : Working or other */
				String pathType     = String.valueOf(rowPathListObj.get("RoutePriority"));
				String protecting   ="2"; /**Input : Instead of zero default value will be 2*/
				String lspLabelName = ""; 
				String secondaryString = "2";/**Input : Instead of zero default value will be 2*/
				
				/** Find Out ProtectionType to make 1:2R all path restoration as per the input from GMPLS*/
				String pathProtectionLocalType = dbService.getDemandService().FindDemand(networkId, Integer.parseInt(rowPathListObj.get("DemandId").toString())).getProtectionType();
				
				System.out.println("pathProtectionLocalType "+pathProtectionLocalType+ " and wavelenghtNo: "+wavelenghtNo);
				
				
				if(pathType.equalsIgnoreCase(String.valueOf(DataPathConfigFileConstants.RoutePriority_Working))){ /** Working path*/
					/*protecting = "0";*/
					lspLabelName = "LSP_"+DataPathConfigFileConstants.RoutePriority_Working_Str+"_"+rowPathListObj.get("DemandId") ;
				}
				else if(pathType.equalsIgnoreCase(String.valueOf(DataPathConfigFileConstants.RoutePriority_Protection))
						&&
						!(pathProtectionLocalType.equalsIgnoreCase(DataPathConfigFileConstants.one_isto_twoR))
						&&
						!(pathProtectionLocalType.equalsIgnoreCase(DataPathConfigFileConstants.one_isto_R))){ /** Protection Path : In case of 1:2R and 1:R second 
																												priority path is  restoration*/
					protecting = "1";
					lspLabelName = "LSP_"+DataPathConfigFileConstants.RoutePriority_Protection_Str+"_"+rowPathListObj.get("DemandId") ;
				}
				else{ /** Restoration path*/
					protecting = "1";
					lspLabelName = "LSP_"+DataPathConfigFileConstants.RoutePriority_Restoration_Str+"_"+rowPathListObj.get("DemandId") ;
					secondaryString = "1"; /**Set true in case of restoration as per the input*/
				}
				
				System.out.println(" protection & secondary " + protecting + " and "+ secondaryString);
				
				
				/** Determine Signal Type based on Lambda path */
				String lineRate    = String.valueOf(rowPathListObj.get("LineRate")), signalType="";
				String strMT=MapConstants.S_ONE;
				
				if(lineRate.equalsIgnoreCase("100")){
					signalType = String.valueOf(DataPathConfigFileConstants.LineRate_100G);					
				}
				else if(lineRate.equalsIgnoreCase(DataPathConfigFileConstants.LineRate_200G)){
					signalType = String.valueOf(DataPathConfigFileConstants.Signal_OCh_100Gbps);
					strMT=MapConstants.S_TWO;
				}
				else if(lineRate.equalsIgnoreCase("40")){
					signalType = String.valueOf(DataPathConfigFileConstants.Signal_OCh_40Gbps);
				}
				
			
				/** Forwarding Adjacency generation */				
//				String forwardingAdj		= /*(((( directionCounter & 0xff) << 8) | (incrementalCounter & 0xff)))+"";*/
//											  forwardingAdgencyLocalCounter+"";
												
				interfaceMapForLsp.put(interfaceMapForLspKey,lambdaLspInformationObj.getForwardingAdj());/// forwardingAdgencyLocalCounter);/**Add value to the map for OTN LSP use*/
				
				/** Path : Source and Destination*/
				// String pathString = rowPathListObj.get("Path").toString(), pathLocalSourceNode="", pathLocalDestinationNode="";
				// int pathLocalDemandId=(int)rowPathListObj.get("DemandId");/**localDemandId to check with the global one*/
				
				System.out.println("pathString : "+ pathString + " and length" + pathString.length());
				
				
				String[] pathStringArray= pathString.split(",");
				int nodeCount=0;
				
				/**Split the given path for individual node*/				
				pathLocalSourceNode = pathString.split(",")[0]; /** Extract the first/source node */						
				
				for(String tempPath : pathString.split(",")){
					pathLocalDestinationNode = tempPath;	
					nodeCount++;
				}
				
				/** For the first iteration where Global var not updated : Need to Phase out since checking based on DemandId now */
				if(pathDestinationNode.equalsIgnoreCase("")){				
					pathDestinationNode = pathLocalDestinationNode;
				}
						
				
				/** Update the global node by local node */
				pathSourceNode      = pathLocalSourceNode;				
				pathDestinationNode = pathLocalDestinationNode;				
				pathDemandId = pathLocalDemandId;
				
				System.out.println("pathSourceNode "+ pathSourceNode +" and pathDestinationNode : "+ pathDestinationNode);
				
				/**
				 * Find out LSP Flag based on path protection type 
				 */
				///int pathDemandId = (int) rowPathListObj.get("DemandId");
				/**Derived from Demand Id*/
				
				System.out.println("						\n" + 
						"						dbService.getCircuitService().FindCircuit(networkId, \n" + 
						"						dbService.getCircuitDemandMappingService().FindCircuits(networkId, pathDemandId).get(MapConstants.I_ZERO).getCircuitId()).getTrafficType().toString()"
						+ 						
						dbService.getCircuitService().FindCircuit(networkId, 
						dbService.getCircuitDemandMappingService().FindCircuits(networkId, pathDemandId).get(MapConstants.I_ZERO).getCircuitId()).getTrafficType().toString());
				
				int GPID = (int) DataPathConfigFileConstants.gpidConstantsMap.get(						
						dbService.getCircuitService().FindCircuit(networkId, 
						dbService.getCircuitDemandMappingService().FindCircuits(networkId, pathDemandId).get(MapConstants.I_ZERO).getCircuitId()).getTrafficType().toString());
				System.out.println(" GPID LambdaLSP : "+ GPID);
				
				String pathProtectionType = dbService.getDemandService().FindDemand(networkId, pathDemandId).getProtectionType(), pathLspFlag="";
				System.out.println("pathProtectionType : "+ pathProtectionType);
				if(pathProtectionType.equalsIgnoreCase("none")){
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_Unprotected);
				}
				else if(pathProtectionType.equalsIgnoreCase("1:1")){
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_1_N_Protection);
				}
				else if(pathProtectionType.equalsIgnoreCase("1+1+R") 
						|| pathProtectionType.equalsIgnoreCase("1+1") 
						){
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_1_Plus_1_Bidirectional);
				}
				else if(pathProtectionType.equalsIgnoreCase("1+1+2R")){
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_1_Plus_1_Bidirectional);
				}
				else if(pathProtectionType.equalsIgnoreCase("1:2R")){ /**As per the input*/
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_1_N_Protection);
				}
				System.out.println(" patjLspFlag : "+pathLspFlag);

				// DataPathDetails->LSP->LambdaLSP->PathDetails
				Element PathDetails  = doc.createElement("PathDetails");
				LambdaLSP.appendChild(PathDetails);
				

				/**Common Params to PathDetails Start*/


				// DataPathDetails->LSP->LambdaLSP->PathDetails->TunnelId
				Element TunnelId  = doc.createElement("TunnelId");				
				TunnelId.appendChild(doc.createTextNode(String.valueOf(lambdaLspInformationObj.getLambdaLspTunnelId())));/**It would be 2 byte positive integer value. Unique across source and destination*/
				PathDetails.appendChild(TunnelId);
				
				// DataPathDetails->LSP->LambdaLSP->PathDetails->LSPOrdering
				Element LSPOrdering  = doc.createElement("LSPOrdering");				
				LSPOrdering.appendChild(doc.createTextNode(pathType));/**It will provide LSP Ordering with below mentioned values
																	Working     : 1
																	Protection  : 2
																	Restoration : 3 and above
															   */
				PathDetails.appendChild(LSPOrdering);				
				
				
				// DataPathDetails->LSP->LambdaLSP->PathDetails->Source
				Element Source  = doc.createElement("Source");
				Long sourceIp = dbService.getIpSchemeNodeService().FindIpSchemeNode(networkId, Integer.parseInt(pathSourceNode)).getRouterIp();
				Source.appendChild(doc.createTextNode(/*"192.168.115.240"*/IPv4.integerToStringIP(sourceIp)));/**It signifies the IPv4 address of the source nodes given at the time of Node setup*/
				PathDetails.appendChild(Source);

				// DataPathDetails->LSP->LambdaLSP->PathDetails->Destination
				Element Destination  = doc.createElement("Destination");
				Long desitnationIp = dbService.getIpSchemeNodeService().FindIpSchemeNode(networkId, Integer.parseInt(pathDestinationNode)).getRouterIp();
				Destination.appendChild(doc.createTextNode(/*"192.168.115.241"*/IPv4.integerToStringIP(desitnationIp)));/**It signifies the IPv4 address of the destination nodes given at the time of Node setup*/
				PathDetails.appendChild(Destination);

				// DataPathDetails->LSP->LambdaLSP->PathDetails->LSPId
				Element LSPId  = doc.createElement("LSPId");
				LSPId.appendChild(doc.createTextNode(String.valueOf(lspId)));/**It would be 2 byte positive integer value, Different then TunnelId*/
				PathDetails.appendChild(LSPId);
				
				
				
				/**ExplicitRoute Start*/
				// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute
				Element ExplicitRoute  = doc.createElement("ExplicitRoute");					
				PathDetails.appendChild(ExplicitRoute);
				
				System.out.println(" nodeCount "+ nodeCount);
				for(int i=0; i<nodeCount; i++)
				{					
					System.out.println("pathStringArray  : "+ pathStringArray[i]);
					
					int interFaceType;
					boolean lastNodeFlag=false;/**Enable for Last node so that InterfaceEncoding type can be set as 0 for
														 differentiation*/
					
					if(i==MapConstants.I_ZERO){
						interFaceType = MapConstants.I_ONE; /**For First Node, IPV4 Type*/
					}
					else{
						interFaceType = MapConstants.I_ZERO; /** For Rest of the Node */
					}
					
					/**Common Params to PathDetails End*/
					{	
						// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node
						Element Node  = doc.createElement("Node");
						ExplicitRoute.appendChild(Node);
						{
							/**Determine  IP  based on the first and rest nodes*/							
							Long nodeIp;
							String nodeIp_string = "", IpPrefixLen_Str="";
							
							if(pathStringArray[i].equalsIgnoreCase(pathLocalSourceNode)){/**First Node : Link Source IP*/
								nodeIp = Long.parseLong(dbService.getIpSchemeLinkService().FindIpOfSrcNodeEnd_cf(networkId,  Integer.parseInt(pathStringArray[i]),
										                                            Integer.parseInt(pathStringArray[i+1])).toString());
								nodeIp_string = String.valueOf(IPv4.integerToStringIP(nodeIp));			
								IpPrefixLen_Str=DataPathConfigFileConstants.Link_IpPrefixLength;
							}
							else{/**Other Node :  Node Router IP*/
								nodeIp =  dbService.getIpSchemeNodeService().FindIpSchemeNode(networkId, Integer.parseInt(pathStringArray[i])).getRouterIp();
								nodeIp_string = String.valueOf(IPv4.integerToStringIP(nodeIp));
								IpPrefixLen_Str=DataPathConfigFileConstants.Node_IpPrefixLength;
							}
							
							// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->IP
							Element IP  = doc.createElement("IP");
							System.out.println("nodeIp_string "+ nodeIp_string);
							IP.appendChild(doc.createTextNode(nodeIp_string));/**The IP address field will carry an IP address of a link or an
								 													IP address associated with the source router of the span. */
							Node.appendChild(IP);
								
							// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->InterfaceType
							Element InterfaceType  = doc.createElement("InterfaceType");
							InterfaceType.appendChild(doc.createTextNode(String.valueOf(interFaceType)));/** 0 for unnumbered and 1 for IPV4  */
							Node.appendChild(InterfaceType);
							/**
							 *  Upstream        : Input for the node
							 *  Downstream  : Input for the other node
							 *  Ex:
							 *         Node 1 <---------> Node 2
							 *         Node1 Upstream 		:  Incoming Lambda from Node2 to Node1      
							 *         Node1 Downstream  :  Incoming Lambda from Node1 to Node2/ Outgoing Lambda from Node1 to Node2
							 */
							// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->InterfaceId
							String linkDirection_U="", linkDirection_D="";
							if(i== nodeCount-1){/** For Last Node in the Path*/
								linkDirection_U = dbService.getLinkService().FindLinkDirection
										(networkId,Integer.parseInt(pathStringArray[i-1]), Integer.parseInt(pathStringArray[i]));/** */
								linkDirection_D = dbService.getLinkService().FindLinkDirection
										(networkId,Integer.parseInt(pathStringArray[i]), Integer.parseInt(pathStringArray[i-1]));/** */
								lastNodeFlag=true;
							}
							else{/** For Rest of the node in the Path */
								linkDirection_U = dbService.getLinkService().FindLinkDirection
										(networkId,Integer.parseInt(pathStringArray[i+1]), Integer.parseInt(pathStringArray[i]));/** */
								linkDirection_D = dbService.getLinkService().FindLinkDirection
										(networkId,Integer.parseInt(pathStringArray[i]), Integer.parseInt(pathStringArray[i+1]));/** */
							}
							
							int interfaceInput = (int) DataPathConfigFileConstants.directionConstantsHashMap.get(linkDirection_D);							
							String InterfaceIdValue 	= (((( interfaceInput & 0xff) << 8) | (Integer.parseInt(wavelenghtNo) & 0xff)))+"";
							Element InterfaceId  = doc.createElement("InterfaceId");
							InterfaceId.appendChild(doc.createTextNode(InterfaceIdValue));/** As  per the input*/
							Node.appendChild(InterfaceId);
							
							// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->IpPrefixLen
							Element IpPrefixLen = doc.createElement("IpPrefixLen");
							IpPrefixLen.appendChild(doc.createTextNode(IpPrefixLen_Str));/** For Link 30 Bits and Node 29 bits */
							Node.appendChild(IpPrefixLen);
							
							
							
							// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel
							Element GeneralisedLabel  = doc.createElement("GeneralisedLabel");							
							Node.appendChild(GeneralisedLabel);
								
							/**GeneralisedLabel Start*/
							{
								// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream
								Element Upstream  = doc.createElement("Upstream");								
								GeneralisedLabel.appendChild(Upstream);
									
								/**Upstream Start*/
								{
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->EncodingType
									Element EncodingType_U  = doc.createElement("EncodingType");
									if(!lastNodeFlag)
										EncodingType_U.appendChild(doc.createTextNode(DataPathConfigFileConstants.InterfaceEncodingType));/** Lambda (photonic) LSP is at lambda level */									   
									else
										 EncodingType_U.appendChild(doc.createTextNode(String.valueOf(MapConstants.I_ZERO)));/** Lambda (photonic) LSP is at lambda level */

									Upstream.appendChild(EncodingType_U);
										
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->SwitchingType
									Element SwitchingType_U  = doc.createElement("SwitchingType");
									SwitchingType_U.appendChild(doc.createTextNode(DataPathConfigFileConstants.SwitchingType));/** Lambda-Switch Capable (LSC) */
									Upstream.appendChild(SwitchingType_U);
										
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->GPID
									Element GPID_U  = doc.createElement("GPID");
									GPID_U.appendChild(doc.createTextNode(String.valueOf(GPID)));/** DBG => Mapping For Ethernet Client, need to check for other => checked for OTU2*/									
									Upstream.appendChild(GPID_U);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->Grid
									Element Grid_U  = doc.createElement("Grid");
									Grid_U.appendChild(doc.createTextNode(DataPathConfigFileConstants.ChannelSpacing[1]));/** This is 3 bits long field and the value for Grid 
																						is set to 1 for the ITU-T DWDM grid as defined in G.694.1*/
									Upstream.appendChild(Grid_U);
										
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->ChannelSpacing
									Element ChannelSpacing_U  = doc.createElement("ChannelSpacing");
									ChannelSpacing_U.appendChild(doc.createTextNode(DataPathConfigFileConstants.ChannelSpacing[2]));/**  This is 4 bit long field  */
									Upstream.appendChild(ChannelSpacing_U);
										
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->Identifier
									Element Identifier_U  = doc.createElement("Identifier");
									Identifier_U.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap.get(linkDirection_U))));/** */
									Upstream.appendChild(Identifier_U);
										
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->Frequency
									Element Frequency_U  = doc.createElement("Frequency");
									Frequency_U.appendChild(doc.createTextNode(wavelenghtNo));/** DBG => As of now lambda is placed later on can be updated*/
									Upstream.appendChild(Frequency_U);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->ProtectionSubType
									Element ProtectionSubType_U  = doc.createElement("ProtectionSubType");
									ProtectionSubType_U.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.ProtectionsubType_YCable)));/** */
									Upstream.appendChild(ProtectionSubType_U);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->IsRevertive
									Element IsRevertive_U  = doc.createElement("IsRevertive");
									IsRevertive_U.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.IsRevertive_Yes)));/** */
									Upstream.appendChild(IsRevertive_U);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->FecType
									Element FecType_U  = doc.createElement("FecType");
									FecType_U.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.FEC_Type_G709_LAMBDALSP)));/** */
									Upstream.appendChild(FecType_U);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->FecStatus
									Element FecStatus_U  = doc.createElement("FecStatus");
									FecStatus_U.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.FEC_Status_Enable)));/** */
									Upstream.appendChild(FecStatus_U);
									
									
								}
								/**Upstream End*/
									
								/**Downstream Start*/								
								// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream
								Element Downstream  = doc.createElement("Downstream");								
								GeneralisedLabel.appendChild(Downstream);								
								{
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->EncodingType
									Element EncodingType_D  = doc.createElement("EncodingType");
									if(!lastNodeFlag)
										EncodingType_D.appendChild(doc.createTextNode(DataPathConfigFileConstants.InterfaceEncodingType));/** Lambda (photonic) LSP is at lambda level */									   
									else
										 EncodingType_D.appendChild(doc.createTextNode(String.valueOf(MapConstants.I_ZERO)));/** Lambda (photonic) LSP is at lambda level */

									Downstream.appendChild(EncodingType_D);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->SwitchingType
									Element SwitchingType_D  = doc.createElement("SwitchingType");
									SwitchingType_D.appendChild(doc.createTextNode(DataPathConfigFileConstants.SwitchingType));/** Lambda-Switch Capable (LSC) */
									Downstream.appendChild(SwitchingType_D);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->GPID
									Element GPID_D  = doc.createElement("GPID");
									GPID_D.appendChild(doc.createTextNode(String.valueOf(GPID)));/** DBG => Mapping For Ethernet Client, need to check for other => checked for OTU2*/ 								
									Downstream.appendChild(GPID_D);
										
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->Grid
									Element Grid_D  = doc.createElement("Grid");
									Grid_D.appendChild(doc.createTextNode("1"));/** */
									Downstream.appendChild(Grid_D);
										
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->ChannelSpacing
									Element ChannelSpacing_D  = doc.createElement("ChannelSpacing");
									ChannelSpacing_D.appendChild(doc.createTextNode(DataPathConfigFileConstants.ChannelSpacing[2]));/**  This is 4 bit long field  */
									Downstream.appendChild(ChannelSpacing_D);
										
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->Identifier
									Element Identifier_D  = doc.createElement("Identifier");
									Identifier_D.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap.get(linkDirection_D))));/** */
									Downstream.appendChild(Identifier_D);
										
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->Frequency
									Element Frequency_D = doc.createElement("Frequency");
									Frequency_D.appendChild(doc.createTextNode(wavelenghtNo));/** DBG => As of now lambda is placed later on can be updated*/
									Downstream.appendChild(Frequency_D);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->ProtectionSubType
									Element ProtectionSubType_D  = doc.createElement("ProtectionSubType");
									ProtectionSubType_D.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.ProtectionsubType_YCable)));/** */
									Downstream.appendChild(ProtectionSubType_D);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->IsRevertive
									Element IsRevertive_D  = doc.createElement("IsRevertive");
									IsRevertive_D.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.IsRevertive_Yes)));/** */
									Downstream.appendChild(IsRevertive_D);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->FecType
									Element FecType_D  = doc.createElement("FecType");
									FecType_D.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.FEC_Type_G709_LAMBDALSP)));/** */
									Downstream.appendChild(FecType_D);
									
									// DataPathDetails->LSP->LambdaLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->FecStatus
									Element FecStatus_D  = doc.createElement("FecStatus");
									FecStatus_D.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.FEC_Status_Enable)));/** */
									Downstream.appendChild(FecStatus_D);
								}
								/**Downstream End*/
							}
							/**GeneralisedLabel End*/
						}				
						/**ExplicitRoute End*/				
					}
				}
					
				/**Common Params to PathDetails Start*/
					
				{
					// DataPathDetails->LSP->LambdaLSP->PathDetails->SetUpPriority
					Element SetUpPriority  = doc.createElement("SetUpPriority");
					SetUpPriority.appendChild(doc.createTextNode("4"));/** */
					PathDetails.appendChild(SetUpPriority);
						
					// DataPathDetails->LSP->LambdaLSP->PathDetails->HoldPriority
					Element HoldPriority  = doc.createElement("HoldPriority");
					HoldPriority.appendChild(doc.createTextNode("4"));/** */
					PathDetails.appendChild(HoldPriority);
						
					// DataPathDetails->LSP->LambdaLSP->PathDetails->ExcludeAny
					Element ExcludeAny  = doc.createElement("ExcludeAny");
					ExcludeAny.appendChild(doc.createTextNode("0"));/** As per the input*/
					PathDetails.appendChild(ExcludeAny);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->IncludeAny
					Element IncludeAny = doc.createElement("IncludeAny");
					IncludeAny.appendChild(doc.createTextNode("0"));/** */
					PathDetails.appendChild(IncludeAny);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->IncludeAll
					Element IncludeAll = doc.createElement("IncludeAll");
					IncludeAll.appendChild(doc.createTextNode("0"));/** */
					PathDetails.appendChild(IncludeAll);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->ForwardingAdgency
					Element ForwardingAdgency = doc.createElement("ForwardingAdjacency"); /** This field indicates that 
															whether this LSP would be created as hierarchical LSP or not, thus enabling other LSP to use it as a TE-LINK*/									
					ForwardingAdgency.appendChild(doc.createTextNode(lambdaLspInformationObj.getForwardingAdj()+""));/** */
					PathDetails.appendChild(ForwardingAdgency);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->Secondary
					Element Secondary = doc.createElement("Secondary");
					Secondary.appendChild(doc.createTextNode(secondaryString));/** DBG => insert zero as per the input*/
					PathDetails.appendChild(Secondary);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->Protecting
					Element Protecting = doc.createElement("Protecting");					
					Protecting.appendChild(doc.createTextNode(protecting));/** For protecting : 1, non-protecting:0 */
					PathDetails.appendChild(Protecting);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->Notification
					Element Notification = doc.createElement("Notification");
					Notification.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.Notification_Flag)));/** Set as 1 as per the input*/
					PathDetails.appendChild(Notification);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->LSPFlags
					Element LSPFlags = doc.createElement("LSPFlags");
					LSPFlags.appendChild(doc.createTextNode(pathLspFlag));
					PathDetails.appendChild(LSPFlags);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->LinkFlags
					Element LinkFlags = doc.createElement("LinkFlags");
					LinkFlags.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.Link_flags_Unprotected)));/** As per the input*/
					PathDetails.appendChild(LinkFlags);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->Flags
					Element Flags = doc.createElement("Flags");
					Flags.appendChild(doc.createTextNode("02"));/** */
					PathDetails.appendChild(Flags);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->SignalType
					Element SignalType = doc.createElement("SignalType");
					SignalType.appendChild(doc.createTextNode(signalType));/** */
					PathDetails.appendChild(SignalType);
					
					// DataPathDetails->LSP->LambdaLSP->PathDetails->MT
					Element MT = doc.createElement("MT");
					MT.appendChild(doc.createTextNode(strMT));/** */
					PathDetails.appendChild(MT);

					// DataPathDetails->LSP->LambdaLSP->PathDetails->LSPName
					Element LSPName = doc.createElement("LSPName");
					LSPName.appendChild(doc.createTextNode(lspLabelName));/** DBG => Need to Discuss it with GMPLS */
					PathDetails.appendChild(LSPName);
				}
					
				/**Common Params to PathDetails End*/
				
				forwardingAdgencyLocalCounter++;

				

			/**Finally Insert the Entry into Database */
			SetLambdaLspInformation(Integer.parseInt(TYPE.getFirstChild().getNodeValue()), rowPathListObj, lambdaLspInformationObj,  dbService);

			}		
			/**LambdaLSP Tag End*/
		  }		  
		}
		/*catch(Exception e){
			System.out.println(" LambdaLSP Exception "+ e);
		}*/
	}

	
	
	/**
	 * 
	 * @desc Special tag to add for deleted OTNLSP as only TunnelId, Source, Destination and LspId are required, all the data to fetch from greenfield only
	 * @date 12th July, 2018
	 * @param networkID
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateDeletedOTNTDLSPTagDataPathFileXML(int networkId, int nodeId, int nodeDegree, List<Map<String,Object>> deletedOtnLspList, DbService dbService,
			Document doc, Element rootElement,HashMap<String,Integer> interfaceMapForLsp){
		
		try {
			
			DataPathLogger.debug("generateDeletedOTNTDLSPTagDataPathFileXML deletedOtnLspList :  "+ deletedOtnLspList);
			
			
			/**OTNTDMLSP Tag Start*/
			for (Map<String, Object> rowdeletedOtnLspListObj : deletedOtnLspList) {
				
				// DataPathDetails->LSP->OTNTDMLSP
				Element OTNTDMLSP  = doc.createElement("OTNTDMLSP");
				rootElement.appendChild(OTNTDMLSP);
				{
					
				
					//DataPathDetails->LSP->OTNTDMLSP -> TYPE
					Element TYPE  = doc.createElement("TYPE");
					TYPE.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.DeleteTagValue))); /**No need to call injectTag since we know its a delete case*/
					OTNTDMLSP.appendChild(TYPE);
					
					{
						// DataPathDetails->LSP->OTNTDMLSP->PathDetails
						Element PathDetails  = doc.createElement("PathDetails");
						OTNTDMLSP.appendChild(PathDetails);			
		
						{

							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->TunnelId
							Element TunnelId  = doc.createElement("TunnelId");				
							TunnelId.appendChild(doc.createTextNode(String.valueOf(rowdeletedOtnLspListObj.get("OtnLspTunnelId"))));/**It would be 2 byte positive integer value. Unique across source and destination*/
							PathDetails.appendChild(TunnelId);
							
							
							/**Find out Source and Destination Node from Path String*/							
							String[] pathStringArray= rowdeletedOtnLspListObj.get("Path").toString().split(",");
							String pathSourceNode        = pathStringArray[MapConstants.I_ZERO];
							String pathDestinationNode   = pathStringArray[pathStringArray.length-MapConstants.I_ONE];
							
							
							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->Source
							Element Source  = doc.createElement("Source");
							Long sourceIp = dbService.getIpSchemeNodeService().FindIpSchemeNode(networkId, Integer.parseInt(pathSourceNode)).getRouterIp();
							Source.appendChild(doc.createTextNode(/*"192.168.115.240"*/IPv4.integerToStringIP(sourceIp)));/**It signifies the IPv4 address of the source nodes given at the time of Node setup*/
							PathDetails.appendChild(Source);
			
							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->Destination
							Element Destination  = doc.createElement("Destination");
							Long destinationIp = dbService.getIpSchemeNodeService().FindIpSchemeNode(networkId, Integer.parseInt(pathDestinationNode)).getRouterIp();
							Destination.appendChild(doc.createTextNode(/*"192.168.115.241"*/IPv4.integerToStringIP(destinationIp)));/**It signifies the IPv4 address of the destination nodes given at the time of Node setup*/
							PathDetails.appendChild(Destination);
			
							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->LSPId
							Element LSPId  = doc.createElement("LSPId");
							LSPId.appendChild(doc.createTextNode(String.valueOf(rowdeletedOtnLspListObj.get("LspId"))));/**It would be 2 byte positive integer value, Different then TunnelId*/
							PathDetails.appendChild(LSPId);	
						}
					}
					
				
				}
				
			}
			/**OTNTDMLSP Tag End*/
				
			
			
			
			
			
		}
		catch(Exception e) {	
			
			e.printStackTrace();
		}
		
	}
 

	/**
	 * 
	 * @param rowPathListObj
	 * @param dbService
	 */
	public boolean CheckIf10gParentOTNLSP(Map<String, Object> rowPathListObj, DbService dbService){


		String[] groupedCircuitIdArray = rowPathListObj.get("CircuitId").toString().split(",");
		int circuitId   = Integer.parseInt(groupedCircuitIdArray[MapConstants.I_ZERO]);
		
		
		if(
			dbService.getCircuit10gAggService()
				.FindAllCircuit10gAgg((int)rowPathListObj.get("NetworkId"), circuitId)
				.isEmpty()
		)
		{
			return false;
		}	
		else			
		{
			return true;
		}						

		
	}
	
	/**
	 * To Map OtnLsp SignalType
	 * @param trafficType
	 * @param otnLspType
	 * @return
	 */
	public String FindSignalType(String trafficType, int otnLspType) {
		
		DataPathLogger.debug("trafficType, otnLspType  : "+trafficType + ","+otnLspType);
		String signalType = MapConstants.S_ZERO;
		
		if (trafficType.equalsIgnoreCase("100.0")) {
			return signalType =  String.valueOf(DataPathConfigFileConstants.Signal_ODU4_100Gbps);
		}
		
		switch (otnLspType) {
		
			case  DataPathConfigFileConstants.Non10GAggClientSideOtnLsp :
				signalType = String.valueOf(DataPathConfigFileConstants.Signal_ODU2_10Gbps);
	        break;	    
		
			case  DataPathConfigFileConstants.MPNClientSideOtnLsp :
				signalType = String.valueOf(DataPathConfigFileConstants.Signal_ODU2_10Gbps);
	        break;
	        
			case  DataPathConfigFileConstants.XGMClientSideOtnLsp :
				signalType = String.valueOf(DataPathConfigFileConstants.Signal_ODU0_10Gbps);
	        break;
	    
		}
		
		DataPathLogger.debug("signalType : "+signalType);
		
		return signalType;
		
		
	}
	
		
	/**
	 * 
	 * @param networkID
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateOTNTDLSPTagDataPathFileXML(int networkId, int nodeId, int nodeDegree, DbService dbService,
												   Document doc, Element rootElement, HashMap<String, Integer> interfaceMapForLsp,
												   List<Map<String, Object>> pathList, int typeOfOtnLSP) {

		DataPathLogger.debug("FindRoutesWithServiceBasedNodeEnds_cf pathList :  " + pathList + " and typeOfOtnLSP : "
				+ typeOfOtnLSP);

		/** ID initialization */
		int lspId = 0, forwardingAdgencyLocalCounter = DataPathConfigFileConstants.forwardingAdjacencyGlobal,
				pathDemandId = MapConstants.I_MINUS_ONE, GPID = MapConstants.I_MINUS_ONE;

		String pathSourceNode = "", pathDestinationNode = "";

		for (Map<String, Object> rowPathListObj : pathList)
		/** OTNTDMLSP Tag Start */
		{

			DataPathLogger.debug("rowPathListObj => " + rowPathListObj);

			if (!rowPathListObj.get("WavelengthNo").equals(MapConstants.I_ZERO)) /** For Path With Lambda */
			{
				
				/** Path : Source and Destination */
				String pathString = rowPathListObj.get("Path").toString();
				String pathLocalSourceNode = "";
				String pathLocalDestinationNode = "";
				String pathOTNSourceNode = "";
				String pathOTNDestinationNode = "";
				
				
				// localDemandId to check with the global one
				int pathLocalDemandId  = (int) rowPathListObj.get("DemandId");
				int pathLocalGpid      = (int) DataPathConfigFileConstants.gpidConstantsMap
										       .get(rowPathListObj.get("TrafficType"));

				// DataPathDetails->LSP->OTNTDMLSP
				Element OTNTDMLSP = doc.createElement("OTNTDMLSP");
				rootElement.appendChild(OTNTDMLSP);				

				// First occurance
				if (pathDemandId == MapConstants.I_MINUS_ONE) {
					pathDemandId = pathLocalDemandId;
					GPID = pathLocalGpid;
				}

				/**
				 * Condition for path type in order to update lspId & tunnelId :=> Logic changed
				 * from Destination based to demand based => Now Demand + Service Type based
				 */

				// same tunnel only lsp changes 
				if (pathLocalDemandId == pathDemandId && pathLocalGpid == GPID) { 
					lspId++;
				}
				// tunnel changed 
				else {
					
					DataPathConfigFileConstants.tunnelIdGlobal++;

					lspId = MapConstants.I_ONE;/** Reset LspId on changing of tunnel */

				}

				// For 10G Agg 
				if (typeOfOtnLSP <= DataPathConfigFileConstants.MPNClientSideOtnLsp) {

					///DataPathConfigFileConstants.tunnelIdGlobal++;
					OtnLspInformation otnLspObj  = dbService.getOtnLspInformationSerivce().FindLspFor10GAgg(networkId, 
											pathLocalDemandId, (String)rowPathListObj.get("CircuitId").toString(), (String)rowPathListObj.get("Path").toString(),
											(String)rowPathListObj.get("TrafficType"), (int)rowPathListObj.get("RoutePriority"), typeOfOtnLSP)
									;
					if(otnLspObj!= null) {
						System.out.println(" otnLspObj " +otnLspObj);
						lspId = otnLspObj.getLspId();
					}
					else {
//						DataPathConfigFileConstants.tunnelIdGlobal++;
					}
							
				}
				
				DataPathLogger.debug(" tunnelIdGlobal, lspId : " + DataPathConfigFileConstants.tunnelIdGlobal + " , "+ lspId);
				// prepare object for otnlspinfo
				rowPathListObj.put("lspId", lspId);			
				
				//Get from the lambda lsp table 
				System.out.println("rowPathListObj "+ rowPathListObj.toString());
				int interfaceMapForLspValue =  
						dbService.getLambdaLspInformationSerivce().FindLsp(networkId, 
												(int)rowPathListObj.get("DemandId"), (String)rowPathListObj.get("Path")).getForwardingAdj();
				System.out.println("interfaceMapForLspValue " + interfaceMapForLspValue);
				rowPathListObj.put("ForwardingAdj", interfaceMapForLspValue);
				
				OtnLspInformation otnLspInformationObj = GetOtnLspInformation(networkId, rowPathListObj, dbService,typeOfOtnLSP);			
				rowPathListObj.put("OtnLspTunnelId", otnLspInformationObj.getOtnLspTunnelId());
				
				DataPathLogger.debug(" otnLspInformationObj : " + otnLspInformationObj.toString());

				// DataPathDetails->LSP->OTNTDMLSP -> TYPE
				Element TYPE = doc.createElement("TYPE");
				TYPE.appendChild(doc.createTextNode
								(String.valueOf(injectTagFor(DataPathConfigFileConstants.InjectTagForOTNTDMLSP,rowPathListObj, nodeId, dbService))));
				OTNTDMLSP.appendChild(TYPE);

				// Wavelength number throughout the path
				String wavelenghtNo = String.valueOf(rowPathListObj.get("WavelengthNo"));
				// Path Type : Working or other 
				String pathType = String.valueOf(rowPathListObj.get("RoutePriority"));
				String protecting = MapConstants.S_TWO;    
				String lspLabelName = "";
				String secondaryString = MapConstants.S_TWO;

				/**
				 * Find Out ProtectionType to make 1:2R all path restoration as per the input
				 * from GMPLS
				 */
				String pathProtectionLocalType = dbService.getDemandService()
						.FindDemand(networkId, Integer.parseInt(rowPathListObj.get("DemandId").toString()))
						.getProtectionType();

				if (pathType.equalsIgnoreCase(
						String.valueOf(DataPathConfigFileConstants.RoutePriority_Working))) { /** Working path */
					/* protecting = "0"; */
					lspLabelName = "LSP_" + DataPathConfigFileConstants.RoutePriority_Working_Str + "_"
							+ rowPathListObj.get("DemandId");
				} else if (pathType
						.equalsIgnoreCase(String.valueOf(DataPathConfigFileConstants.RoutePriority_Protection))
						&& !(pathProtectionLocalType.equalsIgnoreCase(DataPathConfigFileConstants.one_isto_twoR))
						&& !(pathProtectionLocalType.equalsIgnoreCase(DataPathConfigFileConstants.one_isto_R))) {
					/**
					 * Protection Path : In case of 1:2R and 1:R second priority path is restoration
					 */
					protecting = "1";
					lspLabelName = "LSP_" + DataPathConfigFileConstants.RoutePriority_Protection_Str + "_"
							+ rowPathListObj.get("DemandId");
				} else { /** Restoration Path */
					protecting = "1";
					lspLabelName = "LSP_" + DataPathConfigFileConstants.RoutePriority_Restoration_Str + "_"
							+ rowPathListObj.get("DemandId");
					secondaryString = "1"; /** Set true in case of restoration as per the input */
				}

				// Determine Signal Type based on Lambda path 
				String trafficType = "";
				String signalType = "";

				int localpathCircuitDemandId = (int) rowPathListObj.get("DemandId");
				
				trafficType = String.valueOf(dbService.getCircuitService()
						.FindCircuit(networkId,
								dbService.getCircuitDemandMappingService()
										.FindCircuits(networkId, localpathCircuitDemandId).get(MapConstants.I_ZERO)
										.getCircuitId())
						.getRequiredTraffic());
			

				/** Forwarding Adjacency generation */
				String forwardingAdj = /* (((( directionCounter & 0xff) << 8) | (incrementalCounter & 0xff)))+""; */
						forwardingAdgencyLocalCounter + "";
				/** DBG => Logic changed after input from GMPLS */

				String[] pathStringArray = pathString.split(",");
				int nodeCount = 0;

				/** Split the given path for individual node */
				pathLocalSourceNode = pathString.split(",")[0]; /** Extract the first/source node */
				pathOTNSourceNode = pathString.split(",")[0];

				for (String tempPath : pathString.split(",")) {
					pathLocalDestinationNode = tempPath;
					pathOTNDestinationNode = tempPath;

					nodeCount++;
				}

				/**
				 * For the first iteration where Global var not updated : Need to Phase out
				 * since checking based on DemandId now
				 */
				if (pathDestinationNode.equalsIgnoreCase("")) {
					pathDestinationNode = pathLocalDestinationNode;
				}

				String interfaceMapForLspKey = rowPathListObj.get("DemandId").toString() + "#"
						+ rowPathListObj.get("WavelengthNo").toString() + "#" + rowPathListObj.get("Path");
//				String interfaceMapForLspValue;/// = interfaceMapForLsp.get(interfaceMapForLspKey) + "";
				String forwardingAdjStr = MapConstants.S_ZERO;/** By default it would be zero */
				
				//Get from the lambda lsp table
				/*
				int interfaceMapForLspValue =  
						dbService.getLambdaLspInformationSerivce().FindLsp(networkId, 
												(int)rowPathListObj.get("DemandId"), (String)rowPathListObj.get("Path")).getForwardingAdj();
			 	*/

				/** Only For => 10G Agg Conditions */
				if (DataPathConfigFileConstants.MPNClientSideOtnLsp == typeOfOtnLSP) {
					System.out.println("interfaceMapForLsp " + interfaceMapForLsp);

//					String oldinterfaceMapForLspKey = interfaceMapForLspKey;
//
//					String oldinterfaceMapForLspValue = interfaceMapForLsp.get(oldinterfaceMapForLspKey) + "";

//					forwardingAdjStr = String.valueOf(
//							Integer.parseInt(oldinterfaceMapForLspValue) + DataPathConfigFileConstants.TengAggFAOffset);
//					int add = interfaceMapForLspValue + DataPathConfigFileConstants.TengAggFAOffset;
					int add = dbService.getOtnLspInformationSerivce().FindMaxFA(networkId, otnLspInformationObj);
					forwardingAdjStr = String.valueOf( add);

//					interfaceMapForLspKey = oldinterfaceMapForLspKey + "#" + oldinterfaceMapForLspValue;
//
//					interfaceMapForLsp.put(interfaceMapForLspKey, Integer.parseInt(forwardingAdjStr));
					
//					DataPathConfigFileConstants.TengAggFAOffset++;
					

					//signalType = String.valueOf(DataPathConfigFileConstants.Signal_ODU2_10Gbps);

				} else if (DataPathConfigFileConstants.XGMLineSideOtnLsp == typeOfOtnLSP) {

//					interfaceMapForLspValue = interfaceMapForLsp.get(interfaceMapForLspKey) + "";
//
//					interfaceMapForLsp.put(interfaceMapForLspKey, interfaceMapForLsp.get(interfaceMapForLspKey)
//							+ DataPathConfigFileConstants.TengAggFAOffset);/** Update the key */
//
//					forwardingAdjStr = String.valueOf(interfaceMapForLsp.get(interfaceMapForLspKey));
					

				} else {/** It's a leaf node child OTNLSP */

//					if (null != interfaceMapForLsp.get(interfaceMapForLspKey + "#" + interfaceMapForLspValue)) {
//						interfaceMapForLspValue = interfaceMapForLsp
//								.get(interfaceMapForLspKey + "#" + interfaceMapForLspValue) + "";
//					} else {
//						interfaceMapForLspValue = interfaceMapForLsp.get(interfaceMapForLspKey) + "";
//					}
//					DataPathConfigFileConstants.TengAggFAOffset++;
					System.out.println(" TengAggFAOffset  == > " + DataPathConfigFileConstants.TengAggFAOffset);
					System.out.println(" otnlspinfo == > " + otnLspInformationObj.toString());
//					interfaceMapForLspValue = dbService.getOtnLspInformationSerivce().
//								FindLsp(networkId, otnLspInformationObj.getDemandId(), otnLspInformationObj.getPath(), otnLspInformationObj.getRoutePriority(), 
//										 otnLspInformationObj.getOtnLspTunnelId(), otnLspInformationObj.getLspId()).getForwardingAdj();
//					interfaceMapForLspValue +=  DataPathConfigFileConstants.TengAggFAOffset;
					
					if(typeOfOtnLSP == DataPathConfigFileConstants.Non10GAggClientSideOtnLsp) {
						
					}
					else {
						interfaceMapForLspValue = dbService.getOtnLspInformationSerivce().FindMaxFA(networkId, otnLspInformationObj);	
					}
					
					
					
					
					
					
					otnLspInformationObj.setForwardingAdj(interfaceMapForLspValue);
					
					try {
						dbService.getOtnLspInformationSerivce().UpdateOtnLspInformationFA(otnLspInformationObj);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					forwardingAdjStr = MapConstants.S_ZERO;

					//signalType = String.valueOf(DataPathConfigFileConstants.Signal_ODU0_10Gbps);
				}
				
				signalType =  FindSignalType(trafficType, typeOfOtnLSP);
				otnLspInformationObj.setForwardingAdj(Integer.parseInt(forwardingAdjStr));

				/** Update the global node by local node */
				pathSourceNode = pathLocalSourceNode;
				pathDestinationNode = pathLocalDestinationNode;
				pathDemandId = pathLocalDemandId;
				GPID = pathLocalGpid;
			

				/**
				 * Add into NodeArrayForNetworkInfo for NetworkDetails Tag
				 */
				NodeSetForNetworkInfo.add(Integer.parseInt(pathSourceNode));
				NodeSetForNetworkInfo.add(Integer.parseInt(pathDestinationNode));

				String pathProtectionType = rowPathListObj.get("ProtectionType").toString(), pathLspFlag = "";

				System.out.println("pathProtectionType : " + pathProtectionType);
				if (pathProtectionType.equalsIgnoreCase("none")) {
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_Unprotected);
				} else if (pathProtectionType.equalsIgnoreCase("1:1")) {
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_1_N_Protection);
				} else if (pathProtectionType.equalsIgnoreCase("1+1+R") | pathProtectionType.equalsIgnoreCase("1+1")) {
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_1_Plus_1_Bidirectional);
				} else if (pathProtectionType.equalsIgnoreCase("1+1+2R")) {
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_1_Plus_1_Bidirectional);
				} else if (pathProtectionType.equalsIgnoreCase("1:2R")) { /** As per the input */
					pathLspFlag = String.valueOf(DataPathConfigFileConstants.Lsp_Flag_1_N_Protection);
				}

				System.out.println(" pathLspFlag : " + pathLspFlag);

				// DataPathDetails->LSP->OTNTDMLSP->PathDetails
				Element PathDetails = doc.createElement("PathDetails");
				OTNTDMLSP.appendChild(PathDetails);

				/** Common Params to PathDetails Start */

				// DataPathDetails->LSP->OTNTDMLSP->PathDetails->TunnelId
				Element TunnelId = doc.createElement("TunnelId");
				TunnelId.appendChild(doc.createTextNode(String.valueOf(
						otnLspInformationObj.getOtnLspTunnelId())));/**
																	 * It would be 2 byte positive integer value. Unique
																	 * across source and destination
																	 */
				PathDetails.appendChild(TunnelId);

				// DataPathDetails->LSP->OTNTDMLSP->PathDetails->Source
				Element Source = doc.createElement("Source");
				Long sourceIp = dbService.getIpSchemeNodeService()
						.FindIpSchemeNode(networkId, Integer.parseInt(pathSourceNode)).getRouterIp();
				Source.appendChild(doc.createTextNode(IPv4.integerToStringIP(sourceIp)));
														/**
														 * It signifies the IPv4 address of the source nodes given at
														 * the time of Node setup
														 */
				PathDetails.appendChild(Source);

				// DataPathDetails->LSP->OTNTDMLSP->PathDetails->Destination
				Element Destination = doc.createElement("Destination");
				Long destinationIp = dbService.getIpSchemeNodeService()
						.FindIpSchemeNode(networkId, Integer.parseInt(pathDestinationNode)).getRouterIp();
				Destination.appendChild(doc.createTextNode(/* "192.168.115.241" */IPv4
						.integerToStringIP(destinationIp)));/**
															 * It signifies the IPv4 address of the destination nodes
															 * given at the time of Node setup
															 */
				PathDetails.appendChild(Destination);

				// DataPathDetails->LSP->OTNTDMLSP->PathDetails->LSPId
				Element LSPId = doc.createElement("LSPId");
				LSPId.appendChild(doc.createTextNode(String.valueOf(otnLspInformationObj
						.getLspId())));/** It would be 2 byte positive integer value, Different then TunnelId */
				PathDetails.appendChild(LSPId);

				/** ExplicitRoute Start */
				// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute
				Element ExplicitRoute = doc.createElement("ExplicitRoute");
				PathDetails.appendChild(ExplicitRoute);

				System.out.println(" nodeCount " + nodeCount);
				nodeCount = 2; /** DBG => for first and end ----- */
				int multiplier = 0;/** Number of circuits per OTNLSP */

				for (int i = 0; i < nodeCount; i++) {

					System.out.println("pathStringArray  : " + pathStringArray[i]);
					String nodeIPSTring = "";
					Long nodeIp;

					String nodeIp_string = "", IpPrefixLen_Str = "";
					int interFaceType;
					boolean lastNodeFlag = false; /**
													 * Enable for Last node so that InterfaceEncoding type can be set as
													 * 0 for
													 */

					if (i == MapConstants.I_ZERO) { /** First Node : Link Source IP */
						nodeIPSTring = pathOTNSourceNode;

						nodeIp = Long.parseLong(dbService
								.getIpSchemeLinkService().FindIpOfSrcNodeEnd_cf(networkId,
										Integer.parseInt(pathStringArray[i]), Integer.parseInt(pathStringArray[i + 1]))
								.toString());

						interFaceType = MapConstants.I_ONE; /** For First Node, IPV4 Type */
						IpPrefixLen_Str = DataPathConfigFileConstants.Link_IpPrefixLength;
					} else {/** Last Node : Node Router IP */
						nodeIPSTring = pathOTNDestinationNode;
						nodeIp = dbService.getIpSchemeNodeService()
								.FindIpSchemeNode(networkId, Integer.parseInt(nodeIPSTring)).getRouterIp();
						interFaceType = MapConstants.I_ZERO; /** For Rest of the Node */
						lastNodeFlag = true;
						IpPrefixLen_Str = DataPathConfigFileConstants.Node_IpPrefixLength;
					}
					nodeIp_string = String.valueOf(IPv4.integerToStringIP(nodeIp));

					/** Common Params to PathDetails End */
					{
						// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node
						Element Node = doc.createElement("Node");
						ExplicitRoute.appendChild(Node);
						{

							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->IP
							Element IP = doc.createElement("IP");
							System.out.println("nodeIp_string " + nodeIp_string);
							IP.appendChild(doc.createTextNode(
									nodeIp_string));/**
													 * The IP address field will carry an IP address of a link or an IP
													 * address associated with the source router of the span.
													 */
							Node.appendChild(IP);

							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->InterfaceType
							Element InterfaceType = doc.createElement("InterfaceType");
							InterfaceType.appendChild(doc.createTextNode(
									String.valueOf(interFaceType)));/** 0 for unnumbered and 1 for IPV4 */
							Node.appendChild(InterfaceType);

							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->InterfaceId

							String linkDirection_U = "", linkDirection_D = "";
							System.out.println(" OTN : " + pathOTNSourceNode + " and " + pathOTNDestinationNode);

							
							/**
							 * DBG => As of now direction created based on the next node only (as per the
							 * internal discussion), further discussion required => On Jan 11,2018 it was
							 * cleared from GMPLS -> Now direction assigned based on first and lst node in
							 * case of OTN
							 */
							if (i == nodeCount - 1) {/** For Last Node in the Path */
								linkDirection_U = dbService.getLinkService().FindLinkDirection(networkId,
										Integer.parseInt(pathStringArray[i - 1]),
										Integer.parseInt(pathStringArray[i]));/** TRUE */
								linkDirection_D = dbService.getLinkService().FindLinkDirection(networkId,
										Integer.parseInt(
												pathStringArray[/* i */pathStringArray.length - MapConstants.I_ONE]),
										Integer.parseInt(pathStringArray[/* i-1 */pathStringArray.length
												- MapConstants.I_TWO]));/** */

								System.out.println("if : linkDirection_U " + linkDirection_U + " linkDirection_D"
										+ linkDirection_D);
								System.out.println(" wavelenghtNo " + wavelenghtNo);
							} else {/** For Rest of the node in the Path : in this case the First node */
								linkDirection_U = dbService.getLinkService().FindLinkDirection(networkId,
										Integer.parseInt(
												pathStringArray[/* i+1 */pathStringArray.length - MapConstants.I_ONE]),
										Integer.parseInt(pathStringArray[/* i */pathStringArray.length
												- MapConstants.I_TWO]));/** */
								linkDirection_D = dbService.getLinkService().FindLinkDirection(networkId,
										Integer.parseInt(pathStringArray[i]),
										Integer.parseInt(pathStringArray[i + 1]));/** TRUE */

								System.out.println("else : linkDirection_U " + linkDirection_U + " linkDirection_D"
										+ linkDirection_D);
								System.out.println(" wavelenghtNo " + wavelenghtNo);
							}

							

							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->InterfaceId
							Element InterfaceId = doc.createElement("InterfaceId");
							InterfaceId.appendChild(
									doc.createTextNode(String.valueOf(interfaceMapForLspValue)));/** As per the GMPLS input */
							Node.appendChild(InterfaceId);

							forwardingAdgencyLocalCounter = interfaceMapForLsp.get(interfaceMapForLspKey);

							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->IpPrefixLen
							Element IpPrefixLen = doc.createElement("IpPrefixLen");
							IpPrefixLen.appendChild(
									doc.createTextNode(IpPrefixLen_Str));/** For Link 30 Bits and Node 29 bits */
							Node.appendChild(IpPrefixLen);

							// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel
							Element GeneralisedLabel = doc.createElement("GeneralisedLabel");
							Node.appendChild(GeneralisedLabel);

							/** GeneralisedLabel Start */
							{

								List<TpnPortInfo> tpnUpStreamPortInfoList = null;/**
																					 * Initialised here due to later
																					 * reference of 0th value => Need to
																					 * check for specific
																					 */
								List<TpnPortInfo> tpnDownStreamPortInfoList = null;/**
																					 * Initialised here due to later
																					 * reference of 0th value => Need to
																					 * check for specific
																					 */

								// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream
								Element Upstream = doc.createElement("Upstream");
								GeneralisedLabel.appendChild(Upstream);

								/** Upstream Start */
								{

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->Wavelength
									Element Wavelength_U = doc.createElement("Wavelength");
									Wavelength_U.appendChild(doc.createTextNode(wavelenghtNo));
									Upstream.appendChild(Wavelength_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->Direction
									Element Direction_U = doc.createElement("Direction");
									Direction_U.appendChild(doc.createTextNode(
											String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap
													.get(linkDirection_U))/* "1" */));
									Upstream.appendChild(Direction_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->EncodingType
									Element EncodingType_U = doc.createElement("EncodingType");
									if (!lastNodeFlag)
										EncodingType_U.appendChild(doc.createTextNode(
												DataPathConfigFileConstants.InterfaceEncodingType));/**
																									 * Lambda (photonic)
																									 * LSP is at lambda
																									 * level
																									 */
									else
										EncodingType_U.appendChild(doc.createTextNode(String.valueOf(
												MapConstants.I_ZERO)));/** Lambda (photonic) LSP is at lambda level */

									Upstream.appendChild(EncodingType_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->SwitchingType
									Element SwitchingType_U = doc.createElement("SwitchingType");
									SwitchingType_U.appendChild(doc.createTextNode(
											DataPathConfigFileConstants.SwitchingType));/**
																						 * Lambda-Switch Capable (LSC)
																						 */
									Upstream.appendChild(SwitchingType_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->GPID
									Element GPID_U = doc.createElement("GPID");
									GPID_U.appendChild(doc.createTextNode(String.valueOf(GPID)));
																/**
																 * DBG => Mapping For Ethernet Client, need to check for
																 * other => checked for OTU2
																 */
									Upstream.appendChild(GPID_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->TPN

									Element TPN_U = doc.createElement("TPN");
									TPN_U.appendChild(doc.createTextNode("0"));/** DBG => As per the input from GMPLS */
									Upstream.appendChild(TPN_U);

									System.out.println("rowPathListObj " + rowPathListObj.toString());
									/**
									 * Just taking first circuit Id from the array for common param since config
									 * would be same
									 */
									String[] groupedCircuitIdArray = rowPathListObj.get("CircuitId").toString()
											.split(",");
									System.out.println("groupedCircuitId : "
											+ Integer.parseInt(groupedCircuitIdArray[MapConstants.I_ZERO])
											+ " and its length : " + groupedCircuitIdArray.length);

									multiplier = groupedCircuitIdArray.length;/** Number of circuits per OTNLSP */

									for (String groupedCircuitIdString : groupedCircuitIdArray) {

										System.out.println("groupedCircuitIdString : " + groupedCircuitIdString);

										List<PortInfo> circuitPortInfoMappingList = dbService.getPortInfoService()
												.FindCircuitPortInfo(networkId, nodeId,
														Integer.parseInt(groupedCircuitIdString), typeOfOtnLSP);

										System.out
												.println("circuitPortInfoMappingList : " + circuitPortInfoMappingList);

										for (PortInfo circuitPortInfoMappingListObj : circuitPortInfoMappingList) {/**
																													 * Added
																													 * as
																													 * an
																													 * outer
																													 * layer
																													 * for
																													 * circuit
																													 * id
																													 * mapping
																													 */

											String portMappingPortNumber = "";

											System.out.println(
													"circuitPortInfoMappingListObj " + circuitPortInfoMappingListObj);

											tpnUpStreamPortInfoList = dbService.getTpnPortInfoService().FindAll(
													networkId, nodeId, circuitPortInfoMappingListObj.getRack(),
													circuitPortInfoMappingListObj.getSbrack(),
													circuitPortInfoMappingListObj.getCard(),
													circuitPortInfoMappingListObj.getPort(),
													DataPathConfigFileConstants.UpStream, typeOfOtnLSP);



											System.out.println("tpnUpStreamPortInfoList after it otnlsp : "
													+ tpnUpStreamPortInfoList);

										

											System.out.println(" tpnUpStreamPortInfoList : " + tpnUpStreamPortInfoList
													+ " and Size : " + tpnUpStreamPortInfoList.size());

											System.out.println("LENGTH =====> " + tpnUpStreamPortInfoList.size());

											for (int tpnPortInfoListCnt = 0; tpnPortInfoListCnt < tpnUpStreamPortInfoList
													.size(); tpnPortInfoListCnt++) {

												Element PortMapping_U = doc.createElement("PortMapping");
												/// PortMapping_U.appendChild(doc.createTextNode("0"));/** */
												Upstream.appendChild(PortMapping_U);

												// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->PortMapping

												// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->PortMapping->PortNumber
												Element PortNumber_U = doc.createElement("PortNumber");
												PortNumber_U.appendChild(
														doc.createTextNode(String.valueOf(tpnUpStreamPortInfoList
																.get(tpnPortInfoListCnt).getPortId())));/** DB get */
												PortMapping_U.appendChild(PortNumber_U);

												// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->PortMapping->TsGroupId
												Element TsGroupId_U = doc.createElement("TsGroupId");
												TsGroupId_U.appendChild(doc.createTextNode(
														String.valueOf(tpnUpStreamPortInfoList.get(tpnPortInfoListCnt)
																.getPortId())));/**
																				 * TRibutary slot id : same as port
																				 * number
																				 */
												PortMapping_U.appendChild(TsGroupId_U);

												System.out.println("PORTID =========> " + String.valueOf(
														tpnUpStreamPortInfoList.get(tpnPortInfoListCnt).getPortId()));

											}

										}
									}
								}
								/** Upstream End */

								/** Downstream Start */
								// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream
								Element Downstream = doc.createElement("Downstream");
								GeneralisedLabel.appendChild(Downstream);
								{

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->Wavelength
									Element Wavelength_D = doc.createElement("Wavelength");
									Wavelength_D.appendChild(doc.createTextNode(wavelenghtNo));
									Downstream.appendChild(Wavelength_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->Direction
									Element Direction_D = doc.createElement("Direction");
									Direction_D.appendChild(doc.createTextNode(
											String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap
													.get(linkDirection_D))/* "2" */));/** */
									Downstream.appendChild(Direction_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->EncodingType
									Element EncodingType_D = doc.createElement("EncodingType");
									if (!lastNodeFlag)
										EncodingType_D.appendChild(doc.createTextNode(
												DataPathConfigFileConstants.InterfaceEncodingType));/**
																									 * Lambda (photonic)
																									 * LSP is at lambda
																									 * level
																									 */
									else
										EncodingType_D.appendChild(doc.createTextNode(String.valueOf(
												MapConstants.I_ZERO)));/** Lambda (photonic) LSP is at lambda level */

									Downstream.appendChild(EncodingType_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->SwitchingType
									Element SwitchingType_D = doc.createElement("SwitchingType");
									SwitchingType_D.appendChild(doc.createTextNode(
											DataPathConfigFileConstants.SwitchingType));/**
																						 * Lambda-Switch Capable (LSC)
																						 */
									Downstream.appendChild(SwitchingType_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->GPID
									Element GPID_D = doc.createElement("GPID");
									GPID_D.appendChild(doc.createTextNode(/* "54" */String.valueOf(
											GPID)));/** DBG => Mapping For Ethernet Client, need to check for other */
									Downstream.appendChild(GPID_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->Grid
									Element Grid_D = doc.createElement("Grid");
									Grid_D.appendChild(doc.createTextNode("1"));/** */
									Downstream.appendChild(Grid_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->TPN
									Element TPN_D = doc.createElement("TPN");
									TPN_D.appendChild(doc.createTextNode("0"));/** DBG => Needed Explanation */
									Downstream.appendChild(TPN_D);

									/**
									 * Just taking first circuit Id from the array for common param since config
									 * would be same
									 */
									String[] groupedCircuitIdArray = rowPathListObj.get("CircuitId").toString()
											.split(",");
									System.out.println("groupedCircuitId : "
											+ Integer.parseInt(groupedCircuitIdArray[MapConstants.I_ZERO])
											+ " and its length : " + groupedCircuitIdArray.length);

									multiplier = groupedCircuitIdArray.length;/** Number of circuits per OTNLSP */

									for (String groupedCircuitIdString : groupedCircuitIdArray) {

										List<PortInfo> circuitPortInfoMappingList = dbService.getPortInfoService()
												.FindCircuitPortInfo(networkId, nodeId,
														Integer.parseInt(groupedCircuitIdString), typeOfOtnLSP);

										for (PortInfo circuitPortInfoMappingListObj : circuitPortInfoMappingList) {/**
																													 * Added
																													 * as
																													 * an
																													 * outer
																													 * layer
																													 * for
																													 * circuit
																													 * id
																													 * mapping
																													 */

											tpnDownStreamPortInfoList = dbService.getTpnPortInfoService().FindAll(
													networkId, nodeId, circuitPortInfoMappingListObj.getRack(),
													circuitPortInfoMappingListObj.getSbrack(),
													circuitPortInfoMappingListObj.getCard(),
													circuitPortInfoMappingListObj.getPort(),
													DataPathConfigFileConstants.DownStream, typeOfOtnLSP);



											System.out.println("tpnDownStreamPortInfoList after it otnlsp : "
													+ tpnDownStreamPortInfoList);

											/*
											 * dbService.getTpnPortInfoService().FindAll(networkId,
											 * Integer.parseInt(nodeIPSTring), DataPathConfigFileConstants.DownStream);
											 */

											for (int tpnPortInfoListCnt = 0; tpnPortInfoListCnt < tpnDownStreamPortInfoList
													.size(); tpnPortInfoListCnt++) {
												Element PortMapping_D = doc.createElement("PortMapping");
												/// PortMapping_D.appendChild(doc.createTextNode("0"));/** */
												Downstream.appendChild(PortMapping_D);

												// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->PortMapping

												// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->PortMapping->PortNumber
												Element PortNumber_D = doc.createElement("PortNumber");
												PortNumber_D.appendChild(
														doc.createTextNode(String.valueOf(tpnDownStreamPortInfoList
																.get(tpnPortInfoListCnt).getPortId())));/**  */
												PortMapping_D.appendChild(PortNumber_D);

												// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->PortMapping->TsGroupId
												Element TsGroupId_D = doc.createElement("TsGroupId");
												TsGroupId_D.appendChild(
														doc.createTextNode(String.valueOf(tpnDownStreamPortInfoList
																.get(tpnPortInfoListCnt).getPortId())));/**  */
												PortMapping_D.appendChild(TsGroupId_D);

											}

										}
									}

									System.out.println(" tpnUpStreamPortInfoList " + tpnUpStreamPortInfoList);

									System.out.println(" tpnUpStreamPortInfoList.get(0).getProtectionSubType() "
											+ tpnUpStreamPortInfoList.get(0).getProtectionSubType());
									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->ProtectionSubType
									Element ProtectionSubType_U = doc.createElement("ProtectionSubType");
									ProtectionSubType_U.appendChild(doc.createTextNode(String.valueOf(/*
																										 * DataPathConfigFileConstants
																										 * .
																										 * ProtectionsubType_YCable
																										 */
											tpnUpStreamPortInfoList.get(0).getProtectionSubType())));/**  */
									Upstream.appendChild(ProtectionSubType_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->IsRevertive
									Element IsRevertive_U = doc.createElement("IsRevertive");
									IsRevertive_U.appendChild(doc.createTextNode(String.valueOf(/*
																								 * DataPathConfigFileConstants
																								 * .IsRevertive_Yes
																								 */
											tpnUpStreamPortInfoList.get(0).getIsRevertive())));/**  */
									Upstream.appendChild(IsRevertive_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->FecType
									Element FecType_U = doc.createElement("FecType");
									FecType_U.appendChild(doc.createTextNode(String.valueOf(/*
																							 * DataPathConfigFileConstants
																							 * .FEC_Type_G709
																							 */
											/* tpnUpStreamPortInfoList.get(0).getFecType() */
											DataPathConfigFileConstants.FEC_Type_G709)));/**  */
									Upstream.appendChild(FecType_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->FecStatus
									Element FecStatus_U = doc.createElement("FecStatus");
									FecStatus_U.appendChild(doc.createTextNode(String.valueOf(/*
																								 * DataPathConfigFileConstants
																								 * .FEC_Status_Enable
																								 */
											/* tpnUpStreamPortInfoList.get(0).getFecStatus() */
											DataPathConfigFileConstants.FEC_Status_Disable)));/**
																								 * Changed to Disable
																								 * Mode As per the Mail
																								 * Input
																								 */
									Upstream.appendChild(FecStatus_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->TxSegment
									Element TxSegment_U = doc.createElement("TxSegment");
									TxSegment_U.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnUpStreamPortInfoList.get(0).getTxSegment())));/**  */
									Upstream.appendChild(TxSegment_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->OperatorSpecific
									Element OperatorSpecific_U = doc.createElement("OperatorSpecific");
									OperatorSpecific_U.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnUpStreamPortInfoList.get(0).getOperatorSpecific())));/**  */
									Upstream.appendChild(OperatorSpecific_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->TimDectMode
									Element TimDectMode_U = doc.createElement("TimDectMode");
									TimDectMode_U.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnUpStreamPortInfoList.get(0).getTimDectMode())));/**  */
									Upstream.appendChild(TimDectMode_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->TCMActStatus
									Element TCMActStatus_U = doc.createElement("TCMActStatus");
									TCMActStatus_U.appendChild(doc.createTextNode(String.valueOf(/*
																									 * DataPathConfigFileConstants
																									 * .
																									 * TCMActStatus_ENABLE
																									 */
											tpnUpStreamPortInfoList.get(0).getTCMActStatus())));/**  */
									Upstream.appendChild(TCMActStatus_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->TCMActValue
									Element TCMActValue_U = doc.createElement("TCMActValue");
									TCMActValue_U.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnUpStreamPortInfoList.get(0).getTCMActValue())));/**  */
									Upstream.appendChild(TCMActValue_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->TxTCMMode
									Element TxTCMMode_U = doc.createElement("TxTCMMode");
									TxTCMMode_U.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnUpStreamPortInfoList.get(0).getTxTCMMode())));/**  */
									Upstream.appendChild(TxTCMMode_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->TxTCMPriority
									{
										String[] txTcmPriorityStr = tpnUpStreamPortInfoList.get(0).getTxTCMPriority()
												.split("-");
										/** For All 8-Bytes : DBG => As of now value filled randomly as per the input */
										for (String tempTxTcmPriorityStr : txTcmPriorityStr) {
											Element TxTCMPriority_U = doc.createElement("TxTCMPriority");
											TxTCMPriority_U.appendChild(
													doc.createTextNode(/* "1" */tempTxTcmPriorityStr));/**  */
											Upstream.appendChild(TxTCMPriority_U);
										}
									}

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->GccType
									Element GccType_U = doc.createElement("GccType");
									GccType_U.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnUpStreamPortInfoList.get(0).getGccType())));/** */
									Upstream.appendChild(GccType_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->GccStatus
									Element GccStatus_U = doc.createElement("GccStatus");
									GccStatus_U.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnUpStreamPortInfoList.get(0).getGccStatus())));/** */
									Upstream.appendChild(GccStatus_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->UpStream->GccValue
									Element GccValue_U = doc.createElement("GccValue");
									GccValue_U.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnUpStreamPortInfoList.get(0).getGccValue())));/** */
									Upstream.appendChild(GccValue_U);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->ProtectionSubType
									Element ProtectionSubType_D = doc.createElement("ProtectionSubType");
									ProtectionSubType_D.appendChild(doc.createTextNode(String
											.valueOf(DataPathConfigFileConstants.ProtectionsubType_YCable)));/**  */
									Downstream.appendChild(ProtectionSubType_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->IsRevertive
									Element IsRevertive_D = doc.createElement("IsRevertive");
									IsRevertive_D.appendChild(doc.createTextNode(
											String.valueOf(DataPathConfigFileConstants.IsRevertive_Yes)));/**  */
									Downstream.appendChild(IsRevertive_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->FecType
									Element FecType_D = doc.createElement("FecType");
									FecType_D.appendChild(doc.createTextNode(
											String.valueOf(DataPathConfigFileConstants.FEC_Type_G709)));/**  */
									Downstream.appendChild(FecType_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->FecStatus
									Element FecStatus_D = doc.createElement("FecStatus");
									FecStatus_D.appendChild(doc.createTextNode(String.valueOf(
											DataPathConfigFileConstants.FEC_Status_Disable)));/**
																								 * Changed to Disable
																								 * Mode As per the Mail
																								 * Input
																								 */
									Downstream.appendChild(FecStatus_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->TxSegment
									Element TxSegment_D = doc.createElement("TxSegment");
									TxSegment_D.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnDownStreamPortInfoList.get(0).getTxSegment())));/**  */
									Downstream.appendChild(TxSegment_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->OperatorSpecific
									Element OperatorSpecific_D = doc.createElement("OperatorSpecific");
									OperatorSpecific_D.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnDownStreamPortInfoList.get(0).getOperatorSpecific())));/**  */
									Downstream.appendChild(OperatorSpecific_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->TimDectMode
									Element TimDectMode_D = doc.createElement("TimDectMode");
									TimDectMode_D.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnDownStreamPortInfoList.get(0).getTimDectMode())));/**  */
									Downstream.appendChild(TimDectMode_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->TCMActStatus
									Element TCMActStatus_D = doc.createElement("TCMActStatus");
									TCMActStatus_D.appendChild(doc.createTextNode(String.valueOf(/*
																									 * DataPathConfigFileConstants
																									 * .
																									 * TCMActStatus_ENABLE
																									 */
											tpnDownStreamPortInfoList.get(0).getTCMActStatus())));/**  */
									Downstream.appendChild(TCMActStatus_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->TCMActValue
									Element TCMActValue_D = doc.createElement("TCMActValue");
									TCMActValue_D.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnDownStreamPortInfoList.get(0).getTCMActValue())));/**  */
									Downstream.appendChild(TCMActValue_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->TxTCMMode
									Element TxTCMMode_D = doc.createElement("TxTCMMode");
									TxTCMMode_D.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnDownStreamPortInfoList.get(0).getTxTCMMode())));/**  */
									Downstream.appendChild(TxTCMMode_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->TxTCMPriority

									{
										/** For All 8-Bytes : DBG => As of now value filled randomly as per the input */

										Element TxTCMPriority_D_1 = doc.createElement("TxTCMPriority");
										TxTCMPriority_D_1.appendChild(doc.createTextNode("1"));/**  */
										Downstream.appendChild(TxTCMPriority_D_1);

										Element TxTCMPriority_D_2 = doc.createElement("TxTCMPriority");
										TxTCMPriority_D_2.appendChild(doc.createTextNode("2"));/**  */
										Downstream.appendChild(TxTCMPriority_D_2);

										Element TxTCMPriority_D_3 = doc.createElement("TxTCMPriority");
										TxTCMPriority_D_3.appendChild(doc.createTextNode("3"));/**  */
										Downstream.appendChild(TxTCMPriority_D_3);

										Element TxTCMPriority_D_4 = doc.createElement("TxTCMPriority");
										TxTCMPriority_D_4.appendChild(doc.createTextNode("4"));/**  */
										Downstream.appendChild(TxTCMPriority_D_4);

										Element TxTCMPriority_D_5 = doc.createElement("TxTCMPriority");
										TxTCMPriority_D_5.appendChild(doc.createTextNode("5"));/**  */
										Downstream.appendChild(TxTCMPriority_D_5);

										Element TxTCMPriority_D_6 = doc.createElement("TxTCMPriority");
										TxTCMPriority_D_6.appendChild(doc.createTextNode("6"));/**  */
										Downstream.appendChild(TxTCMPriority_D_6);

										Element TxTCMPriority_D_7 = doc.createElement("TxTCMPriority");
										TxTCMPriority_D_7.appendChild(doc.createTextNode("7"));/**  */
										Downstream.appendChild(TxTCMPriority_D_7);

										Element TxTCMPriority_D_8 = doc.createElement("TxTCMPriority");
										TxTCMPriority_D_8.appendChild(doc.createTextNode("8"));/**  */
										Downstream.appendChild(TxTCMPriority_D_8);

									}

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->GccType
									Element GccType_D = doc.createElement("GccType");
									GccType_D.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnDownStreamPortInfoList.get(0).getGccType())));/** */
									Downstream.appendChild(GccType_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->GccStatus
									Element GccStatus_D = doc.createElement("GccStatus");
									GccStatus_D.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnDownStreamPortInfoList.get(0).getGccStatus())));/** */
									Downstream.appendChild(GccStatus_D);

									// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExplicitRoute->Node->GeneralisedLabel->Downstream->GccValue
									Element GccValue_D = doc.createElement("GccValue");
									GccValue_D.appendChild(doc.createTextNode(/* "0" */String
											.valueOf(tpnDownStreamPortInfoList.get(0).getGccValue())));/** */
									Downstream.appendChild(GccValue_D);
								}
								/** Downstream End */
							}
							/** GeneralisedLabel End */
						}

						/** ExplicitRoute End */
					}
				}

				/** Common Params to PathDetails Start */

				{
					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->SetUpPriority
					Element SetUpPriority = doc.createElement("SetUpPriority");
					SetUpPriority.appendChild(doc.createTextNode("4"));/** */
					PathDetails.appendChild(SetUpPriority);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->HoldPriority
					Element HoldPriority = doc.createElement("HoldPriority");
					HoldPriority.appendChild(doc.createTextNode("4"));/** */
					PathDetails.appendChild(HoldPriority);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->ExcludeAny
					Element ExcludeAny = doc.createElement("ExcludeAny");
					ExcludeAny.appendChild(doc.createTextNode("0"));/** As per the input */
					PathDetails.appendChild(ExcludeAny);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->IncludeAny
					Element IncludeAny = doc.createElement("IncludeAny");
					IncludeAny.appendChild(doc.createTextNode("0"));/** */
					PathDetails.appendChild(IncludeAny);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->IncludeAll
					Element IncludeAll = doc.createElement("IncludeAll");
					IncludeAll.appendChild(doc.createTextNode("0"));/** */
					PathDetails.appendChild(IncludeAll);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->forwardingAdgency
					Element ForwardingAgency = doc
							.createElement("ForwardingAdjacency"); /**
																	 * This field indicates that whether this LSP would
																	 * be created as hierarchical LSP or not, thus
																	 * enabling other LSP to use it as a TE-LINK
																	 */
					ForwardingAgency.appendChild(doc.createTextNode(forwardingAdjStr));/** As per the GMPLS input */
					PathDetails.appendChild(ForwardingAgency);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->Secondary
					Element Secondary = doc.createElement("Secondary");
					Secondary.appendChild(doc.createTextNode(secondaryString));/** For Restoration It should be 1 */
					PathDetails.appendChild(Secondary);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->Protecting
					Element Protecting = doc.createElement("Protecting");
					Protecting.appendChild(doc.createTextNode(protecting));/** For protecting : 1, non-protecting:0 */
					PathDetails.appendChild(Protecting);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->Notification
					Element Notification = doc.createElement("Notification");
					Notification.appendChild(doc.createTextNode(
							String.valueOf(DataPathConfigFileConstants.Notification_Flag)));/** Set as 1 */
					PathDetails.appendChild(Notification);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->LSPFlags
					Element LSPFlags = doc.createElement("LSPFlags");
					LSPFlags.appendChild(doc.createTextNode(pathLspFlag));
					PathDetails.appendChild(LSPFlags);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->LinkFlags
					Element LinkFlags = doc.createElement("LinkFlags");
					LinkFlags.appendChild(doc.createTextNode(
							String.valueOf(DataPathConfigFileConstants.Link_flags_Unprotected)));/** As per the input */
					PathDetails.appendChild(LinkFlags);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->Flags
					Element Flags = doc.createElement("Flags");
					Flags.appendChild(doc.createTextNode("02"));/** */
					PathDetails.appendChild(Flags);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->SignalType
					Element SignalType = doc.createElement("SignalType");
					SignalType.appendChild(doc.createTextNode(signalType));/** */
					PathDetails.appendChild(SignalType);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->NMC
					Element NMC = doc.createElement("NMC");
					NMC.appendChild(doc.createTextNode("8"));/** As per the Input */
					PathDetails.appendChild(NMC);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->NVC
					Element NVC = doc.createElement("NVC");
					NVC.appendChild(doc.createTextNode("0"));/** As per the Input */
					PathDetails.appendChild(NVC);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->MT
					Element MT = doc.createElement("MT");
					MT.appendChild(doc.createTextNode(/* "1" */String
							.valueOf(multiplier)));/**
													 * DBG => As of now no multiplier support so same fields will be
													 * repeated => Support Added after Last Discussion for service type
													 */
					PathDetails.appendChild(MT);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->BitRate
					Element BitRate = doc.createElement("BitRate");
					BitRate.appendChild(doc.createTextNode("0"));/** As per the Input */
					PathDetails.appendChild(BitRate);

					// DataPathDetails->LSP->OTNTDMLSP->PathDetails->LSPName
					Element LSPName = doc.createElement("LSPName");
					LSPName.appendChild(doc.createTextNode(lspLabelName));/** DBG => Need to Discuss it with GMPLS */
					PathDetails.appendChild(LSPName);
				}

				/** Common Params to PathDetails End */

				/// forwardingAdgencyLocalCounter++;

				/** Finaly Insert the Entry into Database */
				SetOtnLspInformation(Integer.parseInt(TYPE.getFirstChild().getNodeValue()), rowPathListObj,
						otnLspInformationObj, dbService);

			}
			/** OTNTDMLSP Tag End */

			DataPathConfigFileConstants.forwardingAdjacencyGlobal = forwardingAdgencyLocalCounter + MapConstants.I_ONE;
			System.out.println(" Final DataPathConfigFileConstants.forwardingAdjacencyGlobal "
					+ DataPathConfigFileConstants.forwardingAdjacencyGlobal);
		}


	}
		
	
	
	
	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateLSPTagDataPathFileXML(int networkId, int nodeId, int nodeDegree, DbService dbService, Document doc, Element rootElement){

				
		// DataPathDetails->LSP
		Element LSP  = doc.createElement("LSP");
		rootElement.appendChild(LSP);	
		
		
		HashMap<String, Integer> interfaceMapForLsp = new HashMap<>();
		
		/** Generate LambdaLSP tag */
		generateLambdaLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, dbService, doc, LSP, interfaceMapForLsp);
		
		/** Generate Deleted LAMBDA LSP tag : Only if its a delete case in brownfield*/
		if(MapConstants.BrownField.equalsIgnoreCase(currentNetworkType)) {
		
			List<LambdaLspInformation> deletedLambdaLspList =    dbService.getLambdaLspInformationSerivce().FindDeletedLambdaLspInBrField
					(greenFieldNetworkId, brownFieldNetworkId, nodeId);
			
			if(! deletedLambdaLspList.isEmpty())			
				generateDeletedLambdaLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, deletedLambdaLspList, dbService, doc, LSP);
		}			
		
		
		DataPathConfigFileConstants.tunnelIdGlobal++;   /** Update the Tunnel Id by one as per the input from GMPLS*/
		
		System.out.println(" Final interfaceMapForLsp Before 10G "+ interfaceMapForLsp);		
		
		/** Generate Service/OTNTDLSP tag */
		List<Map<String, Object>> pathList    = dbService.getNetworkRouteService().
												FindRoutesWithServiceBasedNodeEnds_cf(networkId, nodeId);


		

		System.out.println(" Final interfaceMapForLsp : "+ interfaceMapForLsp);

		/**Check for the 10G Agg List */		
		List<Map<String, Object>> XgMClientList  = dbService.getCircuit10gAggService().FindRoutesWithServiceBasedNodeEnds10GAgg(networkId, nodeId);


		
		System.out.println(pathList.isEmpty() + " and " + XgMClientList.isEmpty() + " and NodeId "+ nodeId);
		if(!pathList.isEmpty() && XgMClientList.isEmpty()){/**CASE : Non 10G Agg  */
			System.out.println(" First condition ");
			generateOTNTDLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, dbService, doc, LSP, interfaceMapForLsp, pathList,DataPathConfigFileConstants.Non10GAggClientSideOtnLsp);
		}
		
		if(!XgMClientList.isEmpty()){ /**In case of 10G Agg, two otn lsps need to be created : one for line side and the other for client side */

			if(!pathList.isEmpty()){ /**Mix Case with 10GAgg and Non 10GAgg Client*/
				System.out.println(" Second condition ");
				List<Map<String, Object>> pathNon10gAggList    = dbService.getNetworkRouteService().
											FindRoutesWithServiceBasedNodeEnds_PureNon10gAgg(networkId, nodeId);
				System.out.println("pathNon10gAggList "+pathNon10gAggList);											

				generateOTNTDLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, dbService, doc, LSP, interfaceMapForLsp, pathNon10gAggList,DataPathConfigFileConstants.Non10GAggClientSideOtnLsp);
				
				List<Map<String, Object>> MpnClientList    = dbService.getNetworkRouteService().
										FindRoutesWithServiceBasedNodeEnds_Pure10gAgg(networkId, nodeId);

				System.out.println("MpnClientList : "+MpnClientList);
				
				List<Map<String, Object>> CombinedList =   CombineXgmClientWithMpnClient(XgMClientList, MpnClientList);
				
				///generateOTNTDLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, dbService, doc, LSP, interfaceMapForLsp, MpnClientList,DataPathConfigFileConstants.MPNClientSideOtnLsp);/**MPN Client Side*/
				System.out.println("CombinedList "+ CombinedList);
				
				CombinedList.forEach(Map-> {
					List<Map<String, Object>> IndividualList = new ArrayList<>();
					IndividualList.add(Map);
					//System.out.println("IndividualList ==> "+ IndividualList);
					System.out.println(" interfaceMapForLsp "+ interfaceMapForLsp);
					generateOTNTDLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, dbService, doc, LSP, interfaceMapForLsp, IndividualList, (int)Map.get("TypeOfOtnLsp"));	
					IndividualList.clear();
				});
				
				
			}			

			///generateOTNTDLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, dbService, doc, LSP, interfaceMapForLsp, pathList,DataPathConfigFileConstants.MPNClientSideOtnLsp);/**MPN Client Side*/
			///generateOTNTDLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, dbService, doc, LSP, interfaceMapForLsp, pathListFor10GAgg, DataPathConfigFileConstants.XGMLineSideOtnLsp);/**XGM Line Side*/
			////generateOTNTDLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, dbService, doc, LSP, interfaceMapForLsp, XgMClientList, DataPathConfigFileConstants.XGMClientSideOtnLsp);/**XGM Client Side*/
	
		}
		
		
			

		
		
		/** Generate Deleted Service/OTNTDLSP tag : Only if its a delete case in brownfield*/
		if(MapConstants.BrownField.equalsIgnoreCase(currentNetworkType)) {
		
			List<Map<String,Object>> deletedOtnLspList =    dbService.getOtnLspInformationSerivce().FindDeletedOtnLspInBrField
					(greenFieldNetworkId, brownFieldNetworkId, nodeId);
			
			if(! deletedOtnLspList.isEmpty())			
				generateDeletedOTNTDLSPTagDataPathFileXML(networkId, nodeId, nodeDegree, deletedOtnLspList, dbService, doc, LSP, interfaceMapForLsp);
		}
		


		/** In case of Delete Lambda,  */

	}
	
	
	
	@SuppressWarnings("null")
	public List<Map<String, Object>> CombineXgmClientWithMpnClient(List<Map<String, Object>> XgMClientList, List<Map<String, Object>> MpnClientList) {
		
		List<Map<String, Object>> CombinedList = new ArrayList<Map<String, Object>>();
		
		System.out.println("XgMClientList => "+ XgMClientList);
		System.out.println("MpnClientList => "+ MpnClientList);

		XgMClientList.forEach(Map-> {
			Map.put("TypeOfOtnLsp", DataPathConfigFileConstants.XGMClientSideOtnLsp);
		});
		
		MpnClientList.forEach(			
						
						Map -> {
							
							System.out.println("MpnClient Map "+ Map.toString());
//							System.out.println(XgMClientList
//									.stream()
//									.filter(xgmMapObj ->  Integer.parseInt(xgmMapObj.get("CircuitId").toString()) == 7)
//									.collect(Collectors.toList()));
							
							Map.put("TypeOfOtnLsp", DataPathConfigFileConstants.MPNClientSideOtnLsp);
							
							CombinedList.add(Map);
							CombinedList.addAll(
								XgMClientList
									.stream()
									.filter(xgmMapObj ->  xgmMapObj.get("CircuitOriginalId").toString().equalsIgnoreCase(Map.get("CircuitId").toString()) 
														  &&
														  xgmMapObj.get("Path").toString().equalsIgnoreCase(Map.get("Path").toString())
										  )
									.collect(Collectors.toList())
							);							

						}
				
					);
		
		
		XgMClientList.forEach(			
				
				Map -> {
					
					System.out.println("XgmClient Map "+ Map.toString());
					
				}
		
			);
		
//		XgmCl
//		.stream()
//		.filter(rowElement -> rowElement.getWavelengthNo() == (int) teAllLambdaPerLinkListObj.get(localJ).get("Wavelength"))
//		.collect(Collectors.toList())
//		.isEmpty()	
		
		
		
		
		CombinedList.forEach(Map -> {System.out.println(Map);});
		///System.out.println(1/0);
		return CombinedList;
	}
	

	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateTPNDetailsTagDataPathFileXML(int nodeId, int networkId, DbService dbService, Document doc, Element rootElement){
	
		List<CardInfo> cardInfoList = dbService.getCardInfoService().findMpns(networkId, nodeId);		
		
		 
//		if(currentNetworkType.equalsIgnoreCase(MapConstants.BrownField)) {
//			cardInfoList.addAll(dbService.getCardInfoService().FindDeletedCardsInBrField(greenFieldNetworkId, 
//						brownFieldNetworkId, nodeId, DataPathConfigFileConstants.MuxponderFilter));
//		}			

 		
		DataPathLogger.debug(" cardInfoList :  "+ cardInfoList + " and size : "+ cardInfoList.size()); 		
		 
 		for(int j=0; j<cardInfoList.size(); j++){
 			
 			generateFinalTpnDetails( cardInfoList.get(j), nodeId,  networkId,  dbService,  doc,  rootElement, DataPathConfigFileConstants.LinePortNum_101);
 			
 			if(cardInfoList.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.CardMuxponderOPX)
 	 					||
	  		  cardInfoList.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.CardMuxponderOPXCGX)) {
 				
 				
 				cardInfoList.get(j).setDirection( 						
 						dbService.getPortInfoService().FindPortInfo(networkId, nodeId, cardInfoList.get(j).getRack(), cardInfoList.get(j).getSbrack(),
 						cardInfoList.get(j).getCard()).stream().filter(
 								portInfoObj -> portInfoObj.getLinePort()== Integer.parseInt(DataPathConfigFileConstants.LinePortNum_102)).
 								collect(Collectors.toList()).get(0).getDirection());
 				
 				
 				generateFinalTpnDetails( cardInfoList.get(j),  nodeId,  networkId,  dbService,  doc,  rootElement, DataPathConfigFileConstants.LinePortNum_102);
 			}
 		}
	}
	
	
	/**
	 * 
	 * @param nodeId
	 * @param networkId
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateFinalTpnDetails(CardInfo cardInfoObj, int nodeId, int networkId, DbService dbService, Document doc, Element rootElement, String LinePortNumber) {
		
		// DataPathDetails->TPNDetails
		Element TPNDetails  = doc.createElement("TPNDetails");
		rootElement.appendChild(TPNDetails);
		 
		Map<String, Object> listToSearchFor = new HashMap<>();
		listToSearchFor.put(DataPathConfigFileConstants.InjectTagForTPNDetails, cardInfoObj);
		
		String portNum = "0", txWavelengthNum=String.valueOf(cardInfoObj.getWavelength()),
				rxWavelengthNum=String.valueOf(cardInfoObj.getWavelength());
		
		if(cardInfoObj.getCardType().equalsIgnoreCase(ResourcePlanConstants.CardMuxponder10G)) { /**For MPN 10G(10G Agg card) Port would be : MPN 
																										Port on which XGM connects to MPN */
			
			int circuitIdOf10gAggCircuit = dbService.getCircuit10gAggService().FindAllCircuit10gAgg(networkId, cardInfoObj.getCircuitId()).get(0).getCircuit10gAggId();
			System.out.println("circuitIdOf10gAggCircuit "+ circuitIdOf10gAggCircuit);
			portNum = String.valueOf(
							 dbService.getPortInfoService().FindPortInfo(networkId, nodeId, cardInfoObj.getRack(), cardInfoObj.getSbrack(),
										cardInfoObj.getCard(), circuitIdOf10gAggCircuit).getMpnPortNo()
						);
			
			NetworkRoute NetworkRouteObj =  dbService.getNetworkRouteService().FindAllByDemandId(networkId, cardInfoObj.getDemandId()).get(0);
			
			txWavelengthNum =  String.valueOf(NetworkRouteObj.getWavelengthNo());
			rxWavelengthNum =  String.valueOf(NetworkRouteObj.getWavelengthNo());
			
		}


		//DataPathDetails->TPNDetails->TYPE
		Element TYPE  = doc.createElement("TYPE");
		TYPE.appendChild(doc.createTextNode(String.valueOf(
			injectTagFor(DataPathConfigFileConstants.InjectTagForTPNDetails, listToSearchFor, nodeId, dbService))));
		TPNDetails.appendChild(TYPE);	
			
			{
	 			// DataPathDetails->TPNDetails->RackId
	 			Element RackId  = doc.createElement("RackId");
	 			RackId.appendChild(doc.createTextNode(String.valueOf(cardInfoObj.getRack())));/** */
	 			TPNDetails.appendChild(RackId); 	 	

	 			// DataPathDetails->TPNDetails->SubrackId
	 			Element SubrackId  = doc.createElement("SubrackId");
	 			SubrackId.appendChild(doc.createTextNode(String.valueOf(cardInfoObj.getSbrack())));/** */
	 			TPNDetails.appendChild(SubrackId);

	 			// DataPathDetails->TPNDetails->CardId
	 			Element CardId  = doc.createElement("CardId");
	 			CardId.appendChild(doc.createTextNode(String.valueOf(cardInfoObj.getCard())));/** */
	 			TPNDetails.appendChild(CardId);

	 			// DataPathDetails->TPNDetails->CardType
	 			Element CardType  = doc.createElement("CardType");
	 			CardType.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.cardMappingConstantsHashMap
	 					.get(cardInfoObj.getCardType()))));/** */
	 			TPNDetails.appendChild(CardType);

	 			// DataPathDetails->TPNDetails->CardSubType
	 			Element CardSubType  = doc.createElement("CardSubType");
	 			CardSubType.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.
	 					subtypeCardMappingConstantsHashMap
	 					.get(cardInfoObj.getCardType()))));
	 			TPNDetails.appendChild(CardSubType);
	 			
	 			// DataPathDetails->TPNDetails->PortNum
	 			Element PortNum  = doc.createElement("PortNum");
	 			PortNum.appendChild(doc.createTextNode(portNum));/** */
	 			TPNDetails.appendChild(PortNum);

	 			// DataPathDetails->TPNDetails->Direction
	 			Element Direction  = doc.createElement("Direction");
	 			Direction.appendChild(doc.createTextNode(String.valueOf(
	 					DataPathConfigFileConstants.directionConstantsHashMap.get(cardInfoObj.getDirection()))));/** */
	 			TPNDetails.appendChild(Direction);
	 			
	 			// DataPathDetails->TPNDetails->TxWavelengthNum
	 			Element TxWavelengthNum  = doc.createElement("TxWavelengthNum");
	 			TxWavelengthNum.appendChild(doc.createTextNode(String.valueOf(txWavelengthNum)));/** */
	 			TPNDetails.appendChild(TxWavelengthNum);

	 			// DataPathDetails->TPNDetails->RxWavelengthNum
	 			Element RxWavelengthNum  = doc.createElement("RxWavelengthNum");
	 			RxWavelengthNum.appendChild(doc.createTextNode(String.valueOf(rxWavelengthNum)));
	 			TPNDetails.appendChild(RxWavelengthNum);

	 			// DataPathDetails->TPNDetails->LinePortNum	 			
	 			Element LinePortNum  = doc.createElement("LinePortNum");
	 			LinePortNum.appendChild(doc.createTextNode(LinePortNumber));
	 			TPNDetails.appendChild(LinePortNum);
			}
	}
	
	
	
	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 *//*
	public void generateProtectionTagDataPathFileXML(int nodeId, int nodeDegree, DbService dbService, Document doc, Element rootElement){
	
		// DataPathDetails->TPNDetails
		Element TPNDetails  = doc.createElement("TPNDetails");
		rootElement.appendChild(TPNDetails);
	}
	*/
	
	
	
	
	public void generateCmWssDirectionConfigTagSpecificData(int nodeId, int networkId, DbService dbService, Document doc, 
			Element rootElement,int wssDirectionType,WssDirectionConfig  wssDirectionConfigList){

 		
			int attenuationConfigMode=0, wssDirectionLocal = 0 ;
			
			/**DBG => As per the Input from CM*/
			if(wssDirectionType == MapConstants.I_ONE){ /**WSS_1x2*/
				attenuationConfigMode = MapConstants.I_ZERO;
			}
			else if(wssDirectionType == MapConstants.I_TWO){ /**WSS_2x1x9*/
				attenuationConfigMode = MapConstants.I_THREE;
			}
		
			// DataPathDetails->CmWssDirectionConfig
			Element CmWssDirectionConfig  = doc.createElement("CmWssDirectionConfig");
			rootElement.appendChild(CmWssDirectionConfig);

			 
			//DataPathDetails->CmWssDirectionConfig->TYPE
			Element TYPE  = doc.createElement("TYPE");
			TYPE.appendChild(doc.createTextNode(String.valueOf("0")));
			CmWssDirectionConfig.appendChild(TYPE);	
			
			{
	 			// DataPathDetails->CmWssDirectionConfig->RackId
	 			Element RackId  = doc.createElement("RackId");
	 			RackId.appendChild(doc.createTextNode(String.valueOf(wssDirectionConfigList.getRack())));/** */
	 			CmWssDirectionConfig.appendChild(RackId); 	 	

	 			// DataPathDetails->CmWssDirectionConfig->SubrackId
	 			Element SubrackId  = doc.createElement("SubrackId");
	 			SubrackId.appendChild(doc.createTextNode(String.valueOf(wssDirectionConfigList.getSbrack())));/** */
	 			CmWssDirectionConfig.appendChild(SubrackId);

	 			// DataPathDetails->CmWssDirectionConfig->CardId
	 			Element CardId  = doc.createElement("CardId");
	 			CardId.appendChild(doc.createTextNode(String.valueOf(wssDirectionConfigList.getCard())));/** */
	 			CmWssDirectionConfig.appendChild(CardId);

	 			// DataPathDetails->CmWssDirectionConfig->CardType
	 			Element CardType  = doc.createElement("CardType");
	 			CardType.appendChild(doc.createTextNode(String.valueOf(
	 					DataPathConfigFileConstants.cardMappingConstantsHashMap
	 					.get(wssDirectionConfigList.getCardType()))));/** */
	 			CmWssDirectionConfig.appendChild(CardType);

	 			// DataPathDetails->CmWssDirectionConfig->CardSubType
	 			Element CardSubType  = doc.createElement("CardSubType");
	 			CardSubType.appendChild(doc.createTextNode(String.valueOf(
	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap
	 					.get(wssDirectionConfigList.getCardType()))));/** */
	 			CmWssDirectionConfig.appendChild(CardSubType);

	 			// DataPathDetails->CmWssDirectionConfig->WssDirection
	 			Element WssDirection  = doc.createElement("WssDirection");
	 			if(wssDirectionConfigList.getCardType().equalsIgnoreCase(DataPathConfigFileConstants.WSS_2X1X9)
	 					|| wssDirectionConfigList.getCardType().equalsIgnoreCase(DataPathConfigFileConstants.WSS_2X1X20))
	 			{
	 				/**Added with 20 as per input from CM*/ 				
	 				
	 				 wssDirectionLocal =
	 						 (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
		 		 					wssDirectionConfigList.getWssDirection()) + 
	 						 		DataPathConfigFileConstants.WSS_DIRECTION_CONSTANT;
	 				 
	 				if(wssDirectionLocal > MapConstants.I_HUNDREAD) {
	 					wssDirectionLocal = 
	 							 (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
	 		 		 					wssDirectionConfigList.getWssDirection());
	 				}
	 				 
	 			}	
	 			else if(wssDirectionConfigList.getCardType().equalsIgnoreCase(DataPathConfigFileConstants.WSS_1X2)){
	 				wssDirectionLocal =
	 						 (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
		 		 					wssDirectionConfigList.getWssDirection()) ;
	 			}
	 			else{/** wssdirection for rest of the wss cards : same as wss_1x2*/
	 				wssDirectionLocal =
	 						 (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
		 		 					wssDirectionConfigList.getWssDirection()) ;
	 			}
	 			WssDirection.appendChild(doc.createTextNode(String.valueOf(wssDirectionLocal)));/** */
	 			CmWssDirectionConfig.appendChild(WssDirection);
	 			
	 			// DataPathDetails->CmWssDirectionConfig->WssDirectionType
	 			Element WssDirectionType  = doc.createElement("WssDirectionType");	 			
	 			WssDirectionType.appendChild(doc.createTextNode(String.valueOf(
	 					/*wssDirectionConfigList.getWssDirectionType()*/wssDirectionType)));/** */
	 			CmWssDirectionConfig.appendChild(WssDirectionType);

	 			// DataPathDetails->CmWssDirectionConfig->LaserStatus
	 			Element LaserStatus  = doc.createElement("LaserStatus");
	 			LaserStatus.appendChild(doc.createTextNode(String.valueOf(wssDirectionConfigList.getLaserStatus())));/** */
	 			CmWssDirectionConfig.appendChild(LaserStatus);

	 			// DataPathDetails->CmWssDirectionConfig->AttenuationConfigMode
	 			Element AttenuationConfigMode  = doc.createElement("AttenuationConfigMode");
	 			AttenuationConfigMode.appendChild(doc.createTextNode(String.valueOf
	 					(/*DataPathConfigFileConstants.WSS_ATTENUATIONCONFIGMODE*/attenuationConfigMode)));/** */
	 			CmWssDirectionConfig.appendChild(AttenuationConfigMode);

	 			// DataPathDetails->CmWssDirectionConfig->FixedAttenuation
	 			Element FixedAttenuation  = doc.createElement("FixedAttenuation");
	 			FixedAttenuation.appendChild(doc.createTextNode(String.valueOf(wssDirectionConfigList.getFixedAttenuation())));/** */
	 			CmWssDirectionConfig.appendChild(FixedAttenuation);

	 			// DataPathDetails->CmWssDirectionConfig->PreEmphasisTriggerPowerDiff
	 			Element PreEmphasisTriggerPowerDiff  = doc.createElement("PreEmphasisTriggerPowerDiff");
	 			PreEmphasisTriggerPowerDiff.appendChild(doc.createTextNode(String.valueOf(
	 							DataPathConfigFileConstants.WSS_PREMPHASISTRIGGERPOWERDIFF)));/** */
	 			CmWssDirectionConfig.appendChild(PreEmphasisTriggerPowerDiff);

	 			/**
	 			 * Following Static Entries Added as per the input from CM
	 			 */
	 			
	 			// DataPathDetails->CmWssDirectionConfig->PreEmphasisTerminationPowerDiff
	 			Element PreEmphasisTerminationPowerDiff  = doc.createElement("PreEmphasisTerminationPowerDiff");
	 			PreEmphasisTerminationPowerDiff.appendChild(doc.createTextNode(DataPathConfigFileConstants.PreEmphasisTerminationPowerDiff));/** */
	 			CmWssDirectionConfig.appendChild(PreEmphasisTerminationPowerDiff);

	 			// DataPathDetails->CmWssDirectionConfig->PowerEqualizationTerminationPowerDiff
	 			Element PowerEqualizationTerminationPowerDiff  = doc.createElement("PowerEqualizationTerminationPowerDiff");
	 			PowerEqualizationTerminationPowerDiff.appendChild(doc.createTextNode(DataPathConfigFileConstants.PowerEqualizationTerminationPowerDiff));/** */
	 			CmWssDirectionConfig.appendChild(PowerEqualizationTerminationPowerDiff);

	 			// DataPathDetails->CmWssDirectionConfig->TxChannelMode
	 			Element TxChannelMode  = doc.createElement("TxChannelMode");
	 			TxChannelMode.appendChild(doc.createTextNode(DataPathConfigFileConstants.TxChannelMode));/** */
	 			CmWssDirectionConfig.appendChild(TxChannelMode);
	 			
	 			/**Attenuation Was there before just changed to 99.9 as of now */
	 			
	 			// DataPathDetails->CmWssDirectionConfig->Attenuation
	 			Element Attenuation  = doc.createElement("Attenuation");
	 			Attenuation.appendChild(doc.createTextNode(DataPathConfigFileConstants.Attenuation/*String.valueOf(wssDirectionConfigList.getAttenuation())*/));/**DBG => In future actual entry will be added as of now 99.9 considered as per the cm inout */
	 			CmWssDirectionConfig.appendChild(Attenuation);
	 			
	 			
	 		    // DataPathDetails->CmWssDirectionConfig->IsGainTiltDone
	 			Element IsGainTiltDone  = doc.createElement("IsGainTiltDone");
	 			IsGainTiltDone.appendChild(doc.createTextNode("1"));/**DBG => After db entry add, change the hard coded value */
	 			CmWssDirectionConfig.appendChild(IsGainTiltDone);
	 			
	 			
	 	



			}
		
	}
	
	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateCmWssDirectionConfigTagDataPathFileXML(int nodeId, int networkId, DbService dbService, 
			Document doc, Element rootElement){
	
		List<WssDirectionConfig> wssDirectionConfigList = dbService.getWssDirectionConfigService().FindAll(networkId, nodeId);
		
 		System.out.println(" wssDirectionConfigList "+ wssDirectionConfigList.size());
 		
 		for(int j=0; j<wssDirectionConfigList.size(); j++){
 			
 			/**Check for the WSS Type and Act Accordingly*/
 			if(wssDirectionConfigList.get(j).getCardType().equalsIgnoreCase(DataPathConfigFileConstants.WSS_1X2)){

 				/**WSS1x2 Call*/
 				
 				generateCmWssDirectionConfigTagSpecificData(nodeId, networkId, dbService, doc, 
						rootElement,MapConstants.I_ONE,	wssDirectionConfigList.get(j) ); /**Added for Direction One too as per the input of CM*/
 				
 				generateCmWssDirectionConfigTagSpecificData(nodeId, networkId, dbService, doc, 
						rootElement,MapConstants.I_TWO,	wssDirectionConfigList.get(j) );			
 			}
 			else if(wssDirectionConfigList.get(j).getCardType().equalsIgnoreCase(DataPathConfigFileConstants.WSS_2X1X9)
 					|| wssDirectionConfigList.get(j).getCardType().equalsIgnoreCase(DataPathConfigFileConstants.WSS_2X1X20) ){

 				/**WSS2x1x9 Call*/ 				
 				generateCmWssDirectionConfigTagSpecificData(nodeId, networkId, dbService, doc, 
						rootElement,MapConstants.I_ONE, wssDirectionConfigList.get(j));
 				

 				generateCmWssDirectionConfigTagSpecificData(nodeId, networkId, dbService, doc, 
						rootElement,MapConstants.I_TWO,	wssDirectionConfigList.get(j)); 
			}
 					
 			
 		}
	}
	
	/**
	 * Find Enabled direction Array from Topology
	 * @param topologyObj
	 * @return
	 */
	public int[] FindEnabledDirectionFromTopology(Topology topologyObj) {
		
		int [] directionArray = new int[MapConstants.I_EIGHT];
		
		if(topologyObj.getDir1() != MapConstants.I_ZERO) {
			directionArray[0] = MapConstants.I_ONE;
			directionArray[4] = MapConstants.I_ONE;
		}
		if(topologyObj.getDir2() != MapConstants.I_ZERO) {
			directionArray[1] = MapConstants.I_ONE;
			directionArray[5] = MapConstants.I_ONE;
		}
		if(topologyObj.getDir3() != MapConstants.I_ZERO) {
			directionArray[2] = MapConstants.I_ONE;
			directionArray[6] = MapConstants.I_ONE;
		}
		if(topologyObj.getDir4() != MapConstants.I_ZERO) {
			directionArray[3] = MapConstants.I_ONE;
			directionArray[7] = MapConstants.I_ONE;
		}		
		
		DataPathLogger.debug("directionArray "+ directionArray.toString() );
		
//		for (int dir : directionArray) {
//			System.out.print(dir+",");
//		}
		
		
		return directionArray;
	}
	
	
	/**
	 * 
	 * @param amplType
	 * @param amplConfigObj
	 * @param dbService
	 * @return
	 */
	public int FindAmplifierStatusBasedOnMcsMap(int amplType, AmplifierConfig amplConfigObj , DbService dbService) {
		
		String edfaLoc = amplConfigObj.getRack()+"_"+amplConfigObj.getSbrack()+"_"+amplConfigObj.getCard();
		String[] edfaDir= DataPathConfigFileConstants.edfaDirectionConstantsHashMap.get(amplType).toString().split("_");		
		int amplStatus=DataPathConfigFileConstants.Amplifier_STATUS_DISABLE;
		
		/***NEED TO CHECK BASED ON  EDFA ID : Now taking status based on the Direction value instead previous lambda configured cards**/
//		List<McsMap> mcsMapList = dbService.getMcsMapService().FindByCommonPortForDirection(amplConfigObj.getNetworkId(), amplConfigObj.getNodeId(),edfaLoc, edfaDir[0], edfaDir[1]);
//		DataPathLogger.debug("FindAmplifierStatusBasedOnMcsMap : mcsMapList "+mcsMapList.toString());
//		
//		if(!mcsMapList.isEmpty()) {
//			amplStatus = DataPathConfigFileConstants.Amplifier_STATUS_ENABLE;
//		}
		

		// Check out the direction and make the status enable acc
		amplStatus = enabledDirArrayForEdfa[amplType - MapConstants.I_FOUR]; // Since array starts from 0 and type is from 4
		DataPathLogger.debug("amplStatus : "+ amplStatus);
		
		return amplStatus;
		
	}
	
	
	/**
	 * @brief Generate Amplifier Tags on Type Basis : PA/BA : 2 Entries, ILA : 1 Entry and EDFA : 8 Entries
	 * @param nodeId
	 * @param networkId
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 * @param amplifierType
	 * @param amplifierConfigServiceList
	 */
	public void generateCmAplifierConfigTagSpecificData(int nodeId, int networkId, 
						DbService dbService, Document doc, Element rootElement,
						int amplifierType, AmplifierConfig amplifierConfigServiceList){
		
		
			/**
			 * Decision Making Based on Amplifier Type
			 */
			float gainLimit		 = MapConstants.I_ZERO;
 			int gainTilt    	 = MapConstants.I_ZERO, 
 				direction 		 = MapConstants.I_ZERO;
 			int subType          = MapConstants.I_ZERO; /**0 For All except RAMAN*/
 			int amplifierStatus  = DataPathConfigFileConstants.Amplifier_STATUS_ENABLE; /**By Default enable for all*/
 			
 			
			if(amplifierType == DataPathConfigFileConstants.AmplifierType_PA){
				gainLimit = DataPathConfigFileConstants.AmplifierType_PA_Default_GainLimit;		
				gainTilt = DataPathConfigFileConstants.gainTilt;
				direction = (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
							amplifierConfigServiceList.getDirection());				
			}
			else if(amplifierType == DataPathConfigFileConstants.AmplifierType_BA){
				gainLimit = DataPathConfigFileConstants.AmplifierType_BA_Default_GainLimit;
				direction = (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
							amplifierConfigServiceList.getDirection());
			}
			else if(amplifierType == DataPathConfigFileConstants.AmplifierType_ILA){
				gainLimit = DataPathConfigFileConstants.AmplifierType_ILA_Default_GainLimit;
				gainTilt = DataPathConfigFileConstants.gainTilt;
				direction = (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
							amplifierConfigServiceList.getDirection());
			}
			else if(amplifierType == DataPathConfigFileConstants.AmplifierType_HYBRID_RAMAN){
				System.out.println(" hybrid raman specific");
				
				gainLimit = DataPathConfigFileConstants.AmplifierType_RAMAN_Default_GainLimit;
				gainTilt = DataPathConfigFileConstants.gainTilt;
				direction = (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
							amplifierConfigServiceList.getDirection());
				
				
				subType = (int)DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(ResourcePlanConstants.CardRamanHybrid);
			 
				
			}
			else if(amplifierType == DataPathConfigFileConstants.AmplifierType_SIMPLE_RAMAN){
				System.out.println(" simple raman specific");
				
				gainLimit = DataPathConfigFileConstants.AmplifierType_RAMAN_Default_GainLimit;
				gainTilt = DataPathConfigFileConstants.gainTilt;
				direction = (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
							amplifierConfigServiceList.getDirection());
			
				subType = (int)DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(ResourcePlanConstants.CardRamanDra);		 
				
			}
			else if(amplifierType == DataPathConfigFileConstants.AmplifierType_HYBRID_ILA){
				System.out.println(" hybrid ila specific");
				
				gainLimit = DataPathConfigFileConstants.AmplifierType_RAMAN_Default_GainLimit;
				gainTilt = DataPathConfigFileConstants.gainTilt;
				direction = (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
							amplifierConfigServiceList.getDirection());
				
				
				subType = (int)DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(ResourcePlanConstants.CardIlaRamanHybrid);
			 
				
			}
			else if(amplifierType == DataPathConfigFileConstants.AmplifierType_SIMPLE_ILA){
				System.out.println(" simple ila specific");
				
				gainLimit = DataPathConfigFileConstants.AmplifierType_RAMAN_Default_GainLimit;
				gainTilt = DataPathConfigFileConstants.gainTilt;
				direction = (int) DataPathConfigFileConstants.directionConstantsHashMap.get(
							amplifierConfigServiceList.getDirection());
			
				subType = (int)DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(ResourcePlanConstants.CardIlaRamanDra);		 
				
			}
			else if(amplifierType >= DataPathConfigFileConstants.AmplifierType_EDARY && 
					amplifierType <= MapConstants.I_ELEVEN
					){
				
				direction= Integer.parseInt(amplifierConfigServiceList.getEdfaDirId());
				
				amplifierStatus = FindAmplifierStatusBasedOnMcsMap(amplifierType, amplifierConfigServiceList,dbService);
				
	 			if(amplifierType>= MapConstants.I_FOUR && amplifierType<=MapConstants.I_SEVEN)/**EDFA ADD*/
	 				gainLimit = DataPathConfigFileConstants.AmplifierType_EDFA_ADD_GainLimit;
	 			else if(amplifierType>= MapConstants.I_EIGHT && amplifierType<=MapConstants.I_ELEVEN)/**EDFA DROP*/
	 				gainLimit = DataPathConfigFileConstants.AmplifierType_EDFA_DROP_GainLimit;
			}
			
			else{
				// Default case if any
			}	
			
			// DataPathDetails->CmAmplifierConfig
			Element CmAmplifierConfig  = doc.createElement("CmAmplifierConfig");
			rootElement.appendChild(CmAmplifierConfig);

				
			//DataPathDetails->CmAmplifierConfig->TYPE
			Element TYPE  = doc.createElement("TYPE");
			TYPE.appendChild(doc.createTextNode(String.valueOf("0")));
			CmAmplifierConfig.appendChild(TYPE);	
			
			{
	 			// DataPathDetails->CmAmplifierConfig->RackId
	 			Element RackId  = doc.createElement("RackId");
	 			RackId.appendChild(doc.createTextNode(String.valueOf(amplifierConfigServiceList.getRack())));/** */
	 			CmAmplifierConfig.appendChild(RackId); 	 	

	 			// DataPathDetails->CmAmplifierConfig->SubrackId
	 			Element SubrackId  = doc.createElement("SubrackId");
	 			SubrackId.appendChild(doc.createTextNode(String.valueOf(amplifierConfigServiceList.getSbrack())));/** */
	 			CmAmplifierConfig.appendChild(SubrackId);

	 			// DataPathDetails->CmAmplifierConfig->CardId
	 			Element CardId  = doc.createElement("CardId");
	 			CardId.appendChild(doc.createTextNode(String.valueOf(amplifierConfigServiceList.getCard())));/** */
	 			CmAmplifierConfig.appendChild(CardId);

	 			// DataPathDetails->CmAmplifierConfig->Direction
	 			Element Direction  = doc.createElement("Direction");
	 			Direction.appendChild(doc.createTextNode(String.valueOf(direction)));/** */
	 			CmAmplifierConfig.appendChild(Direction);

	 			// DataPathDetails->CmAmplifierConfig->AmpType
	 			Element AmpType  = doc.createElement("AmpType");
	 			AmpType.appendChild(doc.createTextNode(String.valueOf(amplifierType)));/** */
	 			CmAmplifierConfig.appendChild(AmpType);
	 			
	 			// DataPathDetails->CmAmplifierConfig->AmpSubType
	 			Element AmpSubType  = doc.createElement("AmpSubType");
	 			AmpSubType.appendChild(doc.createTextNode(String.valueOf(subType)));/** */
	 			CmAmplifierConfig.appendChild(AmpSubType);

	 			// DataPathDetails->CmAmplifierConfig->AmpStatus
	 			Element AmpStatus  = doc.createElement("AmpStatus");
	 			AmpStatus.appendChild(doc.createTextNode(
	 					String.valueOf(amplifierStatus)));/** */
	 			CmAmplifierConfig.appendChild(AmpStatus);
	 			
	 			// DataPathDetails->CmAmplifierConfig->GainLimit
	 			Element GainLimit  = doc.createElement("GainLimit");
	 			GainLimit.appendChild(doc.createTextNode(String.valueOf(gainLimit)));/**Filled from the Decision Making Loop */
	 			CmAmplifierConfig.appendChild(GainLimit);	 			

	 			// DataPathDetails->CmAmplifierConfig->GainTilt
	 			Element GainTilt  = doc.createElement("GainTilt");
	 			GainTilt.appendChild(doc.createTextNode(String.valueOf(gainTilt)));/**For PA/ILA -1 and 0 for teh rest */
	 			CmAmplifierConfig.appendChild(GainLimit);


	 			// DataPathDetails->CmAmplifierConfig->ConfigurationMode
	 			Element ConfigurationMode  = doc.createElement("ConfigurationMode");
	 			ConfigurationMode.appendChild(doc.createTextNode(String.valueOf(amplifierConfigServiceList.getConfigurationMode())));/** */
	 			CmAmplifierConfig.appendChild(ConfigurationMode);

	 			// DataPathDetails->CmAmplifierConfig->VoaAttenuation
	 			Element VoaAttenuation  = doc.createElement("VoaAttenuation");
	 			VoaAttenuation.appendChild(doc.createTextNode(String.valueOf(amplifierConfigServiceList.getVoaAttenuation())));/** */
	 			CmAmplifierConfig.appendChild(VoaAttenuation);		
			}
	}
	
	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	@SuppressWarnings("unchecked")
	public void generateCmAmplifierConfigTagDataPathFileXML(int nodeId, int networkId, DbService dbService, Document doc, Element rootElement){
	
		List<AmplifierConfig> amplifierConfigServiceList = dbService.getAmplifierConfigService().FindAll(networkId, nodeId);
 		System.out.println(" amplifierConfigServiceList "+ amplifierConfigServiceList.size());
 		
 		try {
 			Topology topologyObjToSearchFor =  dbService.getTopologyService().FindTopology(networkId,nodeId);
 	 		enabledDirArrayForEdfa =  FindEnabledDirectionFromTopology(topologyObjToSearchFor);	
 		}
 		catch (Exception e) { 			
 			DataPathLogger.error("Error in enabledDirArrayForEdfa");
 			e.printStackTrace();
		}
 		
 		
 		/**All for loops executed separately in order to avoid sorting [since from db its strin type] to cater requirement for AmpType based entry*/
 		
 		for(int j=0; j<amplifierConfigServiceList.size(); j++){
 			
 			 			
 			/**Check for the Amplifier Type and Act Accordingly*/
 			if(amplifierConfigServiceList.get(j).getAmpType().equalsIgnoreCase(DataPathConfigFileConstants.PA_BA)){

 					/**BA Call*/
					generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
					rootElement,DataPathConfigFileConstants.AmplifierType_BA,
					amplifierConfigServiceList.get(j));
 				
 					/**PA Call*/
 					generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
							rootElement,DataPathConfigFileConstants.AmplifierType_PA,
							amplifierConfigServiceList.get(j));
 		
 			}
 		}
 		
 		for(int j=0; j<amplifierConfigServiceList.size(); j++){
 			
 			if(amplifierConfigServiceList.get(j).getAmpType().equalsIgnoreCase(DataPathConfigFileConstants.ILA)){

				/**ILA Call*/
					generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, rootElement,
					DataPathConfigFileConstants.AmplifierType_ILA,
					amplifierConfigServiceList.get(j));
			}
 		}
 		
 		for(int j=0; j<amplifierConfigServiceList.size(); j++){
 			
 			if(amplifierConfigServiceList.get(j).getAmpType().equalsIgnoreCase(DataPathConfigFileConstants.EDFA_ARRAY)){

				/**EDARAY Call*/
					int edfaCount=MapConstants.I_FOUR;
					for(int innerCount=0; innerCount<MapConstants.I_EIGHT; innerCount++){ 					
						
						generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, rootElement, 								
								edfaCount,amplifierConfigServiceList.get(j));
						edfaCount++; 						
					} 					
					
			}
 		}
 		
 		for(int j=0; j<amplifierConfigServiceList.size(); j++){
	
	 			//Raman Change
	 			if(amplifierConfigServiceList.get(j).getAmpType().equalsIgnoreCase(DataPathConfigFileConstants.Raman_Hybrid)){
	
	 					System.out.println(" Inside raman hybrid");
	 					
						/**BA Call*/
						generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
						rootElement,DataPathConfigFileConstants.AmplifierType_BA,
						amplifierConfigServiceList.get(j));
	
						/**RAMAN Call*/ 				
						generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
							rootElement,DataPathConfigFileConstants.AmplifierType_HYBRID_RAMAN,
							amplifierConfigServiceList.get(j));
						
				}
	 			
	 			else if(amplifierConfigServiceList.get(j).getAmpType().equalsIgnoreCase(DataPathConfigFileConstants.Raman_Dra)){
	
	 					System.out.println(" Inside raman dra");
	 				
		 				/**BA Call*/
						generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
						rootElement,DataPathConfigFileConstants.AmplifierType_BA,
						amplifierConfigServiceList.get(j));
	 				
	 				
	 					/**RAMAN Call*/ 				
						generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
							rootElement,DataPathConfigFileConstants.AmplifierType_SIMPLE_RAMAN,
							amplifierConfigServiceList.get(j));
										
						
				}
	 			
	 			/**ILA Simple | Hybrid : Need to Handle in future*/ 
	 			else if(amplifierConfigServiceList.get(j).getAmpType().equalsIgnoreCase(DataPathConfigFileConstants.Ila_Hybrid)){
	
						System.out.println(" Inside ILA hybrid");
						
					/**BA Call*/
					generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
					rootElement,DataPathConfigFileConstants.AmplifierType_BA,
					amplifierConfigServiceList.get(j));
	
					/**RAMAN Call*/ 				
					generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
						rootElement,DataPathConfigFileConstants.AmplifierType_HYBRID_ILA,
						amplifierConfigServiceList.get(j));
					
			}
				
				else if(amplifierConfigServiceList.get(j).getAmpType().equalsIgnoreCase(DataPathConfigFileConstants.Ila_Dra)){
	
						System.out.println(" Inside ILA dra");
					
	 				/**BA Call*/
					generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
					rootElement,DataPathConfigFileConstants.AmplifierType_BA,
					amplifierConfigServiceList.get(j));
					
					
						/**RAMAN Call*/ 				
					generateCmAplifierConfigTagSpecificData(nodeId, networkId, dbService, doc, 
						rootElement,DataPathConfigFileConstants.AmplifierType_SIMPLE_ILA,
						amplifierConfigServiceList.get(j));
									
					
			}
		}	 	
	}
	
	
	/**
	 * Generate Mapping For CD Roadm : McsMap
	 * @date  23rd Aug, 2018
	 * @param nodeId
	 * @param networkId
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	
	public void generateCmCdNodeMscPortMappingTagDataPathFileXML(int nodeId, int networkId, DbService dbService,
			Document doc, Element rootElement){  
		
		List<McsMap> mcsPortMappingForCdNodeServiceUniqueRSList = dbService.getMcsMapService()
																.FindDistinct(networkId, nodeId);
 		System.out.println(" mcsPortMappingForCdNodeServiceUniqueRSList "+ mcsPortMappingForCdNodeServiceUniqueRSList.size());
 		
 	
 		
 		
 		for(int j=0; j<mcsPortMappingForCdNodeServiceUniqueRSList.size(); j++)
 		{
 			
 			
 			// DataPathDetails->CmMcsPortMapping
 	 		Element CmMcsPortMapping  = doc.createElement("CmMcsPortMapping");
			rootElement.appendChild(CmMcsPortMapping);
		
			//DataPathDetails->TELink->DirectionDetails->TYPE
			Element TYPE  = doc.createElement("TYPE");
			TYPE.appendChild(doc.createTextNode(String.valueOf("0")));
			CmMcsPortMapping.appendChild(TYPE);	

 				
 	 		// DataPathDetails->CmMcsPortMapping->RackId
 			Element RackId  = doc.createElement("RackId");
 			RackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getRack())));/** */
 			CmMcsPortMapping.appendChild(RackId); 	 	

 			// DataPathDetails->CmMcsPortMapping->SubrackId
 			Element SubrackId  = doc.createElement("SubrackId");
 			SubrackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getSbrack())));/** */
 			CmMcsPortMapping.appendChild(SubrackId);

 			// DataPathDetails->CmMcsPortMapping->CardId
 			Element CardId  = doc.createElement("CardId");
 			CardId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getCard())));/** */
 			CmMcsPortMapping.appendChild(CardId);

 			
 			Element CardType  = doc.createElement("CardType");
 			CardType.appendChild(doc.createTextNode(String.valueOf(
 					DataPathConfigFileConstants.cardMappingConstantsHashMap
 					.get(ResourcePlanConstants.CardWss8x12))));/** */
 			CmMcsPortMapping.appendChild(CardType);

 			
 			Element CardSubType  = doc.createElement("CardSubType");
 			CardSubType.appendChild(doc.createTextNode(String.valueOf(
 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap
 					.get(ResourcePlanConstants.CardWss8x12))));/** DBG => Input Pending */
 			CmMcsPortMapping.appendChild(CardSubType);

 			// DataPathDetails->CmMcsPortMapping->McsId
 			Element McsId  = doc.createElement("McsId");
 			McsId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getMcsId())));/** */
 			CmMcsPortMapping.appendChild(McsId);
 		
 			
 			/**
 			 * Generate Add and Drop Mapping 
 			 */
 			{ 		
 				List<McsMap> mcsPortMappingCdNodeServiceAddDropDataList = dbService.getMcsMapService()
 						.FindAllAddDropData(
 						networkId, nodeId, 
 						mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getRack(),
 						mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getSbrack(),
 						mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getCard());
 				
 		 		System.out.println(" mcsPortMappingCdNodeServiceAddDropDataList "+ mcsPortMappingCdNodeServiceAddDropDataList.size());
 		 	
 				
 				for(int k=0; k<mcsPortMappingCdNodeServiceAddDropDataList.size(); k++){
 				 
 					// DataPathDetails->CmMcsPortMapping->AddMapping
 	 	 			Element AddMapping  = doc.createElement("AddMapping");
 	 	 			CmMcsPortMapping.appendChild(AddMapping);
 		 	 			{
 		 	 				// DataPathDetails->CmMcsPortMapping->AddMapping->AddSwitchPortNumber
 		 	 	 			Element AddSwitchPortNumber  = doc.createElement("AddSwitchPortNumber");
 		 	 	 			AddSwitchPortNumber.appendChild(doc.createTextNode(String.valueOf
 		 	 	 					(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getMcsSwitchPort())));/** */
 		 	 	 			AddMapping.appendChild(AddSwitchPortNumber);
 		 	 				
 		 	 				// DataPathDetails->CmMcsPortMapping->AddMapping->McsAddPortInfo
 		 	 	 			Element McsAddPortInfo  = doc.createElement("McsAddPortInfo");
 		 	 	 			McsAddPortInfo.appendChild(doc.createTextNode(
 		 	 	 						String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap.get(
 		 	 	 								String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getMcsCommonPort())))));/** */
 		 	 	 			AddMapping.appendChild(McsAddPortInfo);		
 		 	 	 			 	 	 					 	 	 			
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnRackId
 		 	 	 			Element AddTpnRackId  = doc.createElement("AddTpnRackId");
 		 	 	 			AddTpnRackId.appendChild(doc.createTextNode(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[0]));/** */
 		 	 	 			AddMapping.appendChild(AddTpnRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnSubRackId
 		 	 	 			Element AddTpnSubRackId  = doc.createElement("AddTpnSubRackId");
 		 	 	 			AddTpnSubRackId.appendChild(doc.createTextNode(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[1]));
 		 	 	 			AddMapping.appendChild(AddTpnSubRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnSlotId
 		 	 	 			Element AddTpnSlotId  = doc.createElement("AddTpnSlotId");
 		 	 	 			AddTpnSlotId.appendChild(doc.createTextNode(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[2]));
 		 	 	 			AddMapping.appendChild(AddTpnSlotId);
 		 	 	 			
 		 	 	 			
 		 	 	 			
 		 	 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnCardType
 		 	 	 			Element AddTpnCardType  = doc.createElement("AddTpnCardType");
 		 	 	 			AddTpnCardType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId,
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[0]),
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[1]), 
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[2]) 		 	 	 					
 		 	 	 					).getCardType()))));/** */
 		 	 	 			AddMapping.appendChild(AddTpnCardType);
 		 	 	 			

 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnCardSubType
 		 	 	 			Element AddTpnCardSubType  = doc.createElement("AddTpnCardSubType");
 		 	 	 			AddTpnCardSubType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 		 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId,
 		 	 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[0]),
 		 	 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[1]), 
 		 	 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[2]) 		 	 	 					
 		 	 		 	 	 					
 		 		 	 	 						).getCardType()
 		 		 	 	 					))));/** */
 		 	 	 			AddMapping.appendChild(AddTpnCardSubType);

 		 	 	 			
 		 	 	 			
 		 	 	 			
 		 	 	 			
 		 	 	 			
 		 	 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnLinePortNum
 		 	 	 			Element AddTpnLinePortNum  = doc.createElement("AddTpnLinePortNum");
 		 	 	 			AddTpnLinePortNum.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLinePortNo())));/** */
 		 	 	 			AddMapping.appendChild(AddTpnLinePortNum);
 		 	 	 			
 		 	 	 			//DataPathDetails->CmMcsPortMapping->AddMapping->AddAttenuation
 		 	 	 			Element AddAttenuation  = doc.createElement("AddAttenuation");
 		 	 	 		    AddAttenuation.appendChild(doc.createTextNode("0"));/**DBG => As of now, default 0 */
 		 	 	 			AddMapping.appendChild(AddAttenuation);
 		 	 	 			
 		 	 			}
 				}
 				
 				
 				for(int k=0; k<mcsPortMappingCdNodeServiceAddDropDataList.size(); k++){
 					
 					// DataPathDetails->CmMcsPortMapping->DropMapping
 					Element DropMapping  = doc.createElement("DropMapping");
 	 	 			CmMcsPortMapping.appendChild(DropMapping);
 	 	 			
 		 	 			{
 		 	 				// DataPathDetails->CmMcsPortMapping->DropMapping->DropSwitchPortNumber
 		 	 	 			Element DropSwitchPortNumber  = doc.createElement("DropSwitchPortNumber");
 		 	 	 			DropSwitchPortNumber.appendChild(doc.createTextNode(
 		 	 	 					String.valueOf(
 		 	 	 					mcsPortMappingCdNodeServiceAddDropDataList.get(k).getMcsSwitchPort())));/** */
 		 	 	 			DropMapping.appendChild(DropSwitchPortNumber); 	 			
 		 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->McsDropPortInfo
 		 	 	 			Element McsDropPortInfo  = doc.createElement("McsDropPortInfo");
 		 	 	 			McsDropPortInfo.appendChild(doc.createTextNode(
 		 	 	 						String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap.get(
 		 	 	 								String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getMcsCommonPort())))));/** */
 		 	 	 			DropMapping.appendChild(McsDropPortInfo);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnRackId
 		 	 	 			Element DropTpnRackId  = doc.createElement("DropTpnRackId");
 		 	 	 			DropTpnRackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[0])));/** */
 		 	 	 			DropMapping.appendChild(DropTpnRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnSubRackId
 		 	 	 			Element DropTpnSubRackId  = doc.createElement("DropTpnSubRackId");
 		 	 	 			DropTpnSubRackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[1])));/** */
 		 	 	 			DropMapping.appendChild(DropTpnSubRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnSlotId
 		 	 	 			Element DropTpnSlotId  = doc.createElement("DropTpnSlotId");
 		 	 	 			DropTpnSlotId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[2])));/** */
 		 	 	 			DropMapping.appendChild(DropTpnSlotId);
 		 	 	 			
 		 	 	 			
 		 	 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnCardType
 		 	 	 			Element DropTpnCardType  = doc.createElement("DropTpnCardType");
 		 	 	 			DropTpnCardType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId,
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[0]),
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[1]), 
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[2]) 		 	 	 					
 		 	 	 					).getCardType()
 		 	 	 					))));/** */
 		 	 	 			DropMapping.appendChild(DropTpnCardType);
 		 	 	 			

 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnCardSubType
 		 	 	 			Element DropTpnCardSubType  = doc.createElement("DropTpnCardSubType");
 		 	 	 			DropTpnCardSubType.appendChild(doc.createTextNode(String.valueOf(																																																																	
 		 	 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId,
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[0]),
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[1]), 
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[2]) 		 	 	 					
 		 	 	 					).getCardType()
 		 	 	 					))));/** */
 		 	 	 			DropMapping.appendChild(DropTpnCardSubType);
 		 	 	 			
 		 	 	 			
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnLinePortNum
 		 	 	 			Element DropTpnLinePortNum  = doc.createElement("DropTpnLinePortNum");
 		 	 	 			DropTpnLinePortNum.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLinePortNo())));/** */
 		 	 	 			DropMapping.appendChild(DropTpnLinePortNum);
 		 	 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropAttenuation
 		 	 	 			Element DropAttenuation  = doc.createElement("DropAttenuation");
 		 	 	 			DropAttenuation.appendChild(doc.createTextNode(String.valueOf("0")));/**DBG => As of now, default 0 */
 		 	 	 			DropMapping.appendChild(DropAttenuation);
 		 	 	 			
 		 	 			} 		 	 			
 		 	 			
 				}
 			
 			}
 		}		
	}
	
	
	/**
	 * Generate Mapping For CD Roadm : WssMap
	 * @date 11th May, 2018
	 * @param nodeId
	 * @param networkId
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	
	public void generateCmCdNodeWssMapMappingTagDataPathFileXML(int nodeId, int networkId, DbService dbService,
			Document doc, Element rootElement){  
		
		List<WssMap> mcsPortMappingForCdNodeServiceUniqueRSList = dbService.
													getWssMapService().FindDistinct(networkId, nodeId);
 		System.out.println(" mcsPortMappingForCdNodeServiceUniqueRSList "+ mcsPortMappingForCdNodeServiceUniqueRSList.size());
 		
 	
 		
 		
 		for(int j=0; j<mcsPortMappingForCdNodeServiceUniqueRSList.size(); j++)
 		{
 			
 			
 			// DataPathDetails->CmMcsPortMapping
 	 		Element CmMcsPortMapping  = doc.createElement("CmMcsPortMapping");
			rootElement.appendChild(CmMcsPortMapping);
		
			//DataPathDetails->TELink->DirectionDetails->TYPE
			Element TYPE  = doc.createElement("TYPE");
			TYPE.appendChild(doc.createTextNode(String.valueOf("0")));
			CmMcsPortMapping.appendChild(TYPE);	

 				
 	 		// DataPathDetails->CmMcsPortMapping->RackId
 			Element RackId  = doc.createElement("RackId");
 			RackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getRack())));/** */
 			CmMcsPortMapping.appendChild(RackId); 	 	

 			// DataPathDetails->CmMcsPortMapping->SubrackId
 			Element SubrackId  = doc.createElement("SubrackId");
 			SubrackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getSbrack())));/** */
 			CmMcsPortMapping.appendChild(SubrackId);

 			// DataPathDetails->CmMcsPortMapping->CardId
 			Element CardId  = doc.createElement("CardId");
 			CardId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getCard())));/** */
 			CmMcsPortMapping.appendChild(CardId);

 			// DataPathDetails->CmMcsPortMapping->CardType
 			Element CardType  = doc.createElement("CardType");
 			CardType.appendChild(doc.createTextNode(String.valueOf(
 					DataPathConfigFileConstants.cardMappingConstantsHashMap
 					.get(	mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getCardType()))));/** */
 			CmMcsPortMapping.appendChild(CardType);

 			// DataPathDetails->CmMcsPortMapping->CardSubType
 			Element CardSubType  = doc.createElement("CardSubType");
 			CardSubType.appendChild(doc.createTextNode(String.valueOf(
 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap
 					.get(mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getCardType()))));/** DBG => Input Pending */
 			CmMcsPortMapping.appendChild(CardSubType);

 			// DataPathDetails->CmMcsPortMapping->McsId
 			Element McsId  = doc.createElement("McsId");
 			McsId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getWssSetNo())));/** */
 			CmMcsPortMapping.appendChild(McsId);
 		
 			
 			/**
 			 * Generate Add and Drop Mapping 
 			 */
 			{ 		
 				List<WssMap> mcsPortMappingCdNodeServiceAddDropDataList = dbService.getWssMapService()
 						.FindAllAddDropData(
 						networkId, nodeId, 
 						mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getRack(),
 						mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getSbrack(),
 						mcsPortMappingForCdNodeServiceUniqueRSList.get(j).getCard());
 				
 		 		System.out.println(" mcsPortMappingCdNodeServiceAddDropDataList "+ mcsPortMappingCdNodeServiceAddDropDataList.size());
 		 	
 				
 				for(int k=0; k<mcsPortMappingCdNodeServiceAddDropDataList.size(); k++){
 				 
 					// DataPathDetails->CmMcsPortMapping->AddMapping
 	 	 			Element AddMapping  = doc.createElement("AddMapping");
 	 	 			CmMcsPortMapping.appendChild(AddMapping);
 		 	 			{
 		 	 				// DataPathDetails->CmMcsPortMapping->AddMapping->AddSwitchPortNumber
 		 	 	 			Element AddSwitchPortNumber  = doc.createElement("AddSwitchPortNumber");
 		 	 	 			AddSwitchPortNumber.appendChild(doc.createTextNode(String.valueOf
 		 	 	 					(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getWssLevel2SwitchPort())));/** */
 		 	 	 			AddMapping.appendChild(AddSwitchPortNumber);
 		 	 				
 		 	 				// DataPathDetails->CmMcsPortMapping->AddMapping->McsAddPortInfo
 		 	 	 			Element McsAddPortInfo  = doc.createElement("McsAddPortInfo");
 		 	 	 			McsAddPortInfo.appendChild(doc.createTextNode(
 		 	 	 						String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap.get(
 		 	 	 								String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getWssLevel1CommonPort())))));/** */
 		 	 	 			AddMapping.appendChild(McsAddPortInfo);		
 		 	 	 			 	 	 					 	 	 			
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnRackId
 		 	 	 			Element AddTpnRackId  = doc.createElement("AddTpnRackId");
 		 	 	 			AddTpnRackId.appendChild(doc.createTextNode(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[0]));/** */
 		 	 	 			AddMapping.appendChild(AddTpnRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnSubRackId
 		 	 	 			Element AddTpnSubRackId  = doc.createElement("AddTpnSubRackId");
 		 	 	 			AddTpnSubRackId.appendChild(doc.createTextNode(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[1]));
 		 	 	 			AddMapping.appendChild(AddTpnSubRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnSlotId
 		 	 	 			Element AddTpnSlotId  = doc.createElement("AddTpnSlotId");
 		 	 	 			AddTpnSlotId.appendChild(doc.createTextNode(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[2]));
 		 	 	 			AddMapping.appendChild(AddTpnSlotId);
 		 	 	 			
 		 	 	 			
 		 	 	 		// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnCardType
 		 	 	 			Element AddTpnCardType  = doc.createElement("AddTpnCardType");
 		 	 	 			AddTpnCardType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId,
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[0]),
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[1]), 
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[2]) 		 	 	 					
 		 	 	 					).getCardType()))));/** */
 		 	 	 			AddMapping.appendChild(AddTpnCardType);
 		 	 	 			

 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnCardSubType
 		 	 	 			Element AddTpnCardSubType  = doc.createElement("AddTpnCardSubType");
 		 	 	 			AddTpnCardSubType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 		 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId,
 		 	 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[0]),
 		 	 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[1]), 
 		 	 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[2]) 		 	 	 					
 		 	 		 	 	 					
 		 		 	 	 						).getCardType()))));/** */
 		 	 	 			AddMapping.appendChild(AddTpnCardSubType);

 		 	 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnLinePortNum
 		 	 	 			Element AddTpnLinePortNum  = doc.createElement("AddTpnLinePortNum");
 		 	 	 			AddTpnLinePortNum.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLinePortNo())));/** */
 		 	 	 			AddMapping.appendChild(AddTpnLinePortNum);
 		 	 	 			
 		 	 	 		
 		 	 	 			//DataPathDetails->CmMcsPortMapping->AddMapping->AddAttenuation
 		 	 	 			Element AddAttenuation  = doc.createElement("AddAttenuation");
 		 	 	 		    AddAttenuation.appendChild(doc.createTextNode("0"));/**DBG => As of now, default 0 */
 		 	 	 			AddMapping.appendChild(AddAttenuation);
 		 	 	 			
 		 	 			}
 				}
 				
 				
 				for(int k=0; k<mcsPortMappingCdNodeServiceAddDropDataList.size(); k++){
 					
 					// DataPathDetails->CmMcsPortMapping->DropMapping
 					Element DropMapping  = doc.createElement("DropMapping");
 	 	 			CmMcsPortMapping.appendChild(DropMapping);
 	 	 			
 		 	 			{
 		 	 				// DataPathDetails->CmMcsPortMapping->DropMapping->DropSwitchPortNumber
 		 	 	 			Element DropSwitchPortNumber  = doc.createElement("DropSwitchPortNumber");
 		 	 	 			DropSwitchPortNumber.appendChild(doc.createTextNode(
 		 	 	 					String.valueOf(
 		 	 	 					mcsPortMappingCdNodeServiceAddDropDataList.get(k).getWssLevel2SwitchPort())));/** */
 		 	 	 			DropMapping.appendChild(DropSwitchPortNumber); 	 			
 		 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->McsDropPortInfo
 		 	 	 			Element McsDropPortInfo  = doc.createElement("McsDropPortInfo");
 		 	 	 			McsDropPortInfo.appendChild(doc.createTextNode(
 		 	 	 						String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap.get(
 		 	 	 								String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getWssLevel1CommonPort())))));/** */
 		 	 	 			DropMapping.appendChild(McsDropPortInfo);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnRackId
 		 	 	 			Element DropTpnRackId  = doc.createElement("DropTpnRackId");
 		 	 	 			DropTpnRackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[0])));/** */
 		 	 	 			DropMapping.appendChild(DropTpnRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnSubRackId
 		 	 	 			Element DropTpnSubRackId  = doc.createElement("DropTpnSubRackId");
 		 	 	 			DropTpnSubRackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[1])));/** */
 		 	 	 			DropMapping.appendChild(DropTpnSubRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnSlotId
 		 	 	 			Element DropTpnSlotId  = doc.createElement("DropTpnSlotId");
 		 	 	 			DropTpnSlotId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.
 		 	 	 					get(k).getTpnLoc().split("_")[2])));/** */
 		 	 	 			DropMapping.appendChild(DropTpnSlotId);
 		 	 	 			
 		 	 	 			
 		 	 	 		// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnCardType
 		 	 	 			Element DropTpnCardType  = doc.createElement("DropTpnCardType");
 		 	 	 			DropTpnCardType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId,
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[0]),
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[1]), 
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[2]) 		 	 	 					
 		 	 	 					).getCardType()
 		 	 	 					))));/** */
 		 	 	 			DropMapping.appendChild(DropTpnCardType);
 		 	 	 			

 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnCardSubType
 		 	 	 			Element DropTpnCardSubType  = doc.createElement("DropTpnCardSubType");
 		 	 	 			DropTpnCardSubType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId,
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[0]),
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[1]), 
 		 	 	 						Integer.parseInt(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLoc().split("_")[2]) 		 	 	 					
 		 	 	 					).getCardType()
 		 	 	 					))));/** */
 		 	 	 			DropMapping.appendChild(DropTpnCardSubType);
 		 	 	 			

 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnLinePortNum
 		 	 	 			Element DropTpnLinePortNum  = doc.createElement("DropTpnLinePortNum");
 		 	 	 			DropTpnLinePortNum.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingCdNodeServiceAddDropDataList.get(k).getTpnLinePortNo())));/** */
 		 	 	 			DropMapping.appendChild(DropTpnLinePortNum);
 		 	 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropAttenuation
 		 	 	 			Element DropAttenuation  = doc.createElement("DropAttenuation");
 		 	 	 			DropAttenuation.appendChild(doc.createTextNode(String.valueOf("0")));/**DBG => As of now, default 0 */
 		 	 	 			DropMapping.appendChild(DropAttenuation);

 		 	 	 			
 		 	 			} 		 	 			
 		 	 			
 				}
 			
 			}
 		}		
	}
	

	
	
	
	
	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param docmcsPortMappingServiceUniqueRSList
	 * @param rootElement
	 */
	public void generateCmMcsPortMappingTagDataPathFileXML(int nodeId, int networkId, DbService dbService, Document doc, Element rootElement){  
	
		List<McsPortMapping> mcsPortMappingServiceUniqueRSList = dbService.getMcsPortMappingService().FindDistinctData(networkId, nodeId);
 		System.out.println(" mcsPortMappingServiceUniqueRSList "+ mcsPortMappingServiceUniqueRSList.size());
		 
		 
		//  mcsPortMappingServiceUniqueRSList.addAll(

		// 	dbService.getMcsPortMappingService().FindAllDeletedBrField(brownFieldNetworkId, greenFieldNetworkId, nodeId)
		//  );	

 		
 		for(int j=0; j<mcsPortMappingServiceUniqueRSList.size(); j++)
 		{
 			
			Map<String, Object> listToSearchFor = new HashMap<>();
			listToSearchFor.put(DataPathConfigFileConstants.InjectTagForCmMcsPortMapping,
									mcsPortMappingServiceUniqueRSList.get(j)); 
			
 			// DataPathDetails->CmMcsPortMapping
 	 		Element CmMcsPortMapping  = doc.createElement("CmMcsPortMapping");
			rootElement.appendChild(CmMcsPortMapping);
		
			//DataPathDetails->TELink->DirectionDetails->TYPE
			Element TYPE  = doc.createElement("TYPE");
			TYPE.appendChild(doc.createTextNode(String.valueOf(
												injectTagFor(DataPathConfigFileConstants.InjectTagForCmMcsPortMapping,
												 listToSearchFor, nodeId, dbService)
												)));
			CmMcsPortMapping.appendChild(TYPE);	

 				
 	 		// DataPathDetails->CmMcsPortMapping->RackId
 			Element RackId  = doc.createElement("RackId");
 			RackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceUniqueRSList.get(j).getRack())));/** */
 			CmMcsPortMapping.appendChild(RackId); 	 	

 			// DataPathDetails->CmMcsPortMapping->SubrackId
 			Element SubrackId  = doc.createElement("SubrackId");
 			SubrackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceUniqueRSList.get(j).getSbrack())));/** */
 			CmMcsPortMapping.appendChild(SubrackId);

 			// DataPathDetails->CmMcsPortMapping->CardId
 			Element CardId  = doc.createElement("CardId");
 			CardId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceUniqueRSList.get(j).getCard())));/** */
 			CmMcsPortMapping.appendChild(CardId);

 			// DataPathDetails->CmMcsPortMapping->CardType
 			Element CardType  = doc.createElement("CardType");
 			CardType.appendChild(doc.createTextNode(String.valueOf(
 					DataPathConfigFileConstants.cardMappingConstantsHashMap
 					.get(	mcsPortMappingServiceUniqueRSList.get(j).getCardType()))));/** */
 			CmMcsPortMapping.appendChild(CardType);

 			// DataPathDetails->CmMcsPortMapping->CardSubType
 			Element CardSubType  = doc.createElement("CardSubType");
 			CardSubType.appendChild(doc.createTextNode(String.valueOf(
 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap
 					.get(mcsPortMappingServiceUniqueRSList.get(j).getCardType()))));/** DBG => Input Pending */
 			CmMcsPortMapping.appendChild(CardSubType);

 			// DataPathDetails->CmMcsPortMapping->McsId
 			Element McsId  = doc.createElement("McsId");
 			McsId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceUniqueRSList.get(j).getMcsId())));/** */
 			CmMcsPortMapping.appendChild(McsId);
 		
 			
 			/**
 			 * Generate Add and Delete Mapping 
 			 */
 			{ 		
 				List<McsPortMapping> mcsPortMappingServiceAddDeleteDataList = dbService.getMcsPortMappingService().FindAllAddDeleteData(
 						networkId, nodeId, 
 						mcsPortMappingServiceUniqueRSList.get(j).getRack(),
 						mcsPortMappingServiceUniqueRSList.get(j).getSbrack(),
 						mcsPortMappingServiceUniqueRSList.get(j).getCard());
 				
 		 		System.out.println(" mcsPortMappingServiceAddDeleteDataList "+ mcsPortMappingServiceAddDeleteDataList.size());
 		 	
 				
 				for(int k=0; k<mcsPortMappingServiceAddDeleteDataList.size(); k++){
 				 
 					// DataPathDetails->CmMcsPortMapping->AddMapping
 	 	 			Element AddMapping  = doc.createElement("AddMapping");
 	 	 			CmMcsPortMapping.appendChild(AddMapping);
 		 	 			{
 		 	 				// DataPathDetails->CmMcsPortMapping->AddMapping->AddSwitchPortNumber
 		 	 	 			Element AddSwitchPortNumber  = doc.createElement("AddSwitchPortNumber");
 		 	 	 			AddSwitchPortNumber.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getMcsSwitchPort())));/** */
 		 	 	 			AddMapping.appendChild(AddSwitchPortNumber);
 		 	 				
 		 	 				// DataPathDetails->CmMcsPortMapping->AddMapping->McsAddPortInfo
 		 	 	 			Element McsAddPortInfo  = doc.createElement("McsAddPortInfo");
 		 	 	 			McsAddPortInfo.appendChild(doc.createTextNode(
 		 	 	 						String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap.get(
 		 	 	 								String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getMcsAddPortInfo())))));/** */
 		 	 	 			AddMapping.appendChild(McsAddPortInfo);
 		
 		 	 	 		
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnRackId
 		 	 	 			Element AddTpnRackId  = doc.createElement("AddTpnRackId");
 		 	 	 			AddTpnRackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnRackId())));/** */
 		 	 	 			AddMapping.appendChild(AddTpnRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnSubRackId
 		 	 	 			Element AddTpnSubRackId  = doc.createElement("AddTpnSubRackId");
 		 	 	 			AddTpnSubRackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnSubRackId())));/** */
 		 	 	 			AddMapping.appendChild(AddTpnSubRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnSlotId
 		 	 	 			Element AddTpnSlotId  = doc.createElement("AddTpnSlotId");
 		 	 	 			AddTpnSlotId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnSlotId())));/** */
 		 	 	 			AddMapping.appendChild(AddTpnSlotId);
 		 	 	 			
 		 	 	 			

 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnCardType
 		 	 	 			Element AddTpnCardType  = doc.createElement("AddTpnCardType");
 		 	 	 			AddTpnCardType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId, mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnRackId(),
		 	 	 						mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnSubRackId(), 
	 		 	 	 					mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnSlotId()).getCardType())
 		 	 	 					)));/** */
 		 	 	 			AddMapping.appendChild(AddTpnCardType);
 		 	 	 			

 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnCardSubType
 		 	 	 			Element AddTpnCardSubType  = doc.createElement("AddTpnCardSubType");
 		 	 	 			AddTpnCardSubType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId, mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnRackId(),
		 	 	 						mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnSubRackId(), 
	 		 	 	 					mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnSlotId()).getCardType())
 		 	 	 					)));/** */
 		 	 	 			AddMapping.appendChild(AddTpnCardSubType);
 		 	 	 			
 		 	 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddTpnLinePortNum
 		 	 	 			Element AddTpnLinePortNum  = doc.createElement("AddTpnLinePortNum");
 		 	 	 			AddTpnLinePortNum.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getAddTpnLinePortNum())));/** */
 		 	 	 			AddMapping.appendChild(AddTpnLinePortNum);
 		 	 	 			

 		 	 	 			// DataPathDetails->CmMcsPortMapping->AddMapping->AddAttenuation
 		 	 	 			Element AddAttenuation  = doc.createElement("AddAttenuation");
 		 	 	 		    AddAttenuation.appendChild(doc.createTextNode("0"));/**DBG => As of now, default 0 */
 		 	 	 			AddMapping.appendChild(AddAttenuation);
 		 	 	 			
 		 	 			}
 				}
 				
 				
 				for(int k=0; k<mcsPortMappingServiceAddDeleteDataList.size(); k++){
 					
 					// DataPathDetails->CmMcsPortMapping->DropMapping
 					Element DropMapping  = doc.createElement("DropMapping");
 	 	 			CmMcsPortMapping.appendChild(DropMapping);
 	 	 			
 		 	 			{
 		 	 				// DataPathDetails->CmMcsPortMapping->DropMapping->DropSwitchPortNumber
 		 	 	 			Element DropSwitchPortNumber  = doc.createElement("DropSwitchPortNumber");
 		 	 	 			DropSwitchPortNumber.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getMcsSwitchPort())));/** */
 		 	 	 			DropMapping.appendChild(DropSwitchPortNumber); 	 			
 		 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->McsDropPortInfo
 		 	 	 			Element McsDropPortInfo  = doc.createElement("McsDropPortInfo");
 		 	 	 			McsDropPortInfo.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.directionConstantsHashMap
 		 	 	 					.get(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getMcsDropPortInfo())))));/** */
 		 	 	 			DropMapping.appendChild(McsDropPortInfo);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnRackId
 		 	 	 			Element DropTpnRackId  = doc.createElement("DropTpnRackId");
 		 	 	 			DropTpnRackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnRackId())));/** */
 		 	 	 			DropMapping.appendChild(DropTpnRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnSubRackId
 		 	 	 			Element DropTpnSubRackId  = doc.createElement("DropTpnSubRackId");
 		 	 	 			DropTpnSubRackId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnSubRackId())));/** */
 		 	 	 			DropMapping.appendChild(DropTpnSubRackId);
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnSlotId
 		 	 	 			Element DropTpnSlotId  = doc.createElement("DropTpnSlotId");
 		 	 	 			DropTpnSlotId.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnSlotId())));/** */
 		 	 	 			DropMapping.appendChild(DropTpnSlotId);
 		 	 	 			
 		 	 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnCardType
 		 	 	 			Element DropTpnCardType  = doc.createElement("DropTpnCardType");
 		 	 	 			DropTpnCardType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId, mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnRackId(),
		 	 	 						mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnSubRackId(), 
	 		 	 	 					mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnSlotId()).getCardType())
 		 	 	 					)));/** */
 		 	 	 			DropMapping.appendChild(DropTpnCardType);
 		 	 	 			

 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnCardSubType
 		 	 	 			Element DropTpnCardSubType  = doc.createElement("DropTpnCardSubType");
 		 	 	 			DropTpnCardSubType.appendChild(doc.createTextNode(String.valueOf(
 		 	 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 		 	 	 					dbService.getCardInfoService().FindCard(networkId, nodeId, mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnRackId(),
		 	 	 						mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnSubRackId(), 
	 		 	 	 					mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnSlotId()).getCardType())
 		 	 	 					)));/** */
 		 	 	 			DropMapping.appendChild(DropTpnCardSubType);
 		 	 	 			
 		 	 	 			
 		
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropTpnLinePortNum
 		 	 	 			Element DropTpnLinePortNum  = doc.createElement("DropTpnLinePortNum");
 		 	 	 			DropTpnLinePortNum.appendChild(doc.createTextNode(String.valueOf(mcsPortMappingServiceAddDeleteDataList.get(k).getDropTpnLinePortNum())));/** */
 		 	 	 			DropMapping.appendChild(DropTpnLinePortNum);
 		 	 	 			
 		 	 	 			// DataPathDetails->CmMcsPortMapping->DropMapping->DropAttenuation
 		 	 	 			Element DropAttenuation  = doc.createElement("DropAttenuation");
 		 	 	 			DropAttenuation.appendChild(doc.createTextNode(String.valueOf("0")));/**DBG => As of now, default 0 */
 		 	 	 			DropMapping.appendChild(DropAttenuation);
 		 	 	 			
 		 	 			} 		 	 			
 		 	 			
 				}
 				
 	 		 
 	 			
 	 			
 	 			
 			
 			}
 		}		
	}
	
	
	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateCmOcmConfigTagDataPathFileXML(int nodeId, int networkId, DbService dbService, Document doc, Element rootElement){  
	
		List<OcmConfig> ocmConfigServiceList = dbService.getOcmConfigService().FindAll(networkId, nodeId);
 		System.out.println(" ocmConfigServiceList  Size : "+ ocmConfigServiceList.size() + ", ocmConfigServiceList : "+ ocmConfigServiceList);
 		
 		for(int j=0; j<ocmConfigServiceList.size(); j++)
 		{
 			// DataPathDetails->CmOcmConfig
 			Element CmOcmConfig  = doc.createElement("CmOcmConfig");
 			rootElement.appendChild(CmOcmConfig);
			
			//DataPathDetails->TELink->DirectionDetails->TYPE
			Element TYPE  = doc.createElement("TYPE");
			TYPE.appendChild(doc.createTextNode(String.valueOf("0")));
			CmOcmConfig.appendChild(TYPE);	
			 
 			{
 	 			// DataPathDetails->CmOcmConfig->RackId
 	 			Element RackId  = doc.createElement("RackId");
 	 			RackId.appendChild(doc.createTextNode(String.valueOf(ocmConfigServiceList.get(j).getRack())));/** */
 	 			CmOcmConfig.appendChild(RackId); 	 	

 	 			// DataPathDetails->CmOcmConfig->SubrackId
 	 			Element SubrackId  = doc.createElement("SubrackId");
 	 			SubrackId.appendChild(doc.createTextNode(String.valueOf(ocmConfigServiceList.get(j).getSbrack())));/** */
 	 			CmOcmConfig.appendChild(SubrackId);

 	 			// DataPathDetails->CmOcmConfig->CardId
 	 			Element CardId  = doc.createElement("CardId");
 	 			CardId.appendChild(doc.createTextNode(String.valueOf(ocmConfigServiceList.get(j).getCard())));/** */
 	 			CmOcmConfig.appendChild(CardId);

 	 			// DataPathDetails->CmOcmConfig->CardType
 	 			Element CardType  = doc.createElement("CardType");
 	 			CardType.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap
 	 					.get(ocmConfigServiceList.get(j).getCardType()))));/** */
 	 			CmOcmConfig.appendChild(CardType);

 	 			// DataPathDetails->CmOcmConfig->CardSubType
 	 			Element CardSubType  = doc.createElement("CardSubType");
 	 			CardSubType.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap
 	 					.get(ocmConfigServiceList.get(j).getCardType()))));/** Mapped from Sub type map*/
 	 			CmOcmConfig.appendChild(CardSubType);

 	 			// DataPathDetails->CmOcmConfig->OcmId
 	 			Element OcmId  = doc.createElement("OcmId");
 	 			OcmId.appendChild(doc.createTextNode(String.valueOf(ocmConfigServiceList.get(j).getOcmId())));/** */
 	 			CmOcmConfig.appendChild(OcmId);

 	 			

 			}
 		}		
	}
	
	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generatePtcClientProtInfoTagDataPathFileXML(int nodeId, int networkId, DbService dbService, Document doc, Element rootElement, List<PtcClientProtInfo> ptcClientProtInfoServiceList,
			boolean ... ifDeleteTagEntries){ 
	
		
 		System.out.println(" ptcClientProtInfoServiceList "+ ptcClientProtInfoServiceList.size());	
 		
 		for(int j=0; j<ptcClientProtInfoServiceList.size(); j++)
 		{
 			// DataPathDetails->PtcClientProtInfo
 			Element PtcClientProtInfo  = doc.createElement("PtcClientProtInfo");
			rootElement.appendChild(PtcClientProtInfo);		


			Map<String, Object> listToSearchFor = new HashMap<>();
			listToSearchFor.put("ptcClientProtInfoServiceList", ptcClientProtInfoServiceList.get(j));

			//DataPathDetails->PtcClientProtInfo-> TYPE
			Element TYPE  = doc.createElement("TYPE");	

	 		if(ifDeleteTagEntries.length!= MapConstants.I_ZERO) {
	 			if(ifDeleteTagEntries[MapConstants.I_ZERO]) {	 				
	 				TYPE.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.DeleteTagValue)));/**For Delete List*/
	 			}
	 		}
	 		else {
	 			TYPE.appendChild(doc.createTextNode(String.valueOf(
												injectTagFor(DataPathConfigFileConstants.InjectTagForPtcClientProtInfo, listToSearchFor, nodeId, dbService)	 		
												)));
	 		}
			PtcClientProtInfo.appendChild(TYPE);
 			
 			{
 	 			// DataPathDetails->PtcClientProtInfo->ActMpnRackId
 	 			Element ActMpnRackId  = doc.createElement("ActMpnRackId");
 	 			ActMpnRackId.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getActMpnRackId())));/** */
 	 			PtcClientProtInfo.appendChild(ActMpnRackId); 	 	

 	 			// DataPathDetails->PtcClientProtInfo->ActMpnSubrackId
 	 			Element ActMpnSubrackId  = doc.createElement("ActMpnSubrackId");
 	 			ActMpnSubrackId.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getActMpnSubrackId())));/** */
 	 			PtcClientProtInfo.appendChild(ActMpnSubrackId);

 	 			// DataPathDetails->PtcClientProtInfo->ActMpnCardId
 	 			Element ActMpnCardId  = doc.createElement("ActMpnCardId");
 	 			ActMpnCardId.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getActMpnCardId())));/** */
 	 			PtcClientProtInfo.appendChild(ActMpnCardId);

 	 			// DataPathDetails->PtcClientProtInfo->ActMpnCardType
 	 			Element ActMpnCardType  = doc.createElement("ActMpnCardType");
 	 			ActMpnCardType.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 	 					ptcClientProtInfoServiceList.get(j).getActMpnCardType()))));/** */
 	 			PtcClientProtInfo.appendChild(ActMpnCardType);

 	 			// DataPathDetails->PtcClientProtInfo->ActMpnCardSubType
 	 			Element ActMpnCardSubType  = doc.createElement("ActMpnCardSubType");
 	 			ActMpnCardSubType.appendChild(doc.createTextNode(
 	 					String.valueOf(/*ptcClientProtInfoServiceList.get(j).getActMpnCardSubType()*/
 	 							DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 	 	 	 							ptcClientProtInfoServiceList.get(j).getActMpnCardType()
 	 							))));/** with same card type map its subtype*/
 	 			System.out.println("DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(\n" + 
 	 					" 	 	 	 							ptcClientProtInfoServiceList.get(j).getActMpnCardType()  "+DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 	 	 	 							ptcClientProtInfoServiceList.get(j).getActMpnCardType()));
 	 			System.out.println("\n" + 
 	 					" 	 					String.valueOf(/*ptcClientProtInfoServiceList.get(j).getActMpnCardSubType()*/\n" + 
 	 					" 	 							DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(\n" + 
 	 					" 	 	 	 							ptcClientProtInfoServiceList.get(j).getActMpnCardType()\n" + 
 	 					" 	 							))"+
 	 					String.valueOf(/*ptcClientProtInfoServiceList.get(j).getActMpnCardSubType()*/
 	 							
 	 	 	 							ptcClientProtInfoServiceList.get(j).getActMpnCardType()
 	 							));
 	 			PtcClientProtInfo.appendChild(ActMpnCardSubType);

 	 			// DataPathDetails->PtcClientProtInfo->ActMpnPort
 	 			Element ActMpnPort  = doc.createElement("ActMpnPort");
 	 			ActMpnPort.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getActMpnPort())));/** */
 	 			PtcClientProtInfo.appendChild(ActMpnPort);

 	 			// DataPathDetails->PtcClientProtInfo->ProtMpnRackId
 	 			Element ProtMpnRackId  = doc.createElement("ProtMpnRackId");
 	 			ProtMpnRackId.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getProtMpnRackId())));/** */
 	 			PtcClientProtInfo.appendChild(ProtMpnRackId);

 	 			// DataPathDetails->PtcClientProtInfo->ProtMpnSubrackId
 	 			Element ProtMpnSubrackId  = doc.createElement("ProtMpnSubrackId");
 	 			ProtMpnSubrackId.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getProtMpnSubrackId())));/** */
 	 			PtcClientProtInfo.appendChild(ProtMpnSubrackId);

 	 			// DataPathDetails->PtcClientProtInfo->ProtMpnCardId
 	 			Element ProtMpnCardId  = doc.createElement("ProtMpnCardId");
 	 			ProtMpnCardId.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getProtMpnCardId())));/** */
 	 			PtcClientProtInfo.appendChild(ProtMpnCardId);

 	 			// DataPathDetails->PtcClientProtInfo->ProtMpnCardType
 	 			Element ProtMpnCardType  = doc.createElement("ProtMpnCardType");
 	 			ProtMpnCardType.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 	 							ptcClientProtInfoServiceList.get(j).getProtMpnCardType()))));/** */ 	 			
 	 			PtcClientProtInfo.appendChild(ProtMpnCardType);
 	 			
 	 			// DataPathDetails->PtcClientProtInfo->ProtMpnCardSubType
 	 			Element ProtMpnCardSubType  = doc.createElement("ProtMpnCardSubType");
 	 			ProtMpnCardSubType.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 	 							ptcClientProtInfoServiceList.get(j).getProtMpnCardType()/*ptcClientProtInfoServiceList.get(j).getProtMpnCardSubType()*/
 	 							))));/** with same card type map its subtype*/
 	 			PtcClientProtInfo.appendChild(ProtMpnCardSubType);
 	 			
	 			// DataPathDetails->PtcClientProtInfo->ProtMpnPort
 	 			Element ProtMpnPort  = doc.createElement("ProtMpnPort");
 	 			ProtMpnPort.appendChild(doc.createTextNode(
 	 					String.valueOf(ptcClientProtInfoServiceList.get(j).getProtMpnPort())));/**  DBG =>  Added as per the last format*/
 	 			PtcClientProtInfo.appendChild(ProtMpnPort);
 	 			
 	 			// DataPathDetails->PtcClientProtInfo->ProtCardRackId
 	 			Element ProtCardRackId  = doc.createElement("ProtCardRackId");
 	 			ProtCardRackId.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getProtCardRackId())));/** */
 	 			PtcClientProtInfo.appendChild(ProtCardRackId);

 	 			// DataPathDetails->PtcClientProtInfo->ProtCardSubrackId
 	 			Element ProtCardSubrackId  = doc.createElement("ProtCardSubrackId");
 	 			ProtCardSubrackId.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getProtCardSubrackId())));/** */
 	 			PtcClientProtInfo.appendChild(ProtCardSubrackId);

 	 			// DataPathDetails->PtcClientProtInfo->ProtCardCardId
 	 			Element ProtCardCardId  = doc.createElement("ProtCardCardId");
 	 			ProtCardCardId.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getProtCardCardId())));/** */
 	 			PtcClientProtInfo.appendChild(ProtCardCardId);

 	 			// DataPathDetails->PtcClientProtInfo->ProtTopology
 	 			Element ProtTopology  = doc.createElement("ProtTopology");
 	 			ProtTopology.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.protectionTopologyMappingConstantsHashMap.get(
 	 					ptcClientProtInfoServiceList.get(j).getProtTopology()))));/** */
 	 			PtcClientProtInfo.appendChild(ProtTopology);

 	 			// DataPathDetails->PtcClientProtInfo->ProtMechanism
 	 			Element ProtMechanism  = doc.createElement("ProtMechanism");
 	 			ProtMechanism.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.protectionConstantsHashMap.get(
 	 							ptcClientProtInfoServiceList.get(j).getProtMechanism()))));/**Y-cable : 1, OLP :2 */
 	 			PtcClientProtInfo.appendChild(ProtMechanism);

 	 			// DataPathDetails->PtcClientProtInfo->ProtStatus
 	 			Element ProtStatus  = doc.createElement("ProtStatus");
 	 			ProtStatus.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getProtStatus())));/** */
 	 			PtcClientProtInfo.appendChild(ProtStatus);

 	 			// DataPathDetails->PtcClientProtInfo->ProtType
 	 			Element ProtType  = doc.createElement("ProtType");
 	 			ProtType.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getProtType())));/** */
 	 			PtcClientProtInfo.appendChild(ProtType);
 	 			
 	 			// DataPathDetails->PtcClientProtInfo->ActiveLine
 	 			Element ActiveLine  = doc.createElement("ActiveLine");
 	 			ActiveLine.appendChild(doc.createTextNode(String.valueOf(ptcClientProtInfoServiceList.get(j).getActiveLine())));/** */
 	 			PtcClientProtInfo.appendChild(ActiveLine);
 			}
 		}		
	}
	
	/**
	 * 
	 * @param nodeId
	 * @param nodeDegree
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generatePtcLineProtInfoTagDataPathFileXML(int nodeId, int networkId, DbService dbService, Document doc, Element rootElement, List<PtcLineProtInfo> ptcLineProtInfoServiceList,
			boolean ... ifDeleteTagEntries){
		
 		//System.out.println(" ptcLineProtInfoServiceList "+ ptcLineProtInfoServiceList.size());
 		
 		for(int j=0; j<ptcLineProtInfoServiceList.size(); j++)
 		{
 			// DataPathDetails->PtcLineProtInfo
 			Element PtcLineProtInfo  = doc.createElement("PtcLineProtInfo");
			rootElement.appendChild(PtcLineProtInfo);
			 
			
			
			Map<String, Object> listToSearchFor = new HashMap<>();
			listToSearchFor.put(DataPathConfigFileConstants.InjectTagForPtcLineProtInfo, ptcLineProtInfoServiceList.get(j));
			
			
			//DataPathDetails->PtcLineProtInfo-> TYPE
			Element TYPE  = doc.createElement("TYPE");
			
			if(ifDeleteTagEntries.length!= MapConstants.I_ZERO) {
	 			if(ifDeleteTagEntries[MapConstants.I_ZERO]) {	 				
	 				TYPE.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.DeleteTagValue)));/**For Delete List*/
	 			}
	 		}
	 		else {
	 			TYPE.appendChild(doc.createTextNode(String.valueOf(
												injectTagFor(DataPathConfigFileConstants.InjectTagForPtcLineProtInfo, listToSearchFor, nodeId, dbService)	 		
												)));
	 		}
			
			PtcLineProtInfo.appendChild(TYPE);
 			
 			
 			{
 	 			// DataPathDetails->PtcLineProtInfo->ProtCardRackId
 	 			Element ProtCardRackId  = doc.createElement("ProtCardRackId");
 	 			ProtCardRackId.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getProtCardRackId())));/** */
 	 			PtcLineProtInfo.appendChild(ProtCardRackId); 	 	

 	 			// DataPathDetails->PtcLineProtInfo->ProtCardSubrackId
 	 			Element ProtCardSubrackId  = doc.createElement("ProtCardSubrackId");
 	 			ProtCardSubrackId.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getProtCardSubrackId())));/** */
 	 			PtcLineProtInfo.appendChild(ProtCardSubrackId);

 	 			// DataPathDetails->PtcLineProtInfo->ProtCardCardId
 	 			Element ProtCardCardId  = doc.createElement("ProtCardCardId");
 	 			ProtCardCardId.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getProtCardCardId())));/** */
 	 			PtcLineProtInfo.appendChild(ProtCardCardId);
 	 			
 	 			
 	 			
 	 			/**DBG => New 4 field added w.r.t cardtype and cardsubtype: requirement from protection  */
 	 			// DataPathDetails->PtcLineProtInfo->ProtCardType
 	 			Element ProtCardType  = doc.createElement("ProtCardType");
 	 			ProtCardType.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 	 					ptcLineProtInfoServiceList.get(j).getProtCardCardType()))));/** */
 	 			PtcLineProtInfo.appendChild(ProtCardType);
 	 			
 	 			// DataPathDetails->PtcLineProtInfo->ProtCardSubType
 	 			Element ProtCardSubType  = doc.createElement("ProtCardSubType");
 	 			ProtCardSubType.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 	 					ptcLineProtInfoServiceList.get(j).getProtCardCardType()))));/** with same card type map its subtype*/
 	 			PtcLineProtInfo.appendChild(ProtCardSubType);
 	 			
 	 			

 	 			// DataPathDetails->PtcLineProtInfo->MpnRackId
 	 			Element MpnRackId  = doc.createElement("MpnRackId");
 	 			MpnRackId.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getMpnRackId())));/** */
 	 			PtcLineProtInfo.appendChild(MpnRackId);

 	 			// DataPathDetails->PtcLineProtInfo->MpnSubrackId
 	 			Element MpnSubrackId  = doc.createElement("MpnSubrackId");
 	 			MpnSubrackId.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getMpnSubrackId())));/** */
 	 			PtcLineProtInfo.appendChild(MpnSubrackId);

 	 			// DataPathDetails->PtcLineProtInfo->MpnCardId
 	 			Element MpnCardId  = doc.createElement("MpnCardId");
 	 			MpnCardId.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getMpnCardId())));/** */
 	 			PtcLineProtInfo.appendChild(MpnCardId);
 	 			
 	 			
 	 			
 	 			/**DBG => New 4 field added w.r.t cardtype and cardsubtype: requirement from protection  */
 	 			// DataPathDetails->PtcLineProtInfo->MpnCardType
 	 			Element MpnCardType  = doc.createElement("MpnCardType");
 	 			MpnCardType.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.cardMappingConstantsHashMap.get(
 	 							ptcLineProtInfoServiceList.get(j).getMpnCardType()))));/** */
 	 			PtcLineProtInfo.appendChild(MpnCardType);
 	 			
 	 			// DataPathDetails->PtcLineProtInfo->MpnCardSubType
 	 			Element MpnCardSubType  = doc.createElement("MpnCardSubType");
 	 			MpnCardSubType.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.subtypeCardMappingConstantsHashMap.get(
 	 					ptcLineProtInfoServiceList.get(j).getMpnCardType()))));/** with same card type map its subtype*/
 	 			PtcLineProtInfo.appendChild(MpnCardSubType);
 	 			
 	 			

 	 			// DataPathDetails->PtcLineProtInfo->ProtTopology
 	 			Element ProtTopology  = doc.createElement("ProtTopology");
 	 			
 	 			ProtTopology.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.protectionTopologyMappingConstantsHashMap.get(ptcLineProtInfoServiceList.get(j).getProtTopology()))));/** Input => 1:1 : 5, 1:2R: 6*/ 	 			
 	 			/*ProtTopology.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getProtTopology())));*//** Input => 1:1 : 5, 1:2R: 6*/
 	 			PtcLineProtInfo.appendChild(ProtTopology);

 	 			// DataPathDetails->PtcLineProtInfo->ProtMechanism
 	 			Element ProtMechanism  = doc.createElement("ProtMechanism"); 	 			
 	 			ProtMechanism.appendChild(doc.createTextNode(String.valueOf(
 	 					DataPathConfigFileConstants.protectionConstantsHashMap.get(
 	 					ptcLineProtInfoServiceList.get(j).getProtMechanism()))));/** Input => 0 */
 	 			PtcLineProtInfo.appendChild(ProtMechanism);

 	 			// DataPathDetails->PtcLineProtInfo->ProtStatus
 	 			Element ProtStatus  = doc.createElement("ProtStatus");
 	 			ProtStatus.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getProtStatus())));/** Input => Enable :1, Disable : 0 */
 	 			PtcLineProtInfo.appendChild(ProtStatus);

 	 			// DataPathDetails->PtcLineProtInfo->ProtType
 	 			Element ProtType  = doc.createElement("ProtType");
 	 			ProtType.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getProtType())));/** Input => Revertive :1, Non Revertive : 2 */
 	 			PtcLineProtInfo.appendChild(ProtType);
 	 			
 	 			// DataPathDetails->PtcLineProtInfo->ActiveLine
 	 			Element ActiveLine  = doc.createElement("ActiveLine");
 	 			ActiveLine.appendChild(doc.createTextNode(String.valueOf(ptcLineProtInfoServiceList.get(j).getActiveLine())));/**Input => Primary: 1, Secondary:2 */
 	 			PtcLineProtInfo.appendChild(ActiveLine);

 			}
 		}		
	}
	
	/**
	 * 
	 * @param nodeId
	 * @param networkId
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateOSCTagDataPathFileXML(int nodeId, int networkId, DbService dbService, Document doc, Element rootElement){ 
		
		/**SUPY Card info Fetch*/
		List<CardInfo> oscCardInfoServiceList = dbService.getCardInfoService().
				FindCardInfoByCardType(networkId, nodeId, ResourcePlanConstants.CardSupy);
 		
		System.out.println(" oscCardInfoServiceList "+ oscCardInfoServiceList.size());
 		
 		for(int j=0; j<oscCardInfoServiceList.size(); j++)
 		{
 			// DataPathDetails->Osc
 			Element Osc  = doc.createElement("Osc");
			rootElement.appendChild(Osc); 		
			
			//DataPathDetails->PtcClientProtInfo-> TYPE
			Element TYPE  = doc.createElement("TYPE");
			TYPE.appendChild(doc.createTextNode(String.valueOf("0")));
			Osc.appendChild(TYPE);
			 
 			{
 	 			// DataPathDetails->OSc->RackId
 	 			Element RackId  = doc.createElement("RackId");
 	 			RackId.appendChild(doc.createTextNode(String.valueOf(oscCardInfoServiceList
 	 					.get(j).getRack())));/** */
 	 			Osc.appendChild(RackId);
 	 			
 	 			// DataPathDetails->OSc->SubrackId
 	 			Element SubrackId  = doc.createElement("SubrackId");
 	 			SubrackId.appendChild(doc.createTextNode(String.valueOf(oscCardInfoServiceList
 	 					.get(j).getSbrack())));/** */
 	 			Osc.appendChild(SubrackId); 
 	 			
 	 			// DataPathDetails->OSc->CardId
 	 			Element CardId  = doc.createElement("CardId");
 	 			CardId.appendChild(doc.createTextNode(String.valueOf(oscCardInfoServiceList
 	 					.get(j).getCard())));/** */
 	 			Osc.appendChild(CardId); 
 	 			
 	 			// DataPathDetails->OSc->SubType
 	 			Element SubType  = doc.createElement("SubType");
 	 			SubType.appendChild(doc.createTextNode(String.valueOf(DataPathConfigFileConstants.
 	 					subtypeCardMappingConstantsHashMap.get(DataPathConfigFileConstants.SUPY))));/** */
 	 			Osc.appendChild(SubType); 

 			}
 			
 		}
	}
	
	
	/**
	 * 
	 * @param nodeId
	 * @param networkId
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateNetworkDetailsTagDataPathFileXML(int networkId, DbService dbService, Document doc, Element rootElement){ 
		
		System.out.println( " NodeSetForNetworkInfo : "+ NodeSetForNetworkInfo);
		
		for(int nodeId: NodeSetForNetworkInfo){
			System.out.println(" Node Id To Process : "+ nodeId);
			
			/**Get Node-Network Info*/
	 		Node nodeObj = dbService.getNodeService().FindNode(networkId, nodeId);	
	 				
	 		{
	 			
	 			Element NetworkDetails  = doc.createElement("NetworkDetails");
				rootElement.appendChild(NetworkDetails); 
				 
					
				//DataPathDetailsNetworkDetails->TYPE
				Element TYPE  = doc.createElement("TYPE");
				TYPE.appendChild(doc.createTextNode(String.valueOf("0")));
				NetworkDetails.appendChild(TYPE);	
				 
				
	 			//DataPathDetails->NetworkDetails->Version
	 			Element Version  = doc.createElement("Version");
	 			Version.appendChild( doc.createTextNode("1.0"));
	 			NetworkDetails.appendChild(Version);
		          			
	 			// DataPathDetails->NetworkDetails->SiteName
		         Element SiteName = doc.createElement("SiteName");
		         SiteName.appendChild( doc.createTextNode(nodeObj.getSiteName()));
		         NetworkDetails.appendChild(SiteName);

		         // DataPathDetails->NetworkDetails->StationName
		         Element StationName = doc.createElement("StationName");
		         StationName.appendChild( doc.createTextNode(nodeObj.getStationName()));
		         NetworkDetails.appendChild(StationName);
		         
		         
		         // DataPathDetails->NetworkDetails->SAPI
		         Element SAPI= doc.createElement("SAPI");
		         SAPI.appendChild( doc.createTextNode(String.valueOf("0")));
		         NetworkDetails.appendChild( SAPI);
		         
		         // DataPathDetails->NetworkDetails->Subnet
		         Element Subnet= doc.createElement("Subnet");
		         Subnet.appendChild( doc.createTextNode(String.valueOf(
	 		 			IPv4.integerToStringIP(
	 		 			dbService.getIpSchemeNodeService().
	 		 			FindIpSchemeNode(networkId, nodeId).getMcpSubnet()))));
		         NetworkDetails.appendChild( Subnet);
		         
		         // DataPathDetails->  NetworkDetails->RouterIp
		         Element RouterIp= doc.createElement("RouterIp");
		         RouterIp.appendChild( doc.createTextNode(String.valueOf(
		        		 			IPv4.integerToStringIP(
		        		 			dbService.getIpSchemeNodeService().
		        		 			FindIpSchemeNode(networkId, nodeId).getRouterIp()))));
		         NetworkDetails.appendChild( RouterIp);
		         
		         // DataPathDetails->NetworkDetails->IsGNE
		         Element IsGNE= doc.createElement("IsGNE");
		         IsGNE.appendChild( doc.createTextNode(String.valueOf(nodeObj.getIsGne())));
		         NetworkDetails.appendChild( IsGNE);

	 		}	
	 			
		}
		
		NodeSetForNetworkInfo.clear(); /**Flush The List*/
				
	}
	

	/**
	 * 
	 * @param nodeId
	 * @param networkId
	 * @param dbService
	 * @param doc
	 * @param rootElement
	 */
	public void generateVoaConfigInfoTagDataPathFileXML(int nodeId, int networkId, DbService dbService, Document doc, Element rootElement){  
		
		/**VOA Config info Fetch*/
		List<VoaConfigInfo> voaConfigInfoServiceList = dbService.getVoaConfigService()
				.FindAll(networkId, nodeId);				
				
		System.out.println(" voaConfigInfoServiceList "+ voaConfigInfoServiceList.size());
 		
 		for(int j=0; j<voaConfigInfoServiceList.size(); j++)
 		{
 			// DataPathDetails->VoaConfigInfo
 			Element VoaConfigInfo  = doc.createElement("VoaConfigInfo");
			rootElement.appendChild(VoaConfigInfo); 
			
			
			//DataPathDetails->VoaConfigInfo->TYPE
			Element TYPE  = doc.createElement("TYPE");
			TYPE.appendChild(doc.createTextNode(String.valueOf("0")));
			VoaConfigInfo.appendChild(TYPE);	
			 
 			{
 	 			// DataPathDetails->VoaConfigInfo->RackId
 	 			Element RackId  = doc.createElement("RackId");
 	 			RackId.appendChild(doc.createTextNode(String.valueOf(voaConfigInfoServiceList
 	 					.get(j).getRack())));/** */
 	 			VoaConfigInfo.appendChild(RackId);
 	 			
 	 			// DataPathDetails->VoaConfigInfo->SubrackId
 	 			Element SubrackId  = doc.createElement("SubrackId");
 	 			SubrackId.appendChild(doc.createTextNode(String.valueOf(voaConfigInfoServiceList
 	 					.get(j).getSbrack())));/** */
 	 			VoaConfigInfo.appendChild(SubrackId); 
 	 			
 	 			// DataPathDetails->VoaConfigInfo->CardId
 	 			Element CardId  = doc.createElement("CardId");
 	 			CardId.appendChild(doc.createTextNode(String.valueOf(voaConfigInfoServiceList
 	 					.get(j).getCard())));/** */
 	 			VoaConfigInfo.appendChild(CardId); 
 	 			
 	 			// DataPathDetails->VoaConfigInfo->Direction
 	 			Element Direction  = doc.createElement("Direction");
 	 			Direction.appendChild(doc.createTextNode(String.valueOf(voaConfigInfoServiceList
 	 					.get(j).getDirection())));/** */
 	 			VoaConfigInfo.appendChild(Direction); 
 	 			
 	 			// DataPathDetails->VoaConfigInfo->ChannelType
 	 			Element ChannelType  = doc.createElement("ChannelType");
 	 			ChannelType.appendChild(doc.createTextNode(String.valueOf(voaConfigInfoServiceList
 	 					.get(j).getChannelType())));/** */
 	 			VoaConfigInfo.appendChild(ChannelType); 
 	 			
 	 			// DataPathDetails->VoaConfigInfo->AttenuationConfigMode
 	 			Element AttenuationConfigMode  = doc.createElement("AttenuationConfigMode");
 	 			AttenuationConfigMode.appendChild(doc.createTextNode(String.valueOf(voaConfigInfoServiceList
 	 					.get(j).getAttenuationConfigMode())));/** */
 	 			VoaConfigInfo.appendChild(AttenuationConfigMode); 
 	 			
 	 			// DataPathDetails->VoaConfigInfo->Attenuation
 	 			Element Attenuation  = doc.createElement("Attenuation");
 	 			Attenuation.appendChild(doc.createTextNode(String.valueOf(voaConfigInfoServiceList
 	 					.get(j).getAttenuation())));/** */
 	 			VoaConfigInfo.appendChild(Attenuation); 
 	 	

 			}
 			
 		}
	}
 		

	/**
	 * @brief  This API Check For suitable Case for Brownfield Configpath file : Add, Modify or Delete Case	 * 
	 * @param  InjectTagFor     tring which contains the parent tag name
	 * @param  listToSearchFor
	 * @return int             No Change: 0, Add: 1, Modify: 2, Delete:3             
	 * @author hp
	 * @date   Mar 15, 2018
	 */
	@SuppressWarnings("rawtypes")
	public int injectTagFor(String InjectTagFor,Map<String, Object> listToSearchFor, int nodeId, DbService dbService) {
		
		DataPathLogger.info(" injectTagFor() :  "+ InjectTagFor);
		DataPathLogger.info(" currentNetworkType : "+currentNetworkType);
		DataPathLogger.info("GreenField Id : "+ greenFieldNetworkId + " and Brownfield Id : "+ brownFieldNetworkId);
		int type=0;
		
		if(currentNetworkType.equalsIgnoreCase(MapConstants.GreenField))
			return type;

		else if(currentNetworkType.equalsIgnoreCase(MapConstants.BrownField))
		{
			
			try{
			
			
			switch(InjectTagFor){


				case DataPathConfigFileConstants.InjectTagForDirectionDetails :{
				
					DataPathLogger.info(" InjectTagForDirectionDetails ");								
					

					List<RouteMapping> listAddedNetworkRouteWithDemand = dbService.getNetworkRouteService()
												.FindAddedRouteDemMappedInBrField(greenFieldNetworkId, brownFieldNetworkId);
					
					DataPathLogger.info(" Added Lambda Query Output : " +listAddedNetworkRouteWithDemand);


					List<RouteMapping> listModifiedNetworkRouteWithDemand = dbService.getNetworkRouteService()
												.FindModifiedRouteDemMappedInBrField(greenFieldNetworkId, brownFieldNetworkId);
					
					DataPathLogger.info(" Modified Lambda Query Output : " +listModifiedNetworkRouteWithDemand);


					List<RouteMapping> listDeletedNetworkRouteWithDemand = dbService.getNetworkRouteService()
												.FindDeletedRouteDemMappedInBrField(greenFieldNetworkId, brownFieldNetworkId);
					
					DataPathLogger.info(" Deleted Lambda Query Output : " +listDeletedNetworkRouteWithDemand);





					DataPathLogger.info(" listToSearchFor "+ listToSearchFor.get("teLinkPerDirectionList"));

					 /** for per node link data*/
					 List<Map<String,Object>> teAllLambdaPerLinkListObj   = dbService.getLinkService().PerLinkWavelengths_cf
																			 (brownFieldNetworkId, nodeId, 
																			  ((Link)listToSearchFor.get("teLinkPerDirectionList"))
																			  .getLinkId()
																			  );

					DataPathLogger.info(" teAllLambdaPerLinkListObj : "+teAllLambdaPerLinkListObj);
					

					for(int j=0; j<teAllLambdaPerLinkListObj.size(); j++)
					{
						final int localJ = j;

						DataPathLogger.info(" teAllLambdaPerLinkListObj Wavelength : "+teAllLambdaPerLinkListObj.get(localJ).get("Wavelength"));					
									
						
						if(!
							listAddedNetworkRouteWithDemand
							.stream()
							.filter(rowElement -> rowElement.getWavelengthNo() == (int) teAllLambdaPerLinkListObj.get(localJ).get("Wavelength"))
							.collect(Collectors.toList())
							.isEmpty()			 		    
						) 	
						{
							//type = DataPathConfigFileConstants.AddTagValue;
							type = DataPathConfigFileConstants.ModifiedTagValue; /**DBG => As of now consider as modify case */
						}
						
						else if(!listDeletedNetworkRouteWithDemand.isEmpty()  || !listModifiedNetworkRouteWithDemand.isEmpty()) {
							type = DataPathConfigFileConstants.ModifiedTagValue; /**DBG => As of now consider as modify case */	
						}						
					}		
				}
				break;


				case DataPathConfigFileConstants.InjectTagForLAMBDALSP :{
				
					DataPathLogger.info(" InjectTagFor  : "+ InjectTagFor);					
					
					DataPathLogger.info(" listToSearchFor:  "+ listToSearchFor);
						
					if(!
						dbService.getLambdaLspInformationSerivce()
							.FindAddedLambdaLspInBrField(greenFieldNetworkId, brownFieldNetworkId)
							.stream()
							.filter(rowElement -> 
												(int)rowElement.getDemandId() ==
														 (int)listToSearchFor.get("DemandId")
												&&
												(int)rowElement.getRoutePriority() ==
														 (int)listToSearchFor.get("RoutePriority")												
												&&
												String.valueOf(rowElement.getPath()).
														equalsIgnoreCase(listToSearchFor.get("Path").toString())		
								   )
							.collect(Collectors.toList())
							.isEmpty()			 		    
					) 	
					{
						type = DataPathConfigFileConstants.AddTagValue;
					}
					
				


				}
				break;


				case DataPathConfigFileConstants.InjectTagForOTNTDMLSP :{
					
					DataPathLogger.info(" InjectTagFor  : "+ InjectTagFor);					
					


					if(!
						dbService.getOtnLspInformationSerivce()
							.FindCommonOtnLspInBrField(greenFieldNetworkId, brownFieldNetworkId)
							.stream()
							.filter(rowElement -> 
										(int)rowElement.getDemandId() ==
												(int)listToSearchFor.get("DemandId")
										&&
										(int)rowElement.getRoutePriority() ==
												(int)listToSearchFor.get("RoutePriority")		
										&&
										(int)rowElement.getOtnLspTunnelId() ==
												(int)listToSearchFor.get("OtnLspTunnelId")		
										&&										
										String.valueOf(rowElement.getTrafficType()).
												equalsIgnoreCase(listToSearchFor.get("TrafficType").toString())											
										&&
										String.valueOf(rowElement.getPath()).
												equalsIgnoreCase(listToSearchFor.get("Path").toString())
										&&
										String.valueOf(rowElement.getCircuitId()).
												equalsIgnoreCase(listToSearchFor.get("CircuitId").toString())		
			   				)
							.collect(Collectors.toList())
							.isEmpty()			 		    
					) 	
					{
						
						type = DataPathConfigFileConstants.NoChangeTagValue;
					}


					else if(!
						dbService.getOtnLspInformationSerivce()
							.FindAddedOtnLspInBrField(greenFieldNetworkId, brownFieldNetworkId)
							.stream()
							.filter(rowElement -> 
												(int)rowElement.getOtnLspTunnelId() ==
												(int)listToSearchFor.get("OtnLspTunnelId")
			   				)
							.collect(Collectors.toList())
							.isEmpty()			 		    
					) 	
					{						
						type = DataPathConfigFileConstants.AddTagValue;
					}	
					
					
					else if(!
							dbService.getOtnLspInformationSerivce()
							.FindModifiedOtnLspInBrField(greenFieldNetworkId,brownFieldNetworkId,nodeId)
							.stream()
							.filter(rowElement -> 
													(int)rowElement.get("DemandId") ==
													(int)listToSearchFor.get("DemandId")
													&&
													(int)rowElement.get("RoutePriority") ==
															(int)listToSearchFor.get("RoutePriority")		
													&&
													(int)rowElement.get("OtnLspTunnelId") ==
															(int)listToSearchFor.get("OtnLspTunnelId")																								
													&&
													String.valueOf(rowElement.get("Path")).
															equalsIgnoreCase(listToSearchFor.get("Path").toString())		)
							.collect(Collectors.toList())
							.isEmpty()			 		    
					) 	
					{
						type = DataPathConfigFileConstants.ModifiedTagValue;
						System.out.println(" modified case " + type);
					}

								
				}			
				break;


				
				case DataPathConfigFileConstants.InjectTagForPtcClientProtInfo :{
					
					DataPathLogger.debug(" InjectTagForPtcClientProtInfo ");
					DataPathLogger.debug(" Modified Query Output : " +dbService.getPtcClientProtInfoService()
							.FindAllModifiedBrField(brownFieldNetworkId,greenFieldNetworkId,nodeId) );

					DataPathLogger.debug(" Added Query Output : " +dbService.getPtcClientProtInfoService()
							.FindAllAddedBrField(brownFieldNetworkId,greenFieldNetworkId,nodeId) );
												
					
					DataPathLogger.debug(" listToSearchFor.get(\"ptcClientProtInfoServiceList\") "+ listToSearchFor.get("ptcClientProtInfoServiceList"));

					

					if(!
						dbService.getPtcClientProtInfoService()
						.FindAllAddedBrField(brownFieldNetworkId,greenFieldNetworkId,nodeId) 
						.stream()
						.filter(
								rowElement -> rowElement.getActMpnRackId()
									== ((PtcClientProtInfo)listToSearchFor.get("ptcClientProtInfoServiceList")).getActMpnRackId()
								&&

								rowElement.getActMpnSubrackId()
									== ((PtcClientProtInfo)listToSearchFor.get("ptcClientProtInfoServiceList")).getActMpnSubrackId()

								&&
								
								rowElement.getActMpnCardId()
									== ((PtcClientProtInfo)listToSearchFor.get("ptcClientProtInfoServiceList")).getActMpnCardId()
									
								&&
								
								rowElement.getActMpnPort()
									== ((PtcClientProtInfo)listToSearchFor.get("ptcClientProtInfoServiceList")).getActMpnPort()
								)
						.collect(Collectors.toList())
						.isEmpty()			 		    
					) 	
					{
						type = DataPathConfigFileConstants.AddTagValue;
					}				
								
				}			
				break;
				
				case DataPathConfigFileConstants.InjectTagForPtcLineProtInfo :{
					
					DataPathLogger.debug(" InjectTagForPtcLineProtInfo ");
					

					DataPathLogger.debug(" Added Query Output : " +dbService.getPtcLineProtInfoService()
							.FindAllAddedBrField(brownFieldNetworkId,greenFieldNetworkId,nodeId) );
												
					
					DataPathLogger.debug(" listToSearchFor.get(\"getPtcLineProtInfoService\") "+ listToSearchFor.get(DataPathConfigFileConstants.InjectTagForPtcLineProtInfo));

					

					if(!
						dbService.getPtcLineProtInfoService()
						.FindAllAddedBrField(brownFieldNetworkId,greenFieldNetworkId,nodeId) 
						.stream()
						.filter(
								rowElement -> rowElement.getProtCardRackId()
									== ((PtcClientProtInfo)listToSearchFor.get(DataPathConfigFileConstants.InjectTagForPtcLineProtInfo)).getProtCardRackId()
								&&

								rowElement.getProtCardSubrackId()
									== ((PtcClientProtInfo)listToSearchFor.get(DataPathConfigFileConstants.InjectTagForPtcLineProtInfo)).getProtCardSubrackId()

								&&
								
								rowElement.getProtCardCardId()
									== ((PtcClientProtInfo)listToSearchFor.get(DataPathConfigFileConstants.InjectTagForPtcLineProtInfo)).getProtCardCardId()
								)
						.collect(Collectors.toList())
						.isEmpty()			 		    
					) 	
					{
						type = DataPathConfigFileConstants.AddTagValue;
					}
				}
				break;


				case DataPathConfigFileConstants.InjectTagForTPNDetails :{
					
					DataPathLogger.debug(" InjectTagForTPNDetails ");
					
					List<CardInfo> cardInfoAddedCardsList = dbService.getCardInfoService()
													.FindAddedCardsInBrField(greenFieldNetworkId,brownFieldNetworkId,nodeId);

													


					DataPathLogger.debug(" Added Query Output : " +cardInfoAddedCardsList);												
					
					DataPathLogger.debug(" listToSearchFor: "+ listToSearchFor.get(DataPathConfigFileConstants.InjectTagForTPNDetails));
					
					
					if(!
						cardInfoAddedCardsList
						.stream()
						.filter(
								rowElement -> rowElement.getRack()
									== ((CardInfo)listToSearchFor.get(DataPathConfigFileConstants.InjectTagForTPNDetails)).getRack()
								&&

								rowElement.getSbrack()
									== ((CardInfo)listToSearchFor.get(DataPathConfigFileConstants.InjectTagForTPNDetails)).getSbrack()

								&&
								
								rowElement.getCard()
									== ((CardInfo)listToSearchFor.get(DataPathConfigFileConstants.InjectTagForTPNDetails)).getCard()
								)
						.collect(Collectors.toList())
						.isEmpty()			 		    
					) 	
					{
						type = DataPathConfigFileConstants.AddTagValue;
					}
				}

				break;

				case DataPathConfigFileConstants.InjectTagForCmMcsPortMapping:{

					DataPathLogger.debug(" InjectTagForCmMcsPortMapping ");
					
					List<McsPortMapping> mcsInfoAddedCardsList = dbService.getMcsPortMappingService()
													.FindAllAddedBrField(brownFieldNetworkId,greenFieldNetworkId,nodeId);

					List<McsPortMapping> mcsInfoModifiedCardsList = dbService.getMcsPortMappingService()
													.FindAllModifiedBrField(brownFieldNetworkId,greenFieldNetworkId,nodeId);

					List<McsPortMapping> mcsInfoDeletedCardsList = dbService.getMcsPortMappingService()
													.FindAllDeletedBrField(brownFieldNetworkId,greenFieldNetworkId,nodeId);


					
		
					
					if( MapConstants.I_ZERO!= mcsInfoAddedCardsList.size() 	    
					    ||
						 MapConstants.I_ZERO!= mcsInfoModifiedCardsList.size() 	    
						||
						MapConstants.I_ZERO!= mcsInfoDeletedCardsList.size() 	    
					) /**In either case set as modified */

					{
						type = DataPathConfigFileConstants.ModifiedTagValue;
					}
					
				


				}
				break;
				
				default:{
					DataPathLogger.info("Default Case for injectTagFor");/**nothing to do*/
					type = -1;
				}				
				break;
			 }	
			}
			catch(Exception e)	{
				DataPathLogger.error(" Error In InjectTag : "+ type );
				e.printStackTrace();
			}
		
		}
		
		DataPathLogger.info(" Inject Type To Return : "+ type );
		return type;
	}

	/**Just Demo */
	interface add{
		public int addInterface(int a, int b);
	}
 	 			
	
	
	/**
	 * 
	 * @param nodeSiteName
	 * @param nodeStationName
	 */
	public void createTarGz(String nodeSiteName, String nodeStationName) {
	   
		String home = System.getProperty("user.home");		
		String xmlFileName = home+"/Downloads/ConfigPathFiles/DataPath/"+nodeSiteName+"_"+nodeStationName+"_"+"DataPath"+".xml"; 
		
		/**tar.gz start*/
        String Tar_Name=nodeSiteName+"_"+nodeStationName+"_DataPath"+".tar.gz";        
        new TAR(DataPathConfigFileConstants.PATH_FILENAME);//Tar Object Created
        
        File directory = new File(xmlFileName);
         try {      	 
			TAR.compress(Tar_Name,directory);		
         } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @brief This is an API to print the file
	 * 
	 * @param doc
	 * @param nodeId
	 * @param nodeStationName 
	 * @param nodeSiteName 
	 * @throws TransformerException 
	 */
	@Autowired
	public void printDataPathConfigFile(Document doc, int nodeId, int index, String nodeSiteName, String nodeStationName) throws TransformerException{
		
		
		 String home = System.getProperty("user.home");
         File fileDir = new File(home+"/Downloads/ConfigPathFiles");
	        
         if (!fileDir.exists()) {
        	 if (fileDir.mkdir()) {
        		 System.out.println("Directory is created!");
        	 } else {
        		 System.out.println("Failed to create directory!");
        	 }
         }		        
		 
		   
		 fileDir = new File(home+"/Downloads/ConfigPathFiles/DataPath");
				        
		 if (!fileDir.exists()) {
			 if (fileDir.mkdir()) {
				 System.out.println("Directory is created!");
			 } else {
				 System.out.println("Failed to create directory!");
			 }
		 }         
         
         /**
          * Delete All Existed File
          */
         
         if(index==0){/**Just for first time*/
        	 String[]entries = fileDir.list();
        	 
	         
	         for(String s: entries){
	        	 
	             File currentFile = new File(fileDir.getPath(),s);
	             
	             if(currentFile.delete()) {
	            	 
	             }
	             else {/**Tar Directory data delete*/
	            	 ////System.out.println("S ==================> "+s);	 
	            	 
	            	 File fileDirLocal = new File(home+"/Downloads/ConfigPathFiles/DataPath/Tar");
	            	 
	            	 String[] entriesLocal = fileDirLocal.list();
	            	 
	            	
	    	         for(String sLocal: entriesLocal){
	    	        	/// System.out.println("sLocal ==================> "+sLocal);
	    	        	 File currentFileLocal  = new File(fileDirLocal.getPath(),sLocal);
	    	        	 currentFileLocal.delete();
	    	         }
	    	         
	    	        
	             }
	             	        
	         }				         
         }
         

         
		 
		 
	     String xmlFileName = home+"/Downloads/ConfigPathFiles/DataPath/"+nodeSiteName+"_"+nodeStationName+"_"+nodeId+"_"+"DataPath"+".xml"; 
	     MainMap.logger.info("xmlFileName :- "+xmlFileName);		 
		 
	     
         fileDir = new File(home+"/Downloads/ConfigPathFiles/DataPath/Tar");																																																																																 
	        
         if (!fileDir.exists()) {
        	 if (fileDir.mkdir()) {
        		 System.out.println("Tar Directory is created!");
        	 } else {
        		 System.out.println("Failed to create directory!");
        	 }
		 }	
         
		
         
         
         /** Write the content into xml file */
         TransformerFactory transformerFactory =  TransformerFactory.newInstance();
         Transformer transformer 			   =  transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File(xmlFileName));
         transformer.transform(source, result);
       
      
	}

	
	/**
	 * @brief This Method generate the config file(xml format) after taking inputs from the various tables
	 * @param dbService
	 * @param jsonStr
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public int generateDataPathConfigFile(String jsonStr, DbService dbService){
	
		try{
			
			 /**Logger Start*/		
		 	 MainMap.logger = MainMap.logger.getLogger(MapWebGenerateDataPathConfigFile.class.getName());
		 	 
		 	/**Map From Common API*/ 
		 	 HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
				
			int networkId=  (int) networkInfoMap.get(MapConstants.NetworkId);
			greenFieldNetworkId = (int) networkInfoMap.get(MapConstants.GreenFieldId);

			if(MapConstants.BrownField.equalsIgnoreCase((String) networkInfoMap.get(MapConstants.NetworkType))) {
				currentNetworkType  = MapConstants.BrownField;
				///greenFieldNetworkId = (int) networkInfoMap.get(MapConstants.GreenFieldId);
				brownFieldNetworkId = (int) networkInfoMap.get(MapConstants.BrownFieldId);
			}
			else{
				currentNetworkType= MapConstants.GreenField;
			}
			
			/**Delete All LSP entries from Database in case of GreenField Only*/
			if(currentNetworkType.equalsIgnoreCase(MapConstants.GreenField)) {
				dbService.getLambdaLspInformationSerivce().DeleteLambdaLspInformation(networkId);
				dbService.getOtnLspInformationSerivce().DeleteOtnLspInformation(networkId);
			}
				
			
			DataPathConfigFileConstants.RESOURCES_NAME =  ((String) networkInfoMap.get(MapConstants.NetworkName)).replaceAll("//", "////");/// + "_1.0";
			System.out.println(" currentNetworkType : "+currentNetworkType +
					" greenFieldNetworkId : "+ greenFieldNetworkId + " brownFieldNetworkId : "+brownFieldNetworkId);

	        List<Node> nodes = dbService.getNodeService().FindAll(networkId);
	         System.out.println("nodes.size() "+nodes.size());
	        System.out.println(" DataPathConfigFileConstants.forwardingAdjacencyGlobal : " +
	        DataPathConfigFileConstants.forwardingAdjacencyGlobal);
	        
		 	for(int i=0;i<nodes.size(); i++)
		 	 /**Data Path Formation Start*/
		 	 {
		 		int nodeIdToProcess = nodes.get(i).getNodeId();	
		 		
		 		/** Create the Doc root */	
		 		
		 		/**XML Building Started*/
		 		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		 		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		 		Document doc = dBuilder.newDocument();
		 		
		 		/**DataPathDetails Tag Start*/
		 		Element rootElement = doc.createElement("DataPathDetails");
		 		doc.appendChild(rootElement);
			
				//DataPathDetails->NetworkName
		 		Element NetworkName  = doc.createElement("NetworkName");
		 		NetworkName.appendChild( doc.createTextNode((String) networkInfoMap.get(MapConstants.NetworkName)));
				 rootElement.appendChild(NetworkName);
				 
				//DataPathDetails->NetworkType

				String networkType = ""; 
				if(MapConstants.GreenField.equalsIgnoreCase((String) networkInfoMap.get(MapConstants.NetworkType)))
					networkType = String.valueOf(MapConstants.I_ZERO);
				else if(MapConstants.BrownField.equalsIgnoreCase((String) networkInfoMap.get(MapConstants.NetworkType)))
					networkType = String.valueOf(MapConstants.I_ONE);

				Element NetworkType  = doc.createElement("NetworkType");
				NetworkType.appendChild( doc.createTextNode(networkType));
				rootElement.appendChild(NetworkType);



		 		/**DBG => Added afterwards for File Verification */
		 		String nodeStationName = dbService.getNodeService().FindStationByNodeId(nodeIdToProcess, networkId).toString();
		 		String nodeSiteName= dbService.getNodeService().FindSiteByNodeId(nodeIdToProcess, networkId).toString();
			 
				 //  DataPathDetails->SiteName
		 		Element SiteName  = doc.createElement("SiteName");
		 		SiteName.appendChild( doc.createTextNode(nodeSiteName));
		 		rootElement.appendChild(SiteName);
		 		
		 		//  DataPathDetails->StationName
		 		Element StationName  = doc.createElement("StationName");
		 		StationName.appendChild( doc.createTextNode(nodeStationName));
				 rootElement.appendChild(StationName);
				 


		   		// DataPathDetails->NodeId
		 		Element NodeId  = doc.createElement("NodeId");
		 		NodeId.appendChild( doc.createTextNode(String.valueOf(nodeIdToProcess)));
				rootElement.appendChild(NodeId);
		 	

		 		
		 		{
		        	 /**TELink Tag Start*/
			 		 {		 			 
			 			 generateTELinkTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement);
			 		 }		 		 
			 		 /**TELink Tag End*/
			 		 
			 		 /**LSP Tag Start*/
			 		 {		 			 
			 			DataPathConfigFileConstants.tunnelIdGlobal= MapConstants.I_ONE; /** Reset Tunnel to One for Next node generation*/
			 			generateLSPTagDataPathFileXML(networkId, nodeIdToProcess, nodes.get(i).getDegree(), dbService, doc, rootElement);
			 		 }		 		 
			 		 /**LSP Tag End*/
			 		 
			 		 /**TPNDetails Tag Start*/
			 		 {		 			 
			 			generateTPNDetailsTagDataPathFileXML(nodeIdToProcess,networkId, dbService, doc, rootElement);
			 		 }		 		 
			 		 /**TPNDetails Tag End*/
			 		 
					 /**Protection Tag Start*/			 		
			 		 {		 			 
			 			 //generateProtectionTagDataPathFileXML(nodeIdToProcess, nodes.get(i).getDegree(), dbService, doc, rootElement);
			 		 }		 		 
			 		 /**Protection Tag End*//*
			 		 
			 		/**CmWssDirectionConfig Tag Start*/			 
			 		 {		 			 
			 			 generateCmWssDirectionConfigTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement);
			 		 }		 		 
			 		 /**CmWssDirectionConfig Tag End*/
			 		 
			 		
			 		/**CmAmplifierConfig Tag Start*/			 	
			 		 {		 			 
			 			 generateCmAmplifierConfigTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement);
			 		 }			 		
			 		 /**CmAmplifierConfig Tag End*/		 		 
			 		
			 		/**CmMcsPortMapping Tag Start*/	
			 		 {		 	
			 			 
			 			if(dbService.getNodeService().FindNode(networkId, nodeIdToProcess).getNodeType() == MapConstants.cdRoadm) {/**for cd roadm access data from wssmap*/
			 				
			 				
			 				EquipmentPreference WssAddDropSetType = dbService.getEquipmentPreferenceService().FindPreferredEq(networkId,
			 								nodeIdToProcess, ResourcePlanConstants.CatAddDropWssCdRoadm);
							if(WssAddDropSetType.getCardType().toString().equals(ResourcePlanConstants.AddDropSingleCardSet)) {
								// api for fetching data from mcsmap
								generateCmCdNodeMscPortMappingTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement);
							}else {
								// api for fetching data from wssmap
								generateCmCdNodeWssMapMappingTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement);
							}
			 				
			 				
			 					
			 			}
			 			else {
			 				generateCmMcsPortMappingTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement);	
			 			}			 			 
			 		 }		 		 
			 		 /**CmMcsPortMapping Tag End*/
			 		 
			 		/**CmOcmConfig Tag Start*/			 		
			 		 {		 			 
			 			 generateCmOcmConfigTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement);
			 		 }		 		 
			 		 /**CmOcmConfig Tag End*/
			 		 
			 		/**PtcClientProtInfo Tag Start*/
			 		 {		 			 
			 			 
			 			List<PtcClientProtInfo> ptcClientProtInfoServiceList = dbService.getPtcClientProtInfoService().FindAll(networkId, nodeIdToProcess);
			 			 
			 			 generatePtcClientProtInfoTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement, ptcClientProtInfoServiceList);
			 			 		 
				 		 if(currentNetworkType.equalsIgnoreCase(MapConstants.BrownField)){
				 			 
				 			List<PtcClientProtInfo> ptcClientProtInfoServiceDeleteList = dbService.getPtcClientProtInfoService().FindAllDeletedBrField(brownFieldNetworkId, 
				 					greenFieldNetworkId, nodeIdToProcess);
				 			
				 			if(!ptcClientProtInfoServiceDeleteList.isEmpty())			 				
				 				generatePtcClientProtInfoTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement,ptcClientProtInfoServiceDeleteList, true);
				 		 }
			 		 }	
			 		 /**PtcClientProtInfo Tag End*/
			 		 
			 		 /**PtcLineProtInfo Tag Start*/			 		
			 		 {		 			 
			 			List<PtcLineProtInfo> ptcLineProtInfoServiceList = dbService.getPtcLineProtInfoService().FindAll(networkId, nodeIdToProcess);
			 			 
			 			 generatePtcLineProtInfoTagDataPathFileXML(nodeIdToProcess, networkId , dbService, doc, rootElement, ptcLineProtInfoServiceList);
			 			 
			 			if(currentNetworkType.equalsIgnoreCase(MapConstants.BrownField)){
				 			 
				 			List<PtcLineProtInfo> ptcLineProtInfoServiceDeleteList = dbService.getPtcLineProtInfoService().FindAllDeletedBrField(brownFieldNetworkId, 
				 					greenFieldNetworkId, nodeIdToProcess);
				 			
				 			if(!ptcLineProtInfoServiceDeleteList.isEmpty())			 				
				 				generatePtcLineProtInfoTagDataPathFileXML(nodeIdToProcess, networkId, dbService, doc, rootElement,ptcLineProtInfoServiceDeleteList, true);
				 		 }
			 			
			 		 }		 		 
			 		 /**PtcLineProtInfo Tag End*/
			 		 
			 		 /**OSC Tag Start*/			 		
			 		 {		 			 
			 			generateOSCTagDataPathFileXML(nodeIdToProcess, networkId , dbService, doc, rootElement);
			 		 }		 		 
			 		 /**OSC Tag End*/
			 		 
			 		 /**NetworkDetails Tag Start*/
			 		 
			 		 /**
			 		  * Process the Following API for Source and Destination Node from the OTN Path 
			 		  * input : NodeToProcess[numberOfNodes]
			 		  * output: Tags for all the processed Node
			 		  */
			 		 {		 			 
			 			generateNetworkDetailsTagDataPathFileXML(networkId , dbService, doc, rootElement);
			 		 }		 		 
			 		 /**NetworkDetails Tag End*/
			 		 
			 		 /**VoaConfigInfo Tag Start*/			 		
			 		 {		 			 
			 			generateVoaConfigInfoTagDataPathFileXML(nodeIdToProcess, networkId , dbService, doc, rootElement);
			 		 }		 		 
			 		 /**VoaConfigInfo Tag End*/
			 		 
		 		}
		 		/**DataPathDetails Tag End*/         
		 		/** DataPath file Printing */
		 		printDataPathConfigFile(doc, nodeIdToProcess, i,nodeSiteName,nodeStationName); 	
		 		createTarGz(nodeSiteName, nodeStationName);
		 		
		 		}
		 	
		     DataPathConfigFileConstants.forwardingAdjacencyGlobal  = 2560; /**Finally Reset to 0xA00 before next cycle file generation */
		     
		    try {
		    	 TAR.convertTartoTar(DataPathConfigFileConstants.RESOURCES_NAME,DataPathConfigFileConstants.OUTPUT_DIRECTORY); //Compress many tar file to one tar file	 
		     }
		     catch (Exception e) {
		    	 e.printStackTrace();
		     }		 	
    
			 return MapConstants.SUCCESS;
		 	 }	 
		catch (Exception e) {
	         e.printStackTrace();
	         return MapConstants.FAILURE;
		 }
	}

}
