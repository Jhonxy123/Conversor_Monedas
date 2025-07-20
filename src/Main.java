import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    private static final String API_KEY = "d5cc277c801fba12c4b6a712";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n=== Conversor de Divisas ===");
            System.out.println("1. Dólar -> Peso Argentino");
            System.out.println("2. Peso Argentino -> Dólar");
            System.out.println("3. Real brasileño -> Dólar");
            System.out.println("4. Dólar -> Real brasileño");
            System.out.println("5. Peso Colombiano -> Dólar");
            System.out.println("6. Dólar -> Peso Colombiano");
            System.out.println("7. Salir");
            System.out.print("Elija una opción válida: ");
            System.out.println("\n=============================");
            option = scanner.nextInt();

            String from = "", to = "";
            switch (option) {
                case 1 -> { from = "USD"; to = "ARS"; }
                case 2 -> { from = "ARS"; to = "USD"; }
                case 3 -> { from = "BRL"; to = "USD"; }
                case 4 -> { from = "USD"; to = "BRL"; }
                case 5 -> { from = "COP"; to = "USD"; }
                case 6 -> { from = "USD"; to = "COP"; }
                case 7 -> {
                    System.out.println("Saliendo...");
                    break;
                }
                default -> {
                    System.out.println("Opción inválida.");
                    continue;
                }
            }

            if (option >= 1 && option <= 6) {
                System.out.print("Ingrese cantidad a convertir: ");
                double amount = scanner.nextDouble();
                convertCurrency(from, to, amount);
            }

        } while (option != 7);

        scanner.close();
    }

    public static void convertCurrency(String from, String to, double amount) {
        try {
            String urlStr = BASE_URL + from + "/" + to;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            Gson gson = new Gson();
            JsonObject response = gson.fromJson(reader, JsonObject.class);

            if (response.get("result").getAsString().equals("success")) {
                double rate = response.get("conversion_rate").getAsDouble();
                double result = amount * rate;
                System.out.printf("Tasa actual: 1 %s = %.4f %s\n", from, rate, to);
                System.out.printf("%.2f %s = %.2f %s\n", amount, from, result, to);
            } else {
                System.out.println("Error al obtener el tipo de cambio.");
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}
