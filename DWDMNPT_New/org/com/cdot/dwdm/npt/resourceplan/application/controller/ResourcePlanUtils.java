package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;

import com.mysql.fabric.xmlrpc.base.Array;

import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;
import application.model.CardPreference;
import application.model.Demand;
import application.model.Equipment;
import application.model.EquipmentPreference;
import application.model.Link;
import application.model.NetworkRoute;
import application.model.Node;
import application.model.NodeSystem;
import application.model.PortInfo;
import application.model.Topology;
import application.service.DbService;

/**
 * 
 * @author sunaina
 * @brief this contains general utility functions used while resource planning
 *
 */
public class ResourcePlanUtils {
	DbService dbService;
	public static Logger logger = Logger.getLogger(ResourcePlanUtils.class.getName());

	public DbService getDbService() {
		return dbService;
	}

	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public ResourcePlanUtils(DbService dbService) {
		super();
		this.dbService = dbService;
	} 

	/**
	 * Function generates the cardtype for rack view generation corresponding to equipment id
	 * @param Eid
	 * @return
	 */
	public String fgetCardTypeFromEId(int Eid)
	{
		String cardtype="";
		switch(Eid)
		{
		case ResourcePlanConstants.EIdCscc:
		{
			cardtype = ResourcePlanConstants.CardCscc;
		}break;		
		case ResourcePlanConstants.EIdMpc:
		{
			cardtype = ResourcePlanConstants.CardMpc;
		}break;	
		case ResourcePlanConstants.EIdMpnCgm:
		{
			cardtype = ResourcePlanConstants.CardMuxponderCGM;
		}break;	
		case ResourcePlanConstants.EId_200G_MPN_Card:
		{
			cardtype = ResourcePlanConstants.CardMuxponder200G;
		}break;	
		case ResourcePlanConstants.EId_100G_MUXPONDER_CARD_OPX:
		{
			cardtype = ResourcePlanConstants.CardMuxponderOPX;
		}break;	
		case ResourcePlanConstants.EId_100G_MUXPONDER_CARD_OPX_CGX:
		{
			cardtype = ResourcePlanConstants.CardMuxponderOPXCGX;
		}break;	
		//			case ResourcePlanConstants.EId_100G_TPN_Card:
		//			{
		//				cardtype = ResourcePlanConstants.CardTPN100G;
		//			}break;		
		case ResourcePlanConstants.EId_100G_TPN_Card_CGC:
		{
			cardtype = ResourcePlanConstants.CardTPN100GCGC;
		}break;		
		case ResourcePlanConstants.EIdOlp:
		{
			cardtype = ResourcePlanConstants.CardOlp;
		}break;
		case ResourcePlanConstants.EIdPaBa:
		{
			cardtype = ResourcePlanConstants.CardPaBa;
		}break;
		case ResourcePlanConstants.EIdWss1x9:
		{
			cardtype = ResourcePlanConstants.CardWss1x9;
		}break;
		case ResourcePlanConstants.EIdSupy:
		{
			cardtype = ResourcePlanConstants.CardSupy;
		}break;
		case ResourcePlanConstants.EIdMcs:
		{
			cardtype = ResourcePlanConstants.CardMcs;
		}break;
		case ResourcePlanConstants.EId1x2_OPM_Card_2D_ILA:
		{
			cardtype = ResourcePlanConstants.CardOcm1x2;
		}break;
		case ResourcePlanConstants.EId1x8_OPM_Card_2D_TE_n_2D_ROADM:
		{
			cardtype = ResourcePlanConstants.CardOcm1x8;
		}break;			
		case ResourcePlanConstants.EIdEdfa:
		{
			cardtype = ResourcePlanConstants.CardEdfa;
		}break;				
		case ResourcePlanConstants.EIdIlaCard:
		{
			cardtype = ResourcePlanConstants.CardIla;
		}break;			
		case ResourcePlanConstants.EId1x2_WSS_Card_for_2D_TE_n_2D_ROADM:
		{
			cardtype = ResourcePlanConstants.CardWss1x2;
		}break;
		case ResourcePlanConstants.EId_Twin_1x20_WSS_Card_8D_CDC:
		{
			cardtype = ResourcePlanConstants.CardWss1x2x20;
		}break;
		case ResourcePlanConstants.EId_40G_Muxponder_Card:
		{
			cardtype = ResourcePlanConstants.CardMuxponder40G;
		}break;
		case ResourcePlanConstants.EId_10G_MPN_Card:
		{
			cardtype = ResourcePlanConstants.CardMuxponder10G;
		}break;
		case ResourcePlanConstants.EId_10G_TPN_Card:
		{
			cardtype = ResourcePlanConstants.CardTPN5x10G;
		}break;
		case ResourcePlanConstants.EId_100G_MUXPONDER_CARD_CGX:
		{
			cardtype = ResourcePlanConstants.CardMuxponderCGX;
		}break;
		case ResourcePlanConstants.EId_1x16_OPM_8D_CDC:
		{
			cardtype = ResourcePlanConstants.CardOcm1x16;
		}break;
		case ResourcePlanConstants.EId_Hybrid_Raman_n_EDFA_card_8D:
		{
			cardtype = ResourcePlanConstants.CardEdfa;
		}break;				
		case ResourcePlanConstants.EId_LC_APC_to_FC_UPC_20m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m;
		}break;
		case ResourcePlanConstants.EId_LC_APC_to_LC_UPC_01m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_UPC_01m;
		}break;
		case ResourcePlanConstants.EId_LC_APC_to_LC_UPC_10m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_UPC_10m;
		}break;
		case ResourcePlanConstants.EId_LC_UPC_to_LC_UPC_01m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_UPC_to_LC_UPC_01m;
		}break;
		case ResourcePlanConstants.EId_LC_UPC_to_LC_UPC_02m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_UPC_to_LC_UPC_02m;
		}break;			
		case ResourcePlanConstants.EId_LC_APC_to_LC_APC_01m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_APC_01m;
		}break;
		case ResourcePlanConstants.EId_LC_APC_to_LC_APC_02m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_APC_02m;
		}break;
		case ResourcePlanConstants.EId_LC_APC_to_LC_APC_03m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_APC_03m;
		}break;
		case ResourcePlanConstants.EId_LC_APC_to_LC_APC_10m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_APC_10m;
		}break;
		case ResourcePlanConstants.EId_LC_APC_to_LC_APC_point2m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m;
		}break;
		case ResourcePlanConstants.EId_LC_APC_to_LC_UPC_02m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_UPC_02m;
		}break;
		case ResourcePlanConstants.EId_LC_APC_to_LC_UPC_03m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_UPC_03m;
		}break;
		case ResourcePlanConstants.EId_LC_UPC_to_FC_UPC_03m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_03m;
		}break;
		case ResourcePlanConstants.EId_LC_UPC_to_FC_UPC_20m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m;
		}break;	
		case ResourcePlanConstants.EId_TE_Main_Rack:
		{
			cardtype = ResourcePlanConstants.TE_Main_Rack;
		}break;
		case ResourcePlanConstants.EId_ILA_Rack:
		{
			cardtype = ResourcePlanConstants.ILA_Rack;
		}break;
		case ResourcePlanConstants.EId_ROADM_Main_Rack_2Degree:
		{
			cardtype = ResourcePlanConstants.ROADM_Main_Rack_2Degree;
		}break;
		case ResourcePlanConstants.EId_Muxponder_Rack:
		{
			cardtype = ResourcePlanConstants.Muxponder_Rack;
		}break;
		case ResourcePlanConstants.EId_ROADM_Main_Rack_8Degree:
		{
			cardtype = ResourcePlanConstants.ROADM_Main_Rack_8Degree;
		}break;
		case ResourcePlanConstants.EIdYCable1x2Unit:
		{
			cardtype = ResourcePlanConstants.YCable1x2Unit;
		}break;
		case ResourcePlanConstants.EId_Odd_Mux_Demux_Unit_2D_TE_n_ROADM:
		{
			cardtype = ResourcePlanConstants.Odd_Mux_Demux_Unit;
		}break;
		case ResourcePlanConstants.EId_Even_Demux_Unit:
		{
			cardtype = ResourcePlanConstants.Even_Demux_Unit;
		}break;
		case ResourcePlanConstants.EId_Even_Mux_Unit:
		{
			cardtype = ResourcePlanConstants.Even_Mux_Unit;
		}break;
		case ResourcePlanConstants.EId_Odd_Demux_Unit:
		{
			cardtype = ResourcePlanConstants.Odd_Demux_Unit;
		}break;
		case ResourcePlanConstants.EId_Odd_Mux_Unit:
		{
			cardtype = ResourcePlanConstants.Odd_Mux_Unit;
		}break;
		case ResourcePlanConstants.EId_CAT6_With_RJ45_10m:
		{
			cardtype = ResourcePlanConstants.PCord_CAT6_With_RJ45_10m;
		}break;
		case ResourcePlanConstants.EId_CAT6_With_RJ45_3m:
		{
			cardtype = ResourcePlanConstants.PCord_CAT6_With_RJ45_3m;
		}break;				
		case ResourcePlanConstants.EId_DCM_Unit:
		{
			cardtype = ResourcePlanConstants.PCord_CAT6_With_RJ45_3m;
		}break;	
		case ResourcePlanConstants.EId_100G_Regenerator:
		{
			cardtype = ResourcePlanConstants.Regenerator100G;
		}break;	
		case ResourcePlanConstants.EId_VoiP_Card:
		{
			cardtype = ResourcePlanConstants.VoiP_Card;
		}break;	
		case ResourcePlanConstants.EId_Mid_Stage_amplifier:
		{
			cardtype = ResourcePlanConstants.Mid_Stage_amplifier;
		}break;	
		case ResourcePlanConstants.EId_Tunable_Filter_Card:
		{
			cardtype = ResourcePlanConstants.Tunable_Filter_Card;
		}break;
		case ResourcePlanConstants.EId_Port_Free_cable:
		{
			cardtype = ResourcePlanConstants.BayTop;
		}break;

		case ResourcePlanConstants.EId_FillerPlate:
		{
			cardtype = ResourcePlanConstants.FillerPlate;
		}break;
		case ResourcePlanConstants.EId_CardOtdr1X2:
		{
			cardtype = ResourcePlanConstants.CardOtdr1X2;
		}break;
		case ResourcePlanConstants.EId_CardOtdr1X4:
		{
			cardtype = ResourcePlanConstants.CardOtdr1X4;
		}break;
		case ResourcePlanConstants.EId_CardOtdr1X8:
		{
			cardtype = ResourcePlanConstants.CardOtdr1X8;
		}break;
		case ResourcePlanConstants.EId_CardOtdr1X16:
		{
			cardtype = ResourcePlanConstants.CardOtdr1X16;
		}break;
		case ResourcePlanConstants.EId_CardRamanHybrid:
		{
			cardtype = ResourcePlanConstants.CardRamanHybrid;
		}break;
		case ResourcePlanConstants.EId_CardRamanDra:
		{
			cardtype = ResourcePlanConstants.CardRamanDra;
		}break;
		case ResourcePlanConstants.EId_Wss8x12:
		{
			cardtype = ResourcePlanConstants.CardWss8x12;
		}break;
		case ResourcePlanConstants.EId_LC_APC_to_LC_APC_point_5m:
		{
			cardtype = ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point_5m;
		}break;
		
		case ResourcePlanConstants.EId_E2000_UPC_to_LC_UPC_10m:
		{
			cardtype = ResourcePlanConstants.PCord_E2000_UPC_to_LC_UPC_10m;
		}break;
		default:
			cardtype="";
			break;			

		}
		return cardtype;
	}

	/**
	 * function to get default mapped Equipmentid to a cardtype
	 * @param cardtype
	 * @return
	 */
	public int fgetEIdFromCardType(String cardtype)
	{
		int id=0;
		switch(cardtype)
		{
		case ResourcePlanConstants.CardMpc:
		{
			id = ResourcePlanConstants.EIdMpc;
		}break;
		case ResourcePlanConstants.CardCscc:
		{
			id = ResourcePlanConstants.EIdCscc;
		}break;
		case ResourcePlanConstants.CardMuxponderCGM:
		{
			id = ResourcePlanConstants.EIdMpnCgm;				
		}break;
		case ResourcePlanConstants.CardOlp:
		{
			id = ResourcePlanConstants.EIdOlp;
		}break;
		case ResourcePlanConstants.CardPaBa:
		{
			id = ResourcePlanConstants.EIdPaBa;
		}break;
		case ResourcePlanConstants.CardWss1x9:
		{
			id = ResourcePlanConstants.EIdWss1x9;				
		}break;
		case ResourcePlanConstants.CardSupy:
		{
			id = ResourcePlanConstants.EIdSupy;
		}break;
		case ResourcePlanConstants.CardMcs:
		{
			id = ResourcePlanConstants.EIdMcs;
		}break;
		case ResourcePlanConstants.CardOcm1x2:
		{
			id = ResourcePlanConstants.EId1x2_OPM_Card_2D_ILA;
		}break;
		case ResourcePlanConstants.CardOcm1x8:
		{
			id = ResourcePlanConstants.EId1x8_OPM_Card_2D_TE_n_2D_ROADM;
		}break;
		case ResourcePlanConstants.CardOcm1x16:
		{
			id = ResourcePlanConstants.EId_1x16_OPM_8D_CDC;
		}break;
		case ResourcePlanConstants.CardEdfa:
		{
			id = ResourcePlanConstants.EIdEdfa;
		}break;				
		case ResourcePlanConstants.CardIla:
		{
			id = ResourcePlanConstants.EIdIlaCard;
		}break;	
		case ResourcePlanConstants.CardIlaRamanHybrid:
		{
			id = ResourcePlanConstants.EIdIlaRamanHybridCard;
		}break;	
		case ResourcePlanConstants.CardIlaRamanDra:
		{
			id = ResourcePlanConstants.EIdIlaRamanDraCard;
		}break;	
		case ResourcePlanConstants.CardWss1x2:
		{
			id = ResourcePlanConstants.EId1x2_WSS_Card_for_2D_TE_n_2D_ROADM;
		}break;
		case ResourcePlanConstants.CardWss1x2x20:
		{
			id = ResourcePlanConstants.EId_Twin_1x20_WSS_Card_8D_CDC;
		}break;
		case ResourcePlanConstants.CardMuxponder40G:
		{
			id = ResourcePlanConstants.EId_40G_Muxponder_Card;
		}break;			
		case ResourcePlanConstants.CardMuxponderCGX:
		{
			id = ResourcePlanConstants.EId_100G_MUXPONDER_CARD_CGX;				
		}break;
		case ResourcePlanConstants.CardMuxponder10G:
		{
			id = ResourcePlanConstants.EId_10G_MPN_Card;				
		}break;
		//			case ResourcePlanConstants.CardTPN100G:
		//			{
		//				id = ResourcePlanConstants.EId_100G_TPN_Card;				
		//			}break;		
		case ResourcePlanConstants.CardTPN100GCGC:
		{
			id = ResourcePlanConstants.EId_100G_TPN_Card_CGC;
		}break;	
		case ResourcePlanConstants.CardTPN5x10G:
		{
			id = ResourcePlanConstants.EId_10G_TPN_Card;
		}break;
		case ResourcePlanConstants.CardMuxponder200G:
		{
			id = ResourcePlanConstants.EId_200G_MPN_Card;				
		}break;	
		case ResourcePlanConstants.CardMuxponderOPX:
		{
			id = ResourcePlanConstants.EId_100G_MUXPONDER_CARD_OPX;				
		}break;
		case ResourcePlanConstants.CardMuxponderOPXCGX:
		{
			id = ResourcePlanConstants.EId_100G_MUXPONDER_CARD_OPX_CGX;				
		}break;
		case ResourcePlanConstants.CardVoip:
		{
			id = ResourcePlanConstants.EId_VoiP_Card;
		}break;
		case ResourcePlanConstants.Odd_Mux_Demux_Unit:
		{
			id = ResourcePlanConstants.EId_Odd_Mux_Demux_Unit_2D_TE_n_ROADM;
		}break;
		case ResourcePlanConstants.Even_Mux_Demux_Unit:
		{
			id = ResourcePlanConstants.EId_Even_Mux_Demux_Unit_2D_TE_n_ROADM;
		}break;
		case ResourcePlanConstants.Odd_Mux_Unit:
		{
			id = ResourcePlanConstants.EId_Odd_Mux_Unit;
		}break;
		case ResourcePlanConstants.Even_Mux_Unit:
		{
			id = ResourcePlanConstants.EId_Even_Mux_Unit;
		}break;
		case ResourcePlanConstants.Odd_Demux_Unit:
		{
			id = ResourcePlanConstants.EId_Odd_Demux_Unit;
		}break;
		case ResourcePlanConstants.Even_Demux_Unit:
		{
			id = ResourcePlanConstants.EId_Even_Demux_Unit;
		}break;
		case ResourcePlanConstants.DCM_Tray_Unit:
		{
			id = ResourcePlanConstants.EId_DCM_Unit;
		}break;
		case ResourcePlanConstants.Regenerator100G:
		{
			id = ResourcePlanConstants.EId_100G_Regenerator;
		}break;
		case ResourcePlanConstants.Regenerator10G:
		{
			id = ResourcePlanConstants.EId_10G_Regenerator;
		}break;
		case ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_APC_point2m;
		}break;
		case ResourcePlanConstants.Mid_Stage_amplifier:
		{
			id = ResourcePlanConstants.EId_Mid_Stage_amplifier;
		}break;
		case ResourcePlanConstants.Tunable_Filter_Card:
		{
			id = ResourcePlanConstants.EId_Tunable_Filter_Card;
		}break;
		case ResourcePlanConstants.CardReserved:
		{
			id = ResourcePlanConstants.EId_CardReserved;
		}break;
		case ResourcePlanConstants.SFP_Electrical_40Km:
		{
			id = ResourcePlanConstants.EId_SFP_Electrical_40Km;
		}break;
		case ResourcePlanConstants.SFP_Electrical_80Km:
		{
			id = ResourcePlanConstants.EId_SFP_Electrical_80Km;
		}break;
		case ResourcePlanConstants.SFP_Multirate_40Km:
		{
			id = ResourcePlanConstants.EId_SFP_Multirate_40Km;
		}break;
		case ResourcePlanConstants.SFP_Multirate_80Km:
		{
			id = ResourcePlanConstants.EId_SFP_Multirate_80Km;
		}break;
		case ResourcePlanConstants.BayTop:
		{
			id = ResourcePlanConstants.EId_Port_Free_cable;
		}break;

		case ResourcePlanConstants.FillerPlate:
		{
			id = ResourcePlanConstants.EId_FillerPlate;
		}break;
		
		case ResourcePlanConstants.CardOtdr1X2:
		{
			id = ResourcePlanConstants.EId_CardOtdr1X2;
		}break;
		case ResourcePlanConstants.CardOtdr1X4:
		{
			id = ResourcePlanConstants.EId_CardOtdr1X4;
		}break;
		case ResourcePlanConstants.CardOtdr1X8:
		{
			id = ResourcePlanConstants.EId_CardOtdr1X8;
		}break;
		case ResourcePlanConstants.CardOtdr1X16:
		{
			id = ResourcePlanConstants.EId_CardOtdr1X16;
		}break;
		case ResourcePlanConstants.CardRamanHybrid:
		{
			id = ResourcePlanConstants.EId_CardRamanHybrid;
		}break;
		case ResourcePlanConstants.CardRamanDra:
		{
			id = ResourcePlanConstants.EId_CardRamanDra;
		}break;
		
		case ResourcePlanConstants.CardWss8x12:
		{
			id = ResourcePlanConstants.EId_Wss8x12;
		}break;
		case ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point_5m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_APC_point_5m;
		}break;
		case ResourcePlanConstants.PCord_E2000_UPC_to_LC_UPC_10m:
		{
			id = ResourcePlanConstants.EId_E2000_UPC_to_LC_UPC_10m;
		}break;

		//			case ResourcePlanConstants.CardEdfa:
		//			{
		//				id = ResourcePlanConstants.EId_Hybrid_Raman_n_EDFA_card_8D;
		//			}break;			
		default:
			id=0;
			break;			

		}
		return id;
	}

	/**
	 * Function generates the Equipment Id on the basis of the Equipment partcode
	 * @param partno
	 * @return
	 */	
	public int fgetEId(String partno)
	{		
		int id = 0;
		switch(partno)
		{
		case ResourcePlanConstants.EPartCscc:
		{
			id = ResourcePlanConstants.EIdCscc;
		}break;
		case ResourcePlanConstants.EPartMpc:
		{
			id = ResourcePlanConstants.EIdMpc;
		}break;
		case ResourcePlanConstants.EPartMpnCgm:
		{
			id = ResourcePlanConstants.EIdMpnCgm;
		}break;
		case ResourcePlanConstants.EPartOlp:
		{
			id = ResourcePlanConstants.EIdOlp;
		}break;
		case ResourcePlanConstants.EPartPaBa:
		{
			id = ResourcePlanConstants.EIdPaBa;
		}break;
		case ResourcePlanConstants.EPartWss1x9:
		{
			id = ResourcePlanConstants.EIdWss1x9;
		}break;
		case ResourcePlanConstants.EPartSupy:
		{
			id = ResourcePlanConstants.EIdSupy;
		}break;
		case ResourcePlanConstants.EPartMcs:
		{
			id = ResourcePlanConstants.EIdMcs;
		}break;
		case ResourcePlanConstants.EPart1x8_OPM_Card_2D_TE_n_2D_ROADM:
		{
			id = ResourcePlanConstants.EId1x8_OPM_Card_2D_TE_n_2D_ROADM;
		}break;
		case ResourcePlanConstants.EPartEdfa:
		{
			id = ResourcePlanConstants.EIdEdfa;
		}break;
		case ResourcePlanConstants.EPartAtcaChassis:
		{
			id = ResourcePlanConstants.EIdAtcaChassis;
		}break;
		case ResourcePlanConstants.EPartSixSlotChassis:
		{
			id = ResourcePlanConstants.EIdSixSlotChassis;
		}break;
		case ResourcePlanConstants.EPartYCable1x2Unit:
		{
			id = ResourcePlanConstants.EIdYCable1x2Unit;
		}break;
		case ResourcePlanConstants.EPartIlaCard:
		{
			id = ResourcePlanConstants.EIdIlaCard;
		}break;
		case ResourcePlanConstants.EPartIlaRamanHybridCard:
		{
			id = ResourcePlanConstants.EIdIlaRamanHybridCard;
		}break;
		case ResourcePlanConstants.EPartIlaRamanDraCard:
		{
			id = ResourcePlanConstants.EIdIlaRamanDraCard;
		}break;
		case ResourcePlanConstants.EPart1x2_OPM_Card_2D_ILA:
		{
			id = ResourcePlanConstants.EId1x2_OPM_Card_2D_ILA;
		}break;
		case ResourcePlanConstants.EPart1x2_WSS_Card_for_2D_TE_n_2D_ROADM:
		{
			id = ResourcePlanConstants.EId1x2_WSS_Card_for_2D_TE_n_2D_ROADM;
		}break;
		case ResourcePlanConstants.EPart_Twin_1x20_WSS_Card_8D_CDC:
		{
			id = ResourcePlanConstants.EId_Twin_1x20_WSS_Card_8D_CDC;
		}break;
		case ResourcePlanConstants.EPart_40G_Muxponder_Card:
		{
			id = ResourcePlanConstants.EId_40G_Muxponder_Card;
		}break;
		case ResourcePlanConstants.EPart_10G_TPN_Card:
		{
			id = ResourcePlanConstants.EId_10G_TPN_Card;
		}break;
		case ResourcePlanConstants.EPart_10G_MPN_Card:
		{
			id = ResourcePlanConstants.EId_10G_MPN_Card;
		}break;
		//			case ResourcePlanConstants.EPart_100G_TPN_Card:
		//			{
		//				id = ResourcePlanConstants.EId_100G_TPN_Card;
		//			}break;			
		case ResourcePlanConstants.EPart_100G_TPN_Card_CGC:
		{
			id = ResourcePlanConstants.EId_100G_TPN_Card_CGC;
		}break;	
		case ResourcePlanConstants.EPart_200G_MPN_Card:
		{
			id = ResourcePlanConstants.EId_200G_MPN_Card;
		}break;
		case ResourcePlanConstants.EPart_100G_MUXPONDER_CARD_OPX:
		{
			id = ResourcePlanConstants.EId_100G_MUXPONDER_CARD_OPX;
		}break;
		case ResourcePlanConstants.EPart_100G_MUXPONDER_CARD_OPX_CGX:
		{
			id = ResourcePlanConstants.EId_100G_MUXPONDER_CARD_OPX_CGX;
		}break;
		case ResourcePlanConstants.EPart_100G_MUXPONDER_CARD_CGX:
		{
			id = ResourcePlanConstants.EId_100G_MUXPONDER_CARD_CGX;
		}break;
		case ResourcePlanConstants.EPart_1x16_OPM_8D_CDC:
		{
			id = ResourcePlanConstants.EId_1x16_OPM_8D_CDC;
		}break;
		case ResourcePlanConstants.EPart_Hybrid_Raman_n_EDFA_card_8D:
		{
			id = ResourcePlanConstants.EId_Hybrid_Raman_n_EDFA_card_8D;
		}break;
		case ResourcePlanConstants.EPart_Odd_Mux_Demux_Unit_2D_TE_n_ROADM:
		{
			id = ResourcePlanConstants.EId_Odd_Mux_Demux_Unit_2D_TE_n_ROADM;
		}break;
		case ResourcePlanConstants.EPart_Even_Mux_Demux_Unit_2D_TE_n_ROADM:
		{
			id = ResourcePlanConstants.EId_Even_Mux_Demux_Unit_2D_TE_n_ROADM;
		}break;

		case ResourcePlanConstants.EPart_2x2_YCable_Unit:
		{
			id = ResourcePlanConstants.EId_2x2_YCable_Unit;
		}break;
		case ResourcePlanConstants.EPart_DCM_Unit:
		{
			id = ResourcePlanConstants.EId_DCM_Unit;
		}break;			
		case ResourcePlanConstants.EPartPowerSupplyUnit:
		{
			id = ResourcePlanConstants.EIdPowerSupplyUnit;
		}break;
		case ResourcePlanConstants.EPart_1x16_OTDR_8D_CDC:
		{
			id = ResourcePlanConstants.EId_1x16_OTDR_8D_CDC;
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_FC_UPC_20m;
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_UPC_01m;
		}break;
		case ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m:
		{
			id = ResourcePlanConstants.EId_LC_UPC_to_LC_UPC_01m;
		}break;
		case ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_02m:
		{
			id = ResourcePlanConstants.EId_LC_UPC_to_LC_UPC_02m;
		}break;
		case ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_03m:
		{
			id = ResourcePlanConstants.EId_LC_UPC_to_LC_UPC_03m;
		}break;		
		case ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_10m:
		{
			id = ResourcePlanConstants.EId_LC_UPC_to_LC_UPC_10m;
		}break;					
		case ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_APC_01m;
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_LC_APC_02m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_APC_02m;
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_LC_APC_03m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_APC_03m;
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_LC_APC_10m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_APC_10m;
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point2m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_APC_point2m;
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_02m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_UPC_02m;
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_03m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_UPC_03m;
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_10m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_UPC_10m;
		}break;
		case ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_03m:
		{
			id = ResourcePlanConstants.EId_LC_UPC_to_FC_UPC_03m;
		}break;
		case ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m:
		{
			id = ResourcePlanConstants.EId_LC_UPC_to_FC_UPC_20m;
		}break;
		case ResourcePlanConstants.EPart_CAT6_With_RJ45_10m:
		{
			id = ResourcePlanConstants.EId_CAT6_With_RJ45_10m;
		}break;
		case ResourcePlanConstants.EPart_CAT6_With_RJ45_3m:
		{
			id = ResourcePlanConstants.EId_CAT6_With_RJ45_3m;
		}break;
		case ResourcePlanConstants.EPart_Power_Cable_Exchange1_TE_PSU_Red:
		{
			id = ResourcePlanConstants.EId_Power_Cable_Exchange1_TE_PSU_Red;
		}break;
		case ResourcePlanConstants.EPart_Power_Cable_Exchange2_TE_PSU_Red:
		{
			id = ResourcePlanConstants.EId_Power_Cable_Exchange2_TE_PSU_Red;
		}break;
		case ResourcePlanConstants.EPart_Power_Cable_Exchange1_TE_PSU_Blue:
		{
			id = ResourcePlanConstants.EId_Power_Cable_Exchange1_TE_PSU_Blue;
		}break;
		case ResourcePlanConstants.EPart_Power_Cable_Exchange2_TE_PSU_Blue:
		{
			id = ResourcePlanConstants.EId_Power_Cable_Exchange2_TE_PSU_Blue;
		}break;
		case ResourcePlanConstants.EPart_Power_Cable_Exchange1_TE_EarthBusBar_Black:
		{
			id = ResourcePlanConstants.EId_Power_Cable_Exchange1_TE_EarthBusBar_Black;
		}break;
//		case ResourcePlanConstants.EPart_Power_Cable_Exchange2_TE_EarthBusBar_Black:
//		{
//			id = ResourcePlanConstants.EId_Power_Cable_Exchange2_TE_EarthBusBar_Black;
//		}break;				
		case ResourcePlanConstants.EPart_TE_Main_Rack:
		{
			id = ResourcePlanConstants.EId_TE_Main_Rack;
		}break;
		case ResourcePlanConstants.EPart_Muxponder_Rack:
		{
			id = ResourcePlanConstants.EId_Muxponder_Rack;
		}break;
		case ResourcePlanConstants.EPart_ILA_Rack:
		{
			id = ResourcePlanConstants.EId_ILA_Rack;
		}break;
		case ResourcePlanConstants.EPart_ROADM_Main_Rack_2Degree:
		{
			id = ResourcePlanConstants.EId_ROADM_Main_Rack_2Degree;
		}break;			
		case ResourcePlanConstants.EPart_ROADM_Main_Rack_8Degree:
		{
			id = ResourcePlanConstants.EId_ROADM_Main_Rack_8Degree;
		}break;
		case ResourcePlanConstants.EPart_100G_Regenerator:
		{
			id = ResourcePlanConstants.EId_100G_Regenerator;
		}break;
		case ResourcePlanConstants.EPart_10G_Regenerator:
		{
			id = ResourcePlanConstants.EId_10G_Regenerator;
		}break;
		case ResourcePlanConstants.EPart_Voip_Card:
		{
			id = ResourcePlanConstants.EId_VoiP_Card;
		}break;
		case ResourcePlanConstants.EPart_Mid_Stage_amplifier:
		{
			id = ResourcePlanConstants.EId_Mid_Stage_amplifier;
		}break;
		case ResourcePlanConstants.EPart_Tunable_Filter_Card:
		{
			id = ResourcePlanConstants.EId_Tunable_Filter_Card;
		}break;
		case ResourcePlanConstants.EPart_SFP_Electrical_40Km:
		{
			id = ResourcePlanConstants.EId_SFP_Electrical_40Km;
		}break;
		case ResourcePlanConstants.EPart_SFP_Electrical_80Km:
		{
			id = ResourcePlanConstants.EId_SFP_Electrical_80Km;
		}break;
		case ResourcePlanConstants.EPart_SFP_Multirate_40Km:
		{
			id = ResourcePlanConstants.EId_SFP_Multirate_40Km;
		}break;
		case ResourcePlanConstants.EPart_SFP_Multirate_80Km:
		{
			id = ResourcePlanConstants.EId_SFP_Multirate_80Km;
		}break;
		case ResourcePlanConstants.EPart_SFP_Electrical_SCM:
		{
			id = ResourcePlanConstants.EId_SFP_Electrical_SCM;
		}break;
		case ResourcePlanConstants.EPart_CardReserved:
		{
			id = ResourcePlanConstants.EId_CardReserved;
		}break;
		case ResourcePlanConstants.EPart_Pot_Free_Cable:
		{
			id=ResourcePlanConstants.EId_Port_Free_cable;
		}break;
		case ResourcePlanConstants.EPart_FillerPlate:
		{
			id = ResourcePlanConstants.EId_FillerPlate;
		}break;
		
		case ResourcePlanConstants.EPart_CardOtdr1X2:
		{
			id = ResourcePlanConstants.EId_CardOtdr1X2;
		}break;
		case ResourcePlanConstants.EPart_CardOtdr1X4:
		{
			id = ResourcePlanConstants.EId_CardOtdr1X4;
		}break;
		case ResourcePlanConstants.EPart_CardOtdr1X8:
		{
			id = ResourcePlanConstants.EId_CardOtdr1X8;
		}break;
		case ResourcePlanConstants.EPart_CardOtdr1X16:
		{
			id = ResourcePlanConstants.EId_CardOtdr1X16;
		}break;
		
		case ResourcePlanConstants.EPart_CardRamanHybrid:
		{
			id = ResourcePlanConstants.EId_CardRamanHybrid;
		}break;
		
		case ResourcePlanConstants.EPart_CardRamanDra:
		{
			id = ResourcePlanConstants.EId_CardRamanDra;
		}break;
		
		case ResourcePlanConstants.EPart_Wss8X12:
		{
			id = ResourcePlanConstants.EId_Wss8x12;
		}break;

		case ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m:
		{
			id = ResourcePlanConstants.EId_LC_APC_to_LC_APC_point_5m;
		}break;
		
		case ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m:
		{
			id = ResourcePlanConstants.EId_E2000_UPC_to_LC_UPC_10m;
		}break;
		default:
			id=0;
			break;			
		}
		return id;
	}


	/**
	 * gets the Wss cardtype and Equipment id to be placed  
	 * @param nodedegree
	 * @return
	 */
	public Object[] fgetWssPreference(int networkid, int nodeid, int nodedegree)
	{
		Object[] data = null; 
		String Cardtype = null;
		int Eid=0;
		Object Eqid = null;

		try 
		{
			Eqid=dbService.getCardPreferenceService().FindPreferenceByFeature(networkid, nodeid, ResourcePlanConstants.CardFeatureWSS);
		} catch (SQLException e) 
		{			
			e.printStackTrace();
		}

		if(Eqid!=null)
		{
			Eid = Integer.parseInt(Eqid.toString());
			Cardtype = fgetCardTypeFromEId(Eid);
		}
		else
		{	
			Node n = dbService.getNodeService().FindNode(networkid, nodeid);
			if(n.getNodeType()==MapConstants.roadm)
			{
				Cardtype=ResourcePlanConstants.CardWss1x9;
			}
			else if(n.getNodeType()==MapConstants.twoBselectRoadm)
			{
				Cardtype=ResourcePlanConstants.CardWss1x2;
			}
			else if(n.getNodeType()==MapConstants.te)
			{
				Cardtype=ResourcePlanConstants.CardWss1x2;
			}
			//			switch(nodedegree)
			//			{
			//				case ResourcePlanConstants.ONE:
			//				case ResourcePlanConstants.TWO:
			//				{
			//					Cardtype=ResourcePlanConstants.CardWss1x2;
			//				}
			//				break;
			//				case ResourcePlanConstants.THREE:
			//				case ResourcePlanConstants.FOUR:
			//				case ResourcePlanConstants.FIVE:
			//				case ResourcePlanConstants.SIX:
			//				case ResourcePlanConstants.SEVEN:
			//				case ResourcePlanConstants.EIGHT:
			//				{
			//					Cardtype=ResourcePlanConstants.CardWss1x9;
			//				}
			//				break;
			//			}						
			//			 Eid=dbService.getCardPreferenceService().FindPreferenceByType(networkid, nodeid, Cardtype);
			Eqid=fgetEIdFromCardType(Cardtype);
			Eid = Integer.parseInt(Eqid.toString());					
		}
		data = new Object[2];
		data[0]=Cardtype;
		data[1]=Eqid;
		return data;

	}

	/**
	 * gets the Mpn cardtype and Equipment id to be placed  
	 * @param nodedegree
	 * @return
	 */
	public Object[] fgetMpnPreference(int networkid, int nodeid)
	{
		Object[] data = null; 
		String Cardtype = null;
		int Eid=0;
		Object Eqid = null;
		try {
			Eqid = dbService.getCardPreferenceService().FindPreferenceByFeature(networkid, nodeid, ResourcePlanConstants.CardFeatureMPN);
		} catch (SQLException e) {			
			e.printStackTrace();
		}	 
		if(Eqid!=null)
		{
			Eid = Integer.parseInt(Eqid.toString());
			Cardtype = fgetCardTypeFromEId(Eid);
		}
		else
		{	
			Cardtype=ResourcePlanConstants.CardMuxponderCGM;
			Eqid=fgetEIdFromCardType(Cardtype);
			Eid = Integer.parseInt(Eqid.toString());	
		}
		data = new Object[2];
		data[0]=Cardtype;
		data[1]=Eqid;
		return data;
	}

	public String locationStr(int rack, int sbrack, int card)
	{
		String str=new String();
		str = ""+rack+"_"+sbrack+"_"+card;
		return str;
	}

	public int[] locationIds(String str)
	{
		int[] id = new int[3];
		String[] ids=str.split("_");
		for (int i = 0; i < ids.length; i++) 
		{
			id[i]=Integer.parseInt(ids[i].toString());				
		}
		return id;
	}
	
	public String rackSubrackParamValStr(int rack,int subrack)
	{
		return ""+rack+"-"+subrack;
	}

	/**
	 * function gets the location of PABA card on the basis of degree
	 * @param degreeIndex
	 * @return
	 */
	public String fgetLocationPaBa(int degreeIndex)
	{
		String location = null;
		switch(degreeIndex)
		{
		case 1:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.TWO;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.ONE);			
		}break;
		case 2:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.TWO;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.FOURTEEN);			
		}break;

		case 3:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.THREE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.ONE);			
		}break;
		case 4:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.THREE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.FOURTEEN);			
		}break;
		case 5:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.ONE);			
		}break;
		case 6:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.FOURTEEN);			
		}break;

		case 7:
		{
			int rackid=ResourcePlanConstants.TWO;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.ONE);					
		}break;
		case 8:
		{
			int rackid=ResourcePlanConstants.TWO;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.FOURTEEN);		
		}break;		
		}
		return location;
	}
	
	/**
	 * function gets the location of PABA card on the basis of degree
	 * @param degreeIndex
	 * @return
	 */
	public String fgetLocationRaman(int degreeIndex)
	{
		String location = null;
		switch(degreeIndex)
		{
		case 1:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.TWO;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.ONE);			
		}break;
		case 2:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.TWO;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.THIRTEEN);			
		}break;

		case 3:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.THREE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.ONE);			
		}break;
		case 4:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.THREE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.THIRTEEN);			
		}break;
		case 5:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.ONE);			
		}break;
		case 6:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.THIRTEEN);			
		}break;

		case 7:
		{
			int rackid=ResourcePlanConstants.TWO;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.ONE);					
		}break;
		case 8:
		{
			int rackid=ResourcePlanConstants.TWO;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.THIRTEEN);		
		}break;		
		}
		return location;
	}

	/**
	 * fuctions gets the location of wss card on the basis of degree
	 * @param degreeIndex
	 * @return
	 */
	public String fgetLocationWss(int degreeIndex)
	{
		String location = null;
		switch(degreeIndex)
		{
		case 1:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.TWO;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.TWO);			
		}break;
		case 2:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.TWO;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.TWELVE);			
		}break;
		case 3:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.THREE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.TWO);			
		}break;
		case 4:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.THREE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.TWELVE);			
		}break;
		case 5:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.TWO);			
		}break;
		case 6:
		{
			int rackid=ResourcePlanConstants.ONE;
			int sbrackid=ResourcePlanConstants.ONE;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.TWELVE);			
		}break;

		case 7:
		{
			int rackid=ResourcePlanConstants.TWO;
			int sbrackid=ResourcePlanConstants.TWO;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.TWO);					
		}break;
		case 8:
		{
			int rackid=ResourcePlanConstants.TWO;
			int sbrackid=ResourcePlanConstants.TWO;
			location=locationStr(rackid,sbrackid,ResourcePlanConstants.TWELVE);		
		}break;		
		}
		return location;
	}

	/**
	 * function returns the list of nodeids that need to be configured in a network
	 * @param networkid
	 * @return
	 */
	public List<Integer> fgetNodesToConfigureold(int networkid)
	{
		HashSet NodesToConfigure= new HashSet();
		List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List <String> paths = new ArrayList<String>();
		String[] nodeids;

		for (int i = 0; i < routesAll.size(); i++) 
		{
			paths.add(i, routesAll.get(i).getPath());			 
			nodeids=paths.get(i).split(",");		
			for (int j = 0; j < nodeids.length; j++) 
			{			 									
				int nodeid=Integer.parseInt(nodeids[j].toString());
				NodesToConfigure.add(nodeid);
			}
		}

		/*nodes which participate in the routes will need configuration*/
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.addAll(NodesToConfigure);
		return list;
	}

	public List<Integer> fgetNodesToConfigure(int networkid)
	{
		HashSet NodesToConfigure= new HashSet();
		List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
		List<Node> nodesAll = dbService.getNodeService().FindAll(networkid);
		List <String> paths = new ArrayList<String>();
		String[] nodeids;

		/**Find out nodes from the Network Route*/
		for (int i = 0; i < nodesAll.size(); i++) 
		{
			int nodeid=nodesAll.get(i).getNodeId();			 
			NodesToConfigure.add(nodeid);

		}		 
		/*nodes which participate in the routes will need configuration*/
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.addAll(NodesToConfigure);
		return list;
	}

	public int fgetSupyForDirection(String dir)
	{
		int id = 7;
		switch(dir)
		{
		case MapConstants.EAST:		
		case MapConstants.NORTH:
		case MapConstants.NE:
		case MapConstants.SE:

			id=7;
			break;
		case MapConstants.WEST:
		case MapConstants.SOUTH:		
		case MapConstants.NW:		
		case MapConstants.SW:
			id=8;
			break;

		}
		return id;		
	}

	public String fgetEdfaLocationForDirection(int networkid,int nodeid,String dir)
	{
		String location="0_0_0";
		List<CardInfo> edfa;
		switch(dir)
		{
		case MapConstants.EAST:		
		case MapConstants.WEST:
		case MapConstants.NORTH:
		case MapConstants.SOUTH:
			
			edfa=dbService.getCardInfoService().FindCards(networkid, nodeid, ResourcePlanConstants.CardEdfa, ""+11);
			if(edfa!=null && edfa.size()!=0)
			location = locationStr(edfa.get(0).getRack(),edfa.get(0).getSbrack(),edfa.get(0).getCard());
			break;
		case MapConstants.NE:
		case MapConstants.NW:		
		case MapConstants.SE:		
		case MapConstants.SW:
			edfa=dbService.getCardInfoService().FindCards(networkid, nodeid, ResourcePlanConstants.CardEdfa, ""+12);
			if(edfa!=null && edfa.size()!=0)
			location = locationStr(edfa.get(0).getRack(),edfa.get(0).getSbrack(),edfa.get(0).getCard());
			break;

		}
		return location;		
	}

	public String fcheckProximity(String loc1 , String loc2)
	{
		String proximity="";
		int id1[]=locationIds(loc1);
		int id2[]=locationIds(loc2);
		if(id1[0]==id2[0])//same rack
		{			
			if(id1[1]==id2[1])//same sbrack
			{				
				proximity=ResourcePlanConstants.sameSbr;		
			}
			else
			{
				int n =id1[1]-id2[1];
				if(Math.abs(n)==1)
				{
					proximity=ResourcePlanConstants.adjacentSbr;
				}
				else if(Math.abs(n)==2)
				{
					proximity=ResourcePlanConstants.farSbr;		
				}
			}
		}
		else
		{
			proximity=ResourcePlanConstants.farRack;
		}				
		return proximity;		
	}

	//	public int fgetPatchCord(String proximity, String partcode)
	//	{
	//		System.out.println("ResourcePlanUtils.fgetPatchCord()"+partcode);
	//		switch(proximity)
	//		{
	//			case ResourcePlanConstants.sameSbr:
	//			{
	//				partcode=partcode.substring(0, partcode.length()-3).concat("01m");		
	//			}
	//			break;
	//			case ResourcePlanConstants.adjacentSbr:
	//			{
	//				partcode=partcode.substring(0, partcode.length()-3).concat("02m");		
	//			}
	//			break;
	//			case ResourcePlanConstants.farSbr:
	//			{
	//				partcode=partcode.substring(0, partcode.length()-3).concat("03m");		
	//			}
	//			break;
	//			case ResourcePlanConstants.farRack:
	//			{
	//				partcode=partcode.substring(0, partcode.length()-3).concat("10m");		
	//			}
	//			break;
	//		}
	//		int eid= fgetEId(partcode);
	//		System.out.println("ResourcePlanUtils.fgetPatchCord() partcode "+partcode);
	//		System.out.println("ResourcePlanUtils.fgetPatchCord() return "+eid);
	//		return eid;
	//	}

	public int fgetPatchCord(String proximity, String partcode)
	{
		//		System.out.println("ResourcePlanUtils.fgetPatchCord()"+partcode);
		switch(partcode)
		{
		case ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m:
		{
			switch(proximity)
			{
			case ResourcePlanConstants.sameSbr:
			{
				partcode=ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m;		
			}
			break;
			case ResourcePlanConstants.adjacentSbr:
			{
				partcode=ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_02m;				
			}
			break;
			case ResourcePlanConstants.farSbr:
			{
				partcode=ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_03m;				
			}
			break;
			case ResourcePlanConstants.farRack:
			{
				partcode=ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_10m;				
			}
			break;
			}				
		}break;
		case ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m:
		{
			switch(proximity)
			{
			case ResourcePlanConstants.sameSbr:
			{
				partcode=ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m;		
			}
			break;
			case ResourcePlanConstants.adjacentSbr:
			{
				partcode=ResourcePlanConstants.EPart_LC_APC_to_LC_APC_02m;				
			}
			break;
			case ResourcePlanConstants.farSbr:
			{
				partcode=ResourcePlanConstants.EPart_LC_APC_to_LC_APC_03m;				
			}
			break;
			case ResourcePlanConstants.farRack:
			{
				partcode=ResourcePlanConstants.EPart_LC_APC_to_LC_APC_10m;				
			}
			break;
			}		

		}break;
		case ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m:
		{
			switch(proximity)
			{
			case ResourcePlanConstants.sameSbr:
			{
				partcode=ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m;		
			}
			break;
			case ResourcePlanConstants.adjacentSbr:
			{
				partcode=ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_02m;				
			}
			break;
			case ResourcePlanConstants.farSbr:
			{
				partcode=ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_03m;				
			}
			break;
			case ResourcePlanConstants.farRack:
			{
				partcode=ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_10m;				
			}
			break;
			}		

		}break;
		}

		int eid= fgetEId(partcode);
		//		System.out.println("ResourcePlanUtils.fgetPatchCord() partcode "+partcode);
		//		System.out.println("ResourcePlanUtils.fgetPatchCord() return "+eid);
		return eid;
	}

	public boolean fcheckIsGne(int networkid, int nodeid)
	{
		boolean flag =false;
		Node node = dbService.getNodeService().FindNode(networkid, nodeid);
		if(node.getIsGne()==ResourcePlanConstants.ONE)
		{
			flag = true;
		}
		else
		{
			flag = false;
		}

		return flag;

	}

	public int fgetSfpEid(int networkid, int nodeid, String param, int value, String servicetype) throws SQLException
	{
		int eid;
		switch(servicetype)
		{
		case ResourcePlanConstants.EthernetService:
		{
			EquipmentPreferences eqp = new EquipmentPreferences(dbService);
			Object[] pEq = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatSfpEthernet, param, ""+value);
			eid=fgetEIdFromCardType(pEq[0].toString());
			//eid=ResourcePlanConstants.EId_SFP_Electrical_40Km;
		}
		break;
		case ResourcePlanConstants.ScmSfp:
		{
			eid=ResourcePlanConstants.EId_SFP_Electrical_SCM;
		}
		break;
		default:
		{
			EquipmentPreferences eqp = new EquipmentPreferences(dbService);
			Object[] pEq = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatSfpNonEthernet, param, ""+value);
			eid=fgetEIdFromCardType(pEq[0].toString());
			//eid=ResourcePlanConstants.EId_SFP_Multirate_40Km;
		}

		}
		return eid;

	}

	public CardInfo fgetFreeSbrackSpace(int networkid, int nodeid)
	{
		CardInfo space =null;

		List<Map<String, Object>> racks= dbService.getCardInfoService().FindRacks(networkid, nodeid);				
		for (int i = 0; i < racks.size(); i++) 
		{
			Integer cardid = dbService.getCardInfoService().FindfreeSubrackSpaceInRack(networkid, nodeid, Integer.parseInt(racks.get(i).get("Rack").toString()));
			if(cardid!=null)
			{
				if(cardid<5) 
				{
					//space exists , send the next card of ith rack
					space = new CardInfo();
//					space.setNodeKey(""+networkid+"_"+nodeid);
					space.setNetworkId(networkid);;
					space.setNodeId(nodeid);
					space.setCard(Integer.parseInt(cardid.toString())+1);
					space.setRack(Integer.parseInt(racks.get(i).get("Rack").toString()));
					space.setSbrack(0);	
					return space;
				}				
			}
			else
			{
				//no card in space exists
				//send the first space of ith rack
				space = new CardInfo();
//				space.setNodeKey(""+networkid+"_"+nodeid);
				space.setNetworkId(networkid);;
				space.setNodeId(nodeid);;
				space.setCard(1);
				space.setRack(Integer.parseInt(racks.get(i).get("Rack").toString()));
				space.setSbrack(0);				
				return space;
			}
		}	

		return space;		
	}

	public int fgetDirectionNumber(String dir)
	{
		int d=0;
		switch(dir)
		{
		case MapConstants.EAST:
		{
			d=1;
		}
		break;
		case MapConstants.WEST:
		{
			d=2;
		}
		break;
		case MapConstants.NORTH:
		{
			d=3;
		}
		break;
		case MapConstants.SOUTH:
		{
			d=4;
		}
		break;
		case MapConstants.NE:
		{
			d=5;
		}
		break;
		case MapConstants.NW:
		{
			d=6;
		}
		break;
		case MapConstants.SE:
		{
			d=7;
		}
		break;
		case MapConstants.SW:
		{
			d=8;
		}
		break;
		}
		return d;
	}

	/**
	 * function checks the availability of a particular slot in a particular rack sbrack of a node from the db
	 * @param networkid
	 * @param nodeid
	 * @param rackid
	 * @param sbrackid
	 * @param slotid
	 * @return
	 */
	public boolean fCheckSlotAvailabilityDb(int networkid, int nodeid, int rackid, int sbrackid, int slotid )
	{		
		boolean flag = true;		 
		if(dbService.getCardInfoService().IsSlotFree(networkid, nodeid, rackid, sbrackid, slotid))
		{
			if(slotid!=1)//check the previous slot for the two slot card
			{
				CardInfo card = dbService.getCardInfoService().FindCardInfoWithEq(networkid, nodeid, rackid, sbrackid,slotid-1);
				if(card!=null)
				{
					Equipment e = dbService.getEquipmentService().findEquipmentById(card.getEquipmentId());
					if(e.getSlotSize()==ResourcePlanConstants.SlotSizeTwo)
					{
						flag =false;
					}
					else
					{
						flag = true;
					}
				}
				else
				{
					flag = true;
				}
			}
			else
			{
				flag = true;
			}
		}
		else
		{
			flag =false;
		}
		logger.info(" Network:"+networkid+" Node:"+nodeid+" Rack:"+rackid+" Sbrack:"+sbrackid+" Slot:"+slotid+" Available:"+flag);
		return flag;

	}
	
	/**
	 * function checks the availability of a particular slot in a particular rack sbrack of a node from the db
	 * @param networkid
	 * @param nodeid
	 * @param rackid
	 * @param sbrackid
	 * @param slotid
	 * @return
	 */
	public boolean fCheckDouibleSlotAvailabilityDb(int networkid, int nodeid, int rackid, int sbrackid, int slotid )
	{		
		boolean flag = true;		 
		if(dbService.getCardInfoService().IsSlotFree(networkid, nodeid, rackid, sbrackid, slotid))
		{
			if(slotid!=1)//check the previous slot for the two slot card
			{
				CardInfo cardPrev = dbService.getCardInfoService().FindCardInfoWithEq(networkid, nodeid, rackid, sbrackid,slotid-1);
				CardInfo cardNext = dbService.getCardInfoService().FindCardInfoWithEq(networkid, nodeid, rackid, sbrackid,slotid+1);
				
				if(cardNext!=null)
				{
					flag=false;
				}
				else if(cardPrev!=null)
				{
					Equipment e = dbService.getEquipmentService().findEquipmentById(cardPrev.getEquipmentId());
					if(e.getSlotSize()==ResourcePlanConstants.SlotSizeTwo)
					{
						flag =false;
					}
					else
					{
						flag = true;
					}
				}
				else
				{
					flag = true;
				}
				
				
			}
			else
			{
				flag = true;
			}
		}
		else
		{
			flag =false;
		}
		logger.info(" Network:"+networkid+" Node:"+nodeid+" Rack:"+rackid+" Sbrack:"+sbrackid+" Slot:"+slotid+" Available:"+flag);
		return flag;

	}

	//	public String[] fGetFirstFreeMpnSlotDb(int networkid,int nodeid,int nodetype,int nodedegree,int slotsize,String paired)
	//	{		
	//		boolean flag=true;		
	//		String[] location = null;
	//		switch(nodedegree)
	//		{
	//
	//
	//		case ResourcePlanConstants.ONE:
	//		case ResourcePlanConstants.TWO:
	//		case ResourcePlanConstants.THREE:
	//		case ResourcePlanConstants.FOUR:
	//		case ResourcePlanConstants.FIVE:
	//		case ResourcePlanConstants.SIX:
	//		case ResourcePlanConstants.SEVEN:
	//		case ResourcePlanConstants.EIGHT:
	//		{
	//			//check if 1,2 and 4,5 are free or check 10,11 and 13,14 are free
	//
	//			if(slotsize==ResourcePlanConstants.TWO)
	//			{
	//				if(paired==ResourcePlanConstants.Yes)
	//				{
	//					//start checking from rack 1 and sbrack 2	
	//					for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
	//					{
	//						for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++)
	//						{	
	//							location = new String[2];
	//							//skip for r,s 1,2
	//							//								 if(!(i==1)&(j==2))
	//							//								 {
	//							//for a two slot card check availability for both slots
	//							if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 1))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 2)))
	//							{
	//								if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 4))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 5)))
	//								{											 
	//									location[0]=locationStr(i,j,1);
	//									location[1]=locationStr(i,j,4);
	//									return location;									
	//
	//								}else
	//								{
	//									location=null;										 
	//								}						 
	//							}
	//							else if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 10))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 11)))
	//							{
	//								if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 13))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 14)))
	//								{											 
	//									location[0]=locationStr(i,j,10);
	//									location[1]=locationStr(i,j,13);
	//									return location;									
	//
	//								}else
	//								{
	//									location=null;										 
	//								}						 
	//							}
	//							else
	//							{
	//								location=null;
	//							}
	//							//								 }
	//						}
	//					}
	//
	//				}
	//				else if(paired==ResourcePlanConstants.No)//not a paired slot but a two slot
	//				{														
	//					for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
	//					{
	//						for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++)
	//						{			
	//							location = new String[2];
	//							//skip for r,s 1,2
	//							//								 if(!(i==1)&(j==2))
	//							//								 {
	//							if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 1))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 2)))
	//							{
	//								location[0]=locationStr(i,j,1);
	//								return location;											 									 
	//
	//							}	
	//							else if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 2))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 3)))
	//							{
	//								location[0]=locationStr(i,j,2);
	//								return location;											 									 
	//
	//							}	
	//							else if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 3))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 4)))
	//							{
	//								location[0]=locationStr(i,j,3);
	//								return location;											 									 
	//
	//							}	
	//							else if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 4))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 5)))
	//							{
	//								location[0]=locationStr(i,j,4);
	//								return location;											 
	//
	//							}
	//							else if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 7))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 8)))
	//							{
	//								location[0]=locationStr(i,j,7);
	//								return location;											 
	//
	//							}
	//							else if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 10))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 11)))
	//							{
	//								location[0]=locationStr(i,j,10);
	//								return location;												
	//
	//							}
	//							else if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 11))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 12)))
	//							{
	//								location[0]=locationStr(i,j,11);
	//								return location;												
	//
	//							}
	//							else if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 12))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 13)))
	//							{
	//								location[0]=locationStr(i,j,12);
	//								return location;												
	//
	//							}
	//							else if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 13))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 14)))
	//							{
	//								location[0]=locationStr(i,j,13);	
	//								return location;											 
	//
	//							}
	//							//								 }
	//						}
	//					}
	//
	//				}						 
	//			}
	//			else if(slotsize==ResourcePlanConstants.ONE)
	//			{
	//				if(paired==ResourcePlanConstants.Yes)
	//				{												
	//					for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
	//					{
	//						for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++)
	//						{	
	//							location = new String[2];
	//							if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 1))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 4)))
	//							{
	//								location[0]=locationStr(i,j,1);
	//								location[1]=locationStr(i,j,4);
	//								return location;					 
	//							}
	//							else  if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 10))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 13)))
	//							{
	//								location[0]=locationStr(i,j,10);
	//								location[1]=locationStr(i,j,13);												
	//								return location;				 								 
	//							}
	//							else  if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 3))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 7)))
	//							{
	//								location[0]=locationStr(i,j,3);
	//								location[1]=locationStr(i,j,7);												
	//								return location;				 								 
	//							}
	//							else  if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 8))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 12)))
	//							{
	//								location[0]=locationStr(i,j,8);
	//								location[1]=locationStr(i,j,12);												
	//								return location;				 								 
	//							}
	//							else  if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 2))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 5)))
	//							{
	//								location[0]=locationStr(i,j,2);
	//								location[1]=locationStr(i,j,5);												
	//								return location;				 								 
	//							}
	//							else  if((fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 11))&(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 14)))
	//							{
	//								location[0]=locationStr(i,j,11);
	//								location[1]=locationStr(i,j,14);												
	//								return location;														 								 
	//							}
	//							else
	//							{
	//								location=null;
	//							}
	//						}
	//					}
	//				}
	//				else if(paired==ResourcePlanConstants.No)
	//				{							 
	//					for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
	//					{
	//						for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++)
	//						{	
	//							location = new String[2];
	//							if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 1))
	//							{
	//								location[0]=locationStr(i,j,1);										 
	//								return location;								 
	//							}
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 2))
	//							{
	//								location[0]=locationStr(i,j,2);										 
	//								return location;								 
	//							}
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 3))
	//							{
	//								location[0]=locationStr(i,j,3);										 
	//								return location;								 
	//							}
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 4))
	//							{
	//								location[0]=locationStr(i,j,4);										 
	//								return location;								 
	//							}
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 5))
	//							{
	//								location[0]=locationStr(i,j,5);										 
	//								return location;								 
	//							}
	//
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 7))
	//							{
	//								location[0]=locationStr(i,j,7);										 
	//								return location;								 
	//							}
	//
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 8))
	//							{
	//								location[0]=locationStr(i,j,8);										 
	//								return location;								 
	//							}
	//
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 10))
	//							{
	//								location[0]=locationStr(i,j,10);										 
	//								return location;								 
	//							}
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 11))
	//							{
	//								location[0]=locationStr(i,j,11);										 
	//								return location;								 
	//							}
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 12))
	//							{
	//								location[0]=locationStr(i,j,12);										 
	//								return location;								 
	//							}
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 13))
	//							{
	//								location[0]=locationStr(i,j,13);										 
	//								return location;								 
	//							}
	//							else if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, 14))
	//							{
	//								location[0]=locationStr(i,j,14);										 
	//								return location;								 
	//							}									 
	//						}
	//					}
	//				}
	//			}				 
	//
	//		}break;		 
	//		}
	//		logger.info("fGetFirstFreeMpnSlotDb: Network:"+networkid+" Node:"+nodeid+" Location:"+location);
	//		return location;
	//
	//	}

	public String[] fGetFirstFreeMpnSlotDb(int networkid,int nodeid,int nodetype,int nodedegree,int slotsize,String paired)
	{		
		boolean flag=true;		
		String[] location = new String[2];
		int sbRackNo;
		
		System.out.println("************ fGetFirstFreeMpnSlotDb ************");
		System.out.println("NodeId :"+nodeid+" nodetype :"+nodetype+" slotsize :"+slotsize+" paired :"+paired);
				
		switch(nodedegree)
		{
		case ResourcePlanConstants.ONE:
		case ResourcePlanConstants.TWO:
		case ResourcePlanConstants.THREE:
		case ResourcePlanConstants.FOUR:
		case ResourcePlanConstants.FIVE:
		case ResourcePlanConstants.SIX:
		case ResourcePlanConstants.SEVEN:
		case ResourcePlanConstants.EIGHT:
		{
			
			if(slotsize==ResourcePlanConstants.ONE && paired.equals(ResourcePlanConstants.No)) {
				System.out.println("************ SingleSlot ************");
				String loc=fGetFirstFreeSlotDb(networkid, nodeid);
				location[0]=loc;
				return location;
			}else if(slotsize==ResourcePlanConstants.TWO && paired.equals(ResourcePlanConstants.No)) {
				System.out.println("************ DoubleSlot ************");
				String loc=fGetFirstFreeDoubleSlotDb(networkid, nodeid);
				location[0]=loc;
				return location;
			}else
			/* Paired MPN Cards*/
			{
				//check if 1,2 and 4,5 are free or check 10,11 and 13,14 are free
				for (int rackNo = 1; rackNo <= ResourcePlanConstants.MaxRack; rackNo++) 
				{
					sbRackNo=2;
					location=fGetFirstFreeMpnLocationDb(networkid,nodeid,rackNo,sbRackNo,slotsize,paired);
//					logger.info("fGetFirstFreeMpnSlotDb: rackNo:"+rackNo+" sbRackNo:"+sbRackNo+" nodeid:"+nodeid+" Location:"+location);
					if(location!=null)
						return location;

					sbRackNo=3;
					location=fGetFirstFreeMpnLocationDb(networkid,nodeid,rackNo,sbRackNo,slotsize,paired);
//					logger.info("fGetFirstFreeMpnSlotDb: rackNo:"+rackNo+" sbRackNo:"+sbRackNo+" nodeid:"+nodeid+" Location:"+location);
					if(location!=null)
						return location;

					sbRackNo=1;
					location=fGetFirstFreeMpnLocationDb(networkid,nodeid,rackNo,sbRackNo,slotsize,paired);
//					logger.info("fGetFirstFreeMpnSlotDb: rackNo:"+rackNo+" sbRackNo:"+sbRackNo+" nodeid:"+nodeid+" Location:"+location);
					if(location!=null)
						return location;
				}
			}

		}break;		 
		}
		logger.info("fGetFirstFreeMpnSlotDb: Network:"+networkid+" Node:"+nodeid+" Location:"+location);
		return null;

	}

	public String[] fGetFirstFreeMpnLocationDb(int networkid,int nodeid,int rack,int sbRack,int slotsize,String paired)
	{
		String slot_paired_str=""+slotsize+"_"+paired;
//		logger.info("nodeid:"+nodeid+"rack:"+rack+"sbRack:"+sbRack+"slot_paired_str:"+slot_paired_str);
		String []location = new String[2];
		
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		String PrefChassisType=ResourcePlanConstants.EmersionChassis;
		try {
			PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
					ResourcePlanConstants.ParamSubrack, ""+rack+"-"+sbRack)[0].toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		switch (slot_paired_str) {
		case "2_"+ResourcePlanConstants.Yes:
		{

			Map<Integer,Integer> referenceSlotpairs=fGetMpnSlotPairReferenceList(PrefChassisType);
			String MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);
			
			logger.info("**** NodeId"+nodeid+"Double Slot with Pairing Case:"+slot_paired_str+" PrefChassisType"+PrefChassisType);
			
			if(PrefChassisType.equals(ResourcePlanConstants.EmersionChassis)) {
			for (Entry<Integer, Integer> obj : referenceSlotpairs.entrySet()) {				
					if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getKey()/*FirstSlot*/) 
							&& fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getKey()+1/*FirstSlot*/)
							&& fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getValue()/*SecondSlot*/)
							&& fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getValue()+1/*SecondSlot*/)
							&& obj.getValue()!=ResourcePlanConstants.FOURTEEN
							&& obj.getValue() !=ResourcePlanConstants.FIVE)
					{
						if(MpcRedun.equals(ResourcePlanConstants.Yes) && obj.getKey() ==ResourcePlanConstants.EIGHT)
						{
							// Go to next value
							System.out.println("Mpc Redun + Slot 8  ---- NodeId :"+nodeid);
						}	
						else {							
							System.out.println("NodeId"+nodeid+" Slot1:"+obj.getKey()+" Slot2:"+obj.getValue());
							location[0]=locationStr(rack,sbRack,obj.getKey());
							location[1]=locationStr(rack,sbRack,obj.getValue());
							return location;
						}
						
					}
				}
			}
			else 
			{
				for (Entry<Integer, Integer> obj : referenceSlotpairs.entrySet()) {		
					if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getKey()/*FirstSlot*/) 
							&& fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getKey()+1/*FirstSlot*/)
							&& fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getValue()/*SecondSlot*/)
							&& fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getValue()+1/*SecondSlot*/)
							&& obj.getValue()!=ResourcePlanConstants.FOURTEEN
							&& obj.getKey()!=ResourcePlanConstants.SIX)
					{
						location[0]=locationStr(rack,sbRack,obj.getKey());
						location[1]=locationStr(rack,sbRack,obj.getValue());
						return location;
					}
				}
			}
			
			System.out.println("Location for double slot paired cards :"+location.toString());
			
