/**
 * контрлллер для тестов работоспособности
 */
package pn.back.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
//@RequestMapping("/")
public class TestController {


    @GetMapping("/test/{id}")
    @ResponseBody
    public String testP() {
        return "TEST ***   *** STRING  DBURL   ";
    }


    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "TEST ***   *** STRING  DBURL   ";
    }

    @GetMapping("/a/test")
    @ResponseBody
    public String testa() {
        return "TEST ****A**** STRING ";
    }

    @GetMapping("/b/test")
    @ResponseBody
    public String testb() {
        return "TEST **B** STRING ";
    }

    @GetMapping("/api/b/test")
    @ResponseBody
    public String testAPIB() {
        return "TEST ***API-B** STRING ";
    }

    @GetMapping("/api/test")
    @ResponseBody
    public String testAPI() {
        return "TEST ***API ** STRING ";
    }


}
