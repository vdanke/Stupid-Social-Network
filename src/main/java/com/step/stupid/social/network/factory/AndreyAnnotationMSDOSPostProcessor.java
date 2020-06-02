package com.step.stupid.social.network.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.FixedValue;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
//@Order(1)
public class AndreyAnnotationMSDOSPostProcessor implements BeanPostProcessor {

    /*
    Главный дирежер
     */
    private final ConfigurableListableBeanFactory beanFactory;
    /*
    Идет под номером 1
     */
//    private final BeanDefinition beanDefinition;

    @Autowired
    public AndreyAnnotationMSDOSPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if (bean.getClass().isAnnotationPresent(MSDOSEverywhere.class)) {
//            MSDOSEverywhere declaredAnnotation = bean.getClass().getDeclaredAnnotation(MSDOSEverywhere.class);
//            System.out.println(beanName + " in before " + "default value is " + declaredAnnotation.operationSystem());
//        }
        Field[] declaredFields = bean.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            field.setAccessible(true);

            boolean isAnnotationPresent = field.isAnnotationPresent(MSDOSEverywhere.class);

            if (isAnnotationPresent) {
                MSDOSEverywhere declaredAnnotation = field.getDeclaredAnnotation(MSDOSEverywhere.class);

                String operationSystem = declaredAnnotation.operationSystem();

                System.out.println(bean.getClass().getSimpleName());

                try {
                    field.set(bean, operationSystem);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (bean.getClass().isAnnotationPresent(MSDOSEverywhere.class)) {
//            MSDOSEverywhere declaredAnnotation = bean.getClass().getDeclaredAnnotation(MSDOSEverywhere.class);
//            System.out.println(beanName + " in after " + "default value is " + declaredAnnotation.operationSystem());
//        }
        if (bean.getClass().isAnnotationPresent(MSDOSEverywhere.class)) {
            MethodInterceptor handler = (obj, method, args, proxy) -> {
                if (method.getName().equals("getOperationSystem")) {
                    method.setAccessible(true);
                    return ((String) proxy.invoke(bean, args));
                }
                return proxy.invoke(bean, args);
            };
            OperationSystem operationSystem = (OperationSystem) Enhancer.create(bean.getClass(), handler);
            System.out.println(operationSystem.getClass().getSimpleName());
            System.out.println(operationSystem.getOperationSystem());
        }
        return bean;
    }
}
