import os

from langchain_community.chat_message_histories import ChatMessageHistory
from langchain_core.chat_history import BaseChatMessageHistory
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.runnables import RunnableWithMessageHistory
from langchain_google_vertexai import ChatVertexAI

store = {}  # memory is maintained outside the chain


def get_session_history(session_id: str) -> BaseChatMessageHistory:
    if session_id not in store:
        store[session_id] = ChatMessageHistory()
    return store[session_id]


if __name__ == "__main__":
    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model="gemini-2.0-flash"
    )

    prompt = ChatPromptTemplate.from_messages(
        [
            ("system", "You are a helpful assistant."),
            ("placeholder", "{history}"),
            ("human", "{input}"),
        ]
    )

    chain = prompt | llm

    with_message_history = RunnableWithMessageHistory(
        chain,
        get_session_history,
        input_messages_key="input",
        history_messages_key="history",
    )

    messages = [
        "Hello!",
        "What is the country where the Eiffel tower is situated?",
        "How many inhabitants are there in that country?"
    ]

    for message in messages:
        print(f"User: {message}")
        print("Gemini: ", end="")
        for chunk in with_message_history.stream(
            {"input": message},
            config={"configurable": {"session_id": "abc123"}},
        ):
            print(chunk.content, end="", flush=True)
