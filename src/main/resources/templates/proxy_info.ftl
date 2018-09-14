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
    </script>
</head>
<body>

<h2>${title!}</h2>

<form action="${action!}" method="post">
    <table>
        <tr>
            <td>Name</td>
            <td><input name="name"></td>
        </tr>
        <tr>
            <td>Type</td>
            <td>
                <label><input type="radio" checked name="type" value="HTTP"> HTTP</label>
                <label><input type="radio" name="type" value="SOCKS"> SOCKS</label>
            </td>
        </tr>
        <tr>
            <td>Host</td>
            <td><input name="host"></td>
        </tr>
        <tr>
            <td>Port</td>
            <td><input type="number" min="1" max="65535" name="port" value="1"></td>
        </tr>
        <tr>
            <td colspan="2">
                <button type="submit">Add</button>
                <a href="../main">back</a>
            </td>
        </tr>
    </table>
</form>

</body>
</html>