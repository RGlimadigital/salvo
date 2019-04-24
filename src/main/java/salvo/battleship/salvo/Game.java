package salvo.battleship.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity

public class Game{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String novaDate;
    //declarando vazia
    //Aqui edito o formato de hora, para que seja formato 24 horas

    //O que significa essa estrutura?
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();




    public Game(){

    }
    //no argument constructor

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public static String getNovaDate() {
        Date date = new Date();
        String editDate = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat objDate = new SimpleDateFormat(editDate);
        String novaDate = objDate.format(date);


        return novaDate;
    }

    public void setNovaData(String novaData) {
        this.novaDate = novaData;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Game(String novaData) {
        this.novaDate = novaData;

    }


    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public void addScore(Score score) {
        score.setGame(this);
        scores.add(score);
    }

    public Set<Score> getScores() {
        return scores;
    }
}

