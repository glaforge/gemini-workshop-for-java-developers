/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gemini.workshop;

import com.github.dockerjava.api.model.Image;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

// Make sure you have Docker installed and running locally before running this sample
public class GemmaWithOllamaContainer {

    private static final String TC_OLLAMA_GEMMA2 = "tc-ollama-gemma2-2b";
    public static final String GEMMA_2 = "gemma2:2b";

    // Creating an Ollama container with Gemma 2 if it doesn't exist.
    private static OllamaContainer createGemmaOllamaContainer() throws IOException, InterruptedException {

        // Check if the custom Gemma Ollama image exists already
        List<Image> listImagesCmd = DockerClientFactory.lazyClient()
            .listImagesCmd()
            .withImageNameFilter(TC_OLLAMA_GEMMA2)
            .exec();

        if (listImagesCmd.isEmpty()) {
            System.out.println("Creating a new Ollama container with Gemma 2 image...");
            OllamaContainer ollama = new OllamaContainer("ollama/ollama:0.3.12");
            System.out.println("Starting Ollama...");
            ollama.start();
            System.out.println("Pulling model...");
            ollama.execInContainer("ollama", "pull", GEMMA_2);
            System.out.println("Committing to image...");
            ollama.commitToImage(TC_OLLAMA_GEMMA2);
        }

        System.out.println("Ollama image substitution...");
        // Substitute the default Ollama image with our Gemma variant
        return new OllamaContainer(
            DockerImageName.parse(TC_OLLAMA_GEMMA2)
                .asCompatibleSubstituteFor("ollama/ollama"));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Instant start = Instant.now();

        OllamaContainer ollama = createGemmaOllamaContainer();
        System.out.printf("Container created in %ds %n", Duration.between(start, Instant.now()).getSeconds());
        start = Instant.now();

        ollama.start();
        System.out.printf("Ollama container started in %ds %n", Duration.between(start, Instant.now()).getSeconds());
        start = Instant.now();

        ChatLanguageModel model = OllamaChatModel.builder()
            .baseUrl(String.format("http://%s:%d", ollama.getHost(), ollama.getFirstMappedPort()))
            .modelName(GEMMA_2)
            .timeout(Duration.ofMinutes(2))
            .build();

        System.out.printf("Model ready in %ds %n", Duration.between(start, Instant.now()).getSeconds());
        start = Instant.now();

        System.out.println(model.generate("Why is the sky blue?"));
        System.out.printf("First response: %ds %n", Duration.between(start, Instant.now()).getSeconds());
        start = Instant.now();

        System.out.println(model.generate("Who was the first cat who stepped on the moon?"));
        System.out.printf("Second response: %ds %n", Duration.between(start, Instant.now()).getSeconds());
        start = Instant.now();

        System.out.println(model.generate("What are the differences between the Gemini model and the Gemma models?"));
        System.out.printf("Third response: %ds %n", Duration.between(start, Instant.now()).getSeconds());
    }
}
