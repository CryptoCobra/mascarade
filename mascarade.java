public class mascarade
{
    public static void main(String args[])
    {
        game();
    }

    static void game()
    {
        System.out.println("Welcome to mascarade!");
        player player1 = new player();
        player player2 = new player();
        player player3 = new player();
        player player4 = new player();
        player currentPlayer = new player();
        tribunal court = new tribunal();
        victory vic = new victory();
        Log l = new Log();
        int switchCounter = 0;
        int[] charList = characters.generate();
        characters.assign(charList, player1, player2, player3, player4);
        Pn.Number(player1, player2, player3, player4);
        setupLog(l);
        gameplay(switchCounter, currentPlayer, player1, player2, player3, player4, court, vic, l);
    }

    static void gameInterface(player current, player p1, player p2, player p3, player p4, tribunal court, char dev, Log log)
    {
        System.out.println("====================");
        System.out.println("Player " + current.number);
        System.out.println("====================");
        System.out.println("Wealth : " + p1.coins + "/" + p2.coins + "/" + p3.coins + "/" + p4.coins);
        System.out.println("Tribunal : " + court.coins);
        System.out.println("====================");
        System.out.println("Summary of last 3 actions");
        System.out.println("-----" + log.playerAction[2]);
        System.out.println("-----" + log.playerAction[1]);
        System.out.println("-----" + log.playerAction[0]);
        System.out.println("====================");
        if(turns<4)
        {
            System.out.println("Player role: " + current.role);
        }
        if(dev == 'o')
        {
            System.out.println("Player roles in order: " + p1.role + " " + p2.role + " " + p3.role + " " + p4.role);
        }
    }

    static void gameplay(int switchCounter, player current, player p1, player p2, player p3, player p4, tribunal court, victory vic, Log l)
    {
        char devMode = developerMode();
        do
        {
            current = currentplayer(switchCounter, current, p1, p2, p3, p4);
            gameInterface(current, p1, p2, p3, p4, court, devMode, l);
            playerAction(current, court, p1, p2, p3, p4, vic, l);
            switchCounter = goToNextPlayer(switchCounter);
            vic.isVictory(p1, p2, p3, p4, vic);
            turns++;
        } while(!vic.victory);
        determineWinner(p1, p2, p3, p4);
    }

    static void playerAction(player current, tribunal court, player p1, player p2, player p3, player p4, victory vic, Log log)
    {
        if(turns<4)
        {
            actions.switchRole(current, p1, p2, p3, p4);
        }
        else
        {
            System.out.println("Would you like to look at your card(R), switch roles(E), or use your power(U)?");
            char chooseAction;
            do
            {
                chooseAction = Lire.c();
                if(chooseAction != 'R' && chooseAction != 'E' && chooseAction != 'U')
                {
                    System.out.println("This command is not recognized");
                }
            } while(chooseAction != 'R' && chooseAction != 'E' && chooseAction != 'U');
            if(chooseAction == 'R')
            {
                actions.look(current);
            }
            if(chooseAction == 'E')
            {
                actions.switchRole(current, p1, p2, p3, p4);
            }
            if(chooseAction == 'U')
            {
                actions.usePower(current, court, p1, p2, p3, p4, vic);
            }
        }
        log.fillLogger();
    }

    static int goToNextPlayer(int switchCounter)
    {
        switchCounter = nextPlayer(switchCounter);
        if(switchCounter>3)
        {
            switchCounter = 0;
        }
        return switchCounter;
    }

    static int nextPlayer(int switchCount)
    {
        System.out.println("Click enter to switch to next player");
        boolean enterKeyPressed = false;
        do
        {
            char enterKey = Lire.c();
            if(enterKey == '\n')
            {
                switchCount++;
                enterKeyPressed = true;
            }
        } while(enterKeyPressed == false);
        return switchCount;
    }

    static player currentplayer(int numofswitch, player cP, player p1, player p2, player p3, player p4)
    {
        cP = changePlayer(0, numofswitch, cP, p1);
        cP = changePlayer(1, numofswitch, cP, p2);
        cP = changePlayer(2, numofswitch, cP, p3);
        cP = changePlayer(3, numofswitch, cP, p4);
        return cP;
    }

    static player changePlayer(int i, int numofswitch, player current, player Player)
    {
        if(numofswitch == i)
        {
            current = Player;
        }
        return current;
    }

    static char developerMode()
    {
        System.out.println("Would you like to turn on developer mode(o/n): ");
        char devSwitch;
        do
        {
            devSwitch = Lire.c();
            if(devSwitch != 'o' && devSwitch != 'n')
            {
                System.out.println("This command is not recognized");
            }
        } while(devSwitch != 'o' && devSwitch != 'n');
        return devSwitch;
    }

    static void setupLog(Log l)
    {
        for(int i=0; i<3; i++)
        {
            l.fillLogger();
        }
    }

    static void determineWinner(player p1, player p2, player p3, player p4)
    {
        int maxCoins = p1.coins;
        int winner = p1.number;
        winner = findWinner(p1, maxCoins, winner);
        winner = findWinner(p2, maxCoins, winner);
        winner = findWinner(p3, maxCoins, winner);
        winner = findWinner(p4, maxCoins, winner);
        System.out.println("Player " + winner + " has won!");
    }

    static int findWinner(player Player, int maxCoins, int winner)
    {
        if(Player.coins>maxCoins)
        {
            maxCoins = Player.coins;
            winner = Player.number;
        }
        return winner;
    }

    static String logEntry = "-";
    static int turns = 0;
}

