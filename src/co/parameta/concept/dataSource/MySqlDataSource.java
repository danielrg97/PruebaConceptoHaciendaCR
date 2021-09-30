package co.parameta.concept.dataSource;

import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.parameta.concept.utils.GeneralConstantsLocal;
import co.parameta.concept.utils.UtilPif;


public class MySqlDataSource {
	
	private static Logger logger = LogManager.getLogger(MySqlDataSource.class.getSimpleName());
	private static BasicDataSource dataSource;
	private static boolean valid = false;

	public static BasicDataSource getConnection(Properties properties) {
		
		if(dataSource == null) {
			String sufijo = properties.getProperty(GeneralConstantsLocal.COD_PAIS);
			String USER_NAME 	= properties.getProperty(UtilPif.getSufijo("DB_USER", sufijo));
			String PASSWORD_DB 	= properties.getProperty(UtilPif.getSufijo("DB_PASS", sufijo));
			String HOST_DB 		= properties.getProperty(UtilPif.getSufijo("DB_HOST", sufijo));
			String PORT_DB 		= properties.getProperty(UtilPif.getSufijo("DB_PORT", sufijo));
			String NAME_DB 		= properties.getProperty(UtilPif.getSufijo("DB_DBNAME", sufijo));
			String EXTRA_DB 	= properties.getProperty(UtilPif.getSufijo("DB_EXTRA", sufijo));

			dataSource = getConnection(USER_NAME, PASSWORD_DB, HOST_DB, PORT_DB, NAME_DB, EXTRA_DB);
		}
		return dataSource;
		
	}

	public static BasicDataSource getConnection(Properties properties, String NAME_DB) {
		if(dataSource == null) {
			String sufijo = properties.getProperty(GeneralConstantsLocal.COD_PAIS);
			String USER_NAME 	= properties.getProperty(UtilPif.getSufijo("DB_USER", sufijo));
			String PASSWORD_DB 	= properties.getProperty(UtilPif.getSufijo("DB_PASS", sufijo));
			String HOST_DB 		= properties.getProperty(UtilPif.getSufijo("DB_HOST", sufijo));
			String PORT_DB 		= properties.getProperty(UtilPif.getSufijo("DB_PORT", sufijo));
			String EXTRA_DB 	= properties.getProperty(UtilPif.getSufijo("DB_EXTRA", sufijo));
			
			dataSource = getConnection(USER_NAME, PASSWORD_DB, HOST_DB, PORT_DB, NAME_DB, EXTRA_DB);
		}
		return dataSource;
		
	}
	
	public static BasicDataSource getConnection(String USER_NAME, String PASSWORD_DB
			, String HOST_DB, String PORT_DB
			, String NAME_DB, String EXTRA_DB) {
			
		try {
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
			dataSource.setUsername(USER_NAME);
			dataSource.setPassword(PASSWORD_DB);
			dataSource.setUrl("jdbc:mysql://"+HOST_DB+":"+PORT_DB+"/"+NAME_DB+EXTRA_DB);
			dataSource.setCacheState(false);
			logger.info(dataSource.getUrl());
			valid = true;
		} catch (Exception e) {
			logger.error("Problema en la creación de la conexión MYSQL",e);
		}
			
		return dataSource;
	}

	public static boolean isValid() {
		return valid;
	}	
	
}
