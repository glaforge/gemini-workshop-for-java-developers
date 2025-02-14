import json
import os

from langchain import hub
from langchain.agents import create_tool_calling_agent, AgentExecutor
from langchain_core.tools import tool
from langchain_google_vertexai import ChatVertexAI


# Reference:
# https://python.langchain.com/v0.2/docs/how_to/function_calling/
# https://python.langchain.com/v0.1/docs/use_cases/tool_use/agents/

@tool
def get_weather_forecast(location: str) -> str:
    """Get the weather forecast and temperature for a location"""
    if location == "Paris":
        return json.dumps({
            "location": location,
            "forecast": "sunny",
            "temperature": 20
        })
    elif location == "London":
        return json.dumps({
            "location": location,
            "forecast": "rainy",
            "temperature": 15
        })

    return json.dumps({
        "location": "unknown",
        "forecast": "unknown",
        "temperature": 0
    })


if __name__ == "__main__":
    tools = [get_weather_forecast]

    prompt = hub.pull("hwchase17/openai-tools-agent")
    prompt.pretty_print()

    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model="gemini-1.5-pro-002"
    )

    # Construct the tool calling agent
    agent = create_tool_calling_agent(llm, tools, prompt)

    # Create an agent executor by passing in the agent and tools
    agent_executor = AgentExecutor(agent=agent, tools=tools, verbose=True)

    query = "How is the weather in Paris?"
    print(f"User: {query}")
    response = agent_executor.invoke({"input": query})
    print(f"Response: {response['output']}")

    query = "Is it warmer in London or in Paris?"
    print(f"User: {query}")
    response = agent_executor.invoke({"input": query})
    print(f"Response: {response['output']}")
