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
import com.example.demo.entity.Chat;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventUser;
import com.example.demo.entity.User;
import com.example.demo.service.ChatService;
import com.example.demo.service.EventService;
import com.example.demo.service.EventUserService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/chats")
public class ChatsController {
	@Autowired
	EventService eventService;
	@Autowired
	UserService userService;
	@Autowired
	EventUserService eventUserService;
	@Autowired
	ChatService chatService;

	
	/*
	 * チャット画面
	 */
	@GetMapping(value = "/talk/{id}")
	public String talk(Chat chat,@PathVariable Integer id,EventUser eventuser, Model model, @AuthenticationPrincipal UserDetails user)throws DataNotFoundException {
		String email = user.getUsername();
		User userData = userService.findByEmail(email);//
		Integer userId = userData.getId();
//		User userInfo = userService.findById(userId);
		Event event = eventService.findById(id);
		
		List<Chat> chats = chatService.findByEventId(id);//chatデータを取得
		
		//ログインユーザがイベントに参加していない場合
		EventUser joinuser = eventUserService.findByEventIdAndUserId(id,userId);
		if (joinuser == null) {
			return "redirect:/admin/events/view/{id}";
		}
		
		model.addAttribute("event",event);
		model.addAttribute("chats",chats);
		model.addAttribute("userId",userId);
		model.addAttribute("chat",chat);
		
		return "admin/chats/talk";
		
	}
	
	/*
	 * チャット投稿処理
	 */
	@PostMapping(value = "/create")
	public String register(@Valid Chat chat, BindingResult result, Model model,@AuthenticationPrincipal UserDetails user, RedirectAttributes ra)throws DataNotFoundException {
		FlashData flash = null;
		String email = user.getUsername();
		User userData = userService.findByEmail(email);//
		Integer userId = userData.getId();
		User userInfo = userService.findById(userId);//ユーザ情報
		
		chat.setUser(userInfo);//ユーザ情報をchatデータベースにセットしている
		Integer eventId = chat.getEvent().getId();
		try {
	//		Event eventData = eventService.findById(eventId);
	//		chat.setEvent(eventData);
			
			//ログインユーザがイベントに参加していない場合
			
			EventUser joinuser = eventUserService.findByEventIdAndUserId(eventId,userId);
			if (joinuser == null) {
				return "redirect:/admin/events/view/" + eventId;
			}
		
			if (result.hasErrors()) {
				//入力制限
				return "redirect:/admin/chats/talk/" + eventId;
			}
			// チャット登録
			chatService.save(chat);//←使う
			
//			flash = new FlashData().success("新規作成しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
			
		return "redirect:/admin/chats/talk/"+ eventId ;
	}
	
}
