package gemini.workshop;

import com.github.dockerjava.api.command.InspectContainerResponse;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.testcontainers.ollama.OllamaContainer;

import java.io.IOException;

public class CallGemma {

    public static void main(String[] args) throws IOException, InterruptedException {
        OllamaContainer ollama = new OllamaContainer("ollama/ollama:0.1.26") {
            {
                withReuse(true);
            }

            @Override
            protected void containerIsStarted(InspectContainerResponse containerInfo, boolean reused) {
                if (reused) {
                    return;
                }

                try {
                    execInContainer("ollama", "pull", "gemma:2b");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        ollama.start();

        ChatLanguageModel model = OllamaChatModel.builder()
            .baseUrl(String.format("http://%s:%d", ollama.getHost(), ollama.getFirstMappedPort()))
            .modelName("gemma:2b")
            .build();

        String response = model.generate("Why is the sky blue?");

        System.out.println(response);
    }
}
