package salvo.battleship.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private int turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;



    //vou editar essa data.
    private String finishDate;
    private Double points;

    public Score(){};


    public Score(String finishDate, Double playerScore) {
        this.finishDate = finishDate;
        this.points = playerScore;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }


    public void setGame(Game game) {
        this.game = game;
    }
    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Double getPlayerScore() {
        return points;
    }

    public void setPlayerScore(Double playerScore) {
        playerScore = playerScore;
    }

    public String getFinishDate() {
        Date date = new Date();
        String editDate = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat objDate = new SimpleDateFormat(editDate);
        String novaDateS = objDate.format(date);
        return novaDateS;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public void setScore(Player player, Game game) {
        this.player = player;
        this.game = game;
    }


    public void setScore(Game game) {
    }


}
