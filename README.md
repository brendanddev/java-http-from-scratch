# HTTP Server from Scratch in Java

A lightweight HTTP server built entirely from Scratch in Java - No external web frameworks. The goal of this project is to continue expanding on my understanding of how HTTP works under the hood by implementing request parsing, routing, and response handling manually.

---

## Features

- **Custom HTTP Server**: Handles GET and POST requests with manually parsed headers and request bodies.
- **Routing System**: Register routes and handlers for specific HTTP methods and paths.
- **Multithreading Support**: Can handle multiple clients simultaneously using Java threads.
- **Simple HTTP Client**: A companion client implemented from scratch using sockets to test the server.
- **JUnit Tests**: Includes unit tests for functionality and stress tests to simulate multiple concurrent clients.
- **HTML Templates**: Serves static HTML files for basic web pages.

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven (for building and running the project)

### Running the Server

```bash
mvn compile exec:java -Dexec.mainClass="brendanddev.HttpServer"
```

### Testing the Server

```bash
mvn test
```

## Using the Client