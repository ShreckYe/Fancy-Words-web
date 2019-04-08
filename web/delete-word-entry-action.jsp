<%@ page import="fancywords.FancyWordsServer" %>
<%@ page import="fancywords.JspServerContext" %>
<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<%!
    FancyWordsServer server = JspServerContext.getServer();
%>
{
    "success": <%=server.deleteUserWordEntry(request.getParameter("word")) ? 1: 0%>
}