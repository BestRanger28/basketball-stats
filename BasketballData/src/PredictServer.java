import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PredictServer {
    public static void main(String[] args) throws Exception {
        String dataDirectory = args.length > 0 ? args[0] : "BasketballData";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 8080;
        App.initialize(dataDirectory);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> sendJson(exchange, 200, "{\"service\":\"basketball-predictor\",\"endpoints\":[\"/health\",\"/predict?home=LAL&away=BOS\"]}"));
        server.createContext("/health", exchange -> sendJson(exchange, 200, "{\"status\":\"ok\"}"));
        server.createContext("/roster", PredictServer::roster);
        server.createContext("/predict", PredictServer::predict);
        server.start();
        System.out.println("Predictor server listening on http://localhost:" + port);
    }

    private static void predict(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Use GET\"}");
            return;
        }
        Map<String, String> query = queryParameters(exchange.getRequestURI().getRawQuery());
        String home = query.get("home");
        String away = query.get("away");
        if (home == null || away == null || home.equalsIgnoreCase(away)) {
            sendJson(exchange, 400, "{\"error\":\"Provide different home and away team codes\"}");
            return;
        }
        try {
            double[] scores = App.predictTeams(home, away, splitSwaps(query.get("homeOut")), splitSwaps(query.get("homeIn")), splitSwaps(query.get("awayOut")), splitSwaps(query.get("awayIn")));
            String winner = scores[0] > scores[1] ? home.toUpperCase() : away.toUpperCase();
            sendJson(exchange, 200, String.format("{\"homeTeam\":\"%s\",\"awayTeam\":\"%s\",\"homeScore\":%.0f,\"awayScore\":%.0f,\"winner\":\"%s\"}", home.toUpperCase(), away.toUpperCase(), scores[0], scores[1], winner));
        } catch (IllegalArgumentException exception) {
            sendJson(exchange, 400, "{\"error\":\"" + exception.getMessage() + "\"}");
        }
    }

    private static void roster(HttpExchange exchange) throws IOException {
        Map<String, String> query = queryParameters(exchange.getRequestURI().getRawQuery());
        String team = query.get("team");
        if (team == null) {
            sendJson(exchange, 400, "{\"error\":\"Provide a team code\"}");
            return;
        }
        StringBuilder body = new StringBuilder("{\"team\":\"").append(escape(team.toUpperCase())).append("\",\"players\":[");
        boolean first = true;
        for (PlayerStats player : App.teamRoster(team.toUpperCase())) {
            if (!first) body.append(',');
            body.append("{\"name\":\"").append(escape(player.name)).append("\",\"position\":\"").append(escape(player.position)).append("\"}");
            first = false;
        }
        body.append("]}");
        sendJson(exchange, 200, body.toString());
    }

    private static Map<String, String> queryParameters(String rawQuery) {
        Map<String, String> values = new HashMap<>();
        if (rawQuery == null) return values;
        for (String pair : rawQuery.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) values.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8), URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
        }
        return values;
    }

    private static String[] splitSwaps(String value) {
        return value == null || value.isEmpty() ? new String[0] : value.split("\\|", -1);
    }

    private static void sendJson(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
