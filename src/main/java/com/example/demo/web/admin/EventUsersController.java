package com.example.demo.web.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@Controller
@RequestMapping("/admin/eventusers")
public class EventUsersController {
	@Autowired
	EventService eventService;
	@Autowired
	UserService userService;
	@Autowired
	EventUserService eventUserService;

	
	/*
	 * イベント参加処理
	 */
	@GetMapping(value = "/create/{id}")
	public String form(@PathVariable Integer id,EventUser eventuser, Model model, @AuthenticationPrincipal UserDetails user, RedirectAttributes ra)throws DataNotFoundException {
		String email = user.getUsername();
		User userData = userService.findByEmail(email);//
		Integer userId = userData.getId();
		User userInfo = userService.findById(userId);//ユーザ情報
		Event event = eventService.findById(id);
		
		eventuser.setUser(userInfo);//ユーザ情報をデータベースにセットしている
		eventuser.setEvent(event);
		
		List <EventUser> eventData = eventUserService.findByEventId(id);//対象のイベント参加者データ
		Integer number = eventData.size(); //対象のイベント参加者のデータ数から参加者数を調べる
		Integer max = event.getMax_participant();//最大参加者数
		if (number >= max) {
			FlashData flash;
			flash = new FlashData().danger("最大参加者数に到達しました");
			ra.addFlashAttribute("flash", flash);
			return "redirect:/admin/events/view/{id}";
		}
		
		EventUser joinuser = eventUserService.findByEventIdAndUserId(id,userId);
		if (joinuser == null) {
			//データベースに登録
			eventUserService.save(eventuser);
			return "redirect:/admin/events/view/{id}";
//			return "admin/events/joinview";
		}else {
			return "redirect:/admin/events/view/{id}";
		}
		
	}
	
	/*
	 * 削除
	 */
	@GetMapping(value = "/delete/{id}")
	public String delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetails user, RedirectAttributes ra) throws DataNotFoundException  {
		FlashData flash;
		try {
			String email = user.getUsername();
			User userData = userService.findByEmail(email);//
			Integer userId = userData.getId();
			
			EventUser joinuser = eventUserService.findByEventIdAndUserId(id,userId);
			
//			Integer eventuserid = Integer.parseInt(eventUserId);
			
			if (joinuser == null) {
				flash = new FlashData().danger("イベントに参加していません");
			} else {
				eventService.findById(id);
				eventUserService.deleteById(joinuser.getId());
				flash = new FlashData().success("イベントを辞退しました");
			}
		} catch (DataNotFoundException e) {
			flash = new FlashData().danger("該当データがありません");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/events/view/{id}";
	}
	
	
	
	
}
