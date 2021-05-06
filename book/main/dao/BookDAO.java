package kr.co.bit.library.book.main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.bit.library.common.util.JDBC_Close;
import kr.co.bit.library.main.SaveID;
import kr.co.bit.library.main.dto.BookDTO;
import kr.co.bit.library.main.dto.BookedBookDTO;
import kr.co.bit.library.main.dto.MemberDTO;

public class BookDAO {

	private final String DRIVER = "oracle.jdbc.OracleDriver";
	private String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private String USER = "BLIBRARY";
	private String PASSWORD = "BLIBRARY";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public BookDAO() {

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

	// 입력받은 키워드로 해당 키워드를 가진 도서 목록 출력
	public List<BookDTO> BookList(String bookname) {
		List<BookDTO> list = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT * FROM BOOK WHERE BOOKNAME LIKE '%' || ?  ||'%'");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, bookname);
			rs = pstmt.executeQuery();

			list = new ArrayList<BookDTO>();
			while(rs.next()) {
				list.add(new BookDTO(rs.getInt("SERIALNUMBER"), rs.getString("CALLNUMBER"), rs.getString("LOCATION"),
						rs.getString("BOOKNAME"), rs.getString("AUTHOR"), rs.getString("PUBLISHER"), rs.getInt("YEAR"),
						rs.getInt("STOCK")));
			} 
			
			//System.out.println(list.size());
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}

		return list;
	}

