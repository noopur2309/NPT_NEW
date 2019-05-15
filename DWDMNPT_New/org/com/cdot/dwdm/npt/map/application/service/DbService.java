package application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbService {	
		
	@Autowired
	private NetworkService networkService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private LinkService linkService;
	@Autowired
	private TopologyService topologyService;
	@Autowired
	private DemandService demandService;
	@Autowired
	private NetworkRouteService networkRouteService;
	@Autowired
	private CircuitService circuitService;
	@Autowired
	private IpSchemeNodeService ipSchemeNodeService;
	@Autowired
	private IpSchemeLinkService ipSchemeLinkService;
	@Autowired
	private ViewService viewService;
	@Autowired
	private CircuitDemandMappingService circuitDemandMappingService;
	@Autowired
	private CardInfoService cardInfoService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ViewServiceRp viewServiceRp;
	
	@Autowired
	private CardPreferenceService cardPreferenceService;
	@Autowired
	private PatchCordService patchCordService;
	@Autowired
	private LinkWavelengthService linkWavelengthService;	
	@Autowired
	private PortInfoService portInfoService;
	@Autowired
	private MuxDemuxPortInfoService muxDemuxPortInfoService;
	
	@Autowired
	private TpnPortInfoService tpnPortInfoService;
	
	@Autowired
	private McsPortMappingService mcsPortMappingService;
	
	@Autowired
	private WssDirectionConfigService wssDirectionConfigService;
	
	@Autowired
	private PtcClientProtInfoService ptcClientProtInfoService;
	
	@Autowired
	private PtcLineProtInfoService ptcLineProtInfoService;
	@Autowired
	private OcmConfigService ocmConfigService;
	@Autowired
	private LinkWavelengthInfoService linkWavelengthInfoService;
	
	@Autowired
	private AmplifierConfigService   amplifierConfigService;
	
	@Autowired
	private McsMapService mcsMapService;
	
	@Autowired
	private OpticalPowerCdcRoadmAddService cdcRoadmAddService;
	
	@Autowired
	private OpticalPowerCdcRoadmDropService cdcRoadmDropService;
	
	@Autowired
	private MapDataService mapDataService;
	
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private VoaConfigService voaConfigService;
	
	@Autowired
	private YCablePortInfoService ycablePortInfoService;
	
	@Autowired
	private ParametricPreferenceService parametricPreferenceService;
	
	@Autowired
	private EquipmentPreferenceService equipmentPreferenceService;
	
	@Autowired
	private AllocationExceptionsService allocationExceptionsService;
	
	@Autowired
	private StockService stockService;
	
	@Autowired
	private WssMapService wssMapService;

	@Autowired
	private LambdaLspInformationService lambdaLspInformationService;
	
	@Autowired
	private OtnLspInformationService otnLspInformationService;
	
	@Autowired
	private Circuit10gAggService circuit10gAggService;
	
	@Autowired
	private CircuitpotpmappingService CircuitpotpmappingService;
	
	
	public NetworkService getNetworkService() {
		return networkService;
	}
	public StockService getStockService() {
		return stockService;
	}
	public void setNetworkService(NetworkService networkService) {
		this.networkService = networkService;
	}

	public NodeService getNodeService() {
		return nodeService;
	}
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
	public LinkService getLinkService() {
		return linkService;
	}
	public void setLinkService(LinkService linkService) {
		this.linkService = linkService;
	}
	public TopologyService getTopologyService() {
		return topologyService;
	}
	public void setTopologyService(TopologyService topologyService) {
		this.topologyService = topologyService;
	}
	public DemandService getDemandService() {
		return demandService;
	}
	public void setDemandService(DemandService demandService) {
		this.demandService = demandService;
	}
	public NetworkRouteService getNetworkRouteService() {
		return networkRouteService;
	}
	public void setNetworkRouteService(NetworkRouteService networkRouteService) {
		this.networkRouteService = networkRouteService;
	}
	public CircuitService getCircuitService() {
		return circuitService;
	}
	public void setCircuitService(CircuitService circuitService) {
		this.circuitService = circuitService;
	}
	public IpSchemeNodeService getIpSchemeNodeService() {
		return ipSchemeNodeService;
	}
	public void setIpSchemeNodeService(IpSchemeNodeService ipSchemeNodeService) {
		this.ipSchemeNodeService = ipSchemeNodeService;
	}
	public IpSchemeLinkService getIpSchemeLinkService() {
		return ipSchemeLinkService;
	}
	public void setIpSchemeLinkService(IpSchemeLinkService ipSchemeLinkService) {
		this.ipSchemeLinkService = ipSchemeLinkService;
	}
	public ViewService getViewService() {
		return viewService;
	}
	public void setViewService(ViewService viewService) {
		this.viewService = viewService;
	}
	public CircuitDemandMappingService getCircuitDemandMappingService() {
		return circuitDemandMappingService;
	}
	public void setCircuitDemandMappingService(CircuitDemandMappingService circuitDemandMappingService) {
		this.circuitDemandMappingService = circuitDemandMappingService;
	}
	public CardInfoService getCardInfoService() {
		return cardInfoService;
	}
	public CircuitpotpmappingService getCircuitPOTPMappingService() {
		return CircuitpotpmappingService;
	}
	
	public void setCardInfoService(CardInfoService cardInfoService) {
		this.cardInfoService = cardInfoService;
	}
	public EquipmentService getEquipmentService() {
		return equipmentService;
	}
	
	public void setEquipmentService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}
	public InventoryService getInventoryService() {
		return inventoryService;
	}
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	public ViewServiceRp getViewServiceRp() {
		return viewServiceRp;
	}
	public void setViewServiceRp(ViewServiceRp viewServiceRp) {
		this.viewServiceRp = viewServiceRp;
	}
	public CardPreferenceService getCardPreferenceService() {
		return cardPreferenceService;
	}
	public void setCardPreferenceService(CardPreferenceService cardPreferenceService) {
		this.cardPreferenceService = cardPreferenceService;
	}
	public PatchCordService getPatchCordService() {
		return patchCordService;
	}
	public void setPatchCordService(PatchCordService patchCordService) {
		this.patchCordService = patchCordService;
	}
	public LinkWavelengthService getLinkWavelengthService() {
		return linkWavelengthService;
	}
	public void setLinkWavelengthService(LinkWavelengthService linkWavelengthService) {
		this.linkWavelengthService = linkWavelengthService;
	}
	public PortInfoService getPortInfoService() {
		return portInfoService;
	}
	public MuxDemuxPortInfoService getMuxDemuxPortInfoService() {
		return muxDemuxPortInfoService;
	}
	public void setPortInfoService(PortInfoService portInfoService) {
		this.portInfoService = portInfoService;
	}
	public TpnPortInfoService getTpnPortInfoService() {
		return tpnPortInfoService;
	}
	public void setTpnPortInfoService(TpnPortInfoService tpnPortInfoService) {
		this.tpnPortInfoService = tpnPortInfoService;
	}
	public McsPortMappingService getMcsPortMappingService() {
		return mcsPortMappingService;
	}
	public void setMcsPortMappingService(McsPortMappingService mcsPortMappingService) {
		this.mcsPortMappingService = mcsPortMappingService;
	}
	public WssDirectionConfigService getWssDirectionConfigService() {
		return wssDirectionConfigService;
	}
	public void setWssDirectionConfigService(WssDirectionConfigService wssDirectionConfigService) {
		this.wssDirectionConfigService = wssDirectionConfigService;
	}
	public PtcClientProtInfoService getPtcClientProtInfoService() {
		return ptcClientProtInfoService;
	}
	public void setPtcClientProtInfoService(PtcClientProtInfoService ptcClientProtInfoService) {
		this.ptcClientProtInfoService = ptcClientProtInfoService;
	}
	public PtcLineProtInfoService getPtcLineProtInfoService() {
		return ptcLineProtInfoService;
	}
	public void setPtcLineProtInfoService(PtcLineProtInfoService ptcLineProtInfoService) {
		this.ptcLineProtInfoService = ptcLineProtInfoService;
	}
	public OcmConfigService getOcmConfigService() {
		return ocmConfigService;
	}
	public void setOcmConfigService(OcmConfigService ocmConfigService) {
		this.ocmConfigService = ocmConfigService;
	}
	public LinkWavelengthInfoService getLinkWavelengthInfoService() {
		return linkWavelengthInfoService;
	}
	public void setLinkWavelengthInfoService(LinkWavelengthInfoService linkWavelengthInfoService) {
		this.linkWavelengthInfoService = linkWavelengthInfoService;
	}
	public AmplifierConfigService getAmplifierConfigService() {
		return amplifierConfigService;
	}
	public void setAmplifierConfigService(AmplifierConfigService amplifierConfigService) {
		this.amplifierConfigService = amplifierConfigService;
	}
	public McsMapService getMcsMapService() {
		return mcsMapService;
	}
	public void setMcsMapService(McsMapService mcsMapService) {
		this.mcsMapService = mcsMapService;
	}
	public OpticalPowerCdcRoadmAddService getCdcRoadmAddService() {
		return cdcRoadmAddService;
	}
	public void setCdcRoadmAddService(OpticalPowerCdcRoadmAddService cdcRoadmAddService) {
		this.cdcRoadmAddService = cdcRoadmAddService;
	}
	public OpticalPowerCdcRoadmDropService getCdcRoadmDropService() {
		return cdcRoadmDropService;
	}
	public void setCdcRoadmDropService(OpticalPowerCdcRoadmDropService cdcRoadmDropService) {
		this.cdcRoadmDropService = cdcRoadmDropService;
	}
	public MapDataService getMapDataService() {
		return mapDataService;
	}
	public void setMapDataService(MapDataService mapDataService) {
		this.mapDataService = mapDataService;
	}
	public UserListService getUserListService() {
		return userListService;
	}
	public void setUserListService(UserListService userListService) {
		this.userListService = userListService;
	}
	public VoaConfigService getVoaConfigService() {
		return voaConfigService;
	}
	public void setVoaConfigService(VoaConfigService voaConfigService) {
		this.voaConfigService = voaConfigService;
	}
	public YCablePortInfoService getYcablePortInfoService() {
		return ycablePortInfoService;
	}	
	public ParametricPreferenceService getParametricPreferenceService() {
		return parametricPreferenceService;
	}
	public EquipmentPreferenceService getEquipmentPreferenceService() {
		return equipmentPreferenceService;
	}
	public AllocationExceptionsService getAllocationExceptionsService() {
		return allocationExceptionsService;
	}
	public WssMapService getWssMapService() {
		return wssMapService;
	}
	public LambdaLspInformationService getLambdaLspInformationSerivce() {
		return lambdaLspInformationService;
	}
	public OtnLspInformationService getOtnLspInformationSerivce() {
		return otnLspInformationService;
	}
	public Circuit10gAggService getCircuit10gAggService() {
		return circuit10gAggService;
	}
				
}
