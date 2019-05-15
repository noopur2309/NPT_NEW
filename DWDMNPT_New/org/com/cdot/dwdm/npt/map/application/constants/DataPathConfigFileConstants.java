/**
 * 
 */
package application.constants;

import java.util.HashMap;

/**
 * @author hp
 * @date   28th Jun, 2017
 * @brief  This file contains constants value used for Data Path Creation, 
 * 		   any modification related to it has to propagate over here
 *
 */
@SuppressWarnings("unchecked")
public class DataPathConfigFileConstants {

	/** Encoding Types*/
	public static String InterfaceEncodingType = "8"; 	/** Lambda (photonic) LSP is at lambda level */
	
	/** Switching Types*/
	public static String SwitchingType = "150";  /** Lambda-Switch Capable (LSC) */
	
	/** Wss Channel Config Add-Drop Min-Max Values*/
	public static int WssConfigAddDropMin = 9;  
	public static int WssConfigAddDropMax = 21;  
	
	/** G-PID */
    @SuppressWarnings("rawtypes")
	public final static HashMap gpidConstantsMap = new HashMap();
	static {
		gpidConstantsMap.put("Ethernet", 54);	
		gpidConstantsMap.put("OTU2", 66);
		gpidConstantsMap.put("STM-64", 49);
		gpidConstantsMap.put("STM-16", 49); /***DBG => CHECK**/
		gpidConstantsMap.put("FC-1200", 58);
		gpidConstantsMap.put("FC-100", 58); /**58 for All Fibre Channel*/
		gpidConstantsMap.put("OTU4", 66 );
		gpidConstantsMap.put("Ethernet 100G",54 );
		
	}
	
	/** Direction Constants */
	public static int EAST  	= 1;
    public static int WEST  	= 2;
    public static int NORTH 	= 3;
    public static int SOUTH  	= 4;
    public static int NE  		= 5;
    public static int NW  		= 6;
    public static int SE 		= 7;
    public static int SW  		= 8;
    
    
    /** Topology Constants
    		#define TOPOLOGY_LINEAR			1
			#define TOPOLOGY_HUBBED_RING	2
			#define TOPOLOGY_CLOSED_RING	3
			#define TOPOLOGY_MESH			4
     */
    @SuppressWarnings("rawtypes")
	public final static HashMap topologyConstantsMap = new HashMap();
	static {
		topologyConstantsMap.put("Mesh", 4);
		topologyConstantsMap.put("Close Ring", 3);
		topologyConstantsMap.put("Hub Ring", 2);
		topologyConstantsMap.put("Linear", 1);		
	}
	
    
    /** Map for Direction Constants */
    @SuppressWarnings("rawtypes")
	public final static HashMap directionConstantsHashMap = new HashMap();
	static {
		directionConstantsHashMap.put("east", 1);
		directionConstantsHashMap.put("west", 2);
		directionConstantsHashMap.put("north", 3);
		directionConstantsHashMap.put("south", 4);
		directionConstantsHashMap.put("ne", 5);
		directionConstantsHashMap.put("nw", 6);
		directionConstantsHashMap.put("se", 7);
		directionConstantsHashMap.put("sw", 8);
		directionConstantsHashMap.put("101", 101);
		directionConstantsHashMap.put("201", 201);
		directionConstantsHashMap.put(null, 0);
	}
	
	
   /** Map for EDFA Direction Constants */
    @SuppressWarnings("rawtypes")
	public final static HashMap edfaDirectionConstantsHashMap = new HashMap();
	static {
		edfaDirectionConstantsHashMap.put(4,"east_ne");
		edfaDirectionConstantsHashMap.put(5,"west_nw");
		edfaDirectionConstantsHashMap.put(6,"north_se");
		edfaDirectionConstantsHashMap.put(7,"south_sq");
		edfaDirectionConstantsHashMap.put(8,"east_ne");
		edfaDirectionConstantsHashMap.put(9,"west_nw");
		edfaDirectionConstantsHashMap.put(10,"north_se");
		edfaDirectionConstantsHashMap.put(11,"south_sw");	
		
	}

    
    /** Lambda Wavelength */
    public static float WavelengthValueSet[] = new float[]

