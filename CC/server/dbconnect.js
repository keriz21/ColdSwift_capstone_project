const mysql = require('mysql2/promise');

const connection = mysql.createPool({
  host: '34.101.47.125', // Replace with your MySQL host
  user: 'root', // Replace with your MySQL username
  password: 'coldSwift', // Replace with your MySQL password
  database: 'event-db' // Replace with your database name
});

module.exports = connection;
