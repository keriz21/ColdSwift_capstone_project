CREATE TABLE events ( 
    eventId INT AUTO_INCREMENT PRIMARY KEY,
    eventName VARCHAR(50),
    organizer VARCHAR(30),
    eventDate DATETIME,
    eventLocation VARCHAR(50),
    eventDescription TEXT,
    ticketAvailable INT,
    price decimal(12,2),
    eventImageURL VARCHAR(255)
);