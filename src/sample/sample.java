package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class sample {

    @FXML private TextField inputTextField;
    @FXML private TextField outputTextField;
    @FXML private Label resolutionLabel;
    @FXML private Slider tileSizeSlider;
    @FXML private Label tileSizeValueLabel;
    @FXML private Slider zoomSlider;
    @FXML private Label zoomValueLabel;
    @FXML private ListView<File> inputImageList;
    @FXML private TileView tileView;

    public Stage stage;
    private int zoomValue;
    private int tileSizeValue;
    private File inputDirectoryFile;
    private File outputDirectoryFile;


    @FXML private void initialize() {

        this.zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setZoomValue(newValue.intValue());
            }
        });

        this.tileSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setTileSizeValue(newValue.intValue());
            }
        });

        this.tileSizeSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tileView.redraw();
            }
        });

        this.inputImageList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
                Image image = new Image(newValue.toURI().toString());
                tileView.setFile(newValue);
                resolutionLabel.setText((int)image.getWidth() + "x" + (int)image.getHeight());
            }
        });


        this.inputImageList.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
            @Override
            public ListCell<File> call(ListView<File> param) {
                return new XCell();
            }
        });
    }


    @FXML private void selectInputDirectoryHandler() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Select the input directory");

        File file = fileChooser.showDialog(this.stage);
        this.setInputDirectoryFile(file);
    }

    @FXML private void selectOutputDirectoryHandler() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Select the output directory");

        File file = fileChooser.showDialog(this.stage);
        this.setOutputDirectoryFile(file);
    }

    @FXML private void buildHandler() {

        Image inputImage = new Image(this.tileView.file.toURI().toString());
        PixelReader reader = inputImage.getPixelReader();

        int w = (int)inputImage.getWidth();
        int h = (int)inputImage.getHeight();
        int size = (int)this.tileSizeSlider.getValue();

        try {
            for(int x=0; x<=w; x+=size) {
                for(int y=0; y<=h; y+=size) {
                    if(size+x < inputImage.getWidth() && size+y < inputImage.getHeight()) {
                        WritableImage newImage = new WritableImage(reader, x, y, size, size);
                        String outputFilePath = this.outputDirectoryFile.getAbsolutePath() + "/output_" + this.tileView.file.getName() + "_" + x + "_" + y + ".jpeg";

                        File outputFile = new File(outputFilePath);

                        BufferedImage image = SwingFXUtils.fromFXImage(newImage, null); // Get buffered image.
                        BufferedImage imageRGB = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.OPAQUE); // Remove alpha-channel from buffered image.
                        Graphics2D graphics = imageRGB.createGraphics();
                        graphics.drawImage(image, 0, 0, null);
                        ImageIO.write(imageRGB, "jpg", outputFile);
                        graphics.dispose();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }


    public void setTileSizeValue(int newValue) {
        if(tileSizeValue != newValue)
        {
            tileSizeValue = newValue;
            this.tileSizeSlider.setValue(tileSizeValue);
            this.tileSizeValueLabel.setText(tileSizeValue+"");
            this.tileView.tileSize = newValue;
        }
    }

    public void setZoomValue(int newValue) {
        if(zoomValue != newValue)
        {
            zoomValue = newValue;
            this.zoomSlider.setValue(zoomValue);
            this.zoomValueLabel.setText(zoomValue+"");
        }
    }

    private void setInputDirectoryFile(File file) {
        if(file != null) {
            inputDirectoryFile = file;
            this.inputTextField.setText(file.getAbsolutePath());

            File[] inputFileArray = file.listFiles(new JpegFileFiler());
            this.inputImageList.setItems(FXCollections.observableList(Arrays.asList(inputFileArray)));
        }
    }


    private void setOutputDirectoryFile(File file) {
        if(file != null) {
            outputDirectoryFile = file;
            this.outputTextField.setText(file.getAbsolutePath());
        }
    }

    private class JpegFileFiler implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() && (pathname.getName().endsWith("jpg") || pathname.getName().endsWith("jpeg"));
        }
    }


    private class XCell extends ListCell<File> {

        @Override
        protected void updateItem(File file, boolean empty) {
            super.updateItem(file, empty);
            if(file == null) setText("");
            else setText(file.getName());
        }
    }
}
