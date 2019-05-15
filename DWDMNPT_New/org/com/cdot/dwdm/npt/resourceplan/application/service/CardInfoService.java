/**
 * 
 */
/**
 * @author sunaina
 *
 */
package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import application.dao.CardInfoDao;
import application.model.Circuit;
import application.model.CardInfo;

@Service
public class CardInfoService{

	@Autowired
	private CardInfoDao dao;
	
	public List<CardInfo> FindAll()
	{
		return this.dao.findAll();
	}
	
	/*
	 * NodeKey = networkid+"_"+nodeid
	 * */
	public List<CardInfo> FindAll(int networkid,int nodeid)
	{
		return this.dao.findAll(networkid,nodeid);
	}
	
	public List<Map<String, Object>> TempFindAll(int networkid,int nodeid)
	{
		return this.dao.tempFindAll(networkid,nodeid);
	}
	
	public CardInfo FindCard(int networkid, int nodeid, int rack, int sbrack, int cardid)
	{
		return this.dao.findCardInfo(networkid, nodeid, rack, sbrack, cardid);
	}
	
	public CardInfo FindCardInfoWithEq(int networkid, int nodeid, int rack, int sbrack, int cardid)
	{
		return this.dao.findCardInfoWithEq(networkid, nodeid, rack, sbrack, cardid);
	}
	
	public CardInfo FindCardInfo(int networkid, int nodeid, int demandid, String cardtype)
	{
		return this.dao.findCardInfo(networkid, nodeid, demandid, cardtype);
	}
	
	public CardInfo FindMpn(int networkid, int nodeid, int demandid, String status)
	{
		return this.dao.findMpn(networkid, nodeid, demandid, status);
	}
	
	public CardInfo FindMpn(int networkid, int nodeid, int demandid, int circuitId, String status)
	{
		return this.dao.findMpn(networkid, nodeid, demandid, circuitId, status);
	}
	
	public List <CardInfo> FindMpn(int networkid, int nodeid, int demandid)
	{
		return this.dao.findMpn(networkid, nodeid, demandid);
	}
	
	public CardInfo FindOLPByCircuitId(int networkid, int nodeid, int circuitid)
	{
		return this.dao.findOLPByCircuitId(networkid, nodeid, circuitid);
	}
	
	public List <CardInfo> FindMpnByCircuitId(int networkid, int nodeid, int circuitid)
	{
		return this.dao.findMpnByCircuitId(networkid, nodeid, circuitid);
	}
	
	public CardInfo FindCard(int networkid, int nodeid, String cardtype, String dir)
	{
		return this.dao.findCardInfo(networkid, nodeid, cardtype, dir);
	}
	
	public List <CardInfo> FindCards(int networkid, int nodeid, String cardtype, String dir)
	{
		return this.dao.findCard(networkid, nodeid, cardtype, dir);
	}
	
	
	public List<CardInfo> FindMpnsByDir(int networkid, int nodeid, String dir)
	{
		return this.dao.findMpnsByDir(networkid, nodeid, dir);
	}
		
	public CardInfo FindWss(int networkid, int nodeid, String dir)
	{
		return this.dao.findWss(networkid, nodeid, dir);
	}
	
	public List<CardInfo> FindOcm(int networkid, int nodeid)
	{
		return this.dao.findOcm(networkid, nodeid);
	}
	
	public List<CardInfo> FindWss(int networkid, int nodeid)
	{
		return this.dao.findWss(networkid, nodeid);
	}
	
	public List<CardInfo> FindDirectionWss(int networkid, int nodeid)
	{
		return this.dao.findDirectionWss(networkid, nodeid);
	}
	
	public int CountMpn(int networkid, int nodeid, String dir)
	{
		return this.dao.countMpn(networkid, nodeid, dir);
	}
	
	public int CountMpn(int networkid, int nodeid)
	{
		return this.dao.countMpn(networkid, nodeid);
	}
	
	public int FindCountWorkingMpns(int networkid, int nodeid, String dir)
	{
		return this.dao.findCountWorkingMpns(networkid, nodeid,dir);
	}
	
	public List<CardInfo> FindCardInfoByCardType(int networkid, int nodeid, String cardtype)
	{
		return this.dao.findCardInfoByCardType(networkid, nodeid, cardtype);
	}
	
	public List<CardInfo> FindWssLevelTwoCards(int networkid, int nodeid)
	{
		return this.dao.findWssLevelTwoCards(networkid, nodeid);
	}
	
	public List<CardInfo> FindWssLevelOneCards(int networkid, int nodeid)
	{
		return this.dao.findWssLevelOneCards(networkid, nodeid);
	}
	
