<%@ page import="fancywords.BriefUserWordEntry" %>
<%@ page import="fancywords.FancyWordsServer" %>
<%@ page import="fancywords.JspServerContext" %>
<%@ page import="fancywords.Utils" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%!
    FancyWordsServer server = JspServerContext.getServer();
%>
<!doctype html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fancy Words 单词本</title>

    <!-- Disable tap highlight on IE -->
    <meta name="msapplication-tap-highlight" content="no">

    <!-- Web Application Manifest -->
    <link rel="manifest" href="manifest.json">

    <!-- Add to homescreen for Chrome on Android -->
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="application-name" content="Fancy Words 单词本">
    <link rel="icon" sizes="192x192" href="images/touch/chrome-touch-icon-192x192.png">

    <!-- Add to homescreen for Safari on iOS -->
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-title" content="Fancy Words 单词本">
    <link rel="apple-touch-icon" href="images/touch/apple-touch-icon.png">

    <!-- Tile icon for Win8 (144x144 + tile color) -->
    <meta name="msapplication-TileImage" content="images/touch/ms-touch-icon-144x144-precomposed.png">
    <meta name="msapplication-TileColor" content="#2196F3">

    <!-- Color the status bar on mobile devices -->
    <meta name="theme-color" content="#2196F3">

    <!-- SEO: If your mobile URL is different from the desktop URL, add a canonical link to the desktop page https://developers.google.com/webmasters/smartphone-sites/feature-phones -->
    <!--
    <link rel="canonical" href="http://www.example.com/">
    -->

    <link rel="stylesheet" href="styles/material.blue-red.min.css"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" type="text/css" href="styles/dialog-polyfill.css">

    <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <script>
        (adsbygoogle = window.adsbygoogle || []).push({
            google_ad_client: "ca-pub-8653893281398077",
            enable_page_level_ads: true
        });
    </script>
