# <p align="center">Vier-Gewinnt-README</p>

<p align="center"> Jerome Habanz, 5321331</p>

## Vier-Gewinnt

Vier-Gewinnt ist ein Zweipersonen-Strategiespiel, was auf einem sieben mal sechs Spielfeld gespielt wird. Jeder Spieler
erhält 21 Spielchips in Rot oder Gelb. Die Spielsteine werden von oben in eine freie Spalte im Spielfeld eingesteckt und
fallen dann nach unten. Ziel ist es, vier Spielsteine so anzuordnen, dass eine Linie entsteht.

<img src="https://miro.medium.com/max/640/0*HqTdkytsHijhlRsd.gif">

## Beschreibung des Algorithmus

Der MiniMax-Algorithmus gibt eine Bewertung für mögliche Züge in der Zukunft zurück. Diese Bewertung basiert auf dem
Monte Carlo Tree Search.

### MiniMax Baum

In einem Spiel treten 2 Spieler gegeneinander an und versuchen immer den besten Zug zu erreichen.

* Der minimierende Spieler wird immer den Pfad mit der niedrigsten Bewertung gehen
* Der maximierende Spieler wird immer den Pfad mit der höchsten Bewertung gehen

Basierend auf dem Wechsel der Spieler mit dessen besten Zug, kann man bewerten, welcher Zug der Beste ist.

Beispiel:

#### 2 Spieler mit maximal zwei Möglichkeiten

<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Minimax.svg/640px-Minimax.svg.png">

#### 2 Spieler mit maximal 7 Möglichkeiten

<img src="https://miro.medium.com/max/1236/1*IyyCmHRYJpVhkU7SWyuF5Q.png">

### MCTS

Für die Bewertungsfunktion des Algorithmus kommt die Monte Carlo Tree Search Methode zum Einsatz. Die Bewertungsfunktion
spielt vom ausgehenden Spielstand so lange zufällige Züge bis das Spiel vorbei ist.

Lässt man diese Bewertungsfunktion oft genug spielen lässt, dann kann man aus dem Verhältnis zwischen WIN, LOOSE, TIE
berechnen, wie hoch die Wahrscheinlichkeit ist zu gewinnen

<img src="https://i.ibb.co/JzS53fK/Screenshot-2022-07-11-221238.png">

random_game(c):

* +1, wenn Gewinner
* -1, wenn Verlierer
* 0, wenn Unentschieden

## Erklärung des Logging Protokolls

Das Logging Protokoll basiert auf Log4J. Es wird ein Logger erstellt, der die Log-Meldungen in einer Datei speichert und
in der Konsole ausgibt.
Das Logging Protokoll ist in der Lage, anzuzeigen welche Schritte der Algorithmus in welcher Zeit durchgeführt hat.

Die Ausgabe zeigt, dass der Bot den Zug gemacht hat, der am höchsten bewertet wurde.

<img src="https://i.ibb.co/nmJ5c5Z/image.png">

### Quellen

* https://ssaurel.medium.com/creating-a-connect-four-game-in-java-f45356f1d6ba
* https://codereview.stackexchange.com/questions/100917/connect-four-game-in-java
* https://github.com/brendanator/connect4
* https://github.com/denkspuren/BitboardC4/blob/master/BitboardDesign.md
* https://stackoverflow.com/questions/571960/disabling-log4j-output-in-java
* https://stackoverflow.com/questions/42338187/log-file-not-generating-in-log4j
* https://www.tutorialspoint.com/log4j/log4j_logging_files.htm
* https://www.javatpoint.com/log4j-example
* https://docs.google.com/presentation/d/11cWA5c-Vvmop2DpBALqzHHdndSAyBuuiyfv7tLyvgFk/edit#slide=id.g131c9cb927_0_110
* https://www.baeldung.com/java-util-concurrent
* https://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
* https://www.vogella.com/tutorials/JavaConcurrency/article.html#:~:text=Concurrency%20is%20the%20ability%20to,the%20interactivity%20of%20the%20program
  .
* https://docs.oracle.com/javase/tutorial/essential/concurrency/collections.html
* https://www.youtube.com/watch?v=CjldSexfOuU
* https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html
* https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
* https://www.baeldung.com/java-bitwise-operators#:~:text=Bitwise%20operators%20work%20on%20a,and%20the%20result%20is%20calculated
* https://en.wikipedia.org/wiki/Minimax
* https://en.wikipedia.org/wiki/Negamax
* https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning