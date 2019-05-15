/**
 * @author hp
 * @date 1st Jun, 2018
 */

package application.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mysql.fabric.xmlrpc.base.Array;

import application.constants.DataPathConfigFileConstants;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.DemandMapping;
import application.model.OtnLspInformation;
import application.service.DbService;

@Component
public class OtnLspInformationDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);

	public List<OtnLspInformation> findAll(int networkId) {
		String sql = "SELECT * FROM OtnLspInformationDao where NetworkId = ?";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<OtnLspInformation> routes = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OtnLspInformation.class),
				networkId);
		logger.debug("OtnLspInformation FindAll  : " + routes.toString());
		return routes;
	}

	/**
	 * To Find out OTNLSP for specific Demand and TrafficType and Given Path
	 * 
	 * @param networkid
	 * @param demandId
	 * @param Path
	 * @param TrafficType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OtnLspInformation findLsp(int networkid, int demandId, String circuitId, String Path, String TrafficType,
			int RoutePriority, int typeOfOtnLsp) {

		String sql = "SELECT * FROM OtnLspInformation where NetworkId = ? and DemandId =?  and Path=? and TrafficType =?";
		OtnLspInformation routes = null;

		if (typeOfOtnLsp < DataPathConfigFileConstants.MPNClientSideOtnLsp) {

			sql += " and RoutePriority=? and CircuitId = ? ";
			routes = (OtnLspInformation) jdbcTemplate.queryForObject(sql,
					new BeanPropertyRowMapper(OtnLspInformation.class), networkid, demandId, Path, TrafficType,
					RoutePriority, circuitId);

		} else {
			routes = (OtnLspInformation) jdbcTemplate.queryForObject(sql,
					new BeanPropertyRowMapper(OtnLspInformation.class), networkid, demandId, /* routePriority, */ Path,
					TrafficType);
		}

		/** Check if the circuit set not modified yet */
		List<String> circuitIdList = new ArrayList<String>(Arrays.asList(routes.getCircuitId().split(",")));

		logger.debug(" circuitIdList : " + circuitIdList);
		logger.debug(" circuitId to Search/append : " + circuitId);
		if (!routes.getCircuitId().equalsIgnoreCase(circuitId)) {/** Update CircuitSet as its not exist into db */
			logger.debug(" Update CircuitSet ...");
			OtnLspInformation newOtnLSpInformationObj = new OtnLspInformation();
			// circuitIdList.add(String.valueOf(circuitId));
			// newOtnLSpInformationObj.setCircuitId(StringUtils.join(circuitIdList,","));
			newOtnLSpInformationObj.setCircuitId(circuitId);
			updateLsp(networkid, demandId, TrafficType, newOtnLSpInformationObj);

		}

		logger.debug("OtnLspInformation findLsp  : " + routes.toString());

		return routes;
	}

	/**
	 * To Find out both Working and Protection OTNLSP for specific Demand and
	 * TrafficType
	 * 
	 * @param networkid
	 * @param demandId
	 * @param TrafficType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OtnLspInformation> findLsp(int networkid, int demandId, String TrafficType) {

		String sql = "SELECT * FROM OtnLspInformation where NetworkId = ? and DemandId =? and TrafficType =?";
		List<OtnLspInformation> routes = null;
		routes = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OtnLspInformation.class), networkid, demandId,
				TrafficType);
		logger.debug("OtnLspInformation findLsp  : " + routes.toString());

		return routes;
	}

	/**
	 * Update both working and protection LSPs
	 * 
	 * @param networkId
	 * @param demandId
	 * @param TrafficType
	 */
	public void updateLsp(int networkId, int demandId, String TrafficType, OtnLspInformation newOtnLSpInformationObj) {
		String sql = "update OtnLspInformation set CircuitId=?  where NetworkId=? and DemandId=? and TrafficType=? ";
		try {
			jdbcTemplate.update(sql, newOtnLSpInformationObj.getCircuitId(), networkId, demandId, TrafficType);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void insertOtnLspInformation(final OtnLspInformation lspInformation) throws SQLException {

		logger.debug("Insert OtnLspInformation : " + lspInformation.toString());
		String sql = "INSERT into OtnLspInformation(NetworkId, DemandId, LineRate,Path, WavelengthNo,"
				+ "RoutePriority, CircuitId, TrafficType, ProtectionType, OtnLspTunnelId, LspId, ForwardingAdj) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			jdbcTemplate.update(sql,
					new Object[] { lspInformation.getNetworkId(), lspInformation.getDemandId(),
							lspInformation.getLineRate(), lspInformation.getPath(), lspInformation.getWavelengthNo(),
							lspInformation.getRoutePriority(), lspInformation.getCircuitId(),
							lspInformation.getTrafficType(), lspInformation.getProtectionType(),
							lspInformation.getOtnLspTunnelId(), lspInformation.getLspId(),lspInformation.getForwardingAdj() });

		} catch (Exception e) {
			logger.error("Error in insertOtnLspInformation : " + e);
			e.printStackTrace();
		}

	}

	public void deleteByNetworkId(int networkid) {
		logger.info("Delete OtnLspInformation");
		String sql = "delete from OtnLspInformation where NetworkId = ?";
		jdbcTemplate.update(sql, networkid);
	}

	public int findMaxTunnelIdOtnLspInformation(int brownFieldNetworkid, int greenFieldNetworkid, String circuitId,
			String trafficType, int typeOfOtnLSP) {

		String sql;
		int OtnLspTunnelId = 0;

		try {

			if (typeOfOtnLSP == DataPathConfigFileConstants.XGMClientSideOtnLsp) {

				/// circuitId =
				/// dbService.getCircuit10gAggService().FindCircuit10gAgg(brownFieldNetworkid,
				/// Integer.parseInt(circuitId), circuit10gAggId)

				/**
				 * First check from the working path, because protection will have the same
				 * otnlspid
				 */
				sql = "select OtnLspTunnelId from OtnLspInformation  where NetworkId = ? and CircuitId =?  and TrafficType =? and RoutePriority=?";
				OtnLspTunnelId = jdbcTemplate.queryForObject(sql, int.class, brownFieldNetworkid, circuitId,
						trafficType, MapConstants.I_ONE);

			} else {
				/**
				 * First check from the working path, because protection will have the same
				 * otnlspid
				 */
				sql = "select OtnLspTunnelId from OtnLspInformation  where NetworkId = ? and CircuitId =?  and TrafficType =? and RoutePriority=?";
				OtnLspTunnelId = jdbcTemplate.queryForObject(sql, int.class, brownFieldNetworkid, circuitId,
						trafficType, MapConstants.I_ONE);
			}

		} catch (Exception e) {/** Else give the maximum id from table */

			logger.error(e);

			/** Maxumum id out of the two lsp tables */
			sql = "SELECT MAX(T.Id)+1 AS OtnLspTunnelId " + "FROM ( " + "SELECT LambdaLspTunnelId AS Id "
					+ "FROM LambdaLspInformation where NetworkId=? or NetworkId=? " + "UNION ALL "
					+ "SELECT OtnLspTunnelId AS Id " + "FROM OtnLspInformation where NetworkId=? or NetworkId=? "
					+ ") AS T";

			// sql = "select Max(OtnLspTunnelId)+1 as OtnLspTunnelId from
			// OtnLspInformation"+
			// " where NetworkId = ?";

			OtnLspTunnelId = jdbcTemplate.queryForObject(sql, int.class, brownFieldNetworkid, greenFieldNetworkid,
					brownFieldNetworkid, greenFieldNetworkid);

		}

		logger.debug("OtnLspInformation FindMaxLspTunnelId  : " + OtnLspTunnelId);

		return OtnLspTunnelId;
	}

	public void copyOtnLspInformationInBrField(int networkidGrField, int networkidBrField) throws SQLException {

		String sql = "insert into OtnLspInformation (NetworkId, DemandId, LineRate,Path, WavelengthNo,"
				+ "RoutePriority, CircuitId, TrafficType, ProtectionType, OtnLspTunnelId,LspId, ForwardingAdj) select ?, DemandId, LineRate,Path, WavelengthNo,"
				+ "RoutePriority, CircuitId, TrafficType, ProtectionType, OtnLspTunnelId,LspId,ForwardingAdj from OtnLspInformation where NetworkId = ? ";
		logger.info("copyOtnLspInformationInBrField: " + sql);
		jdbcTemplate.update(sql, networkidBrField, networkidGrField);
	}

	public int count(int networkid) {
		String sql = "select count(*) from OtnLspInformation where NetworkId = ? ";
		return jdbcTemplate.queryForObject(sql, int.class, networkid);
	}

	/**
	 * finds the list of added/new Otn Lsp in brownfield : Add based on TunnelId as
	 * it will be unique
	 * 
	 * @param networkidGrField
	 * @param networkidBrField
	 * @sample query : select * from (select bf.NetworkId, bf.DemandId,bf.LineRate,
	 *         bf.WavelengthNo, bf.CircuitId, bf.TrafficType, bf.ProtectionType,
	 *         bf.RoutePriority, bf.Path, bf.OtnLspTunnelId, bf.LspId, bf.NetworkId
	 *         as NetworkIdBf, bf.DemandId as DemandIdBf, gf.NetworkId as
	 *         NetworkIdGf, gf.DemandId as DemandIdGf from ( select * from
	 *         OtnLspInformation where NetworkId = 35 ) as bf left join ( select *
	 *         from OtnLspInformation where NetworkId = 27 ) as gf on
	 *         gf.OtnLspTunnelId=bf.OtnLspTunnelId ) as t where t.DemandIdGf is NULL
	 *         and t.NetworkIdGf is NULL;
	 *
	 * @author hp
	 * @created 1st Jun, 2018
	 * @return
	 */
	public List<OtnLspInformation> findAddedOtnLspInBrField(int networkidGrField, int networkidBrField) {
		String sql = "select * from"
				+ "(select bf.NetworkId, bf.DemandId,bf.LineRate, bf.WavelengthNo, bf.CircuitId, bf.TrafficType, bf.ProtectionType,"
				+ "  bf.RoutePriority, bf.Path, bf.OtnLspTunnelId, bf.LspId,"
				+ "  bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, "
				+ "  gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from  "
				+ "    ( select * from OtnLspInformation where NetworkId = ? ) as bf " + "  left join "
				+ "    ( select * from OtnLspInformation where NetworkId = ?  ) as gf "
				+ "  on gf.OtnLspTunnelId=bf.OtnLspTunnelId ) as t "
				+ "  where t.DemandIdGf is NULL and t.NetworkIdGf is NULL;    ";

		List<OtnLspInformation> OtnLspInformation;
		try {
			return OtnLspInformation = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OtnLspInformation.class),
					networkidBrField, networkidGrField);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * finds the list of Common Otn Lsp in brownfield [which exists in greenfield
	 * too]
	 * 
	 * @param networkidGrField
	 * @param networkidBrField
	 * @sample query : select * from (select bf.NetworkId, bf.DemandId,
	 *         bf.CircuitId, bf.RoutePriority, bf.TrafficType, bf.Path,
	 *         bf.OtnLspTunnelId, bf.NetworkId as NetworkIdBf, bf.DemandId as
	 *         DemandIdBf, bf.LspId, gf.NetworkId as NetworkIdGf, gf.DemandId as
	 *         DemandIdGf from ( select * from OtnLspInformation where NetworkId =
	 *         35 ) as bf left join ( select * from OtnLspInformation where
	 *         NetworkId = 27 ) as gf on gf.DemandId = bf.DemandId and
	 *         gf.OtnLspTunnelId = bf.OtnLspTunnelId and gf.RoutePriority =
	 *         bf.RoutePriority and gf.Path = bf.Path and gf.CircuitId =
	 *         bf.CircuitId ) as t where t.NetworkIdGf IS NOT NULL and t.DemandIdGf
	 *         IS NOT NULL;
	 *
	 * @author hp
	 * @created 4th Jun, 2018
	 * @return
	 */
	public List<OtnLspInformation> findCommonOtnLspInBrField(int networkidGrField, int networkidBrField) {
		String sql = "select * from " + "(select bf.NetworkId, bf.DemandId, bf.CircuitId, bf.RoutePriority,"
				+ " bf.TrafficType, bf.Path, bf.OtnLspTunnelId,bf.LspId,"
				+ " bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, "
				+ " gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from  "
				+ " ( select * from OtnLspInformation where NetworkId = ? ) as bf  " + " left join  "
				+ " ( select * from OtnLspInformation where NetworkId = ?  ) as gf "
				+ " on  gf.DemandId = bf.DemandId and gf.OtnLspTunnelId =  bf.OtnLspTunnelId"
				+ " and gf.RoutePriority = bf.RoutePriority" + " and gf.Path = bf.Path"
				+ " and gf.TrafficType = bf.TrafficType" + " and gf.LspId = bf.LspId "
				+ " and gf.CircuitId = bf.CircuitId " + " ) as t"
				+ " where t.NetworkIdGf IS NOT NULL and t.DemandIdGf IS NOT NULL;";

		try {
			List<OtnLspInformation> otnLspInformationList = null;
			return otnLspInformationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OtnLspInformation.class),
					networkidBrField, networkidGrField);
		} catch (Exception e) {
			return null;
		}
	}

	public void deleteSpecificOtnLsp(int networkid, DemandMapping demandMappingOBJ) {
		logger.info("Delete OtnLspInformation" + demandMappingOBJ);

		List<OtnLspInformation> otnLspInformationsList = findLsp(networkid, demandMappingOBJ.getDemandId(),
				demandMappingOBJ.getTrafficType());

		for (OtnLspInformation rowObj : otnLspInformationsList) {

			List<String> circuitIdList = new ArrayList<String>(Arrays.asList(rowObj.getCircuitId().split(",")));
			System.out.println("String.valueOf(demandMappingOBJ.getCircuitId()) "
					+ String.valueOf(demandMappingOBJ.getCircuitId()));
			circuitIdList.remove(String.valueOf(demandMappingOBJ.getCircuitId()));

			System.out.println(" circuitIdList To Update :  " + circuitIdList);
			String CircuitIdString = StringUtils.join(circuitIdList, ",");
			System.out.println("Final  circuitIdList To Update :  " + CircuitIdString);

			if (circuitIdList.isEmpty()) {/** No More Circuits Left for this LSP => Delete LambdaLsp too */
				System.out.println(" Goinf to delete Lambda too ");
				String sql = "delete from OtnLspInformation where NetworkId = ? and DemandId=? and TrafficType=?";
				jdbcTemplate.update(sql, networkid, rowObj.getDemandId(), rowObj.getTrafficType());

//	        		sql = "delete from LambdaLspInformation where NetworkId = ? and DemandId=? ";
//	    	        jdbcTemplate.update(sql, networkid,rowObj.getDemandId());

			} else {/** Update the CircuitSet of LSP */

				OtnLspInformation obj = new OtnLspInformation();
				obj.setCircuitId(CircuitIdString);
				updateLsp(networkid, rowObj.getDemandId(), rowObj.getTrafficType(), obj);

			}

		}

	}

	/**
	 * finds the list of Deleted Otn Lsp in brownfield [which exists in greenfield ]
	 * 
	 * @param networkidGrField
	 * @param networkidBrField
	 * @sample query :
	 * 
	 *         select * from (select gf.NetworkId, gf.DemandId,gf.LineRate,
	 *         gf.WavelengthNo, gf.CircuitId, gf.TrafficType, gf.ProtectionType,
	 *         gf.RoutePriority, gf.Path, gf.OtnLspTunnelId,gf.LspId, bf.NetworkId
	 *         as NetworkIdBf, bf.DemandId as DemandIdBf, gf.NetworkId as
	 *         NetworkIdGf, gf.DemandId as DemandIdGf from ( select * from
	 *         OtnLspInformation where NetworkId = 27 ) as gf left join ( select *
	 *         from OtnLspInformation where NetworkId = 35 ) as bf on gf.DemandId =
	 *         bf.DemandId and gf.OtnLspTunnelId = bf.OtnLspTunnelId and
	 *         gf.TrafficType= bf.TrafficType) as t where t.DemandIdBf is NULL and
	 *         t.Path LIKE '1,%' ;
	 *
	 * @author hp
	 * @created 5th Jun, 2018
	 * @return
	 */
	public List<Map<String, Object>> findDeletedOtnLspInBrField(int networkidGrField, int networkidBrField,
			int... sourceNode) {

		String pathLike = "";

		String sql = "select * from"
				+ " (select gf.NetworkId, gf.DemandId,gf.LineRate, gf.WavelengthNo, gf.CircuitId, gf.TrafficType, gf.ProtectionType,"
				+ " gf.RoutePriority, gf.Path, gf.OtnLspTunnelId,gf.LspId,"
				+ " bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf,"
				+ " gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from"
				+ " ( select * from OtnLspInformation where NetworkId = ? ) as gf" + " left join"
				+ " ( select * from OtnLspInformation where NetworkId = ?  ) as bf"
				+ " on gf.DemandId = bf.DemandId and gf.OtnLspTunnelId = bf.OtnLspTunnelId and gf.TrafficType= bf.TrafficType) as t"
				+ " where t.DemandIdBf is  NULL  ";

		if (sourceNode.length > MapConstants.I_ZERO) {

			pathLike = sourceNode[MapConstants.I_ZERO] + ",%";

			sql += " and t.Path LIKE ?";

		}

		try {
			List<Map<String, Object>> otnLspInformationList = null;

			if (pathLike.equalsIgnoreCase(""))
				return otnLspInformationList = jdbcTemplate.queryForList(sql, networkidGrField, networkidBrField);
			else
				return otnLspInformationList = jdbcTemplate.queryForList(sql, networkidGrField, networkidBrField,
						pathLike);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * finds the list of Modified Otn Lsp in brownfield [ As of now modification on
	 * CircuitId and TrafficType only checked]
	 * 
	 * @param networkidGrField
	 * @param networkidBrField
	 * @sample query :
	 * 
	 * 
	 * 
	 *         select * from (select bf.NetworkId, bf.DemandId, bf.CircuitId,
	 *         bf.RoutePriority, bf.TrafficType, bf.Path, bf.OtnLspTunnelId,
	 *         bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, gf.NetworkId
	 *         as NetworkIdGf, gf.DemandId as DemandIdGf from ( select * from
	 *         OtnLspInformation where NetworkId = 41 ) as bf left join ( select *
	 *         from OtnLspInformation where NetworkId = 40 ) as gf on gf.DemandId =
	 *         bf.DemandId and gf.OtnLspTunnelId = bf.OtnLspTunnelId and
	 *         gf.RoutePriority = bf.RoutePriority and gf.Path = bf.Path and
	 *         gf.WavelengthNo = bf.WavelengthNo where gf.CircuitId <> bf.CircuitId
	 *         or gf.TrafficType <> bf.TrafficType
	 * 
	 *         ) as t where t.NetworkIdGf IS NOT NULL and t.DemandIdGf IS NOT NULL;
	 *
	 * @author hp
	 * @created 21st July, 2018
	 * @return
	 */

	public List<Map<String, Object>> findModifiedOtnLspInBrField(int networkidGrField, int networkidBrField,
			int... sourceNode) {

		String pathLike = "";

		String sql = "select * from  \n"
				+ "			        			(select bf.NetworkId, bf.DemandId, bf.CircuitId, bf.RoutePriority, bf.TrafficType, bf.Path, bf.OtnLspTunnelId,\n"
				+ "									bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, \n"
				+ "									gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from   \n"
				+ "			        			( select * from OtnLspInformation where NetworkId =  ? ) as bf  \n"
				+ "			        			left join  \n"
				+ "			        			( select * from OtnLspInformation where NetworkId =  ?  ) as gf \n"
				+ "			        			on  gf.DemandId = bf.DemandId and gf.OtnLspTunnelId =  bf.OtnLspTunnelId\n"
				+ "		                           and gf.RoutePriority = bf.RoutePriority\n"
				+ "		                           and gf.Path = bf.Path\n"
				+ "		                           and gf.WavelengthNo = bf.WavelengthNo\n"
				+ "		                        where gf.CircuitId <> bf.CircuitId or gf.TrafficType <> bf.TrafficType\n"
				+ "		                        \n" + "		                        ) as t\n"
				+ "			        			where t.NetworkIdGf IS NOT NULL and t.DemandIdGf IS NOT NULL     ";

		if (sourceNode.length > MapConstants.I_ZERO) {

			pathLike = sourceNode[MapConstants.I_ZERO] + ",%";

			sql += " and t.Path LIKE ?";

		}

		try {

			List<Map<String, Object>> otnLspInformationList = null;

			if (pathLike.equalsIgnoreCase(""))
				return otnLspInformationList = jdbcTemplate.queryForList(sql, networkidBrField, networkidGrField);
			else
				return otnLspInformationList = jdbcTemplate.queryForList(sql, networkidBrField, networkidGrField,
						pathLike);

		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public OtnLspInformation findLspFor10GAgg(int networkid, int demandId, String circuitId, String Path,
			String TrafficType, int RoutePriority, int typeOfOtnLsp) {

		OtnLspInformation routes = null;

		try {
			String sql = "SELECT "
					+ " NetworkId, DemandId, LineRate, Path, WavelengthNo, RoutePriority, CircuitId, TrafficType, ProtectionType, OtnLspTunnelId, (LspId+1) as LspId "
					+ " FROM OtnLspInformation where NetworkId = ? and DemandId =?  and CircuitId = ?  and  TrafficType =? and RoutePriority=? ";

			System.out.println("Tempppp ===> " + networkid + "," + demandId + "," + circuitId + "," + Path + ","
					+ TrafficType + "," + RoutePriority);
			routes = (OtnLspInformation) jdbcTemplate.queryForObject(sql,
					new BeanPropertyRowMapper(OtnLspInformation.class), networkid, demandId, circuitId, TrafficType,
					RoutePriority - 1);

			logger.debug("OtnLspInformation findLspFor10GAgg  : " + routes.toString());

		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return routes;

	}

	@SuppressWarnings("unchecked")
	public OtnLspInformation findLSP(int networkid, int demandId, int TunnelId, int LSPId) {

		String sql = "SELECT * FROM OtnLspInformation where NetworkId = ? and DemandId = ? and OtnLspTunnelId = ? and LspId = ?";
		try {
			OtnLspInformation routes = (OtnLspInformation) jdbcTemplate.queryForObject(sql,
					new BeanPropertyRowMapper(OtnLspInformation.class), networkid, demandId, TunnelId, LSPId);
			logger.debug("OtnLspInformation findLsp  : " + routes.toString());
			return routes;
		} catch (EmptyResultDataAccessException e) {

			return null;
		}

	}

	public void updatecircuitID(int networkid, int OtnLspTunnelId, String CircuitID) {

		String sql = "Update OtnLspInformation set CircuitId = ? where NetworkId = ? and OtnLspTunnelId = ? ";
		jdbcTemplate.update(sql, CircuitID, networkid, OtnLspTunnelId);

	}
	
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public List<OtnLspInformation > findCircuitIdByTunnelId(int networkId, int OtnLspTunnelId)
	 {      
			String sql = "SELECT * FROM  OtnLspInformation where NetworkId = ? and OtnLspTunnelId = ? ;";
	
			
			try
			  {  List<OtnLspInformation> OtnLsp  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OtnLspInformation.class),networkId,OtnLspTunnelId);
			return OtnLsp ;
		       }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	 }
	 
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public void deleteSpecificOtnLsp(int networkId, int DemandId,int OtnLspTunnelId,int LSPId)
	 {
		 String sql = "DELETE FROM  OtnLspInformation where NetworkId = ? and DemandId = ? and OtnLspTunnelId = ? and LspId = ?;";
		
			 
			 jdbcTemplate.update(sql,networkId,DemandId,OtnLspTunnelId,LSPId);
			 logger.info("Deleting OtnLsp: "+sql); 	
	 }
	 


		@SuppressWarnings("unchecked")
		public OtnLspInformation findLsp(int networkid, int demandId, String Path, int routePriority, int otnLspTunnelId, int lspId) {

			System.out.println(networkid+ " , "+ demandId + " , "+ Path + " , "+ routePriority);
			String sql = "SELECT * FROM OtnLspInformation where NetworkId = ? and DemandId =? and Path =? and RoutePriority = ? and OtnLspTunnelId = ? and LspId = ? ";
			OtnLspInformation routes = null;
			routes = (OtnLspInformation)jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(OtnLspInformation.class), networkid, demandId, Path, 
						routePriority, otnLspTunnelId, lspId);
			logger.debug("OtnLspInformation findLsp  for FA: " + routes.toString());

			return routes;
		}
		
		
		public void updateOtnLspInformationFA(OtnLspInformation newOtnLSpInformationObj) {
			String sql = "update OtnLspInformation set ForwardingAdj=?  where NetworkId=? and DemandId=? and Path = ? "+
						" and RoutePriority = ? and  OtnLspTunnelId = ? and LspId = ? ";
			try {
				jdbcTemplate.update(sql, newOtnLSpInformationObj.getForwardingAdj(),
										newOtnLSpInformationObj.getNetworkId(),
						 				newOtnLSpInformationObj.getDemandId(),
						 				newOtnLSpInformationObj.getPath(),
						 				newOtnLSpInformationObj.getRoutePriority(),
						 				newOtnLSpInformationObj.getOtnLspTunnelId(),
						 				newOtnLSpInformationObj.getLspId());
						 				
										
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		public int  findMaxFA(int networkId, OtnLspInformation otnLspInformationObj) {
			
			int maxFA = -1;
			try {
				String sql = "SELECT MAX(ForwardingAdj + 100)  as ForwardingAdj  FROM OtnLspInformation where NetworkId = ?";
//				String sql = "SELECT ForwardingAdj  FROM OtnLspInformation where NetworkId = ? and Deamand = ? and Path = ? and WavelengthNo = ? and RoutePriority= ?";
				
				maxFA = jdbcTemplate.queryForObject(sql, int.class, networkId);
//				maxFA = jdbcTemplate.queryForObject(sql, int.class, networkId, otnLspInformationObj.getDemandId(), otnLspInformationObj.getPath(),
//													otnLspInformationObj.getWavelengthNo(), otnLspInformationObj.getRoutePriority());
//				
//				maxFA += DataPathConfigFileConstants.TengAggFAOffset;
				
				logger.debug("OtnLspInformation maxFA  : " + maxFA);	
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return maxFA;
		}
		


}
