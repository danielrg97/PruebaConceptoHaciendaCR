package co.parameta.concept.main;

import java.util.Properties;

import org.apache.camel.main.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.parameta.concept.routeBuilder.AuthorizationTokenRouteBuilder;
import co.parameta.concept.routeBuilder.RestApiRouteBuilder;
import co.parameta.concept.routeBuilder.SendInvoiceRouteBuilder;
import co.parameta.concept.utils.GlobalInstances;
;

public class MainClass {

	private static Logger logger = LogManager.getLogger(MainClass.class);

	public static void main(String... args) throws Exception {
		String file_proerties = "";
	
		try {
			file_proerties = args[0];
		} catch (Exception e) {
			logger.error("Problema al obtener el parametro del archivo de configuración", e);
			file_proerties = "";
		}
	
		Properties properties = GlobalInstances.getPropertiesInstance(file_proerties);
		Main main = new Main();
		String port = "1234";
		main.configure().addRoutesBuilder(new RestApiRouteBuilder(port));
		main.configure().addRoutesBuilder(new SendInvoiceRouteBuilder(properties));
		main.configure().addRoutesBuilder(new AuthorizationTokenRouteBuilder());

		main.run();
	}
}
