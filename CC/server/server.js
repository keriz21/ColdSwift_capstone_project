const express = require("express");
const bodyParser = require('body-parser');
const admin = require("./firebaseAdmin.js");
const firebase = require("./firebaseConfig.js");
const app = express();

app.use(express.json());

app.use(express.urlencoded({extended: true}));

app.use(bodyParser.json());

app.post('/signup', async (req, res) => {
  const { email, password } = req.body;
  try {
    const user = await firebase.auth().createUserWithEmailAndPassword(email, password);
    res.status(201).send(user);
  } catch (error) {
    res.status(400).send(error.message);
  }
})

app.post('/signin', async (req, res) => {
  const { email, password } = req.body;
  try {
    const user = await firebase.auth().signInWithEmailAndPassword(email, password);
    const idToken = await user.user.getIdToken();
    res.status(200).send({ idToken });
  } catch (error) {
    res.status(400).send(error.message);
  }
});

const verifyToken = async (req, res, next) => {
  const idToken = req.headers.authorization;
  try {
    const decodedToken = await admin.auth().verifyIdToken(idToken);
    req.user = decodedToken;
    next();
  } catch (error) {
    res.status(401).send('Unauthorized');
  }
};

app.get('/protected', verifyToken, (req, res) => {
  res.send(`Hello ${req.user.email}, you are authenticated!`);
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});