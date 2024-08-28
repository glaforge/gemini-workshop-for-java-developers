# Gemini with Vertex AI and LangChain (Python)

> [!NOTE]
> This is the Python code for [Gemini in Java with Vertex AI and LangChain4j](https://codelabs.developers.google.com/codelabs/gemini-java-developers)
> codelab geared towards Python developers to discover [Gemini](https://deepmind.google/technologies/gemini/) 
> Large Language Model by Google using [LangChain](https://www.langchain.com/) framework. 

## Prerequisites

Before running the samples, it's a good idea to create a Python virtual environment and activate it:

```shell
python -m venv .venv
source .venv/bin/activate
```

Install the dependencies:

```shell
pip install -r requirements.txt
```

You also need to have a Google Cloud account and project ready and set up two environment variables:

```shell
export PROJECT_ID=YOUR_PROJECT_ID
export LOCATION=us-central1
```

## Samples

These are the list of samples for different use cases:

* [Simple Question & Answer](qa.py)

    ```shell
    python qa.py
    ```

* [Simple Question & Answer via streaming](stream_qa.py)

  ```shell
  python stream_qa.py
  ```

* [Hold a conversation with a chatbot](conversation.py)

  ```shell
  python conversation.py
  ```

* [Describing an image with multimodality](multimodal.py)

  ```shell
  python multimodal.py
  ```

* [Extracting structured data from unstructured text](extract_data.py)

  ```shell
  python extract_data.py
  ```

* [Manipulating prompt templates](template_prompt.py)

  ```shell
  python template_prompt.py
  ```

* [Text classification & sentiment analysis](text_classification.py)

  ```shell
  python text_classification.py
  ```

* [Retrieval Augmented Generation](rag.py)

  ```shell
  python rag.py
  ```

* [Function calling](function_calling.py)

  ```shell
  python function_calling.py
  ```

* [Function calling assistant](function_calling_assistant.py)

  ```shell
  python function_calling_assistant.py
  ```

* [Multi function calling assistant](multi_function_calling_assistant.py)

  ```shell
  python multi_function_calling_assistant.py
  ```

---
This is not an official Google product.
