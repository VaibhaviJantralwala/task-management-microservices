import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import { Routes, Route } from 'react-router-dom'
import Login from './pages/UI/Login'
import Project from './pages/Project'
import Task from './pages/Task'
import Dashboard from './pages/Dashboard'
import Layout from './pages/UI/Layout'


function App() {
  const [count, setCount] = useState(0)

  return (

    <Routes>
      {/* Public routes */}
      <Route path="/" element={<Login />} />
      {/* <Route path="/projects" element={<Project />} />
      <Route path="/tasks" element={<Task />} />
      <Route path="/dashboard" element={<Dashboard />} /> */}
      {/* <Route path="/register" element={<Register />} /> */}

      <Route element={<Layout />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/projects" element={<Project />} />
        <Route path="/tasks" element={<Task />} />
      </Route>

      {/* Protected routes */}
      {/* <Route element={<PrivateRoute />}>
        <Route path="/projects" element={<Projects />} />
      </Route> */}

      {/* 404 */}
      {/* <Route path="*" element={<NotFound />} /> */}
    </Routes>

  )
}

export default App
