package org.apache.mybatis.jpa.dialect;


import java.sql.PreparedStatement;
import java.util.HashMap;

import org.apache.mybatis.jpa.persistence.JpaPagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * DB Access Dialect,inspiration from hibernate
 * 
 * @author Crystal.Sea
 * Create 2017-7-24
 *
 */
public abstract class Dialect {

	private static final Logger _logger 			= 	LoggerFactory.getLogger(Dialect.class);

	public static final String DEFAULT_BATCH_SIZE	= 	"15";
	public static final String NO_BATCH 			= 	"0";
	public static final String DEFAULT_DIALECT 		= 	"mysql";

	protected static HashMap<String,String> dialectMap;
	
	static {
		
		dialectMap=new HashMap<String,String>();
		dialectMap.put("db2", 			"org.apache.mybatis.jpa.dialect.DB2Dialect");
		dialectMap.put("derby", 		"org.apache.mybatis.jpa.dialect.DerbyDialect");
		dialectMap.put("mysql", 		"org.apache.mybatis.jpa.dialect.MySQLDialect");
		dialectMap.put("oracle", 		"org.apache.mybatis.jpa.dialect.OracleDialect");
		dialectMap.put("postgresql", 	"org.apache.mybatis.jpa.dialect.PostgreSQLDialect");
		dialectMap.put("sqlserver", 	"org.apache.mybatis.jpa.dialect.SQLServerDialect");
		
		_logger.trace("Dialect Mapper : \n"+dialectMap);
	}
	
	// constructors and factory methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected Dialect() {

	}

	@Override
    public String toString() {
		return getClass().getName();
	}

	/**
	 * Given a limit and an offset, apply the limit clause to the query.
	 *
	 * @param query The query to which to apply the limit.
	 * @param offset The offset of the limit
	 * @param limit The limit of the limit ;)
	 * @return The modified query statement with the limit applied.
	 */

	public String getLimitString(String query, JpaPagination pagination) {
		throw new UnsupportedOperationException( "Paged queries not supported by " + getClass().getName());
	}
	
	
	
	
	public String getPreparedStatementLimitString(String query, JpaPagination pagination) {
		throw new UnsupportedOperationException( "Paged queries not supported by " + getClass().getName());
	}
	
	
	public void setLimitParamters(PreparedStatement preparedStatement,int parameterSize,JpaPagination pagination) {
		throw new UnsupportedOperationException( "Paged queries not supported by " + getClass().getName());
	}
	
	
	public boolean supportsLimit() {
		return false;
	}

	/**
	 * @return the dialectMap
	 */
	public static HashMap<String, String> getDialectMap() {
		return dialectMap;
	}
	
	public static String getDialect(String dialect) {
		String dialectString =dialectMap.get(dialect);
		if(dialectString == null) {
			dialectString =dialectMap.get(DEFAULT_DIALECT);
		}
		return dialectString;
	}
	
	
}
