package com.project.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.request.SignupRequest;
import com.project.model.response.LoginResponse;

@Service
public class MemberSO {
	
	@Autowired
	private MemberDAO memberDao;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public MemberSO() {
		
	}
	
	@Transactional
	public void signupStudent(SignupRequest req) {
		MemberDO newMember = new MemberDO();
		
		newMember.setMember_id(req.getMember_id());
		newMember.setM_acctid(req.getM_acctid());
		newMember.setM_acctpwd(bCryptPasswordEncoder.encode(req.getM_acctpwd())); // 비밀번호 해싱
		newMember.setM_name(req.getM_name());
		newMember.setM_email(req.getM_email());
		newMember.setM_tel(req.getM_tel());
		newMember.setM_dept(req.getM_dept());
        
        // 학생 기본값 설정
		newMember.setM_role(1);
        newMember.setM_status(1);
		
		memberDao.insertMember(newMember);
	}
	
	public MemberDO selectedByMember_id(int member_id) {
		return memberDao.selectedByMember_id(member_id);
	}
	
	// 로그인
	public LoginResponse login(String m_acctid, String m_acctpwd) {
		MemberDO member = memberDao.login(m_acctid);
		if(member == null) {
			return null;
		}
		// 해싱되지 않은 기존 비밀번호일 때 해싱해서 DB에 업데이트
		if(member.getM_acctpwd().length() < 60) {
			if(!m_acctpwd.equals(member.getM_acctpwd())) {
				return null;
			}
			String hassedPwd = bCryptPasswordEncoder.encode(m_acctpwd);
			member.setM_acctpwd(hassedPwd);
			memberDao.updatePassword(member);
			
		}
		else {
			if(!bCryptPasswordEncoder.matches(m_acctpwd, member.getM_acctpwd())) {
				return null;
			}
		}
		return new LoginResponse(member.getMember_id(), member.getM_name(), member.getM_acctid(), member.getM_email(), member.getM_tel(), member.getM_dept(), member.getM_pfp(), member.getM_role());
	}
	// 로그인 시 m_role 불러오기
	public int checkM_role(int member_id) {
		return memberDao.checkM_role(member_id);
	}
	
	// 중복되지 않은 아이디일 때 true 반환
	public boolean isM_acctidDuplicate(String m_acctid) {
		return !memberDao.duplicateCheckM_acctid(m_acctid);
	}
	// 중복되지 않은 이메일이면 true
	public boolean isM_emailDuplicate(String m_email) {
		return !memberDao.duplicateCheckM_email(m_email);
	}
	
	public String findM_acctid(String m_name, String m_email, int m_role) {
		return memberDao.findM_acctid(m_name, m_email, m_role);
	}
	public String findM_acctpwd(String m_acctid, String m_email, int m_role) {
		return memberDao.findM_acctpwd(m_acctid, m_email, m_role);
	}
	
	// 비밀번호 재설정
	public int updateM_acctpwd(MemberDO member) {
		member.setM_acctpwd(bCryptPasswordEncoder.encode(member.getM_acctpwd()));
		return memberDao.updatePassword(member);
	}
	
	public int updateM_status(MemberDO member) {
		return memberDao.updateMemberStatus(member);
	}
}
