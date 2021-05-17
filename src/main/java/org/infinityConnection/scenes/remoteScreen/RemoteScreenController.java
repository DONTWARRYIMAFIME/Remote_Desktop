package org.infinityConnection.scenes.remoteScreen;

import com.jfoenix.controls.JFXToolbar;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.infinityConnection.utils.EventsChangeListener;
import org.infinityConnection.utils.SceneController;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RemoteScreenController {

    @FXML
    private Label host;

    @FXML
    private Label timer;

    @FXML
    private ImageView iw;

    @FXML
    private JFXToolbar toolbar;

    private double iwFitWidth;
    private double iwFitHeight;

    private final RemoteScreenModel model = new RemoteScreenModel();
    private final ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> onResize();

    private EventsChangeListener updateHostName() {
        return new EventsChangeListener() {
            @Override
            public void onReadingChange() {
                Platform.runLater(() -> host.setText(model.getHostName()));
            }

            @Override
            public boolean isAutoCloasable() {
                return true;
            }
        };
    }

    private EventsChangeListener updateTimer() {
        return new EventsChangeListener() {
            @Override
            public void onReadingChange() {
                Platform.runLater(() -> timer.setText(model.getSessionTime()));
            }

            @Override
            public boolean isAutoCloasable() {
                return false;
            }
        };
    }

    private EventsChangeListener updateScreen() {
        return new EventsChangeListener() {
            @Override
            public void onReadingChange() {
                Image image = iw.getImage();
                Platform.runLater(() -> {
                    iw.setImage(model.getReceivedImage());
                    if (image == null) {
                        onResize();
                    }
                });
            }

            @Override
            public boolean isAutoCloasable() {
                return false;
            }
        };
    }

    private void onResize() {
        iwFitWidth = SceneController.scene.getWidth();
        iwFitHeight = SceneController.scene.getHeight() - toolbar.getHeight();

        iw.setFitWidth(iwFitWidth);
        iw.setFitHeight(iwFitHeight);
    }

    //Mouse events
    private EventsChangeListener onMouseMoved() {
        return new EventsChangeListener() {
            @Override
            public void onReadingChange() {
                iw.setOnMouseClicked(model.getMouseMovedEH(iwFitWidth, iwFitHeight));
            }

            @Override
            public boolean isAutoCloasable() {
                return false;
            }
        };
    }

    private EventsChangeListener onMousePressed() {
        return new EventsChangeListener() {
            @Override
            public void onReadingChange() {
                iw.setOnMousePressed(model.getMousePressedEH());
            }

            @Override
            public boolean isAutoCloasable() {
                return true;
            }
        };
    }

    private EventsChangeListener onMouseReleased() {
        return new EventsChangeListener() {
            @Override
            public void onReadingChange() {
                iw.setOnMouseReleased(model.getMouseReleasedEH());
            }

            @Override
            public boolean isAutoCloasable() {
                return true;
            }
        };
    }

    //Keyboard events
    private EventsChangeListener onKeyReleased() {
        return new EventsChangeListener() {
            @Override
            public void onReadingChange() {
                iw.setOnKeyTyped(model.getKeyReleasedEH());
            }

            @Override
            public boolean isAutoCloasable() {
                return true;
            }
        };
    }

    private void closeWindowEvent(WindowEvent event) {
        model.shutDown();
    }

    public void initialize() {
        Stage stage = (Stage) SceneController.scene.getWindow();
        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    public void exchangeData(DataInputStream dis, DataOutputStream dos) {
        iw.setImage(null);

        model.start(dis, dos);

        SceneController.scene.widthProperty().addListener(stageSizeListener);
        SceneController.scene.heightProperty().addListener(stageSizeListener);

        model.addListener(updateHostName());
        model.addListener(updateTimer());
        model.addListener(updateScreen());


        //Events
        model.addListener(onMouseMoved());
        model.addListener(onMousePressed());
        model.addListener(onMouseReleased());

        model.addListener(onKeyReleased());
        Platform.runLater(() -> {
            onResize();
        });


    }

    public boolean isStopped() {
        return model.isStopped();
    }

    public void onDisconnect() {
        SceneController.scene.widthProperty().removeListener(stageSizeListener);
        SceneController.scene.heightProperty().removeListener(stageSizeListener);

        model.shutDown();
    }
}
