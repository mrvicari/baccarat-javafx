import java.util.*;

public class Game
{
  // Fields required for a game
  private Shoe shoe;
  private Hand player = new Hand();
  private Hand banker = new Hand();
  private int roundCount = 1, playerWins, bankerWins, ties;

  /**
   * Create a Game object
   *
   *@param number of decks in the shoe
   */
  public Game(int numDecks)
  {
    shoe = new Shoe(numDecks);
  }

  /**
   * Play a single round of Baccarat Punto Banco
   */
  public void playRound()
  {
    // Hold the third card dealt to the player (if at all)
    Card playerThirdCard = null;
    // Shuffle the shoe and deal initial four cards
    shuffleDeal();
    // Check for a natural win
    if (!isNatural())
    {
      // If player does not stand, deal card
      if (player.value() < 6)
      {
        playerThirdCard = shoe.deal();
        player.add(playerThirdCard);
        System.out.println("  Dealing third card to player...");
        printStatus();
        // If player drew, check whether banker draws as well
        if (doesBankerDraw(playerThirdCard.value(), banker.value()))
        {
          banker.add(shoe.deal());
          System.out.println("  Dealing third card to banker...");
          printStatus();
        }
      }
      // If banker does not stand, deal card
      else if (banker.value() < 6)
      {
        banker.add(shoe.deal());
        System.out.println("  Dealing third card to banker...");
        printStatus();
      }
      // Compare scores to find winner
      decideWinner();
    }
    // Discard both hands
    player.discard();
    banker.discard();
    // Increase the round counter
    roundCount++;
  }
  /**
   * Play rounds until shoe is exhausted
   */
  public void play()
  {
    while (shoe.size() > 6)
      playRound();
    System.out.printf("\nRounds played: %d\nPlayer wins: %d\nBanker wins: %d\nTies: %d\n\n", roundCount-1, playerWins, bankerWins, ties);
  }

  /**
   * Play while asking user for another rounds
   */
  public void playWithPrompt()
  {
    // Scan for user input, start with 'y'
    Scanner input = new Scanner(System.in);
    char choice = 'y';
    // If user does not input 'n' and shoe still has enough cards
    while (choice != 'n' && shoe.size() > 6)
    {
      // Play a round and ask for input
      playRound();
      System.out.printf("Another Round? (y/n): ");
      choice = input.next().charAt(0);
    }
    // Print statistics when game is finished
    System.out.printf("\nRounds played: %d\nPlayer wins: %d\nBanker wins: %d\nTies: %d\n\n", roundCount-1, playerWins, bankerWins, ties);
  }

  /**
   * Shuffle the shoe and deal initial four cards
   */
  public void shuffleDeal()
  {
    System.out.printf("\nRound %d\n", roundCount);
    shoe.shuffle();
    player.add(shoe.deal());
    banker.add(shoe.deal());
    player.add(shoe.deal());
    banker.add(shoe.deal());
    printStatus();
  }

  /**
   * Check initial cards for a natural win
   *
   *@return True if natural win, False otherwise
   */
  public boolean isNatural()
  {
    // If either hand's score is 8 or 9, it is a natural win
    if (player.value() == 8 || player.value() == 9)
    {
      System.out.println("Player wins!");
      playerWins++;
      return true;
    }
    else if (banker.value() == 8 || banker.value() == 9)
    {
      System.out.println("Banker wins!");
      bankerWins++;
      return true;
    }
    else
      return false;
  }

  /**
   * Print the contents of both hands as strings
   */
  public void printStatus()
  {
    System.out.printf(" Player: %s = %d\n", player, player.value());
    System.out.printf(" Banker: %s = %d\n", banker, banker.value());
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
  public void decideWinner()
  {
    // Whoever is closest to 9 wins
    if ((9 - player.value()) < (9 - banker.value()))
    {
      System.out.println("Player wins!");
      playerWins++;
    }
    else if ((9 - banker.value()) < (9 - player.value()))
    {
      System.out.println("Banker wins!");
      bankerWins++;
    }
    // If equal scores, then it is a tie
    else
    {
      System.out.println("It's a tie!");
      ties++;
    }
  }

  public static void main(String[] args)
  {
    try
    {
      // Prompt user for number of decks to be used
      Scanner input = new Scanner(System.in);
      System.out.printf("\nNumber of decks: ");
      int numDecks = input.nextInt();

      // Create Game object
      Game game = new Game(numDecks);

      System.out.printf("\n\nPlay with prompt = 1\nPlay until shoe is exhausted = 2\n\nEnter choice: ");
      int gameChoice = input.nextInt();

      if (gameChoice == 1)
        // Play and prompt user after each round
        game.playWithPrompt();
      else if (gameChoice == 2)
        // Play rounds automatically until shoe is exhausted
        game.play();
      else
        System.out.printf("\nError: no such choice %d\n\n", gameChoice);
    }
    catch (IllegalArgumentException error)
    {
      System.out.printf("\nError: Number of decks must be 4, 6 or 8\n\n");
    }
  }
}
