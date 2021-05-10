package org.infinityConnection.scenes.container;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.infinityConnection.utils.SceneController;

public class ContainerSceneController {

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXHamburger hamburger;

    private void setHamburgerAction() {
        HamburgerBasicCloseTransition task = new HamburgerBasicCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            task.setRate(task.getRate() * -1);
            task.play();

            if (drawer.isOpened()) {
                drawer.close();
            } else {
                drawer.open();
            }
        });
    }

    public void initialize() {
        drawer.setSidePane(SceneController.getParent("drawerScene"));
        drawer.setOnDrawerOpening((e) -> drawer.setMinWidth(drawer.getDefaultDrawerSize()));
        drawer.setOnDrawerClosed((e) -> drawer.setMinWidth(0));
        setHamburgerAction();
    }

}
