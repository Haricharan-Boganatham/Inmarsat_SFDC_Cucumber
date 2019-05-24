package com.inmarsat.sfdc.framework;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class RestApiForJira {

	private static Properties properties = Settings.getInstance();
	public static final String JIRA_URL = properties.getProperty("Jira_Url");
	public static final String JIRA_USERNAME = properties.getProperty("Jira_UserName");
	public static final String JIRA_PASSWORD = properties.getProperty("Jira_Password");
	public static final String JIRA_PROJECT_KEY = properties.getProperty("Jira_Project_Key");
	public static final String JIRA_ISSUE_TYPE = properties.getProperty("Jira_Issue_Type");

	/**
	 * Function to create Defect/bug in JIRA
	 * 
	 * @param summary
	 *            The summary of the Defect/Bug
	 * @param description
	 *            The Description of Defect/Bug
	 */
	public static void creatLog(String summary, String description) {
		try {
			certifiacteHandle();
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter(JIRA_USERNAME, JIRA_PASSWORD));
			WebResource webResource = client.resource(JIRA_URL + "/rest/api/2/issue");
			String input = "{\"fields\":{\"project\":{\"key\":\"" + JIRA_PROJECT_KEY + "\"},\"summary\":\"" + summary
					+ "\",\"description\":\"" + description + "\",\"issuetype\":{\"name\":\"" + JIRA_ISSUE_TYPE
					+ "\"}}}";
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
			response.getEntity(String.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to create Defect/bug in JIRA along with screenshot of the
	 * Application
	 * 
	 * @param summary
	 *            The summary of the Defect/Bug
	 * @param description
	 *            The Description of Defect/Bug
	 * @param screenShotPath
	 *            The screenshot of Defect/Bug
	 */

	public static void createLog(String summary, String description, String screenShotPath) {
		try {
			certifiacteHandle();
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter(JIRA_USERNAME, JIRA_PASSWORD));
			WebResource webResource = client.resource(JIRA_URL + "/rest/api/2/issue");
			String input = "{\"fields\":{\"project\":{\"key\":\"" + JIRA_PROJECT_KEY + "\"},\"summary\":\"" + summary
					+ "\",\"description\":\"" + description + "\",\"issuetype\":{\"name\":\"" + JIRA_ISSUE_TYPE
					+ "\"}}}";
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
			String output = response.getEntity(String.class);
			String[] parts1 = output.split(":");
			String issueKey = parts1[2];
			String[] parts2 = issueKey.split(",");
			issueKey = parts2[0];
			issueKey = issueKey.replace("\"", "");

			// For Add attachment to Jira issue
			try {
				addAttachmentToIssue(issueKey, screenShotPath);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public static boolean addAttachmentToIssue(String issueKey, String screenShotPath) throws IOException {
		certifiacteHandle();
		String jira_attachment_authentication = new String(
				org.apache.commons.codec.binary.Base64.encodeBase64((JIRA_USERNAME + ":" + JIRA_PASSWORD).getBytes()));

		CloseableHttpClient httpclient = HttpClients.createSystem();
		HttpPost httppost = new HttpPost(JIRA_URL + "/rest/api/2/issue/" + issueKey + "/attachments");
		httppost.setHeader("X-Atlassian-Token", "no-check");
		httppost.setHeader("Authorization", "Basic " + jira_attachment_authentication);
		File fileToUpload = new File(screenShotPath);
		FileBody fileBody = new FileBody(fileToUpload);
		HttpEntity entity = MultipartEntityBuilder.create().addPart("file", fileBody).build();
		httppost.setEntity(entity);
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httppost);
		} finally {
			httpclient.close();
		}
		if (response.getStatusLine().getStatusCode() == 200) {
			return true;
		} else {
			return false;
		}

	}

	private static void certifiacteHandle() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			;
		}
	}

}
