# REST API Testing Guide

Follow these instructions to set up and test the REST API.

## Prerequisites

*   [Postman](https://www.postman.com/downloads/) installed on your machine.

## 1. Get the Code

Clone the repository to your local machine using one of these methods:

*   **HTTPS:**
    ```bash
    git clone https://github.com/simonkod2021/simon-security.git
    ```
*   **Github CLI:**
    ```bash
    gh repo clone simonkod2021/simon-security
    ```

## 2. Open the Project

Open the cloned `simon-security` directory in your favorite IDE (e.g., IntelliJ IDEA, VS Code, Eclipse).

## 3. Test the Endpoints

1.  Ensure the application is running (it should start on `localhost:8080`).
2.  Open the Postman client.
3.  Test the following endpoints using the **POST** method.

### User Registration

*   **Endpoint:** `http://localhost:8080/api/auth/signup`
*   **Method:** `POST`
*   **Body (raw JSON):**
    ```json
    {
      "username": "your_username",
      "password": "your_password"
    }
    ```

### User Login

*   **Endpoint:** `http://localhost:8080/api/auth/signin`
*   **Method:** `POST`
*   **Body (raw JSON):**
    ```json
    {
      "username": "your_username",
      "password": "your_password"
    }
    ```
