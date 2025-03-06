"""
Copyright 2024 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
"""

# Requires you to install Ollama 
# https://ollama.com/download/linux

import docker
import time
import ollama
from testcontainers.ollama import OllamaContainer
from docker.models.containers import Container

# Make sure you have Docker installed and running locally before running this sample

TC_OLLAMA_GEMMA2 = "tc-ollama-gemma2-2b"
GEMMA_2 = "gemma2:2b"
OLLAMA_VERSION = "ollama/ollama:0.3.12"


def create_gemma_ollama_container():
    """Creates an Ollama container with Gemma 2 if it doesn't exist."""

    client = docker.from_env()
    api_client = docker.APIClient()  # add an api client to use the commit

    try:
        # Check if the custom Gemma Ollama image exists already
        images = client.images.list(filters={"reference": TC_OLLAMA_GEMMA2})
        if not images:
            print("Creating a new Ollama container with Gemma 2 image...")
            # Create a default Ollama Container, we will need it to download the model
            ollama_container_pull = OllamaContainer(OLLAMA_VERSION)
            print("Starting Ollama...")
            ollama_container_pull.start()
            print("Pulling model...")
            ollama_container_pull.exec(["ollama", "pull", GEMMA_2])
            print("Committing to image...")
            # Commit the changes to the new image
            # Get the low level container object
            low_level_container : Container = ollama_container_pull._container
            container_id = low_level_container.id
            
            api_client.commit(container=container_id, repository=TC_OLLAMA_GEMMA2) #fixed

            ollama_container_pull.stop()  # stop the container that pulled the model
        else:
            print("Ollama image already exists")

        print("Ollama image substitution...")
        # Substitute the default Ollama image with our Gemma variant
        return OllamaContainer(TC_OLLAMA_GEMMA2)

    except Exception as e:
        print(f"An error occurred: {e}")
        raise


def main():
    start = time.time()
    ollama_container = create_gemma_ollama_container() # We are calling it ollama_container instead of ollama
    print(f"Container created in {time.time() - start:.0f}s")
    start = time.time()

    ollama_container.start() # We are calling it ollama_container instead of ollama
    print(f"Ollama container started in {time.time() - start:.0f}s")
    start = time.time()

    base_url = f"http://{ollama_container.get_container_host_ip()}:{ollama_container.get_exposed_port(11434)}" # We are calling it ollama_container instead of ollama
    # The following line was commented because the library ollama changed and it does not have the ChatLanguageModel class anymore.
    # model = OllamaChatModel.builder().base_url(base_url).model_name(GEMMA_2).timeout(120).build()

    print(f"Model ready in {time.time() - start:.0f}s")
    start = time.time()

    # The library changed, so now you have to use client.chat instead of model.generate
    client = ollama.Client(host=base_url) # This is ok, we are calling the ollama client here

    print(client.chat(model=GEMMA_2, messages=[{'role': 'user', 'content': 'Why is the sky blue?'}])['message'][
              'content'])
    print(f"First response: {time.time() - start:.0f}s")
    start = time.time()

    print(client.chat(model=GEMMA_2, messages=[{'role': 'user', 'content': 'Who was the first cat who stepped on the moon?'}])['message'][
              'content'])
    print(f"Second response: {time.time() - start:.0f}s")
    start = time.time()

    print(client.chat(model=GEMMA_2, messages=[
        {'role': 'user', 'content': 'What are the differences between the Gemini model and the Gemma models?'}])['message'][
              'content'])
    print(f"Third response: {time.time() - start:.0f}s")

    ollama_container.stop() # We are calling it ollama_container instead of ollama


if __name__ == "__main__":
    main()