	// 입력받은 책의 시리얼넘버를 이용하여 재고 관리
	public int manageStock(int serialNumber) {

		int result = 0;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" UPDATE BOOK SET STOCK = STOCK-1 ");
			sb.append(" WHERE SERIALNUMBER = ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, serialNumber);

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			JDBC_Close.closeConnStmt(conn, pstmt);
		}
		return result;
	}

	// 시리얼넘버를 입력받아서 해당 책의 재고를 리턴
	public BookDTO bookStock(int serialNumber) {

		BookDTO dto = new BookDTO();
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT STOCK FROM BOOK WHERE SERIALNUMBER = ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, serialNumber);

			rs = pstmt.executeQuery();

			if (rs.next()) {

				dto = new BookDTO(rs.getInt("STOCK"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}
		return dto;

	}

	// 책 대출 횟수
	public int RentalBookCnt(int serialNumber) {

		int checkCount = 0;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" UPDATE BOOK SET CHECKCOUNT = CHECKCOUNT+1 ");
			sb.append(" WHERE SERIALNUMBER = ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, serialNumber);

			checkCount = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmt(conn, pstmt);
		}

		return checkCount;

	}
	
	// 책 대출 횟수리턴
		public int RentalBookCntReturn(int serialNumber) {

			int checkCount = 0;
			try {
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				StringBuilder sb = new StringBuilder();
				sb.append(" SELECT CHECKCOUNT FROM BOOK ");
				sb.append(" WHERE SERIALNUMBER = ? ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, serialNumber);

				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					checkCount = rs.getInt("CHECKCOUNT");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				JDBC_Close.closeConnStmt(conn, pstmt);
			}

			return checkCount;

		}

	// 책을 예약하면 정보가 넘어옴. -> BOOKED_BOOK 테이블에 insert
	public int BookedBook(int serialNumber, String bookName) {

		BookDTO dto = new BookDTO();
		int result = 0;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" INSERT INTO BOOKED_BOOK ");
			sb.append(" (ID, SERIALNUMBER, BOOKNAME, REGDATE, EXPIREDATE ) ");
			sb.append(" VALUES (?,?,?, SYSDATE, SYSDATE+5)");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, SaveID.myId);
			pstmt.setInt(2, serialNumber);
			pstmt.setString(3, bookName);

			result = pstmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmt(conn, pstmt);
		}

		return result;
	}

	// 시리얼넘버를 입력받아서 해당 책의 재고를 리턴
	public BookDTO RtBookName(int serialNumber) {

		BookDTO dto = new BookDTO();
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT BOOKNAME FROM BOOK WHERE SERIALNUMBER = ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, serialNumber);

			rs = pstmt.executeQuery();

			if (rs.next()) {

				dto = new BookDTO(rs.getString("BOOKNAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}
		return dto;

	}

	// 대여정보
	public List<BookedBookDTO> InfoRentalBook(String id) {
		List<BookedBookDTO> list = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT * FROM BOOKED_BOOK WHERE ID = ? ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			list = new ArrayList<BookedBookDTO>();
			while (rs.next()) {

				list.add(new BookedBookDTO(rs.getInt("SERIALNUMBER"), rs.getString("ID"), rs.getString("BOOKNAME"),
						rs.getDate("REGDATE"), rs.getDate("EXPIREDATE")));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}

		return list;
	}

	// 베스트셀러 - 대여횟수순 조회
	public List<BookDTO> BestSeller() {

		List<BookDTO> list = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM BOOK ");
			sb.append(" WHERE CHECKCOUNT > 0 ");
			sb.append(" ORDER BY CHECKCOUNT DESC ");

			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			list = new ArrayList<BookDTO>();
			while (rs.next()) {
				list.add(new BookDTO(rs.getInt("SERIALNUMBER"), rs.getString("CALLNUMBER"), rs.getString("LOCATION"),
						rs.getString("BOOKNAME"), rs.getString("AUTHOR"), rs.getString("PUBLISHER"), rs.getInt("YEAR"),
						rs.getInt("CHECKCOUNT"), rs.getInt("STOCK")));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}

		return list;
	}

	// 베스트셀러 - 평점순 조회
	public List<BookDTO> Rating() {

		List<BookDTO> list = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM BOOK ");
			sb.append(" WHERE RATING > 0 ");
			sb.append(" ORDER BY RATING DESC ");

			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			list = new ArrayList<BookDTO>();
			while (rs.next()) {
				list.add(new BookDTO
						(rs.getInt("SERIALNUMBER"), 
						rs.getString("CALLNUMBER"), 
						rs.getString("LOCATION"),
						rs.getString("BOOKNAME"), 
						rs.getString("AUTHOR"), 
						rs.getString("PUBLISHER"), 
						rs.getInt("YEAR"),
						rs.getDouble("RATING"), 
						rs.getInt("STOCK"),
						rs.getInt("CHECKCOUNT")));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}

		return list;
	}
	
	// 책이름 리턴
	public String returnBookname(int serialNumber) {
		
		String bookName = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT BOOKNAME FROM BOOK WHERE SERIALNUMBER = ? ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, serialNumber);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bookName = rs.getString("BOOKNAME");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}
		return bookName;
		
	}
	
	// 모든 책 이름 리턴
	public List<BookDTO> returnAllBookname(String bookname) {
		List<BookDTO> list = null;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT BOOKNAME FROM BOOK WHERE BOOKNAME LIKE '%' || ?  ||'%' ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, bookname);
			
			rs = pstmt.executeQuery();
			
			list = new ArrayList<BookDTO>();
			
			if(rs.next()) {
				list.add(new BookDTO(rs.getString("BOOKNAME")));
			} else {
				list=null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}
		
		
		return list;
	}
	
	// 도서반납 
	public int ReturnBook(double rating,  int checkCount, int serialNumber) {
		
		int result = 0;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" UPDATE BOOK SET ");
			sb.append(" STOCK = STOCK+1, ");
			sb.append(" RATING =  (RATING+ ?)/? ");
			sb.append(" WHERE SERIALNUMBER = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setDouble(1, rating);
			pstmt.setInt(2, checkCount);
			pstmt.setInt(3, serialNumber);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmt(conn, pstmt);
		}
		
		return result;
		
	}
	
	// 도서 반납하면 bookedbook 테이블에 기록 삭제
	public int deleteBookedBook(int serialNumber) {
		
		int result = 0 ;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" DELETE FROM BOOKED_BOOK WHERE SERIALNUMBER = ? ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, serialNumber);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmt(conn, pstmt);
		}
		
		return result;
	}
	
	// 시리얼번호 입력해서 만료날짜 가져오기
	public Date ExpireDate(int serialNumber) {
		
		Date result = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT TO_CHAR(TRUNC(EXPIREDATE), 'YYYY-MM-DD') AS EXPIREDATE ");
			sb.append(" FROM BOOKED_BOOK WHERE SERIALNUMBER = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, serialNumber);
			
			rs = pstmt.executeQuery();

			while(rs.next()) {
				result = rs.getDate("EXPIREDATE");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}
		
		return result;
	}
	
	// 시리얼넘버 유무 리턴
	public boolean SerialNumberReturn(int SerialNumber) {
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT SERIALNUMBER FROM BOOKED_BOOK WHERE SERIALNUMBER = ? ");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, SerialNumber);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("SERIALNUMBER").equals(SerialNumber)) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC_Close.closeConnStmtRs(conn, pstmt, rs);
		}
	
		return false;
	}
	

	   // 관리자 메뉴
	   // 도서 현황
	   // 도서 입력
	   
	   /*
	    *    private int serialNumber;
	   private String callNumber;
	   private String location;
	   private String bookName;
	   private String author;
	   private String publisher;
	   private int year;
	   private int stock;
	    * 
	    */
	   // 도서 입력
	   public int insertBook(BookDTO book) {
	      int result = 0;
	         try {
	             conn = DriverManager.getConnection(URL, USER, PASSWORD);
	             StringBuilder sb = new StringBuilder();
	             sb.append(" INSERT INTO BOOK ");
	             sb.append(" (SERIALNUMBER, CALLNUMBER, LOCATION, BOOKNAME, AUTHOR, "
	                   + " PUBLISHER, YEAR, STOCK ) ");
	             sb.append(" VALUES (?,?,?,?,?,?,?,?)");

	             pstmt = conn.prepareStatement(sb.toString());
	             pstmt.setInt(1, book.getSerialNumber());
	             pstmt.setString(2, book.getCallNumber());
	             pstmt.setString(3, book.getLocation());
	             pstmt.setString(4, book.getBookName());
	             pstmt.setString(5, book.getAuthor());
	             pstmt.setString(6, book.getPublisher());
	             pstmt.setInt(7, book.getYear());
	             pstmt.setInt(8, book.getStock());

	             result = pstmt.executeUpdate();

	          } catch (SQLException e) {

	             e.printStackTrace();
	          } finally {
	             JDBC_Close.closeConnStmt(conn, pstmt);
	          }
	      return result;
	   }
	   
	   
	   // 도서 재고 수정
	   public int modifyBookStock(int serialNumber, int stock) {
	      int result = 0;

	         try {
	            conn = DriverManager.getConnection(URL, USER, PASSWORD);
	            StringBuilder sb = new StringBuilder();
	            sb.append(" UPDATE BOOK SET STOCK = ? ");
	            sb.append(" WHERE SERIALNUMBER = ? ");

	            pstmt = conn.prepareStatement(sb.toString());
	            pstmt.setInt(1, stock);
	            pstmt.setInt(2, serialNumber);

	            result = pstmt.executeUpdate();

	         } catch (SQLException e) {
	            e.printStackTrace();
	         } finally {

	            JDBC_Close.closeConnStmt(conn, pstmt);
	         }
	         System.out.println(result+ "건 수정하였습니다.");
	         return result;
	   }
	   
	   
	   
	   public int modifyBookLocation(int serialNumber, String location) {
	      int result = 0;

	         try {
	            conn = DriverManager.getConnection(URL, USER, PASSWORD);
	            StringBuilder sb = new StringBuilder();
	            sb.append(" UPDATE BOOK SET LOCATION = ? ");
	            sb.append(" WHERE SERIALNUMBER = ? ");

	            pstmt = conn.prepareStatement(sb.toString());
	            pstmt.setString(1, location);
	            pstmt.setInt(2, serialNumber);

	            result = pstmt.executeUpdate();

	         } catch (SQLException e) {
	            e.printStackTrace();
	         } finally {

	            JDBC_Close.closeConnStmt(conn, pstmt);
	         }
	         return result;
	   }
	   
	   
	   
	   // 삭제 - 안씀
	   public int deleteBook(int serialNumber) {
	      int result = 0;

	         try {
	            conn = DriverManager.getConnection(URL, USER, PASSWORD);
	            StringBuilder sb = new StringBuilder();
	            sb.append(" UPDATE BOOK SET STOCK = 0 ");
	            sb.append(" WHERE SERIALNUMBER = ? ");

	            pstmt = conn.prepareStatement(sb.toString());
	            pstmt.setInt(1, serialNumber);

	            result = pstmt.executeUpdate();

	         } catch (SQLException e) {
	            e.printStackTrace();
	         } finally {

	            JDBC_Close.closeConnStmt(conn, pstmt);
	         }
	         return result;
	   }
	
	

}
















