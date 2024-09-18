import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class SMSgeneration {
    public static void main(String[] args) {
        try {

            String apiUrl = "https://textbelt.com/text";
            String phoneNumber = "+919354044806";
            String message = "Hello, this is a test message from jai.";
            String apiKey = "belt";

            // Create the HTTP client and POST request
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(apiUrl);

            // Add parameters to the request
            List<NameValuePair> data = Arrays.asList(
                    new BasicNameValuePair("phone", phoneNumber),
                    new BasicNameValuePair("message", message),
                    new BasicNameValuePair("key", apiKey)
            );
            httpPost.setEntity(new UrlEncodedFormEntity(data));


            HttpResponse httpResponse = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(httpResponse.getEntity());


            JSONObject response = new JSONObject(responseString);


            System.out.println("Response: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
