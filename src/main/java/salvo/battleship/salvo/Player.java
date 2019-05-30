package salvo.battleship.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();


    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    private String password;


    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);

    }

    public Player( String userName, String password) {
        this.userName = userName;
        this.password = password;

    }

    public Player() { }
    //No-argument constructor.

    public Player(String user) {
        this.userName = user;


    }


    public String getUserName(){
        return userName;
    }

    public void setUsetName(String userName){
        this.userName = userName;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String toString(){
        return id + " " + userName;
    }

    public Map<String, Object> toDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId());
        dto.put("name", getUserName());

        return dto;
    }

    public void addScore(Score score) {
        score.setPlayer(this);
        scores.add(score);
    }

    public Score getScore(Game game){
        return scores.stream()
                .filter(score -> score.getGame().equals(game))
                .findFirst()
                .orElse(null);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}