import React, { createContext, useContext, useEffect, useState } from "react";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [accessToken, setAccessToken] = useState(null);
  const [userId, setUserId] = useState(null);
  const [roles, setRoles] = useState([]);

  useEffect(() => {
    const storedToken = localStorage.getItem("accessToken");
    const storedUserId = localStorage.getItem("userId");
    const storedRoles = localStorage.getItem("roles");

    if (storedToken && storedUserId) {
      setAccessToken(storedToken);
      setUserId(Number(storedUserId));
      setRoles(storedRoles ? JSON.parse(storedRoles) : []);
    }
  }, []);

  const login = (token, id, rolesArr) => {
    setAccessToken(token);
    setUserId(id);
    setRoles(rolesArr || []);
    localStorage.setItem("accessToken", token);
    localStorage.setItem("userId", id);
    localStorage.setItem("roles", JSON.stringify(rolesArr || []));
  };

  const logout = () => {
    setAccessToken(null);
    setUserId(null);
    setRoles([]);
    localStorage.removeItem("accessToken");
    localStorage.removeItem("userId");
    localStorage.removeItem("roles");
  };

  const isAdmin = roles.includes("ADMIN");

  return (
    <AuthContext.Provider
      value={{ accessToken, userId, roles, login, logout, isAdmin }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
