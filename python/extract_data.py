import os

from langchain_core.prompts import ChatPromptTemplate
from langchain_google_vertexai import ChatVertexAI
from pydantic import BaseModel, Field

# Reference: https://python.langchain.com/v0.2/docs/how_to/structured_output/


class Person(BaseModel):
    """Information about a person with name and age"""

    name: str = Field(description="The name of the person")
    age: int = Field(description="The age of the person")


if __name__ == "__main__":

    prompt = ChatPromptTemplate.from_messages(
        [
            (
                "system",
                "You're an expert extractor. Extract the relevant information from the text"
            ),
            # MessagesPlaceholder('examples'),
            ("human", "{text}"),
        ]
    )

    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model="gemini-1.5-flash-002",
        temperature=0
    )

    runnable = prompt | llm.with_structured_output(Person)

    text = """Anna is a 23 year old artist based in Brooklyn, New York. She was born and
            raised in the suburbs of Chicago, where she developed a love for art at a
            young age. She attended the School of the Art Institute of Chicago, where
            she studied painting and drawing. After graduating, she moved to New York
            City to pursue her art career. Anna's work is inspired by her personal
            experiences and observations of the world around her. She often uses bright
            colors and bold lines to create vibrant and energetic paintings. Her work
            has been exhibited in galleries and museums in New York City and Chicago."""

    response = runnable.invoke({"text": text})
    print(response)
