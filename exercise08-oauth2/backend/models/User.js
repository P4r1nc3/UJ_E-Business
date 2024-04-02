const mongoose = require('mongoose');
const bcrypt = require('bcrypt');

const userSchema = new mongoose.Schema({
    email: { type: String, required: true, unique: true },
    password: { type: String, required: function() { return this.isLocal; }},
    isLocal: { type: Boolean, default: true },
    googleId: { type: String, required: false }
});

userSchema.pre('save', async function (next) {
    if (this.isModified('password') && this.password) {
        try {
            const hashedPassword = await bcrypt.hash(this.password, 10);
            this.password = hashedPassword;
            next();
        } catch (error) {
            next(error);
        }
    } else {
        next();
    }
});


module.exports = mongoose.model('User', userSchema);
