# ReadMe

# CHAT ROOM

---

# messages-controller

---

## Post new message

### Request

    URL: /messages/save
    Method: POST
    Headers:
        Content-Type: application/json
        Accept: */*
    Body:
        "message text"

### Response

    Status Code: 201 Created
    Body:
        Message saved successfully

---

## Get all messages

### Request

    URL: /messages
    Method: GET

### Response

    Status Code: 200 OK
    Body:
        [
            {
                "id": 1,
                "account": {
                "id": 1,
                "name": "admin",
                "role": "ADMIN"
                },
                "content": "\"string\"",
                "timestamp": "2025-07-27T10:20:08.176093"
            }
        ]

---

# admin-controller

---

## Register new account

### Request

    URL: /admin/register
    Method: POST
    Headers:
        Content-Type: application/json
        Accept: */*
    Body:
        {
            "id": 1,
            "name": "admin1",
            "role": "ADMIN"
        }

### Response

    Status Code: 200 OK
    Body:
        {}

---

## Get all accounts

### Request

    URL: /admin
    Method: GET

### Response

    Status Code: 200 OK
    Body:
        [
            {
                "id": 0,
                "name": "string",
                "role": "ACCOUNT"
            }
        ]

---

## Get statistics

### Request

    URL: /admin/stats
    Method: GET

### Response

    Status Code: 200 OK
    Body:
        []

---

## Delete account

### Request

    URL /admin/account/{id}
    Method: DELETE

### Response Body

    Status: 200 OK
    Body:
        {}

---