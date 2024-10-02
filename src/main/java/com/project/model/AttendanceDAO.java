package com.project.model;

import java.sql.*;
import java.util.List;
import org.apache.tomcat.jdbc.pool.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class AttendanceDAO {

	private JdbcTemplate jdbcTemplate;
	private String sql;

	public AttendanceDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<StudentAttendanceDO> selectAllMemberAttendanceByCourse(int course_id){
		this.sql = "select m_name, m_dept, c, ab, l from (select fsa.student_id, a_status, m_name, m_dept from final_student_attend fsa inner join (select student_id, m_name, m_dept from final_member fm inner join (select * from final_course_student where course_id = ?) fcs on fm.member_id=fcs.member_id) fcm  on fsa.student_id=fcm.student_id) pivot (count(a_status) for a_status in (1 as c, 2 as ab, 3 as l)) order by student_id";	

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
	
	public CourseScheduleDO getCourseDateInfo(int course_id) {
		this.sql = "select * from final_course_schedule where course_id=?";
	
		return this.jdbcTemplate.queryForObject(this.sql, new RowMapper<CourseScheduleDO>() {
			@Override
			public CourseScheduleDO mapRow(ResultSet rs, int rownum) throws SQLException{
				CourseScheduleDO courseSchedule = new CourseScheduleDO();
				courseSchedule.setS_stime(rs.getString("s_stime"));
				courseSchedule.setS_etime(rs.getString("s_etime"));
				courseSchedule.setS_sdate(rs.getTimestamp("s_sdate").toLocalDateTime());
				courseSchedule.setS_edate(rs.getTimestamp("s_edate").toLocalDateTime());	
				return courseSchedule;
			}
		},course_id);
	}
	
	

	
}
