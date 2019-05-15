package application.controller;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import org.apache.log4j.Logger;

import application.MainMap;
import application.constants.DataConfigConstants;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.AmplifierConfig;
import application.model.CardInfo;
import application.model.Circuit;
import application.model.Demand;
import application.model.EquipmentPreference;
import application.model.McsMap;
import application.model.McsPortMapping;
import application.model.OcmConfig;
import application.model.PortInfo;
import application.model.PtcClientProtInfo;
import application.model.PtcLineProtInfo;
import application.model.TpnPortInfo;
import application.model.VoaConfigInfo;
import application.model.WssDirectionConfig;
import application.service.DbService;
import application.service.OcmConfigService;
import application.service.PtcClientProtInfoService;

public class InitializeConfigurationData {
	
	DbService dbService; 	
	ResourcePlanUtils rpu; 
	 DecimalFormat dcm = new DecimalFormat("00.#");
	 public static Logger logger = Logger.getLogger(InitializeConfigurationData.class.getName());
	public InitializeConfigurationData(DbService dbService) {		
		super();
//		MainMap.logger = MainMap.logger.getLogger(InitializeConfigurationData.class.getName());		
		this.dbService = dbService;		
		rpu = new ResourcePlanUtils(dbService);	
	}

	public DbService getDbService() {
		return dbService;
	}

	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}
	
	public void fInitializeConfigurationData(int networkId) throws SQLException
	{
		logger.info("fInitializeConfigurationData ");
		
		List <Integer> nodelist=rpu.fgetNodesToConfigure(networkId);
		 for (int i = 0; i < nodelist.size(); i++) 
		 {
			 //initialize TPNs
			 List <CardInfo> mpns=dbService.getCardInfoService().findMpns(networkId, nodelist.get(i));
			 logger.info("InitializeConfigurationData.fInitializeConfigurationData() node id: "+ nodelist.get(i)+" Mpn list size: "+mpns.size());
			 for (int j = 0; j < mpns.size(); j++) 
			 {
				 int demandId = mpns.get(j).getDemandId();
				 Demand d = dbService.getDemandService().FindDemand(networkId, demandId);
//				 String prottype = dbService.getDemandService().FindDemand(networkId, demandId).getProtectionType();
//				 String channelProt=dbService.getDemandService().FindDemand(networkId, demandId).getChannelProtection();
//				 String channelProtType=dbService.getDemandService().FindDemand(networkId, demandId).getClientProtectionType();
				 
				 TpnPortInfo info = new TpnPortInfo();
				 info.setNetworkId(networkId);
				 info.setNodeId(nodelist.get(i));
				 info.setRack(mpns.get(j).getRack());
				 info.setSbrack(mpns.get(j).getSbrack());
				 info.setCard(mpns.get(j).getCard());
				 info.setCardType(mpns.get(j).getCardType());
				 info.setCardSubType(0);
				 info.setStream(DataConfigConstants.Upstream);	
				 info.setOperatorSpecific("-");
				 info.setTxTCMMode(1);
				 info.setTxTCMPriority("1-2-3-4-5-6-7-8");
				 info.setFecType(1);
				 info.setFecStatus(1);
				 
				 
				 List <PortInfo> portsu = dbService.getPortInfoService().FindPortInfo(networkId, nodelist.get(i), mpns.get(j).getRack(), mpns.get(j).getSbrack(), mpns.get(j).getCard());
//				 if(mpns.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpns.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {
				 if(d.getChannelProtection().equals(MapConstants.Yes)){
					 portsu = dbService.getPortInfoService().FindPortInfo(networkId, nodelist.get(i), mpns.get(j).getRack(), mpns.get(j).getSbrack(), mpns.get(j).getCard(),true);
				 }
				 System.out.println("Card Type::"+mpns.get(j).getCardType()+" PortsList::"+portsu.toString());
				 for (int k = 0; k < portsu.size(); k++) 
				 {
					 logger.info("Inserting TpnPortInfo Upstream "+info.toString());
					 info.setPortId(portsu.get(k).getPort());	
					 dbService.getTpnPortInfoService().Insert(info);	
				 }
//				 dbService.getTpnPortInfoService().Insert(info);	
				 
				 TpnPortInfo info1 = new TpnPortInfo();
				 info1.setNetworkId(networkId);
				 info1.setNodeId(nodelist.get(i));
				 info1.setRack(mpns.get(j).getRack());
				 info1.setSbrack(mpns.get(j).getSbrack());
				 info1.setCard(mpns.get(j).getCard());
				 info1.setCardType(mpns.get(j).getCardType());
				 info1.setCardSubType(0);
				 info1.setStream(DataConfigConstants.Downstream);
				 info1.setOperatorSpecific("-");
				 info1.setTxTCMMode(1);
				 info1.setTxTCMPriority("8-7-6-5-4-3-2-1");
				 info1.setFecType(1);
				 info1.setFecStatus(1);
				 List <PortInfo> portsd = dbService.getPortInfoService().FindPortInfo(networkId, nodelist.get(i), mpns.get(j).getRack(), mpns.get(j).getSbrack(), mpns.get(j).getCard());
//				 if(mpns.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpns.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {
				 if(d.getChannelProtection().equals(MapConstants.Yes)){
					 portsd = dbService.getPortInfoService().FindPortInfo(networkId, nodelist.get(i), mpns.get(j).getRack(), mpns.get(j).getSbrack(), mpns.get(j).getCard(),true);	 
				 }
				 System.out.println("Card Type::"+mpns.get(j).getCardType()+" PortsList::"+portsd.toString());
				 for (int k = 0; k < portsd.size(); k++) 
				 {
					 logger.info("Inserting TpnPortInfo Downstream "+info1.toString());					
					 info1.setPortId(portsd.get(k).getPort());	
					 dbService.getTpnPortInfoService().Insert(info1);	
				 }	
//				 dbService.getTpnPortInfoService().Insert(info1);
				 
				 ////////				 
				 PtcClientProtInfo infoM = new PtcClientProtInfo();
				 if(mpns.get(j).getStatus().equalsIgnoreCase(ResourcePlanConstants.Protection))
				 {					 
					 infoM.setNetworkId(networkId);
					 infoM.setNodeId(nodelist.get(i));
//					 infoM.setActMpnRackId(mpns.get(j).getRack());
//					 infoM.setActMpnSubrackId(mpns.get(j).getSbrack());
//					 infoM.setActMpnCardId(mpns.get(j).getCard());		
					 infoM.setProtMpnRackId(mpns.get(j).getRack());
					 infoM.setProtMpnSubrackId(mpns.get(j).getSbrack());
					 infoM.setProtMpnCardId(mpns.get(j).getCard());	
					 infoM.setProtMpnCardType(mpns.get(j).getCardType());
					 infoM.setProtMpnCardSubType(0);
					 if(d.getProtectionType().equalsIgnoreCase(MapConstants.OnePlusOnePlusRPtc))
					 {
						 infoM.setProtTopology(DataConfigConstants.ProtClient_OSNCP);
					 }
					 else{/**DBG => Temp change, need to add */
						infoM.setProtTopology(DataConfigConstants.ProtClient_OSNCP);
					 }
										 
					 infoM.setProtMechanism(ResourcePlanConstants.YCableProtection);
					 infoM.setProtStatus(DataConfigConstants.ProtEnable);
					 infoM.setProtType(DataConfigConstants.ProtNonRevertive);
					 infoM.setActiveLine(1);
					 
					 //get ports of protection mpn as they are same as working mpn
					 List <PortInfo> portsM = dbService.getPortInfoService().FindPortInfo(networkId, nodelist.get(i), mpns.get(j).getRack(), mpns.get(j).getSbrack(), mpns.get(j).getCard());
					 for (int k = 0; k < portsM.size(); k++) 
					 {
						 infoM.setActMpnPort(portsM.get(k).getPort());
						 //port mapping prot tpn is also same
						 infoM.setProtMpnPort(portsM.get(k).getPort());
						 CardInfo infoW = dbService.getCardInfoService().FindMpn(networkId, nodelist.get(i), mpns.get(j).getDemandId(),ResourcePlanConstants.Working);
						 if(infoW!=null)
						 {
							 infoM.setActMpnRackId(infoW.getRack());
							 infoM.setActMpnSubrackId(infoW.getSbrack());
							 infoM.setActMpnCardId(infoW.getCard());			
							 infoM.setActMpnCardType(infoW.getCardType());
							 infoM.setActMpnCardSubType(0);
						 }	
						 Circuit c = dbService.getCircuitService().FindCircuit(networkId, portsM.get(k).getCircuitId());						 
						 if(c.getClientProtectionType().equalsIgnoreCase(MapConstants.OLPProtection))
						 {
							 CardInfo olp = dbService.getCardInfoService().FindOLPByCircuitId(networkId, nodelist.get(i), portsM.get(k).getCircuitId());
							 infoM.setProtCardRackId(olp.getRack());
							 infoM.setProtCardSubrackId(olp.getSbrack());
							 infoM.setProtCardCardId(olp.getCard());							
							 infoM.setProtMechanism(ResourcePlanConstants.OLPProtection);//change it to olp if card exists
							 
							 //fill voa/ olp info for olps in client protection
							 VoaConfigInfo voaN = new VoaConfigInfo();
							 voaN.setNetworkId(networkId);
							 voaN.setNodeId(nodelist.get(i));
							 voaN.setRack(olp.getRack());
							 voaN.setSbrack(olp.getSbrack());
							 voaN.setCard(olp.getCard());
							 voaN.setChannelType(DataConfigConstants.OlpChannelTypeNormal);
							 voaN.setAttenuationConfigMode(DataConfigConstants.AttConfigModeAuto);
							 voaN.setAttenuation(0);
							 //concat (Direction, Wavelength and Client Port No. of Normal TPN)							 
							 String directionOlpN = ""+rpu.fgetDirectionNumber(infoW.getDirection())+dcm.format(infoW.getWavelength())+dcm.format(infoM.getActMpnPort());
							 logger.info("InitializeConfigurationData.fInitializeConfigurationData() Direction Client Prot OLP : "+directionOlpN);
							 voaN.setDirection(Integer.parseInt(directionOlpN.toString()));
							 dbService.getVoaConfigService().Insert(voaN);
							 
							 VoaConfigInfo voaP = new VoaConfigInfo();
							 voaP.setNetworkId(networkId);
							 voaP.setNodeId(nodelist.get(i));
							 voaP.setRack(olp.getRack());
							 voaP.setSbrack(olp.getSbrack());
							 voaP.setCard(olp.getCard());
							 voaP.setChannelType(DataConfigConstants.OlpChannelTypeProt);
							 voaP.setAttenuationConfigMode(DataConfigConstants.AttConfigModeAuto);
							 voaP.setAttenuation(0);
							 //concat (Direction, Wavelength and Client Port No. of Normal TPN)							 
							 String directionOlpP = ""+rpu.fgetDirectionNumber(infoW.getDirection())+dcm.format(infoW.getWavelength())+dcm.format(infoM.getActMpnPort());
							 logger.info("InitializeConfigurationData.fInitializeConfigurationData() Direction Client Prot OLP : "+directionOlpP);
							 voaP.setDirection(Integer.parseInt(directionOlpP.toString()));
							 dbService.getVoaConfigService().Insert(voaP);
						 }
						 
						 logger.info("Inserting PtcClientProtInfo "+infoM.toString());
						 dbService.getPtcClientProtInfoService().Insert(infoM);			
						 
						
					 }						 
				 }

				 // PtcLineProtInfo is to be populated for the 1:1 and 1:2R types as well and in case of channel protection with OPX card
				 if((d.getProtectionType().equalsIgnoreCase(MapConstants.OneIsToOnePtcType)|(d.getProtectionType().equalsIgnoreCase(MapConstants.OneIsToTwoRPtcTypeStr)))|(d.getChannelProtection().equalsIgnoreCase(MapConstants.Yes)))
				 {
					 PtcLineProtInfo infoL = new PtcLineProtInfo();
					 infoL.setNetworkId(networkId);
					 infoL.setNodeId(nodelist.get(i));
					 //prot card is to be filled as mpn card only in this case on Rinkal's demand
					 infoL.setProtCardRackId(mpns.get(j).getRack());
					 infoL.setProtCardSubrackId(mpns.get(j).getSbrack());
					 infoL.setProtCardCardId(mpns.get(j).getCard());
					 infoL.setProtCardCardType(mpns.get(j).getCardType());
					 infoL.setProtCardCardSubType(0);
					 infoL.setMpnRackId(mpns.get(j).getRack());
					 infoL.setMpnSubrackId(mpns.get(j).getSbrack());
					 infoL.setMpnCardId(mpns.get(j).getCard());	
					 infoL.setMpnCardType(mpns.get(j).getCardType());
					 infoL.setMpnCardSubType(0);
					 
					 infoL.setActiveLine(1);	
					 infoL.setProtMechanism(ResourcePlanConstants.OPXProtection);
					 infoL.setProtType(DataConfigConstants.ProtNonRevertive);
					 
					 if(d.getProtectionType().equalsIgnoreCase(MapConstants.OneIsToOnePtcType))
						 infoL.setProtTopology(DataConfigConstants.ProtOneIsToOne);
					 else if(d.getProtectionType().equalsIgnoreCase(MapConstants.OneIsToTwoRPtcTypeStr))
						 infoL.setProtTopology(DataConfigConstants.ProtOneIsToTwoR);
					 
					 if(d.getChannelProtection().equals(MapConstants.Yes)){
						 infoL.setProtTopology(DataConfigConstants.ProtChannel);
						 
						 if(d.getClientProtectionType().equalsIgnoreCase(ResourcePlanConstants.OLPProtection)){
							 CardInfo olp=dbService.getCardInfoService().FindCardInfo(networkId, nodelist.get(i), d.getDemandId(), ResourcePlanConstants.OLPProtection);
							 infoL.setProtCardRackId(olp.getRack());
							 infoL.setProtCardSubrackId(olp.getSbrack());
							 infoL.setProtCardCardId(olp.getCard());
							 infoL.setProtCardCardType(olp.getCardType());
							 infoL.setProtCardCardSubType(0);
						 }
					 }
						 
					 logger.info("Inserting getPtcLineProtInfoService "+infoL.toString());
					 dbService.getPtcLineProtInfoService().Insert(infoL);					 
				 }
				 
				
				 
			 }
			 
			 //get link and channel protection info
			 
			 //getting OLPs for Channel Protection
			 List <CardInfo> olps = dbService.getCardInfoService().FindOLPsWithDemandId(networkId,nodelist.get(i));
			 for (int k = 0; k < olps.size(); k++) 
			 {
//				 PtcLineProtInfo infoL = new PtcLineProtInfo();
//				 infoL.setNetworkId(networkId);
//				 infoL.setNodeId(nodelist.get(i));
//				 infoL.setProtCardRackId(olps.get(k).getRack());
//				 infoL.setProtCardSubrackId(olps.get(k).getSbrack());
//				 infoL.setProtCardCardId(olps.get(k).getCard());
				 CardInfo mpn = dbService.getCardInfoService().FindMpn(networkId, nodelist.get(i), olps.get(k).getDemandId(), ResourcePlanConstants.Working);
//				 infoL.setMpnRackId(mpn.getRack());
//				 infoL.setMpnSubrackId(mpn.getSbrack());
//				 infoL.setMpnCardId(mpn.getCard());
//				 infoL.setProtTopology(DataConfigConstants.ProtChannel);
//				 infoL.setProtMechanism(ResourcePlanConstants.OLPProtection);	
//				 logger.info("Inserting getPtcLineProtInfoService "+infoL.toString());
//				 dbService.getPtcLineProtInfoService().Insert(infoL);
				 
				 //fill voa/ olp info for olps in Channel protection
				 VoaConfigInfo voaN = new VoaConfigInfo();
				 voaN.setNetworkId(networkId);
				 voaN.setNodeId(nodelist.get(i));
				 voaN.setRack(olps.get(k).getRack());
				 voaN.setSbrack(olps.get(k).getSbrack());
				 voaN.setCard(olps.get(k).getCard());
				 voaN.setChannelType(DataConfigConstants.OlpChannelTypeNormal);
				 voaN.setAttenuationConfigMode(DataConfigConstants.AttConfigModeAuto);
				 voaN.setAttenuation(0);
				 //concat (Direction of Normal TPN, Wavelength of Normal TPN)				 
				 String directionOlpN = ""+rpu.fgetDirectionNumber(mpn.getDirection())+dcm.format(mpn.getWavelength());
				 logger.info("InitializeConfigurationData.fInitializeConfigurationData() Direction Channel Prot OLP : "+directionOlpN);
				 voaN.setDirection(Integer.parseInt(directionOlpN.toString()));
				 dbService.getVoaConfigService().Insert(voaN);
				 
				 VoaConfigInfo voaP = new VoaConfigInfo();
				 voaP.setNetworkId(networkId);
				 voaP.setNodeId(nodelist.get(i));
				 voaP.setRack(olps.get(k).getRack());
				 voaP.setSbrack(olps.get(k).getSbrack());
				 voaP.setCard(olps.get(k).getCard());
				 voaP.setChannelType(DataConfigConstants.OlpChannelTypeProt);
				 voaP.setAttenuationConfigMode(DataConfigConstants.AttConfigModeAuto);
				 voaP.setAttenuation(0);
				 //concat (Direction, Wavelength and Client Port No. of Normal TPN)							 
				 String directionOlpP = ""+rpu.fgetDirectionNumber(mpn.getDirection())+dcm.format(mpn.getWavelength());
				 logger.info("InitializeConfigurationData.fInitializeConfigurationData() Direction Channel Prot OLP : "+directionOlpP);
				 voaP.setDirection(Integer.parseInt(directionOlpP.toString()));
				 dbService.getVoaConfigService().Insert(voaP);
				 
			 }
			 //getting OLPs for Link Protection
			 List <CardInfo> olpsLink = dbService.getCardInfoService().FindOLPsForLinkProt(networkId,nodelist.get(i));
			 for (int k = 0; k < olpsLink.size(); k++) 
			 {
				 PtcLineProtInfo infoL = new PtcLineProtInfo();
				 infoL.setNetworkId(networkId);
				 infoL.setNodeId(nodelist.get(i));
				 infoL.setProtCardRackId(olpsLink.get(k).getRack());
				 infoL.setProtCardSubrackId(olpsLink.get(k).getSbrack());
				 infoL.setProtCardCardId(olpsLink.get(k).getCard());
				 infoL.setProtCardCardType(ResourcePlanConstants.CardOlp);
				 infoL.setProtCardCardSubType(0);
				 infoL.setProtTopology(DataConfigConstants.ProtLink);
				 infoL.setProtMechanism(ResourcePlanConstants.OLPProtection);		
				 logger.info("Inserting getPtcLineProtInfoService "+infoL.toString());
				 dbService.getPtcLineProtInfoService().Insert(infoL);
				 
				 //fill voa/ olp info for olps in Link protection
				 VoaConfigInfo voaN = new VoaConfigInfo();
				 voaN.setNetworkId(networkId);
				 voaN.setNodeId(nodelist.get(i));
				 voaN.setRack(olpsLink.get(k).getRack());
				 voaN.setSbrack(olpsLink.get(k).getSbrack());
				 voaN.setCard(olpsLink.get(k).getCard());
				 voaN.setChannelType(DataConfigConstants.OlpChannelTypeNormal);
				 voaN.setAttenuationConfigMode(DataConfigConstants.AttConfigModeAuto);
				 voaN.setAttenuation(0);
				 //concat (Direction)				 
				 String directionOlpN = ""+rpu.fgetDirectionNumber(olpsLink.get(k).getDirection());
				 logger.info("InitializeConfigurationData.fInitializeConfigurationData() Direction Link Prot OLP : "+directionOlpN);
				 voaN.setDirection(Integer.parseInt(directionOlpN.toString()));
				 dbService.getVoaConfigService().Insert(voaN);
				 
				 VoaConfigInfo voaP = new VoaConfigInfo();
				 voaP.setNetworkId(networkId);
				 voaP.setNodeId(nodelist.get(i));
				 voaP.setRack(olpsLink.get(k).getRack());
				 voaP.setSbrack(olpsLink.get(k).getSbrack());
				 voaP.setCard(olpsLink.get(k).getCard());
				 voaP.setChannelType(DataConfigConstants.OlpChannelTypeProt);
				 voaP.setAttenuationConfigMode(DataConfigConstants.AttConfigModeAuto);
				 voaP.setAttenuation(0);
				 //concat (Direction, Wavelength and Client Port No. of Normal TPN)							 
				 String directionOlpP = ""+rpu.fgetDirectionNumber(olpsLink.get(k).getDirection());
				 logger.info("InitializeConfigurationData.fInitializeConfigurationData() Direction Link Prot OLP : "+directionOlpP);
				 voaP.setDirection(Integer.parseInt(directionOlpP.toString()));
				 dbService.getVoaConfigService().Insert(voaP);
			 }
			 
			
			 			 
			 //initialize PABA
			 List <CardInfo> pabas = dbService.getCardInfoService().FindCardInfoByCardType(networkId, nodelist.get(i), ResourcePlanConstants.CardPaBa);
			 for (int j = 0; j < pabas.size(); j++) 
			 {
				 AmplifierConfig info = new AmplifierConfig();
				 info.setNetworkId(networkId);
				 info.setNodeId(nodelist.get(i));
				 info.setRack(pabas.get(j).getRack());
				 info.setSbrack(pabas.get(j).getSbrack());
				 info.setCard(pabas.get(j).getCard());
				 info.setAmpType(ResourcePlanConstants.AmpPaBa);
				 info.setDirection(pabas.get(j).getDirection());
				 info.setAmpStatus(DataConfigConstants.AmpEnable);
				 info.setVoaAttenuation(20);
				 info.setEdfaDirId("");
				 dbService.getAmplifierConfigService().Insert(info);				
			 }	
			 
			 //initialize RAMAN
			 List <CardInfo> ramans = dbService.getCardInfoService().FindCardInfoByCardType(networkId, nodelist.get(i), ResourcePlanConstants.CardRamanHybrid);
			 List <CardInfo> ramanDra = dbService.getCardInfoService().FindCardInfoByCardType(networkId, nodelist.get(i), ResourcePlanConstants.CardRamanDra);
			 ramans.addAll(ramanDra); // Raman Change
			 for (int j = 0; j < ramans.size(); j++) 
			 {
				 AmplifierConfig info = new AmplifierConfig();
				 info.setNetworkId(networkId);
				 info.setNodeId(nodelist.get(i));
				 info.setRack(ramans.get(j).getRack());
				 info.setSbrack(ramans.get(j).getSbrack());
				 info.setCard(ramans.get(j).getCard());
				 info.setAmpType(ramans.get(j).getCardType()); // Raman Change
				 info.setDirection(ramans.get(j).getDirection());
				 info.setAmpStatus(DataConfigConstants.AmpEnable);
				 info.setVoaAttenuation(20);
				 info.setEdfaDirId("");
				 dbService.getAmplifierConfigService().Insert(info);				
			 }		
			 
			 //initialize ILA
			 List <CardInfo> ilas = dbService.getCardInfoService().FindCardInfoByCardType(networkId, nodelist.get(i), ResourcePlanConstants.CardIla);
			 for (int j = 0; j < ilas.size(); j++) 
			 {
				 AmplifierConfig info = new AmplifierConfig();
				 info.setNetworkId(networkId);
				 info.setNodeId(nodelist.get(i));
				 info.setRack(ilas.get(j).getRack());
				 info.setSbrack(ilas.get(j).getSbrack());
				 info.setCard(ilas.get(j).getCard());
				 info.setAmpType(ResourcePlanConstants.AmpIla);
				 info.setDirection(ilas.get(j).getDirection());
				 info.setAmpStatus(DataConfigConstants.AmpEnable);
				 info.setVoaAttenuation(20);
				 info.setEdfaDirId("");
				 dbService.getAmplifierConfigService().Insert(info);				
			 }	
			 
			//initialize Edfa
			 List <CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkId, nodelist.get(i), ResourcePlanConstants.CardEdfa);
			 for (int j = 0; j < edfas.size(); j++) 
			 {
				 AmplifierConfig info = new AmplifierConfig();
				 info.setNetworkId(networkId);
				 info.setNodeId(nodelist.get(i));
				 info.setRack(edfas.get(j).getRack());
				 info.setSbrack(edfas.get(j).getSbrack());
				 info.setCard(edfas.get(j).getCard());
				 info.setAmpType(ResourcePlanConstants.AmpEdfa);
				 info.setDirection(edfas.get(j).getDirection());	
				 info.setAmpStatus(DataConfigConstants.AmpEnable);
				 info.setVoaAttenuation(20);
				 String EdfaLoc = "" + edfas.get(j).getRack()+"_"+ edfas.get(j).getSbrack()+"_"+ edfas.get(j).getCard();
				 int nodeType=dbService.getNodeService().FindNode(networkId, nodelist.get(i)).getNodeType();
				 System.out.println("NodeType AmpConfig ::"+nodeType);
				 Object edfaDirId;
				 if(nodeType==MapConstants.roadm) {					 
					 edfaDirId= dbService.getMcsMapService().GetEdfaDirId(networkId, nodelist.get(i), EdfaLoc);
					 if(edfaDirId!=null){
						 info.setEdfaDirId(edfaDirId.toString());
					 } else {
						 info.setEdfaDirId("11");
					 }
				 }else if(nodeType==MapConstants.cdRoadm) {
					 EquipmentPreference wssSetPref=dbService.getEquipmentPreferenceService().FindPreferredEq(networkId, nodelist.get(i), ResourcePlanConstants.CatAddDropWssCdRoadm);
					 
					 if(wssSetPref.getCardType().equals(ResourcePlanConstants.AddDropSingleCardSet)) {
						 System.out.println(wssSetPref.getCardType());
						 edfaDirId= dbService.getMcsMapService().GetEdfaDirId(networkId, nodelist.get(i), EdfaLoc);
						 if(edfaDirId!=null){
							 System.out.println(edfaDirId.toString());
							 info.setEdfaDirId(edfaDirId.toString());
						 } else {
							 info.setEdfaDirId("11");
						 }
					 }else if(wssSetPref.getCardType().equals(ResourcePlanConstants.AddDropTwoCardSet)) {
						 System.out.println(wssSetPref.getCardType());
						 edfaDirId= dbService.getWssMapService().GetEdfaDirId(networkId, nodelist.get(i), EdfaLoc);
						 if(edfaDirId!=null){
							 info.setEdfaDirId(edfaDirId.toString());
						 } else {
							 info.setEdfaDirId("11");
						 }
					 }
				 }
				 
//				 if(edfaDirId!=null){
//					 info.setEdfaDirId(edfaDirId.toString());
//				 } else {
//					 info.setEdfaDirId("");
//				 }				 				 
				 
				 dbService.getAmplifierConfigService().Insert(info);				
			 }
			 
			//initialize Ocm
			 List <CardInfo> ocms = dbService.getCardInfoService().FindOcm(networkId, nodelist.get(i));
			 for (int j = 0; j < ocms.size(); j++) 
			 {
				 OcmConfig info = new OcmConfig();
				 info.setNetworkId(networkId);
				 info.setNodeId(nodelist.get(i));
				 info.setRack(ocms.get(j).getRack());
				 info.setSbrack(ocms.get(j).getSbrack());
				 info.setCard(ocms.get(j).getCard());				 
				 info.setCardType(ocms.get(j).getCardType());
				 info.setCardSubType(0);
				 int nodetype = dbService.getNodeService().FindNode(networkId, nodelist.get(i)).getNodeType();
				 if(nodetype==MapConstants.roadm || nodetype==MapConstants.cdRoadm || nodetype==MapConstants.dRoadm)
				 {		
					 if(ocms.get(j).getDirection().equals(MapConstants.EAST))
						 info.setOcmId(1);
					 else if(ocms.get(j).getDirection().equals(MapConstants.NE))
						 info.setOcmId(2);
				 }
				 else if(nodetype==MapConstants.ila)
				 {
					 info.setOcmId(3);
				 }
				 else if(nodetype==MapConstants.twoBselectRoadm || nodetype==MapConstants.hub)
				 {
					 info.setOcmId(3);
				 }
//				 info.setOcmId(j+1);
				 dbService.getOcmConfigService().Insert(info);				
			 }
			 
			//initialize Wss
			 List <CardInfo> wsses = dbService.getCardInfoService().FindWss(networkId, nodelist.get(i));
			 for (int j = 0; j < wsses.size(); j++) 
			 {
				 WssDirectionConfig info = new WssDirectionConfig();
				 info.setNetworkId(networkId);
				 info.setNodeId(nodelist.get(i));
				 info.setRack(wsses.get(j).getRack());
				 info.setSbrack(wsses.get(j).getSbrack());
				 info.setCard(wsses.get(j).getCard());				 
				 info.setCardType(wsses.get(j).getCardType());
				 info.setCardSubType(0);
				 info.setWssDirection(wsses.get(j).getDirection());				 
				 dbService.getWssDirectionConfigService().Insert(info);				
			 }
			 
			//initialize Mcs
//			 List <CardInfo> mcs = dbService.getCardInfoService().FindCardInfoByCardType(networkId, nodelist.get(i), ResourcePlanConstants.CardMcs);
			 List <McsMap> mcsmap = dbService.getMcsMapService().Find(networkId, nodelist.get(i));
			 for (int j = 0; j < mcsmap.size(); j++) 
			 {
				 
				 McsPortMapping info = new McsPortMapping();
				 info.setNetworkId(networkId);
				 info.setNodeId(nodelist.get(i));
				 info.setRack(mcsmap.get(j).getRack());
				 info.setSbrack(mcsmap.get(j).getSbrack());
				 info.setCard(mcsmap.get(j).getCard());
				 info.setCardType(ResourcePlanConstants.CardMcs);	
				 info.setCardSubType(0);
				 info.setMcsId(mcsmap.get(j).getMcsId());
				 info.setMcsAddPortInfo(mcsmap.get(j).getMcsCommonPort());
				 info.setMcsDropPortInfo(mcsmap.get(j).getMcsCommonPort());
				 info.setAddTpnLinePortNum(mcsmap.get(j).getTpnLinePortNo());
				 int loc[] = rpu.locationIds(mcsmap.get(j).getTpnLoc());
				 info.setAddTpnRackId(loc[0]);
				 info.setAddTpnSubRackId(loc[1]);
				 info.setAddTpnSlotId(loc[2]);				 
				 info.setDropTpnRackId(loc[0]);
				 info.setDropTpnSubRackId(loc[1]);
				 info.setDropTpnSlotId(loc[2]);
				 info.setDropTpnLinePortNum(mcsmap.get(j).getTpnLinePortNo());
				 info.setMcsSwitchPort(mcsmap.get(j).getMcsSwitchPort());
				 logger.info("InitializeConfigurationData.fInitializeConfigurationData() "+info.toString());
				 System.out.println("InitializeConfigurationData.fInitializeConfigurationData() "+info.toString());
				 dbService.getMcsPortMappingService().Insert(info);		
				 				 
			 }			 		 
		 }
		 
		 
		
		 
		 
		
	}
	
	public void fDeleteConfigurationData(int networkid)
	{
		logger.info("fDeleteConfigurationData ");
		dbService.getTpnPortInfoService().DeleteByNetworkId(networkid);
		dbService.getAmplifierConfigService().DeleteByNetworkId(networkid);
		dbService.getMcsPortMappingService().DeleteByNetworkId(networkid);
		dbService.getPtcClientProtInfoService().DeleteByNetworkId(networkid);
		dbService.getPtcLineProtInfoService().DeleteByNetworkId(networkid);
		dbService.getOcmConfigService().DeleteByNetworkId(networkid);
		dbService.getWssDirectionConfigService().DeleteByNetworkId(networkid);
		dbService.getMcsMapService().DeleteByNetworkId(networkid);
		dbService.getMuxDemuxPortInfoService().DeleteByNetworkId(networkid);
		dbService.getWssMapService().DeleteByNetworkId(networkid);
		dbService.getVoaConfigService().DeleteByNetworkId(networkid);
	}
	
}
