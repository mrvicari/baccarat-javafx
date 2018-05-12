public class Hand extends CardCollection
{
  /**
   * Creates a two-character string representation of each card in this hand.
   *
   * <p>The first character represents rank, the second represents suit.</p>
   *
   * @return String representation of this hand
   */
  public String toString()
  {
    String handString = "";
    for (int i = 0; i < cards.size(); i++)
    {
      handString += cards.get(i) + " ";
    }
    return handString;
  }

  /**
   * Computes the total value of this hand.
   *
   * <p>Value is based on rank and disregards suit. Aces score 1,
   * 10's and face cards all score 0.</p>
   *
   * @return Hand value
   */
  public int value()
  {
    int handValue = 0;
    for (int i = 0; i < cards.size(); i++)
    {
      handValue += (cards.get(i)).value();
    }
    return handValue%10;
  }
}