//			Optional<Entry<Integer, Integer>> loc=referenceSlotpairs.entrySet().stream().filter(obj -> {
//				if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getKey()/*FirstSlot*/) 
//						&& fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getKey()+1/*FirstSlot*/)
//						&& fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getValue()/*SecondSlot*/)
//						&& fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getValue()+1/*SecondSlot*/)
//						&& obj.getValue()!=ResourcePlanConstants.FOURTEEN)
//					return true;
//				return false;
//			}).findFirst();
			
//			if(loc.isPresent()) {
//				logger.info(" ++++++ Loc Found ++++++");
//				// String returned is of type x(firstSlot)=y(secondSlot)
//				location[0]=locationStr(rack,sbRack,Integer.parseInt(loc.get().toString().split("=")[0]));
//				location[1]=locationStr(rack,sbRack,Integer.parseInt(loc.get().toString().split("=")[1]));
//				logger.info(" location[0] :"+location[0]+" location[1] :"+location[1]);
//				
//				return location;
//			}else {
//				location=null;
//			}
			
			
//			if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 1))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 2)))
//			{
//				if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 4))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 5)))
//				{											 
//					location[0]=locationStr(rack,sbRack,1);
//					location[1]=locationStr(rack,sbRack,4);								
//					return location;
//				}					 
//			}
//			
//			if(sbRack!=ResourcePlanConstants.TWO && (fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 3))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 4)))
//			{
//				if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 7))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 8)))
//				{											 
//					location[0]=locationStr(rack,sbRack,3);
//					location[1]=locationStr(rack,sbRack,7);						
//					return location;
//				}					 
//			}
//			
//			if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 10))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 11)))
//			{
//				if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 13))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 14)))
//				{											 
//					location[0]=locationStr(rack,sbRack,10);
//					location[1]=locationStr(rack,sbRack,13);	
//					return location;
//				}					 
//			}
			
			location=null;
		}
		break;
