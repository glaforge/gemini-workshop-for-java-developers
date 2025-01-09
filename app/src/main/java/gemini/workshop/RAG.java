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

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class RAG {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // ===============
        // INGESTION PHASE

        URL url = new URI("https://raw.githubusercontent.com/meteatamel/genai-beyond-basics/main/samples/grounding/vertexai-search/cymbal-starlight-2024.pdf").toURL();
        ApachePdfBoxDocumentParser pdfParser = new ApachePdfBoxDocumentParser();
        Document document = pdfParser.parse(url.openStream());

        VertexAiEmbeddingModel embeddingModel = VertexAiEmbeddingModel.builder()
            .endpoint(System.getenv("LOCATION") + "-aiplatform.googleapis.com:443")
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .publisher("google")
            .modelName("text-embedding-005")
            .maxRetries(3)
            .build();

        InMemoryEmbeddingStore<TextSegment> embeddingStore =
            new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor storeIngestor = EmbeddingStoreIngestor.builder()
            .documentSplitter(DocumentSplitters.recursive(500, 100))
            .embeddingModel(embeddingModel)
            .embeddingStore(embeddingStore)
            .build();
        System.out.println("Chunking and embedding PDF...");
        storeIngestor.ingest(document);

        // ===============
        // RETRIEVAL PHASE

        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
                .project(System.getenv("PROJECT_ID"))
                .location(System.getenv("LOCATION"))
                .modelName("gemini-1.5-flash-002")
                .maxOutputTokens(1000)
                .build();

        EmbeddingStoreContentRetriever retriever =
            new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel);

        interface CarExpert {
            Result<String> ask(String question);
        }

        CarExpert expert = AiServices.builder(CarExpert.class)
            .chatLanguageModel(model)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .contentRetriever(retriever)
            /*
            .retrievalAugmentor(DefaultRetrievalAugmentor.builder()
                .contentInjector(DefaultContentInjector.builder()
                    .promptTemplate(PromptTemplate.from("""
                        You are an expert in car automotive, and you answer concisely.

                        Here is the question: {{userMessage}}

                        Answer using the following information:
                        {{contents}}
                        """))
                    .build())
                .contentRetriever(retriever)
                .build())
             */
            .build();

        System.out.println("Ready!\n");
        List.of(
            "What is the cargo capacity of Cymbal Starlight?",
            "What's the emergency roadside assistance phone number?",
            "Are there some special kits available on that car?"
        ).forEach(query -> {
            Result<String> response = expert.ask(query);
            System.out.printf("%n=== %s === %n%n %s %n%n", query, response.content());
            System.out.println("SOURCE: " + response.sources().getFirst().textSegment().text());
        });
    }
}
