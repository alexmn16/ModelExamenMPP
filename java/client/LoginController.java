package client;

import controller.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import services.Service;

public class LoginController {
    private IController server;
    private MainController mainController;
    Parent mainParent;


    @FXML
    TextField textFieldUsername;

    @FXML
    Button buttonConnect;

    @FXML
    Label labelError;

    @FXML
    void initialize()
    {
    }

    @FXML
    void handleConnect(ActionEvent actionEvent) throws Exception {
        String alias = textFieldUsername.getText();
        User user = new User(alias);
        try
        {
            User searchedUser = server.login(user, mainController);
            Stage stage = new Stage();
            stage.setTitle("Game");
            stage.setScene(new Scene(mainParent));
            mainController.setUser(searchedUser);
            mainController.setGame(server.getRandomGame());
            mainController.showAllResult();
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Authentication failure");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }


    public void setServer(IController server) {
        this.server = server;
    }

    public void setMainParent(Parent p) { mainParent = p;}

    public void setMainController(MainController mainController)
    {
        this.mainController = mainController;
    }
}
