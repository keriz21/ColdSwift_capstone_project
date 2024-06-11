// server.js
const express = require('express');
const sequelize = require('./config/db');
const userRoutes = require('./routes/userRoutes');
const eventRoutes = require('./routes/eventRoutes');
const Event = require('./models/event');
const Ticket = require('./models/ticket');
const User = require('./models/User');
require('dotenv').config();

const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

Event.hasMany(Ticket, { foreignKey: 'eventId' });
Ticket.belongsTo(Event, { foreignKey: 'eventId' });

User.hasMany(Ticket, { foreignKey: 'userId' });
Ticket.belongsTo(User, { foreignKey: 'userId' });

// Tambahkan route untuk root URL
app.get('/', (req, res) => {
    res.send('Welcome to the coldswift-app API');
  });

app.use('/api/users', userRoutes);
app.use('/', eventRoutes);

const PORT = process.env.PORT || 5000;

sequelize.sync().then(() => {
    app.listen(PORT, () => console.log(`Server running at http://localhost:${PORT}`));
}).catch(err => console.log(err));
