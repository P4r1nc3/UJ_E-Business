import React, { useState } from 'react';
import axios from 'axios';


const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [userInput, setUserInput] = useState('');

    const sendMessage = async () => {
        if (userInput.trim() === '') return;

        const newMessage = { message: userInput, author: 'user' };
        setMessages([...messages, newMessage]);

        try {
            const response = await axios.post('http://localhost:8080/openai/chat', {
                message: userInput
            });
            const { message } = response.data;
            setMessages(messages => [...messages, { message, author: 'ai' }]);
        } catch (error) {
            console.error("There was an error sending the message: ", error);
        }
        setUserInput('');
    };

    return (
        <div className="flex flex-col h-screen p-4">
            <div className="flex-grow overflow-auto">
                {messages.map((msg, index) => (
                    <div key={index} className={`p-2 ${msg.author === 'user' ? 'text-right' : ''}`}>
                        {msg.message}
                    </div>
                ))}
            </div>
            <div className="flex-none">
                <input
                    className="border p-2 w-full"
                    value={userInput}
                    onChange={(e) => setUserInput(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && sendMessage()}
                />
            </div>
        </div>
    );
};

export default Chat;