//		case "2_"+ResourcePlanConstants.No:
//		{
//			logger.info("fGetFirstFreeMpnLocationDb Case:"+slot_paired_str);
//			if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 1))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 2)))
//			{
//				location[0]=locationStr(rack,sbRack,1);
//			}	
//			else if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 2))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 3)))
//			{
//				location[0]=locationStr(rack,sbRack,2);											 									 
//
//			}	
//			else if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 3))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 4)))
//			{
//				location[0]=locationStr(rack,sbRack,3);
//
//			}	
//			else if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 4))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 5)))
//			{
//				location[0]=locationStr(rack,sbRack,4);
//
//			}
//			else if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 7))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 8)))
//			{
//				location[0]=locationStr(rack,sbRack,7);
//
//			}
//			else if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 10))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 11)))
//			{
//				logger.info(locationStr(rack,sbRack,10));
//				location[0]=locationStr(rack,sbRack,10);				
//			}
//			else if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 11))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 12)))
//			{
//				location[0]=locationStr(rack,sbRack,11);
//
//			}
//			else if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 12))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 13)))
//			{
//				location[0]=locationStr(rack,sbRack,12);
//
//			}
//			else if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 13))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 14)))
//			{
//				location[0]=locationStr(rack,sbRack,13);					
//			}
//			else{
//				location=null;
//			}
//		}
//		break;
		case "1_"+ResourcePlanConstants.Yes:
		{			
			Map<Integer,Integer> referenceSlotpairs=fGetMpnSlotPairReferenceList(PrefChassisType);
			
			logger.info("Single Slot with Pairing Case:"+slot_paired_str+" PrefChassisType"+PrefChassisType);
				
			Optional<Entry<Integer, Integer>> loc=referenceSlotpairs.entrySet().stream().filter(obj -> {
				if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getKey()/*FirstSlot*/) && fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, obj.getValue()/*SecondSlot*/))
					return true;
				return false;
			}).findFirst();			
			
			if(loc.isPresent()) {
				logger.info(" ++++++ Loc Found ++++++");
				// String returned is of type x(firstSlot)=y(secondSlot)
				location[0]=locationStr(rack,sbRack,Integer.parseInt(loc.get().toString().split("=")[0]));
				location[1]=locationStr(rack,sbRack,Integer.parseInt(loc.get().toString().split("=")[1]));
//				return location;
			}else {
				location=null;
			}
					
			
