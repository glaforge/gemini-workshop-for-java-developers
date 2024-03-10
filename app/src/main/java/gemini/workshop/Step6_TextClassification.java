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
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;

import java.util.Map;

public class Step6_TextClassification {
    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .modelName("gemini-pro")
            .maxOutputTokens(10)
            .maxRetries(3)
            .build();

        PromptTemplate promptTemplate = PromptTemplate.from("""
            Analyze the sentiment of the text below. Respond only with one word to describe the sentiment.

            INPUT: This is fantastic news!
            OUTPUT: POSITIVE

            INPUT: Pi is roughly equal to 3.14
            OUTPUT: NEUTRAL

            INPUT: I really disliked the pizza. Who would use pineapples as a pizza topping?
            OUTPUT: NEGATIVE

            INPUT: {{text}}
            OUTPUT: 
            """);

        Prompt prompt = promptTemplate.apply(
            Map.of("text", "I love strawberries!"));

        Response<AiMessage> response = model.generate(prompt.toUserMessage());

        System.out.println(response.content().text());
    }
}
