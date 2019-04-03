<%@ page import="fancywords.FancyWordsServer" %>
<%@ page import="fancywords.JspServerContext" %>
<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<%!
    FancyWordsServer server = JspServerContext.getServer();
%>
<%
    String word = request.getParameter("word"),
    definition = request.getParameter("definition");
%>
{
    "success": <%=server.updateUserWordEntry(word, definition) ? 1 : 0%>
}