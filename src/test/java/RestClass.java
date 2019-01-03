import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import groovy.json.JsonException;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import javafx.scene.control.Tab;
import jdk.nashorn.internal.parser.JSONParser;
import netscape.javascript.JSException;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import io.restassured.path.json.JsonPath;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.contains;

import com.google.gson.Gson;

import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import sun.security.provider.ConfigFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class RestClass {
    public String xUserId = "5bd08cf2c81bcf27f3d471e6";
    public String tabId = "5bacca0f27000010003ef73e";
    public String orderId = "5b8900723e4683073f0dca3e";
    Response response;
    public static Properties config;

    public RestClass() throws IOException {
        config = new Properties();
        String appConfig = System.getProperty("appconfig") != null ? System.getProperty("appconfig") : "\\src\\test\\java\\Config.properties";
        FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + appConfig);
        config.load(ip);


    }

    @Before
    public void setBaseUri() {

        RestAssured.baseURI = "http://10.81.2.222";
        RestAssured.port = 8181;

    }

    @Test
    public void getTabNullAuth() {
        SessionFilter sessionFilter = new SessionFilter();

        given().log().all().when().header("X-User-Id", "").get("/api/traffic/v1/tab")
                .then().log().all().statusCode(403).extract().response();
        System.out.println("printing response......." + response.asString());
    }

    @Test
    public void getTabs123() {
        //    System.out.println(System.getProperty("user.dir"));
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();

        given().log().all().when().header("X-User-Id", xUserId).get("/api/traffic/v1/tab").then().log()
                .all().assertThat().body(matchesJsonSchemaInClasspath("Tab.json").using(jsonSchemaFactory));

        //extract().response();
        //  System.out.println("printing response...."+ response.asString());
    }

    @Test
    public void retrieveTabsUnAuthTest() {
        given().log().all().header("X-User-Id", "1234").when().get("/api/traffic/v1/tab")
                .then().log().all().statusCode(403).assertThat().body(equalTo("The supplied authentication is not authorized to access this resource"));
        //extract().response();
        //   System.out.println(response.asString());
    }

    @Test
    public void retrieveTabsUsingHasItems() {
        given().log().all().header("X-User-Id", xUserId).when().get("/api/traffic/v1/tab")
                .then().log().all().statusCode(200).assertThat().body("tabType", hasItems("Order", "MyOrders"));


    }

    @Test
    public void retrieveTabsUsingContains() {
        given().log().all().header("X-User-Id", xUserId).when().get("/api/traffic/v1/tab")
                .then().log().all().statusCode(200).assertThat().body("tabType", contains("Order"));

    }

    @Test
    public void exportUsingParam() {
        response = given().log().all().header("X-User-Id", xUserId).when().get("/api/traffic/v1/export/tab/" + tabId + "/csv")
                .then().log().all().statusCode(200).extract().response();
        response.asString();
    }

    @Test
    public void coreAPiTest() {
        response = given().log().all().param("id", "4ef31ce1766ec96769b399c0").when().get("ordering/delivery/terminate-destinations")
                .then().log().all().extract().response();
        response.asString();

    }



  /*  public void createTab(String name,boolean isPublic, boolean isDefault,String tabType,String xUserId)throws IOException {
            TabUtils body = new TabUtils();

            body.setDefault(isPublic);
            body.setName(name);
            body.setPublic(isPublic);
            body.setTabType(tabType);
       //   Response response=  createNewTab(xUserId, body);
       Response response = given().log().all().when().header("X-User-Id", xUserId).when().body(body).post()
                .then().log().all().extract().response();
        System.out.println(response.asString());
    }*/

    @Test
    public void getNewTabWithoutConfig() {
        TabUtils body = new TabUtils();
        body.setPublic(true);
        body.setName("test12");
        body.setDefault(true);
        body.setTabType("Order");

        given().log().all().when().header("X-User-Id", xUserId).contentType(ContentType.JSON).body(body).post("/api/traffic/v1/tab")
                .then().log().all().statusCode(200).assertThat()
                .body("name", equalTo("test12")).body("tabType", equalTo("Order")).statusLine("HTTP/1.1 200 OK");
        //.extract().response();
        //  response.asString();


    }


    @Test
    public void createTabFromConfig() {
        TabUtils body = new TabUtils();
        body.setName(config.getProperty("name"));
        body.setDefault(Boolean.parseBoolean(config.getProperty("isDefault")));
        body.setPublic(Boolean.parseBoolean(config.getProperty("isPublic")));
        body.setTabType(config.getProperty("tabType"));

        given().log().all().when().header("X-User-Id", xUserId).contentType(ContentType.JSON).body(body).
                post("/api/traffic/v1/tab")
                .then().log().all().statusCode(200).assertThat().
                body("name", equalTo("QATraffic01")).statusLine("HTTP/1.1 200 OK");

    }

    @Test
    public void getAllTabs() {
        String expectedId = "5bacca0f27000010003ef73e";
        Response response = given().log().all().when().header("X-User-Id", xUserId).get("/api/traffic/v1/tab")
                .then().log().all().statusCode(200).extract().response();
        System.out.println("printing ids here......." + response.path("_id"));
        String responseAsString = response.path("_id").toString();

        String parsedString = responseAsString.substring(1, responseAsString.length() - 1);

        String ids[] = parsedString.split(",\\s");
        System.out.println("printing array length......." + ids.length);
        System.out.println("printing expectedId......." + expectedId);

        Boolean var = Arrays.asList(ids).contains(expectedId);
        System.out.println("printing var....." + var);
        Assert.assertTrue(Arrays.asList(ids).contains(expectedId));

    }


    @Test
    public void getTabNames() {
        String expectedName = "OrderTab";
        Response response = given().log().all().when().header("X-User-Id", xUserId).get("/api/traffic/v1/tab")
                .then().log().all().statusCode(200).extract().response();

        String tabNamesAsString = response.path("name").toString();
        String parsedNames = tabNamesAsString.substring(1, tabNamesAsString.length() - 1);
        String names[] = parsedNames.split(",\\s");
        Boolean var = Arrays.asList(names).contains(expectedName);
        Assert.assertTrue(var);
    }

    @Test
    public void getTabType() {
        String expectedTabType = "Order";

        Response response = given().log().all().header("X-User-Id", xUserId).when().get("/api/traffic/v1/tab").
                then().log().all().extract().response();
        System.out.println(response.path("_id"));

        String tabTypeAsString = response.path("tabType").toString();
        String parsedTabTypes = tabTypeAsString.substring(1, tabTypeAsString.length() - 1);
        String tabTypes[] = parsedTabTypes.split(",\\s");
        Boolean var = Arrays.asList(tabTypes).contains(expectedTabType);
        Assert.assertTrue(var);

    }

    @Test
    public void validateResponse() {

        String expectedTabType = "Order";
        String expectedTabName = "OrderTab";


        Response response = given().log().all().when().header("X-User-Id", xUserId).when().get("/api/traffic/v1/tab").
                then().log().all().extract().response();


        String tabTypeAsString = response.path("tabType").toString();
        String parsedTabTypes = tabTypeAsString.substring(1, tabTypeAsString.length() - 1);
        String tabTypes[] = parsedTabTypes.split(",\\s");
        Boolean var = Arrays.asList(tabTypes).contains(expectedTabType);
        Assert.assertTrue(var);


        String tabNamesAsString = response.path("name").toString();
        String parsedNames = tabNamesAsString.substring(1, tabNamesAsString.length() - 1);
        String names[] = parsedNames.split(",\\s");
        for (int i = 0; i < names.length; i++) {
            System.out.println("printing tabNames.........." + names.length);
            System.out.println("printing tabNames.........." + names[i]);
        }
        Boolean var1 = Arrays.asList(names).contains(expectedTabName);
        Assert.assertTrue(var1);




   /*     for(int i=0; i< tabTypes.length; i++){

         String actaulTabType =    response.path("tabType["+i+"]");
    //     Arrays.asList(tabTypes)
         Assert.assertEquals(expectedTabType, actaulTabType);*/


    }

    /* ResponseTabHelper[] names=   response.jsonPath().getObject("name",ResponseTabHelper[].class);
     for(ResponseTabHelper name: names){
         System.out.println("tab names......"+ name);*/
    //   }
     /*       String tabNamesAsString =    response.path("name").toString();
            String parsedTabNames = tabNamesAsString.substring(1, tabNamesAsString.length()-1);
          String[] tabNames = parsedTabNames.split(",\\s");

          for(int i=0; i< tabNames.length; i++){
          Boolean tabNamesVal =         Arrays.asList(tabNames).contains(expectedTabName);
          Assert.assertTrue(tabNamesVal);
          }*/


