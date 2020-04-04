package br.com.virtuallibrary.commons;

public interface IConstants {
	String PACKAGE = "br.com.virtuallibrary";
	
	String DATE_FORMAT_FULL = "dd/MM/yyyy HH:mm:ss";
	String DATE_FORMAT_DATE = "dd/MM/yyyy";
	String DATE_FORMAT_SHORT = "dd/MM/yy";
	String DATE_FORMAT_HOUR = "hh:mm";
	String DATE_FORMAT_SQL = "yyyy-MM-dd";
	String DATE_FORMAT_SQL_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";

	String APPLICATION_JSON_UTF_8 = "application/json;charset=utf-8";
	String APPLICATION_XML_UTF_8 = "application/xml;charset=utf-8";

	String UTF_8 = "UTF-8";
	String ISO_8859_1 = "ISO-8859-1";
	String BLANK = "";
	String SPACE = " ";
	String SEMICOLON = ";";
	String DOT = ".";
	String COMMA = ",";
	String HIFEN = "-";
	String SLASH = "/";
	String BACKSLASH = "\\";
	
	String defaultPage = "0";
	String defaultSize = "5";

	String V1 = "v1/";
	String ROOT_URL = "/api/";
	
	String BOOKS = IConstants.ROOT_URL + IConstants.V1 + "books";
	String RATINGS= IConstants.ROOT_URL + IConstants.V1 + "ratings";
}
