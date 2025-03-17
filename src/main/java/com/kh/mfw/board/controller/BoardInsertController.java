package com.kh.mfw.board.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mfw.board.model.dto.BoardDTO;
import com.kh.mfw.board.model.service.BoardService;
import com.kh.mfw.member.model.dto.MemberDTO;

// enrollform에서 글작성 누르면 오는 곳 => 진짜 글 insert 버튼
@WebServlet("/insert.board")
public class BoardInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BoardInsertController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		BoardDTO board = new BoardDTO();
		
		// 게시글 번호, 카테고리, 내용, 작성자, 조회수 , 작성일
		// 시퀀스	  , 앞단    , 앞단, ???   , default 값, default 값
		board.setBoardCategory(request.getParameter("category"));
		board.setBoardTitle(request.getParameter("title"));
		board.setBoardContent(request.getParameter("content"));
		
		// 작성자에 대한 정보도 담아서 보내야 하는데
		// 로그인하지 않은 사용자는 게시글 작성 요청을 보낼 수 없게끔
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loginMember") == null) {
			session.setAttribute("message", "로그인하고 하세유.");
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		String memberId = ((MemberDTO)session.getAttribute("loginMember")).getMemberId();
		// getAttribute 는 object 형 주소값 or null 만 리턴해주는 메서드.
		
		board.setBoardWriter(memberId);
		
		int result = new BoardService().insertBoard(board);
		
		// 포워딩으로 하지 않고 리다이렉트로 하는 이유가 뭘까?
		if(result > 0) {
			session.setAttribute("message", "게시글 작성되었슴당~");
		}else {
			session.setAttribute("message", "게시글 작성 실패~~~");
		}
		//response.sendRedirect(request.getContextPath() + "/boards?page=1");
		Map<String, Object> map = new BoardService().selectBoards(1);
		request.setAttribute("map", map);
		request.getRequestDispatcher("/WEB-INF/views/board/boards.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
