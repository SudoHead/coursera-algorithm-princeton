package part_II.week3;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseballGraphVisualizer {

    private final Map<String, Integer> teams;
    private final int[] wins, losses, remaining;
    private final int[][] games;
    private final boolean[] trivalElimination;

    // create a baseball division from given filename in format specified below
    public BaseballGraphVisualizer(String filename) {
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

    private void draw(String team) {
        checkInputTeam(team);
        int teamId = teams.get(team);
        if (trivalElimination[teamId]) {
            StdOut.println("No Graph, trivial elimination...");
            return;
        }

        FlowNetwork fn = buildNetwork(teamId);

        // count number of game vertices and team vertices with edges
        int numTeamsVertices = 0;
        int numGameVertices = 0;
        for (int i = 0; i < numberOfTeams(); i++) {
            if (fn.adj(i).iterator().hasNext()) numTeamsVertices++;
            for (int j = 0; j < numberOfTeams(); j++) {
                int v = i * numberOfTeams() + j + numberOfTeams();
                if (fn.adj(v).iterator().hasNext()) numGameVertices++;
            }
        }

        double origin_x = 0.05, origin_y = 0.5;
        double level_offset_x = 0.28;
        double node_size = 0.02;

        // column 2 -- numT x numT nodes of remeaning matches
        double col_2_origin_y = 0.9 / (numGameVertices - 1);
        double _x[] = new double[fn.V()];
        double _y[] = new double[fn.V()];
        double offset_y = 0.95;
        for (int i = 0; i < numberOfTeams(); i++) {
            for (int j = 0; j < numberOfTeams(); j++) {
                int v = i * numberOfTeams() + j + numberOfTeams();
                if (i == teamId || j == teamId || !fn.adj(v).iterator().hasNext()) continue;
                double x = origin_x + level_offset_x;
                double y = offset_y;
                offset_y -= col_2_origin_y;
                _x[v] = x;
                _y[v] = y;
            }
        }
        // column 3 -- numT nodes
        double col_3_origin_y = 0.6 / (numTeamsVertices - 1);
        offset_y = 0.8;
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == teamId) continue;
            double x = origin_x + level_offset_x * 2;
            double y = offset_y;
            offset_y -= col_3_origin_y;
            _x[i] = x;
            _y[i] = y;
        }

        _x[fn.V() - 2] = origin_x;
        _y[fn.V() - 2] = origin_y;
        _x[fn.V() - 1] = origin_x + level_offset_x * 3;
        _y[fn.V() - 1] = origin_y;

        FordFulkerson ff = new FordFulkerson(fn, fn.V() - 2, fn.V() - 1);
        // draw edges
        StdDraw.setPenColor(StdDraw.GRAY);
        for (FlowEdge e : fn.edges()) {
            double from_x = _x[e.from()], from_y = _y[e.from()];
            double to_x = _x[e.to()], to_y = _y[e.to()];
            StdDraw.setPenColor(StdDraw.GRAY);
            if (ff.inCut(e.to())) StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(from_x, from_y, to_x, to_y);
            StdDraw.setPenColor(StdDraw.DARK_GRAY);
            if (e.flow() > 0) StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            String cap = e.capacity() == Integer.MAX_VALUE ? "∞" : (int) e.capacity() + "";
            double rotDegree = Math.atan2(Math.abs(to_y - from_y), Math.abs(to_x - from_x)) * 180 / Math.PI;
            rotDegree = to_y > from_y ? rotDegree : -rotDegree;
            StdDraw.text(from_x + (to_x - from_x) / 2, from_y + (to_y - from_y) / 2, (int) e.flow() + "/" + cap, rotDegree);
        }

        // Draw game vertices
        for (int i = 0; i < numberOfTeams(); i++) {
            for (int j = 0; j < numberOfTeams(); j++) {
                int v = i * numberOfTeams() + j + numberOfTeams();
                if (i == teamId || j == teamId || !fn.adj(v).iterator().hasNext()) continue;
                double x = _x[v];
                double y = _y[v];
                StdDraw.setPenColor(StdDraw.GRAY);
                if (ff.inCut(v)) StdDraw.setPenColor(StdDraw.BOOK_RED);
                StdDraw.filledCircle(x, y, node_size);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.text(x, y, i + "-" + j);
            }
        }

        // Draw team vertices
        for (int i = 0; i < numberOfTeams(); i++) {
            if (!fn.adj(i).iterator().hasNext()) continue;
            double x = _x[i];
            double y = _y[i];
            StdDraw.setPenColor(StdDraw.GRAY);
            if (ff.inCut(i)) StdDraw.setPenColor(StdDraw.BOOK_RED);
            StdDraw.filledCircle(x, y, node_size);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(x, y, "" + i);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.text(x + 0.06, y + 0.02, new ArrayList<>(teams.keySet()).get(i));
        }

        // column 1 -- only s
        StdDraw.setPenColor(StdDraw.GRAY);
        if (ff.inCut(fn.V() - 2)) StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.filledCircle(origin_x, origin_y, node_size);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(origin_x, origin_y, "s");

        // column 4 -- t
        StdDraw.setPenColor(StdDraw.GRAY);
        if (ff.inCut(fn.V() - 1)) StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.filledCircle(origin_x + level_offset_x * 3, origin_y, node_size);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(origin_x + level_offset_x * 3, origin_y, "t");
    }

    public static void main(String[] args) {
        if (args.length != 1)
            System.out.println("Argument missing: teams filename");
        BaseballGraphVisualizer bse = new BaseballGraphVisualizer(args[0]);
        bse.teams().forEach(t -> {
            if (bse.isEliminated(t)) {
                System.out.print(t + " is eliminated by the subset R = { ");
                bse.certificateOfElimination(t).forEach(r -> System.out.print(r + " "));
                System.out.println("}");
            } else
                System.out.println(t + " is not eliminated");
        });


        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(900, 900);
        while (true) {
            StdOut.println("Input team name: ");
            StdDraw.clear();
            String team = StdIn.readLine();
            try {
                bse.draw(team);
            } catch (IllegalArgumentException e) {
                StdOut.println(team + " not in teams");
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.text(0.5, 0.5, team + " not in teams");
                StdDraw.show();
                continue;
            }

            if (bse.isEliminated(team)) {
                System.out.print(team + " is eliminated by the subset R = { ");
                bse.certificateOfElimination(team).forEach(r -> System.out.print(r + " "));
                System.out.println("}");
            } else
                System.out.println(team + " is not eliminated");

            StdDraw.setPenColor(StdDraw.BOOK_RED);
            StdDraw.text(0.5, 0.98, "Graph for " + team);
            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}
