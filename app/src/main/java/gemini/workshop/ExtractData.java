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
import dev.langchain4j.service.SystemMessage;

import static dev.langchain4j.model.vertexai.SchemaHelper.fromClass;

public class ExtractData {

    record Person(String name, int age) { }

    interface PersonExtractor {
        @SystemMessage("""
            Your role is to extract the name and age 
            of the person described in the biography.
            """)
        Person extractPerson(String biography);
    }

    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(System.getenv("PROJECT_ID"))
            .location(System.getenv("LOCATION"))
            .modelName("gemini-1.5-flash-002")
            .responseMimeType("application/json")
            .responseSchema(fromClass(Person.class))
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
            """);

        System.out.println(person.name());  // Anna
        System.out.println(person.age());   // 23
    }
}
