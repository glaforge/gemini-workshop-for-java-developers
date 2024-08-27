import os

from langchain_core.messages import HumanMessage
from langchain_google_vertexai import ChatVertexAI

CLOUD_NEXT_URL = "https://storage.googleapis.com/github-repo/img/vision/google-cloud-next.jpeg"

if __name__ == "__main__":
    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location=os.environ["LOCATION"],
        model="gemini-1.5-flash-001"
    )

    image_message = {
        "type": "image_url",
        "image_url": {"url": CLOUD_NEXT_URL},
    }
    text_message = {
        "type": "text",
        "text": "Describe the picture",
    }

    message = HumanMessage(content=[text_message, image_message])

    response = llm.invoke([message])
    print(response.content)
