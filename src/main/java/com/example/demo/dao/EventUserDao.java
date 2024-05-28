package com.example.demo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.entity.EventUser;
import com.example.demo.repository.EventUserRepository;

@Repository
public class EventUserDao implements BaseDao<EventUser> {
	@Autowired
	EventUserRepository repository;

	@Override
	public List<EventUser> findAll() {
		return repository.findAll();
	}

	@Override
	public EventUser findById(Integer id) throws DataNotFoundException {
		return repository.findById(id).orElseThrow(() -> new DataNotFoundException());
	}

	@Override
	public void save(EventUser eventuser) {
		this.repository.save(eventuser);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			EventUser eventuser = this.findById(id);
			this.repository.deleteById(eventuser.getId());
		} catch (DataNotFoundException e) {
			System.out.println("no data");
		}
	}
	
	public List<EventUser> findByEventId(Integer eventId) {
		return repository.findByEventId(eventId);
	}
	
	public List<EventUser> findByUserId(Integer userId) {
		return repository.findByUserId(userId);
	}
	
	public EventUser findByEventIdAndUserId(Integer eventId,Integer userId) {
		return repository.findByEventIdAndUserId(eventId,userId);
	}
}

