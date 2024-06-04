const express = require('express');
// const bcrypt = require('bcrypt');
require('firebase/auth');
const bodyParser = require('body-parser');
const firebase = require('firebase/app');
const admin = require('./firebaseAdmin');
const firebaseConfig = require('./firebaseConfig');
const db = require('./dbconnect');


firebase.initializeApp(firebaseConfig);

const app = express();
app.use(bodyParser.json());
const port = process.env.PORT || 3000; // Use environment variable for port

// Middleware for parsing JSON request body
app.use(express.json());

// Registration route (POST)
app.post('/register', async (req, res) => {
    const { NIK, email, password } = req.body;

    try {
        // const existingUser = await admin.auth().getUserByEmail(NIK); // Modify if using a different identifier
        
        // // Check for existing user with the identifier
        // if ( NIK === undefined || NIK.length !== 16 ){
        //     return res.status(409).json({message: 'NIK tidak sesuai'});
        // }else if (existingUser) {
        //   return res.status(409).json({ message: 'User with this identifier already exists' });
        // }
    
        // If no existing user, create a new user
        const user = await admin.auth().createUser({
          email,
          NIK,
          password
        });
        res.status(201).json({ message: 'User created successfully' });

    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Error creating user' });
    }
});

// // Login route (POST)
// app.post('/login', async (req, res) => {
//     const { email, password } = req.body;

//     try {
//         const user = await admin.auth().signInWithEmailAndPassword(email, password);
//         const token = await user.user.getIdToken(); // Generate a Firebase ID token
//         res.json({ message: 'Login successful', token });
//     } catch (error) {
//         console.error(error);
//         res.status(401).json({ message: 'Invalid username or password' });
//     }
// });

app.post('/login', async (req, res) => {
    const { email, password } = req.body;
    try {
      const userRecord = await admin.auth().getUserByEmail(email);
      const token = await admin.auth().createCustomToken(userRecord.uid);
      res.status(200).send({ token });
    } catch (error) {
      res.status(400).send(error);
    }
});

app.post('/addEvent', async (req, res) => {
  
  const { eventName,
    organizer,
    eventDate,
    eventLocation,
    eventDescription,
    price,
    ticketAvailable, eventImageURL } = req.body;
  // let eventImageURL = '';
  
  if ( !eventName || !organizer || !eventDate || !eventLocation || !eventDescription || !price || !ticketAvailable || !eventImageURL ) {
    if (!eventName ) {
      return res.status(400).json({ message: 'eventName' });  
    }else if (!organizer ) {
      return res.status(400).json({ message: 'organizer' });  
    }else if (!eventDate ) {
      return res.status(400).json({ message: 'eventDate' });  
    }else if (!eventLocation ) {
      return res.status(400).json({ message: 'eventLocation' });  
    }else if (!eventDescription ) {
      return res.status(400).json({ message: 'eventDescription' });  
    }else if (!price ) {
      return res.status(400).json({ message: 'price' });  
    }else if (!ticketAvailable ) {
      return res.status(400).json({ message: 'ticketAvailable' });  
    }else if (!eventImageURL ) {
      return res.status(400).json({ message: 'eventImageURL' });  
    }
    return res.status(400).json({ message: 'Missing required fields' });
  }
  
  const conn = await db.getConnection();

  try {
    const sql = "INSERT INTO events (eventName, organizer, eventDate, eventLocation, eventDescription, price, ticketAvailable, eventImageURL) values (?, ?, ?, ?, ?, ?, ?, ?)"
    const values = [eventName,
      organizer,
      eventDate,
      eventLocation,
      eventDescription,
      price,
      ticketAvailable,
      eventImageURL
    ];

    await conn.query(sql, values);
    res.status(201).json({ message: 'Product created successfully' });
    
  } catch (error) {
    await conn.rollback();
    throw error; // Re-throw the error for handling in the outer catch block
  }
  //   res.status(500).json({ message: 'Internal server error' }); // Generic error message for the client
  // }
})

app.get('/events', async (req, res) => {
  const conn = await db.getConnection();

  try {
    const [ events ] = await db.query('SELECT * FROM events'); // Replace with your product table name
    res.status(200).json(events);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal server error' });
  }
})

app.get('/events/:eventId', async (req, res) => {
  const conn = await db.getConnection();

  try {
    const { eventId } = req.params;
    const [ event ] = await db.query('SELECT * FROM events WHERE eventId = ?', eventId);

    if ( event.length === 0 ) {
      return res.status(400).json({ message: "event didn't exist" });
    }
    res.status(200).json(event);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal server error' });
  }
})


app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
});

