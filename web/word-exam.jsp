<%@ page import="fancywords.*"%>
<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<%!
    FancyWordsServer server = JspServerContext.getServer();
%>
<%
    WordExam wordExam = (WordExam) server.requestExam(Exam.EXAM_TYPE_WORD);
%>
{
    <%
        if (wordExam != null) {
    %>
    "success": 1,
    "exam": {
        "word": "<%=wordExam.getWord()%>",
        "choices": <%=Utils.toJSONArray(wordExam.getDefinitionChoices())%>,
        "correctChoice": <%=wordExam.getCorrectChoice()%>
    }
    <%
        } else {
    %>
    "success": 0
    <%
        }
    %>
}