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
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

public class Step2_Conversation {
    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .modelName("gemini-pro")
            .build();

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
            .maxMessages(20)
            .build();

        ConversationalChain chain = ConversationalChain.builder()
            .chatLanguageModel(model)
            .chatMemory(chatMemory)
            .build();

        System.out.println(
            chain.execute("Hello!"));
            // Hi there! How can I assist you today?

        System.out.println(
            chain.execute("What is the country where the Eiffel tower is situated?"));
            // The Eiffel Tower is located in Paris, France.

        System.out.println(
            chain.execute("How many habitants are there in that country?"));
            // As of 2023, the population of France is estimated to be approximately 67.3 million.
    }
}
