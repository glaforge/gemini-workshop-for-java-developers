import os

from langchain_core.prompts import PromptTemplate
from langchain_google_vertexai import ChatVertexAI

if __name__ == "__main__":
    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model="gemini-1.5-flash-002"
    )

    prompt_template = PromptTemplate.from_template("""
    Analyze the sentiment of the text below. Respond only with one word to describe the sentiment.

    INPUT: This is fantastic news!
    OUTPUT: POSITIVE

    INPUT: Pi is roughly equal to 3.14
    OUTPUT: NEUTRAL

    INPUT: I really disliked the pizza. Who would use pineapples as a pizza topping?
    OUTPUT: NEGATIVE

    INPUT: {text}
    OUTPUT:
    """)

    prompt = prompt_template.format(text="I love strawberries!")

    response = llm.invoke(prompt)
    print(response.content)
