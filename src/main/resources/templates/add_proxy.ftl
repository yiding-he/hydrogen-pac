<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="css/default.css">
    <script src="js/jquery-3.2.1.min.js"></script>
    <title>添加代理</title>
</head>
<body>

<form action="add_proxy" method="post">
    <table border="1">
        <tr>
            <td>代理名称</td>
            <td><input type="text" name="proxyName"></td>
        </tr>
        <tr>
            <td>代理类型</td>
            <td><select name="proxyType">
                <option value="http/https">proxy</option>
                <option value="socks5">socks5</option>
            </select></td>
        </tr>
        <tr>
            <td>服务器地址（地址:端口）</td>
            <td><input type="text" name="proxyAddress"></td>
        </tr>
    </table>

    <p>
        <button type="submit">保存</button>
    </p>
</form>

</body>
</html>