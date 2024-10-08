package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.model.MemberDO;
import com.project.model.MemberSO;
import com.project.model.request.LoginRequest;
import com.project.model.request.SignupRequest;
import com.project.model.response.LoginResponse;

import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {
	
	@Autowired
	private MemberSO memberSo;
	
	public MemberController(MemberSO memberSo) {
		this.memberSo = memberSo;
	}
	
	@GetMapping("/login")
	public String loginHandler() {
		return "login";
	}
	
	@PostMapping("/loginProcess")
	public String loginProcessHandler(LoginRequest req, HttpSession session) {
		try {
			LoginResponse auth = memberSo.login(req.getM_acctid(), req.getM_acctpwd());
			
			if(auth != null) {
				session.setAttribute("auth", auth);
				
				int m_role = memberSo.checkM_role(auth.getMember_id());
				
				switch(m_role) { //리턴주소 추후 변경
					case 1:
						return "redirect:/";
					case 2:
						return "redirect:/instructHome";
					default:
						return "redirect:/adminHome";
				}
			}
			else {
				session.setAttribute("loginFailMsg", "로그인에 실패했습니다.");
				return "redirect:/login?error=loginFailed";
			}
		}
		catch(EmptyResultDataAccessException e) {
			session.setAttribute("loginFailMsg", "일치하는 정보가 없습니다.");
			return "redirect:/login?error=loginFailed";
		}
	}
	
	@GetMapping("/logout")
	public String logoutHandler(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	@GetMapping("/agreement")
	public String agreementHandler() {
		return "agreement";
	}
	
	@PostMapping("/agreementForm")
	public String checkAgreementHandler(
			@RequestParam(value = "allagree", required = false) boolean allagree,
			@RequestParam(value = "memberagree", required = true) boolean memberagree,
			@RequestParam(value = "personalagree", required = true) boolean personalagree) {
		if(memberagree && personalagree) {
			return "/signupform";
		}
		else {
			return "redirect:/agreement";
		}
	}
	
	@GetMapping("/signupform")
	public String signupHandler() {
		return "signupform";
	}
	
	@PostMapping("/signupProcess")
	public String signupProcessHandler(SignupRequest req) {
		try {
			memberSo.signupStudent(req);
			return "redirect:/login";
		}
		catch(Exception e) {
			e.printStackTrace();
			return "redirect:/signupform?error=signupFailed";
		}
	}
	
	@GetMapping("/findcheck")
	public String findHandler() {
		return "findcheck";
	}
	
	@GetMapping("/findid")
	public String findidHandler() {
		return "findid";
	}
	
	@PostMapping("/findidProcess")
	public String findProcessHandler(
			@RequestParam("m_name") String m_name,
			@RequestParam("m_email") String m_email,
			@RequestParam("m_role") int m_role,
			Model model) {
		String result = memberSo.findM_acctid(m_name, m_email, m_role);
		try {
			if(result != null) {
				model.addAttribute("result", result);
				return "findid";
			}
			else {
				model.addAttribute("result", "일치하는 아이디가 없습니다.");
				return "findid";
			}
		}
		catch (Exception e) {
			model.addAttribute("result", "아이디 찾기 중 오류가 발생했습니다.");
			return "redirect:/findid?error=findidFailed";
		}
	}
	
	@GetMapping("/findpwd")
	public String findpwdHandler() {
		return "findpwd";
	}
	
	@PostMapping("/findpwdProcess")
	public String findpwdProcessHandler(
			@RequestParam("m_acctid") String m_acctid,
			@RequestParam("m_email") String m_email,
			@RequestParam("m_role") int m_role,
			Model model) {
		String result = memberSo.findM_acctpwd(m_acctid, m_email, m_role);
		try {
			if(result != null) {
				return "redirect:/changepwd";
			}
			else {
				model.addAttribute("result", "일치하는 정보가 없습니다.");
				return "findpwd";
			}
		}
		catch (Exception e) {
			model.addAttribute("result", "비밀번호 찾기 중 오류가 발생했습니다.");
			return "findpwd?error=findpwdFaild";
		}
	}
	
	@GetMapping("/changepwd")
	public String changePwdHandler() {
		return "changepwd";
	}
	
//	@PostMapping("/changepwProcess")
//	public String changePwdProcessHandler() {
//		
//	}
 	
	@GetMapping("/mypage")
	public String mypageHandler(HttpSession session, Model model) {
		LoginResponse auth = (LoginResponse)session.getAttribute("auth");
		
		// auth가 없으면 로그인 페이지로
		if(auth == null) {
			return "redirect:/login";
		}
		
		MemberDO member = memberSo.selectedByMember_id(auth.getMember_id());
		
		String roleName;
		if(member.getM_role() == 1) {
			roleName = "학생";
		}
		else if(member.getM_role() == 2) {
			roleName = "강사";
		}
		else {
			roleName = "사용자 오류";
		}
			
		model.addAttribute("m_pfp", member.getM_pfp());
		model.addAttribute("m_name", member.getM_name());
		model.addAttribute("m_dept", member.getM_dept());
		model.addAttribute("m_role", roleName);
		model.addAttribute("m_acctid", member.getM_acctid());
		model.addAttribute("m_email", member.getM_email());
		return "mypage";
	}
	
	
}
