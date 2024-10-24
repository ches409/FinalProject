package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.*;
import com.project.model.dao.CourseItemDAO;
import com.project.model.dao.NoticeItemDAO;

@Service
public class MainSO extends ItemSO {
	@Autowired
	private CourseItemDAO courseItemDAO;
	@Autowired
	private NoticeItemDAO noticeItemDAO;

	private int limit;

	public MainSO() {
		super();
		this.limit = 10;
	}

	/* 강의 관련 메소드 정의 */

	public List<CourseItem> selectByMemberId(int memberId, int page) {
		return courseItemDAO.selectByMemberId(memberId, this.getStartNum(page, limit), this.getEndNum(page, limit));
	}

	public List<CourseItem> selectByInstructorId(int memberId, int page) {
		return courseItemDAO.selectByInstructorId(memberId, this.getStartNum(page, limit), this.getEndNum(page, limit));
	}

	public int getSizeByMemberId(int memberId) {
		return this.getSize(courseItemDAO.getCountByMemberId(memberId), limit);
	}

	public int getSizeByInstructorId(int memberId) {
		return this.getSize(courseItemDAO.getCountByInstructorId(memberId), limit);
	}

	public List<CourseItem> selectByDates(int page, int memberId) {
		return courseItemDAO.selectByDates(this.getStartNum(page, limit), this.getEndNum(page, limit), memberId);
	}

	public List<CourseItem> selectByDates(int page, int memberId, String keyword) {
		return courseItemDAO.selectByDates(keyword, this.getStartNum(page, limit), this.getEndNum(page, limit),
				memberId);
	}

	public int getSizeByDates(int memberId) {
		return this.getSize(courseItemDAO.getCountByDates(memberId), limit);
	}

	public int getSizeByDates(int memberId, String keyword) {
		return this.getSize(courseItemDAO.getCountByDates(keyword, memberId), limit);
	}

	public int checkCourseForStudentId(int memberId) {
		return courseItemDAO.checkCourseForStudentId(memberId);
	}

	public int checkCourseForCourseId(int memberId) {
		return courseItemDAO.checkCourseForCourseId(memberId);
	}

	public Timetable getTimetable(int studentId) {
		return courseItemDAO.getTimetable(studentId);
	}

	public boolean isQRValid(int studentId, String code) {
		return courseItemDAO.isQRValid(code, studentId) > 0;
	}

	public int updateTimetable(int studentId, String keyword) {
		String updateKey = "";

		switch (keyword) {
		case "입실":
			updateKey = "setCheckin";
			break;
		case "퇴실":
			updateKey = "setCheckout";
			break;
		case "외출":
			updateKey = "setStepout";
			break;
		case "복귀":
			updateKey = "setReturn";
			break;
		default:
			return -1;
		}

		return courseItemDAO.updateTimetable(updateKey, studentId);
	}

	public CourseItem getInfoByStudentId(int studentId) {
		CourseItem courseItem = courseItemDAO.getInfoByStudentId(studentId);
		return courseItemDAO.getQrCode(courseItem.getCourseId(), courseItem);
	}

	public CourseItem getInfoByCourseId(int courseId) {
		CourseItem courseItem = courseItemDAO.getInfoByCourseId(courseId);
		return this.getQrCode(courseId, courseItem);
	}

	public CourseItem getQrCode(int courseId, CourseItem courseItem) {
		return courseItemDAO.getQrCode(courseId, courseItem);
	}

	public StatsItem getStats(int studentId) {
		return courseItemDAO.getStats(studentId);
	}

	public StatsItem getStatsByCourseId(int courseId) {
		boolean attended = courseItemDAO.checkAttendStatus(courseId) > 0;
		if (!attended) {
			/* 만일 출석 데이터가 존재하지 않다면 */
			courseItemDAO.initAttendance(courseId);
		}
		return courseItemDAO.getStatsByCourseId(courseId);
	}

	public boolean createQR(int courseId, String qrCode) {
		return courseItemDAO.createQR(courseId, qrCode) > 0;
	}

	public boolean checkCourseConflicts(int memberId, int courseId) {
		return courseItemDAO.checkCourseConflicts(memberId, courseId) > 0;
	}

	public boolean register(int memberId, int courseId) {
		return courseItemDAO.register(courseId, memberId) > 0;
	}

	public boolean checkAlreadyRegistered(int memberId, int courseId) {
		return courseItemDAO.checkAlreadyRegistered(courseId, memberId) > 0;
	}

	/* 공지사항 관련 DAO 메소드 정의 */

	public List<NoticeItem> selectList(int page) {
		return noticeItemDAO.selectList(this.getStartNum(page, limit), this.getEndNum(page, limit));
	}

	public List<NoticeItem> selectList(int page, String keyword) {
		return noticeItemDAO.selectByKeyword(keyword, this.getStartNum(page, limit), this.getEndNum(page, limit));
	}

	public List<NoticeItem> selectAll(int page) {
		return noticeItemDAO.selectAll(this.getStartNum(page, limit), this.getEndNum(page, limit));
	}

	public List<NoticeItem> selectAll(int page, String keyword) {
		return noticeItemDAO.selectAllByKeyword(keyword, this.getStartNum(page, limit), this.getEndNum(page, limit));
	}

	public List<NoticeItem> selectList(int startNum, int endNum) {
		return noticeItemDAO.selectList(startNum, endNum);
	}

	public NoticeItem selectOne(int noticeId) {
		return noticeItemDAO.selectOne(noticeId);
	}

	public int getSize() {
		return this.getSize(noticeItemDAO.getListSize(), limit);
	}

	public int getSize(String keyword) {
		return this.getSize(noticeItemDAO.getListSize(keyword), limit);
	}

	public int getTotalSize() {
		return this.getSize(noticeItemDAO.getAllSize(), limit);
	}

	public int getTotalSize(String keyword) {
		return this.getSize(noticeItemDAO.getAllSize(keyword), limit);
	}
}