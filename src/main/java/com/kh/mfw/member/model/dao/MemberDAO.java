package com.kh.mfw.member.model.dao;

import org.apache.ibatis.session.SqlSession;

import com.kh.mfw.member.model.dto.MemberDTO;

public class MemberDAO {

	public MemberDTO login(SqlSession sqlSession, MemberDTO member) {
		
		// SqlSession이 제공하는 메서드를 통해 SQL 문을 찾아서 실행하고 결과를 받을 수 있음
		// sqlSession.sql문 종류에 맞는 메서드("mapper 파일의 namespace.SQL문의 id 속성값)
		
		return sqlSession.selectOne("memberMapper.login", member);
	}
	

	public boolean checkId(SqlSession sqlSession, String memberId) {
		return (Integer)sqlSession.selectOne("memberMapper.checkId", memberId) > 0 ? true : false;
	}
	
	public int signUp(SqlSession sqlSession, MemberDTO member) {
		return sqlSession.insert("memberMapper.signUp", member);
	}
}