//			if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 1))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 4)))
//			{
//				location[0]=locationStr(rack,sbRack,1);
//				location[1]=locationStr(rack,sbRack,4);				 
//			}
//			else  if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 10))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 13)))
//			{
//				location[0]=locationStr(rack,sbRack,10);
//				location[1]=locationStr(rack,sbRack,13);																 								 
//			}
//			else  if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 3))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 7)))
//			{
//				location[0]=locationStr(rack,sbRack,3);
//				location[1]=locationStr(rack,sbRack,7);												
//			}
//			else  if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 8))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 12)))
//			{
//				location[0]=locationStr(rack,sbRack,8);
//				location[1]=locationStr(rack,sbRack,12);												
//			}
//			else  if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 2))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 5)))
//			{
//				location[0]=locationStr(rack,sbRack,2);
//				location[1]=locationStr(rack,sbRack,5);																 								 
//			}
//			else  if((fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 11))&(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 14)))
//			{
//				location[0]=locationStr(rack,sbRack,11);
//				location[1]=locationStr(rack,sbRack,14);																										 								 
//			}
//			else{
//				location=null;
//			}
		}
		break;
//		case "1_"+ResourcePlanConstants.No:
//		{
//			logger.info("fGetFirstFreeMpnLocationDb Case:"+slot_paired_str);
//			if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 1))
//			{
//				location[0]=locationStr(rack,sbRack,1);										 								 
//			}
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 2))
//			{
//				location[0]=locationStr(rack,sbRack,2);										 
//			}
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 3))
//			{
//				location[0]=locationStr(rack,sbRack,3);									
//			}
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 4))
//			{
//				location[0]=locationStr(rack,sbRack,4);										 
//			}
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 5))
//			{
//				location[0]=locationStr(rack,sbRack,5);										 							 
//			}
//
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 7))
//			{
//				location[0]=locationStr(rack,sbRack,7);										 								 
//			}
//
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 8))
//			{
//				location[0]=locationStr(rack,sbRack,8);										 						 
//			}
//
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 10))
//			{
//				location[0]=locationStr(rack,sbRack,10);										 								 
//			}
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 11))
//			{
//				location[0]=locationStr(rack,sbRack,11);										 							 
//			}
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 12))
//			{
//				location[0]=locationStr(rack,sbRack,12);																	 
//			}
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 13))
//			{
//				location[0]=locationStr(rack,sbRack,13);										 							 
//			}
//			else if(fCheckSlotAvailabilityDb(networkid, nodeid, rack, sbRack, 14))
//			{
//				location[0]=locationStr(rack,sbRack,14);										 							 
//			}
//			else{
//				location=null;
//			}
//		}
//		break;

		default:
