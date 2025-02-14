import os
from random import random

from langchain import hub
from langchain.agents import create_tool_calling_agent, AgentExecutor
from langchain_core.tools import tool
from langchain_google_vertexai import ChatVertexAI


# Reference:
# https://python.langchain.com/v0.2/docs/how_to/function_calling/
# https://python.langchain.com/v0.1/docs/use_cases/tool_use/agents/


@tool
def convert_currency(from_currency: str, to_currency: str, amount: float) -> float:
    """Convert from from_currency to to_currency with the specified amount"""

    result = amount
    if from_currency == "USD" and to_currency == "EUR":
        result = amount * 0.93
    elif from_currency == "USD" and to_currency == "GBP":
        result = amount * 0.79

    print(f"convertCurrency(fromCurrency = {from_currency}, toCurrency = {to_currency}, amount = {amount}) == {result}")

    return result

@tool
def get_stock_price(symbol: str) -> float:
    """"Get the current value of a stock in US dollars"""
    result = 170.0 + 10 * random()
    print(f"get_stock_price(symbol = {symbol}) == {result}")
    return result


@tool
def apply_percentage(amount: float, percentage: float) -> float:
    """ Applies a percentage to a given amount"""
    result = amount * (percentage / 100)
    print(f"applyPercentage(amount = {amount}, percentage = {percentage}) == {result}")
    return result


if __name__ == "__main__":
    tools = [convert_currency, get_stock_price, apply_percentage]

    prompt = hub.pull("hwchase17/openai-tools-agent")
    prompt.pretty_print()

    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model="gemini-1.5-flash-002"
    )

    # Construct the tool calling agent
    agent = create_tool_calling_agent(llm, tools, prompt)

    # Create an agent executor by passing in the agent and tools
    agent_executor = AgentExecutor(agent=agent, tools=tools, verbose=True)

    query = "What is 10% of the AAPL stock price converted from USD to EUR?"
    print(f"User: {query}")
    response = agent_executor.invoke({"input": query})
    print(f"Response: {response['output']}")
