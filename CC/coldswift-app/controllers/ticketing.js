const Event = require('../models/event');
const Ticket = require('../models/ticket');
const User = require('../models/User');
const { nanoid } = require('nanoid');
// const ImgUpload = require('../middleware/imageUpload');

exports.addEvent = async (req, res) => {
    console.log(req.body); // Log the request body for debugging
    console.log(req.file); // Log the request file for debugging
    const { eventName,
        organizer,
        eventDate,
        eventLocation,
        eventDescription,
        price,
        ticketAvailable,
        eventCategory } = req.body;
        let eventImageURL = '';
        
        try {
            if (req.file && req.file.cloudStoragePublicUrl) {
                eventImageURL = req.file.cloudStoragePublicUrl
            }
            
            const newEvent = await Event.create({
                eventName,
                organizer,
                eventDate,
                eventLocation,
                eventDescription,
                price,
                ticketAvailable,
                eventCategory,
                eventImageURL
            });
            res.status(201).json({ message: 'Event created successfully'});
        } catch (err) {
            console.error(err); // Log the error for debugging
            res.status(500).json({ error: err.message });
        }
  };
  
exports.getAllEvents = async (req, res) => {
  
    try {
      const events = await Event.findAll();
      res.status(200).json(events);
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
}

exports.spesificEvent = async (req, res) => {
    const { eventId } = req.params;
  
    try {
        const event = await Event.findOne({where: {eventId}});
        res.status(200).json(event);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
}

exports.purchaseTicket = async (req, res) => {
    const { eventId } = req.params;
    const { id } = req.user;

    const ticketId = nanoid(16);
    const purchasedAt = new Date();
    
    try {
        const event = await Event.findByPk(eventId);
        if (event.ticketAvailable <= 0 ) {
            return res.status(400).json({ message: 'ticket already sold' });
        }
        
        const existingTicket = await Ticket.findOne({ where: { userId: id, eventId } });
        if (existingTicket) {
            return res.status(400).json({ error: 'User already has a ticket for this event' });
        }

        const user = await User.findByPk(id, { attributes: [ 'name', 'id' ] });

        const newTicket = Ticket.create({
            ticketId,
            eventId,
            userName: user.name ,
            userId : user.id,
            purchasedAt
        });
        
        await event.decrement('ticketAvailable', {by: 1});
        res.status(200).json({ message: 'Ticket purchased successfully' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
}

exports.getAllticket = async (req, res) => {
    const { id } = req.user;
    
    try {
        const ticket = await Ticket.findAll({where: {userId: id}, attributes: [ 'ticketId', 'eventId', 'userName', 'purchasedAt' ]});
        res.status(200).json(ticket);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
}