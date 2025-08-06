# MCP Server (Model Context Protocol with GraphQL)

This project demonstrates a Spring Boot application that integrates the Model Context Protocol (MCP) with a GraphQL API. It serves as a backend for managing authors and books, exposing its functionalities as "tools" that can be invoked by Large Language Models (LLMs) via the MCP.

## Features

*   **GraphQL API**: Exposes GraphQL queries and mutations for managing `Author` and `Book` entities.
*   **Model Context Protocol (MCP) Integration**: Allows LLMs to interact with the application's functionalities ("tools") over SSE.
*   **Tool Calling for LLMs**: GraphQL operations are exposed as callable tools for an LLM using Spring AI's `@Tool` annotations.
*   **Database Persistence**: Uses Spring Data JPA with MySQL and Liquibase for database management.
*   **Reactive Web (WebFlux)**: Utilizes Spring WebFlux for its SSE transport, providing a non-blocking and efficient communication channel for MCP.

## Technologies Used

*   **Spring Boot**: Main application framework.
    *   `spring-boot-starter-webflux`: Reactive web stack for SSE.
    *   `spring-boot-starter-graphql`: For building GraphQL APIs.
    *   `spring-boot-starter-data-jpa`: For database interaction.
*   **Model Context Protocol (MCP) SDK**: `mcp-bom`, `mcp-spring-webflux`
*   **Spring AI**: `spring-ai-bom`, `spring-ai-model`, `spring-ai-mcp` (for tool calling integration).
*   **MySQL**: Relational database.
*   **Liquibase**: Database schema migration tool.
*   **Lombok**: Reduces boilerplate code (getters, setters, constructors).

## Prerequisites

Before running this application, ensure you have the following installed:

*   **Java 17**
*   **Maven**
*   **MySQL Server**: Running instance of MySQL.
*   **Postman** or any other API client for testing.

## Getting Started

Follow these steps to set up and run the application locally:

### 1. Clone the Repository

```bash
git clone https://github.com/anastasiia-peleshok/new-mcp-server.git
cd new-mcp-server
```

### 2. Database Setup

1.  **Create a MySQL Database**:
    Create a new database for this application (e.g., `mcp_db`).

    ```sql
    CREATE DATABASE mcp_server;
    ```

2.  **Configure Application Properties**:
    Open `src/main/resources/application.properties` and configure your MySQL database connection details. Replace `your_username` and `your_password` with your MySQL credentials.

    ```properties
    # MySQL Database Configuration
    spring.datasource.url=jdbc:mysql://localhost:3306/mcp_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

    # JPA Configuration
    spring.jpa.hibernate.ddl-auto=none # Liquibase will handle schema
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

    # Liquibase Configuration
    spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml

    # MCP Transport Configuration (choose one: stdio or sse)
    # For HTTP/SSE communication (recommended for client interaction)
    transport.mode=sse
    # For standard input/output communication (e.g., for direct process interaction)
    #transport.mode=stdio
    ```

    **Note**: The `transport.mode=sse` property is crucial for enabling the SSE endpoint for MCP communication.

### 3. Run the Application

You can run the application using Maven:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## Usage

### 1. GraphQL Endpoint

The application exposes a GraphQL endpoint at `http://localhost:8080/graphql`. You can send `POST` requests to this endpoint for executing GraphQL queries and mutations.

**Example GraphQL Query (using Postman or curl):**

*   **URL**: `http://localhost:8080/graphql`
*   **Method**: `POST`
*   **Headers**:
    *   `Content-Type`: `application/json`
*   **Body (raw JSON)**:

    ```json
    query { listAuthors { id firstName lastName } }

**Example GraphQL Mutation (using Postman or curl):**

*   **URL**: `http://localhost:8080/graphql`
*   **Method**: `POST`
*   **Headers**:
    *   `Content-Type`: `application/json`
*   **Body (raw JSON)**:

    ```json
      mutation { createAuthor(firstName: "John", lastName: "Doe") { id firstName lastName } }
    ```

### 2. MCP (Model Context Protocol) Endpoint

The MCP server endpoint for SSE communication is located at `http://localhost:8080/mcp/message`.

This endpoint is designed for LLMs (Large Language Models) or other MCP-compatible clients to connect and invoke the exposed "tools". These tools are your GraphQL queries and mutations, annotated with `@Tool` (from Spring AI), allowing the client to understand what requests it can make and with what parameters.

### Available MCP Tools

Here is a list of the GraphQL operations exposed as tools for the MCP server:

**Query Tools:**
*   `getAuthor`: Get author by ID
*   `getBook`: Get book by ID
*   `listAuthors`: List all authors
*   `listBooks`: List all books
*   `getBooksByAuthor`: Get books by author ID
*   `getAuthorsByBook`: Get authors by book ID

**Mutation Tools:**
*   `createAuthor`: Create and save new author
*   `updateAuthor`: Update existing author
*   `deleteAuthor`: Delete author by ID
*   `createBook`: Create and save new book
*   `updateBook`: Update existing book
*   `deleteBook`: Delete book by ID
*   `addAuthorToBook`: Add author to book
*   `removeAuthorFromBook`: Remove author from book
*   `addBookToAuthor`: Add book to author
*   `removeBookFromAuthor`: Remove book from author

--- 