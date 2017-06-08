package com.imooc.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AsynServlet
 */
@WebServlet("/AsynServlet")
public class AsynServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AsynServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Servlet开始时间:"+new Date());
		
		//要让Servlet支持异步，也就是startAsync()可以使用，要添加该代码，虽然不加也不会给予警告
		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
		
		//异步执行请求，返回结果是context，我们可以通过context获取请求的信息和响应的信息
		AsyncContext context =  request.startAsync(); //这个异步启动需要过滤器类@WebFilter的asyncSupported=true，这样过滤器才能过滤
		
		//由于线程里面无法访问请求的request，所以我们将context传递给线程
		//如何把参数传递给线程，我们采用的方式是线程添加一个需要context参数的构造方法
		new Thread(new Executor(context)).start();
		System.out.println("Servlet结束时间:"+new Date());
	}
	
	public class Executor implements Runnable{
		private AsyncContext context;
		public Executor(AsyncContext context ) {
			this.context = context;
		}
		
		@Override
		public void run() {
			//执行相关的复杂业务
			try {
				Thread.sleep(1000*10); //线程睡眠10秒
//				通过构造函数获取context，于是线程里面可以通过下面两个方法获取请求的参数和响应结果进行处理
//				通常用于耗时处理操作放在这里面，我们只是模拟异步，有前面睡眠10秒就够了
//				context.getRequest();
//				context.getResponse();
				System.out.println("业务执行完成时间:"+new Date());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
