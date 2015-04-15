package com.coursework.program;

import com.coursework.generator.ImageScanner;
import com.coursework.utils.AppConst;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable {

    @FXML
    private ProgressIndicator progInd;
    @FXML
    private ProgressIndicator progInd1;

    @FXML
    private TextField tfdXo;
    @FXML
    private TextField tfdYo;
    @FXML
    private TextField tfdZo;
    @FXML
    private TextField tfdA;
    @FXML
    private TextField tfdB;
    @FXML
    private TextField tfdXo1;
    @FXML
    private TextField tfdYo1;
    @FXML
    private TextField tfdZo1;
    @FXML
    private TextField tfdA1;
    @FXML
    private TextField tfdB1;

    @FXML
    private ImageView imgView;
    @FXML
    private ImageView imgView1;

    @FXML
    private Button btnOpen;
    @FXML
    private Button btnOpen1;
    @FXML
    private Button btnEncrypt;
    @FXML
    private Button btnDecrypt;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSave1;

    private FileChooser fileChooser, savefileChooser;
    private File selectedFile;
    private ImageScanner imageScanner = new ImageScanner();
    private Image image;
    private boolean flag, enFlag, deFlag;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Image Files", AppConst.IMAGES_EXTENSIONS));
        savefileChooser = new FileChooser();
        savefileChooser.setTitle("Save Image");
        savefileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Image File", AppConst.IMAGES_EXTENSIONS_SAVE));
        tfdA.setDisable(true);
        tfdA1.setDisable(true);
        tfdB.setDisable(true);
        tfdB1.setDisable(true);
        tfdXo.setDisable(true);
        tfdXo1.setDisable(true);
        tfdYo.setDisable(true);
        tfdYo1.setDisable(true);
        tfdZo.setDisable(true);
        tfdZo1.setDisable(true);
    }

    public void clickOpen() {
        selectedFile = fileChooser.showOpenDialog(btnEncrypt.getParent().getScene().getWindow());
        if (selectedFile != null) {
            image = new Image(selectedFile.toURI().toString());
            imgView.setImage(image);
            tfdXo.setDisable(false);
            tfdYo.setDisable(false);
            tfdZo.setDisable(false);
            tfdA.setDisable(false);
            tfdB.setDisable(false);
            btnEncrypt.setDisable(false);
        }
    }

    public void clickOpen1() {
        selectedFile = fileChooser.showOpenDialog(btnDecrypt.getParent().getScene().getWindow());
        if (selectedFile != null) {
            image = new Image(selectedFile.toURI().toString());
            imgView1.setImage(image);
            tfdXo1.setDisable(false);
            tfdYo1.setDisable(false);
            tfdZo1.setDisable(false);
            tfdA1.setDisable(false);
            tfdB1.setDisable(false);
            btnDecrypt.setDisable(false);
        }
    }

    public void clickDecrypt() {
        enFlag = false;
        deFlag = true;
        Task task = createTask();
        progInd1.progressProperty().bind(task.progressProperty());
        new Thread(task).start();

    }

    public void clickEncrypt() {
        enFlag = true;
        deFlag = false;
        Task task = createTask();
        progInd.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    private Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            public Void call() {
                if (enFlag && !deFlag) {
                    if (!validator(tfdXo.getText()) || !validator(tfdYo.getText()) || !validator(tfdZo.getText())
                            || Double.parseDouble(tfdA.getText()) > 0.6 || Double.parseDouble(tfdA.getText()) < -0.6
                            || Double.parseDouble(tfdB.getText()) > 2.5 || Double.parseDouble(tfdB.getText()) < 0.8) {
                        JOptionPane.showMessageDialog(null, "Введіть корректні дані",
                                "Помилка", JOptionPane.ERROR_MESSAGE);
                        updateProgress(0, 10);
                    } else if (validator(tfdXo.getText()) || validator(tfdYo.getText()) || validator(tfdZo.getText())
                            || Double.parseDouble(tfdA.getText()) <= 0.6 || Double.parseDouble(tfdA.getText()) >= -0.6
                            || Double.parseDouble(tfdB.getText()) <= 2.5 || Double.parseDouble(tfdB.getText()) >= 0.8) {

                        double x = Double.parseDouble(tfdXo.getText());
                        double y = Double.parseDouble(tfdYo.getText());
                        double z = Double.parseDouble(tfdZo.getText());
                        double a = Double.parseDouble(tfdA.getText());
                        double b = Double.parseDouble(tfdB.getText());
                        imageScanner.scanImage(selectedFile, x, y, z, a, b, enFlag, deFlag);
                        updateProgress(10, 10);
                        btnSave.setDisable(false);
                        image = new Image(imageScanner.getSavedImage().toURI().toString());
                        imgView.setImage(image);

                    }
                } else if (deFlag && !enFlag) {
                    if (!validator(tfdXo1.getText()) || !validator(tfdYo1.getText()) || !validator(tfdZo1.getText())
                            || Double.parseDouble(tfdA1.getText()) > 0.6 || Double.parseDouble(tfdA1.getText()) < -0.6
                            || Double.parseDouble(tfdB1.getText()) > 2.5 || Double.parseDouble(tfdB1.getText()) < 0.8) {
                        JOptionPane.showMessageDialog(null, "Введіть корректні дані",
                                "Помилка", JOptionPane.ERROR_MESSAGE);
                        updateProgress(0, 10);
                    } else if (validator(tfdXo.getText()) || validator(tfdYo.getText()) || validator(tfdZo.getText())
                            || Double.parseDouble(tfdA1.getText()) <= 0.6 || Double.parseDouble(tfdA1.getText()) >= -0.6
                            || Double.parseDouble(tfdB1.getText()) <= 2.5 || Double.parseDouble(tfdB1.getText()) >= 0.8) {
                        double x = Double.parseDouble(tfdXo1.getText());
                        double y = Double.parseDouble(tfdYo1.getText());
                        double z = Double.parseDouble(tfdZo1.getText());
                        double a = Double.parseDouble(tfdA1.getText());
                        double b = Double.parseDouble(tfdB1.getText());
                        imageScanner.scanImage(selectedFile, x, y, z, a, b, enFlag, deFlag);
                        updateProgress(10, 10);
                        btnSave1.setDisable(false);
                        image = new Image(imageScanner.getSavedImage().toURI().toString());
                        imgView1.setImage(image);
                    }

                }
                return null;
            }
        };
    }

    public void clickSave() {
        File file = savefileChooser.showSaveDialog(btnDecrypt.getParent().getScene().getWindow());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(imgView.getImage(),
                        null), "png", file);
            } catch (IOException ex) {
                Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void clickSave1() {
        File file = savefileChooser.showSaveDialog(btnDecrypt.getParent().getScene().getWindow());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(imgView1.getImage(),
                        null), "png", file);
            } catch (IOException ex) {
                Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean validator(String string) {
        Pattern pattern = Pattern.compile(AppConst.PATTERN_XYZ);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

}