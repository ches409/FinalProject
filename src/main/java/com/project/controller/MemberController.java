package com.project.controller;

//import java.util.HashMap;
//import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.model.MemberDO;
import com.project.model.MemberSO;
//import com.project.model.JWT.JwtUtil;
import com.project.model.request.LoginRequest;
import com.project.model.request.SignupRequest;
//import com.project.model.response.ErrorResponse;
import com.project.model.response.LoginResponse;

//import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {
	
	@Autowired
	private MemberSO memberSo;
	/*
	 * @Autowired private JwtUtil jwtUtil;
	 */
	
	@GetMapping("/login")
	public String loginHandler() {
		return "login";
	}
	
	/*
	 * // 토큰 구현 중단
	 * @PostMapping("/loginProcess") public ResponseEntity<?>
	 * loginProcessHandler(@RequestBody LoginRequest req) { try { LoginResponse auth
	 * = memberSo.login(req.getM_acctid(), req.getM_acctpwd());
	 * 
	 * if(auth != null) { String token = jwtUtil.generateToken(auth.getM_acctid());
	 * 
	 * int m_role = memberSo.checkM_role(auth.getMember_id());
	 * 
	 * Map<String, Object> response = new HashMap<>(); response.put("token", token);
	 * response.put("m_role", m_role); response.put("message", "로그인 성공");
	 * 
	 * return ResponseEntity.ok(response); } else { return
	 * ResponseEntity.status(HttpStatus.UNAUTHORIZED) .body(new
	 * ErrorResponse("로그인에 실패했습니다.")); } } catch(EmptyResultDataAccessException e) {
	 * return ResponseEntity.status(HttpStatus.UNAUTHORIZED) .body(new
	 * ErrorResponse("일치하는 정보가 없습니다.")); } }
	 */
	
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
					return "redirect:/";
				default:
					return "redirect:/adminMain";
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
			Model model,
			RedirectAttributes rttr) {
		String member_id = memberSo.findM_acctpwd(m_acctid, m_email, m_role);
		try {
			if(member_id != null) {
				rttr.addFlashAttribute("member_id", member_id);
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
	
	@PostMapping("/changepwdProcess")
	public String changePwdProcessHandler(
			@RequestParam(value="member_id") int member_id,
			@RequestParam("m_acctpwd") String m_acctpwd,
			@RequestParam("confirmpwd") String confirmpwd,
			Model model) {
		if(!m_acctpwd.equals(confirmpwd)) {
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "changepwd";
		}
		
		MemberDO memberDo = new MemberDO();
		memberDo.setMember_id(member_id);
		memberDo.setM_acctpwd(m_acctpwd);
		
		int result = memberSo.updateM_acctpwd(memberDo);
		
		if(result == 1) {
			model.addAttribute("msg", "비밀번호를 변경했습니다.");
			return "redirect:/login";
		}
		else {
			model.addAttribute("msg", "비밀번호를 변경하지 못했습니다.");
			return "changepwd";
		}
	}
	
	/*
	 * // 토큰 구현 중단
	 * 
	 * @GetMapping("/mypage") public String mypageHandler(HttpServletRequest
	 * request, Model model) { String auth = request.getHeader("Authorization");
	 * 
	 * if (auth == null || !auth.startsWith("Bearer ")) {
	 * System.out.println("Authorization is null."); }
	 * 
	 * String token = auth.substring(7); String m_acctid;
	 * 
	 * try { m_acctid = jwtUtil.extractUsername(token); } catch (Exception e) {
	 * return "redirect:/login"; }
	 * 
	 * MemberDO member = memberSo.selectedByM_acctid(m_acctid);
	 * 
	 * String roleName; if(member.getM_role() == 1) { roleName = "학생"; } else
	 * if(member.getM_role() == 2) { roleName = "강사"; } else { roleName = "사용자 오류";
	 * }
	 * 
	 * model.addAttribute("member_id", member.getMember_id());
	 * model.addAttribute("m_pfp", member.getM_pfp()); model.addAttribute("m_name",
	 * member.getM_name()); model.addAttribute("m_dept", member.getM_dept());
	 * model.addAttribute("m_role", roleName); model.addAttribute("m_acctid",
	 * member.getM_acctid()); model.addAttribute("m_email", member.getM_email());
	 * 
	 * model.addAttribute("menu", "mypage"); // 사이드바 색변경 return "mypage"; }
	 */
	
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

		model.addAttribute("member_id", member.getMember_id());
		model.addAttribute("m_pfp", member.getM_pfp());
		model.addAttribute("m_name", member.getM_name());
		model.addAttribute("m_dept", member.getM_dept());
		model.addAttribute("m_role", roleName);
		model.addAttribute("m_acctid", member.getM_acctid());
		model.addAttribute("m_email", member.getM_email());
		
		model.addAttribute("menu", "mypage");
		return "mypage";
	}
	
//	@GetMapping("leave")
//	public String leaveProcessHandler(HttpSession session, int member_id) {
//		LoginResponse auth = (LoginResponse)session.getAttribute("auth");
//		MemberDO member = memberSo.updateM_status(auth.getMember_id());
//		session.invalidate();
//		return "redirect:/login";
//	}
	
	
}
