package services;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.Result;
import model.ResultDTO;
import model.User;
import model.Game;
import persistance.repository.orm.GameHbmRepository;
import persistance.repository.orm.ResultHbmRepository;
import persistance.repository.orm.UserHbmRepository;

public class Service {
    private UserHbmRepository userHbmRepository;
    private GameHbmRepository gameHbmRepository;
    private ResultHbmRepository resultHbmRepository;

    public Service(UserHbmRepository userHbmRepository, GameHbmRepository gameHbmRepository, ResultHbmRepository resultHbmRepository) {
        this.userHbmRepository = userHbmRepository;
        this.gameHbmRepository = gameHbmRepository;
        this.resultHbmRepository = resultHbmRepository;
    }

    public User findUserByAlias(String alias) {
        return userHbmRepository.findOneByAlias(alias);
    }

    public Game getRandomGame(){
        var list = (List) gameHbmRepository.getAll();
        Random random = new Random();
        return (Game) list.get(random.nextInt(list.size()));
    }

    public Result resultThisRound(String guess, Game game, Result result){
        int positionOfLettersGuessedWord1 = 0;
        int positionOfLettersGuessedWord2 = 0;
        int positionOfLettersGuessedWord3 = 0;

        if(guess.equals( game.getWord1()) && !result.getWord1()){
            result.setWord1(true);
            result.setPoints(result.getPoints() + guess.length());
            return result;
        }
        if(guess.equals( game.getWord2()) && !result.getWord2()){
            result.setWord2(true);
            result.setPoints(result.getPoints() + guess.length());
            return result;
        }
        if(guess.equals( game.getWord3()) && !result.getWord3()){
            result.setWord3(true);
            result.setPoints(result.getPoints() + guess.length());
            return result;
        }

        for(int i = 0; i < guess.length(); i++){
            if(game.getWord1().length()>i && guess.charAt(i) == game.getWord1().charAt(i) && !result.getWord1())
                positionOfLettersGuessedWord1 ++;
            if(game.getWord2().length()>i && guess.charAt(i) == game.getWord2().charAt(i) && !result.getWord2())
                positionOfLettersGuessedWord2 ++;
            if(game.getWord3().length()>i && guess.charAt(i) == game.getWord3().charAt(i) && !result.getWord3())
                positionOfLettersGuessedWord3 ++;
        }
        int maxim = Math.max(Math.max(positionOfLettersGuessedWord1, positionOfLettersGuessedWord2), positionOfLettersGuessedWord3);
        result.setPoints(result.getPoints() + maxim);
        return result;
    }

    public Collection<Result> getAllResults(){
        return resultHbmRepository.getAll();
    }

    public void addResult(Result result){
        try {
            resultHbmRepository.add(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection<ResultDTO> getAllResultsDTO(){
        Collection<Result> results = getAllResults();
        List<ResultDTO> resultDTOS = new ArrayList<>();
        for(Result result: results){
            int userId = result.getUser();
            User user = userHbmRepository.findById(userId);
            ResultDTO resultDTO = new ResultDTO(user.getAlias(), result.getDate(), result.getPoints());
            resultDTOS.add(resultDTO);
        }
        return (Collection<ResultDTO>) resultDTOS;
    }

}
