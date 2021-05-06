package kr.co.bit.library.book.main.dao;

import kr.co.bit.library.insert.login.dao.InsertLoginTest;
import kr.co.bit.library.login.main.dao.LoginTest;
import kr.co.bit.library.main.LibraryMain;
import kr.co.bit.library.main.SaveID;
import kr.co.bit.library.main.dto.BookDTO;
import kr.co.bit.library.main.dto.BookedBookDTO;
import oracle.sql.DATE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BookTest {
	String id = SaveID.myId;
	Scanner sc = new Scanner(System.in);
	BookDAO dao = new BookDAO();
	LoginTest loginTest = new LoginTest();
	BookDTO bookDTO = new BookDTO();
	Date last = null;
	LibraryMain main = new LibraryMain();

	// 메인에서 도서 검색 클릭 후 들어오는 화면
	public void BookMain() {

		System.out.println("__________________________________");
		System.out.println("                                  ");
		System.out.println("1. 도서 검색");
		System.out.println("2. 도서 대출");
		System.out.println("3. 도서 반납");
		System.out.println("4. 뒤로 가기");
		System.out.println("__________________________________");
		System.out.print(">>> ");
		String choice = sc.next();

		switch (choice) {

		case "1":
			// 도서 검색
			SearchBookMain();
			break;
		case "2":
			// 도서 대출
			RentalBook();
			break;

		case "3":
			// 도서 반납
			ReturnBook();
			break;

		case "4":
			LibraryMain.main(null);
			break;

		default:
			System.out.println("[!] 잘못된 입력입니다. 다시 입력해주세요.");
			BookMain();
			break;
		}

	}

	// 도서 검색
	public void SearchBookMain() {
		System.out.println("__________________________________");
		System.out.println("");
		System.out.println("1. 이달의 베스트 셀러");
		System.out.println("2. 도서 검색");
		System.out.println("3. 뒤로 가기");
		System.out.println("__________________________________");
		System.out.print(">>>");
		String menuChoice = sc.next();

		switch (menuChoice) {

		case "1":
			BestSeller();
			break;
		case "2":
			SearchBookIn();
			break;
		case "3":
			// 뒤로 가기
			BookMain();
			break;
			
		default:
			System.out.println("[!] 잘못된 입력입니다. 다시 입력해주세요.");
			SearchBookMain();

		}

	}

	// 베스트 셀러 조회 - 대출이 많은 책
	public void BestSeller() {
		// 1. 대출횟수 순 2. 평점순
		System.out.println("_________[ Best Seller ] _________");
		System.out.println("1. 대여 횟수 높은 순으로 조회"); // 구구절절..
		System.out.println("2. 평점 순 조회");
		System.out.println("3. 뒤로 가기");
		System.out.print(">>>");
		String choice = sc.next();

		switch (choice) {

		case "1":
			CheckCount();
			break;
		case "2":
			Rating();
			break;
		case "3":
			SearchBookMain();
		default:
			System.out.println("[!] 잘못된 입력입니다. 다시 입력해주세요.");
			BestSeller();
		}

	}

	// 대여횟수 높은 순 조회
	public void CheckCount() {
		System.out.println("대여횟수가 높은 순서대로 나열된 책 목록입니다.");

		List<BookDTO> list = dao.BestSeller();
		for (int i = 0; i < list.size(); i++) {
			System.out
					.println((i + 1) + " . ____________________________[대여횟수:'" + list.get(i).getCheckCount() + "'회]");
			System.out.println("연번 : " + list.get(i).getSerialNumber());
			System.out.println("청구기호 : " + list.get(i).getCallNumber());
			System.out.println("자료실 위치 : " + list.get(i).getLocation());
			System.out.println("도서명 : " + list.get(i).getBookName());
			System.out.println("저자명 : " + list.get(i).getAuthor());
			System.out.println("출판사 : " + list.get(i).getPublisher());
			System.out.println("출간연도 : " + list.get(i).getYear());
			System.out.println("__________________________________");

		}
		BookMain();

	}

	// 평점 높은 순 조회
	public void Rating() {
		System.out.println("평점이 높은 순서대로 나열된 책 목록입니다.");

		List<BookDTO> list = dao.Rating();

		for (int i = 0; i < list.size(); i++) {
			System.out.println((i + 1) + " . ____________________________[평점:'" + list.get(i).getRating() + "']");
			System.out.println("연번 : " + list.get(i).getSerialNumber());
			System.out.println("청구기호 : " + list.get(i).getCallNumber());
			System.out.println("자료실 위치 : " + list.get(i).getLocation());
			System.out.println("도서명 : " + list.get(i).getBookName());
			System.out.println("저자명 : " + list.get(i).getAuthor());
			System.out.println("출판사 : " + list.get(i).getPublisher());
			System.out.println("출간연도 : " + list.get(i).getYear());
			System.out.println("__________________________________");
		}
		BookMain();

	}

	// 도서 검색-2
	public void SearchBookIn() {
		System.out.println("_________[ 도서 검색 ] _________");
		System.out.print("찾으시는 도서 명을 입력해주세요  :  ");
		String bookName = sc.next();

		List<BookDTO> list = dao.BookList(bookName);
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println((i + 1) + " . _________________________________________");
				System.out.println("연번 : " + list.get(i).getSerialNumber());
				System.out.println("청구기호 : " + list.get(i).getCallNumber());
				System.out.println("자료실 위치 : " + list.get(i).getLocation());
				System.out.println("도서명 : " + list.get(i).getBookName());
				System.out.println("저자명 : " + list.get(i).getAuthor());
				System.out.println("출판사 : " + list.get(i).getPublisher());
				System.out.println("출간연도 : " + list.get(i).getYear());
				System.out.println("__________________________________");

			}

		} else if (list.size() == 0) {
			System.out.println("[!] 해당 도서가 존재하지 않습니다. ");
			BookMain();
		}
		BookMain();
	}

	// 도서 대출
	public void RentalBook() {
		id = SaveID.myId;
		// 로그인 상태면 다음 화면, 아니면 로그인 화면

		if (id != null) {

			int i = 0;

			System.out.println("대여 할 도서명을 입력해주세요 ");
			System.out.print(">>> ");
			String bookName = sc.next();

			// 책 제목이 있다면 실행
			List<BookDTO> list = dao.returnAllBookname(bookName);
			if (list != null) {

				list = dao.BookList(bookName);
				ArrayList<Integer> list2 = new ArrayList<>();

				for (i = 0; i < list.size(); i++) {
					System.out.println((i + 1) + " . _________________________________________");
					System.out.println("연번 : " + list.get(i).getSerialNumber());
					System.out.println("청구기호 : " + list.get(i).getCallNumber());
					System.out.println("자료실 위치 : " + list.get(i).getLocation());
					System.out.println("도서명 : " + list.get(i).getBookName());
					System.out.println("저자명 : " + list.get(i).getAuthor());
					System.out.println("출판사 : " + list.get(i).getPublisher());
					System.out.println("출간연도 : " + list.get(i).getYear());
					System.out.println("재고 : " + list.get(i).getStock());

					list2.add(list.get(i).getSerialNumber());

				}
				System.out.println("--------------------------------");
				System.out.println("대여하실 책의 연번을 입력해주세요. ");
				System.out.print(">>>");

				int SerialNumber = 0;
				SerialNumber = sc.nextInt();
				// 목록에 있는 연번 입력 외에 다른 연번을 입력하면 목록에 존재하지 않는 것이라고 띄우기
				if (list2.contains(SerialNumber)) {

					// 책의 재고 확인
					BookDTO bdto = dao.bookStock(SerialNumber);
					int stock = bdto.getStock();

					// 재고가 있다면
					if (stock >= 1) {

						int result = dao.manageStock(SerialNumber);
						bookDTO = new BookDTO(result);

						// 대출 후 책의 재고
						bdto = dao.bookStock(SerialNumber);
						stock = bdto.getStock();
						// 대출 한 책 이름
						bdto = dao.RtBookName(SerialNumber);
						bookName = bdto.getBookName();

						// 대여하면 대여 횟수 증가
						int checkCount = dao.RentalBookCnt(SerialNumber);
						bookDTO = new BookDTO(checkCount);

						// 대여정보?가 넘어감
						dao.BookedBook(SerialNumber, bookName);

						System.out.println("책 대여가 완료되었습니다. ");
						// 메인으로 이동
						main.main(null);

						// 대출 후 로그인후의 화면으로 이동

					} else {
						System.out.println("[SYSTEM] 재고가 없습니다. 죄송합니다.");
						BookMain();
					}
				} else {
					System.out.println("목록에 없는 연번입니다. 다시 확인해주세요. ");
				}

			} else {
				System.out.println("[!] 해당 도서가 존재하지 않습니다. 다시 확인해주세요. ");
				BookMain();
			}

		} else {
			System.out.println("[SYSTEM] 로그인 후 이용바랍니다. ");
			loginTest.LoginView();

		}
	}

	// 대출증? 대출 확인서?
	public void InfoRentalBook() {

		System.out.println("_________[" + id + " 님의 대 여 정 보 ]_________");
		List<BookedBookDTO> list = dao.InfoRentalBook(id);

		for (int i = 0; i < list.size(); i++) {
			System.out.println("연번 : " + list.get(i).getSerialNumber());
			System.out.println("도서명 : " + list.get(i).getBookName());
			System.out.println("대여일 : " + list.get(i).getRegdate());
			System.out.println("반납일 : " + list.get(i).getExpiredate());
			System.out.println("__________________________________");
			System.out.println("			※반납일을 반드시 지켜주세요!");

		}

	}

	// 도서 반납
	public void ReturnBook() {

		while (true) {
			id = SaveID.myId;
			// 로그인 상태면 다음 화면, 아니면 로그인 화면
			if (id != null) {

				// 대여 책 리스트 가져오기 -> 여러권일 경우 선택하게 하기(연번),
				// 반납일이 초과일경우 초과한 일수동안 대여x
				// 반납할 때 평점 넣게하기...(최대5점)
				System.out.println("__________________________________");
				System.out.println("");
				System.out.println("'" + id + "' 님의 대여목록입니다. ");
				List<BookedBookDTO> list = dao.InfoRentalBook(id);

				ArrayList<Integer> list2 = new ArrayList<>();

				for (int i = 0; i < list.size(); i++) {
					System.out.println("연번 : " + list.get(i).getSerialNumber());
					System.out.println("도서명 : " + list.get(i).getBookName());
					System.out.println("대여일 : " + list.get(i).getRegdate());
					System.out.println("__________________________________");
					last = list.get(i).getExpiredate();
					list2.add(list.get(i).getSerialNumber());
				}

				System.out.println("도서를 반납하시겠습니까? (Y/N) ");
				System.out.print(">>> ");
				char choice = sc.next().toUpperCase().charAt(0);

				if (choice == 'Y') {

					int serialNumber = 0;

					System.out.println("반납할 도서의 연번을 입력해주세요. ");
					System.out.print(">>> ");
					serialNumber = sc.nextInt();

					// 대출목록에 있는 연번을 입력했을 경우 진행
					if (list2.contains(serialNumber)) {

						String bookName = dao.returnBookname(serialNumber);
						if (bookName == null) {
							System.out.println("----------------------------------");
							System.out.println("[!] 잘못된 입력입니다. 다시 입력해주세요. ");
						} else {
							System.out.println("반납을 선택한 도서는 [" + bookName + "] 입니다.");

							System.out.println("해당 도서의 평점을 입력해주세요.");
							System.out.print(">>> ");
							double rating = 0;
							try {
								rating = sc.nextDouble();

								while (rating > 5) {
									System.out.println("[!] 최대 입력 값은 5점입니다. 다시 입력해주세요.");
									System.out.print(">>> ");
									rating = sc.nextDouble();
									System.out.println();
								}

								// 최대 평점 5점

								int checkCount = dao.RentalBookCntReturn(serialNumber);

								System.out.println("해당 도서를 반납 하시겠습니까? (Y/N)");
								System.out.print(">>> ");
								char userChoice = sc.next().toUpperCase().charAt(0);

								if (userChoice == 'Y') {
									// 반납을 선택할 경우 정보 전달
									dao.ReturnBook(rating, checkCount, serialNumber);
									// 도서 반납일자 계산
									SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
									Date now = new Date(); // 오늘일자
									try {
										// Date returnDate = format1.parse("2021-05-12"); // 반납날짜 계산 위해 임의로 넣어놓은 날짜
										Date expireDate = dao.ExpireDate(serialNumber);

										long diff = now.getTime() - expireDate.getTime();
										TimeUnit time = TimeUnit.DAYS;
										long difference = time.convert(diff, TimeUnit.MILLISECONDS);
										// System.out.println(difference); 현재날짜-도서대여만료날짜

										// 반납일자 초과
										if (difference > 0) {
											System.out.println("[SYSTEM] 반납일보다 '" + difference + "'일 초과되었습니다. ");
											System.out.println(id + "님은 '" + difference + "'일 후 부터 대여 가능합니다. ");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

									// 기록 삭제
									dao.deleteBookedBook(serialNumber);
									System.out.println(" 도서 반납이 완료되었습니다. 이용해주셔서 감사합니다.");
									BookMain();

								} else if (userChoice == 'N') {
									ReturnBook();
								} else {
									System.out.println("----------------------------------");
									System.out.println("[!] 잘못된 입력입니다. 다시 입력해주세요. ");
									ReturnBook();
								}

							} catch (Exception e) {
								System.out.println("[!] 잘못된 입력입니다. 다시 입력해주세요.");
								ReturnBook();
							}
						}
					} else {
						System.out.println("목록에 없는 도서입니다. 확인해주세요.");
					}
				} else if (choice == 'N') {
					BookMain();

				} else {
					System.out.println("----------------------------------");
					System.out.println("[!] 잘못된 입력입니다. 다시 입력해주세요.");
					ReturnBook();

				}

			} else {
				System.out.println("----------------------------------");
				System.out.println("[SYSTEM] 로그인 후 이용바랍니다. ");
			}

		}
	}
}
