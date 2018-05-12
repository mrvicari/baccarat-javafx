import javafx.animation.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import javafx.geometry.*;
import javafx.event.*;

public class BaccaratFX extends Application
{
  // Buttons within the game
  private Button btThird = new Button("Third Card");
  private Button btNewRound = new Button("Another Round");
  private Button btEndGame = new Button("End Game");
  // Fields required for a game of Baccarat
  private Shoe shoe;
  private Hand player = new Hand();
  private Hand banker = new Hand();
  private int roundCount = 1, playerWins, bankerWins, ties;

  @Override
  public void start(Stage primaryStage)
  {
      // Initialise shoe and set the stage
      shoe = new Shoe(8);
      Group root = new Group();
      Scene scene = new Scene(root, 1280, 720, Color.DARKGREEN);
      primaryStage.setTitle("Baccarat");
      primaryStage.setScene(scene);
      primaryStage.show();

      // Show text to differentiate the two hands
      Text playerText = new Text(110, 670, "PLAYER");
      root.getChildren().add(playerText);
      playerText.setFont(Font.font("Arial", FontWeight.BOLD, 35));

      Text bankerText = new Text(1010, 670, "BANKER");
      root.getChildren().add(bankerText);
      bankerText.setFont(Font.font("Arial", FontWeight.BOLD, 35));

      // Add the buttons to the screen
      btNewRound.setLayoutX(590);
      btNewRound.setLayoutY(450);
      root.getChildren().add(btNewRound);

      btEndGame.setLayoutX(608);
      btEndGame.setLayoutY(490);
      root.getChildren().add(btEndGame);

      btThird.setLayoutX(608);
      btThird.setLayoutY(100);
      root.getChildren().add(btThird);
      // Make the 'Third card' button invisible initially
      btThird.setVisible(false);

      // Play the first round automatically
      playRound(root);

      // Click on the 'Another round' button
      btNewRound.setOnAction((ActionEvent e3) -> {
        // Discard both hands
        player.discard();
        banker.discard();

        int numNodes = (root.getChildren().size());
        // Check if the previous round has finished by adding the wins
        if ((roundCount - 1) == (playerWins + bankerWins + ties))
        {
          // Remove the cards from the screen
          for (int i = 0; i < (numNodes - 5); i++)
          {
            root.getChildren().remove(5);
          }
          // Play another round
          playRound(root);
        }
      });
    // Click on the 'End game' button
    btEndGame.setOnAction((ActionEvent e4) -> {
      // Print the game statistics
      endStats(root);
    });
  }

  /**
   * Shuffle the shoe and deal initial four cards
   */
  public void shuffleDeal(Group root)
  {
    shoe.shuffle();
    Card p1 = shoe.deal();
    player.add(p1);
    root.getChildren().add(new ImageView(p1.getImage()));
    root.getChildren().get(5).setLayoutX(604);
    root.getChildren().get(5).setLayoutY(-96);
    Card b1 = shoe.deal();
    banker.add(b1);
    root.getChildren().add(new ImageView(b1.getImage()));
    root.getChildren().get(6).setLayoutX(604);
    root.getChildren().get(6).setLayoutY(-96);
    Card p2 = shoe.deal();
    player.add(p2);
    root.getChildren().add(new ImageView(p2.getImage()));
    root.getChildren().get(7).setLayoutX(604);
    root.getChildren().get(7).setLayoutY(-96);
    Card b2 = shoe.deal();
    banker.add(b2);
    root.getChildren().add(new ImageView(b2.getImage()));
    root.getChildren().get(8).setLayoutX(604);
    root.getChildren().get(8).setLayoutY(-96);

    TranslateTransition translateP1 = new TranslateTransition(Duration.millis(1500), root.getChildren().get(5));
    translateP1.setToX(-554);
    translateP1.setToY(596);

    TranslateTransition translateP2 = new TranslateTransition(Duration.millis(1500), root.getChildren().get(7));
    translateP2.setToX(-454);
    translateP2.setToY(596);

    TranslateTransition translateB1 = new TranslateTransition(Duration.millis(1500), root.getChildren().get(6));
    translateB1.setToX(554);
    translateB1.setToY(596);

    TranslateTransition translateB2 = new TranslateTransition(Duration.millis(1500), root.getChildren().get(8));
    translateB2.setToX(454);
    translateB2.setToY(596);

    SequentialTransition transitionP1 = new SequentialTransition(translateP1);
    transitionP1.play();

    SequentialTransition transitionB1 = new SequentialTransition(new PauseTransition(Duration.millis(1000)), translateB1);
    transitionB1.play();

    SequentialTransition transitionP2 = new SequentialTransition(new PauseTransition(Duration.millis(2000)), translateP2);
    transitionP2.play();

    SequentialTransition transitionB2 = new SequentialTransition(new PauseTransition(Duration.millis(3000)), translateB2);
    transitionB2.play();
  }


