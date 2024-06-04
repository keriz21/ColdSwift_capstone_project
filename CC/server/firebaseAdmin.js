const admin = require('firebase-admin');
const serviceAccount = require('../coldswift-firebase-adminsdk-achmt-67ec1cd22e.json');

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

module.exports = admin;