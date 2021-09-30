package co.parameta.concept.utils;

public interface GeneralConstantsLocal{
	//Routes
	public final static String ROUTE_GENERATE_TOKEN = "direct:generateToken";
	public final static String ROUTE_SEND_INVOICE = "direct:sendInvoice";
	public final static String ROUTE_CERRAR_TOKEN = "direct:cerrarToken";
	public final static String ROUTE_REFRESCAR_TOKEN = "direct:refreshToken";



	//Route Ids
	public final static String ROUTE_GENERATE_TOKEN_ID = "ROUTE_GENERATE_TOKEN";
	public final static String ROUTE_SEND_INVOICE_ID = "ROUTE_SEND_INVOICE";
	public final static String ROUTE_CERRAR_TOKEN_ID = "ROUTE_CERRAR_TOKEN";
	public final static String ROUTE_REFRESCAR_TOKEN_ID = "ROUTE_REFRESH_TOKEN";


	public static final String ROUTE_WS = "ROUTE_WS";
	public static final String HTTP_METHOD_POST = "POST";
	public static final String APPLICATION_XML = "application/xml";
	public static final String COD_PAIS 			= "COD_PAIS";
	public static final String DB_CONFIG			= "DB_CONFIG";
	public static final String DB_DBNAME			= "DB_DBNAME";
	public static final String DB_DEFAULT_SCHEMA	= "DB_DEFAULT_SCHEMA";
	public static final String CODE_CONFIG			= "CODE_CONFIG";


	
	//URI (debe estar en BD)
	public final static String GENERATE_TOKEN_URI= "https://idp.comprobanteselectronicos.go.cr/auth/realms/rut-stag/protocol/openid-connect/token";
	public final static String SEND_INVOICE_URI= "https://api.comprobanteselectronicos.go.cr/recepcion-sandbox/v1/recepcion";
	public final static String CERRAR_SESION_URI = "“https://idp.comprobanteselectronicos.go.cr/auth/realms/rut-stag/protocol/openid-connect/logout";
}
