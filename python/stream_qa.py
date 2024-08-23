import os

from langchain_core.messages import SystemMessage, HumanMessage
from langchain_google_vertexai import ChatVertexAI

if __name__ == "__main__":
    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location=os.environ["LOCATION"],
        model="gemini-1.5-flash-001",
        max_output_tokens=4000
    )

    messages = [
        SystemMessage(content="You're a helpful assistant"),
        HumanMessage(content="Why is sky blue?"),
    ]

    for chunk in llm.stream(messages):
        print(chunk.content, end="", flush=True)