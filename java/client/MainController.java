package client;

import controller.IController;
import controller.IObserver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Game;
import model.Result;
import model.ResultDTO;
import model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


public class MainController implements IObserver {
    private IController server;
    private User user;
    private Game game;
    private Result result;
    private int points = 0;

    ObservableList<ResultDTO> resultsList = FXCollections.observableArrayList();

    private int round = 0;

    @FXML
    Label labelLetters;
    
    @FXML
    TextField textFieldWord;
    
    @FXML
    Button buttonSubmit;
    
    @FXML
    TableView<ResultDTO> tableViewResults;

    @FXML
    void initialize() {
        tableViewResults.setItems(resultsList);
        tableViewResults.setVisible(false);
        
        tableViewResults.getColumns().clear();

        TableColumn<ResultDTO, String> nameColumn = new TableColumn<>("Alias");
        TableColumn<ResultDTO, String> dateColumn = new TableColumn<>("Date");
        TableColumn<ResultDTO, String> pointsColumn = new TableColumn<>("Points");
        
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("alias"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        
        tableViewResults.getColumns().add(nameColumn);
        tableViewResults.getColumns().add(dateColumn);
        tableViewResults.getColumns().add(pointsColumn);
    }
    
    public void showAllResult()
    {
        try {
            resultsList.setAll((Collection<? extends ResultDTO>) server.getAllResults());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    @FXML
    void handleSubmit() throws Exception {
        round++;
        if (round == 3)
        {
            String guess = textFieldWord.getText();
            int prevPoints = result.getPoints();
            result = server.getPoints(guess, game, result);
            int pointsCurrentRound  = result.getPoints() - prevPoints;

            try {
                server.addResult(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tableViewResults.setVisible(true);
            labelLetters.setVisible(false);
            textFieldWord.setVisible(false);
            buttonSubmit.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Points");
            if((guess.equals(game.getWord1()) || guess.equals(game.getWord2()) || guess.equals(game.getWord3()))
                    && guess.length() == pointsCurrentRound)
                alert.setHeaderText("Congratulations, you've guessed one word: " + guess);
            else
                alert.setHeaderText("Points received this round:");
            alert.setContentText("+" + String.valueOf(pointsCurrentRound));
            alert.showAndWait();
        }
        else{
            String guess = textFieldWord.getText();
            int prevPoints = result.getPoints();
            result = server.getPoints(guess, game, result);
            int pointsCurrentRound  = result.getPoints() - prevPoints;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Points");
            if((guess.equals(game.getWord1()) || guess.equals(game.getWord2()) || guess.equals(game.getWord3())) 
                    && guess.length() == pointsCurrentRound)
                alert.setHeaderText("Congratulations, you've guessed one word: " + guess);
            else
                alert.setHeaderText("Points received this round:");
            alert.setContentText("+" + String.valueOf(pointsCurrentRound));
            alert.showAndWait();

        }
    }


    public void setServer(IController server) throws Exception {
        this.server = server;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGame(Game game) {
        this.game = game;
        this.labelLetters.setText("Letters: " + game.getLetters());
        this.result = new Result(user.getId(), game.getId(), 0, LocalDateTime.now());
    }

    @Override
    public void resultAdded(List<ResultDTO> results) throws Exception {
        Platform.runLater(() -> {
            try {
                this.showAllResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
