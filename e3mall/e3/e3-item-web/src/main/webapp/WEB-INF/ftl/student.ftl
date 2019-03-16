<html>
    <head>
        <title>student</title>
    </head>
    <body>
        学生信息：<br>
        学号：${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
        姓名：${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
        年龄：${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
        家庭住址：${student.address}<br>
        学生列表：
        <table border="1">
            <tr>
                <th>序号</th>
                <th>学号</th>
                <th>姓名</th>
                <th>年龄</th>
                <th>家庭住址</th>
            </tr>
            <#list studentList as stu>
            <#if stu_index %2 ==0>
            <tr bgcolor="red">
            <#else >
            <tr bgcolor="blue">
            </#if>
                <td>${stu_index+1}</td>
                <td>${stu.id}</td>
                <td>${stu.name}</td>
                <td>${stu.age}</td>
                <td>${stu.address}</td>
            </tr>
            </#list>
        </table>
        当前日期：${date?datetime}<br>
                  ${date?string("yyyy/mm/dd hh:mm:ss")}<br>
    null值得处理：${val!73845}<br>
    判断val值是否为空：<br>
    <#if val??>
        val中有内容
    <#else >
        val中的值为null
    </#if>
    引用模板测试：<br>
    <#include "hello.ftl">
    </body>
</html>