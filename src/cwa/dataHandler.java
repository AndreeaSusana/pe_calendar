/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;


/**
 *
 * @author cklose
 */
public class dataHandler {
    static String prefix = "http://cwa.crdlux.de/";
    static String POST = "POST";
    static String GET = "GET";
    
    private static JSONArray HandleRequest(String callURL, String method) throws Exception{
        String url = prefix + callURL;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod(method);
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
        	response.append(inputLine);
        }
        in.close();
        
        //System.out.println("response: " + response.toString());
        
        //Read JSON response and print
        JSONParser parser = new JSONParser();
        JSONArray json = (JSONArray) parser.parse(response.toString());
        
        return json;
    }
    
    private static JSONArray HandlePOST_Request(String callURL, HashMap<String, String> postDataParams) throws Exception {
        String url = prefix + callURL;
        String response = "";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setReadTimeout(10000);
        con.setConnectTimeout(15000);
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(postDataParams));

        writer.flush();
        writer.close();
        os.close();
        int responseCode=con.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        }
        else {
            response="";
        }
        
        System.out.println("response: " + response.toString());
        try{
            //Read JSON response and print
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(response.toString());
            return json;
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
        
        return null;
    }
    
      private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    
    public static void getUsers() throws Exception {
        String callURL = "info.php?users";
	JSONArray output = HandleRequest(callURL, GET);
        
        System.out.println(output.get(0).toString());
        
    }
    
    public static void login(String name,String password) throws Exception {
        String callURL = "login.php?login=1";
        
        HashMap<String, String> hmap = new HashMap<String, String>();
        hmap.put("name", name);
        hmap.put("passwort", password);

        
        JSONArray output = HandlePOST_Request(callURL, hmap);
        
        System.out.println(output.get(0).toString());
    }
}

