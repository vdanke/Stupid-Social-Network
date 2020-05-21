package com.step.stupid.social.network;

import com.step.stupid.social.network.model.OurAutowired;
import com.step.stupid.social.network.model.Role;
import com.step.stupid.social.network.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClass {

    @Test
    public void test() {
        User user = new User();

        Field[] declaredFields = user.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(OurAutowired.class)) {

                Class<?> type = field.getType();

                Object typeOf = getTypeOf(type);

                try {
                    field.set(user, (Role) typeOf);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(user.getRole());
    }

    private Object getTypeOf(Class<?> type) {
        if (type.isAssignableFrom(Role.class)) {
            return Role.ROLE_ADMIN;
        }
        return new Object();
    }
}
