package org.remoteDesktop;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class Loader {

    public static FXMLLoader loadFXML(String fxml) {
        return new FXMLLoader(Loader.class.getResource(fxml + ".fxml"));
    }

    public static Image loadImage(String path) {
        Image image = null;
        try {
            image = new Image(Loader.class.getResource(path).toURI().toString());
        } catch (Exception e) {
            System.out.format("Cannot convert your path(%s) to URI\n", path);
        }

        return image;
    }

}