class Log
{
    protected String[] playerAction = new String[3];


    protected void fillLogger()
    {
        for(int i=2; i>=1; i--)
        {
            playerAction[i] = playerAction[i-1];
        }
        playerAction[0] = mascarade.logEntry;
    }
}

class tribunal
{
    protected int coins;
}

class victory
{
    protected boolean victory = false;

    protected void isVictory(player p1, player p2, player p3, player p4, victory victory)
    {
        victoryCheck(p1, victory);
        victoryCheck(p2, victory);
        victoryCheck(p3, victory);
        victoryCheck(p4, victory);
    }

    static void victoryCheck(player Player, victory victory)
    {
        if(Player.coins >= 13 || Player.coins <= 0)
        {
            victory.victory = true;
        }
    }
}

class player
{
    protected int coins = 6;
    protected String role;
    protected int number;
}

class Pn
{
    static void Number(player p1, player p2, player p3, player p4)
    {
        p1.number = 1;
        p2.number = 2;
        p3.number = 3;
        p4.number = 4;
    }
}

class actions
{
    static void look(player Player)
    {
        System.out.println("Your role is: " + Player.role);
        mascarade.logEntry = "Player " + Player.number + " has looked at their role";
    }

    static void switchRole(player current, player p1, player p2, player p3, player p4)
    {
        System.out.println("Please choose a player to switch roles with: ");
        int playerSwitch;
        do
        {
            playerSwitch = Lire.i();
            if(playerSwitch == current.number)
            {
                System.out.println("Must be a player other than yourself: ");
            }
        } while(playerSwitch == current.number);
        mascarade.logEntry = "Player " + current.number + " has maybe switched with player " + playerSwitch;
        System.out.println("Are you sure you would like to change your role? (o/n)");
        char decision;
        do
        {
            decision = Lire.c();
            if(decision != 'o' && decision != 'n')
            {
                System.out.println("This command is not recognized");
            }
        } while(decision != 'o' && decision != 'n');
        if(decision == 'o')
        {
            switchPlayer(playerSwitch, 1, current, p1);
            switchPlayer(playerSwitch, 2, current, p2);
            switchPlayer(playerSwitch, 3, current, p3);
            switchPlayer(playerSwitch, 4, current, p4);
        }
        if(decision == 'n')
        {
            System.out.println("You haven't changed the role");
        }
    }

    static void switchPlayer(int playerSwitch, int i, player current, player switchedPlayer)
    {
        String keep = "";
        if(playerSwitch == i)
        {
            keep = switchedPlayer.role;
            switchedPlayer.role = current.role;
            current.role = keep;
        }
    }

    static void usePower(player Player, tribunal court, player p1, player p2, player p3, player p4, victory vic)
    {
        System.out.print("Please guess your role: ");
        String guess = Lire.S();
        if(guess.equals(Player.role))
        {
            System.out.println("Yes, this is indeed your role");
            rolePower(Player, court, p1, p2, p3, p4, vic);
            mascarade.logEntry = "Player " + Player.number + " has used their power: " + Player.role;
        }
        else
        {
            System.out.println("Incorrect guess, you were " + Player.role);
            Player.coins--;
            court.coins++;
            mascarade.logEntry = "Player " + Player.number + " thought their role was " + guess + " but their role was " + Player.role;
        }
    }

