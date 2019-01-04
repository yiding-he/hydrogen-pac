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
    <script src="js/current-pac.js"></script>
    <script>
        var testUrl = function () {
            var url = $('#url').val();
            alert(FindProxyForURL(url));
        };
    </script>
</head>
<body>
<input type="text" name="url" id="url">
<button onclick="testUrl()">test</button>
</body>
</html>