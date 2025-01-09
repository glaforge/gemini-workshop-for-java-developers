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
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.model.output.Response;

import java.util.List;
import java.util.ArrayList;

public class FunctionCalling {
    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .modelName("gemini-1.5-pro-002")
            .maxOutputTokens(100)
            .build();

        ToolSpecification weatherToolSpec = ToolSpecification.builder()
            .name("getWeather")
            .description("Get the weather forecast for a given location or city")
            .parameters(JsonObjectSchema.builder()
                .addStringProperty(
                    "location",
                    "the location or city to get the weather forecast for")
                .build())
            .build();

        List<ChatMessage> allMessages = new ArrayList<>();

        // 1) Ask about the weather
        UserMessage weatherQuestion = UserMessage.from("What is the weather in Paris?");
        allMessages.add(weatherQuestion);

        // 2) The model replies with a function call request
        Response<AiMessage> messageResponse = model.generate(allMessages, weatherToolSpec);
        ToolExecutionRequest toolExecutionRequest = messageResponse.content().toolExecutionRequests().get(0);
        System.out.println("Tool execution request: " + toolExecutionRequest);
        allMessages.add(messageResponse.content());

        // Here, we would call a real weather forecast service

        // 3) We send back the result of the function call
        ToolExecutionResultMessage toolExecResMsg = ToolExecutionResultMessage.from(toolExecutionRequest,
            "{\"location\":\"Paris\",\"forecast\":\"sunny\", \"temperature\": 20}");
        allMessages.add(toolExecResMsg);

        // 4) The model answers with a sentence describing the weather
        Response<AiMessage> weatherResponse = model.generate(allMessages);
        System.out.println("Answer: " + weatherResponse.content().text());
    }
}
