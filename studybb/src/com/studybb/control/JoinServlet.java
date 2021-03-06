package com.studybb.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.studybb.service.MemberService;
import com.studybb.vo.Member;

@WebServlet("/join")
public class JoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private MemberService service;
    public JoinServlet() {
        service = new MemberService();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("loginInfo"); //기존세션을 지워준다(Cleaning)
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String interest = request.getParameter("interest");
		String major = request.getParameter("major");
		String area = request.getParameter("area");
		
		Member m = new Member();
		m.setMem_id(id);
		m.setMem_pwd(pwd);
		m.setMem_email(email);
		m.setMem_name(name);
		m.setMem_gender(gender);
		m.setMem_interest(interest);
		m.setMem_major(major);
		m.setMem_area(area);
		
		String str = service.join(m);
		str = service.login(id, pwd);
		
		/*로그인성공시 HttpSession객체의 속성으로 추가*/
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(str);
			JSONObject jsonObj = (JSONObject)obj;
			if((Long)jsonObj.get("status")==1) { //로그인성공 -> 세션객체에 저장(request보다 오래살아있다)
				session.setAttribute("loginInfo", id);
				String logid = (String)session.getAttribute("loginInfo");
				System.out.println("로그인한 아이디: " + logid);
			}
		} catch(ParseException e) {
			e.printStackTrace();
		}
		request.setAttribute("result", str);
		
		String path = "/result.jsp";
		RequestDispatcher rd = request.getRequestDispatcher(path);
		rd.forward(request, response);
	}

}
