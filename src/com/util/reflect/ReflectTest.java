package com.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 获取class 对象的三种方法
 * 1、Object 的getClass方法
 * 2、类的静态变量
 * 3、累的静态方法
 * 
 * 
 * 
 *   4、通过反射获取无参构造方法并且使用
 * @author huiyi
 *
 */
public class ReflectTest {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//1
		 Person person=new Person();
		Class p1= person.getClass();
		 //2
		Class  p2=Person.class;
		//3
		Class  p3=Person.class.forName("com.util.reflect.Person");
		
		System.out.println(p1==p2);
		System.out.println(p2==p3);
		
		
		//4
		Constructor<Person> constructor= p1.getConstructor();
		Person person2=constructor.newInstance();
		person2.name="hahha";
		System.out.println(person2.name);
		
		 
		

	}

}
