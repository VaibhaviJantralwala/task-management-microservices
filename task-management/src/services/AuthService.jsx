import Api from "./Api";


export const login = (credentials) => {
    return Api.post("/auth/login", credentials);
};

export const register = (user) => {
    return Api.post("/auth/register", user);
};
