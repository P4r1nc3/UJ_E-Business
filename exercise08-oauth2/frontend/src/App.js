import React from 'react';
import AppRouter from './AppRouter';
import { AuthProvider } from './contexts/AuthContext';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return (
      <div>
          <ToastContainer />
          <AuthProvider>
              <AppRouter />
          </AuthProvider>
      </div>
  );
}

export default App;
