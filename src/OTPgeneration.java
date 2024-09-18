import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Arrays;

public class OTPgeneration {
    public static void main(String[] args) {
        try {

            String phoneNumber = "+919354044806";
            String userId = "jaianmol84@gmail.com";
            String apiKey = "belt";


            NameValuePair[] data = {
                    new BasicNameValuePair("phone", phoneNumber),
                    new BasicNameValuePair("userid", userId),
                    new BasicNameValuePair("key", apiKey)
            };


            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://textbelt.com/otp/generate");
            httpPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(data)));


            HttpResponse httpResponse = httpClient.execute(httpPost);


            String responseString = EntityUtils.toString(httpResponse.getEntity());
            JSONObject response = new JSONObject(responseString);


            System.out.println("Response: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
