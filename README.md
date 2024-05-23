# Gemini Workshop

> Welcome to the Gemini workshop for Java developers, 
using the LangChain4j LLM orchestration framework.

This workshop is geared towards Java developers, to discover the 
[Gemini](https://deepmind.google/technologies/gemini/#introduction) 
Large Language Model created by Google.
We will explore the model through the use of 
[LangChain4j](https://docs.langchain4j.dev/) 
to interact with the LLM.

The steps and instructions in this workshop are detailed in the 
[Gemini for Java Developers codelab](https://codelabs.developers.google.com/codelabs/gemini-java-developers).

## Prerequisites

The code examples have been tested on the following environment:

* Java 21
* Gradle 8.6

In order to run these examples, you need to have a Google Cloud account and project ready.

Before running the examples, you'll need to set up two environment variables:

```bash
export PROJECT_ID=YOUR_PROJECT_ID
export LOCATION=us-central1
```

> [!WARNING]
> Be sure to update the project ID and location to match your project.

Create the Gradle wrapper:

```bash
gradle wrapper
```

## Samples

These are the list of samples for different use cases:

* [Simple Question & Answer](app/src/main/java/gemini/workshop/QA.java)
    ```bash
    ./gradlew run -q -DjavaMainClass=gemini.workshop.QA
    ```
* [Simple Question & Answer via streaming](app/src/main/java/gemini/workshop/QA_Streaming.java)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.StreamQA
  ```
* [Hold a conversation with a chatbot](app/src/main/java/gemini/workshop/Conversation.java)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.Conversation
  ```
* [Describing an image with multimodality](app/src/main/java/gemini/workshop/Multimodal.java) (text+image)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.Multimodal
  ```
* [Extracting structured data from unstructured text](app/src/main/java/gemini/workshop/ExtractData.java)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.ExtractData
  ```
* [Manipulating prompt templates](app/src/main/java/gemini/workshop/PromptTemplate.java)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.PromptTemplate
  ```
* [Text classification & sentiment analysis](app/src/main/java/gemini/workshop/TextClassification.java)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.TextClassification
  ```
* [Retrieval Augmented Generation](app/src/main/java/gemini/workshop/RAG.java)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.RAG
  ```
* [Function calling](app/src/main/java/gemini/workshop/FunctionCalling.java)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.FunctionCalling
  ```
* [Function calling assistant](app/src/main/java/gemini/workshop/FunctionCallingAssistant.java)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.FunctionCallingAssistant
  ```
* [Multi function calling assistant](app/src/main/java/gemini/workshop/MultiFunctionCalling.java)
  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.MultiFunctionCalling
  ```

---
This is not an official Google product.
