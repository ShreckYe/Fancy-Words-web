<%@ page import="fancywords.FancyWordsServer" %>
<%@ page import="fancywords.JspServerContext" %>
<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<%!
FancyWordsServer server = JspServerContext.getServer();
%>
{
    "success": <%=server.updateWordExamFinished(request.getParameter("word"), Boolean.parseBoolean(request.getParameter("correct"))) ? 1 : 0%>
}