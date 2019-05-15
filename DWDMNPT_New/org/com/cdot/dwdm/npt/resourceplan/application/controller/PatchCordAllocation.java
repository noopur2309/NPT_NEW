package application.controller;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.constants.SMConstants;
import application.model.AggregatorClientMap;
import application.model.CardInfo;
import application.model.Circuit;
import application.model.Circuit10gAgg;
import application.model.EquipmentPreference;
import application.model.McsMap;
import application.model.NetworkRoute;
import application.model.Node;
import application.model.PatchCord;
import application.model.PortInfo;
import application.model.WssMap;
import application.model.YCablePortInfo;
import application.service.DbService;

@Service
public class PatchCordAllocation{
	DbService dbService;
	ResourcePlanUtils rpu;
	public static Logger logger = Logger.getLogger(ResourcePlanning.class.getName());

	public PatchCordAllocation(DbService dbService) {
		super();
		this.dbService = dbService;
		rpu = new ResourcePlanUtils(dbService);
		// logger = logger.getLogger(ResourcePlanning.class.getName());

	}


	public void fAssignPatchCordsROADM(int networkid, int nodeid) throws SQLException {
		int defaultLen = 1;
		String nodekey = "" + networkid + "_" + nodeid;
		logger.info("fAssignPatchCordsROADM nodekey " + nodekey);

		String proximity, ocmloc1="", ocmloc2="";
		int eid;
		try {

			// CardInfo cocm = dbService.getCardInfoService().FindCard(networkid, nodeid,
			// ResourcePlanConstants.CardOcm1x16, "");
			List<CardInfo> cocm = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
					ResourcePlanConstants.CardOcm1x8);
			if (cocm.size() > 1) {
				//ocmloc1 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
				//ocmloc2 = rpu.locationStr(cocm.get(1).getRack(), cocm.get(1).getSbrack(), cocm.get(1).getCard());
				if(MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._EAST || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._WEST
						|| MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._NORTH || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._SOUTH)
				{
					ocmloc1 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
					ocmloc2 = rpu.locationStr(cocm.get(1).getRack(), cocm.get(1).getSbrack(), cocm.get(1).getCard());
				}
				else
				{
					ocmloc1 = rpu.locationStr(cocm.get(1).getRack(), cocm.get(1).getSbrack(), cocm.get(1).getCard());
					ocmloc2 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
				}

			} else {
				if(MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._EAST || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._WEST
						|| MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._NORTH || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._SOUTH)
				{
					ocmloc1 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
					ocmloc2 = "";
				}
				else
				{
					ocmloc1 = "";
					ocmloc2 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
				}
			}

			// connecting PA/BA
			List<CardInfo> pabas = dbService.getCardInfoService().FindAllPaBaCards(networkid, nodeid);
			for (int j = 0; j < pabas.size(); j++) {
				String dir = pabas.get(j).getDirection();
				int dirInt=MapConstants.fGetDirectionStrVal(dir);
				String location = rpu.locationStr(pabas.get(j).getRack(), pabas.get(j).getSbrack(),
						pabas.get(j).getCard());
				String ePart="";
				String pabaCardType="";
				if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanDra))
				{
					pabaCardType=ResourcePlanConstants.AmpRamanDra;
					// ATX to CRX
					PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
							ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.AmpRamanDra, location,
							ResourcePlanConstants.Ila_Atx, ResourcePlanConstants.AmpRamanDra,location,
							ResourcePlanConstants.Ila_Crx, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc6);
					ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
				}
				else if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanHybrid))
				{
					pabaCardType=ResourcePlanConstants.AmpRamanHybrid;
					// ATX to CRX
					PatchCord pc7 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
							ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.AmpRamanHybrid, location,
							ResourcePlanConstants.Ila_Atx, ResourcePlanConstants.AmpRamanHybrid, location,
							ResourcePlanConstants.Ila_Crx, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc7);
					ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
				}
				else
				{
					pabaCardType=ResourcePlanConstants.CardPaBa;
					ePart=ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m;
				}
				// From Field (Pin)

				PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ePart),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
						ResourcePlanConstants.PaBA_FromField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
						ResourcePlanConstants.Field, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc1);

				// To field (Btx)
				PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
						ResourcePlanConstants.PaBA_ToField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
						ResourcePlanConstants.Field, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc2);

				// Supy Add (Sad)

				int supycardid = rpu.fgetSupyForDirection(pabas.get(j).getDirection());
				String supyloc = rpu.locationStr(ResourcePlanConstants.MainRack, ResourcePlanConstants.MainSbrack,
						supycardid);
				proximity = rpu.fcheckProximity(location, supyloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
				// logger.info("fAssignPatchCordsROADM proximity "+proximity);
				// logger.info("fAssignPatchCordsROADM eid "+eid);

				PatchCord pc3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_SupyAdd,
						ResourcePlanConstants.CardSupy, supyloc, ResourcePlanConstants.Supy_Tx + "_" + dir, defaultLen,
						dir, dir);
				dbService.getPatchCordService().Insert(pc3);

				// Supy Drop (Sdp)
				PatchCord pc4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_SupyDrop,
						ResourcePlanConstants.CardSupy, supyloc, ResourcePlanConstants.Supy_Rx + "_" + dir, defaultLen,
						dir, dir);
				dbService.getPatchCordService().Insert(pc4);

				// check if this direction carries 10G traffic or not
				PortInfo dcm = dbService.getPortInfoService().FindDcmModuleByDir(networkid, nodeid, dir);
				if (dcm != null) {
					// 10G traffic exists
					/**********************
					 * PABA to DCM to WSS
					 **********************/
					// To Drx (Ptx)
					String dcmloc = rpu.locationStr(dcm.getRack(), dcm.getSbrack(), dcm.getCard());
					proximity = rpu.fcheckProximity(location, dcmloc);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

					PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_PTx, dcm.getCardType(),
							dcmloc, ResourcePlanConstants.DCM_Drx + "_" + dir, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc5);

					CardInfo cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
					String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

					// From DCM (DTx) to WSS (WRC)
					PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), dcm.getCardType(), dcmloc,
							ResourcePlanConstants.DCM_Dtx + "_" + dir, cwss.getCardType(), wssloc,
							ResourcePlanConstants.Wss_RC, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc6);

					// From WSS (Brx)
					PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_FromWss,
							cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TC, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc7);

				} else// no 10G traffic on this direction
				{
					/**********
					 * PABA to WSS
					 *************/

					// To WSS (Ptx)
					CardInfo cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
					String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

					proximity = rpu.fcheckProximity(location, wssloc);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

					PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_PTx,
							cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_RC, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc5);

					// From WSS (Brx)
					PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_FromWss,
							cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TC, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc6);
				}

				/**************
				 * PABA to OCM
				 **************/
				if ((dirInt==MapConstants._EAST) || (dirInt==MapConstants._WEST) || (dirInt==MapConstants._NORTH) || (dirInt==MapConstants._SOUTH)) {
					proximity = rpu.fcheckProximity(location, ocmloc1);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
					// To OCM (PTM)
					PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_ToOcm,
							ResourcePlanConstants.CardOcm1x8, ocmloc1, ResourcePlanConstants.Ocm_Ptm + "_" + dir,
							defaultLen, dir);
					dbService.getPatchCordService().Insert(pc7);

					// From OCM (BTM)
					PatchCord pc8 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_FromOcm,
							ResourcePlanConstants.CardOcm1x8, ocmloc1, ResourcePlanConstants.Ocm_Btm + "_" + dir,
							defaultLen, dir);
					dbService.getPatchCordService().Insert(pc8);

				} else if ((dirInt==MapConstants._NE) || (dirInt==MapConstants._NW) || (dirInt==MapConstants._SE) || (dirInt==MapConstants._SW)) {
					proximity = rpu.fcheckProximity(location, ocmloc2);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
					// To OCM (PTM)
					PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_ToOcm,
							ResourcePlanConstants.CardOcm1x8, ocmloc2, ResourcePlanConstants.Ocm_Ptm + "_" + dir,
							defaultLen, dir);
					dbService.getPatchCordService().Insert(pc7);

					// From OCM (BTM)
					PatchCord pc8 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_FromOcm,
							ResourcePlanConstants.CardOcm1x8, ocmloc2, ResourcePlanConstants.Ocm_Btm + "_" + dir,
							defaultLen, dir);
					dbService.getPatchCordService().Insert(pc8);
				}
			}

			// connecting wss

			//			int Eqid = (int) dbService.getCardPreferenceService().FindPreferenceByFeature(networkid, nodeid,
			//					ResourcePlanConstants.CardFeatureWSS);
			//			EquipmentPreferences eqp = new EquipmentPreferences(dbService);
			//			String cardtypeWss = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
			//					ResourcePlanConstants.ParamDirection, "")[0].toString();

			//			String cardtypeWss = rpu.fgetCardTypeFromEId(Eqid);

			/* Find All WSS */
			List<CardInfo> wsss = dbService.getCardInfoService().FindDirectionWss(networkid, nodeid);

			for (int j = 0; j < wsss.size(); j++) {
				String dir = wsss.get(j).getDirection();
				String locationWss = rpu.locationStr(wsss.get(j).getRack(), wsss.get(j).getSbrack(),
						wsss.get(j).getCard());
				String locationEdfa = rpu.fgetEdfaLocationForDirection(networkid,nodeid,dir);
				/*************
				 * WSS to EDFA
				 *************/
				if(!locationEdfa.equalsIgnoreCase("0_0_0"))
				{
					proximity = rpu.fcheckProximity(locationWss, locationEdfa);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
					// To EDFA
					PatchCord pc9 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), wsss.get(j).getCardType(), locationWss,
							ResourcePlanConstants.Wss_ToEdfa + "_" + dir, ResourcePlanConstants.CardEdfa, locationEdfa,
							ResourcePlanConstants.Edfa_FromWss + "_" + dir, defaultLen, dir, dir);
					dbService.getPatchCordService().Insert(pc9);

					PatchCord pc10 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), wsss.get(j).getCardType(), locationWss,
							ResourcePlanConstants.Wss_FromEdfa + "_" + dir, ResourcePlanConstants.CardEdfa, locationEdfa,
							ResourcePlanConstants.Edfa_ToWss + "_" + dir, defaultLen, dir, dir);
					dbService.getPatchCordService().Insert(pc10);
				}
				else
				{
					System.out.println("Edfa Not Found!");
				}
				// connecting EDFA per direction
				//CardInfo mcs = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardMcs,
				//	"");

				/*************
				 * WSS to WSS
				 **************/
				for (int k = 0; k < wsss.size(); k++) {
					String otherdir = wsss.get(k).getDirection();

					System.out.println("WSS TO WSS CONNECTION***********");
					if (!dir.equalsIgnoreCase(otherdir)) {
						System.out.println("WSS TO WSS CONNECTION***********" + otherdir);
						String otherlocationWss = rpu.locationStr(wsss.get(k).getRack(), wsss.get(k).getSbrack(),
								wsss.get(k).getCard());

						proximity = rpu.fcheckProximity(locationWss, otherlocationWss);
						eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);

						PatchCord pc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), wsss.get(j).getCardType(),
								locationWss, ResourcePlanConstants.Wss_ToWss_CDC + "_" + dir, wsss.get(k).getCardType(),
								otherlocationWss, ResourcePlanConstants.Wss_FromWss_CDC + "_" + otherdir, defaultLen,
								dir,otherdir);
						dbService.getPatchCordService().Insert(pc);
					}

				}
			}
			List<McsMap> mcsToMpnTpnConnectionMap = dbService.getMcsMapService().Find(networkid, nodeid,true);
			for(int i=0;i<mcsToMpnTpnConnectionMap.size();i++)
			{
				String mcsloc = rpu.locationStr(mcsToMpnTpnConnectionMap.get(i).getRack(), mcsToMpnTpnConnectionMap.get(i).getSbrack(), mcsToMpnTpnConnectionMap.get(i).getCard());
				String dir = mcsToMpnTpnConnectionMap.get(i).getMcsCommonPort();
				proximity = rpu.fcheckProximity(mcsToMpnTpnConnectionMap.get(i).getEdfaLoc(), mcsloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
				PatchCord pc11 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardEdfa, mcsToMpnTpnConnectionMap.get(i).getEdfaLoc(), ResourcePlanConstants.Edfa_ToMcs + "_" + dir,
						ResourcePlanConstants.CardMcs, mcsloc, ResourcePlanConstants.Mcs_FromEdfa + "_" + dir,
						defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc11);

				PatchCord pc12 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardEdfa, mcsToMpnTpnConnectionMap.get(i).getEdfaLoc(), ResourcePlanConstants.Edfa_FromMcs + "_" + dir,
						ResourcePlanConstants.CardMcs, mcsloc, ResourcePlanConstants.Mcs_ToEdfa + "_" + dir, defaultLen,
						dir, dir);
				dbService.getPatchCordService().Insert(pc12);
			}
			/*
			 * int EqidMpn=(int)
			 * dbService.getCardPreferenceService().FindPreferenceByFeature(networkid,
			 * nodeid, ResourcePlanConstants.CardFeatureMPN); String cardtypeMpn =
			 * rpu.fgetCardTypeFromEId(EqidMpn); List<CardInfo> mpns =
			 * dbService.getCardInfoService().FindCardInfoByCardType(networkid,nodeid,
			 * cardtypeMpn);
			 * 
			 * CardInfo mcs = dbService.getCardInfoService().FindCard(networkid, nodeid,
			 * ResourcePlanConstants.CardMcs, ""); String mcsloc =
			 * rpu.locationStr(mcs.getRack(), mcs.getSbrack(), mcs.getCard()); for (int j =
			 * 0; j < mpns.size(); j++) { String dir=mpns.get(j).getDirection(); String
			 * locationMpn = rpu.locationStr(mpns.get(j).getRack(), mpns.get(j).getSbrack(),
			 * mpns.get(j).getCard()); //ACT or STB ports in CGM String
			 * sourcePortRx=ResourcePlanConstants.Mpn_Rx; String
			 * sourcePortTx=ResourcePlanConstants.Mpn_Tx;
			 * if(mpns.get(j).getCardType().contains("MPN(CGM)")) {
			 * sourcePortRx=sourcePortRx+"(ACT)"; sourcePortTx=sourcePortTx+"(ACT)"; }
			 * proximity= rpu.fcheckProximity(mcsloc,locationMpn); eid =
			 * rpu.fgetPatchCord(proximity,ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m)
			 * ;
			 * 
			 * PatchCord pc1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
			 * ResourcePlanConstants.CardMcs, mcsloc,
			 * ResourcePlanConstants.Mcs_ToWorkingTpn+"_"+dir, cardtypeMpn, locationMpn,
			 * sourcePortRx, defaultLen,dir,dir);
			 * dbService.getPatchCordService().Insert(pc1);
			 * 
			 * PatchCord pc2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
			 * ResourcePlanConstants.CardMcs, mcsloc,
			 * ResourcePlanConstants.Mcs_FromWorkingTpn+"_"+dir, cardtypeMpn, locationMpn,
			 * sourcePortTx, defaultLen,dir,dir);
			 * dbService.getPatchCordService().Insert(pc2); }
			 */
			/*
			 * int mcsCount=0; List<CardInfo> mpns =
			 * dbService.getCardInfoService().findMpns(networkid, nodeid); List<CardInfo>
			 * mcs = dbService.getCardInfoService().FindCards(networkid, nodeid,
			 * ResourcePlanConstants.CardMcs, ""); String mcsloc =
			 * rpu.locationStr(mcs.get(mcsCount).getRack(), mcs.get(mcsCount).getSbrack(),
			 * mcs.get(mcsCount).getCard()); String sourcePortRx=""; String sourcePortTx="";
			 * int mcsPortCount=0; for (int j = 0; j < mpns.size(); j++) { String
			 * cardtypeMpn=mpns.get(j).getCardType(); String dir=mpns.get(j).getDirection();
			 * String locationMpn = rpu.locationStr(mpns.get(j).getRack(),
			 * mpns.get(j).getSbrack(), mpns.get(j).getCard());
			 * 
			 * //ACT or STB ports in CGM
			 * 
			 * proximity= rpu.fcheckProximity(mcsloc,locationMpn); eid =
			 * rpu.fgetPatchCord(proximity,ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m)
			 * ; List <PortInfo> ports =
			 * dbService.getPortInfoService().FindPortInfo(networkid, nodeid,
			 * mpns.get(j).getRack(), mpns.get(j).getSbrack(),
			 * mpns.get(j).getCard(),true,true); for (int i = 0; i < ports.size(); i++) {
			 * 
			 * sourcePortRx=ResourcePlanConstants.Mpn_Rx;
			 * sourcePortTx=ResourcePlanConstants.Mpn_Tx;
			 * sourcePortRx=sourcePortRx+"_"+rpu.getLinePortStr(cardtypeMpn,ports.get(i).
			 * getLinePort());
			 * sourcePortTx=sourcePortTx+"_"+rpu.getLinePortStr(cardtypeMpn,ports.get(i).
			 * getLinePort()); PatchCord pc1 = new PatchCord(networkid,nodeid, eid,
			 * rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardMcs, mcsloc,
			 * ResourcePlanConstants.Mcs_ToWorkingTpn+"_"+(mcsPortCount+1), cardtypeMpn,
			 * locationMpn, sourcePortRx, defaultLen,dir,dir);
			 * dbService.getPatchCordService().Insert(pc1);
			 * 
			 * PatchCord pc2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
			 * ResourcePlanConstants.CardMcs, mcsloc,
			 * ResourcePlanConstants.Mcs_FromWorkingTpn+"_"+(mcsPortCount+1), cardtypeMpn,
			 * locationMpn, sourcePortTx, defaultLen,dir,dir);
			 * dbService.getPatchCordService().Insert(pc2);
			 * 
			 * } }
			 */
			List<McsMap> mcsToMpnTpnConnectionMap1 = dbService.getMcsMapService().Find(networkid, nodeid);
			int mcsPortNumber = 0;
			String cardtypeMpn, locationMpn, sourcePortRx, mcsloc, sourcePortTx;
			for (int i = 0; i < mcsToMpnTpnConnectionMap1.size(); i++) {
				String dir = mcsToMpnTpnConnectionMap1.get(i).getMcsCommonPort();
				String mpnData[] = mcsToMpnTpnConnectionMap1.get(i).getTpnLoc().split("_");
				int mpnRack = Integer.parseInt(mpnData[0]);
				int mpnSubRack = Integer.parseInt(mpnData[1]);
				int mpnCardId = Integer.parseInt(mpnData[2]);
				sourcePortRx = ResourcePlanConstants.Mpn_Rx;
				sourcePortTx = ResourcePlanConstants.Mpn_Tx;

				mcsloc = rpu.locationStr(mcsToMpnTpnConnectionMap1.get(i).getRack(),
						mcsToMpnTpnConnectionMap1.get(i).getSbrack(), mcsToMpnTpnConnectionMap1.get(i).getCard());
				locationMpn = mcsToMpnTpnConnectionMap1.get(i).getTpnLoc();
				cardtypeMpn = dbService.getCardInfoService().FindCard(networkid, nodeid, mpnRack, mpnSubRack, mpnCardId)
						.getCardType();
				sourcePortRx = sourcePortRx + "_"
						+ rpu.getLinePortStr(cardtypeMpn, mcsToMpnTpnConnectionMap1.get(i).getTpnLinePortNo());
				sourcePortTx = sourcePortTx + "_"
						+ rpu.getLinePortStr(cardtypeMpn, mcsToMpnTpnConnectionMap1.get(i).getTpnLinePortNo());

				proximity = rpu.fcheckProximity(mcsloc, locationMpn);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
				mcsPortNumber = mcsToMpnTpnConnectionMap1.get(i).getMcsSwitchPort();
				PatchCord pc1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardMcs,
						mcsloc, ResourcePlanConstants.Mcs_ToWorkingTpn + "_" + mcsPortNumber, cardtypeMpn, locationMpn,
						sourcePortRx, defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc1);

				PatchCord pc2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardMcs,
						mcsloc, ResourcePlanConstants.Mcs_FromWorkingTpn + "_" + mcsPortNumber, cardtypeMpn,
						locationMpn, sourcePortTx, defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc2);
			}
			// ethernet cables
			String locationCsccActive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerActive);
			PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
					ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc, locationCsccActive,
					ResourcePlanConstants.Cscc_ToLct, "", "", "", defaultLen, "");
			dbService.getPatchCordService().Insert(pc1);

			if (rpu.fcheckIsGne(networkid, nodeid) == true) {
				PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
						ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc,
						locationCsccActive, ResourcePlanConstants.Cscc_ToEms, "", "", "", defaultLen, "");
				dbService.getPatchCordService().Insert(pc2);
			}

			/*
			 * List<CardInfo> mpcs =
			 * dbService.getCardInfoService().FindCardInfoByCardType(networkid,nodeid,
			 * ResourcePlanConstants.CardMpc); for (int j = 0; j < mpcs.size(); j++) {
			 * String dir=mpcs.get(j).getDirection(); locationCsccActive =
			 * rpu.locationStr(ResourcePlanConstants.MainRack,
			 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerActive);
			 * String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
			 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerPassive)
			 * ; String locationMpc = rpu.locationStr(mpcs.get(j).getRack(),
			 * mpcs.get(j).getSbrack(), mpcs.get(j).getCard());
			 * if(locationMpc.contains("_6")) { PatchCord pc = new PatchCord(networkid,nodeid,
			 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
			 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
			 * ResourcePlanConstants.CardCscc, ""+locationCsccActive,
			 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
			 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
			 * dbService.getPatchCordService().Insert(pc); } else { PatchCord pc = new
			 * PatchCord(networkid,nodeid,
			 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
			 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
			 * ResourcePlanConstants.CardCscc, ""+locationCsccPassive,
			 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
			 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
			 * dbService.getPatchCordService().Insert(pc); } }
			 */
			// CSCC to MPC Connections
			cscc_to_mpc_connection(networkid, nodeid, nodekey, defaultLen);
			// CSCC to BayTop
			List<CardInfo> csccs = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
					ResourcePlanConstants.CardCscc);
			for (int j = 0; j < csccs.size(); j++) {
				String dir = csccs.get(j).getDirection();
				locationCsccActive = rpu.locationStr(ResourcePlanConstants.MainRack, ResourcePlanConstants.MainSbrack,
						ResourcePlanConstants.MainControllerActive);
				String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
						ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerPassive);
				String locationCscc = rpu.locationStr(csccs.get(j).getRack(), csccs.get(j).getSbrack(),
						csccs.get(j).getCard());
				if (locationCscc.contains("_6") | locationCscc.contains("_7")){
					PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
							ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
							"" + locationCsccActive, ResourcePlanConstants.Cscc_ToBayTop1, ResourcePlanConstants.BayTop,
							"", "", defaultLen, "");
					dbService.getPatchCordService().Insert(pc);
				} else {
					PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
							ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
							"" + locationCsccPassive, ResourcePlanConstants.Cscc_ToBayTop2,
							ResourcePlanConstants.BayTop, "", "", defaultLen, "");
					dbService.getPatchCordService().Insert(pc);
				}
			}
			mpnToFdfMapping(networkid, nodeid,defaultLen);
			/*Xgm(Agregator) to MPN connection*/
			List<AggregatorClientMap> aggregatorLinePortMapping = dbService.getPortInfoService().FindAggregatorLinePortMapping(networkid, nodeid);
			if(aggregatorLinePortMapping!=null)
			{
				for(int i=0;i<aggregatorLinePortMapping.size();i++)
				{
					System.out.println("10G Agg Mapping");
					String xgmLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getRack(), aggregatorLinePortMapping.get(i).getSbRack(),
							aggregatorLinePortMapping.get(i).getCard());
					String mpnLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getMpnRack(), aggregatorLinePortMapping.get(i).getMpnSbRack(),
							aggregatorLinePortMapping.get(i).getMpnCard());
					proximity = rpu.fcheckProximity(xgmLoc, mpnLoc);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
					System.out.println("eid:"+eid+" proximity:"+ proximity +" rpu.fgetCardTypeFromEId(eid):"+rpu.fgetCardTypeFromEId(eid));
					PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Tx",
							ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
							"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Rx", defaultLen, "");
					dbService.getPatchCordService().Insert(pc_1);
					PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Rx",
							ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
							"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Tx", defaultLen, "");
					dbService.getPatchCordService().Insert(pc_2);

				}
			}
			// client field patch cords for the unprotected lines or paths
			// List<CardInfo> mpnsAll =
			// dbService.getCardInfoService().findMpns(networkid,nodeid);
			// for (int i = 0; i < mpnsAll.size(); i++)
			// {
			// int demandid=mpnsAll.get(i).getDemandId();
			// Demand d = dbService.getDemandService().FindDemand(networkid, demandid);
			// if(d.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr))
			// {
			// String cirSet=d.getCircuitSet();
			// String cirs[] = cirSet.split(",");
			// for (int j = 0; j < cirs.length; j++)
			// {
			// String mpnloc=rpu.locationStr(mpnsAll.get(i).getRack(),
			// mpnsAll.get(i).getSbrack(), mpnsAll.get(i).getCard());
			// PortInfo port =
			// dbService.getPortInfoService().FindPortInfo(networkid,nodeid,mpnsAll.get(i).getRack(),
			// mpnsAll.get(i).getSbrack(),
			// mpnsAll.get(i).getCard(),Integer.parseInt(cirs[j]));
			// PatchCord pc = new PatchCord(networkid,nodeid,
			// rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
			// ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m,
			// mpnsAll.get(i).getCardType(), mpnloc,
			// ""+port.getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
			// ResourcePlanConstants.Field, defaultLen,"");
			// dbService.getPatchCordService().Insert(pc);
			// }
			// }
			// }

			// client field patch cords for the unprotected lines or paths
			update_ycable_patchcord_info(networkid,nodeid,defaultLen);

		} catch (Exception e) {
			logger.error("fAssignPatchCordsROADM " + e.getMessage());
			e.printStackTrace();
		}

	}
	public void mpnToFdfMapping(int networkid,int nodeid,int defaultLen) throws SQLException
	{
		List<PortInfo> ports = dbService.getPortInfoService().FindMpnToFdfMap(networkid, nodeid);
		System.out.println("NodeId:"+nodeid+" PortsCount:"+ports.size());
		if(ports!=null)
		{
			for (int j = 0;( j <ports.size()); j++) {
				System.out.println("Mpn Port No:" + ports.get(j).getMpnPortNo()+" Circuit Id :"+ports.get(j).getCircuitId());

				Circuit cir;
				if(ports.get(j).getMpnPortNo()==0)
					cir = dbService.getCircuitService().FindCircuit(networkid, ports.get(j).getCircuitId());
				else
					// 10g aggregator circuit
				{					
					Circuit10gAgg circuit10g=dbService.getCircuit10gAggService().FindCircuitByAggCircuitId(networkid, ports.get(j).getCircuitId());
					//					AggregatorClientMap circuitIdAggMapping=dbService.getCircuit10gAggService().FindMpnDetailsFromAggClientMapping(ports.get(j));
					cir = dbService.getCircuitService().FindCircuit(networkid, circuit10g.getCircuitId());
				}			

				String mpnLoc=rpu.locationStr(ports.get(j).getRack(), ports.get(j).getSbrack(),
						ports.get(j).getCard());
				System.out.println("getProtectionType:" + cir.getProtectionType());
				if (cir.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToOnePtcTypeStr)
						||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToTwoRPtcTypeStr)) {
					System.out.println("circuitId:" + cir.getCircuitId());
					PatchCord pc = new PatchCord(networkid,nodeid,
							rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m,ports.get(j).getCardType(), mpnLoc,
							"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc);
					PatchCord pc2 = new PatchCord(networkid,nodeid,
							rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, ports.get(j).getCardType(), mpnLoc,
							"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc2);
				} else if(cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusRPtcStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusTwoRPtcTypeStr)
						||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePtcStr)) {
					if(cir.getClientProtection().equalsIgnoreCase(SMConstants.YesStr))
					{
						if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.yCablePtcTypeStr))
						{
							//Handled below
						}
						else 
						{
							//OLP Case TODO
						}
					}
					else if(cir.getChannelProtection().equalsIgnoreCase(SMConstants.YesStr)) 
					{
						if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.opxPtcTypeStr))
						{
							PatchCord pc = new PatchCord(networkid,nodeid,
									rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
									ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, ports.get(j).getCardType(), mpnLoc,
									"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
									ResourcePlanConstants.Field, defaultLen, "");
							dbService.getPatchCordService().Insert(pc);
							PatchCord pc2 = new PatchCord(networkid,nodeid,
									rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
									ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, ports.get(j).getCardType(), mpnLoc,
									"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
									ResourcePlanConstants.Field, defaultLen, "");
							dbService.getPatchCordService().Insert(pc2);
						}
						else 
						{
							//OLP Case TODO
						}
					}
					else
					{

					}
				}

			}
		}
	}
	public void update_ycable_patchcord_info(int networkid, int nodeid,int defaultLen)
	{
		String proximity;
		int eid;
		/*List<CardInfo> mpnsAll = dbService.getCardInfoService().findMpns(networkid, nodeid);
		for (int i = 0; i < mpnsAll.size(); i++) {
			String mpnloc = rpu.locationStr(mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(),
					mpnsAll.get(i).getCard());

			List<PortInfo> ports = dbService.getPortInfoService().FindPortInfo(networkid, nodeid,
					mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(), mpnsAll.get(i).getCard(),true);
			System.out.println("NodeId:" + nodeid + " Mpn:" + mpnloc + " PortsCount:" + ports.size());
			for (int j = 0;( j <ports.size()); j++) {
				System.out.println("Port:" + ports.get(j).getCircuitId());
				Circuit cir = dbService.getCircuitService().FindCircuit(networkid, ports.get(j).getCircuitId());
				System.out.println("getProtectionType:" + cir.getProtectionType());
				if (cir.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToOnePtcTypeStr)
						||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToTwoRPtcTypeStr)) {
					System.out.println("circuitId:" + cir.getCircuitId());
					PatchCord pc = new PatchCord(networkid,nodeid,
							rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
							"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					try {
						dbService.getPatchCordService().Insert(pc);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					PatchCord pc2 = new PatchCord(networkid,nodeid,
							rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
							"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					try {
						dbService.getPatchCordService().Insert(pc2);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if(cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusRPtcStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusTwoRPtcTypeStr)
						||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePtcStr)) {
					if(cir.getClientProtection().equalsIgnoreCase(SMConstants.YesStr))
					{
						if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.yCablePtcTypeStr))
						{
							//Handled below
						}
						else 
						{
							//OLP Case TODO
						}
					}
					else if(cir.getChannelProtection().equalsIgnoreCase(SMConstants.YesStr)) 
					{
						if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.opxPtcTypeStr))
						{
							PatchCord pc = new PatchCord(networkid,nodeid,
									rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
									ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
									"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
									ResourcePlanConstants.Field, defaultLen, "");
							try {
								dbService.getPatchCordService().Insert(pc);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							PatchCord pc2 = new PatchCord(networkid,nodeid,
									rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
									ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
									"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
									ResourcePlanConstants.Field, defaultLen, "");
							try {
								dbService.getPatchCordService().Insert(pc2);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else 
						{
							//OLP Case TODO
						}
					}
					else
					{

					}
				}

			}
		}*/

		List<YCablePortInfo> YCabPorts = dbService.getYcablePortInfoService().FindAll(networkid, nodeid);
		for (int i = 0; i < YCabPorts.size(); i++) {
			int ids[] = rpu.locationIds(YCabPorts.get(i).getMpnLocN());

			CardInfo mpnN = dbService.getCardInfoService().FindCard(networkid, nodeid, ids[0], ids[1], ids[2]);

			String YCabLoc = rpu.locationStr(YCabPorts.get(i).getYCableRack(), YCabPorts.get(i).getYCableSbrack(),
					YCabPorts.get(i).getYCableCard());
			proximity = rpu.fcheckProximity(YCabLoc, YCabPorts.get(i).getMpnLocN());
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_02m);

			PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc,
					"" + YCabPorts.get(i).getYCablePort() + "_OP1(T)", mpnN.getCardType(),
					YCabPorts.get(i).getMpnLocN(), "Client " + YCabPorts.get(i).getMpnPortN() + "_Rx", defaultLen,
					"");
			try {
				dbService.getPatchCordService().Insert(pc_1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PatchCord pc_3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc,
					"" + YCabPorts.get(i).getYCablePort() + "_OP1(R)", mpnN.getCardType(),
					YCabPorts.get(i).getMpnLocN(), "Client " + YCabPorts.get(i).getMpnPortN() + "_Tx", defaultLen,
					"");
			try {
				dbService.getPatchCordService().Insert(pc_3);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (YCabPorts.get(i).getMpnLocP() != null) {
				int idsP[] = rpu.locationIds(YCabPorts.get(i).getMpnLocP());
				CardInfo mpnP = dbService.getCardInfoService().FindCard(networkid, nodeid, idsP[0], idsP[1],
						idsP[2]);

				PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP2(T)", mpnP.getCardType(),
						YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Rx",
						defaultLen, "");
				try {
					dbService.getPatchCordService().Insert(pc_2);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				PatchCord pc_4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP2(R)", mpnP.getCardType(),
						YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Tx",
						defaultLen, "");
				try {
					dbService.getPatchCordService().Insert(pc_4);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Y-Cable to Field
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m);
			PatchCord pc_5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(T)",
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					defaultLen, "");
			try {
				dbService.getPatchCordService().Insert(pc_5);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PatchCord pc_6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(R)",
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					defaultLen, "");
			try {
				dbService.getPatchCordService().Insert(pc_6);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/*
	 * Function assigns patchchord information for CD-ROADM
	 * 
	 * @param networkid
	 * @throws SQLException 
	 * */
	public void fAssignPatchCordsCDROADM(int networkid, int nodeid) throws SQLException {
		int defaultLen = 1;
		String nodekey = "" + networkid + "_" + nodeid;
		logger.info("fAssignPatchCordsROADM nodekey " + nodekey);

		String proximity, ocmloc1="", ocmloc2 = "";
		int eid;
		try {

			// CardInfo cocm = dbService.getCardInfoService().FindCard(networkid, nodeid,
			// ResourcePlanConstants.CardOcm1x16, "");
			List<CardInfo> cocm = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
					ResourcePlanConstants.CardOcm1x8);
			if (cocm.size() > 1) {
				//ocmloc1 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
				//ocmloc2 = rpu.locationStr(cocm.get(1).getRack(), cocm.get(1).getSbrack(), cocm.get(1).getCard());
				if(MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._EAST || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._WEST
						|| MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._NORTH || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._SOUTH)
				{
					ocmloc1 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
					ocmloc2 = rpu.locationStr(cocm.get(1).getRack(), cocm.get(1).getSbrack(), cocm.get(1).getCard());
				}
				else
				{
					ocmloc1 = rpu.locationStr(cocm.get(1).getRack(), cocm.get(1).getSbrack(), cocm.get(1).getCard());
					ocmloc2 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
				}

			} else {
				if(MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._EAST || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._WEST
						|| MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._NORTH || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._SOUTH)
				{
					ocmloc1 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
					ocmloc2 = "";
				}
				else
				{
					ocmloc1 = "";
					ocmloc2 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
				}
			}

			// connecting PA/BA
			List<CardInfo> pabas = dbService.getCardInfoService().FindAllPaBaCards(networkid, nodeid);
			for (int j = 0; j < pabas.size(); j++) {
				String dir = pabas.get(j).getDirection();
				String pabaCardType="";
				int dirInt=  MapConstants.fGetDirectionStrVal( dir);
				String location = rpu.locationStr(pabas.get(j).getRack(), pabas.get(j).getSbrack(),
						pabas.get(j).getCard());
				String ePart="";
				if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanDra))
				{
					pabaCardType=ResourcePlanConstants.AmpRamanDra;
					// ATX to CRX
					PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
							ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
							ResourcePlanConstants.Ila_Atx, pabaCardType,location,
							ResourcePlanConstants.Ila_Crx, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc6);
					ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
				}
				else if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanHybrid))
				{
					pabaCardType=ResourcePlanConstants.AmpRamanHybrid;
					// ATX to CRX
					PatchCord pc7 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
							ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
							ResourcePlanConstants.Ila_Atx, pabaCardType, location,
							ResourcePlanConstants.Ila_Crx, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc7);
					ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
				}
				else
				{
					pabaCardType=ResourcePlanConstants.CardPaBa;
					ePart=ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m;
				}
				// From Field (Pin)

				PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ePart),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
						ResourcePlanConstants.PaBA_FromField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
						ResourcePlanConstants.Field, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc1);

				// To field (Btx)
				PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
						ResourcePlanConstants.PaBA_ToField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
						ResourcePlanConstants.Field, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc2);

				// Supy Add (Sad)

				int supycardid = rpu.fgetSupyForDirection(pabas.get(j).getDirection());
				String supyloc = rpu.locationStr(ResourcePlanConstants.MainRack, ResourcePlanConstants.MainSbrack,
						supycardid);
				proximity = rpu.fcheckProximity(location, supyloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
				// logger.info("fAssignPatchCordsROADM proximity "+proximity);
				// logger.info("fAssignPatchCordsROADM eid "+eid);

				PatchCord pc3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_SupyAdd,
						ResourcePlanConstants.CardSupy, supyloc, ResourcePlanConstants.Supy_Tx + "_" + dir, defaultLen,
						dir, dir);
				dbService.getPatchCordService().Insert(pc3);

				// Supy Drop (Sdp)
				PatchCord pc4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_SupyDrop,
						ResourcePlanConstants.CardSupy, supyloc, ResourcePlanConstants.Supy_Rx + "_" + dir, defaultLen,
						dir, dir);
				dbService.getPatchCordService().Insert(pc4);

				// check if this direction carries 10G traffic or not
				PortInfo dcm = dbService.getPortInfoService().FindDcmModuleByDir(networkid, nodeid, dir);
				if (dcm != null) {
					// 10G traffic exists
					/**********************
					 * PABA to DCM to WSS
					 **********************/
					// To Drx (Ptx)
					String dcmloc = rpu.locationStr(dcm.getRack(), dcm.getSbrack(), dcm.getCard());
					proximity = rpu.fcheckProximity(location, dcmloc);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

					PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_PTx, dcm.getCardType(),
							dcmloc, ResourcePlanConstants.DCM_Drx + "_" + dir, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc5);

					CardInfo cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
					String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

					// From DCM (DTx) to WSS (WRC)
					PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), dcm.getCardType(), dcmloc,
							ResourcePlanConstants.DCM_Dtx + "_" + dir, cwss.getCardType(), wssloc,
							ResourcePlanConstants.Wss_RC, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc6);

					// From WSS (Brx)
					PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_FromWss,
							cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TC, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc7);

				} else// no 10G traffic on this direction
				{
					/**********
					 * PABA to WSS
					 *************/

					// To WSS (Ptx)
					CardInfo cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
					String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

					proximity = rpu.fcheckProximity(location, wssloc);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

					PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_PTx,
							cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_RC, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc5);

					// From WSS (Brx)
					PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_FromWss,
							cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TC, defaultLen, dir);
					dbService.getPatchCordService().Insert(pc6);
				}

				/**************
				 * PABA to OCM
				 **************/
				if ((dirInt==MapConstants._EAST) || (dirInt==MapConstants._WEST) || (dirInt==MapConstants._NORTH) || (dirInt==MapConstants._SOUTH)) {
					proximity = rpu.fcheckProximity(location, ocmloc1);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
					// To OCM (PTM)
					PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_ToOcm,
							ResourcePlanConstants.CardOcm1x8, ocmloc1, ResourcePlanConstants.Ocm_Ptm + "_" + dir,
							defaultLen, dir);
					dbService.getPatchCordService().Insert(pc7);

					// From OCM (BTM)
					PatchCord pc8 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_FromOcm,
							ResourcePlanConstants.CardOcm1x8, ocmloc1, ResourcePlanConstants.Ocm_Btm + "_" + dir,
							defaultLen, dir);
					dbService.getPatchCordService().Insert(pc8);

				} else if ((dirInt==MapConstants._NE) || (dirInt==MapConstants._NW) || (dirInt==MapConstants._SE) || (dirInt==MapConstants._SW)) {
					proximity = rpu.fcheckProximity(location, ocmloc2);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
					// To OCM (PTM)
					PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_ToOcm,
							ResourcePlanConstants.CardOcm1x8, ocmloc2, ResourcePlanConstants.Ocm_Ptm + "_" + dir,
							defaultLen, dir);
					dbService.getPatchCordService().Insert(pc7);

					// From OCM (BTM)
					PatchCord pc8 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							pabaCardType, location, ResourcePlanConstants.PaBA_FromOcm,
							ResourcePlanConstants.CardOcm1x8, ocmloc2, ResourcePlanConstants.Ocm_Btm + "_" + dir,
							defaultLen, dir);
					dbService.getPatchCordService().Insert(pc8);
				}
			}

			//Connecting EDFA to WSS-L1 ,WSS-L1 to WSS-L2 and WSS-L2 to MPN
			List<Integer> cardSetList=dbService.getWssMapService().FindWssSets(networkid, nodeid);		

			int numberOfSet=cardSetList.size();
			for(int i=0;i<numberOfSet;i++)
			{

				//List<String> wssL1CommonPort=dbService.getWssMapService().FindCommonPortUsed(networkid, nodeid,cardSetList.get(i));
				List<WssMap> wssL1PortMapping=dbService.getWssMapService().FindWssL1EdfaInfoBySetNo(networkid, nodeid,cardSetList.get(i));
				String locationWssL1=new String();
				String cardtypeWssL1=new String();
				String locationEdfa=new String();
				String dir=new String();
				if(wssL1PortMapping!=null)
				{
					//Connecting EDFA to WSS-L1

					for(int j=0;j<wssL1PortMapping.size();j++)
					{


						locationWssL1 = wssL1PortMapping.get(j).getWssLevel1Loc();
						locationEdfa = wssL1PortMapping.get(j).getEdfaLoc();
						cardtypeWssL1=ResourcePlanConstants.CardWss1x9;//Constant
						dir =wssL1PortMapping.get(j).getWssLevel1CommonPort();

						proximity = rpu.fcheckProximity(locationWssL1, locationEdfa);
						eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
						PatchCord pc9 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cardtypeWssL1, locationWssL1,
								ResourcePlanConstants.Wss_ToEdfa +"_"+wssL1PortMapping.get(j).getWssLevel1CommonPort(), ResourcePlanConstants.CardEdfa, locationEdfa,
								ResourcePlanConstants.Edfa_FromWss + "_" + dir, defaultLen, dir, dir);

						dbService.getPatchCordService().Insert(pc9);

						PatchCord pc10 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cardtypeWssL1, locationWssL1,
								ResourcePlanConstants.Wss_FromEdfa +"_"+wssL1PortMapping.get(j).getWssLevel1CommonPort(), ResourcePlanConstants.CardEdfa, locationEdfa,
								ResourcePlanConstants.Edfa_ToWss + "_" + dir, defaultLen, dir, dir);

						dbService.getPatchCordService().Insert(pc10);

					}
				}

				List<WssMap> wssLPortMapping=dbService.getWssMapService().FindBySetNo(networkid, nodeid,cardSetList.get(i));
				if(wssLPortMapping!=null)
				{

					for(int j=0;j<wssLPortMapping.size();j++)
					{
						String locationWssL2=new String();
						String cardtypeWssL2=new String();
						locationWssL1=wssLPortMapping.get(j).getWssLevel1Loc();
						cardtypeWssL1=ResourcePlanConstants.CardWss1x9;//Constant
						cardtypeWssL2=wssLPortMapping.get(0).getCardType();
						locationWssL2=rpu.locationStr(wssLPortMapping.get(0).getRack(), wssLPortMapping.get(0).getSbrack(),
								wssLPortMapping.get(0).getCard());

						if(j==0)
						{
							//Connecting WSS-L1 to WSS-L2

							proximity = rpu.fcheckProximity(locationWssL1, locationWssL2);
							eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);

							PatchCord pc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cardtypeWssL1,
									locationWssL1, ResourcePlanConstants.Wss_TC, cardtypeWssL2,
									locationWssL2, ResourcePlanConstants.Wss_RC , defaultLen, "");
							dbService.getPatchCordService().Insert(pc);

							PatchCord pc1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cardtypeWssL1,
									locationWssL1, ResourcePlanConstants.Wss_RC , cardtypeWssL2,
									locationWssL2, ResourcePlanConstants.Wss_TC , defaultLen, "");
							dbService.getPatchCordService().Insert(pc1);
						}
						//Connecting WSS-L2 to MPN
						String locationTpn=wssLPortMapping.get(j).getTpnLoc();
						String tpnInfo[]=locationTpn.split("_");
						int tpnRack=Integer.parseInt(tpnInfo[0]);
						int tpnSubRack=Integer.parseInt(tpnInfo[1]);
						int tpnCardId=Integer.parseInt(tpnInfo[2]);
						String sourcePortRx = ResourcePlanConstants.Mpn_Rx;
						String sourcePortTx = ResourcePlanConstants.Mpn_Tx;
						CardInfo cardTypeTpn=dbService.getCardInfoService().FindCard(networkid, nodeid, tpnRack, tpnSubRack, tpnCardId);

						sourcePortRx = sourcePortRx + "_"
								+ rpu.getLinePortStr(cardTypeTpn.getCardType(), wssLPortMapping.get(j).getTpnLinePortNo());
						sourcePortTx = sourcePortTx + "_"
								+ rpu.getLinePortStr(cardTypeTpn.getCardType(), wssLPortMapping.get(j).getTpnLinePortNo());

						proximity = rpu.fcheckProximity(locationWssL2, locationTpn);
						eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
						PatchCord pc9 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cardtypeWssL2, locationWssL2,
								ResourcePlanConstants.Wss_ToEdfa+"_"+wssLPortMapping.get(j).getWssLevel2SwitchPort(), cardTypeTpn.getCardType(), locationTpn,
								sourcePortTx + "_" + dir, defaultLen, dir, dir);
						dbService.getPatchCordService().Insert(pc9);

						PatchCord pc10 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cardtypeWssL2, locationWssL2,
								ResourcePlanConstants.Wss_FromEdfa+"_"+wssLPortMapping.get(j).getWssLevel2SwitchPort(), cardTypeTpn.getCardType(), locationTpn,
								sourcePortRx + "_" + dir, defaultLen, dir, dir);
						dbService.getPatchCordService().Insert(pc10);



					}


				}
				else
				{

				}


			}

			// connecting wss

			//			int Eqid = (int) dbService.getCardPreferenceService().FindPreferenceByFeature(networkid, nodeid,
			//					ResourcePlanConstants.CardFeatureWSS);
			//			EquipmentPreferences eqp = new EquipmentPreferences(dbService);
			//			String cardtypeWss = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatWss,
			//					ResourcePlanConstants.ParamDirection, "")[0].toString();

			//			String cardtypeWss = rpu.fgetCardTypeFromEId(Eqid);

			/* Find All WSS */
			List<CardInfo> wsss = dbService.getCardInfoService().FindDirectionWss(networkid, nodeid);

			for (int j = 0; j < wsss.size(); j++) {
				String dir = wsss.get(j).getDirection();
				String locationWss = rpu.locationStr(wsss.get(j).getRack(), wsss.get(j).getSbrack(),
						wsss.get(j).getCard());
				String locationEdfa = rpu.fgetEdfaLocationForDirection(networkid,nodeid,dir);

				/*************
				 * WSS to EDFA
				 *************/
				if(!locationEdfa.equalsIgnoreCase("0_0_0"))
				{
					proximity = rpu.fcheckProximity(locationWss, locationEdfa);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
					// To EDFA
					PatchCord pc9 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), wsss.get(j).getCardType(), locationWss,
							ResourcePlanConstants.Wss_ToEdfa + "_" + dir, ResourcePlanConstants.CardEdfa, locationEdfa,
							ResourcePlanConstants.Edfa_FromWss + "_" + dir, defaultLen, dir, dir);
					dbService.getPatchCordService().Insert(pc9);

					PatchCord pc10 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), wsss.get(j).getCardType(), locationWss,
							ResourcePlanConstants.Wss_FromEdfa + "_" + dir, ResourcePlanConstants.CardEdfa, locationEdfa,
							ResourcePlanConstants.Edfa_ToWss + "_" + dir, defaultLen, dir, dir);
					dbService.getPatchCordService().Insert(pc10);
				}
				else
				{
					System.out.println("Edfa not found!");
				}
				// connecting EDFA per direction
				//CardInfo mcs = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardMcs,
				//	"");

				/*************
				 * WSS to WSS
				 **************/
				for (int k = 0; k < wsss.size(); k++) {
					String otherdir = wsss.get(k).getDirection();

					System.out.println("WSS TO WSS CONNECTION***********");
					if (!dir.equalsIgnoreCase(otherdir)) {
						System.out.println("WSS TO WSS CONNECTION***********" + otherdir);
						String otherlocationWss = rpu.locationStr(wsss.get(k).getRack(), wsss.get(k).getSbrack(),
								wsss.get(k).getCard());

						proximity = rpu.fcheckProximity(locationWss, otherlocationWss);
						eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);

						PatchCord pc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), wsss.get(j).getCardType(),
								locationWss, ResourcePlanConstants.Wss_ToWss_CDC + "_" + dir, wsss.get(k).getCardType(),
								otherlocationWss, ResourcePlanConstants.Wss_FromWss_CDC + "_" + otherdir, defaultLen,
								dir,otherdir);
						dbService.getPatchCordService().Insert(pc);
					}

				}
			}
			//Connecting EDFA to WSS2812
			List<McsMap> mcsToMpnTpnConnectionMap = dbService.getMcsMapService().Find(networkid, nodeid,true);
			for(int i=0;i<mcsToMpnTpnConnectionMap.size();i++)
			{
				String mcsloc = rpu.locationStr(mcsToMpnTpnConnectionMap.get(i).getRack(), mcsToMpnTpnConnectionMap.get(i).getSbrack(), mcsToMpnTpnConnectionMap.get(i).getCard());
				String dir = mcsToMpnTpnConnectionMap.get(i).getMcsCommonPort();
				proximity = rpu.fcheckProximity(mcsToMpnTpnConnectionMap.get(i).getEdfaLoc(), mcsloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_02m);
				PatchCord pc11 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardEdfa, mcsToMpnTpnConnectionMap.get(i).getEdfaLoc(), ResourcePlanConstants.Edfa_ToMcs + "_" + dir,
						ResourcePlanConstants.CardWss8x12, mcsloc, ResourcePlanConstants.Mcs_FromEdfa + "_" + dir,
						defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc11);

				PatchCord pc12 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardEdfa, mcsToMpnTpnConnectionMap.get(i).getEdfaLoc(), ResourcePlanConstants.Edfa_FromMcs + "_" + dir,
						ResourcePlanConstants.CardWss8x12, mcsloc, ResourcePlanConstants.Mcs_ToEdfa + "_" + dir, defaultLen,
						dir, dir);
				dbService.getPatchCordService().Insert(pc12);
			}



			List<McsMap> mcsToMpnTpnConnectionMap1 = dbService.getMcsMapService().Find(networkid, nodeid);
			int mcsPortNumber = 0;
			String cardtypeMpn, locationMpn, sourcePortRx, mcsloc, sourcePortTx;
			for (int i = 0; i < mcsToMpnTpnConnectionMap1.size(); i++) {
				String dir = mcsToMpnTpnConnectionMap1.get(i).getMcsCommonPort();
				String mpnData[] = mcsToMpnTpnConnectionMap1.get(i).getTpnLoc().split("_");
				int mpnRack = Integer.parseInt(mpnData[0]);
				int mpnSubRack = Integer.parseInt(mpnData[1]);
				int mpnCardId = Integer.parseInt(mpnData[2]);
				sourcePortRx = ResourcePlanConstants.Mpn_Rx;
				sourcePortTx = ResourcePlanConstants.Mpn_Tx;

				mcsloc = rpu.locationStr(mcsToMpnTpnConnectionMap1.get(i).getRack(),
						mcsToMpnTpnConnectionMap1.get(i).getSbrack(), mcsToMpnTpnConnectionMap1.get(i).getCard());
				locationMpn = mcsToMpnTpnConnectionMap1.get(i).getTpnLoc();
				cardtypeMpn = dbService.getCardInfoService().FindCard(networkid, nodeid, mpnRack, mpnSubRack, mpnCardId)
						.getCardType();
				sourcePortRx = sourcePortRx + "_"
						+ rpu.getLinePortStr(cardtypeMpn, mcsToMpnTpnConnectionMap1.get(i).getTpnLinePortNo());
				sourcePortTx = sourcePortTx + "_"
						+ rpu.getLinePortStr(cardtypeMpn, mcsToMpnTpnConnectionMap1.get(i).getTpnLinePortNo());

				proximity = rpu.fcheckProximity(mcsloc, locationMpn);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
				mcsPortNumber = mcsToMpnTpnConnectionMap1.get(i).getMcsSwitchPort();
				PatchCord pc1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardWss8x12,
						mcsloc, ResourcePlanConstants.Mcs_ToWorkingTpn + "_" + mcsPortNumber, cardtypeMpn, locationMpn,
						sourcePortRx, defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc1);

				PatchCord pc2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardWss8x12,
						mcsloc, ResourcePlanConstants.Mcs_FromWorkingTpn + "_" + mcsPortNumber, cardtypeMpn,
						locationMpn, sourcePortTx, defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc2);
			}	

			// ethernet cables
			String locationCsccActive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerActive);
			PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
					ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc, locationCsccActive,
					ResourcePlanConstants.Cscc_ToLct, "", "", "", defaultLen, "");
			dbService.getPatchCordService().Insert(pc1);

			if (rpu.fcheckIsGne(networkid, nodeid) == true) {
				PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
						ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc,
						locationCsccActive, ResourcePlanConstants.Cscc_ToEms, "", "", "", defaultLen, "");
				dbService.getPatchCordService().Insert(pc2);
			}

			/*
			 * List<CardInfo> mpcs =
			 * dbService.getCardInfoService().FindCardInfoByCardType(networkid,nodeid,
			 * ResourcePlanConstants.CardMpc); for (int j = 0; j < mpcs.size(); j++) {
			 * String dir=mpcs.get(j).getDirection(); locationCsccActive =
			 * rpu.locationStr(ResourcePlanConstants.MainRack,
			 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerActive);
			 * String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
			 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerPassive)
			 * ; String locationMpc = rpu.locationStr(mpcs.get(j).getRack(),
			 * mpcs.get(j).getSbrack(), mpcs.get(j).getCard());
			 * if(locationMpc.contains("_6")) { PatchCord pc = new PatchCord(networkid,nodeid,
			 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
			 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
			 * ResourcePlanConstants.CardCscc, ""+locationCsccActive,
			 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
			 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
			 * dbService.getPatchCordService().Insert(pc); } else { PatchCord pc = new
			 * PatchCord(networkid,nodeid,
			 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
			 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
			 * ResourcePlanConstants.CardCscc, ""+locationCsccPassive,
			 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
			 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
			 * dbService.getPatchCordService().Insert(pc); } }
			 */
			// CSCC to MPC Connections
			cscc_to_mpc_connection(networkid, nodeid, nodekey, defaultLen);
			// CSCC to BayTop
			List<CardInfo> csccs = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
					ResourcePlanConstants.CardCscc);
			for (int j = 0; j < csccs.size(); j++) {
				String dir = csccs.get(j).getDirection();
				locationCsccActive = rpu.locationStr(ResourcePlanConstants.MainRack, ResourcePlanConstants.MainSbrack,
						ResourcePlanConstants.MainControllerActive);
				String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
						ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerPassive);
				String locationCscc = rpu.locationStr(csccs.get(j).getRack(), csccs.get(j).getSbrack(),
						csccs.get(j).getCard());
				if (locationCscc.contains("_6") | locationCscc.contains("_7")) {
					PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
							ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
							"" + locationCsccActive, ResourcePlanConstants.Cscc_ToBayTop1, ResourcePlanConstants.BayTop,
							"", "", defaultLen, "");
					dbService.getPatchCordService().Insert(pc);
				} else {
					PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
							ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
							"" + locationCsccPassive, ResourcePlanConstants.Cscc_ToBayTop2,
							ResourcePlanConstants.BayTop, "", "", defaultLen, "");
					dbService.getPatchCordService().Insert(pc);
				}
			}
			// client field patch cords for the unprotected lines or paths
			// List<CardInfo> mpnsAll =
			// dbService.getCardInfoService().findMpns(networkid,nodeid);
			// for (int i = 0; i < mpnsAll.size(); i++)
			// {
			// int demandid=mpnsAll.get(i).getDemandId();
			// Demand d = dbService.getDemandService().FindDemand(networkid, demandid);
			// if(d.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr))
			// {
			// String cirSet=d.getCircuitSet();
			// String cirs[] = cirSet.split(",");
			// for (int j = 0; j < cirs.length; j++)
			// {
			// String mpnloc=rpu.locationStr(mpnsAll.get(i).getRack(),
			// mpnsAll.get(i).getSbrack(), mpnsAll.get(i).getCard());
			// PortInfo port =
			// dbService.getPortInfoService().FindPortInfo(networkid,nodeid,mpnsAll.get(i).getRack(),
			// mpnsAll.get(i).getSbrack(),
			// mpnsAll.get(i).getCard(),Integer.parseInt(cirs[j]));
			// PatchCord pc = new PatchCord(networkid,nodeid,
			// rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
			// ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m,
			// mpnsAll.get(i).getCardType(), mpnloc,
			// ""+port.getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
			// ResourcePlanConstants.Field, defaultLen,"");
			// dbService.getPatchCordService().Insert(pc);
			// }
			// }
			// }

			// client field patch cords for the unprotected lines or paths
			/*List<CardInfo> mpnsAll = dbService.getCardInfoService().findMpns(networkid, nodeid);
			for (int i = 0; i < mpnsAll.size(); i++) {
				String mpnloc = rpu.locationStr(mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(),
						mpnsAll.get(i).getCard());

				List<PortInfo> ports = dbService.getPortInfoService().FindPortInfo(networkid, nodeid,
						mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(), mpnsAll.get(i).getCard());
				System.out.println("NodeId:" + nodeid + " Mpn:" + mpnloc + " PortsCount:" + ports.size());
				for (int j = 0; j < ports.size(); j++) {
					System.out.println("Port:" + ports.get(j).getCircuitId());
					Circuit cir = dbService.getCircuitService().FindCircuit(networkid, ports.get(j).getCircuitId());
					if (cir.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)  ) {
						System.out.println("circuitId:" + cir.getCircuitId());
						PatchCord pc = new PatchCord(networkid,nodeid,
								rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
								ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
								"" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
								ResourcePlanConstants.Field, defaultLen, "");
						dbService.getPatchCordService().Insert(pc);
					}

				}
			}*/

			/*List<CardInfo> mpnsAll = dbService.getCardInfoService().findMpns(networkid, nodeid);
			for (int i = 0; i < mpnsAll.size(); i++) {
				String mpnloc = rpu.locationStr(mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(),
						mpnsAll.get(i).getCard());

				List<PortInfo> ports = dbService.getPortInfoService().FindPortInfo(networkid, nodeid,
						mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(), mpnsAll.get(i).getCard(),true);
				System.out.println("NodeId:" + nodeid + " Mpn:" + mpnloc + " PortsCount:" + ports.size());
				for (int j = 0;( j <ports.size()) && ports.get(j).getMpnPortNo()==0; j++) {
					System.out.println("Port:" + ports.get(j).getCircuitId());
					Circuit cir = dbService.getCircuitService().FindCircuit(networkid, ports.get(j).getCircuitId());
					System.out.println("getProtectionType:" + cir.getProtectionType());
					if (cir.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToOnePtcTypeStr)
							||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToTwoRPtcTypeStr)) {
						System.out.println("circuitId:" + cir.getCircuitId());
						PatchCord pc = new PatchCord(networkid,nodeid,
								rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
								ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
								"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
								ResourcePlanConstants.Field, defaultLen, "");
						dbService.getPatchCordService().Insert(pc);
						PatchCord pc2 = new PatchCord(networkid,nodeid,
								rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
								ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
								"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
								ResourcePlanConstants.Field, defaultLen, "");
						dbService.getPatchCordService().Insert(pc2);
					} else if(cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusRPtcStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusTwoRPtcTypeStr)
							||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePtcStr)) {
						if(cir.getClientProtection().equalsIgnoreCase(SMConstants.YesStr))
						{
							if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.yCablePtcTypeStr))
							{
								//Handled below
							}
							else 
							{
								//OLP Case TODO
							}
						}
						else if(cir.getChannelProtection().equalsIgnoreCase(SMConstants.YesStr)) 
						{
							if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.opxPtcTypeStr))
							{
								PatchCord pc = new PatchCord(networkid,nodeid,
										rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
										ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
										"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
										ResourcePlanConstants.Field, defaultLen, "");
								dbService.getPatchCordService().Insert(pc);
								PatchCord pc2 = new PatchCord(networkid,nodeid,
										rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
										ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
										"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
										ResourcePlanConstants.Field, defaultLen, "");
								dbService.getPatchCordService().Insert(pc2);
							}
							else 
							{
								//OLP Case TODO
							}
						}
						else
						{

						}
					}

				}
			}*/
			mpnToFdfMapping(networkid, nodeid,defaultLen);
			/*Xgm(Agregator) to MPN connection*/
			List<AggregatorClientMap> aggregatorLinePortMapping = dbService.getPortInfoService().FindAggregatorLinePortMapping(networkid, nodeid);
			if(aggregatorLinePortMapping!=null)
			{
				for(int i=0;i<aggregatorLinePortMapping.size();i++)
				{
					System.out.println("10G Agg Mapping");
					String xgmLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getRack(), aggregatorLinePortMapping.get(i).getSbRack(),
							aggregatorLinePortMapping.get(i).getCard());
					String mpnLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getMpnRack(), aggregatorLinePortMapping.get(i).getMpnSbRack(),
							aggregatorLinePortMapping.get(i).getMpnCard());
					proximity = rpu.fcheckProximity(xgmLoc, mpnLoc);
					eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
					System.out.println("eid:"+eid+" proximity:"+ proximity +" rpu.fgetCardTypeFromEId(eid):"+rpu.fgetCardTypeFromEId(eid));
					PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Tx",
							ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
							"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Rx", defaultLen, "");
					dbService.getPatchCordService().Insert(pc_1);
					PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Rx",
							ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
							"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Tx", defaultLen, "");
					dbService.getPatchCordService().Insert(pc_2);

				}
			}
			List<YCablePortInfo> YCabPorts = dbService.getYcablePortInfoService().FindAll(networkid, nodeid);
			for (int i = 0; i < YCabPorts.size(); i++) {
				int ids[] = rpu.locationIds(YCabPorts.get(i).getMpnLocN());

				CardInfo mpnN = dbService.getCardInfoService().FindCard(networkid, nodeid, ids[0], ids[1], ids[2]);

				String YCabLoc = rpu.locationStr(YCabPorts.get(i).getYCableRack(), YCabPorts.get(i).getYCableSbrack(),
						YCabPorts.get(i).getYCableCard());
				proximity = rpu.fcheckProximity(YCabLoc, YCabPorts.get(i).getMpnLocN());
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_02m);

				PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP1(T)", mpnN.getCardType(),
						YCabPorts.get(i).getMpnLocN(), "Client " + YCabPorts.get(i).getMpnPortN() + "_Rx", defaultLen,
						"");
				dbService.getPatchCordService().Insert(pc_1);

				PatchCord pc_3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP1(R)", mpnN.getCardType(),
						YCabPorts.get(i).getMpnLocN(), "Client " + YCabPorts.get(i).getMpnPortN() + "_Tx", defaultLen,
						"");
				dbService.getPatchCordService().Insert(pc_3);

				if (YCabPorts.get(i).getMpnLocP() != null) {
					int idsP[] = rpu.locationIds(YCabPorts.get(i).getMpnLocP());
					CardInfo mpnP = dbService.getCardInfoService().FindCard(networkid, nodeid, idsP[0], idsP[1],
							idsP[2]);

					PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							ResourcePlanConstants.CardTypeYCable, YCabLoc,
							"" + YCabPorts.get(i).getYCablePort() + "_OP2(T)", mpnP.getCardType(),
							YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Rx",
							defaultLen, "");
					dbService.getPatchCordService().Insert(pc_2);

					PatchCord pc_4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
							ResourcePlanConstants.CardTypeYCable, YCabLoc,
							"" + YCabPorts.get(i).getYCablePort() + "_OP2(R)", mpnP.getCardType(),
							YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Tx",
							defaultLen, "");
					dbService.getPatchCordService().Insert(pc_4);
				}
				// Y-Cable to Field
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m);
				PatchCord pc_5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(T)",
						ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
						defaultLen, "");
				dbService.getPatchCordService().Insert(pc_5);

				PatchCord pc_6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(R)",
						ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
						defaultLen, "");
				dbService.getPatchCordService().Insert(pc_6);

			}

		} catch (Exception e) {
			logger.error("fAssignPatchCordsROADM " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Creates the Patch cord db tables, assigns the patchcords for ILA
	 * 
	 * @throws SQLException
	 * 
	 */
	public void fAssignPatchCordsILA(int networkid, int nodeid) throws SQLException {
		EquipmentPreferences eqp = new EquipmentPreferences(dbService);
		int defaultLen = 1;
		String nodekey = "" + networkid + "_" + nodeid;
		String proximity;
		int eid;

		CardInfo cocm = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardOcm1x2,
				"");
		String ocmloc;


		// connecting ILA Card
		List<CardInfo> ilas = dbService.getCardInfoService().FindAllIlaCards(networkid, nodeid);
		for (int j = 0; j < ilas.size(); j++) {
			String dir = ilas.get(j).getDirection();
			String ilaCardType="";
			String ilalocation = rpu.locationStr(ilas.get(j).getRack(), ilas.get(j).getSbrack(), ilas.get(j).getCard());
			String ePart="";
			if(ilas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpIlaRamanDra))
			{
				ilaCardType=ResourcePlanConstants.AmpIlaRamanDra;
				// ATX to CRX
				PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.AmpIlaRamanDra, ilalocation,
						ResourcePlanConstants.Ila_Atx, ResourcePlanConstants.AmpIlaRamanDra,ilalocation,
						ResourcePlanConstants.Ila_Crx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);
				ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
			}
			else if(ilas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpIlaRamanHybrid))
			{
				ilaCardType=ResourcePlanConstants.AmpIlaRamanHybrid;
				// ATX to CRX
				PatchCord pc7 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.AmpIlaRamanHybrid, ilalocation,
						ResourcePlanConstants.Ila_Atx, ResourcePlanConstants.AmpIlaRamanHybrid, ilalocation,
						ResourcePlanConstants.Ila_Crx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc7);
				ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
			}
			else
			{
				ilaCardType=ResourcePlanConstants.CardIla;
				ePart=ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m;
			}
			// Irx
			PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ePart),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ilaCardType, ilalocation,
					ResourcePlanConstants.Ila_Irx, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc1);

			// Itx
			PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ilaCardType, ilalocation,
					ResourcePlanConstants.Ila_Itx, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc2);

			try {
				ocmloc = rpu.locationStr(cocm.getRack(), cocm.getSbrack(), cocm.getCard());
				// ITM
				proximity = rpu.fcheckProximity(ilalocation, ocmloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				PatchCord pc3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ilaCardType,
						ilalocation, ResourcePlanConstants.Ila_Itm, ResourcePlanConstants.CardOcm1x2, ocmloc,
						ResourcePlanConstants.Ocm_Ptm + "_" + dir, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc3);
			} catch (Exception e) {
				// TODO: handle exception
				ocmloc=null;
			}


			// Supy Add (Sad)
			int supycardid = rpu.fgetSupyForDirection(dir);
			String supyloc ;
			try {
				supyloc= rpu.locationStr(ResourcePlanConstants.MainRack, ResourcePlanConstants.MainSbrack,
						supycardid);
				proximity = rpu.fcheckProximity(ilalocation, supyloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord pc4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ilaCardType,
						ilalocation, ResourcePlanConstants.Ila_SupyAdd, ResourcePlanConstants.CardSupy, supyloc,
						ResourcePlanConstants.Supy_Tx + "_" + dir, defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc4);

				// Supy Drop (Sdp)
				PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ilaCardType,
						ilalocation, ResourcePlanConstants.Ila_SupyDrop, ResourcePlanConstants.CardSupy, supyloc,
						ResourcePlanConstants.Supy_Rx + "_" + dir, defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc5);
			} catch (Exception e) {
				// TODO: handle exception
				supyloc=null;
			}


		}
		// CSCC to BayTop
		List<CardInfo> csccs = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
				ResourcePlanConstants.CardCscc);
		for (int k = 0; k < csccs.size(); k++) {
			// String dir=csccs.get(j).getDirection();
			String locationCsccActive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerActive);
			String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerPassive);
			String locationCscc = rpu.locationStr(csccs.get(k).getRack(), csccs.get(k).getSbrack(),
					csccs.get(k).getCard());
			int[] location=rpu.locationIds(locationCscc);

			String PrefChassisType = eqp.fGetPreferredEqType(networkid, nodeid, ResourcePlanConstants.CatChassisType,
					ResourcePlanConstants.ParamSubrack, ""+location[0]+"-"+location[1])[0].toString();
			logger.info("Pref Chassis Type in CSCC to Baytop connection"+PrefChassisType+" for Rack:"+location[0]+" and SbRack:"+location[1]+" and Card:"+location[2]);

			// Six Slot chassis has CSCC at location 1 and 2
			if(PrefChassisType.equals(ResourcePlanConstants.SixSlotChassis)) {
				if (location[2]==ResourcePlanConstants.ONE) {
					PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
							ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
							"" + locationCsccActive, ResourcePlanConstants.Cscc_ToBayTop1, ResourcePlanConstants.BayTop, "",
							"", defaultLen, "");
					dbService.getPatchCordService().Insert(pc6);
				}else {
					PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
							ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
							"" + locationCsccPassive, ResourcePlanConstants.Cscc_ToBayTop2, ResourcePlanConstants.BayTop,
							"", "", defaultLen, "");
					dbService.getPatchCordService().Insert(pc6);
				}

			}else {
				if (locationCscc.contains("_6") | locationCscc.contains("_7")) {
					PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
							ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
							"" + locationCsccActive, ResourcePlanConstants.Cscc_ToBayTop1, ResourcePlanConstants.BayTop, "",
							"", defaultLen, "");
					dbService.getPatchCordService().Insert(pc6);
				} else {
					PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
							ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
							"" + locationCsccPassive, ResourcePlanConstants.Cscc_ToBayTop2, ResourcePlanConstants.BayTop,
							"", "", defaultLen, "");
					dbService.getPatchCordService().Insert(pc6);
				}	
			}			

		}

		// ethernet cables

		PatchCord pc7 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
				ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc,
				"" + ResourcePlanConstants.MainControllerActive, ResourcePlanConstants.Cscc_ToLct, "", "", "",
				defaultLen, "");
		dbService.getPatchCordService().Insert(pc7);
		mpnToFdfMapping(networkid, nodeid,defaultLen);
		/*Xgm(Agregator) to MPN connection*/
		List<AggregatorClientMap> aggregatorLinePortMapping = dbService.getPortInfoService().FindAggregatorLinePortMapping(networkid, nodeid);
		if(aggregatorLinePortMapping!=null)
		{
			for(int i=0;i<aggregatorLinePortMapping.size();i++)
			{
				System.out.println("10G Agg Mapping");
				String xgmLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getRack(), aggregatorLinePortMapping.get(i).getSbRack(),
						aggregatorLinePortMapping.get(i).getCard());
				String mpnLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getMpnRack(), aggregatorLinePortMapping.get(i).getMpnSbRack(),
						aggregatorLinePortMapping.get(i).getMpnCard());
				proximity = rpu.fcheckProximity(xgmLoc, mpnLoc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
				System.out.println("eid:"+eid+" proximity:"+ proximity +" rpu.fgetCardTypeFromEId(eid):"+rpu.fgetCardTypeFromEId(eid));
				PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Tx",
						ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
						"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Rx", defaultLen, "");
				dbService.getPatchCordService().Insert(pc_1);
				PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Rx",
						ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
						"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Tx", defaultLen, "");
				dbService.getPatchCordService().Insert(pc_2);

			}
		}

	}

	/**
	 * Creates the Patch cord db tables, assigns the patchcords for ILA for 10G Line
	 * 
	 * @throws SQLException
	 * 
	 */
	public void fAssignPatchCordsILAFor10GLine(int networkid, int nodeid) throws SQLException {
		int defaultLen = 1;
		String nodekey = "" + networkid + "_" + nodeid;
		String proximity;
		int eid;

		CardInfo cocm = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardOcm1x2,
				"");
		String ocmloc = rpu.locationStr(cocm.getRack(), cocm.getSbrack(), cocm.getCard());
		// CardInfo dcm = dbService.getCardInfoService().FindCard(networkid, nodeid,
		// ResourcePlanConstants.DCM_Tray_Unit, "");
		// String dcmloc = rpu.locationStr(dcm.getRack(), dcm.getSbrack(),
		// dcm.getCard());

		// connecting MidStageAmp Card
		List<CardInfo> midSAmp = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
				ResourcePlanConstants.Mid_Stage_amplifier);
		for (int j = 0; j < midSAmp.size(); j++) {
			String dir = midSAmp.get(j).getDirection();
			String midSAmplocation = rpu.locationStr(midSAmp.get(j).getRack(), midSAmp.get(j).getSbrack(),
					midSAmp.get(j).getCard());

			// Mrx
			PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.Mid_Stage_amplifier,
					midSAmplocation, ResourcePlanConstants.MidSAmp_Mrx, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc1);

			// Mtx
			PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.Mid_Stage_amplifier,
					midSAmplocation, ResourcePlanConstants.MidSAmp_Mtx, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc2);

			// MTM
			proximity = rpu.fcheckProximity(midSAmplocation, ocmloc);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			PatchCord pc3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.Mid_Stage_amplifier, midSAmplocation, ResourcePlanConstants.MidSAmp_Mtm,
					ResourcePlanConstants.CardOcm1x2, ocmloc, ResourcePlanConstants.Ocm_Ptm, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc3);

			// Supy Add (Sad)
			int supycardid = rpu.fgetSupyForDirection(dir);
			String supyloc = rpu.locationStr(ResourcePlanConstants.MainRack, ResourcePlanConstants.MainSbrack,
					supycardid);

			proximity = rpu.fcheckProximity(midSAmplocation, supyloc);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

			PatchCord pc4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.Mid_Stage_amplifier, midSAmplocation, ResourcePlanConstants.Ila_SupyAdd,
					ResourcePlanConstants.CardSupy, supyloc, ResourcePlanConstants.Supy_Tx, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc4);

			// Supy Drop (Sdp)
			PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.Mid_Stage_amplifier, midSAmplocation, ResourcePlanConstants.Ila_SupyDrop,
					ResourcePlanConstants.CardSupy, supyloc, ResourcePlanConstants.Supy_Rx, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc5);

			// DCM and MidstageAmp
			PortInfo dcm = dbService.getPortInfoService().FindDcmModuleByDir(networkid, nodeid, dir);
			String dcmloc = rpu.locationStr(dcm.getRack(), dcm.getSbrack(), dcm.getCard());
			proximity = rpu.fcheckProximity(midSAmplocation, dcmloc);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
			PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.Mid_Stage_amplifier, midSAmplocation, ResourcePlanConstants.MidSAmp_Tx,
					ResourcePlanConstants.DCM_Tray_Unit, dcmloc, ResourcePlanConstants.DCM_Drx + "_" + dir, defaultLen,
					dir);
			dbService.getPatchCordService().Insert(pc6);

			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
			PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.Mid_Stage_amplifier, midSAmplocation, ResourcePlanConstants.MidSAmp_Rx,
					ResourcePlanConstants.DCM_Tray_Unit, dcmloc, ResourcePlanConstants.DCM_Dtx + "_" + dir, defaultLen,
					dir);
			dbService.getPatchCordService().Insert(pc7);
		}
		/*Xgm(Agregator) to MPN connection*/
		List<AggregatorClientMap> aggregatorLinePortMapping = dbService.getPortInfoService().FindAggregatorLinePortMapping(networkid, nodeid);
		if(aggregatorLinePortMapping!=null)
		{
			for(int i=0;i<aggregatorLinePortMapping.size();i++)
			{
				System.out.println("10G Agg Mapping");
				String xgmLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getRack(), aggregatorLinePortMapping.get(i).getSbRack(),
						aggregatorLinePortMapping.get(i).getCard());
				String mpnLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getMpnRack(), aggregatorLinePortMapping.get(i).getMpnSbRack(),
						aggregatorLinePortMapping.get(i).getMpnCard());
				proximity = rpu.fcheckProximity(xgmLoc, mpnLoc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
				System.out.println("eid:"+eid+" proximity:"+ proximity +" rpu.fgetCardTypeFromEId(eid):"+rpu.fgetCardTypeFromEId(eid));
				PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Tx",
						ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
						"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Rx", defaultLen, "");
				dbService.getPatchCordService().Insert(pc_1);
				PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Rx",
						ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
						"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Tx", defaultLen, "");
				dbService.getPatchCordService().Insert(pc_2);

			}
		}
	}

	/**
	 * Creates the Patch cord db tables, assigns the patchcords for TE
	 * 
	 * @throws SQLException
	 * 
	 */
	public void fAssignPatchCordsTE(int networkid, int nodeid) throws SQLException {
		int defaultLen = 1;
		String nodekey = "" + networkid + "_" + nodeid;
		String proximity;
		int eid;

		CardInfo cocm = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardOcm1x8,
				"");
		String ocmloc = rpu.locationStr(cocm.getRack(), cocm.getSbrack(), cocm.getCard());
		CardInfo cwss = null;

		// connecting PA/BA
		List<CardInfo> pabas = dbService.getCardInfoService().FindAllPaBaCards(networkid, nodeid);
		for (int j = 0; j < pabas.size(); j++) {
			String dir = pabas.get(j).getDirection();
			String location = rpu.locationStr(pabas.get(j).getRack(), pabas.get(j).getSbrack(), pabas.get(j).getCard());
			String ePart="";
			if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanDra))
			{
				// ATX to CRX
				PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.AmpRamanDra, location,
						ResourcePlanConstants.Ila_Atx, ResourcePlanConstants.AmpRamanDra,location,
						ResourcePlanConstants.Ila_Crx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);
				ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
			}
			else if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanHybrid))
			{
				// ATX to CRX
				PatchCord pc7 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.AmpRamanHybrid, location,
						ResourcePlanConstants.Ila_Atx, ResourcePlanConstants.AmpRamanHybrid, location,
						ResourcePlanConstants.Ila_Crx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc7);
				ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
			}
			else
			{
				ePart=ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m;
			}
			// From Field (Pin)

			PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ePart),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.CardPaBa, location,
					ResourcePlanConstants.PaBA_FromField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc1);


			// To field (Btx)
			PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.CardPaBa, location,
					ResourcePlanConstants.PaBA_ToField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc2);

			// Supy Add (Sad)

			// int supycardid = rpu.fgetSupyForDirection(pabas.get(j).getDirection());
			CardInfo supycard = dbService.getCardInfoService().FindCard(networkid, nodeid,
					ResourcePlanConstants.CardSupy, "");
			String supyloc = rpu.locationStr(supycard.getRack(), supycard.getSbrack(), supycard.getCard());
			proximity = rpu.fcheckProximity(location, supyloc);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
			// logger.info("fAssignPatchCordsROADM proximity "+proximity);
			// logger.info("fAssignPatchCordsROADM eid "+eid);

			PatchCord pc3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardPaBa,
					location, ResourcePlanConstants.PaBA_SupyAdd, ResourcePlanConstants.CardSupy, supyloc,
					ResourcePlanConstants.Supy_Tx + "_" + dir, defaultLen, dir, dir);
			dbService.getPatchCordService().Insert(pc3);

			// Supy Drop (Sdp)
			PatchCord pc4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardPaBa,
					location, ResourcePlanConstants.PaBA_SupyDrop, ResourcePlanConstants.CardSupy, supyloc,
					ResourcePlanConstants.Supy_Rx + "_" + dir, defaultLen, dir, dir);
			dbService.getPatchCordService().Insert(pc4);

			// check if this direction carries 10G traffic or not
			PortInfo dcm = dbService.getPortInfoService().FindDcmModuleByDir(networkid, nodeid, dir);
			if (dcm != null) {
				// 10G traffic exists
				/**********************
				 * PABA to DCM to WSS
				 **********************/
				// To Drx (Ptx)
				String dcmloc = rpu.locationStr(dcm.getRack(), dcm.getSbrack(), dcm.getCard());
				proximity = rpu.fcheckProximity(location, dcmloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardPaBa, location, ResourcePlanConstants.PaBA_PTx, dcm.getCardType(),
						dcmloc, ResourcePlanConstants.DCM_Drx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc5);

				cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
				String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

				// From DCM (DTx) to WSS (WRC)
				PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), dcm.getCardType(), dcmloc,
						ResourcePlanConstants.DCM_Dtx, cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_SIN,
						defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);

				// From WSS (Brx)
				PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardPaBa, location, ResourcePlanConstants.PaBA_FromWss,
						cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TX, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc7);

			} else// no 10G traffic on this direction
			{
				/**********
				 * PABA to WSS
				 *************/

				// To WSS (Ptx)
				cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
				String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

				proximity = rpu.fcheckProximity(location, wssloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardPaBa, location, ResourcePlanConstants.PaBA_PTx, cwss.getCardType(),
						wssloc, ResourcePlanConstants.Wss_SIN, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc5);

				// From WSS (Brx)
				PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardPaBa, location, ResourcePlanConstants.PaBA_FromWss,
						cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TX, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);
			}

			/**************
			 * PABA to OCM
			 **************/

			proximity = rpu.fcheckProximity(location, ocmloc);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			// To OCM (PTM)
			PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardPaBa,
					location, ResourcePlanConstants.PaBA_ToOcm, ResourcePlanConstants.CardOcm1x8, ocmloc,
					ResourcePlanConstants.Ocm_Ptm + "_" + dir, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc7);

			// From OCM (BTM)
			PatchCord pc8 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardPaBa,
					location, ResourcePlanConstants.PaBA_FromOcm, ResourcePlanConstants.CardOcm1x8, ocmloc,
					ResourcePlanConstants.Ocm_Btm + "_" + dir, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc8);
		}

		// ethernet cables

		PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
				ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc,
				"" + ResourcePlanConstants.MainControllerActive, ResourcePlanConstants.Cscc_ToLct, "", "", "",
				defaultLen, "");
		dbService.getPatchCordService().Insert(pc1);

		if (rpu.fcheckIsGne(networkid, nodeid) == true) {
			PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
					ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc,
					"" + ResourcePlanConstants.MainControllerActive, ResourcePlanConstants.Cscc_ToEms, "", "", "",
					defaultLen, "");
			dbService.getPatchCordService().Insert(pc2);
		}

		/*
		 * List<CardInfo> mpcs =
		 * dbService.getCardInfoService().FindCardInfoByCardType(networkid,nodeid,
		 * ResourcePlanConstants.CardMpc); for (int j = 0; j < mpcs.size(); j++) {
		 * String dir=mpcs.get(j).getDirection(); String locationCsccActive =
		 * rpu.locationStr(ResourcePlanConstants.MainRack,
		 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerActive);
		 * String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
		 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerPassive)
		 * ; String locationMpc = rpu.locationStr(mpcs.get(j).getRack(),
		 * mpcs.get(j).getSbrack(), mpcs.get(j).getCard());
		 * if(locationMpc.contains("_6")) { PatchCord pc = new PatchCord(networkid,nodeid,
		 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
		 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
		 * ResourcePlanConstants.CardCscc, ""+locationCsccActive,
		 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
		 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
		 * dbService.getPatchCordService().Insert(pc); } else { PatchCord pc = new
		 * PatchCord(networkid,nodeid,
		 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
		 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
		 * ResourcePlanConstants.CardCscc, ""+locationCsccPassive,
		 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
		 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
		 * dbService.getPatchCordService().Insert(pc); }
		 * 
		 * }
		 */
		// CSCC to MPC Connections
		cscc_to_mpc_connection(networkid, nodeid, nodekey, defaultLen);
		// CSCC to BayTop
		List<CardInfo> csccs = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
				ResourcePlanConstants.CardCscc);
		for (int k = 0; k < csccs.size(); k++) {
			// String dir=csccs.get(j).getDirection();
			String locationCsccActive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerActive);
			String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerPassive);
			String locationCscc = rpu.locationStr(csccs.get(k).getRack(), csccs.get(k).getSbrack(),
					csccs.get(k).getCard());			
			if (locationCscc.contains("_6") | locationCscc.contains("_7")) {
				PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
						ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
						"" + locationCsccActive, ResourcePlanConstants.Cscc_ToBayTop1, ResourcePlanConstants.BayTop, "",
						"", defaultLen, "");
				dbService.getPatchCordService().Insert(pc);
			} else {
				PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
						ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
						"" + locationCsccPassive, ResourcePlanConstants.Cscc_ToBayTop2, ResourcePlanConstants.BayTop,
						"", "", defaultLen, "");
				dbService.getPatchCordService().Insert(pc);
			}
		}
		// connecting MPN and MUX/DEMUX
		List<CardInfo> mpns = dbService.getCardInfoService().findMpns(networkid, nodeid);
		List<CardInfo> evenmux = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
				ResourcePlanConstants.Even_Mux_Demux_Unit);
		String locEvenmux = rpu.locationStr(evenmux.get(0).getRack(), evenmux.get(0).getSbrack(),
				evenmux.get(0).getCard());
		List<CardInfo> oddmux = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
				ResourcePlanConstants.Odd_Mux_Demux_Unit);
		String locOddmux = rpu.locationStr(oddmux.get(0).getRack(), oddmux.get(0).getSbrack(), oddmux.get(0).getCard());

		for (int j = 0; j < mpns.size(); j++) {
			String dir = mpns.get(j).getDirection();
			String mpntype = mpns.get(j).getCardType();
			String location = rpu.locationStr(mpns.get(j).getRack(), mpns.get(j).getSbrack(), mpns.get(j).getCard());

			proximity = rpu.fcheckProximity(location, locEvenmux);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			// ACT or STB ports in CGM
			String sourcePortRx = ResourcePlanConstants.Mpn_Rx;
			String sourcePortTx = ResourcePlanConstants.Mpn_Tx;
			if (mpntype.contains("MPN(CGM)")) {
				sourcePortRx = sourcePortRx + "(ACT)";
				sourcePortTx = sourcePortTx + "(ACT)";
			}
			if (mpns.get(j).getWavelength() % 2 == 0)// even wavelength
			{
				PatchCord pcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						sourcePortRx, ResourcePlanConstants.Even_Demux_Unit, locEvenmux,
						"Wave No " + mpns.get(j).getWavelength(), defaultLen, dir);
				dbService.getPatchCordService().Insert(pcr);

				PatchCord pct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						sourcePortTx, ResourcePlanConstants.Even_Mux_Unit, locEvenmux,
						"Wave No " + mpns.get(j).getWavelength(), defaultLen, dir);
				dbService.getPatchCordService().Insert(pct);
			} else {
				PatchCord oddpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						sourcePortRx, ResourcePlanConstants.Odd_Demux_Unit, locOddmux,
						"Wave No " + mpns.get(j).getWavelength(), defaultLen, dir);
				dbService.getPatchCordService().Insert(oddpcr);

				PatchCord oddpct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						sourcePortTx, ResourcePlanConstants.Odd_Mux_Unit, locOddmux,
						"Wave No " + mpns.get(j).getWavelength(), defaultLen, dir);
				dbService.getPatchCordService().Insert(oddpct);
			}
		}
		// ocm and MUX/DEMUX
		proximity = rpu.fcheckProximity(ocmloc, locOddmux);
		eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
		PatchCord ocmpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardOcm1x8,
				ocmloc, ResourcePlanConstants.Ocm_Mtm, ResourcePlanConstants.Even_Mux_Unit, locOddmux,
				ResourcePlanConstants.MUX_Tap, defaultLen, "");
		dbService.getPatchCordService().Insert(ocmpcr);

		proximity = rpu.fcheckProximity(ocmloc, locEvenmux);
		eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
		PatchCord ocmpc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardOcm1x8,
				ocmloc, ResourcePlanConstants.Ocm_Dtm, ResourcePlanConstants.Even_Demux_Unit, locEvenmux,
				ResourcePlanConstants.DEMUX_Tap, defaultLen, "");
		dbService.getPatchCordService().Insert(ocmpc);

		// joining even-odd mux/demux
		PatchCord muxpc = new PatchCord(networkid,nodeid,
				rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
				ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Mux_Unit, locEvenmux,
				ResourcePlanConstants.EVEN_MUX, ResourcePlanConstants.Odd_Mux_Unit, locOddmux,
				ResourcePlanConstants.ODD_MUX, defaultLen, "");
		dbService.getPatchCordService().Insert(muxpc);

		PatchCord muxpc1 = new PatchCord(networkid,nodeid,
				rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
				ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Demux_Unit, locOddmux,
				ResourcePlanConstants.EVEN_DEMUX, ResourcePlanConstants.Odd_Demux_Unit, locEvenmux,
				ResourcePlanConstants.ODD_DEMUX, defaultLen, "");
		dbService.getPatchCordService().Insert(muxpc1);

		// wss and even mux/demux
		String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

		proximity = rpu.fcheckProximity(wssloc, locEvenmux);
		eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

		PatchCord wadpc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwss.getCardType(), wssloc,
				ResourcePlanConstants.Wss_AD, ResourcePlanConstants.Even_Mux_Unit, locEvenmux,
				ResourcePlanConstants.MUX_op, defaultLen, cwss.getDirection());
		dbService.getPatchCordService().Insert(wadpc);

		PatchCord wdppc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwss.getCardType(), wssloc,
				ResourcePlanConstants.Wss_DP, ResourcePlanConstants.Even_Demux_Unit, locEvenmux,
				ResourcePlanConstants.DEMUX_In, defaultLen, cwss.getDirection());
		dbService.getPatchCordService().Insert(wdppc);

		// client field patch cords for the unprotected lines or paths
		/*
		 * List<CardInfo> mpnsAll =
		 * dbService.getCardInfoService().findMpns(networkid,nodeid); for (int i = 0; i
		 * < mpnsAll.size(); i++) { int demandid=mpnsAll.get(i).getDemandId(); Demand d
		 * = dbService.getDemandService().FindDemand(networkid, demandid);
		 * if(d.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)) {
		 * String cirSet=d.getCircuitSet(); String cirs[] = cirSet.split(","); for (int
		 * j = 0; j < cirs.length; j++) { String
		 * mpnloc=rpu.locationStr(mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(),
		 * mpnsAll.get(i).getCard()); PortInfo port =
		 * dbService.getPortInfoService().FindPortInfo(networkid,nodeid,mpnsAll.get(i).
		 * getRack(), mpnsAll.get(i).getSbrack(),
		 * mpnsAll.get(i).getCard(),Integer.parseInt(cirs[j])); PatchCord pc = new
		 * PatchCord(networkid,nodeid,
		 * rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
		 * ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m,
		 * mpnsAll.get(i).getCardType(), mpnloc, ""+port.getPort(),
		 * ResourcePlanConstants.Field, ResourcePlanConstants.Field,
		 * ResourcePlanConstants.Field, defaultLen,"");
		 * dbService.getPatchCordService().Insert(pc); } } }
		 */
		/*List<CardInfo> mpnsAll = dbService.getCardInfoService().findMpns(networkid, nodeid);
		for (int i = 0; i < mpnsAll.size(); i++) {
			String mpnloc = rpu.locationStr(mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(),
					mpnsAll.get(i).getCard());

			List<PortInfo> ports = dbService.getPortInfoService().FindPortInfo(networkid, nodeid,
					mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(), mpnsAll.get(i).getCard(),true);
			System.out.println("NodeId:" + nodeid + " Mpn:" + mpnloc + " PortsCount:" + ports.size());
			for (int j = 0;( j <ports.size()) && ports.get(j).getMpnPortNo()==0; j++) {
				System.out.println("Port:" + ports.get(j).getCircuitId());
				Circuit cir = dbService.getCircuitService().FindCircuit(networkid, ports.get(j).getCircuitId());
				System.out.println("getProtectionType:" + cir.getProtectionType());
				if (cir.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToOnePtcTypeStr)
						||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToTwoRPtcTypeStr)) {
					System.out.println("circuitId:" + cir.getCircuitId());
					PatchCord pc = new PatchCord(networkid,nodeid,
							rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
							"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc);
					PatchCord pc2 = new PatchCord(networkid,nodeid,
							rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
							"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc2);
				} else if(cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusRPtcStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusTwoRPtcTypeStr)
						||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePtcStr)) {
					if(cir.getClientProtection().equalsIgnoreCase(SMConstants.YesStr))
					{
						if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.yCablePtcTypeStr))
						{
							//Handled below
						}
						else 
						{
							//OLP Case TODO
						}
					}
					else if(cir.getChannelProtection().equalsIgnoreCase(SMConstants.YesStr)) 
					{
						if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.opxPtcTypeStr))
						{
							PatchCord pc = new PatchCord(networkid,nodeid,
									rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
									ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
									"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
									ResourcePlanConstants.Field, defaultLen, "");
							dbService.getPatchCordService().Insert(pc);
							PatchCord pc2 = new PatchCord(networkid,nodeid,
									rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
									ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
									"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
									ResourcePlanConstants.Field, defaultLen, "");
							dbService.getPatchCordService().Insert(pc2);
						}
						else 
						{
							//OLP Case TODO
						}
					}
					else
					{

					}
				}

			}
		}*/
		mpnToFdfMapping(networkid, nodeid,defaultLen);

		/*Xgm(Agregator) to MPN connection*/
		List<AggregatorClientMap> aggregatorLinePortMapping = dbService.getPortInfoService().FindAggregatorLinePortMapping(networkid, nodeid);
		if(aggregatorLinePortMapping!=null)
		{
			for(int i=0;i<aggregatorLinePortMapping.size();i++)
			{
				System.out.println("10G Agg Mapping");
				String xgmLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getRack(), aggregatorLinePortMapping.get(i).getSbRack(),
						aggregatorLinePortMapping.get(i).getCard());
				String mpnLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getMpnRack(), aggregatorLinePortMapping.get(i).getMpnSbRack(),
						aggregatorLinePortMapping.get(i).getMpnCard());
				proximity = rpu.fcheckProximity(xgmLoc, mpnLoc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
				System.out.println("eid:"+eid+" proximity:"+ proximity +" rpu.fgetCardTypeFromEId(eid):"+rpu.fgetCardTypeFromEId(eid));
				PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Tx",
						ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
						"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Rx", defaultLen, "");
				dbService.getPatchCordService().Insert(pc_1);
				PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Rx",
						ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
						"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Tx", defaultLen, "");
				dbService.getPatchCordService().Insert(pc_2);

			}
		}
		List<YCablePortInfo> YCabPorts = dbService.getYcablePortInfoService().FindAll(networkid, nodeid);
		for (int i = 0; i < YCabPorts.size(); i++) {
			int ids[] = rpu.locationIds(YCabPorts.get(i).getMpnLocN());

			CardInfo mpnN = dbService.getCardInfoService().FindCard(networkid, nodeid, ids[0], ids[1], ids[2]);

			String YCabLoc = rpu.locationStr(YCabPorts.get(i).getYCableRack(), YCabPorts.get(i).getYCableSbrack(),
					YCabPorts.get(i).getYCableCard());
			proximity = rpu.fcheckProximity(YCabLoc, YCabPorts.get(i).getMpnLocN());
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_02m);

			PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_OP1(T)",
					mpnN.getCardType(), YCabPorts.get(i).getMpnLocN(),
					"Client " + YCabPorts.get(i).getMpnPortN() + "_Rx", defaultLen, "");
			dbService.getPatchCordService().Insert(pc_1);

			PatchCord pc_3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_OP1(R)",
					mpnN.getCardType(), YCabPorts.get(i).getMpnLocN(),
					"Client " + YCabPorts.get(i).getMpnPortN() + "_Tx", defaultLen, "");
			dbService.getPatchCordService().Insert(pc_3);

			if (YCabPorts.get(i).getMpnLocP() != null) {
				int idsP[] = rpu.locationIds(YCabPorts.get(i).getMpnLocP());
				CardInfo mpnP = dbService.getCardInfoService().FindCard(networkid, nodeid, idsP[0], idsP[1], idsP[2]);

				PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP2(T)", mpnP.getCardType(),
						YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Rx", defaultLen,
						"");
				dbService.getPatchCordService().Insert(pc_2);

				PatchCord pc_4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP2(R)", mpnP.getCardType(),
						YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Tx", defaultLen,
						"");
				dbService.getPatchCordService().Insert(pc_4);
			}

			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m);
			PatchCord pc_5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(T)",
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field, defaultLen,
					"");
			dbService.getPatchCordService().Insert(pc_5);

			PatchCord pc_6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(R)",
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field, defaultLen,
					"");
			dbService.getPatchCordService().Insert(pc_6);

		}

	}

	/**
	 * Creates the Patch cord db tables, assigns the patchcords for BroadCast and
	 * Select
	 * 
	 * @throws SQLException
	 * 
	 */
	public void fAssignPatchCordsBnS(int networkid, int nodeid) throws SQLException {
		int defaultLen = 1;
		String nodekey = "" + networkid + "_" + nodeid;
		String proximity,ocmloc1="", ocmloc2 = "";
		int eid;

		Node n = dbService.getNodeService().FindNode(networkid, nodeid);
		String Capacity = n.getCapacity();
		CardInfo cocm = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardOcm1x8,
				"");
		String ocmloc;
		try {
			ocmloc = rpu.locationStr(cocm.getRack(), cocm.getSbrack(), cocm.getCard());
		} catch (Exception e) {
			// TODO: handle exception
			ocmloc=null;
		}
		/*List<CardInfo> cocm = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
				ResourcePlanConstants.CardOcm1x8);
		if (cocm.size() > 1) {
			//ocmloc1 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
			//ocmloc2 = rpu.locationStr(cocm.get(1).getRack(), cocm.get(1).getSbrack(), cocm.get(1).getCard());
			if(MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._EAST || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._WEST
					|| MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._NORTH || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._SOUTH)
			{
				ocmloc1 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
				ocmloc2 = rpu.locationStr(cocm.get(1).getRack(), cocm.get(1).getSbrack(), cocm.get(1).getCard());
			}
			else
			{
				ocmloc1 = rpu.locationStr(cocm.get(1).getRack(), cocm.get(1).getSbrack(), cocm.get(1).getCard());
				ocmloc2 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
			}

		} else {
			if(MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._EAST || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._WEST
					|| MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._NORTH || MapConstants.fGetDirectionStrVal(cocm.get(0).getDirection())==MapConstants._SOUTH)
			{
				ocmloc1 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
				ocmloc2 = "";
			}
			else
			{
				ocmloc1 = "";
				ocmloc2 = rpu.locationStr(cocm.get(0).getRack(), cocm.get(0).getSbrack(), cocm.get(0).getCard());
			}
		}
		 */
		CardInfo cwss, cwssEast, cwssWest = null;

		// connecting PA/BA
		List<CardInfo> pabas = dbService.getCardInfoService().FindAllPaBaCards(networkid, nodeid);
		for (int j = 0; j < pabas.size(); j++) {
			String dir = pabas.get(j).getDirection();
			String pabaCardType="";
			String location = rpu.locationStr(pabas.get(j).getRack(), pabas.get(j).getSbrack(), pabas.get(j).getCard());
			String ePart="";
			if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanDra))
			{
				pabaCardType=ResourcePlanConstants.AmpRamanDra;
				// ATX to CRX
				PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
						ResourcePlanConstants.Ila_Atx, pabaCardType,location,
						ResourcePlanConstants.Ila_Crx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);
				ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
			}
			else if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanHybrid))
			{
				pabaCardType=ResourcePlanConstants.AmpRamanHybrid;
				// ATX to CRX
				PatchCord pc7 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
						ResourcePlanConstants.Ila_Atx, pabaCardType, location,
						ResourcePlanConstants.Ila_Crx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc7);
				ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
			}
			else
			{
				pabaCardType=ResourcePlanConstants.CardPaBa;
				ePart=ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m;
			}
			// From Field (Pin)

			PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ePart),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
					ResourcePlanConstants.PaBA_FromField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc1);


			// To field (Btx)
			PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
					ResourcePlanConstants.PaBA_ToField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc2);

			// Supy Add (Sad)

			// int supycardid = rpu.fgetSupyForDirection(pabas.get(j).getDirection());
			CardInfo supycard = dbService.getCardInfoService().FindCard(networkid, nodeid,
					ResourcePlanConstants.CardSupy, "");
			String supyloc;
			try {
				supyloc = rpu.locationStr(supycard.getRack(), supycard.getSbrack(), supycard.getCard());
				proximity = rpu.fcheckProximity(location, supyloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
				// logger.info("fAssignPatchCordsROADM proximity "+proximity);
				// logger.info("fAssignPatchCordsROADM eid "+eid);

				PatchCord pc3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), pabaCardType,
						location, ResourcePlanConstants.PaBA_SupyAdd, ResourcePlanConstants.CardSupy, supyloc,
						ResourcePlanConstants.Supy_Tx + "_" + dir, defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc3);

				// Supy Drop (Sdp)
				PatchCord pc4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), pabaCardType,
						location, ResourcePlanConstants.PaBA_SupyDrop, ResourcePlanConstants.CardSupy, supyloc,
						ResourcePlanConstants.Supy_Rx + "_" + dir, defaultLen, dir, dir);
				dbService.getPatchCordService().Insert(pc4);
			} catch (Exception e) {
				// TODO: handle exception
				supyloc=null;
			}



			/************************************************
			 * //Supy Add (Sad)
			 * 
			 * int supycardid = rpu.fgetSupyForDirection(pabas.get(j).getDirection());
			 * String supyloc=rpu.locationStr(ResourcePlanConstants.MainRack,
			 * ResourcePlanConstants.MainSbrack, supycardid); proximity=
			 * rpu.fcheckProximity(location,supyloc); eid =
			 * rpu.fgetPatchCord(proximity,ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
			 * // logger.info("fAssignPatchCordsROADM proximity "+proximity); //
			 * logger.info("fAssignPatchCordsROADM eid "+eid);
			 * 
			 * PatchCord pc3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
			 * ResourcePlanConstants.CardPaBa, location, ResourcePlanConstants.PaBA_SupyAdd,
			 * ResourcePlanConstants.CardSupy, supyloc,
			 * ResourcePlanConstants.Supy_Tx+"_"+dir, defaultLen,dir);
			 * dbService.getPatchCordService().Insert(pc3);
			 * 
			 * //Supy Drop (Sdp) PatchCord pc4 = new PatchCord(networkid,nodeid,eid,
			 * rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardPaBa, location,
			 * ResourcePlanConstants.PaBA_SupyDrop, ResourcePlanConstants.CardSupy, supyloc,
			 * ResourcePlanConstants.Supy_Rx+"_"+dir, defaultLen,dir);
			 * dbService.getPatchCordService().Insert(pc4);
			 * 
			 */

			// check if this direction carries 10G traffic or not
			PortInfo dcm = dbService.getPortInfoService().FindDcmModuleByDir(networkid, nodeid, dir);
			if (dcm != null) {
				// 10G traffic exists
				/**********************
				 * PABA to DCM to WSS
				 **********************/

				// To Drx (Ptx)
				String dcmloc = rpu.locationStr(dcm.getRack(), dcm.getSbrack(), dcm.getCard());
				proximity = rpu.fcheckProximity(location, dcmloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_PTx, dcm.getCardType(),
						dcmloc, ResourcePlanConstants.DCM_Drx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc5);

				cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
				String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

				// From DCM (DTx) to WSS (WRC)
				PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), dcm.getCardType(), dcmloc,
						ResourcePlanConstants.DCM_Dtx, cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_SIN,
						defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);

				// From WSS (Brx)
				PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_FromWss,
						cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TX, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc7);

			} else// no 10G traffic on this direction
			{
				/**********
				 * PABA to WSS
				 *************/

				// To WSS (Ptx)
				cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
				String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

				proximity = rpu.fcheckProximity(location, wssloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_PTx, cwss.getCardType(),
						wssloc, ResourcePlanConstants.Wss_SIN, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc5);

				// From WSS (Brx)
				PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_FromWss,
						cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TX, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);
			}

			/**************
			 * PABA to OCM
			 **************/

			try {
				proximity = rpu.fcheckProximity(location, ocmloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				// To OCM (PTM)
				PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), pabaCardType,
						location, ResourcePlanConstants.PaBA_ToOcm, ResourcePlanConstants.CardOcm1x8, ocmloc,
						ResourcePlanConstants.Ocm_Ptm + "_" + dir, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc7);

				// From OCM (BTM)
				PatchCord pc8 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), pabaCardType,
						location, ResourcePlanConstants.PaBA_FromOcm, ResourcePlanConstants.CardOcm1x8, ocmloc,
						ResourcePlanConstants.Ocm_Btm + "_" + dir, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc8);	
			}catch (Exception e) {
				// TODO: handle exception
				logger.warn("OCM to PA/BA -- No Connection");
			}
		}

		// ethernet cables

		PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
				ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc,
				"" + ResourcePlanConstants.MainControllerActive, ResourcePlanConstants.Cscc_ToLct, "", "", "",
				defaultLen, "");
		dbService.getPatchCordService().Insert(pc1);

		if (rpu.fcheckIsGne(networkid, nodeid) == true) {
			PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
					ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc,
					"" + ResourcePlanConstants.MainControllerActive, ResourcePlanConstants.Cscc_ToEms, "", "", "",
					defaultLen, "");
			dbService.getPatchCordService().Insert(pc2);
		}

		/*
		 * List<CardInfo> mpcs =
		 * dbService.getCardInfoService().FindCardInfoByCardType(networkid,nodeid,
		 * ResourcePlanConstants.CardMpc); for (int j = 0; j < mpcs.size(); j++) {
		 * String dir=mpcs.get(j).getDirection(); String locationCsccActive =
		 * rpu.locationStr(ResourcePlanConstants.MainRack,
		 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerActive);
		 * String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
		 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerPassive)
		 * ; String locationMpc = rpu.locationStr(mpcs.get(j).getRack(),
		 * mpcs.get(j).getSbrack(), mpcs.get(j).getCard());
		 * 
		 * 
		 * if(locationMpc.contains("_6")) { PatchCord pc = new PatchCord(networkid,nodeid,
		 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
		 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
		 * ResourcePlanConstants.CardCscc, ""+locationCsccActive,
		 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
		 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
		 * dbService.getPatchCordService().Insert(pc); } else { PatchCord pc = new
		 * PatchCord(networkid,nodeid,
		 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
		 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
		 * ResourcePlanConstants.CardCscc, ""+locationCsccPassive,
		 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
		 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
		 * dbService.getPatchCordService().Insert(pc); } }
		 */

		// CSCC to MPC Connections
		cscc_to_mpc_connection(networkid, nodeid, nodekey, defaultLen);

		// CSCC to BayTop
		List<CardInfo> csccs = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid,
				ResourcePlanConstants.CardCscc);
		for (int j = 0; j < csccs.size(); j++) {
			String dir = csccs.get(j).getDirection();
			String locationCsccActive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerActive);
			String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerPassive);
			String locationCscc = rpu.locationStr(csccs.get(j).getRack(), csccs.get(j).getSbrack(),
					csccs.get(j).getCard());
			if (locationCscc.contains("_6") | locationCscc.contains("_7")) {
				PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
						ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
						"" + locationCsccActive, ResourcePlanConstants.Cscc_ToBayTop1, ResourcePlanConstants.BayTop, "",
						"", defaultLen, "");
				dbService.getPatchCordService().Insert(pc);
			} else {
				PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_Pot_Free_Cable),
						ResourcePlanConstants.PCord_Assembled_Pot_free_cable, ResourcePlanConstants.CardCscc,
						"" + locationCsccPassive, ResourcePlanConstants.Cscc_ToBayTop2, ResourcePlanConstants.BayTop,
						"", "", defaultLen, "");
				dbService.getPatchCordService().Insert(pc);
			}
		}
		// connecting wss-wss (WRX-WXP)
		cwssEast = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.EAST);
		cwssWest = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.WEST);
		if ((cwssEast != null) & (cwssWest != null)) {
			String wsslocEast = rpu.locationStr(cwssEast.getRack(), cwssEast.getSbrack(), cwssEast.getCard());
			String wsslocWest = rpu.locationStr(cwssWest.getRack(), cwssWest.getSbrack(), cwssWest.getCard());
			proximity = rpu.fcheckProximity(wsslocEast, wsslocWest);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);

			PatchCord pcE = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssEast.getCardType(),
					wsslocEast, ResourcePlanConstants.Wss_ToWss, cwssWest.getCardType(), wsslocWest,
					ResourcePlanConstants.Wss_FromWss, defaultLen, MapConstants.EAST);
			dbService.getPatchCordService().Insert(pcE);

			PatchCord pcW = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssWest.getCardType(),
					wsslocWest, ResourcePlanConstants.Wss_ToWss, cwssEast.getCardType(), wsslocEast,
					ResourcePlanConstants.Wss_FromWss, defaultLen, MapConstants.WEST);
			dbService.getPatchCordService().Insert(pcW);

		}

		if (Capacity.equalsIgnoreCase("3")) {
			// connecting East MPN and East MUX/DEMUX
			List<CardInfo> mpnseast = dbService.getCardInfoService().FindMpnsByDir(networkid, nodeid,
					MapConstants.EAST);
			List<CardInfo> evenmuxeast = dbService.getCardInfoService().FindCards(networkid, nodeid,
					ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST);
			String locEvenmuxEast = rpu.locationStr(evenmuxeast.get(0).getRack(), evenmuxeast.get(0).getSbrack(),
					evenmuxeast.get(0).getCard());
			List<CardInfo> oddmuxeast = dbService.getCardInfoService().FindCards(networkid, nodeid,
					ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.EAST);
			String locOddmuxEast = rpu.locationStr(oddmuxeast.get(0).getRack(), oddmuxeast.get(0).getSbrack(),
					oddmuxeast.get(0).getCard());

			for (int j = 0; j < mpnseast.size(); j++) {
				String dir = mpnseast.get(j).getDirection();
				String mpntype = mpnseast.get(j).getCardType();
				String location = rpu.locationStr(mpnseast.get(j).getRack(), mpnseast.get(j).getSbrack(),
						mpnseast.get(j).getCard());

				proximity = rpu.fcheckProximity(location, locEvenmuxEast);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				// ACT or STB ports in CGM
				String sourcePortRx = ResourcePlanConstants.Mpn_Rx;
				String sourcePortTx = ResourcePlanConstants.Mpn_Tx;
				if (mpntype.contains("MPN(CGM)")) {
					sourcePortRx = sourcePortRx + "(ACT)";
					sourcePortTx = sourcePortTx + "(ACT)";
				}
				if (mpnseast.get(j).getWavelength() % 2 == 0)// even wavelength
				{

					PatchCord pcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortRx, ResourcePlanConstants.Even_Demux_Unit, locEvenmuxEast,
							"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
					dbService.getPatchCordService().Insert(pcr);

					PatchCord pct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortTx, ResourcePlanConstants.Even_Mux_Unit, locEvenmuxEast,
							"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
					dbService.getPatchCordService().Insert(pct);
				} else {
					PatchCord oddpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortRx, ResourcePlanConstants.Odd_Demux_Unit, locOddmuxEast,
							"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
					dbService.getPatchCordService().Insert(oddpcr);

					PatchCord oddpct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortTx, ResourcePlanConstants.Odd_Mux_Unit, locOddmuxEast,
							"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
					dbService.getPatchCordService().Insert(oddpct);
				}
			}

			// ocm and MUX/DEMUX
			try {
				proximity = rpu.fcheckProximity(ocmloc, locOddmuxEast);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				PatchCord ocmpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Mtm + "_east",
						ResourcePlanConstants.Even_Mux_Unit, locOddmuxEast, ResourcePlanConstants.MUX_Tap, defaultLen, "");
				dbService.getPatchCordService().Insert(ocmpcr);

				proximity = rpu.fcheckProximity(ocmloc, locEvenmuxEast);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				PatchCord ocmpc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Dtm + "_east",
						ResourcePlanConstants.Even_Demux_Unit, locEvenmuxEast, ResourcePlanConstants.DEMUX_Tap, defaultLen,
						"");
				dbService.getPatchCordService().Insert(ocmpc);
			} catch (Exception e) {
				// TODO: handle exception
				logger.warn("OCM to MUX-DEMUX -- No Connection");
			}


			// joining even-odd mux/demux
			PatchCord muxpc = new PatchCord(networkid,nodeid,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
					ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Mux_Unit,
					locEvenmuxEast, ResourcePlanConstants.EVEN_MUX, ResourcePlanConstants.Odd_Mux_Unit, locOddmuxEast,
					ResourcePlanConstants.ODD_MUX, defaultLen, MapConstants.EAST);
			dbService.getPatchCordService().Insert(muxpc);

			PatchCord muxpc1 = new PatchCord(networkid,nodeid,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
					ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Demux_Unit,
					locEvenmuxEast, ResourcePlanConstants.EVEN_DEMUX, ResourcePlanConstants.Odd_Demux_Unit,
					locOddmuxEast, ResourcePlanConstants.ODD_DEMUX, defaultLen, MapConstants.EAST);
			dbService.getPatchCordService().Insert(muxpc1);

			// wss and even mux/demux
			cwssEast = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.EAST);
			if (cwssEast != null) {
				String wsslocEast = rpu.locationStr(cwssEast.getRack(), cwssEast.getSbrack(), cwssEast.getCard());

				proximity = rpu.fcheckProximity(wsslocEast, locEvenmuxEast);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord wadpc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssEast.getCardType(),
						wsslocEast, ResourcePlanConstants.Wss_AD, ResourcePlanConstants.Even_Mux_Unit, locEvenmuxEast,
						ResourcePlanConstants.MUX_op, defaultLen, cwssEast.getDirection());
				dbService.getPatchCordService().Insert(wadpc);

				PatchCord wdppc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssEast.getCardType(),
						wsslocEast, ResourcePlanConstants.Wss_DP, ResourcePlanConstants.Even_Demux_Unit, locEvenmuxEast,
						ResourcePlanConstants.DEMUX_In, defaultLen, cwssEast.getDirection());
				dbService.getPatchCordService().Insert(wdppc);
			}

			/////////////////

			// connecting West MPN and West MUX/DEMUX
			List<CardInfo> mpnswest = dbService.getCardInfoService().FindMpnsByDir(networkid, nodeid,
					MapConstants.WEST);
			List<CardInfo> evenmuxwest = dbService.getCardInfoService().FindCards(networkid, nodeid,
					ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST);
			String locEvenmuxWest = rpu.locationStr(evenmuxwest.get(0).getRack(), evenmuxwest.get(0).getSbrack(),
					evenmuxwest.get(0).getCard());
			List<CardInfo> oddmuxwest = dbService.getCardInfoService().FindCards(networkid, nodeid,
					ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.WEST);
			String locOddmuxWest = rpu.locationStr(oddmuxwest.get(0).getRack(), oddmuxwest.get(0).getSbrack(),
					oddmuxwest.get(0).getCard());

			for (int j = 0; j < mpnswest.size(); j++) {
				String dir = mpnswest.get(j).getDirection();
				String mpntype = mpnswest.get(j).getCardType();
				String location = rpu.locationStr(mpnswest.get(j).getRack(), mpnswest.get(j).getSbrack(),
						mpnswest.get(j).getCard());

				proximity = rpu.fcheckProximity(location, locEvenmuxWest);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				// ACT or STB ports in CGM
				String sourcePortRx = ResourcePlanConstants.Mpn_Rx;
				String sourcePortTx = ResourcePlanConstants.Mpn_Tx;
				if (mpntype.contains("MPN(CGM)")) {
					sourcePortRx = sourcePortRx + "(ACT)";
					sourcePortTx = sourcePortTx + "(ACT)";
				}
				if (mpnswest.get(j).getWavelength() % 2 == 0)// even wavelength
				{
					PatchCord pcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortRx, ResourcePlanConstants.Even_Demux_Unit, locEvenmuxWest,
							"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
					dbService.getPatchCordService().Insert(pcr);

					PatchCord pct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortTx, ResourcePlanConstants.Even_Mux_Unit, locEvenmuxWest,
							"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
					dbService.getPatchCordService().Insert(pct);
				} else {
					PatchCord oddpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortRx, ResourcePlanConstants.Odd_Demux_Unit, locOddmuxWest,
							"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
					dbService.getPatchCordService().Insert(oddpcr);

					PatchCord oddpct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortTx, ResourcePlanConstants.Odd_Mux_Unit, locOddmuxWest,
							"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
					dbService.getPatchCordService().Insert(oddpct);
				}
			}


			try {
				// ocm and MUX/DEMUX
				proximity = rpu.fcheckProximity(ocmloc, locOddmuxWest);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				PatchCord ocmpcrwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Mtm + "_west",
						ResourcePlanConstants.Even_Mux_Unit, locOddmuxWest, ResourcePlanConstants.MUX_Tap, defaultLen, "");
				dbService.getPatchCordService().Insert(ocmpcrwest);

				proximity = rpu.fcheckProximity(ocmloc, locEvenmuxWest);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				PatchCord ocmpcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Dtm + "_west",
						ResourcePlanConstants.Even_Demux_Unit, locEvenmuxWest, ResourcePlanConstants.DEMUX_Tap, defaultLen,
						"");
				dbService.getPatchCordService().Insert(ocmpcwest);
			} catch (Exception e) {
				// TODO: handle exception
			}


			// joining even-odd mux/demux
			PatchCord muxpcwest = new PatchCord(networkid,nodeid,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
					ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Mux_Unit,
					locEvenmuxWest, ResourcePlanConstants.EVEN_MUX, ResourcePlanConstants.Odd_Mux_Unit, locOddmuxWest,
					ResourcePlanConstants.ODD_MUX, defaultLen, MapConstants.WEST);
			dbService.getPatchCordService().Insert(muxpcwest);

			PatchCord muxpc1west = new PatchCord(networkid,nodeid,
					rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
					ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Demux_Unit,
					locEvenmuxWest, ResourcePlanConstants.EVEN_DEMUX, ResourcePlanConstants.Odd_Demux_Unit,
					locOddmuxWest, ResourcePlanConstants.ODD_DEMUX, defaultLen, MapConstants.WEST);
			dbService.getPatchCordService().Insert(muxpc1west);

			// wss and even mux/demux
			cwssWest = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.WEST);
			if (cwssWest != null) {
				String wsslocWest = rpu.locationStr(cwssWest.getRack(), cwssWest.getSbrack(), cwssWest.getCard());

				proximity = rpu.fcheckProximity(wsslocWest, locEvenmuxWest);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord wadpcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssWest.getCardType(),
						wsslocWest, ResourcePlanConstants.Wss_AD, ResourcePlanConstants.Even_Mux_Unit, locEvenmuxWest,
						ResourcePlanConstants.MUX_op, defaultLen, cwssWest.getDirection());
				dbService.getPatchCordService().Insert(wadpcwest);

				PatchCord wdppcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssWest.getCardType(),
						wsslocWest, ResourcePlanConstants.Wss_DP, ResourcePlanConstants.Even_Demux_Unit, locEvenmuxWest,
						ResourcePlanConstants.DEMUX_In, defaultLen, cwssWest.getDirection());
				dbService.getPatchCordService().Insert(wdppcwest);
			}

		}
		else if (Capacity.equalsIgnoreCase("1")) {

			// connecting East MPN and East MUX/DEMUX
			List<CardInfo> mpnseast = dbService.getCardInfoService().FindMpnsByDir(networkid, nodeid,
					MapConstants.EAST);
			List<CardInfo> evenmuxeast = dbService.getCardInfoService().FindCards(networkid, nodeid,
					ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST);
			String locEvenmuxEast = rpu.locationStr(evenmuxeast.get(0).getRack(), evenmuxeast.get(0).getSbrack(),
					evenmuxeast.get(0).getCard()); 
			// List<CardInfo> oddmuxeast =
			// dbService.getCardInfoService().FindCards(networkid,nodeid,ResourcePlanConstants.Odd_Mux_Demux_Unit,MapConstants.EAST);
			// String locOddmuxEast = rpu.locationStr(oddmuxeast.get(0).getRack(),
			// oddmuxeast.get(0).getSbrack(), oddmuxeast.get(0).getCard());

			for (int j = 0; j < mpnseast.size(); j++) {
				String dir = mpnseast.get(j).getDirection();
				String mpntype = mpnseast.get(j).getCardType();
				String location = rpu.locationStr(mpnseast.get(j).getRack(), mpnseast.get(j).getSbrack(),
						mpnseast.get(j).getCard());

				proximity = rpu.fcheckProximity(location, locEvenmuxEast);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				// ACT or STB ports in CGM
				String sourcePortRx = ResourcePlanConstants.Mpn_Rx;
				String sourcePortTx = ResourcePlanConstants.Mpn_Tx;
				if (mpntype.contains("MPN(CGM)")) {
					sourcePortRx = sourcePortRx + "(ACT)";
					sourcePortTx = sourcePortTx + "(ACT)";
				}
				if (mpnseast.get(j).getWavelength() % 2 == 0)// even wavelength
				{
					PatchCord pcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortRx, ResourcePlanConstants.Even_Demux_Unit, locEvenmuxEast,
							"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
					dbService.getPatchCordService().Insert(pcr);

					PatchCord pct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortTx, ResourcePlanConstants.Even_Mux_Unit, locEvenmuxEast,
							"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
					dbService.getPatchCordService().Insert(pct);
				}
				// else
				// {
				// PatchCord oddpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				// mpntype, location,
				// ResourcePlanConstants.Mpn_Rx, ResourcePlanConstants.Odd_Mux_Demux_Unit,
				// locOddmuxEast, "", defaultLen,MapConstants.EAST);
				// dbService.getPatchCordService().Insert(oddpcr);
				//
				// PatchCord oddpct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				// mpntype, location,
				// ResourcePlanConstants.Mpn_Tx, ResourcePlanConstants.Odd_Mux_Demux_Unit,
				// locOddmuxEast, "", defaultLen,MapConstants.EAST);
				// dbService.getPatchCordService().Insert(oddpct);
				// }
			}

			// ocm and MUX/DEMUX
			// proximity= rpu.fcheckProximity(ocmloc,locOddmuxEast);
			// eid =
			// rpu.fgetPatchCord(proximity,ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			// PatchCord ocmpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
			// ResourcePlanConstants.CardOcm1x8, ocmloc,
			// ResourcePlanConstants.Ocm_Mtm, ResourcePlanConstants.Even_Mux_Demux_Unit,
			// locOddmuxEast, ResourcePlanConstants.MUX_Tap, defaultLen,"");
			// dbService.getPatchCordService().Insert(ocmpcr);

			proximity = rpu.fcheckProximity(ocmloc, locEvenmuxEast);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			PatchCord ocmpc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Dtm,
					ResourcePlanConstants.Even_Demux_Unit, locEvenmuxEast, ResourcePlanConstants.DEMUX_Tap, defaultLen,
					"");
			dbService.getPatchCordService().Insert(ocmpc);

			// joining even-odd mux/demux
			// PatchCord muxpc = new PatchCord(networkid,nodeid,
			// rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
			// ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m,
			// ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxEast,
			// "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxEast, "",
			// defaultLen,MapConstants.EAST);
			// dbService.getPatchCordService().Insert(muxpc);

			// PatchCord muxpc1 = new PatchCord(networkid,nodeid,
			// rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
			// ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m,
			// ResourcePlanConstants.Even_Mux_Demux_Unit, locOddmuxEast,
			// "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locEvenmuxEast, "",
			// defaultLen,MapConstants.EAST);
			// dbService.getPatchCordService().Insert(muxpc1);

			// wss and even mux/demux
			cwssEast = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.EAST);
			String wsslocEast = rpu.locationStr(cwssEast.getRack(), cwssEast.getSbrack(), cwssEast.getCard());

			proximity = rpu.fcheckProximity(wsslocEast, locEvenmuxEast);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

			PatchCord wadpcEast = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssEast.getCardType(),
					wsslocEast, ResourcePlanConstants.Wss_AD, ResourcePlanConstants.Even_Mux_Unit, locEvenmuxEast,
					ResourcePlanConstants.MUX_op, defaultLen, cwssEast.getDirection());
			dbService.getPatchCordService().Insert(wadpcEast);

			PatchCord wdppcEast = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssEast.getCardType(),
					wsslocEast, ResourcePlanConstants.Wss_DP, ResourcePlanConstants.Even_Demux_Unit, locEvenmuxEast,
					ResourcePlanConstants.DEMUX_In, defaultLen, cwssEast.getDirection());
			dbService.getPatchCordService().Insert(wdppcEast);

			// connecting West MPN and West MUX/DEMUX
			List<CardInfo> mpnswest = dbService.getCardInfoService().FindMpnsByDir(networkid, nodeid,
					MapConstants.WEST);
			List<CardInfo> evenmuxwest = dbService.getCardInfoService().FindCards(networkid, nodeid,
					ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST);
			String locEvenmuxWest = rpu.locationStr(evenmuxwest.get(0).getRack(), evenmuxwest.get(0).getSbrack(),
					evenmuxwest.get(0).getCard());
			// List<CardInfo> oddmuxwest =
			// dbService.getCardInfoService().FindCards(networkid,nodeid,ResourcePlanConstants.Odd_Mux_Demux_Unit,MapConstants.WEST);
			// String locOddmuxWest = rpu.locationStr(oddmuxwest.get(0).getRack(),
			// oddmuxwest.get(0).getSbrack(), oddmuxwest.get(0).getCard());

			for (int j = 0; j < mpnswest.size(); j++) {
				String dir = mpnswest.get(j).getDirection();
				String mpntype = mpnswest.get(j).getCardType();
				String location = rpu.locationStr(mpnswest.get(j).getRack(), mpnswest.get(j).getSbrack(),
						mpnswest.get(j).getCard());

				proximity = rpu.fcheckProximity(location, locEvenmuxWest);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				// ACT or STB ports in CGM
				String sourcePortRx = ResourcePlanConstants.Mpn_Rx;
				String sourcePortTx = ResourcePlanConstants.Mpn_Tx;
				if (mpntype.contains("MPN(CGM)")) {
					sourcePortRx = sourcePortRx + "(ACT)";
					sourcePortTx = sourcePortTx + "(ACT)";
				}
				if (mpnswest.get(j).getWavelength() % 2 == 0)// even wavelength
				{
					PatchCord pcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortRx, ResourcePlanConstants.Even_Demux_Unit, locEvenmuxWest,
							"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
					dbService.getPatchCordService().Insert(pcr);

					PatchCord pct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortTx, ResourcePlanConstants.Even_Mux_Unit, locEvenmuxWest,
							"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
					dbService.getPatchCordService().Insert(pct);
				}
				// else
				// {
				// PatchCord oddpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				// mpntype, location,
				// ResourcePlanConstants.Mpn_Rx, ResourcePlanConstants.Odd_Mux_Demux_Unit,
				// locOddmuxWest, "", defaultLen,MapConstants.WEST);
				// dbService.getPatchCordService().Insert(oddpcr);
				//
				// PatchCord oddpct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				// mpntype, location,
				// ResourcePlanConstants.Mpn_Tx, ResourcePlanConstants.Odd_Mux_Demux_Unit,
				// locOddmuxWest, "", defaultLen,MapConstants.WEST);
				// dbService.getPatchCordService().Insert(oddpct);
				// }
			}

			// ocm and MUX/DEMUX
			// proximity= rpu.fcheckProximity(ocmloc,locOddmuxWest);
			// eid =
			// rpu.fgetPatchCord(proximity,ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			// PatchCord ocmpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
			// ResourcePlanConstants.CardOcm1x8, ocmloc,
			// ResourcePlanConstants.Ocm_Mtm, ResourcePlanConstants.Even_Mux_Demux_Unit,
			// locOddmuxWest, ResourcePlanConstants.MUX_Tap, defaultLen,"");
			// dbService.getPatchCordService().Insert(ocmpcr);

			proximity = rpu.fcheckProximity(ocmloc, locEvenmuxWest);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			PatchCord ocmpcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Dtm,
					ResourcePlanConstants.Even_Demux_Unit, locEvenmuxWest, ResourcePlanConstants.DEMUX_Tap, defaultLen,
					"");
			dbService.getPatchCordService().Insert(ocmpcwest);

			// joining even-odd mux/demux
			// PatchCord muxpc = new PatchCord(networkid,nodeid,
			// rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
			// ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m,
			// ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxWest,
			// "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxWest, "",
			// defaultLen,MapConstants.WEST);
			// dbService.getPatchCordService().Insert(muxpc);

			// PatchCord muxpc1 = new PatchCord(networkid,nodeid,
			// rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
			// ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m,
			// ResourcePlanConstants.Even_Mux_Demux_Unit, locOddmuxWest,
			// "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locEvenmuxWest, "",
			// defaultLen,MapConstants.WEST);
			// dbService.getPatchCordService().Insert(muxpc1);

			// wss and even mux/demux
			cwssWest = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.WEST);
			String wsslocWest = rpu.locationStr(cwssWest.getRack(), cwssWest.getSbrack(), cwssWest.getCard());

			proximity = rpu.fcheckProximity(wsslocWest, locEvenmuxWest);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

			PatchCord wadpcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssWest.getCardType(),
					wsslocWest, ResourcePlanConstants.Wss_AD, ResourcePlanConstants.Even_Mux_Unit, locEvenmuxWest,
					ResourcePlanConstants.MUX_op, defaultLen, cwssWest.getDirection());
			dbService.getPatchCordService().Insert(wadpcwest);

			PatchCord wdppcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssWest.getCardType(),
					wsslocWest, ResourcePlanConstants.Wss_DP, ResourcePlanConstants.Even_Demux_Unit, locEvenmuxWest,
					ResourcePlanConstants.DEMUX_In, defaultLen, cwssWest.getDirection());
			dbService.getPatchCordService().Insert(wdppcwest);
		}
		else if (Capacity.equalsIgnoreCase("2")) {

			// connecting East MPN and East MUX/DEMUX
			List<CardInfo> mpnseast = dbService.getCardInfoService().FindMpnsByDir(networkid, nodeid,
					MapConstants.EAST);
			List<CardInfo> evenmuxeast = dbService.getCardInfoService().FindCards(networkid, nodeid,
					ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.EAST);
			String locEvenmuxEast = rpu.locationStr(evenmuxeast.get(0).getRack(), evenmuxeast.get(0).getSbrack(),
					evenmuxeast.get(0).getCard()); 
			// List<CardInfo> oddmuxeast =
			// dbService.getCardInfoService().FindCards(networkid,nodeid,ResourcePlanConstants.Odd_Mux_Demux_Unit,MapConstants.EAST);
			// String locOddmuxEast = rpu.locationStr(oddmuxeast.get(0).getRack(),
			// oddmuxeast.get(0).getSbrack(), oddmuxeast.get(0).getCard());

			for (int j = 0; j < mpnseast.size(); j++) {
				String dir = mpnseast.get(j).getDirection();
				String mpntype = mpnseast.get(j).getCardType();
				String location = rpu.locationStr(mpnseast.get(j).getRack(), mpnseast.get(j).getSbrack(),
						mpnseast.get(j).getCard());

				proximity = rpu.fcheckProximity(location, locEvenmuxEast);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				// ACT or STB ports in CGM
				String sourcePortRx = ResourcePlanConstants.Mpn_Rx;
				String sourcePortTx = ResourcePlanConstants.Mpn_Tx;
				if (mpntype.contains("MPN(CGM)")) {
					sourcePortRx = sourcePortRx + "(ACT)";
					sourcePortTx = sourcePortTx + "(ACT)";
				}
				if (mpnseast.get(j).getWavelength() % 2 != 0)// even wavelength
				{
					PatchCord pcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortRx, ResourcePlanConstants.Odd_Demux_Unit, locEvenmuxEast,
							"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
					dbService.getPatchCordService().Insert(pcr);

					PatchCord pct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortTx, ResourcePlanConstants.Odd_Mux_Unit, locEvenmuxEast,
							"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
					dbService.getPatchCordService().Insert(pct);
				}
				// else
				// {
				// PatchCord oddpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				// mpntype, location,
				// ResourcePlanConstants.Mpn_Rx, ResourcePlanConstants.Odd_Mux_Demux_Unit,
				// locOddmuxEast, "", defaultLen,MapConstants.EAST);
				// dbService.getPatchCordService().Insert(oddpcr);
				//
				// PatchCord oddpct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				// mpntype, location,
				// ResourcePlanConstants.Mpn_Tx, ResourcePlanConstants.Odd_Mux_Demux_Unit,
				// locOddmuxEast, "", defaultLen,MapConstants.EAST);
				// dbService.getPatchCordService().Insert(oddpct);
				// }
			}

			// ocm and MUX/DEMUX
			// proximity= rpu.fcheckProximity(ocmloc,locOddmuxEast);
			// eid =
			// rpu.fgetPatchCord(proximity,ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			// PatchCord ocmpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
			// ResourcePlanConstants.CardOcm1x8, ocmloc,
			// ResourcePlanConstants.Ocm_Mtm, ResourcePlanConstants.Even_Mux_Demux_Unit,
			// locOddmuxEast, ResourcePlanConstants.MUX_Tap, defaultLen,"");
			// dbService.getPatchCordService().Insert(ocmpcr);

			proximity = rpu.fcheckProximity(ocmloc, locEvenmuxEast);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			PatchCord ocmpc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Dtm,
					ResourcePlanConstants.Odd_Demux_Unit, locEvenmuxEast, ResourcePlanConstants.DEMUX_Tap, defaultLen,
					"");
			dbService.getPatchCordService().Insert(ocmpc);

			// joining even-odd mux/demux
			// PatchCord muxpc = new PatchCord(networkid,nodeid,
			// rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
			// ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m,
			// ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxEast,
			// "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxEast, "",
			// defaultLen,MapConstants.EAST);
			// dbService.getPatchCordService().Insert(muxpc);

			// PatchCord muxpc1 = new PatchCord(networkid,nodeid,
			// rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
			// ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m,
			// ResourcePlanConstants.Even_Mux_Demux_Unit, locOddmuxEast,
			// "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locEvenmuxEast, "",
			// defaultLen,MapConstants.EAST);
			// dbService.getPatchCordService().Insert(muxpc1);

			// wss and even mux/demux
			cwssEast = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.EAST);
			String wsslocEast = rpu.locationStr(cwssEast.getRack(), cwssEast.getSbrack(), cwssEast.getCard());

			proximity = rpu.fcheckProximity(wsslocEast, locEvenmuxEast);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

			PatchCord wadpcEast = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssEast.getCardType(),
					wsslocEast, ResourcePlanConstants.Wss_AD, ResourcePlanConstants.Odd_Mux_Unit, locEvenmuxEast,
					ResourcePlanConstants.MUX_op, defaultLen, cwssEast.getDirection());
			dbService.getPatchCordService().Insert(wadpcEast);

			PatchCord wdppcEast = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssEast.getCardType(),
					wsslocEast, ResourcePlanConstants.Wss_DP, ResourcePlanConstants.Odd_Demux_Unit, locEvenmuxEast,
					ResourcePlanConstants.DEMUX_In, defaultLen, cwssEast.getDirection());
			dbService.getPatchCordService().Insert(wdppcEast);

			// connecting West MPN and West MUX/DEMUX
			List<CardInfo> mpnswest = dbService.getCardInfoService().FindMpnsByDir(networkid, nodeid,
					MapConstants.WEST);
			List<CardInfo> evenmuxwest = dbService.getCardInfoService().FindCards(networkid, nodeid,
					ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.WEST);
			String locEvenmuxWest = rpu.locationStr(evenmuxwest.get(0).getRack(), evenmuxwest.get(0).getSbrack(),
					evenmuxwest.get(0).getCard());
			// List<CardInfo> oddmuxwest =
			// dbService.getCardInfoService().FindCards(networkid,nodeid,ResourcePlanConstants.Odd_Mux_Demux_Unit,MapConstants.WEST);
			// String locOddmuxWest = rpu.locationStr(oddmuxwest.get(0).getRack(),
			// oddmuxwest.get(0).getSbrack(), oddmuxwest.get(0).getCard());

			for (int j = 0; j < mpnswest.size(); j++) {
				String dir = mpnswest.get(j).getDirection();
				String mpntype = mpnswest.get(j).getCardType();
				String location = rpu.locationStr(mpnswest.get(j).getRack(), mpnswest.get(j).getSbrack(),
						mpnswest.get(j).getCard());

				proximity = rpu.fcheckProximity(location, locEvenmuxWest);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
				// ACT or STB ports in CGM
				String sourcePortRx = ResourcePlanConstants.Mpn_Rx;
				String sourcePortTx = ResourcePlanConstants.Mpn_Tx;
				if (mpntype.contains("MPN(CGM)")) {
					sourcePortRx = sourcePortRx + "(ACT)";
					sourcePortTx = sourcePortTx + "(ACT)";
				}
				if (mpnswest.get(j).getWavelength() % 2 == 0)// even wavelength
				{
					PatchCord pcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortRx, ResourcePlanConstants.Odd_Demux_Unit, locEvenmuxWest,
							"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
					dbService.getPatchCordService().Insert(pcr);

					PatchCord pct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
							sourcePortTx, ResourcePlanConstants.Odd_Mux_Unit, locEvenmuxWest,
							"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
					dbService.getPatchCordService().Insert(pct);
				}
				// else
				// {
				// PatchCord oddpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				// mpntype, location,
				// ResourcePlanConstants.Mpn_Rx, ResourcePlanConstants.Odd_Mux_Demux_Unit,
				// locOddmuxWest, "", defaultLen,MapConstants.WEST);
				// dbService.getPatchCordService().Insert(oddpcr);
				//
				// PatchCord oddpct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				// mpntype, location,
				// ResourcePlanConstants.Mpn_Tx, ResourcePlanConstants.Odd_Mux_Demux_Unit,
				// locOddmuxWest, "", defaultLen,MapConstants.WEST);
				// dbService.getPatchCordService().Insert(oddpct);
				// }
			}

			// ocm and MUX/DEMUX
			// proximity= rpu.fcheckProximity(ocmloc,locOddmuxWest);
			// eid =
			// rpu.fgetPatchCord(proximity,ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			// PatchCord ocmpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
			// ResourcePlanConstants.CardOcm1x8, ocmloc,
			// ResourcePlanConstants.Ocm_Mtm, ResourcePlanConstants.Even_Mux_Demux_Unit,
			// locOddmuxWest, ResourcePlanConstants.MUX_Tap, defaultLen,"");
			// dbService.getPatchCordService().Insert(ocmpcr);

			proximity = rpu.fcheckProximity(ocmloc, locEvenmuxWest);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			PatchCord ocmpcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Dtm,
					ResourcePlanConstants.Odd_Demux_Unit, locEvenmuxWest, ResourcePlanConstants.DEMUX_Tap, defaultLen,
					"");
			dbService.getPatchCordService().Insert(ocmpcwest);

			// joining even-odd mux/demux
			// PatchCord muxpc = new PatchCord(networkid,nodeid,
			// rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
			// ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m,
			// ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxWest,
			// "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxWest, "",
			// defaultLen,MapConstants.WEST);
			// dbService.getPatchCordService().Insert(muxpc);

			// PatchCord muxpc1 = new PatchCord(networkid,nodeid,
			// rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
			// ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m,
			// ResourcePlanConstants.Even_Mux_Demux_Unit, locOddmuxWest,
			// "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locEvenmuxWest, "",
			// defaultLen,MapConstants.WEST);
			// dbService.getPatchCordService().Insert(muxpc1);

			// wss and even mux/demux
			try {
				cwssWest = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.WEST);
				String wsslocWest = rpu.locationStr(cwssWest.getRack(), cwssWest.getSbrack(), cwssWest.getCard());

				proximity = rpu.fcheckProximity(wsslocWest, locEvenmuxWest);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord wadpcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssWest.getCardType(),
						wsslocWest, ResourcePlanConstants.Wss_AD, ResourcePlanConstants.Odd_Mux_Unit, locEvenmuxWest,
						ResourcePlanConstants.MUX_op, defaultLen, cwssWest.getDirection());
				dbService.getPatchCordService().Insert(wadpcwest);

				PatchCord wdppcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssWest.getCardType(),
						wsslocWest, ResourcePlanConstants.Wss_DP, ResourcePlanConstants.Odd_Demux_Unit, locEvenmuxWest,
						ResourcePlanConstants.DEMUX_In, defaultLen, cwssWest.getDirection());
				dbService.getPatchCordService().Insert(wdppcwest);
			} catch(Exception e) {
				System.out.println("No WSS Found!");
			}
		}
		// client field patch cords for the unprotected lines or paths
		/*
		 * List<CardInfo> mpnsAll =
		 * dbService.getCardInfoService().findMpns(networkid,nodeid); for (int i = 0; i
		 * < mpnsAll.size(); i++) { int demandid=mpnsAll.get(i).getDemandId(); Demand d
		 * = dbService.getDemandService().FindDemand(networkid, demandid);
		 * 
		 * if(d.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)) {
		 * String cirSet=d.getCircuitSet(); String cirs[] = cirSet.split(","); for (int
		 * j = 0; j < cirs.length; j++) { String
		 * mpnloc=rpu.locationStr(mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(),
		 * mpnsAll.get(i).getCard()); PortInfo port =
		 * dbService.getPortInfoService().FindPortInfo(networkid,nodeid,mpnsAll.get(i).
		 * getRack(), mpnsAll.get(i).getSbrack(),
		 * mpnsAll.get(i).getCard(),Integer.parseInt(cirs[j])); PatchCord pc = new
		 * PatchCord(networkid,nodeid,
		 * rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
		 * ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m,
		 * mpnsAll.get(i).getCardType(), mpnloc, ""+port.getPort(),
		 * ResourcePlanConstants.Field, ResourcePlanConstants.Field,
		 * ResourcePlanConstants.Field, defaultLen,"");
		 * dbService.getPatchCordService().Insert(pc); } } }
		 */
		/*List<CardInfo> mpnsAll = dbService.getCardInfoService().findMpns(networkid, nodeid);
		for (int i = 0; i < mpnsAll.size(); i++) {
			String mpnloc = rpu.locationStr(mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(),
					mpnsAll.get(i).getCard());

			List<PortInfo> ports = dbService.getPortInfoService().FindPortInfo(networkid, nodeid,
					mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(), mpnsAll.get(i).getCard());
			// System.out.println("NodeId:"+nodeid+" Mpn:"+mpnloc+"
			// PortsCount:"+ports.size());
			for (int j = 0;( j <ports.size()); j++) {
				// System.out.println("Port:"+ports.get(j).getCircuitId());
				Circuit cir = dbService.getCircuitService().FindCircuit(networkid, ports.get(j).getCircuitId());
				if (cir.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)) {
					// System.out.println("circuitId:"+cir.getCircuitId());
					PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
							"" + ports.get(j).getPort()+"_Rx", ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc);
					PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
							"" + ports.get(j).getPort()+"_Tx", ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc2);
				}

			}
		}*/


		/*List<PortInfo> ports = dbService.getPortInfoService().FindMpnToFdfMap(networkid, nodeid);
		// System.out.println("NodeId:"+nodeid+" Mpn:"+mpnloc+"
		// PortsCount:"+ports.size());
		if(ports!=null)
		{
			for (int j = 0;( j <ports.size()); j++) {
				// System.out.println("Port:"+ports.get(j).getCircuitId());
				Circuit cir = dbService.getCircuitService().FindCircuit(networkid, ports.get(j).getCircuitId());
				String mpnLoc=rpu.locationStr(ports.get(j).getRack(), ports.get(j).getSbrack(),
						ports.get(j).getCard());
				if (cir.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)) {
					// System.out.println("circuitId:"+cir.getCircuitId());
					PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, ports.get(j).getCardType(), mpnLoc,
							"" + ports.get(j).getPort()+"_Rx", ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc);
					PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, ports.get(j).getCardType(), mpnLoc,
							"" + ports.get(j).getPort()+"_Tx", ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc2);
				}
			}
		}*/
		mpnToFdfMapping(networkid, nodeid,defaultLen);
		/*Xgm(Agregator) to MPN connection*/
		List<AggregatorClientMap> aggregatorLinePortMapping = dbService.getPortInfoService().FindAggregatorLinePortMapping(networkid, nodeid);
		if(aggregatorLinePortMapping!=null)
		{
			for(int i=0;i<aggregatorLinePortMapping.size();i++)
			{
				System.out.println("10G Agg Mapping");
				String xgmLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getRack(), aggregatorLinePortMapping.get(i).getSbRack(),
						aggregatorLinePortMapping.get(i).getCard());
				String mpnLoc=rpu.locationStr(aggregatorLinePortMapping.get(i).getMpnRack(), aggregatorLinePortMapping.get(i).getMpnSbRack(),
						aggregatorLinePortMapping.get(i).getMpnCard());
				proximity = rpu.fcheckProximity(xgmLoc, mpnLoc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_01m);
				System.out.println("eid:"+eid+" proximity:"+ proximity +" rpu.fgetCardTypeFromEId(eid):"+rpu.fgetCardTypeFromEId(eid));
				PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Tx",
						ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
						"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Rx", defaultLen, "");
				dbService.getPatchCordService().Insert(pc_1);
				PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardMuxponder10G, xgmLoc, "Line "+(" " + aggregatorLinePortMapping.get(i).getLinePort()).toString().substring(2)+ "_Rx",
						ResourcePlanConstants.CardMuxponderCGM,mpnLoc ,
						"Client " + aggregatorLinePortMapping.get(i).getMpnPortNo() + "_Tx", defaultLen, "");
				dbService.getPatchCordService().Insert(pc_2);

			}
		}
		List<YCablePortInfo> YCabPorts = dbService.getYcablePortInfoService().FindAll(networkid, nodeid);
		for (int i = 0; i < YCabPorts.size(); i++) {
			int ids[] = rpu.locationIds(YCabPorts.get(i).getMpnLocN());

			CardInfo mpnN = dbService.getCardInfoService().FindCard(networkid, nodeid, ids[0], ids[1], ids[2]);

			String YCabLoc = rpu.locationStr(YCabPorts.get(i).getYCableRack(), YCabPorts.get(i).getYCableSbrack(),
					YCabPorts.get(i).getYCableCard());
			proximity = rpu.fcheckProximity(YCabLoc, YCabPorts.get(i).getMpnLocN());
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_02m);

			PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_OP1(T)",
					mpnN.getCardType(), YCabPorts.get(i).getMpnLocN(),
					"Client " + YCabPorts.get(i).getMpnPortN() + "_Rx", defaultLen, "");
			dbService.getPatchCordService().Insert(pc_1);

			PatchCord pc_3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_OP1(R)",
					mpnN.getCardType(), YCabPorts.get(i).getMpnLocN(),
					"Client " + YCabPorts.get(i).getMpnPortN() + "_Tx", defaultLen, "");
			dbService.getPatchCordService().Insert(pc_3);

			if (YCabPorts.get(i).getMpnLocP() != null) {
				int idsP[] = rpu.locationIds(YCabPorts.get(i).getMpnLocP());
				CardInfo mpnP = dbService.getCardInfoService().FindCard(networkid, nodeid, idsP[0], idsP[1], idsP[2]);

				PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP2(T)", mpnP.getCardType(),
						YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Rx", defaultLen,
						"");
				dbService.getPatchCordService().Insert(pc_2);

				PatchCord pc_4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP2(R)", mpnP.getCardType(),
						YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Tx", defaultLen,
						"");
				dbService.getPatchCordService().Insert(pc_4);
			}

			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m);
			PatchCord pc_5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(T)",
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field, defaultLen,
					"");
			dbService.getPatchCordService().Insert(pc_5);

			PatchCord pc_6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(R)",
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field, defaultLen,
					"");
			dbService.getPatchCordService().Insert(pc_6);

		}

	}

	public void cscc_to_mpc_connection(int networkid, int nodeid, String nodekey, int defaultLen) throws SQLException {
		int noOfRacksInNode = dbService.getCardInfoService().FindRackCount(networkid, nodeid);
		ArrayList<Integer> subRackList = new ArrayList<Integer>();
		for (int rackIndex = ResourcePlanConstants.ONE; rackIndex <= noOfRacksInNode; rackIndex++) {

			String locationCsccActive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerActive);
			String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
					ResourcePlanConstants.MainSbrack, ResourcePlanConstants.MainControllerPassive);
			List<Map<String, Object>> subRackPerRack = dbService.getCardInfoService().FindSbracksInRack(networkid,
					nodeid, rackIndex);
			boolean flag = false;
			int subRackCount = subRackPerRack.size();
			int subRackNum[] = new int[3];
			subRackList.clear();

			for (int subRackIndex = ResourcePlanConstants.ZERO; subRackIndex < subRackPerRack.size(); subRackIndex++) {
				subRackNum[0] = Integer.parseInt(subRackPerRack.get(subRackIndex).get("SbRack").toString());
				subRackList.add(subRackNum[0]);
			}

			if ((subRackList.size() == ResourcePlanConstants.TWO && rackIndex == ResourcePlanConstants.MainRack)
					|| (subRackList.size() == ResourcePlanConstants.ONE
					&& rackIndex != ResourcePlanConstants.MainRack)) {
				if (rackIndex == ResourcePlanConstants.MainRack) {
					if (subRackList.contains(ResourcePlanConstants.SubRackOne))
						subRackNum[0] = ResourcePlanConstants.SubRackOne;
					else if (subRackList.contains(ResourcePlanConstants.SubRackThree))
						subRackNum[0] = ResourcePlanConstants.SubRackThree;

				} else {
					subRackNum[0] = subRackList.get(0);
				}
				List<CardInfo> getMpcInfo = dbService.getCardInfoService().FindCardsInSbrack(networkid, nodeid,
						rackIndex, subRackNum[0], ResourcePlanConstants.CardMpc);
				if (getMpcInfo.size() > 0) {
					String locationMpc = rpu.locationStr(rackIndex, subRackNum[0],
							getMpcInfo.get(ResourcePlanConstants.MaxMpcPerSubRack - 1).getCard());
					// Active CSCC to MPC B1 port
					PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
							ResourcePlanConstants.PCord_CAT6_With_RJ45_3m, ResourcePlanConstants.CardCscc,
							"" + locationCsccActive, ResourcePlanConstants.Fabric_Port + " " + rackIndex,
							ResourcePlanConstants.CardMpc, locationMpc, ResourcePlanConstants.Base_Port1, defaultLen,
							"");
					dbService.getPatchCordService().Insert(pc);
					// Passive CSCC to MPC B2 port
					PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
							ResourcePlanConstants.PCord_CAT6_With_RJ45_3m, ResourcePlanConstants.CardCscc,
							"" + locationCsccPassive, ResourcePlanConstants.Fabric_Port + " " + rackIndex,
							ResourcePlanConstants.CardMpc, locationMpc, ResourcePlanConstants.Base_Port2, defaultLen,
							"");
					dbService.getPatchCordService().Insert(pc2);
				}
			} else if ((subRackList.size() == ResourcePlanConstants.THREE
					&& rackIndex == ResourcePlanConstants.MainRack)
					|| (subRackList.size() == ResourcePlanConstants.TWO
					&& rackIndex != ResourcePlanConstants.MainRack)) {
				if (rackIndex == ResourcePlanConstants.MainRack) {
					subRackNum[0] = ResourcePlanConstants.SubRackOne;
					subRackNum[1] = ResourcePlanConstants.SubRackThree;

				} else {
					if (subRackList.contains(ResourcePlanConstants.SubRackOne)) {
						subRackNum[0] = ResourcePlanConstants.SubRackTwo;
						subRackNum[1] = ResourcePlanConstants.SubRackOne;
					} else if (subRackList.contains(ResourcePlanConstants.SubRackThree)) {
						subRackNum[0] = ResourcePlanConstants.SubRackTwo;
						subRackNum[1] = ResourcePlanConstants.SubRackThree;
					}
				}
				List<CardInfo> getMpcInfo = dbService.getCardInfoService().FindCardsInSbrack(networkid, nodeid,
						rackIndex, subRackNum[0], ResourcePlanConstants.CardMpc);
				String locationMpc1 = "", locationMpc2 = "";
				PatchCord pc, pc2, pc3;
				if (getMpcInfo.size() > 0) {
					locationMpc1 = rpu.locationStr(rackIndex, subRackNum[0],
							getMpcInfo.get(ResourcePlanConstants.MaxMpcPerSubRack - 1).getCard());
					// Active CSCC to SubRack-1 MPC B1 port
					pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
							ResourcePlanConstants.PCord_CAT6_With_RJ45_3m, ResourcePlanConstants.CardCscc,
							"" + locationCsccActive, ResourcePlanConstants.Fabric_Port + " " + rackIndex,
							ResourcePlanConstants.CardMpc, locationMpc1, ResourcePlanConstants.Base_Port1, defaultLen,
							"");
					dbService.getPatchCordService().Insert(pc);
					// Passive CSCC to SubRack-3 MPC B1 port
					getMpcInfo.clear();
				}

				getMpcInfo = dbService.getCardInfoService().FindCardsInSbrack(networkid, nodeid, rackIndex,
						subRackNum[1], ResourcePlanConstants.CardMpc);
				if (getMpcInfo.size() > 0) {
					locationMpc2 = rpu.locationStr(rackIndex, subRackNum[1],
							getMpcInfo.get(ResourcePlanConstants.MaxMpcPerSubRack - 1).getCard());
					pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
							ResourcePlanConstants.PCord_CAT6_With_RJ45_3m, ResourcePlanConstants.CardCscc,
							"" + locationCsccPassive, ResourcePlanConstants.Fabric_Port + " " + rackIndex,
							ResourcePlanConstants.CardMpc, locationMpc2, ResourcePlanConstants.Base_Port1, defaultLen,
							"");
					dbService.getPatchCordService().Insert(pc2);

					// SubRack-1 MPC B2 Port to SubRack-3 MPC B2 Port

					pc3 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
							ResourcePlanConstants.PCord_CAT6_With_RJ45_3m, ResourcePlanConstants.CardMpc,
							"" + locationMpc1, ResourcePlanConstants.Base_Port2, ResourcePlanConstants.CardMpc,
							locationMpc2, ResourcePlanConstants.Base_Port2, defaultLen, "");
					dbService.getPatchCordService().Insert(pc3);
				}
			} else if (subRackList.size() == 3 && rackIndex != ResourcePlanConstants.MainRack) {

				List<CardInfo> getMpcInfo = dbService.getCardInfoService().FindCardsInSbrack(networkid, nodeid,
						rackIndex, ResourcePlanConstants.SubRackTwo, ResourcePlanConstants.CardMpc);
				String locationMpc1 = "", locationMpc2 = "", locationMpc3 = "";
				PatchCord pc, pc2, pc3, pc4;
				if (getMpcInfo.size() > 0) {
					locationMpc1 = rpu.locationStr(rackIndex, ResourcePlanConstants.SubRackTwo,
							getMpcInfo.get(ResourcePlanConstants.MaxMpcPerSubRack - 1).getCard());
					// Active CSCC to SubRack-1 MPC B1 port
					pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
							ResourcePlanConstants.PCord_CAT6_With_RJ45_3m, ResourcePlanConstants.CardCscc,
							"" + locationCsccActive, ResourcePlanConstants.Fabric_Port + " " + rackIndex,
							ResourcePlanConstants.CardMpc, locationMpc1, ResourcePlanConstants.Base_Port1, defaultLen,
							"");
					dbService.getPatchCordService().Insert(pc);
					// Passive CSCC to SubRack-3 MPC B1 port
					getMpcInfo.clear();
				}

				getMpcInfo = dbService.getCardInfoService().FindCardsInSbrack(networkid, nodeid, rackIndex,
						ResourcePlanConstants.SubRackThree, ResourcePlanConstants.CardMpc);
				if (getMpcInfo.size() > 0) {
					locationMpc2 = rpu.locationStr(rackIndex, ResourcePlanConstants.SubRackThree,
							getMpcInfo.get(ResourcePlanConstants.MaxMpcPerSubRack - 1).getCard());
					pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
							ResourcePlanConstants.PCord_CAT6_With_RJ45_3m, ResourcePlanConstants.CardCscc,
							"" + locationCsccPassive, ResourcePlanConstants.Fabric_Port + " " + rackIndex,
							ResourcePlanConstants.CardMpc, locationMpc2, ResourcePlanConstants.Base_Port1, defaultLen,
							"");
					dbService.getPatchCordService().Insert(pc2);

					// SubRack-1 MPC B2 Port to SubRack-3 MPC B2 Port
					getMpcInfo.clear();
				}

				getMpcInfo = dbService.getCardInfoService().FindCardsInSbrack(networkid, nodeid, rackIndex,
						ResourcePlanConstants.SubRackOne, ResourcePlanConstants.CardMpc);
				if (getMpcInfo.size() > 0) {
					locationMpc3 = rpu.locationStr(rackIndex, ResourcePlanConstants.SubRackOne,
							getMpcInfo.get(ResourcePlanConstants.MaxMpcPerSubRack - 1).getCard());
					pc3 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
							ResourcePlanConstants.PCord_CAT6_With_RJ45_3m, ResourcePlanConstants.CardMpc,
							"" + locationMpc2, ResourcePlanConstants.Base_Port2, ResourcePlanConstants.CardMpc,
							locationMpc3, ResourcePlanConstants.Base_Port2, defaultLen, "");
					dbService.getPatchCordService().Insert(pc3);

					pc4 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
							ResourcePlanConstants.PCord_CAT6_With_RJ45_3m, ResourcePlanConstants.CardMpc,
							"" + locationMpc3, ResourcePlanConstants.Base_Port1, ResourcePlanConstants.CardMpc,
							locationMpc1, ResourcePlanConstants.Base_Port2, defaultLen, "");
					dbService.getPatchCordService().Insert(pc4);
				}

			} else {
				System.out.println("wrong..");
			}
		}
	}

	/**
	 * Creates the Patch cord db tables, assigns the patchcords for Hub
	 * 
	 * @throws SQLException
	 * 
	 */
	public void fAssignPatchCordsHub(int networkid, int nodeid) throws SQLException {
		int defaultLen = 1;
		String nodekey = "" + networkid + "_" + nodeid;
		String proximity;
		int eid;

		Node n = dbService.getNodeService().FindNode(networkid, nodeid);
		String Capacity = n.getCapacity();
		CardInfo cocm = dbService.getCardInfoService().FindCard(networkid, nodeid, ResourcePlanConstants.CardOcm1x8,
				"");
		String ocmloc = rpu.locationStr(cocm.getRack(), cocm.getSbrack(), cocm.getCard());
		CardInfo cwss, cwssEast, cwssWest = null;

		// connecting PA/BA
		List<CardInfo> pabas = dbService.getCardInfoService().FindAllPaBaCards(networkid, nodeid);
		for (int j = 0; j < pabas.size(); j++) {
			String dir = pabas.get(j).getDirection();
			String pabaCardType="";
			String location = rpu.locationStr(pabas.get(j).getRack(), pabas.get(j).getSbrack(), pabas.get(j).getCard());
			String ePart="";
			if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanDra))
			{
				pabaCardType=ResourcePlanConstants.AmpRamanDra;
				// ATX to CRX
				PatchCord pc6 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.AmpRamanDra, location,
						ResourcePlanConstants.Ila_Atx, ResourcePlanConstants.AmpRamanDra,location,
						ResourcePlanConstants.Ila_Crx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);
				ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
			}
			else if(pabas.get(j).getCardType().equalsIgnoreCase(ResourcePlanConstants.AmpRamanHybrid))
			{
				pabaCardType=ResourcePlanConstants.AmpRamanHybrid;
				// ATX to CRX
				PatchCord pc7 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_LC_APC_point_5m),
						ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, ResourcePlanConstants.AmpRamanHybrid, location,
						ResourcePlanConstants.Ila_Atx, ResourcePlanConstants.AmpRamanHybrid, location,
						ResourcePlanConstants.Ila_Crx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc7);
				ePart=ResourcePlanConstants.EPart_E2000_UPC_to_LC_UPC_10m;
			}
			else
			{
				pabaCardType=ResourcePlanConstants.CardPaBa;
				ePart=ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m;
			}
			// From Field (Pin)

			PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ePart),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
					ResourcePlanConstants.PaBA_FromField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc1);


			// To field (Btx)
			PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
					ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, pabaCardType, location,
					ResourcePlanConstants.PaBA_ToField, ResourcePlanConstants.Field, ResourcePlanConstants.Field,
					ResourcePlanConstants.Field, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc2);

			// Supy Add (Sad)

			// int supycardid = rpu.fgetSupyForDirection(pabas.get(j).getDirection());
			CardInfo supycard = dbService.getCardInfoService().FindCard(networkid, nodeid,
					ResourcePlanConstants.CardSupy, "");
			String supyloc = rpu.locationStr(supycard.getRack(), supycard.getSbrack(), supycard.getCard());
			proximity = rpu.fcheckProximity(location, supyloc);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);
			// logger.info("fAssignPatchCordsROADM proximity "+proximity);
			// logger.info("fAssignPatchCordsROADM eid "+eid);

			PatchCord pc3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),pabaCardType,
					location, ResourcePlanConstants.PaBA_SupyAdd, ResourcePlanConstants.CardSupy, supyloc,
					ResourcePlanConstants.Supy_Tx, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc3);

			// Supy Drop (Sdp)
			PatchCord pc4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),pabaCardType,
					location, ResourcePlanConstants.PaBA_SupyDrop, ResourcePlanConstants.CardSupy, supyloc,
					ResourcePlanConstants.Supy_Rx, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc4);

			// check if this direction carries 10G traffic or not
			PortInfo dcm = dbService.getPortInfoService().FindDcmModuleByDir(networkid, nodeid, dir);
			if (dcm != null) {
				// 10G traffic exists
				/**********************
				 * PABA to DCM to WSS
				 **********************/
				// To Drx (Ptx)
				String dcmloc = rpu.locationStr(dcm.getRack(), dcm.getSbrack(), dcm.getCard());
				proximity = rpu.fcheckProximity(location, dcmloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_PTx, dcm.getCardType(),
						dcmloc, ResourcePlanConstants.DCM_Drx, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc5);

				cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
				String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

				// From DCM (DTx) to WSS (WRC)
				PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), dcm.getCardType(), dcmloc,
						ResourcePlanConstants.DCM_Dtx, cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_SIN,
						defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);

				// From WSS (Brx)
				PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_FromWss,
						cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TX, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc7);

			} else// no 10G traffic on this direction
			{
				/**********
				 * PABA to WSS
				 *************/

				// To WSS (Ptx)
				cwss = dbService.getCardInfoService().FindWss(networkid, nodeid, dir);
				String wssloc = rpu.locationStr(cwss.getRack(), cwss.getSbrack(), cwss.getCard());

				proximity = rpu.fcheckProximity(location, wssloc);
				eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

				PatchCord pc5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_PTx, cwss.getCardType(),
						wssloc, ResourcePlanConstants.Wss_SIN, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc5);

				// From WSS (Brx)
				PatchCord pc6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						pabaCardType, location, ResourcePlanConstants.PaBA_FromWss,
						cwss.getCardType(), wssloc, ResourcePlanConstants.Wss_TX, defaultLen, dir);
				dbService.getPatchCordService().Insert(pc6);
			}

			/**************
			 * PABA to OCM
			 **************/

			proximity = rpu.fcheckProximity(location, ocmloc);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			// To OCM (PTM)
			PatchCord pc7 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), pabaCardType,
					location, ResourcePlanConstants.PaBA_ToOcm, ResourcePlanConstants.CardOcm1x8, ocmloc,
					ResourcePlanConstants.Ocm_Ptm, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc7);

			// From OCM (BTM)
			PatchCord pc8 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), pabaCardType,
					location, ResourcePlanConstants.PaBA_FromOcm, ResourcePlanConstants.CardOcm1x8, ocmloc,
					ResourcePlanConstants.Ocm_Btm, defaultLen, dir);
			dbService.getPatchCordService().Insert(pc8);
		}

		// ethernet cables

		PatchCord pc1 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
				ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc,
				"" + ResourcePlanConstants.MainControllerActive, ResourcePlanConstants.Cscc_ToLct, "", "", "",
				defaultLen, "");
		dbService.getPatchCordService().Insert(pc1);

		if (rpu.fcheckIsGne(networkid, nodeid) == true) {
			PatchCord pc2 = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_10m),
					ResourcePlanConstants.PCord_CAT6_With_RJ45_10m, ResourcePlanConstants.CardCscc,
					"" + ResourcePlanConstants.MainControllerActive, ResourcePlanConstants.Cscc_ToEms, "", "", "",
					defaultLen, "");
			dbService.getPatchCordService().Insert(pc2);
		}

		/*
		 * List<CardInfo> mpcs =
		 * dbService.getCardInfoService().FindCardInfoByCardType(networkid,nodeid,
		 * ResourcePlanConstants.CardMpc); for (int j = 0; j < mpcs.size(); j++) {
		 * String dir=mpcs.get(j).getDirection(); String locationCsccActive =
		 * rpu.locationStr(ResourcePlanConstants.MainRack,
		 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerActive);
		 * String locationCsccPassive = rpu.locationStr(ResourcePlanConstants.MainRack,
		 * ResourcePlanConstants.MainSbrack,ResourcePlanConstants.MainControllerPassive)
		 * ; String locationMpc = rpu.locationStr(mpcs.get(j).getRack(),
		 * mpcs.get(j).getSbrack(), mpcs.get(j).getCard());
		 * if(locationMpc.contains("_6")) { PatchCord pc = new PatchCord(networkid,nodeid,
		 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
		 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
		 * ResourcePlanConstants.CardCscc, ""+locationCsccActive,
		 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
		 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
		 * dbService.getPatchCordService().Insert(pc); } else { PatchCord pc = new
		 * PatchCord(networkid,nodeid,
		 * rpu.fgetEId(ResourcePlanConstants.EPart_CAT6_With_RJ45_3m),
		 * ResourcePlanConstants.PCord_CAT6_With_RJ45_3m,
		 * ResourcePlanConstants.CardCscc, ""+locationCsccPassive,
		 * ResourcePlanConstants.Cscc_ToMpc, ResourcePlanConstants.CardMpc, locationMpc,
		 * ResourcePlanConstants.Mpc_FromCscc, defaultLen,"");
		 * dbService.getPatchCordService().Insert(pc); } }
		 */
		// CSCC to MPC Connections
		cscc_to_mpc_connection(networkid, nodeid, nodekey, defaultLen);

		// connecting East MPN and East MUX/DEMUX
		List<CardInfo> mpnseast = dbService.getCardInfoService().FindMpnsByDir(networkid, nodeid, MapConstants.EAST);
		List<CardInfo> evenmuxeast = dbService.getCardInfoService().FindCards(networkid, nodeid,
				ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.EAST);
		String locEvenmuxEast = rpu.locationStr(evenmuxeast.get(0).getRack(), evenmuxeast.get(0).getSbrack(),
				evenmuxeast.get(0).getCard());
		List<CardInfo> oddmuxeast = dbService.getCardInfoService().FindCards(networkid, nodeid,
				ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.EAST);
		String locOddmuxEast = rpu.locationStr(oddmuxeast.get(0).getRack(), oddmuxeast.get(0).getSbrack(),
				oddmuxeast.get(0).getCard());

		for (int j = 0; j < mpnseast.size(); j++) {
			String dir = mpnseast.get(j).getDirection();
			String mpntype = mpnseast.get(j).getCardType();
			String location = rpu.locationStr(mpnseast.get(j).getRack(), mpnseast.get(j).getSbrack(),
					mpnseast.get(j).getCard());

			proximity = rpu.fcheckProximity(location, locEvenmuxEast);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			if (mpnseast.get(j).getWavelength() % 2 == 0)// even wavelength
			{
				PatchCord pcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						ResourcePlanConstants.Mpn_Rx, ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxEast,
						"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
				dbService.getPatchCordService().Insert(pcr);

				PatchCord pct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						ResourcePlanConstants.Mpn_Tx, ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxEast,
						"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
				dbService.getPatchCordService().Insert(pct);
			} else {
				PatchCord oddpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						ResourcePlanConstants.Mpn_Rx, ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxEast,
						"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
				dbService.getPatchCordService().Insert(oddpcr);

				PatchCord oddpct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						ResourcePlanConstants.Mpn_Tx, ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxEast,
						"Wave No " + mpnseast.get(j).getWavelength(), defaultLen, MapConstants.EAST);
				dbService.getPatchCordService().Insert(oddpct);
			}
		}

		// ocm and MUX/DEMUX
		proximity = rpu.fcheckProximity(ocmloc, locOddmuxEast);
		eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
		PatchCord ocmpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardOcm1x8,
				ocmloc, ResourcePlanConstants.Ocm_Mtm, ResourcePlanConstants.Even_Mux_Demux_Unit, locOddmuxEast,
				ResourcePlanConstants.MUX_Tap, defaultLen, "");
		dbService.getPatchCordService().Insert(ocmpcr);

		proximity = rpu.fcheckProximity(ocmloc, locEvenmuxEast);
		eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
		PatchCord ocmpc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), ResourcePlanConstants.CardOcm1x8,
				ocmloc, ResourcePlanConstants.Ocm_Dtm, ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxEast,
				ResourcePlanConstants.DEMUX_Tap, defaultLen, "");
		dbService.getPatchCordService().Insert(ocmpc);

		// joining even-odd mux/demux
		PatchCord muxpc = new PatchCord(networkid,nodeid,
				rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
				ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Mux_Demux_Unit,
				locEvenmuxEast, "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxEast, "", defaultLen,
				MapConstants.EAST);
		dbService.getPatchCordService().Insert(muxpc);

		PatchCord muxpc1 = new PatchCord(networkid,nodeid,
				rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
				ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Mux_Demux_Unit,
				locOddmuxEast, "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locEvenmuxEast, "", defaultLen,
				MapConstants.EAST);
		dbService.getPatchCordService().Insert(muxpc1);

		// wss and even mux/demux
		cwssEast = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.EAST);
		if (cwssEast != null) {
			String wsslocEast = rpu.locationStr(cwssEast.getRack(), cwssEast.getSbrack(), cwssEast.getCard());

			proximity = rpu.fcheckProximity(wsslocEast, locEvenmuxEast);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

			PatchCord wadpc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssEast.getCardType(),
					wsslocEast, ResourcePlanConstants.Wss_AD, ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxEast,
					ResourcePlanConstants.MUX_op, defaultLen, cwssEast.getDirection());
			dbService.getPatchCordService().Insert(wadpc);

			PatchCord wdppc = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssEast.getCardType(),
					wsslocEast, ResourcePlanConstants.Wss_DP, ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxEast,
					ResourcePlanConstants.DEMUX_In, defaultLen, cwssEast.getDirection());
			dbService.getPatchCordService().Insert(wdppc);
		}

		/////////////////

		// connecting West MPN and West MUX/DEMUX
		List<CardInfo> mpnswest = dbService.getCardInfoService().FindMpnsByDir(networkid, nodeid, MapConstants.WEST);
		List<CardInfo> evenmuxwest = dbService.getCardInfoService().FindCards(networkid, nodeid,
				ResourcePlanConstants.Even_Mux_Demux_Unit, MapConstants.WEST);
		String locEvenmuxWest = rpu.locationStr(evenmuxwest.get(0).getRack(), evenmuxwest.get(0).getSbrack(),
				evenmuxwest.get(0).getCard());
		List<CardInfo> oddmuxwest = dbService.getCardInfoService().FindCards(networkid, nodeid,
				ResourcePlanConstants.Odd_Mux_Demux_Unit, MapConstants.WEST);
		String locOddmuxWest = rpu.locationStr(oddmuxwest.get(0).getRack(), oddmuxwest.get(0).getSbrack(),
				oddmuxwest.get(0).getCard());

		for (int j = 0; j < mpnswest.size(); j++) {
			String dir = mpnswest.get(j).getDirection();
			String mpntype = mpnswest.get(j).getCardType();
			String location = rpu.locationStr(mpnswest.get(j).getRack(), mpnswest.get(j).getSbrack(),
					mpnswest.get(j).getCard());

			proximity = rpu.fcheckProximity(location, locEvenmuxWest);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
			if (mpnswest.get(j).getWavelength() % 2 == 0)// even wavelength
			{
				PatchCord pcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						ResourcePlanConstants.Mpn_Rx, ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxWest,
						"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
				dbService.getPatchCordService().Insert(pcr);

				PatchCord pct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						ResourcePlanConstants.Mpn_Tx, ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxWest,
						"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
				dbService.getPatchCordService().Insert(pct);
			} else {
				PatchCord oddpcr = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						ResourcePlanConstants.Mpn_Rx, ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxWest,
						"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
				dbService.getPatchCordService().Insert(oddpcr);

				PatchCord oddpct = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), mpntype, location,
						ResourcePlanConstants.Mpn_Tx, ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxWest,
						"Wave No " + mpnswest.get(j).getWavelength(), defaultLen, MapConstants.WEST);
				dbService.getPatchCordService().Insert(oddpct);
			}
		}

		// ocm and MUX/DEMUX
		proximity = rpu.fcheckProximity(ocmloc, locOddmuxWest);
		eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
		PatchCord ocmpcrwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Mtm,
				ResourcePlanConstants.Even_Mux_Demux_Unit, locOddmuxWest, ResourcePlanConstants.MUX_Tap, defaultLen,
				"");
		dbService.getPatchCordService().Insert(ocmpcrwest);

		proximity = rpu.fcheckProximity(ocmloc, locEvenmuxWest);
		eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_APC_01m);
		PatchCord ocmpcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
				ResourcePlanConstants.CardOcm1x8, ocmloc, ResourcePlanConstants.Ocm_Dtm,
				ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxWest, ResourcePlanConstants.DEMUX_Tap, defaultLen,
				"");
		dbService.getPatchCordService().Insert(ocmpcwest);

		// joining even-odd mux/demux
		PatchCord muxpcwest = new PatchCord(networkid,nodeid,
				rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
				ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Mux_Demux_Unit,
				locEvenmuxWest, "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locOddmuxWest, "", defaultLen,
				MapConstants.WEST);
		dbService.getPatchCordService().Insert(muxpcwest);

		PatchCord muxpc1west = new PatchCord(networkid,nodeid,
				rpu.fgetEIdFromCardType(ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m),
				ResourcePlanConstants.PCord_LC_APC_to_LC_APC_point2m, ResourcePlanConstants.Even_Mux_Demux_Unit,
				locOddmuxWest, "", ResourcePlanConstants.Odd_Mux_Demux_Unit, locEvenmuxWest, "", defaultLen,
				MapConstants.WEST);
		dbService.getPatchCordService().Insert(muxpc1west);

		// wss and even mux/demux
		cwssWest = dbService.getCardInfoService().FindWss(networkid, nodeid, MapConstants.WEST);
		if (cwssWest != null) {
			String wsslocWest = rpu.locationStr(cwssWest.getRack(), cwssWest.getSbrack(), cwssWest.getCard());

			proximity = rpu.fcheckProximity(wsslocWest, locEvenmuxWest);
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_APC_to_LC_UPC_01m);

			PatchCord wadpcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssWest.getCardType(),
					wsslocWest, ResourcePlanConstants.Wss_AD, ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxWest,
					ResourcePlanConstants.MUX_op, defaultLen, cwssWest.getDirection());
			dbService.getPatchCordService().Insert(wadpcwest);

			PatchCord wdppcwest = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid), cwssWest.getCardType(),
					wsslocWest, ResourcePlanConstants.Wss_DP, ResourcePlanConstants.Even_Mux_Demux_Unit, locEvenmuxWest,
					ResourcePlanConstants.DEMUX_In, defaultLen, cwssWest.getDirection());
			dbService.getPatchCordService().Insert(wdppcwest);
		}

		// client field patch cords for the unprotected lines or paths
		//		List<CardInfo> mpnsAll = dbService.getCardInfoService().findMpns(networkid, nodeid);
		//		for (int i = 0; i < mpnsAll.size(); i++) {
		//			int demandid = mpnsAll.get(i).getDemandId();
		//			Demand d = dbService.getDemandService().FindDemand(networkid, demandid);
		//			if (d.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)) {
		//				String cirSet = d.getCircuitSet();
		//				String cirs[] = cirSet.split(",");
		//				for (int j = 0; j < cirs.length; j++) {
		//					String mpnloc = rpu.locationStr(mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(),
		//							mpnsAll.get(i).getCard());
		//					PortInfo port = dbService.getPortInfoService().FindPortInfo(networkid, nodeid,
		//							mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(), mpnsAll.get(i).getCard(),
		//							Integer.parseInt(cirs[j]));
		//					PatchCord pc = new PatchCord(networkid,nodeid, rpu.fgetEId(ResourcePlanConstants.EPart_LC_APC_to_FC_UPC_20m),
		//							ResourcePlanConstants.PCord_LC_APC_to_FC_PC_20m, mpnsAll.get(i).getCardType(), mpnloc,
		//							"" + port.getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
		//							ResourcePlanConstants.Field, defaultLen, "");
		//					dbService.getPatchCordService().Insert(pc);
		//				}
		//			}
		//		}
		/*List<CardInfo> mpnsAll = dbService.getCardInfoService().findMpns(networkid, nodeid);
		for (int i = 0; i < mpnsAll.size(); i++) {
			String mpnloc = rpu.locationStr(mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(),
					mpnsAll.get(i).getCard());

			List<PortInfo> ports = dbService.getPortInfoService().FindPortInfo(networkid, nodeid,
					mpnsAll.get(i).getRack(), mpnsAll.get(i).getSbrack(), mpnsAll.get(i).getCard(),true);
			System.out.println("NodeId:" + nodeid + " Mpn:" + mpnloc + " PortsCount:" + ports.size());
			for (int j = 0;( j <ports.size()) && ports.get(j).getMpnPortNo()==0; j++) {
				System.out.println("Port:" + ports.get(j).getCircuitId());
				Circuit cir = dbService.getCircuitService().FindCircuit(networkid, ports.get(j).getCircuitId());
				System.out.println("getProtectionType:" + cir.getProtectionType());
				if (cir.getProtectionType().equalsIgnoreCase(SMConstants.PtcTypeNoneStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToOnePtcTypeStr)
						||cir.getProtectionType().equalsIgnoreCase(SMConstants.OneIsToTwoRPtcTypeStr)) {
					System.out.println("circuitId:" + cir.getCircuitId());
					PatchCord pc = new PatchCord(networkid,nodeid,
							rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
							"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc);
					PatchCord pc2 = new PatchCord(networkid,nodeid,
							rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
							ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
							"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
							ResourcePlanConstants.Field, defaultLen, "");
					dbService.getPatchCordService().Insert(pc2);
				} else if(cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusRPtcStr)||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePlusTwoRPtcTypeStr)
						||cir.getProtectionType().equalsIgnoreCase(SMConstants.OnePlusOnePtcStr)) {
					if(cir.getClientProtection().equalsIgnoreCase(SMConstants.YesStr))
					{
						if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.yCablePtcTypeStr))
						{
							//Handled below
						}
						else 
						{
							//OLP Case TODO
						}
					}
					else if(cir.getChannelProtection().equalsIgnoreCase(SMConstants.YesStr)) 
					{
						if(cir.getClientProtectionType().equalsIgnoreCase(SMConstants.opxPtcTypeStr))
						{
							PatchCord pc = new PatchCord(networkid,nodeid,
									rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
									ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
									"Tx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
									ResourcePlanConstants.Field, defaultLen, "");
							dbService.getPatchCordService().Insert(pc);
							PatchCord pc2 = new PatchCord(networkid,nodeid,
									rpu.fgetEId(ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m),
									ResourcePlanConstants.PCord_LC_UPC_to_FC_UPC_20m, mpnsAll.get(i).getCardType(), mpnloc,
									"Rx_" + ports.get(j).getPort(), ResourcePlanConstants.Field, ResourcePlanConstants.Field,
									ResourcePlanConstants.Field, defaultLen, "");
							dbService.getPatchCordService().Insert(pc2);
						}
						else 
						{
							//OLP Case TODO
						}
					}
					else
					{

					}
				}

			}
		}*/
		mpnToFdfMapping(networkid, nodeid,defaultLen);
		List<YCablePortInfo> YCabPorts = dbService.getYcablePortInfoService().FindAll(networkid, nodeid);
		for (int i = 0; i < YCabPorts.size(); i++) {
			int ids[] = rpu.locationIds(YCabPorts.get(i).getMpnLocN());

			CardInfo mpnN = dbService.getCardInfoService().FindCard(networkid, nodeid, ids[0], ids[1], ids[2]);

			String YCabLoc = rpu.locationStr(YCabPorts.get(i).getYCableRack(), YCabPorts.get(i).getYCableSbrack(),
					YCabPorts.get(i).getYCableCard());
			proximity = rpu.fcheckProximity(YCabLoc, YCabPorts.get(i).getMpnLocN());
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_LC_UPC_02m);

			PatchCord pc_1 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_OP1(T)",
					mpnN.getCardType(), YCabPorts.get(i).getMpnLocN(),
					"Client " + YCabPorts.get(i).getMpnPortN() + "_Rx", defaultLen, "");
			dbService.getPatchCordService().Insert(pc_1);

			PatchCord pc_3 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_OP1(R)",
					mpnN.getCardType(), YCabPorts.get(i).getMpnLocN(),
					"Client " + YCabPorts.get(i).getMpnPortN() + "_Tx", defaultLen, "");
			dbService.getPatchCordService().Insert(pc_3);

			if (YCabPorts.get(i).getMpnLocP() != null) {
				int idsP[] = rpu.locationIds(YCabPorts.get(i).getMpnLocP());
				CardInfo mpnP = dbService.getCardInfoService().FindCard(networkid, nodeid, idsP[0], idsP[1], idsP[2]);

				PatchCord pc_2 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP2(T)", mpnP.getCardType(),
						YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Rx", defaultLen,
						"");
				dbService.getPatchCordService().Insert(pc_2);

				PatchCord pc_4 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
						ResourcePlanConstants.CardTypeYCable, YCabLoc,
						"" + YCabPorts.get(i).getYCablePort() + "_OP2(R)", mpnP.getCardType(),
						YCabPorts.get(i).getMpnLocP(), "Client " + YCabPorts.get(i).getMpnPortP() + "_Tx", defaultLen,
						"");
				dbService.getPatchCordService().Insert(pc_4);
			}
			eid = rpu.fgetPatchCord(proximity, ResourcePlanConstants.EPart_LC_UPC_to_FC_UPC_20m);
			PatchCord pc_5 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(T)",
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field, defaultLen,
					"");
			dbService.getPatchCordService().Insert(pc_5);

			PatchCord pc_6 = new PatchCord(networkid,nodeid, eid, rpu.fgetCardTypeFromEId(eid),
					ResourcePlanConstants.CardTypeYCable, YCabLoc, "" + YCabPorts.get(i).getYCablePort() + "_IN(R)",
					ResourcePlanConstants.Field, ResourcePlanConstants.Field, ResourcePlanConstants.Field, defaultLen,
					"");
			dbService.getPatchCordService().Insert(pc_6);

		}

	}

	/**
	 * Creates the Patch cord db tables, assigns the patchcords
	 * 
	 * @throws SQLException
	 * 
	 */
	public void fAssignPatchCords(int networkid) throws SQLException {

		/// logger.info("fAssignPatchCords for Network "+networkid);
		int defaultLen = 1;
		List<Integer> nodelist = rpu.fgetNodesToConfigure(networkid);
		for (int i = 0; i < nodelist.size(); i++) {
			int nodeid = nodelist.get(i);
			int nodetype = dbService.getNodeService().FindNode(networkid, nodeid).getNodeType();
			int nodedegree = dbService.getNodeService().FindNode(networkid, nodeid).getDegree();
			String nodekey = "" + networkid + "_" + nodeid;

			dbService.getPatchCordService().CreatePatchCordTable(nodekey);
			if (nodetype == MapConstants.roadm) {
				fAssignPatchCordsROADM(networkid, nodeid);
			} else if (nodetype == MapConstants.ila) {
				List<NetworkRoute> nr = dbService.getNetworkRouteService().IsNodeOn10GRoute(networkid, nodeid);
				if (!nr.isEmpty()) {
					logger.info("fAssignPatchCords 10G Circuit found for NodeId: " + nodeid);
					fAssignPatchCordsILAFor10GLine(networkid, nodeid);
				} else {
					fAssignPatchCordsILA(networkid, nodeid);
				}
			} else if (nodetype == MapConstants.te) {
				fAssignPatchCordsTE(networkid, nodeid);
			} else if (nodetype == MapConstants.twoBselectRoadm) {
				fAssignPatchCordsBnS(networkid, nodeid);
			} else if (nodetype == MapConstants.hub) {
				// as the connections are going to be same for BnS and hub except the wss - Wss
				// connection
				fAssignPatchCordsHub(networkid, nodeid);
			}else if(nodetype == MapConstants.cdRoadm) {
				fAssignPatchCordsCDROADM(networkid, nodeid);
			}
		}
	}
}