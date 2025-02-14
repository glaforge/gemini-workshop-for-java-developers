import os

from langchain.chains.combine_documents import create_stuff_documents_chain
from langchain.chains.retrieval import create_retrieval_chain
from langchain_community.document_loaders import PyPDFLoader
from langchain_community.vectorstores import Annoy
from langchain_core.prompts import ChatPromptTemplate
from langchain_google_vertexai import VertexAIEmbeddings, ChatVertexAI
from langchain_text_splitters import RecursiveCharacterTextSplitter

# Reference: https://python.langchain.com/v0.2/docs/tutorials/pdf_qa/

if __name__ == "__main__":

    print("Load and parse the PDF")
    loader = PyPDFLoader(
        "https://raw.githubusercontent.com/meteatamel/genai-beyond-basics/main/samples/grounding/vertexai-search/cymbal-starlight-2024.pdf")
    documents = loader.load()

    print("Split the document into chunks")
    text_splitter = RecursiveCharacterTextSplitter(chunk_size=500, chunk_overlap=100)
    texts = text_splitter.split_documents(documents)

    print("Initialize the embedding model")
    embeddingsLlm = VertexAIEmbeddings(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model_name="text-embedding-005"
    )

    print("Create a vector store")
    vector_store = Annoy.from_documents(texts, embeddingsLlm)

    retriever = vector_store.as_retriever()

    print("Initialize the chat model")
    llm = ChatVertexAI(
        project=os.environ["PROJECT_ID"],
        location="us-central1",
        model="gemini-1.5-flash-002"
    )

    system_prompt = (
        "You are an assistant for question-answering tasks. "
        "Use the following pieces of retrieved context to answer "
        "the question. If you don't know the answer, say that you "
        "don't know. Use three sentences maximum and keep the "
        "answer concise."
        "\n\n"
        "{context}"
    )

    prompt = ChatPromptTemplate.from_messages(
        [
            ("system", system_prompt),
            ("human", "{input}"),
        ]
    )

    print("Create RAG chain")
    question_answer_chain = create_stuff_documents_chain(llm, prompt)
    rag_chain = create_retrieval_chain(retriever, question_answer_chain)

    print("Ready!")

    questions = [
        "What is the cargo capacity of Cymbal Starlight?",
        "What's the emergency roadside assistance phone number?",
        "Are there some special kits available on that car?"
    ]

    for question in questions:
        print(f"\n=== {question} ===")
        response = rag_chain.invoke({"input": question})
        print(response['answer'])
