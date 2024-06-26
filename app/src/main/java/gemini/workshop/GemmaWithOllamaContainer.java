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
import java.util.List;

// Make sure you have Docker installed and running locally before running this sample
public class GemmaWithOllamaContainer {

    private static final String TC_OLLAMA_GEMMA_2_B = "tc-ollama-gemma-2b";

    // Creating an Ollama container with Gemma 2B if it doesn't exist.
    private static OllamaContainer createGemmaOllamaContainer() throws IOException, InterruptedException {

        // Check if the custom Gemma Ollama image exists already
        List<Image> listImagesCmd = DockerClientFactory.lazyClient()
            .listImagesCmd()
            .withImageNameFilter(TC_OLLAMA_GEMMA_2_B)
            .exec();

        if (listImagesCmd.isEmpty()) {
            System.out.println("Creating a new Ollama container with Gemma 2B image...");
            OllamaContainer ollama = new OllamaContainer("ollama/ollama:0.1.26");
            ollama.start();
            ollama.execInContainer("ollama", "pull", "gemma:2b");
            ollama.commitToImage(TC_OLLAMA_GEMMA_2_B);
            return ollama;
        } else {
            System.out.println("Using existing Ollama container with Gemma 2B image...");
            // Substitute the default Ollama image with our Gemma variant
            return new OllamaContainer(
                DockerImageName.parse(TC_OLLAMA_GEMMA_2_B)
                    .asCompatibleSubstituteFor("ollama/ollama"));
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        OllamaContainer ollama = createGemmaOllamaContainer();
        ollama.start();

        ChatLanguageModel model = OllamaChatModel.builder()
            .baseUrl(String.format("http://%s:%d", ollama.getHost(), ollama.getFirstMappedPort()))
            .modelName("gemma:2b")
            .build();

        String response = model.generate("Why is the sky blue?");

        System.out.println(response);
    }
}
