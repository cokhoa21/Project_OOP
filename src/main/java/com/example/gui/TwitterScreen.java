package com.example.gui;

import com.example.datacollection.twitter.TwitterDataCollector;
//import com.example.datacollection.twitter.Tweet;
import com.example.model.Tweet;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TwitterScreen extends VBox {

    private ListView<Tweet> tweetsListView;
    private TextField keywordField;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    public TwitterScreen() {
        // Label cho tiêu đề màn hình
        Label label = new Label("Twitter Data Collector");
        label.setFont(new Font(24)); // Đặt kích thước chữ

        // GridPane để tổ chức giao diện
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Ô input nhập keyword
        keywordField = new TextField();
        keywordField.setPromptText("Enter Keyword");

        // Picker date time
        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        // Nút Fetch Twitter Data
        Button fetchDataButton = new Button("Fetch Twitter Data");
        fetchDataButton.setOnAction(event -> fetchTwitterData());

        // Thêm nút xuất dữ liệu
        Button exportDataButton = new Button("Export Data");
        exportDataButton.setOnAction(event -> exportData());

        // Thêm nút nhập dữ liệu
        Button importDataButton = new Button("Import Data");
        importDataButton.setOnAction(event -> importData());

        // Thêm nút "Xem danh sách bài viết theo hashtag/tag/keyword"
        Button viewByHashtagButton = new Button("View by Hashtag/Tag/Keyword");
        viewByHashtagButton.setOnAction(event -> viewByHashtag());

        // Thêm nút "Xếp loại Hashtag Hot Nhất"
        Button hotHashtagsButton = new Button("Hot Hashtags");
        hotHashtagsButton.setOnAction(event -> showHotHashtags());

        // Thêm nút "Xếp loại Tag Phổ Biến"
        Button popularTagsButton = new Button("Popular Tags");
        popularTagsButton.setOnAction(event -> showPopularTags());

        // Thêm các thành phần vào GridPane
        gridPane.add(keywordField, 0, 0, 1, 1);
        gridPane.add(startDatePicker, 1, 0, 1, 1);
        gridPane.add(endDatePicker, 2, 0, 1, 1);
        gridPane.add(fetchDataButton, 3, 0, 1, 1);
        gridPane.add(exportDataButton, 4, 0, 1, 1);
        gridPane.add(importDataButton, 5, 0, 1, 1);

        // ListView để hiển thị tweets
        tweetsListView = new ListView<>();
        tweetsListView.setPrefHeight(400); // Đặt chiều cao của ListView
        tweetsListView.setCellFactory(param -> new ListCell<Tweet>() {
            @Override
            protected void updateItem(Tweet item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getAccount() == null) {
                    setText(null);
                } else {
                    setText(item.getAccount()); // Hiển thị tài khoản của tweet
                    setOnMouseClicked(event -> showTweetDetails(item));
                }
            }
        });

        // Thêm các thành phần vào VBox
        VBox vBox = new VBox();
        vBox.getChildren().addAll(label, gridPane, tweetsListView);

        // Thêm nút View by Hashtag/Tag/Keyword, Hot Hashtags, và Popular Tags vào VBox
        vBox.getChildren().addAll(viewByHashtagButton, hotHashtagsButton, popularTagsButton);
        
        // Căn giữa VBox
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20); // Đặt khoảng cách giữa các thành phần
        vBox.setPadding(new javafx.geometry.Insets(20)); // Đặt độ lệch của VBox

        getChildren().add(vBox);
    }

    
 // Phương thức hiển thị các hashtag hot nhất
    private void showHotHashtags() {
        Map<String, Integer> hotHashtags = findHotHashtagsWithCounts();
        showTagDialog("Hot Hashtags", hotHashtags);
    }

 // Phương thức hiển thị các tag phổ biến
    private void showPopularTags() {
        Map<String, Integer> popularTags = findPopularTagsWithCounts();
        showTagDialog("Popular Tags", popularTags);
    }

    // Phương thức tìm kiếm các hashtag hot nhất
    private List<String> findHotHashtags() {
        Map<String, Integer> hashtagCounts = new HashMap<>();

        // Đếm số lần xuất hiện của mỗi hashtag
        for (Tweet tweet : tweetsListView.getItems()) {
            if (tweet != null && tweet.getHashtags() != null) {
                for (String hashtag : tweet.getHashtags()) {
                    hashtagCounts.put(hashtag, hashtagCounts.getOrDefault(hashtag, 0) + 1);
                }
            }
        }

        // Sắp xếp theo số lần xuất hiện giảm dần
        List<String> hotHashtags = new ArrayList<>(hashtagCounts.keySet());
        hotHashtags.sort((h1, h2) -> hashtagCounts.get(h2).compareTo(hashtagCounts.get(h1)));

        return hotHashtags;
    }
    
 // Phương thức tìm kiếm các hashtag hot nhất với số lần xuất hiện
    private Map<String, Integer> findHotHashtagsWithCounts() {
        Map<String, Integer> hashtagCounts = new HashMap<>();

        // Đếm số lần xuất hiện của mỗi hashtag
        for (Tweet tweet : tweetsListView.getItems()) {
            if (tweet != null && tweet.getHashtags() != null) {
                for (String hashtag : tweet.getHashtags()) {
                    hashtagCounts.put(hashtag, hashtagCounts.getOrDefault(hashtag, 0) + 1);
                }
            }
        }

        // Sắp xếp theo số lần xuất hiện giảm dần
        return sortByValueDescending(hashtagCounts);
    }

    // Phương thức tìm kiếm các tag phổ biến với số lần xuất hiện
    private Map<String, Integer> findPopularTagsWithCounts() {
        Map<String, Integer> tagCounts = new HashMap<>();

        // Đếm số lần xuất hiện của mỗi tag
        for (Tweet tweet : tweetsListView.getItems()) {
            if (tweet != null && tweet.getTags() != null) {
                for (String tag : tweet.getTags()) {
                    tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                }
            }
        }

        // Sắp xếp theo số lần xuất hiện giảm dần
        return sortByValueDescending(tagCounts);
    }

    // Phương thức sắp xếp một Map theo giá trị giảm dần
    private <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap<K, V>::new
                ));
    }

    // Phương thức tìm kiếm các tag phổ biến
    private List<String> findPopularTags() {
        Map<String, Integer> tagCounts = new HashMap<>();

        // Đếm số lần xuất hiện của mỗi tag
        for (Tweet tweet : tweetsListView.getItems()) {
            if (tweet != null && tweet.getTags() != null) {
                for (String tag : tweet.getTags()) {
                    tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                }
            }
        }

        // Sắp xếp theo số lần xuất hiện giảm dần
        List<String> popularTags = new ArrayList<>(tagCounts.keySet());
        popularTags.sort((t1, t2) -> tagCounts.get(t2).compareTo(tagCounts.get(t1)));

        return popularTags;
    }

 // Phương thức hiển thị dialog cho danh sách hashtag hoặc tag
    private void showTagDialog(String title, Map<String, Integer> tagCounts) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);

        // Set the button types
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Create a VBox to hold the tag details
        VBox tagDetails = new VBox();
        for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
            String tag = entry.getKey();
            int count = entry.getValue();
            Label label = new Label(tag + " - số lần xuất hiện: " + count);
            tagDetails.getChildren().add(label);
        }

        ScrollPane scrollPane = new ScrollPane(tagDetails);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(400);
        scrollPane.setPrefHeight(300);
        dialog.getDialogPane().setContent(scrollPane);

        // Show the dialog
        dialog.showAndWait();
    }

    // Phương thức xử lý sự kiện khi nút xuất dữ liệu được nhấn
    private void exportData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            List<Tweet> tweetsToExport = tweetsListView.getItems();
            saveTweetsToJson(tweetsToExport, selectedFile.getAbsolutePath());
        }
    }

    // Phương thức lưu tweets vào file JSON
    private void saveTweetsToJson(List<Tweet> tweets, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Chuyển danh sách tweets thành định dạng JSON
            String jsonString = objectMapper.writeValueAsString(tweets);

            // Ghi nội dung JSON vào file
            Files.write(Paths.get(filePath), jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức xử lý sự kiện khi nút nhập dữ liệu được nhấn
    private void importData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            List<Tweet> importedTweets = readTweetsFromJson(selectedFile.getAbsolutePath());

            // Hiển thị dữ liệu trên ListView
            ObservableList<Tweet> observableTweets = FXCollections.observableArrayList(importedTweets);
            tweetsListView.setItems(observableTweets);
        }
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

    private void fetchTwitterData() {
        String keyword = keywordField.getText();
        
     // Kiểm tra điều kiện: Keyword không được rỗng và cả hai mốc thời gian đều phải được chọn
        if (keyword.trim().isEmpty() || startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            // Hiển thị hộp thoại cảnh báo
            showAlert(Alert.AlertType.WARNING, "Warning", "Missing Information",
                    "Please enter a keyword and select both start and end dates.");
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        String startDateString = startDate != null ? startDate.toString() : null;
        String endDateString = endDate != null ? endDate.plusDays(1).toString() : null;

        TwitterDataCollector dataCollector = new TwitterDataCollector(keyword, startDateString, endDateString);
        List<Tweet> filteredTweets = dataCollector.collectData();

        // Hiển thị dữ liệu trên ListView
        ObservableList<Tweet> observableTweets = FXCollections.observableArrayList(filteredTweets);
        tweetsListView.setItems(observableTweets);
    }

    private void showTweetDetails(Tweet tweet) {
        // Hiển thị thông tin chi tiết của tweet
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Tweet Details");
        dialog.setHeaderText("Details for the selected tweet:");

        // Set the button types
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Create a VBox to hold the tweet details
        VBox tweetDetails = new VBox();
        tweetDetails.getChildren().add(new Label("Account: " + tweet.getAccount()));
        tweetDetails.getChildren().add(new Label("Like: " + tweet.getLike()));
        tweetDetails.getChildren().add(new Label("Post ID: " + tweet.getPost_id()));
        tweetDetails.getChildren().add(new Label("Link: " + tweet.getPost_link()));
        tweetDetails.getChildren().add(new Label("Text: " + tweet.getPost_text()));
        tweetDetails.getChildren().add(new Label("Time: " + tweet.getPost_time()));
        tweetDetails.getChildren().add(new Label("Reply: " + tweet.getReply()));
        tweetDetails.getChildren().add(new Label("Retweet: " + tweet.getRetweet()));
        tweetDetails.getChildren().add(new Label("Tags: " + tweet.getTags()));
        tweetDetails.getChildren().add(new Label("Hashtags: " + tweet.getHashtags()));

        ScrollPane scrollPane = new ScrollPane(tweetDetails);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(800); // Đặt chiều rộng cố định của hộp thoại chi tiết
        scrollPane.setPrefHeight(400); // Đặt chiều cao cố định của hộp thoại chi tiết
        dialog.getDialogPane().setContent(scrollPane);

        // Show the dialog
        dialog.showAndWait();
    }
    
    private void viewByHashtag() {
        // Hiển thị hộp thoại yêu cầu nhập hashtag/tag/keyword
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View by Hashtag/Tag/Keyword");
        dialog.setHeaderText("Enter Hashtag/Tag/Keyword:");
        dialog.setContentText("Hashtag/Tag/Keyword:");

        // Chờ người dùng nhập và nhấn OK hoặc Cancel
        dialog.showAndWait().ifPresent(hashtag -> {
            // Xử lý tìm kiếm theo hashtag/tag/keyword
            searchByHashtag(hashtag);
        });
    }

    // Phương thức thực hiện tìm kiếm theo hashtag/tag/keyword và hiển thị kết quả
    private void searchByHashtag(String input) {
        // Kiểm tra xem hashtag có giá trị hay không
        if (input == null || input.trim().isEmpty()) {
            // Hiển thị cảnh báo nếu hashtag rỗng
            showAlert(Alert.AlertType.WARNING, "Warning", "Invalid Hashtag/Tag/Keyword", "Please enter a valid Hashtag/Tag/Keyword.");
            return;
        }

        String firstChar = input.substring(0, 1);
        if (firstChar.equals("#")) {
            // Nếu bắt đầu bằng #, gọi hàm lọc theo hashtag
            List<Tweet> matchingTweets = filterTweetsByHashtag(tweetsListView.getItems(), input);
            showFilteredTweets(matchingTweets);
        } else if (firstChar.equals("@")) {
            // Nếu bắt đầu bằng @, gọi hàm lọc theo tag
            List<Tweet> matchingTweets = filterTweetsByTag(tweetsListView.getItems(), input);
            showFilteredTweets(matchingTweets);
        } else if (firstChar.matches("[a-z]") && !firstChar.equals("3")) {
            // Nếu bắt đầu bằng 1 ký tự thường, khác @ và 3, gọi hàm lọc theo keyword
            List<Tweet> matchingTweets = filterTweetsByKeyword(tweetsListView.getItems(), input);
            showFilteredTweets(matchingTweets);
        } else {
            // Ngược lại, hiển thị cảnh báo
            showAlert(Alert.AlertType.WARNING, "Warning", "Invalid Hashtag/Tag/Keyword", "Please enter a valid Hashtag/Tag/Keyword.");
        }
    }
    
 // Phương thức lọc các tweet theo tag
    private List<Tweet> filterTweetsByTag(List<Tweet> tweets, String tag) {
        List<Tweet> matchingTweets = new ArrayList<>();

        for (Tweet tweet : tweets) {
            if (tweet != null && tweet.getTags() != null && tweet.getTags().contains(tag)) {
                matchingTweets.add(tweet);
            }
        }

        return matchingTweets;
    }
    
 // Phương thức lọc các tweet theo keyword
    private List<Tweet> filterTweetsByKeyword(List<Tweet> tweets, String keyword) {
        List<Tweet> matchingTweets = new ArrayList<>();

        for (Tweet tweet : tweets) {
            if (tweet != null && tweet.getPost_text() != null && tweet.getPost_text().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTweets.add(tweet);
            }
        }

        return matchingTweets;
    }

    // Phương thức lọc các tweet theo hashtag
    private List<Tweet> filterTweetsByHashtag(List<Tweet> tweets, String hashtag) {
        // Tạo danh sách mới để chứa kết quả
        List<Tweet> matchingTweets = new ArrayList<>();

        // Lặp qua từng tweet
        for (Tweet tweet : tweets) {
            // Kiểm tra xem tweet có khác null và hashtag có tồn tại trong danh sách hashtags của tweet hay không
            if (tweet != null && tweet.getHashtags() != null && tweet.getHashtags().contains(hashtag)) {
                matchingTweets.add(tweet);
            }
        }

        return matchingTweets;
    }
    
 // Hiển thị kết quả trong ListView
    private void showFilteredTweets(List<Tweet> matchingTweets) {
        ObservableList<Tweet> observableMatchingTweets = FXCollections.observableArrayList(matchingTweets);
        tweetsListView.setItems(observableMatchingTweets);
    }

    // Phương thức hiển thị hộp thoại cảnh báo
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
