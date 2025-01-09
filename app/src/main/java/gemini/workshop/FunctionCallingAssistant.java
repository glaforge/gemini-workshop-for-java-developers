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

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.service.AiServices;

public class FunctionCallingAssistant {

    record WeatherForecast(String location, String forecast, int temperature) {}

    static class WeatherForecastService {
        @Tool("Get the weather forecast for a location")
        WeatherForecast getForecast(@P("Location to get the forecast for") String location) {
            if (location.equals("Paris")) {
                return new WeatherForecast("Paris", "sunny", 20);
            } else if (location.equals("London")) {
                return new WeatherForecast("London", "rainy", 15);
            } else {
                return new WeatherForecast("Unknown", "unknown", 0);
            }
        }
    }

    interface WeatherAssistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .modelName("gemini-1.5-pro-002")
            .build();

        WeatherForecastService weatherForecastService = new WeatherForecastService();

        WeatherAssistant assistant = AiServices.builder(WeatherAssistant.class)
            .chatLanguageModel(model)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .tools(weatherForecastService)
            .build();

        System.out.println(assistant.chat("What is the weather in Paris?"));
        System.out.println(assistant.chat("Is it warmer in London or in Paris?"));
    }
}