  /**
   * Check initial cards for a natural win
   *
   *@return True if natural win, False otherwise
   */
  public boolean isNatural(Group root)
  {
    // If either hand's score is 8 or 9, it is a natural win
    if (player.value() == 8 || player.value() == 9)
    {
      // Wait for 5 seconds before showing winner
      PauseTransition pause1 = new PauseTransition(Duration.millis(5000));
      pause1.setOnFinished((e5) -> {
        Text playerWin = new Text(460, 310, "PLAYER WINS");
        root.getChildren().add(playerWin);
        playerWin.setFont(Font.font("Arial", FontWeight.BOLD, 50));

        playerWins++;
      });
      pause1.play();

      return true;
    }
    else if (banker.value() == 8 || banker.value() == 9)
    {
      // Wait for 5 seconds before showing winner
      PauseTransition pause2 = new PauseTransition(Duration.millis(5000));
      pause2.setOnFinished((e6) -> {
        Text bankerWin = new Text(460, 310, "BANKER WINS");
        root.getChildren().add(bankerWin);
        bankerWin.setFont(Font.font("Arial", FontWeight.BOLD, 50));

        bankerWins++;
      });
      pause2.play();

      return true;
    }
    else
      return false;
  }

  /**
   * Decide whether banker draws a third card based on Tableau of Drawing rules
   *
   *@return True if banker must draw, False otherwise
   */
  public boolean doesBankerDraw(int playerThirdCardValue, int bankerHandValue)
  {
    if (((playerThirdCardValue == 2 || playerThirdCardValue == 3) && bankerHandValue <= 4)
        || ((playerThirdCardValue == 4 || playerThirdCardValue == 5) && bankerHandValue <= 5)
        || ((playerThirdCardValue == 6 || playerThirdCardValue == 7) && bankerHandValue <= 6)
        || (playerThirdCardValue == 8 && bankerHandValue <= 2)
        || ((playerThirdCardValue == 1 || playerThirdCardValue == 9 || playerThirdCardValue == 0) && bankerHandValue <= 3))
      return true;
    else
      return false;
  }

  /**
   * Compare scores to find winner
   */
  public void decideWinner(Group root)
  {
    // Whoever is closest to 9 wins
    if ((9 - player.value()) < (9 - banker.value()))
    {
      // Wait for 3 seconds before showing winner
      PauseTransition pause3 = new PauseTransition(Duration.millis(3000));
      pause3.setOnFinished((e7) -> {
        Text playerWin = new Text(460, 310, "PLAYER WINS");
        root.getChildren().add(playerWin);
        playerWin.setFont(Font.font("Arial", FontWeight.BOLD, 50));

        playerWins++;
      });
      pause3.play();
    }
    else if ((9 - banker.value()) < (9 - player.value()))
    {
      // Wait for 3 seconds before showing winner
      PauseTransition pause4 = new PauseTransition(Duration.millis(3000));
      pause4.setOnFinished((e8) -> {
        Text bankerWin = new Text(460, 310, "BANKER WINS");
        root.getChildren().add(bankerWin);
        bankerWin.setFont(Font.font("Arial", FontWeight.BOLD, 50));

        bankerWins++;
      });
      pause4.play();
    }
    // If equal scores, then it is a tie
    else
    {
      // Wait for 3 seconds before showing winner
      PauseTransition pause5 = new PauseTransition(Duration.millis(3000));
      pause5.setOnFinished((e9) -> {
        Text tieWin = new Text(600, 310, "TIE");
        root.getChildren().add(tieWin);
        tieWin.setFont(Font.font("Arial", FontWeight.BOLD, 50));

        ties++;
      });
      pause5.play();
    }
  }

