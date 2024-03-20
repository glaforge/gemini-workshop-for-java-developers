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

* [Simple Question & Answer](app/src/main/gemini/workshop/Step0_QA.java)
* [Simple Question & Answer via streaming](app/src/main/gemini/workshop/Step1_QA_Streaming.java)
* [Hold a conversation with a chatbot](app/src/main/gemini/workshop/Step2_Conversation.java)
* [Describing an image with multimodality](app/src/main/gemini/workshop/Step3_Multimodal.java) (text+image)
* [Extracting structured data from unstructured text](app/src/main/gemini/workshop/Step4_ExtractData.java)
* [Manipulating prompt templates](app/src/main/gemini/workshop/StepT_PromptTemplate.java)
* [Text classification & sentiment analysis](app/src/main/gemini/workshop/Step6_TextClassification.java)
* [Retrieval Augmented Generation](app/src/main/gemini/workshop/Step7_RAG.java)
* [Function calling](app/src/main/gemini/workshop/Step8_FunctionCalling.java) 

### Running the examples

Before running the examples, you'll need to set up two environment variables:

```bash
export PROJECT_ID=YOUR_PROJECT_ID
export LOCATION=us-central1
```

> [!WARNING]
> Be sure to update the project ID and location to match your project.

Use the Gradle wrapper to run the examples:

```bash
./gradlew run -DjavaMainClass=gemini.workshop.Step0_QA
./gradlew run -DjavaMainClass=gemini.workshop.Step1_StreamQA
./gradlew run -DjavaMainClass=gemini.workshop.Step2_Conversation
./gradlew run -DjavaMainClass=gemini.workshop.Step3_Multimodal
./gradlew run -DjavaMainClass=gemini.workshop.Step4_ExtractData
./gradlew run -DjavaMainClass=gemini.workshop.Step5_PromptTemplate
./gradlew run -DjavaMainClass=gemini.workshop.Step6_TextClassification
./gradlew run -DjavaMainClass=gemini.workshop.Step7_RAG
./gradlew run -DjavaMainClass=gemini.workshop.Step8_FunctionCalling
```

# Prerequisites

The code examples have been tested on the following environment:

* Java 21
* Gradle 8.6

In order to run these examples, you need to have a Google Cloud account and project ready.

---
This is not an official Google product.