package com.kh.mfw.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mfw.member.model.dto.MemberDTO;
import com.kh.mfw.member.model.service.MemberService;

// 회원가입
@WebServlet("/sign-up")
public class SignUpController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SignUpController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 인코딩 설정
		request.setCharacterEncoding("UTF-8");
		
		// 컨트롤러의 역할 : 요청처리 => 사용자가 입력한 값들이 저 멀리 있는 DB Server의 KH_MEMBER테이블에 한 행 INSERT
		String memberId = request.getParameter("memberId");
		String memberPw = request.getParameter("memberPw");
		String memberName = request.getParameter("memberName");
		String email = request.getParameter("email");
		
		MemberDTO member = new MemberDTO(memberId, memberPw, memberName, email);
		//=> 원래 객체는 이렇게 생성해야 하는게 이론적으로 맞음. : 객체는 생성과 동시에 유효해야 한다. but 실무에서는 다를 수 있음
		
		// 요청처리 => 사용자가 입력한 값들을 저 멀리 있는 DB Server의 KH_MEMBER 테이블에 한 행 INSERT
		int result = new MemberService().SignUp(member);
		
		String path = request.getContextPath();
		
		// 성공 했을 경우 => 웰컴페이지로 이동
		// 실패 했을 경우 => 회원가입페이지로 이동
		
		// 중복아이디가 있을 때
//		if(result != 0){
//			// 실패 했을 경우 => 회원가입페이지로 이동
//			// SendRedirect 쓰는 이유 
//			// 유지보수가 좋아짐
//			path = "/join";
//		}
		// "중복된 아이디가 존재합니다. 다른 아이디를 입력해주세유."
		
		request.getSession().setAttribute("message", result > 0 ? "로그인 되었읍니다!" : "중복된 아이디가 존재합니다. 다른 아이디를 입력해주세유.");
		response.sendRedirect(result != 0 ? path + "/join" : path);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
