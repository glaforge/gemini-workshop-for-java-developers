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
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Random;

import static dev.langchain4j.memory.chat.MessageWindowChatMemory.*;

public class MultiFunctionCallingAssistant {
    static class MultiTools {
        @Tool("Convert amounts between two currencies")
        double convertCurrency(
            @P("Currency to convert from") String fromCurrency,
            @P("Currency to convert to") String toCurrency,
            @P("Amount to convert") double amount) {

            double result = amount;

            if (fromCurrency.equals("USD") && toCurrency.equals("EUR")) {
                result = amount * 0.93;
            } else if (fromCurrency.equals("USD") && toCurrency.equals("GBP")) {
                result = amount * 0.79;
            }

            System.out.println(
                "convertCurrency(fromCurrency = " + fromCurrency +
                    ", toCurrency = " + toCurrency +
                    ", amount = " + amount + ") == " + result);

            return result;
        }

        @Tool("Get the current value of a stock in US dollars")
        double getStockPrice(@P("Stock symbol") String symbol) {
            double result = 170.0 + 10 * new Random().nextDouble();

            System.out.println("getStockPrice(symbol = " + symbol + ") == " + result);

            return result;
        }

        @Tool("Apply a percentage to a given amount")
        double applyPercentage(@P("Initial amount") double amount, @P("Percentage between 0-100 to apply") double percentage) {
            double result = amount * (percentage / 100);

            System.out.println("applyPercentage(amount = " + amount + ", percentage = " + percentage + ") == " + result);

            return result;
        }
    }

    interface MultiToolsAssistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .modelName("gemini-1.5-flash-002")
            .maxOutputTokens(100)
            .build();

        MultiTools multiTools = new MultiTools();

        MultiToolsAssistant assistant = AiServices.builder(MultiToolsAssistant.class)
            .chatLanguageModel(model)
            .chatMemory(withMaxMessages(10))
            .tools(multiTools)
            .build();

        System.out.println(assistant.chat(
            "What is 10% of the AAPL stock price converted from USD to EUR?"));
    }
}