//        String jsonAsString =     response.asString();
//
//        String actualTabName[]=  responseTabHelper.getName().substring(1,jsonAsString.length()-1).split(",\\s");
//        System.out.println("ptinting actual Tab names......."+ actualTabName.length);


     /*   Boolean var = Arrays.asList(actualTabName).contains(expectedTabName);
        Assert.assertTrue(var);
        String actualTabType = responseTabHelper.getTabType();
*/


    //  Gson gson = new Gson();
    //  ResponseTabHelper[] test= gson.fromJson(jsonAsString,ResponseTabHelper[].class );
    //  Assert.assertEquals(expectedTabName,actualTabName);
    //   System.out.println("trying......" + Arrays.asList(test).toString().substring(1,test.length-1).split(",\\s"));
    //    Boolean tabNameBol =  Arrays.asList(test).contains(expectedTabName);

    //    System.out.println("printing actual tab name......"+ tabNameBol);
    //    Assert.assertEquals(tabNameBol,actualTabName);
    //   Assert.assertTrue(tabNameBol);





    @Test
    public void testResponse() {
        String expectedTabType = "Order";
        String expectedTabName = "OrderTab";
        Response response = given().log().all().when().header("X-User-Id", xUserId).when().get("/api/traffic/v1/tab").
                then().log().all().extract().response();


        ArrayList<String> tabNames = response.path("name");
        ArrayList<String> tabTypes = response.path("tabType");
        Assert.assertTrue(tabNames.contains(expectedTabName));
        Assert.assertTrue(tabTypes.contains(expectedTabType));


    }

}






















