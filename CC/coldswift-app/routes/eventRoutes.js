const express = require('express');
const { addEvent, getAllEvents, spesificEvent, purchaseTicket, getAllticket } = require('../controllers/ticketing');
const auth = require('../middleware/auth');
const router = express.Router();
const Multer = require('multer')
const ImgUpload = require('../middleware/imageUpload');

const multer = Multer({
    storage: Multer.MemoryStorage,
    fileSize: 5 * 1024 * 1024
})

router.post('/addEvent', multer.single('eventImageURL'), ImgUpload.uploadToGcs, addEvent);
router.get('/events', getAllEvents);
router.get('/events/:eventId', spesificEvent);
router.post('/events/:eventId/purchase', auth, purchaseTicket);
router.get('/mytickets', auth, getAllticket);

module.exports = router;
