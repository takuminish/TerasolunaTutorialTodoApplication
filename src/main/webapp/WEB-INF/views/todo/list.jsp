<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Todo List</title>
<style type="text/css">
.strike {
    text-decoration: line-through;
}

.alert {
    border: 1px solid;
}

.alert-error {
    background-color: #c60f13;
    border-color: #970b0e;
    color: white;
}

.alert-success {
    background-color: #5da423;
    border-color: #457a1a;
    color: white;
}

.text-error {
    color: #c60f13;
}

.inline {
    display: inline-block;
}
</style>
</head>
<body>
    <h1>Todo List</h1>
    <hr />
    <div id="todoForm">
        <t:messagesPanel />
        <form:form
            action="${pageContext.request.contextPath}/todo/create"
            method="POST" modelAttribute="todoForm">
            <form:input path="todoTitle" />
            <form:errors  path="todoTitle" cssClass="text-error"/>
            <br />
            <form:button>Create  Todo</form:button>
        </form:form>
    </div>   
    
    <div id="todoList">
        <ul>
            <c:forEach items="${todoList}" var="todo">
            <li>
                <c:choose>
                    <c:when test="${todo.finished}">
                        <span class="strike">
                            ${f:h(todo.todoTitle)}
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span>
                            ${f:h(todo.todoTitle)}
                        </span>
                        <form:form
                            action="${pageContext.request.contextPath}/todo/finish"
                            method="POST"
                            modelAttribute="todoForm"
                            cssClass="inline">
                            <form:hidden path="todoId"
                                value="${f:h(todo.todoId)}" />
                            <form:button>Finish</form:button> 
                        </form:form>
                    </c:otherwise> 
                </c:choose>
                <form:form
                    action="${pageContext.request.contextPath}/todo/delete"
                    method="POST"
                    modelAttribute="todoForm"
                    cssClass="inline">
                    <form:hidden path="todoId"
                        value="${f:h(todo.todoId)}" />
                    <form:button>Delete</form:button> 
                </form:form>
            </li>
            </c:forEach>
        </ul>
    </div>
</body>
</html>