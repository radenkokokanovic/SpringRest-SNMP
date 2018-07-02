package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Person;
import com.example.service.MojServis;
import com.example.service.PersonService;
import com.example.service.UpitiDB;
import com.jcraft.jsch.JSchException;




@RestController
@RequestMapping("/persons")
public class PersonController {

	@Autowired
	PersonService ps;
	
	@Autowired
	MojServis ms;
	
	@Autowired
	UpitiDB db;
	
	
	//Mapping get all person in JSON format
	@CrossOrigin(origins = "*")
	
	@RequestMapping("/all")
	public @ResponseBody Hashtable<String, Person> getAll(){
		return ps.getAll();
	}
	
	
	//Mapping get person by ID
	@CrossOrigin(origins = "*")
	@RequestMapping("{id}")
	public Person getPerson(@PathVariable("id") String id)
	{
		return ps.getPerson(id);
	}
	
	
	
	//Test mapping hello world
	@CrossOrigin(origins="*")
	@RequestMapping("/hello")
	public String pozdrav()
	{
		return ms.pozdrav();
	}
	
	//Mapping get all RX power from RAISECOM modem in network
	@CrossOrigin(origins="*")
	@RequestMapping("/rx")
	public String Rx() throws ClassNotFoundException, JSchException, IOException, DecoderException, SQLException
	{
		return db.prikazSvihSnagaRx();
	}
}
