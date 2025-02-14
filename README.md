# Java Web Server and IoC Framework with POJO Support

This project is a Java application that includes a custom HTTP server, annotation-based routing, and a simple testing framework. The application is structured to handle HTTP requests and serve static files, with a custom annotation-based framework for defining endpoints and testing functionality.

## Project Architecture

1. **Annotations:**
   - `@GetMapping`: Defines HTTP GET request mappings for methods.
   - `@RequestParam`: Specifies request parameters for methods.
   - `@RestController`: Marks a class as a REST controller that handles HTTP requests.
     
2. **Components:**
   - **HelloService**: A REST controller that provides various endpoints for different functionalities (e.g., time, greeting, UUID).
   - **SimpleWebServer**: A basic HTTP server that listens for incoming requests, handles them, and serves static files or responses based on defined routes.
   - **ClientHandler**: A utility class for invoking methods in a service class based on a URL, demonstrating parameter extraction from query strings.
   - **StaticFiles**: A simple class made for getting the files from the resources directory and other required files from the server.

## Running the Project

To run different components of the project, use the following commands:
1. **Running the SpringECI Utility:**

   ```bash
   java -cp target/webserver-1.0-SNAPSHOT.jar:. arep.webserver.SimpleWebServer arep.webserver.HelloService
   ```
   This command invokes methods in `HelloService` based on the URL provided in the code. Modify the URL in the `SimpleWebServer` class as needed.

   ![image](https://github.com/user-attachments/assets/776d6afc-7480-46e1-97b8-f81c9bbea70c)

## Examples of usage

   ![image](https://github.com/user-attachments/assets/4dd6659b-52fb-4a84-94ae-519b16301f5d)
   
   ![image](https://github.com/user-attachments/assets/371a378c-316b-4b35-b684-676666f03987) ![image](https://github.com/user-attachments/assets/7dcd98f9-1556-4811-9548-750ba344e0e0)
   
   ![image](https://github.com/user-attachments/assets/87f256f4-15ea-4f6e-89e3-be4321fc5658)


## Cloning the Repository

To clone the repository and set up the project locally, use the following commands:

### Installation

1. **Clone the Repository**
   ```sh
   git clone https://github.com/Sana1610/Arep-03.git  
   ```

2. **Compile the Project**

   Open the project in Visual Studio Code or your preferred IDE.

   **Visual Studio Code:**

    - Open the project folder.
    - Open the integrated terminal.
    - Compile the project, you can use any Java IDE to import and build the project.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Maven (for building the project)

## Author
This project was developed by Santiago Alberto Naranjo Abril https://github.com/Sana1610.

