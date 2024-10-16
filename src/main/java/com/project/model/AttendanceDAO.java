package com.project.model;

import java.sql.*;
import java.util.*;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class AttendanceDAO {

	private JdbcTemplate jdbcTemplate;
	private String sql;

	public AttendanceDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public int getStudentId(int member_id, int course_id) {
		this.sql="select student_id from final_course_student where member_id=? and course_id=?";
		return this.jdbcTemplate.queryForObject(sql, int.class, member_id, course_id);
	}

	public List<StudentAttendanceDO> selectAllMemberAttendanceByCourse(int course_id){
		this.sql = "select student_id, m_name, m_dept, m_tel, c, ab, l, d from (select fsa.student_id, a_status, m_name, m_dept, m_tel from final_student_attend fsa inner join (select student_id, m_name, m_dept, m_tel from final_member fm inner join (select * from final_course_student where course_id = ?) fcs on fm.member_id=fcs.member_id) fcm  on fsa.student_id=fcm.student_id) pivot (count(a_status) for a_status in (1 as c, 2 as ab, 3 as l, 4 as d)) order by student_id";	

		return this.jdbcTemplate.query(this.sql, new RowMapper<StudentAttendanceDO>() {
			@Override
			public StudentAttendanceDO mapRow(ResultSet rs, int rownum) throws SQLException{
				StudentAttendanceDO studentAtt = new StudentAttendanceDO();
				studentAtt.setStudent_id(rs.getInt(1));
				studentAtt.setM_name(rs.getString(2));
				studentAtt.setM_dept(rs.getString(3));
				studentAtt.setM_tel(rs.getString(4));
				studentAtt.setC(rs.getLong(5));
				studentAtt.setAb(rs.getLong(6));
				studentAtt.setL(rs.getLong(7));
				studentAtt.setD(rs.getLong(8));
				return studentAtt;
			}
		},course_id);
	}
	
	
	public List<StudentAttendanceDO> selectStudentAttendance(int student_id) {
		this.sql ="select fsa.a_date, a_status , r_status,c_contents from (select a_date, a_status from final_student_attend where student_id=?) fsa left outer join (select fac.a_date, req_date, r_status, c_contents, c_attm from (select * from final_attend_correq where student_id=?) fac left outer join final_attend_corres far on far.student_id = fac.student_id) crq on fsa.a_date=crq.a_date order by a_date";
		return this.jdbcTemplate.query(this.sql, new RowMapper<StudentAttendanceDO>() {
			@Override
			public StudentAttendanceDO mapRow(ResultSet rs, int rownum) throws SQLException{
				StudentAttendanceDO studentAtt = new StudentAttendanceDO();
				studentAtt.setA_date(rs.getTimestamp("a_date").toLocalDateTime());
				studentAtt.setA_status(rs.getInt("a_status"));
				
				if(rs.getInt("r_status") == 1) {
					studentAtt.setA_request("정정승낙");	
				}else if(rs.getInt("r_status") == 2) {
					studentAtt.setA_request("정정거절");	
				}else if(rs.getString("c_contents") != null) {
					studentAtt.setA_request("정정요청");	
				}
				return studentAtt;
			}
		},student_id,student_id);
	}
	
	public StudentAttendanceDO getStudentAttendance(int student_id) {
		this.sql = "select student_id, m_name, m_dept, m_tel, c, ab, l, d from (select fcm.student_id, a_status, m_name, m_dept, m_tel from final_student_attend fsa right outer join (select student_id, m_name, m_dept, m_tel from final_member fm inner join (select * from final_course_student where student_id = ?) fcs on fm.member_id=fcs.member_id) fcm on fsa.student_id=fcm.student_id) pivot (count(a_status) for a_status in (1 as c, 2 as ab, 3 as l, 4 as d))";	
		try {
		return this.jdbcTemplate.queryForObject(this.sql, new RowMapper<StudentAttendanceDO>() {
			@Override
			public StudentAttendanceDO mapRow(ResultSet rs, int rownum) throws SQLException{
				StudentAttendanceDO studentAtt = new StudentAttendanceDO();
				studentAtt.setStudent_id(rs.getInt(1));
				studentAtt.setM_name(rs.getString(2));
				studentAtt.setM_dept(rs.getString(3));
				studentAtt.setM_tel(rs.getString(4));
				studentAtt.setC(rs.getLong(5));
				studentAtt.setAb(rs.getLong(6));
				studentAtt.setL(rs.getLong(7));
				studentAtt.setD(rs.getLong(8));
				return studentAtt;
			}
		},student_id);
		}catch(IncorrectResultSizeDataAccessException error) {
			return null;
		}
	}
	
	public List<CourseScheduleDO> getCourseDateInfo(int course_id) {
		this.sql = "select * from final_course_schedule where course_id=?";
	
		return this.jdbcTemplate.query(this.sql, new RowMapper<CourseScheduleDO>() {
			@Override
			public CourseScheduleDO mapRow(ResultSet rs, int rownum) throws SQLException{
				CourseScheduleDO courseSchedule = new CourseScheduleDO();
				courseSchedule.setS_stime(rs.getString("s_stime"));
				courseSchedule.setS_etime(rs.getString("s_etime"));
				courseSchedule.setS_cinterm(rs.getInt("s_cinterm"));
				courseSchedule.setS_coutterm(rs.getInt("s_coutterm"));
				courseSchedule.setS_sdate(rs.getTimestamp("s_sdate").toLocalDateTime());
				courseSchedule.setS_edate(rs.getTimestamp("s_edate").toLocalDateTime());	
				return courseSchedule;
			}
		},course_id);
	}
	
	public void updateAttendanceScore(CourseDO courseDO) {
		this.sql="update final_course set c_prsscore=?, c_absscore=?, c_trdscore=? where course_id=?";
		this.jdbcTemplate.update(this.sql, courseDO.getC_prsscore(),courseDO.getC_absscore(),courseDO.getC_trdscore(), courseDO.getCourse_id());
	}
	
	
	public List<StudentAttendanceDO> searchMemberAttendance(int course_id, String searchType, String searchText){
		if(searchType.equals("name")) {
			this.sql = "select m_name, m_dept, c, ab, l from (select fsa.student_id, a_status, m_name, m_dept from final_student_attend fsa inner join (select student_id, m_name, m_dept from final_member fm inner join (select * from final_course_student where course_id = ?) fcs on fm.member_id=fcs.member_id) fcm  on fsa.student_id=fcm.student_id) pivot (count(a_status) for a_status in (1 as c, 2 as ab, 3 as l)) where m_name like '%"+ searchText +"%' order by student_id";	
		}else {
			this.sql = "select m_name, m_dept, c, ab, l from (select fsa.student_id, a_status, m_name, m_dept from final_student_attend fsa inner join (select student_id, m_name, m_dept from final_member fm inner join (select * from final_course_student where course_id = ?) fcs on fm.member_id=fcs.member_id) fcm  on fsa.student_id=fcm.student_id) pivot (count(a_status) for a_status in (1 as c, 2 as ab, 3 as l)) where m_dept like '%"+ searchText +"%' order by student_id";	
		}
		
		return this.jdbcTemplate.query(this.sql, new RowMapper<StudentAttendanceDO>() {
			@Override
			public StudentAttendanceDO mapRow(ResultSet rs, int rownum) throws SQLException{
				StudentAttendanceDO studentAtt = new StudentAttendanceDO();
				studentAtt.setM_name(rs.getString(1));
				studentAtt.setM_dept(rs.getString(2));	
				studentAtt.setC(rs.getLong(3));
				studentAtt.setAb(rs.getLong(4));
				studentAtt.setL(rs.getLong(5));
				return studentAtt;
			}
		},course_id);
	}
	
	public List<AttendanceRequest> getStudentLvreq(int student_id){
		this.sql = "select fal.l_sdate, l_edate, req_date, r_status, l_reason, l_contents as contents, l_attm as attm, res_date from (select * from final_attend_lvreq where student_id=?) fal left outer join final_attend_lvres far on far.student_id = fal.student_id and far.l_sdate=fal.l_sdate";
		
		return this.jdbcTemplate.query(this.sql, new RowMapper<AttendanceRequest>() {
			@Override
			public AttendanceRequest mapRow(ResultSet rs, int rownum) throws SQLException{
				AttendanceRequest attReq = new AttendanceRequest();
				attReq.setL_sdate(rs.getTimestamp("l_sdate").toLocalDateTime());
				attReq.setL_edate(rs.getTimestamp("l_edate").toLocalDateTime());
				attReq.setReq_date(rs.getTimestamp("req_date").toLocalDateTime());
				attReq.setR_status(rs.getInt("r_status"));	
				attReq.setContents(rs.getString("contents"));
				attReq.setAttm(rs.getString("attm"));
				attReq.setL_reason(rs.getString("l_reason"));
				try{
					attReq.setRes_date(rs.getTimestamp("res_date").toLocalDateTime());
				}catch(NullPointerException e){
					attReq.setRes_date(null);
				}
				
				return attReq;
			}
		},student_id);
	}
	
	public List<AttendanceRequest> getStudentCorreq(int student_id){
		this.sql = "select fsa.a_date, a_status , req_date, r_status, c_contents as contents, c_attm as attm, res_date from (select a_date, a_status from final_student_attend where student_id=?) fsa inner join (select fac.a_date, req_date, r_status, c_contents, c_attm, res_date from (select * from final_attend_correq where student_id=?) fac left outer join final_attend_corres far on far.student_id = fac.student_id) crq on fsa.a_date=crq.a_date";
		
		return this.jdbcTemplate.query(this.sql, new RowMapper<AttendanceRequest>() {
			@Override
			public AttendanceRequest mapRow(ResultSet rs, int rownum) throws SQLException{
				AttendanceRequest attReq = new AttendanceRequest();
				attReq.setA_date(rs.getTimestamp("a_date").toLocalDateTime());
				attReq.setA_status(rs.getInt("a_status"));
				attReq.setReq_date(rs.getTimestamp("req_date").toLocalDateTime());
				attReq.setR_status(rs.getInt("r_status"));	
				attReq.setContents(rs.getString("contents"));
				attReq.setAttm(rs.getString("attm"));
				try{
					attReq.setRes_date(rs.getTimestamp("res_date").toLocalDateTime());
				}catch(NullPointerException e){
					attReq.setRes_date(null);
				}
				
				return attReq;
			}
		},student_id, student_id);
	}
	
	
	public void insertResponse(AttendanceResponse attendanceResponse){
		if(attendanceResponse.getReqType() == 1) {
			this.sql = "insert into final_attend_corres (response_id, r_status, r_details, student_id, a_date) values (seq_att_response_id.nextval, ?, ?, ?, ?)";	
		}else if(attendanceResponse.getReqType() == 2) {
			this.sql = "insert into final_attend_lvres (response_id, r_status, r_details, student_id, l_sdate) values (seq_att_response_id.nextval, ?, ?, ?, ?)";	
		}
		this.jdbcTemplate.update(this.sql, attendanceResponse.getR_status(), attendanceResponse.getR_details(), attendanceResponse.getStudent_id(), attendanceResponse.getDate());

	}
	
	public void updateStudentAttendance(AttendanceResponse attendanceResponse) {
		this.sql = "update final_student_attend set a_status=1 where a_date=? and student_id=?";	
		this.jdbcTemplate.update(this.sql, attendanceResponse.getDate(), attendanceResponse.getStudent_id());
	}
	
	public List<AttendanceCalendar> getStudentAttendanceCalendar(int student_id, int c_year, int c_month){
		this.sql = "select fc.dt, fc.d, req_type, a_status, r_status from(select * from(WITH test2 AS(SELECT trunc(c_sdate, 'MONTH') sdt, c_edate edt FROM final_course where course_id=(select course_id from final_course_student where student_id=?))SELECT TO_date(sdt + lv - 1, 'RR/MM/DD') dt,  TO_CHAR(sdt + lv - 1, 'D') d FROM (SELECT TO_DATE(sdt, 'RR/MM/DD') sdt, TO_DATE(edt, 'RR/MM/DD') edt FROM test2), (SELECT LEVEL lv FROM dual CONNECT BY LEVEL <= 365) WHERE lv <= edt - sdt + 1)) fc left outer join (select * from(WITH test1 AS(SELECT flr.l_sdate sdt, flr.l_edate edt, (select 2 from dual) req_type, r_status FROM (select * from final_attend_lvreq where student_id=?) flr left outer join (select * from final_attend_lvres where student_id=?) fls on flr.l_sdate=fls.l_sdate) SELECT TO_date(sdt + lv - 1, 'RR/MM/DD') dt,  TO_CHAR(sdt + lv - 1, 'D') d, req_type , r_status FROM (SELECT TO_DATE(sdt, 'RR/MM/DD') sdt, TO_DATE(edt, 'RR/MM/DD') edt, req_type, r_status FROM test1), (SELECT LEVEL lv FROM dual CONNECT BY LEVEL <= 99) WHERE lv <= edt - sdt + 1) UNION ALL select fcr.a_date dt, to_char(fcr.a_date,'d') d, (select 1 from dual) req_type, r_status from (select * from final_attend_correq where student_id=?) fcr left outer join (select * from final_attend_corres where student_id=?) fcs on fcr.a_date=fcs.a_date) req on fc.dt=req.dt and fc.d=req.d left outer join (select a_date as dt, to_char(a_date,'D') d, a_status from final_student_attend where student_id=?) fsa on fc.dt=fsa.dt and fc.d=fsa.d where to_char(fc.dt,'YYYY')=? and to_char(fc.dt, 'MM')=? order by 1";

		return this.jdbcTemplate.query(this.sql, new RowMapper<AttendanceCalendar>() {
			@Override
			public AttendanceCalendar mapRow(ResultSet rs, int rownum) throws SQLException{
				AttendanceCalendar attCal = new AttendanceCalendar();
				attCal.setDt(rs.getTimestamp("dt").toLocalDateTime());
				attCal.setD(rs.getString("d"));
				attCal.setReq_type(rs.getInt("req_type"));
				attCal.setA_status(rs.getInt("a_status"));	
				attCal.setReq_type(rs.getInt("r_status"));	
				
				return attCal;
			}
		},student_id, student_id, student_id, student_id, student_id, student_id, c_year, c_month);
		
	}
	
	
	
	
	

	
}
