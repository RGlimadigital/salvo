package salvo.battleship.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api") //chama essa url
public class SalvoController {

    //Temos que criar um @Autowired para cada repositorio que queremos acessar.
    @Autowired//injeccion de dependencia.
    private GamePlayerRepository gamePlayerRepo;
    @Autowired//injeccion de dependencia.
    private GamesRepository gamesRepo ;
    @Autowired//injeccion de dependencia.
    private ScoreRepository scoreRepository ;

    @Autowired//injeccion de dependencia.
    private PlayerRepository playerRepo ;

    @Autowired
    private ShipRepository shipsRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public  ResponseEntity <Object> register(@RequestBody Player playerToRegister) {
        if (playerToRegister.getUserName().isEmpty() || playerToRegister.getPassword().isEmpty())  return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        if (playerRepo.findByUserName(playerToRegister.getUserName()) !=  null)  return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);


        playerRepo.save(playerToRegister);

        return new ResponseEntity<>(HttpStatus.CREATED);


    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public  ResponseEntity <Object> shipList( @PathVariable long gamePlayerId, @RequestBody Set<Ship> ships,Authentication auth)  {
        if(isConvidado(auth))  return new ResponseEntity<>("No ships", HttpStatus.UNAUTHORIZED);

        Player player = playerRepo.findByUserName(auth.getName());
        GamePlayer gameplay = gamePlayerRepo.findOne(gamePlayerId);
        System.out.println("My Player = " + player);
        System.out.println("gameplay = " + gameplay);

        if(gameplay == null) return new ResponseEntity<>("No ships", HttpStatus.UNAUTHORIZED);

        if(!player.getGamePlayers().contains(gameplay)) return new ResponseEntity<>("No ships", HttpStatus.UNAUTHORIZED);

        if(!gameplay.getShips().isEmpty())  return new ResponseEntity<>("No ships", HttpStatus.FORBIDDEN);

        ships.forEach((ship) -> {
           gameplay.addShip(ship);
           shipsRepo.save(ship);

        });

     


        System.out.println("gameplay getShips = " + gameplay.getShips());
        System.out.println("shipRepo = " + shipsRepo);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public  ResponseEntity <Object> createGame(Authentication auth) {

        Game game = new Game(Game.getNovaDate());
        GamePlayer gamePlayer = new GamePlayer();

        String playerName = auth.getName();
        Player player = playerRepo.findByUserName(playerName);

        player.addGamePlayer(gamePlayer);
        game.addGamePlayer(gamePlayer);

        gamesRepo.save(game);
        gamePlayerRepo.save(gamePlayer);

        return new ResponseEntity<>(HttpStatus.CREATED);


    }

    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public  ResponseEntity <Object> joinGame(Authentication auth, @PathVariable long gameId ) {

        Player player = playerRepo.findByUserName(auth.getName());
        Game game = gamesRepo.findOne(gameId);

        if(game.gamePlayers.size() > 1) return new ResponseEntity<>("The game is Full", HttpStatus.FORBIDDEN);

        if(game == null || player == null) return new ResponseEntity<>("No name given", HttpStatus.FORBIDDEN);


        GamePlayer gamePlayer = new GamePlayer();
        player.addGamePlayer(gamePlayer);
        game.addGamePlayer(gamePlayer);

        gamePlayerRepo.save(gamePlayer);

        System.out.println("gpId" + gamePlayer.getId());
        return new ResponseEntity<>(makeMap("gpId", gamePlayer.getId()),  HttpStatus.CREATED);


    }
    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping("/games")//afecta la que esta cerca
    public Map<String, Object> getAll(Authentication authentication) {
        List<Map<String, Object>> allGames = gamesRepo.findAll()
                .stream()
                .map(game -> makeGamesDTO(game))
                .collect(Collectors.toList());

        Map<String, Object> rodrigoDto = new LinkedHashMap<String, Object>();
        rodrigoDto.put("player", isConvidado(authentication) ? null : makeUserDTO(currentUser(authentication)) );
        rodrigoDto.put("games", allGames);

        return rodrigoDto;


    }

    @RequestMapping("/leaderboard")
    public List<LinkedHashMap<String, Object>> getLeader() {
        return playerRepo.findAll()
                .stream()
                .filter(player -> player.getScores().size() > 0)
                .map(this::getScoresDTO)
                .collect(Collectors.toList());
    }

    private LinkedHashMap<String, Object> getScoresDTO(Player player){
        long losts = player.getScores()
                .stream()
                .filter(score -> score.getPlayerScore() == 0.0)
                .count(); 

        long wons = player.getScores()
                .stream()
                .filter(score -> score.getPlayerScore() == 1.0)
                .count();

        long ties = player.getScores()
                .stream()
                .filter(score -> score.getPlayerScore() == 0.5)
                .count();

        double total = player.getScores()
                .stream()
                .mapToDouble(score -> score.getPlayerScore())
                .sum();

        LinkedHashMap<String, Object> miScore = new LinkedHashMap<>();

        miScore.put("player", player.getUserName());
        miScore.put("wons", wons);
        miScore.put("losts", losts);
        miScore.put("ties", ties);
        miScore.put("total", total);

        return miScore;
    }



    @RequestMapping(value = "/game_view/{gamePlayerId}")//afecta la que esta a bajo
    public Map<String, Object> getView(@PathVariable Long gamePlayerId) {

        GamePlayer gamePlayer = gamePlayerRepo.findOne(gamePlayerId);

        Game game = gamePlayer.getGame();

        Set<GamePlayer> gamePlayers = game.getGamePlayers();


        GamePlayer oponent = gamePlayers.stream().filter(gp -> !gamePlayer.equals(gp)).findFirst().orElse(null);


        Map<String, Object> miMapa = new LinkedHashMap<>();

        miMapa.put("id", gamePlayer.getGame().getId());
        miMapa.put("created", gamePlayer.getGame().getNovaDate());


        miMapa.put("gameplayer", gamePlayer.getGame().getGamePlayers().stream()
                .map(gp -> makeGamePlayerDTO(gp))
                .collect(Collectors.toList()));
        miMapa.put("Ships", makeShipDTO(gamePlayer.getShips()));
        miMapa.put("salvos", makeSalvoDTO(gamePlayer.getSalvos()));
        miMapa.put("oponentShips", oponent != null ? makeSalvoDTO(oponent.getSalvos()) : new ArrayList<>());
        miMapa.put("Score", makeScoreDTO(game.getScores()));




                return miMapa;
    }



    //mete aqui 8.data
    //O que temos aqui e uma hierarquia de construcao de informaçoes que serao apresentada.

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String, Object> MapaDTO = new LinkedHashMap<String, Object>();
        MapaDTO.put("id", gamePlayer.getId());
        MapaDTO.put("player", makePlayerDTO(gamePlayer.getPlayer()));

        return MapaDTO;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> playerDto = new LinkedHashMap<String, Object>();
        playerDto.put("id", player.getId());
        playerDto.put("userName", player.getUserName());
        playerDto.put("Password", player.getPassword());
        playerDto.put("Score", player.getScores());
        return playerDto;
    }

