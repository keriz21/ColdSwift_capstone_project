const users = []; // In-memory user storage (replace with database integration)
const bcrypt = require('bcrypt');

module.exports = {
    createUser(username, password) {
        // Hash password before storing (replace with database interaction)
        const hashedPassword = bcrypt.hashSync(password, 10);

        const user = {
            username,
            password: hashedPassword
        };

        users.push(user); // Add user to users array
        return user; // Optionally return the created user object
    },
    getUserByUsername(username) {
        return users.find(user => user.username === username);
    }
};
