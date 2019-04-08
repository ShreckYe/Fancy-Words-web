<%@ page import="fancywords.FancyWordsServer" %>
<%@ page import="fancywords.JspServerContext" %><%@ page import="java.net.URLDecoder"%>
<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<%!
    FancyWordsServer server = JspServerContext.getServer();
%>
{
    "success": <%=server.addNewUserWordEntry(
            request.getParameter("word"),
            request.getParameter("definition")) ? 1 :0%>
}