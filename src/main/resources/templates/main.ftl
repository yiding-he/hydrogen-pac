<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>PAC 管理</title>
    <link rel="stylesheet" href="css/default.css">
    <script src="js/jquery-3.2.1.min.js"></script>
    <script>
        function handlePostResult(data) {
            if (!data.success) {
                alert(data.message);
            } else {
                window.location.reload();
            }
        }

        function deleteProxy(name) {
            if (!confirm("Delete all proxy named '" + name + "'?")) {
                return;
            }

            $.post("delete_proxy", {name:name}, handlePostResult)
        }

        function addPatterns() {
            var name = prompt("Patterns' Name: ");
            $.post("patterns/add", {name: name}, handlePostResult);
        }
    </script>
</head>
<body>

<h2>代理配置</h2>

<p><a href="add_proxy">添加代理</a></p>

<table border="1">
    <thead>
    <tr>
        <td>代理名称</td>
        <td>代理类型</td>
        <td>服务器地址</td>
        <td>&nbsp;</td>
    </tr>
    </thead>
    <tbody>
    <#list proxies as proxy>
    <tr>
        <td>${proxy.proxyName}</td>
        <td>${proxy.proxyType}</td>
        <td>${proxy.proxyAddress}</td>
        <td><a href="javascript:deleteProxy('${proxy.proxyName}')">删除</a></td>
    </tr>
    </#list>
    </tbody>
</table>

<h2>Patterns</h2>

<p><a href="javascript:addPatterns()">Add Patterns</a></p>

<table border="1">
    <thead>
    <tr>
        <td>Name</td>
        <td>Priority</td>
        <td>Proxy</td>
        <td>Patterns</td>
    </tr>
    </thead>
    <tbody>
    <#list patterns as patternList>
        <tr>
            <td>${patternList.name}</td>
            <td>${patternList.prioriry}</td>
            <td>${patternList.proxyName}</td>
            <td>
                <#list patternList.patternList as pattern>
                    <a href="#">${pattern}</a>
                </#list>
            </td>
        </tr>
    </#list>
    </tbody>
</table>

</body>
</html>