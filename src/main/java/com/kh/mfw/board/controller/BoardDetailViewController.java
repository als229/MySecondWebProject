package com.kh.mfw.board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mfw.board.model.dto.BoardDTO;
import com.kh.mfw.board.model.service.BoardService;

@WebServlet("/board")
public class BoardDetailViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BoardDetailViewController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	int boardNo = Integer.parseInt(request.getParameter("boardNo"));
    	
    	HttpSession session = request.getSession();
    	
    	if(boardNo < 1) {
    		session.setAttribute("message", "죽고 싶어?");
    		response.sendRedirect(request.getContextPath());
    		return;
    	}
    	
    	BoardDTO board = new BoardService().findByBoardNo(boardNo);
    	
    	if(board != null) {
    		request.setAttribute("board", board);
    		request.getRequestDispatcher("/WEB-INF/views/board/detail.jsp").forward(request, response);
    	}else {
    		session.setAttribute("message", "없는 게시물 데스");
    		response.sendRedirect(request.getContextPath());
    	}
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
