package application.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface ResourcePlanConstants {

	/*Cardtypes*/
	public static String CardDummy			=	"DUMMY";	
	public static String CardMuxponderCGM	=	"MPN(CGM)";//100G card, double slot
	public static String CardMuxponder40G	=	"MPN 40G";
	public static String CardMuxponder10G	=	"MPN 10G";
	public static String CardMuxponder200G	=	"MPN 200G";
	public static String CardMuxponderOPX	=   "MPN OPX(CGM)";
	public static String CardMuxponderOPXCGX=   "MPN OPX(CGX)";
	public static String CardTPN5x10G		=	"TPN 5x10G";	
	//public static String CardTPN100G		=	"TPN 100G";	

	/******* TPN CGC added to equipment list:28/2/18 ********/  
	public static String CardTPN100GCGC		=	"TPN(CGC)";	
	public static String CardXGM			=	"XGM";	

	public static String CardSupy			=	"OSC";
	public static String CardCscc			=	"CSCC";
	public static String CardWss1x9			=	"WSS2x1x9";
	public static String CardWss1x2x20		=	"WSS2x1x20";
	public static String CardWss1x2			=	"WSS1x2";
	public static String CardWss8x12		=	"WSS8x12";
	public static String CardPaBa			=	"PA/BA";
	public static String CardMuxponderCGX	=	"MPN(CGX)";//100G card, single slot
	public static String CardMcs			=	"MCS";//double slot card
	public static String CardMpc			=	"MPC";//
	public static String CardOlp			=	"OLP";//
//	public static String CardOcm1x2			=	"OPM1x2";//
//	public static String CardOcm1x8			=	"OPM1x8";//
//	public static String CardOcm1x16		=	"OPM1x16";//
	public static String CardOcm1x2			=	"OCM1x2";//
	public static String CardOcm1x8			=	"OCM1x8";//
	public static String CardOcm1x16		=	"OCM1x16";//
	public static String CardEdfa			=	"EDFA ARRAY";//
	public static String CardVoip			=	"VOIP";//
	public static String CardIla			=	"ILA";//
	public static String CardIlaRamanHybrid	=	"RAMAN ILA (HYBRID)";//
	public static String CardIlaRamanDra	=	"RAMAN ILA (DRA)";//
	public static String CardReserved		=   "RESV";//
	public static String BayTop				=   "BAYTOP";//
	public static String FillerPlate		=   "FILLER PLATE";//
	public static String CardRamanHybrid	=   "RAMAN PA/BA (HYBRID)";//
	public static String CardRamanDra		=   "RAMAN PA/BA (DRA)";//

	public static String AddDropTwoCardSet		=   "TWO CARD(2X1X9 + 2X1X9/2X1X20)";//
	public static String AddDropSingleCardSet	=   ResourcePlanConstants.CardWss8x12;//

	public static String EmersionChassis	=   "EMERSION";
	public static String ComtelChassis		=   "COMTEL";
	public static String PentairChassis		=   "PENTAIR";
	public static String SixSlotChassis		=   "SIX-SLOT CHASSIS";


	public static String CardOtdr1X2		=	"OTDR 1X2";
	public static String CardOtdr1X4		=	"OTDR 1X4";
	public static String CardOtdr1X8		=	"OTDR 1X8";
	public static String CardOtdr1X16		=	"OTDR 1X16";

	public String PCord_LC_APC_to_FC_PC_20m				= "LC/APC-FC/PC, 20M LONG";
	public String PCord_LC_APC_to_LC_UPC_01m			= "LC/APC-LC/UPC, 1 MTR LONG";
	public String PCord_LC_APC_to_LC_UPC_10m			= "LC/APC-LC/UPC, 10 MTR LONG";
	public String PCord_LC_APC_to_LC_UPC_03m			= "LC/APC-LC/UPC, 2.5 MTR LONG";
	public String PCord_LC_UPC_to_LC_UPC_02m			= "LC/UPC-LC/UPC, 2M LONG";
	public String PCord_LC_UPC_to_LC_UPC_01m			= "LC/UPC-LC/UPC, 1 MTR LONG";
	public String PCord_LC_APC_to_LC_APC_02m			= "LC/APC-LC/APC, 2.0M LONG";
	public String PCord_LC_APC_to_LC_APC_01m			= "LC/APC-LC/APC, 1MTR LONG";
	public String PCord_LC_APC_to_LC_APC_03m			= "LC/APC-LC/APC, 2.5M LONG";
	public String PCord_LC_APC_to_LC_APC_10m			= "LC/APC-LC/APC, 10M LONG";
	public String PCord_LC_APC_to_LC_APC_point2m		= "LC/APC-LC/APC, 0.2MTR LONG";
	public String PCord_LC_APC_to_LC_UPC_02m			= "LC/APC-LC/UPC, 2M LONG";
	public String PCord_LC_UPC_to_FC_UPC_03m			= "LC/UPC-FC/UPC, 3 MTR LONG";
	public String PCord_LC_UPC_to_FC_UPC_20m			= "LC/UPC-FC/UPC, 20 MTR LONG";
	public String PCord_LC_APC_to_LC_APC_point_5m		= "LC/APC-LC/APC, 0.5 MTR LONG";
	public String PCord_E2000_UPC_to_LC_UPC_10m			= "E2000/UPC-LC/UPC, 10 MTR LONG";

	//Ethernet Cables
	public String PCord_CAT6_With_RJ45_10m							= "CAT6, 24 AWG UTP PATCHCORD WITH RJ-45 PLUG AT ENDS. 10MTR";
	public String PCord_CAT6_With_RJ45_3m							= "CAT6, 24 AWG UTP PATCHCORD WITH RJ-45 PLUG AT ENDS. 3MTR";
	public String PCord_Power_Cable_Exchange1_TE_PSU_Red			= "External Power cable Exchange-1 to TE PSU (Red)";
	public String PCord_Power_Cable_Exchange2_TE_PSU_Red			= "External Power cable Exchange-2 to TE PSU (Red)";
	public String PCord_Power_Cable_Exchange1_TE_PSU_Blue			= "External Power cable Exchange-1 to TE PSU (Blue)";
	public String PCord_Power_Cable_Exchange2_TE_PSU_Blue			= "External Power cable Exchange-2 to TE PSU (Blue)";
	public String PCord_Power_Cable_Exchange1_TE_EarthBusBar_Black	= "External Power cable Exchange-1 to TE Earth Bus Bar (Black)";
	public String PCord_Power_Cable_Exchange2_TE_EarthBusBar_Black	= "External Power cable Exchange-2 to TE Earth  Bus Bar (Black)";
	public String PCord_Assembled_Pot_free_cable					= "Internal POT Free Cable";
	//Racks
	public String TE_Main_Rack										= "TE Main Rack";
	public String Muxponder_Rack									= "Muxponder Rack";
	public String ILA_Rack											= "ILA Rack";
	public String ROADM_Main_Rack_2Degree							= "2 Degree ROADM Main Rack"; 
	public String ROADM_Main_Rack_8Degree							= "8 Degree ROADM Main Rack";

	public String YCable1x2Unit										= "YCable1x2Unit";
	public String Odd_Mux_Demux_Unit								= "Odd Mux/ Demux Unit";
	public String Even_Mux_Demux_Unit								= "Even Mux/Demux Unit";
	public String Even_Mux_Unit										= "Even Mux Unit";
	public String Even_Demux_Unit									= "Even Demux Unit";
	public String Odd_Mux_Unit										= "Odd Mux Unit";
	public String Odd_Demux_Unit									= "Odd Demux Unit";
	public String DCM_Tray_Unit										= "Fixed DCM tray Unit";
	public String Regenerator100G									= "Regnerator 100G";
	public String Regenerator10G									= "Regnerator 10G";
	public String VoiP_Card											= "VoiP Card";
	public String Mid_Stage_amplifier								= "Mid Stage amplifier";
	public String Tunable_Filter_Card								= "Tunable filter Card";
	public String YCable2x2Unit										= "YCable2x2Unit";
	public String ATCA_Chassis										= "ATCA Chassis";

	//SFPS

	public String SFP_Electrical_40Km								= "SFP ETH 40Km";
	public String SFP_Electrical_80Km								= "SFP ETH 80Km";
	public String SFP_Electrical_10Km								= "SFP ETH 10Km";
	public String SFP_Multirate_40Km								= "SFP MULTIRATE 40Km";
	public String SFP_Multirate_80Km								= "SFP MULTIRATE 80Km";
	public String SFP_Multirate_10Km								= "SFP MULTIRATE 10Km";



	/*Muxponder types*/




	/*Node Pool constants*/
	public String Port			 	= 	"Port";
	public String Card			 	= 	"Card";
	public String Sbrack			= 	"Sbrack";
	public String Rack				= 	"Rack";

	/*Option string*/

	public String Yes				="Yes";
	public String No				="No";
	public String None				="None";

	/*Status Mpn*/

	public String Working				="W";
	public String Protection			="P";
	public String Aggregator			="A";

	/*Integer Constants*/
	public static int ZERO  = 0;
	public static int ONE	  = 1;
	public static int TWO	  = 2;
	public static int THREE = 3;
	public static int FOUR  = 4;
	public static int FIVE  = 5;
	public static int SIX   = 6;
	public static int SEVEN = 7;
	public static int EIGHT = 8;   
	public static int NINE  = 9;  
	public static int TEN   = 10; 
	public static int ELEVEN = 11;  
	public static int TWELVE = 12;  
	public static int THIRTEEN = 13;  
	public static int FOURTEEN = 14;	

	public static int MaxRack = 5;	
	public static int MaxSbrackPerRack = 3;
	public static int MaxSlotPerSbrack = 14;
	public static int MaxSbrack = 15;
	public static int MaxMpcPerSubRack=1;
	public static int SubRackOne=1;
	public static int SubRackTwo=2;
	public static int SubRackThree=3;
	public static int MaxNodeDegree = 8;	
	/*slot size*/
	public static int SlotSizeOne = 1;
	public static int SlotSizeTwo = 2;

	/*Main Rack n Sbrack*/
	public static int MainRack =1;
	public static int MainSbrack =2;
	public static int MainControllerActive =6;
	public static int MainControllerPassive =9;

	/*Equipment Ids*/
	public int EIdCscc										=1;
	public int EIdMpnCgm									=2;
	public int EIdOlp										=3;
	public int EIdPaBa										=4;
	public int EIdWss1x9									=5;
	public int EIdSupy										=6;
	public int EIdMcs										=7; 
	public int EId1x8_OPM_Card_2D_TE_n_2D_ROADM				=9;
	public int EIdEdfa										=10;
	public int EIdAtcaChassis								=11;
	public int EIdPowerSupplyUnit							=12;    
	public int EIdYCable1x2Unit								=13;
	public int EIdIlaCard									= 14;
	public int EId1x2_OPM_Card_2D_ILA						= 15;
	public int EId1x2_WSS_Card_for_2D_TE_n_2D_ROADM			= 16;
	public int EId_Twin_1x20_WSS_Card_8D_CDC 				= 17;
	public int EId_40G_Muxponder_Card						= 18;    
	public int EId_10G_MPN_Card								= 19;    
	public int EId_100G_MUXPONDER_CARD_CGX					= 20;
	public int EId_1x16_OTDR_8D_CDC							= 21;
	public int EId_1x16_OPM_8D_CDC							= 22;
	public int EId_Hybrid_Raman_n_EDFA_card_8D				= 23;
	public int EId_Odd_Mux_Demux_Unit_2D_TE_n_ROADM			= 24;
	public int EId_Even_Mux_Demux_Unit_2D_TE_n_ROADM		= 25;
	public int EId_2x2_YCable_Unit							= 26;
	public int EId_DCM_Unit									= 27; 

	public int EId_LC_APC_to_FC_UPC_20m						= 28;

	public int EId_LC_APC_to_LC_UPC_01m						= 29;    
	public int EId_LC_APC_to_LC_UPC_02m						= 30;
	public int EId_LC_APC_to_LC_UPC_03m						= 31;
	public int EId_LC_APC_to_LC_UPC_10m						= 32;


	public int EId_LC_UPC_to_LC_UPC_02m						= 33;
	public int EId_LC_UPC_to_LC_UPC_01m						= 34;
	public int EId_LC_UPC_to_LC_UPC_03m						= 35;
	public int EId_LC_UPC_to_LC_UPC_10m						= 36;
	public int EId_LC_UPC_to_LC_UPC_20m						= 1111;

	public int EId_LC_APC_to_LC_APC_01m						= 37;
	public int EId_LC_APC_to_LC_APC_02m						= 38;
	public int EId_LC_APC_to_LC_APC_03m						= 39;
	public int EId_LC_APC_to_LC_APC_10m						= 40;    
	public int EId_LC_APC_to_LC_APC_point2m					= 41;

	public int EId_LC_UPC_to_FC_UPC_03m						= 42;
	public int EId_LC_UPC_to_FC_UPC_20m						= 43;
	public int EId_CAT6_With_RJ45_10m						= 44;
	public int EId_CAT6_With_RJ45_3m						= 45;
	public int EId_Power_Cable_Exchange1_TE_PSU_Red			= 46;
	public int EId_Power_Cable_Exchange2_TE_PSU_Red			= 47;
	public int EId_Power_Cable_Exchange1_TE_PSU_Blue		= 48;
	public int EId_Power_Cable_Exchange2_TE_PSU_Blue		= 49;
	public int EId_Power_Cable_Exchange1_TE_EarthBusBar_Black	= 50;
	public int EId_Power_Cable_Exchange2_TE_EarthBusBar_Black	= 51;

	public int EId_TE_Main_Rack									= 52;   
	public int EId_ILA_Rack										= 53;
	public int EId_ROADM_Main_Rack_2Degree						= 54;   
	public int EId_ROADM_Main_Rack_8Degree						= 55;
	public int EId_Muxponder_Rack								= 56;  
	public int EId_200G_MPN_Card								= 57;  
	//public int EId_100G_TPN_Card								= 58;
	public int EId_100G_TPN_Card_CGC							= 58;
	public int EId_10G_TPN_Card									= 59;
	public int EId_100G_Regenerator								= 60;
	public int EId_VoiP_Card									= 61;
	public int EId_Mid_Stage_amplifier							= 62;
	public int EId_Tunable_Filter_Card							= 63;
	public int EId_SFP_Multirate_40Km							= 64;
	public int EId_SFP_Multirate_80Km							= 65;
	public int EId_SFP_Electrical_40Km							= 66;
	public int EId_SFP_Electrical_80Km							= 67;
	public int EId_SFP_Electrical_SCM							= 68; 
	public int EId_CardReserved									= 69; 
	public int EId_10G_Regenerator								= 70;
	public int EId_100G_MUXPONDER_CARD_OPX						= 71;
	public int EId_100G_MUXPONDER_CARD_OPX_CGX					= 72;
	public int EId_FillerPlate									= 73;
	public int EId_Port_Free_cable								= 74;
	public int EId_Even_Mux_Unit								= 75;
	public int EId_Even_Demux_Unit								= 76;
	public int EId_Odd_Mux_Unit									= 77;
	public int EId_Odd_Demux_Unit								= 78;
	public int EIdMpc											= 79;
	public int EIdCardXgm										= 80;
	public int EId_CardOtdr1X2									= 81;
	public int EId_CardOtdr1X4									= 82;
	public int EId_CardOtdr1X8									= 83;
	public int EId_CardOtdr1X16									= 84;
	public int EId_CardRamanHybrid								= 85;

	/** CD Roadm add/drop card **/
	public int EId_Wss8x12										= 86;
	/** Raman cards **/
	public int EId_CardRamanDra									= 87;
	public int EIdIlaRamanHybridCard							= 88;
	public int EIdIlaRamanDraCard								= 89;

	/** Six Slot Chassis **/
	public int EIdSixSlotChassis								= 90;

	/** Raman cards **/
	public int EId_SFP_OTU_10Km									= 91;
	public int EId_SFP_SUPY										= 92;
	public int EId_SFP_XGM_Client								= 93;

	/** RAMAN **/
	public int EId_E2000_UPC_to_LC_UPC_10m					   = 95;
	public int EId_LC_APC_to_LC_APC_point_5m					= 96;
	
	/*Equipment Partcodes*/
	public String EPartCscc	    							="APC-GCSF28/G-SE1";
	public String EPartMpc	    							="APC- GCSF28/G-BE1";
	public String EPartSupy									="APC-GCSF28/G-CE1";
	public String EPartMpnCgm								="APC-CGMF41/I-SE1";
	public String EPartOlp									="APC- CAFF55/O-CE2";
	public String EPartPaBa									="APC-CAFF55/O-BE2";
	public String EPartWss1x9								="APC- WSSF64/O-BE2";    
	public String EPartMcs									="APC- WSSF64/O-DE2";   
	public String EPart1x8_OPM_Card_2D_TE_n_2D_ROADM		="APC-CAFF55/O-DE2";
	public String EPartEdfa									="APC- WSSF64/O-EE2";;
	public String EPartAtcaChassis							="ASN-S4755782-DE1";
	public String EPartSixSlotChassis						="ASN-GSYS6006-560";
	public String EPartPowerSupplyUnit						="ASM-TPNMPSUX-000";
	public String EPartYCable1x2Unit						="ASM-OC12YCAM-000";
	public String EPartIlaCard								= "APC-CAFF55/O-AE2";
	public String EPart1x2_OPM_Card_2D_ILA					= "APC-CAFF55/O-EE2";
	public String EPart1x2_WSS_Card_for_2D_TE_n_2D_ROADM	= "APC-WSSF64/O-AE2";
	public String EPart_Twin_1x20_WSS_Card_8D_CDC 			= "APC- WSSF64/O-CE2";
	public String EPart_40G_Muxponder_Card					= "APC-FGMF78/I-SE0";    
	public String EPart_10G_MPN_Card						= "APC-XGMH16/C-SE1";  
	public String EPart_10G_TPN_Card						= "APC-CGXH14/E-BE0";  
	public String EPart_200G_MPN_Card						= "APC-200GH13/E-SE0";
	//public String EPart_100G_TPN_Card						= "APC-100G13/E-SE0";   
	public String EPart_100G_TPN_Card_CGC					= "APC-100G13/E-SE0";   
	public String EPart_100G_MUXPONDER_CARD_CGX				= "APC-CGXH14/E-SE0";
	public String EPart_1x16_OTDR_8D_CDC					= "APC-FFLK12/O-AE0";
	public String EPart_1x16_OPM_8D_CDC						= "APC-FFLK12/O-BE0";
	public String EPart_Hybrid_Raman_n_EDFA_card_8D			= "APC-FFLK12/O-DE0";
	public String EPart_Odd_Mux_Demux_Unit_2D_TE_n_ROADM	= "ASM-OCDOMDUX-000";
	public String EPart_Even_Mux_Demux_Unit_2D_TE_n_ROADM	= "ASM-OCDEMDUX-000";
	public String EPart_2x2_YCable_Unit						= "ASM-OC22YCAM-000";
	public String EPart_DCM_Unit							= "ASM-OCDCM80M-000";

	public String EPart_LC_APC_to_FC_UPC_20m					= "RCA-CAFOPC00-095";
	public String EPart_LC_APC_to_LC_UPC_01m					= "RCA-CAFOPC00-099";
	public String EPart_LC_APC_to_LC_UPC_02m					= "RCA-CAFOPC00-078";
	public String EPart_LC_APC_to_LC_UPC_03m					= "RCA-CAFOPC00-079";
	public String EPart_LC_APC_to_LC_UPC_10m					= "RCA-CAFOPC00-110";

	public String EPart_LC_UPC_to_LC_UPC_01m					= "RCA-CAFOPC00-100";
	public String EPart_LC_UPC_to_LC_UPC_02m					= "RCA-CAFOPC00-089";
	public String EPart_LC_UPC_to_LC_UPC_03m					= "RCA-CAFOPC00-090";
	public String EPart_LC_UPC_to_LC_UPC_10m					= "RCA-CAFOPC00-109";    

	public String EPart_LC_APC_to_LC_APC_01m					= "RCA-CAFOPC00-101";
	public String EPart_LC_APC_to_LC_APC_02m					= "RCA-CAFOPC00-084";
	public String EPart_LC_APC_to_LC_APC_03m					= "RCA-CAFOPC00-085";
	public String EPart_LC_APC_to_LC_APC_10m					= "RCA-CAFOPC00-108";

	public String EPart_LC_APC_to_LC_APC_point2m				= "RCA-CAFOPC00-128";    
	public String EPart_LC_UPC_to_FC_UPC_03m					= "RCA-CAFOPC00-103";
	public String EPart_LC_UPC_to_FC_UPC_20m					= "RCA-CAFOPC00-123";

	public String EPart_E2000_UPC_to_LC_UPC_10m					= "RCA-CAFOPC00-132";
	public String EPart_LC_APC_to_LC_APC_point_5m				= "RCA-CAFOPC00-83";

	public String EPart_CAT6_With_RJ45_10m					= "RCA-CACAT610-001";
	public String EPart_CAT6_With_RJ45_3m					= "RCA-CACAT603-000";
	public String EPart_Power_Cable_Exchange1_TE_PSU_Red	= "ACB-OCTE1PSR-000";
	public String EPart_Power_Cable_Exchange2_TE_PSU_Red	= "ACB-OCTE2PSR-000";
	public String EPart_Power_Cable_Exchange1_TE_PSU_Blue	= "ACB-OCTE1PSB-000";
	public String EPart_Power_Cable_Exchange2_TE_PSU_Blue	= "ACB-OCTE2PSB-000";
	public String EPart_Power_Cable_Exchange1_TE_EarthBusBar_Black	= "ACB-OCTE1EBK-000";
	// public String EPart_Power_Cable_Exchange2_TE_EarthBusBar_Black	= "ACB-OCTE2EBK-000";

	public String EPart_TE_Main_Rack								= "ASU-OCDTEMRX-000";
	public String EPart_Muxponder_Rack								= "ASU-OCDTPNRX-000";
	public String EPart_ILA_Rack									= "ASU-OCDILAMX-000";
	public String EPart_ROADM_Main_Rack_2Degree						= "ASU-OCD2OADM-000"; 
	public String EPart_ROADM_Main_Rack_8Degree						= "ASU-OCD8OADM-000";
	public String EPart_100G_Regenerator							= "APC-REGN13/E-SE0"; 
	public String EPart_Voip_Card									= "APC-WOSL42/O-SE11"; //as supy and voip part code were same for modified the partcode of voip card
	public String EPart_Mid_Stage_amplifier							= "APC-MAAK13/O-AE0";
	public String EPart_Tunable_Filter_Card							= "APC-MAAK13/O-BE0";
	public String EPart_SFP_Multirate_40Km							= "LOX-S1672BCL-F5L";
	public String EPart_SFP_Multirate_80Km							= "LOX-S1871BCL-F5L";
	public String EPart_SFP_Electrical_40Km							= "LOX-S10SERFA-F5L";//used for ethernet service
	public String EPart_SFP_Electrical_80Km							= "LOX-S10SZRFB-F5L";
	public String EPart_SFP_Electrical_SCM							= "LNX-C8522P2B-PJE";   
	public String EPart_CardReserved								= "Partcode-ReservedSlot"; 
	public String EPart_10G_Regenerator								= "APC-XGMH16/C-BE1"; 
	public String EPart_100G_MUXPONDER_CARD_OPX						= "APC-OPXH14/E-SE0"; 
	public String EPart_100G_MUXPONDER_CARD_OPX_CGX					= "APC-OPXH14/E-SZ0"; //Debug
	public String EPart_Pot_Free_Cable	    						= "ACB-OCNTPSCH-000";//CSCC to BayTop
	public String EPart_FillerPlate									= "MAM-TBFPA000-201"; 
	public String EPart_CardXgm										= "Partcode-Xgm";

	public String EPart_CardOtdr1X2									= "APC-RMNK17/Q-AE0";
	public String EPart_CardOtdr1X4									= "APC-RMNK17/Q-BE0";
	public String EPart_CardOtdr1X8									= "APC-RMNK17/Q-CE0";
	public String EPart_CardOtdr1X16								= "APC-RMNK17/Q-DE0";

	public String EPart_CardRamanHybrid								= "APC-RMNK17/Q-IE0";
	public String EPart_CardRamanDra								= "APC-RMNK17/Q-JE0";
	public String EPart_Wss8X12										= "Partcode-Wss8X12";

	public String EPartIlaRamanHybridCard							= "APC-RMNK17/Q-KE0";
	public String EPartIlaRamanDraCard								= "APC-RMNK17/Q-LE0";
	/**
	 * CardFeatures
	 */

	public String CardFeatureWSS							= "WSS";
	public String CardFeatureMPN							= "MPN";

	/**
	 * Ports
	 */    
	public String Field										="FDF";
	public String ToNode									="ToNode";
	public String FromNode									="FromNode";
	public String PaBA_FromField							="PRx";
	public String PaBA_ToField								="BTx";
	public String PaBA_SupyAdd								="SAD";
	public String PaBA_SupyDrop								="SDP";
	public String PaBA_PTx									="PTx";
	public String PaBA_FromWss								="BRx";
	public String PaBA_ToOcm								="PTM";
	public String PaBA_FromOcm								="BTM";

	public String Wss_ToEdfa								="Tx";
	public String Wss_FromEdfa								="Rx";
	public String Wss_ToWss								    ="WXP";
	public String Wss_FromWss								="WRX";
	public String Wss_ToWss_CDC								="Wss Tx";
	public String Wss_FromWss_CDC							="Wss Rx";
	public String Wss_RC								    ="WRC";
	public String Wss_SIN								    ="SIN";
	public String Wss_TC									="WTC";
	public String Wss_TX									="WTX";
	public String Wss_AD									="WAD";
	public String Wss_DP									="WDP";


	public String Edfa_ToMcs								="DropTx";
	public String Edfa_FromMcs								="AddReceive";

	public String Edfa_FromWss								="AddTx";
	public String Edfa_ToWss								="DropRx";

	public String Mcs_ToWorkingTpn							="DropChannel_Port";
	public String Mcs_FromWorkingTpn						="AddChannel_Port";
	public String Mcs_ToProtectionTpn						="ToProtectionTpn";
	public String Mcs_FromProtectionTpn						="FromProtectionTpn";
	public String Mcs_FromEdfa								="DropDir";
	public String Mcs_ToEdfa								="AddDirections";

	public String YCable_ToWorkingTpn						="ToWorkingTpn";
	public String YCable_FromWorkingTpn						="FromWorkingTpn";
	public String YCable_ToProtectionTpn					="ToProtectionTpn";
	public String YCable_FromProtectionTpn					="FromProtectionTpn";
	public String YCable_FromClient							="FromClient";
	public String YCable_ToClient							="ToClient";

	public String Supy_Tx									="Tx";
	public String Supy_Rx									="Rx";

	public String Ocm_Btm									="BTM";
	public String Ocm_Ptm									="PTM";
	public String Ocm_Itm									="ITM";
	public String Ocm_Mtm									="MTM";
	public String Ocm_Dtm									="DTM";
	public String Ocm_Tap									="Tap";

	public String Mpn_Rx									="Rx";
	public String Mpn_Tx									="Tx";

	public String Cscc_ToMpc								="ToMpc";
	public String Cscc_ToLct								="ToLct";
	public String Cscc_ToEms								="ToEms";
	public String Mpc_FromCscc								="FromCscc";
	public String Cscc_ToBayTop1							="ToBayTop1";
	public String Cscc_ToBayTop2							="ToBayTop2";

	public String Fabric_Port 								="Fabric Port";
	public String Base_Port1 								="B1";
	public String Base_Port2 								="B2";

	public String Ila_Irx									="Irx";
	public String Ila_Itx									="Itx";
	public String Ila_Itm									="ITM";
	public String Ila_SupyAdd								="SAD";
	public String Ila_SupyDrop								="SDP";

	public String Ila_Atx									="Atx";
	public String Ila_Crx									="Crx";

	public String MidSAmp_Mtm								="MTM";
	public String MidSAmp_Mrx								="MRX";
	public String MidSAmp_Mtx								="MTX";
	public String MidSAmp_Tx								="TX";
	public String MidSAmp_Rx								="RX";

	public String DCM_Drx									="DRX";
	public String DCM_Dtx									="DTX";

	public String MUX_op									="M_OP";
	public String DEMUX_In									="DM_IN";
	public String MUX_Tap									="MUX Tap";
	public String DEMUX_Tap									="DEMUX Tap";
	public String ODD_MUX									="ODD-O";
	public String ODD_DEMUX									="ODD-I";
	public String EVEN_MUX									="ODD-IN";
	public String EVEN_DEMUX								="ODD-OP";
	public String PRIMARY_LINE								="PRI";
	public String SECONDARY_LINE							="SEC";
	public String LINE										="Line";
	public int PRIMARY_LINE_PORT							=101;
	public int SECONDARY_LINE_PORT							=102;
	public int OLP_PRIMARY_LINE_PORT						=0;
	public int OLP_SECONDARY_LINE_PORT						=1;

	/**
	 * Sbrack Proximity 
	 */

	public String sameSbr								="sameSbrack";
	public String farRack								="farRack";
	public String farSbr								="farSbrack";
	public String adjacentSbr							="adjacentSbrack";

	/**
	 * Apmlifier Types
	 */
	public static String AmpPaBa			=	ResourcePlanConstants.CardPaBa;
	public static String AmpIla				=	ResourcePlanConstants.CardIla;
	public static String AmpIlaRamanHybrid	=	ResourcePlanConstants.CardIlaRamanHybrid;
	public static String AmpIlaRamanDra		=	ResourcePlanConstants.CardIlaRamanDra;
	public static String AmpEdfa			=	ResourcePlanConstants.CardEdfa;
	public static String AmpRamanHybrid		=	ResourcePlanConstants.CardRamanHybrid;
	public static String AmpRamanDra		=	ResourcePlanConstants.CardRamanDra;


	/**
	 * Link Types
	 */
	public static String PaBaLink			=	"DEFAULT (PA/BA)";
	public static String RamanHybridLink	=	ResourcePlanConstants.CardRamanHybrid;
	public static String RamanDraLink		=	ResourcePlanConstants.CardRamanDra;


	/*Client Protection Types*/
	public static String YCableProtection = MapConstants.YCableProtection;
	public static String OLPProtection =MapConstants.OLPProtection;
	public static String OPXProtection 	=MapConstants.CardTypeOPX;

	public static String CardTypeYCable =MapConstants.CardTypeYCable;
	public static String CardTypeOPX 	=MapConstants.CardTypeOPX;

	/**
	 * Max Ports on Controller cards
	 */

	public static int MaxPortCscc			=4;
	public static int MaxPortMpc			=4;

	/**
	 * Circuit Service Types
	 */
	public static String EthernetService 	= "Ethernet";    
	public static String ScmSfp 			= "ScmSfp";

	public String DatabasePrintStr			= "Database";

	/***************************************************
	 * Equipment Preferences
	 *************************************************/

	/**
	 * Equipment Category
	 */
	public String CatMpn100G					= "MPN100G";
	public String CatMpn100GOPX					= "MPN100G-OPX";
	public String CatMpn10G						= "MPN10G";
	public String CatTpn100G					= "TPN100G";
	public String CatTpn10G						= "TPN10G";
	public String CatWss						= "WSS";
	public String CatYCable						= "YCABLE";
	public String CatCscc						= "CSCC";
	public String CatMpc						= "MPC";
	public String CatSfpEthernet				= "SFPETH";
	public String CatSfpNonEthernet				= "SFPNONETH";
	public String CatDegree						= "DEGREE";
	public String CatAmplifier					= "AMPLIFIER";
	public String CatAddDropWssCdRoadm			= "ADD/DROP WSS";
	public String CatChassisType				= "CHASSIS TYPE";
	public String CatOtdr						= "OTDR";
	public String CatOsc						= "OSC";
	public String CatOpm						= "OPM";


	public String[] CategoryList				= {ResourcePlanConstants.CatMpn100G, ResourcePlanConstants.CatMpn10G, ResourcePlanConstants.CatTpn100G, ResourcePlanConstants.CatTpn10G, ResourcePlanConstants.CatWss,ResourcePlanConstants.CatYCable,ResourcePlanConstants.CatCscc,ResourcePlanConstants.CatMpc,ResourcePlanConstants.CatSfpEthernet,ResourcePlanConstants.CatSfpNonEthernet,ResourcePlanConstants.CatDegree
			,ResourcePlanConstants.CatMpn100GOPX,ResourcePlanConstants.CatAmplifier,ResourcePlanConstants.CatAddDropWssCdRoadm,ResourcePlanConstants.CatChassisType,ResourcePlanConstants.CatOsc};

	/**
	 * Card Preference List in order of generic preference
	 */
	public String[] CatMpn100GList				= {ResourcePlanConstants.CardMuxponderCGX,ResourcePlanConstants.CardMuxponderCGM};
	public String[] CatMpn100GOPXList			= {ResourcePlanConstants.CardMuxponderOPXCGX,ResourcePlanConstants.CardMuxponderOPX};
	//TPN CGC : 28/2/18
	public String[] CatTpn100GList				= {ResourcePlanConstants.CardTPN100GCGC};
	public String[] CatTpn10GList				= {ResourcePlanConstants.CardTPN5x10G, ResourcePlanConstants.CatTpn10G};
	public String[] CatWssList					= {ResourcePlanConstants.CardWss1x9,ResourcePlanConstants.CardWss1x2, ResourcePlanConstants.CardWss1x2x20};
	public String[] CatYCableList				= {ResourcePlanConstants.YCable1x2Unit, ResourcePlanConstants.YCable2x2Unit};
	public String[] CatCsccList					= {ResourcePlanConstants.CardCscc};
	public String[] CatMpcList					= {ResourcePlanConstants.CardMpc};
	public String[] CatSfpEthernetList			= {ResourcePlanConstants.SFP_Electrical_80Km,ResourcePlanConstants.SFP_Electrical_40Km};
	public String[] CatSfpNonEthernetList		= {ResourcePlanConstants.SFP_Multirate_80Km,ResourcePlanConstants.SFP_Multirate_40Km};
	public String[] CatDegreeList				= {"One Degree Higher", "Two Degree Higher", "Three Degree Higher"};
	public String[] CatAmplifierList			= {ResourcePlanConstants.AmpPaBa,ResourcePlanConstants.AmpRamanHybrid,ResourcePlanConstants.AmpRamanDra};
	public String[] CatAddDropWssCdRoadmList	= {ResourcePlanConstants.AddDropTwoCardSet,ResourcePlanConstants.AddDropSingleCardSet};
	public String[] CatChassisTypeList			= {ResourcePlanConstants.EmersionChassis,ResourcePlanConstants.ComtelChassis,ResourcePlanConstants.PentairChassis,ResourcePlanConstants.SixSlotChassis};
	public String[] CatOtdrList					= {ResourcePlanConstants.None,ResourcePlanConstants.CatOtdr};
	public String[] CatOscList					= {ResourcePlanConstants.CardSupy,ResourcePlanConstants.None};
	public String[] CatOpmList					= {ResourcePlanConstants.CatOpm,ResourcePlanConstants.None};

	public int PrefOne							= 1;
	public int PrefTwo							= 2;
	public int PrefThree						= 3;

	/**
	 * Parameters for Parametric Preference
	 */

	public String ParamDemand					= "Demand(W)";
	public String ParamDemandPtc				= "Demand(P)";
	public String ParamDirection				= "Direction";
	public String ParamCircuit					= "Circuit";
	public String ParamSubrack					= "Rack-Subrack";


	/**
	 * Allocation Exceptions
	 */

	public String ExceptionTypeResrv					= "Reservation";

	/**
	 * Wss Max Dir Ports
	 */

	public int MaxSwitchPortsWss2x1x20					= 20;
	public int MaxSwitchPortsWss2x1x9					= 9;
	public int MaxSwitchPortsMcs						= 16;
	public int MaxSwitchPortsWss2x8x12					= 12;


	public int WssSetOneLevelOneDir					=101;
	public int WssSetOneLevelTwoDir					=201;
	public int WssSetTwoLevelOneDir					=102;
	public int WssSetTwoLevelTwoDir					=202;
	public int WssSetThreeLevelOneDir				=103;
	public int WssSetThreeLevelTwoDir				=203;
	public int WssSetFourLevelOneDir				=104;
	public int WssSetFourLevelTwoDir				=204;  

	public static final Map<Integer, Integer> MpnPairSlotsEmersionChassis =
			Collections.unmodifiableMap(new HashMap<Integer, Integer>()
			{
				{
					put(ResourcePlanConstants.ONE,ResourcePlanConstants.FOUR);
					put(ResourcePlanConstants.TWO,ResourcePlanConstants.FIVE);
					put(ResourcePlanConstants.THREE,ResourcePlanConstants.SEVEN);
					put(ResourcePlanConstants.EIGHT,ResourcePlanConstants.TWELVE);
					put(ResourcePlanConstants.TEN,ResourcePlanConstants.THIRTEEN);
					put(ResourcePlanConstants.ELEVEN,ResourcePlanConstants.FOURTEEN);

				}
			}); 

	public static final Map<Integer, Integer> MpnPairSlotsComtelPentairChassis =
			Collections.unmodifiableMap(new HashMap<Integer, Integer>()
			{
				{
					put(ResourcePlanConstants.ONE,ResourcePlanConstants.THREE);
					put(ResourcePlanConstants.TWO,ResourcePlanConstants.FOUR);
					put(ResourcePlanConstants.FIVE,ResourcePlanConstants.NINE);
					put(ResourcePlanConstants.SIX,ResourcePlanConstants.TEN);
					put(ResourcePlanConstants.ELEVEN,ResourcePlanConstants.THIRTEEN);
					put(ResourcePlanConstants.TWELVE,ResourcePlanConstants.FOURTEEN);
				}
			}); 

}
