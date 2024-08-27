import os

from langchain_core.messages import SystemMessage, HumanMessage
from langchain_google_vertexai import ChatVertexAI

if __name__ == "__main__":
    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location=os.environ["LOCATION"],
        model="gemini-1.5-flash-001"
    )

    messages = [
        SystemMessage(content="You're a helpful assistant"),
        HumanMessage(content="Why is sky blue?"),
    ]

    response = llm.invoke(messages)
    print(response.content)
