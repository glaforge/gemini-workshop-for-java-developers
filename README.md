# Gemini Vertex AI and LangChain4j (Java)

> [!NOTE]
> This is the code for [Gemini in Java with Vertex AI and LangChain4j](https://codelabs.developers.google.com/codelabs/gemini-java-developers)
> codelab geared towards Java developers to discover [Gemini](https://deepmind.google/technologies/gemini/) 
> and its open-source variant [Gemma](https://ai.google.dev/gemma) Large Language Model by Google using [LangChain4j](https://docs.langchain4j.dev/) 
> framework.
> 
> There's also Python versions of these samples in [python](python) folder.

## Prerequisites

The code examples have been tested on the following environment:

* Java 21
* Gradle 8.6

In order to run these examples, you need to have a Google Cloud account and
project ready.

You also need to make sure the Vertex AI is enabled:

```bash
gcloud services enable aiplatform.googleapis.com
```

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

* [Simple Question & Answer via streaming](app/src/main/java/gemini/workshop/StreamQA.java)

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

* [Manipulating prompt templates](app/src/main/java/gemini/workshop/TemplatePrompt.java)

  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.TemplatePrompt
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

* [Multi function calling assistant](app/src/main/java/gemini/workshop/MultiFunctionCallingAssistant.java)

  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.MultiFunctionCallingAssistant
  ```

* [Running Gemma with Ollama TestContainer](app/src/main/java/gemini/workshop/GemmaWithOllamaContainer.java)

  ```bash
  ./gradlew run -q -DjavaMainClass=gemini.workshop.GemmaWithOllamaContainer
  ```

---
This is not an official Google product.
