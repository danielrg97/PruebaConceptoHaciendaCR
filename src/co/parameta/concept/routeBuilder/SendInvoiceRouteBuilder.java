package co.parameta.concept.routeBuilder;

import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

import co.parameta.concept.utils.GeneralConstantsLocal;

public class SendInvoiceRouteBuilder extends RouteBuilder implements GeneralConstantsLocal{
	private Properties properties;
	public SendInvoiceRouteBuilder(Properties properties) {
		this.properties = properties;
	}
	
	@Override
	public void configure() throws Exception {
		restRequest();
		sendInvoice();
	}
	/**
	 * Metodo que indica los Endpoints para enviar factura a recepcion de facturas y cerrar la sesion
	 */
	private void restRequest() {
		rest(properties.getProperty(ROUTE_WS))
		.post("send_invoice")
		.route()
		.id("restInvoice") 
		.setExchangePattern(ExchangePattern.InOut)
		.to(ROUTE_SEND_INVOICE)
    	.end();
		
		rest(properties.getProperty(ROUTE_WS))
		.post("close_session")
		.route()
		.id("restCloseSession") 
		.setExchangePattern(ExchangePattern.InOut)
		.to(ROUTE_CERRAR_TOKEN)
    	.end();
	}
	private void sendInvoice() {
		from(ROUTE_SEND_INVOICE)
		.routeId(ROUTE_SEND_INVOICE_ID)
		.setProperty("EXPIRED_TOKEN", simple("false"))
		.doTry()
			.choice()
				.when(simple("${exchangeProperty.authorizationToken} == '' || ${exchangeProperty.authorizationToken} == null"))
					.to(ROUTE_GENERATE_TOKEN)
				.otherwise()
					.choice()
						.when(simple("${exchangeProperty.EXPIRED_TOKEN} == true"))
							.to(ROUTE_REFRESCAR_TOKEN)
					.end()
			.end()
			.setProperty(Exchange.HTTP_METHOD, constant(HTTP_METHOD_POST))
			.setProperty(Exchange.CONTENT_TYPE, constant(APPLICATION_XML))
			.setProperty("authorization", simple("Bearer ${exchangeProperty.authorizationToken}"))
			.log("Bearer ${exchangeProperty.authorizationToken}")
			.toD(SEND_INVOICE_URI)
		.endDoTry()
		.doCatch(Exception.class)
			.log("Se presento una excepcion al enviar factura. ${exception.message}")
		.end(); 
	}
}
