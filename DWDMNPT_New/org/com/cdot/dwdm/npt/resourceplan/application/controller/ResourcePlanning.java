package application.controller;

import java.lang.invoke.SwitchPoint;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.mysql.fabric.RangeShardMapping;

import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.constants.SMConstants;
import application.controller.MapWebGenerateDataPathConfigFile.add;
import application.model.AggregatorClientMap;
import application.model.Card;
import application.model.CardInfo;
import application.model.CardPreference;
import application.model.Circuit;
import application.model.Circuit10gAgg;
import application.model.CircuitDemandMapping;
import application.model.Demand;
import application.model.DemandMapping;
import application.model.Equipment;
import application.model.EquipmentPreference;
import application.model.Inventory;
import application.model.Link;
import application.model.LinkWavelengthInfo;
import application.model.McsMap;
import application.model.NetworkRoute;
import application.model.Node;
import application.model.NodeSystem;
import application.model.OcmConfig;
import application.model.PatchCord;
import application.model.PortInfo;
import application.model.Rack;
import application.model.RouteMapping;
import application.model.Sbrack;
import application.model.Topology;
import application.model.WssMap;
import application.model.YCablePortInfo;
import application.service.DbService;

/**
 * 
 * @author sunaina
 * @brief This class contains the functions used in creating the resource
 *        planning i.e. Rack view generation storing cards in the database,
 *        maintaining the node pools for a particular network etc.
 *
 */
@Service
public class ResourcePlanning {
	String tag = "RP: ";
	int SelectedMpnSize = ResourcePlanConstants.SlotSizeTwo;
	HashMap<Integer, NodeSystem> poolmap = new HashMap<Integer, NodeSystem>();
	HashMap<Integer, NodeSystem> assignedmap = new HashMap<Integer, NodeSystem>();
	DbService dbService;
	ResourcePlanUtils rpu;
	public static Logger logger = Logger.getLogger(ResourcePlanning.class.getName());

	public ResourcePlanning() {
		super();
		// logger = logger.getLogger(ResourcePlanning.class.getName());
	}

	public DbService getDbService() {
		return dbService;
	}

	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public ResourcePlanning(DbService dbService) {
		super();
		this.dbService = dbService;
		rpu = new ResourcePlanUtils(dbService);
		// logger = logger.getLogger(ResourcePlanning.class.getName());

	}

	public HashMap<Integer, NodeSystem> finitPoolForNode(int nodeid, int nodetype) {
		/// logger.info(tag+"ResourcePlanning.finitPoolForNode()....Logger..................");
		/* check the node type and initiate site accordingly */
		/// logger.info(tag+"ResourcePlanning.finitPoolForNode()");

		poolmap.put(nodeid, fInitiateSite(nodeid, nodetype));
		/// logger.info(tag+"ResourcePlanning.finitPoolForNode(): Current PoolMap:
		/// "+poolmap.toString());
		return poolmap;
	}

	/**
	 * Initiates node pool, creates cardinfo db and assigns common cards in the
	 * nodes
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fInitAllNodesInNetwork(int networkid) throws SQLException {
		/// logger.info(tag+"ResourcePlanning.fInitAllNodesInNetwork()");
		poolmap.clear();
		assignedmap.clear();
		// List<NetworkRoute> routesAll =
		// dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		// List <String> paths = new ArrayList<String>();
		// String[] nodeids;
		// HashSet NodesToConfigure= new HashSet();

		ArrayList<Integer> list = new ArrayList<Integer>();

		list.addAll(rpu.fgetNodesToConfigure(networkid));

		for (int i = 0; i < list.size(); i++) {
			int nodetype = dbService.getNodeService().FindNode(networkid, list.get(i)).getNodeType();
			int nodedegree = dbService.getNodeService().FindNode(networkid, list.get(i)).getDegree();
			/* initiate the system pool according to the node type declared */
			/// logger.info("RP: Initiating site for Node Id: "+list.get(i));

			/* generate the pools and store in poolmap */
			finitPoolForNode(Integer.parseInt(list.get(i).toString()), nodetype);
			//			fCreateCardnPortTable(networkid, list.get(i));

			// fAssignCommonCards(networkid,list.get(i),nodetype,nodedegree);
		}

	}

	public void fAssignCommonCardsInNetwork(int networkid) throws SQLException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.addAll(rpu.fgetNodesToConfigure(networkid));
		for (int i = 0; i < list.size(); i++) {
			int nodetype = dbService.getNodeService().FindNode(networkid, list.get(i)).getNodeType();
			int nodedegree = dbService.getNodeService().FindNode(networkid, list.get(i)).getDegree();
			List<CardInfo> cList = fAssignCommonCardsDb(networkid, list.get(i), nodetype, nodedegree);
			fAssignMPCsInANodeDb(networkid, list.get(i));
			// assign the unallocated card on the free available slots
			fAllocateRemainingCommonCards(networkid, list.get(i), cList);
		}
	}

	/**
	 * Function will intialize the additional nodes in the network and the modified
	 * nodes (i.e. whose direction/link has been added) Function removes all the
	 * cards of the deleted nodes in the brown field
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fInitNodesInNetworkBrField(int networkid) throws SQLException {
		logger.info("info fInitNodesInNetworkBrField");
		// get the brown field network id
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());

		// get the new added nodes in the Br Field
		List<Node> newNodes = dbService.getNodeService().FindAddedNodesInBrField(networkid, networkidBF);
		for (int i = 0; i < newNodes.size(); i++) {
			logger.info("ResourcePlanning.fInitNodesInNetworkBrField() Node id " + newNodes.get(i).getNodeId());
			//			fCreateCardnPortTable(networkidBF, newNodes.get(i).getNodeId());
			fAssignCommonCardsDb(networkidBF, newNodes.get(i).getNodeId(), newNodes.get(i).getNodeType(),
					newNodes.get(i).getDegree());
			//			fAssignMPCsBrField(networkid);
			fAssignOTDRCardForNode(networkidBF,newNodes.get(i).getNodeId());
			//			fAssignMPCsBrField(networkid);
		}

		// get the deleted nodes and remove their cards
		List<Node> deletedNodes = dbService.getNodeService().FindDeletedNodesInBrField(networkid, networkidBF);
		for (int i = 0; i < deletedNodes.size(); i++) {
			logger.info("fInitNodesInNetworkBrField() Deleting Node id " + deletedNodes.get(i).getNodeId());
			dbService.getCardInfoService().DeleteCardInfo(networkidBF, deletedNodes.get(i).getNodeId());
		}

		// get the modified nodes in the Br Field
		// List <Node> modifiedNodes =
		// dbService.getNodeService().FindModifiedNodesInBrField(networkid,
		// networkidBF);
		// for (int i = 0; i < modifiedNodes.size(); i++) {
		// //find the modified parameter
		// in case of degree modification take action accordingly

		List<Node> modDegNodes = dbService.getNodeService().FindDegreeModifiedNodesInBrField(networkid, networkidBF);
		for (int j = 0; j < modDegNodes.size(); j++) {
			logger.info("ResourcePlanning.fInitNodesInNetworkBrField() modified node " + modDegNodes.get(j).getNodeId());
			int otherNode;

			List<Link> deletedLinks = dbService.getLinkService().findDeletedLinksInBrField(networkid, networkidBF,
					modDegNodes.get(j).getNodeId());
			for (int k = 0; k < deletedLinks.size(); k++) {
				if (modDegNodes.get(j).getNodeId() == deletedLinks.get(k).getSrcNode())
					otherNode = deletedLinks.get(k).getDestNode();
				else
					otherNode = deletedLinks.get(k).getSrcNode();

				logger.info("ResourcePlanning.fInitNodesInNetworkBrField() deletedLinks  "
						+ deletedLinks.get(k).getLinkId());
				String Dir = dbService.getLinkService().FindLinkDirection(networkid, modDegNodes.get(j).getNodeId(),
						otherNode);
				fRemoveCommonCardsPerDirectionDb(networkidBF, modDegNodes.get(j).getNodeId(),
						modDegNodes.get(j).getNodeType(), modDegNodes.get(j).getDegree(), Dir);
			}			

			List<Link> addedLinks = dbService.getLinkService().FindAddedLinksOnNodeInBrField(networkid, networkidBF,
					modDegNodes.get(j).getNodeId());			
			for (int k = 0; k < addedLinks.size(); k++) {
				if (modDegNodes.get(j).getNodeId() == addedLinks.get(k).getSrcNode())
					otherNode = addedLinks.get(k).getDestNode();
				else
					otherNode = addedLinks.get(k).getSrcNode();
				logger.info(
						"ResourcePlanning.fInitNodesInNetworkBrField() addedLinks  " + addedLinks.get(k).getLinkId());
				String Dir = dbService.getLinkService().FindLinkDirection(networkidBF, modDegNodes.get(j).getNodeId(),
						otherNode);

				fAssignAmplifierCardsForNode(networkidBF,modDegNodes.get(j).getNodeId(),addedLinks.get(k));
				fAssignMPCsBrField(networkid);				
				fAssignCommonCardsPerDirectionDb(networkidBF, modDegNodes.get(j).getNodeId(),
						modDegNodes.get(j).getNodeType(), modDegNodes.get(j).getDegree(), Dir);
				fAssignMPCsBrField(networkid);
			}		

			//			List<Link> modifiedLinks = dbService.getLinkService().FindModifiedLinksInBrField(networkid, networkidBF,
			//					modDegNodes.get(j).getNodeId());
			//
			//			for (int k = 0; k < modifiedLinks.size(); k++) {
			//				if (modDegNodes.get(j).getNodeId() == deletedLinks.get(k).getSrcNode())
			//					otherNode = deletedLinks.get(k).getDestNode();
			//				else
			//					otherNode = deletedLinks.get(k).getSrcNode();
			//
			//				logger.info("ResourcePlanning.fInitNodesInNetworkBrField() deletedLinks  "
			//						+ deletedLinks.get(k).getLinkId());
			//				String Dir = dbService.getLinkService().FindLinkDirection(networkid, modDegNodes.get(j).getNodeId(),
			//						otherNode);
			//				fRemoveCommonCardsPerDirectionDb(networkidBF, modDegNodes.get(j).getNodeId(),
			//						modDegNodes.get(j).getNodeType(), modDegNodes.get(j).getDegree(), Dir);
			//			}


			int degreeBF = dbService.getNodeService().FindNode(networkidBF, modDegNodes.get(j).getNodeId()).getDegree();
			int degreeGF = dbService.getNodeService().FindNode(networkid, modDegNodes.get(j).getNodeId()).getDegree();
			int ramanLinksCountGf=dbService.getLinkService().FindAllRamanLinks(networkid, modDegNodes.get(j).getNodeId()).size();
			int ramanLinksCountbf=dbService.getLinkService().FindAllRamanLinks(networkidBF, modDegNodes.get(j).getNodeId()).size();

			/* transition for 4 to 5 in brown field or Raman Links added in Bf but were not present in gf */
			if (ramanLinksCountGf==0 && (ramanLinksCountbf>0 | (degreeBF >4  && degreeGF <= 4)) )
			{
				System.out.println(" ^^^^^^ Degree has changed or Raman links added to bF ^^^^^^^");
				//				if(dbService.getDemandService().FindAllByNodeId(networkidBF, modDegNodes.get(j).getNodeId()).size()>0){

				// increase an edfa card
				String location = rpu.fGetFirstFreeDoubleSlotDb(networkidBF,  modDegNodes.get(j).getNodeId());

				int[] id = rpu.locationIds(location);
				fSaveCardInDb(networkidBF, modDegNodes.get(j).getNodeId(), id[0], id[1], id[2], ResourcePlanConstants.CardEdfa, "12", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa), "", ResourcePlanConstants.ZERO,
						"");

				//				}

				// If degree in GF was zero or there were only raman links in GF			
				fAssignOcmCardInBrownField(networkidBF,modDegNodes.get(j).getNodeId(),ResourcePlanConstants.TWO);				
			}
			/** Only Raman Links in GF*/
			else if(degreeGF==ramanLinksCountGf && degreeBF>degreeGF) {

				// increase an edfa card
				String location = rpu.fGetFirstFreeDoubleSlotDb(networkidBF,  modDegNodes.get(j).getNodeId());

				int[] id = rpu.locationIds(location);
				fSaveCardInDb(networkidBF, modDegNodes.get(j).getNodeId(), id[0], id[1], id[2], ResourcePlanConstants.CardEdfa, "11", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa), "", ResourcePlanConstants.ZERO,
						"");				

				// Place second OCM if degree has increased more than 4 in BF
				if(degreeBF >4)
					fAssignOcmCardInBrownField(networkidBF,modDegNodes.get(j).getNodeId(),ResourcePlanConstants.TWO);
			}
			fAssignMPCsBrField(networkid);
		}
		// }
	}

	/**
	 * @brief Assigns the muxponder cards in NE Stores the cards in db
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignPairedMuxponderCards(int networkid) throws SQLException {
		try {
			logger.info("fAssignPairedMuxponderCards");
			List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
			List<String> paths = new ArrayList<String>();
			String[] nodeids, pnodeids;
			for (int i = 0; i < routesAll.size(); i++) {
				if ((routesAll.get(i).getRoutePriority() == ResourcePlanConstants.ONE)) {
					nodeids = routesAll.get(i).getPath().split(",");
					for (int j = 0; j < nodeids.length; j++) {
						/*
						 * allocate the cards only on the first and the last i.e. src and the
						 * destination sites only
						 */
						if ((j == 0) | (j == nodeids.length - 1)) {
							int currentnodeid = Integer.parseInt(nodeids[j].toString());
							/// logger.info("card allocation node id "+
							/// Integer.parseInt(nodeids[j].toString()));
							// fAssigningCardsInNodes(networkid,Integer.parseInt(nodeids[j].toString()));
							int nodeid = Integer.parseInt(nodeids[j].toString());
							int nodetype = dbService.getNodeService()
									.FindNode(networkid, Integer.parseInt(nodeids[j].toString())).getNodeType();
							int nodedegree = dbService.getNodeService()
									.FindNode(networkid, Integer.parseInt(nodeids[j].toString())).getDegree();
							String DirW = "", DirP = "";
							if (j == 0)
								DirW = dbService.getLinkService().FindLinkDirection(networkid,
										Integer.parseInt(nodeids[j].toString()),
										Integer.parseInt(nodeids[j + 1].toString()));
							else if (j == nodeids.length - 1)
								DirW = dbService.getLinkService().FindLinkDirection(networkid,
										Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));

							/* Get the assigned map */
							NodeSystem assignedSys;
							if (assignedmap.get(nodeid) != null) {
								/// logger.info("found in assigned map ()");
								assignedSys = assignedmap.get(nodeid);
							} else {
								/// logger.info("found not in assigned map ()");
								assignedSys = new NodeSystem(nodeid);
							}

							/* fetch the nodepool from map for that node */
							NodeSystem nodepool = poolmap.get(nodeid);

							Demand d = dbService.getDemandService().FindDemand(networkid,
									routesAll.get(i).getDemandId());
							List<NetworkRoute> rlist = dbService.getNetworkRouteService()
									.FindAllByDemandIdUsable(networkid, routesAll.get(i).getDemandId());

							// add checks for the subrack protection and channel protection also
							Object[][] cReq = getCardRequirement(networkid, nodeid, d.getProtectionType(), "",
									d.getChannelProtection(), d.getClientProtection(), rlist.size(),
									routesAll.get(i).getLineRate(), routesAll.get(i).getDemandId(),d.getClientProtectionType());
							String mpntype = cReq[0][0].toString();
							String pairedStatus = cReq[0][1].toString();
							int mpnEid = rpu.fgetEIdFromCardType(mpntype);

							SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
							logger.info("Nodeid: " + nodeid + " DemandId: " + d.getDemandId() + " Paired Status: "
									+ pairedStatus);

							// get the preferred mpn type
							// Object mpn[]=rpu.fgetMpnPreference(networkid,nodeid);
							// String mpntype=mpn[0].toString();

							String linerate = routesAll.get(i).getLineRate();
							if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
								String dems = "";
								logger.info("ResourcePlanning.fAssignPairedMuxponderCards()" + " Node id " + nodeid
										+ " route with 10G Line found " + " Demand Id : "
										+ routesAll.get(i).getDemandId());
								Map<String, Object> Tpn10gDemands = dbService.getDemandService()
										.FindDemandsFor10GTpn(networkid, nodeid);
								if (Tpn10gDemands.get("Demands") != null) {
									logger.info("ResourcePlanning.fAssignPairedMuxponderCards() " + " Node id " + nodeid
											+ " 10g TPN Demands are " + Tpn10gDemands.get("Demands").toString());
									dems = "," + Tpn10gDemands.get("Demands").toString() + ",";
								}

								if (dems.contains("," + routesAll.get(i).getDemandId() + ",")) {
									logger.info(" 10G TPN to be allocated ");
									// needed a 10G TPN for this route
									// check if this can be added on to existing TPN
									PortInfo port = dbService.getPortInfoService().FindMax5x10GPort(networkid, nodeid);
									if (port != null) {
										if (port.getPort() < 5) {
											// port can be added to the same TPN
											logger.info(
													"ResourcePlanning.fAssignPairedMuxponderCards() Inserting on existing 5x10G TPN ");
											fAssign5x10GTPNDemand(networkid, nodeid, routesAll.get(i).getDemandId(),
													port.getRack(), port.getSbrack(), port.getCard(), DirW, mpntype);
										} else {
											logger.info(
													"ResourcePlanning.fAssignPairedMuxponderCards() Inserting new 5x10G TPN ");
											// insert a new TPN
											mpntype = ResourcePlanConstants.CardTPN5x10G;
											mpnEid = rpu.fgetEIdFromCardType(mpntype);
											SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
											logger.info("ResourcePlanning.fAssignPairedMuxponderCards()" + " Node id "
													+ nodeid + " route with 10G TPN found ");

											String[] location = fGetFirstFreeMpnSlot(networkid, nodeid, nodetype,
													nodedegree, SelectedMpnSize, pairedStatus);
											logger.info(tag + "Location Received1 :" + location[0]);
											/// logger.info(tag+"Location Received2 :"+location[1]);
											if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
												int WaveW = routesAll.get(i).getWavelengthNo();
												if (location[0] != null) {
													// assign the free mpn slot
													int[] id = rpu.locationIds(location[0]);

													assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2],
															mpntype);
													fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW,
															WaveW, d.getDemandId(), mpnEid,
															ResourcePlanConstants.Working, ResourcePlanConstants.ZERO,
															"");
													fRemoveCard(nodepool, id[0], id[1], id[2], mpntype);

													fAssign5x10GTPNDemand(networkid, nodeid,
															routesAll.get(i).getDemandId(), id[0], id[1], id[2], DirW,
															mpntype);
												}
											}

										}
									} else {
										// insert a new TPN
										logger.info(
												"ResourcePlanning.fAssignPairedMuxponderCards() Inserting 5x10G TPN ");
										mpntype = ResourcePlanConstants.CardTPN5x10G;
										mpnEid = rpu.fgetEIdFromCardType(mpntype);
										SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
										logger.info("ResourcePlanning.fAssignPairedMuxponderCards()" + " Node id "
												+ nodeid + " route with 10G TPN found ");

										String[] location = fGetFirstFreeMpnSlot(networkid, nodeid, nodetype,
												nodedegree, SelectedMpnSize, pairedStatus);
										logger.info(tag + "Location Received1 :" + location[0]);
										/// logger.info(tag+"Location Received2 :"+location[1]);
										if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
											int WaveW = routesAll.get(i).getWavelengthNo();
											if (location[0] != null) {
												// assign the free mpn slot
												int[] id = rpu.locationIds(location[0]);

												assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], mpntype);
												fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW,
														WaveW, d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
														ResourcePlanConstants.ZERO, "");
												fRemoveCard(nodepool, id[0], id[1], id[2], mpntype);

												fAssign5x10GTPNDemand(networkid, nodeid, routesAll.get(i).getDemandId(),
														id[0], id[1], id[2], DirW, mpntype);
											}
										}
									}
									poolmap.remove(nodeid);
									/* place the updated nodepool in the poolmap */
									poolmap.put(nodeid, nodepool);
								} else {
									logger.info(" 10G MPN to be allocated ");
									mpntype = ResourcePlanConstants.CardMuxponder10G;
									mpnEid = rpu.fgetEIdFromCardType(mpntype);
									SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
									String[] location = fGetFirstFreeMpnSlot(networkid, nodeid, nodetype, nodedegree,
											SelectedMpnSize, pairedStatus);
									logger.info(tag + "Location Received1 :" + location[0]);
									/// logger.info(tag+"Location Received2 :"+location[1]);
									if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))
											| (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
										int WaveW = routesAll.get(i).getWavelengthNo();

										if (location[0] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[0]);

											assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], mpntype);
											fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW, WaveW,
													d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
													ResourcePlanConstants.ZERO, "");
											fRemoveCard(nodepool, id[0], id[1], id[2], mpntype);
										}
									}

									if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
										int WaveP;
										NetworkRoute r = dbService.getNetworkRouteService().FindAllByDemandId(networkid,
												d.getDemandId(), ResourcePlanConstants.TWO);
										if (r != null) {
											pnodeids = r.getPath().split(",");
											if (currentnodeid == Integer.parseInt(pnodeids[0].toString()))
												DirP = dbService.getLinkService().FindLinkDirection(networkid,
														Integer.parseInt(pnodeids[0].toString()),
														Integer.parseInt(pnodeids[0 + 1].toString()));
											else if (currentnodeid == Integer
													.parseInt(pnodeids[pnodeids.length - 1].toString()))
												DirP = dbService.getLinkService().FindLinkDirection(networkid,
														Integer.parseInt(pnodeids[pnodeids.length - 1].toString()),
														Integer.parseInt(pnodeids[pnodeids.length - 1 - 1].toString()));

											WaveP = r.getWavelengthNo();
											if (location[1] != null) {
												// assign the free mpn slot
												int[] id = rpu.locationIds(location[1]);
												assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], mpntype);
												fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirP,
														WaveP, d.getDemandId(), mpnEid,
														ResourcePlanConstants.Protection, ResourcePlanConstants.ZERO,
														"");
												fRemoveCard(nodepool, id[0], id[1], id[2], mpntype);

											}
										}
									}
									poolmap.remove(nodeid);
									/* place the updated nodepool in the poolmap */
									poolmap.put(nodeid, nodepool);
								}
							} else if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line200)) {
								mpntype = ResourcePlanConstants.CardMuxponder200G;
								mpnEid = rpu.fgetEIdFromCardType(mpntype);
								SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);

								String[] location = fGetFirstFreeMpnSlot(networkid, nodeid, nodetype, nodedegree,
										SelectedMpnSize, pairedStatus);
								logger.info(tag + "Location Received1 :" + location[0]);
								logger.info(tag + "Location Received2 :" + location[1]);
								if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))
										| (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
									if (location[0] != null) {
										int WaveW = routesAll.get(i).getWavelengthNo();

										if (location[0] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[0]);

											assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], mpntype);
											fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW, WaveW,
													d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
													ResourcePlanConstants.ZERO, "");
											fRemoveCard(nodepool, id[0], id[1], id[2], mpntype);
										}
									}
								}

								if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
									int WaveP;
									NetworkRoute r = dbService.getNetworkRouteService().FindAllByDemandId(networkid,
											d.getDemandId(), ResourcePlanConstants.TWO);
									if (r != null) {
										pnodeids = r.getPath().split(",");
										if (currentnodeid == Integer.parseInt(pnodeids[0].toString()))
											DirP = dbService.getLinkService().FindLinkDirection(networkid,
													Integer.parseInt(pnodeids[0].toString()),
													Integer.parseInt(pnodeids[0 + 1].toString()));
										else if (currentnodeid == Integer
												.parseInt(pnodeids[pnodeids.length - 1].toString()))
											DirP = dbService.getLinkService().FindLinkDirection(networkid,
													Integer.parseInt(pnodeids[pnodeids.length - 1].toString()),
													Integer.parseInt(pnodeids[pnodeids.length - 1 - 1].toString()));

										WaveP = r.getWavelengthNo();
										if (location[1] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[1]);
											assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], mpntype);
											fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirP, WaveP,
													d.getDemandId(), mpnEid, ResourcePlanConstants.Protection,
													ResourcePlanConstants.ZERO, "");
											fRemoveCard(nodepool, id[0], id[1], id[2], mpntype);

										}
									}
								}
								poolmap.remove(nodeid);
								/* place the updated nodepool in the poolmap */
								poolmap.put(nodeid, nodepool);
							} else if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line100)) {
								String dems = "";
								logger.info("ResourcePlanning.fAssignPairedMuxponderCards()" + " Node id " + nodeid
										+ " route with 100G Line found ");
								Map<String, Object> Tpn100gDemands = dbService.getDemandService()
										.FindDemandsFor100GTpn(networkid, nodeid);
								if (Tpn100gDemands.get("Demands") != null) {
									logger.info("ResourcePlanning.fAssignPairedMuxponderCards() " + " Node id " + nodeid
											+ " 100g TPN Demands are " + Tpn100gDemands.get("Demands").toString());
									dems = "," + Tpn100gDemands.get("Demands").toString() + ",";
								}

								if (dems.contains("," + routesAll.get(i).getDemandId() + ",")) {
									logger.info(" 100G TPN to be allocated ");
									// mpntype=ResourcePlanConstants.CardTPN100G;

									// TPN CGC in place of TPN100G:28/2/18
									mpntype = ResourcePlanConstants.CardTPN100GCGC;

									mpnEid = rpu.fgetEIdFromCardType(mpntype);
								} else {
									logger.info(" 100G MPN to be allocated ");
									// get the preferred mpn type
									// mpn=rpu.fgetMpnPreference(networkid,nodeid);
									// mpntype=mpn[0].toString();
									// mpnEid= Integer.parseInt(mpn[1].toString());
								}

								SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
								String[] location = fGetFirstFreeMpnSlot(networkid, nodeid, nodetype, nodedegree,
										SelectedMpnSize, pairedStatus);
								logger.info(tag + "Location Received1 :" + location[0]);
								/// logger.info(tag+"Location Received2 :"+location[1]);
								if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))
										|| (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {

									int WaveW = routesAll.get(i).getWavelengthNo();

									if (location[0] != null) {
										// assign the free mpn slot
										int[] id = rpu.locationIds(location[0]);

										assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], mpntype);
										fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW, WaveW,
												d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
												ResourcePlanConstants.ZERO, "");
										fRemoveCard(nodepool, id[0], id[1], id[2], mpntype);
									}
								}

								if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
									int WaveP;
									NetworkRoute r = dbService.getNetworkRouteService().FindAllByDemandId(networkid,
											d.getDemandId(), ResourcePlanConstants.TWO);
									if (r != null) {
										pnodeids = r.getPath().split(",");
										if (currentnodeid == Integer.parseInt(pnodeids[0].toString()))
											DirP = dbService.getLinkService().FindLinkDirection(networkid,
													Integer.parseInt(pnodeids[0].toString()),
													Integer.parseInt(pnodeids[0 + 1].toString()));
										else if (currentnodeid == Integer
												.parseInt(pnodeids[pnodeids.length - 1].toString()))
											DirP = dbService.getLinkService().FindLinkDirection(networkid,
													Integer.parseInt(pnodeids[pnodeids.length - 1].toString()),
													Integer.parseInt(pnodeids[pnodeids.length - 1 - 1].toString()));

										WaveP = r.getWavelengthNo();
										if (location[1] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[1]);
											assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], mpntype);
											fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirP, WaveP,
													d.getDemandId(), mpnEid, ResourcePlanConstants.Protection,
													ResourcePlanConstants.ZERO, "");
											fRemoveCard(nodepool, id[0], id[1], id[2], mpntype);

										}
									}
								}
								poolmap.remove(nodeid);
								/* place the updated nodepool in the poolmap */
								poolmap.put(nodeid, nodepool);
							}

						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("fAssignPairedMuxponderCards" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @brief Assigns the muxponder cards in NE Stores the cards in db
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignPairedMuxponderCardsDb(int networkid) throws SQLException {
		try {
			logger.info("fAssignPairedMuxponderCardsDb");
			List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
			List<String> paths = new ArrayList<String>();
			String[] nodeids, pnodeids;
			String mpntypeProt, mpntype, SrNo, SrNoProt, pairedStatus;
			int mpnEidProt, mpnEid;
			for (int i = 0; i < routesAll.size(); i++) {
				if ((routesAll.get(i).getRoutePriority() == ResourcePlanConstants.ONE)) {
					nodeids = routesAll.get(i).getPath().split(",");
					for (int j = 0; j < nodeids.length; j++) {
						/*
						 * allocate the cards only on the first and the last i.e. src and the
						 * destination sites only
						 */
						if ((j == 0) | (j == nodeids.length - 1)) {
							int currentnodeid = Integer.parseInt(nodeids[j].toString());
							System.out.println("fAssignPairedMuxponderCardsDb :: currentnodeid :"+currentnodeid);
							/// logger.info("card allocation node id "+
							/// Integer.parseInt(nodeids[j].toString()));
							// fAssigningCardsInNodes(networkid,Integer.parseInt(nodeids[j].toString()));
							int nodeid = Integer.parseInt(nodeids[j].toString());
							int nodetype = dbService.getNodeService()
									.FindNode(networkid, Integer.parseInt(nodeids[j].toString())).getNodeType();
							int nodedegree = dbService.getNodeService()
									.FindNode(networkid, Integer.parseInt(nodeids[j].toString())).getDegree();
							String DirW = "", DirP = "";

							String[] linkIdSet=routesAll.get(i).getPathLinkId().split(",");

							if (j == 0)
							{
								Link link=dbService.getLinkService().FindLink(networkid, Integer.parseInt(linkIdSet[j]));

								if(link.getSrcNode()==currentnodeid)
									DirW=link.getSrcNodeDirection();
								else DirW=link.getDestNodeDirection();

								//								DirW = dbService.getLinkService().FindLinkDirection(networkid,
								//										Integer.parseInt(nodeids[j].toString()),
								//										Integer.parseInt(nodeids[j + 1].toString()));
								System.out.println("Working Path :"+ routesAll.get(i).getPath()+" NodeId :"+currentnodeid+" LinkId :"+Integer.parseInt(linkIdSet[j])+" DirW :"+DirW);
							}
							else if (j == nodeids.length - 1)
							{
								Link link=dbService.getLinkService().FindLink(networkid, Integer.parseInt(linkIdSet[linkIdSet.length-1]));

								if(link.getSrcNode()==currentnodeid)
									DirW=link.getSrcNodeDirection();
								else DirW=link.getDestNodeDirection();

								//								DirW = dbService.getLinkService().FindLinkDirection(networkid,
								//										Integer.parseInt(nodeids[nodeids.length - 1].toString()),
								//										Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));

								System.out.println("Working Path :"+ routesAll.get(i).getPath()+" NodeId :"+currentnodeid+" LinkId :"+Integer.parseInt(linkIdSet[linkIdSet.length-1])+" DirW :"+DirW);
							}




							Demand d = dbService.getDemandService().FindDemand(networkid,
									routesAll.get(i).getDemandId());
							List<NetworkRoute> rlist = dbService.getNetworkRouteService()
									.FindAllByDemandIdUsable(networkid, routesAll.get(i).getDemandId());

							// add checks for the subrack protection and channel protection also
							Object[][] cReq = getCardRequirement(networkid, nodeid, d.getProtectionType(), "",
									d.getChannelProtection(), d.getClientProtection(), rlist.size(),
									routesAll.get(i).getLineRate(), routesAll.get(i).getDemandId(),d.getClientProtectionType());
							mpntype = cReq[0][0].toString().trim();
							SrNo = cReq[0][1].toString();

							if (cReq[2][0] != null)
								pairedStatus = cReq[2][0].toString();
							else
								pairedStatus = ResourcePlanConstants.No;

							mpnEid = rpu.fgetEIdFromCardType(mpntype);

							logger.info("Mpn/Tpn eid from card type:" + mpnEid);

							Object[] Pinfo = new Object[2];
							EquipmentPreferences eqp = new EquipmentPreferences(dbService);

							// getting card preference for redundant card
							if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
								if (mpntype.equals(ResourcePlanConstants.CardMuxponderCGM)
										|| mpntype.equals(ResourcePlanConstants.CardMuxponderCGX)) {
									Pinfo = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatMpn100G,
											ResourcePlanConstants.ParamDemandPtc, "" + routesAll.get(i).getDemandId());
									mpntypeProt = Pinfo[0].toString().trim();
									SrNoProt = Pinfo[1].toString();
									mpnEidProt = rpu.fgetEIdFromCardType(mpntypeProt);
								} else if (mpntype.equals(ResourcePlanConstants.CardMuxponderOPXCGX)
										|| mpntype.equals(ResourcePlanConstants.CardMuxponderOPX)) {
									Pinfo = eqp.fGetPreferredEqType(networkid, nodeid,
											ResourcePlanConstants.CatMpn100GOPX, ResourcePlanConstants.ParamDemandPtc,
											"" + routesAll.get(i).getDemandId());
									mpntypeProt = Pinfo[0].toString().trim();
									SrNoProt = Pinfo[1].toString();
									mpnEidProt = rpu.fgetEIdFromCardType(mpntypeProt);
								}
								// Same as working card
								else {
									mpntypeProt = cReq[1][0].toString();
									SrNoProt = cReq[1][1].toString();
									mpnEidProt = rpu.fgetEIdFromCardType(mpntypeProt);
								}

								// mpntypeProt=cReq[1][0].toString();
								// SrNoProt= cReq[1][1].toString();
								// mpnEidProt= rpu.fgetEIdFromCardType(mpntypeProt);
							} else {
								mpntypeProt = mpntype;
								SrNoProt = SrNo;
								mpnEidProt = mpnEid;
							}

							SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
							logger.info("Nodeid: " + nodeid + " DemandId: " + d.getDemandId() + " Paired Status: "
									+ pairedStatus);

							// get the preferred mpn type
							// Object mpn[]=rpu.fgetMpnPreference(networkid,nodeid);
							// String mpntype=mpn[0].toString();

							String linerate = routesAll.get(i).getLineRate();
							if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
								String dems = "";
								logger.info("ResourcePlanning.fAssignPairedMuxponderCards()" + " Node id " + nodeid
										+ " route with 10G Line found " + " Demand Id : "
										+ routesAll.get(i).getDemandId() + " MpnType :" + mpntype);
								Map<String, Object> Tpn10gDemands = dbService.getDemandService()
										.FindDemandsFor10GTpn(networkid, nodeid);
								if (Tpn10gDemands.get("Demands") != null) {
									logger.info("ResourcePlanning.fAssignPairedMuxponderCards() " + " Node id " + nodeid
											+ " 10g TPN Demands are " + Tpn10gDemands.get("Demands").toString());
									dems = "," + Tpn10gDemands.get("Demands").toString() + ",";
								}

								if (dems.contains("," + routesAll.get(i).getDemandId() + ",")) {

									if (mpntype == ResourcePlanConstants.CardMuxponder10G) {
										mpnEid = rpu.fgetEIdFromCardType(mpntype);
										SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);

										String[] location = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype,
												nodedegree, SelectedMpnSize, pairedStatus);
										logger.info(tag + "Location Received for Tpn 10G :" + location[0]);
										/// logger.info(tag+"Location Received2 :"+location[1]);
										if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
											int WaveW = routesAll.get(i).getWavelengthNo();
											if (location[0] != null) {
												// assign the free mpn slot
												int[] id = rpu.locationIds(location[0]);

												// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],mpntype);
												fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW,
														WaveW, d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
														ResourcePlanConstants.ZERO, SrNo);
												// fRemoveCard(nodepool,id[0],id[1],id[2],mpntype);

												fAssign5x10GTPNDemand(networkid, nodeid, routesAll.get(i).getDemandId(),
														id[0], id[1], id[2], DirW, mpntype);

											}
										}
									} else {

										logger.info(" 10G TPN to be allocated ");
										// needed a 10G TPN for this route
										// check if this can be added on to existing TPN
										PortInfo port = dbService.getPortInfoService().FindMax5x10GPort(networkid,
												nodeid);
										if (port != null) {
											if (port.getPort() < 5) {
												// port can be added to the same TPN
												logger.info(
														"ResourcePlanning.fAssignPairedMuxponderCards() Inserting on existing 5x10G TPN ");
												fAssign5x10GTPNDemand(networkid, nodeid, routesAll.get(i).getDemandId(),
														port.getRack(), port.getSbrack(), port.getCard(), DirW,
														mpntype);
											} else {
												logger.info(
														"ResourcePlanning.fAssignPairedMuxponderCards() Inserting new 5x10G TPN ");
												// insert a new TPN
												mpntype = ResourcePlanConstants.CardTPN5x10G;
												mpnEid = rpu.fgetEIdFromCardType(mpntype);
												SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
												logger.info("ResourcePlanning.fAssignPairedMuxponderCards()"
														+ " Node id " + nodeid + " route with 10G TPN found ");

												String[] location = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid,
														nodetype, nodedegree, SelectedMpnSize, pairedStatus);
												logger.info(tag + "Location Received1 :" + location[0]);
												/// logger.info(tag+"Location Received2 :"+location[1]);
												if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
													int WaveW = routesAll.get(i).getWavelengthNo();
													if (location[0] != null) {
														// assign the free mpn slot
														int[] id = rpu.locationIds(location[0]);

														// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],mpntype);
														fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype,
																DirW, WaveW, d.getDemandId(), mpnEid,
																ResourcePlanConstants.Working,
																ResourcePlanConstants.ZERO, SrNo);
														// fRemoveCard(nodepool,id[0],id[1],id[2],mpntype);

														fAssign5x10GTPNDemand(networkid, nodeid,
																routesAll.get(i).getDemandId(), id[0], id[1], id[2],
																DirW, mpntype);
													}
												}

											}
										} else {
											// insert a new TPN
											logger.info(
													"ResourcePlanning.fAssignPairedMuxponderCards() Inserting 5x10G TPN ");
											mpntype = ResourcePlanConstants.CardTPN5x10G;
											mpnEid = rpu.fgetEIdFromCardType(mpntype);
											SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
											logger.info("ResourcePlanning.fAssignPairedMuxponderCards()" + " Node id "
													+ nodeid + " route with 10G TPN found ");

											String[] location = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype,
													nodedegree, SelectedMpnSize, pairedStatus);
											logger.info(tag + "Location Received1 :" + location[0]);
											/// logger.info(tag+"Location Received2 :"+location[1]);
											if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
												int WaveW = routesAll.get(i).getWavelengthNo();
												if (location[0] != null) {
													// assign the free mpn slot
													int[] id = rpu.locationIds(location[0]);

													// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],mpntype);
													fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW,
															WaveW, d.getDemandId(), mpnEid,
															ResourcePlanConstants.Working, ResourcePlanConstants.ZERO,
															SrNo);
													// fRemoveCard(nodepool,id[0],id[1],id[2],mpntype);

													fAssign5x10GTPNDemand(networkid, nodeid,
															routesAll.get(i).getDemandId(), id[0], id[1], id[2], DirW,
															mpntype);
												}
											}
										}
									}

								} else {
									logger.info(" 10G MPN to be allocated ");
									mpntype = ResourcePlanConstants.CardMuxponder10G;
									mpnEid = rpu.fgetEIdFromCardType(mpntype);
									SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
									String[] location = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype,
											nodedegree, SelectedMpnSize, pairedStatus);
									logger.info(tag + "Location Received1 :" + location[0]);
									/// logger.info(tag+"Location Received2 :"+location[1]);
									if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))
											| (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
										int WaveW = routesAll.get(i).getWavelengthNo();

										if (location[0] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[0]);

											// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],mpntype);
											fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW, WaveW,
													d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
													ResourcePlanConstants.ZERO, SrNo);
											// fRemoveCard(nodepool,id[0],id[1],id[2],mpntype);
										}
									}

									if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
										int WaveP;
										NetworkRoute r = dbService.getNetworkRouteService().FindAllByDemandId(networkid,
												d.getDemandId(), ResourcePlanConstants.TWO);
										if (r != null) {
											pnodeids = r.getPath().split(",");
											if (currentnodeid == Integer.parseInt(pnodeids[0].toString()))
												DirP = dbService.getLinkService().FindLinkDirection(networkid,
														Integer.parseInt(pnodeids[0].toString()),
														Integer.parseInt(pnodeids[0 + 1].toString()));
											else if (currentnodeid == Integer
													.parseInt(pnodeids[pnodeids.length - 1].toString()))
												DirP = dbService.getLinkService().FindLinkDirection(networkid,
														Integer.parseInt(pnodeids[pnodeids.length - 1].toString()),
														Integer.parseInt(pnodeids[pnodeids.length - 1 - 1].toString()));

											WaveP = r.getWavelengthNo();
											if (location[1] != null) {
												// assign the free mpn slot
												int[] id = rpu.locationIds(location[1]);
												// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],mpntype);
												fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntypeProt, DirP,
														WaveP, d.getDemandId(), mpnEidProt,
														ResourcePlanConstants.Protection, ResourcePlanConstants.ZERO,
														SrNoProt);
												// fRemoveCard(nodepool,id[0],id[1],id[2],mpntype);

											}
										}
									}
								}
							} else if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line200)) {
								mpntype = ResourcePlanConstants.CardMuxponder200G;
								mpnEid = rpu.fgetEIdFromCardType(mpntype);
								SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);

								String[] location = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype, nodedegree,
										SelectedMpnSize, pairedStatus);
								logger.info(tag + "Location Received1 :" + location[0]);
								logger.info(tag + "Location Received2 :" + location[1]);
								if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))
										| (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
									if (location[0] != null) {
										int WaveW = routesAll.get(i).getWavelengthNo();

										if (location[0] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[0]);

											// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],mpntype);
											fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW, WaveW,
													d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
													ResourcePlanConstants.ZERO, SrNo);
											// fRemoveCard(nodepool,id[0],id[1],id[2],mpntype);
										}
									}
								}

								if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
									int WaveP;
									NetworkRoute r = dbService.getNetworkRouteService().FindAllByDemandId(networkid,
											d.getDemandId(), ResourcePlanConstants.TWO);
									if (r != null) {
										pnodeids = r.getPath().split(",");
										if (currentnodeid == Integer.parseInt(pnodeids[0].toString()))
											DirP = dbService.getLinkService().FindLinkDirection(networkid,
													Integer.parseInt(pnodeids[0].toString()),
													Integer.parseInt(pnodeids[0 + 1].toString()));
										else if (currentnodeid == Integer
												.parseInt(pnodeids[pnodeids.length - 1].toString()))
											DirP = dbService.getLinkService().FindLinkDirection(networkid,
													Integer.parseInt(pnodeids[pnodeids.length - 1].toString()),
													Integer.parseInt(pnodeids[pnodeids.length - 1 - 1].toString()));

										WaveP = r.getWavelengthNo();
										if (location[1] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[1]);
											// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],mpntype);
											fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntypeProt, DirP,
													WaveP, d.getDemandId(), mpnEidProt,
													ResourcePlanConstants.Protection, ResourcePlanConstants.ZERO,
													SrNoProt);
											// fRemoveCard(nodepool,id[0],id[1],id[2],mpntype);

										}
									}
								}
							} else if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line100)) {
								String dems = "";
								int mpnWavelength = routesAll.get(i).getWavelengthNo();
								String DirectionXGM=DirW;
								logger.info("ResourcePlanning.fAssignPairedMuxponderCards()" + " Node id " + nodeid
										+ " route with 100G Line found ");
								Map<String, Object> Tpn100gDemands = dbService.getDemandService()
										.FindDemandsFor100GTpn(networkid, nodeid);
								if (Tpn100gDemands.get("Demands") != null) {
									logger.info("ResourcePlanning.fAssignPairedMuxponderCards() " + " Node id " + nodeid
											+ " 100g TPN Demands are " + Tpn100gDemands.get("Demands").toString());
									dems = "," + Tpn100gDemands.get("Demands").toString() + ",";
								}

								if (dems.contains("," + routesAll.get(i).getDemandId() + ",")) {
									logger.info(" 100G TPN to be allocated ");
									// mpntype=ResourcePlanConstants.CardTPN100G;

									// TPN CGC in place of TPN100G:28/2/18
									mpntype = ResourcePlanConstants.CardTPN100GCGC;

									mpnEid = rpu.fgetEIdFromCardType(mpntype);
								} else {
									logger.info(" 100G MPN to be allocated ");
									// get the preferred mpn type
									// mpn=rpu.fgetMpnPreference(networkid,nodeid);
									// mpntype=mpn[0].toString();
									// mpnEid= Integer.parseInt(mpn[1].toString());
								}

								SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
								String[] location = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype, nodedegree,
										SelectedMpnSize, pairedStatus);
								logger.info(tag + "Location Received1 :" + location[0]);
								/// logger.info(tag+"Location Received2 :"+location[1]);
								if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))
										|| (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {

									int WaveW = routesAll.get(i).getWavelengthNo();

									if (location[0] != null) {
										// assign the free mpn slot
										int[] id = rpu.locationIds(location[0]);

										// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],mpntype);
										fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntype, DirW, WaveW,
												d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
												ResourcePlanConstants.ZERO, SrNo);
										// fRemoveCard(nodepool,id[0],id[1],id[2],mpntype);
									}
								}

								if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
									int WaveP;
									NetworkRoute r = dbService.getNetworkRouteService().FindAllByDemandId(networkid,
											d.getDemandId(), ResourcePlanConstants.TWO);
									String[] PtcLinkIdSet=r.getPathLinkId().split(",");
									if (r != null) {
										pnodeids = r.getPath().split(",");
										if (currentnodeid == Integer.parseInt(pnodeids[0].toString()))
										{
											Link link=dbService.getLinkService().FindLink(networkid, Integer.parseInt(PtcLinkIdSet[0]));

											if(link.getSrcNode()==currentnodeid)
												DirP=link.getSrcNodeDirection();
											else DirP=link.getDestNodeDirection();

											//											DirP = dbService.getLinkService().FindLinkDirection(networkid,
											//													Integer.parseInt(pnodeids[0].toString()),
											//													Integer.parseInt(pnodeids[0 + 1].toString()));			
											System.out.println("Ptc Path :"+ r.getPath()+" NodeId :"+currentnodeid+" LinkId :"+Integer.parseInt(PtcLinkIdSet[j])+" DirP :"+DirP);
										}

										else if (currentnodeid == Integer
												.parseInt(pnodeids[pnodeids.length - 1].toString())) {
											Link link=dbService.getLinkService().FindLink(networkid, Integer.parseInt(PtcLinkIdSet[PtcLinkIdSet.length-1]));

											if(link.getSrcNode()==currentnodeid)
												DirP=link.getSrcNodeDirection();
											else DirP=link.getDestNodeDirection();

											//											DirP = dbService.getLinkService().FindLinkDirection(networkid,
											//													Integer.parseInt(pnodeids[pnodeids.length - 1].toString()),
											//													Integer.parseInt(pnodeids[pnodeids.length - 1 - 1].toString()));
											System.out.println("Ptc Path :"+ r.getPath()+" NodeId :"+currentnodeid+" LinkId :"+Integer.parseInt(PtcLinkIdSet[PtcLinkIdSet.length-1])+" DirP :"+DirP);
										}


										WaveP = r.getWavelengthNo();
										if (location[1] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[1]);
											// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],mpntype);
											fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], mpntypeProt, DirP,
													WaveP, d.getDemandId(), mpnEidProt,
													ResourcePlanConstants.Protection, ResourcePlanConstants.ZERO,
													SrNoProt);
											// fRemoveCard(nodepool,id[0],id[1],id[2],mpntype);

										}
									}
								}

								if (!dems.contains("," + routesAll.get(i).getDemandId() + ",")) {
									System.out.println("100G Demand");	
									List<DemandMapping> TenGCircuitsForNode=dbService.getCircuitService().FindAllTenGAggCircuits(networkid, routesAll.get(i).getDemandId());
									//								Demand TenGAggDemand=dbService.getDemandService().FindDemand(networkid, routesAll.get(i).getDemandId());
									System.out.println("Curr Node::"+currentnodeid+" DemandId ::"+routesAll.get(i).getDemandId()+"  Circuits count 10G:"+TenGCircuitsForNode.size());
									TenGCircuitsForNode.forEach(mapping->{
										String[] locationXGM = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype, nodedegree,
												ResourcePlanConstants.ONE, ResourcePlanConstants.No);
										int[] locationIds = rpu.locationIds(locationXGM[0]);									
										fSaveCardInDb(networkid, nodeid, locationIds[0], locationIds[1], locationIds[2], ResourcePlanConstants.CardMuxponder10G, DirectionXGM,
												mpnWavelength, mapping.getDemandId(), rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMuxponder10G),
												ResourcePlanConstants.Aggregator, mapping.getCircuitId(),
												"");
									});
								}

							}

						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("fAssignPairedMuxponderCards" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void fAssignPairedMuxponderCardsBrField(int networkid) throws SQLException {
		try {
			logger.info(tag + "fAssignPairedMuxponderCardsBrField");
			String mpntypeProt, mpntype, SrNo, SrNoProt, pairedStatus;
			int mpnEidProt, mpnEid;
			// get the brown field network id
			int networkidBF = Integer
					.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
			List<NetworkRoute> routesAdded = dbService.getNetworkRouteService().FindAddedRouteInBrField(networkid,
					networkidBF);

			// List<NetworkRoute> routesAll =
			// dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
			List<String> paths = new ArrayList<String>();
			String[] nodeids, pnodeids;

			/**
			 * delete muxponders for the deleted routes in brown field
			 */

			List<NetworkRoute> routesDeleted = dbService.getNetworkRouteService().FindDeletedRouteInBrField(networkid,
					networkidBF);
			for (int i = 0; i < routesDeleted.size(); i++) {
				if ((routesDeleted.get(i).getRoutePriority() == ResourcePlanConstants.ONE)) {
					nodeids = routesDeleted.get(i).getPath().split(",");
					for (int j = 0; j < nodeids.length; j++) {
						if ((j == 0) | (j == nodeids.length - 1)) {
							int nodeid = Integer.parseInt(nodeids[j].toString());
							if (dbService.getNodeService().FindNode(networkidBF, nodeid) != null)// node still exist in
								// brown field
							{
								Demand d = dbService.getDemandService().FindDemand(networkid,
										routesDeleted.get(i).getDemandId());
								if (!rpu.Is10GTpnDemand(d)) {
									CardInfo mpnW = dbService.getCardInfoService().FindMpn(networkidBF, nodeid,
											routesDeleted.get(i).getDemandId(), ResourcePlanConstants.Working);
									if (mpnW != null) {
										dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, mpnW.getRack(),
												mpnW.getSbrack(), mpnW.getCard());
									}

									CardInfo mpnP = dbService.getCardInfoService().FindMpn(networkidBF, nodeid,
											routesDeleted.get(i).getDemandId(), ResourcePlanConstants.Protection);
									if (mpnP != null) {
										dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, mpnP.getRack(),
												mpnP.getSbrack(), mpnP.getCard());
									}
								} else {
									// 10G demands handling
									List<PortInfo> ports = dbService.getPortInfoService().FindPortInfo(networkidBF,
											nodeid, d.getDemandId());
									if (ports.size() == 1) {
										// this is the last port , delete the port as well as TPN
										dbService.getPortInfoService().DeletePort(networkidBF, nodeid, d.getDemandId());
										dbService.getCardInfoService().DeleteCard(networkidBF, nodeid,
												ports.get(0).getRack(), ports.get(0).getSbrack(),
												ports.get(0).getCard());
									} else {
										// delete only the TPN port
										dbService.getPortInfoService().DeletePort(networkidBF, nodeid, d.getDemandId());
									}
								}
							}
						}
					}
				}
			}

			/**
			 * Add muxponders for the added routes in the brown field
			 */
			for (int i = 0; i < routesAdded.size(); i++) {
				if ((routesAdded.get(i).getRoutePriority() == ResourcePlanConstants.ONE)) {
					nodeids = routesAdded.get(i).getPath().split(",");
					for (int j = 0; j < nodeids.length; j++) {
						/*
						 * allocate the cards only on the first and the last i.e. src and the
						 * destination sites only
						 */
						if ((j == 0) | (j == nodeids.length - 1)) {
							int currentnodeid = Integer.parseInt(nodeids[j].toString());
							/// logger.info("card allocation node id "+
							/// Integer.parseInt(nodeids[j].toString()));
							// fAssigningCardsInNodes(networkid,Integer.parseInt(nodeids[j].toString()));
							int nodeid = Integer.parseInt(nodeids[j].toString());
							int nodetype = dbService.getNodeService()
									.FindNode(networkidBF, Integer.parseInt(nodeids[j].toString())).getNodeType();
							int nodedegree = dbService.getNodeService()
									.FindNode(networkidBF, Integer.parseInt(nodeids[j].toString())).getDegree();
							String DirW = "", DirP = "";
							if (j == 0)
								DirW = dbService.getLinkService().FindLinkDirection(networkidBF,
										Integer.parseInt(nodeids[j].toString()),
										Integer.parseInt(nodeids[j + 1].toString()));
							else if (j == nodeids.length - 1)
								DirW = dbService.getLinkService().FindLinkDirection(networkidBF,
										Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));

							Demand d = dbService.getDemandService().FindDemand(networkidBF,
									routesAdded.get(i).getDemandId());
							List<NetworkRoute> rlist = dbService.getNetworkRouteService()
									.FindAllByDemandIdUsable(networkidBF, routesAdded.get(i).getDemandId());

							// add checks for the subrack protection and channel protection also
							Object[][] cReq = getCardRequirement(networkidBF, nodeid, d.getProtectionType(), "",
									d.getChannelProtection(), d.getClientProtection(), rlist.size(),
									routesAdded.get(i).getLineRate(), routesAdded.get(i).getDemandId(),d.getClientProtectionType());

							//							String mpntype = cReq[0][0].toString();
							//							String pairedStatus = cReq[0][1].toString();

							mpntype = cReq[0][0].toString().trim();
							SrNo = cReq[0][1].toString();

							if (cReq[2][0] != null)
								pairedStatus = cReq[2][0].toString();
							else
								pairedStatus = ResourcePlanConstants.No;

							mpnEid = rpu.fgetEIdFromCardType(mpntype);
							SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
							logger.info("Nodeid: " + nodeid + " DemandId: " + d.getDemandId() + " Paired Status: "
									+ pairedStatus);

							Object[] Pinfo = new Object[2];
							EquipmentPreferences eqp = new EquipmentPreferences(dbService);

							// getting card preference for redundant card
							if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
								if (mpntype.equals(ResourcePlanConstants.CardMuxponderCGM)
										|| mpntype.equals(ResourcePlanConstants.CardMuxponderCGX)) {
									Pinfo = eqp.fGetPreferredEqType(networkidBF, nodeid, ResourcePlanConstants.CatMpn100G,
											ResourcePlanConstants.ParamDemandPtc, "" + routesAdded.get(i).getDemandId());
									mpntypeProt = Pinfo[0].toString().trim();
									SrNoProt = Pinfo[1].toString();
									mpnEidProt = rpu.fgetEIdFromCardType(mpntypeProt);
								} else if (mpntype.equals(ResourcePlanConstants.CardMuxponderOPXCGX)
										|| mpntype.equals(ResourcePlanConstants.CardMuxponderOPX)) {
									Pinfo = eqp.fGetPreferredEqType(networkidBF, nodeid,
											ResourcePlanConstants.CatMpn100GOPX, ResourcePlanConstants.ParamDemandPtc,
											"" + routesAdded.get(i).getDemandId());
									mpntypeProt = Pinfo[0].toString().trim();
									SrNoProt = Pinfo[1].toString();
									mpnEidProt = rpu.fgetEIdFromCardType(mpntypeProt);
								}
								// Same as working card
								else {
									mpntypeProt = cReq[1][0].toString();
									SrNoProt = cReq[1][1].toString();
									mpnEidProt = rpu.fgetEIdFromCardType(mpntypeProt);
								}

								// mpntypeProt=cReq[1][0].toString();
								// SrNoProt= cReq[1][1].toString();
								// mpnEidProt= rpu.fgetEIdFromCardType(mpntypeProt);
							} else {
								mpntypeProt = mpntype;
								SrNoProt = SrNo;
								mpnEidProt = mpnEid;
							}

							// get the preferred mpn type
							// Object mpn[]=rpu.fgetMpnPreference(networkid,nodeid);// preferences are
							// temporarily taken same from green field, change to networkidBf when
							// preferences are available in brown field network
							// String mpntype=mpn[0].toString();
							// int mpnEid= Integer.parseInt(mpn[1].toString());

							if (routesAdded.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
								String dems = "";
								logger.info("fAssignPairedMuxponderCardsBrField()" + " Node id " + nodeid
										+ " route with 10G Line found " + " Demand Id : "
										+ routesAdded.get(i).getDemandId());
								Map<String, Object> Tpn10gDemands = dbService.getDemandService()
										.FindDemandsFor10GTpn(networkidBF, nodeid);
								if (Tpn10gDemands.get("Demands") != null) {
									logger.info("fAssignPairedMuxponderCardsBrField() " + " Node id " + nodeid
											+ " 10g TPN Demands are " + Tpn10gDemands.get("Demands").toString());
									dems = "," + Tpn10gDemands.get("Demands").toString() + ",";
								}

								if (dems.contains("," + routesAdded.get(i).getDemandId() + ",")) {
									logger.info(" 10G TPN to be allocated ");
									// needed a 10G TPN for this route
									// check if this can be added on to existing TPN

									if (mpntype == ResourcePlanConstants.CardMuxponder10G) {
										mpnEid = rpu.fgetEIdFromCardType(mpntype);
										SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);

										String[] location = rpu.fGetFirstFreeMpnSlotDb(networkidBF, nodeid, nodetype,
												nodedegree, SelectedMpnSize, pairedStatus);
										logger.info(tag + "Location Received for Tpn 10G :" + location[0]);
										/// logger.info(tag+"Location Received2 :"+location[1]);
										if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
											int WaveW = routesAdded.get(i).getWavelengthNo();
											if (location[0] != null) {
												// assign the free mpn slot
												int[] id = rpu.locationIds(location[0]);

												fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], mpntype, DirW,
														WaveW, d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
														ResourcePlanConstants.ZERO, SrNo);


												fAssign5x10GTPNDemand(networkidBF, nodeid, routesAdded.get(i).getDemandId(),
														id[0], id[1], id[2], DirW, mpntype);

											}
										}
									} else {
										PortInfo port = dbService.getPortInfoService().FindMax5x10GPort(networkidBF,
												nodeid);
										if (port != null) {
											if (port.getPort() < 5) {
												// port can be added to the same TPN
												logger.info(
														"fAssignPairedMuxponderCardsBrField() Inserting on existing 5x10G TPN ");
												fAssign5x10GTPNDemand(networkidBF, nodeid, routesAdded.get(i).getDemandId(),
														port.getRack(), port.getSbrack(), port.getCard(), DirW, mpntype);
											} else {
												logger.info(
														"fAssignPairedMuxponderCardsBrField() Inserting new 5x10G TPN ");
												// insert a new TPN
												mpntype = ResourcePlanConstants.CardTPN5x10G;
												mpnEid = rpu.fgetEIdFromCardType(mpntype);
												SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
												logger.info("fAssignPairedMuxponderCardsBrField()" + " Node id " + nodeid
														+ " route with 10G TPN found ");

												String[] location = rpu.fGetFirstFreeMpnSlotDb(networkidBF, nodeid,
														nodetype, nodedegree, SelectedMpnSize, pairedStatus);
												logger.info(tag + "Location Received1 :" + location[0]);
												/// logger.info(tag+"Location Received2 :"+location[1]);
												if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
													int WaveW = routesAdded.get(i).getWavelengthNo();
													if (location[0] != null) {
														// assign the free mpn slot
														int[] id = rpu.locationIds(location[0]);

														fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], mpntype,
																DirW, WaveW, d.getDemandId(), mpnEid,
																ResourcePlanConstants.Working, ResourcePlanConstants.ZERO,
																"");

														fAssign5x10GTPNDemand(networkidBF, nodeid,
																routesAdded.get(i).getDemandId(), id[0], id[1], id[2], DirW,
																mpntype);
													}
												}

											}
										} else {
											// insert a new TPN
											logger.info("fAssignPairedMuxponderCardsBrField Inserting 5x10G TPN ");
											mpntype = ResourcePlanConstants.CardTPN5x10G;
											mpnEid = rpu.fgetEIdFromCardType(mpntype);
											SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
											logger.info("fAssignPairedMuxponderCardsBrField()" + " Node id " + nodeid
													+ " route with 10G TPN found ");

											String[] location = rpu.fGetFirstFreeMpnSlotDb(networkidBF, nodeid, nodetype,
													nodedegree, SelectedMpnSize, pairedStatus);
											logger.info(tag + "Location Received1 :" + location[0]);
											/// logger.info(tag+"Location Received2 :"+location[1]);
											if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
												int WaveW = routesAdded.get(i).getWavelengthNo();
												if (location[0] != null) {
													// assign the free mpn slot
													int[] id = rpu.locationIds(location[0]);
													fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], mpntype, DirW,
															WaveW, d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
															ResourcePlanConstants.ZERO, "");
													fAssign5x10GTPNDemand(networkidBF, nodeid,
															routesAdded.get(i).getDemandId(), id[0], id[1], id[2], DirW,
															mpntype);
												}
											}
										}
									}
								}else {
									logger.info(" 10G MPN to be allocated ");
									mpntype = ResourcePlanConstants.CardMuxponder10G;
									mpnEid = rpu.fgetEIdFromCardType(mpntype);
									SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
									String[] location = rpu.fGetFirstFreeMpnSlotDb(networkidBF, nodeid, nodetype,
											nodedegree, SelectedMpnSize, pairedStatus);
									logger.info(tag + "Location Received1 :" + location[0]);
									/// logger.info(tag+"Location Received2 :"+location[1]);
									if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))
											| (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
										int WaveW = routesAdded.get(i).getWavelengthNo();

										if (location[0] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[0]);
											fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], mpntype, DirW,
													WaveW, d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
													ResourcePlanConstants.ZERO, "");
										}
									}

									if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
										int WaveP;
										NetworkRoute r = dbService.getNetworkRouteService().FindAllByDemandId(
												networkidBF, d.getDemandId(), ResourcePlanConstants.TWO);
										if (r != null) {
											pnodeids = r.getPath().split(",");
											if (currentnodeid == Integer.parseInt(pnodeids[0].toString()))
												DirP = dbService.getLinkService().FindLinkDirection(networkidBF,
														Integer.parseInt(pnodeids[0].toString()),
														Integer.parseInt(pnodeids[0 + 1].toString()));
											else if (currentnodeid == Integer
													.parseInt(pnodeids[pnodeids.length - 1].toString()))
												DirP = dbService.getLinkService().FindLinkDirection(networkidBF,
														Integer.parseInt(pnodeids[pnodeids.length - 1].toString()),
														Integer.parseInt(pnodeids[pnodeids.length - 1 - 1].toString()));

											WaveP = r.getWavelengthNo();
											if (location[1] != null) {
												// assign the free mpn slot
												int[] id = rpu.locationIds(location[1]);
												fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], mpntype, DirP,
														WaveP, d.getDemandId(), mpnEid,
														ResourcePlanConstants.Protection, ResourcePlanConstants.ZERO,
														"");
											}
										}
									}
								}
							} else {
								String dems = "";
								String DirectionXGM=DirW;
								int mpnWavelengthW=routesAdded.get(i).getWavelengthNo();
								if (routesAdded.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line200)) {
									mpntype = ResourcePlanConstants.CardMuxponder200G;
									mpnEid = rpu.fgetEIdFromCardType(mpntype);
									SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
								} else if (routesAdded.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line100)) {

									logger.info("fAssignPairedMuxponderCardsBrField()" + " Node id " + nodeid
											+ " route with 100G Line found ");
									Map<String, Object> Tpn100gDemands = dbService.getDemandService()
											.FindDemandsFor100GTpn(networkidBF, nodeid);
									if (Tpn100gDemands.get("Demands") != null) {
										logger.info("fAssignPairedMuxponderCardsBrField() " + " Node id " + nodeid
												+ " 100g TPN Demands are " + Tpn100gDemands.get("Demands").toString());
										dems = "," + Tpn100gDemands.get("Demands").toString() + ",";
									}

									if (dems.contains("," + routesAdded.get(i).getDemandId() + ",")) {
										logger.info(" 100G TPN to be allocated ");
										// mpntype=ResourcePlanConstants.CardTPN100G;

										// TPN CGC in place of TPN100G:28/2/18
										mpntype = ResourcePlanConstants.CardTPN100GCGC;

										mpnEid = rpu.fgetEIdFromCardType(mpntype);
									} else {
										logger.info(" 100G MPN to be allocated ");
										// get the preferred mpn type
										// mpn=rpu.fgetMpnPreference(networkid,nodeid);
										// mpntype=mpn[0].toString();
										// mpnEid= Integer.parseInt(mpn[1].toString());
									}

									SelectedMpnSize = dbService.getEquipmentService().FindSlotSize(mpnEid);
								}

								String[] location = rpu.fGetFirstFreeMpnSlotDb(networkidBF, nodeid, nodetype,
										nodedegree, SelectedMpnSize, pairedStatus);
								logger.info(tag + "Location Received1 :" + location[0]);
								logger.info(tag + "Location Received2 :" + location[1]);
								if ((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))
										| (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No))) {
									int WaveW = routesAdded.get(i).getWavelengthNo();

									if (location[0] != null) {
										// assign the free mpn slot
										int[] id = rpu.locationIds(location[0]);
										fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], mpntype, DirW, WaveW,
												d.getDemandId(), mpnEid, ResourcePlanConstants.Working,
												ResourcePlanConstants.ZERO, "");
									}
								}

								if (pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
									int WaveP;
									NetworkRoute r = dbService.getNetworkRouteService().FindAllByDemandId(networkidBF,
											d.getDemandId(), ResourcePlanConstants.TWO);
									if (r != null) {
										pnodeids = r.getPath().split(",");
										if (currentnodeid == Integer.parseInt(pnodeids[0].toString()))
											DirP = dbService.getLinkService().FindLinkDirection(networkidBF,
													Integer.parseInt(pnodeids[0].toString()),
													Integer.parseInt(pnodeids[0 + 1].toString()));
										else if (currentnodeid == Integer
												.parseInt(pnodeids[pnodeids.length - 1].toString()))
											DirP = dbService.getLinkService().FindLinkDirection(networkidBF,
													Integer.parseInt(pnodeids[pnodeids.length - 1].toString()),
													Integer.parseInt(pnodeids[pnodeids.length - 1 - 1].toString()));

										WaveP = r.getWavelengthNo();
										if (location[1] != null) {
											// assign the free mpn slot
											int[] id = rpu.locationIds(location[1]);
											fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], mpntype, DirP,
													WaveP, d.getDemandId(), mpnEid, ResourcePlanConstants.Protection,
													ResourcePlanConstants.ZERO, "");
										}
									}
								}

								if (!dems.contains("," + routesAdded.get(i).getDemandId() + ",")) {
									System.out.println("100G Demand Aggregartor Clients");	
									List<DemandMapping> TenGCircuitsForNode=dbService.getCircuitService().FindAllTenGAggCircuitsAddedBrField(networkid, networkidBF,routesAdded.get(i).getDemandId());
									//Demand TenGAggDemand=dbService.getDemandService().FindDemand(networkid, routesAll.get(i).getDemandId());
									System.out.println("Curr Node::"+currentnodeid+" DemandId ::"+routesAdded.get(i).getDemandId()+"  Circuits count 10G:"+TenGCircuitsForNode.size());
									int demandId=routesAdded.get(i).getDemandId();
									TenGCircuitsForNode.forEach(mapping->{
										String[] locationXGM = rpu.fGetFirstFreeMpnSlotDb(networkidBF, nodeid, nodetype, nodedegree,
												ResourcePlanConstants.ONE, ResourcePlanConstants.No);
										int[] locationIds = rpu.locationIds(locationXGM[0]);									
										fSaveCardInDb(networkidBF, nodeid, locationIds[0], locationIds[1], locationIds[2], ResourcePlanConstants.CardMuxponder10G, DirectionXGM,
												mpnWavelengthW, mapping.getDemandId(), rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMuxponder10G),
												ResourcePlanConstants.Aggregator, mapping.getCircuitId(),
												"");
									});
								}


							}
						}
					}
				}
			}

			/**
			 * Get Routes with modified Paths
			 */

			// List<NetworkRoute> routesModified =
			// dbService.getNetworkRouteService().FindPathModifiedRouteInBrField(networkid,
			// networkidBF);
			// for (int i = 0; i < routesModified.size(); i++)
			// {
			// if((routesModified.get(i).getRoutePriority()==ResourcePlanConstants.ONE))
			// {
			// nodeids=routesModified.get(i).getPath().split(",");
			// for (int j = 0; j < nodeids.length; j++)
			// {
			// if((j==0)|(j==nodeids.length-1))
			// {
			// int nodeid=Integer.parseInt(nodeids[j].toString());
			// int currentnodeid = nodeid;
			// int nodetype=dbService.getNodeService().FindNode(networkidBF,
			// Integer.parseInt(nodeids[j].toString())).getNodeType();
			// int nodedegree=dbService.getNodeService().FindNode(networkidBF,
			// Integer.parseInt(nodeids[j].toString())).getDegree();
			// String DirW = "",DirP="";
			// if(j==0)
			// DirW =
			// dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(nodeids[j].toString()),Integer.parseInt(nodeids[j+1].toString()));
			// else if(j==nodeids.length-1)
			// DirW =
			// dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(nodeids[nodeids.length-1].toString()),Integer.parseInt(nodeids[nodeids.length-1-1].toString()));
			//
			// Demand dBf = dbService.getDemandService().FindDemand(networkidBF,
			// routesModified.get(i).getDemandId());
			// List <NetworkRoute> rlist=
			// dbService.getNetworkRouteService().FindAllByDemandIdUsable(networkidBF,
			// routesModified.get(i).getDemandId());
			//
			//
			// Object[][] cReq=getCardRequirement(networkid,
			// nodeid,dBf.getProtectionType(),"",dBf.getChannelProtection(),rlist.size(),routesModified.get(i).getLineRate(),
			// routesModified.get(i).getDemandId());
			// String mpntype=cReq[0][0].toString();
			// String pairedStatus=cReq[0][1].toString();
			// int mpnEid= rpu.fgetEIdFromCardType(mpntype);
			// SelectedMpnSize=dbService.getEquipmentService().FindSlotSize(mpnEid);
			// logger.info("Nodeid: "+nodeid+" DemandId: "+dBf.getDemandId()+" Paired
			// Status: "+pairedStatus);
			//
			// //find the card type used earlier
			// CardInfo mpnW = dbService.getCardInfoService().FindMpn(networkidBF, nodeid,
			// routesModified.get(i).getDemandId(), ResourcePlanConstants.Working);
			//
			// if(mpntype==mpnW.getCardType())
			// {
			// //then MPN can be reused/retuned
			// mpnW.setDirection(DirW);
			// mpnW.setWavelength(routesModified.get(i).getWavelengthNo());
			// dbService.getCardInfoService().UpdateMpn(mpnW);
			// }
			// else
			// {
			// //MPN needs to be changed, delete the previous one and place a new
			// dbService.getCardInfoService().DeleteCard(networkidBF, nodeid,
			// mpnW.getRack(), mpnW.getSbrack(), mpnW.getCard());
			// CardInfo mpnP = dbService.getCardInfoService().FindMpn(networkidBF, nodeid,
			// routesModified.get(i).getDemandId(), ResourcePlanConstants.Protection);
			// if(mpnP!=null)
			// {
			// dbService.getCardInfoService().DeleteCard(networkidBF, nodeid,
			// mpnP.getRack(), mpnP.getSbrack(), mpnP.getCard());
			// }
			//
			// // now add a new MPN
			//
			// String[] location =
			// rpu.fGetFirstFreeMpnSlotDb(networkidBF,nodeid,nodetype,nodedegree,SelectedMpnSize,pairedStatus);
			// logger.info(tag+"Location Received1 :"+location[0]);
			// logger.info(tag+"Location Received2 :"+location[1]);
			// if((pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))|(pairedStatus.equalsIgnoreCase(ResourcePlanConstants.No)))
			// {
			// int WaveW = routesModified.get(i).getWavelengthNo();
			//
			// if(location[0]!=null)
			// {
			// //assign the free mpn slot
			// int[] id=rpu.locationIds(location[0]);
			// fSaveCardInDb(networkidBF, nodeid, id[0],id[1],id[2],mpntype,DirW,WaveW,
			// dBf.getDemandId(),mpnEid,ResourcePlanConstants.Working,ResourcePlanConstants.ZERO);
			// }
			// }
			//
			// if(pairedStatus.equalsIgnoreCase(ResourcePlanConstants.Yes))
			// {
			// int WaveP;
			// NetworkRoute r =
			// dbService.getNetworkRouteService().FindAllByDemandId(networkidBF,
			// dBf.getDemandId(), ResourcePlanConstants.TWO);
			// if(r!=null)
			// {
			// pnodeids=r.getPath().split(",");
			// if(currentnodeid==Integer.parseInt(pnodeids[0].toString()))
			// DirP =
			// dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(pnodeids[0].toString()),Integer.parseInt(pnodeids[0+1].toString()));
			// else
			// if(currentnodeid==Integer.parseInt(pnodeids[pnodeids.length-1].toString()))
			// DirP =
			// dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(pnodeids[pnodeids.length-1].toString()),Integer.parseInt(pnodeids[pnodeids.length-1-1].toString()));
			//
			// WaveP = r.getWavelengthNo();
			// if(location[1]!=null)
			// {
			// //assign the free mpn slot
			// int[] id=rpu.locationIds(location[1]);
			// fSaveCardInDb(networkidBF, nodeid,
			// id[0],id[1],id[2],mpntype,DirP,WaveP,dBf.getDemandId(),mpnEid,ResourcePlanConstants.Protection,ResourcePlanConstants.ZERO);
			// }
			// }
			// }
			// }
			// }
			// }
			// }
			//
			// }

		} catch (Exception e) {
			logger.error("fAssignPairedMuxponderCardsBrField" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @brief Assigns the muxponder cards in NE Stores the cards in db, checks the
	 *        preferred MPN type for a node and calls the appropriate functions
	 * @param networkid
	 */
	public void fAssignMuxponderCards(int networkid) {
		List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		String[] nodeids;
		for (int i = 0; i < routesAll.size(); i++) {
			if ((routesAll.get(i).getRoutePriority() == ResourcePlanConstants.ONE)) {
				nodeids = routesAll.get(i).getPath().split(",");
				for (int j = 0; j < nodeids.length; j++) {
					/*
					 * allocate the MPNs only on the first and the last i.e. src and the destination
					 * sites only
					 */
					if ((j == 0) | (j == nodeids.length - 1)) {
						int nodeid = Integer.parseInt(nodeids[j].toString());
						// get MPN card preference
						Object mpn[] = rpu.fgetMpnPreference(networkid, nodeid);
						String mpntype = mpn[0].toString();
						if (mpntype == ResourcePlanConstants.CardMuxponderCGM) {
							// fAssignMuxponderCGM(networkid,nodeid,netroute);
						} else if (mpntype == ResourcePlanConstants.CardMuxponderCGX) {
							// fAssignMuxponderCGX(networkid,nodeid,netroute);
						}
					}
				}
			}
		}
	}

	public void fAssignOLPs(int networkid) {
		logger.info(" fAssignOLPs");
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		String DirP = "";
		String[] pnodeids;
		for (int i = 0; i < nodelist.size(); i++) {
			int nodeid = nodelist.get(i);

			NodeSystem assignedSys;
			if (assignedmap.get(nodeid) != null) {
				/// logger.info("found in assigned map ()");
				assignedSys = assignedmap.get(nodeid);
			} else {
				/// logger.info("found not in assigned map ()");
				assignedSys = new NodeSystem(nodeid);
			}

			NodeSystem nodepool;
			nodepool = poolmap.get(nodeid);
			// int
			// cntOlpCircuits=dbService.getCircuitService().countCircuitsByClientProtPerNode(networkid,
			// nodeid,MapConstants.OLPProtection);
			List<Circuit> cList = dbService.getCircuitService().CircuitsByClientProtPerNode(networkid, nodeid,
					MapConstants.OLPProtection);
			int cntOlp = cList.size();

			logger.info(" NodeId: " + nodeid + "Required OLPs: " + cntOlp);
			for (int j = 0; j < cList.size(); j++) {
				/// logger.info(" NodeId: "+nodeid+"Required OLPs: "+cntOlp);

				// find direction for the OLP , get direction of the working TPN for the demand
				// of this circuit
				// CircuitDemandMapping cdm =
				// dbService.getCircuitDemandMappingService().FindDemand(networkid,
				// cList.get(j).getCircuitId());
				// CardInfo mpnWorking = dbService.getCardInfoService().FindMpn(networkid,
				// nodeid, cdm.getDemandId(), ResourcePlanConstants.Working);

				String location = fGetFirstFreeSlot(networkid, nodeid);
				logger.info(j + " fAssignOLPs() location found : " + location);
				if (((location.contains("_6")/* |(location.contains("_9")) */))) {
					j--;
					int[] id = rpu.locationIds(location);
					assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], ResourcePlanConstants.CardMpc);
					fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO, "");
					fRemoveCard(nodepool, id[0], id[1], id[2], ResourcePlanConstants.CardMpc);
				} else {
					int[] id = rpu.locationIds(location);
					assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], ResourcePlanConstants.CardOlp);
					fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, "", 0, 0,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "", cList.get(j).getCircuitId(),
							"");
					fRemoveCard(nodepool, id[0], id[1], id[2], ResourcePlanConstants.CardOlp);
				}
			}

			/**
			 * commented as OPX card will be used for the channel protection hence OLPs will
			 * not be required
			 */

			// logger.info(" fAssignOLPs for Channel Protection ");
			// List <Demand> demandList =
			// dbService.getDemandService().FindAllByNodeId(networkid, nodeid);
			// for (int j = 0; j < demandList.size(); j++)
			// {
			// if(demandList.get(j).getChannelProtection().equalsIgnoreCase(ResourcePlanConstants.Yes))
			// {
			// NetworkRoute nr =
			// dbService.getNetworkRouteService().FindAllByDemandId(networkid,
			// demandList.get(j).getDemandId(), ResourcePlanConstants.TWO);
			// if(nr!=null)
			// {
			// pnodeids=nr.getPath().split(",");
			// if(nodeid==Integer.parseInt(pnodeids[0].toString()))
			// DirP =
			// dbService.getLinkService().FindLinkDirection(networkid,Integer.parseInt(pnodeids[0].toString()),Integer.parseInt(pnodeids[0+1].toString()));
			// else if(nodeid==Integer.parseInt(pnodeids[pnodeids.length-1].toString()))
			// DirP =
			// dbService.getLinkService().FindLinkDirection(networkid,Integer.parseInt(pnodeids[pnodeids.length-1].toString()),Integer.parseInt(pnodeids[pnodeids.length-1-1].toString()));
			// }
			//
			// //find direction for the OLP , get direction of the working TPN for that
			// demand
			//
			//// CardInfo mpnWorking = dbService.getCardInfoService().FindMpn(networkid,
			// nodeid, demandList.get(j).getDemandId(), ResourcePlanConstants.Working);
			//
			// String location=fGetFirstFreeSlot(networkid, nodeid);
			// logger.info(j+" fAssignOLPs() location found : "+location);
			// if(((location.contains("_6")/*|(location.contains("_9"))*/)))
			// {
			// j--;
			// int[] id=rpu.locationIds(location);
			// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],ResourcePlanConstants.CardMpc);
			// fSaveCardInDb(networkid, nodeid,
			// id[0],id[1],id[2],ResourcePlanConstants.CardMpc,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc),"",ResourcePlanConstants.ZERO);
			// fRemoveCard(nodepool,id[0],id[1],id[2],ResourcePlanConstants.CardMpc);
			// }
			// else
			// {
			// int[] id=rpu.locationIds(location);
			// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],ResourcePlanConstants.CardOlp);
			// fSaveCardInDb(networkid, nodeid,
			// id[0],id[1],id[2],ResourcePlanConstants.CardOlp,"",0,demandList.get(j).getDemandId(),rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp),"",ResourcePlanConstants.ZERO);
			// fRemoveCard(nodepool,id[0],id[1],id[2],ResourcePlanConstants.CardOlp);
			// }
			// }
			// }

			// find OMS protected links and assign OLPs for them
			List<Link> omsLinks = dbService.getLinkService().FindOMSProtectedLinksOnANode(networkid, nodeid);
			for (int j = 0; j < omsLinks.size(); j++) {
				String dirLink = dbService.getLinkService().FindLinkDirection(networkid, omsLinks.get(j).getSrcNode(),
						omsLinks.get(j).getDestNode());

				String location = fGetFirstFreeSlot(networkid, nodeid);
				logger.info(j + " fAssignOLPs() location found : " + location);
				if (((location.contains("_6")/* |(location.contains("_9")) */))) {
					j--;
					int[] id = rpu.locationIds(location);
					assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], ResourcePlanConstants.CardMpc);
					fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO, "");
					fRemoveCard(nodepool, id[0], id[1], id[2], ResourcePlanConstants.CardMpc);
				} else {
					int[] id = rpu.locationIds(location);
					assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], ResourcePlanConstants.CardOlp);
					fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, dirLink, 0, 0,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "", ResourcePlanConstants.ZERO, "");
					fRemoveCard(nodepool, id[0], id[1], id[2], ResourcePlanConstants.CardOlp);
				}
			}

			poolmap.remove(nodeid);
			poolmap.put(nodeid, nodepool);
			assignedmap.remove(nodeid);
			assignedmap.put(nodeid, assignedSys);
		}
	}

	public void fAssignOLPsDb(int networkid) {
		logger.info(" fAssignOLPsDb");
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		String[] pnodeids;
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		for (int i = 0; i < nodelist.size(); i++) {
			int nodeid = nodelist.get(i);

			// int
			// cntOlpCircuits=dbService.getCircuitService().countCircuitsByClientProtPerNode(networkid,
			// nodeid,MapConstants.OLPProtection);
			List<Circuit> cList = dbService.getCircuitService().CircuitsByClientProtPerNode(networkid, nodeid,
					MapConstants.OLPProtection);
			int cntOlp = cList.size();
			System.out.println("Client Ptc with OLP size:"+cntOlp);
			String MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);

			logger.info(" NodeId: " + nodeid + "Required OLPs: " + cntOlp);
			for (int j = 0; j < cList.size(); j++) {

				String location = rpu.fGetFirstFreeSlotDb(networkid, nodeid);
				logger.info(j + " fAssignOLPs() location found : " + location);
				if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
					if (((location.contains("_6") | (location.contains("_9"))))) {
						j--;
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
								"");
					} else {
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "", cList.get(j).getCircuitId(),
								"");
					}

				} else {
					if (((location.contains("_6")/* |(location.contains("_9")) */))) {
						j--;
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
								"");
					} else {
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "", cList.get(j).getCircuitId(),
								"");
					}

				}

			}

			/**
			 * commented as OPX card will be used for the channel protection hence OLPs will
			 * not be required
			 */

			logger.info(" fAssignOLPs for Channel Protection ");
			List <Demand> demandList =
					dbService.getDemandService().FindAllByNodeId(networkid, nodeid);
			for (int j = 0; j < demandList.size(); j++)
			{
				if(demandList.get(j).getChannelProtection().equalsIgnoreCase(ResourcePlanConstants.Yes)
						&& demandList.get(j).getClientProtectionType().equalsIgnoreCase(ResourcePlanConstants.OLPProtection))
				{				
					System.out.println("Channel Ptc with OLP:"+demandList.get(j).getDemandId()+" and Node Id:"+nodeid);
					NetworkRoute nr =
							dbService.getNetworkRouteService().FindAllByDemandId(networkid,
									demandList.get(j).getDemandId(), ResourcePlanConstants.TWO);
					int wavelength=nr.getWavelengthNo();

					MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);
					String location=rpu.fGetFirstFreeSlotDb(networkid, nodeid);//fGetFirstFreeSlot(networkid, nodeid);
					System.out.println(j+" fAssignOLPs() location found : "+location);
					if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
						if (((location.contains("_6") | (location.contains("_9"))))) {
							j--;
							int[] id = rpu.locationIds(location);
							fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
									"");
						} else {
							int[] id = rpu.locationIds(location);
							fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, "",  wavelength, demandList.get(j).getDemandId(),
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "", ResourcePlanConstants.ZERO,
									"");
						}

					} else {
						if (((location.contains("_6")/* |(location.contains("_9")) */))) {
							j--;
							int[] id = rpu.locationIds(location);
							fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
									"");
						} else {
							int[] id = rpu.locationIds(location);
							fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, "", wavelength, demandList.get(j).getDemandId(),
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "", ResourcePlanConstants.ZERO,
									"");
						}

					}


					//					if(((location.contains("_6")|(location.contains("_9")))))
					//					{
					//						j--;
					//						int[] id=rpu.locationIds(location);
					//						//assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],ResourcePlanConstants.CardMpc);
					//						fSaveCardInDb(networkid, nodeid,
					//								id[0],id[1],id[2],ResourcePlanConstants.CardMpc,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc),"",ResourcePlanConstants.ZERO,"");
					//						//fRemoveCard(nodepool,id[0],id[1],id[2],ResourcePlanConstants.CardMpc);
					//					}
					//					else
					//					{
					//						int[] id=rpu.locationIds(location);
					//						//assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],ResourcePlanConstants.CardOlp);
					//						fSaveCardInDb(networkid, nodeid,
					//								id[0],id[1],id[2],ResourcePlanConstants.CardOlp,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp),"",ResourcePlanConstants.ZERO,"");
					//						//fRemoveCard(nodepool,id[0],id[1],id[2],ResourcePlanConstants.CardOlp);
					//					}
				}
			}

			// find OMS protected links and assign OLPs for them
			List<Link> omsLinks = dbService.getLinkService().FindOMSProtectedLinksOnANode(networkid, nodeid);
			System.out.println("OMS links size:"+omsLinks.size());
			for (int j = 0; j < omsLinks.size(); j++) {
				String dirLink = dbService.getLinkService().FindLinkDirection(networkid, omsLinks.get(j).getSrcNode(),
						omsLinks.get(j).getDestNode());

				String location = rpu.fGetFirstFreeSlotDb(networkid, nodeid);
				logger.info(j + " fAssignOLPs() location found : " + location);
				if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
					if (((location.contains("_6") | (location.contains("_9"))))) {
						j--;
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
								"");
					} else {
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, dirLink, 0,
								0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "",
								ResourcePlanConstants.ZERO, "");
					}
				} else {
					if (((location.contains("_6")/* |(location.contains("_9")) */))) {
						j--;
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
								"");
					} else {
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, dirLink, 0,
								0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "",
								ResourcePlanConstants.ZERO, "");
					}
				}
			}

		}
	}

	public void fAssignOLPsBrField(int networkid) {
		logger.info(" fAssignOLPsBrField");
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkidBF);
		String DirP = "", location = "";
		int nodeid = 0;
		String[] pnodeids;
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		for (int i = 0; i < nodelist.size(); i++) {
			nodeid = nodelist.get(i);

			/*********************************************************************************************
			 * ******************************* For Client Protection *************************************
			 *********************************************************************************************/
			/**
			 * For Client Protection
			 */
			logger.info(" fAssignOLPs for Client Protection for added circuits ");

			List<Circuit> cListDel = dbService.getCircuitService().FindDeletedCircuitsInBrField(networkid, networkidBF,
					nodeid);

			for (int j = 0; j < cListDel.size(); j++) {
				if (cListDel.get(j).getClientProtectionType().equalsIgnoreCase(MapConstants.OLPProtection)) {
					CardInfo olp = dbService.getCardInfoService().FindOLPByCircuitId(networkidBF, nodeid,
							cListDel.get(j).getCircuitId());
					dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, olp.getRack(), olp.getSbrack(),
							olp.getCard());
				}
			}

			List<Circuit> cList = dbService.getCircuitService().FindAddedCircuitsInBrField(networkid, networkidBF,
					nodeid);

			for (int j = 0; j < cList.size(); j++) {
				if (cList.get(j).getClientProtectionType().equalsIgnoreCase(MapConstants.OLPProtection)) {
					// String location=fGetFirstFreeSlot(networkid, nodeid);
					// nodeid=cList.get(j).getSrcNodeId();
					location = rpu.fGetFirstFreeSlotDb(networkidBF, nodeid);
					logger.info(j + " fAssignOLPs() location found : " + location);
					if (((location.contains("_6")/* |(location.contains("_9")) */))) {
						j--;
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
								"");
					} else {
						int[] id = rpu.locationIds(location);
						fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "", cList.get(j).getCircuitId(),
								"");
					}
				}
			}

			/*********************************************************************************************
			 * ******************************* For Channel Protection *************************************
			 *********************************************************************************************/

			/**
			 * commented as OPX card will be used for the channel protection hence OLPs will
			 * not be required
			 */

			List <Demand> demandListDel =
					dbService.getDemandService().FindDeletedDemandsInBrField(networkid,
							networkidBF, nodeid);
			for (int j = 0; j < demandListDel.size(); j++)
			{
				if(demandListDel.get(j).getChannelProtection().equalsIgnoreCase(ResourcePlanConstants.Yes) && 
						demandListDel.get(j).getClientProtectionType().equalsIgnoreCase(ResourcePlanConstants.OLPProtection))
				{
					CardInfo olp = dbService.getCardInfoService().FindCardInfo(networkidBF,
							nodeid, demandListDel.get(j).getDemandId(), ResourcePlanConstants.CardOlp);
					dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, olp.getRack(),
							olp.getSbrack(), olp.getCard());
				}

			}

			logger.info(" fAssignOLPs for Channel Protection for new demands");
			List <Demand> demandListnew =
					dbService.getDemandService().FindAddedDemandsInBrField(networkid,
							networkidBF, nodeid);
			for (int j = 0; j < demandListnew.size(); j++)
			{
				if(demandListnew.get(j).getChannelProtection().equalsIgnoreCase(ResourcePlanConstants.Yes) && 
						demandListnew.get(j).getClientProtectionType().equalsIgnoreCase(ResourcePlanConstants.OLPProtection))
				{
					NetworkRoute nr =
							dbService.getNetworkRouteService().FindAllByDemandId(networkidBF,
									demandListnew.get(j).getDemandId(), ResourcePlanConstants.TWO);
					if(nr!=null)
					{
						pnodeids=nr.getPath().split(",");
						if(nodeid==Integer.parseInt(pnodeids[0].toString()))
							DirP =
							dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(pnodeids[0].toString()),Integer.parseInt(pnodeids[0+1].toString()));
						else if(nodeid==Integer.parseInt(pnodeids[pnodeids.length-1].toString()))
							DirP =
							dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(pnodeids[pnodeids.length-1].toString()),Integer.parseInt(pnodeids[pnodeids.length-1-1].toString()));
					}


					location=rpu.fGetFirstFreeSlotDb(networkidBF, nodeid);
					logger.info(j+" fAssignOLPs() location found : "+location);
					String MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);
					if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
						if (((location.contains("_6") | (location.contains("_9"))))) {
							j--;
							int[] id = rpu.locationIds(location);
							fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
									"");
						} else {
							int[] id = rpu.locationIds(location);
							fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, DirP, 0,
									demandListnew.get(j).getDemandId(), rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "",
									ResourcePlanConstants.ZERO, "");
						}
					} else {
						if (((location.contains("_6")/* |(location.contains("_9")) */))) {
							j--;
							int[] id = rpu.locationIds(location);
							fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
									"");
						} else {
							int[] id = rpu.locationIds(location);
							fSaveCardInDb(networkidBF, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardOlp, DirP, 0,
									demandListnew.get(j).getDemandId(), rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp), "",
									ResourcePlanConstants.ZERO, "");
						}
					}

				}
			}

			/*********************************************************************************************
			 * ******************************* For OMS Protection *************************************
			 *********************************************************************************************/

			// // find OMS protected links and assign OLPs for them
			// List <Link>
			// omsLinks=dbService.getLinkService().FindOMSProtectedLinksOnANode(networkidBF,
			// nodeid);
			// for (int j = 0; j < omsLinks.size(); j++) {
			// String dirLink =
			// dbService.getLinkService().FindLinkDirection(omsLinks.get(j).getSrcNode(),
			// omsLinks.get(j).getDestNode());
			//
			// String location=rpu.fGetFirstFreeSlotDb(networkidBF, nodeid);
			// logger.info(j+" fAssignOLPs() location found : "+location);
			// if(((location.contains("_6")|(location.contains("_9")))))
			// {
			// j--;
			// int[] id=rpu.locationIds(location);
			//// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],ResourcePlanConstants.CardMpc);
			// fSaveCardInDb(networkidBF, nodeid,
			// id[0],id[1],id[2],ResourcePlanConstants.CardMpc,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc),"",ResourcePlanConstants.ZERO);
			//// fRemoveCard(nodepool,id[0],id[1],id[2],ResourcePlanConstants.CardMpc);
			// }
			// else
			// {
			// int[] id=rpu.locationIds(location);
			//// assignedSys=fAssignCard(assignedSys,id[0],id[1],id[2],ResourcePlanConstants.CardOlp);
			// fSaveCardInDb(networkidBF, nodeid,
			// id[0],id[1],id[2],ResourcePlanConstants.CardOlp,dirLink,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOlp),"",ResourcePlanConstants.ZERO);
			//// fRemoveCard(nodepool,id[0],id[1],id[2],ResourcePlanConstants.CardOlp);
			// }
			// }
		}
	}

	/**
	 * functions gives how many MPNs will be required according to the protection
	 * types specified
	 * 
	 * @param ProtectionType
	 * @param SbrackProtection
	 * @param ChannelProtection
	 * @return
	 * @throws SQLException
	 */
	public Object[][] getCardRequirement(int networkid, int nodeid, String ProtectionType, String SbrackProtection,
			String ChannelProtection, String ClientProtection, int NoOfPathsFound, String LineRate, int demandid,String ProtectionMechanism)
					throws SQLException {

		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		logger.info(" getCardRequirement " + "Node Id: " + nodeid + "Prot Type: " + ProtectionType + " Channel Prot: "
				+ ChannelProtection + " No Of Paths: " + NoOfPathsFound + " LineRate: " + LineRate + " DemandId: "
				+ demandid);

		Object[] Pinfo = new Object[2];
		Object[][] info = new Object[3][2]; // cardtype W, serial no w
		// cardtype P, Srial no P
		// pairedorNot,
		// right now consider two cardtypes MPN and OLP

		/* Set the appropriate logic on the basis of protection types */

		info[0][1] = "";// initialize serial no
		info[1][1] = "";

		if (LineRate.equalsIgnoreCase(MapConstants.Line100)) {
			if (dbService.getDemandService().FindDemand(networkid, demandid).getCircuitSet().split(",").length > 1) {
				// info[0][0]=rpu.fgetMpnPreference(networkid,nodeid)[0];

				Pinfo = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatMpn100G,
						ResourcePlanConstants.ParamDemand, "" + demandid);
				info[0][0] = Pinfo[0];
				info[0][1] = Pinfo[1];
			} else {
				logger.info(" After Split " + dbService.getDemandService().FindDemand(networkid, demandid)
						.getCircuitSet().toString().split(",")[0]);
				int cir = Integer.parseInt(dbService.getDemandService().FindDemand(networkid, demandid).getCircuitSet()
						.toString().split(",")[0]);
				if (dbService.getCircuitService().FindCircuit(networkid, cir)
						.getRequiredTraffic() == MapConstants.G100) {
					// info[0][0]=ResourcePlanConstants.CardTPN100G;

					// TPN CGC in place of TPN100G:28/2/18
					info[0][0] = ResourcePlanConstants.CardTPN100GCGC;
				} else {
					// info[0][0]=rpu.fgetMpnPreference(networkid,nodeid)[0];
					Pinfo = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatMpn100G,
							ResourcePlanConstants.ParamDemand, "" + demandid);
					info[0][0] = Pinfo[0];
					info[0][1] = Pinfo[1];
				}
			}
		} else if (LineRate.equalsIgnoreCase(MapConstants.Line200)) {
			info[0][0] = ResourcePlanConstants.CardMuxponder200G;
		} else if (LineRate.equalsIgnoreCase(MapConstants.Line10)) {
			if (dbService.getDemandService().FindDemand(networkid, demandid).getCircuitSet().split(",").length > 1) {
				info[0][0] = ResourcePlanConstants.CardMuxponder10G;
			} else {
				int cir = Integer.parseInt(dbService.getDemandService().FindDemand(networkid, demandid).getCircuitSet()
						.toString().replaceAll(",", ""));

				if (dbService.getCircuitService().FindCircuit(networkid, cir)
						.getRequiredTraffic() == MapConstants.G10) {
					// With preference of 10g tpn card
					Pinfo = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatTpn10G,
							ResourcePlanConstants.ParamDemand, "" + demandid);
					if(Pinfo[0].toString().equals("TPN10G"))
						info[0][0]=ResourcePlanConstants.CardMuxponder10G;
					else info[0][0] = Pinfo[0];
					info[0][1] = Pinfo[1];

					/* Previous Code */
					// info[0][0]=ResourcePlanConstants.CardTPN5x10G;
				} else {
					info[0][0] = ResourcePlanConstants.CardMuxponder10G;
				}
			}
		}

		if (ProtectionType.equalsIgnoreCase(SMConstants.OnePlusOnePlusRPtcStr)
				|| ProtectionType.equalsIgnoreCase(SMConstants.OnePlusOnePlusTwoRPtcTypeStr)
				|| ProtectionType.equalsIgnoreCase(
						SMConstants.OnePlusOnePtcStr)/*
						 * ||(ProtectionType.equalsIgnoreCase(SMConstants.
						 * OneIsToOnePtcTypeStr))||(ProtectionType.equalsIgnoreCase(
						 * SMConstants.OneIsToTwoRPtcTypeStr))
						 */) {
			if (NoOfPathsFound >= 2) {
				System.out.println("ClientProtection received -- " + ClientProtection + "ChannelProtection received -- "
						+ ChannelProtection);
				/* If client protection is 'Yes' , then two mpn card are required. */
				if (ClientProtection.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
					System.out.println("ClientProtection received -- " + ClientProtection);
					info[2][0] = ResourcePlanConstants.Yes;

					if ((info[0][0].toString().equalsIgnoreCase(ResourcePlanConstants.CardMuxponderCGM))
							| (info[0][0].toString().equalsIgnoreCase(ResourcePlanConstants.CardMuxponderCGX))) {
						Pinfo = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatMpn100G,
								ResourcePlanConstants.ParamDemand, "" + demandid);
						info[1][0] = Pinfo[0];
						info[1][1] = Pinfo[1];
					} else {
						info[1][0] = info[0][0];
					}
				}
				/*
				 * If channel protection is 'Yes' , then MpnOpx card can be used. One mpn is
				 * required
				 */
				else if (ChannelProtection.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
					System.out.println("ChannelProtection received -- " + ChannelProtection+" ProtectionMechanism--"+ProtectionMechanism);
					if(ProtectionMechanism.equals(ResourcePlanConstants.CardTypeOPX)){
						Pinfo = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatMpn100GOPX,
								ResourcePlanConstants.ParamDemand, "" + demandid);
					}else if(ProtectionMechanism.equals(ResourcePlanConstants.OLPProtection)){
						Pinfo = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatMpn100G,
								ResourcePlanConstants.ParamDemand, "" + demandid);
					}

					info[2][0] = ResourcePlanConstants.No;
					// info[0][0]=ResourcePlanConstants.CardMuxponderOPX;
					// System.out.println("Prefereed Card Type received OPX --
					// "+Pinfo[0].toString());
					info[0][0] = Pinfo[0];
				}

				/* Previous code */

				// info[2][0]=ResourcePlanConstants.Yes;
				// if((info[0][0].toString().equalsIgnoreCase(ResourcePlanConstants.CardMuxponderCGM))|(info[0][0].toString().equalsIgnoreCase(ResourcePlanConstants.CardMuxponderCGX)))
				// {
				// Pinfo=eqp.fGetPreferredEqType(networkid, nodeid,
				// ResourcePlanConstants.CatMpn100G, ResourcePlanConstants.ParamDemand,
				// ""+demandid);
				// info[1][0]= Pinfo[0];
				// info[1][1]= Pinfo[1];
				// }
				// else
				// {
				// info[1][0]=info[0][0];
				// }
			} else {
				info[2][0] = ResourcePlanConstants.No;
			}

		} else if ((ProtectionType.equalsIgnoreCase(SMConstants.OneIsToOnePtcTypeStr))
				|| (ProtectionType.equalsIgnoreCase(SMConstants.OneIsToTwoRPtcTypeStr))) {
			info[2][0] = ResourcePlanConstants.No;
		} else if (ProtectionType.equalsIgnoreCase(SMConstants.PtcTypeNoneStr)) {
			info[2][0] = ResourcePlanConstants.No;

			/*
			 * In case of protection none, channel and client ptc will also be none . Hence
			 * only one MPN is required
			 */

			// if(ChannelProtection.equalsIgnoreCase(MapConstants.Yes))
			// {
			// info[0][0]=ResourcePlanConstants.CardMuxponderOPX;
			// }
		}

		logger.info(" getCardRequirement NoOfPathsFound: " + NoOfPathsFound + " CardType W: " + info[0][0]
				+ "Serial No W: " + info[0][1] + " CardType P: " + info[1][0] + "Serial No P: " + info[1][1]
						+ " PairedStatus: " + info[2][0]);
		return info;
	}

	public String[] fGetFirstFreeMpnSlot(int networkid, int nodeid, int nodetype, int nodedegree, int slotsize,
			String paired) {
		/* fetch the nodepool from map for that node */
		/// logger.info(tag+"fGetFirstFreeMpnSlot(): Network "+networkid+" Node
		/// "+nodeid+" NodeType "+nodetype+" Degree "+nodedegree+" SlotSize "+slotsize+"
		/// Parired "+paired);
		NodeSystem nodepool = poolmap.get(nodeid);
		/// logger.info(tag+" Nodeid: "+nodeid+ " Node Pool: "+nodepool.toString());
		boolean flag = true;
		String[] location = null;
		switch (nodedegree) {

		case ResourcePlanConstants.ONE:
		case ResourcePlanConstants.TWO:
		case ResourcePlanConstants.THREE:
		case ResourcePlanConstants.FOUR:
		case ResourcePlanConstants.FIVE:
		case ResourcePlanConstants.SIX:
		case ResourcePlanConstants.SEVEN:
		case ResourcePlanConstants.EIGHT: {
			// check if 1,2 and 4,5 are free or check 10,11 and 13,14 are free

			if (slotsize == ResourcePlanConstants.TWO) {
				if (paired == ResourcePlanConstants.Yes) {
					// start checking from rack 1 and sbrack 2
					for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) {
						for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) {
							location = new String[2];
							// skip for r,s 1,2
							// if(!(i==1)&(j==2))
							// {
							// for a two slot card check availability for both slots
							if ((fCheckSlotAvailability(networkid, nodeid, i, j, 1))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 2))) {
								if ((fCheckSlotAvailability(networkid, nodeid, i, j, 4))
										& (fCheckSlotAvailability(networkid, nodeid, i, j, 5))) {
									location[0] = rpu.locationStr(i, j, 1);
									location[1] = rpu.locationStr(i, j, 4);
									return location;

									// slots[0]=1;
									// slots[1]=2;
									// slots[2]=4;
									// slots[3]=5;
								} else {
									location = null;
								}
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 10))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 11))) {
								if ((fCheckSlotAvailability(networkid, nodeid, i, j, 13))
										& (fCheckSlotAvailability(networkid, nodeid, i, j, 14))) {
									location[0] = rpu.locationStr(i, j, 10);
									location[1] = rpu.locationStr(i, j, 13);
									return location;

									// slots[0]=1;
									// slots[1]=2;
									// slots[2]=4;
									// slots[3]=5;
								} else {
									location = null;
								}
							} else {
								location = null;
							}
							// }
						}
					}

				} else if (paired == ResourcePlanConstants.No)// not a paired slot but a two slot
				{
					for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) {
						for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) {
							location = new String[2];
							// skip for r,s 1,2
							// if(!(i==1)&(j==2))
							// {
							if ((fCheckSlotAvailability(networkid, nodeid, i, j, 1))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 2))) {
								location[0] = rpu.locationStr(i, j, 1);
								return location;
								// slots[0]=1;
								// slots[1]=2;
								// slots[2]=0;
								// slots[3]=0;
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 4))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 5))) {
								location[0] = rpu.locationStr(i, j, 4);
								return location;
								// slots[0]=4;
								// slots[1]=5;
								// slots[2]=0;
								// slots[3]=0;
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 7))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 8))) {
								location[0] = rpu.locationStr(i, j, 7);
								return location;
								// slots[0]=4;
								// slots[1]=5;
								// slots[2]=0;
								// slots[3]=0;
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 10))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 11))) {
								location[0] = rpu.locationStr(i, j, 10);
								return location;
								// slots[0]=10;
								// slots[1]=11;
								// slots[2]=0;
								// slots[3]=0;
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 13))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 14))) {
								location[0] = rpu.locationStr(i, j, 13);
								return location;
								// slots[0]=13;
								// slots[1]=14;
								// slots[2]=0;
								// slots[3]=0;
							}
							// }
						}
					}

				}
			} else if (slotsize == ResourcePlanConstants.ONE) {
				if (paired == ResourcePlanConstants.Yes) {
					for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) {
						for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) {
							location = new String[2];
							if ((fCheckSlotAvailability(networkid, nodeid, i, j, 1))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 4))) {
								location[0] = rpu.locationStr(i, j, 1);
								location[1] = rpu.locationStr(i, j, 4);
								return location;
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 10))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 13))) {
								location[0] = rpu.locationStr(i, j, 10);
								location[1] = rpu.locationStr(i, j, 13);
								return location;
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 3))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 7))) {
								location[0] = rpu.locationStr(i, j, 3);
								location[1] = rpu.locationStr(i, j, 7);
								return location;
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 8))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 12))) {
								location[0] = rpu.locationStr(i, j, 8);
								location[1] = rpu.locationStr(i, j, 12);
								return location;
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 2))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 5))) {
								location[0] = rpu.locationStr(i, j, 2);
								location[1] = rpu.locationStr(i, j, 5);
								return location;
							} else if ((fCheckSlotAvailability(networkid, nodeid, i, j, 11))
									& (fCheckSlotAvailability(networkid, nodeid, i, j, 14))) {
								location[0] = rpu.locationStr(i, j, 11);
								location[1] = rpu.locationStr(i, j, 14);
								return location;
							} else {
								location = null;
							}
						}
					}
				} else if (paired == ResourcePlanConstants.No) {
					for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) {
						for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) {
							location = new String[2];
							if (fCheckSlotAvailability(networkid, nodeid, i, j, 1)) {
								location[0] = rpu.locationStr(i, j, 1);
								return location;
							} else if (fCheckSlotAvailability(networkid, nodeid, i, j, 2)) {
								location[0] = rpu.locationStr(i, j, 2);
								return location;
							} else if (fCheckSlotAvailability(networkid, nodeid, i, j, 3)) {
								location[0] = rpu.locationStr(i, j, 3);
								return location;
							} else if (fCheckSlotAvailability(networkid, nodeid, i, j, 4)) {
								location[0] = rpu.locationStr(i, j, 4);
								return location;
							} else if (fCheckSlotAvailability(networkid, nodeid, i, j, 5)) {
								location[0] = rpu.locationStr(i, j, 5);
								return location;
							}

							else if (fCheckSlotAvailability(networkid, nodeid, i, j, 7)) {
								location[0] = rpu.locationStr(i, j, 7);
								return location;
							}

							else if (fCheckSlotAvailability(networkid, nodeid, i, j, 8)) {
								location[0] = rpu.locationStr(i, j, 8);
								return location;
							}

							else if (fCheckSlotAvailability(networkid, nodeid, i, j, 10)) {
								location[0] = rpu.locationStr(i, j, 10);
								return location;
							} else if (fCheckSlotAvailability(networkid, nodeid, i, j, 11)) {
								location[0] = rpu.locationStr(i, j, 11);
								return location;
							} else if (fCheckSlotAvailability(networkid, nodeid, i, j, 12)) {
								location[0] = rpu.locationStr(i, j, 12);
								return location;
							} else if (fCheckSlotAvailability(networkid, nodeid, i, j, 13)) {
								location[0] = rpu.locationStr(i, j, 13);
								return location;
							} else if (fCheckSlotAvailability(networkid, nodeid, i, j, 14)) {
								location[0] = rpu.locationStr(i, j, 14);
								return location;
							}
						}
					}
				}
			}

		}
		break;
		}
		return location;

	}

	public String[] fGetFirstFreeCardSlot(int networkid, int nodeid, int slotsize) {
		logger.info("Network " + networkid + " Node " + nodeid + " SlotSize " + slotsize);
		NodeSystem nodepool = poolmap.get(nodeid);
		logger.info(" Nodeid: " + nodeid + " Node Pool: " + nodepool.toString());
		boolean flag = true;
		String[] location = null;
		if (slotsize == ResourcePlanConstants.TWO) {
			fGetFirstFreeSlotInNode(networkid, nodeid);
			for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) {
				for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) {
					for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) {
						location = new String[2];
						if ((fCheckSlotAvailability(networkid, nodeid, i, j, k))
								& (fCheckSlotAvailability(networkid, nodeid, i, j, k + 1))) {
							location[0] = rpu.locationStr(i, j, k);
							location[1] = rpu.locationStr(i, j, k + 1);
							return location;

						} else {
							location = null;
						}
					}
				}
			}
		} else if (slotsize == ResourcePlanConstants.ONE) {
			for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) {
				for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) {
					for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) {
						location = new String[2];
						if ((fCheckSlotAvailability(networkid, nodeid, i, j, k))) {
							location[0] = rpu.locationStr(i, j, k);
							return location;
						} else {
							location = null;
						}
					}
				}
			}
		}
		return location;
	}

	/**
	 * @brief Assigns common cards in NE like cscc, supy, paba, wss etc according to
	 *        nodetype and degree and stores the cards in db
	 * @param networkid
	 * @param nodeid
	 * @param nodetype
	 * @param nodedegree
	 * @throws SQLException
	 */
	public void fAssignCommonCards(int networkid, int nodeid, int nodetype, int nodedegree) throws SQLException {
		int rackid, sbrackid, cardid;
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);

		Node n = dbService.getNodeService().FindNode(networkid, nodeid);
		String Capacity = n.getCapacity();
		/// logger.info(tag+"ResourcePlanning.fAssignCommonCards(): Network:
		/// "+networkid+" Node: "+nodeid+" NodeType: "+nodetype+" Degree: "+nodedegree);
		/* fetch the nodepool from map for that node */
		NodeSystem nodepool = poolmap.get(nodeid);

		Topology t = dbService.getTopologyService().FindTopology(networkid, nodeid);
		String directions = getDirections(t);
		String[] dir = directions.split("-");
		String dir1 = "";

		String CsccRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatCscc);

		/* Get the assigned map */
		NodeSystem assignedSys;
		if (assignedmap.get(nodeid) != null) {
			/// logger.info("found in assigned map ()");
			assignedSys = assignedmap.get(nodeid);
		} else {
			/// logger.info("found not in assigned map ()");
			assignedSys = new NodeSystem(nodeid);
		}

		switch (nodetype) {
		case MapConstants.roadm: {
			/* assuming cdc architecture */
			rackid = ResourcePlanConstants.ONE;
			sbrackid = ResourcePlanConstants.TWO;
			/* Place cscc cards at location 1,2,6 and 1,2,9 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SIX, ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.NINE, ResourcePlanConstants.CardCscc);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
					"", ResourcePlanConstants.ZERO, "");
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");

			/* Place supy cards at location 1,2,7 and 1,2,8 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy);
			// assignedSys=fAssignCard(assignedSys,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);//supy
			// card is needed only one upto 6 directions
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.EIGHT,
					ResourcePlanConstants.CardReserved);// the eighth slot needs to be reserved

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SEVEN, ResourcePlanConstants.CardSupy);
			// fRemoveCard(nodepool,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.EIGHT, ResourcePlanConstants.CardReserved);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
					"", ResourcePlanConstants.ZERO, "");
			// fSaveCardInDb(networkid, nodeid,
			// rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),"",ResourcePlanConstants.ZERO);
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.EIGHT,
					ResourcePlanConstants.CardReserved, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", ResourcePlanConstants.ZERO, "");

			/* Place Voip cards at location 1,1,4 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FOUR, ResourcePlanConstants.CardVoip);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
					"", ResourcePlanConstants.ZERO, "");

			/* Place ocm cards at location 1,1,5 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FIVE, ResourcePlanConstants.CardOcm1x8);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");

			/* Place Edfa cards at location 1,1,10 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.TEN,
					ResourcePlanConstants.CardEdfa);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.TEN, ResourcePlanConstants.CardEdfa);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.TEN,
					ResourcePlanConstants.CardEdfa, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa),
					"", ResourcePlanConstants.ZERO, "");

			/* Place MCS cards at location 1,1,7 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid - 1, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardMcs);
			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid - 1, ResourcePlanConstants.SEVEN, ResourcePlanConstants.CardMcs);
			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid - 1, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardMcs, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMcs), "",
					ResourcePlanConstants.ZERO, "");

			if (nodedegree > ResourcePlanConstants.FOUR) {
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid + 1, ResourcePlanConstants.TEN,
						ResourcePlanConstants.CardEdfa);
				fRemoveCard(nodepool, rackid, sbrackid + 1, ResourcePlanConstants.TEN, ResourcePlanConstants.CardEdfa);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid + 1, ResourcePlanConstants.TEN,
						ResourcePlanConstants.CardEdfa, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa), "", ResourcePlanConstants.ZERO, "");

				assignedSys = fAssignCard(assignedSys, rackid, sbrackid + 1, ResourcePlanConstants.FIVE,
						ResourcePlanConstants.CardOcm1x8);
				fRemoveCard(nodepool, rackid, sbrackid + 1, ResourcePlanConstants.FIVE,
						ResourcePlanConstants.CardOcm1x8);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid + 1, ResourcePlanConstants.FIVE,
						ResourcePlanConstants.CardOcm1x8, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");
			}

			for (int i = 1; i <= nodedegree; i++) {
				String locPaBa = rpu.fgetLocationPaBa(i);
				String[] lp = locPaBa.split("_");
				rackid = Integer.parseInt(lp[0].toString());
				sbrackid = Integer.parseInt(lp[1].toString());
				cardid = Integer.parseInt(lp[2].toString());

				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa);
				fRemoveCard(nodepool, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa, dir[i - 1],
						0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO,
						"");

				String locWss = rpu.fgetLocationWss(i);
				String[] lw = locWss.split("_");
				rackid = Integer.parseInt(lw[0].toString());
				sbrackid = Integer.parseInt(lw[1].toString());
				cardid = Integer.parseInt(lw[2].toString());
				// get the preferred wss type and eid
				// Object wss[]=rpu.fgetWssPreference(networkid, nodeid, nodedegree);
				String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype);
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, cardid, Wsscardtype);
				fRemoveCard(nodepool, rackid, sbrackid, cardid, Wsscardtype);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, Wsscardtype, dir[i - 1], 0, 0, WssEqid, "",
						ResourcePlanConstants.ZERO, "");
			}

		}
		break;
		case MapConstants.ila: {
			/* assuming cdc architecture */
			rackid = ResourcePlanConstants.ONE;
			sbrackid = ResourcePlanConstants.TWO;
			/* Place cscc cards at location 1,2,6 and 1,2,9 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SIX, ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.NINE, ResourcePlanConstants.CardCscc);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
					"", ResourcePlanConstants.ZERO, "");
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");

			/* Place supy cards at location 1,2,7 and 1,2,8 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy);
			// assignedSys=fAssignCard(assignedSys,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SEVEN, ResourcePlanConstants.CardSupy);
			// fRemoveCard(nodepool,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
					"", ResourcePlanConstants.ZERO, "");
			// fSaveCardInDb(networkid, nodeid,
			// rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),"",ResourcePlanConstants.ZERO);

			/* Place Voip cards at location 1,1,4 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FOUR, ResourcePlanConstants.CardVoip);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
					"", ResourcePlanConstants.ZERO, "");

			/* Place ocm cards at location 1,1,5 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x2);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FIVE, ResourcePlanConstants.CardOcm1x2);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x2, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x2), "", ResourcePlanConstants.ZERO, "");

			/* Place ILA cards at location 1,2,1 and 1,2,14 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.ONE,
					ResourcePlanConstants.CardIla);
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FOURTEEN,
					ResourcePlanConstants.CardIla);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.ONE, ResourcePlanConstants.CardIla);
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FOURTEEN, ResourcePlanConstants.CardIla);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.ONE, ResourcePlanConstants.CardIla,
					MapConstants.EAST, 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardIla), "",
					ResourcePlanConstants.ZERO, "");
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOURTEEN,
					ResourcePlanConstants.CardIla, MapConstants.WEST, 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardIla), "", ResourcePlanConstants.ZERO, "");

		}
		break;

		case MapConstants.te: {

			/* assuming cdc architecture */
			rackid = ResourcePlanConstants.ONE;
			sbrackid = ResourcePlanConstants.TWO;
			/* Place cscc cards at location 1,2,6 and 1,2,9 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SIX, ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.NINE, ResourcePlanConstants.CardCscc);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
					"", ResourcePlanConstants.ZERO, "");
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");

			/* Place supy cards at location 1,2,7 and 1,2,8 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy);
			// assignedSys=fAssignCard(assignedSys,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SEVEN, ResourcePlanConstants.CardSupy);
			// fRemoveCard(nodepool,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
					"", ResourcePlanConstants.ZERO, "");
			// fSaveCardInDb(networkid, nodeid,
			// rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy));

			/* Place Voip cards at location 1,1,4 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FOUR, ResourcePlanConstants.CardVoip);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
					"", ResourcePlanConstants.ZERO, "");

			/* Place ocm cards at location 1,1,5 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FIVE, ResourcePlanConstants.CardOcm1x8);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");

			for (int i = 1; i <= nodedegree; i++) {
				String locPaBa = rpu.fgetLocationPaBa(i);
				String[] lp = locPaBa.split("_");
				rackid = Integer.parseInt(lp[0].toString());
				sbrackid = Integer.parseInt(lp[1].toString());
				cardid = Integer.parseInt(lp[2].toString());

				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa);
				fRemoveCard(nodepool, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa, dir[i - 1],
						0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO,
						"");

				String locWss = rpu.fgetLocationWss(i);
				String[] lw = locWss.split("_");
				rackid = Integer.parseInt(lw[0].toString());
				sbrackid = Integer.parseInt(lw[1].toString());
				cardid = Integer.parseInt(lw[2].toString());
				// get the preferred wss type and eid
				// Object wss[]=rpu.fgetWssPreference(networkid, nodeid, nodedegree);
				String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype);
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, cardid, Wsscardtype);
				fRemoveCard(nodepool, rackid, sbrackid, cardid, Wsscardtype);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, Wsscardtype, dir[i - 1], 0, 0, WssEqid, "",
						ResourcePlanConstants.ZERO, "");
			}

			/** Commented due to generation after all the card assignment */	
			// assing mux in space below last sbrack level1
			// assignedSys = fAssignCard(assignedSys, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit, "", 0, 0,
			// 		rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "", ResourcePlanConstants.ZERO,
			// 		"");

			// assignedSys = fAssignCard(assignedSys, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit, "", 0, 0,
			// 		rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "", ResourcePlanConstants.ZERO,
			// 		"");

		}
		break;

		case MapConstants.twoBselectRoadm: {
			/* assuming cdc architecture */
			rackid = ResourcePlanConstants.ONE;
			sbrackid = ResourcePlanConstants.TWO;
			/* Place cscc cards at location 1,2,6 and 1,2,9 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SIX, ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.NINE, ResourcePlanConstants.CardCscc);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
					"", ResourcePlanConstants.ZERO, "");
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");

			/* Place supy cards at location 1,2,7 and 1,2,8 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy);
			// assignedSys=fAssignCard(assignedSys,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SEVEN, ResourcePlanConstants.CardSupy);
			// fRemoveCard(nodepool,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
					"", ResourcePlanConstants.ZERO, "");
			// fSaveCardInDb(networkid, nodeid,
			// rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy));

			/* Place Voip cards at location 1,1,4 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FOUR, ResourcePlanConstants.CardVoip);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
					"", ResourcePlanConstants.ZERO, "");

			/* Place ocm cards at location 1,1,5 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FIVE, ResourcePlanConstants.CardOcm1x8);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");

			for (int i = 1; i <= nodedegree; i++) {
				String locPaBa = rpu.fgetLocationPaBa(i);
				String[] lp = locPaBa.split("_");
				rackid = Integer.parseInt(lp[0].toString());
				sbrackid = Integer.parseInt(lp[1].toString());
				cardid = Integer.parseInt(lp[2].toString());

				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa);
				fRemoveCard(nodepool, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa, dir[i - 1],
						0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO,
						"");

				String locWss = rpu.fgetLocationWss(i);
				String[] lw = locWss.split("_");
				rackid = Integer.parseInt(lw[0].toString());
				sbrackid = Integer.parseInt(lw[1].toString());
				cardid = Integer.parseInt(lw[2].toString());
				// get the preferred wss type and eid
				// Object wss[]=rpu.fgetWssPreference(networkid, nodeid, nodedegree);
				String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype);
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, cardid, Wsscardtype);
				fRemoveCard(nodepool, rackid, sbrackid, cardid, Wsscardtype);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, Wsscardtype, dir[i - 1], 0, 0, WssEqid, "",
						ResourcePlanConstants.ZERO, "");
			}

			/** Commented due to generation after all the card assignment */
			// if(Capacity.equalsIgnoreCase("3"))
			// {
			// rackid=ResourcePlanConstants.ONE;

			// //assing mux demux for east in rack 1
			// assignedSys=fAssignCard(assignedSys,rackid,0,1,ResourcePlanConstants.Odd_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid,
			// rackid,0,1,ResourcePlanConstants.Odd_Mux_Demux_Unit,MapConstants.EAST,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit),"",ResourcePlanConstants.ZERO,"");

			// assignedSys=fAssignCard(assignedSys,rackid,0,2,ResourcePlanConstants.Even_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid,
			// rackid,0,2,ResourcePlanConstants.Even_Mux_Demux_Unit,MapConstants.EAST,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit),"",ResourcePlanConstants.ZERO,"");

			// //assing mux demux for West in rack 2
			// rackid=ResourcePlanConstants.TWO;

			// assignedSys=fAssignCard(assignedSys,rackid,0,1,ResourcePlanConstants.Odd_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid,
			// rackid,0,1,ResourcePlanConstants.Odd_Mux_Demux_Unit,MapConstants.WEST,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit),"",ResourcePlanConstants.ZERO,"");

			// assignedSys=fAssignCard(assignedSys,rackid,0,2,ResourcePlanConstants.Even_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid,
			// rackid,0,2,ResourcePlanConstants.Even_Mux_Demux_Unit,MapConstants.WEST,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit),"",ResourcePlanConstants.ZERO,"");
			// }
			// else if(Capacity.equalsIgnoreCase("1"))
			// {
			// rackid=ResourcePlanConstants.ONE;
			// assignedSys=fAssignCard(assignedSys,rackid,0,1,ResourcePlanConstants.Even_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid,
			// rackid,0,1,ResourcePlanConstants.Even_Mux_Demux_Unit,MapConstants.EAST,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit),"",ResourcePlanConstants.ZERO,"");
			// // rackid=ResourcePlanConstants.TWO;
			// assignedSys=fAssignCard(assignedSys,rackid,0,2,ResourcePlanConstants.Even_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid,
			// rackid,0,2,ResourcePlanConstants.Even_Mux_Demux_Unit,MapConstants.WEST,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit),"",ResourcePlanConstants.ZERO,"");
			// }
			// else if(Capacity.equalsIgnoreCase("2"))
			// {
			// rackid=ResourcePlanConstants.ONE;
			// assignedSys=fAssignCard(assignedSys,rackid,0,1,ResourcePlanConstants.Odd_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid,
			// rackid,0,1,ResourcePlanConstants.Odd_Mux_Demux_Unit,MapConstants.EAST,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit),"",ResourcePlanConstants.ZERO,"");
			// // rackid=ResourcePlanConstants.TWO;
			// assignedSys=fAssignCard(assignedSys,rackid,0,2,ResourcePlanConstants.Odd_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid,
			// rackid,0,2,ResourcePlanConstants.Odd_Mux_Demux_Unit,MapConstants.WEST,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit),"",ResourcePlanConstants.ZERO,"");
			// }
		}
		break;

		case MapConstants.hub: {
			rackid = ResourcePlanConstants.ONE;
			sbrackid = ResourcePlanConstants.TWO;
			/* Place cscc cards at location 1,2,6 and 1,2,9 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SIX, ResourcePlanConstants.CardCscc);
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.NINE, ResourcePlanConstants.CardCscc);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
					"", ResourcePlanConstants.ZERO, "");
			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
						ResourcePlanConstants.CardCscc, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");

			/* Place supy cards at location 1,2,7 and 1,2,8 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy);
			// assignedSys=fAssignCard(assignedSys,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SEVEN, ResourcePlanConstants.CardSupy);
			// fRemoveCard(nodepool,rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
					"", ResourcePlanConstants.ZERO, "");
			// fSaveCardInDb(networkid, nodeid,
			// rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy));

			/* Place Voip cards at location 1,1,4 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FOUR, ResourcePlanConstants.CardVoip);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
					"", ResourcePlanConstants.ZERO, "");

			/* Place ocm cards at location 1,1,5 */
			assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8);

			/* remove the card from the node pool */
			fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.FIVE, ResourcePlanConstants.CardOcm1x8);

			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");

			for (int i = 1; i <= nodedegree; i++) {
				String locPaBa = rpu.fgetLocationPaBa(i);
				String[] lp = locPaBa.split("_");
				rackid = Integer.parseInt(lp[0].toString());
				sbrackid = Integer.parseInt(lp[1].toString());
				cardid = Integer.parseInt(lp[2].toString());

				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa);
				fRemoveCard(nodepool, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa, dir[i - 1],
						0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO,
						"");

				String locWss = rpu.fgetLocationWss(i);
				String[] lw = locWss.split("_");
				rackid = Integer.parseInt(lw[0].toString());
				sbrackid = Integer.parseInt(lw[1].toString());
				cardid = Integer.parseInt(lw[2].toString());
				// get the preferred wss type and eid
				// Object wss[]=rpu.fgetWssPreference(networkid, nodeid, nodedegree);
				String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype);
				assignedSys = fAssignCard(assignedSys, rackid, sbrackid, cardid, Wsscardtype);
				fRemoveCard(nodepool, rackid, sbrackid, cardid, Wsscardtype);
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, Wsscardtype, dir[i - 1], 0, 0, WssEqid, "",
						ResourcePlanConstants.ZERO, "");
			}

			rackid = ResourcePlanConstants.ONE;

			// // assing mux demux for east in rack 1
			// assignedSys = fAssignCard(assignedSys, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.EAST,
			// 		0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
			// 		ResourcePlanConstants.ZERO, "");

			// assignedSys = fAssignCard(assignedSys, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST,
			// 		0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
			// 		ResourcePlanConstants.ZERO, "");

			// // assing mux demux for West in rack 2
			// rackid = ResourcePlanConstants.TWO;

			// assignedSys = fAssignCard(assignedSys, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.WEST,
			// 		0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
			// 		ResourcePlanConstants.ZERO, "");

			// assignedSys = fAssignCard(assignedSys, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit);
			// fSaveCardInDb(networkid, nodeid, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST,
			// 		0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
			// 		ResourcePlanConstants.ZERO, "");

		}
		break;
		}

		poolmap.remove(nodeid);
		/* place the updated nodepool in the poolmap */
		poolmap.put(nodeid, nodepool);

		assignedmap.remove(nodeid);
		assignedmap.put(nodeid, assignedSys);
		/// logger.info(tag+"ResourcePlanning.fAssignCommonCards(): The assigned pool:
		/// "+assignedmap.get(nodeid).toString());
		/// logger.info(tag+"ResourcePlanning.fAssignCommonCards(): The current pool:
		/// "+poolmap.get(nodeid).toString());

	}

	/**
	 * @brief Assigns common cards in NE like cscc, supy, paba, wss etc according to
	 *        nodetype and degree and stores the cards in db, does not uses poolmap,
	 * @param networkid
	 * @param nodeid
	 * @param nodetype
	 * @param nodedegree
	 * @throws SQLException
	 */
	public List<CardInfo> fAssignCommonCardsDb(int networkid, int nodeid, int nodetype, int nodedegree)
			throws SQLException {
		int rackid, sbrackid, cardid;
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);

		Node n = dbService.getNodeService().FindNode(networkid, nodeid);
		String Capacity = n.getCapacity();

		Topology t = dbService.getTopologyService().FindTopology(networkid, nodeid);
		String directions = getDirections(t);
		String[] dir = directions.split("-");
		String dir1 = "";
		String CsccRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatCscc);

		List<CardInfo> cardNotAllocated = new ArrayList<CardInfo>();
		CardInfo card;
		int ramanLinksCount=0;

		fAssignAmplifierCardsForNodesAllDirections(networkid,nodeid);

		/** Assign Common cards irrespective of nodetype **/
		/* assuming cdc architecture */
		rackid = ResourcePlanConstants.ONE;
		sbrackid = ResourcePlanConstants.TWO;

		String PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
				ResourcePlanConstants.ParamSubrack, ""+rackid+"-"+sbrackid)[0].toString();

		// Assign CSCC card in subrack 2
		int[] location=rpu.fGetControllerCardLocation(PrefChassisType);		
		fSaveCardInDb(networkid, nodeid, rackid, sbrackid, location[0],
				ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
				"", ResourcePlanConstants.ZERO, "");
		if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, location[1],
					ResourcePlanConstants.CardCscc, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");
		}			

		EquipmentPreference oscPref=dbService.getEquipmentPreferenceService().FindPreferredEq(networkid, nodeid, ResourcePlanConstants.CatOsc);
		String supyRedun;
		try {
			if(!oscPref.getCardType().equals(ResourcePlanConstants.None)) {

				supyRedun = oscPref.getRedundancy();

				// Assign SUPY card in subrack 2
				int[] supylocation=rpu.fGetCommonCardLocation(PrefChassisType,ResourcePlanConstants.CardSupy);		
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, supylocation[0],
						ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
						"", ResourcePlanConstants.ZERO, "");

				// Assign SUPY reserve card
				if(supyRedun.equals(ResourcePlanConstants.Yes)) {
					fSaveCardInDb(networkid, nodeid, rackid, sbrackid, supylocation[1],ResourcePlanConstants.CardReserved, "", 0, 0,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", ResourcePlanConstants.ZERO,
							"");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

			supyRedun = ResourcePlanConstants.No;
			// Assign SUPY card in subrack 2
			int[] supylocation=rpu.fGetCommonCardLocation(PrefChassisType,ResourcePlanConstants.CardSupy);		
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, supylocation[0],
					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
					"", ResourcePlanConstants.ZERO, "");

			// Assign SUPY reserve card
			if(supyRedun.equals(ResourcePlanConstants.Yes)) {
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, supylocation[1],ResourcePlanConstants.CardReserved, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", ResourcePlanConstants.ZERO,
						"");
			}
		}



		EquipmentPreference opmPref=dbService.getEquipmentPreferenceService().FindPreferredEq(networkid, nodeid, ResourcePlanConstants.CatOpm);


		/** Assign CommonCards Based on NodeType **/
		switch (nodetype) {
		case MapConstants.roadm: {			

			/* assuming cdc architecture */

			/**  Get All Raman links for node **/
			ramanLinksCount=dbService.getLinkService().FindAllRamanLinks(networkid, nodeid).size();

			/**  Flag for OnlyRamanLinks or MixLinks **/
			// boolean hasDegree=nodedegree>0?true:false;
			boolean OnlyRamanFlag=nodedegree==ramanLinksCount && ramanLinksCount>0?true:false;			
			System.out.println("Raman Links Count :: "+ramanLinksCount+" NodeD :: "+nodedegree+" NodeId :: "+nodeid);

			/**  Assign first OCM card only if there are links other than RAMAN type **/
			if(nodedegree!=ramanLinksCount) {
				OnlyRamanFlag=false;
				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
						ResourcePlanConstants.CardOcm1x8, MapConstants.EAST, 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");
				if (card != null)
					cardNotAllocated.add(card);
			}	

			/** If there are demands associated with this , then only add mcs **/
			//			if(dbService.getDemandService().FindAllByNodeId(networkid, nodeid).size()>0){
			String prefChassisTypeSubrackThree=eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
					ResourcePlanConstants.ParamSubrack, rpu.rackSubrackParamValStr(rackid,sbrackid+1))[0].toString();

			int[] mcslocation=rpu.fGetCommonCardLocation(prefChassisTypeSubrackThree,ResourcePlanConstants.CardMcs);	
			fSaveCardInDb(networkid, nodeid, rackid,sbrackid+1,mcslocation[0],ResourcePlanConstants.CardMcs,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMcs),"",ResourcePlanConstants.ZERO,"");

			int[] edfalocation=rpu.fGetCommonCardLocation(PrefChassisType,ResourcePlanConstants.CardEdfa);

			/** If there are links other than RAMAN link, then add first edfa array **/
			if(nodedegree!=ramanLinksCount) {				
				card = fSaveCardInDb(networkid, nodeid, rackid,sbrackid,edfalocation[0],ResourcePlanConstants.CardEdfa,"11",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa),"",ResourcePlanConstants.ZERO,"");
				if(card!=null)
					cardNotAllocated.add(card);					
			}

			// Add second EDFA and OCM card if there are RAMAN links or nodedegree >4
			if(ramanLinksCount>0 | nodedegree>ResourcePlanConstants.FOUR)
			{					
				int SubrackForSecondCards=ResourcePlanConstants.THREE;
				if(OnlyRamanFlag)
					SubrackForSecondCards=ResourcePlanConstants.TWO;
				card = fSaveCardInDb(networkid, nodeid, rackid,SubrackForSecondCards,edfalocation[1],ResourcePlanConstants.CardEdfa,"12",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa),"",ResourcePlanConstants.ZERO,"");
				if(card!=null)
					cardNotAllocated.add(card);

				String direction=MapConstants.NE;
				int sbRackLocation=sbrackid+1;
				// If only raman links are there , then this is OcmId=1 card
				if(OnlyRamanFlag) {
					direction=MapConstants.EAST;
					sbRackLocation=sbrackid;
				}
				card= fSaveCardInDb(networkid, nodeid, rackid,sbRackLocation,ResourcePlanConstants.FIVE,ResourcePlanConstants.CardOcm1x8,direction,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8),"",ResourcePlanConstants.ZERO,"");
				if(card!=null)
					cardNotAllocated.add(card);				

			}
			//			}			


			//				if(PrefChassisType.equals(ResourcePlanConstants.EmersionChassis) )
			//				{
			//					fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardReserved, "", 0, 0,
			//							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", ResourcePlanConstants.ZERO,
			//							"");
			//				}							


			for (int i = 1; i <= nodedegree; i++) {


				//				String AmplifierCardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatAmplifier,
				//						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				//				int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());
				//
				//				String locAmpCard;
				//				String []lp;
				//				if(AmplifierCardtype.equals(ResourcePlanConstants.CardPaBa)){
				//					locAmpCard = rpu.fgetLocationPaBa(i);
				//				}
				//				else{
				//					locAmpCard = rpu.fgetLocationRaman(i);
				//				}
				//				
				//				lp = locAmpCard.split("_");
				//				rackid = Integer.parseInt(lp[0].toString());
				//				sbrackid = Integer.parseInt(lp[1].toString());
				//				cardid = Integer.parseInt(lp[2].toString());
				//				
				//				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, AmplifierCardtype,
				//						dir[i - 1], 0, 0, AmplifierEqid, "",
				//						ResourcePlanConstants.ZERO, "");
				//				
				////				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa, dir[i - 1],
				////						0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO,
				////						"");
				//				
				//				if (card != null)
				//					cardNotAllocated.add(card);

				String locWss = rpu.fgetLocationWss(i);
				String[] lw = locWss.split("_");
				rackid = Integer.parseInt(lw[0].toString());
				sbrackid = Integer.parseInt(lw[1].toString());
				cardid = Integer.parseInt(lw[2].toString());
				// get the preferred wss type and eid
				// Object wss[]=rpu.fgetWssPreference(networkid, nodeid, nodedegree);

				String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype.toString().trim());
				logger.debug("---------------- WssEqid -----------------" + WssEqid + " locWss:" + locWss
						+ " Wsscardtype:" + Wsscardtype);
				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, Wsscardtype, dir[i - 1], 0, 0,
						WssEqid, "", ResourcePlanConstants.ZERO, "");
				if (card != null)
					cardNotAllocated.add(card);
			}

		}
		break;
		case MapConstants.ila: {
			/* assuming cdc architecture */
			rackid = ResourcePlanConstants.ONE;
			sbrackid = ResourcePlanConstants.TWO;

			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
			//					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
			//					"", ResourcePlanConstants.ZERO, "");
			//			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
			//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
			//						ResourcePlanConstants.CardCscc, "", 0, 0,
			//						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");
			//
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
			//					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
			//					"", ResourcePlanConstants.ZERO, "");
			// fSaveCardInDb(networkid, nodeid,
			// rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),"",ResourcePlanConstants.ZERO);

			//			if(PrefChassisType.equals(ResourcePlanConstants.EmersionChassis) )
			//			{
			//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.EIGHT,
			//						ResourcePlanConstants.CardReserved, "", 0, 0,
			//						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", ResourcePlanConstants.ZERO, "");	
			//			}				

			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
			//					"", ResourcePlanConstants.ZERO, "");			

			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardReserved, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved),
			//					"", ResourcePlanConstants.ZERO, "");

			if(!opmPref.getCardType().equals(ResourcePlanConstants.None)) {
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
						ResourcePlanConstants.CardOcm1x2, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x2), "", ResourcePlanConstants.ZERO, "");
			}


			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.ONE, ResourcePlanConstants.CardIla,
			//					MapConstants.EAST, 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardIla), "",
			//					ResourcePlanConstants.ZERO, "");
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOURTEEN,
			//					ResourcePlanConstants.CardIla, MapConstants.WEST, 0, 0,
			//					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardIla), "", ResourcePlanConstants.ZERO, "");

		}
		break;

		case MapConstants.te: {

			/* assuming cdc architecture */
			//			rackid = ResourcePlanConstants.ONE;
			//			sbrackid = ResourcePlanConstants.TWO;
			//
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
			//					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
			//					"", ResourcePlanConstants.ZERO, "");
			//			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
			//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
			//						ResourcePlanConstants.CardCscc, "", 0, 0,
			//						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");
			//
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
			//					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
			//					"", ResourcePlanConstants.ZERO, "");
			// fSaveCardInDb(networkid, nodeid,
			// rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),"",ResourcePlanConstants.ZERO);

			//			if(PrefChassisType.equals(ResourcePlanConstants.EmersionChassis) ) {
			//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.EIGHT,
			//						ResourcePlanConstants.CardReserved, "", 0, 0,
			//						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", ResourcePlanConstants.ZERO, "");
			//			}			

			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
			//					"", ResourcePlanConstants.ZERO, "");			

			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardReserved, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved),
			//					"", ResourcePlanConstants.ZERO, "");

			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");

			for (int i = 1; i <= nodedegree; i++) {

				//				String AmplifierCardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatAmplifier,
				//						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				//				int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());
				//
				//				String locAmpCard;
				//				String []lp;
				//				if(AmplifierCardtype.equals(ResourcePlanConstants.CardPaBa)){
				//					locAmpCard = rpu.fgetLocationPaBa(i);
				//				}
				//				else{
				//					locAmpCard = rpu.fgetLocationRaman(i);
				//				}
				//				
				//				lp = locAmpCard.split("_");
				//				rackid = Integer.parseInt(lp[0].toString());
				//				sbrackid = Integer.parseInt(lp[1].toString());
				//				cardid = Integer.parseInt(lp[2].toString());
				//				
				//				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, AmplifierCardtype,
				//						dir[i - 1], 0, 0, AmplifierEqid, "",
				//						ResourcePlanConstants.ZERO, "");

				//				String locPaBa = rpu.fgetLocationPaBa(i);
				//				String[] lp = locPaBa.split("_");
				//				rackid = Integer.parseInt(lp[0].toString());
				//				sbrackid = Integer.parseInt(lp[1].toString());
				//				cardid = Integer.parseInt(lp[2].toString());

				//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa, dir[i - 1],
				//						0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO,
				//						"");

				String locWss = rpu.fgetLocationWss(i);
				String[] lw = locWss.split("_");
				rackid = Integer.parseInt(lw[0].toString());
				sbrackid = Integer.parseInt(lw[1].toString());
				cardid = Integer.parseInt(lw[2].toString());
				// get the preferred wss type and eid
				// Object wss[]=rpu.fgetWssPreference(networkid, nodeid, nodedegree);
				String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype.toString().trim());
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, Wsscardtype, dir[i - 1], 0, 0, WssEqid, "",
						ResourcePlanConstants.ZERO, "");
			}

			/** Commented due to generation after all the card assignment */
			// // assing mux in space below last sbrack level1
			// fSaveCardInDb(networkid, nodeid, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit, "", 0, 0,
			// 		rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "", ResourcePlanConstants.ZERO,
			// 		"");

			// fSaveCardInDb(networkid, nodeid, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit, "", 0, 0,
			// 		rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8Even_Mux_Demux_Unit), "", ResourcePlanConstants.ZERO,
			// 		"");

		}
		break;

		case MapConstants.twoBselectRoadm: {
			/* assuming cdc architecture */
			//			rackid = ResourcePlanConstants.ONE;
			//			sbrackid = ResourcePlanConstants.TWO;
			//
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
			//					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
			//					"", ResourcePlanConstants.ZERO, "");
			//			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
			//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
			//						ResourcePlanConstants.CardCscc, "", 0, 0,
			//						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");
			//
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
			//					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
			//					"", ResourcePlanConstants.ZERO, "");

			// fSaveCardInDb(networkid, nodeid,
			// rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),"",ResourcePlanConstants.ZERO);
			//			if(PrefChassisType.equals(ResourcePlanConstants.EmersionChassis) ) {
			//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.EIGHT,
			//						ResourcePlanConstants.CardReserved, "", 0, 0,
			//						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", ResourcePlanConstants.ZERO, "");
			//			}


			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
			//					"", ResourcePlanConstants.ZERO, "");

			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardReserved, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved),
			//					"", ResourcePlanConstants.ZERO, "");

			if(!opmPref.getCardType().equals(ResourcePlanConstants.None)) {
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
						ResourcePlanConstants.CardOcm1x8, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");
			}

			for (int i = 1; i <= nodedegree; i++) {

				//				String AmplifierCardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatAmplifier,
				//						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				//				int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());
				//
				//				String locAmpCard;
				//				String []lp;
				//				if(AmplifierCardtype.equals(ResourcePlanConstants.CardPaBa)){
				//					locAmpCard = rpu.fgetLocationPaBa(i);
				//				}
				//				else{
				//					locAmpCard = rpu.fgetLocationRaman(i);
				//				}
				//				
				//				lp = locAmpCard.split("_");
				//				rackid = Integer.parseInt(lp[0].toString());
				//				sbrackid = Integer.parseInt(lp[1].toString());
				//				cardid = Integer.parseInt(lp[2].toString());
				//				
				//				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, AmplifierCardtype,
				//						dir[i - 1], 0, 0, AmplifierEqid, "",
				//						ResourcePlanConstants.ZERO, "");

				//				String locPaBa = rpu.fgetLocationPaBa(i);
				//				String[] lp = locPaBa.split("_");
				//				rackid = Integer.parseInt(lp[0].toString());
				//				sbrackid = Integer.parseInt(lp[1].toString());
				//				cardid = Integer.parseInt(lp[2].toString());


				//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa, dir[i - 1],
				//						0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO,
				//						"");

				String locWss = rpu.fgetLocationWss(i);
				String[] lw = locWss.split("_");
				rackid = Integer.parseInt(lw[0].toString());
				sbrackid = Integer.parseInt(lw[1].toString());
				cardid = Integer.parseInt(lw[2].toString());
				// get the preferred wss type and eid
				// Object wss[]=rpu.fgetWssPreference(networkid, nodeid, nodedegree);

				String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype.toString().trim());
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, Wsscardtype, dir[i - 1], 0, 0, WssEqid, "",
						ResourcePlanConstants.ZERO, "");
			}			
		}
		break;

		case MapConstants.hub: {
			//			rackid = ResourcePlanConstants.ONE;
			//			sbrackid = ResourcePlanConstants.TWO;
			//
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
			//					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
			//					"", ResourcePlanConstants.ZERO, "");
			//			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
			//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
			//						ResourcePlanConstants.CardCscc, "", 0, 0,
			//						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");
			//
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
			//					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
			//					"", ResourcePlanConstants.ZERO, "");
			// fSaveCardInDb(networkid, nodeid,
			// rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy));

			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
			//					"", ResourcePlanConstants.ZERO, "");

			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardReserved, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved),
			//					"", ResourcePlanConstants.ZERO, "");

			/* Place ocm cards at location 1,1,5 */
			/* save card in db */
			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
					ResourcePlanConstants.CardOcm1x8, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");

			for (int i = 1; i <= nodedegree; i++) {

				//				String AmplifierCardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatAmplifier,
				//						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				//				int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());
				//
				//				String locAmpCard;
				//				String []lp;
				//				if(AmplifierCardtype.equals(ResourcePlanConstants.CardPaBa)){
				//					locAmpCard = rpu.fgetLocationPaBa(i);
				//					lp = locAmpCard.split("_");
				//					rackid = Integer.parseInt(lp[0].toString());
				//					sbrackid = Integer.parseInt(lp[1].toString());
				//					cardid = Integer.parseInt(lp[2].toString());
				//				}
				//				else
				//				{
				//					locAmpCard = rpu.fgetLocationRaman(i);
				//					lp = locAmpCard.split("_");
				//					rackid = Integer.parseInt(lp[0].toString());
				//					sbrackid = Integer.parseInt(lp[1].toString());
				//					cardid = Integer.parseInt(lp[2].toString());
				//				}
				//				
				//				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, AmplifierCardtype,
				//						dir[i - 1], 0, 0, AmplifierEqid, "",
				//						ResourcePlanConstants.ZERO, "");

				//				String locPaBa = rpu.fgetLocationPaBa(i);
				//				String[] lp = locPaBa.split("_");
				//				rackid = Integer.parseInt(lp[0].toString());
				//				sbrackid = Integer.parseInt(lp[1].toString());
				//				cardid = Integer.parseInt(lp[2].toString());

				//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa, dir[i - 1],
				//						0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO,
				//						"");

				String locWss = rpu.fgetLocationWss(i);
				String[] lw = locWss.split("_");
				rackid = Integer.parseInt(lw[0].toString());
				sbrackid = Integer.parseInt(lw[1].toString());
				cardid = Integer.parseInt(lw[2].toString());
				// get the preferred wss type and eid
				// Object wss[]=rpu.fgetWssPreference(networkid, nodeid, nodedegree);

				String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype.toString().trim());
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, Wsscardtype, dir[i - 1], 0, 0, WssEqid, "",
						ResourcePlanConstants.ZERO, "");
			}

			/** Commented due to generation after all the card assignment */
			// rackid = ResourcePlanConstants.ONE;

			// // assing mux demux for east in rack 1
			// fSaveCardInDb(networkid, nodeid, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.EAST,
			// 		0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
			// 		ResourcePlanConstants.ZERO, "");

			// fSaveCardInDb(networkid, nodeid, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST,
			// 		0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
			// 		ResourcePlanConstants.ZERO, "");

			// // assing mux demux for West in rack 2
			// rackid = ResourcePlanConstants.TWO;

			// fSaveCardInDb(networkid, nodeid, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.WEST,
			// 		0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
			// 		ResourcePlanConstants.ZERO, "");

			// fSaveCardInDb(networkid, nodeid, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST,
			// 		0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
			// 		ResourcePlanConstants.ZERO, "");

		}
		break;
		case MapConstants.cdRoadm: {
			/* assuming cdc architecture */
			//			rackid = ResourcePlanConstants.ONE;
			//			sbrackid = ResourcePlanConstants.TWO;
			//
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
			//					ResourcePlanConstants.CardCscc, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc),
			//					"", ResourcePlanConstants.ZERO, "");
			//			if (CsccRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
			//				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.NINE,
			//						ResourcePlanConstants.CardCscc, "", 0, 0,
			//						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardCscc), "", ResourcePlanConstants.ZERO, "");
			//
			//			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SEVEN,
			//					ResourcePlanConstants.CardSupy, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),
			//					"", ResourcePlanConstants.ZERO, "");

			//			card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardVoip, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardVoip),
			//					"", ResourcePlanConstants.ZERO, "");

			//			card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FOUR,
			//					ResourcePlanConstants.CardReserved, "", 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved),
			//					"", ResourcePlanConstants.ZERO, "");
			//			
			//			if (card != null)
			//				cardNotAllocated.add(card);

			ramanLinksCount=dbService.getLinkService().FindAllRamanLinks(networkid, nodeid).size();
			System.out.println("Raman Links Count :: "+ramanLinksCount+" nodedegree :: "+nodedegree+" NodeId :: "+nodeid);
			boolean OnlyRamanFlag=true;
			if(nodedegree!=ramanLinksCount) {
				OnlyRamanFlag=false;
				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.FIVE,
						ResourcePlanConstants.CardOcm1x8, MapConstants.EAST, 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8), "", ResourcePlanConstants.ZERO, "");
				if (card != null)
					cardNotAllocated.add(card);
			}

			//If there are demands associated with this , then only add mcs
			//			if(dbService.getDemandService().FindAllByNodeId(networkid, nodeid).size()>0){
			// If there are links other than RAMAN link, then add first edfa array
			if(ramanLinksCount!=nodedegree ) {
				card = fSaveCardInDb(networkid, nodeid, rackid,sbrackid,ResourcePlanConstants.TEN,ResourcePlanConstants.CardEdfa,"11",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa),"",ResourcePlanConstants.ZERO,"");
				if(card!=null)
					cardNotAllocated.add(card);					
			}

			// Add second EDFA card if there are RAMAN links or nodedegree >4
			if(ramanLinksCount>0 | nodedegree>ResourcePlanConstants.FOUR)
			{
				int edfaSbrack=ResourcePlanConstants.THREE;
				if(OnlyRamanFlag){
					edfaSbrack=ResourcePlanConstants.TWO;
				}
				card = fSaveCardInDb(networkid, nodeid, rackid,edfaSbrack,ResourcePlanConstants.TEN,ResourcePlanConstants.CardEdfa,"12",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa),"",ResourcePlanConstants.ZERO,"");
				if(card!=null)
					cardNotAllocated.add(card);
			}
			//			}

			if(ramanLinksCount>0 | nodedegree>ResourcePlanConstants.FOUR)
			{		

				//				fSaveCardInDb(networkid, nodeid, rackid,sbrackid,ResourcePlanConstants.EIGHT,ResourcePlanConstants.CardSupy,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardSupy),"",ResourcePlanConstants.ZERO,"");

				//				card = fSaveCardInDb(networkid, nodeid, rackid,sbrackid+1,ResourcePlanConstants.TEN,ResourcePlanConstants.CardEdfa,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa),"",ResourcePlanConstants.ZERO,"");
				//				if(card!=null)
				//					 cardNotAllocated.add(card);

				String direction=MapConstants.NE;
				int sbRackLocation=sbrackid+1;
				// If only raman links are there , then this is OcmId=1 card
				if(OnlyRamanFlag) {
					direction=MapConstants.EAST;
					sbRackLocation=sbrackid;
				}
				card= fSaveCardInDb(networkid, nodeid, rackid,sbRackLocation,ResourcePlanConstants.FIVE,ResourcePlanConstants.CardOcm1x8,direction,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8),"",ResourcePlanConstants.ZERO,"");
				if(card!=null)
					cardNotAllocated.add(card);
			} 

			//			else {
			//				if(PrefChassisType.equals(ResourcePlanConstants.EmersionChassis) ) {
			//					fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.EIGHT,
			//							ResourcePlanConstants.CardReserved, "", 0, 0,
			//							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardReserved), "", ResourcePlanConstants.ZERO,
			//							"");
			//				}				
			//			}		


			for (int i = 1; i <= nodedegree; i++) {

				//				String AmplifierCardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatAmplifier,
				//						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				//				int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());
				//
				//				String locAmpCard;
				//				String []lp;
				//				if(AmplifierCardtype.equals(ResourcePlanConstants.CardPaBa)){
				//					locAmpCard = rpu.fgetLocationPaBa(i);
				//					lp = locAmpCard.split("_");
				//					rackid = Integer.parseInt(lp[0].toString());
				//					sbrackid = Integer.parseInt(lp[1].toString());
				//					cardid = Integer.parseInt(lp[2].toString());
				//				}
				//				else
				//				{
				//					locAmpCard = rpu.fgetLocationRaman(i);
				//					lp = locAmpCard.split("_");
				//					rackid = Integer.parseInt(lp[0].toString());
				//					sbrackid = Integer.parseInt(lp[1].toString());
				//					cardid = Integer.parseInt(lp[2].toString());
				//				}
				//				
				//				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, AmplifierCardtype,
				//						dir[i - 1], 0, 0, AmplifierEqid, "",
				//						ResourcePlanConstants.ZERO, "");
				//				if (card != null)
				//					cardNotAllocated.add(card);


				//				String locPaBa = rpu.fgetLocationPaBa(i);
				//				String[] lp = locPaBa.split("_");
				//				rackid = Integer.parseInt(lp[0].toString());
				//				sbrackid = Integer.parseInt(lp[1].toString());
				//				cardid = Integer.parseInt(lp[2].toString());

				//				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa,
				//						dir[i - 1], 0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "",
				//						ResourcePlanConstants.ZERO, "");


				String locWss = rpu.fgetLocationWss(i);
				String[] lw = locWss.split("_");
				rackid = Integer.parseInt(lw[0].toString());
				sbrackid = Integer.parseInt(lw[1].toString());
				cardid = Integer.parseInt(lw[2].toString());
				// get the preferred wss type and eid
				// Object wss[]=rpu.fgetWssPreference(networkid, nodeid, nodedegree);

				String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
						ResourcePlanConstants.ParamDirection, dir[i - 1])[0].toString();
				int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype.toString().trim());
				int slotSize=dbService.getEquipmentService().FindSlotSize(WssEqid);

				/* If raman is added as Amplifier card*/
				String locAmp = rpu.fgetLocationPaBa(i);
				String[] la = locAmp.split("_");
				int rackidAmp = Integer.parseInt(la[0].toString()),
						sbrackidAmp = Integer.parseInt(la[1].toString()),
						cardidAmp = Integer.parseInt(la[2].toString());
				CardInfo ampCard=dbService.getCardInfoService().FindCard(networkid, nodeid, rackidAmp, sbrackidAmp, cardidAmp);

				if(ampCard.getCardType().contains("RAMAN"))
				{
					cardid++;
				}

				card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, Wsscardtype, dir[i - 1], 0, 0,
						WssEqid, "", ResourcePlanConstants.ZERO, "");
				if (card != null)
					cardNotAllocated.add(card);
			}

		}
		break;
		}
		return cardNotAllocated;
	}

	/**
	 * @brief Assigns the direction based common cards in NE like PA BA, WSS etc and
	 *        stores the cards in db
	 * @param networkid
	 * @param nodeid
	 * @param nodetype
	 * @param nodedegree
	 * @param direction
	 * @throws SQLException 
	 */
	public void fAssignCommonCardsPerDirectionDb(int networkid, int nodeid, int nodetype, int nodedegree,
			String direction) throws SQLException {
		logger.info("fAssignCommonCardsPerDirectionDb");
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		switch (nodetype) {
		case MapConstants.roadm:
		case MapConstants.cdRoadm:{

			//			String AmplifierCardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatAmplifier,
			//					ResourcePlanConstants.ParamDirection, direction)[0].toString();
			//			int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());
			//			int ampslotsize=dbService.getEquipmentService().FindSlotSize(AmplifierEqid);
			//			
			//			// get free location for Amplifier
			//			String location[];
			//			location = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype, nodedegree,
			//					ampslotsize, ResourcePlanConstants.No);
			//						
			//			if (location[0] != null) {
			//				int[] id = rpu.locationIds(location[0]);
			////				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardPaBa, direction, 0, 0,
			////				rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO, "");
			//				
			//				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], AmplifierCardtype, direction, 0, 0,
			//						AmplifierEqid, "", ResourcePlanConstants.ZERO, "");
			//			}

			// get the preferred wss type and eid
			//			Object wss[] = rpu.fgetWssPreference(networkid, nodeid, nodedegree);			
			String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
					ResourcePlanConstants.ParamDirection, direction)[0].toString();
			int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype.toString().trim());

			int slotsize = dbService.getEquipmentService().FindSlotSize(WssEqid);

			// get free location for WSS
			if(slotsize==ResourcePlanConstants.TWO) {
				String location = rpu.fGetFirstFreeDoubleSlotDb(networkid, nodeid);
				int[] id = rpu.locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], Wsscardtype, direction, 0, 0,
						WssEqid, "", ResourcePlanConstants.ZERO, "");
			}else {
				String location = rpu.fGetFirstFreeSlotDb(networkid, nodeid);
				int[] id = rpu.locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], Wsscardtype, direction, 0, 0,
						WssEqid, "", ResourcePlanConstants.ZERO, "");
			}					
		}
		break;
		case MapConstants.ila: {

		}
		break;

		case MapConstants.te: {

		}
		break;

		case MapConstants.twoBselectRoadm: {

			String AmplifierCardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatAmplifier,
					ResourcePlanConstants.ParamDirection, direction)[0].toString();
			int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());
			int ampslotsize=dbService.getEquipmentService().FindSlotSize(AmplifierEqid);

			// get free location for Amplifier
			String location[];
			location = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype, nodedegree,
					ampslotsize, ResourcePlanConstants.No);

			if (location[0] != null) {
				int[] id = rpu.locationIds(location[0]);
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], AmplifierCardtype, direction, 0, 0,
						AmplifierEqid, "", ResourcePlanConstants.ZERO, "");
			}

			// get the preferred wss type and eid	
			String Wsscardtype = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
					ResourcePlanConstants.ParamDirection, direction)[0].toString();
			int WssEqid = rpu.fgetEIdFromCardType(Wsscardtype.toString().trim());

			int slotsize = dbService.getEquipmentService().FindSlotSize(WssEqid);

			// get free location for WSS
			String[] locationWss = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype, nodedegree, slotsize,
					ResourcePlanConstants.No);
			if (locationWss[0] != null) {
				int[] id = rpu.locationIds(locationWss[0]);				
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], Wsscardtype, direction, 0, 0,
						WssEqid, "", ResourcePlanConstants.ZERO, "");
			}


			//			// get free location for PA BA
			//			String[] location = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype, nodedegree,
			//					ResourcePlanConstants.SlotSizeOne, ResourcePlanConstants.No);
			//
			//			if (location[0] != null) {
			//				int[] id = rpu.locationIds(location[0]);
			//				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardPaBa, direction, 0, 0,
			//						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardPaBa), "", ResourcePlanConstants.ZERO, "");
			//			}
			//
			//			// get the preferred wss type and eid
			//			Object wss[] = rpu.fgetWssPreference(networkid, nodeid, nodedegree);
			//			int slotsize = dbService.getEquipmentService().FindSlotSize(Integer.parseInt(wss[1].toString()));
			//			// get free location for WSS
			//			String[] locationWss = rpu.fGetFirstFreeMpnSlotDb(networkid, nodeid, nodetype, nodedegree, slotsize,
			//					ResourcePlanConstants.No);
			//			if (locationWss[0] != null) {
			//				int[] id = rpu.locationIds(locationWss[0]);
			//				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], wss[0].toString(), direction, 0, 0,
			//						Integer.parseInt(wss[1].toString()), "", ResourcePlanConstants.ZERO, "");
			//			}
		}
		break;

		case MapConstants.hub: {
		}
		break;
		}
	}

	/**
	 * @brief Removes the direction based common cards in NE like PA BA, WSS etc
	 *        from the db
	 * @param networkid
	 * @param nodeid
	 * @param nodetype
	 * @param nodedegree
	 * @param direction
	 */
	public void fRemoveCommonCardsPerDirectionDb(int networkid, int nodeid, int nodetype, int nodedegree,
			String direction) {
		MainMap.logger.info("fAssignCommonCardsPerDirectionDb");

		switch (nodetype) {
		case MapConstants.roadm: {

			CardInfo paba = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardPaBa,
					direction);
			if(paba!=null)
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, paba.getRack(), paba.getSbrack(),
						paba.getCard());

			CardInfo ramanHybrid = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardRamanHybrid,
					direction);
			if(ramanHybrid!=null)
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, ramanHybrid.getRack(), ramanHybrid.getSbrack(),
						ramanHybrid.getCard());

			CardInfo ramanDra = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardRamanDra,
					direction);
			if(ramanDra!=null)
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, ramanDra.getRack(), ramanDra.getSbrack(),
						ramanDra.getCard());

			CardInfo cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, direction);
			dbService.getCardInfoService().DeleteCard(networkid, nodeid, cwss.getRack(), cwss.getSbrack(),
					cwss.getCard());

			int networkidGF = Integer
					.parseInt(dbService.getNetworkService().GetGreenFieldNetworkId(networkid).toString());
			int degreeBF = dbService.getNodeService().FindNode(networkid, nodeid).getDegree();
			int degreeGF = dbService.getNodeService().FindNode(networkidGF, nodeid).getDegree();
			if ((degreeBF == 4) && (degreeGF == 5))// transition for 5 to 4 in brown field
			{
				// remove the edfa card
				List<McsMap> mcs = dbService.getMcsMapService().FindByCommonPort(networkidGF, nodeid, direction);
				int id[] = rpu.locationIds(mcs.get(0).getEdfaLoc());
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, id[0], id[1], id[2]);
			}
		}
		break;
		case MapConstants.ila: {

		}
		break;

		case MapConstants.te: {

		}
		break;

		case MapConstants.twoBselectRoadm: {
			CardInfo paba = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardPaBa,
					direction);
			if(paba!=null)
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, paba.getRack(), paba.getSbrack(),
						paba.getCard());

			CardInfo ramanHybrid = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardRamanHybrid,
					direction);
			if(ramanHybrid!=null)
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, ramanHybrid.getRack(), ramanHybrid.getSbrack(),
						ramanHybrid.getCard());

			CardInfo ramanDra = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardRamanDra,
					direction);
			if(ramanDra!=null)
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, ramanDra.getRack(), ramanDra.getSbrack(),
						ramanDra.getCard());

			CardInfo cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, direction);
			dbService.getCardInfoService().DeleteCard(networkid, nodeid, cwss.getRack(), cwss.getSbrack(),
					cwss.getCard());

		}
		break;

		case MapConstants.hub: {
		}
		break;

		case MapConstants.cdRoadm: {

			CardInfo paba = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardPaBa,
					direction);
			if(paba!=null)
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, paba.getRack(), paba.getSbrack(),
						paba.getCard());

			CardInfo ramanHybrid = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardRamanHybrid,
					direction);
			if(ramanHybrid!=null)
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, ramanHybrid.getRack(), ramanHybrid.getSbrack(),
						ramanHybrid.getCard());

			CardInfo ramanDra = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardRamanDra,
					direction);
			if(ramanDra!=null)
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, ramanDra.getRack(), ramanDra.getSbrack(),
						ramanDra.getCard());

			CardInfo cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, direction);
			dbService.getCardInfoService().DeleteCard(networkid, nodeid, cwss.getRack(), cwss.getSbrack(),
					cwss.getCard());

			int networkidGF = Integer
					.parseInt(dbService.getNetworkService().GetGreenFieldNetworkId(networkid).toString());
			int degreeBF = dbService.getNodeService().FindNode(networkid, nodeid).getDegree();
			int degreeGF = dbService.getNodeService().FindNode(networkidGF, nodeid).getDegree();
			if ((degreeBF == 4) && (degreeGF == 5))// transition for 5 to 4 in brown field
			{
				// remove the edfa card
				List<WssMap> wssSet = dbService.getWssMapService().FindByCommonPort(networkidGF, nodeid, direction);
				int id[] = rpu.locationIds(wssSet.get(0).getEdfaLoc());
				dbService.getCardInfoService().DeleteCard(networkid, nodeid, id[0], id[1], id[2]);
			}
		}
		break;
		}
	}

	public void fCreateCardnPortTable(int networkid, int nodeid) {
		/* Create table for cards in a node in db */
		/// logger.info(tag+"ResourcePlanning creating tables for card()");
		String nodekey = "" + networkid + "_" + nodeid;

		dbService.getCardInfoService().CreateCardInfoTable(nodekey);				
		dbService.getPortInfoService().CreatePortInfoTable(nodekey);
	}

	public void fStoreAllNodeCardsInDb(int networkid, NodeSystem node) {
		/// logger.info(tag+"ResourcePlanning.fStoreAllNodeCardsInDb()");
		/* Store the card info in the db */
		Set keys = node.getRack().keySet();
		for (java.util.Iterator iterator = keys.iterator(); iterator.hasNext();) {
			Integer r = (Integer) iterator.next();
			Set keyssb = node.getRack().get(r).getSbrack().keySet();
			for (java.util.Iterator iterator2 = keyssb.iterator(); iterator2.hasNext();) {
				Integer s = (Integer) iterator2.next();
				Set keyscard = node.getRack().get(r).getSbrack().get(s).getSlots().keySet();
				for (java.util.Iterator iterator3 = keyscard.iterator(); iterator3.hasNext();) {
					Integer c = (Integer) iterator3.next();
					/// logger.info(tag+" Rack: "+r+" Sbrack: "+s+" Card: "+c +"type:
					/// "+node.getRack().get(r).getSbrack().get(s).getSlots().get(c).getType());

					//					String nodekey = "" + networkid + "_" + node.getId();
					CardInfo card = new CardInfo(networkid,node.getId(), r, s, c,
							node.getRack().get(r).getSbrack().get(s).getSlots().get(c).getType(), "", 0, 0, 0, "",
							ResourcePlanConstants.ZERO, "");
					try {
						dbService.getCardInfoService().Insert(card);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public CardInfo fSaveCardInDb(int networkid, int nodeid, int rackid, int sbrackid, int cardid, String cardtype,
			String direction, int wavelength, int demandid, int Eid, String Status, int CircuitId, String SerialNo) {
		// logger.info(tag+"ResourcePlanning.fSaveCardInDb()");
		//		String nodekey = "" + networkid + "_" + nodeid;
		boolean freeSlotFlag;
		CardInfo card = new CardInfo(networkid,nodeid, rackid, sbrackid, cardid, cardtype, direction, wavelength, demandid, Eid,
				Status, CircuitId, SerialNo);

		Equipment eq = dbService.getEquipmentService().findEquipmentById(card.getEquipmentId());
		int slotSize=eq.getSlotSize();
		if(slotSize==ResourcePlanConstants.SlotSizeTwo)
		{
			freeSlotFlag=rpu.fCheckDouibleSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid);
		}
		else {
			freeSlotFlag=rpu.fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid);
		}

		if (freeSlotFlag) {

			try {
				dbService.getCardInfoService().Insert(card);
				card = null;

			} catch (SQLException e) {
				logger.info("ResourcePlanning.fSaveCardInDb()" + e.getMessage());
				e.printStackTrace();
			}
		}

		return card;
	}


	public CardInfo fSaveTrayInDb(int networkid, int nodeid, int rackid, int sbrackid, int cardid, String cardtype,
			String direction, int wavelength, int demandid, int Eid, String Status, int CircuitId, String SerialNo) {		

		//		String nodekey = "" + networkid + "_" + nodeid;		

		CardInfo card = new CardInfo( networkid,nodeid,rackid, sbrackid, cardid, cardtype, direction, wavelength, demandid, Eid,
				Status, CircuitId, SerialNo);


		try {
			dbService.getCardInfoService().Insert(card);
			card = null;

		} catch (SQLException e) {
			logger.info("ResourcePlanning.fSaveCardInDb()" + e.getMessage());
			e.printStackTrace();
		}


		return card;
	}

	public void fSavePortOnCardInDb(int networkid, int nodeid, int rackid, int sbrackid, int cardid, String cardtype,
			int Port, int LinePort, String dir, int demandid, int Eid, int CircuitId,int MpnPortNo) {
		// logger.info(tag+"ResourcePlanning.fSaveCardInDb()");
		//		String nodekey = "" + networkid + "_" + nodeid;
		PortInfo port = new PortInfo(networkid,nodeid, rackid, sbrackid, cardid, cardtype, Port, LinePort, Eid, CircuitId,
				demandid, dir,MpnPortNo);
		try {
			dbService.getPortInfoService().Insert(port);
		} catch (SQLException e) {
			logger.info("ResourcePlanning.fSavePortOnCardInDb()" + e.getMessage());
			e.printStackTrace();
		}

	}

	/*
	 * Function assigns the card from the particular nodepool on the basis of card
	 * type entered
	 */
	public NodeSystem fAssignCard(NodeSystem node, int rackid, int sbrackid, int cardid, String cardtype) {
		switch (cardtype) {
		case ResourcePlanConstants.CardCscc: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardCscc);
		}
		break;
		case ResourcePlanConstants.CardSupy: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardSupy);
		}
		break;
		case ResourcePlanConstants.CardPaBa: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardPaBa);
		}
		break;
		case ResourcePlanConstants.CardVoip: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardVoip);
		}
		break;
		case ResourcePlanConstants.CardOcm1x2: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardOcm1x2);
		}
		break;
		case ResourcePlanConstants.CardOcm1x8: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardOcm1x8);
		}
		break;
		case ResourcePlanConstants.CardOcm1x16: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardOcm1x16);
		}
		break;
		case ResourcePlanConstants.CardEdfa: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardEdfa);
			node = assignCard(node, rackid, sbrackid, cardid + 1, ResourcePlanConstants.CardDummy);
		}
		break;
		case ResourcePlanConstants.CardMcs: {
			/* mcs is a two slot cardid add a dummy cardid in the slot next to it */
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardMcs);
			node = assignCard(node, rackid, sbrackid, cardid + 1, ResourcePlanConstants.CardDummy);
		}
		break;
		case ResourcePlanConstants.CardWss1x9: {
			/* wss1x9 is a two slot cardid add a dummy cardid in the slot next to it */
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardWss1x9);
			node = assignCard(node, rackid, sbrackid, cardid + 1, ResourcePlanConstants.CardDummy);
		}
		break;
		case ResourcePlanConstants.CardWss1x2: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardWss1x2);
		}
		break;
		case ResourcePlanConstants.CardWss1x2x20: {
			/* wss1x2x20 is a two slot cardid add a dummy cardid in the slot next to it */
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardWss1x2x20);
			node = assignCard(node, rackid, sbrackid, cardid + 1, ResourcePlanConstants.CardDummy);
		}
		break;
		case ResourcePlanConstants.CardMuxponderCGM: {
			/* wss1x9 is a two slot cardid add a dummy cardid in the slot next to it */
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardMuxponderCGM);
			node = assignCard(node, rackid, sbrackid, cardid + 1, ResourcePlanConstants.CardDummy);
		}
		break;
		case ResourcePlanConstants.CardMuxponderCGX: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardMuxponderCGX);
		}
		break;
		case ResourcePlanConstants.CardMuxponder10G: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardMuxponder10G);
		}
		break;
		case ResourcePlanConstants.CardTPN5x10G: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardTPN5x10G);
		}
		break;
		case ResourcePlanConstants.CardMuxponder200G: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardMuxponder200G);
		}
		break;
		case ResourcePlanConstants.CardOlp: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardOlp);
		}
		break;
		case ResourcePlanConstants.CardMpc: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.CardMpc);
		}
		break;
		case ResourcePlanConstants.Regenerator100G: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.Regenerator100G);
		}
		break;
		case ResourcePlanConstants.Tunable_Filter_Card: {
			node = assignCard(node, rackid, sbrackid, cardid, ResourcePlanConstants.Tunable_Filter_Card);
		}
		break;
		}
		return node;
	}

	/*
	 * Function removes the card from the particular nodepool on the basis of card
	 * type entered
	 */
	public NodeSystem fRemoveCard(NodeSystem node, int rackid, int sbrackid, int cardid, String cardtype) {
		switch (cardtype) {
		case ResourcePlanConstants.CardCscc: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardSupy: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardPaBa: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardOcm1x2:
		case ResourcePlanConstants.CardOcm1x8:
		case ResourcePlanConstants.CardOcm1x16: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardVoip: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardMcs: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid + 1);
		}
		break;
		case ResourcePlanConstants.CardEdfa: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid + 1);
		}
		break;
		case ResourcePlanConstants.CardWss1x9: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid + 1);
		}
		break;
		case ResourcePlanConstants.CardWss1x2x20: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid + 1);
		}
		break;
		case ResourcePlanConstants.CardWss1x2: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardMuxponderCGM: {
			/* wss1x9 is a two slot cardid add a dummy cardid in the slot next to it */
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid + 1);
		}
		break;
		case ResourcePlanConstants.CardMuxponderCGX: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardMuxponder200G: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardMuxponderOPX: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid + 1);
		}
		break;
		// case ResourcePlanConstants.CardTPN100G:
		case ResourcePlanConstants.CardTPN100GCGC: /****** TPN CGC in place of TPN100G:28/2/18 ****/
		{
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardMuxponder10G: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardTPN5x10G: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardOlp: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardMpc: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.Regenerator100G: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.Tunable_Filter_Card: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		case ResourcePlanConstants.CardReserved: {
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().remove(cardid);
		}
		break;
		}
		// if this sbrack has no more slots then remove it
		if (node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().isEmpty()) {
			node.getRack().get(rackid).getSbrack().remove(sbrackid);
		}
		// if this rack has no more subracks then remove it
		if (node.getRack().get(rackid).getSbrack().isEmpty()) {
			node.getRack().remove(rackid);
		}
		return node;
	}

	public NodeSystem assignCard(NodeSystem node, int rackid, int sbrackid, int cardid, String cardtype) {
		Rack r = new Rack(rackid);
		Sbrack sbr = new Sbrack(sbrackid);
		Card c = new Card(cardid, cardtype);

		if (node.getRack().containsKey(rackid)) {
			if (node.getRack().get(rackid).getSbrack().containsKey(sbrackid)) {

				node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().put(c.getId(), c);
			} else {
				node.getRack().get(rackid).getSbrack().put(sbrackid, sbr);
				node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().put(c.getId(), c);
			}
		} else {
			node.getRack().put(rackid, r);
			node.getRack().get(rackid).getSbrack().put(sbrackid, sbr);
			node.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().put(c.getId(), c);
		}

		return node;
	}

	public Rack fgetRack(NodeSystem node, int rackid) {
		Rack r;
		if (node.getRack().containsKey(rackid)) {
			r = node.getRack().get(rackid);
		} else {
			r = null;
		}
		return r;
	}

	public Sbrack fgetSbrack(Rack r, int sbrackid) {
		Sbrack sbr = null;
		if (r != null) {
			if (r.getSbrack().containsKey(sbrackid)) {
				sbr = r.getSbrack().get(sbrackid);
			} else {
				sbr = null;
			}
		}

		return sbr;
	}

	public Card fgetCard(Sbrack sbr, int cardid) {
		Card c;
		if (sbr.getSlots().containsKey(cardid)) {
			c = sbr.getSlots().get(cardid);
		} else {
			c = null;
		}
		return c;
	}

	public boolean IsSlotFree(Sbrack sbr, int cardid) {
		boolean flag = false;
		if (sbr != null) {
			if (sbr.getSlots().containsKey(cardid)) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	public void fScanRoutes(int networkid) {
		/// logger.info(tag+"ResourcePlanning.fScanRoutes()");
		/// logger.info(tag+"Scanning the Network Routes with Network Id: "+networkid);
		List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List<String> paths = new ArrayList<String>();
		String[] nodeids;
		for (int i = 0; i < routesAll.size(); i++) {
			paths.add(i, routesAll.get(i).getPath());
			nodeids = paths.get(i).split(",");
			for (int j = 0; j < nodeids.length; j++) {
				/*
				 * allocate the cards only on the first and the last i.e. src and the
				 * destination sites only
				 */
				if ((j == 0) | (j == nodeids.length - 1)) {
					/// logger.info("card allocation node id "+
					/// Integer.parseInt(nodeids[j].toString()));
					// fAssigningCardsInNodes(networkid,Integer.parseInt(nodeids[j].toString()));
					int nodeid = Integer.parseInt(nodeids[j].toString());
					int nodetype = dbService.getNodeService()
							.FindNode(networkid, Integer.parseInt(nodeids[j].toString())).getNodeType();

				}
			}
		}
	}

	public int[] fGetFirstFreeSlotInNode(int networkid, int nodeid) {
		/* fetch the nodepool from map for that node */
		NodeSystem nodepool = poolmap.get(nodeid);

		/* get the first free ids from the respective pool */
		int rackid = nodepool.getRack().firstKey();
		int sbrackid = nodepool.getRack().get(rackid).getSbrack().firstKey();
		int cardid = nodepool.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().firstKey();

		int[] location = new int[3];
		location[0] = rackid;
		location[1] = sbrackid;
		location[2] = cardid;
		return location;
	}

	public String fGetFirstFreeSlot(int networkid, int nodeid) {
		/* fetch the nodepool from map for that node */
		NodeSystem nodepool = poolmap.get(nodeid);

		/* get the first free ids from the respective pool */
		int rackid = nodepool.getRack().firstKey();
		int sbrackid = nodepool.getRack().get(rackid).getSbrack().firstKey();
		int cardid = nodepool.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().firstKey();

		String location = "";
		location = rpu.locationStr(rackid, sbrackid, cardid);
		return location;
	}

	public void fAssigningCardsInNodes(int networkid, int nodeid) {
		/* fetch the nodepool from map for that node */
		NodeSystem nodepool = poolmap.get(nodeid);

		int[] location = fGetFirstFreeSlotInNode(networkid, nodeid);
		Card c = new Card(location[2], ResourcePlanConstants.CardMuxponderCGM);
		Sbrack sbr = new Sbrack(location[1]);
		Rack r = new Rack(location[0]);

		/* Assign the card */
		NodeSystem assignedSys;
		if (assignedmap.get(nodeid) != null) {
			/// logger.info("found in assigned map ()");
			assignedSys = assignedmap.get(nodeid);// new NodeSystem(nodeid);
		} else {
			/// logger.info("found not in assigned map ()");
			assignedSys = new NodeSystem(nodeid);
		}

		/* Create table for the node in db */
		//		String nodekey = "" + networkid + "_" + nodeid;
		//		dbService.getCardInfoService().CreateCardInfoTable(nodekey);
		//		dbService.getPortInfoService().CreatePortInfoTable(nodekey);

		/// logger.info("The Received Assigned System : "+ assignedSys.toString());
		/// logger.info("Node Id :"+nodeid+" assigning now :"+r.getId()+"
		/// :"+sbr.getId()+" :"+c.getId());

		if (assignedSys.getRack().containsKey(r.getId())) {
			if (assignedSys.getRack().get(r.getId()).getSbrack().containsKey(sbr.getId())) {
				assignedSys.getRack().get(r.getId()).getSbrack().get(sbr.getId()).getSlots().put(c.getId(), c);
			} else {
				assignedSys.getRack().get(r.getId()).getSbrack().put(sbr.getId(), sbr);
				assignedSys.getRack().get(r.getId()).getSbrack().get(sbr.getId()).getSlots().put(c.getId(), c);
			}
		} else {
			assignedSys.getRack().put(r.getId(), r);
			assignedSys.getRack().get(r.getId()).getSbrack().put(sbr.getId(), sbr);
			assignedSys.getRack().get(r.getId()).getSbrack().get(sbr.getId()).getSlots().put(c.getId(), c);
		}

		/// logger.info("The Output Assigned System : "+ assignedSys.toString());
		assignedmap.remove(nodeid);
		assignedmap.put(nodeid, assignedSys);

		/* remove the card from the node pool */
		nodepool.getRack().get(r.getId()).getSbrack().get(sbr.getId()).getSlots().remove(c.getId());
		poolmap.remove(nodeid);

		/* place the updated card and nodepool in the poolmap */
		poolmap.put(nodeid, nodepool);

		/*
		 * logger.info("The Assigned System : "+ assignedSys.toString());
		 * logger.info("The Current Pool : "+ poolmap.get(nodeid).toString());
		 * logger.info("The Assinged System Pool : "+
		 * assignedmap.get(nodeid).toString());
		 */
		/* Store the card info in the db */
		CardInfo card = new CardInfo(networkid,nodeid, r.getId(), sbr.getId(), c.getId(), c.getType(), "", 0, 0, 0, "",
				ResourcePlanConstants.ZERO, "");
		try {
			dbService.getCardInfoService().Insert(card);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public NodeSystem fInitiateSite(int nodeid, int nodetype) {
		/// logger.info("ResourcePlanning.fInitiateSite(): "+nodeid);
		NodeSystem sys;
		if (nodetype == MapConstants.roadm) {
			sys = fInitiateEmersonSystemPool(nodeid);
		} else {
			sys = fInitiateEmersonSystemPool(nodeid);
		}
		return sys;
	}

	public Card cardPool(String type, int id) {
		SortedMap<Integer, String> mapport = new TreeMap();
		if (type.equalsIgnoreCase(ResourcePlanConstants.CardMuxponderCGM)) {
			mapport.put(1, ResourcePlanConstants.Port);
			mapport.put(2, ResourcePlanConstants.Port);
			mapport.put(3, ResourcePlanConstants.Port);
			mapport.put(4, ResourcePlanConstants.Port);
			mapport.put(5, ResourcePlanConstants.Port);
			mapport.put(6, ResourcePlanConstants.Port);
			mapport.put(7, ResourcePlanConstants.Port);
			mapport.put(8, ResourcePlanConstants.Port);
			mapport.put(9, ResourcePlanConstants.Port);
			mapport.put(10, ResourcePlanConstants.Port);
		}
		Card card = new Card(id, type, mapport);
		return card;
	}

	public Sbrack sbrackPool(int id) {
		SortedMap<Integer, Card> mapsbr = new TreeMap<Integer, Card>();
		mapsbr.put(1, cardPool(ResourcePlanConstants.CardMuxponderCGM, 1));
		mapsbr.put(2, cardPool(ResourcePlanConstants.CardMuxponderCGM, 2));
		mapsbr.put(3, cardPool(ResourcePlanConstants.CardMuxponderCGM, 3));
		mapsbr.put(4, cardPool(ResourcePlanConstants.CardMuxponderCGM, 4));
		mapsbr.put(5, cardPool(ResourcePlanConstants.CardMuxponderCGM, 5));
		mapsbr.put(6, cardPool(ResourcePlanConstants.CardMuxponderCGM, 6));
		mapsbr.put(7, cardPool(ResourcePlanConstants.CardMuxponderCGM, 7));
		mapsbr.put(8, cardPool(ResourcePlanConstants.CardMuxponderCGM, 8));
		mapsbr.put(9, cardPool(ResourcePlanConstants.CardMuxponderCGM, 9));
		mapsbr.put(10, cardPool(ResourcePlanConstants.CardMuxponderCGM, 10));
		mapsbr.put(11, cardPool(ResourcePlanConstants.CardMuxponderCGM, 11));
		mapsbr.put(12, cardPool(ResourcePlanConstants.CardMuxponderCGM, 12));
		mapsbr.put(13, cardPool(ResourcePlanConstants.CardMuxponderCGM, 13));
		mapsbr.put(14, cardPool(ResourcePlanConstants.CardMuxponderCGM, 14));
		Sbrack sbrack = new Sbrack(id, mapsbr);
		return sbrack;
	}

	public Rack rackPool(int id) {
		SortedMap<Integer, Sbrack> maprack = new TreeMap<Integer, Sbrack>();
		maprack.put(1, sbrackPool(1));
		maprack.put(2, sbrackPool(2));
		maprack.put(3, sbrackPool(3));
		Rack rack = new Rack(id, maprack);
		return rack;
	}

	public NodeSystem fInitiateEmersonSystemPool(int id) {
		SortedMap<Integer, Rack> maprack = new TreeMap<Integer, Rack>();
		maprack.put(1, rackPool(1));
		maprack.put(2, rackPool(2));
		maprack.put(3, rackPool(3));
		maprack.put(4, rackPool(4));
		maprack.put(5, rackPool(5));
		NodeSystem sys = new NodeSystem(id, maprack);
		return sys;
	}

	/**
	 * function generates the inventory i.e. no of components or cards required for
	 * a particular RWA output
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fGenerateInventoryold(int networkid) throws SQLException {
		/* assuming CDC architecture */
		// System.getProperty("user.dir")+"hhh";
		// System.getProperty("user.dir")+"/src/dwdm/lct/utility/db/";
		int nMpn = 0;
		int mCscc;
		List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List<String> paths = new ArrayList<String>();
		String[] nodeids;
		HashSet NodesToConfigure = new HashSet();

		for (int i = 0; i < routesAll.size(); i++) {
			paths.add(i, routesAll.get(i).getPath());
			nodeids = paths.get(i).split(",");
			for (int j = 0; j < nodeids.length; j++) {
				int nodeid = Integer.parseInt(nodeids[j].toString());
				NodesToConfigure.add(nodeid);
			}

			/* transponder will be assigned on the working and protection paths only */
			if ((routesAll.get(i).getRoutePriority() == ResourcePlanConstants.ONE)
					| (routesAll.get(i).getRoutePriority() == ResourcePlanConstants.TWO)) {
				paths.add(i, routesAll.get(i).getPath());
				nodeids = paths.get(i).split(",");
				for (int j = 0; j < nodeids.length; j++) {
					/*
					 * MPN cards are required only on the first and the last i.e. src and the
					 * destination sites only
					 */
					if ((j == 0) | (j == nodeids.length - 1)) {
						Inventory in = new Inventory(networkid, Integer.parseInt(nodeids[j].toString()),
								ResourcePlanConstants.EIdMpnCgm, 0);
						in = dbService.getInventoryService().FindInventory(networkid,
								Integer.parseInt(nodeids[j].toString()), ResourcePlanConstants.EIdMpnCgm);
						if (in != null) {
							double q = in.getQuantity() + 1;
							in.setQuantity(q);
							/// logger.info("ResourcePlanning.fGenerateInventory()"+"newtork id:
							/// "+networkid+" node: "+Integer.parseInt(nodeids[j].toString())+" eid:
							/// "+ResourcePlanConstants.EIdMpnCgm+" quantity: "+q);
							dbService.getInventoryService().UpdateInventory(networkid,
									Integer.parseInt(nodeids[j].toString()), ResourcePlanConstants.EIdMpnCgm, q);
						} else {
							Inventory inv = new Inventory(networkid, Integer.parseInt(nodeids[j].toString()),
									ResourcePlanConstants.EIdMpnCgm, 1);
							dbService.getInventoryService().InsertInventory(inv);
						}
					}
				}
			}
		}

		/*
		 * nodes which participate in the routes will need configuration and will be
		 * equipped
		 */
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.addAll(NodesToConfigure);

		for (int i = 0; i < list.size(); i++) {
			// add cards according to nodetype
			// insert ila and wss per node per degree
			Node n = dbService.getNodeService().FindNode(networkid, list.get(i));
			int degree = n.getDegree();

			// check for ila and non ila nodes
			if (dbService.getNodeService().FindNode(networkid, list.get(i)).getNodeType() == MapConstants.ila) {
				Inventory inIla = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdIlaCard, degree);
				dbService.getInventoryService().InsertInventory(inIla);

				// insert the ocm requirement per node
				Inventory inOcmila = new Inventory(networkid, list.get(i), ResourcePlanConstants.EId1x2_OPM_Card_2D_ILA,
						1);
				dbService.getInventoryService().InsertInventory(inOcmila);
			} else {
				// insert paba and wss per node per degree
				Inventory inWss = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdWss1x9, degree);
				dbService.getInventoryService().InsertInventory(inWss);

				Inventory inPaba = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdPaBa, degree);
				dbService.getInventoryService().InsertInventory(inPaba);

				// insert edfa
				if (degree > ResourcePlanConstants.FOUR) {
					Inventory inEdfa = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdEdfa, 2);
					dbService.getInventoryService().InsertInventory(inEdfa);

					Inventory inOcm = new Inventory(networkid, list.get(i),
							ResourcePlanConstants.EId1x8_OPM_Card_2D_TE_n_2D_ROADM, 2);
					dbService.getInventoryService().InsertInventory(inOcm);
				} else {
					Inventory inEdfa = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdEdfa, 1);
					dbService.getInventoryService().InsertInventory(inEdfa);

					Inventory inOcm = new Inventory(networkid, list.get(i),
							ResourcePlanConstants.EId1x8_OPM_Card_2D_TE_n_2D_ROADM, 1);
					dbService.getInventoryService().InsertInventory(inOcm);
				}

				// for ycable
				float cntYCabCircuits = dbService.getCircuitService().countCircuitsByClientProtPerNode(networkid,
						list.get(i), MapConstants.YCableProtection);
				float cntYCab = cntYCabCircuits / 10;

				/// logger.info("ResourcePlanning.fGenerateInventory()cntYCabCircuits
				/// "+cntYCabCircuits +" cntYCab "+cntYCab+" Math "+(double)Math.ceil(cntYCab));

				if (cntYCabCircuits != 0) {
					Inventory inYCableUnit = new Inventory(networkid, list.get(i),
							ResourcePlanConstants.EIdYCable1x2Unit, (double) Math.ceil(cntYCab));
					dbService.getInventoryService().InsertInventory(inYCableUnit);
				}

				// OLP card for client protection
				int cntOlpCircuits = dbService.getCircuitService().countCircuitsByClientProtPerNode(networkid,
						list.get(i), MapConstants.OLPProtection);
				int cntOlp = cntOlpCircuits;

				Inventory inOLP = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdOlp, cntOlp);
				dbService.getInventoryService().InsertInventory(inOLP);
			}

			// insert the supy requirement per node
			Inventory inSupy = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdSupy, 2);
			dbService.getInventoryService().InsertInventory(inSupy);

			// insert the mcs requirement per node
			Inventory inMcs = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdMcs, 1);
			dbService.getInventoryService().InsertInventory(inMcs);

		}

		// to be changed later on after card assignment
		for (int i = 0; i < list.size(); i++) {
			float slotreq = dbService.getInventoryService().FindSlotRequirement(networkid, list.get(i));
			float nSbracks = slotreq / 14;
			float nRacks = nSbracks / 3;
			Inventory inSbrack = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdAtcaChassis,
					Math.round(nSbracks));
			dbService.getInventoryService().InsertInventory(inSbrack);

			Inventory inPowUnit = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdPowerSupplyUnit, 1);
			dbService.getInventoryService().InsertInventory(inPowUnit);

			// controller card count will include the tpc and cscc
			Inventory inControllerCard = new Inventory(networkid, list.get(i), ResourcePlanConstants.EIdCscc,
					Math.round(nSbracks) * 2);
			dbService.getInventoryService().InsertInventory(inControllerCard);
		}
	}

	/**
	 * function generates the inventory i.e. no of components or cards required for
	 * a particular RWA output
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fGenerateInventory(int networkid) throws SQLException {
		// logger.info("Generating Inventory ");

		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);

		for (int i = 0; i < nodelist.size(); i++) {
			int nodeid = nodelist.get(i);
			/// logger.info("For NetworkId: "+ "NodeId: "+nodeid);
			List<Map<String, Object>> cards = dbService.getCardInfoService().CountCardByTypeNEId(networkid, nodeid);
			for (int j = 0; j < cards.size(); j++) {
				int eid = Integer.parseInt(cards.get(j).get("EquipmentId").toString());
				int quantity = Integer.parseInt(cards.get(j).get("CNT").toString());
				Inventory inv = new Inventory(networkid, nodeid, eid, quantity);
				dbService.getInventoryService().InsertInventory(inv);
			}

			// generate chassis inventory
			List<Map<String, Object>> sbracks=dbService.getCardInfoService().FindSbracks(networkid, nodeid);

			int sixSlotChassisCount=0,atcaChassis=0;

			for(int s=0;s<sbracks.size();s++) {

				int rackid=Integer.parseInt(sbracks.get(s).get("Rack").toString());
				int sbrackid=Integer.parseInt(sbracks.get(s).get("SbRack").toString());
				String PrefChassisType;
				try {
					PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
							ResourcePlanConstants.ParamSubrack, ""+rackid+"-"+sbrackid)[0].toString();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					PrefChassisType=ResourcePlanConstants.EmersionChassis;
				}

				if(PrefChassisType.equals(ResourcePlanConstants.SixSlotChassis)) {
					sixSlotChassisCount++;
				}else {
					atcaChassis++;
				}

			}

			if(sixSlotChassisCount>0)
				dbService.getInventoryService().UpdateInventory(networkid, nodeid, ResourcePlanConstants.EIdSixSlotChassis,
						sixSlotChassisCount);
			if(sixSlotChassisCount>0)
				dbService.getInventoryService().UpdateInventory(networkid, nodeid, ResourcePlanConstants.EIdAtcaChassis,
						atcaChassis);

			//				int sbcount = dbService.getCardInfoService().FindSbrackCount(networkid, nodeid);
			//				System.out.println("**********Sbrack Count*************"+sbcount);
			//				dbService.getInventoryService().UpdateInventory(networkid, nodeid, ResourcePlanConstants.EIdAtcaChassis,
			//						sbcount);	



			// generate power supply inventory
			Inventory inPowUnit = new Inventory(networkid, nodeid, ResourcePlanConstants.EIdPowerSupplyUnit, 1);
			dbService.getInventoryService().InsertInventory(inPowUnit);

			// generate y cable inventory
			float cntYCabCircuits = dbService.getCircuitService().countCircuitsByClientProtPerNode(networkid, nodeid,
					MapConstants.YCableProtection);
			float cntYCab = cntYCabCircuits / 10;

			/// logger.info("cntYCabCircuits "+cntYCabCircuits +" cntYCab "+cntYCab+" Math
			/// "+(double)Math.ceil(cntYCab));

			// if(cntYCabCircuits!=0)
			// {
			// Inventory inYCableUnit = new Inventory(networkid, nodeid,
			// ResourcePlanConstants.EIdYCable1x2Unit, (double)Math.ceil(cntYCab));
			// dbService.getInventoryService().InsertInventory(inYCableUnit);
			// }

			//			int q = dbService.getCardInfoService().FindSbrackCount(networkid, nodeid);
			//			Inventory inSbrack = new Inventory(networkid, nodeid, ResourcePlanConstants.EIdAtcaChassis, q);
			//			dbService.getInventoryService().InsertInventory(inSbrack);

			if(sixSlotChassisCount>0) {
				Inventory sixSlotSubrack = new Inventory(networkid, nodeid, ResourcePlanConstants.EIdSixSlotChassis, sixSlotChassisCount);
				dbService.getInventoryService().InsertInventory(sixSlotSubrack);
			}

			if(atcaChassis>0) {
				Inventory atcaSubrack = new Inventory(networkid, nodeid, ResourcePlanConstants.EIdAtcaChassis, atcaChassis);
				dbService.getInventoryService().InsertInventory(atcaSubrack);
			}

			int nodedegree = dbService.getNodeService().FindNode(networkid, nodeid).getDegree();
			int nodetype = dbService.getNodeService().FindNode(networkid, nodeid).getNodeType();
			if (nodetype == MapConstants.ila) {
				Inventory racks = new Inventory(networkid, nodeid, ResourcePlanConstants.EId_ILA_Rack, 1);
				dbService.getInventoryService().InsertInventory(racks);
			} else if (nodetype == MapConstants.te || nodetype == MapConstants.hub) {
				int racks = dbService.getCardInfoService().FindRacks(networkid, nodeid).size();
				Inventory mainrack = new Inventory(networkid, nodeid, ResourcePlanConstants.EId_TE_Main_Rack, 1);
				dbService.getInventoryService().InsertInventory(mainrack);

				if (racks > 1) {
					Inventory mpnracks = new Inventory(networkid, nodeid, ResourcePlanConstants.EId_Muxponder_Rack,
							racks - 1);
					dbService.getInventoryService().InsertInventory(mpnracks);
				}
			} else {
				int racks = dbService.getCardInfoService().FindRacks(networkid, nodeid).size();
				if (nodedegree > ResourcePlanConstants.TWO) {
					Inventory mainrack = new Inventory(networkid, nodeid,
							ResourcePlanConstants.EId_ROADM_Main_Rack_8Degree, 1);
					dbService.getInventoryService().InsertInventory(mainrack);
				} else {
					Inventory mainrack = new Inventory(networkid, nodeid,
							ResourcePlanConstants.EId_ROADM_Main_Rack_2Degree, 1);
					dbService.getInventoryService().InsertInventory(mainrack);
				}

				if (racks > 1) {
					Inventory mpnracks = new Inventory(networkid, nodeid, ResourcePlanConstants.EId_Muxponder_Rack,
							racks - 1);
					dbService.getInventoryService().InsertInventory(mpnracks);
				}
			}

			// patchcords inventory
			List<Map<String, Object>> patchcords = dbService.getPatchCordService().CountCordByTypeNEId(networkid,
					nodeid);
			for (int j = 0; j < patchcords.size(); j++) {
				int eid = Integer.parseInt(patchcords.get(j).get("EquipmentId").toString());
				// logger.info("ResourcePlanning.fGenerateInventory(): "+eid);

				int quantity = Integer.parseInt(patchcords.get(j).get("CNT").toString());
				System.out.println("ResourcePlanning.fGenerateInventory(): " + eid + " CNT:" + quantity);
				Inventory inv = new Inventory(networkid, nodeid, eid, quantity);
				System.out.println("ResourcePlanning.fGenerateInventory():1 ");
				dbService.getInventoryService().InsertInventory(inv);
				System.out.println("ResourcePlanning.fGenerateInventory(): 2");
			}

			// power cables
			Inventory inpowCab1 = new Inventory(networkid, nodeid,
					ResourcePlanConstants.EId_Power_Cable_Exchange1_TE_PSU_Red, 1);
			dbService.getInventoryService().InsertInventory(inpowCab1);
			Inventory inpowCab2 = new Inventory(networkid, nodeid,
					ResourcePlanConstants.EId_Power_Cable_Exchange2_TE_PSU_Red, 1);
			dbService.getInventoryService().InsertInventory(inpowCab2);
			Inventory inpowCab3 = new Inventory(networkid, nodeid,
					ResourcePlanConstants.EId_Power_Cable_Exchange1_TE_PSU_Blue, 1);
			dbService.getInventoryService().InsertInventory(inpowCab3);
			Inventory inpowCab4 = new Inventory(networkid, nodeid,
					ResourcePlanConstants.EId_Power_Cable_Exchange2_TE_PSU_Blue, 1);
			dbService.getInventoryService().InsertInventory(inpowCab4);
			Inventory inpowCab5 = new Inventory(networkid, nodeid,
					ResourcePlanConstants.EId_Power_Cable_Exchange1_TE_EarthBusBar_Black, 1);
			dbService.getInventoryService().InsertInventory(inpowCab5);
			// Inventory inpowCab6 = new Inventory(networkid, nodeid,
			// ResourcePlanConstants.EId_Power_Cable_Exchange2_TE_EarthBusBar_Black, 1);
			// dbService.getInventoryService().InsertInventory(inpowCab6);

			// SFPs inventory
			List<Map<String, Object>> ports = dbService.getPortInfoService().CountPortByTypeNEId(networkid, nodeid);
			for (int j = 0; j < ports.size(); j++) {
				int eid = Integer.parseInt(ports.get(j).get("EquipmentId").toString());
				if (eid != 0) {
					// logger.info("ResourcePlanning.fGenerateInventory(): "+eid);
					int quantity = Integer.parseInt(ports.get(j).get("CNT").toString());
					Inventory inv = new Inventory(networkid, nodeid, eid, quantity);
					dbService.getInventoryService().InsertInventory(inv);
				}
			}

		}
		logger.info("Generating Inventory Complete");

	}

	public void fGenerateEquipmentDb(Equipment[] equipmentModelObjectArray) throws SQLException {
		if (equipmentModelObjectArray != null) {
			for (int i = 0; i < equipmentModelObjectArray.length; i++) {
				// Equipment e1 = new Equipment(ResourcePlanConstants.EIdCscc,"cscc
				// name","abc",1,1,"details",1010,1,"01");
				String partNoStr = equipmentModelObjectArray[i].getPartNo();
				equipmentModelObjectArray[i].setEquipmentId(rpu.fgetEId(partNoStr));
				if(equipmentModelObjectArray[i].getEquipmentId()!=0)
					dbService.getEquipmentService().InsertEquipment(equipmentModelObjectArray[i]);
			}
		} else {
			// fill Dummy data

			logger.info("ResourcePlanning.fGenerateEquipmentDb() filling dummy data");
			Equipment e1 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartMpnCgm),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartMpnCgm)),
					ResourcePlanConstants.EPartMpnCgm, 190, 2, 0, "with 10 no. SFP+ Transceiver Client", 23880, 0,
					"NA");
			dbService.getEquipmentService().InsertEquipment(e1);
			Equipment e2 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartIlaCard),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartIlaCard)),
					ResourcePlanConstants.EPartIlaCard, 65, 1, 0, "2 no.of 1X8 OPM will be mounted in case of CDC node",
					5448, 0, "NA");
			dbService.getEquipmentService().InsertEquipment(e2);

			Equipment e3 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartPaBa),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartPaBa)),
					ResourcePlanConstants.EPartPaBa, 80, 1, 0, "2 no.of 1X8 OPM will be mounted in case of CDC node",
					8209, 0, "NA");
			dbService.getEquipmentService().InsertEquipment(e3);

			Equipment e4 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartOlp),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartOlp)),
					ResourcePlanConstants.EPartOlp, 55, 1, 0, "2 no.of 1X8 OPM will be mounted in case of CDC node",
					1194, 0, "NA");
			dbService.getEquipmentService().InsertEquipment(e4);
			Equipment e5 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart1x8_OPM_Card_2D_TE_n_2D_ROADM),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPart1x8_OPM_Card_2D_TE_n_2D_ROADM)),
					ResourcePlanConstants.EPart1x8_OPM_Card_2D_TE_n_2D_ROADM, 60, 1, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 3731, 0, "NA");
			dbService.getEquipmentService().InsertEquipment(e5);

			Equipment e6 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart1x2_OPM_Card_2D_ILA),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPart1x2_OPM_Card_2D_ILA)),
					ResourcePlanConstants.EPart1x2_OPM_Card_2D_ILA, 60, 1, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 3209, 0, "NA");
			dbService.getEquipmentService().InsertEquipment(e6);
			Equipment e7 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart1x2_WSS_Card_for_2D_TE_n_2D_ROADM),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPart1x2_WSS_Card_for_2D_TE_n_2D_ROADM)),
					ResourcePlanConstants.EPart1x2_WSS_Card_for_2D_TE_n_2D_ROADM, 70, 1, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 7463, 0, "NA");
			dbService.getEquipmentService().InsertEquipment(e7);
			Equipment e8 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartWss1x9),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartWss1x9)),
					ResourcePlanConstants.EPartWss1x9, 75, 2, 0, "2 no.of 1X8 OPM will be mounted in case of CDC node",
					12239, 0, "NA");
			dbService.getEquipmentService().InsertEquipment(e8);
			Equipment e9 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_Twin_1x20_WSS_Card_8D_CDC),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPart_Twin_1x20_WSS_Card_8D_CDC)),
					ResourcePlanConstants.EPart_Twin_1x20_WSS_Card_8D_CDC, 75, 2, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 12836, 0, "NA");
			dbService.getEquipmentService().InsertEquipment(e9);
			Equipment e10 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartMcs),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartMcs)),
					ResourcePlanConstants.EPartMcs, 90, 2, 0, "2 no.of 1X8 OPM will be mounted in case of CDC node",
					6716, 1, "NA");
			dbService.getEquipmentService().InsertEquipment(e10);
			Equipment e11 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartEdfa),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartEdfa)),
					ResourcePlanConstants.EPartEdfa, 70, 2, 0, "2 no.of 1X8 OPM will be mounted in case of CDC node",
					7463, 1, "NA");
			dbService.getEquipmentService().InsertEquipment(e11);
			Equipment e12 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartCscc),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartCscc)) + "_Dummy",
					ResourcePlanConstants.EPartCscc, 162, 1, 0, "2 no.of 1X8 OPM will be mounted in case of CDC node",
					1940, 1, "NA");
			dbService.getEquipmentService().InsertEquipment(e12);
			Equipment e13 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartSupy),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartSupy)),
					ResourcePlanConstants.EPartSupy, 30, 1, 0, "with 8 no. OSC SFP Transceiver", 1343, 1, "NA");
			dbService.getEquipmentService().InsertEquipment(e13);
			Equipment e14 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_40G_Muxponder_Card),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPart_40G_Muxponder_Card)),
					ResourcePlanConstants.EPart_40G_Muxponder_Card, 150, 2, 0, "with 4 no. SFP+ Transceiver", 14179, 1,
					"NA");
			dbService.getEquipmentService().InsertEquipment(e14);
			Equipment e15 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_100G_MUXPONDER_CARD_CGX),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPart_100G_MUXPONDER_CARD_CGX)),
					ResourcePlanConstants.EPart_100G_MUXPONDER_CARD_CGX, 190, 1, 0,
					"with 10 no. SFP+ Transceiver Client", 23881, 1, "NA");
			dbService.getEquipmentService().InsertEquipment(e15);
			Equipment e16 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartPowerSupplyUnit),
					"Optical Supervisory & Order wire Card", ResourcePlanConstants.EPartPowerSupplyUnit, 50, 2, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 1343, 1, "NA");
			dbService.getEquipmentService().InsertEquipment(e16);
			Equipment e17 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartAtcaChassis), "ATCA Chassis",
					ResourcePlanConstants.EPartAtcaChassis, 725, 1, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 4500, 1, "NA");
			dbService.getEquipmentService().InsertEquipment(e17);
			// 19 Equipment e18 = new
			// Equipment(rpu.fgetEId(ResourcePlanConstants.),rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPartPowerSupplyUnit)),ResourcePlanConstants.EPartPowerSupplyUnit,120,1,"with
			// 16 Client SFP(each $40) & Two tunable WDM sideTransceiver(each
			// $1025)",4925,2,"NA");
			// dbService.getEquipmentService().InsertEquipment(e18);
			Equipment e19 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_1x16_OTDR_8D_CDC),
					"1X16 OTDR card for 8D CDC node", ResourcePlanConstants.EPart_1x16_OTDR_8D_CDC, 70, 2, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 5970, 2, "NA");
			dbService.getEquipmentService().InsertEquipment(e19);
			Equipment e20 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_1x16_OPM_8D_CDC),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPart_1x16_OPM_8D_CDC)),
					ResourcePlanConstants.EPart_1x16_OPM_8D_CDC, 65, 2, 0, "will replace S.NO. 5 in CDC node", 4478, 2,
					"NA");
			dbService.getEquipmentService().InsertEquipment(e20);
			Equipment e21 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_Hybrid_Raman_n_EDFA_card_8D),
					rpu.fgetCardTypeFromEId(rpu.fgetEId(ResourcePlanConstants.EPart_Hybrid_Raman_n_EDFA_card_8D)),
					ResourcePlanConstants.EPart_Hybrid_Raman_n_EDFA_card_8D, 110, 2, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 14925, 2, "NA");
			dbService.getEquipmentService().InsertEquipment(e21);
			Equipment e22 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_Odd_Mux_Demux_Unit_2D_TE_n_ROADM),
					"Odd Mux/ Demux Unit for 2D TE & 2D ROADM",
					ResourcePlanConstants.EPart_Odd_Mux_Demux_Unit_2D_TE_n_ROADM, 0, 1, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 1444, 2, "NA");
			dbService.getEquipmentService().InsertEquipment(e22);
			Equipment e23 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_Even_Mux_Demux_Unit_2D_TE_n_ROADM),
					"Even Mux/Demux Unit for 2D TE & 2D ROADM",
					ResourcePlanConstants.EPart_Even_Mux_Demux_Unit_2D_TE_n_ROADM, 0, 1, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 2024, 2, "NA");
			dbService.getEquipmentService().InsertEquipment(e23);
			Equipment e24 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPartYCable1x2Unit),
					"1x2 Y Cable Unit for Client layer protection", ResourcePlanConstants.EPartYCable1x2Unit, 0, 1, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 624, 2, "NA");
			dbService.getEquipmentService().InsertEquipment(e24);
			Equipment e25 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_2x2_YCable_Unit),
					"2x2 Y Cable Unit for Client layer protection", ResourcePlanConstants.EPart_2x2_YCable_Unit, 0, 1,
					0, "2 no.of 1X8 OPM will be mounted in case of CDC node", 704, 2, "NA");
			dbService.getEquipmentService().InsertEquipment(e25);
			Equipment e26 = new Equipment(rpu.fgetEId(ResourcePlanConstants.EPart_DCM_Unit),
					"DCM Unit for 10G Transponder & Muxponder Line card", ResourcePlanConstants.EPart_DCM_Unit, 0, 1, 0,
					"2 no.of 1X8 OPM will be mounted in case of CDC node", 3951, 2, "NA");
			dbService.getEquipmentService().InsertEquipment(e26);
		}
	}

	public String getDirections(Topology t) {
		/// logger.info(tag+"ResourcePlanning.getDirections() "+t.toString());
		String dir = "";

		if (t.getDir1() != ResourcePlanConstants.ZERO) {
			dir = dir.concat(MapConstants.EAST).concat("-");
		}

		if (t.getDir2() != ResourcePlanConstants.ZERO) {
			dir = dir.concat(MapConstants.WEST).concat("-");
		}

		if (t.getDir3() != ResourcePlanConstants.ZERO) {
			dir = dir.concat(MapConstants.NORTH).concat("-");
		}

		if (t.getDir4() != ResourcePlanConstants.ZERO) {
			dir = dir.concat(MapConstants.SOUTH).concat("-");
		}

		if (t.getDir5() != ResourcePlanConstants.ZERO) {
			dir = dir.concat(MapConstants.NE).concat("-");
		}

		if (t.getDir6() != ResourcePlanConstants.ZERO) {
			dir = dir.concat(MapConstants.NW).concat("-");
		}

		if (t.getDir7() != ResourcePlanConstants.ZERO) {
			dir = dir.concat(MapConstants.SE).concat("-");
		}

		if (t.getDir8() != ResourcePlanConstants.ZERO) {
			dir = dir.concat(MapConstants.SW).concat("-");
		}
		/// logger.info(tag+" direction string :"+dir);
		return dir;
	}

	/*
	 * @brief Assigns the Muxponder controller cards(MPCs) in all nodes of a network
	 * and updates them in db. This is to be used after the MPNs have been assigned
	 */
	public void fAssignMPCs(int networkid) {
		int rackid, sbrackid;
		/// logger.info(tag+"ResourcePlanning.fAssignMPCs()");

		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		for (int i = 0; i < nodelist.size(); i++) {
			int nodeid = nodelist.get(i);
			/* fetch the nodepool from map for that node */
			NodeSystem nodepool = poolmap.get(nodeid);

			/* Get the assigned map */
			NodeSystem assignedSys;
			if (assignedmap.get(nodeid) != null) {
				/// logger.info("found in assigned map ()");
				assignedSys = assignedmap.get(nodeid);
			} else {
				/// logger.info("found not in assigned map ()");
				assignedSys = new NodeSystem(nodeid);
			}

			List<Map<String, Object>> sbrUsed = dbService.getCardInfoService().FindSbracks(networkid, nodeid);
			for (int j = 0; j < sbrUsed.size(); j++) {
				rackid = (int) sbrUsed.get(j).get("Rack");
				sbrackid = (int) sbrUsed.get(j).get("Sbrack");
				if (!((rackid == 1) & (sbrackid == 2)))// skip the main rack
				{
					if (fCheckSlotAvailability(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX)) {
						assignedSys = fAssignCard(assignedSys, rackid, sbrackid, ResourcePlanConstants.SIX,
								ResourcePlanConstants.CardMpc);
						fRemoveCard(nodepool, rackid, sbrackid, ResourcePlanConstants.SIX,
								ResourcePlanConstants.CardMpc);
						fSaveCardInDb(networkid, nodeid, rackid, sbrackid, ResourcePlanConstants.SIX,
								ResourcePlanConstants.CardMpc, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
								"");
					}

				}
			}
			poolmap.remove(nodeid);
			poolmap.put(nodeid, nodepool);

			assignedmap.remove(nodeid);
			assignedmap.put(nodeid, assignedSys);

		}
	}

	/*
	 * @brief Assigns the Muxponder controller cards(MPCs) in all nodes of a network
	 * and updates them in db. This is to be used after the MPNs have been assigned
	 */
	public void fAssignMPCsDb(int networkid) {
		int rackid, sbrackid;
		logger.info("fAssignMPCsDb()");

		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		for (int i = 0; i < nodelist.size(); i++) {
			int nodeid = nodelist.get(i);
			EquipmentPreferences eqp = new EquipmentPreferences(dbService);
			String MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);

			String PrefChassisType=ResourcePlanConstants.EmersionChassis;
			int[] location=new int[2];
			List<Map<String, Object>> sbrUsed = dbService.getCardInfoService().FindSbracks(networkid, nodeid);
			for (int j = 0; j < sbrUsed.size(); j++) {
				rackid = (int) sbrUsed.get(j).get("Rack");
				sbrackid = (int) sbrUsed.get(j).get("Sbrack");
				if (!((rackid == 1) & (sbrackid == 2)))// skip the main rack
				{
					try {
						PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
								ResourcePlanConstants.ParamSubrack, rpu.rackSubrackParamValStr(rackid, sbrackid))[0].toString();
						System.out.println(" Inside :: *************** PrefChassisType **********"+PrefChassisType);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Net  "+networkid +"Rackid "+rackid+ "  Rackid "+sbrackid);
					System.out.println("*************** PrefChassisType **********"+PrefChassisType);

					location=rpu.fGetControllerCardLocation(PrefChassisType);

					if (rpu.fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, location[0])) {
						fSaveCardInDb(networkid, nodeid, rackid, sbrackid, location[0],
								ResourcePlanConstants.CardMpc, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
								"");
					}

					if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
						if (rpu.fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid,location[1])) {
							fSaveCardInDb(networkid, nodeid, rackid, sbrackid,location[1],
									ResourcePlanConstants.CardMpc, "", 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "",
									ResourcePlanConstants.ZERO, "");
						}
					}

				}
			}
		}
	}

	/*
	 * @brief Assigns the Muxponder controller cards(MPCs) in a nodes of a network
	 * and updates them in db.
	 * 
	 */
	public void fAssignMPCsInANodeDb(int networkid, int nodeid) {
		int rackid, sbrackid;
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		String MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);
		logger.info("fAssignMPCsInANodeDb()");

		String PrefChassisType=ResourcePlanConstants.EmersionChassis;
		int[] location=new int[2];
		List<Map<String, Object>> sbrUsed = dbService.getCardInfoService().FindSbracks(networkid, nodeid);
		for (int j = 0; j < sbrUsed.size(); j++) {
			rackid = (int) sbrUsed.get(j).get("Rack");
			sbrackid = (int) sbrUsed.get(j).get("Sbrack");
			if (!((rackid == 1) & (sbrackid == 2)))// skip the main rack
			{
				try {
					PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
							ResourcePlanConstants.ParamSubrack, rpu.rackSubrackParamValStr(rackid, sbrackid))[0].toString();
					System.out.println(" Inside :: *************** PrefChassisType **********"+PrefChassisType);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				location=rpu.fGetControllerCardLocation(PrefChassisType);

				if (rpu.fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, location[0])) {
					fSaveCardInDb(networkid, nodeid, rackid, sbrackid, location[0],
							ResourcePlanConstants.CardMpc, "", 0, 0,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
							"");
				}

				if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
					if (rpu.fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid,location[1])) {
						fSaveCardInDb(networkid, nodeid, rackid, sbrackid,location[1],
								ResourcePlanConstants.CardMpc, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "",
								ResourcePlanConstants.ZERO, "");
					}
				}
			}
		}

	}

	/**
	 * Assigns the Muxponder controller cards(MPCs) in the addedall nodes of a
	 * network and updates them in db.
	 * 
	 * @param networkid
	 */
	public void fAssignMPCsBrField(int networkid) {
		int rackid, sbrackid;
		/// logger.info(tag+"ResourcePlanning.fAssignMPCs()");
		// get the brown field network id
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);

		// get the new added nodes in the Br Field
		// List <Node> newNodes =
		// dbService.getNodeService().FindAddedNodesInBrField(networkid, networkidBF);
		String PrefChassisType=ResourcePlanConstants.EmersionChassis;
		int[] location=new int[2];
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkidBF);
		for (int i = 0; i < nodelist.size(); i++) {
			int nodeid = nodelist.get(i);// .getNodeId();
			String MpcRedun = eqp.fgetRedundancyReq(networkidBF, nodeid, ResourcePlanConstants.CatMpc);
			List<Map<String, Object>> sbrUsed = dbService.getCardInfoService().FindSbracks(networkidBF, nodeid);
			for (int j = 0; j < sbrUsed.size(); j++) {
				rackid = (int) sbrUsed.get(j).get("Rack");
				sbrackid = (int) sbrUsed.get(j).get("Sbrack");
				if (!((rackid == 1) & (sbrackid == 2)))// skip the main rack
				{

					try {
						PrefChassisType = eqp.fGetPreferredEqType(networkidBF, nodeid, ResourcePlanConstants.CatChassisType,
								ResourcePlanConstants.ParamSubrack, rpu.rackSubrackParamValStr(rackid, sbrackid))[0].toString();
						System.out.println(" Inside :: *************** PrefChassisType **********"+PrefChassisType);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Net  "+networkid +"Rackid "+rackid+ "  Rackid "+sbrackid);
					System.out.println("*************** PrefChassisType **********"+PrefChassisType);
					location=rpu.fGetControllerCardLocation(PrefChassisType);
					if (rpu.fCheckSlotAvailabilityDb(networkidBF, nodeid, rackid, sbrackid,
							location[0])) {
						fSaveCardInDb(networkidBF, nodeid, rackid, sbrackid, location[0],
								ResourcePlanConstants.CardMpc, "", 0, 0,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO,
								"");
					}

					if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
						if (rpu.fCheckSlotAvailabilityDb(networkidBF, nodeid, rackid, sbrackid,
								location[1])) {
							fSaveCardInDb(networkidBF, nodeid, rackid, sbrackid,location[1],
									ResourcePlanConstants.CardMpc, "", 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "",
									ResourcePlanConstants.ZERO, "");
						}
					}
				}
			}
		}
	}

	public void fUpdateInventory(int networkid) throws SQLException {
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		for (int i = 0; i < nodelist.size(); i++) {
			int nodeid = nodelist.get(i);
			int sbcount = dbService.getCardInfoService().FindSbrackCount(networkid, nodeid);
			dbService.getInventoryService().UpdateInventory(networkid, nodeid, ResourcePlanConstants.EIdAtcaChassis,
					sbcount);
		}
	}

	/**
	 * function checks the availability of a particular slot in a particular rack
	 * sbrack of a node
	 * 
	 * @param networkid
	 * @param nodeid
	 * @param rackid
	 * @param sbrackid
	 * @param slotid
	 * @return
	 */
	public boolean fCheckSlotAvailability(int networkid, int nodeid, int rackid, int sbrackid, int slotid) {
		/// logger.info(tag+"fCheckSlotAvailability nodeid: "+nodeid+" rackid:
		/// "+rackid+" sbrackid: "+sbrackid+" slotid: "+slotid);
		boolean flag = true;
		NodeSystem nodepool = poolmap.get(nodeid);

		if (IsSlotFree(fgetSbrack(fgetRack(nodepool, rackid), sbrackid), slotid)) {
			flag = true;
		} else {
			flag = false;
		}
		/// logger.info(tag+"Slot availibility Status : "+flag);
		return flag;

	}

	/**
	 * function populates the card preference data for wss and mpn cards according
	 * to nodedegree
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fsetDefaultCardPreference(int networkid) throws SQLException {
		try {
			List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
			for (int i = 0; i < nodelist.size(); i++) {
				int nodeid = nodelist.get(i);
				int nodetype = dbService.getNodeService().FindNode(networkid, nodeid).getNodeType();
				int nodedegree = dbService.getNodeService().FindNode(networkid, nodeid).getDegree();
				// ask for preference

				if (nodetype != MapConstants.ila)/** For Non-ILA nodes */
				{
					// if(nodedegree>2)
					// {
					// CardPreference cp = new CardPreference(networkid, nodeid,
					// ResourcePlanConstants.CardFeatureWSS, ResourcePlanConstants.CardWss1x9,
					// rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x9),ResourcePlanConstants.ONE);
					// dbService.getCardPreferenceService().Insert(cp);
					// }
					// else
					// {
					// CardPreference cp = new CardPreference(networkid, nodeid,
					// ResourcePlanConstants.CardFeatureWSS, ResourcePlanConstants.CardWss1x2,
					// rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x2),ResourcePlanConstants.ONE);
					// dbService.getCardPreferenceService().Insert(cp);
					// }
					/** Insert Wss(based on node) and MPN(CGM) (for all) */

					if (nodetype == MapConstants.roadm || nodetype == MapConstants.cdRoadm) {
						CardPreference cp = new CardPreference(networkid, nodeid, ResourcePlanConstants.CardFeatureWSS,
								ResourcePlanConstants.CardWss1x9,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x9), ResourcePlanConstants.ONE);
						dbService.getCardPreferenceService().Insert(cp);
					} else if (nodetype == MapConstants.twoBselectRoadm) {
						CardPreference cp = new CardPreference(networkid, nodeid, ResourcePlanConstants.CardFeatureWSS,
								ResourcePlanConstants.CardWss1x2,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x2), ResourcePlanConstants.ONE);
						dbService.getCardPreferenceService().Insert(cp);
					} else if (nodetype == MapConstants.hub) {
						CardPreference cp = new CardPreference(networkid, nodeid, ResourcePlanConstants.CardFeatureWSS,
								ResourcePlanConstants.CardWss1x2,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x2), ResourcePlanConstants.ONE);
						dbService.getCardPreferenceService().Insert(cp);
					} else if (nodetype == MapConstants.te) {
						CardPreference cp = new CardPreference(networkid, nodeid, ResourcePlanConstants.CardFeatureWSS,
								ResourcePlanConstants.CardWss1x2,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x2), ResourcePlanConstants.ONE);
						dbService.getCardPreferenceService().Insert(cp);
					}

					// set mpn preference
					CardPreference cp = new CardPreference(networkid, nodeid, ResourcePlanConstants.CardFeatureMPN,
							ResourcePlanConstants.CardMuxponderCGM,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMuxponderCGM), ResourcePlanConstants.ONE);
					dbService.getCardPreferenceService().Insert(cp);
				}
			}
		} catch (Exception e) {
			logger.info("ResourcePlanning.fsetDefaultCardPreference()" + e);
		}

	}

	/**
	 * function populates the card preference data for brown field, assumes the same
	 * preferences as green field
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fsetDefaultCardPreferenceBrField(int networkid) throws SQLException {
		logger.info("fsetDefaultCardPreferenceBrField ");
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		try {
			// if(dbService.getCardPreferenceService().FindPreference(networkidBF)!=null)//if
			// data does not exist
			// {

			// for common nodes copy the data
			List<Node> commonNodes = dbService.getNodeService().FindCommonNodesInBrField(networkid, networkidBF);
			for (int i = 0; i < commonNodes.size(); i++) {
				int nodeid = commonNodes.get(i).getNodeId();
				List<CardPreference> li = dbService.getCardPreferenceService().FindPreferenceByNode(networkidBF,
						nodeid);
				if (li.isEmpty())// no preference exists
				{
					logger.info("ResourcePlanning.fsetDefaultCardPreferenceBrField() Data is null ");
					int nodetype = commonNodes.get(i).getNodeType();
					int nodedegree = commonNodes.get(i).getDegree();
					dbService.getCardPreferenceService().InsertCardPreferenceDataInBrField(networkid, nodeid,
							networkidBF);
				} else {
					logger.info("ResourcePlanning.fsetDefaultCardPreferenceBrField() Data found ");
				}
			}

			// for rest of the nodes
			List<Integer> nodelist = rpu.fgetNodesToConfigure(networkidBF);
			for (int i = 0; i < nodelist.size(); i++) {

				int nodeid = nodelist.get(i);
				int nodetype = dbService.getNodeService().FindNode(networkidBF, nodeid).getNodeType();
				int nodedegree = dbService.getNodeService().FindNode(networkidBF, nodeid).getDegree();
				List<CardPreference> li = dbService.getCardPreferenceService().FindPreferenceByNode(networkidBF,
						nodeid);
				if (li.isEmpty())// no preference exists
				{
					if (nodetype != MapConstants.ila)/** For Non-ILA nodes */
					{
						/** Insert Wss(based on node) and MPN(CGM) (for all) */

						if (nodetype == MapConstants.roadm) {
							CardPreference cp = new CardPreference(networkidBF, nodeid,
									ResourcePlanConstants.CardFeatureWSS, ResourcePlanConstants.CardWss1x9,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x9),
									ResourcePlanConstants.ONE);
							dbService.getCardPreferenceService().Insert(cp);
						} else if (nodetype == MapConstants.twoBselectRoadm) {
							CardPreference cp = new CardPreference(networkidBF, nodeid,
									ResourcePlanConstants.CardFeatureWSS, ResourcePlanConstants.CardWss1x2,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x2),
									ResourcePlanConstants.ONE);
							dbService.getCardPreferenceService().Insert(cp);
						} else if (nodetype == MapConstants.hub) {
							CardPreference cp = new CardPreference(networkidBF, nodeid,
									ResourcePlanConstants.CardFeatureWSS, ResourcePlanConstants.CardWss1x2,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x2),
									ResourcePlanConstants.ONE);
							dbService.getCardPreferenceService().Insert(cp);
						} else if (nodetype == MapConstants.te) {
							CardPreference cp = new CardPreference(networkidBF, nodeid,
									ResourcePlanConstants.CardFeatureWSS, ResourcePlanConstants.CardWss1x2,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x2),
									ResourcePlanConstants.ONE);
							dbService.getCardPreferenceService().Insert(cp);
						}

						// set mpn preference
						CardPreference cp = new CardPreference(networkidBF, nodeid,
								ResourcePlanConstants.CardFeatureMPN, ResourcePlanConstants.CardMuxponderCGM,
								rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMuxponderCGM),
								ResourcePlanConstants.ONE);
						dbService.getCardPreferenceService().Insert(cp);
					}

				}
			}
			// }

		} catch (Exception e) {
			logger.error("fsetDefaultCardPreferenceBrField: " + e.getMessage());
		}

	}

	public String fGetFirstFreeLineCardSlot(int networkid, int nodeid) {
		/* fetch the nodepool from map for that node */
		logger.info(tag + "fGetFirstFreeLineCardSlot(): Network " + networkid + " Node " + nodeid);
		NodeSystem nodepool = poolmap.get(nodeid);
		logger.info(tag + " Nodeid: " + nodeid + " Node Pool: " + nodepool.toString());
		String location = "";

		for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) {
			for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) {
				for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) {
					if ((k != 6) | (k != 9)) {
						if (fCheckSlotAvailability(networkid, nodeid, i, j, k)) {
							location = rpu.locationStr(i, j, j);
							return location;
						}
					}
				}
			}
		}
		return location;
	}

	public void fAssignYcablesold(int networkid) throws SQLException {
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		for (int k = 0; k < nodelist.size(); k++) {
			String SrNo = "";
			int nodeid = nodelist.get(k);
			int nodetype = dbService.getNodeService().FindNode(networkid, nodelist.get(k)).getNodeType();
			logger.info("fassignYcables" + "Network: " + networkid + "Node: " + nodeid + "Type: " + nodetype);
			switch (nodetype) {
			case MapConstants.roadm: {
				float cntYCabCircuits = dbService.getCircuitService().countCircuitsByClientProtPerNode(networkid,
						nodeid, MapConstants.YCableProtection);
				float cntYCab = cntYCabCircuits / 10;
				int nYcables = (int) Math.ceil(cntYCab);
				logger.info("Ycables required: " + nYcables);
				//				String NodeKey = "" + networkid + "_" + nodeid;
				int nRacks = dbService.getCardInfoService().FindRackCount(networkid, nodeid);
				if (nYcables < nRacks * 5)// required ycables are less than the spaces available
				{
					for (int i = 1; (i <= nRacks) && (nYcables != 0); i++) {
						for (int j = 1; (j <= 5) && (nYcables != 0); j++) {
							CardInfo ycab = new CardInfo(networkid,nodeid, i, 0, j,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							--nYcables;
						}
					}
				} else {
					int rackid = nRacks;
					int sbrackid = 0;
					int sbrackused = dbService.getCardInfoService().FindSbRackCountInRack(networkid, nodeid, rackid);
					logger.info("ResourcePlanning.fAssignYcables()" + "node " + nodeid + "rack " + rackid
							+ "sbrackused " + sbrackused);
					if (sbrackused < 3) {
						// filling spaces
						for (int i = 1; (i <= nRacks) && (nYcables != 0); i++) {
							for (int j = 1; (j <= 5) && (nYcables != 0); j++) {
								CardInfo ycab = new CardInfo(networkid,nodeid, i, 0, j,
										rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
										ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
								dbService.getCardInfoService().Insert(ycab);
								--nYcables;
							}
						}

						// filling the next subrack
						rackid = nRacks;
						sbrackid = sbrackused + 1;
						for (int i = 9; (i != 0) && (nYcables != 0); i--) {
							CardInfo ycab = new CardInfo(networkid,nodeid, rackid, sbrackid, 10 - i,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							--nYcables;
						}

					} else {
						// filling all racks spaces first
						for (int i = 1; (i <= nRacks + 1) && (nYcables != 0); i++) {
							for (int j = 1; (j <= 5) && (nYcables != 0); j++) {
								CardInfo ycab = new CardInfo(networkid,nodeid, i, 0, j,
										rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
										ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
								dbService.getCardInfoService().Insert(ycab);
								--nYcables;
							}
						}
						// fill the extra rack sbrack
						rackid = nRacks + 1;
						sbrackid = 1;
						for (int i = 9; (i != 0) && (nYcables != 0); i--) {
							CardInfo ycab = new CardInfo(networkid,nodeid, rackid, sbrackid, 10 - i,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							--nYcables;
						}
					}
				}
			}
			break;
			case MapConstants.te: {
				float cntYCabCircuits = dbService.getCircuitService().countCircuitsByClientProtPerNode(networkid,
						nodeid, MapConstants.YCableProtection);
				float cntYCab = cntYCabCircuits / 10;
				int nYcables = (int) Math.ceil(cntYCab);
				logger.info("Ycables required: " + nYcables);
				//				String NodeKey = "" + networkid + "_" + nodeid;
				int nRacks = dbService.getCardInfoService().FindRackCount(networkid, nodeid);
				int spacesAvailable = (nRacks - 1) * 5 + 3; // TE rack has only 3 y cable spaces
				if (nYcables < spacesAvailable)// required ycables are less than the spaces available
				{
					// fill first rack
					for (int j = 3; (j <= 5) && (nYcables != 0); j++) {
						CardInfo ycab = new CardInfo(networkid,nodeid, 1, 0, j,
								rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
								ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
						dbService.getCardInfoService().Insert(ycab);
						--nYcables;
					}

					for (int i = 2; (i <= nRacks) && (nYcables != 0); i++) {
						for (int j = 1; (j <= 5) && (nYcables != 0); j++) {
							CardInfo ycab = new CardInfo(networkid,nodeid, i, 0, j,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							--nYcables;
						}
					}
				} else {
					int rackid = nRacks;
					int sbrackid = 0;
					int sbrackused = dbService.getCardInfoService().FindSbRackCountInRack(networkid, nodeid, rackid);
					logger.info("ResourcePlanning.fAssignYcables()" + "node " + nodeid + "rack " + rackid
							+ "sbrackused " + sbrackused);
					if (sbrackused < 3) {
						// filling spaces

						// fill first rack spaces
						for (int j = 3; (j <= 5) && (nYcables != 0); j++) {
							CardInfo ycab = new CardInfo(networkid,nodeid, 1, 0, j,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							--nYcables;
						}

						for (int i = 2; (i <= nRacks) && (nYcables != 0); i++) {
							for (int j = 1; (j <= 5) && (nYcables != 0); j++) {
								CardInfo ycab = new CardInfo(networkid,nodeid, i, 0, j,
										rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
										ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
								dbService.getCardInfoService().Insert(ycab);
								--nYcables;
							}
						}

						// filling the next subrack
						rackid = nRacks;
						sbrackid = sbrackused + 1;
						for (int i = 9; (i != 0) && (nYcables != 0); i--) {
							CardInfo ycab = new CardInfo(networkid,nodeid, rackid, sbrackid, 10 - i,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							--nYcables;
						}

					} else {
						// filling all racks spaces first
						// fill first rack
						for (int j = 3; (j <= 5) && (nYcables != 0); j++) {
							CardInfo ycab = new CardInfo(networkid,nodeid,1, 0, j,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							--nYcables;
						}

						for (int i = 2; (i <= nRacks + 1) && (nYcables != 0); i++) {
							for (int j = 1; (j <= 5) && (nYcables != 0); j++) {
								CardInfo ycab = new CardInfo(networkid,nodeid, i, 0, j,
										rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
										ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
								dbService.getCardInfoService().Insert(ycab);
								--nYcables;
							}
						}
						// fill the extra rack sbrack
						rackid = nRacks + 1;
						sbrackid = 1;
						for (int i = 9; (i != 0) && (nYcables != 0); i--) {
							CardInfo ycab = new CardInfo(networkid,nodeid, rackid, sbrackid, 10 - i,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							--nYcables;
						}
					}
				}

			}
			break;

			}
		}
	}

	public void fAssignYcables(int networkid) throws SQLException {
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		int r, s, c = 0;
		for (int k = 0; k < nodelist.size(); k++) {
			String SrNo = "";
			int nodeid = nodelist.get(k);
			int nodetype = dbService.getNodeService().FindNode(networkid, nodelist.get(k)).getNodeType();
			logger.info("fassignYcables" + "Network: " + networkid + "Node: " + nodeid + "Type: " + nodetype);
			switch (nodetype) {
			case MapConstants.te:
			case MapConstants.roadm:
			case MapConstants.twoBselectRoadm:
			case MapConstants.hub: 
			case MapConstants.cdRoadm: {
				float cntYCabCircuits = dbService.getCircuitService().countCircuitsByClientProtPerNode(networkid,
						nodeid, MapConstants.YCableProtection);
				float cntYCab = cntYCabCircuits / 10;
				int nYcables = (int) Math.ceil(cntYCab);
				logger.info(" Node : " + nodeid + " Ycables required All : " + nYcables);
				//				String NodeKey = "" + networkid + "_" + nodeid;
				// CardInfo space =
				// dbService.getCardInfoService().FindfreeSubrackSpacenew(networkid, nodeid);
				// CardInfo space = rpu.fgetFreeSbrackSpace(networkid, nodeid);
				int nRacks = dbService.getCardInfoService().FindRackCount(networkid, nodeid);
				for (int i = 0; i < nYcables; i++) {
					CardInfo space = rpu.fgetFreeSbrackSpace(networkid, nodeid);
					if (space != null) {
						r = space.getRack();
						s = space.getSbrack();
						c = space.getCard();

						CardInfo ycab = new CardInfo(networkid,nodeid, r, s, c,
								rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
								ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
						dbService.getCardInfoService().Insert(ycab);
						logger.info("ResourcePlanning.fAssignYcables()" + ycab.toString());

					} else {
						logger.info(" Node : " + nodeid + " :::0 Ycables required now : " + nYcables);
						nYcables = nYcables - i;

						if ((nYcables != 0))// take extra rack
						{
							r = nRacks + 1;
							s = 0;
							c = 1;
							CardInfo ycab = new CardInfo(networkid,nodeid,r, s, c,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							logger.info("ResourcePlanning.fAssignYcables()" + ycab.toString());
							nYcables = nYcables - 1;
							logger.info(" Node : " + nodeid + " :::2 Ycables required now : " + nYcables);
							for (int m = 0; m < nYcables; m++) {
								space = rpu.fgetFreeSbrackSpace(networkid, nodeid);
								if (space != null) {
									r = space.getRack();
									s = space.getSbrack();
									c = space.getCard();

									ycab = new CardInfo(networkid,nodeid, r, s, c,
											rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
											ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO,
											SrNo);
									dbService.getCardInfoService().Insert(ycab);
									--nYcables;
									logger.info("ResourcePlanning.fAssignYcables()" + ycab.toString());
								} else {
									nYcables = nYcables - m;
									break;
								}
							}
						}
						logger.info(" Node : " + nodeid + " :::3 Ycables required now : " + nYcables);

						if ((nYcables != 0))// all spaces are filled now fill sbrack
						{
							// recount max rack
							r = dbService.getCardInfoService().FindRackCount(networkid, nodeid);
							s = 1;

							// fill the extra rack sbrack
							for (int n = 9; (n != 0) && (nYcables != 0); n--) {
								CardInfo ycab = new CardInfo(networkid,nodeid,r, s, 10 - n,
										rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
										ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
								dbService.getCardInfoService().Insert(ycab);
								logger.info("ResourcePlanning.fAssignYcables()" + ycab.toString());
								--nYcables;
							}
						}

						break;
					}
				}
				logger.info(" Node : " + nodeid + " :::1 Ycables required now : " + nYcables);

			}
			}
		}

	}

	public void fAssignYcablesBrField(int networkid) throws SQLException {
		logger.info(" fAssignYcablesBrField");
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkidBF);

		int r, s, c = 0;
		for (int k = 0; k < nodelist.size(); k++) {
			String SrNo = "";
			int nodeid = nodelist.get(k);
			int nodetype = dbService.getNodeService().FindNode(networkidBF, nodelist.get(k)).getNodeType();
			logger.info("fAssignYcablesBrField" + "Network: " + networkid + "Node: " + nodeid + "Type: " + nodetype);
			switch (nodetype) {
			case MapConstants.te:
			case MapConstants.roadm:
			case MapConstants.twoBselectRoadm:
			case MapConstants.hub:
			case MapConstants.cdRoadm:{
				float cntYCabCircuits = dbService.getCircuitService().countCircuitsByClientProtPerNode(networkidBF,
						nodeid, MapConstants.YCableProtection);
				// get the count of all the added circuits
				// float
				// cntYCabCircuits=dbService.getCircuitService().FindAddedCircuitsInBrField(networkid,
				// networkidBF, nodeid).size();
				float cntYCab = cntYCabCircuits / 10;
				int nYcables = (int) Math.ceil(cntYCab);
				logger.info(" Node : " + nodeid + " Ycables required All : " + nYcables);

				// find Y Cables required additionally
				List<CardInfo> Ycabs = dbService.getCardInfoService().FindCardInfoByCardType(networkidBF, nodeid,
						ResourcePlanConstants.YCable1x2Unit);
				int nYcablesPresent = Ycabs.size();
				int nYcablesReq = nYcables - nYcablesPresent;

				//				String NodeKey = "" + networkidBF + "_" + nodeid;
				// CardInfo space =
				// dbService.getCardInfoService().FindfreeSubrackSpacenew(networkid, nodeid);
				// CardInfo space = rpu.fgetFreeSbrackSpace(networkid, nodeid);
				int nRacks = dbService.getCardInfoService().FindRackCount(networkidBF, nodeid);
				for (int i = 0; i < nYcablesReq; i++) {
					CardInfo space = rpu.fgetFreeSbrackSpace(networkidBF, nodeid);
					if (space != null) {
						r = space.getRack();
						s = space.getSbrack();
						c = space.getCard();

						CardInfo ycab = new CardInfo(networkidBF,nodeid, r, s, c,
								rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
								ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
						dbService.getCardInfoService().Insert(ycab);
						logger.info("ResourcePlanning.fAssignYcables()" + ycab.toString());

					} else {
						logger.info(" Node : " + nodeid + " :::0 Ycables required now : " + nYcablesReq);
						nYcablesReq = nYcablesReq - i;

						if ((nYcablesReq != 0))// take extra rack
						{
							r = nRacks + 1;
							s = 0;
							c = 1;
							CardInfo ycab = new CardInfo(networkidBF,nodeid, r, s, c,
									rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
									ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
							dbService.getCardInfoService().Insert(ycab);
							logger.info("ResourcePlanning.fAssignYcables()" + ycab.toString());
							nYcablesReq = nYcablesReq - 1;
							logger.info(" Node : " + nodeid + " :::2 Ycables required now : " + nYcablesReq);
							for (int m = 0; m < nYcablesReq; m++) {
								space = rpu.fgetFreeSbrackSpace(networkidBF, nodeid);
								if (space != null) {
									r = space.getRack();
									s = space.getSbrack();
									c = space.getCard();

									ycab = new CardInfo(networkidBF,nodeid, r, s, c,
											rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
											ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO,
											SrNo);
									dbService.getCardInfoService().Insert(ycab);
									--nYcablesReq;
									logger.info("ResourcePlanning.fAssignYcables()" + ycab.toString());
								} else {
									nYcablesReq = nYcablesReq - m;
									break;
								}
							}
						}
						logger.info(" Node : " + nodeid + " :::3 Ycables required now : " + nYcablesReq);

						if ((nYcablesReq != 0))// all spaces are filled now fill sbrack
						{
							// recount max rack
							r = dbService.getCardInfoService().FindRackCount(networkidBF, nodeid);
							s = 1;

							// fill the extra rack sbrack
							for (int n = 9; (n != 0) && (nYcablesReq != 0); n--) {
								CardInfo ycab = new CardInfo(networkidBF,nodeid, r, s, 10 - n,
										rpu.fgetCardTypeFromEId(ResourcePlanConstants.EIdYCable1x2Unit), "", 0, 0,
										ResourcePlanConstants.EIdYCable1x2Unit, "", ResourcePlanConstants.ZERO, SrNo);
								dbService.getCardInfoService().Insert(ycab);
								logger.info("ResourcePlanning.fAssignYcables()" + ycab.toString());
								--nYcablesReq;
							}
						}

						break;
					}
				}
				logger.info(" Node : " + nodeid + " :::1 Ycables required now : " + nYcablesReq);

			}
			}
		}

	}

	public void fAssignRegenerators(int networkid) {
		logger.info(" fAssignRegenerators");
		List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List<String> paths = new ArrayList<String>();
		String[] regenNodeids, pnodeids;
		String SrNo = "";
		for (int i = 0; i < routesAll.size(); i++) {
			if ((routesAll.get(i).getRoutePriority() == ResourcePlanConstants.ONE)
					|| (routesAll.get(i).getRoutePriority() == ResourcePlanConstants.TWO)) {
				if (!routesAll.get(i).getRegeneratorLoc().equalsIgnoreCase("-")) {
					regenNodeids = routesAll.get(i).getRegeneratorLoc().split(",");
					for (int j = 0; j < regenNodeids.length; j++) {
						int nodeid = Integer.parseInt(regenNodeids[j].toString());
						NodeSystem assignedSys;
						if (assignedmap.get(nodeid) != null) {
							assignedSys = assignedmap.get(nodeid);
						} else {
							assignedSys = new NodeSystem(nodeid);
						}

						NodeSystem nodepool;
						nodepool = poolmap.get(nodeid);

						String location = fGetFirstFreeSlot(networkid, nodeid);
						logger.info(j + " fAssignRegenerators() location found : " + location);
						if (((location.contains("_6")/* |(location.contains("_9")) */))) {
							j--;
							int[] id = rpu.locationIds(location);

							assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], ResourcePlanConstants.CardMpc);
							fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0,
									0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "",
									ResourcePlanConstants.ZERO, SrNo);
							fRemoveCard(nodepool, id[0], id[1], id[2], ResourcePlanConstants.CardMpc);
						} else {
							String regenType = ResourcePlanConstants.Regenerator100G;
							int[] id = rpu.locationIds(location);
							if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
								regenType = ResourcePlanConstants.Regenerator10G;
							} else if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line100)) {
								regenType = ResourcePlanConstants.Regenerator100G;
							}
							assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], regenType);
							fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], regenType, "",
									routesAll.get(i).getWavelengthNo(), routesAll.get(i).getDemandId(),
									rpu.fgetEIdFromCardType(regenType), "", ResourcePlanConstants.ZERO, SrNo);
							fRemoveCard(nodepool, id[0], id[1], id[2], regenType);
						}

						poolmap.remove(nodeid);
						poolmap.put(nodeid, nodepool);
						assignedmap.remove(nodeid);
						assignedmap.put(nodeid, assignedSys);
					}
				}
			}
		}
	}

	public void fAssignRegeneratorsDb(int networkid) {
		logger.info(" fAssignRegeneratorsDb");
		List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List<String> paths = new ArrayList<String>();
		String[] regenNodeids, pnodeids;
		String SrNo = "";
		for (int i = 0; i < routesAll.size(); i++) {
			if ((routesAll.get(i).getRoutePriority() == ResourcePlanConstants.ONE)
					|| (routesAll.get(i).getRoutePriority() == ResourcePlanConstants.TWO)) {
				if (!routesAll.get(i).getRegeneratorLoc().equalsIgnoreCase("-")) {
					regenNodeids = routesAll.get(i).getRegeneratorLoc().split(",");
					for (int j = 0; j < regenNodeids.length; j++) {
						int nodeid = Integer.parseInt(regenNodeids[j].toString());
						EquipmentPreferences eqp = new EquipmentPreferences(dbService);
						String MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);

						String location = rpu.fGetFirstFreeSlotDb(networkid, nodeid);
						logger.info(j + " fAssignRegenerators() location found : " + location);
						if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
							if (((location.contains("_6") | (location.contains("_9"))))) {
								j--;
								int[] id = rpu.locationIds(location);
								fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "",
										0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "",
										ResourcePlanConstants.ZERO, SrNo);
							} else {
								String regenType = ResourcePlanConstants.Regenerator100G;
								int[] id = rpu.locationIds(location);
								if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
									regenType = ResourcePlanConstants.Regenerator10G;
								} else if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line100)) {
									regenType = ResourcePlanConstants.Regenerator100G;
								}
								//
								fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], regenType, "",
										routesAll.get(i).getWavelengthNo(), routesAll.get(i).getDemandId(),
										rpu.fgetEIdFromCardType(regenType), "", ResourcePlanConstants.ZERO, SrNo);
							}
						} else {
							if (((location.contains("_6")/* |(location.contains("_9")) */))) {
								j--;
								int[] id = rpu.locationIds(location);
								fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "",
										0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "",
										ResourcePlanConstants.ZERO, SrNo);

							} else {
								String regenType = ResourcePlanConstants.Regenerator100G;
								int[] id = rpu.locationIds(location);
								if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
									regenType = ResourcePlanConstants.Regenerator10G;
								} else if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line100)) {
									regenType = ResourcePlanConstants.Regenerator100G;
								}
								fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], regenType, "",
										routesAll.get(i).getWavelengthNo(), routesAll.get(i).getDemandId(),
										rpu.fgetEIdFromCardType(regenType), "", ResourcePlanConstants.ZERO, SrNo);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Assigns the Regenerators for the added/new routes in the brown field
	 * 
	 * @param networkid
	 */
	public void fAssignRegeneratorsBrField(int networkid) {
		logger.info(" fAssignRegeneratorsBrField");
		// get the brown field network id
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		List<NetworkRoute> routesAdded = dbService.getNetworkRouteService().FindAddedRouteInBrField(networkid,
				networkidBF);
		// List<NetworkRoute> routesAll =
		// dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List<String> paths = new ArrayList<String>();
		String[] regenNodeids, pnodeids;
		String SrNo = "";
		for (int i = 0; i < routesAdded.size(); i++) {
			if ((routesAdded.get(i).getRoutePriority() == ResourcePlanConstants.ONE)
					|| (routesAdded.get(i).getRoutePriority() == ResourcePlanConstants.TWO)) {
				if (!routesAdded.get(i).getRegeneratorLoc().equalsIgnoreCase("-")) {
					regenNodeids = routesAdded.get(i).getRegeneratorLoc().split(",");
					for (int j = 0; j < regenNodeids.length; j++) {
						int nodeid = Integer.parseInt(regenNodeids[j].toString());

						String location = rpu.fGetFirstFreeSlotDb(networkid, nodeid);
						logger.info(j + " fAssignRegeneratorsBrField() location found : " + location);
						if (((location.contains("_6")/* |(location.contains("_9")) */))) {
							j--;
							int[] id = rpu.locationIds(location);
							fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0,
									0, rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "",
									ResourcePlanConstants.ZERO, SrNo);

						} else {
							String regenType = ResourcePlanConstants.Regenerator100G;
							int[] id = rpu.locationIds(location);
							if (routesAdded.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
								regenType = ResourcePlanConstants.Regenerator10G;
							} else if (routesAdded.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line100)) {
								regenType = ResourcePlanConstants.Regenerator100G;
							}
							fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], regenType, "",
									routesAdded.get(i).getWavelengthNo(), routesAdded.get(i).getDemandId(),
									rpu.fgetEIdFromCardType(regenType), "", ResourcePlanConstants.ZERO, SrNo);
						}
					}
				}
			}
		}

		/**
		 * remove regenerators for the deleted paths
		 */

		List<NetworkRoute> routesDeleted = dbService.getNetworkRouteService().FindDeletedRouteInBrField(networkid,
				networkidBF);
		String cardtype;
		for (int i = 0; i < routesDeleted.size(); i++) {
			if ((routesDeleted.get(i).getRoutePriority() == ResourcePlanConstants.ONE)
					|| (routesDeleted.get(i).getRoutePriority() == ResourcePlanConstants.TWO)) {
				if (!routesDeleted.get(i).getRegeneratorLoc().equalsIgnoreCase("-")) {
					regenNodeids = routesDeleted.get(i).getRegeneratorLoc().split(",");
					for (int j = 0; j < regenNodeids.length; j++) {
						String regenType = ResourcePlanConstants.Regenerator100G;
						int nodeid = Integer.parseInt(regenNodeids[j].toString());
						if (routesDeleted.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
							regenType = ResourcePlanConstants.Regenerator10G;
						} else if (routesDeleted.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line100)) {
							regenType = ResourcePlanConstants.Regenerator100G;
						}
						dbService.getCardInfoService().FindCardInfo(networkidBF, nodeid,
								routesDeleted.get(i).getDemandId(), regenType);
					}
				}
			}
		}
	}

	/**
	 * Assigns DCM tray and replaces ilas with mid stage amplifiers in the 10G path
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fassignDCMTray(int networkid) throws SQLException {
		logger.info(" fassignDCMTray");
		List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List<String> paths = new ArrayList<String>();
		String[] nodeids;
		String dir = "", dir1 = "", dir2 = "";
		String SrNo = "";

		for (int i = 0; i < routesAll.size(); i++) {
			if ((routesAll.get(i).getRoutePriority() == ResourcePlanConstants.ONE)
					|| (routesAll.get(i).getRoutePriority() == ResourcePlanConstants.TWO)) {
				if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
					nodeids = routesAll.get(i).getPath().split(",");
					for (int j = 0; j < nodeids.length; j++) {
						int nodeid = Integer.parseInt(nodeids[j].toString());
						int nodetype = dbService.getNodeService()
								.FindNode(networkid, Integer.parseInt(nodeids[j].toString())).getNodeType();
						if ((j == 0) | (j == nodeids.length - 1)) {
							if (j == nodeids.length - 1) {
								dir = dbService.getLinkService().FindLinkDirection(networkid,
										Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));
								fInsertDCMModule(networkid, Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										dir);
							} else if (j == 0) {
								dir = dbService.getLinkService().FindLinkDirection(networkid,
										Integer.parseInt(nodeids[j].toString()),
										Integer.parseInt(nodeids[j + 1].toString()));
								fInsertDCMModule(networkid, nodeid, dir);
							}
						} else// for intermediate nodes both direction on route will be taken care
						{
							dir1 = dbService.getLinkService().FindLinkDirection(networkid,
									Integer.parseInt(nodeids[j].toString()),
									Integer.parseInt(nodeids[j + 1].toString()));
							fInsertDCMModule(networkid, nodeid, dir1);
							dir2 = dbService.getLinkService().FindLinkDirection(networkid,
									Integer.parseInt(nodeids[j].toString()),
									Integer.parseInt(nodeids[j - 1].toString()));
							fInsertDCMModule(networkid, Integer.parseInt(nodeids[j].toString()), dir2);
						}

						// fsaveDCMTray(networkid, nodeid, dir, routesAll.get(i).getDemandId());
						// fInsertDCMModule(networkid, nodeid, dir);

						if (j == nodeids.length - 1)// add filter along the muxponder locations at the path ends
						{
							fsaveTunableFilter(networkid, nodeid, dir, routesAll.get(i).getDemandId());
						} else if (j == 0) {
							fsaveTunableFilter(networkid, nodeid, dir, routesAll.get(i).getDemandId());
						}

						// replace ILAs with Mid stage amplifier
						if (nodetype == MapConstants.ila) {
							NodeSystem assignedSys;
							if (assignedmap.get(nodeid) != null) {
								assignedSys = assignedmap.get(nodeid);
							} else {
								assignedSys = new NodeSystem(nodeid);
							}

							NodeSystem nodepool;
							nodepool = poolmap.get(nodeid);
							List<CardInfo> ilas = dbService.getCardInfoService().FindCardInfoByCardType(networkid,
									nodeid, ResourcePlanConstants.CardIla);
							for (int k = 0; k < ilas.size(); k++) {
								fRemoveCard(nodepool, ilas.get(k).getRack(), ilas.get(k).getSbrack(),
										ilas.get(k).getCard(), ResourcePlanConstants.CardIla);
								dbService.getCardInfoService().DeleteCard(networkid, nodeid, ilas.get(k).getRack(),
										ilas.get(k).getSbrack(), ilas.get(k).getCard());
								assignedSys = fAssignCard(assignedSys, ilas.get(k).getRack(), ilas.get(k).getSbrack(),
										ilas.get(k).getCard(), ResourcePlanConstants.Mid_Stage_amplifier);
								fSaveCardInDb(networkid, nodeid, ilas.get(k).getRack(), ilas.get(k).getSbrack(),
										ilas.get(k).getCard(), ResourcePlanConstants.Mid_Stage_amplifier,
										ilas.get(k).getDirection(), 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Mid_Stage_amplifier), "",
										ResourcePlanConstants.ZERO, SrNo);

							}
							poolmap.remove(nodeid);
							poolmap.put(nodeid, nodepool);
							assignedmap.remove(nodeid);
							assignedmap.put(nodeid, assignedSys);
						}
					}
				}

			}
		}
	}

	/**
	 * Assigns DCM tray and replaces ilas with mid stage amplifiers in the 10G path
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fassignDCMTrayDb(int networkid) throws SQLException {
		logger.info(" fassignDCMTrayDb");
		List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List<String> paths = new ArrayList<String>();
		String[] nodeids;
		String dir = "", dir1 = "", dir2 = "";
		String SrNo = "";

		for (int i = 0; i < routesAll.size(); i++) {
			if ((routesAll.get(i).getRoutePriority() == ResourcePlanConstants.ONE)
					|| (routesAll.get(i).getRoutePriority() == ResourcePlanConstants.TWO)) {
				if (routesAll.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
					nodeids = routesAll.get(i).getPath().split(",");
					for (int j = 0; j < nodeids.length; j++) {
						int nodeid = Integer.parseInt(nodeids[j].toString());
						int nodetype = dbService.getNodeService()
								.FindNode(networkid, Integer.parseInt(nodeids[j].toString())).getNodeType();
						if ((j == 0) | (j == nodeids.length - 1)) {
							if (j == nodeids.length - 1) {
								dir = dbService.getLinkService().FindLinkDirection(networkid,
										Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));
								fInsertDCMModule(networkid, Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										dir);
							} else if (j == 0) {
								dir = dbService.getLinkService().FindLinkDirection(networkid,
										Integer.parseInt(nodeids[j].toString()),
										Integer.parseInt(nodeids[j + 1].toString()));
								fInsertDCMModule(networkid, nodeid, dir);
							}
						} else// for intermediate nodes both direction on route will be taken care
						{
							dir1 = dbService.getLinkService().FindLinkDirection(networkid,
									Integer.parseInt(nodeids[j].toString()),
									Integer.parseInt(nodeids[j + 1].toString()));
							fInsertDCMModule(networkid, nodeid, dir1);
							dir2 = dbService.getLinkService().FindLinkDirection(networkid,
									Integer.parseInt(nodeids[j].toString()),
									Integer.parseInt(nodeids[j - 1].toString()));
							fInsertDCMModule(networkid, Integer.parseInt(nodeids[j].toString()), dir2);
						}

						// fsaveDCMTray(networkid, nodeid, dir, routesAll.get(i).getDemandId());
						// fInsertDCMModule(networkid, nodeid, dir);

						if (j == nodeids.length - 1)// add filter along the muxponder locations at the path ends
						{
							fsaveTunableFilterDb(networkid, nodeid, dir, routesAll.get(i).getDemandId());
						} else if (j == 0) {
							fsaveTunableFilterDb(networkid, nodeid, dir, routesAll.get(i).getDemandId());
						}

						// replace ILAs with Mid stage amplifier
						if (nodetype == MapConstants.ila) {
							List<CardInfo> ilas = dbService.getCardInfoService().FindCardInfoByCardType(networkid,
									nodeid, ResourcePlanConstants.CardIla);
							for (int k = 0; k < ilas.size(); k++) {
								dbService.getCardInfoService().DeleteCard(networkid, nodeid, ilas.get(k).getRack(),
										ilas.get(k).getSbrack(), ilas.get(k).getCard());
								fSaveCardInDb(networkid, nodeid, ilas.get(k).getRack(), ilas.get(k).getSbrack(),
										ilas.get(k).getCard(), ResourcePlanConstants.Mid_Stage_amplifier,
										ilas.get(k).getDirection(), 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Mid_Stage_amplifier), "",
										ResourcePlanConstants.ZERO, SrNo);
							}
						}
					}
				}

			}
		}
	}

	public void fsaveDCMTray(int networkid, int nodeid, String dir, int demandid) throws SQLException {
		// CardInfo space =
		// dbService.getCardInfoService().FindfreeSubrackSpace(networkid, nodeid);
		CardInfo space = dbService.getCardInfoService().FindfreeSubrackSpacenew(networkid, nodeid);
		CardInfo dcm = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.DCM_Tray_Unit,
				dir);
		int r, s, c;
		String SrNo = "";
		if (dcm == null)// if dcm for that particular dir doesnot exist add it
		{
			if (space == null) {
				r = 1;
				s = 0;
				c = 1;
				fSaveCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, dir, 0, demandid,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit), "", ResourcePlanConstants.ZERO,
						SrNo);
			} else {
				r = space.getRack();
				s = space.getSbrack();
				c = space.getCard();
				if (c < 5) {
					c = c + 1;
					fSaveCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, dir, 0, demandid,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit), "",
							ResourcePlanConstants.ZERO, SrNo);
				} else if (c == 5) {
					r = r + 1;
					c = 1;
					fSaveCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, dir, 0, demandid,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit), "",
							ResourcePlanConstants.ZERO, SrNo);
				} else {
					// put in next rack space
					r = r + 1;
					c = c + 1;
					fSaveCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, dir, 0, demandid,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit), "",
							ResourcePlanConstants.ZERO, SrNo);
				}
				logger.info("ResourcePlanning.fsaveDCMTray() added dcm tray for dir: " + dir + " loc: " + r + " " + s
						+ " " + c);
			}
		} else if (dcm != null) {
			logger.info("ResourcePlanning.fAssignDCMTray() Dcm exists for Dir: " + dir);
		}
	}

	/**
	 * Assigns DCM tray and replaces ilas with mid stage amplifiers in the 10G path
	 * for brown field
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fassignDCMTrayBrField(int networkid) throws SQLException {
		logger.info(" fassignDCMTrayBrField");
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());

		// List<NetworkRoute> routesAll =
		// dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List<String> paths = new ArrayList<String>();
		String[] nodeids;
		String dir = "", dir1 = "", dir2 = "";
		String SrNo = "";

		/**
		 * For Deleted routes
		 */
		List<NetworkRoute> routesDeleted = dbService.getNetworkRouteService().FindDeletedRouteInBrField(networkid,
				networkidBF);
		for (int i = 0; i < routesDeleted.size(); i++) {
			if ((routesDeleted.get(i).getRoutePriority() == ResourcePlanConstants.ONE)
					| (routesDeleted.get(i).getRoutePriority() == ResourcePlanConstants.TWO)) {
				if (routesDeleted.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
					// check if any 10G demand or route exist on this link, if yes then leave or
					// otherwise delete the dcm module for that direction on that node
					nodeids = routesDeleted.get(i).getPath().split(",");
					for (int j = 0; j < nodeids.length; j++) {
						int nodeid = Integer.parseInt(nodeids[j].toString());
						int nodetype = dbService.getNodeService()
								.FindNode(networkidBF, Integer.parseInt(nodeids[j].toString())).getNodeType();
						if ((j == 0) | (j == nodeids.length - 1)) {
							if (j == nodeids.length - 1) {
								boolean dcmReq = false;
								dir = dbService.getLinkService().FindLinkDirection(networkidBF,
										Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));
								Link link = dbService.getLinkService().FindLink(networkidBF,
										Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));
								List<LinkWavelengthInfo> linkInfo = dbService.getLinkWavelengthInfoService()
										.FindAllByLink(networkidBF, link.getLinkId());
								for (int k = 0; k < linkInfo.size(); k++) {

									linkInfo.get(k).getDemandId();
									Demand d = dbService.getDemandService().FindDemand(networkidBF,
											linkInfo.get(k).getDemandId());
									if (d.getLineRate() == MapConstants.Line10) {
										dcmReq = true;
										break;
									} else {
										continue;
									}
								}
								if (dcmReq == false) {
									// remove the dcm module
									logger.info("ResourcePlanning.fassignDCMTrayBrField()Link:" + link.getLinkId()
									+ " dcmReq: " + dcmReq);

									PortInfo port = dbService.getPortInfoService().FindDcmModuleByDir(networkidBF,
											nodeid, dir);
									if (port != null) {
										// set dir as null
										dbService.getPortInfoService().UpdateDcmModule(networkidBF, nodeid,
												port.getRack(), port.getSbrack(), port.getCard(), port.getPort(), "");
										if (rpu.CheckIsDcmTrayEmpty(networkidBF, nodeid, port.getRack(),
												port.getSbrack(), port.getCard())) {
											dbService.getPortInfoService().DeletePort(networkidBF, nodeid,
													port.getRack(), port.getSbrack(), port.getCard(), 1);
											dbService.getPortInfoService().DeletePort(networkidBF, nodeid,
													port.getRack(), port.getSbrack(), port.getCard(), 2);
											// remove the dcm tray also if it is the last assigned module
											dbService.getCardInfoService().DeleteCard(networkidBF, nodeid,
													port.getRack(), port.getSbrack(), port.getCard());
										}
									}
								}

								// delete the tunable filter
								CardInfo filter = dbService.getCardInfoService().FindCardInfo(networkidBF, nodeid,
										routesDeleted.get(i).getDemandId(), ResourcePlanConstants.Tunable_Filter_Card);
								dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, filter.getRack(),
										filter.getSbrack(), filter.getCard());

							} else if (j == 0) {
								boolean dcmReq = false;
								dir = dbService.getLinkService().FindLinkDirection(networkidBF,
										Integer.parseInt(nodeids[j].toString()),
										Integer.parseInt(nodeids[j + 1].toString()));
								Link link = dbService.getLinkService().FindLink(networkidBF,
										Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));
								List<LinkWavelengthInfo> linkInfo = dbService.getLinkWavelengthInfoService()
										.FindAllByLink(networkidBF, link.getLinkId());
								for (int k = 0; k < linkInfo.size(); k++) {
									linkInfo.get(k).getDemandId();
									Demand d = dbService.getDemandService().FindDemand(networkidBF,
											linkInfo.get(k).getDemandId());
									if (d.getLineRate() == MapConstants.Line10) {
										dcmReq = true;
										break;
									} else {
										continue;
									}
								}
								if (dcmReq == false) {
									// remove the dcm module
									logger.info("ResourcePlanning.fassignDCMTrayBrField()Link:" + link.getLinkId()
									+ " dcmReq: " + dcmReq);
									PortInfo port = dbService.getPortInfoService().FindDcmModuleByDir(networkidBF,
											nodeid, dir);
									if (port != null) {
										// set dir as null
										dbService.getPortInfoService().UpdateDcmModule(networkidBF, nodeid,
												port.getRack(), port.getSbrack(), port.getCard(), port.getPort(), "");
										if (rpu.CheckIsDcmTrayEmpty(networkidBF, nodeid, port.getRack(),
												port.getSbrack(), port.getCard())) {
											dbService.getPortInfoService().DeletePort(networkidBF, nodeid,
													port.getRack(), port.getSbrack(), port.getCard(), 1);
											dbService.getPortInfoService().DeletePort(networkidBF, nodeid,
													port.getRack(), port.getSbrack(), port.getCard(), 2);
											// remove the dcm tray also if it is the last assigned module
											dbService.getCardInfoService().DeleteCard(networkidBF, nodeid,
													port.getRack(), port.getSbrack(), port.getCard());
										}
									}

								}

								// delete the tunable filter
								CardInfo filter = dbService.getCardInfoService().FindCardInfo(networkidBF, nodeid,
										routesDeleted.get(i).getDemandId(), ResourcePlanConstants.Tunable_Filter_Card);
								dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, filter.getRack(),
										filter.getSbrack(), filter.getCard());
							}
						} else// for intermediate nodes both direction on route will be taken care
						{
							boolean dcmReq = false;
							dir1 = dbService.getLinkService().FindLinkDirection(networkidBF,
									Integer.parseInt(nodeids[j].toString()),
									Integer.parseInt(nodeids[j + 1].toString()));
							Link link1 = dbService.getLinkService().FindLink(networkidBF,
									Integer.parseInt(nodeids[nodeids.length - 1].toString()),
									Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));
							List<LinkWavelengthInfo> linkInfo1 = dbService.getLinkWavelengthInfoService()
									.FindAllByLink(networkidBF, link1.getLinkId());
							for (int k = 0; k < linkInfo1.size(); k++) {

								linkInfo1.get(k).getDemandId();
								Demand d = dbService.getDemandService().FindDemand(networkidBF,
										linkInfo1.get(k).getDemandId());
								if (d.getLineRate() == MapConstants.Line10) {
									dcmReq = true;
									break;
								} else {
									continue;
								}
							}
							if (dcmReq == false) {
								// remove the dcm module
								logger.info("ResourcePlanning.fassignDCMTrayBrField()Link:" + link1.getLinkId()
								+ " dcmReq: " + dcmReq);
								PortInfo port = dbService.getPortInfoService().FindDcmModuleByDir(networkidBF, nodeid,
										dir1);
								if (port != null) {
									// set dir as null
									dbService.getPortInfoService().UpdateDcmModule(networkidBF, nodeid, port.getRack(),
											port.getSbrack(), port.getCard(), port.getPort(), "");
									if (rpu.CheckIsDcmTrayEmpty(networkidBF, nodeid, port.getRack(), port.getSbrack(),
											port.getCard())) {
										dbService.getPortInfoService().DeletePort(networkidBF, nodeid, port.getRack(),
												port.getSbrack(), port.getCard(), 1);
										dbService.getPortInfoService().DeletePort(networkidBF, nodeid, port.getRack(),
												port.getSbrack(), port.getCard(), 2);
										// remove the dcm tray also if it is the last assigned module
										dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, port.getRack(),
												port.getSbrack(), port.getCard());
									}
								}
							}

							dcmReq = false;
							dir2 = dbService.getLinkService().FindLinkDirection(networkidBF,
									Integer.parseInt(nodeids[j].toString()),
									Integer.parseInt(nodeids[j - 1].toString()));
							Link link2 = dbService.getLinkService().FindLink(networkidBF,
									Integer.parseInt(nodeids[nodeids.length - 1].toString()),
									Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));
							List<LinkWavelengthInfo> linkInfo2 = dbService.getLinkWavelengthInfoService()
									.FindAllByLink(networkidBF, link2.getLinkId());
							for (int k = 0; k < linkInfo2.size(); k++) {

								linkInfo2.get(k).getDemandId();
								Demand d = dbService.getDemandService().FindDemand(networkidBF,
										linkInfo2.get(k).getDemandId());
								if (d.getLineRate() == MapConstants.Line10) {
									dcmReq = true;
									break;
								} else {
									continue;
								}
							}
							if (dcmReq == false) {
								// remove the dcm module
								logger.info("ResourcePlanning.fassignDCMTrayBrField()Link:" + link2.getLinkId()
								+ " dcmReq: " + dcmReq);
								PortInfo port = dbService.getPortInfoService().FindDcmModuleByDir(networkidBF, nodeid,
										dir2);
								if (port != null) {
									// set dir as null
									dbService.getPortInfoService().UpdateDcmModule(networkidBF, nodeid, port.getRack(),
											port.getSbrack(), port.getCard(), port.getPort(), "");
									if (rpu.CheckIsDcmTrayEmpty(networkidBF, nodeid, port.getRack(), port.getSbrack(),
											port.getCard())) {
										dbService.getPortInfoService().DeletePort(networkidBF, nodeid, port.getRack(),
												port.getSbrack(), port.getCard(), 1);
										dbService.getPortInfoService().DeletePort(networkidBF, nodeid, port.getRack(),
												port.getSbrack(), port.getCard(), 2);
										// remove the dcm tray also if it is the last assigned module
										dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, port.getRack(),
												port.getSbrack(), port.getCard());
									}
								}
							}
						}

						if (nodetype == MapConstants.ila) {
							if (!(dbService.getNetworkRouteService().IsNodeOn10GRoute(networkidBF, nodeid) != null)) {
								// this node is not on 10G route now

								List<CardInfo> midamp = dbService.getCardInfoService().FindCardInfoByCardType(
										networkidBF, nodeid, ResourcePlanConstants.Mid_Stage_amplifier);
								for (int k = 0; k < midamp.size(); k++) {
									dbService.getCardInfoService().DeleteCard(networkidBF, nodeid,
											midamp.get(k).getRack(), midamp.get(k).getSbrack(),
											midamp.get(k).getCard());
									fSaveCardInDb(networkidBF, nodeid, midamp.get(k).getRack(),
											midamp.get(k).getSbrack(), midamp.get(k).getCard(),
											ResourcePlanConstants.CardIla, midamp.get(k).getDirection(), 0, 0,
											rpu.fgetEIdFromCardType(ResourcePlanConstants.Mid_Stage_amplifier), "",
											ResourcePlanConstants.ZERO, SrNo);
								}
							}
						}
					}

				}
			}

		}

		/**
		 * For Added routes
		 */
		List<NetworkRoute> routesAdded = dbService.getNetworkRouteService().FindAddedRouteInBrField(networkid,
				networkidBF);
		for (int i = 0; i < routesAdded.size(); i++) {
			if ((routesAdded.get(i).getRoutePriority() == ResourcePlanConstants.ONE)
					| (routesAdded.get(i).getRoutePriority() == ResourcePlanConstants.TWO)) {
				if (routesAdded.get(i).getLineRate().equalsIgnoreCase(MapConstants.Line10)) {
					nodeids = routesAdded.get(i).getPath().split(",");
					for (int j = 0; j < nodeids.length; j++) {
						int nodeid = Integer.parseInt(nodeids[j].toString());
						int nodetype = dbService.getNodeService()
								.FindNode(networkidBF, Integer.parseInt(nodeids[j].toString())).getNodeType();
						if ((j == 0) | (j == nodeids.length - 1)) {
							if (j == nodeids.length - 1) {
								dir = dbService.getLinkService().FindLinkDirection(networkidBF,
										Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										Integer.parseInt(nodeids[nodeids.length - 1 - 1].toString()));
								fInsertDCMModule(networkidBF, Integer.parseInt(nodeids[nodeids.length - 1].toString()),
										dir);
							} else if (j == 0) {
								dir = dbService.getLinkService().FindLinkDirection(networkidBF,
										Integer.parseInt(nodeids[j].toString()),
										Integer.parseInt(nodeids[j + 1].toString()));
								fInsertDCMModule(networkidBF, nodeid, dir);
							}
						} else// for intermediate nodes both direction on route will be taken care
						{
							dir1 = dbService.getLinkService().FindLinkDirection(networkidBF,
									Integer.parseInt(nodeids[j].toString()),
									Integer.parseInt(nodeids[j + 1].toString()));
							fInsertDCMModule(networkidBF, nodeid, dir1);
							dir2 = dbService.getLinkService().FindLinkDirection(networkidBF,
									Integer.parseInt(nodeids[j].toString()),
									Integer.parseInt(nodeids[j - 1].toString()));
							fInsertDCMModule(networkidBF, Integer.parseInt(nodeids[j].toString()), dir2);
						}

						if (j == nodeids.length - 1)// add filter along the muxponder locations at the path ends
						{
							rpu.fsaveTunableFilterDb(networkidBF, nodeid, dir, routesAdded.get(i).getDemandId());
						} else if (j == 0) {
							rpu.fsaveTunableFilterDb(networkidBF, nodeid, dir, routesAdded.get(i).getDemandId());
						}

						// replace ILAs with Mid stage amplifier
						if (nodetype == MapConstants.ila) {
							List<CardInfo> ilas = dbService.getCardInfoService().FindCardInfoByCardType(networkidBF,
									nodeid, ResourcePlanConstants.CardIla);
							for (int k = 0; k < ilas.size(); k++) {
								dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, ilas.get(k).getRack(),
										ilas.get(k).getSbrack(), ilas.get(k).getCard());
								fSaveCardInDb(networkidBF, nodeid, ilas.get(k).getRack(), ilas.get(k).getSbrack(),
										ilas.get(k).getCard(), ResourcePlanConstants.Mid_Stage_amplifier,
										ilas.get(k).getDirection(), 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Mid_Stage_amplifier), "",
										ResourcePlanConstants.ZERO, SrNo);
							}
						}
					}
				}
			}
		}
	}

	public void fInsertDCMTray(int networkid, int nodeid) throws SQLException {
		// CardInfo space =
		// dbService.getCardInfoService().FindfreeSubrackSpace(networkid, nodeid);
		// CardInfo space =
		// dbService.getCardInfoService().FindfreeSubrackSpacenew(networkid, nodeid);
		// CardInfo dcm = dbService.getCardInfoService().FindCard(networkid, nodeid,
		// ResourcePlanConstants.DCM_Tray_Unit,dir);
		CardInfo space = rpu.fgetFreeSbrackSpace(networkid, nodeid);
		int r, s, c;

		if (space != null) {
			r = space.getRack();
			s = space.getSbrack();
			c = space.getCard();
			fSaveCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit), "", ResourcePlanConstants.ZERO, "");
			// initialize module ports
			fSavePortOnCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, 1, 0, "", 0, 0, 0, 0);
			fSavePortOnCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, 2, 0, "", 0, 0, 0, 0);

			logger.info("ResourcePlanning.fsaveDCMTray() added dcm tray for node: " + nodeid + " loc: " + r + " " + s
					+ " " + c);
		} else {
			// get a new rack and fill its first space
			r = space.getRack() + 1;
			s = space.getSbrack();
			c = 1;
			fSaveCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit), "", ResourcePlanConstants.ZERO, "");
			// initialize module ports
			fSavePortOnCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, 1, 0, "", 0, 0, 0, 0);
			fSavePortOnCardInDb(networkid, nodeid, r, s, c, ResourcePlanConstants.DCM_Tray_Unit, 2, 0, "", 0, 0, 0, 0);

			logger.info("ResourcePlanning.fsaveDCMTray() added dcm tray for node: " + nodeid + " loc: " + r + " " + s
					+ " " + c);
		}
		// if(dcm==null)//if dcm for that particular dir doesnot exist add it
		// {
		// if(space==null)
		// {
		// r=1;
		// s=0;
		// c=1;
		// fSaveCardInDb(networkid, nodeid,
		// r,s,c,ResourcePlanConstants.DCM_Tray_Unit,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit),"",ResourcePlanConstants.ZERO);
		// //initialize module ports
		// fSavePortOnCardInDb(networkid, nodeid, r,s,c,
		// ResourcePlanConstants.DCM_Tray_Unit, 1, 0, "", 0,0,0);
		// fSavePortOnCardInDb(networkid, nodeid, r,s,c,
		// ResourcePlanConstants.DCM_Tray_Unit, 2, 0, "", 0,0,0);
		// }
		// else
		// {
		// r= space.getRack();
		// s= space.getSbrack();
		// c= space.getCard();
		// if(c<5)
		// {
		// c=c+1;
		// fSaveCardInDb(networkid, nodeid,
		// r,s,c,ResourcePlanConstants.DCM_Tray_Unit,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit),"",ResourcePlanConstants.ZERO);
		// fSavePortOnCardInDb(networkid, nodeid, r,s,c,
		// ResourcePlanConstants.DCM_Tray_Unit, 1, 0, "", 0,0,0);
		// fSavePortOnCardInDb(networkid, nodeid, r,s,c,
		// ResourcePlanConstants.DCM_Tray_Unit, 2, 0, "", 0,0,0);
		// }
		// else if(c==5)
		// {
		// r=r+1;
		// c=1;
		// fSaveCardInDb(networkid, nodeid,
		// r,s,c,ResourcePlanConstants.DCM_Tray_Unit,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit),"",ResourcePlanConstants.ZERO);
		// fSavePortOnCardInDb(networkid, nodeid, r,s,c,
		// ResourcePlanConstants.DCM_Tray_Unit, 1, 0, "", 0,0,0);
		// fSavePortOnCardInDb(networkid, nodeid, r,s,c,
		// ResourcePlanConstants.DCM_Tray_Unit, 2, 0, "", 0,0,0);
		// }
		// else
		// {
		// //put in next rack space
		// r=r+1;
		// c=c+1;
		// fSaveCardInDb(networkid, nodeid,
		// r,s,c,ResourcePlanConstants.DCM_Tray_Unit,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.DCM_Tray_Unit),"",ResourcePlanConstants.ZERO);
		// fSavePortOnCardInDb(networkid, nodeid, r,s,c,
		// ResourcePlanConstants.DCM_Tray_Unit, 1, 0, "", 0,0,0);
		// fSavePortOnCardInDb(networkid, nodeid, r,s,c,
		// ResourcePlanConstants.DCM_Tray_Unit, 2, 0, "", 0,0,0);
		// }
		// logger.info("ResourcePlanning.fsaveDCMTray() added dcm tray for node:
		// "+nodeid+" loc: "+r+" "+s+" "+c);
		// }
		// }
		// else if(dcm!=null)
		// {
		// logger.info("ResourcePlanning.fAssignDCMTray() Dcm exists for Dir: "+ dir);
		// }
	}

	public void fInsertDCMModule(int networkid, int nodeid, String dir) throws SQLException {
		PortInfo dcmdir = dbService.getPortInfoService().FindDcmModuleByDir(networkid, nodeid, dir);
		if (dcmdir == null)// no dcm module for that direction exists
		{
			PortInfo dcmModule = dbService.getPortInfoService().FindUnassignedDcmModule(networkid, nodeid);
			if (dcmModule == null)// no dcm for that direction exists
			{
				// insert new module and tray
				fInsertDCMTray(networkid, nodeid);
				PortInfo module = dbService.getPortInfoService().FindUnassignedDcmModule(networkid, nodeid);
				logger.info("ResourcePlanning.fInsertDCMModule() No Tray existed ");
				logger.info("ResourcePlanning.fInsertDCMModule() Node " + nodeid + " Direction " + dir);
				logger.info("ResourcePlanning.fInsertDCMModule() r " + module.getRack() + " s " + module.getSbrack()
				+ " c " + module.getCard() + " port " + module.getPort());
				dbService.getPortInfoService().UpdateDcmModule(networkid, nodeid, module.getRack(), module.getSbrack(),
						module.getCard(), module.getPort(), dir);
			} else // one unassigned module exists in tray
			{
				logger.info("ResourcePlanning.fInsertDCMModule() Tray existed ");
				PortInfo module = dbService.getPortInfoService().FindUnassignedDcmModule(networkid, nodeid);
				logger.info("ResourcePlanning.fInsertDCMModule() Node " + nodeid + " Direction " + dir);
				logger.info("ResourcePlanning.fInsertDCMModule() r " + module.getRack() + " s " + module.getSbrack()
				+ " c " + module.getCard() + " port " + module.getPort());
				dbService.getPortInfoService().UpdateDcmModule(networkid, nodeid, module.getRack(), module.getSbrack(),
						module.getCard(), module.getPort(), dir);
			}
		} else {
			// do nothing
		}

	}

	public void fsaveTunableFilter(int networkid, int nodeid, String dir, int demandid) throws SQLException {
		NodeSystem assignedSys;
		if (assignedmap.get(nodeid) != null) {
			assignedSys = assignedmap.get(nodeid);
		} else {
			assignedSys = new NodeSystem(nodeid);
		}

		NodeSystem nodepool;
		nodepool = poolmap.get(nodeid);
		String location = fGetFirstFreeSlot(networkid, nodeid);
		logger.info(" fsaveTunableFilter() location found : " + location);

		if (((location.contains("_6")/* |(location.contains("_9")) */))) {
			// add mpc first
			int[] id = rpu.locationIds(location);
			assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], ResourcePlanConstants.CardMpc);
			fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO, "");
			fRemoveCard(nodepool, id[0], id[1], id[2], ResourcePlanConstants.CardMpc);

			// //add mpc second
			// String locationmpc2 = rpu.locationStr(id[0],id[1],9);
			// logger.info("ResourcePlanning.fsaveTunableFilter() locationmpc2
			// "+locationmpc2);
			// int[] idm=rpu.locationIds(locationmpc2);
			// assignedSys=fAssignCard(assignedSys,idm[0],idm[1],idm[2],ResourcePlanConstants.CardMpc);
			// fSaveCardInDb(networkid, nodeid,
			// idm[0],idm[1],idm[2],ResourcePlanConstants.CardMpc,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc),"",ResourcePlanConstants.ZERO);
			// fRemoveCard(nodepool,idm[0],idm[1],idm[2],ResourcePlanConstants.CardMpc);

			poolmap.remove(nodeid);
			poolmap.put(nodeid, nodepool);
			assignedmap.remove(nodeid);
			assignedmap.put(nodeid, assignedSys);

			// add filter card
			String location2 = fGetFirstFreeSlot(networkid, nodeid);
			logger.info(" location found filter: " + location2);
			int[] idt = rpu.locationIds(location2);
			assignedSys = fAssignCard(assignedSys, idt[0], idt[1], idt[2], ResourcePlanConstants.Tunable_Filter_Card);
			fSaveCardInDb(networkid, nodeid, idt[0], idt[1], idt[2], ResourcePlanConstants.Tunable_Filter_Card, "", 0,
					demandid, rpu.fgetEIdFromCardType(ResourcePlanConstants.Tunable_Filter_Card), "",
					ResourcePlanConstants.ZERO, "");
			fRemoveCard(nodepool, idt[0], idt[1], idt[2], ResourcePlanConstants.Tunable_Filter_Card);
		} else {
			int[] id = rpu.locationIds(location);
			assignedSys = fAssignCard(assignedSys, id[0], id[1], id[2], ResourcePlanConstants.Tunable_Filter_Card);
			fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.Tunable_Filter_Card, "", 0,
					demandid, rpu.fgetEIdFromCardType(ResourcePlanConstants.Tunable_Filter_Card), "",
					ResourcePlanConstants.ZERO, "");
			fRemoveCard(nodepool, id[0], id[1], id[2], ResourcePlanConstants.Tunable_Filter_Card);
			poolmap.remove(nodeid);
			poolmap.put(nodeid, nodepool);
			assignedmap.remove(nodeid);
			assignedmap.put(nodeid, assignedSys);
		}
	}

	public void fsaveTunableFilterDb(int networkid, int nodeid, String dir, int demandid) throws SQLException {

		String location = rpu.fGetFirstFreeSlotDb(networkid, nodeid);
		logger.info(" fsaveTunableFilter() location found : " + location);
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		String MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);

		if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
			if (((location.contains("_6") | (location.contains("_9"))))) {
				// add mpc first
				int[] id = rpu.locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO, "");

				// add filter card
				String location2 = rpu.fGetFirstFreeSlotDb(networkid, nodeid);
				logger.info(" location found filter: " + location2);
				int[] idt = rpu.locationIds(location2);
				fSaveCardInDb(networkid, nodeid, idt[0], idt[1], idt[2], ResourcePlanConstants.Tunable_Filter_Card, "",
						0, demandid, rpu.fgetEIdFromCardType(ResourcePlanConstants.Tunable_Filter_Card), "",
						ResourcePlanConstants.ZERO, "");

			} else {
				int[] id = rpu.locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.Tunable_Filter_Card, "", 0,
						demandid, rpu.fgetEIdFromCardType(ResourcePlanConstants.Tunable_Filter_Card), "",
						ResourcePlanConstants.ZERO, "");
			}
		} else {
			if (((location.contains("_6")/* |(location.contains("_9")) */))) {
				// add mpc first
				int[] id = rpu.locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO, "");

				// add filter card
				String location2 = rpu.fGetFirstFreeSlotDb(networkid, nodeid);
				logger.info(" location found filter: " + location2);
				int[] idt = rpu.locationIds(location2);
				fSaveCardInDb(networkid, nodeid, idt[0], idt[1], idt[2], ResourcePlanConstants.Tunable_Filter_Card, "",
						0, demandid, rpu.fgetEIdFromCardType(ResourcePlanConstants.Tunable_Filter_Card), "",
						ResourcePlanConstants.ZERO, "");

			} else {
				int[] id = rpu.locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.Tunable_Filter_Card, "", 0,
						demandid, rpu.fgetEIdFromCardType(ResourcePlanConstants.Tunable_Filter_Card), "",
						ResourcePlanConstants.ZERO, "");
			}
		}

	}

	/**
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignPortsOnCard(int networkid) throws SQLException {
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		for (int k = 0; k < nodelist.size(); k++) {
			int nodeid = nodelist.get(k);
			fAssignControllerCardPorts(networkid, nodeid);
			fAssignMPNPorts(networkid, nodeid);
		}
	}

	/**
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignPortsOnCardBrField(int networkid) throws SQLException {
		logger.info("fAssignPortsOnCardBrField");
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkidBF);
		for (int k = 0; k < nodelist.size(); k++) {
			int nodeid = nodelist.get(k);
			fAssignMPNPortsBrField(networkid, nodeid);
			fAssignControllerCardPortsBrField(networkid, nodeid);
		}
	}

	/**
	 * Assigns Client ports on MPN/TPN except 5x10G TPN
	 * 
	 * @param networkid
	 * @param nodeid
	 * @throws SQLException
	 */
	public void fAssignMPNPorts(int networkid, int nodeid) throws SQLException {
		List<CardInfo> mpns = dbService.getCardInfoService().FindMpnsExcept5x10G(networkid, nodeid);
		String DirP = "", path = "" + nodeid;
		String pnodeids[];
		for (int i = 0; i < mpns.size(); i++) {
			int demandid = mpns.get(i).getDemandId();
			Demand d = dbService.getDemandService().FindDemand(networkid, demandid);
			String cirSet = d.getCircuitSet();
			// logger.info("ResourcePlanning.fAssignMPNPorts() demand id "+d.getDemandId());
			// logger.info("ResourcePlanning.fAssignMPNPorts() r "+ mpns.get(i).getRack()+"
			// s "+mpns.get(i).getSbrack()+" c "+mpns.get(i).getCard());
			String cirs[] = cirSet.split(",");
			System.out.println("Index:" + i+" Card Type for MPN port ::" + mpns.get(i).getCardType() + " Mpn nodeid:" + nodeid+ " Mpn Demand:" + mpns.get(i).getDemandId()+ " Mpn Direction:" + mpns.get(i).getDirection());

			if (d.getChannelProtection().equalsIgnoreCase(ResourcePlanConstants.Yes)) {

				NetworkRoute nr =
						dbService.getNetworkRouteService().FindAllByDemandId(networkid,
								demandid, ResourcePlanConstants.TWO);

				if(nr!=null)
				{
					System.out.println("Nr not null:"+nr.toString());
					pnodeids=nr.getPath().split(",");
					if(nodeid==Integer.parseInt(pnodeids[0].toString()))
						DirP =
						dbService.getLinkService().FindLinkDirection(networkid,Integer.parseInt(pnodeids[0].toString()),Integer.parseInt(pnodeids[0+1].toString()));
					else if(nodeid==Integer.parseInt(pnodeids[pnodeids.length-1].toString()))
						DirP =
						dbService.getLinkService().FindLinkDirection(networkid,Integer.parseInt(pnodeids[pnodeids.length-1].toString()),Integer.parseInt(pnodeids[pnodeids.length-1-1].toString()));
				}
			}

			//			if (d.getChannelProtection().equalsIgnoreCase(ResourcePlanConstants.Yes)) {
			//
			//				List<NetworkRoute> demandroutes = dbService.getNetworkRouteService().FindAllByDemandId(networkid,
			//						demandid);
			//				System.out.println("demandRoutes" + demandroutes.toString());
			//				for (NetworkRoute nr : demandroutes) {
			//					if (nr.getRoutePriority() == 2) {
			//						path = nr.getPath();
			//					}
			//				}
			//				System.out.println("Path::" + path);
			//
			//				String[] pathNodes = path.split(",");
			//
			//				System.out.println("pathNodes::" + pathNodes[0] + "  " + pathNodes[pathNodes.length - 1]);
			//				Link l;
			//				if(pathNodes.length!=0){
			//					if (Integer.parseInt(pathNodes[0].toString()) == nodeid) {
			//						l = dbService.getLinkService().FindLink(networkid, Integer.parseInt(pathNodes[0].toString().trim()),
			//								Integer.parseInt(pathNodes[1].toString().trim()));
			//						Link link = dbService.getLinkService().FindLink(networkid, l.getLinkId());
			//						System.out.println("Src Node link" + link.getDestNode() + "  " + link.getSrcNode() + " linkid"
			//								+ link.getLinkId());
			//						if (link.getSrcNode() == nodeid)
			//							DirP = link.getSrcNodeDirection();
			//						else
			//							DirP = link.getDestNodeDirection();
			//					} else if (Integer.parseInt(pathNodes[pathNodes.length - 1].toString()) == nodeid) {
			//						l = dbService.getLinkService().FindLink(networkid,
			//								Integer.parseInt(pathNodes[pathNodes.length - 2].toString().trim()),
			//								Integer.parseInt(pathNodes[pathNodes.length - 1].toString().trim()));
			//						Link link = dbService.getLinkService().FindLink(networkid, l.getLinkId());
			//						System.out.println("Src Node link" + link.getDestNode() + "  " + link.getSrcNode() + " linkid"
			//								+ link.getLinkId());
			//						if (link.getSrcNode() == nodeid)
			//							DirP = link.getSrcNodeDirection();
			//						else
			//							DirP = link.getDestNodeDirection();
			//					}
			//				}				
			//
			//				System.out.println("Sec Direction ::" + DirP);
			//			}

			// direction of protection path 
			System.out.println("Protection Direction ::" + DirP);

			for (int j = 0; j < cirs.length; j++) {

				boolean isTenGAggCircuit=dbService.getCircuit10gAggService().FindAllCircuit10gAgg(networkid, Integer.parseInt(cirs[j])).size()>0?true:false;
				Circuit cir = dbService.getCircuitService().FindCircuit(networkid, Integer.parseInt(cirs[j]));
				boolean is10GTpnCircuit=Float.parseFloat(cir.getLineRate().toString())==cir.getRequiredTraffic()?true:false;
				System.out.println("Circuit :"+cir.getCircuitId()+" is10GTpnCircuit"+is10GTpnCircuit);

				if(isTenGAggCircuit && mpns.get(i).getStatus().equals(ResourcePlanConstants.Protection)) {
					CardInfo mpn10GCard=dbService.getCardInfoService().FindMpn(networkid, nodeid, demandid,Integer.parseInt(cirs[j]), "A");
					System.out.println("!0g CardType ::"+mpn10GCard.getRack()+"-"+mpn10GCard.getSbrack()+"-"+mpn10GCard.getCard());
					fAssign10gAggPorts(networkid,nodeid,mpn10GCard,Integer.parseInt(cirs[j]),j+1,ResourcePlanConstants.SECONDARY_LINE_PORT,mpns.get(i).getDirection());
				}else if(isTenGAggCircuit){
					CardInfo mpn10GCard=dbService.getCardInfoService().FindMpn(networkid, nodeid, demandid,Integer.parseInt(cirs[j]), "A");
					System.out.println("!0g CardType ::"+mpn10GCard.getRack()+"-"+mpn10GCard.getSbrack()+"-"+mpn10GCard.getCard());
					fAssign10gAggPorts(networkid,nodeid,mpn10GCard,Integer.parseInt(cirs[j]),j+1,ResourcePlanConstants.PRIMARY_LINE_PORT,mpns.get(i).getDirection());
				}

				// 10g Mpn used as Tpn is already assigned port while assigning a card to the same demand
				// Check implementation in fAssignMuxPonderCardsDb api call under linerate 10G case
				if(!is10GTpnCircuit) {
					PortInfo port = new PortInfo();
					//					port.setNodeKey("" + networkid + "_" + nodeid);
					port.setNetworkId(networkid);
					port.setNodeId(nodeid);
					port.setCircuitId(Integer.parseInt(cirs[j]));
					port.setDemandId(mpns.get(i).getDemandId());
					port.setRack(mpns.get(i).getRack());
					port.setSbrack(mpns.get(i).getSbrack());
					port.setCard(mpns.get(i).getCard());
					port.setCardType(mpns.get(i).getCardType());
					port.setLinePort(ResourcePlanConstants.PRIMARY_LINE_PORT);

					// If protection by olp and mpn combination
					if(d.getChannelProtection().equals(MapConstants.Yes) && d.getClientProtectionType().equals(ResourcePlanConstants.OLPProtection))
					{
						//						CardInfo olp=dbService.getCardInfoService().FindCardInfo(networkid, nodeid, demandid, ResourcePlanConstants.OLPProtection);
						//						port.setRack(olp.getRack());
						//						port.setSbrack(olp.getSbrack());
						//						port.setCard(olp.getCard());
						//						port.setCardType(ResourcePlanConstants.OLPProtection);
						port.setLinePort(ResourcePlanConstants.OLP_PRIMARY_LINE_PORT);
						System.out.println("Setting OLP line port to :"+port.getLinePort());
					}	
					// If line side protection is using any MPN card
					//					else {
					//						port.setRack(mpns.get(i).getRack());
					//						port.setSbrack(mpns.get(i).getSbrack());
					//						port.setCard(mpns.get(i).getCard());
					//						port.setCardType(mpns.get(i).getCardType());
					//						port.setLinePort(ResourcePlanConstants.PRIMARY_LINE_PORT);
					//					}

					port.setPort(j + 1);//Debug

					port.setEquipmentId(rpu.fgetSfpEid(networkid, nodeid, ResourcePlanConstants.ParamCircuit,
							cir.getCircuitId(), cir.getTrafficType()));
					port.setDirection(mpns.get(i).getDirection());
					System.out.println("Insert olp port:"+port.toString());
					dbService.getPortInfoService().Insert(port);


					//				if (mpns.get(i).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX)
					//						| mpns.get(i).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {

					// If Channel protection and ptc path exist

					if (d.getChannelProtection().equals(MapConstants.Yes)) {
						port.setDirection(DirP);

						if(d.getClientProtectionType().equals(ResourcePlanConstants.OLPProtection))

						{
							//							System.out.println("Setting sec line port to one with dir:"+DirP);
							CardInfo olp=dbService.getCardInfoService().FindCardInfo(networkid, nodeid, demandid, ResourcePlanConstants.OLPProtection);
							port.setRack(olp.getRack());
							port.setSbrack(olp.getSbrack());
							port.setCard(olp.getCard());
							port.setCardType(ResourcePlanConstants.OLPProtection);
							port.setLinePort(ResourcePlanConstants.OLP_SECONDARY_LINE_PORT);
						}
						else port.setLinePort(ResourcePlanConstants.SECONDARY_LINE_PORT);

						port.setPort(j + 1);// Debug
						dbService.getPortInfoService().Insert(port);
					}	

				}
			}
		}
	}

	public void fAssign10gAggPorts(int networkid,int nodeid,CardInfo mpn,int circuitId,int mpnPortNo,int linePort,String dir) {
		Demand d=dbService.getDemandService().FindDemand(networkid, mpn.getDemandId());
		List<Circuit10gAgg> TenGCircuits=dbService.getCircuit10gAggService().FindAllCircuit10gAgg(networkid, circuitId);
		System.out.println("10G circuits for nodeid "+nodeid+" count:"+TenGCircuits.size()+" circuit id:"+circuitId);
		for(int i=0;i<TenGCircuits.size();i++) {
			int circuitIdTenGClient=TenGCircuits.get(i).getCircuit10gAggId();
			String trafficType=TenGCircuits.get(i).getTrafficType();
			System.out.println("10G traffic "+circuitIdTenGClient+" type:"+trafficType);
			PortInfo port = new PortInfo();
			//			port.setNodeKey("" + networkid + "_" + nodeid);
			port.setNetworkId(networkid);
			port.setNodeId(nodeid);
			port.setCircuitId(circuitIdTenGClient);
			port.setDemandId(mpn.getDemandId());
			port.setRack(mpn.getRack());
			port.setSbrack(mpn.getSbrack());
			port.setCard(mpn.getCard());
			port.setCardType(mpn.getCardType());
			port.setLinePort(linePort);
			if(linePort==ResourcePlanConstants.PRIMARY_LINE_PORT)
				port.setPort(i + 1);//Debug
			else port.setPort(i + 1+8);//Debug
			port.setMpnPortNo(mpnPortNo);//Debug
			try {
				port.setEquipmentId(rpu.fgetSfpEid(networkid, nodeid, ResourcePlanConstants.ParamCircuit,
						circuitIdTenGClient, trafficType));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			port.setDirection(dir);
			System.out.println("Insert olp port:"+port.toString());
			try {
				dbService.getPortInfoService().Insert(port);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Assigns Client ports on MPN/TPN for new demands and modified circuit-set
	 * demands in brown field
	 * 
	 * @param networkid
	 * @param nodeid
	 * @throws SQLException
	 */
	public void fAssignMPNPortsBrField(int networkid, int nodeid) throws SQLException {
		logger.info("fAssignMPNPortsBrField");
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		String pnodeids[];
		String DirP="";
		/**
		 * For Deleted Demands
		 */
		List<Demand> demandsDel = dbService.getDemandService().FindDeletedDemandsInBrField(networkid, networkidBF,
				nodeid);
		for (int i = 0; i < demandsDel.size(); i++) {
			if (!rpu.Is10GTpnDemand(demandsDel.get(i)))// do port deletion for non 10G TPN demands only, as port
				// deletion for 10G TPN is already done in TPN handling
			{
				logger.info("ResourcePlanning.fAssignMPNPortsBrField() Nw: " + networkidBF + " node: " + nodeid
						+ " demand: " + demandsDel.get(i).getDemandId());
				dbService.getPortInfoService().DeletePort(networkidBF, nodeid, demandsDel.get(i).getDemandId());
			}
		}

		/**
		 * For Added Demands
		 */
		// get the added demands and their associated mpns
		List<Demand> demandsnew = dbService.getDemandService().FindAddedDemandsInBrField(networkid, networkidBF,
				nodeid);
		for (int i = 0; i < demandsnew.size(); i++) {
			if (!rpu.Is10GTpnDemand(demandsnew.get(i)))// do port allocation for non 10G TPN demands only, as port
				// allocation for 10G TPN is already done in TPN allocation
			{
				String cirSet = demandsnew.get(i).getCircuitSet();
				String cirs[] = cirSet.split(",");
				CardInfo mpn = dbService.getCardInfoService().FindMpn(networkidBF, nodeid,
						demandsnew.get(i).getDemandId(), ResourcePlanConstants.Working);

				// If channel protection then find protection channel direction
				if(demandsnew.get(i).getChannelProtection().equals(ResourcePlanConstants.Yes)){

					NetworkRoute nr =
							dbService.getNetworkRouteService().FindAllByDemandId(networkidBF,
									demandsnew.get(i).getDemandId(), ResourcePlanConstants.TWO);

					if(nr!=null)
					{
						pnodeids=nr.getPath().split(",");
						if(nodeid==Integer.parseInt(pnodeids[0].toString()))
							DirP =
							dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(pnodeids[0].toString()),Integer.parseInt(pnodeids[0+1].toString()));
						else if(nodeid==Integer.parseInt(pnodeids[pnodeids.length-1].toString()))
							DirP =
							dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(pnodeids[pnodeids.length-1].toString()),Integer.parseInt(pnodeids[pnodeids.length-1-1].toString()));
					}
				}

				if (mpn != null) {
					logger.info("ResourcePlanning.fAssignMPNPortsBrField() Adding Port For: " + mpn.toString());
					for (int j = 0; j < cirs.length; j++) {

						boolean isTenGAggCircuit=dbService.getCircuit10gAggService().FindAllCircuit10gAgg(networkidBF, Integer.parseInt(cirs[j])).size()>0?true:false;
						Circuit cir = dbService.getCircuitService().FindCircuit(networkidBF, Integer.parseInt(cirs[j]));

						if(isTenGAggCircuit){
							CardInfo mpn10GCard=dbService.getCardInfoService().FindMpn(networkidBF, nodeid, demandsnew.get(i).getDemandId(),Integer.parseInt(cirs[j]), "A");
							System.out.println("10g CardType ::"+mpn10GCard.getRack()+"-"+mpn10GCard.getSbrack()+"-"+mpn10GCard.getCard());
							fAssign10gAggPorts(networkidBF,nodeid,mpn10GCard,Integer.parseInt(cirs[j]),j+1,ResourcePlanConstants.PRIMARY_LINE_PORT,mpn.getDirection());
						}

						PortInfo port = new PortInfo();
						//						port.setNodeKey("" + networkidBF + "_" + nodeid);
						port.setNetworkId(networkidBF);
						port.setNodeId(nodeid);
						port.setCircuitId(Integer.parseInt(cirs[j]));
						port.setRack(mpn.getRack());
						port.setSbrack(mpn.getSbrack());
						port.setCard(mpn.getCard());
						port.setCardType(mpn.getCardType());
						port.setDemandId(mpn.getDemandId());

						if(demandsnew.get(i).getChannelProtection().equals(ResourcePlanConstants.Yes) 
								&& demandsnew.get(i).getClientProtectionType().equals(ResourcePlanConstants.OLPProtection))
							port.setLinePort(ResourcePlanConstants.OLP_PRIMARY_LINE_PORT);
						else port.setLinePort(ResourcePlanConstants.PRIMARY_LINE_PORT);

						System.out.println("Port first free:"+port.toString());
						int nextFreePort=dbService.getPortInfoService().GetFirstFreePortId(port);
						port.setPort(nextFreePort);

						port.setEquipmentId(rpu.fgetSfpEid(networkidBF, nodeid, ResourcePlanConstants.ParamCircuit,
								cir.getCircuitId(), cir.getTrafficType()));
						port.setDirection(mpn.getDirection());
						dbService.getPortInfoService().Insert(port);

						if(demandsnew.get(i).getChannelProtection().equals(ResourcePlanConstants.Yes)){

							// Set Same port number as primary line in case of channel ptc : New Demand Case in Br Field
							//							PortInfo primaryLinePortInfo=dbService.getPortInfoService().FindLineDirectionInfo(networkidBF, nodeid, port.getRack(), port.getSbrack(), port.getCard(), ResourcePlanConstants.PRIMARY_LINE_PORT);
							//							System.out.println("OPX Port Sec:"+primaryLinePortInfo.getPort());
							//							port.setPort(primaryLinePortInfo.getPort());

							System.out.println("OPX Port Sec:"+port.getPort());

							// If OLP card is used for channel ptc , then add secondary line using olp card info
							if(demandsnew.get(i).getClientProtectionType().equals(ResourcePlanConstants.OLPProtection))
							{
								CardInfo olp=dbService.getCardInfoService().FindCardInfo(networkidBF, nodeid, demandsnew.get(i).getDemandId(), ResourcePlanConstants.OLPProtection);
								port.setRack(olp.getRack());
								port.setSbrack(olp.getSbrack());
								port.setCard(olp.getCard());
								port.setCardType(ResourcePlanConstants.OLPProtection);
								port.setLinePort(ResourcePlanConstants.OLP_SECONDARY_LINE_PORT);
							}
							//else store secondary line with mpn card info
							else port.setLinePort(ResourcePlanConstants.SECONDARY_LINE_PORT);

							port.setDirection(DirP);
							dbService.getPortInfoService().Insert(port);
						}
					}


					CardInfo mpnP = dbService.getCardInfoService().FindMpn(networkidBF, nodeid,
							demandsnew.get(i).getDemandId(), ResourcePlanConstants.Protection);
					if (mpnP != null) {
						for (int j = 0; j < cirs.length; j++) {

							//							boolean isTenGAggCircuit=dbService.getCircuit10gAggService().FindAllCircuit10gAgg(networkidBF, Integer.parseInt(cirs[j])).size()>0?true:false;
							Circuit cir = dbService.getCircuitService().FindCircuit(networkidBF, Integer.parseInt(cirs[j]));
							//													
							//							if(isTenGAggCircuit){
							//								CardInfo mpn10GCard=dbService.getCardInfoService().FindMpn(networkidBF, nodeid, demandsnew.get(i).getDemandId(),Integer.parseInt(cirs[j]), "A");
							//								System.out.println("10g CardType ::"+mpn10GCard.getRack()+"-"+mpn10GCard.getSbrack()+"-"+mpn10GCard.getCard());
							//								fAssign10gAggPorts(networkidBF,nodeid,mpn10GCard,Integer.parseInt(cirs[j]),j+1,ResourcePlanConstants.SECONDARY_LINE_PORT,mpnP.getDirection());
							//							}

							PortInfo port = new PortInfo();
							//							port.setNodeKey("" + networkidBF + "_" + nodeid);
							port.setNetworkId(networkidBF);
							port.setNodeId(nodeid);
							port.setCircuitId(Integer.parseInt(cirs[j]));
							port.setRack(mpnP.getRack());
							port.setSbrack(mpnP.getSbrack());
							port.setCard(mpnP.getCard());
							port.setCardType(mpnP.getCardType());
							port.setDemandId(mpnP.getDemandId());
							port.setLinePort(ResourcePlanConstants.PRIMARY_LINE_PORT);

							port.setEquipmentId(rpu.fgetSfpEid(networkidBF, nodeid, ResourcePlanConstants.ParamCircuit,
									cir.getCircuitId(), cir.getTrafficType()));
							port.setDirection(mpnP.getDirection());
							dbService.getPortInfoService().Insert(port);
						}
					}

				}
			}
		}

		/**
		 * For Circuit Set modified demands
		 */
		List<Demand> demandsModfd = dbService.getDemandService().FindModifiedCircuitSetDemandsInBrField(networkid,
				networkidBF, nodeid);
		for (int i = 0; i < demandsModfd.size(); i++) {
			logger.info("ResourcePlanning.fAssignMPNPortsBrField() Modified " + demandsModfd.get(i).getDemandId());
			Demand dem = dbService.getDemandService().FindDemand(networkid, demandsModfd.get(i).getDemandId());
			String cirSetBf = demandsModfd.get(i).getCircuitSet();
			String cirSetGf = dem.getCircuitSet();
			CardInfo mpn = dbService.getCardInfoService().FindMpn(networkidBF, nodeid,
					demandsModfd.get(i).getDemandId(), ResourcePlanConstants.Working);

			// If channel protection then find protection channel direction
			if(demandsModfd.get(i).getChannelProtection().equals(ResourcePlanConstants.Yes)){

				NetworkRoute nr =
						dbService.getNetworkRouteService().FindAllByDemandId(networkidBF,
								demandsModfd.get(i).getDemandId(), ResourcePlanConstants.TWO);

				if(nr!=null)
				{
					pnodeids=nr.getPath().split(",");
					if(nodeid==Integer.parseInt(pnodeids[0].toString()))
						DirP =
						dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(pnodeids[0].toString()),Integer.parseInt(pnodeids[0+1].toString()));
					else if(nodeid==Integer.parseInt(pnodeids[pnodeids.length-1].toString()))
						DirP =
						dbService.getLinkService().FindLinkDirection(networkidBF,Integer.parseInt(pnodeids[pnodeids.length-1].toString()),Integer.parseInt(pnodeids[pnodeids.length-1-1].toString()));
				}
			}

			if (mpn != null) {
				Object addedCirs[] = rpu.fGetAddedCircuits(cirSetGf, cirSetBf);

				for (int k = 0; k < addedCirs.length; k++) {
					if (!addedCirs[k].toString().equalsIgnoreCase("")) {
						PortInfo port = new PortInfo();
						//						port.setNodeKey("" + networkidBF + "_" + nodeid);
						port.setNetworkId(networkidBF);
						port.setNodeId(nodeid);
						port.setCircuitId(Integer.parseInt(addedCirs[k].toString()));
						port.setRack(mpn.getRack());
						port.setSbrack(mpn.getSbrack());
						port.setCard(mpn.getCard());
						port.setCardType(mpn.getCardType());
						port.setDemandId(mpn.getDemandId());

						if(demandsModfd.get(i).getChannelProtection().equals(ResourcePlanConstants.Yes) 
								&& demandsModfd.get(i).getClientProtectionType().equals(ResourcePlanConstants.OLPProtection))
							port.setLinePort(ResourcePlanConstants.OLP_PRIMARY_LINE_PORT);
						else port.setLinePort(ResourcePlanConstants.PRIMARY_LINE_PORT);						
						//						port.setLinePort(101);

						// Used for secondary line port number in case of channel protection 
						int nextFreePort=Integer.parseInt(dbService.getPortInfoService().GetFirstFreePortId(port).toString());
						port.setPort(nextFreePort);

						Circuit cir = dbService.getCircuitService().FindCircuit(networkidBF,
								Integer.parseInt(addedCirs[k].toString()));
						port.setEquipmentId(rpu.fgetSfpEid(networkid, nodeid, ResourcePlanConstants.ParamCircuit,
								cir.getCircuitId(), cir.getTrafficType()));
						port.setDirection(mpn.getDirection());
						dbService.getPortInfoService().Insert(port);
						logger.info("ResourcePlanning.fAssignMPNPortsBrField()" + port.toString());

						if(demandsModfd.get(i).getChannelProtection().equals(ResourcePlanConstants.Yes)){

							if(demandsModfd.get(i).getClientProtectionType().equals(ResourcePlanConstants.OLPProtection))
							{
								CardInfo olp=dbService.getCardInfoService().FindCardInfo(networkid, nodeid, demandsModfd.get(i).getDemandId(), ResourcePlanConstants.OLPProtection);
								port.setRack(olp.getRack());
								port.setSbrack(olp.getSbrack());
								port.setCard(olp.getCard());
								port.setCardType(ResourcePlanConstants.OLPProtection);
								port.setLinePort(ResourcePlanConstants.OLP_SECONDARY_LINE_PORT);
							}
							else port.setLinePort(ResourcePlanConstants.SECONDARY_LINE_PORT);

							port.setDirection(DirP);
							dbService.getPortInfoService().Insert(port);
						}
					}

					CardInfo mpnP = dbService.getCardInfoService().FindMpn(networkidBF, nodeid,
							demandsModfd.get(i).getDemandId(), ResourcePlanConstants.Protection);
					if (mpnP != null) {
						if (!addedCirs[k].toString().equalsIgnoreCase("")) {
							PortInfo port1 = new PortInfo();
							//							port1.setNodeKey("" + networkidBF + "_" + nodeid);
							port1.setNetworkId(networkidBF);
							port1.setNodeId(nodeid);
							port1.setCircuitId(Integer.parseInt(addedCirs[k].toString()));
							port1.setRack(mpnP.getRack());
							port1.setSbrack(mpnP.getSbrack());
							port1.setCard(mpnP.getCard());
							port1.setCardType(mpnP.getCardType());
							port1.setDemandId(mpnP.getDemandId());
							port1.setLinePort(101);
							Circuit cir1 = dbService.getCircuitService().FindCircuit(networkidBF,
									Integer.parseInt(addedCirs[k].toString()));
							port1.setEquipmentId(rpu.fgetSfpEid(networkid, nodeid, ResourcePlanConstants.ParamCircuit,
									cir1.getCircuitId(), cir1.getTrafficType()));
							port1.setDirection(mpnP.getDirection());
							dbService.getPortInfoService().Insert(port1);
						}
					}
				}

				Object deletedCirs[] = rpu.fGetDeletedCircuits(cirSetGf, cirSetBf);
				logger.info("ResourcePlanning.fAssignMPNPortsBrField() deleted cirs size " + deletedCirs.length);
				for (int k = 0; k < deletedCirs.length; k++) {
					logger.info("ResourcePlanning.fAssignMPNPortsBrField() deleted cirs id "
							+ Integer.parseInt(deletedCirs[k].toString()));
					Demand d=dbService.getDemandService().FindDemand(networkidBF, mpn.getDemandId());
					System.out.println("Deleted Circuit:"+deletedCirs[k]+" Demand Ch Ptc:"+d.getChannelProtection());

					if(d.getChannelProtection().equals(ResourcePlanConstants.Yes)){
						List<PortInfo> portsList=dbService.getPortInfoService().FindPortInfoList(networkidBF, nodeid, mpn.getRack(),
								mpn.getSbrack(), mpn.getCard(), Integer.parseInt(deletedCirs[k].toString()));

						portsList.forEach(p->{
							dbService.getPortInfoService().DeletePort(networkidBF, nodeid, mpn.getRack(), mpn.getSbrack(),
									mpn.getCard(), p.getPort());
						});
					}else
					{
						PortInfo portInfo = dbService.getPortInfoService().FindPortInfo(networkidBF, nodeid, mpn.getRack(),
								mpn.getSbrack(), mpn.getCard(), Integer.parseInt(deletedCirs[k].toString()));
						dbService.getPortInfoService().DeletePort(networkidBF, nodeid, mpn.getRack(), mpn.getSbrack(),
								mpn.getCard(), portInfo.getPort());

						CardInfo mpnP = dbService.getCardInfoService().FindMpn(networkidBF, nodeid,
								demandsModfd.get(i).getDemandId(), ResourcePlanConstants.Protection);
						if (mpnP != null) {
							PortInfo portInfo1 = dbService.getPortInfoService().FindPortInfo(networkidBF, nodeid,
									mpnP.getRack(), mpnP.getSbrack(), mpnP.getCard(),
									Integer.parseInt(deletedCirs[k].toString()));
							dbService.getPortInfoService().DeletePort(networkidBF, nodeid, mpnP.getRack(), mpnP.getSbrack(),
									mpnP.getCard(), portInfo1.getPort());
						}

					}		
				}
			}
		}
	}

	/**
	 * Assigns ports on TPN cards
	 * 
	 * @param networkid
	 * @param nodeid
	 * @throws SQLException
	 */
	public void fAssign5x10GTPNDemand(int networkid, int nodeid, int demandid, int rack, int sbrack, int card,
			String direction, String cardType) throws SQLException {
		// setting line port no equal to port no
		logger.info("fAssign5x10GTPNDemand");
		Demand d = dbService.getDemandService().FindDemand(networkid, demandid);
		PortInfo port = new PortInfo();
		//		port.setNodeKey("" + networkid + "_" + nodeid);
		port.setNetworkId(networkid);
		port.setNodeId(nodeid);
		port.setCircuitId(Integer.parseInt(d.getCircuitSet().toString()));
		port.setRack(rack);
		port.setSbrack(sbrack);
		port.setCard(card);
		// port.setCardType(ResourcePlanConstants.CardTPN5x10G);
		port.setCardType(cardType);
		port.setDemandId(demandid);

		// If 10G XGM is used in place of 5x10G card
		if(cardType.equals(ResourcePlanConstants.CardMuxponder10G))
		{
			port.setLinePort(ResourcePlanConstants.SECONDARY_LINE_PORT);
			port.setPort(ResourcePlanConstants.PRIMARY_LINE_PORT);
		}

		Circuit cir = dbService.getCircuitService().FindCircuit(networkid,
				Integer.parseInt(d.getCircuitSet().toString()));
		port.setEquipmentId(rpu.fgetSfpEid(networkid, nodeid, ResourcePlanConstants.ParamCircuit, cir.getCircuitId(),
				cir.getTrafficType()));
		port.setDirection(direction);
		dbService.getPortInfoService().Insert(port);
	}

	/**
	 * Assigns Client ports on MPN/TPN
	 * 
	 * @param networkid
	 * @param nodeid
	 * @throws SQLException
	 */
	public void fAssignControllerCardPorts(int networkid, int nodeid) throws SQLException {
		logger.info("Assigning Controller Card ports for NodeId:" + networkid + "_" + nodeid);
		// List<Map<String, Object>>
		// racks=dbService.getCardInfoService().FindRacks(networkid,nodeid);
		//
		// for (int j = 1; j < racks.size(); j++)
		// {
		//// logger.info("ResourcePlanning.fAssignControllerCardPorts() Rack id:
		// "+racks.get(j).get("Rack").toString());
		// List<CardInfo> cscc =
		// dbService.getCardInfoService().FindCardInfoByCardType(networkid,nodeid,ResourcePlanConstants.CardCscc);
		// for (int i = 0; i < cscc.size(); i++)
		// {
		//// logger.info("ResourcePlanning.fAssignControllerCardPorts() r "+
		// cscc.get(i).getRack()+" s "+cscc.get(i).getSbrack()+" c
		// "+cscc.get(i).getCard());
		// PortInfo port = new PortInfo();
		// port.setNodeKey(""+networkid+"_"+nodeid);
		// port.setRack(cscc.get(i).getRack());
		// port.setSbrack(cscc.get(i).getSbrack());
		// port.setCard(cscc.get(i).getCard());
		// port.setCardType(cscc.get(i).getCardType());
		// port.setPort(Integer.parseInt(racks.get(j).get("Rack").toString())+1);
		// port.setEquipmentId(rpu.fgetSfpEid(networkid, nodeid, "",
		// 0,ResourcePlanConstants.ScmSfp));//change the SFP type later on
		// port.setDirection("");
		// dbService.getPortInfoService().Insert(port);
		// }
		// }

		// for (int j = 0; j < racks.size(); j++)
		// {
		// // find the MPCs of the second subrack of each rack
		// List<CardInfo> mpc =
		// dbService.getCardInfoService().FindCardsInSbrack(networkid,nodeid,Integer.parseInt(racks.get(j).get("Rack").toString()),ResourcePlanConstants.TWO,ResourcePlanConstants.CardMpc);
		// for (int k = 0; k < mpc.size(); k++)
		// {
		// PortInfo mpcport = new PortInfo();
		// mpcport.setNodeKey(""+networkid+"_"+nodeid);
		// mpcport.setRack(mpc.get(k).getRack());
		// mpcport.setSbrack(mpc.get(k).getSbrack());
		// mpcport.setCard(mpc.get(k).getCard());
		// mpcport.setCardType(mpc.get(k).getCardType());
		// mpcport.setEquipmentId(rpu.fgetSfpEid(networkid, nodeid, "",
		// 0,ResourcePlanConstants.ScmSfp));//change the SFP type later on
		// mpcport.setDirection("");
		// dbService.getPortInfoService().Insert(mpcport);
		// }
		// }

		// Copper SFP is added to connect CSCC of main subrack with MPC of anyone of the
		// secondary subracks

		List<Map<String, Object>> racks = dbService.getCardInfoService().FindRacks(networkid, nodeid);
		for (int j = 0; j < racks.size(); j++) {

			int rackId = Integer.parseInt(racks.get(j).get("Rack").toString());
			List<Map<String, Object>> subRacks = dbService.getCardInfoService().FindSbracksInRack(networkid, nodeid,
					rackId);

			// If there are secondary subracks
			if (subRacks.size() > 1) {
				List<CardInfo> cscc = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
						ResourcePlanConstants.CardCscc);
				for (int i = 0; i < cscc.size(); i++) {
					// logger.info("ResourcePlanning.fAssignControllerCardPorts() r "+
					// cscc.get(i).getRack()+" s "+cscc.get(i).getSbrack()+" c
					// "+cscc.get(i).getCard());
					PortInfo port = new PortInfo();
					//					port.setNodeKey("" + networkid + "_" + nodeid);
					port.setNetworkId(networkid);
					port.setNodeId(nodeid);
					port.setRack(cscc.get(i).getRack());
					port.setSbrack(cscc.get(i).getSbrack());
					port.setCard(cscc.get(i).getCard());
					port.setCardType(cscc.get(i).getCardType());
					port.setPort(Integer.parseInt(racks.get(j).get("Rack").toString()) + 1);
					port.setEquipmentId(rpu.fgetSfpEid(networkid, nodeid, "", 0, ResourcePlanConstants.ScmSfp));// change
					// the
					// SFP
					// type
					// later
					// on
					port.setDirection("");
					dbService.getPortInfoService().Insert(port);
				}

			}
		}

	}

	/**
	 * Assigns ports on MPC in brown field
	 * 
	 * @param networkid
	 * @param nodeid
	 * @throws SQLException
	 */
	public void fAssignControllerCardPortsBrField(int networkid, int nodeid) throws SQLException {
		logger.info("fAssignControllerCardPortsBrField");
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		List<Map<String, Object>> racks = dbService.getCardInfoService().FindRacks(networkidBF, nodeid);
		for (int j = 0; j < racks.size(); j++) {
			// find the MPCs of the second subrack of each rack
			List<CardInfo> mpc = dbService.getCardInfoService().FindCardsInSbrack(networkidBF, nodeid,
					Integer.parseInt(racks.get(j).get("Rack").toString()), ResourcePlanConstants.TWO,
					ResourcePlanConstants.CardMpc);
			for (int k = 0; k < mpc.size(); k++) {
				List<PortInfo> info = dbService.getPortInfoService().FindPortInfo(networkidBF, nodeid,
						mpc.get(k).getRack(), mpc.get(k).getSbrack(), mpc.get(k).getCard());
				if (info != null) {
					PortInfo mpcport = new PortInfo();
					//					mpcport.setNodeKey("" + networkidBF + "_" + nodeid);
					mpcport.setNetworkId(networkidBF);
					mpcport.setNodeId(nodeid);
					mpcport.setRack(mpc.get(k).getRack());
					mpcport.setSbrack(mpc.get(k).getSbrack());
					mpcport.setCard(mpc.get(k).getCard());
					mpcport.setCardType(mpc.get(k).getCardType());
					mpcport.setEquipmentId(rpu.fgetSfpEid(networkid, nodeid, "", 0, ResourcePlanConstants.ScmSfp));// change
					// the
					// SFP
					// type
					// later
					// on
					mpcport.setDirection("");
					dbService.getPortInfoService().Insert(mpcport);
				}
			}
		}

	}

	/**
	 * function populates the brown field network with the existing Card and Port
	 * information from the green field
	 * 
	 * @param networkid
	 */
	public void fPopulateBrFieldCardPortInfo(int networkid) {
		logger.info("fPopulateBrFieldCardPortInfo");
		try {
			// get the brown field network id
			int networkidBF = Integer
					.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());

			ArrayList<Integer> list = new ArrayList<Integer>();
			list.addAll(rpu.fgetNodesToConfigure(networkid));

			for (int i = 0; i < list.size(); i++) {
				//				fCreateCardnPortTable(networkidBF, list.get(i));
				dbService.getCardInfoService().InsertCardDataInBrField(networkid, list.get(i), networkidBF);
				//				dbService.getCardInfoService().UpdateNodeKey(networkid, list.get(i), networkidBF);

				dbService.getPortInfoService().InsertPortDataInBrField(networkid, list.get(i), networkidBF);
				//				dbService.getPortInfoService().UpdateNodeKey(networkid, list.get(i), networkidBF);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void fPopulateBrFieldMcsMapInfo(int networkid) {
		logger.info("fPopulateBrFieldMcsMapInfo");
		try {
			// get the brown field network id
			int networkidBF = Integer
					.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
			dbService.getMcsMapService().insertMcsMapDataInBrField(networkid, networkidBF);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void fPopulateBrFieldAddDropCards(int networkid) {
		logger.info("fPopulateBrFieldMcsMapInfo");
		try {
			// get the brown field network id
			int networkidBF = Integer
					.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
			dbService.getMcsMapService().insertMcsMapDataInBrField(networkid, networkidBF);
			dbService.getWssMapService().insertWssMapDataInBrField(networkid, networkidBF);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void fPopulateBrFieldCardPreferences(int networkid) {
		logger.info("fPopulateBrFieldCardPreferences");
		try {
			// get the brown field network id
			int networkidBF = Integer
					.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());

			ArrayList<Integer> list = new ArrayList<Integer>();
			list.addAll(rpu.fgetNodesToConfigure(networkid));

			for (int i = 0; i < list.size(); i++) {
//				fCreateCardnPortTable(networkidBF, list.get(i));
				dbService.getCardInfoService().InsertCardDataInBrField(networkid, list.get(i), networkidBF);
				dbService.getCardInfoService().UpdateNodeKey(networkid, list.get(i), networkidBF);

				dbService.getPortInfoService().InsertPortDataInBrField(networkid, list.get(i), networkidBF);
				dbService.getPortInfoService().UpdateNodeKey(networkid, list.get(i), networkidBF);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void fPopulateBrFieldYCablePortInfo(int networkid) {
		logger.info("fPopulateBrFieldYCablePortInfo");
		try {
			int networkidBF = Integer
					.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
			dbService.getYcablePortInfoService().InsertYCablePortInfoInBrField(networkid, networkidBF);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Just for Testing, can be deleted later
	 */
	public void fResourceAllocationGFTesting(int networkId) throws SQLException {
		/** Test : Find Empty Slotes for */
		/// logger.debug("Fianl empty slots => "+ fFindEmptySlots(21,1,1,2));
		/** Test : Assign Free Slots Card */
		/// fAssignFillerPlates(networkId);

	}

	public JSONObject fResourceAllocationGF(int networkId,String process) throws SQLException {
		logger.debug("fResourceAllocationGF");

		JSONObject responseObject=new JSONObject();
		responseObject.put("ResponseStatus", 1);
		long timeStart = System.currentTimeMillis();
		long timeEnd = System.currentTimeMillis();
		long timetaken;
		InitializeConfigurationData cd = new InitializeConfigurationData(dbService);
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		GenerateMcsMapping gmm = new GenerateMcsMapping(dbService, rpu);

		switch (process) {
		case "Initialize":
		{
			if (dbService.getEquipmentService().Count() == 0) {
				logger.info("Equipment list loaded from xls file and Equipment Db updated");
				MapWebExcelHandler viewBOMExcelDataRequestObj = new MapWebExcelHandler();
				Equipment[] equipmentModelObjectArray;
				equipmentModelObjectArray = viewBOMExcelDataRequestObj.viewBOMExcelDataRequestReq(dbService);
				fGenerateEquipmentDb(equipmentModelObjectArray);
			}

			if (dbService.getCardPreferenceService().Count(networkId) == 0) {
				logger.info("Default preferences loaded");
				fsetDefaultCardPreference(networkId);
			}

			if (dbService.getEquipmentPreferenceService().Count(networkId) == 0) {
				logger.info("Default Equipment preferences loaded");
				eqp.InitializeEqPreferenceDb(networkId);
			}

			// dbService.getEquipmentPreferenceService().DeleteByNetworkId(networkId);
			// eqp.InitializeEqPreferenceDb(networkId);

			logger.info("Handle BOM File Request Processed");


			// deleting all previous card information
			dbService.getCardInfoService().DeleteAllCardInfo(networkId);	

			// deleting all previous port information
			dbService.getPortInfoService().DeleteAllPortInfo(networkId);

			dbService.getYcablePortInfoService().DeleteYCablePortInfo(networkId);
			dbService.getMuxDemuxPortInfoService().DeleteByNetworkId(networkId);
			cd.fDeleteConfigurationData(networkId);

			//			fInitAllNodesInNetwork(networkId);

			// handle allocation exceptions
			eqp.fhandleAllocationExceptions(networkId);
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/1000;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning common cards ..... ";
		}
		break;
		case "CommonCards":
		{
			fAssignCommonCardsInNetwork(networkId);
			fAssignMPCsDb(networkId);

			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning mux demux .....";
		}
		break;
		case "Mux/Demux":
		{
			fAssignPairedMuxponderCardsDb(networkId);
			fAssignRegeneratorsDb(networkId);
			fAssignMPCsDb(networkId);
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning OLP cards ...";
		}
		break;
		case "OLP":
		{
			fAssignOLPsDb(networkId);
			fAssignMPCsDb(networkId);
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning OTDR cards ...";
		}
		break;
		case "OTDR":
		{
			/** Assign OTDR **/
			fAssignOTDRCards(networkId);
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning add/drop cards...";
		}
		break;
		case "ADD/DROP&TRAYS":
		{		
			/** Assign ports **/
			fAssignPortsOnCard(networkId); 

			/** Assign Add Drop Card Set **/
			fAssignAddDropCardSet(networkId);
			fAssignMPCsDb(networkId);	

			/** Assign Mux Demux */
			fAssignTrays(networkId);

			fAssignYcables(networkId);
			fAssignYCablePorts(networkId);

			fassignDCMTrayDb(networkId);	

			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning mux/demux trays and Y-Cable ports ...";
		}
		break;
		case "PORTMAPPING":
		{
			gmm.fgenerateMcsWssMuxDemuxMap(networkId);
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning patch cords ...";
		}
		break;
		case "PATCHCORDS":
		{
			/** create view for cardinfo */
			//			dbService.getCardInfoService().CreateViewAllCardInfo(networkId);
			/** create view for portinfo */
			//			dbService.getPortInfoService().CreateViewAllPortInfo(networkId);

			/** patch cord generation */
			dbService.getPatchCordService().DeletePatchCordInfo(networkId);
			PatchCordAllocation pc=new PatchCordAllocation(dbService);
			pc.fAssignPatchCords(networkId);

			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Wrapping up equipment generation process ... ";
		}
		break;
		case "FINISH":{

			/** Assign Free Slots Card **/
			fAssignFillerPlates(networkId);

			dbService.getCdcRoadmAddService().DeleteByNetworkId(networkId);// DBG
			dbService.getCdcRoadmDropService().DeleteByNetworkId(networkId);// DBG
			MapWebSaveOpticalPowerDb mapWebSaveOpticalPowerDb = new MapWebSaveOpticalPowerDb();
			mapWebSaveOpticalPowerDb.MapWebSaveOpticalPowerCDC(networkId, dbService);

			// inventory generation
			GenerateLinkWavelengthMap linkwmap = new GenerateLinkWavelengthMap(dbService);
			dbService.getLinkWavelengthService().DeleteByNetworkId(networkId);
			dbService.getLinkWavelengthInfoService().DeleteByNetworkId(networkId);
			// linkwmap.fgenerateLinkWavelengthMap(networkId);
			linkwmap.fgenerateLinkWavelengthInformation(networkId);

			dbService.getInventoryService().DeleteInventory(networkId);

			fGenerateInventory(networkId);
			getDbService().getViewServiceRp().FindBoM(networkId).toString();		

			cd.fInitializeConfigurationData(networkId);
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			//			logger.info("Resource Allocation and Inventory Generation Time :" + (timeEnd - timeStart) + " MilliSeconds"
			//					+ "**************");
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Finished generating equipment.";
		}
		break;
		default:
			//			return "Error";
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", "Not Required / Error");
		}

		return responseObject;

		//		InitializeConfigurationData cd = new InitializeConfigurationData(dbService);
		//		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		//		GenerateMcsMapping gmm = new GenerateMcsMapping(dbService, rpu);
		//
		//		if (dbService.getEquipmentService().Count() == 0) {
		//			logger.info("Equipment list loaded from xls file and Equipment Db updated");
		//			MapWebExcelHandler viewBOMExcelDataRequestObj = new MapWebExcelHandler();
		//			Equipment[] equipmentModelObjectArray;
		//			equipmentModelObjectArray = viewBOMExcelDataRequestObj.viewBOMExcelDataRequestReq(dbService);
		//			fGenerateEquipmentDb(equipmentModelObjectArray);
		//		}
		//
		//		if (dbService.getCardPreferenceService().Count(networkId) == 0) {
		//			logger.info("Default preferences loaded");
		//			fsetDefaultCardPreference(networkId);
		//		}
		//
		//		if (dbService.getEquipmentPreferenceService().Count(networkId) == 0) {
		//			logger.info("Default Equipment preferences loaded");
		//			eqp.InitializeEqPreferenceDb(networkId);
		//		}
		//
		//		// dbService.getEquipmentPreferenceService().DeleteByNetworkId(networkId);
		//		// eqp.InitializeEqPreferenceDb(networkId);
		//
		//		logger.info("Handle BOM File Request Processed");
		//		long timeStart = System.currentTimeMillis();
		//
		//		// deleting all previous card information
		//		dbService.getCardInfoService().DeleteAllCardInfo(networkId);
		//		// deleting all previous port information
		//		dbService.getPortInfoService().DeleteAllPortInfo(networkId);
		//		dbService.getYcablePortInfoService().DeleteYCablePortInfo(networkId);
		//		dbService.getMuxDemuxPortInfoService().DeleteByNetworkId(networkId);
		//		cd.fDeleteConfigurationData(networkId);
		//
		//		fInitAllNodesInNetwork(networkId);
		//		// handle allocation exceptions
		//		eqp.fhandleAllocationExceptions(networkId);

		// Card assignment for Rack view
		//		fAssignCommonCardsInNetwork(networkId);
		//		fAssignMPCsDb(networkId);
		//		fAssignPairedMuxponderCardsDb(networkId);
		//		fAssignRegeneratorsDb(networkId);
		//		fAssignMPCsDb(networkId);
		//		fAssignOLPsDb(networkId);
		//		fAssignMPCsDb(networkId);

		//		fassignDCMTrayDb(networkId);
		//		fAssignMPCsDb(networkId);

		//		/** Assign ports **/
		//		fAssignPortsOnCard(networkId); 
		//
		//		/** Assign OTDR **/
		//		fAssignOTDRCards(networkId);
		//		
		//		/** Assign Add Drop Card Set **/
		//		fAssignAddDropCardSet(networkId);
		//		fAssignMPCsDb(networkId);
		//
		//		/** Assign Mux Demux */
		//		fAssignTrays(networkId);
		//
		//		fAssignYcables(networkId);
		//		fAssignYCablePorts(networkId);
		//
		//		fassignDCMTrayDb(networkId);
		//
		//
		//
		//		/** Assign Free Slots Card **/
		//		fAssignFillerPlates(networkId);

		//		// create view for cardinfo
		//		dbService.getCardInfoService().CreateViewAllCardInfo(networkId);
		//		// create view for portinfo
		//		dbService.getPortInfoService().CreateViewAllPortInfo(networkId);
		//
		//		gmm.fgenerateMcsWssMuxDemuxMap(networkId);
		//
		//		// patch cord generation
		//		dbService.getPatchCordService().DeletePatchCordInfo(networkId);
		//		fAssignPatchCords(networkId);

		//		// inventory generation
		//		GenerateLinkWavelengthMap linkwmap = new GenerateLinkWavelengthMap(dbService);
		//		dbService.getLinkWavelengthService().DeleteByNetworkId(networkId);
		//		dbService.getLinkWavelengthInfoService().DeleteByNetworkId(networkId);
		//		// linkwmap.fgenerateLinkWavelengthMap(networkId);
		//		linkwmap.fgenerateLinkWavelengthInformation(networkId);
		//
		//		dbService.getInventoryService().DeleteInventory(networkId);
		//
		//		fGenerateInventory(networkId);
		//		getDbService().getViewServiceRp().FindBoM(networkId).toString();		
		//
		//		cd.fInitializeConfigurationData(networkId);
		//		long timeEnd = System.currentTimeMillis();
		//		logger.info("Resource Allocation and Inventory Generation Time :" + (timeEnd - timeStart) + " MilliSeconds"
		//				+ "**************");
		//		
		//		return process;
	}

	public JSONObject fResourceAllocationBF(int networkId,String process) throws SQLException {
		logger.debug("fResourceAllocationBF");

		JSONObject responseObject=new JSONObject();
		responseObject.put("ResponseStatus", 1);
		long timeStart = System.currentTimeMillis();
		long timeEnd = System.currentTimeMillis();
		long timetaken;
		InitializeConfigurationData cd = new InitializeConfigurationData(dbService);
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkId).toString());
		GenerateMcsMapping gmm = new GenerateMcsMapping(dbService, rpu);
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);

		switch (process) {
		case "Initialize":
		{
			// deleting all previous card information
			dbService.getCardInfoService().DeleteAllCardInfo(networkidBF);
			// deleting all previous port information
			dbService.getPortInfoService().DeleteAllPortInfo(networkidBF);
			dbService.getYcablePortInfoService().DeleteYCablePortInfo(networkidBF);
			cd.fDeleteConfigurationData(networkidBF);

			GenerateLinkWavelengthMap linkwmap = new GenerateLinkWavelengthMap(dbService);
			dbService.getLinkWavelengthService().DeleteByNetworkId(networkidBF);
			dbService.getLinkWavelengthInfoService().DeleteByNetworkId(networkidBF);
			linkwmap.fgenerateLinkWavelengthInformation(networkidBF);

			fPopulateBrFieldCardPortInfo(networkId);
			fsetDefaultCardPreferenceBrField(networkId);
			//		fPopulateBrFieldMcsMapInfo(networkId);
			fPopulateBrFieldAddDropCards(networkId);
			fPopulateBrFieldYCablePortInfo(networkId);

			/** DeAssign Free Slots Card **/
			fDeAssignFillerPlates(networkidBF);

			// handle allocation exceptions
			eqp.fhandleAllocationExceptionsBrField(networkId);

			fInitNodesInNetworkBrField(networkId);			

			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/1000;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning common cards ..... ";
		}
		break;
		case "Mux/Demux":
		{
			fAssignPairedMuxponderCardsBrField(networkId);
			fAssignRegeneratorsBrField(networkId);
			fAssignMPCsBrField(networkId);
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning OLP cards ...";
		}
		break;
		case "OLP":
		{
			fAssignOLPsBrField(networkId);
			fAssignMPCsBrField(networkId);
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning OTDR cards ...";
		}
		break;
		case "ADD/DROP&TRAYS":
		{		
			fassignDCMTrayBrField(networkId);
			fAssignMPCsBrField(networkId);
			fAssignYcablesBrField(networkId);

			fAssignPortsOnCardBrField(networkId);
			fAssignYCablePortsBrField(networkId);


			/** Assign Add Drop Card Set **/
			fAssignAddDropCardSetBrField(networkId);
			//			fAssignMPCsBrField(networkId);		
			/** Assign Mux Demux */
			fAssignTrays(networkidBF);

			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning mux/demux trays and Y-Cable ports ...";
		}
		break;
		case "PORTMAPPING":
		{
			gmm.fgenerateMcsWssMapBrField(networkId);
			fAssignMPCsBrField(networkId);		
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Assigning patch cords ...";
		}
		break;
		case "PATCHCORDS":
		{
			fRemoveEmptySubracksInBrField(networkId); 
			/** create view for cardinfo */
			//			dbService.getCardInfoService().CreateViewAllCardInfo(networkidBF);
			/** create view for portinfo */
			//			dbService.getPortInfoService().CreateViewAllPortInfo(networkidBF);

			// patch cord generation
			dbService.getPatchCordService().DeletePatchCordInfo(networkidBF);
			
			PatchCordAllocation pc=new PatchCordAllocation(dbService);
			pc.fAssignPatchCords(networkidBF);

			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			timeStart=timeEnd;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Wrapping up equipment generation process ... ";
		}
		break;
		case "FINISH":{

			dbService.getCdcRoadmAddService().DeleteByNetworkId(networkId);// DBG
			dbService.getCdcRoadmDropService().DeleteByNetworkId(networkId);// DBG
			MapWebSaveOpticalPowerDb mapWebSaveOpticalPowerDb = new MapWebSaveOpticalPowerDb();
			mapWebSaveOpticalPowerDb.MapWebSaveOpticalPowerCDC(networkId, dbService);

			/** Assign Free Slots Card **/
			fAssignFillerPlates(networkidBF);

			dbService.getInventoryService().DeleteInventory(networkidBF);
			fGenerateInventory(networkidBF);
			getDbService().getViewServiceRp().FindBoM(networkidBF).toString();	

			cd.fInitializeConfigurationData(networkidBF);			

			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			//			logger.info("Resource Allocation and Inventory Generation Time :" + (timeEnd - timeStart) + " MilliSeconds"
			//					+ "**************");
			responseObject.put("Time", timetaken);
			responseObject.put("Response", process);
			//			return "Finished generating equipment.";
		}
		break;
		default:
			//			return "Error";
			timeEnd = System.currentTimeMillis();
			timetaken=(timeEnd-timeStart)/100;
			responseObject.put("Time", timetaken);
			responseObject.put("Response", "Not required in BF");
		}

		return responseObject;


		//		InitializeConfigurationData cd = new InitializeConfigurationData(dbService);
		//		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkId).toString());
		//		GenerateMcsMapping gmm = new GenerateMcsMapping(dbService, rpu);
		//		EquipmentPreferences eqp = new EquipmentPreferences(dbService);

		//		// deleting all previous card information
		//		dbService.getCardInfoService().DeleteAllCardInfo(networkidBF);
		//		// deleting all previous port information
		//		dbService.getPortInfoService().DeleteAllPortInfo(networkidBF);
		//		dbService.getYcablePortInfoService().DeleteYCablePortInfo(networkidBF);
		//		cd.fDeleteConfigurationData(networkidBF);
		//
		//		GenerateLinkWavelengthMap linkwmap = new GenerateLinkWavelengthMap(dbService);
		//		dbService.getLinkWavelengthService().DeleteByNetworkId(networkidBF);
		//		dbService.getLinkWavelengthInfoService().DeleteByNetworkId(networkidBF);
		//		linkwmap.fgenerateLinkWavelengthInformation(networkidBF);
		//
		//		fPopulateBrFieldCardPortInfo(networkId);
		//		fsetDefaultCardPreferenceBrField(networkId);
		//		//		fPopulateBrFieldMcsMapInfo(networkId);
		//		fPopulateBrFieldAddDropCards(networkId);
		//		fPopulateBrFieldYCablePortInfo(networkId);
		//
		//		fInitNodesInNetworkBrField(networkId);
		//
		//		/** DeAssign Free Slots Card **/
		//		fDeAssignFillerPlates(networkidBF);
		//
		//		// handle allocation exceptions
		//		eqp.fhandleAllocationExceptionsBrField(networkId);


		////		fInitNodesInNetworkBrField(networkId);
		//		
		//		fAssignPairedMuxponderCardsBrField(networkId);
		//		fAssignRegeneratorsBrField(networkId);
		//		fAssignMPCsBrField(networkId);

		//		fAssignOLPsBrField(networkId);
		//		fAssignMPCsBrField(networkId);

		//		fassignDCMTrayBrField(networkId);
		//		fAssignMPCsBrField(networkId);
		//		fAssignYcablesBrField(networkId);

		//		fAssignPortsOnCardBrField(networkId);
		//		fAssignYCablePortsBrField(networkId);
		//		fRemoveEmptySubracksInBrField(networkId);


		//		/** Assign Add Drop Card Set **/
		//		fAssignAddDropCardSetBrField(networkId);
		//		fAssignMPCsBrField(networkId);		

		//		/** Assign Mux Demux */
		//		fAssignTrays(networkidBF);

		//		/** Assign Free Slots Card **/
		//		fAssignFillerPlates(networkidBF);

		//		// create view for cardinfo
		//		dbService.getCardInfoService().CreateViewAllCardInfo(networkidBF);
		//		// create view for portinfo
		//		dbService.getPortInfoService().CreateViewAllPortInfo(networkidBF);

		//		gmm.fgenerateMcsWssMapBrField(networkId);

		//		// patch cord generation
		//		dbService.getPatchCordService().DeletePatchCordInfo(networkidBF);
		//		fAssignPatchCords(networkidBF);

		//		dbService.getInventoryService().DeleteInventory(networkidBF);
		//		fGenerateInventory(networkidBF);
		//		getDbService().getViewServiceRp().FindBoM(networkidBF).toString();	
		//
		//		cd.fInitializeConfigurationData(networkidBF);
	}

	/**
	 * Assigns YCablePorts in the network
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignYCablePorts(int networkid) throws SQLException {
		logger.info("fAssignYCablePorts");
		CardInfo YCabTobeFilled = new CardInfo();
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		for (int k = 0; k < nodelist.size(); k++) {
			int nodeid = nodelist.get(k);
			List<CardInfo> YCabs = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
					ResourcePlanConstants.YCable1x2Unit);
			logger.info("ResourcePlanning.fAssignYCablePorts() Totat Y Cabs " + YCabs.size());

			// List <Circuit> YCabCirs =
			// dbService.getCircuitService().FindCircuitsWithClientProt(networkid, nodeid,
			// ResourcePlanConstants.YCableProtection);
			List<Circuit> YCabCirs = dbService.getCircuitService().FindCircuitsWithClientProt(networkid, nodeid,
					ResourcePlanConstants.YCableProtection);

			for (int j = 0; j < YCabCirs.size(); j++) {
				CircuitDemandMapping dem = dbService.getCircuitDemandMappingService().FindDemand(networkid,
						YCabCirs.get(j).getCircuitId());
				CardInfo mpnN = dbService.getCardInfoService().FindMpn(networkid, nodeid, dem.getDemandId(),
						ResourcePlanConstants.Working);

				if (mpnN != null) {
					logger.info("Circuit " + YCabCirs.get(j).getCircuitId() + " Demand " + dem.getDemandId()
					+ "\n MPN N ()" + mpnN.toString());
					for (int i = 0; i < YCabs.size(); i++) {
						YCabTobeFilled = YCabs.get(i);
						int nPorts = dbService.getYcablePortInfoService().FindUsedPortCount(networkid, nodeid,
								YCabs.get(i).getRack(), YCabs.get(i).getSbrack(), YCabs.get(i).getCard());
						if (nPorts == 10) {
							logger.info(" Y Cable full " + YCabs.get(i).toString());
							continue;
						} else {
							// YCabTobeFilled=YCabs.get(i);
							break;
						}
					}
					logger.info(" YCabTobeFilled " + YCabTobeFilled.toString());
					YCablePortInfo ycab = new YCablePortInfo();
					ycab.setNetworkId(networkid);
					ycab.setNodeId(nodeid);
					ycab.setMpnLocN("" + mpnN.getRack() + "_" + "" + mpnN.getSbrack() + "_" + "" + mpnN.getCard());
					PortInfo port = dbService.getPortInfoService().FindPortInfo(networkid, nodeid, mpnN.getRack(),
							mpnN.getSbrack(), mpnN.getCard(), YCabCirs.get(j).getCircuitId());
					ycab.setMpnPortN(port.getPort());
					ycab.setYCableRack(YCabTobeFilled.getRack());
					ycab.setYCableSbrack(YCabTobeFilled.getSbrack());
					ycab.setYCableCard(YCabTobeFilled.getCard());

					CardInfo mpnP = dbService.getCardInfoService().FindMpn(networkid, nodeid, dem.getDemandId(),
							ResourcePlanConstants.Protection);

					if (mpnP != null)// protection mpn exists
					{
						logger.info("Circuit " + YCabCirs.get(j).getCircuitId() + "Demand " + dem.getDemandId()
						+ "\n MPN P ()" + mpnP.toString());
						ycab.setMpnLocP("" + mpnP.getRack() + "_" + "" + mpnP.getSbrack() + "_" + "" + mpnP.getCard());
						PortInfo portP = dbService.getPortInfoService().FindPortInfo(networkid, nodeid, mpnP.getRack(),
								mpnP.getSbrack(), mpnP.getCard(), YCabCirs.get(j).getCircuitId());
						ycab.setMpnPortP(portP.getPort());
					}
					dbService.getYcablePortInfoService().Insert(ycab);
				}
			}
		}
	}

	/**
	 * Assigns Ycable ports for added circuits in brown field
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignYCablePortsBrField(int networkid) throws SQLException {
		logger.info("fAssignYCablePortsBrField");
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		CardInfo YCabTobeFilled = new CardInfo();
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkidBF);
		for (int k = 0; k < nodelist.size(); k++) {
			int nodeid = nodelist.get(k);

			/**
			 * For Deleted Circuits
			 */
			List<Circuit> deletedCirs = dbService.getCircuitService().FindDeletedCircuitsInBrField(networkid,
					networkidBF, nodeid);
			for (int i = 0; i < deletedCirs.size(); i++) {
				if (deletedCirs.get(i).getClientProtectionType().equalsIgnoreCase(MapConstants.YCableProtection)) {
					CircuitDemandMapping dem = dbService.getCircuitDemandMappingService().FindDemand(networkidBF,
							deletedCirs.get(i).getCircuitId());
					if (dem != null) {
						CardInfo mpnN = dbService.getCardInfoService().FindMpn(networkidBF, nodeid, dem.getDemandId(),
								ResourcePlanConstants.Working);

						if (mpnN != null) {

							PortInfo port = dbService.getPortInfoService().FindPortInfo(networkidBF, nodeid,
									mpnN.getRack(), mpnN.getSbrack(), mpnN.getCard(),
									deletedCirs.get(i).getCircuitId());
							String mpnLoc = "" + mpnN.getRack() + "_" + "" + mpnN.getSbrack() + "_" + ""
									+ mpnN.getCard();
							dbService.getYcablePortInfoService().DeleteYCablePort(networkidBF, nodeid, mpnLoc,
									port.getPort());
							logger.info("Deleted " + mpnLoc + " Port " + port.getPort());
						}
					} else {
						logger.info("Demand No Longer exists in Brown field ");
						// demand will be found in the green field
						CircuitDemandMapping dem2 = dbService.getCircuitDemandMappingService().FindDemand(networkid,
								deletedCirs.get(i).getCircuitId());
						if (dem2 != null) {
							// get its mpn from the green field
							CardInfo mpnN = dbService.getCardInfoService().FindMpn(networkid, nodeid,
									dem2.getDemandId(), ResourcePlanConstants.Working);

							if (mpnN != null) {
								// get its port info from green field
								PortInfo port = dbService.getPortInfoService().FindPortInfo(networkid, nodeid,
										mpnN.getRack(), mpnN.getSbrack(), mpnN.getCard(),
										deletedCirs.get(i).getCircuitId());
								String mpnLoc = "" + mpnN.getRack() + "_" + "" + mpnN.getSbrack() + "_" + ""
										+ mpnN.getCard();
								dbService.getYcablePortInfoService().DeleteYCablePort(networkidBF, nodeid, mpnLoc,
										port.getPort());
								logger.info("Deleted " + mpnLoc + " Port " + port.getPort());
							}
						}
					}
				}
			}

			/**
			 * For Added Circuits
			 */
			List<CardInfo> YCabs = dbService.getCardInfoService().FindCardInfoByCardType(networkidBF, nodeid,
					ResourcePlanConstants.YCable1x2Unit);
			List<Circuit> addedCirs = dbService.getCircuitService().FindAddedCircuitsInBrField(networkid, networkidBF,
					nodeid);
			for (int j = 0; j < addedCirs.size(); j++) {
				if (addedCirs.get(j).getClientProtectionType().equalsIgnoreCase(MapConstants.YCableProtection)) {
					CircuitDemandMapping dem = dbService.getCircuitDemandMappingService().FindDemand(networkidBF,
							addedCirs.get(j).getCircuitId());
					CardInfo mpnN = dbService.getCardInfoService().FindMpn(networkidBF, nodeid, dem.getDemandId(),
							ResourcePlanConstants.Working);

					if (mpnN != null) {
						for (int i = 0; i < YCabs.size(); i++) {
							YCabTobeFilled = YCabs.get(i);
							int nPorts = dbService.getYcablePortInfoService().FindUsedPortCount(networkidBF, nodeid,
									YCabs.get(i).getRack(), YCabs.get(i).getSbrack(), YCabs.get(i).getCard());
							if (nPorts == 10) {
								logger.info(" Y Cable full " + YCabs.get(i).toString());
								continue;
							} else {
								// YCabTobeFilled=YCabs.get(i);
								// logger.info(" YCabTobeFilled "+ YCabTobeFilled.toString());
								break;
							}
						}
						logger.info(" YCabTobeFilled " + YCabTobeFilled.toString());
						logger.info("Circuit " + addedCirs.get(j).getCircuitId() + " Demand " + dem.getDemandId()
						+ "\n MPN N ()" + mpnN.toString());
						YCablePortInfo ycab = new YCablePortInfo();
						ycab.setNetworkId(networkidBF);
						ycab.setNodeId(nodeid);
						ycab.setMpnLocN("" + mpnN.getRack() + "_" + "" + mpnN.getSbrack() + "_" + "" + mpnN.getCard());
						PortInfo port = dbService.getPortInfoService().FindPortInfo(networkidBF, nodeid, mpnN.getRack(),
								mpnN.getSbrack(), mpnN.getCard(), addedCirs.get(j).getCircuitId());
						ycab.setMpnPortN(port.getPort());
						ycab.setYCableRack(YCabTobeFilled.getRack());
						ycab.setYCableSbrack(YCabTobeFilled.getSbrack());
						ycab.setYCableCard(YCabTobeFilled.getCard());

						CardInfo mpnP = dbService.getCardInfoService().FindMpn(networkidBF, nodeid, dem.getDemandId(),
								ResourcePlanConstants.Protection);

						if (mpnP != null)// protection mpn exists
						{
							logger.info("Circuit " + addedCirs.get(j).getCircuitId() + " Demand " + dem.getDemandId()
							+ "\n MPN P ()" + mpnP.toString());
							ycab.setMpnLocP(
									"" + mpnP.getRack() + "_" + "" + mpnP.getSbrack() + "_" + "" + mpnP.getCard());
							PortInfo portP = dbService.getPortInfoService().FindPortInfo(networkidBF, nodeid,
									mpnP.getRack(), mpnP.getSbrack(), mpnP.getCard(), addedCirs.get(j).getCircuitId());
							ycab.setMpnPortP(portP.getPort());
						}
						dbService.getYcablePortInfoService().Insert(ycab);
						logger.info("Inserted " + ycab.toString());
					}
				}
			}

			/**
			 * check for the empty YCable trays , if any, delete them
			 */
			List<CardInfo> YCabTrays = dbService.getCardInfoService().FindCardInfoByCardType(networkidBF, nodeid,
					ResourcePlanConstants.YCable1x2Unit);
			for (int i = 0; i < YCabTrays.size(); i++) {
				int cnt = dbService.getYcablePortInfoService().FindUsedPortCount(networkidBF, nodeid,
						YCabTrays.get(i).getRack(), YCabTrays.get(i).getSbrack(), YCabTrays.get(i).getCard());
				if (cnt == 0)// Tray is empty
				{
					dbService.getCardInfoService().DeleteCard(networkidBF, nodeid, YCabTrays.get(i).getRack(),
							YCabTrays.get(i).getSbrack(), YCabTrays.get(i).getCard());
					logger.info(
							"fAssignYCablePortsBrField: Deleted the empty Ycable tray: " + YCabTrays.get(i).toString());
				}
			}
		}

	}

	/**
	 * function checks if any subrack becomes empty after brown field equipment
	 * allocation, then it removes it
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fRemoveEmptySubracksInBrField(int networkid) throws SQLException {
		logger.info("fRemoveEmptySubracksInBrField");
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkidBF);
		for (int k = 0; k < nodelist.size(); k++) {
			int nodeid = nodelist.get(k);
			for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) {
				for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) {
					List<CardInfo> cards = dbService.getCardInfoService().FindNonMPCCardsInSbrack(networkidBF, nodeid,
							i, j);
					if (cards.size() == 0) {
						// the subrack is empty, remove it, remove MPC
						dbService.getCardInfoService().DeleteMpcInSbrack(networkidBF, nodeid, i, j);
						// logger.info("fRemoveEmptySubracksInBrField Deleted "+ " Rack: "+i +" Sbrack:
						// "+j);
					}
				}
			}
		}
	}

	public void fAllocateRemainingCommonCards(int networkid, int nodeid, List<CardInfo> cardList) {
		String location;
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		String MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);
		for (int j = 0; j < cardList.size(); j++) {
			Equipment e = dbService.getEquipmentService().findEquipmentById(cardList.get(j).getEquipmentId());
			if (e.getSlotSize() == ResourcePlanConstants.SlotSizeTwo)
				location = rpu.fGetFirstFreeDoubleSlotDb(networkid, nodeid);
			else
				location = rpu.fGetFirstFreeSlotDb(networkid, nodeid);
			logger.info(j + " fAllocateRemainingCommonCards() cardList : " + cardList.get(j));
			logger.info(j + " fAllocateRemainingCommonCards() location found : " + location);

			if (MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes)) {
				if (((location.contains("_6") | (location.contains("_9"))))) {
					j--;
					int[] id = rpu.locationIds(location);
					fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO, "");
				} else {
					int[] id = rpu.locationIds(location);
					fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], cardList.get(j).getCardType(),
							cardList.get(j).getDirection(), cardList.get(j).getWavelength(),
							cardList.get(j).getDemandId(), rpu.fgetEIdFromCardType(cardList.get(j).getCardType()),
							cardList.get(j).getStatus(), cardList.get(j).getCircuitId(), "");
				}

			} else {
				if (((location.contains("_6")/* |(location.contains("_9")) */))) {
					j--;
					int[] id = rpu.locationIds(location);
					fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], ResourcePlanConstants.CardMpc, "", 0, 0,
							rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMpc), "", ResourcePlanConstants.ZERO, "");
				} else {
					int[] id = rpu.locationIds(location);
					fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2], cardList.get(j).getCardType(),
							cardList.get(j).getDirection(), cardList.get(j).getWavelength(),
							cardList.get(j).getDemandId(), rpu.fgetEIdFromCardType(cardList.get(j).getCardType()),
							cardList.get(j).getStatus(), cardList.get(j).getCircuitId(), "");
				}
			}
		}

	}

	/**
	 * @desc This Api gives the free slots in return based on the input params given
	 * @created 12th April, 2018
	 * @param networkId
	 */
	public HashMap<Integer, Object[]> fFindEmptySlots(int networkId, Integer... optionalParams) throws SQLException {

		logger.debug("fAssignFillerPlates");

		int nodeId, rackId, subRackId;
		HashMap<Integer, Object[]> listWithEmptySlots = new HashMap<Integer, Object[]>();

		if (dbService != null) {

			if (optionalParams != null && MapConstants.I_THREE == optionalParams.length) {
				nodeId = optionalParams[MapConstants.I_ZERO];
				rackId = optionalParams[MapConstants.I_ONE];
				subRackId = optionalParams[MapConstants.I_TWO];
			} else {

				nodeId = MapConstants.I_MINUS_ONE;
				rackId = MapConstants.I_MINUS_ONE;
				subRackId = MapConstants.I_MINUS_ONE;
			}

			logger.debug(
					"networkId, nodeId, rack, subrack: " + networkId + "," + nodeId + "," + rackId + "," + subRackId);

			List<CardInfo> cardInfoList = dbService.getCardInfoService().FindAll(networkId, nodeId);
			List<Integer> fillerPlateList = new ArrayList<Integer>();

			logger.debug(" cardInfoList: " + cardInfoList);

			for (int i = MapConstants.I_ONE; i <= MapConstants.I_FOURTEEN; i++)
				fillerPlateList.add(i);

			/** Remove Subrack which contains the Tray */
			cardInfoList.removeIf(cardInfoObject -> cardInfoObject.getCardType()
					.equalsIgnoreCase(ResourcePlanConstants.Odd_Mux_Demux_Unit)
					|| cardInfoObject.getCardType().equalsIgnoreCase(ResourcePlanConstants.Even_Mux_Demux_Unit));

			cardInfoList.stream().filter(
					cardInfoObject -> cardInfoObject.getRack() == rackId && cardInfoObject.getSbrack() == subRackId)
			.forEach(cardInfoObject -> {

				fillerPlateList.removeIf(plateObj -> plateObj == cardInfoObject.getCard());
				logger.debug("cardInfoObject.getEquipmentId() " + cardInfoObject.getEquipmentId());

				if (dbService.getEquipmentService()
						.FindSlotSize(cardInfoObject.getEquipmentId()) == MapConstants.I_TWO)
					fillerPlateList
					.removeIf(plateObj -> plateObj == (cardInfoObject.getCard() + MapConstants.I_ONE));

			});

			logger.debug("count ==========> " + cardInfoList.stream().filter(
					cardInfoObject -> cardInfoObject.getRack() == rackId && cardInfoObject.getSbrack() == subRackId)
					.count());

			if (cardInfoList.stream().filter(
					cardInfoObject -> cardInfoObject.getRack() == rackId && cardInfoObject.getSbrack() == subRackId)
					.count() == MapConstants.I_ZERO) {
				fillerPlateList.clear();
				logger.debug("cleared the list");
			}

			logger.debug(" fillerPlateList: " + fillerPlateList);
			listWithEmptySlots.put((rackId * MapConstants.I_TEN) + subRackId, fillerPlateList.toArray());

			logger.debug(" listWithEmptySlots: " + listWithEmptySlots);

		}

		else {

			logger.error("DB Service NullPointerException");
		}
		return listWithEmptySlots;
	}

	/**
	 * API for assignment of Trays : as of now mux-demux tray assignment done
	 * 
	 * @date 2nd May, 2018
	 * @author hp
	 * 
	 * @see run through all nodes of the network based on the nodetype assign the
	 *      mux demux tray or for that matter any tray : a) if 1,1 subrack is empty
	 *      then assign there dont go for the other rack b) else go for the other
	 *      rack
	 * 
	 */
	@SuppressWarnings("unused")
	public void fAssignTrays(int networkId) throws SQLException {

		try {

			logger.debug("fAssignTrays for NetworkId : " + networkId);

			ArrayList<Integer> list = new ArrayList<Integer>();
			list.addAll(rpu.fgetNodesToConfigure(networkId));

			if (list != null) {

				logger.debug("list of nodes to configure : " + list);

				for (int i = 0; i < list.size(); i++) {

					HashMap<Integer, Object[]> fFindEmptySlots;
					Node n = dbService.getNodeService().FindNode(networkId, list.get(i));

					int nodetype = n.getNodeType();
					int rackid = ResourcePlanConstants.ONE,
							sbrackid = ResourcePlanConstants.ONE;/**
							 * initially start with first rack and first subrack
							 */
					int nodeId = n.getNodeId();
					String Capacity = n.getCapacity();

					switch (nodetype) {					
					case MapConstants.twoBselectRoadm: {

						fFindEmptySlots = fFindEmptySlots(networkId, nodeId, rackid, sbrackid);

						logger.debug(" fFindEmptySlots : " + fFindEmptySlots);
						logger.debug(" fFindEmptySlots value length : "
								+ fFindEmptySlots.get((rackid * ResourcePlanConstants.TEN) + sbrackid).length);

						if (ResourcePlanConstants.ZERO == fFindEmptySlots
								.get((rackid * ResourcePlanConstants.TEN) + sbrackid).length) {
							/** Ready to place tray in subrack, to optimise the BOM */

							if (Capacity.equalsIgnoreCase("3")) {

								System.out.println(" if condition 3");
								// assing mux demux for east in rack 1, subrack 0

								fSaveTrayInDb(networkId, nodeId, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit,
										MapConstants.EAST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");

								fSaveTrayInDb(networkId, nodeId, rackid, 0, 2,
										ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");

								// assing mux demux for West in rack 1, subrack 1								
								fSaveTrayInDb(networkId, nodeId, rackid, 1, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit,
										MapConstants.WEST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");

								fSaveTrayInDb(networkId, nodeId, rackid, 1, 2,
										ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
							} else if (Capacity.equalsIgnoreCase("1")) {
								System.out.println(" if condition 1");
								rackid = ResourcePlanConstants.ONE;
								fSaveTrayInDb(networkId, nodeId, rackid, 0, 1,
										ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
								// rackid=ResourcePlanConstants.TWO;
								fSaveTrayInDb(networkId, nodeId, rackid, 0, 2,
										ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
							} else if (Capacity.equalsIgnoreCase("2")) {
								rackid = ResourcePlanConstants.ONE;
								fSaveTrayInDb(networkId, nodeId, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit,
										MapConstants.EAST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
								// rackid=ResourcePlanConstants.TWO;
								fSaveTrayInDb(networkId, nodeId, rackid, 0, 2, ResourcePlanConstants.Odd_Mux_Demux_Unit,
										MapConstants.WEST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
							}

						} else {
							if (Capacity.equalsIgnoreCase("3")) {
								rackid = ResourcePlanConstants.ONE;

								// assing mux demux for east in rack 1

								fSaveTrayInDb(networkId, nodeId, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit,
										MapConstants.EAST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");

								fSaveTrayInDb(networkId, nodeId, rackid, 0, 2,
										ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");

								// assing mux demux for West in rack 2
								rackid = ResourcePlanConstants.TWO;

								fSaveTrayInDb(networkId, nodeId, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit,
										MapConstants.WEST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");

								fSaveTrayInDb(networkId, nodeId, rackid, 0, 2,
										ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
							} else if (Capacity.equalsIgnoreCase("1")) {
								rackid = ResourcePlanConstants.ONE;
								fSaveTrayInDb(networkId, nodeId, rackid, 0, 1,
										ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
								// rackid=ResourcePlanConstants.TWO;
								fSaveTrayInDb(networkId, nodeId, rackid, 0, 2,
										ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
							} else if (Capacity.equalsIgnoreCase("2")) {
								rackid = ResourcePlanConstants.ONE;
								fSaveTrayInDb(networkId, nodeId, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit,
										MapConstants.EAST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
								// rackid=ResourcePlanConstants.TWO;
								fSaveTrayInDb(networkId, nodeId, rackid, 0, 2, ResourcePlanConstants.Odd_Mux_Demux_Unit,
										MapConstants.WEST, 0, 0,
										rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
										ResourcePlanConstants.ZERO, "");
							}
						}
					}
					break;

					case MapConstants.te:{

						fSaveTrayInDb(networkId, nodeId, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit, "", 0,
								0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
								ResourcePlanConstants.ZERO, "");

						fSaveTrayInDb(networkId, nodeId, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit, "", 0,
								0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
								ResourcePlanConstants.ZERO, "");

					}
					break;

					case MapConstants.hub:{


						fFindEmptySlots = fFindEmptySlots(networkId, nodeId, rackid, sbrackid);

						logger.debug(" fFindEmptySlots : " + fFindEmptySlots);
						logger.debug(" fFindEmptySlots value length : "
								+ fFindEmptySlots.get((rackid * ResourcePlanConstants.TEN) + sbrackid).length);

						if (ResourcePlanConstants.ZERO == fFindEmptySlots
								.get((rackid * ResourcePlanConstants.TEN) + sbrackid).length) {
							/** Ready to place tray in subrack, to optimise the BOM */							

							// assing mux demux for east in rack 1, subrack 0
							fSaveTrayInDb(networkId, nodeId, rackid, 0, 1,
									ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.EAST,
									0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
									ResourcePlanConstants.ZERO, "");


							fSaveTrayInDb(networkId, nodeId, rackid, 0, 2,
									ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST,
									0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
									ResourcePlanConstants.ZERO, "");		

							// assing mux demux for east in rack 1, subrack 1
							fSaveTrayInDb(networkId, nodeId, rackid, 1, 1,
									ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.WEST,
									0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
									ResourcePlanConstants.ZERO, "");

							fSaveTrayInDb(networkId, nodeId, rackid, 1, 2,
									ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST,
									0, 0, rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
									ResourcePlanConstants.ZERO, "");					


						}

						else{

							// assing mux demux for east in rack 1
							rackid = ResourcePlanConstants.ONE;

							fSaveTrayInDb(networkId, nodeId, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit,
									MapConstants.EAST, 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
									ResourcePlanConstants.ZERO, "");

							fSaveTrayInDb(networkId, nodeId, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit,
									MapConstants.EAST, 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
									ResourcePlanConstants.ZERO, "");

							// assing mux demux for West in rack 2
							rackid = ResourcePlanConstants.TWO;

							fSaveTrayInDb(networkId, nodeId, rackid, 0, 1, ResourcePlanConstants.Odd_Mux_Demux_Unit,
									MapConstants.WEST, 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.Odd_Mux_Demux_Unit), "",
									ResourcePlanConstants.ZERO, "");

							fSaveTrayInDb(networkId, nodeId, rackid, 0, 2, ResourcePlanConstants.Even_Mux_Demux_Unit,
									MapConstants.WEST, 0, 0,
									rpu.fgetEIdFromCardType(ResourcePlanConstants.Even_Mux_Demux_Unit), "",
									ResourcePlanConstants.ZERO, "");
						}


					}
					break;


					}
				}

			}

			else {
				logger.error("Null List: " + list);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * API for assignmnet of filler plates for the given network id
	 * 
	 * @date 18th April, 2018
	 * @author hp
	 */
	public void fAssignFillerPlates(int networkId) throws SQLException {

		logger.debug("fAssignFillerPlates for NetworkId : " + networkId);

		List<Node> nodeIdList = dbService.getNodeService().FindAll(networkId);

		logger.debug("nodeIdList: " + nodeIdList);

		try {

			nodeIdList.forEach(node -> {

				int rackCount = dbService.getCardInfoService().FindRackCount(networkId, node.getNodeId());
				logger.debug("rackCount, node id :  " + rackCount + ", " + node.getNodeId());

				for (int rack = MapConstants.I_ONE; rack <= rackCount; rack++) {/** rack times */

					int subrackCount = (int) dbService.getCardInfoService()
							.FindMaxRackSubrackCount(networkId, node.getNodeId(), rack).get(0).get("Sbrack");
					logger.debug("subrackCount, node id :  " + subrackCount + ", " + node.getNodeId());

					for (int subrack = MapConstants.I_ONE; subrack <= subrackCount; subrack++) {/** subrack times */

						HashMap<Integer, Object[]> fFindEmptySlots;

						try {
							logger.debug("nodeId, rack, subrack: " + node.getNodeId() + "," + rack + "," + subrack);

							/** Find empty slots for given : network,node,rack,subrack */
							fFindEmptySlots = fFindEmptySlots(networkId, node.getNodeId(), rack, subrack);

							logger.debug(" fFindEmptySlots : " + fFindEmptySlots);

							/** save filler plates to db */
							if (fFindEmptySlots != null) {

								fFindEmptySlots.forEach((k, v) -> {

									Arrays.stream(v).forEach(emptySlot -> {
										logger.debug("slot " + emptySlot);
										logger.debug("Key: " + k + "Value: " + v);

										/** Finally save the filler plate into DB */
										fSaveCardInDb(networkId, node.getNodeId(), k / MapConstants.I_TEN,
												k % MapConstants.I_TEN, (int) emptySlot,
												ResourcePlanConstants.FillerPlate, "", 0, 0,
												rpu.fgetEIdFromCardType(ResourcePlanConstants.FillerPlate), "",
												ResourcePlanConstants.ZERO, "");

									});

								});
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}

					);
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}

	}

	/**
	 * API for checking conditions for adding card sets for Add Drop in Cd Roadm, D
	 * Roadm and CDC Roadm
	 * 
	 * @date 16th April, 2018
	 * @author avinash
	 */
	public void fAssignAddDropCardSet(int networkId) throws SQLException {

		logger.debug("fAssignAddDropCardSet for NetworkId : " + networkId);

		// Map<Integer,Boolean> NodeBasedAddDropCardSetMap=new
		// HashMap<Integer,Boolean>();

		try {

			ArrayList<Integer> list = new ArrayList<Integer>();
			list.addAll(rpu.fgetNodesToConfigure(networkId));
			for (int i = 0; i < list.size(); i++) {
				int nodetype = dbService.getNodeService().FindNode(networkId, list.get(i)).getNodeType();
				int nodedegree = dbService.getNodeService().FindNode(networkId, list.get(i)).getDegree();

				switch (nodetype) {
				case MapConstants.roadm:
				{
					fAssignAddDropCardSetCdCRoadm(networkId,list.get(i),nodetype,nodedegree);
				}
				break;
				case MapConstants.cdRoadm: {
					/* Get card type for Add drop */		
					EquipmentPreference WssAddDropSetType = dbService.getEquipmentPreferenceService().FindPreferredEq(networkId, list.get(i), ResourcePlanConstants.CatAddDropWssCdRoadm);
					if(WssAddDropSetType.getCardType().toString().equals(ResourcePlanConstants.AddDropSingleCardSet)) {
						fAssignAddDropCardSetCdRoadmSingleCard(networkId, list.get(i), nodetype, nodedegree);
					}else {
						fAssignAddDropCardSetCdRoadmDoubleCard(networkId, list.get(i), nodetype, nodedegree);
					}
					//					fAssignAddDropCardSetCdRoadm(networkId, list.get(i), nodetype, nodedegree);
				}
				break;
				default:
					logger.info("fAssignAddDropCardSet : Not a CDC or CD Roadm NodeId:" + list.get(i));
					break;
				}
			}

		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}

	}


	/**
	 * API for checking conditions for adding card sets for Add Drop in Cd Roadm, D
	 * Roadm and CDC Roadm
	 * 
	 * @date 16th April, 2018
	 * @author avinash
	 */
	public void fAssignAddDropCardSetBrField(int networkId) throws SQLException {

		logger.debug("fAssignAddDropCardSet for NetworkId : " + networkId);
		int networkIdBf=Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkId).toString());

		// Map<Integer,Boolean> NodeBasedAddDropCardSetMap=new
		// HashMap<Integer,Boolean>();

		try {

			ArrayList<Integer> list = new ArrayList<Integer>();
			list.addAll(rpu.fgetNodesToConfigure(networkIdBf));
			for (int i = 0; i < list.size(); i++) {
				int nodetype = dbService.getNodeService().FindNode(networkIdBf, list.get(i)).getNodeType();
				int nodedegree = dbService.getNodeService().FindNode(networkIdBf, list.get(i)).getDegree();

				switch (nodetype) {
				case MapConstants.roadm:
				{
					fAssignAddDropCardSetCdCRoadmBrField(networkId,list.get(i),nodetype,nodedegree);
				}
				break;
				case MapConstants.cdRoadm: {
					/* Get card type for Add drop */		
					EquipmentPreference WssAddDropSetType = dbService.getEquipmentPreferenceService().FindPreferredEq(networkIdBf, list.get(i), ResourcePlanConstants.CatAddDropWssCdRoadm);
					if(WssAddDropSetType.getCardType().toString().equals(ResourcePlanConstants.AddDropSingleCardSet)) {
						fAssignAddDropCardSetSingleCardCdRoadmBrField(networkId, list.get(i), nodetype, nodedegree);
					}else {
						fAssignAddDropCardSetDoubleCardCdRoadmBrField(networkId, list.get(i), nodetype, nodedegree);
					}
					//					fAssignAddDropCardSetCdRoadmBrField(networkId, list.get(i), nodetype, nodedegree);
				}
				break;
				default:
					logger.info("fAssignAddDropCardSet : Not a CDC or CD Roadm NodeId:" + list.get(i));
					break;
				}
			}

		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}

	}

	/**
	 * API for assignmnet of cards for Add Drop in Cd Roadm
	 * 
	 * @date 2nd May, 2018
	 * @author avinash
	 * @throws SQLException
	 */
	public void fAssignAddDropCardSetCdRoadmDoubleCard(int networkid, int nodeid, int nodetype, int nodedegree)
			throws SQLException {

		/* Get card preference */
		// Pinfo=eqp.fGetPreferredEqType(networkId, node.getNodeId(),
		// ResourcePlanConstants.CatWss,
		// ResourcePlanConstants.ParamDemandPtc,""+routesAll.get(i).getDemandId());
		// wssType= Pinfo[0].toString().trim();
		// wssSrNo= Pinfo[1].toString();
		// wssEid= rpu.fgetEIdFromCardType(wssType);

		String wssType, wssSrNo;
		int wssEid, setNum;

		/* Default CardType and Card Set Count */
		String levelTwoWssType = ResourcePlanConstants.CardWss1x9;
		int addDropSetNum = 1;

		/* Find Add/Drop Wavelength Count for Node */
		//		int numOfWavelength = dbService.getCardInfoService().CountMpn(networkid, nodeid);
		double numOfWavelength = dbService.getPortInfoService().FindAllUniqueLinePorts(networkid, nodeid).size();
		System.out.println("numOfWavelength for node id " + nodeid + " is " + numOfWavelength);

		// Get all demands for CDC Node which have chnl protection
		List<RouteMapping> routesList = dbService.getViewService().FindChannelProtectionRoutes(networkid, nodeid);
		Double chnlPtcDemandCount = (double) (routesList.size() | 0);

		System.out.println("chnlPtcDemandCount:" + chnlPtcDemandCount);

		int maxPortsForWssTypeChange=ResourcePlanConstants.MaxSwitchPortsWss2x1x9;

		// If there are demands that have chnl ptc ,then two sets are required
		if (chnlPtcDemandCount > 0)
		{
			addDropSetNum = 2;
			/** Two wss sets of wss 2x1x9 will have total 18 ports **/
			maxPortsForWssTypeChange*=2;

			/** Channel Ptc demand > 20 will require minimum of 4 wss sets **/
			if(chnlPtcDemandCount>20)
				addDropSetNum = 4;
		}

		if(numOfWavelength>0) {
			/** Based on wav/mpn count , change Wss Card Type and Cards Set Count **/
			if (numOfWavelength > maxPortsForWssTypeChange) {

				levelTwoWssType = ResourcePlanConstants.CardWss1x2x20;
				int setRequired = (int)Math.ceil(numOfWavelength / ResourcePlanConstants.MaxSwitchPortsWss2x1x20);

				// If wav/mpn are greater than 40 , then setCount becomes setRequired
				if (setRequired > addDropSetNum)
					addDropSetNum = setRequired;

				System.out.println("Wavelength Count > 9 ---- NodeType:: " + nodeid + " Wss Card Type:: " + levelTwoWssType
						+ "Set Count:: " + addDropSetNum + "setRequired:: " + setRequired);
			}

			System.out.println(
					"NodeType:: " + nodetype + " Wss Card Type:: " + levelTwoWssType + "Set Count:: " + addDropSetNum);

			// Start from first set
			setNum = 1;

			// Add Card Sets to Db
			while (setNum <= addDropSetNum) {
				fAssignAddDropCards(networkid, nodeid, nodetype, levelTwoWssType, setNum, nodedegree);
				setNum++;
			}
		}

	}

	/**
	 * API for assignmnet of Wss 8X12 for Add Drop in Cd Roadm
	 * 
	 * @date 2nd May, 2018
	 * @author avinash
	 * @throws SQLException
	 */
	public void fAssignAddDropCardSetCdRoadmSingleCard(int networkid, int nodeid, int nodetype, int nodedegree)
			throws SQLException {

		/* Get card redundancy */		
		EquipmentPreferences eqp=new EquipmentPreferences(dbService);
		String WssRedundancy = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatAddDropWssCdRoadm);

		String wssSrNo;
		int wssEid, startSetNum;


		/** Default CardType and Card Set Count and Max Ports **/
		int addDropSetCount = 1,
				maxPorts=ResourcePlanConstants.MaxSwitchPortsWss2x8x12;
		String wssType=ResourcePlanConstants.CardWss8x12;


		/** Find Add/Drop Wavelength Count for Node **/
		double numOfWavelength = dbService.getPortInfoService().FindAllUniqueLinePorts(networkid, nodeid).size();
		System.out.println("Case : Red -> "+WssRedundancy+" NumOfWavelength for node id " + nodeid + " is " + numOfWavelength);

		/** Get all demands for CDC Node which have chnl protection **/
		List<RouteMapping> routesList = dbService.getViewService().FindChannelProtectionRoutes(networkid, nodeid);
		Double chnlPtcDemandCount = (double) (routesList.size() | 0);

		/** Get all demands for CDC Node which have client protection with 1+1,1+1+R or 1+1+2R **/
		routesList = dbService.getViewService().FindClientProtectionRoutes(networkid, nodeid);
		Double clientPtcDemandCount = (double) (routesList.size() | 0);
		System.out.println("chnlPtcDemandCount:" + chnlPtcDemandCount+" clientPtcDemandCount:" + clientPtcDemandCount);

		/** No hardware redundancy for Client Ptc **/
		if(WssRedundancy.equals(ResourcePlanConstants.No)) {
			// If there are demands that have chnl ptc ,then two/more sets are required
			if (chnlPtcDemandCount > 0)				
				addDropSetCount = (int)Math.ceil(chnlPtcDemandCount / maxPorts);
			System.out.println("addDropSetCount :" + addDropSetCount);
			addDropSetCount*=2;
		}
		/** Hardware redundancy for Client Ptc **/
		else {
			addDropSetCount = (int)Math.ceil((chnlPtcDemandCount+clientPtcDemandCount) / maxPorts);
			System.out.println("addDropSetCount :" + addDropSetCount);
			addDropSetCount*=2;
		}

		System.out.println("addDropSetCount :" + addDropSetCount);

		if(numOfWavelength>0) {
			/** Based on wav/mpn count , change Wss Card Type and Cards Set Count **/
			int setRequired = (int)Math.ceil(numOfWavelength /maxPorts);

			// If wav/mpn are greater than 40 , then setCount becomes setRequired
			if (setRequired > addDropSetCount)
				addDropSetCount = setRequired;

			System.out.println("Set Count:: " + addDropSetCount + "setRequired:: " + setRequired);

			// Start from first set
			startSetNum = 1;

			// Add Card Sets to Db
			while (startSetNum <= addDropSetCount) {
				fAssignAddDropCards(networkid, nodeid, nodetype, wssType, startSetNum, nodedegree);
				startSetNum++;
			}
		}	

	}

	/**
	 * API for assignmnet of cards for Add Drop in Cd Roadm
	 * 
	 * @date 2nd May, 2018
	 * @author avinash
	 * @throws SQLException
	 */
	public void fAssignAddDropCardSetDoubleCardCdRoadmBrField(int networkid, int nodeid, int nodetype, int nodedegree)
			throws SQLException {

		int networkIdBf=Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());

		String wssType, wssSrNo;
		int wssEid, setNum;

		/* Default CardType and Card Set Count */
		String levelTwoWssType = ResourcePlanConstants.CardWss1x9;
		int addDropSetNum = 0;

		/** Find Add/Drop Wavelength Count for Node **/
		List<PortInfo> deletedLineports= dbService.getPortInfoService().FindAllUniqueDeletedBrField( networkIdBf,networkid, nodeid);
		List<PortInfo> addedLineports= dbService.getPortInfoService().FindAllUniqueAddedBrField(networkIdBf,networkid, nodeid);
		List<PortInfo> currentLineports= dbService.getPortInfoService().FindAllUniqueCommonBrField( networkIdBf,networkid, nodeid);

		System.out.println("currentLineports --"+currentLineports.size()+"addedLineports --"+addedLineports.size()+"deletedLineports --"+deletedLineports.size());

		double numOfWavelength=dbService.getPortInfoService().FindAllUniqueLinePorts(networkIdBf, nodeid).size();

		List<PortInfo> gfUniqueLinePorts=dbService.getPortInfoService().FindAllUniqueLinePorts(networkid, nodeid);
		int numOfWavelengthGf=0;
		if(gfUniqueLinePorts!=null)
			numOfWavelengthGf = gfUniqueLinePorts.size();

		int chnlPtcDemandCountGf = dbService.getViewService().FindChannelProtectionRoutes(networkid, nodeid).size();

		/** Sets already placed in GF **/
		List<CardInfo> WssSetListGf=dbService.getCardInfoService().FindWssLevelTwoCards(networkid, nodeid);
		int gfWssSets=0;
		if(WssSetListGf!=null)
			gfWssSets=WssSetListGf.size();
		System.out.println("gfWssSets --> "+ gfWssSets);

		//		System.out.println("numOfWavelengthGf --> "+numOfWavelengthGf);
		//
		//		
		//		/** No set in gf **/
		//		if(numOfWavelengthGf==0) {
		//			numOfWavelength = addedLineports.size();
		//		}else {			
		//			
		//			System.out.println("chnlPtcDemandCountGf --> "+chnlPtcDemandCountGf);
		//			int totalPortCountGf=0;
		//			for (int i = 0; i < WssSetListGf.size(); i++) {
		//				CardInfo card=WssSetListGf.get(i);
		//				if(card.getCardType().equals(ResourcePlanConstants.CardWss1x9))
		//					totalPortCountGf+=ResourcePlanConstants.MaxSwitchPortsWss2x1x9;
		//				else totalPortCountGf+=ResourcePlanConstants.MaxSwitchPortsWss2x1x20;
		//			}	
		//			
		//			/** Channel Ptc demand > 20 in GF will have max num of wss sets 
		//			 * So no need to add any more sets **/
		//			if(chnlPtcDemandCountGf>20){
		//				numOfWavelength=0;
		//			}
		//			/** In GF , Set max ports based on wss level 2 card used ( 2x1x20 or 2x1x9 )  **/
		//			else {
		//				numOfWavelength = addedLineports.size()-(totalPortCountGf-currentLineports.size()-deletedLineports.size());
		//			}
		//		
		////			else if(numOfWavelengthGf > maxPortsWss_2X1X9) {				
		////				numOfWavelength = addedLineports.size()-(maxPortsWss_2X1X20-currentLineports.size()-deletedLineports.size());				
		////			}			
		////			else {
		////				numOfWavelength = addedLineports.size()-(maxPortsWss_2X1X9-currentLineports.size()-deletedLineports.size());
		////			}
		//		}

		//		System.out.println("Added numOfWavelength for node id " + nodeid + " is " + numOfWavelength);
		System.out.println("Total numOfWavelength for node id " + nodeid + " is " + numOfWavelength);

		// Get all demands for CDC Node which have chnl protection in brown field
		//		int chnlPtcDemandAdded= dbService.getViewService().FindRouteMappingAddedBrField(networkid, networkIdBf, nodeid).size();
		int chnlPtcDemand= dbService.getPortInfoService().FindAllChnlPtcLines(networkIdBf, nodeid).size();

		/** If addition of lines require more set than are present in gf **/
		if(numOfWavelength>0) {		

			int maxPortsWss_2X1X9=ResourcePlanConstants.MaxSwitchPortsWss2x1x9;
			int maxPortsWss_2X1X20=ResourcePlanConstants.MaxSwitchPortsWss2x1x20;	
			int bfTotalSetsReqd;

			/** Atleast one set is required**/
			addDropSetNum=1;			

			/** handle case for channel ptc when gf had 0/1 wss sets**/
			if (chnlPtcDemand > 0 )
			{	
				addDropSetNum=2;

				//				/** Two wss sets of wss 2x1x9 will have total 18 switch ports **/
				//				maxPortsWss_2X1X9*=2; 				
				//				/** Two wss sets of wss 2x1x20 will have total 40 switch ports **/
				//				maxPortsWss_2X1X20*=2;
				//
				//				if(chnlPtcDemandAdded>20)
				//				{
				//					/** Two wss sets of wss 2x1x9 will have total 18 switch ports **/
				//					maxPortsWss_2X1X9*=4; 
				//					/** Two wss sets of wss 2x1x20 will have total 40 switch ports **/
				//					maxPortsWss_2X1X20*=4;
				//				}
			}

			/** Based on wav/mpn count , change Wss Card Type and Wss-Set Count **/
			if (numOfWavelength > maxPortsWss_2X1X9) {
				levelTwoWssType = ResourcePlanConstants.CardWss1x2x20;
				int setRequired = (int) Math.ceil(numOfWavelength / 20);			
				/** If wav/mpn > 0 ,then add more wss sets 
				 * Required when gf sets can't accomodate for new wavelengths**/

				//				bfTotalSetsReqd=gfWssSets+setRequired;
				bfTotalSetsReqd=setRequired;

				System.out.println("setRequired -> "+setRequired+" bfTotalSetsReqd -> "+bfTotalSetsReqd+" addDropSetNum -> "+addDropSetNum);

				if(bfTotalSetsReqd>addDropSetNum)
					addDropSetNum=bfTotalSetsReqd;

				System.out.println("Wavelength Count > 9 ---- NodeType:: " + nodeid + " Wss Card Type:: " + levelTwoWssType
						+ "Set Count:: " + addDropSetNum + "setRequired:: " + gfWssSets+setRequired);
			}else {

				bfTotalSetsReqd=gfWssSets+1;

				/** Channel Ptc case covered for less than 1 set required for num of wavelengths**/
				if(bfTotalSetsReqd>addDropSetNum)
					addDropSetNum=bfTotalSetsReqd;
			}

			// Start from first set
			setNum = gfWssSets+1;

			System.out.println(	" ***************  BrownField Wss Set *********** \n NodeId:: " + nodeid + " Wss Card Type:: " + levelTwoWssType
					+ " Total Sets Required:: " + addDropSetNum+" Starting setNum :: "+setNum);

			// Add Card Sets to Db
			while ( setNum <= addDropSetNum) {
				fAssignAddDropCards(networkIdBf, nodeid, nodetype, levelTwoWssType, setNum, nodedegree);
				setNum++;
			}
		}
		else {
			System.out.println(" ****** No wss set is required in BF *******");
		}

	}

	/**
	 * API for assignmnet of Wss8X12 for Add Drop in Cd Roadm
	 * 
	 * @date 2nd May, 2018
	 * @author avinash
	 * @throws SQLException
	 */
	public void fAssignAddDropCardSetSingleCardCdRoadmBrField(int networkid, int nodeid, int nodetype, int nodedegree)
			throws SQLException {

		int networkIdBf=Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());

		/* Get card redundancy */		
		EquipmentPreferences eqp=new EquipmentPreferences(dbService);
		String WssRedundancy = eqp.fgetRedundancyReq(networkIdBf, nodeid, ResourcePlanConstants.CatAddDropWssCdRoadm);
		String WssRedundancyGf = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatAddDropWssCdRoadm);

		/** Sets already placed in GF **/
		List<CardInfo> WssSetListGf=dbService.getCardInfoService().FindWssLevelOneCards(networkid, nodeid);
		int gfWssSets=WssSetListGf.size();

		String wssSrNo;
		int wssEid, startSetNum;


		/** Default CardType and Card Set Count and Max Ports **/
		int addDropSetCount = 0,
				maxPorts=ResourcePlanConstants.MaxSwitchPortsWss2x8x12;
		String wssType=ResourcePlanConstants.CardWss8x12;

		/** Find Add/Drop Wavelength Count for Node **/
		double numOfWavelength = dbService.getPortInfoService().FindAllUniqueLinePorts(networkIdBf, nodeid).size();
		System.out.println("Case : Red -> "+WssRedundancy+" NumOfWavelength for node id " + nodeid + " is " + numOfWavelength);

		/** Get all demands for CDC Node which have chnl protection **/
		//		List<RouteMapping> routesList = dbService.getViewService().FindChannelProtectionRoutes(networkIdBf, nodeid);
		//		double chnlPtcDemandCount = routesList.size() | 0;
		//		List <PortInfo> channelPtcLinesAdded= dbService.getPortInfoService().FindAllChnlPtcLinesAddedBrField(networkid,networkIdBf, nodeid);
		//		int chnlPtcDemandAdded=channelPtcLinesAdded.size()/2;
		//		

		/** Get all demands for CDC Node which have client protection with 1+1,1+1+R or 1+1+2R **/
		//		routesList = dbService.getViewService().FindClientProtectionRoutes(networkIdBf, nodeid);
		//		double clientPtcDemandCount = routesList.size() | 0;

		//		List <PortInfo> clientPtcLinesAdded= dbService.getPortInfoService().FindAllClientPtcLinesAddedBrField(networkid,networkIdBf, nodeid);
		//		int clientPtcDemandAdded=clientPtcLinesAdded.size()/2;
		//		
		//		List <PortInfo> clientUnPtcLinesAdded= dbService.getPortInfoService().FindAllClientPtcLinesAddedBrField(networkid,networkIdBf, nodeid);
		//		int clientUnPtcDemandAdded=clientPtcLinesAdded.size()/2;

		//		List <PortInfo> clientUnPtcLines= dbService.getPortInfoService().FindAllClientUnPtcLines(networkid, nodeid);
		//		int clientUnPtcDemandCountGf=clientUnPtcLines.size()/2;

		//		System.out.println("chnlPtcDemandCount:" + chnlPtcDemandCount+" clientPtcDemandCount:" + clientPtcDemandCount);//+" clientPtcDemandAdded:" + clientPtcDemandAdded);

		/** Assign max cards possible , later remove superflous cards **/
		addDropSetCount = (int)Math.ceil((numOfWavelength)/ maxPorts)*2;	
		System.out.println("addDropSetCount :" + addDropSetCount);

		// Start from first set
		startSetNum = gfWssSets+1;

		// If Total cards required in bf is greater than what is already placed in bf 
		if(addDropSetCount>gfWssSets) {
			// Add Card Sets to Db
			while (startSetNum <= addDropSetCount) {
				fAssignAddDropCards(networkIdBf, nodeid, nodetype, wssType, startSetNum, nodedegree);
				startSetNum++;
			}
		}
		else {
			System.out.println(" ****** No wss set is required in BF *******");
		}

		/** No hardware redundancy for Client Ptc **/
		//		if(WssRedundancy.equals(ResourcePlanConstants.No)) {
		//		// If there are demands that have chnl ptc ,then two/more sets are required
		//		if (chnlPtcDemandCount > 0)							
		//			addDropSetCount = (int)Math.ceil(chnlPtcDemandCount / maxPorts)*2;
		//		}
		//		/** Hardware redundancy for Client Ptc **/
		//		else {
		//				addDropSetCount = (int)Math.ceil((chnlPtcDemandCount+clientPtcDemandCount)/ maxPorts)*2;			
		//		}



		//		if(numOfWavelength>0) {
		//			/** Based on wav/mpn count , change Wss Card Type and Cards Set Count **/
		//			int setRequired = (int)Math.ceil(numOfWavelength /maxPorts);
		//
		//			// If wav/mpn are greater than 40 , then setCount becomes setRequired
		//			if (setRequired > addDropSetCount)
		//				addDropSetCount = setRequired;
		//
		//			System.out.println("Set Count:: " + addDropSetCount + "setRequired:: " + setRequired);
		//			
		//			System.out.println("gfWssSets --> "+ gfWssSets);
		//
		//			// Start from first set
		//			startSetNum = gfWssSets+1;
		//			
		//			// If Total cards required in bf is greater than what is already placed in bf 
		//			if(addDropSetCount>gfWssSets) {
		//				// Add Card Sets to Db
		//				while (startSetNum <= addDropSetCount) {
		//					fAssignAddDropCards(networkIdBf, nodeid, nodetype, wssType, startSetNum, nodedegree);
		//					startSetNum++;
		//				}
		//			}
		//			else {
		//				System.out.println(" ****** No wss set is required in BF *******");
		//			}
		//		}	

	}

	/**
	 * API for assignmnet of cards for Add Drop in CdC Roadm
	 * @date 14th May, 2018
	 * @author avinash 
	 * @throws SQLException 
	 */
	public void fAssignAddDropCardSetCdCRoadm(int networkid,int nodeid,int nodetype,int nodedegree) throws SQLException{

		int mcsRequired=1,mcsNum;
		/*Find Unique Lineport Count for Node*/

		List<PortInfo> lineports= dbService.getPortInfoService().FindAllUniqueLinePorts(networkid, nodeid);

		double numOfWavelength=lineports.size();
		System.out.println("numOfWavelength for node id "+ nodeid+" is "+numOfWavelength+" lineports.size()::"+lineports.size());

		/*Based on wav/mpn count , change Cards Count*/
		if(numOfWavelength>ResourcePlanConstants.MaxSwitchPortsMcs)
			mcsRequired=(int)Math.ceil(numOfWavelength/ResourcePlanConstants.MaxSwitchPortsMcs);

		System.out.println("NodeType:: "+nodetype+"mcsRequired:: "+mcsRequired);

		//Start from second set; One mcs is added as default 
		mcsNum=2;

		// Add Card Sets to Db
		while(mcsNum<=mcsRequired)
		{			
			fAssignAddDropCards(networkid,nodeid,nodetype,ResourcePlanConstants.CardMcs,mcsNum,nodedegree);
			mcsNum++;
		}
	}

	/**
	 * API for assignmnet of cards for Add Drop in CdC Roadm
	 * @date 14th May, 2018
	 * @author avinash 
	 * @throws SQLException 
	 */
	public void fAssignAddDropCardSetCdCRoadmBrField(int networkid,int nodeid,int nodetype,int nodedegree) throws SQLException{

		int mcsRequired=0,mcsNum;
		int networkIdBf=Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		double numOfWavelength;

		/*Find Unique Lineport Count for Node*/

		List<PortInfo> deletedLineports= dbService.getPortInfoService().FindAllUniqueDeletedBrField( networkid,networkIdBf,nodeid);
		List<PortInfo> addedLineports= dbService.getPortInfoService().FindAllUniqueAddedBrField(networkid,networkIdBf,nodeid);
		List<PortInfo> currentLineports= dbService.getPortInfoService().FindAllUniqueCommonBrField( networkid,networkIdBf,nodeid);
		System.out.println("fAssignAddDropCardSetCdCRoadmBrField --- Nodeid -> "+nodeid);
		System.out.println("currentLineports --"+currentLineports.size()+"addedLineports --"+addedLineports.size()+"deletedLineports --"+deletedLineports.size());


		/** No traffic in gf and traffic added in bf **/
		/** Add the default set of mcs and edfa **/
		if(currentLineports.size()==0) {
			numOfWavelength = addedLineports.size();
		}
		/** Add cards for extra traffic **/
		else {
			numOfWavelength = addedLineports.size()-(ResourcePlanConstants.MaxSwitchPortsMcs-currentLineports.size()+deletedLineports.size());
		}		

		System.out.println("numOfWavelength for node id "+ nodeid+" is "+numOfWavelength);		

		/** Based on wav/mpn count. This represents  **/
		if(numOfWavelength>0)
		{
			mcsRequired=(int)Math.ceil(numOfWavelength/ResourcePlanConstants.MaxSwitchPortsMcs);
			System.out.println("NodeType:: "+nodetype+"mcsRequired:: "+mcsRequired);

			// List of already assigned MCS
			List<CardInfo> mcsList=dbService.getCardInfoService().FindCardInfoByCardType(networkIdBf, nodeid, ResourcePlanConstants.CardMcs);

			// Add Card Sets to Db
			while(mcsRequired>0 && mcsRequired>mcsList.size())
			{			
				fAssignAddDropCards(networkIdBf,nodeid,nodetype,ResourcePlanConstants.CardMcs,mcsRequired,nodedegree);
				mcsRequired--;
			}
		}

	}

	/**
	 * API for assignmnet of cards for Add Drop in Cd Roadm, D Roadm and CDC Roadm
	 * 
	 * @date 16th April, 2018
	 * @author avinash
	 */
	public void fAssignAddDropCards(int networkid, int nodeid, int nodeType, String cardType, int setNum,
			int nodedegree) throws SQLException {

		switch (nodeType) {
		case MapConstants.cdRoadm:
		{
			switch (cardType) {
			case ResourcePlanConstants.CardWss8x12:
			{
				int []direction=ResourcePlanUtils.fGetWssDirectionCdRoadm(setNum,cardType);
				// Add WSS 2x1x9 card for 8 direction node 
				String location=rpu.fGetFirstFreeDoubleSlotDb(networkid, nodeid);
				int[] id=rpu.locationIds(location);					 				 
				fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],cardType,""+direction[0],0,0,rpu.fgetEIdFromCardType(cardType),"",ResourcePlanConstants.ZERO,"");

				logger.info("fAssignAddDropCards --- Node Degree ::"+nodedegree);

				// Set 1 edfa added by default ; assigned in common cards 
				// For set 2 
				if(setNum>1){
					// Add EDFA 
					fAssignEdfaCard(networkid,nodeid,nodedegree);
				}
			}
			break;

			case ResourcePlanConstants.CardWss1x9:
			case ResourcePlanConstants.CardWss1x2x20:
			{
				int []direction=ResourcePlanUtils.fGetWssDirectionCdRoadm(setNum,cardType);
				// Add WSS 2x1x9 card for 8 direction node 
				String location=rpu.fGetFirstFreeDoubleSlotDb(networkid, nodeid);
				int[] id=rpu.locationIds(location);					 
				fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],ResourcePlanConstants.CardWss1x9,""+direction[0],0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardWss1x9),"",ResourcePlanConstants.ZERO,"");

				//Add WSS (cardType) 
				location=rpu.fGetFirstFreeDoubleSlotDb(networkid, nodeid);
				id=rpu.locationIds(location);					 
				fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],cardType,""+direction[1],0,0,rpu.fgetEIdFromCardType(cardType),"",ResourcePlanConstants.ZERO,"");

				logger.info("fAssignAddDropCards --- Node Degree ::"+nodedegree);

				// Set 1 edfa added by default ; assigned in common cards 
				// For set 2 
				if(setNum>1){
					// Add EDFA 
					fAssignEdfaCard(networkid,nodeid,nodedegree);
				}
			}
			break;

			default:
				break;
			}


		}
		break;
		case MapConstants.roadm:
		{

			// Add EDFA 
			fAssignEdfaCard(networkid,nodeid,nodedegree);

			// Add MCS card
			String location=rpu.fGetFirstFreeDoubleSlotDb(networkid, nodeid);
			int []id=rpu.locationIds(location);					 
			fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],ResourcePlanConstants.CardMcs,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardMcs),"",ResourcePlanConstants.ZERO,"");

			logger.info("fAssignAddDropCards --- Node Degree ::"+nodedegree);

		}
		break;

		// case MapConstants.dRoadm:
		// break;

		default:
			break;
		}

	}

	public void fAssignEdfaCard(int networkid,int nodeid,int nodedegree){
		// Add EDFA 
		String location=rpu.fGetFirstFreeDoubleSlotDb(networkid, nodeid);
		int[] id=rpu.locationIds(location);
		int ramanLinksCount=dbService.getLinkService().FindAllRamanLinks(networkid, nodeid).size();

		// Assign first edfa card only if there are links other than raman
		if(ramanLinksCount!=nodedegree)
			fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],ResourcePlanConstants.CardEdfa,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa),"",ResourcePlanConstants.ZERO,"");	

		// If nodedegree>4 or There is atleast 1 raman link 
		if(ramanLinksCount>0 | nodedegree>4){
			// Add EDFA 
			location=rpu.fGetFirstFreeDoubleSlotDb(networkid, nodeid);
			id=rpu.locationIds(location);					 
			fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],ResourcePlanConstants.CardEdfa,"",0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardEdfa),"",ResourcePlanConstants.ZERO,"");
		}
	}



	/**
	 * @desc This Api gives the Filler Plate slots in return based on the input params given
	 * @created 21st April, 2018
	 * @param networkId
	 */
	public List<CardInfo> fFindFillerPlateSlots(int networkId, Integer... optionalParams) throws SQLException {

		logger.debug("fFindFillerPlateSlots");

		int nodeId, rackId, subRackId;		

		List<CardInfo> cardInfoList= null;

		if (dbService != null) {

			if (optionalParams != null && MapConstants.I_THREE == optionalParams.length) {
				nodeId = optionalParams[MapConstants.I_ZERO];
				rackId = optionalParams[MapConstants.I_ONE];
				subRackId = optionalParams[MapConstants.I_TWO];
			} else {

				nodeId = MapConstants.I_MINUS_ONE;
				rackId = MapConstants.I_MINUS_ONE;
				subRackId = MapConstants.I_MINUS_ONE;
			}

			logger.debug(
					"networkId, nodeId, rack, subrack: " + networkId + "," + nodeId + "," + rackId + "," + subRackId);

			cardInfoList = dbService.getCardInfoService().FindAll(networkId, nodeId);			

			///logger.debug(" cardInfoList: " + cardInfoList);		

			cardInfoList = cardInfoList.stream().filter(
					cardInfoObject -> cardInfoObject.getRack() == rackId && cardInfoObject.getSbrack() == subRackId
					&& cardInfoObject.getCardType().equalsIgnoreCase(ResourcePlanConstants.FillerPlate)
					)
					.collect(Collectors.toList());			


			logger.debug(" listWithFillerPlateSlots: " + cardInfoList);

		}

		else {

			logger.error("DB Service NullPointerException in fFindFillerPlateSlots");
		}

		return cardInfoList;
	}

	/**
	 * @desc API for Deassignmnet of filler plates for the given BrownField network id in order to make slots free occupied through Filler Plates
	 * 
	 * @date 22nd May, 2018
	 * @author hp
	 */
	public void fDeAssignFillerPlates(int networkId) throws SQLException {

		logger.debug("fDeAssignFillerPlates for NetworkId : " + networkId);

		List<Node> nodeIdList = dbService.getNodeService().FindAll(networkId);

		logger.debug("nodeIdList: " + nodeIdList);

		try {

			nodeIdList.forEach(node -> {

				int rackCount = dbService.getCardInfoService().FindRackCount(networkId, node.getNodeId());
				logger.debug("rackCount, node id :  " + rackCount + ", " + node.getNodeId());

				for (int rack = MapConstants.I_ONE; rack <= rackCount; rack++) {/** rack times */

					int subrackCount = (int) dbService.getCardInfoService()
							.FindMaxRackSubrackCount(networkId, node.getNodeId(), rack).get(0).get("Sbrack");
					logger.debug("subrackCount, node id :  " + subrackCount + ", " + node.getNodeId());

					for (int subrack = MapConstants.I_ONE; subrack <= subrackCount; subrack++) {/** subrack times */

						List<CardInfo> fillerPlateSlots= null;

						try {
							logger.debug("nodeId, rack, subrack: " + node.getNodeId() + "," + rack + "," + subrack);

							/** Find empty slots for given : network,node,rack,subrack */
							fillerPlateSlots = fFindFillerPlateSlots(networkId, node.getNodeId(), rack, subrack);


							/** save filler plates to db */
							if (fillerPlateSlots != null) {

								logger.debug(" fillerPlateSlots : " + fillerPlateSlots);

								fillerPlateSlots.forEach( cardInfoObj -> {

									logger.debug(" cardInfoObj  rack, subrack, cardid, cardtype :"+ cardInfoObj.getRack() +","+
											cardInfoObj.getSbrack() +","+
											cardInfoObj.getCard() +","+
											cardInfoObj.getCardType());

									dbService.getCardInfoService().DeleteCard(networkId, node.getNodeId(), cardInfoObj.getRack(),
											cardInfoObj.getSbrack(), cardInfoObj.getCard());													             

									logger.debug("Filler Plate Deleted");

								});

							}
							else{
								logger.debug("fillerPlateSlots is Null ");
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}

					);
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}

	}


	/**
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignOTDRCards(int networkid) throws SQLException {
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		for (int k = 0; k < nodelist.size(); k++) {
			int nodeid = nodelist.get(k);
			EquipmentPreference eq=dbService.getEquipmentPreferenceService().FindPreferredEq(networkid, nodeid, ResourcePlanConstants.CatOtdr);
			if(!eq.getCardType().equals(ResourcePlanConstants.None))
				fAssignOTDRCardForNode(networkid,nodeid);
		}
	}

	/**
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignOTDRCardForNode(int networkid,int nodeid) throws SQLException {
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		Node node=dbService.getNodeService().FindNode(networkid, nodeid);
		int nodetype=node.getNodeType();
		int nodedegree=node.getDegree();
		String loc=rpu.fGetFirstFreeDoubleSlotDb(networkid, nodeid);
		int[] id = rpu.locationIds(loc);
		switch (nodetype) 
		{
		case MapConstants.roadm:
		{
			if(nodedegree<2) {
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2],
						ResourcePlanConstants.CardOtdr1X4, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOtdr1X4), "", ResourcePlanConstants.ZERO, "");
			}else if(nodedegree>2 && nodedegree<=4){
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2],
						ResourcePlanConstants.CardOtdr1X8, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOtdr1X8), "", ResourcePlanConstants.ZERO, "");
			}else{
				fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2],
						ResourcePlanConstants.CardOtdr1X16, "", 0, 0,
						rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOtdr1X16), "", ResourcePlanConstants.ZERO, "");
			}				
		}
		break;

		case MapConstants.twoBselectRoadm:
		case MapConstants.ila:
		{
			fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2],
					ResourcePlanConstants.CardOtdr1X4, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOtdr1X4), "", ResourcePlanConstants.ZERO, "");
		}
		break;

		case MapConstants.te:
		{
			fSaveCardInDb(networkid, nodeid, id[0], id[1], id[2],
					ResourcePlanConstants.CardOtdr1X2, "", 0, 0,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOtdr1X2), "", ResourcePlanConstants.ZERO, "");
		}
		break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignAmplifierCardsInNetwork(int networkid) throws SQLException {
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);

		//			for (Integer nodeid : nodelist) {
		//				Node node=dbService.getNodeService().FindNode(networkid, nodeid);
		//				int nodetype=node.getNodeType();
		//				int nodedegree=node.getDegree();
		//				
		//			}
		List<Link> allLinks=dbService.getLinkService().FindAll(networkid);
		System.out.println("All Links Size ::"+allLinks.size());

		allLinks.forEach(link->{
			String linktype=link.getLinkType();
			String IlaType=ResourcePlanConstants.CardIla;

			int srcNodeId=link.getSrcNode(),
					destNodeId=link.getDestNode();
			String srcNodeDir=link.getSrcNodeDirection(),
					destNodeDir=link.getDestNodeDirection();

			int srcNodeType=dbService.getNodeService().FindNode(networkid, srcNodeId).getNodeType();
			int destNodeType=dbService.getNodeService().FindNode(networkid, destNodeId).getNodeType();

			System.out.println("linkId :: "+link.getLinkId()+"linkType :: "+link.getLinkType()+" srcNodeId :: "+srcNodeId+" destNodeId :: "+destNodeId+
					" srcNodeDir :: "+srcNodeDir+" destNodeDir :: "+destNodeDir);

			String AmplifierCardtype,locAmpCardSrc,locAmpCardDest;

			int srcNodeAmpCardCount=dbService.getCardInfoService().FindAmplifierCardCount(networkid, srcNodeId).size(),
					destNodeAmpCardCount=dbService.getCardInfoService().FindAmplifierCardCount(networkid, destNodeId).size();
			System.out.println("srcNodeAmpCardCount :: "+srcNodeAmpCardCount+" destNodeAmpCardCount :: "+destNodeAmpCardCount);

			if(linktype.equals(ResourcePlanConstants.RamanHybridLink)) {
				AmplifierCardtype=ResourcePlanConstants.CardRamanHybrid;
				locAmpCardSrc = rpu.fgetLocationRaman(srcNodeAmpCardCount+1);
				locAmpCardDest = rpu.fgetLocationRaman(destNodeAmpCardCount+1);
				IlaType=ResourcePlanConstants.CardIlaRamanHybrid;
			}else if(linktype.equals(ResourcePlanConstants.RamanDraLink)) {
				AmplifierCardtype=ResourcePlanConstants.CardRamanDra;
				locAmpCardSrc = rpu.fgetLocationRaman(srcNodeAmpCardCount+1);
				locAmpCardDest = rpu.fgetLocationRaman(destNodeAmpCardCount+1);
				IlaType=ResourcePlanConstants.CardIlaRamanDra;
			}else {
				AmplifierCardtype=ResourcePlanConstants.CardPaBa;
				locAmpCardSrc = rpu.fgetLocationPaBa(srcNodeAmpCardCount+1);
				locAmpCardDest = rpu.fgetLocationPaBa(destNodeAmpCardCount+1);
			}

			int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());

			System.out.println("locAmpCardSrc :: "+locAmpCardSrc+" locAmpCardDest :: "+
					locAmpCardDest+" AmplifierEqid :: "+AmplifierEqid);

			/* SrcNode type --> Amp Card Type Assign */
			switch (srcNodeType) {
			case MapConstants.ila:
			{
				int rackid = ResourcePlanConstants.ONE;
				int sbrackid = ResourcePlanConstants.TWO;
				int srcNodeIlaCardCount=dbService.getCardInfoService().FindIlaCardCount(networkid, srcNodeId).size();
				int cardid=srcNodeIlaCardCount==0?ResourcePlanConstants.ONE:ResourcePlanConstants.THIRTEEN;

				/* If Ila type is not raman , then use Slot 14 for second ILA card*/
				if(cardid==ResourcePlanConstants.THIRTEEN && IlaType.equals(ResourcePlanConstants.CardIla))
					cardid=ResourcePlanConstants.FOURTEEN;

				fSaveCardInDb(networkid, srcNodeId, rackid, sbrackid, cardid, IlaType,
						srcNodeDir, 0, 0, rpu.fgetEIdFromCardType(IlaType), "",
						ResourcePlanConstants.ZERO, "");
			}
			break;
			case MapConstants.roadm:
			case MapConstants.cdRoadm:
			case MapConstants.twoBselectRoadm:
			case MapConstants.te:
			{
				/* **** Add Raman Amplifier at srcNode in links src direction **** */
				String[] lp = locAmpCardSrc.split("_");
				int rackid = Integer.parseInt(lp[0].toString());
				int sbrackid = Integer.parseInt(lp[1].toString());
				int cardid = Integer.parseInt(lp[2].toString());

				CardInfo card = fSaveCardInDb(networkid, srcNodeId, rackid, sbrackid, cardid, AmplifierCardtype,
						srcNodeDir, 0, 0, AmplifierEqid, "",
						ResourcePlanConstants.ZERO, "");
			}					
			break;

			default:
				break;
			}		

			/* DestNode type --> Amp Card Type Assign */
			switch (destNodeType) {
			case MapConstants.ila:
			{
				int rackid = ResourcePlanConstants.ONE;
				int sbrackid = ResourcePlanConstants.TWO;
				int destNodeIlaCardCount=dbService.getCardInfoService().FindIlaCardCount(networkid, destNodeId).size();
				int cardid=destNodeIlaCardCount==0?ResourcePlanConstants.ONE:ResourcePlanConstants.THIRTEEN;

				/* If Ila type is not raman , then use Slot 14 for second ILA card*/
				if(cardid==ResourcePlanConstants.THIRTEEN && IlaType.equals(ResourcePlanConstants.CardIla))
					cardid=ResourcePlanConstants.FOURTEEN;

				fSaveCardInDb(networkid, destNodeId, rackid, sbrackid, cardid, IlaType,
						destNodeDir, 0, 0, rpu.fgetEIdFromCardType(IlaType), "",
						ResourcePlanConstants.ZERO, "");
			}
			break;
			case MapConstants.roadm:
			case MapConstants.cdRoadm:
			case MapConstants.twoBselectRoadm:
			case MapConstants.te:
			{
				/* **** Add Raman Amplifier at destNode in links dest direction **** */
				String[] lp = locAmpCardDest.split("_");
				int rackid = Integer.parseInt(lp[0].toString());
				int sbrackid = Integer.parseInt(lp[1].toString());
				int cardid = Integer.parseInt(lp[2].toString());

				CardInfo card = fSaveCardInDb(networkid, destNodeId, rackid, sbrackid, cardid, AmplifierCardtype,
						destNodeDir, 0, 0, AmplifierEqid, "",
						ResourcePlanConstants.ZERO, "");
			}					
			break;

			default:
				break;
			}		


		});		

	}

	/**
	 * 
	 * @param networkid
	 * @throws SQLException
	 */
	public void fAssignAmplifierCardsInNetworkBf(int networkidGf) throws SQLException {


		int networkidBf=Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkidGf).toString());
		//			List<Integer> nodelist = rpu.fgetNodesToConfigure(networkidGf);
		System.out.println("************** fAssignAmplifierCardsInNetworkBf ***************** :: NetworkId::"+networkidBf);

		List<Link> deletedLinks=dbService.getLinkService().findDeletedLinksInBrField(networkidGf, networkidBf);
		System.out.println("deletedLinks Size ::"+deletedLinks.size());
		deletedLinks.forEach(link->{

		});

		List<Link> addedLinks=dbService.getLinkService().FindAddedLinksInBrField(networkidGf, networkidBf);
		System.out.println("addedLinks Size ::"+addedLinks.size());
		addedLinks.forEach(link->{
			String linktype=link.getLinkType();
			String IlaType=ResourcePlanConstants.CardIla;

			int srcNodeId=link.getSrcNode(),
					destNodeId=link.getDestNode();
			String srcNodeDir=link.getSrcNodeDirection(),
					destNodeDir=link.getDestNodeDirection();

			int srcNodeType=dbService.getNodeService().FindNode(networkidBf, srcNodeId).getNodeType();
			int destNodeType=dbService.getNodeService().FindNode(networkidBf, destNodeId).getNodeType();
			System.out.println("linkId :: "+link.getLinkId()+"linkType :: "+link.getLinkType()+" srcNodeId :: "+srcNodeId+" destNodeId :: "+destNodeId+
					" srcNodeDir :: "+srcNodeDir+" destNodeDir :: "+destNodeDir);

			String AmplifierCardtype,locAmpCardSrc,locAmpCardDest;

			int srcNodeAmpCardCount=dbService.getCardInfoService().FindAmplifierCardCount(networkidBf, srcNodeId).size(),
					destNodeAmpCardCount=dbService.getCardInfoService().FindAmplifierCardCount(networkidBf, destNodeId).size();
			System.out.println("srcNodeAmpCardCount :: "+srcNodeAmpCardCount+" destNodeAmpCardCount :: "+destNodeAmpCardCount);

			if(linktype.equals(ResourcePlanConstants.RamanHybridLink)) {
				AmplifierCardtype=ResourcePlanConstants.CardRamanHybrid;
				locAmpCardSrc = rpu.fgetLocationRaman(srcNodeAmpCardCount+1);
				locAmpCardDest = rpu.fgetLocationRaman(destNodeAmpCardCount+1);
				IlaType=ResourcePlanConstants.CardIlaRamanHybrid;
			}else if(linktype.equals(ResourcePlanConstants.RamanDraLink)) {
				AmplifierCardtype=ResourcePlanConstants.CardRamanDra;
				locAmpCardSrc = rpu.fgetLocationRaman(srcNodeAmpCardCount+1);
				locAmpCardDest = rpu.fgetLocationRaman(destNodeAmpCardCount+1);
				IlaType=ResourcePlanConstants.CardIlaRamanDra;
			}else {
				AmplifierCardtype=ResourcePlanConstants.CardPaBa;
				locAmpCardSrc = rpu.fgetLocationPaBa(srcNodeAmpCardCount+1);
				locAmpCardDest = rpu.fgetLocationPaBa(destNodeAmpCardCount+1);
			}

			int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());

			System.out.println("locAmpCardSrc :: "+locAmpCardSrc+" locAmpCardDest :: "+
					locAmpCardDest+" AmplifierEqid :: "+AmplifierEqid);

			/* SrcNode type --> Amp Card Type Assign */
			switch (srcNodeType) {
			case MapConstants.ila:
			{
				int rackid = ResourcePlanConstants.ONE;
				int sbrackid = ResourcePlanConstants.TWO;
				int srcNodeIlaCardCount=dbService.getCardInfoService().FindIlaCardCount(networkidBf, srcNodeId).size();
				int cardid=srcNodeIlaCardCount==0?ResourcePlanConstants.ONE:ResourcePlanConstants.THIRTEEN;

				/* If Ila type is not raman , then use Slot 14 for second ILA card*/
				if(cardid==ResourcePlanConstants.THIRTEEN && IlaType.equals(ResourcePlanConstants.CardIla))
					cardid=ResourcePlanConstants.FOURTEEN;

				fSaveCardInDb(networkidBf, srcNodeId, rackid, sbrackid, cardid, IlaType,
						srcNodeDir, 0, 0, rpu.fgetEIdFromCardType(IlaType), "",
						ResourcePlanConstants.ZERO, "");
			}
			break;
			case MapConstants.roadm:
			case MapConstants.cdRoadm:
			case MapConstants.twoBselectRoadm:
			case MapConstants.te:
			{
				/* **** Add Raman Amplifier at srcNode in links src direction **** */
				String[] lp = locAmpCardSrc.split("_");
				int rackid = Integer.parseInt(lp[0].toString());
				int sbrackid = Integer.parseInt(lp[1].toString());
				int cardid = Integer.parseInt(lp[2].toString());

				CardInfo card = fSaveCardInDb(networkidBf, srcNodeId, rackid, sbrackid, cardid, AmplifierCardtype,
						srcNodeDir, 0, 0, AmplifierEqid, "",
						ResourcePlanConstants.ZERO, "");
			}					
			break;

			default:
				break;
			}		

			/* DestNode type --> Amp Card Type Assign */
			switch (destNodeType) {
			case MapConstants.ila:
			{
				int rackid = ResourcePlanConstants.ONE;
				int sbrackid = ResourcePlanConstants.TWO;
				int destNodeIlaCardCount=dbService.getCardInfoService().FindIlaCardCount(networkidBf, destNodeId).size();
				int cardid=destNodeIlaCardCount==0?ResourcePlanConstants.ONE:ResourcePlanConstants.THIRTEEN;

				/* If Ila type is not raman , then use Slot 14 for second ILA card*/
				if(cardid==ResourcePlanConstants.THIRTEEN && IlaType.equals(ResourcePlanConstants.CardIla))
					cardid=ResourcePlanConstants.FOURTEEN;

				fSaveCardInDb(networkidBf, destNodeId, rackid, sbrackid, cardid, IlaType,
						destNodeDir, 0, 0, rpu.fgetEIdFromCardType(IlaType), "",
						ResourcePlanConstants.ZERO, "");
			}
			break;
			case MapConstants.roadm:
			case MapConstants.cdRoadm:
			case MapConstants.twoBselectRoadm:
			case MapConstants.te:
			{
				/* **** Add Raman Amplifier at destNode in links dest direction **** */
				String[] lp = locAmpCardDest.split("_");
				int rackid = Integer.parseInt(lp[0].toString());
				int sbrackid = Integer.parseInt(lp[1].toString());
				int cardid = Integer.parseInt(lp[2].toString());

				CardInfo card = fSaveCardInDb(networkidBf, destNodeId, rackid, sbrackid, cardid, AmplifierCardtype,
						destNodeDir, 0, 0, AmplifierEqid, "",
						ResourcePlanConstants.ZERO, "");
			}					
			break;

			default:
				break;
			}		


		});		

		//			List<Link> modLinks=dbService.getLinkService().findDeletedLinksInBrField(networkidGf, networkidBf);
		//			modLinks.forEach(link->{
		//				String linktype=link.getLinkType();
		//				String IlaType=ResourcePlanConstants.CardIla;
		//
		//				int srcNodeId=link.getSrcNode(),
		//					destNodeId=link.getDestNode();
		//				String srcNodeDir=link.getSrcNodeDirection(),
		//					   destNodeDir=link.getDestNodeDirection();
		//				
		//				int srcNodeType=dbService.getNodeService().FindNode(networkidGf, srcNodeId).getNodeType();
		//				int destNodeType=dbService.getNodeService().FindNode(networkidGf, destNodeId).getNodeType();
		//				
		//				fRemoveCommonCardsPerDirectionDb(networkidBf, srcNodeId, srcNodeType, 1, srcNodeDir);
		//			});		

	}

	public void fAssignAmplifierCardsForNodesAllDirections(int networkid,int nodeid) {
		List<Link> nodeLinks=dbService.getLinkService().FindAllLinks(networkid, nodeid);
		System.out.println("*********** fAssignAmplifierCardsForNode  *******************");
		System.out.println("NetworkId ::"+networkid+" NodeId::"+nodeid);
		System.out.println("Node Links size::"+nodeLinks.size());

		nodeLinks.forEach(link->{
			fAssignAmplifierCardsForNode(networkid,nodeid,link);
		});
	}

	public void fAssignAmplifierCardsForNode(int networkid,int nodeid,Link link) {
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		String linktype,nodeDir,IlaType;
		int nodeType;

		linktype=link.getLinkType();
		IlaType=ResourcePlanConstants.CardIla;
		nodeType=dbService.getNodeService().FindNode(networkid, nodeid).getNodeType();

		if(link.getSrcNode()==nodeid)
			nodeDir=link.getSrcNodeDirection();
		else nodeDir=link.getDestNodeDirection();

		String AmplifierCardtype,locAmpCard,PrefChassisType;

		int nodeAmpCardCount=dbService.getCardInfoService().FindAmplifierCardCount(networkid, nodeid).size();
		System.out.println("srcNodeAmpCardCount :: "+nodeAmpCardCount+" linktype:"+linktype);

		if(linktype.equals(ResourcePlanConstants.RamanHybridLink)) {
			AmplifierCardtype=ResourcePlanConstants.CardRamanHybrid;
			locAmpCard = rpu.fGetFirstFreeRamanLocation(networkid,nodeid);
			IlaType=ResourcePlanConstants.CardIlaRamanHybrid;
		}else if(linktype.equals(ResourcePlanConstants.RamanDraLink)) {
			AmplifierCardtype=ResourcePlanConstants.CardRamanDra;
			locAmpCard = rpu.fGetFirstFreeRamanLocation(networkid,nodeid);
			IlaType=ResourcePlanConstants.CardIlaRamanDra;
		}else {
			AmplifierCardtype=ResourcePlanConstants.CardPaBa;
			locAmpCard = rpu.fGetFirstFreePaBaLocation(networkid,nodeid);
		}

		int AmplifierEqid = rpu.fgetEIdFromCardType(AmplifierCardtype.toString().trim());		

		/* **** Add Raman Amplifier at Node in links direction **** */
		String[] lp = locAmpCard.split("_");
		int rackid = Integer.parseInt(lp[0].toString());
		int sbrackid = Integer.parseInt(lp[1].toString());
		int cardid = Integer.parseInt(lp[2].toString());

		System.out.println("locAmpCard :: "+locAmpCard+" AmplifierEqid :: "+AmplifierEqid);

		/* nodeType --> Amp Card Type Assign */
		switch (nodeType) {
		case MapConstants.ila:
		{
			rackid = ResourcePlanConstants.ONE;
			sbrackid = ResourcePlanConstants.TWO;
			try {
				PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
						ResourcePlanConstants.ParamSubrack, ""+rackid+"-"+sbrackid)[0].toString();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				PrefChassisType=ResourcePlanConstants.SixSlotChassis;
			}
			int srcNodeIlaCardCount=dbService.getCardInfoService().FindIlaCardCount(networkid, nodeid).size();
			if(PrefChassisType.equals(ResourcePlanConstants.SixSlotChassis)) {
				cardid=srcNodeIlaCardCount==0?ResourcePlanConstants.SIX:ResourcePlanConstants.FIVE;
			}
			else {
				cardid=srcNodeIlaCardCount==0?ResourcePlanConstants.ONE:ResourcePlanConstants.THIRTEEN;
			}

			/* If Ila type is not raman , then use Slot 14 for second ILA card*/
			if(cardid==ResourcePlanConstants.THIRTEEN && IlaType.equals(ResourcePlanConstants.CardIla))
				cardid=ResourcePlanConstants.FOURTEEN;

			fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, IlaType,
					nodeDir, 0, 0, rpu.fgetEIdFromCardType(IlaType), "",
					ResourcePlanConstants.ZERO, "");
		}
		break;
		case MapConstants.roadm:
		case MapConstants.cdRoadm:
		case MapConstants.twoBselectRoadm:
		case MapConstants.te:
		{
			/* **** Add Raman Amplifier at srcNode in links src direction **** */

			CardInfo card = fSaveCardInDb(networkid, nodeid, rackid, sbrackid, cardid, AmplifierCardtype,
					nodeDir, 0, 0, AmplifierEqid, "",
					ResourcePlanConstants.ZERO, "");
		}					
		break;

		default:
			break;
		}	
	}

	public void fAssignOcmCardInBrownField(int networkid,int nodeid,int ocmid) {
		String location;
		switch (ocmid) {
		case ResourcePlanConstants.ONE:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.TWO;
			int slotid=ResourcePlanConstants.FIVE;	
			String direction=MapConstants.EAST;

			if(rpu.fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, slotid))
			{
				fSaveCardInDb(networkid, nodeid, sbrackid, sbrackid, slotid, ResourcePlanConstants.CardOcm1x8,
						direction,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8),"",ResourcePlanConstants.ZERO,"");
			}else {
				location=rpu.fGetFirstFreeSlotDb(networkid, nodeid);
				int[] lp=rpu.locationIds(location);
				rackid=lp[0];
				sbrackid=lp[1];
				slotid=lp[2];			
				fSaveCardInDb(networkid, nodeid, sbrackid, sbrackid, slotid, ResourcePlanConstants.CardOcm1x8,
						direction,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8),"",ResourcePlanConstants.ZERO,"");
			}
		}
		break;
		case ResourcePlanConstants.TWO:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.THREE;
			int slotid=ResourcePlanConstants.FIVE;		
			String direction=MapConstants.NE;

			if(rpu.fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, slotid))
			{
				fSaveCardInDb(networkid, nodeid, sbrackid, sbrackid, slotid, ResourcePlanConstants.CardOcm1x8,
						direction,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8),"",ResourcePlanConstants.ZERO,"");
			}else {
				location=rpu.fGetFirstFreeSlotDb(networkid, nodeid);
				int[] lp=rpu.locationIds(location);
				rackid=lp[0];
				sbrackid=lp[1];
				slotid=lp[2];			
				fSaveCardInDb(networkid, nodeid, rackid, sbrackid, slotid, ResourcePlanConstants.CardOcm1x8,
						direction,0,0,rpu.fgetEIdFromCardType(ResourcePlanConstants.CardOcm1x8),"",ResourcePlanConstants.ZERO,"");
			}
		}
		break;
		default:
			break;
		}
	}


}