	public List<CardInfo> FindOLPsWithDemandId(int networkid, int nodeid)
	{
		return this.dao.findOLPsWithDemandId(networkid, nodeid);
	}
	
	public List<CardInfo> FindOLPsForLinkProt(int networkid, int nodeid)
	{
		return this.dao.findOLPsForLinkProt(networkid, nodeid);
	}
	
	public List<CardInfo> findMpns(int networkid, int nodeid)
	{
		return this.dao.findMpns(networkid, nodeid);
	}
	
	public List<CardInfo> FindMpnsExcept5x10G(int networkid, int nodeid)
	{
		return this.dao.findMpnsExcept5x10G(networkid, nodeid);
	}
	
	public List<Object> FindMpnDir(int networkid, int nodeid)
	{
		return this.dao.findMpnDir(networkid, nodeid);
	}
		
	public void Insert(CardInfo info) throws SQLException{
		this.dao.insert(info);
		
	}
	
	public void UpdateMpn(CardInfo info) throws SQLException{
		this.dao.updateMpn(info);		
	}
	
	public int Count()
	{
		return this.dao.count();
	}
	
	public int Count(int networkId)
	{
		return this.dao.count(networkId);
	}
	public void DeleteCard(int networkid, int nodeid, int rack, int sbrack, int card)
	{
		this.dao.deleteCard(networkid, nodeid, rack, sbrack, card);
	}
	
	public void DeleteMpcInSbrack(int networkid, int nodeid, int rack, int sbrack)
	{
		this.dao.deleteMpcInSbrack(networkid, nodeid, rack, sbrack);
	}
	
	public int CountCardByType(int networkid,int nodeid, String cardtype)
	{
		return this.dao.countCardByType(networkid,nodeid,cardtype);
	}
	
	public List<Map<String,Object>> CountCardByTypeNEId(int networkid,int nodeid)
	{
		return this.dao.countCardByTypeNEId(networkid, nodeid);
	}
	
	/*
	 * 
	 * Used to create the card info table in db
	 * Table name will be CardInfo_NetworkId_NodeId
	 * 
	 * * */
	public void CreateCardInfoTable(String nodekey)
	{
		this.dao.createCardInfoTable(nodekey);
	}
	
	public void DeleteAllCardInfo(int networkid)
	{
		this.dao.deleteAllCardInfo(networkid);
	}
	
	public void DeleteCardInfo(int networkid, int nodeid)
	{
		this.dao.deleteCardInfo(networkid, nodeid);
	}
	
	/*
	 * @brief returns the list of all racks , sbracks used in a particular node
	 * */
	public List<Map<String, Object>> FindSbracks(int networkid, int nodeid)
	{
		return this.dao.findSbracks(networkid, nodeid);
	}
	
	/**
	 * returns the list of all racks used in a particular node
	 * @param networkid
	 * @param nodeid
	 * @return
	 */
	public List<Map<String, Object>> FindRacks(int networkid, int nodeid)
	{
		return this.dao.findRacks(networkid, nodeid);
	}
	
	/**
	 * returns the list of all sbracks used in a particular rack of a node
	 * @param networkid
	 * @param nodeid
	 * @return
	 */
	public List<Map<String, Object>> FindSbracksInRack(int networkid, int nodeid, int rackid)
	{
		return this.dao.findSbracksInRack(networkid, nodeid, rackid);
	}
	
	/**
	 * returns the list of all cards in particular sbrack of a node
	 * @param networkid
	 * @param nodeid
	 * @return
	 */
	public List<CardInfo> FindCardsInSbrack(int networkid, int nodeid, int rackid, int sbrackid)
	{
		return this.dao.findCardsInSbrack(networkid, nodeid, rackid, sbrackid);
	}
	
	public List<CardInfo> FindCardsInSbrack(int networkid, int nodeid, int rackid, int sbrackid, String cardtype)
	{
		return this.dao.findCardsInSbrack(networkid, nodeid, rackid, sbrackid, cardtype);
	}
	
	
	/*
	 * @brief returns the count  of all sbracks used in a particular node
	 * */
	public int FindSbrackCount(int networkid, int nodeid)
	{
		return this.dao.findSbrackCount(networkid, nodeid);
	}
	
	/*
	 * @brief returns the count  of all racks used in a particular node
	 * */
	public int FindRackCount(int networkid, int nodeid)
	{
		return this.dao.findRackCount(networkid, nodeid);
	}
	
	/*
	 * @brief returns the count  of all sbracks used in a particular rack of a node
	 * */
	public int FindSbRackCountInRack(int networkid, int nodeid, int rackid)
	{
		return this.dao.findSbRackCountInRack(networkid, nodeid, rackid);
	}

