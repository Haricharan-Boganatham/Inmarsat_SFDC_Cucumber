package com.inmarsat.sfdc.framework;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.testng.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.cucumber.listener.Reporter;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class APIReusuableLibrary {

	public enum SERVICEMETHOD {
		GET, POST, PUT, DELETE;
	}

	public enum SERVICEFORMAT {
		JSON, XML, TEXT, FILE;
	}

	public enum ASSERT_RESPONSE {
		BODY, TAG, HEADER, LIST;

	}

	public enum COMPARISON {
		IS_EXISTS, IS_EQUALS, IS_CONTAINS;
	}

	/**
	 * Function to read input file from given Path
	 * 
	 * @param inputFilePath
	 *            The Path of the given File
	 * @return The String of the input File
	 * 
	 */
	public String readInput(String inputFilePath) {
		String intputFileContent = "";
		BufferedReader bufferReader = null;
		try {
			String line;
			File inputFile = new File(inputFilePath);
			FileReader fileReader = new FileReader(inputFile);
			bufferReader = new BufferedReader(fileReader);
			while ((line = bufferReader.readLine()) != null) {
				intputFileContent += line.trim();
			}
		} catch (FileNotFoundException x) {

		} catch (IOException ex) {

		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return intputFileContent;

	}

	/**
	 * Function to get value from given XML Tag
	 * 
	 * @param response
	 *            The Response of the API
	 * @param tagToValidate
	 *            The XML Tag/Node from which value has to be fetched
	 * @return The String value
	 * 
	 */
	public String extractValue(ValidatableResponse response, String tagToValidate) {

		if (!tagToValidate.contains("//")) {
			return jsonParser(response, tagToValidate);
		} else {
			return xmlParser(response, tagToValidate);
		}
	}

	/**
	 * Function to get the response of an API
	 * 
	 * @param url
	 *            The URL of the Application
	 * @param methodType
	 *            The Service Method GET,POST, PUT ,DELETE {@link SERVICEMETHOD}
	 * @param headersMap
	 *            The headers passed as Map object , refer to
	 *            {@link HeadersForAPI} to create custom Headers
	 * @param statusCode
	 *            The Expected Status Code
	 * @return The Response {@link ValidatableResponse}
	 * 
	 */
	public ValidatableResponse sendNReceive(String url, SERVICEMETHOD methodType, Map<String, String> headersMap,
			int statusCode) throws Exception {
		ValidatableResponse response = null;
		try {
			switch (methodType) {
			case GET:

				if (headersMap != null) {
					response = RestAssured.given().relaxedHTTPSValidation().headers(headersMap).get(url).then()
							.assertThat().statusCode((Integer) statusCode);
					reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
				} else {
					response = RestAssured.given().relaxedHTTPSValidation().get(url).then().assertThat()
							.statusCode((Integer) statusCode);
					reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
				}

				break;

			case DELETE:
				if (headersMap != null) {
					response = RestAssured.given().relaxedHTTPSValidation().headers(headersMap).delete(url).then()
							.assertThat().statusCode((Integer) statusCode);
					reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
				} else {
					response = RestAssured.given().relaxedHTTPSValidation().headers(headersMap).delete(url).then()
							.assertThat().statusCode((Integer) statusCode);
					reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
				}
				break;

			default:
				break;
			}
		}

		catch (AssertionError x) {
			reportItFAIL(url, Integer.toString(statusCode), x.getMessage(), "FAIL");
			Assert.fail(x.getMessage());
		} catch (Exception ex) {
			reportItFAIL(url, Integer.toString(statusCode), ex.getMessage(), "FAIL");
			Assert.fail(ex.getMessage());
		}
		return response;
	}

	/**
	 * Function to get the response of an API
	 * 
	 * @param url
	 *            The URL of the Application
	 * @param methodType
	 *            The Service Method GET,POST, PUT ,DELETE {@link SERVICEMETHOD}
	 * @param postBodyType
	 *            The Format of Post Body {@link SERVICEFORMAT}
	 * @param postBodyContent
	 *            The Post Body Content
	 * @param headersMap
	 *            The headers passed as Map object , refer to
	 *            {@link HeadersForAPI} to create custom Headers
	 * @param statusCode
	 *            The Expected Status Code
	 * @return The Response {@link ValidatableResponse}
	 * 
	 */
	public ValidatableResponse sendNReceive(String url, SERVICEMETHOD methodType, SERVICEFORMAT postBodyType,
			String postBodyContent, Map<String, String> headersMap, int statusCode) throws Exception {
		ValidatableResponse response = null;
		try {
			Object postBody = getPostBodyContent(postBodyContent, postBodyType);
			ContentType contentType = getPostContentType(postBodyType);
			switch (methodType) {
			case POST:

				if (postBody instanceof File) {/* File */
					if (headersMap != null) {
						response = RestAssured.given().contentType(contentType).relaxedHTTPSValidation()
								.body((File) postBody).headers(headersMap).post(url).then().assertThat()
								.statusCode((Integer) statusCode);
						reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
					} else {
						response = RestAssured.given().contentType(contentType).relaxedHTTPSValidation()
								.body((File) postBody).post(url).then().assertThat().statusCode((Integer) statusCode);
						reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
					}
				} else if (postBody instanceof String) {/* String */
					if (headersMap != null) {
						response = RestAssured.given().contentType(contentType).relaxedHTTPSValidation().body(postBody)
								.headers(headersMap).post(url).then().assertThat().statusCode((Integer) statusCode);
						reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
					} else {
						response = RestAssured.given().contentType(contentType).relaxedHTTPSValidation().body(postBody)
								.post(url).then().assertThat().statusCode((Integer) statusCode);
						reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
					}
				}
				break;

			case PUT:

				if (postBody instanceof File) {/* File */
					if (headersMap != null) {
						response = RestAssured.given().contentType(contentType).relaxedHTTPSValidation()
								.body((File) postBody).headers(headersMap).put(url).then().assertThat()
								.statusCode((Integer) statusCode);
						reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
					} else {
						response = RestAssured.given().contentType(contentType).relaxedHTTPSValidation()
								.body((File) postBody).put(url).then().assertThat().statusCode((Integer) statusCode);
						reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
					}
				} else if (postBody instanceof String) {/* String */
					if (headersMap != null) {
						response = RestAssured.given().contentType(contentType).relaxedHTTPSValidation().body(postBody)
								.headers(headersMap).put(url).then().assertThat().statusCode((Integer) statusCode);
						reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
					} else {
						response = RestAssured.given().contentType(contentType).relaxedHTTPSValidation().body(postBody)
								.put(url).then().assertThat().statusCode((Integer) statusCode);
						reportItPASS(url, Integer.toString(statusCode), Integer.toString(statusCode), "PASS");
					}
				}
				break;

			default:
				break;
			}
		}

		catch (AssertionError x) {
			reportItFAIL(url, Integer.toString(statusCode), x.getMessage(), "FAIL");
			Assert.fail(x.getMessage());
		} catch (Exception ex) {
			reportItFAIL(url, Integer.toString(statusCode), ex.getMessage(), "FAIL");
			Assert.fail(ex.getMessage());
		}
		return response;
	}

	/**
	 * Function to get updated String value
	 * 
	 * @param inputString
	 *            The input String
	 * @param paramVar
	 *            The Variable
	 * @param paramValue
	 *            The Variable Value to be updated
	 * @return The updated String
	 * 
	 */
	public String updateContent(String inputString, String paramVar, String paramValue) {
		String updatedString = null;
		try {
			updatedString = inputString.replaceAll("%%" + paramVar + "%%", paramValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedString;
	}

	/**
	 * Function to get the response of an API
	 * 
	 * @param url
	 *            The URL of the Application
	 * @param assertResponse
	 *            The Type of Assertion required for Response
	 *            {@link ASSERT_RESPONSE}
	 * @param tagToValidate
	 *            The Tag to be validated
	 * @param expectedResponse
	 *            The Expected Response
	 * @param comparison
	 *            The Type of Comparison to be done {@link COMPARISON }
	 * 
	 */
	public void assertIt(String uri, ValidatableResponse response, ASSERT_RESPONSE assertResponse, String tagToValidate,
			Object expectedResponse, COMPARISON comparison) throws Exception {
		Object actualResponse = null;
		switch (assertResponse) {

		case BODY:
			actualResponse = response.extract().body().asString();
			if (compareIt(comparison, actualResponse, expectedResponse)) {
				reportItPASS(uri, expectedResponse, actualResponse, "PASS");
				Assert.assertTrue(true);
			} else {
				reportItFAIL(uri, expectedResponse, actualResponse, "FAIL");
				Assert.fail();
			}
			break;

		case TAG:
			if (!tagToValidate.contains("//"))
				actualResponse = jsonParser(response, tagToValidate);
			else
				actualResponse = xmlParser(response, tagToValidate);
			if (compareIt(comparison, actualResponse, expectedResponse)) {
				reportItPASS(uri, expectedResponse, actualResponse, "PASS");
				Assert.assertTrue(true);
			} else {
				reportItFAIL(uri, expectedResponse, actualResponse, "FAIL");
				Assert.fail();
			}
			break;

		case HEADER:
			actualResponse = response.extract().contentType();
			if (compareIt(comparison, actualResponse, expectedResponse)) {
				reportItPASS(uri, expectedResponse, actualResponse, "PASS");
				Assert.assertTrue(true);
			} else {
				reportItFAIL(uri, expectedResponse, actualResponse, "FAIL");
				Assert.fail();
			}
			break;

		case LIST:
			if (!tagToValidate.contains("//"))
				actualResponse = jsonParserAsList(response, tagToValidate);
			else
				actualResponse = xmlParserAsList(response, tagToValidate);
			if (compareIt(comparison, actualResponse, expectedResponse)) {
				reportItPASS(uri, expectedResponse, actualResponse, "PASS");
				Assert.assertTrue(true);
			} else {
				reportItFAIL(uri, expectedResponse, actualResponse, "FAIL");
				Assert.fail();
			}
			break;

		default:
			break;

		}
	}

	/**
	 * Function to estabilish Connection to access DB
	 * 
	 * 
	 */
	public String establishConnectionNExecute(String dbConnection, String dbUname, String dbPassword, String sqlQuery) {
		String queryOutput = null;
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(dbConnection, dbUname, dbPassword);
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				queryOutput = rs.getString(1);
			}
		} catch (SQLException sq) {

		} catch (ClassNotFoundException cs) {

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryOutput;

	}

	/**
	 * Function to estabilish Connection to access DB
	 * 
	 * 
	 */
	public List<String> establishConnectionNExecuteNGetList(String dbConnection, String dbUname, String dbPassword,
			String sqlQuery) {
		List<String> queryOutput = new ArrayList<String>();
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(dbConnection, dbUname, dbPassword);
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			ResultSetMetaData rsmt = rs.getMetaData();
			int columnCount = rsmt.getColumnCount();
			List<List<String>> rowList = new LinkedList<List<String>>();
			while (rs.next()) {
				rowList.add(queryOutput);
				for (int column = 1; column <= columnCount; column++) {
					final Object value = rs.getObject(column);
					String valueAsString = String.valueOf(value);
					if (!valueAsString.contains("."))
						valueAsString = valueAsString + ".0";
					queryOutput.add(valueAsString.toUpperCase());
				}
			}
		} catch (SQLException sq) {
			sq.printStackTrace();
		} catch (ClassNotFoundException cf) {
			cf.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryOutput;
	}

	private Object getPostBodyContent(String postBodyContent, SERVICEFORMAT postBodyType) {

		if (postBodyType.equals(SERVICEFORMAT.FILE)) {
			File file = new File(postBodyContent);
			return file;
		} else {
			return postBodyContent;
		}
	}

	private ContentType getPostContentType(SERVICEFORMAT contentTypes) {
		ContentType contentType = null;
		switch (contentTypes) {
		case FILE:
			contentType = ContentType.BINARY;
			break;
		case JSON:
			contentType = ContentType.JSON;
			break;
		case XML:
			contentType = ContentType.XML;
			break;
		case TEXT:
			contentType = ContentType.TEXT;
			break;
		default:
			break;
		}
		return contentType;
	}

	private String xmlParser(ValidatableResponse response, String tagToValidate) {

		return response.extract().xmlPath().getString(tagToValidate);
	}

	private String jsonParser(ValidatableResponse response, String tagToValidate) {

		return response.extract().jsonPath().getString(tagToValidate);
	}

	private List<String> xmlParserAsList(ValidatableResponse response, String tagToValidate) {

		return response.extract().xmlPath().getList(tagToValidate);
	}

	private Object jsonParserAsList(ValidatableResponse response, String tagToValidate) {

		return response.extract().jsonPath().getList(tagToValidate);
	}

	private Boolean compareIt(COMPARISON comparator, Object actualResponse, Object expectedResponse) {
		switch (comparator) {
		case IS_EQUALS:
			if ((actualResponse).equals(expectedResponse))
				return true;
			else
				return false;

		case IS_CONTAINS:
			if (((String) actualResponse).contains((String) expectedResponse))
				return true;
			else
				return false;

		case IS_EXISTS:
			if (((String) actualResponse).contains((String) expectedResponse))
				return true;
			else
				return false;
		default:
			break;
		}

		return null;
	}

	public String xmlParserByTag(ValidatableResponse response, String tagToValidate) throws SAXException {

		String elementValue = null;
		String inputXml = response.extract().asString();
		DocumentBuilder builder;
		Document doc;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(false);
			InputStream in = new ByteArrayInputStream(inputXml.getBytes("utf-8"));
			builder = factory.newDocumentBuilder();
			doc = builder.parse(in);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(tagToValidate);
			elementValue = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (Exception e) {
			
		} finally {
		}
		return elementValue;

	}

	private void reportItPASS(String uri, Object expectedValue, Object actualValue, String status) {
		expectedValue = ((Object) expectedValue).toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		actualValue = ((Object) actualValue).toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		Reporter.addStepLog(
				"<style>table {border-collapse !important  }td {border: 1px solid black !important;  white-space: normal; !important;}</style>"
						+ "<table><tr><td><b>ENDPOINT</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>"
						+ uri + "</td></tr>" + "<tr><td><b>EXPECTED RESULT</b></td><td>" + expectedValue + "</td></tr>"
						+ "<tr><td><b>ACTUAL RESULT</b>&nbsp;&nbsp;&nbsp;&nbsp;</td><td>" + actualValue + "</td></tr>"
						+ "<tr><td><b>STATUS</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><font color=green>"
						+ status + "</font></td></tr></table>");
	}

	private void reportItFAIL(String uri, Object expectedValue, Object actualValue, String status) {
		expectedValue = ((Object) expectedValue).toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		actualValue = ((Object) actualValue).toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		Reporter.addStepLog(
				"<style>table {border-collapse !important  }td {border: 1px solid black !important;  white-space: normal; !important;}</style>"
						+ "<table><tr><td><b>ENDPOINT</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>"
						+ uri + "</td></tr>" + "<tr><td><b>EXPECTED RESULT</b></td><td>" + expectedValue + "</td></tr>"
						+ "<tr><td><b>ACTUAL RESULT</b>&nbsp;&nbsp;&nbsp;&nbsp;</td><td>" + actualValue + "</td></tr>"
						+ "<tr><td><b>STATUS</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><font color=red>"
						+ status + "</font></td></tr></table>");
	}

}