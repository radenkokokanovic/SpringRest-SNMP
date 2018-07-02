package com.example.service;

import java.util.Hashtable;

import org.springframework.stereotype.Service;

import com.example.model.Person;


//Service for work with Person
@Service
public class PersonService {

	
	Hashtable<String, Person> persons=new Hashtable<String,Person>();
	
	public PersonService()
	{
		Person p=new Person();
		p.setId("101");
		p.setName("Markus");
		p.setLastName("Hemingway");
		p.setAge(25);
		persons.put("1", p);
		
		
		p=new Person();
		p.setId("102");
		p.setName("Mihael");
		p.setLastName("Stosic");
		p.setAge(24);
		persons.put("2", p);
		
		p=new Person();
		p.setId("103");
		p.setName("Ramos");
		p.setLastName("Silva");
		p.setAge(25);
		persons.put("3", p);
	}
	
	public String pozdrav()
	{
		return "Hello Radenko " ;
	}
	
	public Person getPerson(String id)
	{
		if (persons.containsKey(id))
		{
			return persons.get(id);
		}else
		{
			return null;
		}
	}
	
	public Hashtable<String, Person> getAll()
	{
		return persons;
	}
}