</head>
<body>
<div class="mdl-layout__container">
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
        <header class="mdl-layout__header is-casting-shadow">
            <div class="mdl-layout__header-row">
                <span class="mdl-layout-title">Fancy Words 单词本</span>
                <div class="mdl-textfield mdl-js-textfield">
                    <input id="search" class="mdl-textfield__input" name="search" type="text"
                           onkeypress="if (event.keyCode == 13) searchWords(this.value)">
                    <label class="mdl-textfield__label" for="search">搜索单词</label>
                </div>
                <button type="submit" onclick="searchWords(search.value)"
                        class="mdl-button mdl-js-button mdl-button--icon">
                    <i class="material-icons">search</i>
                </button>
            </div>
        </header>
        <main class="mdl-layout__content">
            <div style="margin: 16px;">
                <div class="page-content" align="center">
                    <table id="words-table" class="mdl-data-table mdl-js-data-table mdl-shadow--2dp" width="480px">
                        <thead>
                        <tr>
                            <th><b>单词</b></th>
                            <th><b>释义</b></th>
                            <th><b>熟练度</b></th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            List<BriefUserWordEntry> entries = server.requestAllEntries();
                            long currentTime = System.currentTimeMillis();
                            for (BriefUserWordEntry entry : entries) {
                        %>
                        <tr onclick="showWordDialog('<%=Utils.escapeEcmaScript(entry.getWord())%>')">
                            <td><%=entry.getWord()%>
                            </td>
                            <td><%=entry.getDefinition()%>
                            </td>
                            <td><%=Utils.toPercentage(entry.getMastery(currentTime))%>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>

            <button id="new-word-button" style="position: absolute; right: 16px; bottom: 16px;"
                    class="mdl-button mdl-js-button mdl-button--fab mdl-js-ripple-effect mdl-button--primary"
                    onclick="showNewWordDialog()">
                <i class="material-icons">add</i>
            </button>
            <button id="exam-button" style="position: absolute; left: 16px; bottom: 16px;"
                    class="mdl-button mdl-js-button mdl-button--fab mdl-js-ripple-effect mdl-button--accent"
                    onclick="selectExamTypeDialog.showModal()">
                <i class="material-icons">assignment</i>
            </button>
        </main class="mdl-layout__content">
        <footer class="mdl-mini-footer">
            <div class="mdl-mini-footer__left-section">
                Copyright © 2017 Yongshun Ye, Huiqi Xue, Chaoqun Yang. All rights reserved.
            </div>
        </footer>
    </div>
</div>
<dialog id="message-dialog" class="mdl-dialog">
    <h4 id="message-dialog-title" class="mdl-dialog__title"></h4>
    <div id="message-dialog-content" class="mdl-dialog__content">

    </div>
    <div class="mdl-dialog__actions mdl-dialog__actions--full-width">
        <button type="button" class="mdl-button close" onclick="messageDialog.close()">关闭</button>
    </div>
</dialog>

<dialog id="new-word-dialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">添加新单词</h4>
    <div id="new-word-dialog-content" class="mdl-dialog__content">
        <form id="new-word-form" action="new-word-entry-action.jsp" method="post">
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input id="new-word-textfield" name="word" class="mdl-textfield__input" type="text">
                <label class="mdl-textfield__label" for="word">单词</label>
            </div>
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <textarea id="new-word-definition-textarea" name="definition" class="mdl-textfield__input" type="text"
                          rows="3"></textarea>
                <label class="mdl-textfield__label" for="definition">释义</label>
            </div>
        </form>
    </div>
    <div class="mdl-dialog__actions">
        <button type="button" class="mdl-button mdl-button--primary" onclick="submitNewWord()">确认添加</button>
        <button type="button" class="mdl-button close" onclick="closeNewWordDialog()">关闭</button>
    </div>
</dialog>

<dialog id="edit-word-dialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">编辑单词</h4>
    <div id="edit-word-dialog-content" class="mdl-dialog__content">
        <h3 id="edit-word-word"></h3>
        <form id="edit-word-form" action="edit-word-entry-action.jsp" method="post">
            <input type="hidden" value="apple">
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <textarea id="edit-definition-textarea" name="definition" class="mdl-textfield__input" type="text"
                          rows="3"></textarea>
                <label class="mdl-textfield__label" for="definition">释义</label>
            </div>
        </form>
    </div>
    <div class="mdl-dialog__actions">
        <button type="button" class="mdl-button mdl-button--primary"
                onclick="submitEditWord(editWordWord.innerHTML, editDefinitionTextarea.value)">
            保存修改
        </button>
        <button type="button" class="mdl-button close" onclick="closeEditWordDialog()">关闭</button>
    </div>
</dialog>

<dialog id="select-exam-type-dialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title" style="font-size: 24px">请选择单词测验类型</h4>
    <div class="mdl-dialog__actions mdl-dialog__actions--full-width">
        <button type="button" class="mdl-button mdl-button--primary"
                onclick="closeSelectExamTypeDialogAndShowExamDialog(true)">根据单词选择释义
        </button>
        <button type="button" class="mdl-button mdl-button--primary"
                onclick="closeSelectExamTypeDialogAndShowExamDialog(false)">根据释义选择单词
        </button>
        <button type="button" class="mdl-button close" onclick="selectExamTypeDialog.close()">关闭</button>
    </div>
</dialog>

<dialog id="exam-dialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">单词测验</h4>
    <div id="exam-dialog-content" class="mdl-dialog__content">
        <h3 id="exam-title">n. 苹果</h3>
        <p>以上释义所对应的单词是：</p>
        <form>
            <input id="exam-word" type="hidden" name="word">
            <input id="exam-type" type="hidden" name="exam-type">
            <div>
                <label id="choice-0-label" class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="choice-0">
                    <input type="radio" id="choice-0" class="mdl-radio__button" name="choice" value="0">
                    <span id="choice-0-text" class="mdl-radio__label"></span>
                </label>
            </div>
            <div>
                <label id="choice-1-label" class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="choice-1">
                    <input type="radio" id="choice-1" class="mdl-radio__button" name="choice" value="1">
                    <span id="choice-1-text" class="mdl-radio__label"></span>
                </label></div>
            <div>
                <label id="choice-2-label" class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="choice-2">
                    <input type="radio" id="choice-2" class="mdl-radio__button" name="choice" value="2">
                    <span id="choice-2-text" class="mdl-radio__label"></span>
                </label></div>
            <div>
                <label id="choice-3-label" class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="choice-3">
                    <input type="radio" id="choice-3" class="mdl-radio__button" name="choice" value="3">
                    <span id="choice-3-text" class="mdl-radio__label"></span>
                </label>
            </div>
            <input id="correct-choice" type="hidden" name="correct-choice">
        </form>
    </div>
    <div class="mdl-dialog__actions">
        <button type="button" class="mdl-button mdl-button--primary" onclick="submitExam()">下一题</button>
        <button type="button" class="mdl-button close" onclick="closeExamDialog()">关闭</button>
    </div>
</dialog>

<dialog id="word-dialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">查看单词</h4>
    <div id="word-dialog-content" class="mdl-dialog__content">
        <h3 id="word"></h3>
        <h5 id="definition"></h5>
        <p>熟练度：<span id="mastery"></span></p>
        <p>创建时间：<span id="time-created"></span></p>
        <p>修改时间：<span id="time-edited"></span></p>
        <p>查看次数：<span id="count-viewed"></span></p>
        <p>测试次数：<span id="count-tested"></span></p>
    </div>
    <div class="mdl-dialog__actions">
        <button type="button" class="mdl-button mdl-button--primary"
                onclick="showEditWordDialog(wordText.innerHTML, definitionText.innerHTML)">编辑单词
        </button>
        <button type="button" class="mdl-button mdl-button--accent" onclick="deleteWord(wordText.innerHTML)">删除单词
        </button>
        <button type="button" class="mdl-button close" onclick="closeWordDialog()">关闭</button>
    </div>
</dialog>

<script src="scripts/dialog-polyfill.js"></script>
<script>
    /*function parseHTMLBody(html) {
     var element = document.createElement("html");
     element.innerHTML = html;
     return element.getElementsByTagName("body").item(0).innerHTML;
     }*/
    function postUrlencoded(request, data) {
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.send(data);
    }

    var dialogs = document.querySelectorAll('dialog');
    for (var i = 0; i < dialogs.length; i++)
        if (!dialogs[i].showModal)
            dialogPolyfill.registerDialog(dialogs[i]);

    var wordsTable = document.getElementById("words-table");
    function searchWords(keyword) {
        for (var i = 1, row; row = wordsTable.rows[i]; i++) {
            if (row.cells[0].innerHTML.includes(keyword) || row.cells[1].innerHTML.includes(keyword))
                row.style.display = null;
            else
                row.style.display = "none";
        }
    }
    function insertNewWord(word, definition) {

    }

    var messageDialog = document.getElementById('message-dialog');
    var messageDialogTitle = document.getElementById('message-dialog-title');
    var messageDialogContent = document.getElementById('message-dialog-content');
    function showMessageDialog(title, message) {
        messageDialogTitle.innerHTML = title;
        messageDialogContent.innerHTML = message;
        messageDialog.showModal();
    }
    function closeMessageDialog() {
        messageDialogTitle.innerHTML = null;
        messageDialogContent.innerHTML = null;
        messageDialog.showModal();
    }

    var newWordDialog = document.getElementById('new-word-dialog');
    var newWordForm = document.getElementById('new-word-form');
    var newWordTextfield = document.getElementById('new-word-textfield'),
        newWordDefinitionTextarea = document.getElementById('new-word-definition-textarea');
    function showNewWordDialog() {
        newWordDialog.showModal();
    }
    function closeNewWordDialog() {
        newWordForm.reset();
        newWordDialog.close();
    }
    function submitNewWord() {
        var word = newWordTextfield.value, definition = newWordDefinitionTextarea.value;

        var request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState == 4 && request.status == 200)
                if (JSON.parse(request.responseText).success == 1) {
                    closeNewWordDialog();
                    insertNewWord(word, definition);
                    showWordDialog(word);

                    location.reload();
                } else
                    showMessageDialog("添加新单词失败", "此单词已存在。");
        }

        request.open("POST", newWordForm.action, true);
        postUrlencoded(request, "word=" + encodeURIComponent(word) + "&definition=" + encodeURIComponent(definition));
    }
    var editWordDialog = document.getElementById("edit-word-dialog");
    var editWordWord = document.getElementById("edit-word-word"),
        editDefinitionTextarea = document.getElementById("edit-definition-textarea");
    var editWordForm = document.getElementById('edit-word-form');
    function showEditWordDialog(word, definition) {
        editWordWord.innerHTML = word;
        editDefinitionTextarea.value = definition;
        editWordDialog.showModal();
    }
    function closeEditWordDialog() {
        editWordWord.innerHTML = null;
        editDefinitionTextarea.value = null;
        editWordDialog.close();
    }
    function submitEditWord(word, definition) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState == 4 && request.status == 200)
                if (JSON.parse(request.responseText).success == 1) {
                    definitionText.innerHTML = definition;
                    closeEditWordDialog();
                    showMessageDialog("编辑成功", "对" + word + "所做的修改已保存。")

                    location.reload()
                }
        }
        request.open("POST", editWordForm.action, true);
        postUrlencoded(request, "word=" + encodeURIComponent(word) + "&definition=" + encodeURIComponent(definition));
    }

    var selectExamTypeDialog = document.getElementById('select-exam-type-dialog');
    function closeSelectExamTypeDialogAndShowExamDialog(type) {
        selectExamTypeDialog.close();
        showExamDialog(type);
    }

    var examDialog = document.getElementById('exam-dialog');
    var examDialogContent = document.getElementById('exam-dialog-content');
    var examTitle = document.getElementById("exam-title"),
        examWord = document.getElementById("exam-word"),
        examType = document.getElementById("exam-type"),
        choice0 = document.getElementById("choice-0"),
        choice1 = document.getElementById("choice-1"),
        choice2 = document.getElementById("choice-2"),
        choice3 = document.getElementById("choice-3"),
        choice0Label = document.getElementById("choice-0-label"),
        choice1Label = document.getElementById("choice-1-label"),
        choice2Label = document.getElementById("choice-2-label"),
        choice3Label = document.getElementById("choice-3-label"),
        choice0Text = document.getElementById("choice-0-text"),
        choice1Text = document.getElementById("choice-1-text"),
        choice2Text = document.getElementById("choice-2-text"),
        choice3Text = document.getElementById("choice-3-text"),
        correctChoice = document.getElementById("correct-choice");
    function loadExamDialog(type, successCallback, failCallback) {
        var request = new XMLHttpRequest();
        var url = type ? "word-exam.jsp" : "definition-exam.jsp";
        request.onreadystatechange = function () {
            if (request.readyState == 4 && request.status == 200) {
                var obj = JSON.parse(request.responseText);
                if (obj.success == 1) {
                    var exam = obj.exam;
                    examTitle.innerHTML = type ? exam.word : exam.definition;
                    examWord.value = exam.word;
                    examType.value = type;
                    choice0Text.innerHTML = exam.choices[0];
                    choice1Text.innerHTML = exam.choices[1];
                    choice2Text.innerHTML = exam.choices[2];
                    choice3Text.innerHTML = exam.choices[3];
                    correctChoice.value = exam.correctChoice;
                    successCallback();
                } else
                    failCallback();
            }
        }
        request.open("GET", url, true);
        request.send();
    }
    function showExamDialog(type) {
        examType.value = type;
        loadExamDialog(type, function () {
                examDialog.showModal();
            },
            function () {
                showMessageDialog("请求测验失败", "你的单词少于4个，暂时不能进行测验。")
            });
    }
    function clearExamDialog() {
        examTitle.innerHTML = null;
        examWord.value = null;
        choice0.checked = false;
        choice1.checked = false;
        choice2.checked = false;
        choice3.checked = false;
        choice0Label.classList.remove("is-checked");
        choice1Label.classList.remove("is-checked");
        choice2Label.classList.remove("is-checked");
        choice3Label.classList.remove("is-checked");
        choice0Text.innerHTML = null;
        choice1Text.innerHTML = null;
        choice2Text.innerHTML = null;
        choice3Text.innerHTML = null;
        correctChoice.value = null;
    }
    function closeExamDialog() {
        clearExamDialog();
        examType.value = null;
        examDialog.close();
    }
    function submitExam() {
        /*var choices = [choice0, choice1, choice2, choice3];*/
        var choice = -1;
        if (choice0.checked) choice = choice0.value;
        else if (choice1.checked) choice = choice1.value;
        else if (choice2.checked) choice = choice2.value;
        else if (choice3.checked) choice = choice3.value;
        if (choice == -1) {

            showMessageDialog("请选择选项", "请从4个答案选项中选择一个。");
            return;
        }

        var correct = choice == correctChoice.value;
        var correctWord;
        switch (correctChoice.value) {
            case 0:
                correctWord = choice0Text.innerHTML;
                break;
            case 1:
                correctWord = choice1Text.innerHTML;
                break;
            case 2:
                correctWord = choice2Text.innerHTML;
                break;
            case 3:
                correctWord = choice3Text.innerHTML;
                break;
        }

        var request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState == 4 && request.status == 200) {
                showMessageDialog(correct ? "答案正确" : "答案错误", correct ? "" : "正确答案为第" + (parseInt(correctChoice.value) + 1) + "项，单词" + correctWord + "。");

                clearExamDialog();
                loadExamDialog(examType.value, function () {
                }, function () {
                });
            }
        }
        request.open("POST", "submit-exam-action.jsp", true);
        postUrlencoded(request, "word=" + encodeURIComponent(examWord.value) + "&correct=" + correct);
    }

    var wordDialog = document.getElementById('word-dialog');
    var wordText = document.getElementById("word"),
        definitionText = document.getElementById("definition"),
        mastery = document.getElementById("mastery"),
        timeCreated = document.getElementById("time-created"),
        timeEdited = document.getElementById("time-edited"),
        countViewed = document.getElementById("count-viewed"),
        countTested = document.getElementById("count-tested");
    function showWordDialog(word) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState == 4 && request.status == 200) {
                var obj = JSON.parse(request.responseText);
                if (obj.success == 1) {
                    wordText.innerHTML = obj.wordEntry.word;
                    definitionText.innerHTML = obj.wordEntry.definition;
                    mastery.innerHTML = Math.round(obj.wordEntry.mastery * 100) + "%";
                    timeCreated.innerHTML = new Date(obj.wordEntry.timeCreated).toDateString();
                    timeEdited.innerHTML = new Date(obj.wordEntry.timeEdited).toDateString();
                    countViewed.innerHTML = obj.wordEntry.countViewed;
                    countTested.innerHTML = obj.wordEntry.countTestedCorrect + "/" + obj.wordEntry.countTested;
                } else
                    wordText.innerHTML = "此单词已不存在。";

                wordDialog.showModal();
            }
        }
        request.open("GET", "word-entry.jsp?word=" + word, true);
        request.send();
    }
    function deleteWord(word) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState == 4 && request.status == 200) {
                var obj = JSON.parse(request.responseText);
                if (obj.success == 1) {
                    closeWordDialog();
                    showMessageDialog("单词删除成功", "单词" + word + "已被删除。");

                    location.reload();
                }
                else
                    showMessageDialog("单词删除失败", "");
            }
        }
        request.open("POST", "delete-word-entry-action.jsp", true);
        postUrlencoded(request, "word=" + encodeURIComponent(word));
    }
    function closeWordDialog() {
        // Clear text
        wordDialog.close();
    }

</script>

<script src="scripts/material.min.js"></script>
<!-- build:js scripts/main.min.js -->
<!-- <script src="scripts/main.js"></script> -->
<!-- endbuild -->

</body>
</html>
