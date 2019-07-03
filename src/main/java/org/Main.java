package org;

import org.beans.Dog;
import org.beans.Owner;
import org.config.Config;
import org.config.EmptyConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main
{
    public static void main( String[] args )
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AnnotationConfigApplicationContext annCtx = new AnnotationConfigApplicationContext(Config.class);
        AnnotationConfigApplicationContext scanCtx = new AnnotationConfigApplicationContext(EmptyConfig.class);

        Owner owner = ctx.getBean(Owner.class);
        Dog dog = ctx.getBean(Dog.class);
        for (String name : ctx.getBeanDefinitionNames()) {
            System.out.println(name);
        }


        Owner owner2 = annCtx.getBean(Owner.class);
        Dog dog2 = annCtx.getBean(Dog.class);

        Owner owner3 = scanCtx.getBean(Owner.class);
        Dog dog3 = scanCtx.getBean(Dog.class);


        System.out.println();
    }
}