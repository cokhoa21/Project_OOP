package com.example.gui;

import com.example.datacollection.blog.BlogDataCollector;
import com.example.model.Blog;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BlogScreen extends VBox {

    private ListView<Blog> urlResultListView;
    private ListView<Blog> keywordResultListView;
    private TextField urlInput;

    public BlogScreen() {
        Label label = new Label("Blog Data Collection");
        label.setFont(new Font(30));
        getChildren().add(label);
        setAlignment(Pos.CENTER);

        HBox urlBox = new HBox();
        urlBox.setAlignment(Pos.CENTER_LEFT);

        urlInput = new TextField();
        urlInput.setPrefSize(700, 0);
        urlInput.setPromptText("Enter Blog URL");
        urlBox.getChildren().add(urlInput);

        Button collectButton = new Button("Collect Data");
        collectButton.setOnAction(event -> collectBlogData());
        urlBox.getChildren().add(collectButton);

        getChildren().add(urlBox);

        urlResultListView = new ListView<>();
        urlResultListView.setPrefHeight(200);
        getChildren().add(urlResultListView);

        HBox keywordBox = new HBox();
        keywordBox.setAlignment(Pos.CENTER_LEFT);

        TextField keywordInput = new TextField();
        keywordInput.setPrefSize(700, 0);
        keywordInput.setPromptText("Enter Hashtag/Tag/Keyword");
        keywordBox.getChildren().add(keywordInput);

        Button viewByKeywordButton = new Button("Search Data");
        viewByKeywordButton.setOnAction(event -> viewByKeyword(keywordInput.getText()));
        keywordBox.getChildren().add(viewByKeywordButton);

        getChildren().add(keywordBox);

        keywordResultListView = new ListView<>();
        keywordResultListView.setPrefHeight(200);
        getChildren().add(keywordResultListView);

        urlResultListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showBlogDetails(newValue)
        );

        keywordResultListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showBlogDetails(newValue)
        );

        Button exportButton = new Button("Export Data");
        exportButton.setOnAction(event -> exportData());
        getChildren().add(exportButton);
    }

    private void collectBlogData() {
        String blogUrl = urlInput.getText();
        BlogDataCollector blogDataCollector = new BlogDataCollector(blogUrl);
        List<Blog> blogPosts = blogDataCollector.collectData();

        urlResultListView.getItems().setAll(blogPosts);
    }

    private void viewByKeyword(String keyword) {
        String blogUrl = urlInput.getText();
        BlogDataCollector blogDataCollector = new BlogDataCollector(blogUrl);
        List<Blog> postsByKeyword = blogDataCollector.getPostsByKeyword(keyword);

        keywordResultListView.getItems().setAll(postsByKeyword);
    }

    private void showBlogDetails(Blog blog) {
        if (blog != null) {
            Dialog<String> blogDetailsDialog = new Dialog<>();
            blogDetailsDialog.setTitle(blog.getTitle());

            TextArea blogContentTextArea = new TextArea(blog.getContent());
            blogContentTextArea.setEditable(false);
            blogContentTextArea.setWrapText(true);

            blogDetailsDialog.getDialogPane().setContent(blogContentTextArea);

            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            blogDetailsDialog.getDialogPane().getButtonTypes().add(closeButton);

            blogDetailsDialog.showAndWait();
        }
    }

    private void exportData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            List<Blog> blogsToExport = urlResultListView.getItems();
            saveBlogsToJson(blogsToExport, selectedFile.getAbsolutePath());
        }
    }

    private void saveBlogsToJson(List<Blog> blogs, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Chuyển danh sách blog thành định dạng JSON
            String jsonString = objectMapper.writeValueAsString(blogs);

            // Ghi nội dung JSON vào file
            Files.write(Paths.get(filePath), jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
