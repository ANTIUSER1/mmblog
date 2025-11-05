package pn.back.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DoSomthingTest {

    @Autowired
    DoSomthing doSomthing;

    @Test
    void summa() {
        Assertions.assertEquals(56, doSomthing.summa());
    }
}