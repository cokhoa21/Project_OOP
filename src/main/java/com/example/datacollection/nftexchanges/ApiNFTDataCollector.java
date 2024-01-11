package com.example.datacollection.nftexchanges;

import com.example.datacollection.DataCollector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiNFTDataCollector implements DataCollector {
    private static final String API_ENDPOINT = "https://api.niftygateway.com/v1/me";
    private static final String ACCESS_TOKEN = "CyeCQTgGeOt9F7ZRjYRV18dC9kiByl"; 

    @Override
    public List<String> collectData() {
        List<String> niftyGatewayData = new ArrayList<>();
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .build();

        try {
            Response response = httpClient.newCall(request).execute();

            // Kiểm tra nếu yêu cầu thành công (HTTP status code 200)
            if (response.isSuccessful()) {
                // Lấy dữ liệu từ phản hồi
                String responseData = response.body().string();
                niftyGatewayData.add(responseData);
                System.out.println(responseData);
            } else {
                System.err.println("Failed to fetch data. HTTP Error Code: " + response.code());
                System.err.println("Error response: " + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return niftyGatewayData;
    }
}
