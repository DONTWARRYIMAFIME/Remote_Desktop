package org.infinityConnection.scenes.members;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;

public class MembersController {


    @FXML
    private JFXTreeTableView<Member> treeTableView;

    public void initialize() {
        JFXTreeTableColumn<Member, String> hostName = new JFXTreeTableColumn<>("Host name");
        hostName.setMinWidth(250);
        hostName.setCellValueFactory((param) -> param.getValue().getValue().hostName);

        JFXTreeTableColumn<Member, String> ip = new JFXTreeTableColumn<>("Client's ip address");
        ip.setMinWidth(250);
        ip.setCellValueFactory((param) -> param.getValue().getValue().ip);

        JFXTreeTableColumn<Member, String> connectionTime = new JFXTreeTableColumn<>("Connection time");
        connectionTime.setMinWidth(120);
        connectionTime.setCellValueFactory((param) -> param.getValue().getValue().connectionTime);

        JFXTreeTableColumn<Member, String> sessionTime = new JFXTreeTableColumn<>("Session time");
        sessionTime.setMinWidth(120);
        sessionTime.setCellValueFactory((param) -> param.getValue().getValue().sessionTime);

        ObservableList<Member> members = FXCollections.observableArrayList();
        final TreeItem<Member> root = new RecursiveTreeItem<>(members, RecursiveTreeObject::getChildren);
        treeTableView.getColumns().setAll(hostName, ip, connectionTime, sessionTime);
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);

        members.add(new Member("Client1", "192.168.40.penis"));
        members.add(new Member("Client2", "i <3 your mum"));
        members.add(new Member("Client3", "333"));
    }


}