//			logger.info("fGetFirstFreeMpnLocationDb: unknown slot_paired_str = "+slot_paired_str);
			break;
		}
		logger.info("fGetFirstFreeMpnLocationDb :: Returning location:"+location);
		return location;

	}

	public String fGetFirstFreeSlotDb(int networkid, int nodeid)
	{
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		/*fetch the nodepool from map for that node*/
		//		 NodeSystem nodepool=poolmap.get(nodeid);

		/*get the first free ids from the respective pool*/
		//		 int rackid=nodepool.getRack().firstKey();
		//		 int sbrackid=nodepool.getRack().get(rackid).getSbrack().firstKey();
		//		 int cardid=nodepool.getRack().get(rackid).getSbrack().get(sbrackid).getSlots().firstKey();
		//		 
		//		 String location = "";
		//		 location=rpu.locationStr(rackid,sbrackid,cardid);	

		String location = "";		 		 
		//		 for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
		//		 {
		//			 for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) 
		//			 {
		//				 for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
		//				 {
		//					 if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, k))
		//					 {						 
		//						 location=locationStr(i,j,k);
		//						 logger.info("fGetFirstFreeSlotDb Location: "+location);
		//						 return location;
		//					 }
		//				 }
		//			 }
		//		 }	
		int sbRack;
		int []mpcLocation=new int[2];
		int mpcLocationOne=ResourcePlanConstants.SIX,
		mpcLocationTwo=ResourcePlanConstants.NINE;
		// Find slot in subrack 2 
		for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
		{
			sbRack=2;
			mpcLocationOne=ResourcePlanConstants.SIX;
			mpcLocationTwo=ResourcePlanConstants.NINE;
			try {
				String PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
						ResourcePlanConstants.ParamSubrack, ""+i+"-"+sbRack)[0].toString();
				mpcLocation=fGetControllerCardLocation(PrefChassisType);
				mpcLocationOne=mpcLocation[0];
				mpcLocationTwo=mpcLocation[1];
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
			{
				if(k!=mpcLocationOne & k!=mpcLocationTwo & fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, k))

				{						 
					location=locationStr(i,sbRack,k);
					logger.info("fGetFirstFreeSlotDb Location: "+location);
					return location;
				}
			}

			sbRack=3;
			mpcLocationOne=ResourcePlanConstants.SIX;
			mpcLocationTwo=ResourcePlanConstants.NINE;
			try {
				String PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
						ResourcePlanConstants.ParamSubrack, ""+sbRack)[0].toString();
				mpcLocation=fGetControllerCardLocation(PrefChassisType);
				mpcLocationOne=mpcLocation[0];
				mpcLocationTwo=mpcLocation[1];
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
			{
				if(k!=mpcLocationOne & k!=mpcLocationTwo & fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, k))

				{						 
					location=locationStr(i,sbRack,k);
					logger.info("fGetFirstFreeSlotDb Location: "+location);
					return location;
				}
			}

			sbRack=1;
			mpcLocationOne=ResourcePlanConstants.SIX;
			mpcLocationTwo=ResourcePlanConstants.NINE;
			try {
				String PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
						ResourcePlanConstants.ParamSubrack, ""+sbRack)[0].toString();
				mpcLocation=fGetControllerCardLocation(PrefChassisType);
				mpcLocationOne=mpcLocation[0];
				mpcLocationTwo=mpcLocation[1];
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
			{
				if(k!=mpcLocationOne & k!=mpcLocationTwo & fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, k))

				{						 
					location=locationStr(i,sbRack,k);
					logger.info("fGetFirstFreeSlotDb Location: "+location);
					return location;
				}
			}


		}


		logger.info("fGetFirstFreeSlotDb Location: "+location);	
		return location;
	}

	public String fGetFirstFreeLineCardSlotDb(int networkid, int nodeid)
	{
		String location = "";		 
		//		for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
		//		{
		//			for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) 
		//			{
		//				for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
		//				{
		//					if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, k))
		//					{						 
		//						location=locationStr(i,j,k);
		//						if(((location.contains("_6")/*|(location.contains("_9"))*/)))
		//						{
		//							k--;
		//						}
		//						else
		//						{
		//							logger.info("fGetFirstFreeSlotDb Location: "+location);
		//							return location;
		//						}
		//					}
		//				}
		//			}
		//		}	

		int sbRack;
		
		// Find slot in subrack 2 
		for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
		{
			sbRack=2;
			for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
			{
				if((k!=6 | k!=9) & fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, k))
				{						 
					location=locationStr(i,sbRack,k);
					logger.info("fGetFirstFreeLineCardSlotDb Location: "+location);
					return location;
//					if(((location.contains("_6")|(location.contains("_9")))))
//					{
//						k--;
//					}
//					else
//					{
//						logger.info("fGetFirstFreeSlotDb Location: "+location);
//						return location;
//					}
				}
			}

			sbRack=3;
			for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
			{
				if((k!=6 | k!=9) & fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, k))
				{						 
					location=locationStr(i,sbRack,k);
					logger.info("fGetFirstFreeLineCardSlotDb Location: "+location);
					return location;
//					if(((location.contains("_6")|(location.contains("_9")))))
//					{
//						k--;
//					}
//					else
//					{
//						logger.info("fGetFirstFreeSlotDb Location: "+location);
//						return location;
//					}
				}
			}

			sbRack=1;
			for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
			{
				if((k!=6 | k!=9) & fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, k))
				{						 
					location=locationStr(i,sbRack,k);
					logger.info("fGetFirstFreeLineCardSlotDb Location: "+location);
					return location;
//					if(((location.contains("_6")|(location.contains("_9")))))
//					{
//						k--;
//					}
//					else
//					{
//						logger.info("fGetFirstFreeSlotDb Location: "+location);
//						return location;
//					}
				}
			}


		}
		logger.info("fGetFirstFreeSlotDb Location: "+location);
		return location;
	}
			 
	public String fGetFirstFreeDoubleSlotDb(int networkid, int nodeid)
	{		 
		String location = "";		 
//		for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
//		{
//			for (int j = 1; j <= ResourcePlanConstants.MaxSbrackPerRack; j++) 
//			{
//				for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack-1; k++) 
//				{
//					if(fCheckSlotAvailabilityDb(networkid, nodeid, i, j, k)&fCheckSlotAvailabilityDb(networkid, nodeid, i, j, (k+1)))
//					{						 
//						location=locationStr(i,j,k);
//						logger.info("fGetFirstFreeDoubleSlotDb Location: "+location);
//						return location;
//					}
//				}
//			}
//		}	
		
		int sbRack;
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		int []mpcLocation=new int[2];
//		ArrayList<Integer> reservedSlotsEmersion=new ArrayList<Integer>();
//		reservedSlotsEmersion.add(ResourcePlanConstants.FIVE);
//		reservedSlotsEmersion.add(ResourcePlanConstants.SIX);
//		reservedSlotsEmersion.add(ResourcePlanConstants.EIGHT);
//		reservedSlotsEmersion.add(ResourcePlanConstants.NINE);
//		
//		ArrayList<Integer> reservedSlotsOther=new ArrayList<Integer>();
//		reservedSlotsOther.add(ResourcePlanConstants.SEVEN);
//		reservedSlotsOther.add(ResourcePlanConstants.SIX);
//		reservedSlotsOther.add(ResourcePlanConstants.EIGHT);
//		ArrayList<Integer> reservedSlots=new ArrayList<Integer>();
		int mpcLocationOne=ResourcePlanConstants.SIX,
		mpcLocationTwo=ResourcePlanConstants.NINE;
		 // Find slot in subrack 2 
		 for (int i = 1; i <= ResourcePlanConstants.MaxRack; i++) 
		 {
			 sbRack=2;
//			 mpcLocationOne=ResourcePlanConstants.SIX;
//			 mpcLocationTwo=ResourcePlanConstants.NINE;
				try {
					String PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
							ResourcePlanConstants.ParamSubrack, ""+i+"-"+sbRack)[0].toString();
					mpcLocation=fGetControllerCardLocation(PrefChassisType);
					mpcLocationOne=mpcLocation[0];
					mpcLocationTwo=mpcLocation[1];
//					ArrayList<Integer> unavailableSlots=new ArrayList<Integer>();
//					if(PrefChassisType.equals(ResourcePlanConstants.EmersionChassis)) {
//						reservedSlots=reservedSlotsEmersion;
//					}else {
//						reservedSlots=reservedSlotsOther;
//					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
			 {
				 if(fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, k) & fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, (k+1)) & k!=mpcLocationOne & k!=mpcLocationTwo & k!=mpcLocationOne-1 & k!=mpcLocationTwo-1 & k!=14 )
					{						 
						location=locationStr(i,sbRack,k);
						logger.info("fGetFirstFreeDoubleSlotDb Location: "+location);
						return location;
					}
			 }
			 
			 sbRack=3;
