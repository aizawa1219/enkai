package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.dao.BaseDao;
import com.example.demo.dao.EventUserDao;
import com.example.demo.entity.EventUser;

@Service
public class EventUserService implements BaseService<EventUser> {
	@Autowired
	private BaseDao<EventUser> dao;
	@Autowired
	private EventUserDao eventUserdao;

	@Override
	public List<EventUser> findAll() {
		return dao.findAll();
	}

	@Override
	public EventUser findById(Integer id) throws DataNotFoundException {
		return dao.findById(id);
	}

	@Override
	public void save(EventUser eventuser) {
		dao.save(eventuser);
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}
	
	public List<EventUser> findByEventId(Integer eventId) throws DataNotFoundException {
		return eventUserdao.findByEventId(eventId);
	}
	
	public List<EventUser> findByUserId(Integer userId) throws DataNotFoundException {
		return eventUserdao.findByUserId(userId);
	}
	
	public EventUser findByEventIdAndUserId(Integer eventId,Integer userId) throws DataNotFoundException {
		return eventUserdao.findByEventIdAndUserId(eventId,userId);
	}
	
	
	
}
