# Kanban Application API Documentation

The Kanban Application follows a Client-Server model using a RESTful API. This document outlines the available endpoints, request formats, and expected responses.

## General Information

- **Base URL:** `http://localhost:3000` (Default)
- **Response Format:** All responses are returned in `application/json` format.
- **Authentication:** Protected endpoints require a valid `access_token` in the request header.

## Authentication & Authorization

For protected routes, include the `access_token` in the request headers:

```http
access_token: <your_access_token>
```

---

## User Endpoints

### Register User
Creates a new user account.

- **URL:** `/register`
- **Method:** `POST`
- **Auth required:** No
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "organization": "Organization Name"
  }
  ```
- **Note:** `organization` is optional and defaults to "Hacktiv8".
- **Success Response:**
  - **Code:** 201 Created
  - **Content:**
    ```json
    {
      "id": 1,
      "email": "user@example.com",
      "organization": "Organization Name"
    }
    ```
- **Error Responses:**
  - **Code:** 400 Bad Request (Validation errors)
  - **Code:** 500 Internal Server Error

### Login User
Authenticates a user and returns an access token.

- **URL:** `/login`
- **Method:** `POST`
- **Auth required:** No
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Success Response:**
  - **Code:** 200 OK
  - **Content:**
    ```json
    {
      "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "id": 1,
      "email": "user@example.com",
      "organization": "Hacktiv8"
    }
    ```
- **Error Responses:**
  - **Code:** 400 Bad Request (Invalid credentials)
  - **Code:** 500 Internal Server Error

---

## Board Endpoints

### Create Board
Creates a new board.

- **URL:** `/boards`
- **Method:** `POST`
- **Auth required:** Yes
- **Request Body:**
  ```json
  {
    "name": "Board Name"
  }
  ```
- **Success Response:**
  - **Code:** 201 Created
  - **Content:**
    ```json
    {
      "id": 1,
      "name": "Board Name"
    }
    ```
- **Error Responses:**
  - **Code:** 400 Bad Request (Name required)
  - **Code:** 401 Unauthorized (Invalid token)
  - **Code:** 500 Internal Server Error

### List All Boards
Retrieves all boards for the authenticated user.

- **URL:** `/boards`
- **Method:** `GET`
- **Auth required:** Yes
- **Success Response:**
  - **Code:** 200 OK
  - **Content:**
    ```json
    {
      "boards": [
        {
          "id": 1,
          "name": "Board Name"
        }
      ]
    }
    ```
- **Error Responses:**
  - **Code:** 401 Unauthorized
  - **Code:** 500 Internal Server Error

### Get Board Details
Retrieves details of a specific board, including its columns and cards.

- **URL:** `/boards/:id`
- **Method:** `GET`
- **Auth required:** Yes
- **Success Response:**
  - **Code:** 200 OK
  - **Content:**
    ```json
    {
      "board": {
        "id": 1,
        "name": "Board Name",
        "columns": [
          {
            "id": 1,
            "name": "Backlog",
            "boardId": 1,
            "cards": [
              {
                "id": 1,
                "title": "Card Title",
                "columnId": 1,
                "description": "Card Description",
                "userId": 1,
                "boardId": 1,
                "createdAt": "2023-01-01T00:00:00Z",
                "updatedAt": "2023-01-01T00:00:00Z"
              }
            ]
          }
        ]
      }
    }
    ```
- **Error Responses:**
  - **Code:** 401 Unauthorized
  - **Code:** 404 Not Found
  - **Code:** 500 Internal Server Error

---

## Column Endpoints

### Create Column
Creates a new column within a board.

- **URL:** `/columns`
- **Method:** `POST`
- **Auth required:** Yes
- **Request Body:**
  ```json
  {
    "name": "Column Name",
    "boardId": 1
  }
  ```
- **Success Response:**
  - **Code:** 201 Created
  - **Content:**
    ```json
    {
      "id": 1,
      "name": "Column Name",
      "boardId": 1
    }
    ```
- **Error Responses:**
  - **Code:** 400 Bad Request
  - **Code:** 401 Unauthorized
  - **Code:** 500 Internal Server Error

### List Columns by Board (Optional)
Note: Columns are typically retrieved via the Board Details endpoint.

### Get Column Details
Retrieves details of a specific column.

- **URL:** `/columns/:id`
- **Method:** `GET`
- **Auth required:** Yes
- **Success Response:**
  - **Code:** 200 OK
  - **Content:**
    ```json
    {
      "column": {
        "id": 1,
        "name": "Column Name",
        "boardId": 1
      }
    }
    ```

### Update Column
Updates an existing column.

- **URL:** `/columns/:id`
- **Method:** `PUT`
- **Auth required:** Yes
- **Request Body:**
  ```json
  {
    "name": "Updated Column Name"
  }
  ```
- **Success Response:**
  - **Code:** 200 OK
  - **Content:**
    ```json
    {
      "column": {
        "id": 1,
        "name": "Updated Column Name",
        "boardId": 1
      }
    }
    ```

### Delete Column
Deletes a column by ID.

- **URL:** `/columns/:id`
- **Method:** `DELETE`
- **Auth required:** Yes
- **Success Response:**
  - **Code:** 200 OK
  - **Content:**
    ```json
    {
      "msg": "Column has been successfully deleted!"
    }
    ```

---

## Card Endpoints

### Create Card
Creates a new card within a board.

- **URL:** `/cards`
- **Method:** `POST`
- **Auth required:** Yes
- **Request Body:**
  ```json
  {
    "title": "Card Title",
    "description": "Card Description",
    "columnId": 1,
    "boardId": 1
  }
  ```
- **Success Response:**
  - **Code:** 201 Created
  - **Content:**
    ```json
    {
      "card": {
        "id": 1,
        "title": "Card Title",
        "columnId": 1,
        "description": "Card Description",
        "boardId": 1,
        "userId": 1,
        "createdAt": "2023-01-01T00:00:00Z",
        "updatedAt": "2023-01-01T00:00:00Z"
      }
    }
    ```
- **Error Responses:**
  - **Code:** 400 Bad Request
  - **Code:** 401 Unauthorized
  - **Code:** 404 Not Found
  - **Code:** 500 Internal Server Error

### Get Card Details
Retrieves details of a specific card.

- **URL:** `/cards/:id`
- **Method:** `GET`
- **Auth required:** Yes
- **Success Response:**
  - **Code:** 200 OK
  - **Content:**
    ```json
    {
      "card": {
        "id": 1,
        "title": "Card Title",
        "columnId": 1,
        "description": "Card Description",
        "userId": 1,
        "boardId": 1,
        "createdAt": "2023-01-01T00:00:00Z",
        "updatedAt": "2023-01-01T00:00:00Z"
      }
    }
    ```
- **Error Responses:**
  - **Code:** 401 Unauthorized
  - **Code:** 404 Not Found
  - **Code:** 500 Internal Server Error

### Update Card
Updates an existing card.

- **URL:** `/cards/:id`
- **Method:** `PUT`
- **Auth required:** Yes
- **Request Body:**
  ```json
  {
    "title": "Updated Card Title",
    "columnId": 1,
    "description": "Updated Card Description"
  }
  ```
- **Success Response:**
  - **Code:** 200 OK
  - **Content:**
    ```json
    {
      "card": {
        "id": 1,
        "title": "Updated Card Title",
        "columnId": 1,
        "description": "Updated Card Description",
        "userId": 1,
        "boardId": 1,
        "createdAt": "2023-01-01T00:00:00Z",
        "updatedAt": "2023-01-01T00:00:00Z"
      }
    }
    ```
- **Error Responses:**
  - **Code:** 401 Unauthorized
  - **Code:** 404 Not Found
  - **Code:** 500 Internal Server Error

### Delete Card
Deletes a card by ID.

- **URL:** `/cards/:id`
- **Method:** `DELETE`
- **Auth required:** Yes
- **Success Response:**
  - **Code:** 200 OK
  - **Content:**
    ```json
    {
      "msg": "Card has been successfully deleted!"
    }
    ```
- **Error Responses:**
  - **Code:** 401 Unauthorized
  - **Code:** 404 Not Found
  - **Code:** 500 Internal Server Error
