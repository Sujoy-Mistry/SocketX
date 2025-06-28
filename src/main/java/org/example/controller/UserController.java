package org.example.controller;

import org.example.model.Controller;
import org.example.model.RequestType;

@Controller
public class UserController {

    @RequestType.Post("/api/create")
    public String createUser(){
        return "User Created";
    }
}
