import json
import os

from langchain_core.messages import HumanMessage, ToolMessage
from langchain_core.tools import tool
from langchain_google_vertexai import ChatVertexAI


# Reference: https://python.langchain.com/v0.2/docs/how_to/function_calling/

@tool
def get_weather_forecast(location: str) -> str:
    """Get the weather forecast for a given location or city"""
    data = {
        "location": location,
        "forecast": "sunny",
        "temperature": 20
    }
    json_data = json.dumps(data)
    return json_data


if __name__ == "__main__":
    tools = [get_weather_forecast]

    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model="gemini-1.5-flash-002"
    )

    llm_with_tools = llm.bind_tools(tools)

    # Ask about the weather
    query = "How's the weather in Paris?"
    print(f"User: {query}")
    messages = [HumanMessage(query)]
    response = llm_with_tools.invoke(messages)
    messages.append(response)

    # The model replies with a function call request
    print(f"Response: {response.tool_calls}")

    for tool_call in response.tool_calls:
        selected_tool = {"get_weather_forecast": get_weather_forecast}[tool_call["name"].lower()]
        tool_output = selected_tool.invoke(tool_call["args"])
        # Send back the result of the function call
        messages.append(ToolMessage(tool_output, tool_call_id=tool_call["id"]))

    # Invoke the model again with function call response
    response = llm_with_tools.invoke(messages)
    print(f"Response: {response.content}")