    	{ 1529.55f, 1529.94f, 1530.33f, 1530.72f, 1531.12f, 1531.51f, 1531.90f, 1532.29f, 
    	  1532.68f, 1533.07f, 1533.47f, 1533.86f, 1534.25f, 1534.64f, 1535.04f, 1535.43f, 
    	  1535.82f, 1536.22f, 1536.61f, 1537.00f, 1537.40f, 1537.79f, 1538.19f, 1538.58f, 
    	  1538.98f, 1539.37f, 1539.77f, 1540.16f, 1540.56f, 1540.95f, 1541.35f, 1541.75f, 
    	  1542.14f, 1542.54f, 1542.94f, 1543.33f, 1543.73f, 1544.13f, 1544.53f, 1544.92f,
    	  1545.32f, 1545.72f, 1546.12f, 1546.52f, 1546.92f, 1547.32f, 1547.72f, 1548.11f, 
    	  1548.51f, 1548.91f, 1549.32f, 1549.72f, 1550.12f, 1550.52f, 1550.92f, 1551.32f, 
    	  1551.72f, 1552.12f, 1552.52f, 1552.93f, 1553.33f, 1553.73f, 1554.13f, 1554.54f, 
    	  1554.94f, 1555.34f, 1555.75f, 1556.15f, 1556.55f, 1556.96f, 1557.36f, 1557.77f, 
    	  1558.17f, 1558.58f, 1558.98f, 1559.39f, 1559.79f, 1560.20f, 1560.61f, 1561.01f
    	};
    
    /** Lambda Frequency  in MHz*/
    public static float FrequencyValueSet[] = new float[]

    	{  		 119572.5f,119553.0f,119533.5f,119514.0f,119494.0f,119474.5f,119455.0f,119435.5f,119416.0f,119396.5f,
    			 119376.5f,119357.0f,119337.5f,119318.0f,119298.0f,119278.5f,119259.0f,119239.0f,119219.5f,119200.0f,
    			 119180.0f,119160.5f,119140.5f,119121.0f,119101.0f,119081.5f,119061.5f,119042.0f,119022.0f,119002.5f,
    			 118982.5f,118962.5f,118943.0f,118923.0f,118903.0f,118883.5f,118863.5f,118843.5f,118823.5f,118804.0f,
    			 118784.0f,118764.0f,118744.0f,118724.0f,118704.0f,118684.0f,118664.0f,118644.5f,118624.5f,118604.5f,
    			 118584.0f,118564.0f,118544.0f,118524.0f,118504.0f,118484.0f,118464.0f,118444.0f,118424.0f,118403.5f,
    			 118383.5f,118363.5f,118343.5f,118323.0f,118303.0f,118283.0f,118262.5f,118242.5f,118222.5f,118202.0f,
    			 118182.0f,118161.5f,118141.5f,118121.0f,118101.0f,118080.5f,118060.5f,118040.0f,118019.5f,117999.5f
    	};
    
