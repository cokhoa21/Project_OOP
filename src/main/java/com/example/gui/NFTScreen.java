package com.example.gui;

import com.example.datacollection.nftexchanges.BinanceDataCollection;
import com.example.model.BinanceItem;
import com.example.model.Tweet;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFTScreen extends ScrollPane {

    private ListView<BinanceItem> binanceItemsListView;
    private List<BinanceItem> currentData;
    private List<Tweet> currentTweets;

    public NFTScreen() {
        BorderPane mainPane = new BorderPane();
        VBox topBox = new VBox();
        Label label = new Label("Binance Data Collection");
        label.setStyle("-fx-font-size: 24px;");
        topBox.getChildren().add(label);

        // Tạo một HBox để chứa nút "Fetch Data", "Export Data", "Import Data", và "Sort by Price"
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);

        // Thêm nút "Fetch Data"
        Button fetchDataButton = new Button("Fetch Data");
        fetchDataButton.setOnAction(event -> fetchAndDisplayBinanceData());
        buttonBox.getChildren().add(fetchDataButton);

        // Thêm nút "Export Data"
        Button exportButton = new Button("Export Data");
        exportButton.setOnAction(event -> exportData());
        buttonBox.getChildren().add(exportButton);

        // Thêm nút "Import Data"
        Button importButton = new Button("Import Data");
        importButton.setOnAction(event -> importData());
        buttonBox.getChildren().add(importButton);

        // Thêm nút "Sort by Price"
        Button sortButton = new Button("Sort by Price (Descending)");
        sortButton.setOnAction(event -> sortDataByPriceDescending());
        buttonBox.getChildren().add(sortButton);

        // Thêm nút "Import Tweet Data"
        Button importTweetDataButton = new Button("Import Tweet Data");
        importTweetDataButton.setOnAction(event -> importTweetData());
        buttonBox.getChildren().add(importTweetDataButton);

        // Thêm nút "Show Hashtag Chart"
        Button showHashtagChartButton = new Button("Show Hashtag Chart");
        showHashtagChartButton.setOnAction(event -> showHashtagChart());
        buttonBox.getChildren().add(showHashtagChartButton);

        // Thêm HBox chứa nút vào topBox
        topBox.getChildren().add(buttonBox);

        // Thêm khoảng cách giữa nút và phần còn lại của giao diện
        topBox.setMargin(buttonBox, new Insets(10, 0, 0, 0));

        // Thêm topBox vào mainPane ở vị trí đỉnh (TOP)
        mainPane.setTop(topBox);

        // Khởi tạo currentData
        currentData = new ArrayList<>();

        // Thêm mainPane vào ScrollPane
        setContent(mainPane);
    }

    // Phương thức xử lý sự kiện nút Import Tweet Data
    private void importTweetData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Tweet Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            List<Tweet> importedTweets = readTweetsFromJson(selectedFile.getAbsolutePath());

            // Tính toán số lượng hashtag
            Map<String, Integer> hashtagCounts = calculateHashtagCounts(importedTweets, currentData);

            // Hiển thị biểu đồ
            showHashtagChart(currentData, hashtagCounts);
        }
    }

    // Phương thức tính toán số lượng hashtag
    private Map<String, Integer> calculateHashtagCounts(List<Tweet> tweets, List<BinanceItem> binanceItems) {
        Map<String, Integer> hashtagCounts = new HashMap<>();

        for (Tweet tweet : tweets) {
            if (tweet != null && tweet.getHashtags() != null) {
                for (String hashtag : tweet.getHashtags()) {
                    // Bỏ ký tự '#' ở đầu hashtag
                    String cleanedHashtag = hashtag.replaceFirst("^#", "");

                    // So sánh với name hoặc acronym của từng item binance
                    for (BinanceItem binanceItem : binanceItems) {
                        if (binanceItem != null) {
                            String cleanedName = binanceItem.getName().replaceAll("\\s+", "").toLowerCase();
                            String cleanedAcronym = binanceItem.getAcronym().replaceAll("\\s+", "").toLowerCase();

                            // Nếu hashtag tương ứng với name hoặc acronym, tăng số lượng đếm
                            if (cleanedHashtag.equalsIgnoreCase(cleanedName) || cleanedHashtag.equalsIgnoreCase(cleanedAcronym)) {
                                hashtagCounts.put(binanceItem.getName(), hashtagCounts.getOrDefault(binanceItem.getName(), 0) + 1);
                                break; // Dừng vòng lặp khi đã tìm thấy match
                            }
                        }
                    }
                }
            }
        }

        return hashtagCounts;
    }

    // Phương thức đọc tweets từ file JSON
    private List<Tweet> readTweetsFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Đọc nội dung JSON từ file
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));

            // Chuyển đổi JSON thành danh sách tweets
            return objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, Tweet.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showHashtagChart(List<BinanceItem> binanceItems, Map<String, Integer> hashtagCounts) {
        // Tạo đối tượng BarChart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Hashtag Counts");

        // Thêm dữ liệu vào BarChart
        for (BinanceItem item : binanceItems) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(item.getName()); // Đặt tên của cột là tên của BinanceItem

            // Thêm dữ liệu vào Series
            series.getData().add(new XYChart.Data<>(item.getName(), hashtagCounts.getOrDefault(item.getName(), 0)));

            // Thêm Series vào BarChart
            barChart.getData().add(series);
        }

        // Tạo đối tượng Scene và Stage để hiển thị biểu đồ
        Scene scene = new Scene(barChart, 800, 600);
        Stage stage = new Stage();
        stage.setTitle("Hashtag Chart");
        stage.setScene(scene);

        // Hiển thị Stage
        stage.show();
    }

    private void showHashtagChart() {
        if (currentTweets != null) {
            Map<String, Integer> hashtagCounts = calculateHashtagCounts(currentTweets, currentData);

            // Hiển thị biểu đồ
            showHashtagChart(currentData, hashtagCounts);
        }
    }

    private void fetchAndDisplayBinanceData() {
        // Kiểm tra xem đã có dữ liệu hay chưa
        if (currentData.isEmpty()) {
            BinanceDataCollection dataCollector = new BinanceDataCollection();
            List<BinanceItem> binanceData = dataCollector.collectData();

            // Lưu dữ liệu vào currentData
            currentData.addAll(binanceData);

            // Hiển thị dữ liệu trên giao diện người dùng
            Platform.runLater(() -> displayDataOnUI(binanceData));
        }
    }

    private void displayDataOnUI(List<BinanceItem> data) {
        BorderPane mainPane = (BorderPane) getContent();

        VBox centerBox = new VBox();

        for (BinanceItem item : data) {
            // Tạo một HBox để chứa thông tin của mỗi Item
            HBox itemBox = new HBox();
            itemBox.setSpacing(10);
            itemBox.setPadding(new Insets(10, 0, 10, 10)); // Căn lề trái 10px

            // Hiển thị thông tin chi tiết
            VBox detailsBox = new VBox();
            detailsBox.getChildren().add(new Label("Name: " + item.getName()));
            detailsBox.getChildren().add(new Label("Acronym: " + item.getAcronym()));
            detailsBox.getChildren().add(new Label("Price: " + item.getPrice()));
            detailsBox.getChildren().add(new Label("Volume: " + item.getVolume()));

            itemBox.getChildren().add(detailsBox);

            centerBox.getChildren().add(itemBox);
        }

        // Thêm centerBox vào mainPane ở vị trí giữa (CENTER)
        mainPane.setCenter(centerBox);
    }

    private void exportData() {
        if (!currentData.isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile != null) {
                // Sử dụng dữ liệu đã lưu trữ trong currentData
                saveBinanceDataToJson(currentData, selectedFile.getAbsolutePath());
            }
        }
    }

    private void saveBinanceDataToJson(List<BinanceItem> data, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Chuyển danh sách Item thành định dạng JSON
            String jsonString = objectMapper.writeValueAsString(data);

            // Ghi nội dung JSON vào file
            Files.write(Paths.get(filePath), jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Đọc dữ liệu từ file JSON và hiển thị lên giao diện
            List<BinanceItem> importedData = readBinanceDataFromJson(selectedFile.getAbsolutePath());

            // Lưu dữ liệu vào currentData
            currentData.clear();
            currentData.addAll(importedData);

            // Hiển thị dữ liệu trên giao diện người dùng
            displayDataOnUI(importedData);
        }
    }

    private List<BinanceItem> readBinanceDataFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Đọc nội dung JSON từ file
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));

            // Chuyển đổi JSON thành danh sách Item
            return objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, BinanceItem.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void sortDataByPriceDescending() {
        if (!currentData.isEmpty()) {
            // Sắp xếp danh sách theo giá giảm dần
            currentData.sort((item1, item2) -> {
                double price1 = parsePrice(item1.getPrice());
                double price2 = parsePrice(item2.getPrice());
                // Sắp xếp giảm dần
                return Double.compare(price2, price1);
            });

            // Hiển thị dữ liệu đã sắp xếp trên giao diện người dùng
            displayDataOnUI(currentData);
        }
    }

    private double parsePrice(String price) {
        // Lọc chỉ lấy số từ chuỗi giá
        String numericPart = price.replaceAll("[^0-9.]", "");
        try {
            // Chuyển đổi thành số
            return Double.parseDouble(numericPart);
        } catch (NumberFormatException e) {
            // Xử lý nếu không thể chuyển đổi
            e.printStackTrace();
            return 0.0;
        }
    }
}
