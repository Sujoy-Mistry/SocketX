# 🧠 HTTP Server from Scratch - Java Learning Roadmap

Build your own HTTP server in Java from absolute scratch. This plan will teach you how web servers, sockets, HTTP, and routing actually work — the stuff frameworks like Spring Boot abstract away.

---

## ✅ Week-Long Roadmap

### 🗓️ Day 1: Server Bootstrapping

#### Goal:

Start a basic Java server that listens on a port and accepts client connections.

#### Tasks:

* Use `ServerSocket` to bind to a port (e.g., 8080)
* Accept incoming `Socket` connections in a loop
* Read raw data from `InputStream`
* Print incoming HTTP request line and headers to the console

#### What You'll Learn:

* How TCP sockets work
* The difference between `ServerSocket` and `Socket`
* How HTTP requests arrive as text over a TCP connection

---

### 🗓️ Day 2: Parse Requests

#### Goal:

Manually parse the HTTP request line and headers.

#### Tasks:

* Read the HTTP request line (e.g., `GET /index.html HTTP/1.1`)
* Extract the HTTP method, path, and version
* Read headers line by line into a `Map<String, String>`
* Detect the end of headers with `\r\n\r\n`

#### What You'll Learn:

* The structure of HTTP requests
* How to parse textual data into structured objects
* How clients send headers like `Host`, `User-Agent`, etc.

---

### 🗓️ Day 3: Serve Static Files

#### Goal:

Serve `.html`, `.css`, `.js`, or image files from disk.

#### Tasks:

* Create a `/static/` folder in your project
* On `GET /index.html`, read and serve `static/index.html`
* Add `Content-Type` header based on file extension
* Handle file-not-found cases with a `404 Not Found` response

#### What You'll Learn:

* How browsers request static content
* How to use Java File I/O (`Files.readAllBytes()`)
* How to map file extensions to MIME types

---

### 🗓️ Day 4: Add Basic Routing & APIs

#### Goal:

Serve simple API responses for different endpoints.

#### Tasks:

* For `GET /api/hello`, return:

  ```json
  {"message": "Hello, world!"}
  ```
* Write simple routing logic to detect `/api/*` paths
* Respond with JSON string and set `Content-Type: application/json`
* Send appropriate status codes and headers

#### What You'll Learn:

* How API routing works under the hood
* How to manually construct JSON and HTTP responses
* Importance of HTTP status codes

---

### 🗓️ Day 5: Handle Multiple Clients

#### Goal:

Enable your server to handle concurrent clients.

#### Tasks:

* Create a new `Thread` for each client using `Runnable`
* (Optional) Use `ExecutorService` with a fixed thread pool
* Ensure one slow client doesn't block others

#### What You'll Learn:

* Basics of multithreading in Java
* How real servers handle concurrency
* Why thread pools are preferred over raw threads

---

### 🗓️ Day 6: Logging and Error Handling

#### Goal:

Add structured logs and robust error handling to your server.

#### Tasks:

* Use `java.util.logging.Logger` for log output
* Log every request method, path, and status code
* Send `500 Internal Server Error` for exceptions
* Improve your `404 Not Found` handling

#### What You'll Learn:

* How logging helps in debugging
* How to write meaningful error messages
* How HTTP status codes signal errors to clients

---

### 🗓️ Day 7: Caching & Header Handling

#### Goal:

Improve performance and polish by adding proper headers.

#### Tasks:

* Add `Cache-Control: max-age=3600` for static files
* Set correct `Content-Length` for responses
* Add standard headers like `Date` and `Server`
* (Optional) Support query parameters like `/api/greet?name=Sujoy`

#### What You'll Learn:

* Browser caching behavior
* Importance of response headers
* How to manually compute content length
* How query parameters are parsed in raw HTTP

---

## 🧰 Tools and Concepts You'll Learn

* Java `ServerSocket`, `Socket`
* HTTP/1.1 protocol parsing
* Multithreading and concurrency
* File handling and MIME types
* Routing and JSON serialization
* Logging and error management
* HTTP headers and caching

---

## 🚀 Bonus Ideas

* Add support for POST requests
* Implement simple request body parsing
* Use a thread pool instead of new thread per request
* Add command-line arguments to specify port or root folder
* Build a mini-router using regex for paths like `/api/user/\d+`

---

Happy coding! Build it, break it, fix it — and understand how the web really works under the hood.
