const express = require('express');
const bcrypt = require('bcrypt');
require('firebase/auth');
const firebase = require('firebase/app');
const admin = require('./firebaseAdmin');
const firebaseConfig = require('./firebaseConfig');

firebase.initializeApp(firebaseConfig);

const app = express();
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

app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
});

