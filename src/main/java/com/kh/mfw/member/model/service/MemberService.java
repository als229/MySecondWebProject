package com.kh.mfw.member.model.service;

import static com.kh.mfw.common.Template.getSqlSession;

import org.apache.ibatis.session.SqlSession;

import com.kh.mfw.member.model.dao.MemberDAO;
import com.kh.mfw.member.model.dto.MemberDTO;


public class MemberService {

	public MemberDTO login(MemberDTO member) {
		
		// JDBCUtil 클래스로부터
		// Static Method로 구현해놓은
		// getConnection 메서드를 호출하여
		// Connectioj 객체를 반환받았따.
		SqlSession sqlSession = getSqlSession();
		
		// 유효성 검증 => 패스(원래 해야하는데 나중에 하자 시간이 없다.)
		MemberDTO loginMember = new MemberDAO().login(sqlSession, member);
		
		sqlSession.close();
		
		return loginMember;
	}

	public int SignUp(MemberDTO member) {
		
		SqlSession sqlSession = getSqlSession();
		
//		boolean result = 
		
		if(new MemberDAO().checkId(sqlSession, member.getMemberId())) {
			sqlSession.close();
			return 0;
		}
		int result = new MemberDAO().signUp(sqlSession, member);
		
		sqlSession.commit();
		sqlSession.close();
		
		return result;
	}
	
}
