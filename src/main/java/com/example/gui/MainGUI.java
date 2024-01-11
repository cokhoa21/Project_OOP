package com.example.gui;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGUI extends Application {

    private BorderPane borderPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Collection App");

        // Create buttons for menu items
        Button twitterButton = createMenuItem("Twitter", "/images/icons/twitter.png");
        Button blogButton = createMenuItem("Blog", "/images/icons/blog.png");
        Button nftButton = createMenuItem("NFT", "/images/icons/nft.png");

        twitterButton.setOnAction(event -> switchToScreen(new TwitterScreen()));
        blogButton.setOnAction(event -> switchToScreen(new BlogScreen()));
        nftButton.setOnAction(event -> switchToScreen(new NFTScreen()));

        // Create the VBox for the vertical menu
        VBox menuVBox = new VBox(twitterButton, blogButton, nftButton);
        menuVBox.setSpacing(10); // Set spacing between items
        menuVBox.setStyle("-fx-background-color: #2E3C4E;"); // Set background color

        // Create the main layout
        borderPane = new BorderPane();
        borderPane.setLeft(menuVBox);

        // Apply styles to the menu bar
        borderPane.setStyle("-fx-background-color: #2E3C4E;"); // Set menu bar background color

        // Create the scene
        Scene scene = new Scene(borderPane, 1200, 700);

        // Apply styles to the scene
        scene.getRoot().setStyle("-fx-background-color: white;"); // Set background color to white

        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private Button createMenuItem(String text, String iconPath) {
        Image iconImage = new Image(getClass().getResourceAsStream(iconPath));
        ImageView iconView = new ImageView(iconImage);
        iconView.setFitHeight(30);
        iconView.setFitWidth(30);

        Button button = new Button(text, iconView);

        // Apply styles to the button
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16;");

        // Add some dummy action for illustration
        button.setOnAction(event -> {
            System.out.println("Clicked on: " + text);
            // Add your logic for handling the button click here
        });

        return button;
    }

    private void switchToScreen(Node screen) {
        borderPane.setCenter(screen);
    }
}
