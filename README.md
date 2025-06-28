# SocketX
# 🔌 SocketX - A Lightweight Java HTTP Server

**SocketX** is a custom-built HTTP server written in pure Java using raw `ServerSocket` and `Socket`. It mimics the behavior of lightweight frameworks like Flask and Spring Boot, while giving you full control over request parsing, routing, and response formatting — all without any external web frameworks.

---

## 🚀 Features

* ✅ TCP connection handling via `ServerSocket`
* ✅ HTTP request parsing (method, path, version, headers)
* ✅ Static file serving (`.html`, `.css`, `.js`, images)
* ✅ Annotation-based API routing (`@Get`, `@Post`, etc.)
* ✅ Query and path parameter support
* ✅ JSON response generation
* ✅ HTTP header management (`Date`, `Server`, `Cache-Control`, etc.)
* ✅ Multithreading for handling multiple client connections
* ✅ Reflection-based controller discovery

---

## 📁 Folder Structure

```
src/
 └── main/
     ├── java/
     │   └── org/example/socketx/
     │       ├── Main.java
     │       ├── ServerUtil.java
     │       ├── annotations/
     │       │   ├── Get.java
     │       │   └── Post.java
     │       ├── controller/
     │       │   └── RequestController.java
     │       ├── config/
     │       │   └── RouteRegistrar.java
     │       └── ...
     └── resources/
         ├── application.properties
         └── static/
             ├── index.html
             └── style.css
```

---

## ⚙️ Setup & Run

```bash
git clone https://github.com/your-username/SocketX.git
cd SocketX
mvn clean install
java -jar target/SocketX.jar
```

Configure port in `application.properties`:

```properties
server.port=8080
```

---

## 🛠️ Technologies Used

* Java 21
* Maven
* `java.net.ServerSocket`, `Socket`
* `BufferedReader`, `OutputStream`
* Java Reflection API
* Reflections library (for scanning annotated classes)

---

## 🌐 Example API Usage

### Request

```
GET /api/greet?name=Sujoy HTTP/1.1
```

### Response

```
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 33
Date: Mon, 01 July 2025 10:00:00 GMT
Server: SocketX/1.0
Cache-Control: max-age=3600

{"message": "Hello, Sujoy!"}
```


---
## 🚧 Future Enhancements

* [ ] Support for file uploads via `multipart/form-data`
* [ ] Recursive static folder routing
* [ ] Basic template engine (e.g., `{{name}}` replacements)
* [ ] CLI interface to configure and start the server
* [ ] Dockerize and deploy on cloud VM

---

## 🙌 Author

**Sujoy**
Java Backend Engineer | Spring Boot | Kafka | Distributed Systems

---