//			 mpcLocationOne=ResourcePlanConstants.SIX;
//				mpcLocationTwo=ResourcePlanConstants.NINE;
				try {
					String PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
							ResourcePlanConstants.ParamSubrack, ""+sbRack)[0].toString();
					mpcLocation=fGetControllerCardLocation(PrefChassisType);
					mpcLocationOne=mpcLocation[0];
					mpcLocationTwo=mpcLocation[1];
//					if(PrefChassisType.equals(ResourcePlanConstants.EmersionChassis)) {
//						reservedSlots=reservedSlotsEmersion;
//					}else {
//						reservedSlots=reservedSlotsOther;
//					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
			 {
				 if(fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, k) & fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, (k+1)) & k!=mpcLocationOne & k!=mpcLocationTwo & k!=mpcLocationOne-1 & k!=mpcLocationTwo-1 & k!=14 )
					{						 
						location=locationStr(i,sbRack,k);
						logger.info("fGetFirstFreeDoubleSlotDb Location: "+location);
						return location;
					}
			 }
			 
			 sbRack=1;
//			 mpcLocationOne=ResourcePlanConstants.SIX;
//				mpcLocationTwo=ResourcePlanConstants.NINE;
				try {
					String PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
							ResourcePlanConstants.ParamSubrack, ""+sbRack)[0].toString();
					mpcLocation=fGetControllerCardLocation(PrefChassisType);
					mpcLocationOne=mpcLocation[0];
					mpcLocationTwo=mpcLocation[1];
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 for (int k = 1; k <= ResourcePlanConstants.MaxSlotPerSbrack; k++) 
			 {
				 if(fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, k) & fCheckSlotAvailabilityDb(networkid, nodeid, i, sbRack, (k+1)) & k!=mpcLocationOne & k!=mpcLocationTwo & k!=mpcLocationOne-1 & k!=mpcLocationTwo-1 & k!=14 )
					{						 
						location=locationStr(i,sbRack,k);
						logger.info("fGetFirstFreeDoubleSlotDb Location: "+location);
						return location;
					}
			}
			 
			 
		 }
		logger.info("fGetFirstFreeDoubleSlotDb Location: "+location);	
		return location;
	}

	public void fSaveCardInDb(int networkid, int nodeid, int rackid, int sbrackid, int cardid, String cardtype, String direction, int wavelength, int demandid,int Eid, String Status, int CircuitId, String SerialNo)
	{
		//System.out.println(tag+"ResourcePlanning.fSaveCardInDb()");
//		String nodekey=""+networkid+"_"+nodeid;
		CardInfo card = new CardInfo(networkid,nodeid,rackid,sbrackid,cardid,cardtype,direction,wavelength, demandid,Eid,Status,CircuitId,SerialNo);		
		try 
		{			
			dbService.getCardInfoService().Insert(card);
		} catch (SQLException e) {	
			logger.info("ResourcePlanUtils.fSaveCardInDb()"+e.getMessage());
			e.printStackTrace();
		}

	}

	public void fsaveTunableFilterDb(int networkid, int nodeid, String dir, int demandid) throws SQLException
	{
		String location=fGetFirstFreeSlotDb(networkid, nodeid);
		System.out.println(" fsaveTunableFilterDb() location found : "+location);

		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		String MpcRedun = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatMpc);

		if(MpcRedun.equalsIgnoreCase(ResourcePlanConstants.Yes))
		{
			if(((location.contains("_6")||(location.contains("_9")))))
			{			 
				//add mpc first
				int[] id=locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],ResourcePlanConstants.CardMpc,"",0,0,fgetEIdFromCardType(ResourcePlanConstants.CardMpc),"",ResourcePlanConstants.ZERO,"");									
			}
			else
			{
				int[] id=locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],ResourcePlanConstants.Tunable_Filter_Card,"",0,0,fgetEIdFromCardType(ResourcePlanConstants.Tunable_Filter_Card),"",ResourcePlanConstants.ZERO,"");									
			}			
		}
		else
		{
			if(((location.contains("_6")/*||(location.contains("_9"))*/)))
			{			 
				//add mpc first
				int[] id=locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],ResourcePlanConstants.CardMpc,"",0,0,fgetEIdFromCardType(ResourcePlanConstants.CardMpc),"",ResourcePlanConstants.ZERO,"");									
			}
			else
			{
				int[] id=locationIds(location);
				fSaveCardInDb(networkid, nodeid, id[0],id[1],id[2],ResourcePlanConstants.Tunable_Filter_Card,"",0,0,fgetEIdFromCardType(ResourcePlanConstants.Tunable_Filter_Card),"",ResourcePlanConstants.ZERO,"");									
			}			
		}

	}

	public List <String> fCheckCircuitSet(String cirSetGf, String cirSetBf)
	{
		List <String> result = new ArrayList<String>();
		String cirsGf[]=cirSetGf.split(",");
		String cirsBf[]=cirSetBf.split(",");
		//		if(cirsGf.length<cirsBf.length)
		//		{
		//			result.add(new String("add"));			
		//			for (int i = 0; i < cirsBf.length; i++) 
		//			{
		//				if(!cirSetGf.contains(","+cirsBf[i]+","))
		//				{
		//					result.add(cirsBf[i]);
		//				}
		//			}
		//		}
		//		else if(cirsGf.length>cirsBf.length)
		//		{
		//			result.add(new String("delete"));	
		//			for (int i = 0; i < cirsGf.length; i++) 
		//			{
		//				if(!cirSetBf.contains(","+cirsGf[i]+","))
		//				{
		//					result.add(cirsGf[i]);
		//				}
		//			}
		//		}
		//		else if(cirsGf.length==cirsBf.length)
		//		{
		//			result.add(new String("same"));	
		//		}
		String str = "add";
		result.add(str);
		String str1="5";
		result.add(str1);

		System.out.println("ResourcePlanUtils.fCheckCircuitSet() "+result.get(0)+" "+result.get(1));
		return result;		
	}

	public Object[] fGetAddedCircuits(String cirSetGf, String cirSetBf)
	{
		List <String> result = new ArrayList<String>();
		Set <String> added = new HashSet<String>();
		Set <String> setGf = new HashSet<String>();
		Set <String> setBf = new HashSet<String>();

		String cirsGf[]=cirSetGf.split(",");
		String cirsBf[]=cirSetBf.split(",");
		for (int i = 0; i < cirsGf.length; i++) {
			setGf.add(cirsGf[i]);			
		}
		for (int i = 0; i < cirsBf.length; i++) {
			setBf.add(cirsBf[i]);			
		}

		setBf.removeAll(setGf);
		
		List sortedList = new ArrayList(setBf);
		Collections.sort(sortedList);
//		added=setBf;
		System.out.println("ResourcePlanUtils.fGetAddedCircuits()"+added.toString());
		return sortedList.toArray();
	}

	public Object[] fGetDeletedCircuits(String cirSetGf, String cirSetBf)
	{
		System.out.println("ResourcePlanUtils.fGetDeletedCircuits() cirSetGf: "+cirSetGf+" cirSetBf: "+cirSetBf);
		List <String> result = new ArrayList<String>();
		Set <String> deleted = new HashSet<String>();
		Set <String> setGf = new HashSet<String>();
		Set <String> setBf = new HashSet<String>();

		String cirsGf[]=cirSetGf.split(",");
		String cirsBf[]=cirSetBf.split(",");
		for (int i = 0; i < cirsGf.length; i++) {
			setGf.add(cirsGf[i]);			
		}
		for (int i = 0; i < cirsBf.length; i++) {
			setBf.add(cirsBf[i]);			
		}

		setGf.removeAll(setBf);
		deleted=setGf;
		System.out.println("ResourcePlanUtils.fGetDeletedCircuits()"+deleted.toString());
		return deleted.toArray();
	}

	public boolean Is10GTpnDemand(Demand dem)
	{
		boolean flag;
		int len=dem.getCircuitSet().split(",").length;
		if((dem.getRequiredTraffic()==MapConstants.G10)&&(dem.getLineRate().equalsIgnoreCase(MapConstants.Line10)&&(len==1)))
		{
			flag = true;			
		}
		else
		{
			flag = false;
		}
		System.out.println("ResourcePlanUtils.Is10GTpnDemand()"+flag);
		return flag;
	}

	public boolean CheckIsDcmTrayEmpty(int networkid, int nodeid, int rack , int sbrack, int card)
	{
		boolean flag = false;
		List<PortInfo> dcmPort = dbService.getPortInfoService().FindPortInfo(networkid, nodeid, rack, sbrack, card);
		for (int i = 0; i < dcmPort.size(); i++) {
			if((dcmPort.get(i).getDirection().equalsIgnoreCase(""))&&(dcmPort.get(i).getDirection().equalsIgnoreCase("")))
			{
				flag = true;
			}
			else
			{
				flag =false;
			}			
		}
		return flag;		
	}

	/**
	 * API for returning direction of WSS cards based on Wss Set No. for Add Drop Cards in Cd Roadm
	 * @date 4th May, 2018
	 * @author avinash 
	 */
	public static int[] fGetWssDirectionCdRoadm(int setNum,String cardType)
	{
		int []dir=new int[2];
		switch (cardType) {
		case ResourcePlanConstants.CardWss8x12:
		{
			dir[0]=100+setNum;
		}
		break;
		case ResourcePlanConstants.CardWss1x9:
		case ResourcePlanConstants.CardWss1x2x20:
		{
			switch (setNum) {
			case 1:
			{
				dir[0]=ResourcePlanConstants.WssSetOneLevelOneDir;
				dir[1]=ResourcePlanConstants.WssSetOneLevelTwoDir;
			}
			break;
			case 2:
			{
				dir[0]=ResourcePlanConstants.WssSetTwoLevelOneDir;
				dir[1]=ResourcePlanConstants.WssSetTwoLevelTwoDir;
			}
			break;
			case 3:
			{
				dir[0]=ResourcePlanConstants.WssSetThreeLevelOneDir;
				dir[1]=ResourcePlanConstants.WssSetThreeLevelTwoDir;
			}
			break;
			case 4:
			{
				dir[0]=ResourcePlanConstants.WssSetFourLevelOneDir;
				dir[1]=ResourcePlanConstants.WssSetFourLevelTwoDir;
			}
			break;

			default:
				logger.info("fGetWssDirectionCdRoadm :: setNum Received -->  "+setNum);
				break;
			}
		}
		break;

		default:
			logger.info("fGetWssDirectionCdRoadm :: CardtYpe Received -->  "+cardType);
			break;
		}

		return dir;		
	}

	public String getLinePortStr(String cardType,int PortNum)
	{
		String linePortStr="";
		switch(cardType) {

		case ResourcePlanConstants.CardMuxponderCGM:
			linePortStr="(ACT)"+"_"+ResourcePlanConstants.LINE;
			break;
		case ResourcePlanConstants.CardMuxponder10G:
		case ResourcePlanConstants.CardMuxponder200G:
			linePortStr=ResourcePlanConstants.LINE;
			break;
		case ResourcePlanConstants.CardTPN5x10G:
			linePortStr=ResourcePlanConstants.LINE+"_"+"Port_"+PortNum;
			break;
		case ResourcePlanConstants.CardMuxponderOPX:
		case ResourcePlanConstants.CardMuxponderOPXCGX:
			if(PortNum==ResourcePlanConstants.PRIMARY_LINE_PORT)
				linePortStr=ResourcePlanConstants.PRIMARY_LINE;
			else if(PortNum==ResourcePlanConstants.SECONDARY_LINE_PORT)
				linePortStr=ResourcePlanConstants.SECONDARY_LINE;
			else
				linePortStr="";
			break;
		default:
			linePortStr="";

		}


		return linePortStr;
	}
	
	
	/**
	 * API for returning direction of WSS cards based on Wss Set No. for Add Drop Cards in Cd Roadm
	 * @date 14th Aug, 2018
	 * @author avinash 
	 */
	public static int fGetMaxSwitchPortsforCardType(String cardType)
	{
		int maxPorts=0;
		switch (cardType) {
		case ResourcePlanConstants.CardWss8x12:
			maxPorts=ResourcePlanConstants.MaxSwitchPortsWss2x8x12;
			break;

		case ResourcePlanConstants.CardWss1x9:
			maxPorts=ResourcePlanConstants.MaxSwitchPortsWss2x1x9;
			break;

		case ResourcePlanConstants.CardWss1x2x20:
			maxPorts=ResourcePlanConstants.MaxSwitchPortsWss2x1x20;
			break;

		default:
			logger.info("fGetMaxSwitchPortsforCardType :: CardtYpe Received -->  "+cardType);
			break;
		}

		return maxPorts;	
	}
	
	
	//Function mapping of Ila subtype value to Ila subtype string
	public static int fGetIlaSubtype(String ilaType)
		{
			System.out.println("Fetching val for ILA subtype string  :"+ilaType);
			int  val;
			switch(ilaType)
			{
			case ResourcePlanConstants.AmpRamanHybrid:
				val=MapConstants.AmpRamanHybridSubtype;
			break;
			case ResourcePlanConstants.AmpRamanDra:
				val=MapConstants.AmpRamanDraSubtype;
			break;
			// Else 0 fro default case
			default:
				val=0;
				break;
			}
			
			System.out.println("fGetIlaSubtype Return:: "+val);
			return val;
		}
	
	/* Get First Free PA/BA Location */
	public String fGetFirstFreePaBaLocation(int networkid,int nodeid)
	{
		int degree=1;
		int rackid,sbrackid,cardid;
		String location = fgetLocationPaBa(degree);
		while(degree<=8) {
			int[]lp=locationIds(location);
			rackid=lp[0];
			sbrackid=lp[1];
			cardid=lp[2];
			
			if(fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid))
				return location;
			else {
				degree++;
				location=fgetLocationPaBa(degree);
			}
		}
		return location;
	}
	
	/* Get First Free PA/BA Location */
	public String fGetFirstFreeRamanLocation(int networkid,int nodeid)
	{
		int index=1;
		int rackid,sbrackid,cardid;
		String location;
		int maxRack=ResourcePlanConstants.MaxRack,
			maxSbrack=ResourcePlanConstants.MaxSbrack,
			maxSlots=ResourcePlanConstants.MaxSlotPerSbrack;
		
		for(int i=0;i<maxRack;i++) {
			rackid=i+1;
			
			sbrackid=ResourcePlanConstants.TWO;
			cardid=ResourcePlanConstants.ONE;
			if(fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid) 
				&& fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid+1))
			{
				location=locationStr(rackid, sbrackid, cardid);
				return location;
			}
			
			cardid=ResourcePlanConstants.THIRTEEN;
			if(fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid) 
					&& fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid+1))
				{
					location=locationStr(rackid, sbrackid, cardid);
					return location;
				}
			
			sbrackid=ResourcePlanConstants.THREE;
			cardid=ResourcePlanConstants.ONE;
			if(fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid) 
				&& fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid+1))
			{
				location=locationStr(rackid, sbrackid, cardid);
				return location;
			}
			
			cardid=ResourcePlanConstants.THIRTEEN;
			if(fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid) 
					&& fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid+1))
				{
					location=locationStr(rackid, sbrackid, cardid);
					return location;
				}
			
			sbrackid=ResourcePlanConstants.ONE;
			cardid=ResourcePlanConstants.ONE;
			if(fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid) 
				&& fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid+1))
			{
				location=locationStr(rackid, sbrackid, cardid);
				return location;
			}
			
			cardid=ResourcePlanConstants.THIRTEEN;
			if(fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid) 
					&& fCheckSlotAvailabilityDb(networkid, nodeid, rackid, sbrackid, cardid+1))
				{
					location=locationStr(rackid, sbrackid, cardid);
					return location;
				}

		}
		
		return null;
	}
	
	/* Get First Free PA/BA Location */
	public int[] fGetControllerCardLocation(String chassisType)
	{
		int []location=new int[2]; // 0 - First Slot , 1 - Second Slot
		switch (chassisType) {
		case ResourcePlanConstants.EmersionChassis:
		{
			location[0]=ResourcePlanConstants.SIX;
			location[1]=ResourcePlanConstants.NINE;
		}
		break;

		case ResourcePlanConstants.ComtelChassis:
		case ResourcePlanConstants.PentairChassis:
		{
			location[0]=ResourcePlanConstants.SEVEN;
			location[1]=ResourcePlanConstants.EIGHT;
		}
		break;
		
		case ResourcePlanConstants.SixSlotChassis:
		{
			location[0]=ResourcePlanConstants.ONE;
			location[1]=ResourcePlanConstants.TWO;
		}
		break;

		default:
			break;
		}
		return location;
		
	}
	
	/* Get First Free PA/BA Location */
	public int[] fGetCommonCardLocation(String chassisType,String cardType)
	{
		int []location=new int[2]; // 0 - First Slot , 1 - Second Slot
		switch (chassisType) {
		case ResourcePlanConstants.EmersionChassis:
		{
			switch (cardType) {
			case ResourcePlanConstants.CardSupy:
				location[0]=ResourcePlanConstants.SEVEN;
				location[1]=ResourcePlanConstants.EIGHT;
				break;
			case ResourcePlanConstants.CardMcs:
				location[0]=ResourcePlanConstants.SEVEN;
				break;
			case ResourcePlanConstants.CardEdfa:
				location[0]=ResourcePlanConstants.TEN;
				location[1]=ResourcePlanConstants.TEN;
				break;
			default:
				break;
			}
			
		}
		break;

		case ResourcePlanConstants.ComtelChassis:
		case ResourcePlanConstants.PentairChassis:
		{
			switch (cardType) {
			case ResourcePlanConstants.CardSupy:
				location[0]=ResourcePlanConstants.SIX;
				location[1]=ResourcePlanConstants.NINE;
				break;
			case ResourcePlanConstants.CardMcs:
				location[0]=ResourcePlanConstants.FIVE;
				break;
			case ResourcePlanConstants.CardEdfa:
				location[0]=ResourcePlanConstants.TEN;
				location[1]=ResourcePlanConstants.TEN;
				break;
			default:
				break;
			}
		}
		break;
		
		case ResourcePlanConstants.SixSlotChassis:
		{
			switch (cardType) {
			case ResourcePlanConstants.CardOcm1x2:
				location[0]=ResourcePlanConstants.FOUR;
				break;
			case ResourcePlanConstants.CardSupy:
				location[0]=ResourcePlanConstants.THREE;
//				location[1]=ResourcePlanConstants.TEN;
				break;
			default:
				break;
			}
			
		}
		break;

		default:
			break;
		}
		return location;
		
	}
	
	/* Get Mpn Pair Reference List based on Chassis type */
	public Map<Integer, Integer> fGetMpnSlotPairReferenceList(String chassisType)
	{
		switch (chassisType) {
		case ResourcePlanConstants.EmersionChassis:
		{
			return ResourcePlanConstants.MpnPairSlotsEmersionChassis;
		}
	
		case ResourcePlanConstants.ComtelChassis:
		case ResourcePlanConstants.PentairChassis:
		{
			return ResourcePlanConstants.MpnPairSlotsComtelPentairChassis;
		}
		
		default:
			return ResourcePlanConstants.MpnPairSlotsEmersionChassis;
		}
				
	}
	
}
