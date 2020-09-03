package com.fengchao.crm.web.filter;

import com.fengchao.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse reps, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登陆过的过滤器");
        //因为SerletRequest中没有HttpServlet方法，所以需要强转为HttpServlet
        HttpServletRequest request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) reps;
        String path = request.getServletPath();
        //不应该被拦截的资源，放行
        if("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)){
                chain.doFilter(req,reps);
        }else{
            //其他资源 进入过滤器，验证是否登陆过
            System.out.println("是否是其他资源");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            //User user = (User) request.getAttribute("user");
            if(user!=null){
                chain.doFilter(req,reps);
            }else{
                //如果user为空，则表示之前没有登陆过，则需要重定向到登陆页面
                /**
                 * 重定向的路径该怎么写？
                 * 在实际开发项目中，对于路径的使用，无论是前端还是后端，应该一律使用绝对路径
                 * 关于转发和重定向的路径怎么写
                 * 转发：使用的是特殊的绝对路径的方式，这种路径前不加/项目名，这种路径也成为内部路径
                 * /login.jap
                 * 重定向：使用的是传统绝对路径的写法，前面必须以/项目名开头，后面跟具体的资源路径
                 * /crm/login.jsp 因为不同项目的项目名不同，所以项目名最好写成活的
                 * 可以通过 ${request.contextPath}/login.jsp
                 * 重定向于请求转发的区别
                 * 请求转发过去的路径会显示的是之前的老路径，而不是最新的资源路径
                 * 应该使用重定向，重定向之后的路径应该显示最新的资源路径
                 */
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }
        }

}