    static void rolePower(player Player, tribunal court, player p1, player p2, player p3, player p4, victory vic)
    {
        if(Player.role == "King")
        {
            powers.king(Player);
        }
        if(Player.role == "Queen")
        {
            powers.queen(Player);
        }
        if(Player.role == "Judge")
        {
            powers.judge(Player, court);
        }
        if(Player.role == "Bishop")
        {
            powers.bishop(Player, p1, p2, p3, p4);
        }
        if(Player.role == "Thief")
        {
            powers.thief(Player, p1, p2, p3, p4);
        }
        if(Player.role == "Cheater")
        {
            powers.cheater(Player, vic);
        }
    }
}

class characters
{
    static int[] generate()
    {
        int options[] = new int[4];
        int choice;
        for(int i=0; i<4; i++)
        {
            do
            {
                choice = (int)Math.floor(1+Math.random()*6);
            } while(alreadyChosen(choice, options));
            options[i] = choice;
        }
        return options;
    }

    static boolean alreadyChosen(int choice, int[] options)
    {
        for(int i=0; i<4;i++)
        {
            if(choice == options[i])
            {
                return true;
            }
        }
        return false;
    }

    static void assign(int[] charList, player player1, player player2, player player3, player player4)
    {
        select(0, charList, player1);
        select(1, charList, player2);
        select(2, charList, player3);
        select(3, charList, player4);
    }

    static void select(int i, int[] generatedList, player Player)
    {
        if(generatedList[i] == 1)
        {
            Player.role = "King";
        }
        if(generatedList[i] == 2)
        {
            Player.role = "Queen";
        }
        if(generatedList[i] == 3)
        {
            Player.role = "Judge";
        }
        if(generatedList[i] == 4)
        {
            Player.role = "Bishop";
        }
        if(generatedList[i] == 5)
        {
            Player.role = "Thief";
        }
        if(generatedList[i] == 6)
        {
            Player.role = "Cheater";
        }
    }
}

class powers
{
    static void king(player Player)
    {
        Player.coins+=3;
    }

    static void queen(player Player)
    {
        Player.coins+=2;
    }

    static void judge(player Player, tribunal court)
    {
        Player.coins+= court.coins;
        court.coins = 0;
    }

    static void bishop(player Player, player p1, player p2, player p3, player p4)
    {
        int max = Player.coins;
        int maxPlayer = Player.number;
        maxPlayer = bishopCheck(p1, max, maxPlayer);
        maxPlayer = bishopCheck(p2, max, maxPlayer);
        maxPlayer = bishopCheck(p3, max, maxPlayer);
        maxPlayer = bishopCheck(p4, max, maxPlayer);
        bishopPower(Player, maxPlayer, p1, p2, p3, p4);
    }

    static int bishopCheck(player Player, int max, int maxPlayer)
    {
        if(Player.coins>max)
        {
            max = Player.coins;
            maxPlayer = Player.number;
        }
        return maxPlayer;
    }
    
    static player bishopPower(player Player, int maxPlayer, player p1, player p2, player p3, player p4)
    {
        if(maxPlayer == 1 && Player != p1)
        {
            p1.coins-=2;
            Player.coins+=2;
        }
        if(maxPlayer == 2 && Player != p2)
        {
            p2.coins-=2;
            Player.coins+=2;
        }
        if(maxPlayer == 3 && Player != p3)
        {
            p3.coins-=2;
            Player.coins+=2;
        }
        if(maxPlayer == 4 && Player != p4)
        {
            p4.coins-=2;
            Player.coins+=2;
        }
        return Player;
    }

    static void thief(player Player, player p1, player p2, player p3, player p4)
    {
        int getLeft = Player.number-1;
        if(getLeft == 0)
        {
            getLeft = 4;
        }
        int getRight = Player.number+1;
        if(getRight == 5)
        {
            getRight = 1;
        }
        thiefPower(Player, p1, p2, p3, p4, getLeft);
        thiefPower(Player, p1, p2, p3, p4, getRight);
    }
    
    static void thiefPower(player Player, player p1, player p2, player p3, player p4, int getValue)
    {
        if(getValue == 1)
        {
            p1.coins-=1;
            Player.coins+=1;
        }
        if(getValue == 2)
        {
            p2.coins-=1;
            Player.coins+=1;
        }
        if(getValue == 3)
        {
            p3.coins-=1;
            Player.coins+=1;
        }
        if(getValue == 4)
        {
            p4.coins-=1;
            Player.coins+=1;
        }
    }

    static void cheater(player Player, victory vic)
    {
        if(Player.coins>=10)
        {
            vic.victory = true;
        }
    }
}