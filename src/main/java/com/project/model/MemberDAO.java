package com.project.model;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class MemberDAO {

	private JdbcTemplate jdbcTemplate;
	private String sql;
	
	public MemberDAO(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}
	
	public MemberDO selectedByMember_id(int member_id) {
		MemberDO member = null;
		this.sql = "select member_id, m_name, m_acctid, m_acctpwd, m_email, m_tel, m_dept, m_pfp, m_status, m_role "
				+ "from final_member "
				+ "where member_id=?";
		
		member = this.jdbcTemplate.queryForObject(sql, new MemberRowMapper(), member_id);
		return member;
	}
	
	public MemberDO login(String m_acctid) {
		MemberDO member = null;
		this.sql = "select member_id, m_name, m_acctid, m_acctpwd, m_email, m_tel, m_dept, m_pfp, m_status, m_role "
				+ "from final_member "
				+ "where m_acctid=?";
		member = this.jdbcTemplate.queryForObject(sql, new MemberRowMapper(), m_acctid);
		return member;
	}
	
	public MemberDO findMember_idByM_acctid(String m_acctid) {
		this.sql = "select member_id from final_member where m_acctid=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new MemberRowMapper(), m_acctid);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public int checkM_role(int member_id) {
		this.sql = "select m_role from final_member where member_id=?";
		return this.jdbcTemplate.queryForObject(sql, int.class, member_id);
	}
	
	// 중복된 아이디가 존재하면 true, 없으면 false
	public boolean duplicateCheckM_acctid(String m_acctid) {
		this.sql = "select m_acctid from final_member where m_acctid=?";
		
		try {
			String result = this.jdbcTemplate.queryForObject(sql, String.class, m_acctid);
			return result != null;
		}
		catch (EmptyResultDataAccessException e) {
			return false;
		}
	}
	
	// 중복된 이메일이 존재하면 true, 없으면 false
	public boolean duplicateCheckM_email(String m_email) {
		this.sql = "select m_email from final_member where m_email=?";
		
		try {
			String result = this.jdbcTemplate.queryForObject(sql, String.class, m_email);
			return result != null;
		}
		catch (EmptyResultDataAccessException e) {
			return false;
		}
		
	}

	public String findM_acctid(String m_name, String m_email, int m_role) {
		this.sql = "select m_acctid from final_member where m_name=? and m_email=? and m_role=?";
		try {
			String result = this.jdbcTemplate.queryForObject(sql, String.class, m_name, m_email, m_role);
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public String findM_acctpwd(String m_acctid, String m_email, int m_role) {
		this.sql = "select member_id from final_member where m_acctid=? and m_email=? and m_role=?";
		try {
			String result = this.jdbcTemplate.queryForObject(sql, String.class, m_acctid, m_email, m_role);
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public int insertMember(MemberDO member) {
		this.sql = "insert into final_member (member_id, m_acctid, m_acctpwd, m_name, m_email, m_tel, m_dept, m_status, m_role) "
				+ "values (seq_member_id.nextval, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			return this.jdbcTemplate.update(sql, member.getM_acctid(), member.getM_acctpwd(), member.getM_name(), member.getM_email(), member.getM_tel(), member.getM_dept(), member.getM_status(), member.getM_role());			
		}
		catch(DuplicateKeyException e) {
			throw new RuntimeException("아이디 혹은 이메일이 중복되었습니다.");
		}
	}
	
	public int updateEmail(MemberDO member) {
		this.sql = "update final_member "
				+ "set m_email=?"
				+ "where member_id=?";
		return this.jdbcTemplate.update(sql, member.getM_email(), member.getMember_id());
	}
	
	public int updatePassword(MemberDO member) {
		this.sql = "update final_member "
				+ "set m_acctpwd=? "
				+ "where member_id=?";
		return this.jdbcTemplate.update(sql, member.getM_acctpwd(), member.getMember_id());
	}
	
	public int updateMemberStatus(MemberDO member) {
		this.sql = "update final_member "
				+ "set m_status=?"
				+ "where member_id=?";
		return this.jdbcTemplate.update(sql, member.getM_status(), member.getMember_id());
	}
	
	public int updateMemberProfileImg(MemberDO member) {
		this.sql = "update final_member "
				+ "set m_pfp=?"
				+ "where member_id=?";
		return this.jdbcTemplate.update(sql, member.getM_pfp());
	}
	
	public int deleteMemberInfo(MemberDO member) {
		this.sql = "delete from final_member "
				+ "where member_id=?";
		return this.jdbcTemplate.update(sql, member.getMember_id());
	}

}
