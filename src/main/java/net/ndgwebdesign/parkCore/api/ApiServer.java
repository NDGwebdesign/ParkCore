package net.ndgwebdesign.parkCore.api;

import com.google.gson.Gson;
import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.objects.AttractionStatus;
import org.bukkit.Bukkit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ApiServer {

    private ServerSocket serverSocket;
    private final int port;
    private final ParkCore plugin;
    private boolean running = false;
    private final Gson gson = new Gson();

    public ApiServer(ParkCore plugin, int port) {
        this.plugin = plugin;
        this.port = port;
    }

    /* ===================== */
    /* Lifecycle             */
    /* ===================== */

    public void start() {
        if (running) return;

        running = true;

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                plugin.getLogger().info("[API] Server gestart op poort " + port);

                while (running) {
                    Socket client = serverSocket.accept();
                    handleClient(client);
                }
            } catch (IOException e) {
                if (running) {
                    plugin.getLogger().severe("[API] Fout: " + e.getMessage());
                }
            }
        }, "ParkCore-ApiServer").start();
    }

    public void stop() {
        running = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            plugin.getLogger().info("[API] Server gestopt.");
        } catch (IOException e) {
            plugin.getLogger().severe("[API] Kon server niet stoppen.");
        }
    }

    /* ===================== */
    /* Client handling       */
    /* ===================== */

    private void handleClient(Socket socket) {

        new Thread(() -> {

            try (
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
            ) {

                /* ---------- API KEY ---------- */
                String apiKey = in.readLine();
                if (apiKey == null || !apiKey.equals(plugin.getConfig().getString("api.key"))) {
                    out.println("ERROR Invalid API key");
                    return;
                }

                /* ---------- COMMAND ---------- */
                String input = in.readLine();
                if (input == null) {
                    out.println("ERROR No command");
                    return;
                }

                String[] args = input.split(" ");
                String command = args[0].toUpperCase();

                switch (command) {

                    case "PING" -> out.println("OK PONG");
                    case "GET_ATTRACTION" ->
                            handleGetAttraction(out, args);
                    case "SET_STATUS" ->
                            handleSetStatus(out, args);
                    default ->
                            out.println("ERROR Unknown command");

                }

            } catch (Exception e) {
                plugin.getLogger().warning("[API] Client error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }

        }, "ParkCore-ApiClient").start();
    }

    /* ===================== */
    /* API Commands          */
    /* ===================== */

    private void handleGetAttraction(PrintWriter out, String[] args) {

        if (args.length < 2) {
            out.println("ERROR Usage: GET_ATTRACTION <name>");
            return;
        }

        String name = args[1];

        Attraction attraction = AttractionManager.getAttraction(name);
        if (attraction == null) {
            out.println("ERROR Attraction not found");
            return;
        }

        out.println("OK "
                + "name=" + attraction.getName()
                + ";region=" + attraction.getRegion()
                + ";status=" + attraction.getStatus().name()
        );
    }

    private void handleSetStatus(PrintWriter out, String[] args) {

        if (args.length < 3) {
            out.println("ERROR Usage: SET_STATUS <name> <OPEN|CLOSED|MAINTENANCE>");
            return;
        }

        String name = args[1];
        String statusArg = args[2].toUpperCase();

        Attraction attraction = AttractionManager.getAttraction(name);
        if (attraction == null) {
            out.println("ERROR Attraction not found");
            return;
        }

        AttractionStatus status;
        try {
            status = AttractionStatus.valueOf(statusArg);
        } catch (IllegalArgumentException e) {
            out.println("ERROR Invalid status");
            return;
        }

        // Bukkit-thread veilig uitvoeren
        Bukkit.getScheduler().runTask(plugin, () -> {
            attraction.setStatus(status);
        });

        out.println("OK Status updated");
    }
}
