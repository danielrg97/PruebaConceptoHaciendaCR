package co.parameta.concept.routeBuilder;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import co.parameta.concept.utils.GeneralConstantsLocal;

public class AuthorizationTokenRouteBuilder extends RouteBuilder implements GeneralConstantsLocal{
	private final static Logger logger = LogManager.getLogger(AuthorizationTokenRouteBuilder.class);
	@SuppressWarnings("serial")
	private final static Map<String, String> GENERATE_TOKEN_BODY  = new HashMap<>() {{
	    put("client_id", "api-stag");//Test: 'api-stag' Production: 'api-prod'
	    put("client_secret", "");//always empty
	    put("grant_type", "password");//always 'password'
        //go to https://www.hacienda.go.cr/ATV/login.aspx to generate a username and password credentials
	    put("username", "");
	    put("password", "");
	    put("scope", "");//always empty
	}};
	
	@Override
	public void configure() throws Exception {
		obtenerToken();
		cerrarToken();
		refrescarToken();
	}
		
	private void obtenerToken() {
		from(ROUTE_GENERATE_TOKEN)
		.routeId(ROUTE_GENERATE_TOKEN_ID)
		.doTry()
			.setProperty(Exchange.HTTP_METHOD, constant(HTTP_METHOD_POST))
			.setProperty(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
			.setBody(constant(GENERATE_TOKEN_BODY))
			.toD(GENERATE_TOKEN_URI)
			.log("Response ${header.CamelHttpResponseCode} ${header.CamelHttpResponseText}")
			.doTry()
				.process(exchange ->{
					JSONObject object = new JSONObject(exchange.getMessage().getBody(String.class));
					logger.info("Authorization token: "+object.get("access_token"));
					exchange.setProperty("authorizationToken", object.get("access_token"));
					exchange.setProperty("refreshToken", object.get("refresh_token"));
				})
			.doCatch(JSONException.class)
				.log("No se pudo obtener access token del Json de respuesta: "+body())
		.doCatch(Exception.class)
			.log("Se presento una excepcion al obtener el token de acceso de hacienda.")
		.end(); 
	}
	private void cerrarToken() {
		from(ROUTE_CERRAR_TOKEN)
		.routeId(ROUTE_CERRAR_TOKEN_ID)
		.doTry()
			.setProperty(Exchange.HTTP_METHOD, constant(HTTP_METHOD_POST))
			.setProperty(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
			.process(exchange ->{
				@SuppressWarnings("serial")
				Map<String, String> map  = new HashMap<>() {{
				    put("client_id", "api-stag");//Test: 'api-stag' Production: 'api-prod'
				    put("refresh_token", exchange.getProperty("refreshToken", String.class));
				}};
				exchange.getMessage().setBody(map);
			})
			.toD(CERRAR_SESION_URI)
		.doCatch(Exception.class)
			.log("Se presento una excepcion al cerrar el token de acceso de hacienda.")
		.end(); 
	}
	private void refrescarToken() {
		from(ROUTE_REFRESCAR_TOKEN)
		.routeId(ROUTE_REFRESCAR_TOKEN_ID)
		.doTry()
			.setProperty(Exchange.HTTP_METHOD, constant(HTTP_METHOD_POST))
			.setProperty(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
			.process(exchange ->{
				@SuppressWarnings("serial")
				Map<String, String> map  = new HashMap<>() {{
				    put("client_id", "api-stag");//Test: 'api-stag' Production: 'api-prod'
				    put("grant_type", "refresh_token");
				    put("refresh_token", exchange.getProperty("refreshToken", String.class));
				}};
				exchange.getMessage().setBody(map);
			})
			.toD(GENERATE_TOKEN_URI)
		.doCatch(Exception.class)
			.log("Se presento una excepcion al refrescar el token de acceso de hacienda.")
		.end(); 
	}
}
