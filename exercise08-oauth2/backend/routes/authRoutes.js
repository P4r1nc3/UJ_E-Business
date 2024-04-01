const express = require('express');
const User = require('../models/User');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const router = express.Router();

router.post('/register', async (req, res) => {
    try {
        const { email, password } = req.body;
        const userExists = await User.findOne({ email });

        if (userExists) return res.status(400).send('User already exists');

        const user = new User({ email, password });
        await user.save();

        res.status(201).send('User created');
    } catch (err) {
        res.status(500).send(err.message);
    }
});

router.post('/login', async (req, res) => {
    try {
        const { email, password } = req.body;
        const user = await User.findOne({ email });

        if (!user || !(await bcrypt.compare(password, user.password))) {
            return res.status(401).send('Error while trying to login');
        }

        const token = jwt.sign({ _id: user._id }, process.env.TOKEN_SECRET);
        res.header('auth-token', token).send(token);
    } catch (err) {
        res.status(500).send(err.message);
    }
});

module.exports = router;
