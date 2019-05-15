package application.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.constants.ResourcePlanConstants;
import application.model.AllocationExceptions;
import application.model.Equipment;
import application.model.EquipmentPreference;
import application.model.Stock;

@Component
public class StockDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	 
	 public void updateQuantity(Stock info) throws SQLException {		     
	     String sql = "Update Stock  set Quantity = ? where NetworkId = ? and NodeId = ? and CardType = ?  ";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { info.getQuantity(), info.getNetworkId(), info.getNodeId(), info.getCardType() });
	 }
	 
	 public void updateUsedQuantity(Stock info) throws SQLException {		     
	     String sql = "Update Stock  set UsedQuantity = ? where NetworkId = ? and NodeId = ? and CardType = ?  ";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { info.getUsedQuantity(), info.getNetworkId(), info.getNodeId(), info.getCardType() });
	 }
	 
	 public void updateStatus(Stock info) throws SQLException {		     
	     String sql = "Update Stock  set Status = ? where NetworkId = ? and NodeId = ? and CardType = ? and SerialNo =? ";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { info.getStatus(), info.getNetworkId(), info.getNodeId(), info.getCardType(), info.getSerialNo() });
	 }
	 
	 public Equipment findEquipmentById(int eid) {
	        logger.info("Query Equipment: findEquipmentById");
	        String sql = "SELECT * FROM Equipment where EquipmentId = ? ";	        
	        logger.info("For EquipmentId No: "+ eid);
	        Equipment e  = (Equipment) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(Equipment.class),eid);			
			return e; 
	    }
	 
	 public int findSlotSize(int eid) {	        
	        String sql = "SELECT SlotSize FROM Equipment where EquipmentId = ? ";	       
	        int e  = jdbcTemplate.queryForObject(sql,int.class,eid);			
			return e; 
	    }
		 
	 public Stock findUnUsedSrNoStock(int networkid, int nodeid, String category, String cardtype ){
		 try
		 {
			String sql = "SELECT * FROM Stock where NetworkId = ? and NodeId = ? and Category = ? and CardType = ? and  Status != 'Used' and Quantity !=0 and SerialNo !=''  LIMIT 1";
			Stock e  = (Stock) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Stock.class),networkid, nodeid, category, cardtype);
			return e;
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
		}
	 
	 public Stock findUnUsedStockFreeFromParamPref(int networkid, int nodeid, String category, String cardtype ){
		 try
		 {
			String sql = "SELECT st.NetworkId, st.NodeId, st.Category, st.CardType, st.Quantity, st.UsedQuantity, st.SerialNo, st.Status from Stock st "+
					"LEFT JOIN ParametricPreference pp on pp.NetworkId = st.NetworkId and pp.NodeId = st.NodeId " + 
					"and pp.Category =  st.Category and pp.CardType =  st.CardType  and pp.SerialNo=st.SerialNo " + 
					"where st.NetworkId = ? and st.NodeId = ? and st.Category = ? and st.CardType = ? and st.SerialNo !='' and Status != 'Used' and pp.CardType is null " + 
					"and pp.SerialNo is null LIMIT 1 ";
			Stock e  = (Stock) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Stock.class),networkid, nodeid, category, cardtype);
			return e;
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
		}
	 
	 public Stock findUnUsedStockWithoutSrNo(int networkid, int nodeid, String category, String cardtype ){
		 try
		 {
			String sql = "SELECT NetworkId, NodeId, Category , Cardtype , Quantity , UsedQuantity, (Quantity + UsedQuantity) as tot  from Stock where NetworkId = ? and NodeId = ? and Category = ? and CardType = ? "
					+ "and Quantity != 0 and (Quantity + UsedQuantity) > ( SELECT count(*) from Stock where NetworkId = ? and NodeId = ? and Category = ? and CardType = ? and SerialNo != '') "
					+ "and Quantity > (SELECT count(*) from Stock where NetworkId = ? and NodeId = ? and Category = ?  and CardType = ? and SerialNo != '' and Status != 'Used' ) LIMIT 1;";
			Stock e  = (Stock) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Stock.class),networkid, nodeid, category, cardtype, networkid, nodeid, category, cardtype, networkid, nodeid, category, cardtype);
			return e;
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
		}
	 
	 public Stock find(int networkid, int nodeid, String category, String cardtype ){
		 try
		 {
			String sql = "SELECT * FROM Stock where NetworkId = ? and NodeId = ? and Category = ? and CardType = ? and  Quantity !=0 and  Status != 'Used' LIMIT 1";
			Stock e  = (Stock) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Stock.class),networkid, nodeid, category, cardtype);
			return e;
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
		}
	 
	 public List <Stock> find(int networkid ){
		 try
		 {
			String sql = "SELECT * FROM Stock where NetworkId = ? ";
			List <Stock> e  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Stock.class),networkid);
			return e;
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
		}
	 
	 public Stock find(int networkid, int nodeid, String category, String cardtype , String SrNo){
		 try
		 {
			String sql = "SELECT * FROM Stock where NetworkId = ? and NodeId = ? and Category = ? and CardType = ? and  Quantity !=0 and  SerialNo = ? and  Status != 'Used' ";
			Stock e  = (Stock) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Stock.class),networkid, nodeid, category, cardtype, SrNo);
			return e;
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
		}
	 
	 /**
	  * finds the stock with highest preference from the existing quantities in the stock
	  * @param networkid
	  * @param nodeid
	  * @param category
	  * @return
	  */
	 
	 public String findPreferredExistingStock(int networkid, int nodeid, String category ){		 
		 try
		 {
			String sql = "SELECT CardType from EquipmentPreference where NetworkId=? and NodeId=? and Category = ? and Preference = " + 
					"(select MIN(Preference) from EquipmentPreference  where CardType IN " + 
					"(SELECT CardType FROM Stock where NetworkId = ? and NodeId = ? and Category = ? and Quantity != 0) " + 
					"and NetworkId = ? and NodeId = ? and Category = ?  ) ";
			logger.info(sql);
			String e  = (String) jdbcTemplate.queryForObject(sql, String.class,networkid, nodeid, category,networkid, nodeid, category,networkid, nodeid, category);			
			return e;
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
		}
	 
	 public void insert(final Stock ep) throws SQLException {
     String sql = "INSERT into Stock(NetworkId, NodeId, Category, CardType, Quantity, UsedQuantity, SerialNo, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";
     jdbcTemplate.update(
             sql,
             new Object[] { ep.getNetworkId(), ep.getNodeId(), ep.getCategory(), ep.getCardType(), ep.getQuantity(), ep.getUsedQuantity(),ep.getSerialNo(), ep.getStatus()});
	 	}
	 
	 public int count(int networkId)
	 {		
		 System.out.println("Count Equipment");
		  String sql = "select count(*) from Stock where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        System.out.println("Delete Stock");
	        String sql = "delete from Stock where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	 
	 public void initializeParamPrefbrField(int networkIdGf,int networkIdBf) throws SQLException {
	     String sql = "INSERT into Stock (NetworkId, NodeId, Category, CardType, Quantity, UsedQuantity, SerialNo,Status) select ?, NodeId, Category, CardType, Quantity, UsedQuantity, SerialNo,Status from Stock where NetworkId=?";
	     jdbcTemplate.update(sql,networkIdBf,networkIdGf);
		 	}
	
	
	 }
