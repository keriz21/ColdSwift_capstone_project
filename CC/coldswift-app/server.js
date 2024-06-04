// server.js
const express = require('express');
const sequelize = require('./config/db');
const userRoutes = require('./routes/userRoutes');
require('dotenv').config();

const app = express();

app.use(express.json());

// Tambahkan route untuk root URL
app.get('/', (req, res) => {
    res.send('Welcome to the coldswift-app API');
  });

app.use('/api/users', userRoutes);

const PORT = process.env.PORT || 5000;

sequelize.sync().then(() => {
    app.listen(PORT, () => console.log(`Server running at http://localhost:${PORT}`));
}).catch(err => console.log(err));
