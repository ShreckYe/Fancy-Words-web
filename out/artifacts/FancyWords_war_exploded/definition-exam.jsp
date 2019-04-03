<%@ page import="fancywords.*"%>
<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<%!
    FancyWordsServer server = JspServerContext.getServer();
%>
<%
    DefinitionExam definitionExam = (DefinitionExam) server.requestExam(Exam.EXAM_TYPE_DEFINITION);
%>
{
    <%
        if (definitionExam != null) {
    %>
    "success": 1,
    "exam": {
        "word": "<%=definitionExam.getWord()%>",
        "definition": "<%=definitionExam.getDefinition()%>",
        "choices": <%=Utils.toJSONArray(definitionExam.getWordChoices())%>,
        "correctChoice": <%=definitionExam.getCorrectChoice()%>
    }
    <%
        } else {
    %>
    "success": 0
    <%
        }
    %>
}