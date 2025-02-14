import os

from langchain_google_vertexai import ChatVertexAI

if __name__ == "__main__":
    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model="gemini-1.5-flash-002",
        max_output_tokens=4000
    )

    for chunk in llm.stream("Why is the sky blue?"):
        print(chunk.content, end="", flush=True)
