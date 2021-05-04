package org.infinityConnection.scenes.container;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import org.infinityConnection.utils.SceneController;

public class ContainerSceneController {

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXHamburger hamburger;

    private void setHamburgerAction() {
        HamburgerBasicCloseTransition burgerTransition = new HamburgerBasicCloseTransition(hamburger);
        burgerTransition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            burgerTransition.setRate(burgerTransition.getRate() * -1);
            burgerTransition.play();

            if (drawer.isOpened()) {
                drawer.close();
            } else {
                drawer.open();
            }
        });
    }

    public void initialize() {
        drawer.setSidePane(SceneController.getParent("drawerScene"));
        setHamburgerAction();
    }

}
