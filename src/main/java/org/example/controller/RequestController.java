package org.example.controller;

import org.example.model.Controller;
import org.example.model.RequestType;

@Controller
public class RequestController {

        @RequestType.Get("/api/hello")
        public String hello(@RequestType.RequestParam("count") int no) {
            return "{\"message\": \"Hello with requestParam " + no + "\"}";
        }

        @RequestType.Get("/api/greet")
        public String greet() {
            return "{\"message\": \"Welcome!\"}";
        }
        @RequestType.Get("/nice")
        public String test(){
            return "{\"message\": \"Welcome!\"}";
        }

}
