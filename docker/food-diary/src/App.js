import React from 'react'
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom'
import {AuthProvider} from './components/context/AuthContext'
import PrivateRoute from './components/misc/PrivateRoute'
import Navbar from './components/misc/Navbar'
import Home from './components/home/Home'
import Login from './components/home/Login'
import AdminTab from "./components/admin/AdminTab";
import CuratorTab from "./components/curator/CuratorTab";
import FoodDiaryTab from "./components/curator/foodDiary/FoodDiaryTab";
import UserTab from "./components/user/UserTab";
import EditFoodDiary from "./components/user/foodDiary/EditFoodDiary";
function App() {
    return (
        <AuthProvider>
            <Router>
                <Navbar/>
                <Routes>
                    <Route path='/' element={<Home/>}/>
                    <Route path='/login' element={<Login/>}/>
                    <Route path="/adminPage" element={<PrivateRoute><AdminTab /></PrivateRoute>}/>
                    <Route path="/curatorPage" element={<PrivateRoute><CuratorTab /></PrivateRoute>}/>
                    <Route path="/get/food/diary/:userId" element={<PrivateRoute><FoodDiaryTab /></PrivateRoute>} />
                    <Route path="/edit-food-diary/:foodDiaryId" element={<PrivateRoute><EditFoodDiary /></PrivateRoute>} />
                    <Route path="/userPage" element={<PrivateRoute><UserTab /></PrivateRoute>}/>
                    <Route path="*" element={<Navigate to="/"/>}/>
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
