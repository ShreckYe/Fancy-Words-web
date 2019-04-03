<%@ page import="fancywords.FancyWordsServer" %>
<%@ page import="fancywords.JspServerContext" %>
<%@ page import="fancywords.UserWordEntry"%>
<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<%!
    FancyWordsServer server = JspServerContext.getServer();
%>
<%
    UserWordEntry userWordEntry = server.requestUserWordEntry(request.getParameter("word"));
%>
{
    <%
        if (userWordEntry != null) {
    %>
    "success": 1,
    "wordEntry": {
        "word": "<%=userWordEntry.getWord()%>",
        "definition": "<%=userWordEntry.getDefinition()%>",
        "timeCreated":
        <%=userWordEntry.getTimeCreated()%>,
        "timeEdited":
        <%=userWordEntry.getTimeEdited()%>,
        "mastery":
        <%=userWordEntry.getMastery(System.currentTimeMillis())%>,
        "countViewed":
        <%=userWordEntry.getCountViewed()%>,
        "countTested":
        <%=userWordEntry.getCountTested()%>,
        "countTestedCorrect":
        <%=userWordEntry.getCountTestedCorrect()%>
    }
    <%
        } else {
    %>
    "success": 0
    <%
        }
    %>
}