package co.parameta.concept.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.parameta.concept.dataSource.MySqlDataSource;

public class GlobalInstances implements GeneralConstantsLocal{

	private static Logger logger = LogManager.getLogger(GlobalInstances.class.getSimpleName());
	
	private static Properties properties = null;
	
	private static String FILE_PROPERTIES = "pif_for_coupa.properties";
	private static String RUTA_DEFAULT_linux = "/pif_configuration/";
	private static String RUTA_DEFAULT_windows = "c:\\pif_configuration\\";
	
	public static Properties getPropertiesInstance(String file_proerties){
		if (properties == null) {
			try {
				if(!(file_proerties != null && !file_proerties.equals(""))) {
					file_proerties = FILE_PROPERTIES;
				}
				properties = loadApplicationProperties(file_proerties);
				String cod_pais = properties.getProperty(COD_PAIS, "");
				if(!properties.containsKey(CODE_CONFIG)) {
					properties.put(CODE_CONFIG, cod_pais);
				} else if(UtilPif.isNullOrEmpty(properties.getProperty(CODE_CONFIG))) {
					properties.replace(CODE_CONFIG, cod_pais);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				System.exit(0);
			}
		}
		return properties;
	}
	
	private static Properties loadApplicationProperties(String file_proerties) throws Exception {
		Properties p_os = System.getProperties();
		Properties properties = new Properties();
		try {
			String os = p_os.getProperty("os.name");
			logger.info(os);
			logger.info("CARGA DE LOS PARAMETROS DEL ARCHIVO DE CONFIGURACION " + file_proerties);
			if(os.equalsIgnoreCase("Linux")) {
				properties.load(new FileInputStream(RUTA_DEFAULT_linux+file_proerties));
			} else {
				properties.load(new FileInputStream(RUTA_DEFAULT_windows+file_proerties));
			}
			String db_config = properties.getProperty(DB_CONFIG);
			String cod_pais = properties.getProperty(COD_PAIS);
			String schema = properties.getProperty(UtilPif.getSufijo(DB_DBNAME, cod_pais));
			if(!UtilPif.isNullOrEmpty(db_config)) {
				if(db_config.contains(",")) {
					String[] db_arr = db_config.split(",");
					for (String schemaStr : db_arr) {
						if(!schema.equals(schemaStr)) {
							properties = loadDbProperties(properties, schemaStr);
						}
					}
				} else if(!schema.equals(db_config)) {
					properties = loadDbProperties(properties, db_config);
				}
			}
			properties = loadDbProperties(properties, schema);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(),e);
			System.exit(0);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			System.exit(0);
		}		
		return properties;
	}
	
	private static Properties loadDbProperties(Properties properties, String schema) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			logger.info("CARGA DE LOS PARAMETROS DE LA BASE DE DATOS " + schema);
			BasicDataSource dataSource = MySqlDataSource.getConnection(properties);
			if(dataSource != null) {
				connection = dataSource.getConnection();
				String query = String.format("SELECT * FROM %s.pif_configuration WHERE STATUS_CONFIG = 1", schema);
				
				statement = connection.prepareStatement(query);
				resultSet = statement.executeQuery();
				int i = 0;
				while(resultSet.next()) {
					if(!UtilPif.isNullOrEmpty(resultSet.getObject(1)) && !UtilPif.isNullOrEmpty(resultSet.getObject(2))) {
						if(properties.containsKey(resultSet.getObject(1))) {
							properties.replace(resultSet.getObject(1), resultSet.getObject(2));
						} else {
							properties.put(resultSet.getObject(1), resultSet.getObject(2));
							i++;
						}
					}
				}
				logger.info("SE CARGO TODA LA CONFIGURACION DESDE DB " + i);
			} else {
				logger.info("NO SE CARGO TODA LA CONFIGURACION DESDE DB ");
			}
		} catch (Exception e) {
			logger.info("Problema al cargar la configuración de la db ");
		} finally {
			if(resultSet != null) {
				resultSet.close();
			}
			if(statement != null) {
				statement.close();
			}
			if(connection != null) {
				connection.close();
			}
		}
		return properties;
	}
	
}
