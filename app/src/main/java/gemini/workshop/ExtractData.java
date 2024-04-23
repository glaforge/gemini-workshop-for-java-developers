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
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

public class ExtractData {

    record Person(String name, int age) {}

    interface PersonExtractor {
        @UserMessage("""
            Extract the name and age of the person described below.
            Return a JSON document with a "name" and an "age" property, \
            following this structure: {"name": "John Doe", "age": 34}
            Return only JSON, without any markdown markup surrounding it.
            Here is the document describing the person:
            ---
            {{it}}
            ---
            JSON: 
            """)
        Person extractPerson(String text);
    }

    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .modelName("gemini-1.0-pro-001")
            .temperature(0f)
            .topK(1)
            .build();

        PersonExtractor extractor = AiServices.create(PersonExtractor.class, model);

        Person person = extractor.extractPerson("""
            Anna is a 23 year old artist based in Brooklyn, New York. She was born and 
            raised in the suburbs of Chicago, where she developed a love for art at a 
            young age. She attended the School of the Art Institute of Chicago, where 
            she studied painting and drawing. After graduating, she moved to New York 
            City to pursue her art career. Anna's work is inspired by her personal 
            experiences and observations of the world around her. She often uses bright 
            colors and bold lines to create vibrant and energetic paintings. Her work 
            has been exhibited in galleries and museums in New York City and Chicago.    
            """
        );

        System.out.println(person.name());  // Anna
        System.out.println(person.age());   // 23
    }
}
