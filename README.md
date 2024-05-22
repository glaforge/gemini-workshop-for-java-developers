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

## List of the samples

In this workshop, you will be able to go through the following examples:

* [Simple Question & Answer](app/src/main/java/gemini/workshop/QA.java)
* [Simple Question & Answer via streaming](app/src/main/java/gemini/workshop/QA_Streaming.java)
* [Hold a conversation with a chatbot](app/src/main/java/gemini/workshop/Conversation.java)
* [Describing an image with multimodality](app/src/main/java/gemini/workshop/Multimodal.java) (text+image)
* [Extracting structured data from unstructured text](app/src/main/java/gemini/workshop/ExtractData.java)
* [Manipulating prompt templates](app/src/main/java/gemini/workshop/PromptTemplate.java)
* [Text classification & sentiment analysis](app/src/main/java/gemini/workshop/TextClassification.java)
* [Retrieval Augmented Generation](app/src/main/java/gemini/workshop/RAG.java)
* [Function calling](app/src/main/java/gemini/workshop/FunctionCalling.java)
* [Function calling assistant](app/src/main/java/gemini/workshop/FunctionCallingAssistant.java)

## Running the examples

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

Now, you can use the Gradle wrapper to run the examples:

```bash
./gradlew run -q -DjavaMainClass=gemini.workshop.QA
./gradlew run -q -DjavaMainClass=gemini.workshop.StreamQA
./gradlew run -q -DjavaMainClass=gemini.workshop.Conversation
./gradlew run -q -DjavaMainClass=gemini.workshop.Multimodal
./gradlew run -q -DjavaMainClass=gemini.workshop.ExtractData
./gradlew run -q -DjavaMainClass=gemini.workshop.PromptTemplate
./gradlew run -q -DjavaMainClass=gemini.workshop.TextClassification
./gradlew run -q -DjavaMainClass=gemini.workshop.RAG
./gradlew run -q -DjavaMainClass=gemini.workshop.FunctionCalling
./gradlew run -q -DjavaMainClass=gemini.workshop.FunctionCallingAssistant
./gradlew run -q -DjavaMainClass=gemini.workshop.MultiFunctionCalling
```

## Prerequisites

The code examples have been tested on the following environment:

* Java 21
* Gradle 8.6

In order to run these examples, you need to have a Google Cloud account and project ready.

---
This is not an official Google product.
