package part_II.week3;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {

    private final Map<String, Integer> teams;
    private final int[] wins, losses, remaining;
    private final int[][] games;
    private final boolean[] trivalElimination;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In file = new In(filename);
        int numTeams = Integer.parseInt(file.readLine());
        teams = new LinkedHashMap<>();
        wins = new int[numTeams];
        losses = new int[numTeams];
        remaining = new int[numTeams];
        games = new int[numTeams][numTeams];
        trivalElimination = new boolean[numTeams];

        int i = 0, maxWins = Integer.MIN_VALUE;
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] splits = line.trim().replaceAll(" +", " ").split(" ");
            if (splits.length != 4 + numTeams) // team win loss nGames n1 n2 n3 ...
                throw new IllegalArgumentException("Input file format error");
            teams.put(splits[0], i);
            wins[i] = Integer.parseInt(splits[1]);
            losses[i] = Integer.parseInt(splits[2]);
            remaining[i] = Integer.parseInt(splits[3]);
            if (wins[i] > maxWins) maxWins = wins[i];

            for (int j = 4; j < splits.length; j++)
                games[i][j - 4] = Integer.parseInt(splits[j]);
            i++;
        }

        // Check trivial elimination
        for (i = 0; i < numTeams; i++)
            if (wins[i] + remaining[i] < maxWins) trivalElimination[i] = true;
    }

    public int numberOfTeams() {
        return teams.size();
    }

    public Iterable<String> teams() {
        return teams.keySet();
    }

    private void checkInputTeam(String team) {
        if (team == null || !teams.containsKey(team))
            throw new IllegalArgumentException("Team not part of the division.");
    }

    public int wins(String team) {
        checkInputTeam(team);
        return wins[teams.get(team)];
    }

    public int losses(String team) {
        checkInputTeam(team);
        return losses[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkInputTeam(team);
        return remaining[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkInputTeam(team1);
        checkInputTeam(team2);
        return games[teams.get(team1)][teams.get(team2)];
    }

    private FlowNetwork buildNetwork(int teamId) {
        int n = numberOfTeams();
        int numVertices = n + n * n + 2;
        FlowNetwork fn = new FlowNetwork(numVertices);
        // last two vertices are source and target
        int vSource = numVertices - 2;
        int vTarget = numVertices - 1;

        for (int i = 0; i < n; i++) {
            if (i == teamId) continue;
            for (int j = i + 1; j < n; j++) {
                if (j == teamId || i == j) continue;
                int vGames = n + n * i + j;
                fn.addEdge(new FlowEdge(vSource, vGames, games[i][j]));
                fn.addEdge(new FlowEdge(vGames, i, Integer.MAX_VALUE));
                fn.addEdge(new FlowEdge(vGames, j, Integer.MAX_VALUE));
            }
            fn.addEdge(new FlowEdge(i, vTarget, wins[teamId] + remaining[teamId] - wins[i]));
        }
        return fn;
    }

    public boolean isEliminated(String team) {
        checkInputTeam(team);
        if (trivalElimination[teams.get(team)])
            return true;
        int teamId = teams.get(team);
        FlowNetwork fn = buildNetwork(teamId);
        int source = fn.V() - 2;
        int target = fn.V() - 1;
        new FordFulkerson(fn, source, target);
        // If some edges pointing from s are not full, then this team is eliminated
        for (FlowEdge e : fn.adj(source)) {
            if (e.to() != teams.get(team) && e.flow() != e.capacity()) return true;
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkInputTeam(team);
        int teamId = teams.get(team);
        List<String> teamList = new ArrayList<>(teams.keySet());
        List<String> certificate = new ArrayList<>();
        if (trivalElimination[teamId]) {
            for (int i = 0; i < numberOfTeams(); i++) {
                if (i != teamId && wins[i] > wins[teamId] + remaining[teamId])
                    certificate.add(teamList.get(i));
            }
        } else {
            FlowNetwork fn = buildNetwork(teamId);
            int source = fn.V() - 2;
            int target = fn.V() - 1;
            FordFulkerson ff = new FordFulkerson(fn, source, target);

            for (int i = 0; i < numberOfTeams(); i++)
                if (i != teamId && ff.inCut(i))
                    certificate.add(teamList.get(i));
        }
        return certificate.size() > 0 ? certificate : null;
    }

    public static void main(String[] args) {
        if (args.length != 1)
            System.out.println("Argument missing: teams filename");
        BaseballElimination bse = new BaseballElimination(args[0]);
        bse.teams().forEach(t -> {
            if (bse.isEliminated(t)) {
                System.out.print(t + " is eliminated by the subset R = { ");
                bse.certificateOfElimination(t).forEach(r -> System.out.print(r + " "));
                System.out.println("}");
            } else
                System.out.println(t + " is not eliminated");
        });
    }
}
