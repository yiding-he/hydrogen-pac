<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Hahaha</title>
    <link rel="stylesheet" href="css/default.css">
    <script src="js/jquery-3.2.1.min.js"></script>
    <script>
        $(function () {

        });

        function handlePostResult(data) {
            if (!data.success) {
                alert(data.message);
            } else {
                window.location.reload();
            }
        }

        function addProxy() {
            var name = prompt("Proxy Name:");
            var value = prompt("Proxy Value:");
            $.post("proxy/add", {name: name, value: value}, handlePostResult);
        }

        function deleteProxy(name) {
            if (!confirm("Delete all proxy named '" + name + "'?")) {
                return;
            }

            $.post("proxy/delete", {name:name}, handlePostResult)
        }

        function addPatterns() {
            var name = prompt("Patterns' Name: ");
            $.post("patterns/add", {name: name}, handlePostResult);
        }
    </script>
</head>
<body>

<h2>Proxies</h2>

<p><a href="proxy/add">Add Proxy</a></p>

<table border="1">
    <thead>
    <tr>
        <td>Name</td>
        <td>Value</td>
        <td>&nbsp;</td>
    </tr>
    </thead>
    <tbody>
    <#list proxies as proxy>
    <tr>
        <td>${proxy.name}</td>
        <td>${proxy.value}</td>
        <td><a href="javascript:deleteProxy('${proxy.name}')">Delete</a></td>
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