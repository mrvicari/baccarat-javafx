import java.util.*;

public class Shoe extends CardCollection
{
  /**
   * Creates a Shoe object
   *
   *@param number of decks in the shoe
   *@throws IllegalArgumentException if number is not 4, 6 or 8
   */
  public Shoe (int numDecks)
  {
    if (numDecks != 4 && numDecks != 6 && numDecks != 8)
      throw new IllegalArgumentException("Number of decks must be 4, 6 or 8");
    for (int i = 0; i < numDecks; i++)
    {
      for (char rank: Card.getRanks())
      {
        for (char suit: Card.getSuits())
        {
          BaccaratCard currentCard = new BaccaratCard(rank, suit);
          this.add(currentCard);
        }
      }
    }
  }

  /**
   * Remove the top card from the shoe
   *
   *@return top card from the shoe
   */
  public Card deal()
  {
    Card topCard = cards.get(0);
    cards.remove(topCard);
    return topCard;
  }

  /**
   * Shuffle the cards in the shoe
   */
  public void shuffle()
  {
    Collections.shuffle(cards);
  }
}
