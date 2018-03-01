package selenium;

import com.github.javafaker.Faker;
import infra.Locator;
import infra.SeleniumWebDriver;
import jdk.nashorn.internal.runtime.URIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;


/**
 * Created by shay.toledano on 10/22/15.
 */
public class zopaAutomationPage {

    private SeleniumWebDriver chromedriver;
    public String postcode = "";
    private static final Faker faker = new Faker();
    private static final Logger LOGGER = LogManager.getLogger(URIUtils.class);

    @Parameters({ "browser" })
    @BeforeClass
    public void startWebDriver() throws InterruptedException, IOException, AWTException {
        chromedriver = new SeleniumWebDriver();
        checkURL(Locator.homePageUrl);
    }

    @BeforeMethod
    public void navigate() {
            chromedriver.navigate(Locator.homePageUrl);
    }

    @AfterClass
    public void tearDown() {
        chromedriver.close();
    }

    public void checkURL(String NewUrl) throws IOException {
        URL url1 = new URL(NewUrl);
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
        conn.setRequestMethod("GET");
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println(String.format("Site is up, content length = %s", conn.getHeaderField("content-length")));
            } else {
                System.out.println(String.format("Site is up, but returns non-ok status = %d", responseCode));
            }
        } catch (java.net.UnknownHostException e) {
            System.out.println("Site is down");
            System.exit(0);
        }
    }

    @Test
    public void test1() {

        chromedriver.click(By.xpath(Locator.GET_ZOPA_LOAN));

        chromedriver.waitinsecons("5");
        chromedriver.scrollDown();

        chromedriver.click(By.xpath(Locator.SUBMIT_LOAN_BUTTON));
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress(firstName + "." + lastName);
        String phonenum = faker.phoneNumber().cellPhone();

        chromedriver.sendkey(By.xpath(Locator.EMAIL_ADDRESS), email);
        chromedriver.sendkey(By.xpath(Locator.FIRST_NAME), firstName);
        chromedriver.sendkey(By.xpath(Locator.LAST_NAME) , lastName);
        chromedriver.sendkey(By.xpath(Locator.PHONENUMBER), phonenum);


        int[] data = new int[3];
        createBirthday(data);
        chromedriver.sendkey(By.xpath(Locator.DD) , String.valueOf(data[0]));
        chromedriver.sendkey(By.xpath(Locator.MM) ,String.valueOf(data[1]));
        chromedriver.sendkey(By.xpath(Locator.YYYY), String.valueOf(data[2]));
        String bitrhday = data[0]+ "/"+data[1]+"/"+data[2];

        chromedriver.click(By.xpath(Locator.SHOW_MORE));

        chromedriver.waitinsecons("3");

        try {
            GetPostcode();
            chromedriver.sendkey(By.xpath(Locator.POSTCODE) , postcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        chromedriver.click(By.xpath(Locator.LOOKUPADDRESS));
        WritetoLogger(firstName,lastName, email, phonenum , bitrhday);

    }


    private String GetPostcode() throws Exception {

        String url = "http://api.postcodes.io/random/postcodes";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject myResponse = new JSONObject(response.toString());
        JSONObject form_data = myResponse.getJSONObject("result");
        postcode = form_data.getString("postcode");

        return postcode;

    }

    public void WritetoLogger(String firstname , String lastname , String email, String phonenumber , String bitrhday) {

        LOGGER.info("---------------" + "parameters Result for : " + LocalDateTime.now() + "-------------");
        LOGGER.info("this is the firstName: " + firstname);
        LOGGER.info("this is the lastName: " + lastname);
        LOGGER.info("this is the email: " + email);
        LOGGER.info("this is the phonenumber: " + phonenumber);
        LOGGER.info("this is the birthday: " + bitrhday);
        LOGGER.info("this is the postcode: " + postcode);
    }

    public static void createBirthday(int[] values)
    {
        int month, year, day;
        Random call = new Random();
        month = 1 + call.nextInt(12);
        year = 1969 + call.nextInt(2017-1969+1);
        day  = 1 + call.nextInt(31);

        if (month==2 && day>28){
            day = day - 3;
        } else {
            if((month%2==0 && month != 8 ) && day==31 ){
                day = day -1;
            }
        }
        values[0] = day;
        values[1] = month;
        values[2] = year;
    }

}
