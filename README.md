# 🚀 REST API Installation & Testing Guide

Follow these instructions to set up and test the REST API.

---

## 📋 Prerequisites

Before testing the endpoints, make sure you have the following installed:

* **[Postman](https://www.postman.com/downloads/)** – required to test the API endpoints.
* **[MongoDB Compass](https://www.mongodb.com/try/download/compass)** – optional, recommended for viewing database documents in a GUI. Skip if you prefer using the MongoDB shell.

---

## 1️⃣ Get the Code

Clone the repository to your local machine:

* **HTTPS:**
```bash
git clone https://github.com/simonkod2021/simon-security.git
```

* **GitHub CLI:**
```bash
gh repo clone simonkod2021/simon-security
```

* Or download as a [ZIP file](https://github.com/simonkod2021/simon-security-server/archive/refs/heads/main.zip)

---

## 2️⃣ Open the Project

Open the cloned `simon-security` directory in your favorite IDE (e.g., IntelliJ IDEA, VS Code, Eclipse).  

> 💡 Recommended: IntelliJ IDEA, but any IDE works.

---

## 3️⃣ Test the Endpoints

1. ✅ Make sure the application is running. You should see something like this:

![bild](https://github.com/user-attachments/assets/cbffe256-2acc-4b09-acb7-703ba154bde4)
![bild](https://github.com/user-attachments/assets/7648cd7b-23b3-4519-a371-d48912ea966f)

2. 🌐 Open the Postman client.

3. 🔍 Test the endpoints below using the **POST** method.

---

### 👤 User Registration

* **Endpoint:** `http://localhost:8080/api/auth/signup`  
* **Method:** `POST`  
* **Body (raw JSON):**
```json
{
  "username": "your_username",
  "password": "your_password"
}
```

---

### 🔑 User Login

* **Endpoint:** `http://localhost:8080/api/auth/signin`  
* **Method:** `POST`  
* **Body (raw JSON):**
```json
{
  "username": "your_username",
  "password": "your_password"
}
```

---

### 📝 Create a Blogpost

* **Endpoint:** `http://localhost:8080/api/auth/blogposts/create`  
* **Method:** `POST`  
* **Body (raw JSON):**
```json
{
  "title": "your blogpost title",
  "content": "your blogpost content"
}
```

---

### 🖊 Add a Comment

* **Endpoint:** `http://localhost:8080/api/auth/comments/{id}`  
  > Replace `{id}` with the blogpost ID  
* **Method:** `POST`  
* **Body (raw JSON):**
```json
{
  "text": "your comment"
}
```

---

**🎉 Happy Testing!**
