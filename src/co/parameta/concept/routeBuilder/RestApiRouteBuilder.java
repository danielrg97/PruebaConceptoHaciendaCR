package co.parameta.concept.routeBuilder;

import org.apache.camel.builder.RouteBuilder;

public class RestApiRouteBuilder extends RouteBuilder {
	
	private String portForJetty;
	
	public RestApiRouteBuilder(String portForJetty) {
		this.portForJetty = portForJetty; 
	}

	@Override
	public void configure() throws Exception {
		this.configureJettyComponent();
	}
	
	/**
	 * Configuración del servidor Jetty
	 */
	private void configureJettyComponent() {
		restConfiguration()		
		.component("jetty")
		.enableCORS(true)
		.port(portForJetty)
		.corsHeaderProperty("Access-Control-Allow-Origin", "*")
		.corsHeaderProperty("Access-Control-Allow-Headers", 
				"Access-Control-Allow-Headers, Origin,Accept"
				+ ", X-Requested-With"
				+ ", Content-Type"
				+ ", Access-Control-Request-Method");
	}
}
