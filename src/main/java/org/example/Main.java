package org.example;

import org.example.dao.UserDOMapper;
import org.example.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"org.example"})
@RestController
@MapperScan("org.example.dao")
public class Main {
//    @Autowired
//    private UserDOMapper userDOMapper;
//    @RequestMapping("/")
//    public String home(){
//        UserDO userDO=userDOMapper.selectByPrimaryKey(1);
//        if(userDO==null){
//            return "没对象！";
//        }else {
//            return userDO.getName();
//        }
//    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
}