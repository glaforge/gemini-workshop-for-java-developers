import os

from langchain_google_vertexai import ChatVertexAI

if __name__ == "__main__":
    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model="gemini-2.0-flash"
    )

    response = llm.invoke("Why is the sky blue?")
    print(response.content)
