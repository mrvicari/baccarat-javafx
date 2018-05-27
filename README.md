# Baccarat JavaFX
JavaFX implementation of the card game Baccarat Punto Banco.

## Rules of the game
The Player plays against the Banker, where each participant is given an initial two cards. These two cards' values are added together with the following card values:
* Aces count as "1"
* Tens, Jacks, Queens and Kings count as "0"
* All other cards, 2-9, count as their pip value, or 2-9
If the total valuse of the hand reaches double digits, the first digits is then dropped to obtain a total value between 0 and 9.

If neither the Player nor the Baker are dealt a toatl of 8 or 9, then the tableau of drawing is consulted.

### Tableau of drawing

#### Player's rule
If Player has an initial total of 0–5, he draws a third card. If Player has an initial total of 6 or 7, he stands.

#### Banker's rule
If Player stood pat (i.e., has only two cards), the banker regards only his own hand and acts according to the same rule as Player. That means Banker draws a third card with hands 0–5 and stands with 6 or 7.

If Player drew a third card, the Banker acts according to the following more complex rules:

* If Player drew a 2 or 3, Banker draws with 0–4 and stands with 5–7.
* If Player drew a 4 or 5, Banker draws with 0–5 and stands with 6–7.
* If Player drew a 6 or 7, Banker draws with 0–6 and stands with 7.
* If Player drew an 8, Banker draws with 0–2 and stands with 3–7.
* If Player drew an ace, 9, 10, or face-card, the Banker draws with 0–3 and stands with 4–7.

## Execution

Compile all source code files with `javac *.java`  
To run the command-line application, run `java Game`  
To run the GUI JavaFX application, run `java BaccaratFX`
