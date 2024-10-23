package com.project.model;

import java.sql.Timestamp;
import java.util.UUID;

import java.util.List;

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

    public int updateStatus(int member_id, int m_status) {
        String sql = "UPDATE FINAL_MEMBER SET M_STATUS = ? WHERE MEMBER_ID = ?";
        return this.jdbcTemplate.update(sql, m_status, member_id);
    }

    public int checkM_role(int member_id) {
        this.sql = "select m_role from final_member where member_id=?";
        return this.jdbcTemplate.queryForObject(sql, int.class, member_id);
    }

    public int checkM_status(int member_id) {
        String sql = "select m_status from final_member where member_id = ?";
        return this.jdbcTemplate.queryForObject(sql, Integer.class, member_id);
    }

    // 중복된 아이디가 존재하면 true, 없으면 false
    public boolean duplicateCheckM_acctid(String m_acctid) {
        this.sql = "select m_acctid from final_member where m_acctid=?";

        try {
            String result = this.jdbcTemplate.queryForObject(sql, String.class, m_acctid);
            return result != null;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    // 중복된 이메일이 존재하면 true, 없으면 false
    public boolean duplicateCheckM_email(String m_email) {
        this.sql = "select m_email from final_member where m_email=?";

        try {
            String result = this.jdbcTemplate.queryForObject(sql, String.class, m_email);
            return result != null;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

    }

    public String findM_acctid(String m_name, String m_email, int m_role) {
        this.sql = "select m_acctid from final_member where m_name=? and m_email=? and m_role=?";
        try {
            String result = this.jdbcTemplate.queryForObject(sql, String.class, m_name, m_email, m_role);
            return result;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String findM_acctpwd(String m_acctid, String m_email, int m_role) {
        this.sql = "select member_id from final_member where m_acctid=? and m_email=? and m_role=?";
        try {
            String result = this.jdbcTemplate.queryForObject(sql, String.class, m_acctid, m_email, m_role);
            return result;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String generateTemporaryId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder tempId = new StringBuilder("inst_");
        for (int i = 0; i < 3; i++) { 
            int index = (int) (Math.random() * characters.length());
            tempId.append(characters.charAt(index));
        }
        String generatedId = tempId.toString();
        System.out.println("Generated Temporary ID: " + generatedId);
        return generatedId;
    }

    public int insertMember(MemberDO member) {
        System.out.println("insertMember called with m_acctid: " + member.getM_acctid());
        if (member.getM_acctid() == null || member.getM_acctid().isEmpty()) {
            member.setM_acctid(generateTemporaryId());
        }

        if (member.getM_acctpwd() != null && member.getM_acctpwd().length() > 15) {
        	member.setM_acctpwd(member.getM_acctpwd().substring(0, 15));
            System.out.println("Trimmed m_acctpwd to 15 chars: " + member.getM_acctpwd());
        }
        if (member.getM_acctid() != null && member.getM_acctid().length() > 15) {
            member.setM_acctid(member.getM_acctid().substring(0, 15));
            System.out.println("Trimmed m_acctid to 15 chars: " + member.getM_acctid());
        }

        this.sql = "insert into final_member (member_id, m_acctid, m_acctpwd, "
                + "m_name, m_email, m_tel, m_dept, m_status, m_role) "
                + "values (seq_member_id.nextval, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            int result = this.jdbcTemplate.update(sql, member.getM_acctid(), member.getM_acctpwd(),
                    member.getM_name(), member.getM_email(), member.getM_tel(), member.getM_dept(),
                    member.getM_status(), member.getM_role());
            System.out.println("Insert successful for m_acctid: " + member.getM_acctid());
            return result;
        } catch (DuplicateKeyException e) {
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
                + "set m_status=?, m_deactivation_date = ? "
                + "where member_id=?";
        Timestamp timestamp = (member.getDeactivationDate() != null) ? Timestamp.valueOf(member.getDeactivationDate()) : null;
        return this.jdbcTemplate.update(sql, member.getM_status(), timestamp, member.getMember_id());
    }

    public int updateMemberProfileImg(MemberDO member) {
        this.sql = "update final_member "
                + "set m_pfp=?"
                + "where member_id=?";
        return this.jdbcTemplate.update(sql, member.getM_pfp());
    }

    public int deleteMemberInfo() {
        String sql = "delete from final_member "
                + "where m_status=0 and m_deactivation_date<sysdate-interval '5' MINUTE";
        return this.jdbcTemplate.update(sql);
    }

    public List<MemberDO> getAllInstructors() {
        this.sql = "select * from final_member where m_role = 1";
        return this.jdbcTemplate.query(sql, new MemberRowMapper());
    }

    public List<MemberDO> getPagedInstructors(int page, int itemsPerPage) {
        int offset = (page - 1) * itemsPerPage;
        this.sql = """
                select * from (
                    select rownum as rn, fm.* from final_member fm
                    where fm.m_role = 1 and rownum <= ?
                ) where rn > ?
                """;
        return this.jdbcTemplate.query(sql, new MemberRowMapper(), offset + itemsPerPage, offset);
    }

    public int getTotalInstructorsCount() {
        this.sql = "select count(*) from final_member where m_role = 1";
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<MemberDO> searchInstructors(String keyword) {
        String sql = "SELECT * from final_member where m_name LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%"}, new MemberRowMapper());
    }
}
