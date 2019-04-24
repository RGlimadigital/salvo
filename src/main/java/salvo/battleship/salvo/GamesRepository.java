package salvo.battleship.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface GamesRepository extends JpaRepository<Game, Long> {
    //List<Game> findById(long id);



}
