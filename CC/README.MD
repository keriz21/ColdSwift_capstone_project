1. Register
- URL : http://localhost:5000/api/users/register
- Method : POST
- Request Body : 
   * nik as string, unique: true
   * name as string
   * email as string, unique: true
   * password as string, minimal 8 karakter
- Response
   * Kalau NIK belum terdaftar
{
    "id": 1,
    "nik": "1234567890123897",
    "name": "John Week",
    "email": "johnweek@gmail.com",
    "password": "$2a$10$9nOvxiI.UBfwhQPDyidEa.gAC1Nztm8fuU4pcdRpdHMEOHp3QLOZG",
    "updatedAt": "2024-06-05T06:48:25.417Z",
    "createdAt": "2024-06-05T06:48:25.417Z"
}

   * Kalau NIK sudah terdaftar
{
    "message": "NIK already registered"
}

   * Kalau Password tidak 8 karakter
{
    "message": "Password must be at least 8 characters long"
}


2. Login
- URL : http://localhost:5000/api/users/login
- Method : POST
- Request Body : 
   * name as string
   * email as string
   * password as string
- Response
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWF0IjoxNzE3NTcwNTA5LCJleHAiOjE3MTc1NzQxMDl9.nZcq8BvH-qQnQAhjlZ1IQEJnc7u3TNcx2x8Db3kRaM4"
}


3. Forget Password
- URL : http://localhost:5000/api/users/forget-password
- Method : POST
- Request Body : 
   * name as string
   * email as string
   * newpassword as string
- Response
{
    "message": "Password updated successfully"
}


4. View Profile
- URL : http://localhost:5000/api/users/profile
- Method : GET
- Request Body : 
   Type : Bearer Token (didapat pas login)
- Response
{
    "nik": "1234567890123897",
    "name": "John Week",
    "email": "johnweek@gmail.com"
}

5. Add Event
- URL : http://localhost:5000/addEvent
- Method : POST
- Request Body : 
   * eventName as string
   * organizer as string
   * eventDate as date
   * eventLocation as string
   * eventDescription as string
   * ticketAvailable as integer
   * price as decimal
   * eventCategory as string
   * eventImageURL as string
- Response
{
   message: 'Event created successfully'
}

6. List all events
- URL : http://localhost:5000/events
- Method : GET
- Response
{
   [
    {
        "eventId": 1,
        "eventName": "lorem ipsum",
        "organizer": "lorem ipsum",
        "eventDate": "2024-12-12T00:00:00.000Z",
        "eventLocation": "lorem ipsum",
        "eventDescription": "lorem ipsum",
        "ticketAvailable": 99,
        "price": "100000.00",
        "eventCategory": "music",
        "eventImageURL": "lorem ipsum",
        "createdAt": "2024-06-07T09:42:41.000Z",
        "updatedAt": "2024-06-07T09:42:54.000Z"
    },
    {
        "eventId": 2,
        "eventName": "lorem ipsum",
        "organizer": "lorem ipsum",
        "eventDate": "2024-12-12T00:00:00.000Z",
        "eventLocation": "lorem ipsum",
        "eventDescription": "lorem ipsum",
        "ticketAvailable": 100,
        "price": "100000.00",
        "eventCategory": "music",
        "eventImageURL": "",
        "createdAt": "2024-06-10T12:05:37.000Z",
        "updatedAt": "2024-06-10T12:05:37.000Z"
    }
   ]
}

7. List spesific event
- URL : http://localhost:5000/events/:eventId
- Method : GET
- Response
{
   "eventId": eventId,
    "eventName": "lorem ipsum",
    "organizer": "lorem ipsum",
    "eventDate": "2024-12-12T00:00:00.000Z",
    "eventLocation": "lorem ipsum",
    "eventDescription": "lorem ipsum",
    "ticketAvailable": 99,
    "price": "100000.00",
    "eventCategory": "music",
    "eventImageURL": "lorem ipsum",
    "createdAt": "2024-06-07T09:42:41.000Z",
    "updatedAt": "2024-06-07T09:42:54.000Z"
}

8. Purchase a ticket
- URL : http://localhost:5000/events/:eventId/purchase
- Method : POST
- Request Body : 
   Type : Bearer Token (didapat pas login)
- Response
{
   message: 'Ticket purchased successfully'
}

9. Purchase a ticket
- URL : http://localhost:5000/mytickets
- Method : GET
- Request Body : 
   Type : Bearer Token (didapat pas login)
- Response
{
   [
    {
        "ticketId": "wJ6N6FasOuTRbCTf",
        "eventId": 1,
        "userName": "nic",
        "purchasedAt": "2024-06-07T09:42:54.000Z"
    }
]
}