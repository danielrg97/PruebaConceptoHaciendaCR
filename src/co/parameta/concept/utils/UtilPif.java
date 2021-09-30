package co.parameta.concept.utils;

import java.math.BigDecimal;
import java.util.Collection;

public class UtilPif {
    /**
	 * Metodo para validar objetos, string, interger, double, bigdecimal, collection si son nulos o ceros y vacios respectivamente 
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(Object obj){
        if(obj==null)return true;
        if(obj instanceof String) return ((String)obj).trim().equals("") || ((String)obj).equalsIgnoreCase("NULL");
        if(obj instanceof Integer) return ((Integer)obj)==0;
        if(obj instanceof Double) return ((Double)obj).equals(0.0);
        if(obj instanceof BigDecimal) return ((BigDecimal)obj).equals(new BigDecimal(0.0));
        if(obj instanceof Collection) return ((Collection)obj).isEmpty();
        return false;
    }

	/**
	 * Metodo para agregar un sufijo con un guion bajo a una variable en el caso que no sea nula o vacia
	 */	
	public static String getSufijo(String valor, String sufijo) {
		String temp = valor;
		if(!isNullOrEmpty(sufijo)) {
			temp = valor + "_" + sufijo;
		}
		return temp;
	}
}
