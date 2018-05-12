import java.util.*;

public class BaccaratCard extends Card
{
  /**
   * Creates a BaccaratCard object.
   *
   * @param rank Rank of the card
   * @param suit Suit of the card
   * @throws IllegalArgumentException if rank or suit are invalid
   */
  public BaccaratCard(char rank, char suit)
  {
    super(rank, suit);
  }

  /**
   * Creates a BaccaratCard object, given rank and suit as a string.
   *
   * @param code Two-character code representing the card - e.g., "AC"
   * @throws IllegalArgumentException if string is invalid
   */
  public BaccaratCard(String code)
  {
    super(code);
  }

  /**
   * Computes the value of this card.
   *
   * <p>Value is based on rank and disregards suit. Aces score 1,
   * 10's and face cards all score 0.</p>
   *
   * @return Card value
   */
  public int value()
  {
    int indexOfRank = (Card.getRanks()).indexOf(this.getRank());

    if (indexOfRank < 9)
      return (indexOfRank + 1);
    else
      return 0;
  }
}
