package br.com.alura.screensound.servico;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaGemini {

    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-pro:generateContent?key=" + API_KEY;

    public static String obterInformacao(String texto) {
        try {
            String body = """
                {
                  "contents": [{
                    "parts": [{ "text": "me fale sobre o artista: %s" }]
                  }]
                }
                """.formatted(texto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return extrairTextoDaResposta(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao consultar a API do Gemini.";
        }
    }

    private static String extrairTextoDaResposta(String json) {
        int indexInicio = json.indexOf("\"text\": \"");
        if (indexInicio == -1) return "Não foi possível interpretar a resposta da IA.";
        int indexFim = json.indexOf("\"", indexInicio + 9);
        return json.substring(indexInicio + 9, indexFim);
    }
}