    /**
     *  Channel Spacing :	
     *  				Channel Spacing (GHz) - Value
     *  					Reserved		 	 0
     *  			 			100	 			 1	
     *  						 50 			 2
     *    						 25				 3
     *    						12.5             4
     *  					Future Use			 5-15	
     */
    public static String ChannelSpacing[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
    

    /**
     *  Grid :	
     *  						Grid	 - 		Value
     *  					Reserved		 	 0
     *  			 		ITU-T DWDM	 		 1	
     *  					ITU-T CWDM 			 2
     *    					Future Use			 3-7     						
     */
    public static String Grid[] = {"0", "1", "2", "3", "4", "5", "6", "7"};
    
    
    /**
     * Attenuation Mode : 
     * 						0 - Automatic
     * 						1 - Manual
     * 						2 - Pre-emphasis
     */
    public static int  AttenuationConfigMode_Auto	 	 = 0;
    public static int  AttenuationConfigMode_Manual 	 = 1;
    public static int  AttenuationConfigMode_Preemphasis = 2;
    
    /**
     * Possible values of AttenuationConfigMode for transmit side (out from the node) are :
     * 
     *		CM_AUTOMATIC		= 0
     *		CM_MANUAL_FIX		= 1  //CsccCmSetWssConfigReq->Attenuation filled
     *		CM_MANUAL_VARIABLE	= 2  //WssChannelConfig->Attenuation filled
     *		CM_PRE_EMPHASIS		= 3
     *
     */
    public static int TX_CM_AUTOMATIC 		 	 = 0;
    public static int TX_CM_MANUAL_FIX			 = 1;
    public static int TX_CM_MANUAL_VARIABLE		 = 2;    	
    
    
    /**
     * Possible values of AttenuationConfigMode for receive side (in to the node) are :
     * 
     * 		CM_AUTOMATIC		= 0
     *		CM_MANUAL_FIX		= 1  //CsccCmSetWssConfigReq->Attenuation filled
     *		CM_MANUAL_VARIABLE	= 2  //WssChannelConfig->Attenuation filled
     */
     public static int RX_CM_AUTOMATIC 		 	 = 0;
     public static int RX_CM_MANUAL_FIX			 = 1;
     public static int RX_CM_MANUAL_VARIABLE	 = 2;   
     

     /**
      * 
      * Signal Type :
      * 				Values  <--------->   Types
      * 
      * 				0					Not significant1
	  *					1					ODU1 (i.e., 2.5 Gbps)
	  *					2					ODU2 (i.e., 10 Gbps)
	  *					3					ODU3 (i.e., 40 Gbps)
	  *					4					ODU4 (i.e., 100 Gbps)
	  *					5					Reserved (for future use)
	  *					6					OCh at 2.5 Gbps
	  *					7					OCh at 10 Gbps
	  *					8					OCh at 40 Gbps
	  *					9					OCh at 100 Gbps
	  *					10					ODU0 (i.e., 1.25 Gbps)
	  *					11					ODU2e (i.e., 10 Gbps for FC1200 and GE LAN)
	  *					12-19				Reserved (for future use)
	  *					20					ODUflex(CBR) (i.e., 1.25*N Gbps)
	  *					21					ODUflex(GFP-F), resizable (i.e., 1.25*N Gbps)
	  *					22					ODUflex(GFP-F), non-resizable (i.e., 1.25*N Gbps)
	  *					23-255				Reserved (for future use)
	  *
      */
	
	  public static int Signal_ODU0_10Gbps = 10;
      public static int Signal_ODU2_10Gbps = 2;
      public static int Signal_ODU4_100Gbps = 4;
      public static int Signal_OCh_40Gbps  = 8;
      public static int Signal_OCh_100Gbps = 9;
      
      
      /**
       * Notification Flag
       */
      public static int Notification_Flag  = 1;
      
      /**
       * 	     LSP Flag:
       *    	This is 6 bit field and it indicates the desired end-to-end LSP recovery type. 
       * 		A value of 0 implies that the LSP is "Unprotected". 
       * 
       *    		     Values  <--------->   Types
      * 
      *   				    0x00							Unprotected
	  *					0x01							Rerouting
	  *					0x02							Rerouting without extra traffic
	  *					0x04							1:N Protection 
	  *					0x08							1+1 unidirectional
	  *					0x10							1+1 Bidirectional 								
       */
      public static int Lsp_Flag_Unprotected 										= 0;
      public static int Lsp_Flag_Rerouting    										= 1;
      public static int Lsp_Flag_Rerouting_Without_Extra_Traffic = 2;
      public static int Lsp_Flag_1_N_Protection                              = 4;
      public static int Lsp_Flag_1_Plus_1_Unidirectional 			 = 8;
      public static int Lsp_Flag_1_Plus_1_Bidirectional 			     = 16;
      
      /**
       *  Link Flags:
       *  This is 6 bits field and it indicates desired link protection type.
       */
      public static int Link_flags_Unprotected                               = 2;   /**It indicates that the LSP should not use any link layer protection.*/
      
      
      /**
       *  IsRevertive Values
       */
      public static int IsRevertive_Unidirectional_and_non_revertive = 1;
      public static int IsRevertive_Unidirectional_and_revertive 	 = 2;
      public static int IsRevertive_Bidirectional_and_non_revertive  = 3;
      public static int IsRevertive_Bidirectional_and_revertive 	 = 4;
      
      public static int IsRevertive_Yes  = 1;
      public static int IsRevertive_No 	 = 0;
      
      
      /**
       * Protection Subtype : Y-cable, OLP
       */
      public static int ProtectionsubType_YCable   		= 1;
      public static int ProtectionsubType_OLP_Card = 2;
      
      /**
       * FECType : G-709
       */
      public static int FEC_Type_G709  = 1;
      
      public static int FEC_Type_G709_LAMBDALSP  = 8;
      
      /**
       * FECStatus : Enable, Disable
       */
      public static int FEC_Status_Enable   = 1;
      public static int FEC_Status_Disable  = 0;
      
      
      /**
       * "TimDectMode" : OFF : 0, SAPI only : 1, DAPI only : 2, SAPI+DAPI : 3
       */
      public static int TimDectMode_OFF  		   = 0;
      public static int TimDectMode_SAPI 		   = 1;
      public static int TimDectMode_DAPI	 	   = 2;
      public static int TimDectMode_SAPI_AND_DAPI  = 3;
      
      /**
       * "GccType" [ GCC0 : 0, GCC1 : 1, GCC2 : 2, GCC_DEFAULT : 3 ]
       */
      
      public static int GccType_GCC0  			   = 0;
      public static int GccType_GCC1 			   = 1;
      public static int GccType_GCC2 			   = 2;
      public static int GccType_GCC_DEFAULT 	   = 3;
      
      
      /**
       * "GccValue" [0](currently GCC bytes are not in use)
       */
      public static int GccValue_Default		   = 0;
     
      
      /**
       * "GccStatus" [GCC_DISABLED : 0, GCC_ENABLED : 1]
       */
      public static int GccStatus_GCC_DISABLED  	= 0;
      
      /**
       *"TCMActStatus" [Enable(1)/Disable(0)] 
       */
      
      public static int TCMActStatus_ENABLE   		= 1;
      public static int TCMActStatus_DISABLE 		= 0;
      
      /**
       * RoutePriority Integers
       */
      public static int RoutePriority_Working        = 1;
      public static int RoutePriority_Protection     = 2;
      public static int RoutePriority_Restoration   = 3;
      
      /**
       * RoutePriority Strings
       */
      public static String  RoutePriority_Working_Str        = "Working";
      public static String  RoutePriority_Protection_Str     = "Protection";
      public static String  RoutePriority_Restoration_Str   = "Restoration"; 
      
      /**
       * Upstream and Downstream for Tpnport info
       */
      public static int UpStream 		= 1;
      public static int DownStream   = 0;
      
      
      /**
       * Card Type Constants
       */
      @SuppressWarnings("rawtypes")
  	public final static HashMap cardMappingConstantsHashMap = new HashMap();
  	static {
  		
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponderCGM, 1);
		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponderCGX, 1);
		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponderOPX, 1);  
		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponderOPXCGX, 1);  
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardCscc, 2); /**DBG => as of now only one part code for tpc and cscc */
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardPaBa, 4);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardIla, 5);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardOlp, 6);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardOcm1x16, 7); 
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardOcm1x8, 7); 
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardOcm1x2, 7); 
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardWss1x2, 8);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardWss1x9, 9);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardWss8x12, 15);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardEdfa, 10);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardMcs,11);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardSupy, 12);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardWss1x2x20, 13);
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponder10G, 1); 
		cardMappingConstantsHashMap.put("TPN 100G", 14);
		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardTPN100GCGC, 14);
		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardRamanHybrid, 4); // Raman Change
		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardRamanDra, 4); // Raman Change
  		cardMappingConstantsHashMap.put(null, 0); /**For the unspecified value*/
  		cardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponder200G, 1); // 200G Change
  	}
      
  	
  	 /**
     * Sub Card Type Constants
     */
    @SuppressWarnings("rawtypes")
	public final static HashMap subtypeCardMappingConstantsHashMap = new HashMap();
	static {
		
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponderCGM, 1);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponderOPX, 6);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponderOPXCGX, 7);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponderCGX, 5);
		subtypeCardMappingConstantsHashMap.put("TPN 100G", 4);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardTPN100GCGC, 4);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponder10G, 4);
		///subtypeCardMappingConstantsHashMap.put("CSCC", 2); /**DBG => as of now only one part code for tpc and cscc */
		
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardEdfa, 0);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardPaBa, 0);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardIla, 0);

		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardOcm1x8, 0); 
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardOcm1x2, 1);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardOcm1x16, 2); /**DBG => need to look in, since not defined as of now */
		
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardWss1x2, 0);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardWss1x9, 0);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardWss8x12, 0);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardWss1x2x20, 0);
		
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardMcs, 0);
		
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardSupy, 0);
		
		subtypeCardMappingConstantsHashMap.put("0", 0); /**For the unspecified value*/
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardRamanHybrid, 8);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardRamanDra, 10);
		subtypeCardMappingConstantsHashMap.put(ResourcePlanConstants.CardMuxponder200G, 8); // 200G Change

	}
  	
	
	 /**
     * Protection Topology Constants : As used by DB
     */
    @SuppressWarnings("rawtypes")
	public final static HashMap protectionTopologyMappingConstantsHashMap = new HashMap();
	static {
				
		protectionTopologyMappingConstantsHashMap.put(DataConfigConstants.ProtClient_BPSR, 1);
		protectionTopologyMappingConstantsHashMap.put(DataConfigConstants.ProtLink, 2);
		protectionTopologyMappingConstantsHashMap.put(DataConfigConstants.ProtChannel, 3);
		protectionTopologyMappingConstantsHashMap.put(DataConfigConstants.ProtClient_OSNCP, 4);
		protectionTopologyMappingConstantsHashMap.put(DataConfigConstants.ProtOneIsToOne, 5);
		protectionTopologyMappingConstantsHashMap.put(DataConfigConstants.ProtOneIsToTwoR, 6);
		
	}
		
	
	/** Protection Mechanism Constants */
    @SuppressWarnings("rawtypes")
	public final static HashMap protectionConstantsHashMap = new HashMap();
	static {
		protectionConstantsHashMap.put("Y-Cable", 1);
		protectionConstantsHashMap.put("OLP", 2);
		protectionConstantsHashMap.put("OPX", 3);
	}

  	
  	public final static String PA_BA 			           = ResourcePlanConstants.CardPaBa; 
  	public final static String ILA	 			           = ResourcePlanConstants.CardIla; 
  	public final static String SUPY		 	               = ResourcePlanConstants.CardSupy;
  	public final static String EDFA_ARRAY		           = ResourcePlanConstants.CardEdfa;
  	public final static String WSS_1X2  	               = ResourcePlanConstants.CardWss1x2;
  	public final static String WSS_2X1X9 	               = ResourcePlanConstants.CardWss1x9;
  	public final static String WSS_2X1X20 	               = ResourcePlanConstants.CardWss1x2x20;
  	public final static String Raman_Hybrid                = ResourcePlanConstants.CardRamanHybrid;
  	public final static String Raman_Dra                   = ResourcePlanConstants.CardRamanDra;
  	public final static String Ila_Hybrid                  = ResourcePlanConstants.CardIlaRamanHybrid;
  	public final static String Ila_Dra                     = ResourcePlanConstants.CardIlaRamanDra;
 
  	
  	/**
  	 * CmAmplifier Configuration Constants
  	 */
  	public static int 	Amplifier_STATUS_ENABLE	           = 1;
  	public static int 	Amplifier_STATUS_DISABLE	       = 0;
  	
  	public static int 	AmplifierType_BA 		           = 1;
  	public static int 	AmplifierType_PA 		  		   = 2;
  	public static int 	AmplifierType_ILA 		           = 3;
  	public static int 	AmplifierType_EDARY		           = 4;
  	public static int 	AmplifierType_HYBRID_RAMAN		   = 12; 	
  	public static int 	AmplifierType_SIMPLE_RAMAN		   = 14;
  	public static int 	AmplifierType_HYBRID_ILA		   = 13;
  	public static int 	AmplifierType_SIMPLE_ILA		   = 15;
  	
  	public static float	AmplifierType_BA_Default_GainLimit	 		   = 20;
  	public static float	AmplifierType_PA_Default_GainLimit			   = 25;
  	public static float	AmplifierType_ILA_Default_GainLimit			   = 25;
  	public static float	AmplifierType_EDFA_ADD_GainLimit		   	   = 15;
  	public static float	AmplifierType_EDFA_DROP_GainLimit		   	   = 20;
  	public static float	AmplifierType_RAMAN_Default_GainLimit		   	   = 19;
  	
  	
  	public static int tunnelIdGlobal = 1;  	
  	public static int forwardingAdjacencyGlobal = 2560; /**In Hex 0xA00*/
  	public static int gainTilt = -1;
  	
  	public static int EDFA_DIRECTION									= 11;
  	  	
  	
  	/**
  	 * CmWss Configuration Constants
  	 */
  	public static int WSS_1x2											= 8;
  	public static int WSS_2x1x9											= 9;
  	public static int WSS_2x1x20										= 13;
  	
  	public static int WSS_PREMPHASISTRIGGERPOWERDIFF  					= 2;
  	public static int WSS_ATTENUATIONCONFIGMODE  						= 3;
  	public static int WSS_DIRECTION_CONSTANT	  						= 20;
  	
  	
  	/**
  	 * CmPtcClientPortInfo Configuration Constants
  	 */
  	public static int ProtMechanism_YCABLE 								=1;
  	public static int ProtMechanism_OLP 								=2;
  	
  	/**
  	 * Subnet Constants 
  	 */
  	public static String Node_IpPrefixLength 							="29";
  	public static String Link_IpPrefixLength 							="30";
  	
  	
  	/**
  	 * String Constants
  	 */
  	public static String Rack 										   ="Rack";
  	public static String SubRack									   ="SubRack";
  	public static String CardId										   ="CardId";
  	
  	
  	/**
  	 * Wss Static Constants 
  	 */
  	public static String PreEmphasisTerminationPowerDiff 										   ="5";
  	public static String PowerEqualizationTerminationPowerDiff									   ="1";
  	public static String TxChannelMode														       ="0";
  	public static String Attenuation														       ="99.9";	
  	
  	
  	/**
  	 * Protection Type String Constants
  	 */
  	public static String one_isto_R = "1:1";
  	public static String one_isto_twoR = "1:2R";
  	public static String one_plus_one_plus_R = "1+1+R";
  	public static String one_plus_one_plus_twoR = "1+1+2R";
  	
  	/**
  	 * Inject Tag Constants
  	 */
	public static int    NoChangeTagValue	   = 0;  
  	public static int    AddTagValue		   = 1;
  	public static int    ModifiedTagValue      = 2;
  	public static int    DeleteTagValue        = 3;
	  
	public static final String InjectTagForLAMBDALSP           = "LAMBDAMLSP";
  	public static final String InjectTagForOTNTDMLSP           = "OTNTDMLSP";
	public static final String InjectTagForPtcClientProtInfo   = "PtcClientProtInfo";
	public static final String InjectTagForPtcLineProtInfo     = "PtcLineProtInfo";
	public static final String InjectTagForDirectionDetails    = "DirectionDetails";
	public static final String InjectTagForTPNDetails          = "TPNDetails";
	public static final String InjectTagForCmMcsPortMapping    = "CmMcsPortMapping";
  	
  	/**
  	 * LSP Bandwidth Constants
  	 */
	public static String  LineRate_200G = "200";
  	public static String  LineRate_100G = "100";
  	public static String  LineRate_10G = "10";
  	public static String  Traffic_10G   = "10";
  	public static String  Traffic_1G   = "1";
  	
  	/**
  	 * TAR file Object Parameter
  	 */
  	public static String home = System.getProperty("user.home");//home directory
  	public static String PATH_FILENAME = home+"/Downloads/ConfigPathFiles/DataPath/Tar/";//Created Path
  	public static String RESOURCES_NAME = "/LABNETWORK_1.0";//Compressed tar.gz File Name
  	public static final String OUTPUT_DIRECTORY = home+"/Downloads/ConfigPathFiles/DataPath/Tar"; //Compressed tar.gz Output Directory
  	
  	
  	/**
  	 * Channel Protection Constants
  	 */
  	public static String  LinePortNum_101 = "101";
  	public static String  LinePortNum_102 = "102";
  
    /**
	 * Card Filter Strings 
	 */
	public static String MuxponderFilter = "MPN";

	/**
	 * 10G Agg Constants 
	 */
	public static int TengAggFAOffset   	 	   = 100;
	public static final int Non10GAggClientSideOtnLsp    = 3; /**Super Parent */
	public static final int MPNClientSideOtnLsp   	   = 2; /**Super Parent */
	public static final int XGMLineSideOtnLsp    		   = 1; /**Intermediate node */
	public static final int XGMClientSideOtnLsp  		   = 0; /**Leaf node of tree structure */

	public static int TenGAggGlobalOtnLspID 	   = 0;
	
	
	
	
	@SuppressWarnings("rawtypes")	public final static HashMap ReversegpidConstantsMapcase2 = new HashMap<Integer,Object>();
	static {
		ReversegpidConstantsMapcase2.put( 54,"Ethernet");	
		ReversegpidConstantsMapcase2.put( 49,"STM-64"); /***DBG => CHECK**/
		ReversegpidConstantsMapcase2.put( 58,"FC-1200"); /**58 for All Fibre Channel*/
		ReversegpidConstantsMapcase2.put( 66,"OTU2");
		
		}
	
	
	@SuppressWarnings("rawtypes")
	public final static HashMap ReversegpidConstantsMapcase4_10 = new HashMap<Integer,Object>();
	static {
		ReversegpidConstantsMapcase4_10.put( 54,"Ethernet 100G");	
		ReversegpidConstantsMapcase4_10.put( 66,"OTU4");
		}
	
	@SuppressWarnings("rawtypes")
	public final static HashMap ReverseDirectionMap = new HashMap<Integer,Object>();
	static {
		
		
		
		ReverseDirectionMap.put(1,"east");
		ReverseDirectionMap.put(2,"west");
		ReverseDirectionMap.put(3,"north");
		ReverseDirectionMap.put(4,"south");
		ReverseDirectionMap.put(5,"ne");
		ReverseDirectionMap.put(6,"nw");
		ReverseDirectionMap.put(7,"se");
		ReverseDirectionMap.put(8,"sw");
		ReverseDirectionMap.put(101,"101");
		ReverseDirectionMap.put(201,"201");
		ReverseDirectionMap.put(0,null);
		
	}
	
	@SuppressWarnings("rawtypes")
	public final static HashMap ReverseDirectionMapforWss = new HashMap<Integer,Object>();
	static {
		
		
		
		ReverseDirectionMapforWss.put(21,"east");
		ReverseDirectionMapforWss.put(22,"west");
		ReverseDirectionMapforWss.put(23,"north");
		ReverseDirectionMapforWss.put(24,"south");
		ReverseDirectionMapforWss.put(25,"ne");
		ReverseDirectionMapforWss.put(26,"nw");
		ReverseDirectionMapforWss.put(27,"se");
		ReverseDirectionMapforWss.put(28,"sw");
		ReverseDirectionMapforWss.put(101,"101");
		ReverseDirectionMapforWss.put(201,"201");
		ReverseDirectionMapforWss.put(0,null);
		
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public static String TrafficTypeMapping(int SignalType, int GPID)
	{
		String TrafficType = null;
		
		switch(SignalType)
		
		{
		case 2  : { 
			            TrafficType = ReversegpidConstantsMapcase2.get(GPID).toString();
			            break;
		                                                                                   }
		
		case 10 : {
			            TrafficType = ReversegpidConstantsMapcase4_10.get(GPID).toString(); 
			            break;
		                                                                                   }
		
		case 4  : {
			            TrafficType = ReversegpidConstantsMapcase4_10.get(GPID).toString();
			            break;
			
		                                                                                   }
		default :      
			            break;
		
		}
		
		
		return TrafficType;
	}
	
	@SuppressWarnings("rawtypes")
	public static HashMap ReverseServiceTypeForLCTLambda = new HashMap<Integer,Object>();
	static {
		
		ReverseServiceTypeForLCTLambda.put(9, "Ethernet");
		ReverseServiceTypeForLCTLambda.put(10, "STM64");
		ReverseServiceTypeForLCTLambda.put(1, "OTU2");
		ReverseServiceTypeForLCTLambda.put(26, "OTU4");
		ReverseServiceTypeForLCTLambda.put(15, "FC1200");
		ReverseServiceTypeForLCTLambda.put(25, "Ethernet 100G");
	}
	
	@SuppressWarnings("rawtypes")
	public static String ReverseCardTypeMapping(int CardType, int CardSubType)
	{
		String Type = "";
		
		if(CardType == 1 && CardSubType == 1)
		Type = "MPN(CGM)";
		
		
		else if(CardType == 1 && CardSubType == 6)
		Type = "MPN(OPX)";
		
		else if(CardType == 1 && CardSubType == 5)
		Type = "MPN(CGX)";
		
		else if(CardType == 1 && CardSubType == 7)
		Type = "MPN(OPXCGX)";
		
		else if(CardType == 1 && CardSubType == 4)
	    Type = "MPN 10G";
		
		else if(CardType == 14 && CardSubType == 4)
		Type = "TPN 100G";	
		
		
		return Type;
		
	}
	
	
	
	
   

}