    private Map<String, Object> makeUserDTO(Player player) {
        Map<String, Object> playerDto = new LinkedHashMap<String, Object>();
        playerDto.put("id", player.getId());
        playerDto.put("userName", player.getUserName());
        return playerDto;
    }

    private List<Map<String, Object>> makeShipDTO(Set<Ship> ships) {
            return ships.stream().map(ship -> {
                Map<String, Object> shipDto = new LinkedHashMap<String, Object>();
                shipDto.put("type", ship.getType());
                shipDto.put("locations", ship.getLocation());
                return shipDto;
            }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> makeScoreDTO(Set<Score> scores) {
        return scores.stream().map(score -> {
            Map<String, Object> scoreDto = new LinkedHashMap<String, Object>();
            scoreDto.put("score", score.getPlayerScore());
            scoreDto.put("player", score.getPlayer().getId());

            return scoreDto;

        }).collect(Collectors.toList());
    }


    private List<Map<String, Object>> makeSalvoDTO(Set<Salvo> salvos) {
        return salvos.stream().map(salvo -> {
            Map<String, Object> salvoDto = new LinkedHashMap<String, Object>();
            salvoDto.put("turn", salvo.getTurn());
            salvoDto.put("salvos", salvo.getLocations());
            return salvoDto;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> makeGamesDTO(Game game){
            Map<String, Object> gameDto = new LinkedHashMap<String, Object>();
            gameDto.put("id", game.getId());
            gameDto.put("fecha", game.getNovaDate());
            gameDto.put("gamePlayers", game.getGamePlayers().stream()
                        .map(gamePlayer -> makeGamePlayerDTO( gamePlayer))
                    .collect(Collectors.toList()));

            return gameDto;
    }

    private boolean isConvidado(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public Player currentUser (Authentication authentication){

        return playerRepo.findByUserName(authentication.getName());
    }
}