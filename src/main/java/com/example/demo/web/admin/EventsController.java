package com.example.demo.web.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.common.FlashData;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventUser;
import com.example.demo.entity.User;
import com.example.demo.service.EventService;
import com.example.demo.service.EventUserService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class EventsController {
	@Autowired
	EventService eventService;
	@Autowired
	UserService userService;
	@Autowired
	EventUserService eventUserService;

	/*
	 * 一覧表示
	 */
	@GetMapping(path = {"/", ""})
	public String list(Model model) {
		// 全件取得
		List<Event> events = eventService.findAll();
		model.addAttribute("events", events);
		return "events/list";
	}
	
	/*
	 * イベント詳細
	 */
	@GetMapping(value = "/events/view/{id}")
	public String view(Model model, @PathVariable Integer id) throws DataNotFoundException {
		// idから取得
		Event eventdetail = eventService.findById(id);
		model.addAttribute("eventdetail", eventdetail);
		return "events/view";
	}
	
	/*
	 * マイイベント一覧	
	 */
	@GetMapping(value = "/admin/events/mylist")
	public String mylist(Model model, @AuthenticationPrincipal UserDetails user) throws DataNotFoundException {
		// user_idから取得
		String email = user.getUsername();
		User userData = userService.findByEmail(email);//
		Integer userId = userData.getId();
		List<Event> events = eventService.findByUserId(userId);
		model.addAttribute("events", events);
		return "admin/events/mylist";
	}
	
	/*
	 * 新規作成画面表示
	 */
	@GetMapping(value = "/admin/events/create")
	public String form(Event event, Model model, @AuthenticationPrincipal UserDetails user)throws DataNotFoundException {
		String email = user.getUsername();
		User userData = userService.findByEmail(email);//
		Integer userId = userData.getId();
		model.addAttribute("userId", userId);
		model.addAttribute("event", event);
		return "admin/events/create";
	}
	
	/*
	 * 新規登録
	 */
	@PostMapping(value = "/admin/events/create")
	public String register(@Valid Event event, BindingResult result, Model model, RedirectAttributes ra) {
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/events/create";
			}
			// 新規登録
			eventService.save(event);
			flash = new FlashData().success("新規作成しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin";
	}
	
	/*
	 * 編集画面表示
	 */
	@GetMapping(value = "/admin/events/edit/{id}")
	public String edit(@PathVariable Integer id, Model model,@AuthenticationPrincipal UserDetails user ,RedirectAttributes ra)throws DataNotFoundException {
		String email = user.getUsername();
		User userData = userService.findByEmail(email);
		Integer userId = userData.getId();
		try {
			// 存在確認
			Event event = eventService.findById(id);
			Integer userRegId = event.getUser().getId();
			
			if(userId != userRegId) {
				FlashData flash = new FlashData().danger("ログインユーザの登録イベントではありません");
				ra.addFlashAttribute("flash", flash);
				return "redirect:/admin/events/mylist";
			}
			model.addAttribute("event", event);
			model.addAttribute("userId", userId);
		} catch (Exception e) {
			FlashData flash = new FlashData().danger("該当データがありません");
			ra.addFlashAttribute("flash", flash);
			return "redirect:/admin/events/mylist";
		}
		return "admin/events/edit";
	}
	
	/*
	 * 更新
	 */
	@PostMapping(value = "/admin/events/edit/{id}")
	public String update(@PathVariable Integer id,@Valid Event event, BindingResult result, Model model, @AuthenticationPrincipal UserDetails user ,RedirectAttributes ra)throws DataNotFoundException {
		FlashData flash;
		String email = user.getUsername();
		User userData = userService.findByEmail(email);
		Integer userId = userData.getId();
		try {
			if (result.hasErrors()) {
				model.addAttribute("userId", userId);
				return "admin/events/edit";
			}
			// 更新
			eventService.save(event);
			flash = new FlashData().success("更新しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/events/mylist";
	}
	
	/*
	 * 削除
	 */
	@GetMapping(value = "/admin/events/delete/{id}")
	public String delete(@PathVariable Integer id,@AuthenticationPrincipal UserDetails user , RedirectAttributes ra)throws DataNotFoundException {
		FlashData flash;
		String email = user.getUsername();
		User userData = userService.findByEmail(email);
		Integer userId = userData.getId();
		try {
			Event event = eventService.findById(id);
			Integer userRegId = event.getUser().getId();
			
			if (userId != userRegId) {
				flash = new FlashData().danger("ログインユーザの登録イベントではありません");
				ra.addFlashAttribute("flash", flash);
				return "redirect:/admin/events/mylist";
			}
			
			List<EventUser> eventUsers = eventUserService.findByEventId(id);
			if(eventUsers.isEmpty()) {
				eventService.findById(id);
				eventService.deleteById(id);
				flash = new FlashData().success("カテゴリの削除が完了しました");
			} else {
				flash = new FlashData().danger("参加者のいるイベントは削除できません");
				ra.addFlashAttribute("flash", flash);
				return "redirect:/admin/events/mylist";
			}
		} catch (DataNotFoundException e) {
			flash = new FlashData().danger("該当データがありません");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/events/mylist";
	}
	
	/*
	 * イベント表示
	 */
	@GetMapping(value = "/admin/events/view/{id}")
	public String viewLog(Model model,@AuthenticationPrincipal UserDetails user, @PathVariable Integer id) throws DataNotFoundException {
		// idから取得
		String email = user.getUsername();
		User userData = userService.findByEmail(email);
		Integer userId = userData.getId();
//		List<EventUser> joinusers = eventUserService.findByUserId(userId);
		EventUser joinuser = eventUserService.findByEventIdAndUserId(id,userId);
		List<EventUser> eventusers = eventUserService.findByEventId(id);
		Event eventdetail = eventService.findById(id);
		
		model.addAttribute("eventdetail", eventdetail);
		model.addAttribute("eventusers", eventusers);
		model.addAttribute("joinuser",joinuser);
		
		return "admin/events/view";
		
//		if (joinusers.isEmpty()) {
//			return "admin/events/view";
//		}else {
//			return "admin/events/joinview";
//		}
		
	}

	
	
}
