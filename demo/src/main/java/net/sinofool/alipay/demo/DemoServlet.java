package net.sinofool.alipay.demo;

import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DemoServlet extends HttpServlet {
    private PCHandler pcHandler = new PCHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            dispatch(req, resp);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            dispatch(req, resp);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, ParserConfigurationException, SAXException {
        String uri = req.getRequestURI();
        if ("/alipay-java-sdk-demo/".equalsIgnoreCase(uri)) {
            processIndex(req, resp);
        } else if ("/alipay-java-sdk-demo/pc".equalsIgnoreCase(uri)) {
            pcHandler.processBegin(req, resp);
        } else if ("/alipay-java-sdk-demo/pc/notify".equalsIgnoreCase(uri)) {
            pcHandler.processNotify(req, resp);
        } else if ("/alipay-java-sdk-demo/pc/return".equalsIgnoreCase(uri)) {
            pcHandler.processReturn(req, resp);
        } else {
            resp.sendRedirect("/alipay-java-sdk-demo/");
        }
    }

    private void processIndex(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Content-Type", "text/html; charset=utf-8");
        resp.getWriter().write("<html><body>" +
                "<p><a href=\"pc\">PC</a></p>" +
                "</body></html>");
    }
}
