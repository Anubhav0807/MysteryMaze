# Mystery Maze
Welcome to the exciting world of "Mystery Maze," a puzzle-adventure game that challenges players with tricky mazes, strategic thinking, and problem-solving. In "Mystery Maze," players explore a labyrinth full of hidden treasures, valuable coins, and clues to solve a final mystery. Adding to the thrill, a roaming AI enemy sets traps and chases the player. Players must use bombs to break certain walls or defeat the AI, while also collecting coins to increase their score. "Mystery Maze" combines exploration, strategy, and quick reflexes, offering a thrilling gaming experience.

This game is made purely in Java and is a submission to Game-A-Thon in IIT Madras BS in partnership with GMonks.

## Running the Game
> **Note:** The following commands are for Windows PowerShell.

1. **Compile**
    ```powershell
    javac -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
    ```
    Compiles all `.java` files from `src` and places the compiled `.class` files in `bin`.

2. **Run**
    ```powershell
    java -cp "bin;res" main.MainClass
    ```
    Runs the `main.MainClass` using the compiled files in `bin` and game resources from `res`.