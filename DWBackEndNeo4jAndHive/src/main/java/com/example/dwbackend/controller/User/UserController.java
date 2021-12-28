package com.example.dwbackend.controller.user;

import com.example.dwbackend.model.user.LoginInDto;
import com.example.dwbackend.model.user.LoginOutDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity Login(@RequestBody LoginInDto loginInDto) {
        if (!loginInDto.getUsername().equals("admin") || !loginInDto.getPassword().equals("111111")){
            LoginOutDto user = new LoginOutDto();
            user.setCode(50008);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        LoginOutDto loginOutDto = new LoginOutDto();
        loginOutDto.setName(loginInDto.getUsername());
        loginOutDto.setAvator("https://anjt.oss-cn-shanghai.aliyuncs.com/amazon.png");
        loginOutDto.setToken("token");
        loginOutDto.setCode(20000);
        return new ResponseEntity<>(loginOutDto, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public ResponseEntity Info() {
        LoginOutDto loginOutDto = new LoginOutDto();
        loginOutDto.setName("admin");
        loginOutDto.setAvator("https://anjt.oss-cn-shanghai.aliyuncs.com/amazon.png");
        loginOutDto.setToken("token");
        loginOutDto.setCode(20000);
        return new ResponseEntity<>(loginOutDto, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseEntity Logout() {
        LoginOutDto loginOutDto = new LoginOutDto();
        loginOutDto.setCode(20000);
        return new ResponseEntity<>(loginOutDto, HttpStatus.OK);
    }
}
