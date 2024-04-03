import React, { useState } from 'react';
import axios from 'axios';

const Chat = () => {
    const [allMessages, setAllMessages] = useState([]);
    const [userInput, setUserInput] = useState('');
    const [conversationStarted, setConversationStarted] = useState(false);
    const [conversationEnded, setConversationEnded] = useState(false);

    const startConversation = async () => {
        try {
            const response = await axios.get('http://localhost:8080/opening');
            const { message } = response.data;
            setAllMessages([{ message, author: 'ai' }]);
            setConversationStarted(true);
        } catch (error) {
            console.error("There was an error starting the conversation: ", error);
        }
    };

    const endConversation = async () => {
        try {
            const response = await axios.get('http://localhost:8080/closing');
            const { message } = response.data;
            setAllMessages(messages => [...messages, { message, author: 'ai' }]);
            setConversationEnded(true);
        } catch (error) {
            console.error("There was an error ending the conversation: ", error);
        }
    };

    const sendMessage = async () => {
        if (userInput.trim() === '' || conversationEnded) return;

        const newMessage = { message: userInput, author: 'user' };
        setAllMessages(messages => [...messages, newMessage]);

        try {
            const response = await axios.post('http://localhost:8080/openai/chat', {
                message: userInput
            });
            const { message } = response.data;
            setAllMessages(messages => [...messages, { message, author: 'ai' }]);
        } catch (error) {
            console.error("There was an error sending the message: ", error);
        }
        setUserInput('');
    };

    return (
        <div className="flex flex-col h-screen p-4 bg-gray-100">
            <div className="flex-grow overflow-auto">
                {allMessages.map((msg, index) => (
                    <div key={index} className={`p-4 rounded-lg max-w-md mx-2 my-2 shadow ${msg.author === 'user' ? 'bg-gray-300 ml-auto' : 'bg-gray-200 mr-auto'}`}>
                        <span className="block text-gray-800">{msg.message}</span>
                    </div>
                ))}
            </div>
            {conversationStarted ? (
                <div className="flex-none flex gap-2">
                    <input
                        className="border p-2 w-full rounded-lg"
                        placeholder="Type your message here..."
                        value={userInput}
                        onChange={(e) => setUserInput(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' && sendMessage()}
                        disabled={conversationEnded}
                    />
                    <button
                        onClick={sendMessage}
                        className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded"
                        disabled={conversationEnded}
                    >
                        Send
                    </button>
                    {!conversationEnded && (
                        <button
                            onClick={endConversation}
                            className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded"
                        >
                            End Conversation
                        </button>
                    )}
                </div>
            ) : (
                <div className="flex flex-col items-center justify-center h-full">
                    <button
                        onClick={startConversation}
                        className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded"
                    >
                        Start Conversation
                    </button>
                </div>
            )}
        </div>
    );
};

export default Chat;
