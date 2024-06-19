// routes/userRoutes.js
const express = require('express');
const { register, login, forgetPassword, viewProfile } = require('../controllers/userController');
const auth = require('../middleware/auth');
const router = express.Router();
const Multer = require('multer');

const upload = Multer({ 
    storage: Multer.MemoryStorage,
     limits: { fileSize: 15 * 1024 * 1024 },
     fileFilter: (req, file, cb) => {
        if (file.fieldname === 'ktp_image' || file.fieldname === 'face_image') {
            cb(null, true); //first param is for error, sec param is wether access the file or nah
        } else {
            cb(new Error('File fieldname should be ktp_image or face_image'));
        }
    }
    });

// router.post('/register', upload.single('ktp_image'), register);
router.post('/register', upload.fields([
    { name: 'ktp_image', maxCount: 1 },
    { name: 'face_image', maxCount: 1 }
]), register);
router.post('/login', login);
router.post('/forget-password', forgetPassword);
router.get('/profile', auth, viewProfile);

module.exports = router;