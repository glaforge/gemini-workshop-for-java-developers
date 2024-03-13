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

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Step7_RAG {
    public static void main(String[] args) throws IOException {
        ApachePdfBoxDocumentParser pdfParser = new ApachePdfBoxDocumentParser();
        Document document = pdfParser.parse(new FileInputStream(
            "/PATH/TO/PDF/attention-is-all-you-need.pdf"));

        VertexAiEmbeddingModel embeddingModel = VertexAiEmbeddingModel.builder()
            .endpoint(System.getenv("LOCATION") + "-aiplatform.googleapis.com:443")
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .publisher("google")
            .modelName("textembedding-gecko@001")
            .maxRetries(3)
            .build();

        InMemoryEmbeddingStore<TextSegment> embeddingStore =
            new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor storeIngestor = EmbeddingStoreIngestor.builder()
            .documentSplitter(DocumentSplitters.recursive(500, 100))
            .embeddingModel(embeddingModel)
            .embeddingStore(embeddingStore)
            .build();
        storeIngestor.ingest(document);

        EmbeddingStoreContentRetriever retriever =
            new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel);

        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
                .project(System.getenv("PROJECT_ID"))
                .location(System.getenv("LOCATION"))
                .modelName("gemini-pro")
                .maxOutputTokens(1000)
                .build();

        ConversationalRetrievalChain rag = ConversationalRetrievalChain.builder()
            .chatLanguageModel(model)
            .contentRetriever(retriever)
            .build();

        // with a custom prompt template:
        /*
        ConversationalRetrievalChain rag = ConversationalRetrievalChain.builder()
            .chatLanguageModel(model)
            .contentRetriever(retriever)
            .retrievalAugmentor(DefaultRetrievalAugmentor.builder()
                .contentInjector(DefaultContentInjector.builder()
                    .promptTemplate(PromptTemplate.from("""
                        You are an expert in large language models,\s
                        you excel at explaining simply and clearly questions about LLMs.

                        Here is the question: {{userMessage}}

                        Answer using the following information:
                        {{contents}}
                        """))
                    .build())
                .build())
            .build();
         */

        List.of(
            "What neural network architecture can be used for language models?",
            "What are the different components of a transformer neural network?",
            "What is attention in large language models?",
            "What is the name of the process that transforms text into vectors?"
        ).forEach(query ->
            System.out.printf("%n==== %s ================ %n%n %s %n%n", query, rag.execute(query)));
    }
}
