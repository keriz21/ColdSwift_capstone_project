// routes/userRoutes.js
const express = require('express');
const { register, login, forgetPassword, viewProfile } = require('../controllers/userController');
const auth = require('../middleware/auth');
const router = express.Router();

router.post('/register', register);
router.post('/login', login);
router.post('/forget-password', forgetPassword);
router.get('/profile', auth, viewProfile);

module.exports = router;
