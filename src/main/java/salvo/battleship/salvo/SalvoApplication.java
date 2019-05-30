package salvo.battleship.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class 	SalvoApplication extends SpringBootServletInitializer {







	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	//Todos os Repositorios tem que ser colocados aqui:
	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GamesRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {
			// save a couple of customersp
			//O que fazemos aqui e guardar o novo player dentro da variavel e depois validar dentro
			//do repositorio.
			Player p1 = new Player("j.bauer@ctu.gov", "24");


			Player p2 = new Player("c.obrian@ctu.gov","42");


			Player p3 = new Player("kim_bauer@gmail.com","kb");

			//Aqui eu crio o novo player.
			Player p4 = new Player("t.almeida@ctu.gov","mole");
			//Aqui eu meto ele dentro do repositorio.




			Game g1 = new Game();
			Game g2 = new Game();
			Game g3 = new Game();
			Game g4 = new Game();
			Game g5 = new Game();
			Game g6 = new Game();
			Game g7 = new Game();
			Game g8 = new Game();



			List<String> list1 = Arrays.asList("H2", "H3", "H4");//O que 'e uma asList?
			List<String> list2 = Arrays.asList("E1", "F1", "G1");
			List<String> list3 = Arrays.asList("B4", "B5");
			List<String> list4 = Arrays.asList("B5", "B9");
			List<String> list5 = Arrays.asList("B5", "B9","B10", "B11");
			List<String> listgp2 = Arrays.asList("B5", "C5","D5");
			List<String> Patrogp2 = Arrays.asList("F1", "F2");
			//Game2
			List<String> JbDgp3 = Arrays.asList("B5", "C5", "D5");
			List<String> JbPBgp3 = Arrays.asList("C6", "C7");

			List<String> COSgp4 = Arrays.asList("A2", "A3", "A4");
			List<String> COPBgp4 = Arrays.asList("G6", "H6");



			Ship carrier = new Ship("Carrier", list1);
			Ship battleship = new Ship("Battleship", list4);
			Ship submarine = new Ship("Submarine",list2);
			Ship destroyer = new Ship("Destroyer", list5);
			Ship patrolBoat = new Ship("PatrolBoat", list3);
			Ship ronnie = new Ship("carrier ", list4);
			Ship destroyergp2 = new Ship("Destroyer", listgp2);
			Ship patrolBgp2 = new Ship("PatrolBoat", Patrogp2);
			//Game2
			Ship JBDestrogp3 = new Ship("Destroyer", JbDgp3);
			Ship JBBPatrolgp3 = new Ship("PatrolBoat", JbPBgp3);

			Ship CoSubgp4 = new Ship("Submarine",COSgp4);
			Ship CoPbGp4 = new Ship("PatrolBoat", COPBgp4);

			//Game 1:
			GamePlayer gp1 = new GamePlayer();
			p1.addGamePlayer(gp1);
			g1.addGamePlayer(gp1);
			gp1.addShip(ronnie);
			gp1.addShip(submarine);

			List<String> salvogp1 = Arrays.asList("E1", "C5", "F1");
			List<String> salvogp2 = Arrays.asList("B4", "B5", "B6");

			List<String> salvogp3 = Arrays.asList("F2", "D5");
			List<String> salvogp4 = Arrays.asList("E1", "H3", "A2");

			//Um salvo se faz assim?
			Salvo svg1 = new Salvo(1,salvogp1);
			Salvo svg2 = new Salvo(1,salvogp2);
			Salvo svg3 = new Salvo(2,salvogp3);
			Salvo svg4 = new Salvo(2,salvogp4);


			//Adicionando os Scores:
			Score score1 = new Score("07/04/2019",0.0);
			p1.addScore(score1);
			g1.addScore(score1);




			GamePlayer gp2 = new GamePlayer();
			p2.addGamePlayer(gp2);
			g1.addGamePlayer(gp2);
			gp2.addShip(destroyergp2);
			gp2.addShip(patrolBgp2);
			gp2.addSalvo(svg2);
			gp2.addSalvo(svg4);
			Score scoregp2 = new Score("07/04/2019",1.0);
			p2.addScore(scoregp2);
			g1.addScore(scoregp2);

			gp1.addSalvo(svg1);







			//Game2

			GamePlayer gp3 = new GamePlayer();
			p1.addGamePlayer(gp3);
			g2.addGamePlayer(gp3);
			gp3.addShip(JBDestrogp3);
			gp3.addShip(JBBPatrolgp3);
			gp3.addSalvo(svg3);



			GamePlayer gp4 = new GamePlayer();
			p2.addGamePlayer(gp4);
			g2.addGamePlayer(gp4);
			gp4.addShip(CoSubgp4);
			gp4.addShip(CoPbGp4);




			//Game3
			GamePlayer gp5 = new GamePlayer();
			p2.addGamePlayer(gp5);
			g3.addGamePlayer(gp5);

			GamePlayer gp6 = new GamePlayer();
			p4.addGamePlayer(gp6);
			g3.addGamePlayer(gp6);

			//Game4
			GamePlayer gp7 = new GamePlayer();
			p2.addGamePlayer(gp7);
			g4.addGamePlayer(gp7);

			GamePlayer gp8 = new GamePlayer();
			p1.addGamePlayer(gp8);
			g4.addGamePlayer(gp8);

			//Game5
			GamePlayer gp9 = new GamePlayer();
			p4.addGamePlayer(gp9);
			g5.addGamePlayer(gp9);

			GamePlayer gp10 = new GamePlayer();
			p1.addGamePlayer(gp10);
			g5.addGamePlayer(gp10);

			//Game6
			GamePlayer gp11 = new GamePlayer();
			p3.addGamePlayer(gp11);
			g6.addGamePlayer(gp11);

			//Game7
			GamePlayer gp12 = new GamePlayer();
			p4.addGamePlayer(gp12);
			g7.addGamePlayer(gp12);

			//Game8
			GamePlayer gp13 = new GamePlayer();
			p4.addGamePlayer(gp13);
			g8.addGamePlayer(gp13);

			GamePlayer gp14 = new GamePlayer();
			p3.addGamePlayer(gp14);
			g8.addGamePlayer(gp14);




			repository.save(p4);
			repository.save(p1);
			repository.save(p2);
			repository.save(p3);

			gameRepository.save(g1);
			gameRepository.save(g2);
			gameRepository.save(g3);
			gameRepository.save(g4);
			gameRepository.save(g5);
			gameRepository.save(g6);
			gameRepository.save(g7);
			gameRepository.save(g8);

			gamePlayerRepository.save(gp1);
			gamePlayerRepository.save(gp2);
			 gamePlayerRepository.save(gp3);
			gamePlayerRepository.save(gp4);
			gamePlayerRepository.save(gp5);
			gamePlayerRepository.save(gp6);
			gamePlayerRepository.save(gp7);
			gamePlayerRepository.save(gp8);
			gamePlayerRepository.save(gp9);
			gamePlayerRepository.save(gp10);
			gamePlayerRepository.save(gp11);
			gamePlayerRepository.save(gp12);
			gamePlayerRepository.save(gp13);

			/*
			shipRepository.save(carrier);
			shipRepository.save(submarine);
			shipRepository.save(destroyer);
			shipRepository.save(battleship);
			shipRepository.save(patrolBoat);
			shipRepository.save(ronnie);
			shipRepository.save(destroyergp2);
			shipRepository.save(patrolBgp2);
			//Game2:
			shipRepository.save(JBDestrogp3);
			shipRepository.save(JBBPatrolgp3);
			shipRepository.save(CoSubgp4);
			shipRepository.save(CoPbGp4);
			*/
			//Salvo
			/*
			salvoRepository.save(svg1);
			salvoRepository.save(svg2);
			salvoRepository.save(svg3);
			salvoRepository.save(svg4);
			scoreRepository.save(score1);
			scoreRepository.save(scoregp2);

			*/

















			//creando los barcos:








		};




	}





}


@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter{


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();

	}

	@Autowire
	PasswordEncoder passwordEncoder;

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName -> {

			Player player = playerRepository.findByUserName(inputName);
			System.out.println("Mi user es: " + inputName);
			System.out.println("Mi passss es: " + player.getPassword());
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});


	}
}

//Segunda classe

@Configuration
@EnableWebSecurity


class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers(
						"/web/games.html",
						"/web/script/games.js",
						"/api/leaderboard",
						"/api/games",
						"/api/game",
						"/api/login",
						"/api/logout",
						"/web/style/style.css",
						"/web/style/styleGame.css",
						"/favicon.ico",
						"/api/players"

				).permitAll()
				.anyRequest().hasAnyAuthority("USER");


		http.formLogin()
				.usernameParameter("user")
				.passwordParameter("pwd")
				.loginPage("/api/login")
				.permitAll();

		http.logout().logoutUrl("/api/logout");
		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

		}

	}


	@Component
	public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint {

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
							 AuthenticationException authException)
				throws IOException, ServletException {

			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}


}