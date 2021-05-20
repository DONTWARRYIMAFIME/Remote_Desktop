package org.infinityConnection.scenes.members;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import org.infinityConnection.utils.SceneController;

public class MembersController {


    @FXML
    private JFXTreeTableView<Member> treeTableView;

    public static ObservableList<Member> members = FXCollections.observableArrayList();
    private final ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> onResize();

    private final JFXTreeTableColumn<Member, String> hostName = new JFXTreeTableColumn<>("Host name");
    private final JFXTreeTableColumn<Member, String> ip = new JFXTreeTableColumn<>("Client's ip address");
    private final JFXTreeTableColumn<Member, String> connectionTime = new JFXTreeTableColumn<>("Connection time");
    private final JFXTreeTableColumn<Member, String> sessionTime = new JFXTreeTableColumn<>("Session time");

    private void onResize() {
        double widthPart = SceneController.childContainer.getWidth() / 6;
        hostName.setPrefWidth(2 * widthPart);
        ip.setPrefWidth(2 * widthPart);
        connectionTime.setPrefWidth(widthPart);
        sessionTime.setPrefWidth(widthPart);
    }

    public void initialize() {
        hostName.setCellValueFactory((param) -> param.getValue().getValue().hostName);
        ip.setCellValueFactory((param) -> param.getValue().getValue().ip);
        connectionTime.setCellValueFactory((param) -> param.getValue().getValue().connectionTime);
        sessionTime.setCellValueFactory((param) -> param.getValue().getValue().sessionTime);

        final TreeItem<Member> root = new RecursiveTreeItem<>(members, RecursiveTreeObject::getChildren);
        treeTableView.getColumns().setAll(hostName, ip, connectionTime, sessionTime);
        treeTableView.setColumnResizePolicy((resizeFeatures) -> false);
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);

        SceneController.childContainer.widthProperty().addListener(stageSizeListener);

    }

}