	public List<Map<String, Object>> FindMaxRackSubrackCount(int networkid, int nodeid, int rackid)
	{
		return this.dao.findMaxRackSubrackCount(networkid, nodeid, rackid);
	}
	
	public List<Map<String, Object>> FgetCircuitMatix(int networkid, int nodeid)
	{
		return this.dao.fgetCircuitMatix(networkid, nodeid);
	}
	
	public List<Map<String, Object>> FgetWavelenthsNodewise_Rep(int networkid, int nodeid)
	{
		return this.dao.fgetWavelenthsNodewise_Rep(networkid, nodeid);
	}
	
	public Integer FindfreeSubrackSpaceInRack(int networkid, int nodeid, int rackid)
	{
		return this.dao.findfreeSubrackSpaceInRack(networkid, nodeid, rackid);		
	}
	
	public CardInfo FindfreeSubrackSpacenew(int networkid, int nodeid)throws SQLException{
//		return this.dao.findfreeSubrackSpace(networkid, nodeid);
		return this.dao.findfreeSubrackSpacenew(networkid, nodeid);
	}
	
	public List<Map<String, Object>> FgetTpnDataPerCircuit_cf(int networkid, int nodeid)
	{
		return this.dao.fgetTpnDataPerCircuit_cf(networkid, nodeid);
	}
	
	public void CreateViewAllCardInfo(int networkid)
	{
		 this.dao.createViewAllCardInfo(networkid);
	}
	public int FgetNodeWavelength(int networkid, int nodeid)
	{
		return this.dao.fgetNodeWavelength(networkid, nodeid);
	}
	
	public boolean IsSlotFree(int networkid, int nodeid, int rackid, int sbrackid, int cardid)
	{
		return this.dao.isSlotFree(networkid, nodeid, rackid, sbrackid, cardid);
	}
	
	public void UpdateNodeKey(int networkid, int nodeid,int networkidBF) throws SQLException
	{
		this.dao.updateNodeKey(networkid, nodeid, networkidBF);
	}	
	
	public void InsertCardDataInBrField(int networkid, int nodeid,int networkidBF) throws SQLException
	{
		this.dao.insertCardDataInBrField(networkid, nodeid, networkidBF);
	}	
	
	public List<CardInfo> FindAddedCardsInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.dao.findAddedCardsInBrField(networkidGrField, networkidBrField, nodeid);
	}	
	
	public List<CardInfo> FindDeletedCardsInBrField(int networkidGrField, int networkidBrField, int nodeid, String ... cardType)
	{
		return this.dao.findDeletedCardsInBrField(networkidGrField, networkidBrField, nodeid, cardType);
	}	
	
	public List<CardInfo> FindCommonCardsInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.dao.findCommonCardsInBrField(networkidGrField, networkidBrField, nodeid);
	}
	
	public boolean CheckIfTableExists(int networkid, int nodeid)
	{
		return this.dao.checkIfTableExists(networkid, nodeid);
	}
	
	public List<CardInfo> FindNonMPCCardsInSbrack(int networkid, int nodeid, int rack , int sbrack)
	{
		return this.dao.findNonMPCCardsInSbrack(networkid, nodeid, rack, sbrack);
	}
	public int FindingCountWorkingMpnsForGraphs(int networkid, int nodeid, String dir)
	{
		return this.dao.findingCountWorkingMpnsForGraphs(networkid, nodeid,dir);
	}

	public List<CardInfo> FindWorkingMpnsDirectionForGraphs(int networkId, int nodeid) {
		
		return this.dao.findWorkingMpnsForDirectionGraphs(networkId, nodeid);
	}

	public List<CardInfo> FindAmplifierCardCount(int networkId, int nodeid) {

		return this.dao.findAmplifierCardCount(networkId, nodeid);
	}
	
	public List<CardInfo> FindIlaCardCount(int networkId, int nodeid) {

		return this.dao.findIlaCardCount(networkId, nodeid);
	}
	
//	public List<CardInfo> FindCardsInDirectionSetOne(int networkId, int nodeid,String cardtype) {
//
//		return this.dao.findCardsInDirectionSetOne(networkId, nodeid, cardtype);
//	}
//	
//	public List<CardInfo> FindCardsInDirectionSetTwo(int networkId, int nodeid,String cardtype) {
//
//		return this.dao.findCardsInDirectionSetTwo(networkId, nodeid, cardtype);
//	}

	public List<CardInfo> FindAllPaBaCards(int networkid, int nodeid)
	{
		return this.dao.findAllCardsPaBa(networkid, nodeid);
	}
	public List<CardInfo> FindAllIlaCards(int networkid, int nodeid)
	{
		return this.dao.findAllCardsPaBa(networkid, nodeid);
	}
	
}