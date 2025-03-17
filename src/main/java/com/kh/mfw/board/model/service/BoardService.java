package com.kh.mfw.board.model.service;

import static com.kh.mfw.common.Template.getSqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.kh.mfw.board.model.dao.BoardDAO;
import com.kh.mfw.board.model.dto.BoardDTO;

public class BoardService {

	private BoardDAO  boardDao = new BoardDAO();
	
	public int insertBoard(BoardDTO board) {
		
		SqlSession sqlSession = getSqlSession();
		
		int result = boardDao.insertBoard(sqlSession, board);
		
		sqlSession.commit();
		sqlSession.close();
		
		return result;
	}
	
	public Map<String, Object> selectBoards(int page){
		/*
		 * page 		: 현재 페이지 번호
		 * boardCount 	: 데이터 행 총 개수
		 * boardLimit 	: 한 페이지에 보여줄 게시글 개수
		 * maxPage 		: 맨끝 마지막 페이지 버튼 번호
		 * btnLimit 	: 한 페이지에 보여줄 버튼 개수
		 * startBtn		: 페이지에 보이는 처음 시작하는 버튼
		 * endBtn		: 페이지에 보이는 마지막 버튼
		 */
		
		SqlSession sqlSession = getSqlSession();

		// KH_BOARD 총 데이터 행 개수
		int boardCount = boardDao.selectBoardCount(sqlSession);
		// page = 앞단에서 넘어온 요청 페이지
		int boardLimit = 4; // 한 페이지에 보여줄 게시글 개수
		
		// 마지막 페이지
		// 한 페이지에 보여줄 게시글 개수 : 10개
		int maxPage = (int)(Math.ceil(boardCount / (double)boardLimit));
		/*
		 * boardCount 		한 페이지 개수 			마지막 페이지
		 * 
		 * 	  100               10                       10.0      10
		 * 	  103               10                       11.3      11
		 *    112               10                       12.2      12
		 * 
		 * => 총 게시글 개수 / 한 페이지 개수 를 올림 처리 할 경우 마지막 페이지를 구할 수 있음
		 */
		
		// startBtn : 페이지 하단에 보여질 버튼 중 시작 값
		/*
		 * 한 페이지에 보여질 버튼 개수 : 5개
		 * 
		 * start : 1, 6, 11, 16 ... => n * 5 + 1
		 * 
		 * 한 페이지에 보여질 버튼 개수 : 3개
		 * 
		 * start : 1, 4, 7, 10 ... => n * 3 + 1
		 * 
		 * startBtn = n * 한 페이지 개수 + 1;
		 *
		 * page			start
		 * 	1			  1
		 * 	5			  1
		 * 	6			  6
		 *  8 			  6
		 * 	10			  10
		 *  14			  11
		 *  => 1 ~ 5 	: n * 5 + 1 ==> n == 0
		 *  => 6 ~ 10 	: n * 5 + 1 ==> n == 1
		 *  => 11 ~ 15 	: n * 5 + 1 ==> n == 2
		 * 	
		 * 	1 ~  5  - 1 == (0 ~ 4)   / 5 == 0
		 * 	6 ~  10 - 1 == (5 ~ 9)   / 5 == 1
		 * 	11 ~ 15 - 1 == (10 ~ 14) / 5 == 2
		 * 
		 * (page - 1) / 5 == n
		 * 
		 * startBtn = (page - 1) / 5 * 5 + 1;
		 */

		int btnLimit = 5;   // 한 페이지에 보여줄 버튼 개수
		int startBtn = (page -1) / btnLimit * btnLimit + 1;
		
		// endBtn
		/*
		 * startBtn : 1 => 5
		 * endBtn = startBtn + btnLimit -1;
		 */
		int endBtn = startBtn + btnLimit -1;
		
		if(endBtn > maxPage) endBtn = maxPage;
			
		/*
		 * MyBatis RowBounds 사용하기
		 * 
		 * offset과 limit을 생성자 매개변수로 전달해주어야 함.
		 * 
		 * page 1 : 1 ~ 3 ==> 0
		 * page 2 : 4 ~ 6 ==> 3
		 * page 3 : 7 ~ 9 ==> 6
		 * 
		 * (page - 1) * boardLimit
		 */
		RowBounds rowBounds = new RowBounds((page - 1) * boardLimit , boardLimit);
		
		List<BoardDTO> boards = boardDao.selectBoards(sqlSession, rowBounds);
		
		sqlSession.close();
		
		Map<String, Object> map = new HashMap();
		map.put("boards", boards);
		map.put("boardCount", boardCount);
		map.put("page", page);
		map.put("boardLimit", boardLimit);
		map.put("btnLimit", btnLimit);
		map.put("maxPage", maxPage);
		map.put("startBtn", startBtn);
		map.put("endBtn", endBtn);
		
		
		return map;
	}

	public BoardDTO findByBoardNo(int boardNo) {
		
		SqlSession sqlSession = getSqlSession();
		
		// 최소 두 번 가야함
		
		// 조회수 증가 로직 한 번 		==> UPDATE / COMMIT 해야함
		// 게시글 정보 조회 로직 한 번 	==> SELECT
		int updateResult = boardDao.increaseCount(sqlSession, boardNo);
		
		if(updateResult == 0) {
			sqlSession.close();
			return null;
		}
		
		BoardDTO board = boardDao.findByBoardNo(sqlSession, boardNo);
		
		if(board != null) {
			sqlSession.commit();
		}
		sqlSession.close();

		return board;
	}

}
