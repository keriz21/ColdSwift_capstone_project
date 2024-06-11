const { DataTypes } = require('sequelize');
const sequelize = require('../config/db');

const Event = sequelize.define('Event', {
  eventId: {
    type: DataTypes.INTEGER,
    allowNull: true,
    primaryKey: true,
    autoIncrement: true
  },
  eventName: {                
    type: DataTypes.STRING, 
    allowNull: false
  },
  organizer: {                
    type: DataTypes.STRING, 
    allowNull: false
  },
  eventDate: {                
    type: DataTypes.DATE, 
    allowNull: false
  },
  eventLocation: {                
    type: DataTypes.STRING, 
    allowNull: false
  },
  eventDescription: {                
    type: DataTypes.STRING, 
    allowNull: false
  },
  ticketAvailable: {                
    type: DataTypes.INTEGER, 
    allowNull: false
  },
  price: {                
    type: DataTypes.DECIMAL, 
    allowNull: false
  },
  eventCategory: {
    type: DataTypes.STRING,
    allowNull: false
  },
  eventImageURL: {
    type: DataTypes.STRING,
    allowNull: false
  }
});

module.exports = Event;