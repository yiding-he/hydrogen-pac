<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>PAC Manager</title>
    <link rel="stylesheet" href="css/default.css">
    <script src="js/jquery-3.2.1.min.js"></script>
    <script>
    </script>
</head>
<body>

<#if !loginEntries?has_content>
    <p><a href="login_no_entry">登录</a></p>
</#if>

<#list loginEntries as entry>
<p><a href="${entry.loginUrl}">使用${entry.getType().getName()}账号登录</a></p>
</#list>

</body>
</html>