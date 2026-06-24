import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class App {
    static HashMap<String, PlayerStats> players;
    static ArrayList<GameData> games;
    static         double maxPoints=160;
    static final double homeCourtAdvantage=2.5;
    public static void main(String[] args) throws Exception {
        String dataDirectory = args.length > 0 ? args[0] : "BasketballData";
        initialize(dataDirectory);
        if (args.length == 3) {
            double[] prediction = predictTeams(args[1], args[2]);
            System.out.printf("%s %.0f - %s %.0f%n", args[1], prediction[0], args[2], prediction[1]);
        }
    }

    public static void initialize(String dataDirectory) throws IOException {
        players = loadPlayers(dataDirectory + "/NBAstats.csv", dataDirectory + "/BasicStats.csv");
        games = loadGamesWithPlayers(dataDirectory + "/allgames.csv", players);
        LoadNetwork(dataDirectory + "/weights.csv");
    }
    public static void FullGamePrediction(){
        Network network = new Network(400, new int[]{160,80,40}, 2, 0.0001);
        double maxPoints=160;
        for(int i = 0; i < 300; i++) {
            for(int j = 0; j < games.size()-200; j++) {
                GameData game = games.get(j);
                double[] data = game.data;
                double[] expected = new double[2];
                expected[0] = game.homePoints/maxPoints;
                expected[1] = game.awayPoints/maxPoints;
                network.GetOutputs(data);
                network.UpdateWeights(expected);
            }
        }
        int correct = 0;
        int homeWins = 0;
        int homePointsError = 0;
        int awayPointsError = 0;
        for(int i = games.size()-200; i < games.size(); i++) {
            GameData game = games.get(i);
            double[] data = game.data;
            double[] output = network.GetOutputs(data);
                if((output[0] > output[1] && game.homePoints > game.awayPoints) ||
                (output[1] > output[0] && game.awayPoints > game.homePoints)) {
                    correct++;
                }
                if(game.homePoints > game.awayPoints) {
                    homeWins++;
                }
                if((int)(output[0]*maxPoints)==(int)(output[1]*maxPoints)) {
                    if(output[0] > output[1]) {
                        output[0] += 1/maxPoints;
                    }
                    else {
                        output[1] += 1/maxPoints;
                    }
                }
                homePointsError += Math.abs(game.homePoints - (int)(output[0]*maxPoints));
                awayPointsError += Math.abs(game.awayPoints - (int)(output[1]*maxPoints));
        }  
        System.out.println("Home team win percentage: " + (homeWins/200.0)*100 + "%");
        System.out.println("Accuracy: " + (correct/200.0)*100 + "%");
        System.out.println("Home points error: " + homePointsError/200.0);
        System.out.println("Away points error: " + awayPointsError/200.0);
    }
    public static void OneSidePrediction(){
        Network network = new Network(401, new int[]{160,80,40}, 1, 0.0001);
        for(int i = 0; i < 300; i++) {
            for(int j = 0; j < games.size()-200; j++) {
                GameData game = games.get(j);
                double[] homedata = game.homedata;
                double[] awaydata = game.awaydata;
                double[] expectedhome = new double[1];
                double[] expectedaway = new double[1];
                expectedhome[0] = game.homePoints/maxPoints;
                expectedaway[0] = game.awayPoints/maxPoints;
                network.GetOutputs(homedata);
                network.UpdateWeights(expectedhome);
                network.GetOutputs(awaydata);
                network.UpdateWeights(expectedaway);
            }
        }
        int correct = 0;
        int homeWins = 0;
        int homePointsError = 0;
        int awayPointsError = 0;
        for(int i = games.size()-200; i < games.size(); i++) {
            GameData game = games.get(i);
            double[] homedata = game.homedata;
            double[] awaydata = game.awaydata;
            double homescore = network.GetOutputs(homedata)[0];
            double awayscore = network.GetOutputs(awaydata)[0];
                if((homescore > awayscore && game.homePoints > game.awayPoints) ||
                (awayscore > homescore && game.awayPoints > game.homePoints)) {
                    correct++;
                }
                if(game.homePoints > game.awayPoints) {
                    homeWins++;
                }
                if((int)(homescore*maxPoints)==(int)(awayscore*maxPoints)) {
                    if(homescore > awayscore) {
                        homescore += 1/maxPoints;
                    }
                    else {
                        awayscore += 1/maxPoints;
                    }
                }
                homePointsError += Math.abs(game.homePoints - (int)(homescore*maxPoints));
                awayPointsError += Math.abs(game.awayPoints - (int)(awayscore*maxPoints));
        }  
        System.out.println("Home team win percentage: " + (homeWins/200.0)*100 + "%");
        System.out.println("Accuracy: " + (correct/200.0)*100 + "%");
        System.out.println("Home points error: " + homePointsError/200.0);
        System.out.println("Away points error: " + awayPointsError/200.0);
    }
    public static HashMap<String, PlayerStats> loadPlayers(String filePath, String basicStatsPath) throws IOException {

        HashMap<String, PlayerStats> players = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(filePath));

        // Skip header
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {

            String[] data = line.split(",", -1);

            PlayerStats p = new PlayerStats();

            p.rank = parseInt(data[0]);
            p.name = data[1];
            p.age = parseInt(data[2]);
            p.team = data[3];
            p.position = data[4];
            p.games = parseInt(data[5]);
            p.gamesStarted = parseInt(data[6]);
            p.minutes = parseInt(data[7]);
            p.per = parseDouble(data[8]);
            p.trueShooting = parseDouble(data[9]);
            p.threePointAttemptRate = parseDouble(data[10]);
            p.freeThrowAttemptRate = parseDouble(data[11]);
            p.offensiveReboundPercentage = parseDouble(data[12]);
            p.defensiveReboundPercentage = parseDouble(data[13]);
            p.assistPercentage = parseDouble(data[15]);
            p.stealPercentage = parseDouble(data[16]);
            p.blockPercentage = parseDouble(data[17]);
            p.usage = parseDouble(data[19]);
            p.offensiveWinShares = parseDouble(data[20]);
            p.defensiveWinShares = parseDouble(data[21]);
            p.winShares48 = parseDouble(data[23]);
            p.offensiveBpm = parseDouble(data[24]);
            p.defensiveBpm = parseDouble(data[25]);
            p.bpm = parseDouble(data[26]);
            p.vorp = parseDouble(data[27]);
            p.awards = data[28];
            p.id = data[29];

            // If this is a combined row, always use it
            if (p.team.equals("2TM")) {
                players.put(p.name, p);
            }
            // Otherwise only add if we haven't seen this player yet
            else if (!players.containsKey(p.name)) {
                players.put(p.name, p);
            }
        }

        br.close();

        BufferedReader brr = new BufferedReader(new FileReader(basicStatsPath));

        // Skip header
        brr.readLine();
        String line2;
        while ((line2 = brr.readLine()) != null) {

            String[] data = line2.split(",", -1);

            PlayerStats p = players.get(data[1]);
            if (p != null) {
                p.points = parseDouble(data[29]);
                p.rebounds = parseDouble(data[23]);
                p.assists = parseDouble(data[24]);
                p.steals = parseDouble(data[25]);
                p.blocks = parseDouble(data[26]);
                p.turnovers = parseDouble(data[27]);
                p.fgpercentage = parseDouble(data[10]);
                p.threefgpercentage = parseDouble(data[13]);
                p.ftpercentage = parseDouble(data[18]);
            }
        }

        br.close();
        return players;

    }
    public static ArrayList<GameData> loadGamesWithPlayers(
        String gamesFilePath,
        HashMap<String, PlayerStats> players
) throws IOException {

    ArrayList<GameData> games = new ArrayList<>();

    BufferedReader br = new BufferedReader(new FileReader(gamesFilePath));

    // Skip header
    br.readLine();

    String line;

    while ((line = br.readLine()) != null) {
        String[] data = line.split(",", -1);

        GameData game = new GameData();

        game.date = data[0];

        game.awayTeam = data[1];
        game.homeTeam = data[2];

        game.awayPoints = parseInt(data[3]);
        game.homePoints = parseInt(data[4]);

        String[] awayPlayerNames = data[5].split(";");
        String[] homePlayerNames = data[6].split(";");

        for (String name : awayPlayerNames) {
            name = name.trim();

            PlayerStats p = players.get(name);

            if (p != null) {
                game.awayPlayers.add(p);
            } else {
                System.out.println("Missing player data for away player: " + name);
            }
        }

        for (String name : homePlayerNames) {
            name = name.trim();

            PlayerStats p = players.get(name);

            if (p != null) {
                game.homePlayers.add(p);
            } else {
                System.out.println("Missing player data for home player: " + name);
            }
        }
        game.GetData();
        game.GetHomeData();
        game.GetAwayData();
        games.add(game);
    }

    br.close();

    return games;
}
    
    private static int parseInt(String s) {
        if (s == null || s.isEmpty())
            return 0;

        return Integer.parseInt(s);
    }
    private static double parseDouble(String s) {
        if (s == null || s.isEmpty())
            return 0;

        return Double.parseDouble(s);
    }
    public static Network network;
    public static Network LoadNetwork(String filePath) throws IOException {
        network = new Network(401, new int[]{160,80,40}, 1, 0.0001);
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        ArrayList<String> lines = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        int index=0;
        for(int i = 0; i < network.hiddenLayer.size(); i++) {
            for(int j = 0; j < network.hiddenLayer.get(i).length; j++) {
                loadNode(network.hiddenLayer.get(i)[j], lines.get(index++));
            }            
        }
        for (Node outputNode : network.outputNodes) {
            loadNode(outputNode, lines.get(index++));
        }
        if (index != lines.size()) {
            throw new IOException("weights.csv has unexpected extra rows");
        }
        br.close();
        return network;
    }
    private static void loadNode(Node node, String line) throws IOException {
        String[] data = line.split(",", -1);
        if (data.length != node.weights.length + 2) {
            throw new IOException("weights.csv row has " + data.length + " values; expected " + (node.weights.length + 2));
        }
        node.bias = parseDouble(data[0]);
        for (int k = 0; k < node.weights.length; k++) {
            node.weights[k] = parseDouble(data[k + 2]);
        }
    }
    public static synchronized double[] Predict(GameData game){
        double[] homedata = game.homedata;
        double[] awaydata = game.awaydata;
        double homescore = network.GetOutputs(homedata)[0] * maxPoints + homeCourtAdvantage;
        double awayscore = network.GetOutputs(awaydata)[0] * maxPoints;
                
        if(Math.round(homescore) == Math.round(awayscore)) {
            if(homescore > awayscore) {
                homescore += 1;
            }
            else {
                awayscore += 1;
            }
        }
        return new double[]{Math.round(homescore), Math.round(awayscore)};
    }
    public static double[] predictTeams(String homeTeam, String awayTeam) {
        return predictTeams(homeTeam, awayTeam, new String[0], new String[0], new String[0], new String[0]);
    }
    public static double[] predictTeams(String homeTeam, String awayTeam, String[] homeOut, String[] homeIn, String[] awayOut, String[] awayIn) {
        if (players == null || network == null) {
            throw new IllegalStateException("Predictor has not been initialized");
        }
        GameData game = new GameData();
        game.homeTeam = homeTeam.toUpperCase();
        game.awayTeam = awayTeam.toUpperCase();
        game.homePlayers.addAll(teamRoster(game.homeTeam));
        game.awayPlayers.addAll(teamRoster(game.awayTeam));
        applySwaps(game.homePlayers, homeOut, homeIn, "home");
        applySwaps(game.awayPlayers, awayOut, awayIn, "away");
        if (game.homePlayers.isEmpty() || game.awayPlayers.isEmpty()) {
            throw new IllegalArgumentException("No model player data found for one or both teams");
        }
        game.GetHomeData();
        game.GetAwayData();
        return Predict(game);
    }
    public static double[] predictCustomHome(String awayTeam, String[] homePlayerNames) {
        if (players == null || network == null) {
            throw new IllegalStateException("Predictor has not been initialized");
        }
        if (homePlayerNames.length != 10) {
            throw new IllegalArgumentException("A custom team needs exactly 10 players");
        }
        GameData game = new GameData();
        game.homeTeam = "CUSTOM";
        game.awayTeam = awayTeam.toUpperCase();
        game.homePlayers.addAll(customRoster(homePlayerNames));
        game.awayPlayers.addAll(teamRoster(game.awayTeam));
        if (game.awayPlayers.isEmpty()) {
            throw new IllegalArgumentException("No model player data found for " + game.awayTeam);
        }
        game.GetHomeData();
        game.GetAwayData();
        return Predict(game);
    }
    public static ArrayList<PlayerStats> teamRoster(String team) {
        ArrayList<PlayerStats> roster = new ArrayList<>();
        for (PlayerStats player : players.values()) {
            if (team.equals(player.team)) {
                roster.add(player);
            }
        }
        ArrayList<PlayerStats> regularRotation = new ArrayList<>();
        for (PlayerStats player : roster) {
            if (player.games >= 10) regularRotation.add(player);
        }
        regularRotation.sort(Comparator.comparingDouble((PlayerStats player) -> (double) player.minutes / player.games).reversed());
        if (regularRotation.size() < 10) {
            roster.sort(Comparator.comparingInt((PlayerStats player) -> player.minutes).reversed());
            for (PlayerStats player : roster) {
                if (!regularRotation.contains(player)) regularRotation.add(player);
                if (regularRotation.size() == 10) break;
            }
        }
        return new ArrayList<>(regularRotation.subList(0, Math.min(10, regularRotation.size())));
    }
    private static ArrayList<PlayerStats> customRoster(String[] playerNames) {
        ArrayList<PlayerStats> roster = new ArrayList<>();
        for (String name : playerNames) {
            PlayerStats player = players.get(name);
            if (player == null) {
                throw new IllegalArgumentException("No model data found for " + name);
            }
            if (roster.contains(player)) {
                throw new IllegalArgumentException(name + " was selected more than once");
            }
            roster.add(player);
        }
        roster.sort(Comparator.comparingDouble((PlayerStats player) -> (double) player.minutes / player.games).reversed());
        return roster;
    }
    private static void applySwaps(ArrayList<PlayerStats> roster, String[] outgoingNames, String[] incomingNames, String side) {
        if (outgoingNames.length != incomingNames.length) throw new IllegalArgumentException("Incomplete " + side + " team swaps");
        for (int swapIndex = 0; swapIndex < outgoingNames.length; swapIndex++) {
            String outgoingName = outgoingNames[swapIndex];
            String incomingName = incomingNames[swapIndex];
        int index = -1;
        for (int i = 0; i < roster.size(); i++) {
            if (roster.get(i).name.equals(outgoingName)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException(outgoingName + " is not in the " + side + " model roster");
        }
        PlayerStats incoming = players.get(incomingName);
        if (incoming == null) {
            throw new IllegalArgumentException("No model data found for " + incomingName);
        }
        for (PlayerStats player : roster) {
            if (player.name.equals(incomingName)) {
                throw new IllegalArgumentException(incomingName + " is already in the " + side + " model roster");
            }
        }
        roster.set(index, incoming);
        }
    }
}

