package restservices.restservices;

import model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import persistance.repository.orm.GameHbmRepository;

@CrossOrigin
@RestController
@RequestMapping("restservices/games")
public class GameRestController {
    @Autowired
    private GameHbmRepository gameHbmRepository = new GameHbmRepository();

    @PostMapping
    public Game create(@RequestBody Game game) throws Exception {
        System.out.println("Creating game");
        gameHbmRepository.add(game);
        return game;
    }

}
