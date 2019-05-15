"use strict";

// NPT Constants Module
let nptGlobals = (function() {
  const _LOADING_IMAGE_URL = "images/loading.gif",
    _NETWORK_MIN_NODES = 0,
    _NETWORK_MAX_NODES = 64,
    _ILA_MIN_SPANLENGTH = 10,
    _ILA_MAX_SPANLENGTH = 120,
    _RAMANHYBRID_MAX_SPANLOSS = 37,
    _RAMANHYBRID_MIN_SPANLOSS = 19,
    _RAMANDRA_MAX_SPANLOSS = 13,
    _RAMANDRA_MIN_SPANLOSS = 13,
    _ILA_MAX_SPANLOSS = 35,
    _ILA_MIN_SPANLOSS = 20,
    _RAMAN_HYBRID_STR = "RAMAN PA/BA (HYBRID)",
    _RAMAN_DRA_STR = "RAMAN PA/BA (DRA)",
    _DEFAULT_PA_BA_STR = "DEFAULT (PA/BA)";

  /* ******** global Variables for NPT state *************** */
  var isCanvasActive = true,
    isFirstTimeOpticalCalculations = true,
    isMapSaved = false,
    isCircuitSaved = false,
    isRwaExecuted = false,
    isIpSchemeGenerated = false,
    isInventoryGenerated = true,
    isChassisViewClickedFirstTime = true,
    isConfigFileGenerated = true,
    isDataPathConfigFileGenerated = true;

  const dialogType = {
    info: "info",
    warning: "warning",
    alert: "alert",
    success: "success",
    neutral: "neutral"
  };

  return {
    /* ******** Map State Variables *************** */
    nodeCount: 0,
    linkCount: 0,
    nextNodeId: 1,
    nextLinkId: 1,
    nextAvailableId: 1,
    elSelected: undefined, // Not assigned to any map element initially
    contextElSelected: undefined, // Not assigned to any map el initially
    reversedLink: false,
    nodeAvailableArr: [],
    linkAvailableArr: [],
    NetworkTopology: "Mesh",
    NetworkName: "",
    DemandMatrixViewData: [],
    nodeIdForReplacingNode: 0,
    isGneFlag: 0,
    isPrimaryGneSet: false,
    primaryGneStr: "Primary GNE",
    secondaryGneStr: "Secondary GNE",
    isSecondaryGneSet: false,
    opticalPowerPathNodesArr: [],
    contextMenuAddNodePos: {},
    cellPointerDownPosition: {},

    /* ******** Max Node Degrees *************** */
    CDC_MAX_DEGREE: 8,
    CDR_MAX_DEGREE: 8,
    ILA_MAX_DEGREE: 2,
    TE_MAX_DEGREE: 1,
    BS_MAX_DEGREE: 2,

    /* ******** Common Strings *************** */
    YesStr: "Yes",
    NoStr: "No",
    NoneStr: "None",
    GreenFieldStr: "GreenField",
    BrownFieldStr: "BrownField",

    /* ******** Server Response *************** */
    Success: 1,
    Failure: 0,

    /* ******** Fault Analysis Link/Path Color *************** */
    workingPathColor: "rgb(10,117,60)",
    protectionPathColor: "#0649cc",
    restorationPathColor: "#c17d11",
    restorationPath2Color: "#333",
    rejectedPathErrorColor: "#babdb6",

    /*********** Brown Field Label Classes************* */
    NewRowClassStr: "NewRowClass",
    OldRowClassStr: "OldRowClass",
    ModifiedRowClassStr: "ModifiedRowClass",
    DeletedRowClassStr: "DeletedRowClass",
    GreenFieldRowClassStr: "GreenFieldRowClass",

    /*********** Circuit Labels************* */
    ClientRateLabelStr: "Client Rate",
    ServiceTypeLabelStr: "Service Type",
    ProtectionLabelStr: "Protection Type",
    VendorLabelStr: "Vendor Label",
    SrcNodeLabelStr: "Source Node",
    DestNodeLabelStr: "Dest Node",
    PtcMechLabelStr: "Protection Mechanism",
    LineRateLabelStr: "Line Rate",
    MultiplierLabelStr: "Multiplier",
    ClientPtcLabelStr: "Client Protection",
    ChannelPtcLabelStr: "Channel Protection",
    NodeIncLabelStr: "Node Inclusion",
    LambdaBlockingLabelStr: "Block Lambda",
    ColorPrefLabelStr: "Color Preference",
    DeleteLabelStr: "Delete",
    PathTypeLabelStr: "Path Type",

    /* ******** Network Topology *************** */
    TopologyLinearStr: "Linear",
    TopologyTwoFiberRingStr: "Close Ring",
    TopologyHubRingStr: "Hub Ring",
    TopologyMeshStr: "Mesh",

    /* ******** Fault Analysis *************** */
    isFaultAnalysisReady: false,
    faultElementsCount: 0,
    faultNodesArr: [],
    faultLinksArr: [],

    /* ******** RWA error states *************** */
    NoPathRwaErrorState: 0,
    LessPathRwaErrorState: -1,
    RejectedPathRwaErrorState: 5,

    /*********** Directions NPT ******************/
    AllDirections: "all",
    EastDirection: "east",
    WestDirection: "west",
    NorthDirection: "north",
    SouthDirection: "south",
    NorthEastDirection: "ne",
    NorthWestDirection: "nw",
    SouthEastDirection: "se",
    SouthWestDirection: "sw",
    _EAST: 1,
    _WEST: 2,
    _NORTH: 3,
    _SOUTH: 4,
    _NE: 5,
    _NW: 6,
    _SE: 7,
    _SW: 8,

    /* ******** Network Node types *************** */
    NodeTypeILA: "devs.Ila",
    NodeTypeROADM: "devs.roadm",
    NodeTypeCDROADM: "devs.cdroadm",
    NodeTypeTE: "devs.TE",
    NodeTypeTwoDegreeRoadm: "devs.BSRoadm",
    NodeTypeHub: "devs.hub",
    NodeTypePotp: "devs.potp",

    /* ******** Node type display name *************** */
    NodeTypeILADisplayName: "ILA",
    NodeTypeROADMDisplayName: "CDC Roadm",
    NodeTypeCDROADMDisplayName: "CD Roadm",
    NodeTypeTEDisplayName: "TE",
    NodeTypeTwoDegreeRoadmDisplayName: "B & Select Roadm",
    NodeTypeHubDisplayName: "Hub",
    NodeTypePotpDisplayName: "POTP",

    /* ******** Node type : values (Int) *************** */
    numNodeTypeILA: 2,
    numNodeTypeROADM: 8,
    numNodeTypeCDROADM: 9,
    numNodeTypeTE: 1,
    numNodeTypeTwoDegreeRoadm: 6,
    numNodeTypeHub: 7,
    numNodeTypePotp: 5,

    /* ******** Service Types *************** */
    ServiceTypeEthernet: "Ethernet",
    ServiceTypeEthernet100G: "Ethernet 100G",
    ServiceTypeOTU0: "OTU0",
    ServiceTypeOTU1: "OTU1",
    ServiceTypeOTU2: "OTU2",
    ServiceTypeOTU2_LOWER: "OTU2-LOWER",
    ServiceTypeOTU4: "OTU4",
    ServiceTypeFC_100: "FC-100",
    ServiceTypeFC_200: "FC-200",
    ServiceTypeFC_1200: "FC-1200",
    ServiceTypeSTM_16: "STM-16",
    ServiceTypeSTM_64: "STM-64",

    /* ******** Ptc Types *************** */
    PtcTypeUnprotected: "UnProtected",
    PtcTypeOneIsToOne: "1:1",
    PtcTypeOnePlusOnePlusR: "1+1+R",
    PtcTypeOnePlusOnePlusTwoR: "1+1+2R",
    PtcTypeOneIsToTwoR: "1:2R",
    PtcTypeOnePlusOne: "1+1",
    PtcTypeLowPriority: "Low Priority",

    /* ******** Color Types *************** */
    Violet: "Violet",
    Indigo: "Indigo",
    Blue: "Blue",
    Green: "Green",
    Yellow: "Yellow",
    Orange: "Orange",
    Red: "Red",

    /* ******** Ptc Mech Types *************** */
    PtcMechOPX: "OPX",
    PtcMechOLP: "OLP",
    PtcMechYCable: "Y-Cable",

    /* ******** Fiber Types *************** */
    FibreTypeG652D: "G.652.D",
    FibreTypeG655D: "G.655",

    /* ******** Path Type *************** */
    DisjointPathStr: "Disjoint",
    Non_DisjointPathStr: "Non-Disjoint",

    /* ******** Line Rates  *************** */
    LineRate_10: "10",
    LineRate_100: "100",
    LineRate_200: "200",

    /* ******** Client Rates *************** */
    ClientRate_1: "1",
    ClientRate_1_25: "1.25",
    ClientRate_2_5: "2.5",
    ClientRate_10: "10",
    ClientRate_10_Agg: "10(Agg)",
    ClientRate_100: "100",
    
    /********** Tributory Ids ****************/
    TributoryId_Zero: "Select Id",
    TributoryId_One : "1",
    TributoryId_Two : "2",
    TributoryId_Three : "3",
    TributoryId_Four : "4",
    TributoryId_Five : "5",
    TributoryId_Six  : "6",
    TributoryId_Seven : "7",
    TributoryId_Eight : "8",
    

    /* ******** Card Configuration Constants *************** */
    TcmPriorityOperational: "OPERATIONAL",
    TcmPriorityTransparent: "TRANSPARENT",
    TcmPriorityMonitor: "MONITOR",
    TcmPriorityNonIntrusiveMonitor: "NON-INTRUSIVE MONITOR",

    /* ******** Card Types NPT *************** */
    CardTypeMpn: "MPN",
    CardTypeWss: "WSS",
    CardTypeMcs: "MCS",
    CardTypeEdary: "EDARY",
    CardTypePaBa: "PA/BA",
    CardTypeocm: "OCM",

    /* ******** Node Capacity *************** */
    NodeCapacityEven40Str: "Even 40 Channel",
    NodeCapacityOdd40Str: "Odd 40 Channel",
    NodeCapacity80Str: "80 Channel",
    NodeCapacityEven40: 1,
    NodeCapacityOdd40: 2,
    NodeCapacity80: 3,

    /*** Link Selected for ILA insertion ***/
    IlaInsertion_Link: null,
    IlaInsertion_SpanLoss: 0,
    IlaInsertion_SpanLength: 0,
    IlaInsertion_MinIla: 1,

    LinkNodeConfigModalId: undefined,

    /*********************************************
     * Constants -- Get
     *********************************************/
    getLoadingImageUrl: function() {
      return _LOADING_IMAGE_URL;
    },
    getNetworkMinNodes: function() {
      return _NETWORK_MIN_NODES;
    },
    getNetworkMaxNodes: function() {
      return _NETWORK_MAX_NODES;
    },
    getMinIlaSpanLength: function() {
      return _ILA_MIN_SPANLENGTH;
    },
    getMaxIlaSpanLength: function() {
      return _ILA_MAX_SPANLENGTH;
    },
    // Raman h MAx Spanloss
    getRamanHybridMaxSpanloss: function() {
      return _RAMANHYBRID_MAX_SPANLOSS;
    },
    // Dra MAx Spanloss
    getRamanDraMaxSpanloss: function() {
      return _RAMANDRA_MAX_SPANLOSS;
    },
    // ILA MAx Spanloss
    getDefaultLinkMaxSpanLoss: function() {
      return _ILA_MAX_SPANLOSS;
    },
    // Raman h Min Spanloss
    getRamanHybridMinSpanloss: function() {
      return _RAMANHYBRID_MIN_SPANLOSS;
    },
    // Dra Min Spanloss
    getRamanDraMinSpanloss: function() {
      return _RAMANDRA_MIN_SPANLOSS;
    },
    // ILA Min Spanloss
    getDefaultLinkMinSpanLoss: function() {
      return _ILA_MIN_SPANLOSS;
    },
    // Default Link Str
    getDefaultLinkStr: function() {
      return _DEFAULT_PA_BA_STR;
    },
    // Raman Hybrid Str
    getRamanHybridLinkStr: function() {
      return _RAMAN_HYBRID_STR;
    },
    // Raman Dra Str
    getRamanDraLinkStr: function() {
      return _RAMAN_DRA_STR;
    },
    getMaxSpanLoss: function(linkType) {
      switch (linkType) {
        case _RAMAN_HYBRID_STR:
          return _RAMANHYBRID_MAX_SPANLOSS;
        case _RAMAN_DRA_STR:
          return _RAMANDRA_MAX_SPANLOSS;
        case _DEFAULT_PA_BA_STR:
          return _ILA_MAX_SPANLOSS;
        default:
          break;
      }
    },
    /*********************************************
     * Map Saved State
     *********************************************/
    setMapSaved: function(value) {
      isMapSaved = value;
    },
    getMapSavedStatus: function() {
      return isMapSaved;
    },

    /*********************************************
     * Circuit Saved
     *********************************************/
    setCircuitSaved: function(value) {
      isCircuitSaved = value;
    },
    getCircuitSavedStatus: function() {
      return isCircuitSaved;
    },

    /*********************************************
     * RWA Executed
     *********************************************/
    setRwaExecuted: function(value) {
      isRwaExecuted = value;
    },
    getRwaExecutedStatus: function() {
      return isRwaExecuted;
    },

    /*********************************************
     * Inventory Generated
     *********************************************/
    setInventoryGenerated: function(value) {
      isInventoryGenerated = value;
    },
    getInventoryGeneratedStatus: function() {
      return isInventoryGenerated;
    },
    
    /*********************************************
     * Chassis View First click
     *********************************************/
    setChassisViewClickedFirstTime: function(value) {
    	isChassisViewClickedFirstTime = value;
    },
    getChassisViewClickedFirstTime: function() {
      return isChassisViewClickedFirstTime;
    },
    
    /*********************************************
     * Canvas Active/Not Active
     *********************************************/
    setCanvasActive: function(value) {
      isCanvasActive = value;
    },
    getCanvasActive: function() {
      console.log(isCanvasActive);
      return isCanvasActive;
    },

    /*********************************************
     * IP Scheme generated
     *********************************************/
    setIpSchemeGenerated: function(value) {
      isIpSchemeGenerated = value;
    },
    getIpSchemeGeneratedStatus: function() {
      console.log(isIpSchemeGenerated);
      return isIpSchemeGenerated;
    },

    /*********************************************
     * Dialog Types
     *********************************************/
    getDialogTypes: function() {
      return dialogType;
    },
    /*********************************************
     * Original link for ILA insertion - parameters
     *********************************************/
    setIlaInsertionOriginalLinkParameters: function(
      link,
      spanLoss,
      spanLength,
      minIla
    ) {
      this.IlaInsertion_Link = link;
      this.IlaInsertion_MinIla = minIla;
      this.IlaInsertion_SpanLength = spanLength;
      this.IlaInsertion_SpanLoss = spanLoss;
    },
    /************************************************************************
     *  Function mapping of Direction value to Direction string
     ************************************************************************/
    fGetDirectionStr: function(val) {
      // console.log("fGetDirectionStr input:" + val);
      var dirStr = "";
      switch (val) {
        case this._EAST:
          dirStr = this.EastDirection;
          break;
        case this._WEST:
          dirStr = this.WestDirection;
          break;
        case this._NORTH:
          dirStr = this.NorthDirection;
          break;
        case this._SOUTH:
          dirStr = this.SouthDirection;
          break;
        case this._NE:
          dirStr = this.NorthEastDirection;
          break;
        case this._NW:
          dirStr = this.NorthWestDirection;
          break;
        case this._SE:
          dirStr = this.SouthEastDirection;
          break;
        case this._SW:
          dirStr = this.SouthWestDirection;
          break;
        default:
          dirStr = "Unkown Value for direction";
          break;
      }
      // console.log("fGetDirectionStr output:" + dirStr);
      return dirStr;
    },
    /*********************************************
     * Node Capacity map, Input : Capacity Value , Return: Capacity String
     *********************************************/
    getCapacityString: function(capVal) {
      let capStr;
      switch (capVal) {
        case this.NodeCapacityEven40:
          capStr = this.NodeCapacityEven40Str;
          break;
        case this.NodeCapacityOdd40:
          capStr = this.NodeCapacityOdd40Str;
          break;
        case this.NodeCapacity80:
          capStr = this.NodeCapacity80Str;
          break;
        default:
          capStr = this.NodeCapacityEven40Str;
          break;
      }
      return capStr;
    },
    /*********************************************
     * Service Type for Client and Line rate,
     * Input : Client Rate , LineRate, Qostag | Return: ServiceType Source
     *********************************************/
    fGetServiceTypeSrc: function(clientRate, lineRate) {
      let ServiceTypeSource;
      if (clientRate == this.ClientRate_1 && lineRate == this.LineRate_10)
        ServiceTypeSource = [
          /*{ value: 0, text: 'None' },*/ {
            value: this.ServiceTypeFC_100,
            text: this.ServiceTypeFC_100
          }
        ];
      else if (
        clientRate == this.ClientRate_1_25 &&
        lineRate == this.LineRate_10
      )
        ServiceTypeSource = [
          /*{ value: 0, text: 'None' },*/ {
            value: this.ServiceTypeEthernet,
            text: this.ServiceTypeEthernet
          }
        ];
      else if (
        clientRate == this.ClientRate_2_5 &&
        lineRate == this.LineRate_10
      )
        ServiceTypeSource = [
          /*{ value: 0, text: 'None' },*/ {
            value: this.ServiceTypeSTM_16,
            text: this.ServiceTypeSTM_16
          },
          { value: this.ServiceTypeOTU1, text: this.ServiceTypeOTU1 },
          { value: this.ServiceTypeFC_200, text: this.ServiceTypeFC_200 }
        ];
      else if (clientRate == this.ClientRate_10 && lineRate == this.LineRate_10)
        ServiceTypeSource = [
          /*{ value: 0, text: 'None' },*/ {
            value: this.ServiceTypeEthernet,
            text: this.ServiceTypeEthernet
          },
          { value: this.ServiceTypeOTU2, text: this.ServiceTypeOTU2 },
          { value: this.ServiceTypeFC_1200, text: this.ServiceTypeFC_1200 },
          { value: this.ServiceTypeSTM_64, text: this.ServiceTypeSTM_64 }
        ];
      else if (
        clientRate == this.ClientRate_10 &&
        lineRate == this.LineRate_100
      )
        ServiceTypeSource = [
          /*{ value: 0, text: 'None' },*/ {
            value: this.ServiceTypeEthernet,
            text: this.ServiceTypeEthernet
          },
          { value: this.ServiceTypeOTU2, text: this.ServiceTypeOTU2 },
          { value: this.ServiceTypeFC_1200, text: this.ServiceTypeFC_1200 },
          { value: this.ServiceTypeSTM_64, text: this.ServiceTypeSTM_64 }
        ];
      else
        ServiceTypeSource = [
          /*{ value: 0, text: 'None' },*/ {
            value: this.ServiceTypeOTU4,
            text: this.ServiceTypeOTU4
          },
          {
            value: this.ServiceTypeEthernet100G,
            text: this.ServiceTypeEthernet100G
          }
        ];
      console.log(
        "Service type for ClientRate :",
        clientRate,
        " and LineRate :",
        lineRate,
        " is ::",
        ServiceTypeSource
      );
      return ServiceTypeSource;
    },
    /**
	Get Service Type for POTP client
	**/
    
    fServiceTypeOTU2Lower: function(Qos) { 
     var ServiceTypeSrc;
     
    	if(Qos == 1 )
    	{  
    	   ServiceTypeSrc =  [ {
              value: this.ServiceTypeEthernet,
              text: this.ServiceTypeEthernet
            },
            { value: this.ServiceTypeOTU2, text: this.ServiceTypeOTU2 },
            { value: this.ServiceTypeFC_1200, text: this.ServiceTypeFC_1200 },
            { value: this.ServiceTypeSTM_64, text: this.ServiceTypeSTM_64 },
            { value: this.ServiceTypeOTU2_LOWER, text: this.ServiceTypeOTU2_LOWER } ]; 
    	   
    	  
    	}
      
      
      else
    	{
    	  ServiceTypeSrc =  [ {
            value: this.ServiceTypeEthernet,
            text: this.ServiceTypeEthernet
          },
          { value: this.ServiceTypeOTU2, text: this.ServiceTypeOTU2 },
          { value: this.ServiceTypeFC_1200, text: this.ServiceTypeFC_1200 },
          { value: this.ServiceTypeSTM_64, text: this.ServiceTypeSTM_64 }];
    	}
    	 console.log(ServiceTypeSrc);
    	return ServiceTypeSrc;
    	
    },

    /*********************************************
     * Chnl Ptc Mech Types ,
     * Return: Chnl Ptc Mech Source
     *********************************************/
    fGetChannelPtcTypeSrc: function() {
      return [
        { value: this.NoneStr, text: this.NoneStr },
        { value: this.PtcMechOPX, text: this.PtcMechOPX },
        { value: this.PtcMechOLP, text: this.PtcMechOLP }
      ];
    },
    /*********************************************
     * Client Ptc Types ,
     * Return: Client Ptc Mech Source
     *********************************************/
    fGetClientPtcTypeSrc: function() {
      return [
        { value: this.NoneStr, text: this.NoneStr },
        { value: this.PtcMechYCable, text: this.PtcMechYCable },
        { value: this.PtcMechOLP, text: this.PtcMechOLP }
      ];
    },
    /*********************************************
     * Line Rate Types ,
     * Return: Line Rate Source
     *********************************************/
    fGetLineRateTypeSrc: function() {
      return [
        { value: this.LineRate_10, text: this.LineRate_10 },
        { value: this.LineRate_100, text: this.LineRate_100 },
        { value: this.LineRate_200, text: this.LineRate_200 }
      ];
    },
    /*********************************************
     * Client Rate Types ,
     * Return: Client Rate Source
     *********************************************/
    fGetClientRateTypeSrc: function() {
      return [
        { value: this.ClientRate_1, text: this.ClientRate_1 },
        { value: this.ClientRate_1_25, text: this.ClientRate_1_25 },
        { value: this.ClientRate_2_5, text: this.ClientRate_2_5 },
        { value: this.ClientRate_10, text: this.ClientRate_10 },
        { value: this.ClientRate_10_Agg, text: this.ClientRate_10_Agg },
        { value: this.ClientRate_100, text: this.ClientRate_100 }
      ];
    },

    fGetClientRateSrcPOTP: function()
      {
    	  return [
    		 
    		  { value : this.ClientRate_1_25,text: this.ClientRate_1_25},
    		  { value : this.ClientRate_2_5, text: this.ClientRate_2_5 }
    		  
    		  ];
    	     },
    	     
    	     
    	     
      fGetServiceTypeSrcPOTP : function()
      { return [
 		 
		  { value : this.ServiceTypeOTU1,text: this.ServiceTypeOTU0},
		  { value : this.ServiceTypeOTU0, text: this.ServiceTypeOTU1 }
		  
		  ];
	     },
	     
	     
	  fGetTributoryIdSrc   : function(ClientRate)
	  {
		  if(ClientRate == this.ClientRate_2_5)
		{	
		  
		 
			 
			 return [
				 
				 { value : this.TributoryId_Zero,text: this.TributoryId_Zero},
				 { value : this.TributoryId_One,text: this.TributoryId_One},
				 { value : this.TributoryId_Two, text : this.TributoryId_Two},
				 { value : this.TributoryId_Three,text: this.TributoryId_Three},
				 { value : this.TributoryId_Four,text: this.TributoryId_Four},
				 
				 
				 ]; 
			 
		}
		  
		  else
			  {
			  
			  return [
				 { value : this.TributoryId_Zero,text: this.TributoryId_Zero},
				 { value : this.TributoryId_One,text: this.TributoryId_One},
				 { value : this.TributoryId_Two, text : this.TributoryId_Two},
				 { value : this.TributoryId_Three,text: this.TributoryId_Three},
				 { value : this.TributoryId_Four,text: this.TributoryId_Four},
				 { value : this.TributoryId_Five,text: this.TributoryId_Five},
				 { value : this.TributoryId_Six,text: this.TributoryId_Six},
				 { value : this.TributoryId_Seven,text: this.TributoryId_Seven},
				 { value : this.TributoryId_Eight,text: this.TributoryId_Eight}
				 
				 ]; 
			  
			  
			  
			  }
		    },

    /*********************************************
     * Protection Types ,
     * Return: Protection types Source
     *********************************************/
    fGetPtcTypeSrc: function() {
      return [
        { value: this.PtcTypeUnprotected, text: this.PtcTypeUnprotected },
        { value: this.PtcTypeOneIsToOne, text: this.PtcTypeOneIsToOne },
        {
          value: this.PtcTypeOnePlusOnePlusR,
          text: this.PtcTypeOnePlusOnePlusR
        },
        {
          value: this.PtcTypeOnePlusOnePlusTwoR,
          text: this.PtcTypeOnePlusOnePlusTwoR
        },
        { value: this.PtcTypeOneIsToTwoR, text: this.PtcTypeOneIsToTwoR },
        { value: this.PtcTypeOnePlusOne, text: this.PtcTypeOnePlusOne },
        { value: this.PtcTypeLowPriority, text: this.PtcTypeLowPriority }
      ];
    },
    /*********************************************
     * Color Types ,
     * Return: Color types Source
     *********************************************/
    fGetColorTypeSrc: function() {
      return [
        // {value: 'Default', text: 'Default'},
        { value: this.Violet, text: this.Violet },
        { value: this.Indigo, text: this.Indigo },
        { value: this.Blue, text: this.Blue },
        { value: this.Green, text: this.Green },
        { value: this.Yellow, text: this.Yellow },
        { value: this.Orange, text: this.Orange },
        { value: this.Red, text: this.Red }
      ];
    },
    /*********************************************
     * Yes/No Types ,
     * Return: yes/no types Source
     *********************************************/
    fGetYesNoTypeSrc: function() {
      return [
        { value: this.YesStr, text: this.YesStr },
        { value: this.NoStr, text: this.NoStr }
      ];
    },
    /*********************************************
     * Gne Types ,
     * Return: Gne types Source
     *********************************************/
    fGetGneTypeSrc: function() {
      return [
        { value: 0, text: this.NoStr },
        { value: 1, text: this.primaryGneStr },
        { value: 2, text: this.secondaryGneStr }
      ];
    },
    /*********************************************
     * Link Types ,
     * Return: Link types Source
     *********************************************/
    fGetLinkTypeSrc: function() {
      return [
        {
          value: this.getDefaultLinkStr(),
          text: this.getDefaultLinkStr()
        },
        {
          value: this.getRamanHybridLinkStr(),
          text: this.getRamanHybridLinkStr()
        },
        {
          value: this.getRamanDraLinkStr(),
          text: this.getRamanDraLinkStr()
        }
      ];
    },
    /*********************************************
     * Path Types ,
     * Return: Path types Source
     *********************************************/
    fGetPathTypeSrc: function() {
      return [
        { value: this.DisjointPathStr, text: this.DisjointPathStr }
        // { value: this.Non_DisjointPathStr, text: this.Non_DisjointPathStr }
      ];
    }
  }; //return end
})();