  /**
   * Check hand values and decide whether or not to deal a third card
   */
  public void thirdCards(Group root)
  {
    // If player does not stand, deal card
    if (player.value() < 6)
    {
      Card p3 = shoe.deal();
      player.add(p3);
      root.getChildren().add(new ImageView(p3.getImage()));
      root.getChildren().get(9).setLayoutX(604);
      root.getChildren().get(9).setLayoutY(-96);

      TranslateTransition translateP3 = new TranslateTransition(Duration.millis(1500), root.getChildren().get(9));
      translateP3.setToX(-354);
      translateP3.setToY(596);

      SequentialTransition transitionP3 = new SequentialTransition(translateP3);
      transitionP3.play();

      // If player drew, check whether banker draws as well
      if (doesBankerDraw(p3.value(), banker.value()))
      {
        Card b3 = shoe.deal();
        banker.add(b3);
        root.getChildren().add(new ImageView(b3.getImage()));
        root.getChildren().get(10).setLayoutX(604);
        root.getChildren().get(10).setLayoutY(-96);

        TranslateTransition translateB3 = new TranslateTransition(Duration.millis(1500), root.getChildren().get(10));
        translateB3.setToX(354);
        translateB3.setToY(596);

        SequentialTransition transitionB3 = new SequentialTransition(new PauseTransition(Duration.millis(1000)), translateB3);
        transitionB3.play();
      }
    }
    // If banker does not stand, deal card
    else if (banker.value() < 6)
    {
      Card b3 = shoe.deal();
      banker.add(b3);
      root.getChildren().add(new ImageView(b3.getImage()));
      root.getChildren().get(9).setLayoutX(604);
      root.getChildren().get(9).setLayoutY(-96);

      TranslateTransition translateB3 = new TranslateTransition(Duration.millis(1500), root.getChildren().get(9));
      translateB3.setToX(354);
      translateB3.setToY(596);

      SequentialTransition transitionB3 = new SequentialTransition(new PauseTransition(Duration.millis(1000)), translateB3);
      transitionB3.play();
    }
  }

  /**
   * Play a single round of Baccarat Punto Banco
   */
  public void playRound(Group root)
  {
    // Check if there are enough cards left in the shoe
    if (shoe.size() < 6)
    {
      endStats(root);
      return;
    }
    // Shuffle the shoe and deal initial four cards
    shuffleDeal(root);

    if (!isNatural(root))
    {
      // Wait for 5 seconds before showing button
      PauseTransition pause6 = new PauseTransition(Duration.millis(5000));
      pause6.setOnFinished((e10) -> {
        // Make the 'Third card' button visible
        btThird.setVisible(true);
      });
      pause6.play();

      // Click on the 'Third card' button
      btThird.setOnAction((ActionEvent e2) -> {
        // Make the 'Third card' button invisible
        btThird.setVisible(false);

        // Check hand values and decide whether or not to deal a third card
        thirdCards(root);
        // Compare scores to find winner
        decideWinner(root);
      });
    }
    // Increment the round counter
    roundCount++;
  }

  /**
   * Show game statistics on screen
   */
  public void endStats(Group root)
  {
    int numNodes = (root.getChildren().size());
    // Remove everything from the screen
    for (int i = 0; i < (numNodes); i++)
    {
      root.getChildren().remove(0);
    }

    Text gameStats = new Text(405, 150, "GAME STATISTICS");
    root.getChildren().add(gameStats);
    gameStats.setFont(Font.font("Arial", FontWeight.BOLD, 50));

    Text roundsPlayed = new Text(500, 250, "Rounds played: " + (roundCount - 1));
    root.getChildren().add(roundsPlayed);
    roundsPlayed.setFont(Font.font("Arial", FontWeight.BOLD, 35));

    Text playerWon = new Text(500, 325, "Player wins: " + playerWins);
    root.getChildren().add(playerWon);
    playerWon.setFont(Font.font("Arial", FontWeight.BOLD, 35));

    Text bankerWon = new Text(500, 400, "Banker wins: " + bankerWins);
    root.getChildren().add(bankerWon);
    bankerWon.setFont(Font.font("Arial", FontWeight.BOLD, 35));

    Text tieWon = new Text(500, 475, "Ties: " + ties);
    root.getChildren().add(tieWon);
    tieWon.setFont(Font.font("Arial", FontWeight.BOLD, 35));
  }
}
