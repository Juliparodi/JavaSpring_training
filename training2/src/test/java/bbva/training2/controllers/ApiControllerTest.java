package bbva.training2.controllers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Slf4j
public class ApiControllerTest {


    //Junit will start/stop Wiremock automatically using port 8080.
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);

    @Test
    public void whenCallingOpenApiByIsbn_ThenReturnBook() throws IOException, JSONException {
        //Stubbing part
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/v1/books/0765304368"))
                .willReturn(aResponse()
                        .withBodyFile("OpenApiResponse.json")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        //Configuring http request and response using OkHttpClient (took it from Postman code)
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Builder()
                .url("http://localhost:8080/v1/books/0765304368")
                .method("GET", null)
                //   .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        //converting RequestBody to String
        String book = response.body().string();
        JSONObject Jobject = new JSONObject(book);
        String title = Jobject.getString("title");

        //assert and verify
        assertTrue(response.isSuccessful());
        assertFalse(title.isEmpty());
        assertThat(title).isEqualTo("Down and out in the Magic Kingdom");
        verify(getRequestedFor(urlEqualTo("/v1/books/0765304368")));
        verify(getRequestedFor(urlEqualTo("/v1/books/0765304368"))
                .withHeader("Content-Type", equalTo("application/json")));

        //loggin responses (just to check responses)
        log.info("----- STATUS CODE: {}", response.code());
        log.info("----- HEADERS: {}", request.headers().toString());
        log.info("------ LALA:   {}", response.body().string());
        log.info("------ title {}", title);
    }
}

//uploading json to use for POST
        /*
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("filea", "OpenApiResponse.json",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("src/test/resources/__files/OpenApiResponse.json")))
                .build();

         */
