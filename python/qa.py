import os

from langchain_google_vertexai import ChatVertexAI

if __name__ == "__main__":
    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location=os.environ["LOCATION"],
        model="gemini-1.5-flash-001"
    )

    response = llm.invoke("Why is the sky blue?")
    print(response.content)
