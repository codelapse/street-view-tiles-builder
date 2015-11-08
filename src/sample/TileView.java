package sample;


import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by eric on 07/11/2015.
 */
public class TileView extends Pane {

    private Canvas canvas;
    public File file;
    public int tileSize = 270;

    public void setFile(File newFile) {
        this.file = newFile;
        this.redraw();
    }


    public void redraw() {
        if (this.canvas != null)
        {
            this.getChildren().remove(this.canvas);
        }

        {
            Canvas c = new Canvas(this.getWidth(), this.getHeight());

            if(this.getChildren().add(c)) {
                this.canvas = c;
            }
        }
        GraphicsContext context = this.canvas.getGraphicsContext2D();

        if (this.file != null) {
            Image image = new Image(this.file.toURI().toString());
            double ratio = canvas.getWidth() / image.getWidth();

            int w = (int)(image.getWidth() * ratio);
            int h = (int)(image.getHeight() * ratio);

            int offset = (int)(canvas.getHeight() - h)/2;

            context.drawImage(image, 0, offset, w, h);


            context.setFill(Color.LIGHTGRAY);

            context.setLineWidth(1);


            context.setLineWidth(1);

            int size = (int)(this.tileSize * ratio);
            for(int x=0; x<w; x+=size) {
                context.fillRect(x, offset, 1, h);

                for(int y=size; y<=h; y+=size) {
                    context.fillRect(0, y+offset, w, 1);
                }
            }
        }
    }

}
